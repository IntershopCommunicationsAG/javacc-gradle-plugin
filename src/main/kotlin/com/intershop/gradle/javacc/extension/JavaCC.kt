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
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.SourceSet
import java.io.File
import kotlin.reflect.KProperty

operator fun <T> Property<T>.setValue(receiver: Any?, property: KProperty<*>, value: T) = set(value)
operator fun <T> Property<T>.getValue(receiver: Any?, property: KProperty<*>): T = get()

class JavaCC(project: Project, private val confname: String) : Named {

    override fun getName() : String {
        return confname
    }

    private val outputDirProperty: DirectoryProperty = project.objects.directoryProperty()
    private val inputFileProperty: RegularFileProperty = project.objects.fileProperty()
    private val sourceSetNameProperty: Property<String> = project.objects.property(String::class.java)

    private val packageNameProperty: Property<String> = project.objects.property(String::class.java)
    private val jdkVersionProperty: Property<String> = project.objects.property(String::class.java)

    // property is a string, because there are problems with Integer and Int for the property
    private val lookaheadProperty: Property<String> = project.objects.property(String::class.java)
    // property is a string, because there are problems with Integer and Int for the property
    private val choiceAmbiguityCheckProperty: Property<String> = project.objects.property(String::class.java)
    // property is a string, because there are problems with Integer and Int for the property
    private val otherAmbiguityCheckProperty: Property<String> = project.objects.property(String::class.java)

    // these properties are interpreted as Boolean, if a value is set
    private val staticParamProperty: Property<String> = project.objects.property(String::class.java)
    private val supportClassVisibilityPublicProperty: Property<String> = project.objects.property(String::class.java)
    private val debugParserProperty: Property<String> = project.objects.property(String::class.java)
    private val debugLookaheadProperty: Property<String> = project.objects.property(String::class.java)
    private val debugTokenManagerProperty: Property<String> = project.objects.property(String::class.java)
    private val errorReportingProperty: Property<String> = project.objects.property(String::class.java)
    private val javaUnicodeEscapeProperty: Property<String> = project.objects.property(String::class.java)
    private val unicodeInputProperty: Property<String> = project.objects.property(String::class.java)
    private val ignoreCaseProperty: Property<String> = project.objects.property(String::class.java)
    private val commonTokenActionProperty: Property<String> = project.objects.property(String::class.java)
    private val userTokenManagerProperty: Property<String> = project.objects.property(String::class.java)
    private val userCharStreamProperty: Property<String> = project.objects.property(String::class.java)
    private val buildParserProperty: Property<String> = project.objects.property(String::class.java)
    private val buildTokenManagerProperty: Property<String> = project.objects.property(String::class.java)
    private val tokenManagerUsesParserProperty: Property<String> = project.objects.property(String::class.java)
    private val sanityCheckProperty: Property<String> = project.objects.property(String::class.java)
    private val forceLaCheckProperty: Property<String> = project.objects.property(String::class.java)
    private val cacheTokensProperty: Property<String> = project.objects.property(String::class.java)
    private val keepLineColumnProperty: Property<String> = project.objects.property(String::class.java)
    private val tokenExtendsProperty: Property<String> = project.objects.property(String::class.java)
    private val tokenFactoryProperty: Property<String> = project.objects.property(String::class.java)

    private val argumentsProperty : ListProperty<String> = project.objects.listProperty(String::class.java)

    val jjtree : JJTree = project.objects.newInstance(JJTree::class.java)

    init {
        outputDirProperty.set(project.layout.buildDirectory.dir("${JavaCCExtension.CODEGEN_OUTPUTPATH}/${name.replace(' ', '_')}"))
        sourceSetNameProperty.set(SourceSet.MAIN_SOURCE_SET_NAME)
    }

    // output directory
    val outputDirProvider: Provider<Directory>
        get() = outputDirProperty

    var outputDir: File
        get() =  outputDirProperty.get().asFile
        set(value) = outputDirProperty.set(value)

    // input file
    val inputFileProvider: Provider<RegularFile>
        get() = inputFileProperty

    var inputFile: File
        get() = inputFileProperty.get().asFile
        set(value) = inputFileProperty.set(value)

    // sourceSet name
    val sourceSetNameProvider: Provider<String>
        get() = sourceSetNameProperty

    var sourceSetName by sourceSetNameProperty

    // package name for generated code
    val packageNameProvider: Provider<String>
        get() = packageNameProperty

    var packageName by packageNameProperty

    // jdk version
    val jdkVersionProvider: Provider<String>
        get() = jdkVersionProperty

    var jdkVersion by jdkVersionProperty

    // lookahead parameter
    val lookaheadProvider: Provider<String>
        get() = lookaheadProperty

    var lookahead: Int?
        get() = lookaheadProperty.orNull?.toInt()
        set(value) = lookaheadProperty.set(value.toString())

    // choiceAmbiguityCheck parameter
    val choiceAmbiguityCheckProvider: Provider<String>
        get() = choiceAmbiguityCheckProperty

    var choiceAmbiguityCheck: Int?
        get() = choiceAmbiguityCheckProperty.orNull?.toInt()
        set(value) = choiceAmbiguityCheckProperty.set(value.toString())

    // otherAmbiguityCheck parameter
    val otherAmbiguityCheckProvider: Provider<String>
        get() = otherAmbiguityCheckProperty

    var otherAmbiguityCheck: Int?
        get() = otherAmbiguityCheckProperty.orNull?.toInt()
        set(value) = otherAmbiguityCheckProperty.set(value.toString())

    // staticParam parameter
    val staticParamProvider: Provider<String>
        get() = staticParamProperty

    var staticParam by staticParamProperty

    // supportClassVisibilityPublic parameter
    val supportClassVisibilityPublicProvider: Provider<String>
        get() = supportClassVisibilityPublicProperty

    var supportClassVisibilityPublic by supportClassVisibilityPublicProperty

    // debugParser parameter
    val debugParserProvider: Provider<String>
        get() = debugParserProperty

    var debugParser by debugParserProperty

    // debugLookahead parameter
    val debugLookaheadProvider: Provider<String>
        get() = debugLookaheadProperty

    var debugLookahead by debugLookaheadProperty

    // debugTokenManager parameter
    val debugTokenManagerProvider: Provider<String>
        get() = debugTokenManagerProperty

    var debugTokenManager by debugTokenManagerProperty

    // errorReporting parameter
    val errorReportingProvider: Provider<String>
        get() = errorReportingProperty

    var errorReporting by errorReportingProperty

    // javaUnicodeEscape parameter
    val javaUnicodeEscapeProvider: Provider<String>
        get() = javaUnicodeEscapeProperty

    var javaUnicodeEscape by javaUnicodeEscapeProperty

    // unicodeInput parameter
    val unicodeInputProvider: Provider<String>
        get() = unicodeInputProperty

    var unicodeInput by unicodeInputProperty

    // ignoreCase parameter
    val ignoreCaseProvider: Provider<String>
        get() = ignoreCaseProperty

    var ignoreCase by ignoreCaseProperty

    // commonTokenAction parameter
    val commonTokenActionProvider: Provider<String>
        get() = commonTokenActionProperty

    var commonTokenAction by commonTokenActionProperty

    // userTokenManager parameter
    val userTokenManagerProvider: Provider<String>
        get() = userTokenManagerProperty

    var userTokenManager by userTokenManagerProperty

    // userCharStream parameter
    val userCharStreamProvider: Provider<String>
        get() = userCharStreamProperty

    var userCharStream by userCharStreamProperty

    // buildParser parameter
    val buildParserProvider: Provider<String>
        get() = buildParserProperty

    var buildParser by buildParserProperty

    // buildTokenManager parameter
    val buildTokenManagerProvider: Provider<String>
        get() = buildTokenManagerProperty

    var buildTokenManager by buildTokenManagerProperty

    // tokenManagerUsesParser parameter
    val tokenManagerUsesParserProvider: Provider<String>
        get() = tokenManagerUsesParserProperty

    var tokenManagerUsesParser by tokenManagerUsesParserProperty

    // sanityCheck parameter
    val sanityCheckProvider: Provider<String>
        get() = sanityCheckProperty

    var sanityCheck by sanityCheckProperty

    // forceLaCheck parameter
    val forceLaCheckProvider: Provider<String>
        get() = forceLaCheckProperty

    var forceLaCheck by forceLaCheckProperty

    // cacheTokens parameter
    val cacheTokensProvider: Provider<String>
        get() = cacheTokensProperty

    var cacheTokens by cacheTokensProperty

    // keepLineColumn parameter
    val keepLineColumnProvider: Provider<String>
        get() = keepLineColumnProperty

    var keepLineColumn by keepLineColumnProperty

    // tokenExtends parameter
    val tokenExtendsProvider: Provider<String>
        get() = tokenExtendsProperty

    var tokenExtends by tokenExtendsProperty

    // tokenFactory parameter
    val tokenFactoryProvider: Provider<String>
        get() = tokenFactoryProperty

    var tokenFactory by tokenFactoryProperty

    // args parameter
    val argsProvider: Provider<List<String>>
        get() = argumentsProperty

    var args: List<String>
        get() = argumentsProperty.get()
        set(value) = argumentsProperty.set(value)

    fun addArg(argument: String) {
        argumentsProperty.add(argument)
    }

    fun addArgs(args: List<String>) {
        for(arg in args) {
            argumentsProperty.add(arg)
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