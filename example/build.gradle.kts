import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("com.vitaliykharchenko.kotlin.lens") version "0.0.1"
}

lens {
    annotations = listOf("hello.world")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    testImplementation("junit:junit:4.12")
}

tasks.withType<KotlinCompile> {
    println("Hello from kotlin compile- $name")
    kotlinOptions {
        useIR = true
    }
}
