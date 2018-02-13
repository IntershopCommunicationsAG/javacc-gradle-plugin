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

import org.gradle.api.GradleException
import org.javacc.jjtree.JJTree
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import javax.inject.Inject

class JavaCCRunner @Inject constructor(private val outputDir: File,
                                       private val inputFile: File,
                                       private val javaCCParamList: List<String>,
                                       private val jjTreeParamList: List<String>) : Runnable {

    companion object {
        val log: Logger = LoggerFactory.getLogger(JavaCCRunner::class.java.name)
    }

    override fun run() {
        var fileName = ""
        var jjTreeResult = 0

        if(jjTreeParamList.isNotEmpty()) {
            log.info("Start JJTree first ...")

            val jjTreeParams: MutableList<String> = mutableListOf<String>()
            jjTreeParams.apply {
                addAll(jjTreeParamList)
                add("-OUTPUT_DIRECTORY=${outputDir.absolutePath}")
                add(inputFile.absolutePath)
            }

            //calculate filename for JavaCC
            fileName = inputFile.name.replaceFirst("\\.[^\\.]+\$".toRegex(), ".jj")

            jjTreeResult = JJTree().main(jjTreeParams.toTypedArray())
        }
        if(jjTreeResult == 0) {
            log.info("Start JavaCC code generation")

            val javaCCParams: MutableList<String> = mutableListOf<String>()
            javaCCParams.apply {
                addAll(javaCCParamList)
                add("-OUTPUT_DIRECTORY=${outputDir.absolutePath}")
                add(if(fileName.isNotBlank()) { File(outputDir, fileName).absolutePath } else { inputFile.absolutePath } )
            }

            val javaccResult = org.javacc.parser.Main.mainProgram(javaCCParams.toTypedArray())
            if (javaccResult != 0) {
                log.error("Java CC code generation failed!")
                throw GradleException("Java CC code generation failed!")
            }
        } else {
            throw GradleException("JJTree code generation failed!")
        }
    }
}