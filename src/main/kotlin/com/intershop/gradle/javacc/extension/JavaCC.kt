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

import com.intershop.gradle.javacc.extension.JavaCCExtension.Companion.CODEGEN_OUTPUTPATH
import com.intershop.gradle.javacc.utils.getValue
import com.intershop.gradle.javacc.utils.setValue
import groovy.lang.Closure
import org.gradle.api.Action
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.ProjectLayout
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.SourceSet
import org.gradle.util.ConfigureUtil
import java.io.File
import javax.inject.Inject

/**
 * Configuration container for special JavaCC
 * code generation.
 */
open class JavaCC @Inject constructor(objectFactory: ObjectFactory,
                                          projectLayout: ProjectLayout, @Internal val name: String) {

    private val outputDirProperty: DirectoryProperty = objectFactory.directoryProperty()
    private val inputFileProperty: RegularFileProperty = objectFactory.fileProperty()
    private val sourceSetNameProperty: Property<String> = objectFactory.property(String::class.java)

    private val packageNameProperty: Property<String> = objectFactory.property(String::class.java)
    private val jdkVersionProperty: Property<String> = objectFactory.property(String::class.java)

    // property is a string, because there are problems with Integer and Int for the property
    private val lookaheadProperty: Property<String> = objectFactory.property(String::class.java)
    // property is a string, because there are problems with Integer and Int for the property
    private val choiceAmbiguityCheckProperty: Property<String> = objectFactory.property(String::class.java)
    // property is a string, because there are problems with Integer and Int for the property
    private val otherAmbiguityCheckProperty: Property<String> = objectFactory.property(String::class.java)

    // these properties are interpreted as Boolean, if a value is set
    private val staticParamProperty: Property<String> = objectFactory.property(String::class.java)
    private val supportClassVisibilityPublicProperty: Property<String> = objectFactory.property(String::class.java)
    private val debugParserProperty: Property<String> = objectFactory.property(String::class.java)
    private val debugLookaheadProperty: Property<String> = objectFactory.property(String::class.java)
    private val debugTokenManagerProperty: Property<String> = objectFactory.property(String::class.java)
    private val errorReportingProperty: Property<String> = objectFactory.property(String::class.java)
    private val javaUnicodeEscapeProperty: Property<String> = objectFactory.property(String::class.java)
    private val unicodeInputProperty: Property<String> = objectFactory.property(String::class.java)
    private val ignoreCaseProperty: Property<String> = objectFactory.property(String::class.java)
    private val commonTokenActionProperty: Property<String> = objectFactory.property(String::class.java)
    private val userTokenManagerProperty: Property<String> = objectFactory.property(String::class.java)
    private val userCharStreamProperty: Property<String> = objectFactory.property(String::class.java)
    private val buildParserProperty: Property<String> = objectFactory.property(String::class.java)
    private val buildTokenManagerProperty: Property<String> = objectFactory.property(String::class.java)
    private val tokenManagerUsesParserProperty: Property<String> = objectFactory.property(String::class.java)
    private val sanityCheckProperty: Property<String> = objectFactory.property(String::class.java)
    private val forceLaCheckProperty: Property<String> = objectFactory.property(String::class.java)
    private val cacheTokensProperty: Property<String> = objectFactory.property(String::class.java)
    private val keepLineColumnProperty: Property<String> = objectFactory.property(String::class.java)
    private val tokenExtendsProperty: Property<String> = objectFactory.property(String::class.java)
    private val tokenFactoryProperty: Property<String> = objectFactory.property(String::class.java)

    private val argumentsProperty : ListProperty<String> = objectFactory.listProperty(String::class.java)

    init {
        outputDirProperty.set(
            projectLayout.buildDirectory.dir("${CODEGEN_OUTPUTPATH}/${name.replace(' ', '_')}"))
        sourceSetNameProperty.set(SourceSet.MAIN_SOURCE_SET_NAME)
    }

    /**
     * Tree building configuration for JavaCC parser.
     *
     * @property jjtree
     */
    val jjtree : JJTree = objectFactory.newInstance(JJTree::class.java)

    /**
     * Provider for outputDir property.
     */
    val outputDirProvider: Provider<Directory>
        get() = outputDirProperty

    /**
     * Output dir for JavaCC code generation.
     *
     * @property outputDir
     */
    var outputDir: File
        get() =  outputDirProperty.get().asFile
        set(value) = outputDirProperty.set(value)

    /**
     * Provider for input file property.
     */
    val inputFileProvider: Provider<RegularFile>
        get() = inputFileProperty

    /**
     * Input file property for JavaCC code generation.
     *
     * @property inputFile
     */
    var inputFile: File
        get() = inputFileProperty.get().asFile
        set(value) = inputFileProperty.set(value)

    /**
     * Generated code will be added to a SourceSet.
     * This is the provider for the SourceSet name.
     */
    val sourceSetNameProvider: Provider<String>
        get() = sourceSetNameProperty

    /**
     * Generated code will be added to a SourceSet.
     * This is the SourceSet name property.
     *
     * @property sourceSetName
     */
    var sourceSetName by sourceSetNameProperty

    /**
     * This is the provider for the package name
     * of the generated code.
     */
    val packageNameProvider: Provider<String>
        get() = packageNameProperty

    /**
     * This is the package name property of the
     * generated code.
     *
     * @property packageName
     */
    var packageName by packageNameProperty

    /**
     * This is the provider for the JDK version configuration
     * of the JavaCC code generator.
     */
    val jdkVersionProvider: Provider<String>
        get() = jdkVersionProperty

    /**
     * JDK version property of the JavaCC code
     * generator.
     *
     * @property jdkVersion
     */
    var jdkVersion by jdkVersionProperty

    /**
     * LOOKAHEAD: The number of tokens to look ahead before making a decision at a choice point during parsing.
     * The smaller this number, the faster the parser. This number may be overridden for specific productions
     * within the grammar as described later.
     * This is provider for the LOOKAHEAD property.
     */
    val lookaheadProvider: Provider<String>
        get() = lookaheadProperty

    /**
     * LOOKAHEAD: The number of tokens to look ahead before making a decision at a choice point during parsing.
     * The smaller this number, the faster the parser. This number may be overridden for specific productions
     * within the grammar as described later.
     * This is the lookahead property of the parser configuration.
     *
     * @property lookahead
     */
    var lookahead: Int?
        get() = lookaheadProperty.orNull?.toInt()
        set(value) = lookaheadProperty.set(value.toString())

    /**
     * CHOICE_AMBIGUITY_CHECK: This is the number of tokens considered in
     * checking choices of the form "A \| B \| ..." for ambiguity.
     * This is the provider for choiceAmbiguityCheck property.
     */
    val choiceAmbiguityCheckProvider: Provider<String>
        get() = choiceAmbiguityCheckProperty

    /**
     * CHOICE_AMBIGUITY_CHECK: This is the number of tokens considered in
     * checking choices of the form "A \| B \| ..." for ambiguity.
     * This is the property for choiceAmbiguityCheck of the parser configuration.
     *
     * @property choiceAmbiguityCheck
     */
    var choiceAmbiguityCheck: Int?
        get() = choiceAmbiguityCheckProperty.orNull?.toInt()
        set(value) = choiceAmbiguityCheckProperty.set(value.toString())

    /**
     * OTHER_AMBIGUITY_CHECK: This is the number of tokens considered in checking all
     * other kinds of choices (i.e., of the forms "(A)*", "(A)+", and "(A)?") for ambiguity.
     * This is the provider for otherAmbiguityCheck property.
     */
    val otherAmbiguityCheckProvider: Provider<String>
        get() = otherAmbiguityCheckProperty

    /**
     * OTHER_AMBIGUITY_CHECK: This is the number of tokens considered in checking all
     * other kinds of choices (i.e., of the forms "(A)*", "(A)+", and "(A)?") for ambiguity.
     * This is the property otherAmbiguityCheck of the parser configuration.
     *
     * @property otherAmbiguityCheck
     */
    var otherAmbiguityCheck: Int?
        get() = otherAmbiguityCheckProperty.orNull?.toInt()
        set(value) = otherAmbiguityCheckProperty.set(value.toString())

    /**
     * STATIC: If true, all methods and class variables are
     * specified as static in the generated parser and token manager.
     * This is the provider for staticParam property.
     */
    val staticParamProvider: Provider<String>
        get() = staticParamProperty

    /**
     * STATIC: If true, all methods and class variables are
     * specified as static in the generated parser and token manager.
     * This is the property staticParam of the parser configuration.
     *
     * @property staticParam
     */
    var staticParam by staticParamProperty

    /**
     * SUPPORT_CLASS_VISIBILITY_PUBLIC: The default action is to generate
     * support classes (such as Token.java, ParseException.java etc)
     * with Public visibility.
     * This is the provider for supportClassVisibilityPublic property.
     */
    val supportClassVisibilityPublicProvider: Provider<String>
        get() = supportClassVisibilityPublicProperty

    /**
     * SUPPORT_CLASS_VISIBILITY_PUBLIC: The default action is to generate
     * support classes (such as Token.java, ParseException.java etc)
     * with Public visibility.
     * This is the property supportClassVisibilityPublic of the parser configuration.
     *
     * @property supportClassVisibilityPublic
     */
    var supportClassVisibilityPublic by supportClassVisibilityPublicProperty

    /**
     * DEBUG_PARSER: This option is used to obtain debugging
     * information from the generated parser.
     * This is the provider for debugParser property.
     */
    val debugParserProvider: Provider<String>
        get() = debugParserProperty

    /**
     * DEBUG_PARSER: This option is used to obtain debugging
     * information from the generated parser.
     * This is the property debugParser of the parser configuration.
     *
     * @property debugParser
     */
    var debugParser by debugParserProperty

    /**
     * DEBUG_LOOKAHEAD: Setting this option to true causes the parser
     * to generate all the tracing information it does when the option
     * DEBUG_PARSER is true.
     * This is the provider for debugLookahead property.
     */
    val debugLookaheadProvider: Provider<String>
        get() = debugLookaheadProperty

    /**
     * DEBUG_LOOKAHEAD: Setting this option to true causes the parser
     * to generate all the tracing information it does when the option
     * DEBUG_PARSER is true.
     * This is the property debugLookahead of the parser configuration.
     *
     * @property debugLookahead
     */
    var debugLookahead by debugLookaheadProperty

    /**
     * DEBUG_TOKEN_MANAGER: This option is used to obtain debugging
     * information from the generated token manager.
     * This is the provider for debugTokenManager property.
     */
    val debugTokenManagerProvider: Provider<String>
        get() = debugTokenManagerProperty

    /**
     * DEBUG_TOKEN_MANAGER: This option is used to obtain debugging
     * information from the generated token manager.
     * This is the property debugTokenManager of the parser configuration.
     *
     * @property debugTokenManager
     */
    var debugTokenManager by debugTokenManagerProperty

    /**
     * ERROR_REPORTING: Setting it to false causes errors due to
     * parse errors to be reported in somewhat less detail.
     * This is the provider for errorReporting property.
     */
    val errorReportingProvider: Provider<String>
        get() = errorReportingProperty

    /**
     * ERROR_REPORTING: Setting it to false causes errors due to
     * parse errors to be reported in somewhat less detail.
     * This is the property errorReporting of the parser configuration.
     *
     * @property errorReporting
     */
    var errorReporting by errorReportingProperty

    /**
     * JAVA_UNICODE_ESCAPE: When set to true, the generated parser
     * uses an input stream object that processes Java Unicode
     * escapes (\u...) before sending characters to the token manager.
     * This is the provider for javaUnicodeEscape property.
     */
    val javaUnicodeEscapeProvider: Provider<String>
        get() = javaUnicodeEscapeProperty

    /**
     * JAVA_UNICODE_ESCAPE: When set to true, the generated parser
     * uses an input stream object that processes Java Unicode
     * escapes (\u...) before sending characters to the token manager.
     * This is the property javaUnicodeEscape of the parser configuration.
     *
     * @property javaUnicodeEscape
     */
    var javaUnicodeEscape by javaUnicodeEscapeProperty


    /**
     * UNICODE_INPUT: When set to true, the generated parser uses
     * an input stream object that reads Unicode files.
     * This is the provider for unicodeInput property.
     */
    val unicodeInputProvider: Provider<String>
        get() = unicodeInputProperty

    /**
     * UNICODE_INPUT: When set to true, the generated parser uses
     * an input stream object that reads Unicode files.
     * This is the property unicodeInput of the parser configuration.
     *
     * @property unicodeInput
     */
    var unicodeInput by unicodeInputProperty


    /**
     * IGNORE_CASE: Setting this option to true causes the generate
     * token manager to ignore case in the token specifications and
     * the input files.
     * This is the provider for ignoreCase property.
     */
    val ignoreCaseProvider: Provider<String>
        get() = ignoreCaseProperty

    /**
     * IGNORE_CASE: Setting this option to true causes the generate
     * token manager to ignore case in the token specifications and
     * the input files.
     * This is the property ignoreCase of the parser configuration.
     *
     * @property ignoreCase
     */
    var ignoreCase by ignoreCaseProperty


    /**
     * COMMON_TOKEN_ACTION: When set to true, every call to the token
     * manager's method "getNextToken" will cause a call to a used
     * defined method "CommonTokenAction" after the token has been
     * scanned in by the token manager.
     * This is the provider for commonTokenAction property.
     */
    val commonTokenActionProvider: Provider<String>
        get() = commonTokenActionProperty

    /**
     * COMMON_TOKEN_ACTION: When set to true, every call to the token
     * manager's method "getNextToken" will cause a call to a used
     * defined method "CommonTokenAction" after the token has been
     * scanned in by the token manager.
     * This is the property commonTokenAction of the parser configuration.
     *
     * @property commonTokenAction
     */
    var commonTokenAction by commonTokenActionProperty

    /**
     * USER_TOKEN_MANAGER: The default action is to generate
     * a token manager that works on the specified grammar tokens.
     * This is the provider for userTokenManager property.
     */
    val userTokenManagerProvider: Provider<String>
        get() = userTokenManagerProperty

    /**
     * USER_TOKEN_MANAGER: The default action is to generate
     * a token manager that works on the specified grammar tokens.
     * This is the property userTokenManager of the parser configuration.
     *
     * @property userTokenManager
     */
    var userTokenManager by userTokenManagerProperty

    /**
     * USER_CHAR_STREAM: The default action is to generate
     * a character stream reader as specified by the options
     * JAVA_UNICODE_ESCAPE and UNICODE_INPUT.
     * This is the provider for userCharStream property.
     */
    val userCharStreamProvider: Provider<String>
        get() = userCharStreamProperty

    /**
     * USER_CHAR_STREAM: The default action is to generate
     * a character stream reader as specified by the options
     * JAVA_UNICODE_ESCAPE and UNICODE_INPUT.
     * This is the property userCharStream of the parser configuration.
     *
     * @property userCharStream
     */
    var userCharStream by userCharStreamProperty

    /**
     * BUILD_PARSER: When set to false, the parser
     * file is not generated.
     * This is the provider for buildParser property.
     */
    val buildParserProvider: Provider<String>
        get() = buildParserProperty

    /**
     * BUILD_PARSER: When set to false, the parser file is not generated.
     * This is the property buildParser of the parser configuration.
     *
     * @property buildParser
     */
    var buildParser by buildParserProperty

    /**
     * BUILD_TOKEN_MANAGER: When set to false the token manager file is not generated.
     * This is the provider for buildTokenManager property.
     */
    val buildTokenManagerProvider: Provider<String>
        get() = buildTokenManagerProperty

    /**
     * BUILD_TOKEN_MANAGER: When set to false the token manager file is not generated.
     * This is the property buildTokenManager of the parser configuration.
     *
     * @property buildTokenManager
     */
    var buildTokenManager by buildTokenManagerProperty

    /**
     * TOKEN_MANAGER_USES_PARSER: When set to true, the generated
     * token manager will include a field called parser that
     * references the instantiating parser instance.
     * This is the provider for tokenManagerUsesParser property.
     */
    val tokenManagerUsesParserProvider: Provider<String>
        get() = tokenManagerUsesParserProperty

    /**
     * TOKEN_MANAGER_USES_PARSER: When set to true, the generated
     * token manager will include a field called parser that
     * references the instantiating parser instance.
     * This is the property tokenManagerUsesParser of the parser configuration.
     *
     * @property tokenManagerUsesParser
     */
    var tokenManagerUsesParser by tokenManagerUsesParserProperty

    /**
     * SANITY_CHECK: JavaCC performs many syntactic and semantic
     * checks on the grammar file during parser generation.
     * This is the provider for sanityCheck property.
     */
    val sanityCheckProvider: Provider<String>
        get() = sanityCheckProperty

    /**
     * SANITY_CHECK: JavaCC performs many syntactic and semantic
     * checks on the grammar file during parser generation.
     * This is the property sanityCheck of the parser configuration.
     *
     * @property sanityCheck
     */
    var sanityCheck by sanityCheckProperty

    /**
     * FORCE_LA_CHECK: This option setting controls lookahead
     * ambiguity checking performed by JavaCC.
     * This is the provider for forceLaCheck property.
     */
    val forceLaCheckProvider: Provider<String>
        get() = forceLaCheckProperty

    /**
     * FORCE_LA_CHECK: This option setting controls lookahead
     * ambiguity checking performed by JavaCC.
     * This is the property forceLaCheck of the parser configuration.
     *
     * @property forceLaCheck
     */
    var forceLaCheck by forceLaCheckProperty

    /**
     * CACHE_TOKENS: Setting this option to true causes
     * the generated parser to lookahead for extra tokens
     * ahead of time.
     * This is the provider for cacheTokens property.
     */
    val cacheTokensProvider: Provider<String>
        get() = cacheTokensProperty

    /**
     * CACHE_TOKENS: Setting this option to true causes
     * the generated parser to lookahead for extra tokens
     * ahead of time.
     * This is the property cacheTokens of the parser configuration.
     *
     * @property cacheTokens
     */
    var cacheTokens by cacheTokensProperty

    /**
     * KEEP_LINE_COLUMN: If you set this option to false,
     * the generated CharStream will not have any
     * line/column tracking code.
     * This is the provider for keepLineColumn property.
     */
    val keepLineColumnProvider: Provider<String>
        get() = keepLineColumnProperty

    /**
     * KEEP_LINE_COLUMN: If you set this option to false,
     * the generated CharStream will not have any
     * line/column tracking code.
     * This is the property keepLineColumn of the parser configuration.
     *
     * @property keepLineColumn
     */
    var keepLineColumn by keepLineColumnProperty

    /**
     * TOKEN_EXTENDS: This is a string option whose default
     * value is "", meaning that the generated Token class
     * will extend java.lang.Object.
     * This is the provider for tokenExtends property.
     */
    val tokenExtendsProvider: Provider<String>
        get() = tokenExtendsProperty

    /**
     * TOKEN_EXTENDS: This is a string option whose default
     * value is "", meaning that the generated Token class
     * will extend java.lang.Object.
     * This is the property tokenExtends of the parser configuration.
     *
     * @property tokenExtends
     */
    var tokenExtends by tokenExtendsProperty

    /**
     * TOKEN_FACTORY: This is a string option whose default
     * value is "", meaning that Tokens will be created
     * by calling Token.newToken().
     * This is the provider for tokenFactory property.
     */
    val tokenFactoryProvider: Provider<String>
        get() = tokenFactoryProperty

    /**
     * TOKEN_FACTORY: This is a string option whose default
     * value is "", meaning that Tokens will be created
     * by calling Token.newToken().
     * This is the property tokenFactory of the parser configuration.
     *
     * @property tokenFactory
     */
    var tokenFactory by tokenFactoryProperty

    /**
     * Provider for property with additional command line
     * arguments passed to javaCC.
     */
    val argsProvider: Provider<List<String>>
        get() = argumentsProperty

    /**
     * Property with additional command
     * line arguments passed to javaCC.
     *
     * @property args
     */
    var args: List<String>
        get() = argumentsProperty.get()
        set(value) = argumentsProperty.set(value)

    /**
     * Add single argument to the command line
     * arguments passed to javaCC.
     *
     * @param argument
     */
    fun addArg(argument: String) {
        argumentsProperty.add(argument)
    }

    /**
     * Add a list of arguments to the command line
     * arguments passed to javaCC.
     *
     * @param args
     */
    fun addArgs(args: List<String>) {
        for(arg in args) {
            argumentsProperty.add(arg)
        }
    }

    /**
     * Configures jjtree with an action.
     *
     * @param action
     */
    fun jjtree(action: Action<in JJTree>) {
        action.execute(jjtree)
        jjtree.isConfigured = true
    }

    /**
     * Configures jjtree with a closure.
     *
     * @param c
     */
    fun jjtree(c: Closure<JJTree>) {
        ConfigureUtil.configure(c, jjtree)
        jjtree.isConfigured = true
    }

    /**
     * Calculate the task name.
     * @return task name
     */
     fun getJavaCCTaskName(): String {
        return "javacc${name.toCamelCase()}"
    }

    private fun String.toCamelCase() : String {
        return split(" ").joinToString("") { it.capitalize() }
    }
}
