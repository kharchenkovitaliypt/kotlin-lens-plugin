package com.vitaliykharchenko.kotlin.lens

import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.codegen.ClassBuilder
import org.jetbrains.kotlin.codegen.DelegatingClassBuilder
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.resolve.jvm.diagnostics.JvmDeclarationOrigin
import org.jetbrains.org.objectweb.asm.MethodVisitor
import org.jetbrains.org.objectweb.asm.Opcodes
import org.jetbrains.org.objectweb.asm.Opcodes.ALOAD
import org.jetbrains.org.objectweb.asm.Type
import org.jetbrains.org.objectweb.asm.commons.InstructionAdapter
import kotlin.math.sign

class LensClassBuilder(
        val messageCollector: MessageCollector,
        val delegateBuilder: ClassBuilder,
        annotations: List<String>
) : DelegatingClassBuilder() {

    private val annotationFqNames = annotations.map(::FqName)

    override fun getDelegate(): ClassBuilder = delegateBuilder

    override fun newMethod(
            origin: JvmDeclarationOrigin, access: Int,
            name: String, desc: String,
            signature: String?, exceptions: Array<out String>?
    ): MethodVisitor {
        val original = super.newMethod(origin, access, name, desc, signature, exceptions)

//        messageCollector.report("newMethod() name: $name")

//        val descriptor = origin.descriptor as? FunctionDescriptor ?: return original
//        if (annotationFqNames.none { descriptor.annotations.hasAnnotation(it) }) {
//            return original
//        }
        return object : MethodVisitor(Opcodes.ASM5, original) {
            override fun visitCode() {
                super.visitCode()
                InstructionAdapter(this).apply {
                    messageCollector.report("begin() name: $name, desc: $desc, signature: ${signature}, exceptions: $exceptions")

                    getstatic("j/l/System",  "out", "Ljava/io/PrintStream;")
                    anew(Type.getObjectType("j/l/StringBuilder"))
                    dup()
                    invokespecial("j/l/StringBuilder", "<init>", "()V", false)
                    visitLdcInsn("⇢ $name(")
                    invokevirtual("j/l/StringBuilder", "append",
                            "(Lj/l/String;)Lj/l/StringBuilder;", false)
//                    function.valueParameters.forEachIndexed { i, param ->
//                        visitLdcInsn(" ${param.name}=")
//                        invokevirtual("j/l/StringBuilder", "append", "(Lj/l/String;)Lj/l/SB;", false)
//                        visitVarInsn(ALOAD, i + 1)
//                        invokevirtual("j/l/StringBuilder", "append", "(Lj/l/Object;)Lj/l/SB;", false)
//                    }
                    invokevirtual("j/l/StringBuilder", "toString", "()Lj/l/String;", false)
                    invokevirtual("j/io/PrintStream", "println", "(Lj/l/String;)V", false)
                }
            }
            override fun visitInsn(opcode: Int) {
                when (opcode) {
                    Opcodes.RETURN /* void */, Opcodes.ARETURN /* object */, Opcodes.IRETURN /* int */ -> {
                        InstructionAdapter(this).apply {
                            messageCollector.report("end() name: $name, opcode: $opcode")
                        }
                    }
                }
                super.visitInsn(opcode)
            }
        }
    }
}