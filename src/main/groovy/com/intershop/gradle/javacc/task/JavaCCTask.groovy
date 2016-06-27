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
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.tasks.*
import org.gradle.process.JavaForkOptions
import org.gradle.process.internal.DefaultJavaForkOptions
import org.gradle.process.internal.JavaExecHandleBuilder

import javax.inject.Inject

class JavaCCTask extends DefaultTask {

    final static String JJTREE_MAIN_CLASS_NAME = 'jjtree'
    final static String JAVACC_MAIN_CLASS_NAME = 'javacc'

    @OutputDirectory
    File outputDirectory

    @Optional
    @Input
    String packageName

    @InputFile
    File inputFile

    @Input
    Properties javaCCParameters

    @Optional
    @Input
    String jdkVersion

    @Input
    List<String> javaCCArgs

    @Optional
    @Input
    List<String> jjTreeArgs

    @Optional
    @Input
    Properties  jjTreeParameters

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
        File srcOut = getPackageName() ? new File(getOutputDirectory(), getPackageName().replace('.', '/')) : getOutputDirectory()

        // if jjtree is configured ...
        if(getJjTreeParameters().size() > 0) {
            // jjtree run ...
            List<String> jjTreeArgs = getExecCommands(getJjTreeParameters(), srcOut, getJjTreeArgs(), getInputFile())
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
    List<String> getExecCommands(Properties props, File outputDir, List<String> args, File inputFile) {
        List<String> pArgs = []

        props.each { key, value ->
            pArgs.add("-${key}=${value}")
        }

        // If jdk version is set, add this to the parameter list.
        if(getJdkVersion()) {
            pArgs.add("-JDK_VERSION=${jdkVersion}")
        }
        // output dir
        pArgs.add("-OUTPUT_DIRECTORY=${outputDir.absolutePath}")
        pArgs.addAll(args)
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
    public JavaForkOptions getForkOptions() {
        if (forkOptions == null) {
            forkOptions = new DefaultJavaForkOptions(getFileResolver());
        }

        return forkOptions;
    }

    @Inject
    protected FileResolver getFileResolver() {
        throw new UnsupportedOperationException();
    }
}
