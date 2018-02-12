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

import org.gradle.api.Action
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.SourceSet
import java.io.File

class JavaCC(project: Project, private val confname: String) : Named {

    override fun getName() : String {
        return confname
    }

    val outputDirProvider: DirectoryProperty = project.layout.directoryProperty()
    val inputFileProvider: RegularFileProperty = project.layout.fileProperty()
    val sourceSetNameProvider: Property<String> = project.objects.property(String::class.java)

    val packageNameProvider: Property<String> = project.objects.property(String::class.java)
    val jdkVersionProvider: Property<String> = project.objects.property(String::class.java)

    // property is a string, because there are problems with Integer and Int for the property
    val lookaheadProvider: Property<String> = project.objects.property(String::class.java)
    // property is a string, because there are problems with Integer and Int for the property
    val choiceAmbiguityCheckProvider: Property<String> = project.objects.property(String::class.java)
    // property is a string, because there are problems with Integer and Int for the property
    val otherAmbiguityCheckProvider: Property<String> = project.objects.property(String::class.java)

    // these properties are interpreted as Boolean, if a value is set
    val staticParamProvider: Property<String> = project.objects.property(String::class.java)
    val supportClassVisibilityPublicProvider: Property<String> = project.objects.property(String::class.java)
    val debugParserProvider: Property<String> = project.objects.property(String::class.java)
    val debugLookaheadProvider: Property<String> = project.objects.property(String::class.java)
    val debugTokenManagerProvider: Property<String> = project.objects.property(String::class.java)
    val errorReportingProvider: Property<String> = project.objects.property(String::class.java)
    val javaUnicodeEscapeProvider: Property<String> = project.objects.property(String::class.java)
    val unicodeInputProvider: Property<String> = project.objects.property(String::class.java)
    val ignoreCaseProvider: Property<String> = project.objects.property(String::class.java)
    val commonTokenActionProvider: Property<String> = project.objects.property(String::class.java)
    val userTokenManagerProvider: Property<String> = project.objects.property(String::class.java)
    val userCharStreamProvider: Property<String> = project.objects.property(String::class.java)
    val buildParserProvider: Property<String> = project.objects.property(String::class.java)
    val buildTokenManagerProvider: Property<String> = project.objects.property(String::class.java)
    val tokenManagerUsesParserProvider: Property<String> = project.objects.property(String::class.java)
    val sanityCheckProvider: Property<String> = project.objects.property(String::class.java)
    val forceLaCheckProvider: Property<String> = project.objects.property(String::class.java)
    val cacheTokensProvider: Property<String> = project.objects.property(String::class.java)
    val keepLineColumnProvider: Property<String> = project.objects.property(String::class.java)
    val tokenExtendsProvider: Property<String> = project.objects.property(String::class.java)
    val tokenFactoryProvider: Property<String> = project.objects.property(String::class.java)

    val argumentsProvider : ListProperty<String> = project.objects.listProperty(String::class.java)
    val jjtree : JJTree = project.objects.newInstance(JJTree::class.java)

    init {
        outputDirProvider.set(project.layout.buildDirectory.dir("${JavaCCExtension.CODEGEN_OUTPUTPATH}/${name.replace(' ', '_')}"))
        sourceSetNameProvider.set(SourceSet.MAIN_SOURCE_SET_NAME)
    }

    var outputDir: File
        get() {
            return outputDirProvider.get().asFile
        }
        set(value) {
            this.outputDirProvider.set(value)
        }

    var inputFile: File
        get() {
            return inputFileProvider.get().asFile
        }
        set(value) {
            this.inputFileProvider.set(value)
        }

    var sourceSetName: String
        get() {
            return sourceSetNameProvider.get()
        }
        set(value) {
            this.sourceSetNameProvider.set(value)
        }

    var packageName: String
        get() {
            return packageNameProvider.get()
        }
        set(value) {
            this.packageNameProvider.set(value)
        }

    var jdkVersion: String
        get() {
            return jdkVersionProvider.get()
        }
        set(value) {
            this.jdkVersionProvider.set(value)
        }

    var lookahead: Int?
        get() {
            return lookaheadProvider.orNull?.toInt()
        }
        set(value) {
            this.lookaheadProvider.set(value.toString())
        }

    var choiceAmbiguityCheck: Int?
        get() {
            return choiceAmbiguityCheckProvider.orNull?.toInt()
        }
        set(value) {
            this.choiceAmbiguityCheckProvider.set(value.toString())
        }

    var otherAmbiguityCheck: Int?
        get() {
            return otherAmbiguityCheckProvider.orNull?.toInt()
        }
        set(value) {
            this.otherAmbiguityCheckProvider.set(value.toString())
        }

    var staticParam: String
        get() {
            return staticParamProvider.get()
        }
        set(value) {
            this.staticParamProvider.set(value)
        }

    var supportClassVisibilityPublic: String
        get() {
            return supportClassVisibilityPublicProvider.get()
        }
        set(value) {
            this.supportClassVisibilityPublicProvider.set(value)
        }

    var debugParser: String
        get() {
            return debugParserProvider.get()
        }
        set(value) {
            this.debugParserProvider.set(value)
        }

    var debugLookahead: String
        get() {
            return debugLookaheadProvider.get()
        }
        set(value) {
            this.debugLookaheadProvider.set(value)
        }

    var debugTokenManager: String
        get() {
            return debugTokenManagerProvider.get()
        }
        set(value) {
            this.debugTokenManagerProvider.set(value)
        }

    var errorReporting: String
        get() {
            return errorReportingProvider.get()
        }
        set(value) {
            this.errorReportingProvider.set(value)
        }

    var javaUnicodeEscape: String
        get() {
            return javaUnicodeEscapeProvider.get()
        }
        set(value) {
            this.javaUnicodeEscapeProvider.set(value)
        }

    var unicodeInput: String
        get() {
            return unicodeInputProvider.get()
        }
        set(value) {
            this.unicodeInputProvider.set(value)
        }

    var ignoreCase: String
        get() {
            return ignoreCaseProvider.get()
        }
        set(value) {
            this.ignoreCaseProvider.set(value)
        }

    var commonTokenAction: String
        get() {
            return commonTokenActionProvider.get()
        }
        set(value) {
            this.commonTokenActionProvider.set(value)
        }

    var userTokenManager: String
        get() {
            return userTokenManagerProvider.get()
        }
        set(value) {
            this.userTokenManagerProvider.set(value)
        }

    var userCharStream: String
        get() {
            return userCharStreamProvider.get()
        }
        set(value) {
            this.userCharStreamProvider.set(value)
        }

    var buildParser: String
        get() {
            return buildParserProvider.get()
        }
        set(value) {
            this.buildParserProvider.set(value)
        }

    var buildTokenManager: String
        get() {
            return buildTokenManagerProvider.get()
        }
        set(value) {
            this.buildTokenManagerProvider.set(value)
        }

    var tokenManagerUsesParser: String
        get() {
            return tokenManagerUsesParserProvider.get()
        }
        set(value) {
            this.tokenManagerUsesParserProvider.set(value)
        }

    var sanityCheck: String
        get() {
            return sanityCheckProvider.get()
        }
        set(value) {
            this.sanityCheckProvider.set(value)
        }

    var forceLaCheck: String
        get() {
            return forceLaCheckProvider.get()
        }
        set(value) {
            this.forceLaCheckProvider.set(value)
        }

    var cacheTokens: String
        get() {
            return cacheTokensProvider.get()
        }
        set(value) {
            this.cacheTokensProvider.set(value)
        }

    var keepLineColumn: String
        get() {
            return keepLineColumnProvider.get()
        }
        set(value) {
            this.keepLineColumnProvider.set(value)
        }

    var tokenExtends: String
        get() {
            return tokenExtendsProvider.get()
        }
        set(value) {
            this.tokenExtendsProvider.set(value)
        }

    var tokenFactory: String
        get() {
            return tokenFactoryProvider.get()
        }
        set(value) {
            this.tokenFactoryProvider.set(value)
        }

    var args: List<String>
        get() {
            return argumentsProvider.get()
        }
        set(value) {
            this.argumentsProvider.set(value)
        }

    fun addArg(argument: String) {
        argumentsProvider.add(argument)
    }

    fun addArgs(args: List<String>) {
        for(arg in args) {
            argumentsProvider.add(arg)
        }
    }

    fun jjtree(action: Action<in JJTree>) {
        action.execute(jjtree)
        jjtree.isConfigured = true
    }

    /**
     * Calculate the task name
     * @return
     */
     fun getJavaCCTaskName(): String {
        return "javacc${confname.toCamelCase()}"
    }

    private fun String.toCamelCase() : String {
        return split(" ").joinToString("") { it.capitalize() }
    }
}