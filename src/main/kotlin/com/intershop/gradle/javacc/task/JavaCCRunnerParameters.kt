package com.intershop.gradle.javacc.task

import org.gradle.api.provider.Property
import org.gradle.api.provider.ListProperty
import org.gradle.workers.WorkParameters
import java.io.File

interface JavaCCRunnerParameters : WorkParameters {

    val outputDir: Property<File>

    val inputFile: Property<File>

    val javaCCParamList: ListProperty<String>

    val jjTreeParamList: ListProperty<String>
}