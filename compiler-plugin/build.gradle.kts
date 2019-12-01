plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("maven-publish")
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
    implementation(kotlin("stdlib"))
    compileOnly(kotlin("compiler-embeddable"))

    compileOnly("com.google.auto.service:auto-service:1.0-rc6")
    kapt("com.google.auto.service:auto-service:1.0-rc6")
}