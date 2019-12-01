plugins {
    id("java-gradle-plugin")
    kotlin("jvm")
    kotlin("kapt")
    id("maven-publish")
}

group = "com.vitaliykharchenko"
version = "0.0.1"

gradlePlugin {
    plugins {
        create("lens") {
            id = "$group.kotlin.lens"
            implementationClass = "$group.kotlin.lens.LensGradlePlugin"
        }
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("gradle-plugin-api"))

    compileOnly("com.google.auto.service:auto-service:1.0-rc6")
    kapt("com.google.auto.service:auto-service:1.0-rc6")
}