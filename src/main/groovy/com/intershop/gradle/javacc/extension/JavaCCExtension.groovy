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
package com.intershop.gradle.javacc.extension

import groovy.transform.CompileStatic
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.process.JavaForkOptions

/**
 * This is the implementation
 */
@CompileStatic
class JavaCCExtension {

    /**
     * Default version of JAXB
     */
    final static String JAVACC_DEFAULT_VERSION = '4.2'

    /**
     * Extension name
     */
    final static String JAVACC_EXTENSION_NAME = 'javacc'

    /**
     * Dependency configuration name
     */
    final static String JAVACC_CONFIGURATION_NAME = 'javacc'

    /**
     * Task group name
     */
    final static String JAVACC_TASK_GROUP = 'javacc code generation'

    /**
     * Default output path
     */
    final static String CODEGEN_DEFAULT_OUTPUTPATH = 'generated/javacc'

    /**
     * Default source set name
     */
    final static String DEFAULT_SOURCESET_NAME = 'main'

    /**
     * Configuration sets for JavaCC
     */
    final NamedDomainObjectContainer<JavaCC> configs

    private Project project

    /**
     * Constructor of the extension
     * @param project reference
     */
    JavaCCExtension(Project project) {
        this.project = project

        if(! javaCCVersion) {
            javaCCVersion = JAVACC_DEFAULT_VERSION
        }

        configs = project.container(JavaCC, new JavaCCFactory(project))
    }

    /**
     * Closure for all javacc configurations.
     * @param c
     * @return
     */
    def configs(Closure c) {
        configs.configure(c)
    }

    /**
     * Version of javacc, default is 4.2
     */
    String javaCCVersion

    /**
     * This configures the special options for the used VM.
     */
    JavaForkOptions forkOptions

    void forkOptions(Closure c) {
        project.configure(forkOptions, c)
    }
}