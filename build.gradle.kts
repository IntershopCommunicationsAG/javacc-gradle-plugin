import org.asciidoctor.gradle.jvm.AsciidoctorTask
import io.gitee.pkmer.enums.PublishingType

/*
 * Copyright 2022 Intershop Communications AG.
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
 * limitations under the License.
 */
plugins {

    // project plugins
    `java-gradle-plugin`
    groovy

    kotlin("jvm") version "1.9.25"

    // test coverage
    jacoco

    // ide plugin
    idea

    // publish plugin
    `maven-publish`

    // artifact signing - necessary on Maven Central
    signing

    `jvm-test-suite`

    // plugin for documentation
    id("org.asciidoctor.jvm.convert") version "4.0.3"

    // documentation
    id("org.jetbrains.dokka") version "1.9.20"

    // plugin for publishing to Gradle Portal
    id("com.gradle.plugin-publish") version "1.3.0"

    id("io.gitee.pkmer.pkmerboot-central-publisher") version "1.1.1"
}

group = "com.intershop.gradle.javacc"
description = "Gradle javacc plugin"
// apply gradle property 'projectVersion' to project.version, default to 'LOCAL'
val projectVersion : String? by project
version = projectVersion ?: "LOCAL"

val sonatypeUsername: String? by project
val sonatypePassword: String? by project

repositories {
    mavenLocal()
    mavenCentral()
}

val pluginUrl = "https://github.com/IntershopCommunicationsAG/${project.name}"
gradlePlugin {
    website = pluginUrl
    vcsUrl = pluginUrl
    plugins {
        create("javaccPlugin") {
            id = "com.intershop.gradle.javacc"
            implementationClass = "com.intershop.gradle.javacc.JavaCCPlugin"
            displayName = project.name
            description = project.description
            tags = listOf("intershop", "build", "code", "generator", "javacc")
        }
    }
}

java {
    withJavadocJar()
    withSourcesJar()
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

// set correct project status
if (project.version.toString().endsWith("-SNAPSHOT")) {
    status = "snapshot"
}

testing {
    suites.withType<JvmTestSuite> {
        useSpock()
        dependencies {
            implementation("commons-io:commons-io:2.17.0")
            implementation("com.intershop.gradle.test:test-gradle-plugin:5.1.0")
            implementation(gradleTestKit())
        }

        targets {
            all {
                testTask.configure {
                    systemProperty("intershop.gradle.versions", "8.5,8.10.2")
                    testLogging {
                        showStandardStreams = true
                    }
                }
            }
        }
    }
}

tasks {
    val copyAsciiDocTask = register<Copy>("copyAsciiDoc") {
        includeEmptyDirs = false

        val outputDir = project.layout.buildDirectory.dir("tmp/asciidoctorSrc")
        val inputFiles = fileTree(rootDir) {
            include("**/*.asciidoc")
            exclude("build/**")
        }

        inputs.files.plus( inputFiles )
        outputs.dir( outputDir )

        doFirst {
            outputDir.get().asFile.mkdir()
        }

        from(inputFiles)
        into(outputDir)
    }

    withType<AsciidoctorTask> {
        dependsOn(copyAsciiDocTask)
        sourceDirProperty.set(project.provider<Directory>{
            val dir = project.objects.directoryProperty()
            dir.set(copyAsciiDocTask.get().outputs.files.first())
            dir.get()
        })
        sources {
            include("README.asciidoc")
        }

        outputOptions {
            setBackends(listOf("html5", "docbook"))
        }

        setOptions(mapOf(
            "doctype"               to "article",
            "ruby"                  to "erubis"
        ))
        setAttributes(mapOf(
            "latestRevision"        to project.version,
            "toc"                   to "left",
            "toclevels"             to "2",
            "source-highlighter"    to "coderay",
            "icons"                 to "font",
            "setanchors"            to "true",
            "idprefix"              to "asciidoc",
            "idseparator"           to "-",
            "docinfo1"              to "true"
        ))
    }

    withType<JacocoReport> {
        reports {
            xml.required.set(true)
            html.required.set(true)

            html.outputLocation.set( project.layout.buildDirectory.dir("jacocoHtml") )
        }

        val jacocoTestReport by tasks
        jacocoTestReport.dependsOn(test)
    }

    jar.configure {
        dependsOn(asciidoctor)
    }

    dokkaJavadoc.configure {
        outputDirectory.set(project.layout.buildDirectory.dir("dokka"))
    }

    withType<Sign> {
        val sign = this
        withType<PublishToMavenLocal> {
            this.dependsOn(sign)
        }
        withType<PublishToMavenRepository> {
            this.dependsOn(sign)
        }
    }

    afterEvaluate {
        named<Jar>("javadocJar") {
            dependsOn(dokkaJavadoc)
            from(dokkaJavadoc)
        }
    }
}

val stagingRepoDir = project.layout.buildDirectory.dir("stagingRepo")

publishing {
    publications {
        create("intershopMvn", MavenPublication::class.java) {

            from(components["java"])

            artifact(project.layout.buildDirectory.file("docs/asciidoc/html5/README.html")) {
                classifier = "reference"
            }

            artifact(project.layout.buildDirectory.file("docs/asciidoc/docbook/README.xml")) {
                classifier = "docbook"
            }
        }
        withType<MavenPublication>().configureEach {
            pom {
                name.set(project.name)
                description.set(project.description)
                url.set(pluginUrl)
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
                    }
                }
                organization {
                    name.set("Intershop Communications AG")
                    url.set("http://intershop.com")
                }
                developers {
                    developer {
                        id.set("m-raab")
                        name.set("M. Raab")
                        email.set("mraab@intershop.de")
                    }
                }
                scm {
                    connection.set("git@github.com:IntershopCommunicationsAG/${project.name}.git")
                    developerConnection.set("git@github.com:IntershopCommunicationsAG/${project.name}.git")
                    url.set(pluginUrl)
                }
            }
        }
    }
    repositories {
        maven {
            name = "LOCAL"
            url = stagingRepoDir.get().asFile.toURI()
        }
    }
}

pkmerBoot {
    sonatypeMavenCentral{
        // the same with publishing.repositories.maven.url in the configuration.
        stagingRepository = stagingRepoDir

        /**
         * get username and password from
         * <a href="https://central.sonatype.com/account"> central sonatype account</a>
         */
        username = sonatypeUsername
        password = sonatypePassword

        // Optional the publishingType default value is PublishingType.AUTOMATIC
        publishingType = PublishingType.USER_MANAGED
    }
}

signing {
    sign(publishing.publications["intershopMvn"])
}

dependencies {
    implementation(gradleApi())
    implementation(gradleKotlinDsl())

    implementation("net.java.dev.javacc:javacc:4.2")
}
