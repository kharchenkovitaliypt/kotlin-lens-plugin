package com.vitaliykharchenko.kotlin.lens

import org.gradle.api.Plugin
import org.gradle.api.Project

class LensGradlePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create("lens", LensGradleExtension::class.java)
    }
}