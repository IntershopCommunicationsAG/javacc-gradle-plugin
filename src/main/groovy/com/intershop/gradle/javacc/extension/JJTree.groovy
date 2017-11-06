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

        argsProvider = project.objects.property(List)

        multi = project.objects.property(String)
        nodeDefaultVoid = project.objects.property(String)
        nodeScopeHook = project.objects.property(String)
        nodeUsesParser = project.objects.property(String)
        buildNodeFiles = project.objects.property(String)
        trackTokens = project.objects.property(String)
        visitor = project.objects.property(String)
        staticParam = project.objects.property(String)

        nodeClass = project.objects.property(String)
        nodePrefix = project.objects.property(String)
        nodePackage = project.objects.property(String)
        nodeExtends = project.objects.property(String)
        nodeFactory = project.objects.property(String)

        visitorDataType = project.objects.property(String)
        visitorReturnType = project.objects.property(String)
        visitorException = project.objects.property(String)

        setNodePrefix('AST')
    }

    /**
     * Parameters, see jjtree documentation
     *
     * This are String parameters.
     */
    private final Property<String> multi

    Provider<String> getMultiProvider() {
        return multi
    }

    String getMulti() {
        return multi.getOrNull()
    }

    void setMulti(String multi) {
        this.multi.set(multi)
    }

    private final Property<String> nodeDefaultVoid

    Provider<String> getNodeDefaultVoidProvider() {
        return nodeDefaultVoid
    }

    String getNodeDefaultVoid() {
        return nodeDefaultVoid.getOrNull()
    }

    void setNodeDefaultVoid(String nodeDefaultVoid) {
        this.nodeDefaultVoid.set(nodeDefaultVoid)
    }

    private final Property<String> nodeScopeHook

    Provider<String> getNodeScopeHookProvider() {
        return nodeScopeHook
    }

    String getNodeScopeHook() {
        return nodeScopeHook.getOrNull()
    }

    void setNodeScopeHook(String nodeScopeHook) {
        this.nodeScopeHook.set(nodeScopeHook)
    }

    private final Property<String> nodeUsesParser

    Provider<String> getNodeUsesParserProvider() {
        return nodeUsesParser
    }

    String getNodeUsesParser() {
        return nodeUsesParser.getOrNull()
    }

    void setNodeUsesParser(String nodeUsesParser) {
        this.nodeUsesParser.set(nodeUsesParser)
    }

    private final Property<String> buildNodeFiles

    Provider<String> getBuildNodeFilesProvider() {
        return buildNodeFiles
    }

    String getBuildNodeFiles() {
        return buildNodeFiles.getOrNull()
    }

    void setBuildNodeFiles(String buildNodeFiles) {
        this.buildNodeFiles.set(buildNodeFiles)
    }

    private final Property<String> trackTokens

    Provider<String> getTrackTokensProvider() {
        return trackTokens
    }

    String getTrackTokens() {
        return trackTokens.getOrNull()
    }

    void setTrackTokens(String trackTokens) {
        this.trackTokens.set(trackTokens)
    }

    private final Property<String> visitor

    Provider<String> getVisitorProvider() {
        return visitor
    }

    String getVisitor() {
        return visitor.getOrNull()
    }

    void setVisitor(String visitor) {
        this.visitor.set(visitor)
    }

    private final Property<String> staticParam

    Provider<String> getStaticParamProvider() {
        return staticParam
    }

    String getStaticParam() {
        return staticParam.getOrNull()
    }

    void setStaticParam(String staticParam) {
        this.staticParam.set(staticParam)
    }

    /**
     * This parameters are strings.
     */

    private final Property<String> nodeClass

    Provider<String> getNodeClassProvider() {
        return nodeClass
    }

    String getNodeClass() {
        return nodeClass.getOrNull()
    }

    void setNodeClass(String nodeClass) {
        this.nodeClass.set(nodeClass)
    }

    private final Property<String> nodePrefix

    Provider<String> getNodePrefixProvider() {
        return nodePrefix
    }

    String getNodePrefix() {
        return nodePrefix.getOrNull()
    }

    void setNodePrefix(String nodePrefix) {
        this.nodePrefix.set(nodePrefix)
    }

    private final Property<String> nodePackage

    Provider<String> getNodePackageProvider() {
        return nodePackage
    }

    String getNodePackage() {
        return nodePackage.getOrNull()
    }

    void setNodePackage(String nodePackage) {
        this.nodePackage.set(nodePackage)
    }

    private final Property<String> nodeExtends

    Provider<String> getNodeExtendsProvider() {
        return nodeExtends
    }

    String getNodeExtends() {
        return nodeExtends.getOrNull()
    }

    void setNodeExtends(String nodeExtends) {
        this.nodeExtends.set(nodeExtends)
    }

    private final Property<String> nodeFactory

    Provider<String> getNodeFactoryProvider() {
        return nodeFactory
    }

    String getNodeFactory() {
        return nodeFactory.getOrNull()
    }

    void setNodeFactory(String nodeFactory) {
        this.nodeFactory.set(nodeFactory)
    }

    private final Property<String> visitorDataType

    Provider<String> getVisitorDataTypeProvider() {
        return visitorDataType
    }

    String getVisitorDataType() {
        return visitorDataType.getOrNull()
    }

    void setVisitorDataType(String visitorDataType) {
        this.visitorDataType.set(visitorDataType)
    }

    private final Property<String> visitorReturnType

    Provider<String> getVisitorReturnTypeProvider() {
        return visitorReturnType
    }

    String getVisitorReturnType() {
        return visitorReturnType.getOrNull()
    }

    void setVisitorReturnType(String visitorReturnType) {
        this.visitorReturnType.set(visitorReturnType)
    }

    private final Property<String> visitorException

    Provider<String> getVisitorExceptionProvider() {
        return visitorException
    }

    String getVisitorException() {
        return visitorException.getOrNull()
    }

    void setVisitorException(String visitorException) {
        this.visitorException.set(visitorException)
    }

    /**
     * Additional ars for jjtree
     */
    private final Property<List<String>> argsProvider

    Provider<List<String>> getArgsProvider() {
        return argsProvider
    }

    List<String> getArgs() {
        return argsProvider.getOrNull()
    }

    void setArgs(List<String> args) {
        this.argsProvider.set(args)
    }

    void args(String paramater) {
        argsProvider.getOrNull().add(paramater)
    }

}
