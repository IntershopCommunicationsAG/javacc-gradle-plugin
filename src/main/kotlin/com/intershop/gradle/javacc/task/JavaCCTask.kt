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
package com.intershop.gradle.javacc.task

import com.intershop.gradle.javacc.extension.JJTree
import com.intershop.gradle.javacc.extension.JavaCCExtension
import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.file.*
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.*
import org.gradle.process.JavaForkOptions
import org.gradle.workers.ForkMode
import org.gradle.workers.IsolationMode
import org.gradle.workers.WorkerExecutor
import java.io.File
import javax.inject.Inject
import kotlin.reflect.KProperty

operator fun <T> Property<T>.setValue(receiver: Any?, property: KProperty<*>, value: T) = set(value)
operator fun <T> Property<T>.getValue(receiver: Any?, property: KProperty<*>): T = get()

open class JavaCCTask @Inject constructor(private val workerExecutor: WorkerExecutor) : DefaultTask(){

    private val outputDirProperty: DirectoryProperty = project.objects.directoryProperty()

    @get:OutputDirectory
    var outputDir: File
        get() = outputDirProperty.get().asFile
        set(value) = outputDirProperty.set(value)

    fun provideOutputDir(outputDir: Provider<Directory>) = outputDirProperty.set(outputDir)

    // Java CC configuration file
    private val inputFileProperty: RegularFileProperty = project.objects.fileProperty()

    @get:InputFile
    var inputFile: File
        get() = inputFileProperty.get().asFile
        set(value) = inputFileProperty.set(value)

    fun provideInputFile(inputFile: Provider<RegularFile>) = inputFileProperty.set(inputFile)

    private val packageNameProperty: Property<String> = project.objects.property(String::class.java)

    @get:Input
    var packageName: String
        get() = packageNameProperty.getOrElse("")
        set(value) = packageNameProperty.set(value)

    fun providePackageName(packageName: Provider<String>) = packageNameProperty.set(packageName)

    private val jdkVersionProperty: Property<String> = project.objects.property(String::class.java)

    @get:Optional
    @get:Input
    var jdkVersion: String?
        get() = jdkVersionProperty.orNull
        set(value) = jdkVersionProperty.set(value)

    fun provideJdkVersion(jdkVersion: Provider<String>) = jdkVersionProperty.set(jdkVersion)

    // property is a string, because there are problems with Integer and Int for the property
    private val lookaheadProperty: Property<String> = project.objects.property(String::class.java)

    @get:Optional
    @get:Input
    var lookahead: Int?
        get() = lookaheadProperty.orNull?.toInt()
        set(value) = lookaheadProperty.set(value.toString())

    fun provideLookahead(lookahead: Provider<String>) = lookaheadProperty.set(lookahead)

    // property is a string, because there are problems with Integer and Int for the property
    private val choiceAmbiguityCheckProperty: Property<String> = project.objects.property(String::class.java)

    @get:Optional
    @get:Input
    var choiceAmbiguityCheck: Int?
        get() = choiceAmbiguityCheckProperty.orNull?.toInt()
        set(value) = choiceAmbiguityCheckProperty.set(value.toString())

    fun provideChoiceAmbiguityCheck(choiceAmbiguityCheck: Provider<String>) = choiceAmbiguityCheckProperty.set(choiceAmbiguityCheck)

    // property is a string, because there are problems with Integer and Int for the property
    private val otherAmbiguityCheckProperty: Property<String> = project.objects.property(String::class.java)

    @get:Optional
    @get:Input
    var otherAmbiguityCheck: Int?
        get() = otherAmbiguityCheckProperty.orNull?.toInt()
        set(value) = otherAmbiguityCheckProperty.set(value.toString())

    fun provideOtherAmbiguityCheck(otherAmbiguityCheck: Provider<String>) = otherAmbiguityCheckProperty.set(otherAmbiguityCheck)

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    private val staticParamProperty: Property<String> = project.objects.property(String::class.java)

    @get:Optional
    @get:Input
    var staticParam: String
        get() = staticParamProperty.getOrElse("")
        set(value) = staticParamProperty.set(value)

    fun provideStaticParam(staticParam: Provider<String>) = staticParamProperty.set(staticParam)

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    private val supportClassVisibilityPublicProperty: Property<String> = project.objects.property(String::class.java)

    @get:Optional
    @get:Input
    var supportClassVisibilityPublic: String
        get() = supportClassVisibilityPublicProperty.getOrElse("")
        set(value) = supportClassVisibilityPublicProperty.set(value)

    fun provideSupportClassVisibilityPublic(supportClassVisibilityPublic: Provider<String>) = supportClassVisibilityPublicProperty.set(supportClassVisibilityPublic)

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    private val debugParserProperty: Property<String> = project.objects.property(String::class.java)

    @get:Optional
    @get:Input
    var debugParser: String
        get() = debugParserProperty.getOrElse("")
        set(value) = debugParserProperty.set(value)

    fun provideDebugParser(debugParser: Provider<String>) = debugParserProperty.set(debugParser)

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    private val debugLookaheadProperty: Property<String> = project.objects.property(String::class.java)

    @get:Optional
    @get:Input
    var debugLookahead: String
        get() = debugLookaheadProperty.getOrElse("")
        set(value) = debugLookaheadProperty.set(value)

    fun provideDebugLookahead(debugLookahead: Provider<String>) = debugLookaheadProperty.set(debugLookahead)

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    private val debugTokenManagerProperty: Property<String> = project.objects.property(String::class.java)

    @get:Optional
    @get:Input
    var debugTokenManager: String
        get() = debugTokenManagerProperty.getOrElse("")
        set(value) = debugTokenManagerProperty.set(value)

    fun provideDebugTokenManager(debugTokenManager: Provider<String>) = debugTokenManagerProperty.set(debugTokenManager)

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    private val errorReportingProperty: Property<String> = project.objects.property(String::class.java)

    @get:Optional
    @get:Input
    var errorReporting: String
        get() = errorReportingProperty.getOrElse("")
        set(value) = errorReportingProperty.set(value)

    fun provideErrorReporting(errorReporting: Provider<String>) = errorReportingProperty.set(errorReporting)

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    private val javaUnicodeEscapeProperty: Property<String> = project.objects.property(String::class.java)

    @get:Optional
    @get:Input
    var javaUnicodeEscape: String
        get() = javaUnicodeEscapeProperty.getOrElse("")
        set(value) = javaUnicodeEscapeProperty.set(value)

    fun provideJavaUnicodeEscape(javaUnicodeEscape: Provider<String>) = javaUnicodeEscapeProperty.set(javaUnicodeEscape)

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    private val unicodeInputProperty: Property<String> = project.objects.property(String::class.java)

    @get:Optional
    @get:Input
    var unicodeInput: String
        get() = unicodeInputProperty.getOrElse("")
        set(value) = unicodeInputProperty.set(value)

    fun provideUnicodeInput(unicodeInput: Provider<String>) = unicodeInputProperty.set(unicodeInput)

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    private val ignoreCaseProperty: Property<String> = project.objects.property(String::class.java)

    @get:Optional
    @get:Input
    var ignoreCase: String
        get() = ignoreCaseProperty.getOrElse("")
        set(value) = ignoreCaseProperty.set(value)

    fun provideIgnoreCase(ignoreCase: Provider<String>) = ignoreCaseProperty.set(ignoreCase)

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    private val commonTokenActionProperty: Property<String> = project.objects.property(String::class.java)

    @get:Optional
    @get:Input
    var commonTokenAction: String
        get() = commonTokenActionProperty.getOrElse("")
        set(value) = commonTokenActionProperty.set(value)

    fun provideCommonTokenAction(commonTokenAction: Provider<String>) = commonTokenActionProperty.set(commonTokenAction)

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    private val userTokenManagerProperty: Property<String> = project.objects.property(String::class.java)

    @get:Optional
    @get:Input
    var userTokenManager: String
        get() = userTokenManagerProperty.getOrElse("")
        set(value) = userTokenManagerProperty.set(value)

    fun provideUserTokenManager(userTokenManager: Provider<String>) = userTokenManagerProperty.set(userTokenManager)

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    private val userCharStreamProperty: Property<String> = project.objects.property(String::class.java)

    @get:Optional
    @get:Input
    var userCharStream: String
        get() = userCharStreamProperty.getOrElse("")
        set(value) = userCharStreamProperty.set(value)

    fun provideUserCharStream(userCharStream: Provider<String>) = userCharStreamProperty.set(userCharStream)

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    private val buildParserProperty: Property<String> = project.objects.property(String::class.java)

    @get:Optional
    @get:Input
    var buildParser: String
        get() = buildParserProperty.getOrElse("")
        set(value) = buildParserProperty.set(value)

    fun provideBuildParser(buildParser: Provider<String>) = buildParserProperty.set(buildParser)

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    private val buildTokenManagerProperty: Property<String> = project.objects.property(String::class.java)

    @get:Optional
    @get:Input
    var buildTokenManager: String
        get() = buildTokenManagerProperty.getOrElse("")
        set(value) = buildTokenManagerProperty.set(value)

    fun provideBuildTokenManager(buildTokenManager: Provider<String>) = buildTokenManagerProperty.set(buildTokenManager)

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    private val tokenManagerUsesParserProperty: Property<String> = project.objects.property(String::class.java)

    @get:Optional
    @get:Input
    var tokenManagerUsesParser: String
        get() = tokenManagerUsesParserProperty.getOrElse("")
        set(value) = tokenManagerUsesParserProperty.set(value)

    fun provideTokenManagerUsesParser(tokenManagerUsesParser: Provider<String>) = tokenManagerUsesParserProperty.set(tokenManagerUsesParser)

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    private val sanityCheckProperty: Property<String> = project.objects.property(String::class.java)

    @get:Optional
    @get:Input
    var sanityCheck: String
        get() = sanityCheckProperty.getOrElse("")
        set(value) = sanityCheckProperty.set(value)

    fun provideSanityCheck(sanityCheck: Provider<String>) = sanityCheckProperty.set(sanityCheck)

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    private val forceLaCheckProperty: Property<String> = project.objects.property(String::class.java)

    @get:Optional
    @get:Input
    var forceLaCheck: String
        get() = forceLaCheckProperty.getOrElse("")
        set(value) = forceLaCheckProperty.set(value)

    fun provideForceLaCheck(forceLaCheck: Provider<String>) = forceLaCheckProperty.set(forceLaCheck)

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    private val cacheTokensProperty: Property<String> = project.objects.property(String::class.java)

    @get:Optional
    @get:Input
    var cacheTokens: String
        get() = cacheTokensProperty.getOrElse("")
        set(value) = cacheTokensProperty.set(value)

    fun provideCacheTokens(cacheTokens: Provider<String>) = cacheTokensProperty.set(cacheTokens)

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    private val keepLineColumnProperty: Property<String> = project.objects.property(String::class.java)

    @get:Optional
    @get:Input
    var keepLineColumn: String
        get() = keepLineColumnProperty.getOrElse("")
        set(value) = keepLineColumnProperty.set(value)

    fun provideKeepLineColumn(keepLineColumn: Provider<String>) = keepLineColumnProperty.set(keepLineColumn)

    private val tokenExtendsProperty: Property<String> = project.objects.property(String::class.java)

    @get:Optional
    @get:Input
    var tokenExtends: String
        get() = tokenExtendsProperty.getOrElse("")
        set(value) = tokenExtendsProperty.set(value)

    fun provideTokenExtends(tokenExtends: Provider<String>) = tokenExtendsProperty.set(tokenExtends)

    private val tokenFactoryProperty: Property<String> = project.objects.property(String::class.java)

    @get:Optional
    @get:Input
    var tokenFactory: String?
        get() = tokenFactoryProperty.orNull
        set(value) = tokenFactoryProperty.set(value)

    fun provideTokenFactory(tokenFactory: Provider<String>) = tokenFactoryProperty.set(tokenFactory)

    private val javaCCArgsProperty: ListProperty<String> = project.objects.listProperty(String::class.java)

    @get:Input
    var javaCCArgs: List<String>
        get() =  javaCCArgsProperty.get()
        set(value) = javaCCArgsProperty.set(value)

    fun provideJavaCCArgs(javaCCArgs: Provider<List<String>>) = javaCCArgsProperty.set(javaCCArgs)

    @get:Input
    var jjTree: JJTree? = null

    @get:InputFiles
    val toolsclasspathfiles : FileCollection by lazy {
        val returnFiles = project.files()
        // find files of original JASPER and Eclipse compiler
        returnFiles.from(project.configurations.findByName(JavaCCExtension.JAVACC_CONFIGURATION_NAME))
        returnFiles
    }

    /**
     * Java fork options for the Java task.
     */
    private var internalForkOptionsAction: Action<in JavaForkOptions>? = null

    fun forkOptions(forkOptionsAction: Action<in JavaForkOptions>) {
        internalForkOptionsAction = forkOptionsAction
    }

    /**
     * Task aktion for code generation
     */
    @TaskAction
    fun generate() {
        val outDir: File = if(packageName.isBlank()) { outputDir } else { File(outputDir, packageName.replace('.', '/')) }

        // start runner
        workerExecutor.submit(JavaCCRunner::class.java) {
            it.displayName = "Worker for Java source file creation by JavaCC."
            it.setParams( outDir,
                    inputFile,
                    calculateJavaCCParameterList(),
                    calculateJJTreeParameterList())
            it.classpath(toolsclasspathfiles)
            it.isolationMode = IsolationMode.CLASSLOADER
            it.forkMode = ForkMode.AUTO
            if(internalForkOptionsAction != null) {
                project.logger.debug("Add configured JavaForkOptions for JavaCC compile runner.")
                (internalForkOptionsAction as Action<in JavaForkOptions>).execute(it.forkOptions)
            }
        }

        workerExecutor.await()
    }

    private fun calculateJJTreeParameterList() : List<String> {
        val paramlist: MutableList<String> = mutableListOf()

        if(jjTree != null && (jjTree as JJTree).isConfigured) {
            val pJJTree: JJTree = jjTree as JJTree

            addBooleanParameter(paramlist,"BUILD_NODE_FILES", pJJTree.buildNodeFiles)
            addBooleanParameter(paramlist,"MULTI", pJJTree.multi)
            addBooleanParameter(paramlist,"NODE_DEFAULT_VOID", pJJTree.nodeDefaultVoid)
            addBooleanParameter(paramlist,"NODE_SCOPE_HOOK", pJJTree.nodeScopeHook)
            addBooleanParameter(paramlist,"NODE_USES_PARSER", pJJTree.nodeUsesParser)
            addBooleanParameter(paramlist,"TRACK_TOKENS", pJJTree.trackTokens)
            addBooleanParameter(paramlist,"VISITOR", pJJTree.visitor)
            addBooleanParameter(paramlist,"STATIC", pJJTree.staticParam)

            addStringParameter(paramlist, "NODE_CLASS", pJJTree.nodeClass)
            addStringParameter(paramlist, "NODE_PREFIX", pJJTree.nodePrefix)
            addStringParameter(paramlist, "NODE_EXTENDS", pJJTree.nodeExtends)
            addStringParameter(paramlist, "NODE_PACKAGE", pJJTree.nodePackage)
            addStringParameter(paramlist, "NODE_FACTORY", pJJTree.nodeFactory)
            addStringParameter(paramlist, "VISITOR_DATA_TYPE", pJJTree.visitorDataType)
            addStringParameter(paramlist, "VISITOR_RETURN_TYPE", pJJTree.visitorReturnType)
            addStringParameter(paramlist, "VISITOR_EXCEPTION", pJJTree.visitorException)

            addStringParameter(paramlist, "JDK_VERSION", jdkVersion)

            pJJTree.args.forEach {
                paramlist.add(it)
            }
        }

        return paramlist
    }

    private fun calculateJavaCCParameterList() : List<String> {
        val paramlist: MutableList<String> = mutableListOf()

        addBooleanParameter(paramlist,"STATIC", staticParam)
        addBooleanParameter(paramlist,"SUPPORT_CLASS_VISIBILITY_PUBLIC", supportClassVisibilityPublic)
        addBooleanParameter(paramlist,"DEBUG_PARSER", debugParser)
        addBooleanParameter(paramlist,"DEBUG_LOOKAHEAD", debugLookahead)
        addBooleanParameter(paramlist,"DEBUG_TOKEN_MANAGER", debugTokenManager)
        addBooleanParameter(paramlist,"ERROR_REPORTING", errorReporting)
        addBooleanParameter(paramlist,"JAVA_UNICODE_ESCAPE", javaUnicodeEscape)
        addBooleanParameter(paramlist,"UNICODE_INPUT", unicodeInput)
        addBooleanParameter(paramlist,"IGNORE_CASE", ignoreCase)
        addBooleanParameter(paramlist,"COMMON_TOKEN_ACTION", commonTokenAction)
        addBooleanParameter(paramlist,"USER_TOKEN_MANAGER", userTokenManager)
        addBooleanParameter(paramlist,"USER_CHAR_STREAM", userCharStream)
        addBooleanParameter(paramlist,"BUILD_PARSER", buildParser)
        addBooleanParameter(paramlist,"BUILD_TOKEN_MANAGER", buildTokenManager)
        addBooleanParameter(paramlist,"TOKEN_MANAGER_USES_PARSER", tokenManagerUsesParser)
        addBooleanParameter(paramlist,"SANITY_CHECK", sanityCheck)
        addBooleanParameter(paramlist,"FORCE_LA_CHECK", forceLaCheck)
        addBooleanParameter(paramlist,"CACHE_TOKENS", cacheTokens)
        addBooleanParameter(paramlist,"KEEP_LINE_COLUMN", keepLineColumn)

        addIntegerParameter(paramlist, "CHOICE_AMBIGUITY_CHECK", choiceAmbiguityCheck)
        addIntegerParameter(paramlist, "OTHER_AMBIGUITY_CHECK", otherAmbiguityCheck)
        addIntegerParameter(paramlist, "LOOKAHEAD", lookahead)

        addStringParameter(paramlist, "TOKEN_EXTENDS", tokenExtends)
        addStringParameter(paramlist, "TOKEN_FACTORY", tokenFactory)
        addStringParameter(paramlist, "JDK_VERSION", jdkVersion)

        javaCCArgs.forEach {
            paramlist.add(it)
        }

        return paramlist
    }

    private fun addStringParameter(paramList: MutableList<String>, paramName: String, paramValue: String?) {
        if(paramValue != null) {
            paramList.add("-$paramName=$paramValue")
        }
    }

    private fun addIntegerParameter(paramList: MutableList<String>, paramName: String, paramValue: Int?) {
        if(paramValue != null) {
            paramList.add("-$paramName=$paramValue")
        }
    }

    private fun addBooleanParameter(paramList: MutableList<String>, paramName: String, paramValue: String) {
        if(paramValue.isNotBlank()) {
            paramList.add("-$paramName=${paramValue.toBoolean()}")
        }
    }
}