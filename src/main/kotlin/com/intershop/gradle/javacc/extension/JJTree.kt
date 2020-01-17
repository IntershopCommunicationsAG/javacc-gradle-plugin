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

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional

/**
 * Configuration container for JJTree code generation.
 */
open class JJTree {

    private val arguments: MutableList<String> = mutableListOf()

    /**
     * This must be set to true if JTree
     * will be used.
     *
     * @property isConfigured
     */
    @get:Input
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

    /**
     * MULTI: Generate a multi mode parse tree.
     *
     * @property multi
     */
    @get:Optional
    @get:Input
    var multi: String = ""

    /**
     * NODE_DEFAULT_VOID: Instead of making each non-decorated
     * production an indefinite node, make it void instead.
     *
     * @property nodeDefaultVoid
     */
    @get:Optional
    @get:Input
    var nodeDefaultVoid: String = ""

    /**
     * NODE_SCOPE_HOOK: Insert calls to user-defined
     * parser methods on entry and exit of every node scope.
     *
     * @property nodeScopeHook
     */
    @get:Optional
    @get:Input
    var nodeScopeHook: String = ""

    /**
     * NODE_USES_PARSER: JJTree will use an alternate form
     * of the node construction routines where it passes
     * the parser object in.
     *
     * @property nodeUsesParser
     */
    @get:Optional
    @get:Input
    var nodeUsesParser: String = ""

    /**
     * BUILD_NODE_FILES: Generate sample implementations
     * for SimpleNode and any other nodes used in the grammar.
     *
     * @property buildNodeFiles
     */
    @get:Optional
    @get:Input
    var buildNodeFiles: String = ""

    /**
     * TRACK_TOKENS: Insert jjtGetFirstToken(), jjtSetFirstToken(),
     * getLastToken(),  and jjtSetLastToken() methods in SimpleNode.
     *
     * @property trackTokens
     */
    @get:Optional
    @get:Input
    var trackTokens: String = ""

    /**
     * VISITOR: Insert a jjtAccept() method in the node classes,
     * and generate a visitor implementation with an entry for
     * every node type used in the grammar.
     *
     * @property visitor
     */
    @get:Optional
    @get:Input
    var visitor: String = ""

    /**
     * STATIC: Generate code for a static parser.
     *
     * @property staticParam
     */
    @get:Optional
    @get:Input
    var staticParam: String = ""

    /**
     * This parameters are interpreted as string.
     */

    /**
     * NODE_CLASS: If set defines the name of a
     * user-supplied class that will extend SimpleNode.
     * Any tree nodes created will then be subclasses
     * of NODE_CLASS.
     *
     * @property nodeClass
     */
    @get:Optional
    @get:Input
    var nodeClass: String? = null

    /**
     * NODE_PREFIX: The prefix used to construct node
     * class names from node identifiers in multi mode.
     *
     * @property nodePrefix
     */
    @get:Optional
    @get:Input
    var nodePrefix: String = "AST"

    /**
     * NODE_PACKAGE: The package to generate the node classes into.
     *
     * @property nodePackage
     */
    @get:Optional
    @get:Input
    var nodePackage: String? = null

    /**
     * NODE_EXTENDS: The superclass for the SimpleNode class.
     *
     * @property nodeExtends
     */
    @get:Optional
    @get:Input
    var nodeExtends: String? = null

    /**
     * NODE_FACTORY: Specify a class containing a factory
     * method with following signature to construct nodes.
     *
     * @property nodeFactory
     */
    @get:Optional
    @get:Input
    var nodeFactory: String? = null

    /**
     * VISITOR_DATA_TYPE: If this option is set, it is used in
     * the signature of the generated jjtAccept() methods and
     * the visit() methods as the type of the data argument.
     *
     * @property visitorDataType
     */
    @get:Optional
    @get:Input
    var visitorDataType: String? = null

    /**
     * VISITOR_RETURN_TYPE: If this option is set, it is
     * used in the signature of the generated jjtAccept()
     * methods and the visit() methods as the return
     * type of the method.
     *
     * @property visitorReturnType
     */
    @get:Optional
    @get:Input
    var visitorReturnType: String? = null

    /**
     * VISITOR_EXCEPTION: If this option is set, it is
     * used in the signature of the generated jjtAccept()
     * methods and the visit() methods.
     *
     * @property visitorException
     */
    @get:Optional
    @get:Input
    var visitorException: String? = null

    /**
     * Additional command line arguments passed to jjTree.
     *
     * @property args
     */
    @get:Optional
    @get:Input
    var args: List<String>
        get() {
            return arguments
        }
        set(value) {
            arguments.addAll(value)
        }

    /**
     * Add a single argument to the list
     * of arguments for JTree.
     *
     * @param argument
     */
    fun addArg(argument: String) {
        arguments.add(argument)
    }

    /**
     * Add a list of arguments to the list
     * of arguments for JTree.
     *
     * @param args
     */
    fun addArgs(args: List<String>) {
        arguments += args
    }
}
