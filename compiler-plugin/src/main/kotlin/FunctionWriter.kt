package com.vitaliykharchenko.kotlin.lens

import com.vitaliykharchenko.kotlin.lens.SerialEntityNames.typeArgPrefix
import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.descriptors.annotations.AnnotationDescriptorImpl
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.descriptors.impl.SimpleFunctionDescriptorImpl
import org.jetbrains.kotlin.descriptors.impl.TypeParameterDescriptorImpl
import org.jetbrains.kotlin.descriptors.impl.ValueParameterDescriptorImpl
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.descriptorUtil.builtIns
import org.jetbrains.kotlin.resolve.descriptorUtil.module
import org.jetbrains.kotlin.storage.LockBasedStorageManager
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.KotlinTypeFactory
import org.jetbrains.kotlin.types.TypeProjectionImpl
import org.jetbrains.kotlin.types.Variance

fun createWriteSelfFunctionDescriptor(thisClass: ClassDescriptor): FunctionDescriptor {
    val jvmStaticClass = thisClass.module.findClassAcrossModuleDependencies(
        ClassId(
            FqName("kotlin.jvm"),
            Name.identifier("JvmStatic")
        )
    )!!
    val jvmStaticAnnotation = AnnotationDescriptorImpl(jvmStaticClass.defaultType, mapOf(), jvmStaticClass.source)
    val annotations = Annotations.create(listOf(jvmStaticAnnotation))

    val f = SimpleFunctionDescriptorImpl.create(
        thisClass,
        annotations,
        SerialEntityNames.WRITE_SELF_NAME,
        CallableMemberDescriptor.Kind.SYNTHESIZED,
        thisClass.source
    )

    val (typeArgs, argsKSer) = createKSerializerParamsForEachGenericArgument(f, thisClass, actualArgsOffset = 3)

    val args = mutableListOf<ValueParameterDescriptor>()

    // object
    val objectType = KotlinTypeFactory.simpleNotNullType(
        Annotations.EMPTY, thisClass, typeArgs.map { TypeProjectionImpl(it.defaultType) })

    args += ValueParameterDescriptor(
        containingDeclaration = f,
        index = 0,
        name = Name.identifier("self"),
        outType = objectType
    )

    // encoder
    args += ValueParameterDescriptor(
        containingDeclaration = f,
        index = 1,
        name = Name.identifier("output"),
        outType = thisClass.getClassFromSerializationPackage(SerialEntityNames.STRUCTURE_ENCODER_CLASS).toSimpleType()
    )

    //descriptor
    args += ValueParameterDescriptor(
        containingDeclaration = f,
        index = 2,
        name = Name.identifier("serialDesc"),
        outType = thisClass.getClassFromSerializationPackage(SerialEntityNames.SERIAL_DESCRIPTOR_CLASS).toSimpleType()
    )

    args.addAll(argsKSer)

    f.initialize(
        null,
        thisClass.thisAsReceiverParameter,
        typeArgs,
        args,
        f.builtIns.unitType,
        Modality.FINAL,
        Visibilities.PUBLIC
    )

    return f
}

@Suppress("FunctionName")
private fun ValueParameterDescriptor(
    containingDeclaration: CallableDescriptor,
    index: Int,
    name: Name,
    outType: KotlinType
) =
    ValueParameterDescriptorImpl(
        containingDeclaration = containingDeclaration,
        source = containingDeclaration.source,
        original = null,
        index = index,
        annotations = Annotations.EMPTY,
        name = name,
        outType = outType,
        declaresDefaultValue = false,
        isCrossinline = false,
        isNoinline = false,
        varargElementType = null
    )

/**
 * Creates free type parameters T0, T1, ... for given serializable class
 * Returns [T0, T1, ...] and [KSerializer<T0>, KSerializer<T1>,...]
 */
private fun createKSerializerParamsForEachGenericArgument(
    parentFunction: FunctionDescriptor,
    serializableClass: ClassDescriptor,
    actualArgsOffset: Int = 0
): Pair<List<TypeParameterDescriptor>, List<ValueParameterDescriptor>> {
    val serializerClass = serializableClass.getClassFromSerializationPackage(SerialEntityNames.KSERIALIZER_CLASS)
    val args = mutableListOf<ValueParameterDescriptor>()
    val typeArgs = mutableListOf<TypeParameterDescriptor>()
    var i = 0

    serializableClass.declaredTypeParameters.forEach { _ ->
        val targ = TypeParameterDescriptorImpl.createWithDefaultBound(
            parentFunction, Annotations.EMPTY, false, Variance.INVARIANT,
            Name.identifier("T$i"), i, LockBasedStorageManager.NO_LOCKS
        )

        val pType =
            KotlinTypeFactory.simpleNotNullType(Annotations.EMPTY, serializerClass, listOf(TypeProjectionImpl(targ.defaultType)))

        args.add(
            ValueParameterDescriptorImpl(
                containingDeclaration = parentFunction,
                original = null,
                index = actualArgsOffset + i,
                annotations = Annotations.EMPTY,
                name = Name.identifier("$typeArgPrefix$i"),
                outType = pType,
                declaresDefaultValue = false,
                isCrossinline = false,
                isNoinline = false,
                varargElementType = null,
                source = parentFunction.source
            )
        )

        typeArgs.add(targ)
        i++
    }

    return typeArgs to args
}