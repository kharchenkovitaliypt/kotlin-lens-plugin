package com.vitaliykharchenko.kotlin.lens

import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.codegen.ClassBuilderFactory
import org.jetbrains.kotlin.codegen.extensions.ClassBuilderInterceptorExtension
import org.jetbrains.kotlin.diagnostics.DiagnosticSink
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.jvm.diagnostics.JvmDeclarationOrigin

class LensClassGenerationInterceptor(
        val messageCollector: MessageCollector,
        val annotations: List<String>
) : ClassBuilderInterceptorExtension {

    override fun interceptClassBuilderFactory(
            interceptedFactory: ClassBuilderFactory,
            bindingContext: BindingContext,
            diagnostics: DiagnosticSink
    ): ClassBuilderFactory =
    object: ClassBuilderFactory by interceptedFactory {
        override fun newClassBuilder(origin: JvmDeclarationOrigin) =
                LensClassBuilder(
                        messageCollector = messageCollector,
                        delegateBuilder = interceptedFactory.newClassBuilder(origin),
                        annotations = annotations
                )
    }
}