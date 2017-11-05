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
        return outputDir.get()
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
        return packageName.get()
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
        return inputFile.get()
    }

    void setInputFile(File inputFile) {
        this.inputFile.set(inputFile)
    }

    private final Property<String> jdkVersion

    Provider<String> getJdkVersionProvider() {
        return jdkVersion
    }

    String getJdkVersion() {
        return jdkVersion.get()
    }

    void setJdkVersion(String jdkVersion) {
        this.jdkVersion.set(jdkVersion)
    }

    /**
     * Parameter properties for javaCC
     */
    private final Property<Map<String, String>> parameters

    Provider<Map<String, String>> getParametersProvider() {
        Map<String, String> params = [:]

        if(getStaticParam()) {
            params.put('STATIC', getStaticParam().toBoolean().toString())
        }
        if(getSupportClassVisibilityPublic()) {
            params.put('SUPPORT_CLASS_VISIBILITY_PUBLIC', getSupportClassVisibilityPublic().toBoolean().toString())
        }
        if(getDebugParser()) {
            params.put('DEBUG_PARSER', getDebugParser().toBoolean().toBoolean().toString())
        }
        if(getDebugLookahead()) {
            params.put('DEBUG_LOOKAHEAD', getDebugLookahead().toBoolean().toString())
        }
        if(getDebugTokenManager()) {
            params.put('DEBUG_TOKEN_MANAGER', getDebugTokenManager().toBoolean().toString())
        }
        if(getErrorReporting()) {
            params.put('ERROR_REPORTING', getErrorReporting().toBoolean().toString())
        }
        if(getJavaUnicodeEscape()) {
            params.put('JAVA_UNICODE_ESCAPE', getJavaUnicodeEscape().toBoolean().toString())
        }
        if(getUnicodeInput()) {
            params.put('UNICODE_INPUT', getUnicodeInput().toBoolean().toString())
        }
        if(getIgnoreCase()) {
            params.put('IGNORE_CASE', getIgnoreCase().toBoolean().toString())
        }
        if(getCommonTokenAction()) {
            params.put('COMMON_TOKEN_ACTION', getCommonTokenAction().toBoolean().toString())
        }
        if(getUserTokenManager()) {
            params.put('USER_TOKEN_MANAGER', getUserTokenManager().toBoolean().toString())
        }
        if(getUserCharStream()) {
            params.put('USER_CHAR_STREAM', getUserCharStream().toBoolean().toString())
        }
        if(getBuildParser()) {
            params.put('BUILD_PARSER', getBuildParser().toBoolean().toString())
        }
        if(getBuildTokenManager()) {
            params.put('BUILD_TOKEN_MANAGER', getBuildTokenManager().toBoolean().toString())
        }
        if(getTokenManagerUsesParser()) {
            params.put('TOKEN_MANAGER_USES_PARSER', getTokenManagerUsesParser())
        }
        if(getSanityCheck()) {
            params.put('SANITY_CHECK', getSanityCheck().toBoolean().toString())
        }
        if(getForceLaCheck()) {
            params.put('FORCE_LA_CHECK', getForceLaCheck().toBoolean().toString())
        }
        if(getCacheTokens()) {
            params.put('CACHE_TOKENS', getCacheTokens().toBoolean().toString())
        }
        if(getKeepLineColumn()) {
            params.put('KEEP_LINE_COLUMN', getKeepLineColumn().toBoolean().toString())
        }

        if(getTokenExtends()) {
            try {
                params.put('CHOICE_AMBIGUITY_CHECK', getChoiceAmbiguityCheck().toInteger().toString())
            }catch(Exception ex) {
                log.warn('{} is not an integer value. CHOICE_AMBIGUITY_CHECK is not set. ({})', getChoiceAmbiguityCheck(), ex.getMessage())
            }
        }
        if(getTokenExtends()) {
            try {
                params.put('LOOKAHEAD', getLookahead().toInteger().toString())
            }catch(Exception ex) {
                log.warn('{} is not an integer value. LOOKAHEAD is not set. ({})', getLookahead(), ex.getMessage())
            }
        }
        if(getTokenExtends()) {
            try {
                params.put('OTHER_AMBIGUITY_CHECK', getOtherAmbiguityCheck().toInteger().toString())
            }catch(Exception ex) {
                log.warn('{} is not an integer value. OTHER_AMBIGUITY_CHECK is not set. ({})', getOtherAmbiguityCheck(), ex.getMessage())
            }
        }

        if(getTokenExtends()) {
            params.put('TOKEN_EXTENDS', getTokenExtends())
        }
        if(getTokenFactory()) {
            params.put('TOKEN_FACTORY', getTokenFactory())
        }

        parameters.set(params)
        return parameters
    }

    Map<String, String> getParameters() {

        return parameters.get()
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
        return sourceSetName.get()
    }

    void setSourceSetName(String sourceSetName) {
        this.sourceSetName.set(sourceSetName)
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
        parameters = project.objects.property(Map)
        sourceSetName = project.objects.property(String)
        argsProvider = project.objects.property(List)

        setSourceSetName(JavaCCExtension.DEFAULT_SOURCESET_NAME)

        outputDir.set(project.getLayout().getBuildDirectory().
                dir("${JavaCCExtension.CODEGEN_DEFAULT_OUTPUTPATH}/${name.replace(' ', '_')}").get().asFile)

    }

    /**
     * Parameters, see javacc documentation
     * All parameters are strings, so that it is possible to
     * identify if the setting is configured.
     * The configuration of a parameter will override
     * existing inline configuration.
     *
     * This parameters are interpreted as integer.
     */
    String lookahead
    String choiceAmbiguityCheck
    String otherAmbiguityCheck

    /**
     * This parameters are interpreted as boolean.
     */
    String staticParam
    String supportClassVisibilityPublic
    String debugParser
    String debugLookahead
    String debugTokenManager
    String errorReporting
    String javaUnicodeEscape
    String unicodeInput
    String ignoreCase
    String commonTokenAction
    String userTokenManager
    String userCharStream
    String buildParser
    String buildTokenManager
    String tokenManagerUsesParser
    String sanityCheck
    String forceLaCheck
    String cacheTokens
    String keepLineColumn

    /**
     * This parameters are interpreted as string.
     */
    String tokenExtends
    String tokenFactory


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
        return argsProvider.get()
    }

    void setArgs(List<String> args) {
        this.argsProvider.set(args)
    }

    void args(String paramater) {
        argsProvider.get().add(paramater)
    }

    /**
     * JJTree configuration closure
     *
     * @param closure
     * @return
     */
    JJTree jjtree(Closure closure) {
        ConfigureUtil.configure(closure, jjtree)
    }

    JJTree getJJTree() {
        return jjtree
    }
}
