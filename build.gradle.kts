import org.asciidoctor.gradle.jvm.AsciidoctorTask
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
 * limitations under the License.
 */
plugins {
    // project plugins
    `java-gradle-plugin`
    groovy
    kotlin("jvm") version "1.4.20"

    // test coverage
    jacoco

    // ide plugin
    idea

    // publish plugin
    `maven-publish`

    // artifact signing - necessary on Maven Central
    signing

    // intershop version plugin
    id("com.intershop.gradle.scmversion") version "6.2.0"

    // plugin for documentation
    id("org.asciidoctor.jvm.convert") version "3.3.0"

    // documentation
    id("org.jetbrains.dokka") version "0.10.1"

    // code analysis for kotlin
    id("io.gitlab.arturbosch.detekt") version "1.15.0"

    // plugin for publishing to Gradle Portal
    id("com.gradle.plugin-publish") version "0.13.0"
}

scm {
    version.initialVersion = "1.0.0"
}

group = "com.intershop.gradle.javacc"
description = "Gradle javacc plugin"
version = scm.version.version

val sonatypeUsername: String by project
val sonatypePassword: String? by project

repositories {
    mavenCentral()
    jcenter()
}

val pluginId = "com.intershop.gradle.javacc"

gradlePlugin {
    plugins {
        create("javaccPlugin") {
            id = pluginId
            implementationClass = "com.intershop.gradle.javacc.JavaCCPlugin"
            displayName = project.name
            description = project.description
        }
    }
}

pluginBundle {
    website = "https://github.com/IntershopCommunicationsAG/${project.name}"
    vcsUrl = "https://github.com/IntershopCommunicationsAG/${project.name}"
    tags = listOf("intershop", "gradle", "plugin", "build", "javacc")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

// set correct project status
if (project.version.toString().endsWith("-SNAPSHOT")) {
    status = "snapshot'"
}

detekt {
    input = files("src/main/kotlin")
    config = files("detekt.yml")
}

tasks {
    withType<Test>().configureEach {
        systemProperty("intershop.gradle.versions", "6.8")

        dependsOn("jar")
    }

    val copyAsciiDoc = register<Copy>("copyAsciiDoc") {
        includeEmptyDirs = false

        val outputDir = file("$buildDir/tmp/asciidoctorSrc")
        val inputFiles = fileTree(rootDir) {
            include("**/*.asciidoc")
            exclude("build/**")
        }

        inputs.files.plus( inputFiles )
        outputs.dir( outputDir )

        doFirst {
            outputDir.mkdir()
        }

        from(inputFiles)
        into(outputDir)
    }

    withType<AsciidoctorTask> {
        dependsOn("copyAsciiDoc")

        setSourceDir(file("$buildDir/tmp/asciidoctorSrc"))
        sources(delegateClosureOf<PatternSet> {
            include("README.asciidoc")
        })

        outputOptions {
            setBackends(listOf("html5", "docbook"))
        }

        options = mapOf( "doctype" to "article",
                "ruby"    to "erubis")
        attributes = mapOf(
                "latestRevision"        to  project.version,
                "toc"                   to "left",
                "toclevels"             to "2",
                "source-highlighter"    to "coderay",
                "icons"                 to "font",
                "setanchors"            to "true",
                "idprefix"              to "asciidoc",
                "idseparator"           to "-",
                "docinfo1"              to "true")
    }

    withType<JacocoReport> {
        reports {
            xml.isEnabled = true
            html.isEnabled = true

            html.destination = File(project.buildDir, "jacocoHtml")
        }

        val jacocoTestReport by tasks
        jacocoTestReport.dependsOn("test")
    }

    getByName("jar")?.dependsOn("asciidoctor")

    val compileKotlin by getting(KotlinCompile::class) {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    val dokka by existing(DokkaTask::class) {
        outputFormat = "javadoc"
        outputDirectory = "$buildDir/javadoc"

        // Java 8 is only version supported both by Oracle/OpenJDK and Dokka itself
        // https://github.com/Kotlin/dokka/issues/294
        enabled = JavaVersion.current().isJava8
    }

    register<Jar>("sourceJar") {
        description = "Creates a JAR that contains the source code."

        from(sourceSets.getByName("main").allSource)
        archiveClassifier.set("sources")
    }

    register<Jar>("javaDoc") {
        dependsOn(dokka)
        from(dokka)
        getArchiveClassifier().set("javadoc")
    }
}

publishing {
    publications {
        create("intershopMvn", MavenPublication::class.java) {

            from(components["java"])
            artifact(tasks.getByName("sourceJar"))
            artifact(tasks.getByName("javaDoc"))

            artifact(File(buildDir, "docs/asciidoc/html5/README.html")) {
                classifier = "reference"
            }

            artifact(File(buildDir, "docs/asciidoc/docbook/README.xml")) {
                classifier = "docbook"
            }

            pom {
                name.set(project.name)
                description.set(project.description)
                url.set("https://github.com/IntershopCommunicationsAG/${project.name}")
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
                    url.set("https://github.com/IntershopCommunicationsAG/${project.name}")
                }
            }
        }
    }
    repositories {
        maven {
            val releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2"
            val snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots"
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
            credentials {
                username = sonatypeUsername
                password = sonatypePassword
            }
        }
    }
}

signing {
    sign(publishing.publications["intershopMvn"])
}

dependencies {
    implementation(gradleKotlinDsl())
    implementation(gradleApi())
    implementation(localGroovy())

    implementation("net.java.dev.javacc:javacc:4.2")

    testImplementation("commons-io:commons-io:2.2")
    testImplementation("com.intershop.gradle.test:test-gradle-plugin:3.7.0")
    testImplementation(gradleTestKit())
}

