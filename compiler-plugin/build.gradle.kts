import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile

plugins {
    kotlin("jvm") version "1.3.70-eap-42"
    kotlin("kapt") version "1.3.70-eap-42"
    id("maven-publish")
}

tasks.withType<KotlinJvmCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.vitaliykharchenko"
            artifactId = "kotlin-lens-compiler-plugin"
            version = "0.0.1"

            from(components["java"])
        }
    }
}

dependencies {
    implementation(kotlin("stdlib", version = "1.3.70-eap-42"))
    compileOnly(kotlin("compiler-embeddable", version = "1.3.70-eap-42"))

    compileOnly("com.google.auto.service:auto-service:1.0-rc6")
    kapt("com.google.auto.service:auto-service:1.0-rc6")
}