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
import groovy.util.logging.Slf4j
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.util.ConfigureUtil
import org.gradle.util.GUtil

import javax.swing.JTree

/**
 * This is the container for all JavaCC related configuration details.
 */
@CompileStatic
@Slf4j
class JavaCC implements Named {

    private final Project project
    private JJTree jjtree

    final String name

    /**
     * Output path
     */
    private final Property<File> outputDir

    Provider<File> getOutputDirProvider() {
        return outputDir
    }

    File getOutputDir() {
        return outputDir.getOrNull()
    }

    void setOutputDir(File outputDir) {
        this.outputDir.set(outputDir)
    }

    /**
     * Package name of the generated code
     */
    private final Property<String> packageName

    Provider<String> getPackageNameProvider() {
        return packageName
    }

    String getPackageName() {
        return packageName.getOrNull()
    }

    void setPackageName(String packageName) {
        this.packageName.set(packageName)
    }

    /**
     * Input file
     */

    private final Property<File> inputFile

    Provider<File> getInputFileProvider() {
        return inputFile
    }

    File getInputFile() {
        return inputFile.getOrNull()
    }

    void setInputFile(File inputFile) {
        this.inputFile.set(inputFile)
    }

    private final Property<String> jdkVersion

    Provider<String> getJdkVersionProvider() {
        return jdkVersion
    }

    String getJdkVersion() {
        return jdkVersion.getOrNull()
    }

    void setJdkVersion(String jdkVersion) {
        this.jdkVersion.set(jdkVersion)
    }

    /**
     * Name of the source set for generated Java code
     * default value is 'main' (JavaCCExtension.DEFAULT_SOURCESET_NAME)
     */
    private final Property<String> sourceSetName

    Provider<String> getSourceSetNameProvider() {
        return sourceSetName
    }

    String getSourceSetName() {
        return sourceSetName.getOrNull()
    }

    void setSourceSetName(String sourceSetName) {
        this.sourceSetName.set(sourceSetName)
    }

    /**
     * Parameters, see javacc documentation
     * This are the integer parameters.
     */
    private final Property<Integer> lookahead

    Provider<Integer> getLookaheadProvider() {
        return lookahead
    }

    int getLookahead() {
        return lookahead.getOrNull()
    }

    void setLookahead(int lookahead) {
        this.lookahead.set(lookahead)
    }

    private final Property<Integer> choiceAmbiguityCheck

    Provider<Integer> getChoiceAmbiguityCheckProvider() {
        return choiceAmbiguityCheck
    }

    int getChoiceAmbiguityCheck() {
        return choiceAmbiguityCheck.getOrNull()
    }

    void setChoiceAmbiguityCheck(int choiceAmbiguityCheck) {
        this.choiceAmbiguityCheck.set(choiceAmbiguityCheck)
    }

    private final Property<Integer> otherAmbiguityCheck

    Provider<Integer> getOtherAmbiguityCheckProvider() {
        return otherAmbiguityCheck
    }

    Integer getOtherAmbiguityCheck() {
        return otherAmbiguityCheck.getOrNull()
    }

    void setOtherAmbiguityCheck(Integer otherAmbiguityCheck) {
        this.otherAmbiguityCheck.set(otherAmbiguityCheck)
    }

    /**
     * This are the boolean parameters.
     */
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

    private final Property<String> supportClassVisibilityPublic

    Provider<String> getSupportClassVisibilityPublicProvider() {
        return supportClassVisibilityPublic
    }

    String getSupportClassVisibilityPublic() {
        return supportClassVisibilityPublic.getOrNull()
    }

    void setSupportClassVisibilityPublic(String supportClassVisibilityPublic) {
        this.supportClassVisibilityPublic.set(supportClassVisibilityPublic)
    }

    private final Property<String> debugParser

    Provider<String> getDebugParserProvider() {
        return debugParser
    }

    String getDebugParser() {
        return debugParser.getOrNull()
    }

    void setDebugParser(String debugParser) {
        this.debugParser.set(debugParser)
    }

    private final Property<String> debugLookahead

    Provider<String> getDebugLookaheadProvider() {
        return debugLookahead
    }

    String getDebugLookahead() {
        return debugLookahead.getOrNull()
    }

    void setDebugLookahead(String debugLookahead) {
        this.debugLookahead.set(debugLookahead)
    }

    private final Property<String> debugTokenManager

    Provider<String> getDebugTokenManagerProvider() {
        return debugTokenManager
    }

    String getDebugTokenManager() {
        return debugTokenManager.getOrNull()
    }

    void setDebugTokenManager(String debugTokenManager) {
        this.debugTokenManager.set(debugTokenManager)
    }

    private final Property<String> errorReporting

    Provider<String> getErrorReportingProvider() {
        return errorReporting
    }

    String getErrorReporting() {
        return errorReporting.getOrNull()
    }

    void setErrorReporting(String errorReporting) {
        this.errorReporting.set(errorReporting)
    }

    private final Property<String> javaUnicodeEscape

    Provider<String> getJavaUnicodeEscapeProvider() {
        return javaUnicodeEscape
    }

    String getJavaUnicodeEscape() {
        return javaUnicodeEscape.getOrNull()
    }

    void setJavaUnicodeEscape(String javaUnicodeEscape) {
        this.javaUnicodeEscape.set(javaUnicodeEscape)
    }

    private final Property<String> unicodeInput

    Provider<String> getUnicodeInputProvider() {
        return unicodeInput
    }

    String getUnicodeInput() {
        return unicodeInput.getOrNull()
    }

    void setUnicodeInput(String unicodeInput) {
        this.unicodeInput.set(unicodeInput)
    }

    private final Property<String> ignoreCase

    Provider<String> getIgnoreCaseProvider() {
        return ignoreCase
    }

    String getIgnoreCase() {
        return ignoreCase.getOrNull()
    }

    void setIgnoreCase(String ignoreCase) {
        this.ignoreCase.set(ignoreCase)
    }

    private final Property<String> commonTokenAction

    Provider<String> getCommonTokenActionProvider() {
        return commonTokenAction
    }

    String getCommonTokenAction() {
        return commonTokenAction.getOrNull()
    }

    void setCommonTokenAction(String commonTokenAction) {
        this.commonTokenAction.set(commonTokenAction)
    }

    private final Property<String> userTokenManager

    Provider<String> getUserTokenManagerProvider() {
        return userTokenManager
    }

    String getUserTokenManager() {
        return userTokenManager.getOrNull()
    }

    void setUserTokenManager(String userTokenManager) {
        this.userTokenManager.set(userTokenManager)
    }

    private final Property<String> userCharStream

    Provider<String> getUserCharStreamProvider() {
        return userCharStream
    }

    String getUserCharStream() {
        return userCharStream.getOrNull()
    }

    void setUserCharStream(String userCharStream) {
        this.userCharStream.set(userCharStream)
    }

    private final Property<String> buildParser

    Provider<String> getBuildParserProvider() {
        return buildParser
    }

    String getBuildParser() {
        return buildParser.getOrNull()
    }

    void setBuildParser(String buildParser) {
        this.buildParser.set(buildParser)
    }

    private final Property<String> buildTokenManager

    Provider<String> getBuildTokenManagerProvider() {
        return buildTokenManager
    }

    String getBuildTokenManager() {
        return buildTokenManager.getOrNull()
    }

    void setBuildTokenManager(String buildTokenManager) {
        this.buildTokenManager.set(buildTokenManager)
    }

    private final Property<String> tokenManagerUsesParser

    Provider<String> getTokenManagerUsesParserProvider() {
        return tokenManagerUsesParser
    }

    String getTokenManagerUsesParser() {
        return tokenManagerUsesParser.getOrNull()
    }

    void setTokenManagerUsesParser(String tokenManagerUsesParser) {
        this.tokenManagerUsesParser.set(tokenManagerUsesParser)
    }

    private final Property<String> sanityCheck

    Provider<String> getSanityCheckProvider() {
        return sanityCheck
    }

    String getSanityCheck() {
        return sanityCheck.getOrNull()
    }

    void setSanityCheck(String sanityCheck) {
        this.sanityCheck.set(sanityCheck)
    }

    private final Property<String> forceLaCheck

    Provider<String> getForceLaCheckProvider() {
        return forceLaCheck
    }

    String getForceLaCheck() {
        return forceLaCheck.getOrNull()
    }

    void setForceLaCheck(String forceLaCheck) {
        this.forceLaCheck.set(forceLaCheck)
    }

    private final Property<String> cacheTokens

    Provider<String> getCacheTokensProvider() {
        return cacheTokens
    }

    String getCacheTokens() {
        return cacheTokens.getOrNull()
    }

    void setCacheTokens(String cacheTokens) {
        this.cacheTokens.set(cacheTokens)
    }

    private final Property<String> keepLineColumn

    Provider<String> getKeepLineColumnProvider() {
        return keepLineColumn
    }

    String getKeepLineColumn() {
        return keepLineColumn.getOrNull()
    }

    void setKeepLineColumn(String keepLineColumn) {
        this.keepLineColumn.set(keepLineColumn)
    }

    /**
     * This parameters are strings.
     */
    private final Property<String> tokenExtends

    Provider<String> getTokenExtendsProvider() {
        return tokenExtends
    }

    String getTokenExtends() {
        return tokenExtends.getOrNull()
    }

    void setTokenExtends(String tokenExtends) {
        this.tokenExtends.set(tokenExtends)
    }

    private final Property<String> tokenFactory

    Provider<String> getTokenFactoryProvider() {
        return tokenFactory
    }

    String getTokenFactory() {
        return tokenFactory.getOrNull()
    }

    void setTokenFactory(String tokenFactory) {
        this.tokenFactory.set(tokenFactory)
    }

    /**
     * Calculate the task name
     * @return
     */
    String getTaskName() {
        return "javacc" + GUtil.toCamelCase(name)
    }

    /**
     * Additional args for javaCC
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

    private final Property<String> runJJTree

    Provider<String> getRunJJTreeProvider() {
        return runJJTree
    }

    String getRunJJTree() {
        return runJJTree.get()
    }

    void setRunJJTree(String runJJTree) {
        this.runJJTree.set(runJJTree)
    }


    /**
     * Constructor
     *
     * @param project
     * @param name
     */
    JavaCC(Project project, String name) {
        this.project = project
        this.name = name

        jjtree = new JJTree(project)

        outputDir = project.objects.property(File)
        packageName = project.objects.property(String)
        inputFile = project.objects.property(File)
        jdkVersion = project.objects.property(String)
        sourceSetName = project.objects.property(String)

        lookahead = project.objects.property(Integer)
        choiceAmbiguityCheck = project.objects.property(Integer)
        otherAmbiguityCheck = project.objects.property(Integer)

        staticParam = project.objects.property(String)
        supportClassVisibilityPublic = project.objects.property(String)
        debugParser = project.objects.property(String)
        debugLookahead = project.objects.property(String)
        debugTokenManager = project.objects.property(String)
        errorReporting = project.objects.property(String)
        javaUnicodeEscape = project.objects.property(String)
        unicodeInput = project.objects.property(String)
        ignoreCase = project.objects.property(String)
        commonTokenAction = project.objects.property(String)
        userTokenManager = project.objects.property(String)
        userCharStream = project.objects.property(String)
        buildParser = project.objects.property(String)
        buildTokenManager = project.objects.property(String)
        tokenManagerUsesParser = project.objects.property(String)
        sanityCheck = project.objects.property(String)
        forceLaCheck = project.objects.property(String)
        cacheTokens = project.objects.property(String)
        keepLineColumn = project.objects.property(String)

        tokenExtends = project.objects.property(String)
        tokenFactory = project.objects.property(String)

        argsProvider = project.objects.property(List)

        runJJTree = project.objects.property(String)
        setRunJJTree('false')

        setSourceSetName(JavaCCExtension.DEFAULT_SOURCESET_NAME)

        outputDir.set(project.getLayout().getBuildDirectory().
                dir("${JavaCCExtension.CODEGEN_DEFAULT_OUTPUTPATH}/${name.replace(' ', '_')}").getOrNull().asFile)

    }

    /**
     * JJTree configuration closure
     *
     * @param closure
     * @return
     */
    JJTree jjtree(Closure closure) {
        setRunJJTree('true')
        return ConfigureUtil.configure(closure, jjtree)
    }

    JJTree getJJTree() {
        return jjtree
    }
}
