/*
 * Copyright 2018 Intershop Communications AG.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intershop.gradle.javacc

import com.intershop.gradle.javacc.extension.JavaCCExtension
import com.intershop.gradle.javacc.task.JavaCCTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.plugins.JavaPluginConvention

/**
 * Plugin Class
 */
class JavaCCPlugin : Plugin<Project> {

    companion object {
        const val TASKDESCRIPTION = "Generate Java code with JAVA Compiler Compiler (JavaCC)"
        const val TASKNAME = "javacc"
    }

    override fun apply(project: Project) {
        with(project) {
            logger.info("JavaCC plugin adds extension {} to {}", JavaCCExtension.JAVACC_EXTENSION_NAME, name)
            val extension = extensions.findByType(JavaCCExtension::class.java) ?: extensions.create(JavaCCExtension.JAVACC_EXTENSION_NAME, JavaCCExtension::class.java, this)

            addJavaCCConfiguration(this, extension)

            configureTask(this, extension)
        }
    }

    /**
     * Configure task for javaCC code generation
     *
     * @param project       project to configure
     * @param extension     extension of this plugin
     */
    private fun configureTask(project: Project, extension: JavaCCExtension) {
        with(project) {
            val javaCCMain = tasks.maybeCreate(TASKNAME).apply {
                description = TASKDESCRIPTION
                group = JavaCCExtension.JAVACC_GROUP_NAME
            }

            extension.configs.all { javaCC ->
                tasks.maybeCreate(javaCC.getJavaCCTaskName(), JavaCCTask::class.java).apply {
                    group = JavaCCExtension.JAVACC_GROUP_NAME

                    outputDirProperty.set(javaCC.outputDirProvider)
                    inputFileProperty.set(javaCC.inputFileProvider)

                    packageNameProperty.set(javaCC.packageNameProvider)
                    jdkVersionProperty.set(javaCC.jdkVersionProvider)

                    lookaheadProperty.set(javaCC.lookaheadProvider)
                    choiceAmbiguityCheckProperty.set(javaCC.choiceAmbiguityCheckProvider)
                    otherAmbiguityCheckProperty.set(javaCC.otherAmbiguityCheckProvider)
                    staticParamProperty.set(javaCC.staticParamProvider)
                    supportClassVisibilityPublicProperty.set(javaCC.supportClassVisibilityPublicProvider)
                    debugParserProperty.set(javaCC.debugParserProvider)
                    debugLookaheadProperty.set(javaCC.debugLookaheadProvider)
                    debugTokenManagerProperty.set(javaCC.debugTokenManagerProvider)
                    errorReportingProperty.set(javaCC.errorReportingProvider)
                    javaUnicodeEscapeProperty.set(javaCC.javaUnicodeEscapeProvider)
                    unicodeInputProperty.set(javaCC.unicodeInputProvider)
                    ignoreCaseProperty.set(javaCC.ignoreCaseProvider)
                    commonTokenActionProperty.set(javaCC.commonTokenActionProvider)
                    userTokenManagerProperty.set(javaCC.userTokenManagerProvider)
                    userCharStreamProperty.set(javaCC.userCharStreamProvider)
                    buildParserProperty.set(javaCC.buildParserProvider)
                    buildTokenManagerProperty.set(javaCC.buildTokenManagerProvider)
                    tokenManagerUsesParserProperty.set(javaCC.tokenManagerUsesParserProvider)
                    sanityCheckProperty.set(javaCC.sanityCheckProvider)
                    forceLaCheckProperty.set(javaCC.forceLaCheckProvider)
                    cacheTokensProperty.set(javaCC.cacheTokensProvider)
                    keepLineColumnProperty.set(javaCC.keepLineColumnProvider)
                    tokenExtendsProperty.set(javaCC.tokenExtendsProvider)
                    tokenFactoryProperty.set(javaCC.tokenFactoryProvider)

                    javaCCArgsProperty.set(javaCC.argumentsProvider)

                    jjTree = javaCC.jjtree

                    afterEvaluate {
                        plugins.withType(JavaBasePlugin::class.java, {
                            val javaPluginConvention = project.convention.getPlugin(JavaPluginConvention::class.java)
                            val sourceSet = javaPluginConvention.sourceSets.findByName(javaCC.sourceSetName)
                            if(sourceSet != null) {
                                sourceSet.java.srcDir(this@apply.outputs)
                            }
                        })
                    }

                    javaCCMain.dependsOn(this)
                }
            }
        }
    }

    /**
     * Adds the dependencies for the code generation. It is possible to override this.
     *
     * @param project
     * @param extension
     */
    private fun addJavaCCConfiguration(project: Project, extension: JavaCCExtension) {
        val configuration = project.configurations.maybeCreate(JavaCCExtension.JAVACC_CONFIGURATION_NAME)
        configuration.setVisible(false)
                .setTransitive(true)
                .setDescription("Configuration for JavaCC code generator")
                .defaultDependencies {
                    val dependencyHandler = project.dependencies
                    it.add(dependencyHandler.create("net.java.dev.javacc:javacc:".plus(extension.javaCCVersion)))
                }
    }
}