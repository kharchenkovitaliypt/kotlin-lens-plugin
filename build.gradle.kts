allprojects {
    repositories {
        jcenter()
        mavenLocal()
    }
}

buildscript {
    val kotlinVersion = "1.3.61"
    repositories {
//        mavenLocal()
        jcenter()
        maven {
            url = uri("maven-repo")
        }
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
//        classpath "com.github.hotchemi:gradle-compile-plugin-example:$kotlin_version"
    }
}
