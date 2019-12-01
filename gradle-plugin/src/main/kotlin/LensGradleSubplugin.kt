package com.vitaliykharchenko.kotlin.lens

import com.google.auto.service.AutoService
import org.gradle.api.Project
import org.gradle.api.tasks.compile.AbstractCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonOptions
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinGradleSubplugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

@AutoService(KotlinGradleSubplugin::class)
class LensGradleSubplugin : KotlinGradleSubplugin<AbstractCompile> {

    override fun isApplicable(project: Project, task: AbstractCompile): Boolean =
            project.plugins.hasPlugin(LensGradlePlugin::class.java)

    override fun getCompilerPluginId(): String = "lens"

    override fun getPluginArtifact() = SubpluginArtifact(
            groupId = "com.vitaliykharchenko", artifactId = "kotlin-lens-compiler-plugin", version = "0.0.1")

    override fun apply(
            project: Project,
            kotlinCompile: AbstractCompile,
            javaCompile: AbstractCompile?,
            variantData: Any?,
            androidProjectHandler: Any?,
            kotlinCompilation: KotlinCompilation<KotlinCommonOptions>?
    ): List<SubpluginOption> {
        val extension = project.extensions
                .findByType(LensGradleExtension::class.java)
                ?: LensGradleExtension()

        if (extension.enabled && extension.annotations.isEmpty()) {
            error("Lens is enabled, but no annotations were set")
        }
        val annotationOptions = extension.annotations
                .map { SubpluginOption(key = "annotation", value = it) }
        val enabledOption = SubpluginOption(
                key = "enabled", value = extension.enabled.toString())

        return annotationOptions + enabledOption
    }
}