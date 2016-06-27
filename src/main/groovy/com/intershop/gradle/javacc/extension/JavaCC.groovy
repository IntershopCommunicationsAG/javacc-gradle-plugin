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

import groovy.util.logging.Slf4j
import org.gradle.api.Named
import org.gradle.util.ConfigureUtil
import org.gradle.util.GUtil

/**
 * This is the container for all JavaCC related configuration details.
 */
@Slf4j
class JavaCC implements Named {

    /**
     * Constructor
     * @param name
     */
    JavaCC(String name) {
        this.name = name
    }

    /**
     * Output dir
     */
    File outputDir

    /**
     * Package name for sources
     */
    String packageName

    /**
     * Input file
     */
    File inputFile

    /**
     * Name of this configuration
     */
    String name

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

    String jdkVersion

    /**
     * Calculate the task name
     * @return
     */
    String getTaskName() {
        return "javacc" + GUtil.toCamelCase(name);
    }

    /**
     * Parameter properties for javaCC
     *
     * @return properties
     */
    Properties getParameters() {
        Properties props = new Properties()

        if(getStaticParam()) {
            props.put('STATIC', getStaticParam().toBoolean())
        }
        if(getSupportClassVisibilityPublic()) {
            props.put('SUPPORT_CLASS_VISIBILITY_PUBLIC', getSupportClassVisibilityPublic().toBoolean())
        }
        if(getDebugParser()) {
            props.put('DEBUG_PARSER', getDebugParser().toBoolean())
        }
        if(getDebugLookahead()) {
            props.put('DEBUG_LOOKAHEAD', getDebugLookahead().toBoolean())
        }
        if(getDebugTokenManager()) {
            props.put('DEBUG_TOKEN_MANAGER', getDebugTokenManager().toBoolean())
        }
        if(getErrorReporting()) {
            props.put('ERROR_REPORTING', getErrorReporting().toBoolean())
        }
        if(getJavaUnicodeEscape()) {
            props.put('JAVA_UNICODE_ESCAPE', getJavaUnicodeEscape().toBoolean())
        }
        if(getUnicodeInput()) {
            props.put('UNICODE_INPUT', getUnicodeInput().toBoolean())
        }
        if(getIgnoreCase()) {
            props.put('IGNORE_CASE', getIgnoreCase().toBoolean())
        }
        if(getCommonTokenAction()) {
            props.put('COMMON_TOKEN_ACTION', getCommonTokenAction().toBoolean())
        }
        if(getUserTokenManager()) {
            props.put('USER_TOKEN_MANAGER', getUserTokenManager().toBoolean())
        }
        if(getUserCharStream()) {
            props.put('USER_CHAR_STREAM', getUserCharStream().toBoolean())
        }
        if(getBuildParser()) {
            props.put('BUILD_PARSER', getBuildParser().toBoolean())
        }
        if(getBuildTokenManager()) {
            props.put('BUILD_TOKEN_MANAGER', getBuildTokenManager().toBoolean())
        }
        if(getTokenManagerUsesParser()) {
            props.put('TOKEN_MANAGER_USES_PARSER', getTokenManagerUsesParser())
        }
        if(getSanityCheck()) {
            props.put('SANITY_CHECK', getSanityCheck().toBoolean())
        }
        if(getForceLaCheck()) {
            props.put('FORCE_LA_CHECK', getForceLaCheck().toBoolean())
        }
        if(getCacheTokens()) {
            props.put('CACHE_TOKENS', getCacheTokens().toBoolean())
        }
        if(getKeepLineColumn()) {
            props.put('KEEP_LINE_COLUMN', getKeepLineColumn().toBoolean())
        }

        if(getTokenExtends()) {
            try {
                props.put('CHOICE_AMBIGUITY_CHECK', getChoiceAmbiguityCheck().toInteger())
            }catch(Exception ex) {
                log.warn('{} is not an integer value. CHOICE_AMBIGUITY_CHECK is not set.', getChoiceAmbiguityCheck())
            }
        }
        if(getTokenExtends()) {
            try {
                props.put('LOOKAHEAD', getLookahead().toInteger())
            }catch(Exception ex) {
                log.warn('{} is not an integer value. LOOKAHEAD is not set.', getLookahead())
            }
        }
        if(getTokenExtends()) {
            try {
                props.put('OTHER_AMBIGUITY_CHECK', getOtherAmbiguityCheck().toInteger())
            }catch(Exception ex) {
                log.warn('{} is not an integer value. OTHER_AMBIGUITY_CHECK is not set.', getOtherAmbiguityCheck())
            }
        }

        if(getTokenExtends()) {
            props.put('TOKEN_EXTENDS', getTokenExtends())
        }
        if(getTokenFactory()) {
            props.put('TOKEN_FACTORY', getTokenFactory())
        }


        return props
    }

    /**
     * Additional ars for javacc
     */
    def args = []

    void arg(String parameter) {
        args.add(parameter)
    }

    /**
     * JJTree configuration
     */
    JJTree jjtree

    /**
     * JJTree configuration closure
     *
     * @param closure
     * @return
     */
    JJTree jjtree(Closure closure) {
        jjtree = new JJTree()
        ConfigureUtil.configure(closure, jjtree)
    }

    /**
     * Name of the source set for generated Java code
     * default value is 'main'
     */
    String sourceSetName

    String getSourceSetName() {
        if(! this.sourceSetName) {
            return JavaCCExtension.DEFAULT_SOURCESET_NAME
        } else {
            return this.sourceSetName
        }
    }
}
