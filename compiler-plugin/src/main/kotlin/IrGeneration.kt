package com.vitaliykharchenko.kotlin.lens

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.builders.declarations.buildClass
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid
import org.jetbrains.kotlin.ir.visitors.acceptVoid
import org.jetbrains.kotlin.name.Name

fun generateIr(
    moduleFragment: IrModuleFragment,
    pluginContext: IrPluginContext,
    configuration: CompilerConfiguration
) {
    val report: (String) -> Unit = configuration.messageCollector::report

//    buildClass {
//       name = Name.identifier("hello.here")
//    }

    report("Module fragment: " + moduleFragment)

    moduleFragment.files.forEach { irFile ->
        val text = irFile.declarations.joinToString { it.descriptor.name.identifier }
        report("irFile: $text")

        irFile.acceptVoid(object : IrElementVisitorVoid {
            override fun visitElement(element: IrElement) {
                element.acceptChildrenVoid(this)
            }

            override fun visitClass(declaration: IrClass) {
                declaration.acceptChildrenVoid(this)
            }
        })
    }


}