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
package com.intershop.gradle.javacc.task

import com.intershop.gradle.javacc.extension.JavaCCExtension
import groovy.transform.CompileStatic
import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.file.FileCollection
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.*
import org.gradle.process.JavaForkOptions
import org.gradle.process.internal.DefaultJavaForkOptions
import org.gradle.process.internal.JavaExecHandleBuilder

import javax.inject.Inject

@CompileStatic
class JavaCCTask extends DefaultTask {

    final static String JJTREE_MAIN_CLASS_NAME = 'jjtree'
    final static String JAVACC_MAIN_CLASS_NAME = 'javacc'

    final Property<File> outputDir = project.objects.property(File)

    @OutputDirectory
    File getOutputDir() {
        return outputDir.get()
    }

    void setOutputDir(File outputDir) {
        this.outputDir.set(outputDir)
    }

    void setOutputDir(Provider<File> outputDir) {
        this.outputDir.set(outputDir)
    }

    final Property<String> packageName = project.objects.property(String)

    @Optional
    @Input
    String getPackageName() {
        return packageName.getOrNull()
    }

    void setPackageName(String packageName) {
        this.packageName.set(packageName)
    }

    void setPackageName(Provider<String> packageName) {
        this.packageName.set(packageName)
    }

    final Property<File> inputFile = project.objects.property(File)

    @InputFile
    File getInputFile() {
        return inputFile.get()
    }

    void setInputFile(File inputFile) {
        this.inputFile.set(inputFile)
    }

    void setInputFile(Provider<File> inputFile) {
        this.inputFile.set(inputFile)
    }

    final Property<String> jdkVersion = project.objects.property(String)

    @Optional
    @Input
    String getJdkVersion() {
        return jdkVersion.getOrNull()
    }

    void setJdkVersion(String jdkVersion) {
        this.jdkVersion.set(jdkVersion)
    }

    void setJdkVersion(Provider<String> jdkVersion) {
        this.jdkVersion.set(jdkVersion)
    }

    final ListProperty<String> javaCCArgs = project.objects.listProperty(String)

    @Optional
    @Input
    List<String> getJavaCCArgs() {
        return javaCCArgs.getOrNull()
    }

    void setJavaCCArgs(List<String> args) {
        this.javaCCArgs.set(args)
    }

    void setJavaCCArgs(Provider<List<String>> args) {
        this.javaCCArgs.set(args)
    }

    final Property<Integer> lookahead = project.objects.property(Integer)

    @Optional
    @Input
    int getLookahead() {
        return lookahead.getOrNull()
    }

    void setLookahead(int lookahead) {
        this.lookahead.set(lookahead)
    }

    void setLookahead(Provider<Integer> lookahead) {
        this.lookahead.set(lookahead)
    }

    final Property<Integer> choiceAmbiguityCheck = project.objects.property(Integer)

    @Optional
    @Input
    int getChoiceAmbiguityCheck() {
        return choiceAmbiguityCheck.getOrNull()
    }

    void setChoiceAmbiguityCheck(int choiceAmbiguityCheck) {
        this.choiceAmbiguityCheck.set(choiceAmbiguityCheck)
    }

    void setChoiceAmbiguityCheck(Provider<Integer> choiceAmbiguityCheck) {
        this.choiceAmbiguityCheck.set(choiceAmbiguityCheck)
    }

    final Property<Integer> otherAmbiguityCheck = project.objects.property(Integer)

    @Optional
    @Input
    int getOtherAmbiguityCheck() {
        return otherAmbiguityCheck.getOrNull()
    }

    void setOtherAmbiguityCheck(int otherAmbiguityCheck) {
        this.otherAmbiguityCheck.set(otherAmbiguityCheck)
    }

    void setOtherAmbiguityCheck(Provider<Integer> otherAmbiguityCheck) {
        this.otherAmbiguityCheck.set(otherAmbiguityCheck)
    }

    final Property<String> staticParam = project.objects.property(String)

    @Optional
    @Input
    String getStaticParam() {
        return staticParam.getOrNull()
    }

    void setStaticParam(String staticParam) {
        this.staticParam.set(staticParam)
    }

    void setStaticParam(Provider<String> staticParam) {
        this.staticParam.set(staticParam)
    }

    final Property<String> supportClassVisibilityPublic = project.objects.property(String)

    @Optional
    @Input
    String getSupportClassVisibilityPublic() {
        return supportClassVisibilityPublic.getOrNull()
    }

    void setSupportClassVisibilityPublic(String supportClassVisibilityPublic) {
        this.supportClassVisibilityPublic.set(supportClassVisibilityPublic)
    }

    void setSupportClassVisibilityPublic(Provider<String> supportClassVisibilityPublic) {
        this.supportClassVisibilityPublic.set(supportClassVisibilityPublic)
    }

    final Property<String> debugParser = project.objects.property(String)

    @Optional
    @Input
    String getDebugParser() {
        return debugParser.getOrNull()
    }

    void setDebugParser(String debugParser) {
        this.debugParser.set(debugParser)
    }

    void setDebugParser(Provider<String> debugParser) {
        this.debugParser.set(debugParser)
    }

    final Property<String> debugLookahead = project.objects.property(String)

    @Optional
    @Input
    String getDebugLookahead() {
        return debugLookahead.getOrNull()
    }

    void setDebugLookahead(String debugLookahead) {
        this.debugLookahead.set(debugLookahead)
    }

    void setDebugLookahead(Provider<String> debugLookahead) {
        this.debugLookahead.set(debugLookahead)
    }

    final Property<String> debugTokenManager = project.objects.property(String)

    @Optional
    @Input
    String getDebugTokenManager() {
        return debugTokenManager.getOrNull()
    }

    void setDebugTokenManager(String debugTokenManager) {
        this.debugTokenManager.set(debugTokenManager)
    }

    void setDebugTokenManager(Provider<String> debugTokenManager) {
        this.debugTokenManager.set(debugTokenManager)
    }

    final Property<String> errorReporting = project.objects.property(String)

    @Optional
    @Input
    String getErrorReporting() {
        return errorReporting.getOrNull()
    }

    void setErrorReporting(String errorReporting) {
        this.errorReporting.set(errorReporting)
    }

    void setErrorReporting(Provider<String> errorReporting) {
        this.errorReporting.set(errorReporting)
    }

    final Property<String> javaUnicodeEscape = project.objects.property(String)

    @Optional
    @Input
    String getJavaUnicodeEscape() {
        return javaUnicodeEscape.getOrNull()
    }

    void setJavaUnicodeEscape(String javaUnicodeEscape) {
        this.javaUnicodeEscape.set(javaUnicodeEscape)
    }

    void setJavaUnicodeEscape(Provider<String> javaUnicodeEscape) {
        this.javaUnicodeEscape.set(javaUnicodeEscape)
    }

    final Property<String> unicodeInput = project.objects.property(String)

    @Optional
    @Input
    String getUnicodeInput() {
        return unicodeInput.getOrNull()
    }

    void setUnicodeInput(String unicodeInput) {
        this.unicodeInput.set(unicodeInput)
    }

    void setUnicodeInput(Provider<String> unicodeInput) {
        this.unicodeInput.set(unicodeInput)
    }

    final Property<String> ignoreCase = project.objects.property(String)

    @Optional
    @Input
    String getIgnoreCase() {
        return ignoreCase.getOrNull()
    }

    void setIgnoreCase(String ignoreCase) {
        this.ignoreCase.set(ignoreCase)
    }

    void setIgnoreCase(Provider<String> ignoreCase) {
        this.ignoreCase.set(ignoreCase)
    }

    final Property<String> commonTokenAction = project.objects.property(String)

    @Optional
    @Input
    String getCommonTokenAction() {
        return commonTokenAction.getOrNull()
    }

    void setCommonTokenAction(String commonTokenAction) {
        this.commonTokenAction.set(commonTokenAction)
    }

    void setCommonTokenAction(Provider<String> commonTokenAction) {
        this.commonTokenAction.set(commonTokenAction)
    }

    final Property<String> userTokenManager = project.objects.property(String)

    @Optional
    @Input
    String getUserTokenManager() {
        return userTokenManager.getOrNull()
    }

    void setUserTokenManager(String userTokenManager) {
        this.userTokenManager.set(userTokenManager)
    }

    void setUserTokenManager(Provider<String> userTokenManager) {
        this.userTokenManager.set(userTokenManager)
    }

    final Property<String> userCharStream = project.objects.property(String)

    @Optional
    @Input
    String getUserCharStream() {
        return userCharStream.getOrNull()
    }

    void setUserCharStream(String userCharStream) {
        this.userCharStream.set(userCharStream)
    }

    void setUserCharStream(Provider<String> userCharStream) {
        this.userCharStream.set(userCharStream)
    }

    final Property<String> buildParser = project.objects.property(String)

    @Optional
    @Input
    String getBuildParser() {
        return buildParser.getOrNull()
    }

    void setBuildParser(String buildParser) {
        this.buildParser.set(buildParser)
    }

    void setBuildParser(Provider<String> buildParser) {
        this.buildParser.set(buildParser)
    }

    final Property<String> buildTokenManager = project.objects.property(String)

    @Optional
    @Input
    String getBuildTokenManager() {
        return buildTokenManager.getOrNull()
    }

    void setBuildTokenManager(String buildTokenManager) {
        this.buildTokenManager.set(buildTokenManager)
    }

    void setBuildTokenManager(Provider<String> buildTokenManager) {
        this.buildTokenManager.set(buildTokenManager)
    }

    final Property<String> tokenManagerUsesParser = project.objects.property(String)

    @Optional
    @Input
    String getTokenManagerUsesParser() {
        return tokenManagerUsesParser.getOrNull()
    }

    void setTokenManagerUsesParser(String tokenManagerUsesParser) {
        this.tokenManagerUsesParser.set(tokenManagerUsesParser)
    }

    void setTokenManagerUsesParser(Provider<String> tokenManagerUsesParser) {
        this.tokenManagerUsesParser.set(tokenManagerUsesParser)
    }

    final Property<String> sanityCheck = project.objects.property(String)

    @Optional
    @Input
    String getSanityCheck() {
        return sanityCheck.getOrNull()
    }

    void setSanityCheck(String sanityCheck) {
        this.sanityCheck.set(sanityCheck)
    }

    void setSanityCheck(Provider<String> sanityCheck) {
        this.sanityCheck.set(sanityCheck)
    }

    final Property<String> forceLaCheck = project.objects.property(String)

    @Optional
    @Input
    String getForceLaCheck() {
        return forceLaCheck.getOrNull()
    }

    void setForceLaCheck(String forceLaCheck) {
        this.forceLaCheck.set(forceLaCheck)
    }

    void setForceLaCheck(Provider<String> forceLaCheck) {
        this.forceLaCheck.set(forceLaCheck)
    }

    final Property<String> cacheTokens = project.objects.property(String)

    @Optional
    @Input
    String getCacheTokens() {
        return cacheTokens.getOrNull()
    }

    void setCacheTokens(String cacheTokens) {
        this.cacheTokens.set(cacheTokens)
    }

    void setCacheTokens(Provider<String> cacheTokens) {
        this.cacheTokens.set(cacheTokens)
    }

    final Property<String> keepLineColumn = project.objects.property(String)

    @Optional
    @Input
    String getKeepLineColumn() {
        return keepLineColumn.getOrNull()
    }

    void setKeepLineColumn(String keepLineColumn) {
        this.keepLineColumn.set(keepLineColumn)
    }

    void setKeepLineColumn(Provider<String> keepLineColumn) {
        this.keepLineColumn.set(keepLineColumn)
    }

    final Property<String> tokenExtends = project.objects.property(String)

    @Optional
    @Input
    String getTokenExtends() {
        return tokenExtends.getOrNull()
    }

    void setTokenExtends(String tokenExtends) {
        this.tokenExtends.set(tokenExtends)
    }

    void setTokenExtends(Provider<String> tokenExtends) {
        this.tokenExtends.set(tokenExtends)
    }

    final Property<String> tokenFactory = project.objects.property(String)

    @Optional
    @Input
    String getTokenFactory() {
        return tokenFactory.getOrNull()
    }

    void setTokenFactory(String tokenFactory) {
        this.tokenFactory.set(tokenFactory)
    }

    void setTokenFactory(Provider<String> tokenFactory) {
        this.tokenFactory.set(tokenFactory)
    }

    final Property<String> runJJTree = project.objects.property(String)

    @Input
    String getRunJJTree() {
        return runJJTree.get()
    }

    void setRunJJTree(String runJJTree) {
        this.runJJTree.set(runJJTree)
    }

    void setRunJJTree(Provider<String> runJJTree) {
        this.runJJTree.set(runJJTree)
    }

    final Property<String> jjTreeMulti = project.objects.property(String)

    @Optional
    @Input
    String getJjTreeMulti() {
        return jjTreeMulti.getOrNull()
    }

    void setJjTreeMulti(String jjTreeMulti) {
        this.jjTreeMulti.set(jjTreeMulti)
    }

    void setJjTreeMulti(Provider<String> jjTreeMulti) {
        this.jjTreeMulti.set(jjTreeMulti)
    }

    final Property<String> jjTreeNodeDefaultVoid = project.objects.property(String)

    @Optional
    @Input
    String getJjTreeNodeDefaultVoid() {
        return jjTreeNodeDefaultVoid.getOrNull()
    }

    void setJjTreeNodeDefaultVoid(String jjTreeNodeDefaultVoid) {
        this.jjTreeNodeDefaultVoid.set(jjTreeNodeDefaultVoid)
    }

    void setJjTreeNodeDefaultVoid(Provider<String> jjTreeNodeDefaultVoid) {
        this.jjTreeNodeDefaultVoid.set(jjTreeNodeDefaultVoid)
    }

    final Property<String> jjTreeNodeScopeHook = project.objects.property(String)

    @Optional
    @Input
    String getJjTreeNodeScopeHook() {
        return jjTreeNodeScopeHook.getOrNull()
    }

    void setJjTreeNodeScopeHook(String jjTreeNodeScopeHook) {
        this.jjTreeNodeScopeHook.set(jjTreeNodeScopeHook)
    }

    void setJjTreeNodeScopeHook(Provider<String> jjTreeNodeScopeHook) {
        this.jjTreeNodeScopeHook.set(jjTreeNodeScopeHook)
    }

    final Property<String> jjTreeNodeUsesParser = project.objects.property(String)

    @Optional
    @Input
    String getJjTreeNodeUsesParser() {
        return jjTreeNodeUsesParser.getOrNull()
    }

    void setJjTreeNodeUsesParser(String jjTreeNodeUsesParser) {
        this.jjTreeNodeUsesParser.set(jjTreeNodeUsesParser)
    }

    void setJjTreeNodeUsesParser(Provider<String> jjTreeNodeUsesParser) {
        this.jjTreeNodeUsesParser.set(jjTreeNodeUsesParser)
    }

    final Property<String> jjTreeBuildNodeFiles = project.objects.property(String)

    @Optional
    @Input
    String getJjTreeBuildNodeFiles() {
        return jjTreeBuildNodeFiles.getOrNull()
    }

    void setJjTreeBuildNodeFiles(String jjTreeBuildNodeFiles) {
        this.jjTreeBuildNodeFiles.set(jjTreeBuildNodeFiles)
    }

    void setJjTreeBuildNodeFiles(Provider<String> jjTreeBuildNodeFiles) {
        this.jjTreeBuildNodeFiles.set(jjTreeBuildNodeFiles)
    }

    final Property<String> jjTreeTrackTokens = project.objects.property(String)

    @Optional
    @Input
    String getJjTreeTrackTokens() {
        return jjTreeTrackTokens.getOrNull()
    }

    void setJjTreeTrackTokens(String jjTreeTrackTokens) {
        this.jjTreeTrackTokens.set(jjTreeTrackTokens)
    }

    void setJjTreeTrackTokens(Provider<String> jjTreeTrackTokens) {
        this.jjTreeTrackTokens.set(jjTreeTrackTokens)
    }

    final Property<String> jjTreeVisitor = project.objects.property(String)

    @Optional
    @Input
    String getJjTreeVisitor() {
        return jjTreeVisitor.getOrNull()
    }

    void setJjTreeVisitor(String jjTreeVisitor) {
        this.jjTreeVisitor.set(jjTreeVisitor)
    }

    void setJjTreeVisitor(Provider<String> jjTreeVisitor) {
        this.jjTreeVisitor.set(jjTreeVisitor)
    }

    final Property<String> jjTreeStaticParam = project.objects.property(String)

    @Optional
    @Input
    String getJjTreeStaticParam() {
        return jjTreeStaticParam.getOrNull()
    }

    void setJjTreeStaticParam(String jjTreeStaticParam) {
        this.jjTreeStaticParam.set(jjTreeStaticParam)
    }

    void setJjTreeStaticParam(Provider<String> jjTreeStaticParam) {
        this.jjTreeStaticParam.set(jjTreeStaticParam)
    }

    final Property<String> jjTreeNodeClass = project.objects.property(String)

    @Optional
    @Input
    String getJjTreeNodeClass() {
        return jjTreeNodeClass.getOrNull()
    }

    void setJjTreeNodeClass(String jjTreeNodeClass) {
        this.jjTreeNodeClass.set(jjTreeNodeClass)
    }

    void setJjTreeNodeClass(Provider<String> jjTreeNodeClass) {
        this.jjTreeNodeClass.set(jjTreeNodeClass)
    }

    final Property<String> jjTreeNodePrefix = project.objects.property(String)

    @Optional
    @Input
    String getJjTreeNodePrefix() {
        return jjTreeNodePrefix.getOrNull()
    }

    void setJjTreeNodePrefix(String jjTreeNodePrefix) {
        this.jjTreeNodePrefix.set(jjTreeNodePrefix)
    }

    void setJjTreeNodePrefix(Provider<String> jjTreeNodePrefix) {
        this.jjTreeNodePrefix.set(jjTreeNodePrefix)
    }

    final Property<String> jjTreeNodePackage = project.objects.property(String)

    @Optional
    @Input
    String getJjTreeNodePackage() {
        return jjTreeNodePackage.getOrNull()
    }

    void setJjTreeNodePackage(String jjTreeNodePackage) {
        this.jjTreeNodePackage.set(jjTreeNodePackage)
    }

    void setJjTreeNodePackage(Provider<String> jjTreeNodePackage) {
        this.jjTreeNodePackage.set(jjTreeNodePackage)
    }

    final Property<String> jjTreeNodeExtends = project.objects.property(String)

    @Optional
    @Input
    String getJjTreeNodeExtends() {
        return jjTreeNodeExtends.getOrNull()
    }

    void setJjTreeNodeExtends(String jjTreeNodeExtends) {
        this.jjTreeNodeExtends.set(jjTreeNodeExtends)
    }

    void setJjTreeNodeExtends(Provider<String> jjTreeNodeExtends) {
        this.jjTreeNodeExtends.set(jjTreeNodeExtends)
    }

    final Property<String> jjTreeNodeFactory = project.objects.property(String)

    @Optional
    @Input
    String getJjTreeNodeFactory() {
        return jjTreeNodeFactory.getOrNull()
    }

    void setJjTreeNodeFactory(String jjTreeNodeFactory) {
        this.jjTreeNodeFactory.set(jjTreeNodeFactory)
    }

    void setJjTreeNodeFactory(Provider<String> jjTreeNodeFactory) {
        this.jjTreeNodeFactory.set(jjTreeNodeFactory)
    }

    final Property<String> jjTreeVisitorDataType = project.objects.property(String)

    @Optional
    @Input
    String getJjTreeVisitorDataType() {
        return jjTreeVisitorDataType.getOrNull()
    }

    void setJjTreeVisitorDataType(String jjTreeVisitorDataType) {
        this.jjTreeVisitorDataType.set(jjTreeVisitorDataType)
    }

    void setJjTreeVisitorDataType(Provider<String> jjTreeVisitorDataType) {
        this.jjTreeVisitorDataType.set(jjTreeVisitorDataType)
    }

    final Property<String> jjTreeVisitorReturnType = project.objects.property(String)

    @Optional
    @Input
    String getJjTreeVisitorReturnType() {
        return jjTreeVisitorReturnType.getOrNull()
    }

    void setJjTreeVisitorReturnType(String jjTreeVisitorReturnType) {
        this.jjTreeVisitorReturnType.set(jjTreeVisitorReturnType)
    }

    void setJjTreeVisitorReturnType(Provider<String> jjTreeVisitorReturnType) {
        this.jjTreeVisitorReturnType.set(jjTreeVisitorReturnType)
    }

    final Property<String> jjTreeVisitorException = project.objects.property(String)

    @Optional
    @Input
    String getJjTreeVisitorException() {
        return jjTreeVisitorException.getOrNull()
    }

    void setJjTreeVisitorException(String jjTreeVisitorException) {
        this.jjTreeVisitorException.set(jjTreeVisitorException)
    }

    void setJjTreeVisitorException(Provider<String> jjTreeVisitorException) {
        this.jjTreeVisitorException.set(jjTreeVisitorException)
    }

    final ListProperty<String> jjTreeArgs = project.objects.listProperty(String)

    @Optional
    @Input
    List<String> getJJTreeArgs() {
        return javaCCArgs.getOrNull()
    }

    void setJJTreeArgs(List<String> args) {
        this.javaCCArgs.set(args)
    }

    void setJJTreeArgs(Provider<List<String>> args) {
        this.javaCCArgs.set(args)
    }

    /**
     * Java fork options for the Java task.
     */
    JavaForkOptions forkOptions

    /**
     * Task aktion for code generation
     */
    @TaskAction
    void run() {
        File javaCCInputFile = getInputFile()
        File srcOut = getPackageName() ? new File(getOutputDir(), getPackageName().replace('.', '/')) : getOutputDir()

        // if jjtree is configured ...
        if(getRunJJTree().toBoolean()) {
            List<String> jtreeParams = []

            if(getJjTreeBuildNodeFiles() != null) {
                jtreeParams.add("-BUILD_NODE_FILES=${getJjTreeBuildNodeFiles().toBoolean()}".toString())
            }
            if(getJjTreeMulti() != null) {
                jtreeParams.add("-MULTI=${getJjTreeMulti().toBoolean()}".toString())
            }
            if(getJjTreeNodeDefaultVoid() != null) {
                jtreeParams.add("-NODE_DEFAULT_VOID=${getJjTreeNodeDefaultVoid().toBoolean()}".toString())
            }
            if(getJjTreeNodeDefaultVoid() != null) {
                jtreeParams.add("-NODE_SCOPE_HOOK=${getJjTreeNodeScopeHook().toBoolean()}".toString())
            }
            if(getJjTreeNodeDefaultVoid() != null) {
                jtreeParams.add("-NODE_USES_PARSER=${getJjTreeNodeUsesParser().toBoolean()}".toString())
            }
            if(getJjTreeNodeDefaultVoid()) {
                jtreeParams.add("-TRACK_TOKENS=${getJjTreeTrackTokens().toBoolean()}".toString())
            }
            if(getJjTreeVisitor() != null) {
                jtreeParams.add("-VISITOR=${getJjTreeVisitor().toBoolean()}".toString())
            }
            if(getJjTreeStaticParam() != null) {
                jtreeParams.add("-STATIC=${getJjTreeStaticParam().toBoolean()}".toString())
            }

            if(getJjTreeNodeClass() != null) {
                jtreeParams.add("-NODE_CLASS=${getJjTreeNodeClass()}".toString())
            }
            if(getJjTreeNodePrefix() != null) {
                jtreeParams.add("-NODE_PREFIX=${getJjTreeNodePrefix()}".toString())
            }
            if(getJjTreeNodeExtends() != null) {
                jtreeParams.add("-NODE_EXTENDS=${getJjTreeNodeExtends()}".toString())
            }
            if(getJjTreeNodePackage() != null) {
                jtreeParams.add("-NODE_PACKAGE=${getJjTreeNodePackage()}".toString())
            }
            if(getJjTreeNodeFactory() != null) {
                jtreeParams.add("-NODE_FACTORY=${getJjTreeNodeFactory()}".toString())
            }
            if(getJjTreeVisitorDataType() != null) {
                jtreeParams.add("-VISITOR_DATA_TYPE=${getJjTreeVisitorDataType()}".toString())
            }
            if(getJjTreeVisitorReturnType() != null) {
                jtreeParams.add("-VISITOR_RETURN_TYPE=${getJjTreeVisitorReturnType()}".toString())
            }
            if(getJjTreeVisitorException() != null) {
                jtreeParams.add("-VISITOR_EXCEPTION=${getJjTreeVisitorException()}".toString())
            }

            // jjtree run ...
            List<String> jjTreeArgs = getExecCommands(jtreeParams, srcOut, getJJTreeArgs(), getInputFile())
            prepareExec(JJTREE_MAIN_CLASS_NAME, jjTreeArgs).build().start().waitForFinish().assertNormalExitValue()

            String fileName = getInputFile().name.replaceFirst(~/\.[^\.]+$/, '.jj')
            javaCCInputFile = new File(srcOut, fileName)
        }

        List<String> params = []

        if(getStaticParam() != null) {
            params.add("-STATIC=${getStaticParam().toBoolean()}".toString())
        }
        if(getSupportClassVisibilityPublic() != null) {
            params.add("-SUPPORT_CLASS_VISIBILITY_PUBLIC=${getSupportClassVisibilityPublic().toBoolean()}".toString())
        }
        if(getDebugParser() != null) {
            params.add("-DEBUG_PARSER=${getDebugParser().toBoolean()}".toString())
        }
        if(getDebugLookahead() != null) {
            params.add("-DEBUG_LOOKAHEAD=${getDebugLookahead().toBoolean()}".toString())
        }
        if(getDebugTokenManager() != null) {
            params.add("-DEBUG_TOKEN_MANAGER=${getDebugTokenManager().toBoolean()}".toString())
        }
        if(getErrorReporting() != null) {
            params.add("-ERROR_REPORTING=${getErrorReporting().toBoolean()}".toString())
        }
        if(getJavaUnicodeEscape() != null) {
            params.add("-JAVA_UNICODE_ESCAPE=${getJavaUnicodeEscape().toBoolean()}".toString())
        }
        if(getUnicodeInput() != null) {
            params.add("-UNICODE_INPUT=${getUnicodeInput().toBoolean()}".toString())
        }
        if(getIgnoreCase() != null) {
            params.add("-IGNORE_CASE=${getIgnoreCase().toBoolean()}".toString())
        }
        if(getCommonTokenAction() != null) {
            params.add("-COMMON_TOKEN_ACTION=${getCommonTokenAction().toBoolean()}".toString())
        }
        if(getUserTokenManager() != null) {
            params.add("-USER_TOKEN_MANAGER=${getUserTokenManager().toBoolean()}".toString())
        }
        if(getUserCharStream() != null) {
            params.add("-USER_CHAR_STREAM=${getUserCharStream().toBoolean()}".toString())
        }
        if(getBuildParser() != null) {
            params.add("-BUILD_PARSER=${getBuildParser().toBoolean()}".toString())
        }
        if(getBuildTokenManager() != null) {
            params.add("-BUILD_TOKEN_MANAGER=${getBuildTokenManager().toBoolean()}".toString())
        }
        if(getTokenManagerUsesParser() != null) {
            params.add("-TOKEN_MANAGER_USES_PARSER=${getTokenManagerUsesParser().toBoolean()}".toString())
        }
        if(getSanityCheck() != null) {
            params.add("-SANITY_CHECK=${getSanityCheck().toBoolean()}".toString())
        }
        if(getForceLaCheck() != null) {
            params.add("-FORCE_LA_CHECK=${getForceLaCheck().toBoolean()}".toString())
        }
        if(getCacheTokens() != null) {
            params.add("-CACHE_TOKENS=${getCacheTokens().toBoolean()}".toString())
        }
        if(getKeepLineColumn() != null) {
            params.add("-KEEP_LINE_COLUMN=${getKeepLineColumn().toBoolean()}".toString())
        }

        if(getTokenExtends() != null) {
            try {
                params.add("-CHOICE_AMBIGUITY_CHECK=${getChoiceAmbiguityCheck().intValue()}".toString())
            }catch(Exception ex) {
                project.logger.warn('{} is not an integer value. CHOICE_AMBIGUITY_CHECK is not set. ({})', getChoiceAmbiguityCheck(), ex.getMessage())
            }
        }
        if(getTokenExtends() != null) {
            try {
                params.add("-LOOKAHEAD=${getLookahead().intValue()}".toString())
            }catch(Exception ex) {
                project.logger.warn('{} is not an integer value. LOOKAHEAD is not set. ({})', getLookahead(), ex.getMessage())
            }
        }
        if(getTokenExtends() != null) {
            try {
                params.add("-OTHER_AMBIGUITY_CHECK=${getOtherAmbiguityCheck().intValue()}".toString())
            }catch(Exception ex) {
                project.logger.warn('{} is not an integer value. OTHER_AMBIGUITY_CHECK is not set. ({})', getOtherAmbiguityCheck(), ex.getMessage())
            }
        }

        if(getTokenExtends() != null) {
            params.add("-TOKEN_EXTENDS=${getTokenExtends()}".toString())
        }
        if(getTokenFactory() != null) {
            params.add("-TOKEN_FACTORY=${getTokenFactory()}".toString())
        }

        // javacc run ...
        List<String> javaCCArgs = getExecCommands(params, srcOut, getJavaCCArgs(), javaCCInputFile)
        prepareExec(JAVACC_MAIN_CLASS_NAME, javaCCArgs).build().start().waitForFinish().assertNormalExitValue()
    }

    /**
     * Calculates all arguments, based on properties 'props', jdkVersion, outputDir and input file
     *
     * @param props      Properties with all parameter key value pairs
     * @param jdkVersion Version of JDK
     * @param outputDir  Output directory
     * @param inputFile  Inupt file
     * @return           List of real parameters
     */
    List<String> getExecCommands(List<String> params, File outputDir, List<String> args, File inputFile) {
        List<String> pArgs = []
        pArgs.addAll(params)

        // If jdk version is set, add this to the parameter list.
        if(getJdkVersion()) {
            pArgs.add("-JDK_VERSION=${jdkVersion}".toString())
        }
        // output dir
        pArgs.add("-OUTPUT_DIRECTORY=${outputDir.absolutePath}".toString())
        if(args) {
            pArgs.addAll(args)
        }
        pArgs.add(inputFile.absolutePath)

        return pArgs
    }

    /**
     * Prepares the JavaExecHandlerBuilder for the task.
     *
     * @return JavaExecHandleBuilder
     */
    JavaExecHandleBuilder prepareExec(String main, List<String> args) {
        // create javaexec
        JavaExecHandleBuilder javaExec = new JavaExecHandleBuilder(getFileResolver())

        // copy jvm fork options
        getForkOptions().copyTo(javaExec)

        //necessary files for runner
        FileCollection runnerConfiguration = getProject().getConfigurations().getAt(JavaCCExtension.JAVACC_CONFIGURATION_NAME)

        return javaExec
                .systemProperty("project.home", project.projectDir.getAbsolutePath())
                .setClasspath(runnerConfiguration)
                .setMain(main)
                .setArgs(args)
    }

    /**
     * Set Java fork options.
     *
     * @return JavaForkOptions
     */
    JavaForkOptions getForkOptions() {
        if (forkOptions == null) {
            forkOptions = new DefaultJavaForkOptions(getFileResolver())
        }

        return forkOptions
    }

    @Inject
    protected FileResolver getFileResolver() {
        throw new UnsupportedOperationException()
    }
}
