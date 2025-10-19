plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.goomer.ps"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.goomer.ps"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "BASE_URL", "\"https://api.production.com/\"")
            buildConfigField("boolean", "ENABLE_LOGGING", "false")
        }

        create("preProd") {
            isMinifyEnabled = false
            applicationIdSuffix = ".preprod"
            versionNameSuffix = "-preprod"
            buildConfigField("String", "BASE_URL", "\"https://api.preprod.com/\"")
            buildConfigField("boolean", "ENABLE_LOGGING", "true")
        }

        create("mock") {
            isMinifyEnabled = false
            applicationIdSuffix = ".mock"
            versionNameSuffix = "-mock"
            buildConfigField("String", "BASE_URL", "\"https://api.mock.com/\"")
            buildConfigField("boolean", "ENABLE_LOGGING", "true")
            buildConfigField("boolean", "USE_MOCK_DATA", "true")
        }

        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            buildConfigField("String", "BASE_URL", "\"https://api.debug.com/\"")
            buildConfigField("boolean", "ENABLE_LOGGING", "true")
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    // Android Core Bundle
    implementation(libs.bundles.androidx.core)
    
    // Kotlin Coroutines Bundle
    implementation(libs.bundles.kotlin.coroutines)

    // Koin Dependency Injection Bundle
    implementation(libs.bundles.koin)

    // Moshi JSON Serialization Bundle
    implementation(libs.bundles.moshi)
    ksp(libs.moshi.kotlin.codegen)

    // Legacy Gson
    implementation(libs.gson)

    // Testing Bundle
    testImplementation(libs.bundles.testing)
    testImplementation(libs.koin.test)
    testImplementation(libs.kotlinx.coroutines.test)
}