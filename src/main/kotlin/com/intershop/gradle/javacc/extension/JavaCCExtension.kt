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
@file:Suppress("UnstableApiUsage")
package com.intershop.gradle.javacc.extension

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import javax.inject.Inject

/**
 * JavaCC main extension of this plugin.
 */
open class JavaCCExtension @Inject constructor(objectFactory: ObjectFactory) {

    companion object {
        /**
         * Extension name of plugin.
         */
        const val JAVACC_EXTENSION_NAME = "javacc"

        /**
         * Task group name of jaxb code generation.
         */
        const val JAVACC_GROUP_NAME = "JAVACC Code Generation"

        /**
         * Dependency configuration name for JavaCC code generation.
         */
        const val JAVACC_CONFIGURATION_NAME = "javacc"

        /**
         * Default version of JavaCC library.
         */
        const val JAVACC_VERSION = "5.0" // 5.0 seems to be the highest compatible version

        /**
         * Default output path of all generation tasks.
         **/
        const val CODEGEN_OUTPUTPATH = "generated/javacc"
    }

    /**
     * Config container for JavaCC code generation.
     */
    val configs: NamedDomainObjectContainer<JavaCC> = objectFactory.domainObjectContainer(JavaCC::class.java)

    private val javaCCVersionProperty: Property<String> = objectFactory.property(String::class.java)

    init {
        javaCCVersionProperty.convention(JAVACC_VERSION)
    }

    /**
     * Provider for version configuration of JavaCC lib.
     */
    val javaCCVersionProvider: Provider<String>
        get() = javaCCVersionProperty

    /**
     * Version configuration property of
     * JavaCC lib.
     *
     * @property javaCCVersion
     */
    var javaCCVersion: String
        get() = javaCCVersionProperty.get()
        set(value) = javaCCVersionProperty.set(value)
}
