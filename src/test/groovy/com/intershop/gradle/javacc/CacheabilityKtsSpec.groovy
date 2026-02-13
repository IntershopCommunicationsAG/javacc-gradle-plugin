package com.intershop.gradle.javacc

import com.intershop.gradle.test.AbstractIntegrationKotlinSpec

import java.nio.file.Files
import java.nio.file.StandardCopyOption

import static org.gradle.testkit.runner.TaskOutcome.FROM_CACHE
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

/**
 * Test to verify that JavaCC tasks are properly cacheable
 */
class CacheabilityKtsSpec extends AbstractIntegrationKotlinSpec {

    // Base configuration for all tests to ensure consistent environment
    String TASK_BASE_CONFIGURATION = """
            plugins {
                `java`
                id("com.intershop.gradle.javacc")
            }

            version = "1.0.0"
            group = "com.test.gradle"

            java {
                toolchain {
                    languageVersion = JavaLanguageVersion.of(17)
                }
            }
            
            tasks {
                withType<JavaCompile> {
                    options.compilerArgs.add("-Xlint:deprecation")
                    options.compilerArgs.add("-Xlint:unchecked")
                }
            }
            
            repositories {
                mavenCentral()
            }
    """.stripIndent()

    // Unique, temporary build cache directory for each test method
    File tmpBuildCacheDir

    def setup() {
        tmpBuildCacheDir = Files.createTempDirectory("gradle-build-cache-${CacheabilityKtsSpec.simpleName}-").toFile()

        // Configure settings.gradle.kts to use our unique build cache directory
        settingsFile.text = """
            buildCache {
                local {
                    directory = file("${tmpBuildCacheDir.absolutePath.replace('\\', '\\\\')}")
                }
            }
        """.stripIndent()
    }

    def cleanup() {
        // Clean up the temporary build cache directory after each test
        tmpBuildCacheDir?.deleteDir()
    }

    def 'JavaCC task should be cacheable'() {
        given:
        copyResources('examples/SimpleExamples/jj', 'jj')

        buildFile << """
            ${TASK_BASE_CONFIGURATION}

            javacc {
                configs {
                    register("simple1") {
                        inputFile = file("jj/Simple1.jj")
                        packageName = "com.test.simple1"
                    }
                }
            }
        """.stripIndent()

        when: 'First build with build cache'
        def result1 = getPreparedGradleRunner()
                .withArguments('javaccSimple1', '--build-cache', '-s', '--warning-mode', 'all')
                .withGradleVersion(gradleVersion)
                .build()

        then: 'Task executes successfully and populates cache'
        result1.task(':javaccSimple1').outcome == SUCCESS
        new File(testProjectDir, 'build/generated/javacc/simple1/com/test/simple1/Simple1.java').exists()

        when: 'Clean and rebuild with build cache'
        def result2 = getPreparedGradleRunner()
                .withArguments('clean', 'javaccSimple1', '--build-cache', '-s', '--warning-mode', 'all')
                .withGradleVersion(gradleVersion)
                .build()

        then: 'Task is loaded from cache'
        result2.task(':javaccSimple1').outcome == FROM_CACHE
        new File(testProjectDir, 'build/generated/javacc/simple1/com/test/simple1/Simple1.java').exists()

        where:
        gradleVersion << supportedGradleVersions
    }

    def 'JavaCC task should be cacheable with JJTree'() {
        given:
        copyResources('examples/JJTreeExamples/jjt', 'jjt')

        buildFile << """
            ${TASK_BASE_CONFIGURATION}

            javacc {
                configs {
                    register("eg1") {
                        inputFile = file("jjt/eg1.jjt")
                    }
                }
            }
        """.stripIndent()

        when: 'First build with build cache'
        def result1 = getPreparedGradleRunner()
                .withArguments('javaccEg1', '--build-cache', '-s', '--warning-mode', 'all')
                .withGradleVersion(gradleVersion)
                .build()

        then: 'Task executes successfully and populates cache'
        result1.task(':javaccEg1').outcome == SUCCESS

        when: 'Clean and rebuild with build cache'
        def result2 = getPreparedGradleRunner()
                .withArguments('clean', 'javaccEg1', '--build-cache', '-s', '--warning-mode', 'all')
                .withGradleVersion(gradleVersion)
                .build()

        then: 'Task is loaded from cache'
        result2.task(':javaccEg1').outcome == FROM_CACHE

        where:
        gradleVersion << supportedGradleVersions
    }

    def 'JavaCC task should produce cache miss when input changes'() {
        given:
        copyResources('examples/SimpleExamples/jj', 'jj')

        buildFile << """
            ${TASK_BASE_CONFIGURATION}

            javacc {
                configs {
                    register("simple2") {
                        inputFile = file("jj/Simple2.jj")
                    }
                }
            }
        """.stripIndent()

        when: 'First build with build cache'
        def result1 = getPreparedGradleRunner()
                .withArguments('javaccSimple2', '--build-cache', '-s', '--warning-mode', 'all')
                .withGradleVersion(gradleVersion)
                .build()

        then: 'Task executes successfully and populates cache'
        result1.task(':javaccSimple2').outcome == SUCCESS

        when: 'Modify input file'
        def inputFile = new File(testProjectDir, 'jj/Simple2.jj')
        inputFile.text = inputFile.text.replace('Simple2', 'Simple2Modified')

        and: 'Rebuild with build cache'
        def result2 = getPreparedGradleRunner()
                .withArguments('javaccSimple2', '--build-cache', '-s', '--warning-mode', 'all')
                .withGradleVersion(gradleVersion)
                .build()

        then: 'Task re-executes (not from cache) due to input change'
        result2.task(':javaccSimple2').outcome == SUCCESS

        when: 'Revert input file and rebuild'
        copyResources('examples/SimpleExamples/jj', 'jj')
        def result3 = getPreparedGradleRunner()
                .withArguments('clean', 'javaccSimple2', '--build-cache', '-s', '--warning-mode', 'all')
                .withGradleVersion(gradleVersion)
                .build()

        then: 'Task is loaded from cache (original input)'
        result3.task(':javaccSimple2').outcome == FROM_CACHE

        where:
        gradleVersion << supportedGradleVersions
    }

    def 'JavaCC task should produce cache miss when configuration changes'() {
        given:
        copyResources('examples/SimpleExamples/jj', 'jj')

        buildFile << """
            ${TASK_BASE_CONFIGURATION}

            javacc {
                configs {
                    register("simple3") {
                        inputFile = file("jj/Simple3.jj")
                        staticParam = "true"
                    }
                }
            }
        """.stripIndent()

        when: 'First build with build cache'
        def result1 = getPreparedGradleRunner()
                .withArguments('javaccSimple3', '--build-cache', '-s', '--warning-mode', 'all')
                .withGradleVersion(gradleVersion)
                .build()

        then: 'Task executes successfully and populates cache'
        result1.task(':javaccSimple3').outcome == SUCCESS

        when: 'Change configuration'
        buildFile.text = buildFile.text.replace('staticParam = "true"', 'staticParam = "false"')

        and: 'Rebuild with build cache'
        def result2 = getPreparedGradleRunner()
                .withArguments('javaccSimple3', '--build-cache', '-s', '--warning-mode', 'all')
                .withGradleVersion(gradleVersion)
                .build()

        then: 'Task re-executes (not from cache) due to configuration change'
        result2.task(':javaccSimple3').outcome == SUCCESS

        when: 'Revert configuration and rebuild'
        buildFile.text = buildFile.text.replace('staticParam = "false"', 'staticParam = "true"')
        def result3 = getPreparedGradleRunner()
                .withArguments('clean', 'javaccSimple3', '--build-cache', '-s', '--warning-mode', 'all')
                .withGradleVersion(gradleVersion)
                .build()

        then: 'Task is loaded from cache (original configuration)'
        result3.task(':javaccSimple3').outcome == FROM_CACHE

        where:
        gradleVersion << supportedGradleVersions
    }

    def 'JavaCC task should use cache across different directories with PathSensitivity.RELATIVE'() {
        given:
        copyResources('examples/SimpleExamples/jj', 'jj')

        buildFile << """
            ${TASK_BASE_CONFIGURATION}

            javacc {
                configs {
                    register("idList") {
                        inputFile = file("jj/IdList.jj")
                    }
                }
            }
        """.stripIndent()

        when: 'First build with build cache'
        def result1 = getPreparedGradleRunner()
                .withArguments('javaccIdList', '--build-cache', '-s', '--warning-mode', 'all')
                .withGradleVersion(gradleVersion)
                .build()

        then: 'Task executes successfully and populates cache'
        result1.task(':javaccIdList').outcome == SUCCESS

        when: 'Create a new project directory with same content'
        def testProjectDir2 = Files.createTempDirectory("gradle-test-project-${CacheabilityKtsSpec.simpleName}-").toFile()
        testProjectDir2.deleteOnExit()

        // Copy all files to new directory
        copyDirectory(testProjectDir, testProjectDir2)

        and: 'Build in new directory with same build cache'
        def result2 = getPreparedGradleRunner()
                .withProjectDir(testProjectDir2)
                .withArguments('clean', 'javaccIdList', '--build-cache', '-s', '--warning-mode', 'all')
                .withGradleVersion(gradleVersion)
                .build()

        then: 'Task is loaded from cache (path sensitivity is RELATIVE)'
        result2.task(':javaccIdList').outcome == FROM_CACHE

        cleanup:
        testProjectDir2?.deleteDir()

        where:
        gradleVersion << supportedGradleVersions
    }

    private static void copyDirectory(File source, File target) {
        def sourceRoot = source.toPath()
        def targetRoot = target.toPath()

        Files.walk(sourceRoot).forEach { currentPath ->
            // Calculate where this file/dir should go in the target
            def relativePath = sourceRoot.relativize(currentPath)
            def destinationPath = targetRoot.resolve(relativePath)

            if (Files.isDirectory(currentPath)) {
                Files.createDirectories(destinationPath)
            } else {
                Files.copy(currentPath, destinationPath, StandardCopyOption.REPLACE_EXISTING)
            }
        }
    }
}
