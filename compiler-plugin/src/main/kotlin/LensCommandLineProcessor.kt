package com.vitaliykharchenko.kotlin.lens

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey

val KEY_ENABLED = CompilerConfigurationKey<Boolean>("enabled")
val KEY_ANNOTATIONS = CompilerConfigurationKey<List<String>>("annotations")

@AutoService(CommandLineProcessor::class)
class LensCommandLineProcessor : CommandLineProcessor {

    override val pluginId: String = "lens"

    override val pluginOptions: Collection<CliOption> = listOf(
            CliOption("enabled", "<true|false>", "whether plugin is enabled"),
            CliOption("annotation", "<fqname>", "lens annotation names",
                    required = true, allowMultipleOccurrences = true))

    override fun processOption(
            option: AbstractCliOption,
            value: String,
            configuration: CompilerConfiguration
    ) = when (option.optionName) {
        "enabled" -> configuration.put(KEY_ENABLED, value.toBoolean())
        "annotation" -> configuration.appendList(KEY_ANNOTATIONS, value)
        else -> error("Unexpected config option ${option.optionName}")
    }
}