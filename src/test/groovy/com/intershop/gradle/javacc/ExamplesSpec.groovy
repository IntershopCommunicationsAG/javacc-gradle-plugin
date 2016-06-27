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

import com.intershop.gradle.test.AbstractIntegrationSpec

class ExamplesSpec extends AbstractIntegrationSpec {

    def 'Test Simple Examples'() {
        given:
        copyResources('examples/SimpleExamples/jj', 'jj')

        buildFile << """
            plugins {
                id 'java'
                id 'com.intershop.gradle.javacc'
            }

            version = '1.0.0.0'
            group = 'com.test.gradle'

            sourceCompatibility = 1.7
            targetCompatibility = 1.7

            sourceSets {
                simple1
                simple2
                simple3
                idList
                NL_Xlator
            }

            javacc {
                configs {
                    simple1 {
                        sourceSetName = 'simple1'
                        inputFile = file('jj/Simple1.jj')
                    }
                    simple2 {
                        sourceSetName = 'simple2'
                        inputFile = file('jj/Simple2.jj')
                    }
                    simple3 {
                        sourceSetName = 'simple3'
                        inputFile = file('jj/Simple3.jj')
                    }

                    idList {
                        sourceSetName = 'idList'
                        inputFile = file('jj/IdList.jj')
                    }
                    NL_Xlator {
                        sourceSetName = 'NL_Xlator'
                        inputFile = file('jj/NL_Xlator.jj')
                    }
                }
            }

            repositories {
                jcenter()
            }
        """.stripIndent()

        when:
        List<String> args = ['simple1Classes', 'simple2Classes', 'simple3Classes', 'idListClasses', 'nL_XlatorClasses', '-s']

        def result = getPreparedGradleRunner()
                .withArguments(args)
                .withGradleVersion(gradleVersion)
                .build()

        File parserFileSimple1 = new File(testProjectDir, 'build/generated/javacc/simple1/Simple1.java')
        File parserFileSimple2 = new File(testProjectDir, 'build/generated/javacc/simple2/Simple2.java')
        File parserFileSimple3 = new File(testProjectDir, 'build/generated/javacc/simple3/Simple3.java')
        File parserFileIdList = new File(testProjectDir, 'build/generated/javacc/idList/IdList.java')
        File parserFileNL_Xlator = new File(testProjectDir, 'build/generated/javacc/NL_Xlator/NL_Xlator.java')

        then:
        result.output.contains(':javaccSimple1')
        result.output.contains(':javaccSimple2')
        result.output.contains(':javaccSimple3')
        result.output.contains(':javaccIdList')
        result.output.contains(':javaccNL_Xlator')

        result.output.contains(':compileSimple1Java')
        result.output.contains(':compileSimple2Java')
        result.output.contains(':compileSimple3Java')
        result.output.contains(':compileNL_XlatorJava')

        parserFileSimple1.exists()
        parserFileSimple2.exists()
        parserFileSimple3.exists()
        parserFileIdList.exists()
        parserFileNL_Xlator.exists()

        where:
        gradleVersion << supportedGradleVersions
    }

    def 'Test example MailProcessing'() {
        given:
        copyResources('examples/MailProcessing/jj', 'jj')

        buildFile << """
            plugins {
                id 'java'
                id 'com.intershop.gradle.javacc'
            }

            version = '1.0.0.0'
            group = 'com.test.gradle'

            sourceCompatibility = 1.7
            targetCompatibility = 1.7

            sourceSets {
                digest
                faq
            }

            javacc {
                configs {
                    digest {
                        sourceSetName = 'digest'
                        inputFile = file('jj/Digest.jj')
                    }
                    faq {
                        sourceSetName = 'faq'
                        inputFile = file('jj/Faq.jj')
                    }
                }
            }

            repositories {
                jcenter()
            }
        """.stripIndent()

        when:
        List<String> args = ['digestClasses', 'faqClasses', '-s']

        def result = getPreparedGradleRunner()
                .withArguments(args)
                .withGradleVersion(gradleVersion)
                .build()

        File parserFileDigest = new File(testProjectDir, 'build/generated/javacc/digest/Digest.java')
        File parserFileFaq = new File(testProjectDir, 'build/generated/javacc/faq/Faq.java')

        then:
        result.output.contains(':javaccDigest')
        result.output.contains(':javaccFaq')

        result.output.contains(':compileDigestJava')
        result.output.contains(':compileFaqJava')

        parserFileDigest.exists()
        parserFileFaq.exists()

        where:
        gradleVersion << supportedGradleVersions
    }

    def 'Test JJTreeExamples'() {
        given:
        copyResources('examples/JJTreeExamples/jjt', 'jjt')
        copyResources('examples/JJTreeExamples/jjtSrc', 'jjtSrc')

        buildFile << """
            plugins {
                id 'java'
                id 'com.intershop.gradle.javacc'
            }

            version = '1.0.0.0'
            group = 'com.test.gradle'

            sourceCompatibility = 1.7
            targetCompatibility = 1.7

            sourceSets {
                eg1
                eg2
                eg3
                eg4
            }

            def javaCCOutDirEg2 = new File(project.buildDir, 'generated/javacc/eg2')
            def javaCCOutDirEg3 = new File(project.buildDir, 'generated/javacc/eg3')
            def javaCCOutDirEg4 = new File(project.buildDir, 'generated/javacc/eg4')

            javacc {
                configs {
                    eg1 {
                        sourceSetName = 'eg1'
                        inputFile = file('jjt/eg1.jjt')
                        jjtree {}
                    }
                    eg2 {
                        sourceSetName = 'eg2'
                        outputDir = javaCCOutDirEg2
                        inputFile = file('jjt/eg2.jjt')
                        jjtree {}
                    }
                    eg3 {
                        sourceSetName = 'eg3'
                        outputDir = javaCCOutDirEg3
                        inputFile = file('jjt/eg3.jjt')
                        jjtree {}
                    }
                    eg4 {
                        sourceSetName = 'eg4'
                        outputDir = javaCCOutDirEg4
                        inputFile = file('jjt/eg4.jjt')
                        jjtree {}
                    }
                }
            }

            task copySrcEg2(type: Copy) {
                from 'jjtSrc'
                into javaCCOutDirEg2
                include 'ASTMyID.java'
            }
            task copySrcEg3(type: Copy) {
                from 'jjtSrc'
                into javaCCOutDirEg3
                include 'ASTMyID.java'
            }
            task copySrcEg4(type: Copy) {
                from 'jjtSrc'
                into javaCCOutDirEg4
            }

            javaccEg2.dependsOn copySrcEg2
            javaccEg3.dependsOn copySrcEg3
            javaccEg4.dependsOn copySrcEg4

            repositories {
                jcenter()
            }
        """.stripIndent()

        when:
        List<String> args = ['eg1Classes', 'eg2Classes', 'eg3Classes', 'eg4Classes','-s']

        def result = getPreparedGradleRunner()
                .withArguments(args)
                .withGradleVersion(gradleVersion)
                .build()

        File parserFileEg1 = new File(testProjectDir, 'build/generated/javacc/eg1/Eg1.java')
        File parserFileEg2 = new File(testProjectDir, 'build/generated/javacc/eg2/Eg2.java')
        File parserFileEg3 = new File(testProjectDir, 'build/generated/javacc/eg3/Eg3.java')
        File parserFileEg4 = new File(testProjectDir, 'build/generated/javacc/eg4/Eg4.java')

        then:
        result.output.contains(':javaccEg1')
        result.output.contains(':compileEg1Java')
        result.output.contains(':javaccEg2')
        result.output.contains(':compileEg2Java')
        result.output.contains(':javaccEg3')
        result.output.contains(':compileEg3Java')
        result.output.contains(':javaccEg4')
        result.output.contains(':compileEg4Java')

        parserFileEg1.exists()
        parserFileEg2.exists()
        parserFileEg3.exists()
        parserFileEg4.exists()

        where:
        gradleVersion << supportedGradleVersions
    }

    def 'Test example CORBA-IDL'() {
        given:
        copyResources('examples/CORBA-IDL', 'jj')

        buildFile << """
            plugins {
                id 'java'
                id 'com.intershop.gradle.javacc'
            }

            version = '1.0.0.0'
            group = 'com.test.gradle'

            sourceCompatibility = 1.7
            targetCompatibility = 1.7

            javacc {
                configs {
                    idl {
                        inputFile = file('jj/IDL.jj')
                    }
                }
            }

            repositories {
                jcenter()
            }
        """.stripIndent()

        when:
        List<String> args = ['compileJava', '-s']

        def result = getPreparedGradleRunner()
                .withArguments(args)
                .withGradleVersion(gradleVersion)
                .build()

        File parserFile = new File(testProjectDir, 'build/generated/javacc/idl/IDLParser.java')

        then:
        result.output.contains(':javaccIdl')
        result.output.contains(':compileJava')
        parserFile.exists()

        where:
        gradleVersion << supportedGradleVersions
    }

    def 'Test JavaGrammars'() {
        given:
        copyResources('examples/JavaGrammars/jj', 'jj')
        copyResources('examples/JavaGrammars/jjSrc', 'jjSrc')
        copyResources('examples/JavaGrammars/pre1.5', 'pre1.5')
        copyResources('examples/JavaGrammars/1.5', '1.5')

        buildFile << """
            plugins {
                id 'java'
                id 'com.intershop.gradle.javacc'
            }

            version = '1.0.0.0'
            group = 'com.test.gradle'

            sourceCompatibility = 1.7
            targetCompatibility = 1.7

            sourceSets {
                javaGrammar102
                javaGrammar102LS
                javaGrammar11
                javaGrammar11noLA
                javaGrammar15
            }

            def javaCCOutDir15 = new File(project.buildDir, 'generated/javacc/javaGrammar15')

            javacc {
                configs {
                    javaGrammar102 {
                        sourceSetName = 'javaGrammar102'
                        inputFile = file('jj/Java1.0.2.jj')
                    }
                    javaGrammar102LS {
                        sourceSetName = 'javaGrammar102LS'
                        inputFile = file('jj/Java1.0.2LS.jj')
                    }
                    javaGrammar11 {
                        sourceSetName = 'javaGrammar11'
                        inputFile = file('jj/Java1.1.jj')
                    }
                    javaGrammar11noLA {
                        sourceSetName = 'javaGrammar11noLA'
                        inputFile = file('jj/Java1.1noLA.jj')
                    }
                    javaGrammar15 {
                        sourceSetName = 'javaGrammar15'
                        outputDir = javaCCOutDir15
                        inputFile = file('jj/Java1.5.jj')
                    }
                }
            }

            task copySrc15(type: Copy) {
                from 'jjSrc'
                into javaCCOutDir15
            }

            javaccJavaGrammar15.dependsOn copySrc15

            repositories {
                jcenter()
            }
        """.stripIndent()

        when:
        List<String> args = ['javaGrammar102Classes', 'javaGrammar102LSClasses', 'javaGrammar11Classes', 'javaGrammar11noLAClasses', 'javaGrammar15Classes', '-s']

        def result = getPreparedGradleRunner()
                .withArguments(args)
                .withGradleVersion(gradleVersion)
                .build()

        File parserFileJavaGrammar102 = new File(testProjectDir, 'build/generated/javacc/javaGrammar102/JavaParser.java')
        File parserFileJavaGrammar102LS = new File(testProjectDir, 'build/generated/javacc/javaGrammar102LS/JavaParser.java')
        File parserFileJavaGrammar11 = new File(testProjectDir, 'build/generated/javacc/javaGrammar11/JavaParser.java')
        File parserFileJavaGrammar11noLA = new File(testProjectDir, 'build/generated/javacc/javaGrammar11noLA/JavaParser.java')
        File parserFileJavaGrammar15 = new File(testProjectDir, 'build/generated/javacc/javaGrammar15/JavaParser.java')

        then:
        result.output.contains(':javaccJavaGrammar102')
        result.output.contains(':compileJavaGrammar102Java')
        result.output.contains(':javaccJavaGrammar102LS')
        result.output.contains(':compileJavaGrammar102LSJava')
        result.output.contains(':javaccJavaGrammar11')
        result.output.contains(':compileJavaGrammar11Java')
        result.output.contains(':javaccJavaGrammar11noLA')
        result.output.contains(':compileJavaGrammar11noLAJava')

        parserFileJavaGrammar102.exists()
        parserFileJavaGrammar102LS.exists()
        parserFileJavaGrammar11.exists()
        parserFileJavaGrammar11noLA.exists()
        parserFileJavaGrammar15.exists()

        where:
        gradleVersion << supportedGradleVersions
    }

    def 'Test JavaCCGrammar'() {
        given:
        copyResources('examples/JavaCCGrammar', 'jj')

        buildFile << """
            plugins {
                id 'java'
                id 'com.intershop.gradle.javacc'
            }

            version = '1.0.0.0'
            group = 'com.test.gradle'

            sourceCompatibility = 1.7
            targetCompatibility = 1.7

            javacc {
                configs {
                    javaCC {
                        inputFile = file('jj/JavaCC.jj')
                    }
                }
            }

            repositories {
                jcenter()
            }
        """.stripIndent()

        when:
        List<String> args = ['compileJava', '-s']

        def result = getPreparedGradleRunner()
                .withArguments(args)
                .withGradleVersion(gradleVersion)
                .build()

        File parserFile = new File(testProjectDir, 'build/generated/javacc/javaCC/JavaCCParser.java')

        then:
        result.output.contains(':javaccJavaCC')
        result.output.contains(':compileJava')
        parserFile.exists()

        where:
        gradleVersion << supportedGradleVersions
    }

    def 'Test example Transformer'() {
        given:
        copyResources('examples/Transformer/jjt', 'jjt')
        copyResources('examples/Transformer/jjSrc', 'jjSrc')
        copyResources('examples/Transformer/example', 'example')

        buildFile << """
            plugins {
                id 'java'
                id 'com.intershop.gradle.javacc'
            }

            version = '1.0.0.0'
            group = 'com.test.gradle'

            sourceCompatibility = 1.7
            targetCompatibility = 1.7

            def javaCCOutDir = new File(project.buildDir, 'generated/javacc/toy')

            javacc {
                configs {
                    toy {
                        outputDir = javaCCOutDir
                        inputFile = file('jjt/ToyJava.jjt')
                        jjtree{}
                    }
                }
            }

            task copySrc(type: Copy) {
                from 'jjSrc'
                into javaCCOutDir
            }

            javaccToy.dependsOn copySrc

            task testExec(type: JavaExec) {
                main = 'ToyParser'
                classpath = sourceSets.main.runtimeClasspath
                args((new File(projectDir, 'example/divide.toy')).absolutePath, (new File(projectDir, 'example/divide.java')).absolutePath)
            }

            repositories {
                jcenter()
            }
        """.stripIndent()

        when:
        List<String> args = ['compileJava', '-s']

        def result = getPreparedGradleRunner()
                .withArguments(args)
                .withGradleVersion(gradleVersion)
                .build()

        File parserFile = new File(testProjectDir, 'build/generated/javacc/toy/ToyParser.java')

        then:
        result.output.contains(':javaccToy')
        result.output.contains(':compileJava')
        parserFile.exists()

        when:
        List<String> testArgs = ['testExec', '-s']

        def resultExec = getPreparedGradleRunner()
                .withArguments(args)
                .withGradleVersion(gradleVersion)
                .build()

        File testFile = new File(testProjectDir, 'example/divide.toy')

        then:
        testFile.exists()

        where:
        gradleVersion << supportedGradleVersions
    }

    def 'Test example GUIParsing ParserVersion'() {
        given:
        copyResources('examples/GUIParsing/ParserVersion/jj', 'jj')
        copyResources('examples/GUIParsing/ParserVersion/jjSrc', 'jjSrc')

        buildFile << """
            plugins {
                id 'java'
                id 'com.intershop.gradle.javacc'
            }

            version = '1.0.0.0'
            group = 'com.test.gradle'

            sourceCompatibility = 1.7
            targetCompatibility = 1.7

            def javaCCOutDir = new File(project.buildDir, 'generated/javacc/calcInput')

            javacc {
                configs {
                    calcInput {
                        outputDir = javaCCOutDir
                        inputFile = file('jj/CalcInput.jj')
                    }
                }
            }

            task copySrc(type: Copy) {
                from 'jjSrc'
                into javaCCOutDir
            }

            javaccCalcInput.dependsOn copySrc

            repositories {
                jcenter()
            }
        """.stripIndent()

        when:
        List<String> args = ['compileJava', '-s']

        def result = getPreparedGradleRunner()
                .withArguments(args)
                .withGradleVersion(gradleVersion)
                .build()

        File parserFile = new File(testProjectDir, 'build/generated/javacc/calcInput/CalcInputParser.java')

        then:
        result.output.contains(':javaccCalcInput')
        result.output.contains(':compileJava')
        parserFile.exists()

        where:
        gradleVersion << supportedGradleVersions
    }

    def 'Test example GUIParsing TokenMgrVersion'() {
        given:
        copyResources('examples/GUIParsing/TokenMgrVersion/jj', 'jj')
        copyResources('examples/GUIParsing/TokenMgrVersion/jjSrc', 'jjSrc')

        buildFile << """
            plugins {
                id 'java'
                id 'com.intershop.gradle.javacc'
            }

            version = '1.0.0.0'
            group = 'com.test.gradle'

            sourceCompatibility = 1.7
            targetCompatibility = 1.7

            def javaCCOutDir = new File(project.buildDir, 'generated/javacc/calcInput')

            javacc {
                configs {
                    calcInput {
                        outputDir = javaCCOutDir
                        inputFile = file('jj/CalcInput.jj')
                    }
                }
            }

            task copySrc(type: Copy) {
                from 'jjSrc'
                into javaCCOutDir
            }

            javaccCalcInput.dependsOn copySrc

            repositories {
                mavenCentral()
            }
        """.stripIndent()

        when:
        List<String> args = ['compileJava', '-s']

        def result = getPreparedGradleRunner()
                .withArguments(args)
                .withGradleVersion(gradleVersion)
                .build()

        File parserFile = new File(testProjectDir, 'build/generated/javacc/calcInput/CalcInputParserTokenManager.java')

        then:
        result.output.contains(':javaccCalcInput')
        result.output.contains(':compileJava')
        parserFile.exists()

        where:
        gradleVersion << supportedGradleVersions
    }

    def 'Test example Obfuscator'() {
        copyResources('examples/Obfuscator/jj', 'jj')
        copyResources('examples/Obfuscator/jjSrc', 'jjSrc')
        copyResources('examples/Obfuscator/config', 'config')
        copyResources('examples/Obfuscator/input', 'input')

        buildFile << """
            plugins {
                id 'java'
                id 'com.intershop.gradle.javacc'
            }

            version = '1.0.0.0'
            group = 'com.test.gradle'

            sourceCompatibility = 1.7
            targetCompatibility = 1.7

            def javaCCOutDir = new File(project.buildDir, 'generated/javacc/obfuscator')

            javacc {
                configs {
                    obfuscatorIds {
                        outputDir = javaCCOutDir
                        inputFile = file('jj/IdsFile.jj')
                    }
                    obfuscatorJava {
                        outputDir = javaCCOutDir
                        inputFile = file('jj/Java1.1.jj')
                    }
                    obfuscatorMap {
                        outputDir = javaCCOutDir
                        inputFile = file('jj/MapFile.jj')
                    }
                }
            }

            task copySrc(type: Copy) {
                from 'jjSrc'
                into javaCCOutDir
            }

            javaccObfuscatorIds.dependsOn copySrc
            javaccObfuscatorJava.dependsOn copySrc
            javaccObfuscatorMap.dependsOn copySrc

            task testExec(type: JavaExec) {
                main = 'Main'
                classpath = sourceSets.main.runtimeClasspath
                workingDir projectDir
                args('input', 'output', 'config/maps', 'config/nochangeids', 'config/useids')
            }

            repositories {
                jcenter()
            }
        """.stripIndent()

        when:
        List<String> args = ['compileJava', '-s']

        def result = getPreparedGradleRunner()
                .withArguments(args)
                .withGradleVersion(gradleVersion)
                .build()

        File mapParserFile = new File(testProjectDir, 'build/generated/javacc/obfuscator/MapFile.java')
        File idsParserFile = new File(testProjectDir, 'build/generated/javacc/obfuscator/IdsFile.java')
        File javaParserFile = new File(testProjectDir, 'build/generated/javacc/obfuscator/JavaParser.java')

        then:
        result.output.contains(':javaccObfuscatorIds')
        result.output.contains(':javaccObfuscatorJava')
        result.output.contains(':javaccObfuscatorMap')
        result.output.contains(':compileJava')

        mapParserFile.exists()
        idsParserFile.exists()
        javaParserFile.exists()

        when:
        List<String> testArgs = ['testExec', '-s']

        def resultExec = getPreparedGradleRunner()
                .withArguments(args)
                .withGradleVersion(gradleVersion)
                .build()

        File testFile = new File(testProjectDir, 'config')

        then:
        testFile.exists()

        where:
        gradleVersion << supportedGradleVersions
    }

    def 'Test example Interpreter'() {
        given:
        copyResources('examples/Interpreter/jjt', 'jjt')
        copyResources('examples/Interpreter/jjtSrc', 'jjtSrc')
        copyResources('examples/Interpreter/example', 'example')

        buildFile << """
            plugins {
                id 'java'
                id 'com.intershop.gradle.javacc'
            }

            version = '1.0.0.0'
            group = 'com.test.gradle'

            sourceCompatibility = 1.7
            targetCompatibility = 1.7

            def javaCCOutDir = new File(project.buildDir, 'generated/javacc/interpreter')

            javacc {
                configs {
                    interpreter {
                        outputDir = javaCCOutDir
                        inputFile = file('jjt/SPL.jjt')
                        jjtree { }
                    }
                }
            }

            task copySrc(type: Copy) {
                from 'jjtSrc'
                into javaCCOutDir
            }

            javaccInterpreter.dependsOn copySrc

            repositories {
                jcenter()
            }
        """.stripIndent()

        when:
        List<String> args = ['compileJava', '-s']

        def result = getPreparedGradleRunner()
                .withArguments(args)
                .withGradleVersion(gradleVersion)
                .build()

        File parserFile = new File(testProjectDir, 'build/generated/javacc/interpreter/SPLParser.java')

        then:
        result.output.contains(':javaccInterpreter')
        result.output.contains(':compileJava')
        parserFile.exists()

        where:
        gradleVersion << supportedGradleVersions
    }

    def 'Test example Lookahead'(){
        given:
        copyResources('examples/Lookahead', 'jj')

        buildFile << """
            plugins {
                id 'java'
                id 'com.intershop.gradle.javacc'
            }

            version = '1.0.0.0'
            group = 'com.test.gradle'

            sourceCompatibility = 1.7
            targetCompatibility = 1.7

            sourceSets {
                example1
                example2
                example3
                example4
                example5
                example6
                example7
                example8
                example9
                example10
            }

            javacc {
                configs {
                    example1 {
                        sourceSetName = 'example1'
                        inputFile = file('jj/Example1.jj')
                        lookahead = '2'
                    }
                    example2 {
                        sourceSetName = 'example2'
                        inputFile = file('jj/Example2.jj')
                        lookahead = '2'
                    }
                    example3 {
                        sourceSetName = 'example3'
                        inputFile = file('jj/Example3.jj')
                        lookahead = '2'
                    }
                    example4 {
                        sourceSetName = 'example4'
                        inputFile = file('jj/Example4.jj')
                    }
                    example5 {
                        sourceSetName = 'example5'
                        inputFile = file('jj/Example5.jj')
                    }
                    example6 {
                        sourceSetName = 'example6'
                        inputFile = file('jj/Example6.jj')
                    }
                    example7 {
                        sourceSetName = 'example7'
                        inputFile = file('jj/Example7.jj')
                    }
                    example8 {
                        sourceSetName = 'example8'
                        inputFile = file('jj/Example8.jj')
                    }
                    example9 {
                        sourceSetName = 'example9'
                        inputFile = file('jj/Example9.jj')
                    }
                    example10 {
                        sourceSetName = 'example10'
                        inputFile = file('jj/Example10.jj')
                    }
                }
            }

            repositories {
                jcenter()
            }
        """.stripIndent()

        when:
        List<String> args = ['example1Classes', 'example2Classes', 'example4Classes', 'example5Classes', 'example6Classes', 'example7Classes', 'example8Classes', 'example9Classes', 'example10Classes', '-s']

        def result = getPreparedGradleRunner()
                .withArguments(args)
                .withGradleVersion(gradleVersion)
                .build()

        File parserFileExample1 = new File(testProjectDir, 'build/generated/javacc/example1/Example.java')
        File parserFileExample2 = new File(testProjectDir, 'build/generated/javacc/example2/Example.java')
        File parserFileExample3 = new File(testProjectDir, 'build/generated/javacc/example3/Example.java')
        File parserFileExample4 = new File(testProjectDir, 'build/generated/javacc/example4/Example.java')
        File parserFileExample5 = new File(testProjectDir, 'build/generated/javacc/example5/Example.java')
        File parserFileExample6 = new File(testProjectDir, 'build/generated/javacc/example6/Example.java')
        File parserFileExample7 = new File(testProjectDir, 'build/generated/javacc/example7/Example.java')
        File parserFileExample8 = new File(testProjectDir, 'build/generated/javacc/example8/Example.java')
        File parserFileExample9 = new File(testProjectDir, 'build/generated/javacc/example9/Example.java')
        File parserFileExample10 = new File(testProjectDir, 'build/generated/javacc/example10/Example.java')

        then:
        result.output.contains(':javaccExample1')
        result.output.contains(':compileExample1Java')
        parserFileExample1.exists()

        result.output.contains(':javaccExample2')
        result.output.contains(':compileExample2Java')
        parserFileExample2.exists()

        result.output.contains(':javaccExample4')
        result.output.contains(':compileExample4Java')
        parserFileExample4.exists()

        result.output.contains(':javaccExample5')
        result.output.contains(':compileExample5Java')
        parserFileExample5.exists()

        result.output.contains(':javaccExample6')
        result.output.contains(':compileExample6Java')
        parserFileExample6.exists()

        result.output.contains(':javaccExample7')
        result.output.contains(':compileExample7Java')
        parserFileExample7.exists()

        result.output.contains(':javaccExample8')
        result.output.contains(':compileExample8Java')
        parserFileExample8.exists()

        result.output.contains(':javaccExample9')
        result.output.contains(':compileExample9Java')
        parserFileExample9.exists()

        result.output.contains(':javaccExample10')
        result.output.contains(':compileExample10Java')
        parserFileExample10.exists()

        where:
        gradleVersion << supportedGradleVersions
    }

    def 'Test example VTransformer'(){
        given:
        copyResources('examples/VTransformer/jjt', 'jjt')
        copyResources('examples/VTransformer/jjtSrc', 'jjtSrc')

        buildFile << """
            plugins {
                id 'java'
                id 'com.intershop.gradle.javacc'
            }

            version = '1.0.0.0'
            group = 'com.test.gradle'

            sourceCompatibility = 1.7
            targetCompatibility = 1.7

            def javaCCOutDir = new File(project.buildDir, 'generated/javacc/vtransformer')

            javacc {
                configs {
                    vtransformer {
                        outputDir = javaCCOutDir
                        inputFile = file('jjt/Java1.1.jjt')
                        jjtree { }
                    }
                }
            }

            task copySrc(type: Copy) {
                from 'jjtSrc'
                into javaCCOutDir
            }

            javaccVtransformer.dependsOn copySrc

            repositories {
                jcenter()
            }
        """.stripIndent()

        when:
        List<String> args = ['compileJava', '-s']

        def result = getPreparedGradleRunner()
                .withArguments(args)
                .withGradleVersion(gradleVersion)
                .build()

        File parserFile = new File(testProjectDir, 'build/generated/javacc/vtransformer/JavaParser.java')

        then:
        result.output.contains(':javaccVtransformer')
        result.output.contains(':compileJava')
        parserFile.exists()

        where:
        gradleVersion << supportedGradleVersions
    }
}
