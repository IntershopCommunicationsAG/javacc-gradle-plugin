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

open class JJTree {

    private val arguments: MutableList<String> = mutableListOf()

    var isConfigured: Boolean = false

    /**
     * Parameters, see jjtree documentation
     * All parameters are strings, so that it is possible to
     * identify if the setting is configured.
     * The configuration of a parameter will override
     * existing inline configuration.
     *
     * This parameters are interpreted as boolean.
     */
    var multi: String = ""
    var nodeDefaultVoid: String = ""
    var nodeScopeHook: String = ""
    var nodeUsesParser: String = ""
    var buildNodeFiles: String = ""
    var trackTokens: String = ""
    var visitor: String = ""
    var staticParam: String = ""

    /**
     * This parameters are interpreted as string.
     */
    var nodeClass: String? = null
    var nodePrefix: String = "AST"
    var nodePackage: String? = null
    var nodeExtends: String? = null
    var nodeFactory: String? = null

    var visitorDataType: String? = null
    var visitorReturnType: String? = null
    var visitorException: String? = null

    var args: List<String>
        get() {
            return arguments
        }
        set(value) {
            arguments.addAll(value)
        }

    fun addArg(argument: String) {
        arguments.add(argument)
    }

    fun addArgs(args: List<String>) {
        arguments += args
    }
}