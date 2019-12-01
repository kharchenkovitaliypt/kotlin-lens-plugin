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
