import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.goomer.ps.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 25
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
        
        create("preProd") {
            isMinifyEnabled = false
        }
        
        create("mock") {
            isMinifyEnabled = false
        }
        
        debug {
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    // Core and Domain modules
    implementation(project(":core"))
    implementation(project(":domain"))

    // Kotlin Coroutines
    implementation(libs.bundles.kotlin.coroutines)

    // Gson for JSON parsing
    implementation(libs.gson)

    // Testing
    testImplementation(libs.bundles.testing)
    testImplementation(libs.bundles.mockito)
    testImplementation(libs.kotlinx.coroutines.test)
}

