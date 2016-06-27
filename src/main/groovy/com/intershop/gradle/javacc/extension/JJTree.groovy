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

/**
 * This is the container for the jjtree
 * configuration parameters.
 */
class JJTree {
    /**
     * Parameters, see jjtree documentation
     * All parameters are strings, so that it is possible to
     * identify if the setting is configured.
     * The configuration of a parameter will override
     * existing inline configuration.
     *
     * This parameters are interpreted as boolean.
     */
    String multi
    String nodeDefaultVoid
    String nodeScopeHook
    String nodeUsesParser
    String buildNodeFiles
    String trackTokens
    String visitor
    String staticParam

    /**
     * This parameters are interpreted as string.
     */
    String nodeClass
    String nodePrefix = 'AST'
    String nodePackage
    String nodeExtends
    String nodeFactory

    String visitorDataType
    String visitorReturnType
    String visitorException

    /**
     * Parameter properties for jjtree
     *
     * @return properties
     */
    Properties getParameters() {
        Properties props = new Properties()

        if(getBuildNodeFiles()) {
            props.put('BUILD_NODE_FILES', getBuildNodeFiles().toBoolean())
        }
        if(getMulti()) {
            props.put('MULTI', getMulti().toBoolean())
        }
        if(getNodeDefaultVoid()) {
            props.put('NODE_DEFAULT_VOID', getNodeDefaultVoid().toBoolean())
        }
        if(getNodeDefaultVoid()) {
            props.put('NODE_SCOPE_HOOK', getNodeScopeHook().toBoolean())
        }
        if(getNodeDefaultVoid()) {
            props.put('NODE_USES_PARSER', getNodeUsesParser().toBoolean())
        }
        if(getNodeDefaultVoid()) {
            props.put('TRACK_TOKENS', getTrackTokens().toBoolean())
        }
        if(getVisitor() != null) {
            props.put('VISITOR', getVisitor().toBoolean())
        }
        if(getStaticParam() != null) {
            props.put('STATIC', getStaticParam().toBoolean())
        }

        if(getNodeClass()) {
            props.put('NODE_CLASS', getNodeClass())
        }
        if(getNodePrefix()) {
            props.put('NODE_PREFIX', getNodePrefix())
        }
        if(getNodeExtends()) {
            props.put('NODE_EXTENDS', getNodeExtends())
        }
        if(getNodePackage()) {
            props.put('NODE_PACKAGE', getNodePackage())
        }
        if(getNodeFactory()) {
            props.put('NODE_FACTORY', getNodeFactory())
        }
        if(getVisitorDataType()) {
            props.put('VISITOR_DATA_TYPE', getVisitorDataType())
        }
        if(getVisitorReturnType()) {
            props.put('VISITOR_RETURN_TYPE', getVisitorReturnType())
        }
        if(getVisitorException()) {
            props.put('VISITOR_EXCEPTION', getVisitorException())
        }

        return props
    }

    /**
     * Additional ars for jjtree
     */
    def args = []

    void arg(String parameter) {
        args.add(parameter)
    }

}
