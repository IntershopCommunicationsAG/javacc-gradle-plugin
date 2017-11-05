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
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider

/**
 * This is the container for the jjtree
 * configuration parameters.
 */
@CompileStatic
class JJTree {

    private final Project project

    JJTree(Project project) {
        this.project = project

        parameters = project.objects.property(Map)
        argsProvider = project.objects.property(List)
    }

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
     */
    private final Property<Map<String, String>> parameters

    Provider<Map<String, String>> getParametersProvider() {
        Map<String,String> params = [:]

        if(getBuildNodeFiles()) {
            params.put('BUILD_NODE_FILES', getBuildNodeFiles().toBoolean().toString())
        }
        if(getMulti()) {
            params.put('MULTI', getMulti().toBoolean().toString())
        }
        if(getNodeDefaultVoid()) {
            params.put('NODE_DEFAULT_VOID', getNodeDefaultVoid().toBoolean().toString())
        }
        if(getNodeDefaultVoid()) {
            params.put('NODE_SCOPE_HOOK', getNodeScopeHook().toBoolean().toString())
        }
        if(getNodeDefaultVoid()) {
            params.put('NODE_USES_PARSER', getNodeUsesParser().toBoolean().toString())
        }
        if(getNodeDefaultVoid()) {
            params.put('TRACK_TOKENS', getTrackTokens().toBoolean().toString())
        }
        if(getVisitor() != null) {
            params.put('VISITOR', getVisitor().toBoolean().toString())
        }
        if(getStaticParam() != null) {
            params.put('STATIC', getStaticParam().toBoolean().toString())
        }

        if(getNodeClass()) {
            params.put('NODE_CLASS', getNodeClass())
        }
        if(getNodePrefix()) {
            params.put('NODE_PREFIX', getNodePrefix())
        }
        if(getNodeExtends()) {
            params.put('NODE_EXTENDS', getNodeExtends())
        }
        if(getNodePackage()) {
            params.put('NODE_PACKAGE', getNodePackage())
        }
        if(getNodeFactory()) {
            params.put('NODE_FACTORY', getNodeFactory())
        }
        if(getVisitorDataType()) {
            params.put('VISITOR_DATA_TYPE', getVisitorDataType())
        }
        if(getVisitorReturnType()) {
            params.put('VISITOR_RETURN_TYPE', getVisitorReturnType())
        }
        if(getVisitorException()) {
            params.put('VISITOR_EXCEPTION', getVisitorException())
        }

        parameters.set(params)
        return parameters
    }

    /**
     * Parameter properties for jjtree
     *
     * @return properties
     */
    Map<String,String> getParameters() {
        return parameters.get()
    }

    /**
     * Additional ars for jjtree
     */
    private final Property<List<String>> argsProvider

    Provider<List<String>> getArgsProvider() {
        return argsProvider
    }

    List<String> getArgs() {
        return argsProvider.get()
    }

    void setArgs(List<String> args) {
        this.argsProvider.set(args)
    }

    void args(String paramater) {
        argsProvider.get().add(paramater)
    }

}
