plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("com.squareup.sqldelight")
}

kotlin {
    android()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {
        /* Main source sets */
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
                implementation("co.touchlab:kermit:1.0.0")
                implementation("io.ktor:ktor-client-core:2.0.0-beta-1")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.1")

                implementation("com.squareup.sqldelight:coroutines-extensions:1.5.3")
                implementation("io.github.aakira:napier:2.3.0")

                implementation("com.arkivanov.mvikotlin:mvikotlin:3.0.0-beta01")
                implementation("com.arkivanov.mvikotlin:mvikotlin-main:3.0.0-beta01")
                implementation("com.arkivanov.mvikotlin:mvikotlin-logging:3.0.0-beta01")
                implementation("com.arkivanov.mvikotlin:mvikotlin-timetravel:3.0.0-beta01")
                implementation("com.arkivanov.mvikotlin:mvikotlin-extensions-coroutines:3.0.0-beta01")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:2.0.0-beta-1")
                implementation("com.squareup.sqldelight:android-driver:1.5.3")
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependencies {
                implementation("io.ktor:ktor-client-ios:2.0.0-beta-1")
            }
        }
        val nativeMain by creating {
            dependencies {
                implementation("com.squareup.sqldelight:native-driver:1.5.3")
            }
        }

        /* Main hierarchy */
        androidMain.dependsOn(commonMain)
        iosMain.dependsOn(nativeMain)
        iosX64Main.dependsOn(iosMain)
        iosArm64Main.dependsOn(iosMain)
        iosSimulatorArm64Main.dependsOn(iosMain)
        nativeMain.dependsOn(commonMain)
    }
}

android {
    compileSdk = 31
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        targetSdk = 31
    }
}
