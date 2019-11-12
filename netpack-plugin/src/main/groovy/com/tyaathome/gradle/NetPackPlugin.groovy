package com.tyaathome.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class NetPackPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.dependencies.add("implementation", "com.tyaathome:netpack-annotation:0.0.5")
        project.dependencies.add("annotationProcessor", "com.tyaathome:netpack-processor:0.0.5")
    }
}