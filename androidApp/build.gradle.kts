plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 32
    defaultConfig {
        applicationId = "on.the.stove.android"
        minSdk = 28
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true
    }
    buildFeatures {
        // Enables Jetpack Compose for this module
        compose = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.1"
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")


    // Integration with activities
    implementation("androidx.activity:activity-compose:1.4.0")
    // Compose Material Design
    implementation("androidx.compose.material:material:1.2.0-alpha05")
    // Animations
    implementation("androidx.compose.animation:animation:1.2.0-alpha05")
    // Tooling support (Previews, etc.)
    implementation("androidx.compose.ui:ui-tooling:1.2.0-alpha05")
    implementation("androidx.compose.material:material-icons-extended:1.2.0-alpha05")
    // Integration with ViewModels
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.0-alpha03")

    implementation("androidx.navigation:navigation-compose:2.4.1")

    implementation("io.coil-kt:coil:1.4.0")
    implementation("io.coil-kt:coil-compose:1.4.0")
    implementation("com.github.skydoves:landscapist-coil:1.4.8")

    implementation("io.insert-koin:koin-android:3.1.2")
    implementation("com.google.accompanist:accompanist-pager:0.24.3-alpha")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.24.3-alpha")
}