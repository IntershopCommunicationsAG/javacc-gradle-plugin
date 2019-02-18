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
package com.intershop.gradle.javacc
import com.intershop.gradle.javacc.extension.JavaCCExtension
import com.intershop.gradle.javacc.task.JavaCCTask
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class JavaCCPluginSpec extends Specification {

    private final Project project = ProjectBuilder.builder().build()
    private final JavaCCPlugin plugin = new JavaCCPlugin()
    
    def 'should add extension named javacc'() {
        when:
        plugin.apply(project)
        
        then:
        project.extensions.javacc
    }
    
    def 'should add JavaCC generate task for each javacc config'() {
        when:
        plugin.apply(project)
        project.extensions.javacc.configs {
            testconfiguration {
            }
        }
        
        then:
        project.tasks.findByName("javaccTestconfiguration")
        project.extensions.javacc.configs.testconfiguration.getSourceSetName() == SourceSet.MAIN_SOURCE_SET_NAME
    }

    def 'test configuration in JavaCC with JJTree'() {
        when:
        plugin.apply(project)
        project.extensions.javacc.configs {
            testconfiguration {
                userTokenManager = false
                jjtree {
                    nodePrefix = "AST"
                    nodePackage = "com.test"
                }
            }
        }

        then:
        project.tasks.findByName("javaccTestconfiguration")
        project.extensions.javacc.configs.testconfiguration.getSourceSetName() == SourceSet.MAIN_SOURCE_SET_NAME
    }
}
