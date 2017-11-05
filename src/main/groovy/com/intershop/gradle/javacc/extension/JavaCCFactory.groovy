package com.intershop.gradle.javacc.extension

import groovy.transform.CompileStatic
import org.gradle.api.NamedDomainObjectFactory
import org.gradle.api.Project

@CompileStatic
class JavaCCFactory implements NamedDomainObjectFactory<JavaCC> {

    final Project project

    JavaCCFactory(Project project) {
        this.project = project
    }

    @Override
    JavaCC create(String name) {
        return new JavaCC(project, name )
    }
}