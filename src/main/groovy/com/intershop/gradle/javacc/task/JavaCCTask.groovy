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

    final Property<Map<String, String>> javaCCParameters = project.objects.property(Map)

    @Input
    Map<String, String> getJavaCCParameters() {
        return javaCCParameters.get()
    }

    void setJavaCCParameters(Map<String, String> javaCCParameters) {
        this.javaCCParameters.set(javaCCParameters)
    }

    void setJavaCCParameters(Provider<Map<String, String>> javaCCParameters) {
        this.javaCCParameters.set(javaCCParameters)
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

    final Property<Map<String, String>> jjTreeParameters = project.objects.property(Map)

    @Optional
    @Input
    Map<String, String> getJJTreeParameters() {
        return jjTreeParameters.getOrNull()
    }

    void setJJTreeParameters(Map<String, String> jjTreeParameters) {
        this.jjTreeParameters.set(jjTreeParameters)
    }

    void setJJTreeParameters(Provider<Map<String, String>> jjTreeParameters) {
        this.jjTreeParameters.set(jjTreeParameters)
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
        if(getJJTreeParameters().size() > 0) {
            // jjtree run ...
            List<String> jjTreeArgs = getExecCommands(getJJTreeParameters(), srcOut, getJJTreeArgs(), getInputFile())
            prepareExec(JJTREE_MAIN_CLASS_NAME, jjTreeArgs).build().start().waitForFinish().assertNormalExitValue()

            String fileName = getInputFile().name.replaceFirst(~/\.[^\.]+$/, '.jj')
            javaCCInputFile = new File(srcOut, fileName)
        }

        // javacc run ...
        List<String> javaCCArgs = getExecCommands(getJavaCCParameters(), srcOut, getJavaCCArgs(), javaCCInputFile)
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
    List<String> getExecCommands(Map<String, String> params, File outputDir, List<String> args, File inputFile) {
        List<String> pArgs = []

        params.each { String key, String value ->
            pArgs.add("-${key}=${value}".toString())
        }

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
