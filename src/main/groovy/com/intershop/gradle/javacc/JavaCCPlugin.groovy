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

import com.intershop.gradle.javacc.extension.JavaCCExtension
import com.intershop.gradle.javacc.extension.JavaCC
import com.intershop.gradle.javacc.task.JavaCCTask
import groovy.transform.CompileStatic
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.DependencySet
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.SourceSet

/**
 * The JavaCC plugin implementation.
 */
@CompileStatic
class JavaCCPlugin implements Plugin<Project> {

    /**
     * Extension of this plugin
     */
    private JavaCCExtension extension

    /**
     * Apply this plugin to the given target object.
     *
     * @param project The target object
     */
    void apply (Project project) {

        project.logger.info('Create extension {} for {}', JavaCCExtension.JAVACC_EXTENSION_NAME, project.name)
        // create extension
        extension = project.extensions.create(JavaCCExtension.JAVACC_EXTENSION_NAME, JavaCCExtension, project)

        // add dependency configuration
        addConfiguration(project)

        // create main javaCC task
        Task javaccTask = project.getTasks().create('javacc')
        javaccTask.group = JavaCCExtension.JAVACC_TASK_GROUP
        javaccTask.description = 'All JavaCC code generation tasks'

        // configure tasks for all configurations
        configureJavaCCTasks(project, javaccTask)
    }

    /**
     * Adds the dependencies for the code generation. It is possible to override this.
     *
     * @param project
     * @param extension
     */
    private void addConfiguration(final Project project) {
        final Configuration configuration =
                project.getConfigurations().findByName(JavaCCExtension.JAVACC_CONFIGURATION_NAME) ?:
                        project.getConfigurations().create(JavaCCExtension.JAVACC_CONFIGURATION_NAME)

        configuration
                .setVisible(false)
                .setTransitive(false)
                .setDescription("JavaCC configuration is used for code generation")
                .defaultDependencies(new Action<DependencySet>() {
            @Override
            void execute(DependencySet dependencies ) {
                DependencyHandler dependencyHandler = project.getDependencies()
                dependencies.add(dependencyHandler.create('net.java.dev.javacc:javacc:' + extension.getJavaCCVersion()))
            }
        })
    }

    /**
     * Configure task for javaCC code generation
     *
     * @param project
     * @param javacc
     */
    private void configureJavaCCTasks(Project project, Task javacc) {
        extension.getConfigs().all { JavaCC jcc ->
            JavaCCTask task = project.getTasks().create(jcc.getTaskName(), JavaCCTask)
            task.group = JavaCCExtension.JAVACC_TASK_GROUP

            task.setOutputDir(jcc.getOutputDirProvider())

            task.setPackageName(jcc.getPackageNameProvider())
            task.setInputFile(jcc.getInputFileProvider())
            task.setJdkVersion(jcc.getJdkVersionProvider())
            task.setJavaCCParameters(jcc.getParametersProvider())
            task.setJavaCCArgs(jcc.getArgsProvider())

            task.setJJTreeParameters(jcc.getJJTree()?.getParametersProvider())
            task.setJJTreeArgs(jcc.getJJTree()?.getArgsProvider())

            extension.setForkOptions(task.getForkOptions())

            project.afterEvaluate {
                if (jcc.getSourceSetName() && project.plugins.hasPlugin(JavaBasePlugin) && ! project.convention.getPlugin(JavaPluginConvention.class).sourceSets.isEmpty()) {
                    SourceSet sourceSet = project.convention.getPlugin(JavaPluginConvention.class).sourceSets.findByName(jcc.getSourceSetName())
                    if(sourceSet != null) {
                        if(! sourceSet.java.srcDirs.contains(task.getOutputDir()) ) {
                            sourceSet.java.srcDir(task.getOutputDir())
                        }
                        project.tasks.getByName(sourceSet.compileJavaTaskName).dependsOn(task)
                    }
                }
            }

            javacc.dependsOn task
        }
    }
}