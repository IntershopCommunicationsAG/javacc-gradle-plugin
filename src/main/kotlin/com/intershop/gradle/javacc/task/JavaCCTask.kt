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
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.process.JavaForkOptions
import org.gradle.workers.ForkMode
import org.gradle.workers.IsolationMode
import org.gradle.workers.WorkerExecutor
import java.io.File
import javax.inject.Inject

open class JavaCCTask @Inject constructor(private val workerExecutor: WorkerExecutor) : DefaultTask(){

    @Internal
    val outputDirProperty: DirectoryProperty = this.newOutputDirectory()

    var outputDir: File
        @OutputDirectory
        get() {
            return outputDirProperty.get().asFile
        }
        set(value) {
            outputDirProperty.set(value)
        }

    // Java CC configuration file
    @Internal
    val inputFileProperty: RegularFileProperty = this.newInputFile()

    var inputFile: File
        @InputFile
        get() {
            return inputFileProperty.get().asFile
        }
        set(value) {
            inputFileProperty.set(value)
        }

    @Internal
    val packageNameProperty: Property<String> = project.objects.property(String::class.java)

    var packageName: String
        @Input
        get() {
            return packageNameProperty.getOrElse("")
        }
        set(value) {
            packageNameProperty.set(value)
        }

    val jdkVersionProperty: Property<String> = project.objects.property(String::class.java)

    var jdkVersion: String?
        @Optional
        @Input
        get() {
            return jdkVersionProperty.orNull
        }
        set(value) {
            jdkVersionProperty.set(value)
        }

    // property is a string, because there are problems with Integer and Int for the property
    val lookaheadProperty: Property<String> = project.objects.property(String::class.java)

    var lookahead: Int?
        @Optional
        @Input
        get() {
            return lookaheadProperty.orNull?.toInt()
        }
        set(value) {
            lookaheadProperty.set(value.toString())
        }

    // property is a string, because there are problems with Integer and Int for the property
    val choiceAmbiguityCheckProperty: Property<String> = project.objects.property(String::class.java)

    var choiceAmbiguityCheck: Int?
        @Optional
        @Input
        get() {
            return choiceAmbiguityCheckProperty.orNull?.toInt()
        }
        set(value) {
            choiceAmbiguityCheckProperty.set(value.toString())
        }

    // property is a string, because there are problems with Integer and Int for the property
    val otherAmbiguityCheckProperty: Property<String> = project.objects.property(String::class.java)

    var otherAmbiguityCheck: Int?
        @Optional
        @Input
        get() {
            return otherAmbiguityCheckProperty.orNull?.toInt()
        }
        set(value) {
            otherAmbiguityCheckProperty.set(value.toString())
        }

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    val staticParamProperty: Property<String> = project.objects.property(String::class.java)

    var staticParam: String
        @Optional
        @Input
        get() {
            return staticParamProperty.getOrElse("")
        }
        set(value) {
            staticParamProperty.set(value)
        }

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    val supportClassVisibilityPublicProperty: Property<String> = project.objects.property(String::class.java)

    var supportClassVisibilityPublic: String
        @Optional
        @Input
        get() {
            return supportClassVisibilityPublicProperty.getOrElse("")
        }
        set(value) {
            supportClassVisibilityPublicProperty.set(value)
        }

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    val debugParserProperty: Property<String> = project.objects.property(String::class.java)

    var debugParser: String
        @Optional
        @Input
        get() {
            return debugParserProperty.getOrElse("")
        }
        set(value) {
            debugParserProperty.set(value)
        }

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    val debugLookaheadProperty: Property<String> = project.objects.property(String::class.java)

    var debugLookahead: String
        @Optional
        @Input
        get() {
            return debugLookaheadProperty.getOrElse("")
        }
        set(value) {
            debugLookaheadProperty.set(value)
        }

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    val debugTokenManagerProperty: Property<String> = project.objects.property(String::class.java)

    var debugTokenManager: String
        @Optional
        @Input
        get() {
            return debugTokenManagerProperty.getOrElse("")
        }
        set(value) {
            debugTokenManagerProperty.set(value)
        }

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    val errorReportingProperty: Property<String> = project.objects.property(String::class.java)

    var errorReporting: String
        @Optional
        @Input
        get() {
            return errorReportingProperty.getOrElse("")
        }
        set(value) {
            errorReportingProperty.set(value)
        }

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    val javaUnicodeEscapeProperty: Property<String> = project.objects.property(String::class.java)

    var javaUnicodeEscape: String
        @Optional
        @Input
        get() {
            return javaUnicodeEscapeProperty.getOrElse("")
        }
        set(value) {
            javaUnicodeEscapeProperty.set(value)
        }

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    val unicodeInputProperty: Property<String> = project.objects.property(String::class.java)

    var unicodeInput: String
        @Optional
        @Input
        get() {
            return unicodeInputProperty.getOrElse("")
        }
        set(value) {
            unicodeInputProperty.set(value)
        }

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    val ignoreCaseProperty: Property<String> = project.objects.property(String::class.java)

    var ignoreCase: String
        @Optional
        @Input
        get() {
            return ignoreCaseProperty.getOrElse("")
        }
        set(value) {
            ignoreCaseProperty.set(value)
        }

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    val commonTokenActionProperty: Property<String> = project.objects.property(String::class.java)

    var commonTokenAction: String
        @Optional
        @Input
        get() {
            return commonTokenActionProperty.getOrElse("")
        }
        set(value) {
            commonTokenActionProperty.set(value)
        }

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    val userTokenManagerProperty: Property<String> = project.objects.property(String::class.java)

    var userTokenManager: String
        @Optional
        @Input
        get() {
            return userTokenManagerProperty.getOrElse("")
        }
        set(value) {
            userTokenManagerProperty.set(value)
        }

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    val userCharStreamProperty: Property<String> = project.objects.property(String::class.java)

    var userCharStream: String
        @Optional
        @Input
        get() {
            return userCharStreamProperty.getOrElse("")
        }
        set(value) {
            userCharStreamProperty.set(value)
        }

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    val buildParserProperty: Property<String> = project.objects.property(String::class.java)

    var buildParser: String
        @Optional
        @Input
        get() {
            return buildParserProperty.getOrElse("")
        }
        set(value) {
            buildParserProperty.set(value)
        }

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    val buildTokenManagerProperty: Property<String> = project.objects.property(String::class.java)

    var buildTokenManager: String
        @Optional
        @Input
        get() {
            return buildTokenManagerProperty.getOrElse("")
        }
        set(value) {
            buildTokenManagerProperty.set(value)
        }

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    val tokenManagerUsesParserProperty: Property<String> = project.objects.property(String::class.java)

    var tokenManagerUsesParser: String
        @Optional
        @Input
        get() {
            return tokenManagerUsesParserProperty.getOrElse("")
        }
        set(value) {
            tokenManagerUsesParserProperty.set(value)
        }

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    val sanityCheckProperty: Property<String> = project.objects.property(String::class.java)

    var sanityCheck: String
        @Optional
        @Input
        get() {
            return sanityCheckProperty.getOrElse("")
        }
        set(value) {
            sanityCheckProperty.set(value)
        }

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    val forceLaCheckProperty: Property<String> = project.objects.property(String::class.java)

    var forceLaCheck: String
        @Optional
        @Input
        get() {
            return forceLaCheckProperty.getOrElse("")
        }
        set(value) {
            forceLaCheckProperty.set(value)
        }

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    val cacheTokensProperty: Property<String> = project.objects.property(String::class.java)

    var cacheTokens: String
        @Optional
        @Input
        get() {
            return cacheTokensProperty.getOrElse("")
        }
        set(value) {
            cacheTokensProperty.set(value)
        }

    /**
     * This property will interpreted as boolean if the value is not empty.
     */
    val keepLineColumnProperty: Property<String> = project.objects.property(String::class.java)

    var keepLineColumn: String
        @Optional
        @Input
        get() {
            return keepLineColumnProperty.getOrElse("")
        }
        set(value) {
            keepLineColumnProperty.set(value)
        }


    val tokenExtendsProperty: Property<String> = project.objects.property(String::class.java)

    var tokenExtends: String
        @Optional
        @Input
        get() {
            return tokenExtendsProperty.getOrElse("")
        }
        set(value) {
            tokenExtendsProperty.set(value)
        }

    val tokenFactoryProperty: Property<String> = project.objects.property(String::class.java)

    var tokenFactory: String?
        @Optional
        @Input
        get() {
            return tokenFactoryProperty.orNull
        }
        set(value) {
            tokenFactoryProperty.set(value)
        }

    val javaCCArgsProperty: ListProperty<String> = project.objects.listProperty(String::class.java)

    var javaCCArgs: List<String>
        get() {
            return javaCCArgsProperty.get()
        }
        set(value) {
            javaCCArgsProperty.set(value)
        }

    var jjTree: JJTree? = null

    @get:InputFiles
    private val toolsclasspathfiles : FileCollection by lazy {
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
        workerExecutor.submit(JavaCCRunner::class.java, {
            it.displayName = "Worker for Java source file creation by JavaCC."
            it.setParams( outDir,
                          inputFile,
                          calculateJavaCCParameterList(),
                          calculateJJTreeParameterList())
            it.classpath(toolsclasspathfiles)
            it.isolationMode = IsolationMode.CLASSLOADER
            it.forkMode = ForkMode.AUTO
            if(internalForkOptionsAction != null) {
                project.logger.debug("JavaCC compile runner Add configured JavaForkOptions.")
                (internalForkOptionsAction as Action<in JavaForkOptions>).execute(it.forkOptions)
            }
        })

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
            paramList.add("-$paramName=${paramValue}")
        }
    }

    private fun addIntegerParameter(paramList: MutableList<String>, paramName: String, paramValue: Int?) {
        if(paramValue != null) {
            paramList.add("-$paramName=${paramValue}")
        }
    }

    private fun addBooleanParameter(paramList: MutableList<String>, paramName: String, paramValue: String) {
        if(paramValue.isNotBlank()) {
            paramList.add("-$paramName=${paramValue.toBoolean()}")
        }
    }
}