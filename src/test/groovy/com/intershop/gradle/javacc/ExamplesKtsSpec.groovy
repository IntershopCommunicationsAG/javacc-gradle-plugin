/*
 * Copyright 2015 Intershop Communications AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.intershop.gradle.javacc

import com.intershop.gradle.test.AbstractIntegrationKotlinSpec
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class ExamplesKtsSpec extends AbstractIntegrationKotlinSpec {

    private String TASK_JAVA_COMPILE_CONFIGURATION = """
            tasks {
                withType<JavaCompile> {
                    options.compilerArgs.add("-Xlint:deprecation")
                    options.compilerArgs.add("-Xlint:unchecked")
                }
            }
    """.stripIndent()

    def 'Test Simple Examples'() {
        given:
        copyResources('examples/SimpleExamples/jj', 'jj')

        buildFile << """
            plugins {
                `java`
                id("com.intershop.gradle.javacc")
            }

            version = "1.0.0.0"
            group = "com.test.gradle"

            java {
                sourceCompatibility = JavaVersion.VERSION_1_7
                targetCompatibility = JavaVersion.VERSION_1_7
            }

            sourceSets {
                register("simple1")
                register("simple2")
                register("simple3")
                register("idList")
                register("NL_Xlator")
            }

            javacc {
                configs {
                    register("simple1") {
                        sourceSetName = "simple1"
                        inputFile = file("jj/Simple1.jj")
                    }
                    register("simple2") {
                        sourceSetName = "simple2"
                        inputFile = file("jj/Simple2.jj")
                    }
                    register("simple3") {
                        sourceSetName = "simple3"
                        inputFile = file("jj/Simple3.jj")
                    }
                    register("idList") {
                        sourceSetName = "idList"
                        inputFile = file("jj/IdList.jj")
                    }
                    register("NL_Xlator") {
                        sourceSetName = "NL_Xlator"
                        inputFile = file("jj/NL_Xlator.jj")
                    }
                }
            }
            
            ${TASK_JAVA_COMPILE_CONFIGURATION}

            repositories {
                mavenCentral()
            }
        """.stripIndent()

        when:
        List<String> args = ['simple1Classes', 'simple2Classes', 'simple3Classes', 'idListClasses', 'nL_XlatorClasses', '-s']

        def result = getPreparedGradleRunner()
                .withArguments(args)
                .withGradleVersion(gradleVersion)
                .build()

        File parserFileSimple1 = new File(testProjectDir, 'build/generated/javacc/simple1/Simple1.java')
        File parserFileSimple2 = new File(testProjectDir, 'build/generated/javacc/simple2/Simple2.java')
        File parserFileSimple3 = new File(testProjectDir, 'build/generated/javacc/simple3/Simple3.java')
        File parserFileIdList = new File(testProjectDir, 'build/generated/javacc/idList/IdList.java')
        File parserFileNL_Xlator = new File(testProjectDir, 'build/generated/javacc/NL_Xlator/NL_Xlator.java')

        then:

        result.task(':javaccSimple1').outcome == SUCCESS
        result.task(':javaccSimple2').outcome == SUCCESS
        result.task(':javaccSimple3').outcome == SUCCESS
        result.task(':javaccIdList').outcome == SUCCESS
        result.task(':javaccNL_Xlator').outcome == SUCCESS

        result.task(':compileSimple1Java').outcome == SUCCESS
        result.task(':compileSimple2Java').outcome == SUCCESS
        result.task(':compileSimple3Java').outcome == SUCCESS
        result.task(':compileNL_XlatorJava').outcome == SUCCESS

        parserFileSimple1.exists()
        parserFileSimple2.exists()
        parserFileSimple3.exists()
        parserFileIdList.exists()
        parserFileNL_Xlator.exists()

        where:
        gradleVersion << supportedGradleVersions
    }

    def 'Test example MailProcessing'() {
        given:
        copyResources('examples/MailProcessing/jj', 'jj')

        buildFile << """
            plugins {
                `java`
                id("com.intershop.gradle.javacc")
            }

            version = "1.0.0.0"
            group = "com.test.gradle"

            java {
                sourceCompatibility = JavaVersion.VERSION_1_7
                targetCompatibility = JavaVersion.VERSION_1_7
            }

            sourceSets {
                register("digest")
                register("faq")
            }

            javacc {
                configs {
                    register("digest") {
                        sourceSetName = "digest"
                        inputFile = file("jj/Digest.jj")
                    }
                    register("faq") {
                        sourceSetName = "faq"
                        inputFile = file("jj/Faq.jj")
                    }
                }
            }
            
            ${TASK_JAVA_COMPILE_CONFIGURATION}

            repositories {
                mavenCentral()
            }
        """.stripIndent()

        when:
        List<String> args = ['digestClasses', 'faqClasses', '-s']

        def result = getPreparedGradleRunner()
                .withArguments(args)
                .withGradleVersion(gradleVersion)
                .build()

        File parserFileDigest = new File(testProjectDir, 'build/generated/javacc/digest/Digest.java')
        File parserFileFaq = new File(testProjectDir, 'build/generated/javacc/faq/Faq.java')

        then:
        result.output.contains(':javaccDigest')
        result.output.contains(':javaccFaq')

        result.output.contains(':compileDigestJava')
        result.output.contains(':compileFaqJava')

        parserFileDigest.exists()
        parserFileFaq.exists()

        where:
        gradleVersion << supportedGradleVersions
    }

    def 'Test JJTreeExamples'() {
        given:
        copyResources('examples/JJTreeExamples/jjt', 'jjt')
        copyResources('examples/JJTreeExamples/jjtSrc', 'jjtSrc')

        buildFile << """
            plugins {
                `java`
                id("com.intershop.gradle.javacc")
            }

            version = "1.0.0.0"
            group = "com.test.gradle"

            java {
                sourceCompatibility = JavaVersion.VERSION_1_7
                targetCompatibility = JavaVersion.VERSION_1_7
            }

            sourceSets {
                register("eg1")
                register("eg2")
                register("eg3")
                register("eg4")
            }

            val javaCCOutDirEg2 = File(project.buildDir, "generated/javacc/eg2")
            val javaCCOutDirEg3 = File(project.buildDir, "generated/javacc/eg3")
            val javaCCOutDirEg4 = File(project.buildDir, "generated/javacc/eg4")

            javacc {
                configs {
                    register("eg1") {
                        sourceSetName = "eg1"
                        inputFile = file("jjt/eg1.jjt")
                        jjtree {}
                    }
                    register("eg2") {
                        sourceSetName = "eg2"
                        outputDir = javaCCOutDirEg2
                        inputFile = file("jjt/eg2.jjt")
                        jjtree {}
                    }
                    register("eg3") {
                        sourceSetName = "eg3"
                        outputDir = javaCCOutDirEg3
                        inputFile = file("jjt/eg3.jjt")
                        jjtree {}
                    }
                    register("eg4") {
                        sourceSetName = "eg4"
                        outputDir = javaCCOutDirEg4
                        inputFile = file("jjt/eg4.jjt")
                        jjtree {}
                    }
                }
            }

            tasks {
                val copySrcEg2 = register("copySrcEg2", Copy::class) {
                    from("jjtSrc")
                    into(javaCCOutDirEg2)
                    include("ASTMyID.java")
                }
                val copySrcEg3 = register("copySrcEg3", Copy::class) {
                    from("jjtSrc")
                    into(javaCCOutDirEg3)
                    include("ASTMyID.java")
                }
                val copySrcEg4 = register("copySrcEg4", Copy::class) {
                    from("jjtSrc")
                    into(javaCCOutDirEg4)
                }

                getByName("javaccEg2").dependsOn(copySrcEg2)
                getByName("javaccEg3").dependsOn(copySrcEg3)
                getByName("javaccEg4").dependsOn(copySrcEg4)
            }
            
            ${TASK_JAVA_COMPILE_CONFIGURATION}

            repositories {
                mavenCentral()
            }
        """.stripIndent()

        when:
        List<String> args = ['eg1Classes', 'eg2Classes', 'eg3Classes', 'eg4Classes','-s']

        def result = getPreparedGradleRunner()
                .withArguments(args)
                .withGradleVersion(gradleVersion)
                .build()

        File parserFileEg1 = new File(testProjectDir, 'build/generated/javacc/eg1/Eg1.java')
        File parserFileEg2 = new File(testProjectDir, 'build/generated/javacc/eg2/Eg2.java')
        File parserFileEg3 = new File(testProjectDir, 'build/generated/javacc/eg3/Eg3.java')
        File parserFileEg4 = new File(testProjectDir, 'build/generated/javacc/eg4/Eg4.java')

        then:
        result.output.contains(':javaccEg1')
        result.output.contains(':compileEg1Java')
        result.output.contains(':javaccEg2')
        result.output.contains(':compileEg2Java')
        result.output.contains(':javaccEg3')
        result.output.contains(':compileEg3Java')
        result.output.contains(':javaccEg4')
        result.output.contains(':compileEg4Java')

        parserFileEg1.exists()
        parserFileEg2.exists()
        parserFileEg3.exists()
        parserFileEg4.exists()

        where:
        gradleVersion << supportedGradleVersions
    }

    def 'Test example CORBA-IDL'() {
        given:
        copyResources('examples/CORBA-IDL', 'jj')

        buildFile << """
            plugins {
                `java`
                id("com.intershop.gradle.javacc")
            }

            version = "1.0.0.0"
            group = "com.test.gradle"

            java {
                sourceCompatibility = JavaVersion.VERSION_1_7
                targetCompatibility = JavaVersion.VERSION_1_7
            }

            javacc {
                configs {
                    register("idl") {
                        inputFile = file("jj/IDL.jj")
                    }
                }
            }
            
            ${TASK_JAVA_COMPILE_CONFIGURATION}

            repositories {
                mavenCentral()
            }
        """.stripIndent()

        when:
        List<String> args = ['compileJava', '-s']

        def result = getPreparedGradleRunner()
                .withArguments(args)
                .withGradleVersion(gradleVersion)
                .build()

        File parserFile = new File(testProjectDir, 'build/generated/javacc/idl/IDLParser.java')

        then:
        result.output.contains(':javaccIdl')
        result.output.contains(':compileJava')
        parserFile.exists()

        where:
        gradleVersion << supportedGradleVersions
    }

    def 'Test JavaGrammars'() {
        given:
        copyResources('examples/JavaGrammars/jj', 'jj')
        copyResources('examples/JavaGrammars/jjSrc', 'jjSrc')
        copyResources('examples/JavaGrammars/pre1.5', 'pre1.5')
        copyResources('examples/JavaGrammars/1.5', '1.5')

        buildFile << """
            plugins {
                `java`
                id("com.intershop.gradle.javacc")
            }

            version = "1.0.0.0"
            group = "com.test.gradle"

            java {
                sourceCompatibility = JavaVersion.VERSION_1_7
                targetCompatibility = JavaVersion.VERSION_1_7
            }

            sourceSets {
                register("javaGrammar102")
                register("javaGrammar102LS")
                register("javaGrammar11")
                register("javaGrammar11noLA")
                register("javaGrammar15")
            }

            val javaCCOutDir15 = File(project.buildDir, "generated/javacc/javaGrammar15")

            javacc {
                configs {
                    register("javaGrammar102") {
                        sourceSetName = "javaGrammar102"
                        inputFile = file("jj/Java1.0.2.jj")
                    }
                    register("javaGrammar102LS") {
                        sourceSetName = "javaGrammar102LS"
                        inputFile = file("jj/Java1.0.2LS.jj")
                    }
                    register("javaGrammar11") {
                        sourceSetName = "javaGrammar11"
                        inputFile = file("jj/Java1.1.jj")
                    }
                    register("javaGrammar11noLA") {
                        sourceSetName = "javaGrammar11noLA"
                        inputFile = file("jj/Java1.1noLA.jj")
                    }
                    register("javaGrammar15") {
                        sourceSetName = "javaGrammar15"
                        outputDir = javaCCOutDir15
                        inputFile = file("jj/Java1.5.jj")
                    }
                }
            }

            tasks {
                val copySrc15 = register("copySrc15", Copy::class) {
                    from("jjSrc")
                    into(javaCCOutDir15)
                }
            
                getByName("javaccJavaGrammar15").dependsOn(copySrc15)
            }
            
            ${TASK_JAVA_COMPILE_CONFIGURATION}

            repositories {
                mavenCentral()
            }
        """.stripIndent()

        when:
        List<String> args = ['javaGrammar102Classes', 'javaGrammar102LSClasses', 'javaGrammar11Classes', 'javaGrammar11noLAClasses', 'javaGrammar15Classes', '-s']

        def result = getPreparedGradleRunner()
                .withArguments(args)
                .withGradleVersion(gradleVersion)
                .build()

        File parserFileJavaGrammar102 = new File(testProjectDir, 'build/generated/javacc/javaGrammar102/JavaParser.java')
        File parserFileJavaGrammar102LS = new File(testProjectDir, 'build/generated/javacc/javaGrammar102LS/JavaParser.java')
        File parserFileJavaGrammar11 = new File(testProjectDir, 'build/generated/javacc/javaGrammar11/JavaParser.java')
        File parserFileJavaGrammar11noLA = new File(testProjectDir, 'build/generated/javacc/javaGrammar11noLA/JavaParser.java')
        File parserFileJavaGrammar15 = new File(testProjectDir, 'build/generated/javacc/javaGrammar15/JavaParser.java')

        then:
        result.output.contains(':javaccJavaGrammar102')
        result.output.contains(':compileJavaGrammar102Java')
        result.output.contains(':javaccJavaGrammar102LS')
        result.output.contains(':compileJavaGrammar102LSJava')
        result.output.contains(':javaccJavaGrammar11')
        result.output.contains(':compileJavaGrammar11Java')
        result.output.contains(':javaccJavaGrammar11noLA')
        result.output.contains(':compileJavaGrammar11noLAJava')

        parserFileJavaGrammar102.exists()
        parserFileJavaGrammar102LS.exists()
        parserFileJavaGrammar11.exists()
        parserFileJavaGrammar11noLA.exists()
        parserFileJavaGrammar15.exists()

        where:
        gradleVersion << supportedGradleVersions
    }

    def 'Test JavaCCGrammar'() {
        given:
        copyResources('examples/JavaCCGrammar', 'jj')

        buildFile << """
            plugins {
                `java`
                id("com.intershop.gradle.javacc")
            }

            version = "1.0.0.0"
            group = "com.test.gradle"

            java {
                sourceCompatibility = JavaVersion.VERSION_1_7
                targetCompatibility = JavaVersion.VERSION_1_7
            }

            javacc {
                configs {
                    register("javaCC") {
                        inputFile = file("jj/JavaCC.jj")
                    }
                }
            }
            
            ${TASK_JAVA_COMPILE_CONFIGURATION}

            repositories {
                mavenCentral()
            }
        """.stripIndent()

        when:
        List<String> args = ['compileJava', '-s']

        def result = getPreparedGradleRunner()
                .withArguments(args)
                .withGradleVersion(gradleVersion)
                .build()

        File parserFile = new File(testProjectDir, 'build/generated/javacc/javaCC/JavaCCParser.java')

        then:
        result.output.contains(':javaccJavaCC')
        result.output.contains(':compileJava')
        parserFile.exists()

        where:
        gradleVersion << supportedGradleVersions
    }

    def 'Test example Transformer'() {
        given:
        copyResources('examples/Transformer/jjt', 'jjt')
        copyResources('examples/Transformer/jjSrc', 'jjSrc')
        copyResources('examples/Transformer/example', 'example')

        buildFile << """
            plugins {
                `java`
                id("com.intershop.gradle.javacc")
            }

            version = "1.0.0.0"
            group = "com.test.gradle"

            java {
                sourceCompatibility = JavaVersion.VERSION_1_7
                targetCompatibility = JavaVersion.VERSION_1_7
            }

            val javaCCOutDir = File(project.buildDir, "generated/javacc/toy")

            javacc {
                configs {
                    register("toy") {
                        outputDir = javaCCOutDir
                        inputFile = file("jjt/ToyJava.jjt")
                        jjtree{}
                    }
                }
            }

            tasks {
                val copySrc = register("copySrc", Copy::class) {
                    from("jjSrc")
                    into(javaCCOutDir)
                }

                getByName("javaccToy").dependsOn(copySrc)

                register("testExec", JavaExec::class) {
                    mainClass = "ToyParser"
                    classpath = sourceSets["main"].runtimeClasspath
                    args((File(projectDir, "example/divide.toy")).absolutePath, (File(projectDir, "example/divide.java")).absolutePath)
                }
            }
            
            ${TASK_JAVA_COMPILE_CONFIGURATION}
            
            repositories {
                mavenCentral()
            }
        """.stripIndent()

        when:
        List<String> args = ['compileJava', '-s']

        def result = getPreparedGradleRunner()
                .withArguments(args)
                .withGradleVersion(gradleVersion)
                .build()

        File parserFile = new File(testProjectDir, 'build/generated/javacc/toy/ToyParser.java')

        then:
        result.output.contains(':javaccToy')
        result.output.contains(':compileJava')
        parserFile.exists()

        when:
        List<String> testArgs = ['testExec', '-s']

        getPreparedGradleRunner()
                .withArguments(testArgs)
                .withGradleVersion(gradleVersion)
                .build()

        File testFile = new File(testProjectDir, 'example/divide.toy')

        then:
        testFile.exists()

        where:
        gradleVersion << supportedGradleVersions
    }

    def 'Test example GUIParsing ParserVersion'() {
        given:
        copyResources('examples/GUIParsing/ParserVersion/jj', 'jj')
        copyResources('examples/GUIParsing/ParserVersion/jjSrc', 'jjSrc')

        buildFile << """
            plugins {
                `java`
                id("com.intershop.gradle.javacc")
            }

            version = "1.0.0.0"
            group = "com.test.gradle"

            java {
                sourceCompatibility = JavaVersion.VERSION_1_7
                targetCompatibility = JavaVersion.VERSION_1_7
            }

            val javaCCOutDir = File(project.buildDir, "generated/javacc/calcInput")

            javacc {
                configs {
                    register("calcInput") {
                        outputDir = javaCCOutDir
                        inputFile = file("jj/CalcInput.jj")
                    }
                }
            }

            tasks {
                val copySrc = register("copySrc", Copy::class) {
                    from("jjSrc")
                    into(javaCCOutDir)
                }

                getByName("javaccCalcInput").dependsOn(copySrc)
            }
            
            ${TASK_JAVA_COMPILE_CONFIGURATION}
            
            repositories {
                mavenCentral()
            }
        """.stripIndent()

        when:
        List<String> args = ['compileJava', '-s']

        def result = getPreparedGradleRunner()
                .withArguments(args)
                .withGradleVersion(gradleVersion)
                .build()

        File parserFile = new File(testProjectDir, 'build/generated/javacc/calcInput/CalcInputParser.java')

        then:
        result.output.contains(':javaccCalcInput')
        result.output.contains(':compileJava')
        parserFile.exists()

        where:
        gradleVersion << supportedGradleVersions
    }

    def 'Test example GUIParsing TokenMgrVersion'() {
        given:
        copyResources('examples/GUIParsing/TokenMgrVersion/jj', 'jj')
        copyResources('examples/GUIParsing/TokenMgrVersion/jjSrc', 'jjSrc')

        buildFile << """
            plugins {
                `java`
                id("com.intershop.gradle.javacc")
            }

            version = "1.0.0.0"
            group = "com.test.gradle"

            java {
                sourceCompatibility = JavaVersion.VERSION_1_7
                targetCompatibility = JavaVersion.VERSION_1_7
            }

            val javaCCOutDir = File(project.buildDir, "generated/javacc/calcInput")

            javacc {
                configs {
                    register("calcInput") {
                        outputDir = javaCCOutDir
                        inputFile = file("jj/CalcInput.jj")
                    }
                }
            }

            tasks {
                val copySrc = register("copySrc", Copy::class) {
                    from("jjSrc")
                    into(javaCCOutDir)
                }

                getByName("javaccCalcInput").dependsOn(copySrc)
            }
            
            ${TASK_JAVA_COMPILE_CONFIGURATION}
            
            repositories {
                mavenCentral()
            }
        """.stripIndent()

        when:
        List<String> args = ['compileJava', '-s']

        def result = getPreparedGradleRunner()
                .withArguments(args)
                .withGradleVersion(gradleVersion)
                .build()

        File parserFile = new File(testProjectDir, 'build/generated/javacc/calcInput/CalcInputParserTokenManager.java')

        then:
        result.output.contains(':javaccCalcInput')
        result.output.contains(':compileJava')
        parserFile.exists()

        where:
        gradleVersion << supportedGradleVersions
    }

    def 'Test example Obfuscator'() {
        copyResources('examples/Obfuscator/jj', 'jj')
        copyResources('examples/Obfuscator/jjSrc', 'jjSrc')
        copyResources('examples/Obfuscator/config', 'config')
        copyResources('examples/Obfuscator/input', 'input')

        buildFile << """
            plugins {
                `java`
                id("com.intershop.gradle.javacc")
            }

            version = "1.0.0.0"
            group = "com.test.gradle"

            java {
                sourceCompatibility = JavaVersion.VERSION_1_7
                targetCompatibility = JavaVersion.VERSION_1_7
            }

            val javaCCOutDir = File(project.buildDir, "generated/javacc/obfuscator")

            javacc {
                configs {
                    register("obfuscatorIds") {
                        outputDir = javaCCOutDir
                        inputFile = file("jj/IdsFile.jj")
                    }
                    register("obfuscatorJava") {
                        outputDir = javaCCOutDir
                        inputFile = file("jj/Java1.1.jj")
                    }
                    register("obfuscatorMap") {
                        outputDir = javaCCOutDir
                        inputFile = file("jj/MapFile.jj")
                    }
                }
            }

            tasks {
                val copySrc = register("copySrc", Copy::class) {
                    from("jjSrc")
                    into(javaCCOutDir)
                }

                getByName("javaccObfuscatorIds").dependsOn(copySrc)
                getByName("javaccObfuscatorJava").dependsOn(copySrc)
                getByName("javaccObfuscatorMap").dependsOn(copySrc)

                register("testExec", JavaExec::class) {
                    mainClass = "Main"
                    classpath = sourceSets["main"].runtimeClasspath
                    workingDir = projectDir
                    args("input", "output", "config/maps", "config/nochangeids", "config/useids")
                }
            }
            
            ${TASK_JAVA_COMPILE_CONFIGURATION}

            repositories {
                mavenCentral()
            }
        """.stripIndent()

        when:
        List<String> args = ['compileJava', '-s']

        def result = getPreparedGradleRunner()
                .withArguments(args)
                .withGradleVersion(gradleVersion)
                .build()

        File mapParserFile = new File(testProjectDir, 'build/generated/javacc/obfuscator/MapFile.java')
        File idsParserFile = new File(testProjectDir, 'build/generated/javacc/obfuscator/IdsFile.java')
        File javaParserFile = new File(testProjectDir, 'build/generated/javacc/obfuscator/JavaParser.java')

        then:
        result.output.contains(':javaccObfuscatorIds')
        result.output.contains(':javaccObfuscatorJava')
        result.output.contains(':javaccObfuscatorMap')
        result.output.contains(':compileJava')

        mapParserFile.exists()
        idsParserFile.exists()
        javaParserFile.exists()

        when:
        List<String> testArgs = ['testExec', '-s']

        getPreparedGradleRunner()
                .withArguments(testArgs)
                .withGradleVersion(gradleVersion)
                .build()

        File testFile = new File(testProjectDir, 'config')

        then:
        testFile.exists()

        where:
        gradleVersion << supportedGradleVersions
    }

    def 'Test example Interpreter'() {
        given:
        copyResources('examples/Interpreter/jjt', 'jjt')
        copyResources('examples/Interpreter/jjtSrc', 'jjtSrc')
        copyResources('examples/Interpreter/example', 'example')

        buildFile << """
            plugins {
                `java`
                id("com.intershop.gradle.javacc")
            }

            version = "1.0.0.0"
            group = "com.test.gradle"

            java {
                sourceCompatibility = JavaVersion.VERSION_1_7
                targetCompatibility = JavaVersion.VERSION_1_7
            }

            val javaCCOutDir = File(project.buildDir, "generated/javacc/interpreter")

            javacc {
                configs {
                    register("interpreter") {
                        outputDir = javaCCOutDir
                        inputFile = file("jjt/SPL.jjt")
                        jjtree { }
                    }
                }
            }

            tasks {
                val copySrc = register("copySrc", Copy::class) {
                    from("jjtSrc")
                    into(javaCCOutDir)
                }

                getByName("javaccInterpreter").dependsOn(copySrc)
            }
            
            ${TASK_JAVA_COMPILE_CONFIGURATION}
            
            repositories {
                mavenCentral()
            }
        """.stripIndent()

        when:
        List<String> args = ['compileJava', '-s']

        def result = getPreparedGradleRunner()
                .withArguments(args)
                .withGradleVersion(gradleVersion)
                .build()

        File parserFile = new File(testProjectDir, 'build/generated/javacc/interpreter/SPLParser.java')

        then:
        result.output.contains(':javaccInterpreter')
        result.output.contains(':compileJava')
        parserFile.exists()

        where:
        gradleVersion << supportedGradleVersions
    }

    def 'Test example Lookahead'(){
        given:
        copyResources('examples/Lookahead', 'jj')

        buildFile << """
            plugins {
                `java`
                id("com.intershop.gradle.javacc")
            }

            version = "1.0.0.0"
            group = "com.test.gradle"

            java {
                sourceCompatibility = JavaVersion.VERSION_1_7
                targetCompatibility = JavaVersion.VERSION_1_7
            }

            sourceSets {
                register("example1")
                register("example2")
                register("example3")
                register("example4")
                register("example5")
                register("example6")
                register("example7")
                register("example8")
                register("example9")
                register("example10")
            }

            javacc {
                configs {
                    register("example1") {
                        sourceSetName = "example1"
                        inputFile = file("jj/Example1.jj")
                        lookahead = 2
                    }
                    register("example2") {
                        sourceSetName = "example2"
                        inputFile = file("jj/Example2.jj")
                        lookahead = 2
                    }
                    register("example3") {
                        sourceSetName = "example3"
                        inputFile = file("jj/Example3.jj")
                        lookahead = 2
                    }
                    register("example4") {
                        sourceSetName = "example4"
                        inputFile = file("jj/Example4.jj")
                    }
                    register("example5") {
                        sourceSetName = "example5"
                        inputFile = file("jj/Example5.jj")
                    }
                    register("example6") {
                        sourceSetName = "example6"
                        inputFile = file("jj/Example6.jj")
                    }
                    register("example7") {
                        sourceSetName = "example7"
                        inputFile = file("jj/Example7.jj")
                    }
                    register("example8") {
                        sourceSetName = "example8"
                        inputFile = file("jj/Example8.jj")
                    }
                    register("example9") {
                        sourceSetName = "example9"
                        inputFile = file("jj/Example9.jj")
                    }
                    register("example10") {
                        sourceSetName = "example10"
                        inputFile = file("jj/Example10.jj")
                    }
                }
            }
            
            ${TASK_JAVA_COMPILE_CONFIGURATION}

            repositories {
                mavenCentral()
            }
        """.stripIndent()

        when:
        List<String> args = ['example1Classes', 'example2Classes', 'example4Classes', 'example5Classes', 'example6Classes', 'example7Classes', 'example8Classes', 'example9Classes', 'example10Classes', '-s']

        def result = getPreparedGradleRunner()
                .withArguments(args)
                .withGradleVersion(gradleVersion)
                .build()

        File parserFileExample1 = new File(testProjectDir, 'build/generated/javacc/example1/Example.java')
        File parserFileExample2 = new File(testProjectDir, 'build/generated/javacc/example2/Example.java')
        File parserFileExample4 = new File(testProjectDir, 'build/generated/javacc/example4/Example.java')
        File parserFileExample5 = new File(testProjectDir, 'build/generated/javacc/example5/Example.java')
        File parserFileExample6 = new File(testProjectDir, 'build/generated/javacc/example6/Example.java')
        File parserFileExample7 = new File(testProjectDir, 'build/generated/javacc/example7/Example.java')
        File parserFileExample8 = new File(testProjectDir, 'build/generated/javacc/example8/Example.java')
        File parserFileExample9 = new File(testProjectDir, 'build/generated/javacc/example9/Example.java')
        File parserFileExample10 = new File(testProjectDir, 'build/generated/javacc/example10/Example.java')

        then:
        result.output.contains(':javaccExample1')
        result.output.contains(':compileExample1Java')
        parserFileExample1.exists()

        result.output.contains(':javaccExample2')
        result.output.contains(':compileExample2Java')
        parserFileExample2.exists()

        result.output.contains(':javaccExample4')
        result.output.contains(':compileExample4Java')
        parserFileExample4.exists()

        result.output.contains(':javaccExample5')
        result.output.contains(':compileExample5Java')
        parserFileExample5.exists()

        result.output.contains(':javaccExample6')
        result.output.contains(':compileExample6Java')
        parserFileExample6.exists()

        result.output.contains(':javaccExample7')
        result.output.contains(':compileExample7Java')
        parserFileExample7.exists()

        result.output.contains(':javaccExample8')
        result.output.contains(':compileExample8Java')
        parserFileExample8.exists()

        result.output.contains(':javaccExample9')
        result.output.contains(':compileExample9Java')
        parserFileExample9.exists()

        result.output.contains(':javaccExample10')
        result.output.contains(':compileExample10Java')
        parserFileExample10.exists()

        where:
        gradleVersion << supportedGradleVersions
    }

    def 'Test example VTransformer'(){
        given:
        copyResources('examples/VTransformer/jjt', 'jjt')
        copyResources('examples/VTransformer/jjtSrc', 'jjtSrc')

        buildFile << """
            plugins {
                `java`
                id("com.intershop.gradle.javacc")
            }

            version = "1.0.0.0"
            group = "com.test.gradle"

            java {
                sourceCompatibility = JavaVersion.VERSION_1_7
                targetCompatibility = JavaVersion.VERSION_1_7
            }

            val javaCCOutDir = File(project.buildDir, "generated/javacc/vtransformer")

            javacc {
                configs {
                    register("vtransformer") {
                        outputDir = javaCCOutDir
                        inputFile = file("jjt/Java1.1.jjt")
                        jjtree { }
                    }
                }
            }

            tasks {
                val copySrc = register("copySrc", Copy::class) {
                    from("jjtSrc")
                    into(javaCCOutDir)
                }

                getByName("javaccVtransformer").dependsOn(copySrc)
            }
            
            ${TASK_JAVA_COMPILE_CONFIGURATION}
            
            repositories {
                mavenCentral()
            }
        """.stripIndent()

        when:
        List<String> args = ['compileJava', '-s']

        def result = getPreparedGradleRunner()
                .withArguments(args)
                .withGradleVersion(gradleVersion)
                .build()

        File parserFile = new File(testProjectDir, 'build/generated/javacc/vtransformer/JavaParser.java')

        then:
        result.output.contains(':javaccVtransformer')
        result.output.contains(':compileJava')
        parserFile.exists()

        where:
        gradleVersion << supportedGradleVersions
    }
}
