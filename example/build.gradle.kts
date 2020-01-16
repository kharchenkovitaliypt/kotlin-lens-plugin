import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.70-eap-42"
    id("com.vitaliykharchenko.kotlin.lens") version "0.0.1"
}

lens {
    annotations = listOf("hello.world")
}

dependencies {
    implementation(kotlin("stdlib", version = "1.3.70-eap-42"))
    implementation(kotlin("reflect", version = "1.3.70-eap-42"))

    testImplementation("junit:junit:4.12")
}

tasks.withType<KotlinCompile> {
    println("Hello from kotlin compile- $name")
    kotlinOptions {
        useIR = true
        jvmTarget = "1.8"
    }
}
