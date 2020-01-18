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
package com.intershop.gradle.javacc.task

import org.gradle.api.provider.Property
import org.gradle.api.provider.ListProperty
import org.gradle.workers.WorkParameters
import java.io.File

/**
 * Worker parameters for worker runner.
 */
interface JavaCCRunnerParameters : WorkParameters {

    /**
     * Property outputDir for code generation.
     * @property outputDir
     */
    val outputDir: Property<File>

    /**
     * Property inputFile for code generation.
     * @property inputFile
     */
    val inputFile: Property<File>

    /**
     * Property javaCCParamList with all
     * JavaCC parameters in a list.
     *
     * @property javaCCParamList
     */
    val javaCCParamList: ListProperty<String>

    /**
     * Property jjTreeParamList with all
     * JTree parameters in a list.
     *
     * @property jjTreeParamList
     */
    val jjTreeParamList: ListProperty<String>
}
