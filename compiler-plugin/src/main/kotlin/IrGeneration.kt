package com.vitaliykharchenko.kotlin.lens

import org.jetbrains.kotlin.backend.common.BackendContext
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.builders.declarations.buildClass
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid
import org.jetbrains.kotlin.ir.visitors.acceptVoid
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.BindingContext

fun generateIr(
    irFile: IrFile,
    backendContext: BackendContext,
    bindingContext: BindingContext,
    configuration: CompilerConfiguration
) {
    val report: (String) -> Unit = configuration.messageCollector::report

//    val text = irFile.declarations.joinToString { it.descriptor.name.identifier }
//    report("irFile: $text")

    buildClass {
       name = Name.identifier("hello.here")
    }

    irFile.acceptVoid(object : IrElementVisitorVoid {
        override fun visitElement(element: IrElement) {
            element.acceptChildrenVoid(this)
        }

        override fun visitClass(declaration: IrClass) {
            declaration.acceptChildrenVoid(this)
        }
    })
}