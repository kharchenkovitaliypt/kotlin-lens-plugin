allprojects {
    repositories {
        jcenter()
        mavenLocal()
        maven(url = "https://dl.bintray.com/kotlin/kotlin-eap")
    }
}

//buildscript {
//    val kotlinVersion = "1.3.70-eap-42"
//    repositories {
////        mavenLocal()
//        jcenter()
//        maven {
//            url = uri("maven-repo")
//        }
//    }
//    dependencies {
//        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
////        classpath "com.github.hotchemi:gradle-compile-plugin-example:$kotlin_version"
//    }
//}
