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
package com.intershop.gradle.javacc.extension

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.process.JavaForkOptions
import org.gradle.process.internal.DefaultJavaForkOptions

open class JavaCCExtension (project: Project) {

    companion object {
        // names for the plugin
        const val JAVACC_EXTENSION_NAME = "javacc"
        const val JAVACC_GROUP_NAME = "JAVACC Code Generation"

        const val JAVACC_CONFIGURATION_NAME = "javacc"

        // default versions
        const val JAVACC_VERSION = "4.2"

        const val CODEGEN_OUTPUTPATH = "generated/javacc"
    }

    val configs: NamedDomainObjectContainer<JavaCC> = project.container(JavaCC::class.java, JavaCCFactory(project))

    fun configs(configureAction: Action<in NamedDomainObjectContainer<JavaCC>>) {
        configureAction.execute(configs)
    }

    // javacc version configuration
    val javaCCVersionProvider: Property<String> = project.objects.property(String::class.java)

    init {
        javaCCVersionProvider.set(JAVACC_VERSION)
    }

    var javaCCVersion: String
        get() {
            return javaCCVersionProvider.get()
        }
        set(value) {
            javaCCVersionProvider.set(value)
        }
}