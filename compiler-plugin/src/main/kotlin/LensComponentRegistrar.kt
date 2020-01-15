package com.vitaliykharchenko.kotlin.lens

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.backend.common.BackendContext
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.codegen.extensions.ClassBuilderInterceptorExtension
import org.jetbrains.kotlin.com.intellij.mock.MockProject
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.resolve.BindingContext

@AutoService(ComponentRegistrar::class)
class LensComponentRegistrar : ComponentRegistrar {
    override fun registerProjectComponents(
            project: MockProject,
            configuration: CompilerConfiguration
    ) {
        if (configuration[KEY_ENABLED] == false) {
            return
        }
        val annotations = configuration[KEY_ANNOTATIONS]
                ?: error("lens plugin requires at least one annotation class option passed to it")

//        ClassBuilderInterceptorExtension.registerExtension(
//                project, LensClassGenerationInterceptor(configuration.messageCollector, annotations))

        IrGenerationExtension.registerExtension(project, object : IrGenerationExtension {
            override fun generate(file: IrFile, backendContext: BackendContext, bindingContext: BindingContext) {
                generateIr(file, backendContext, bindingContext, configuration)
            }
        })
    }
}

fun MessageCollector.report(message: String) {
    report(CompilerMessageSeverity.INFO, "TAG, $message")
}

val CompilerConfiguration.messageCollector: MessageCollector
    get() = get(CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY)!!