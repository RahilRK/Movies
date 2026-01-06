
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android") // Apply Hilt Plugin
}

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.rk.movies"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // AndroidX Core (KTX, AppCompat, Legacy)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.legacy.support.v4)

    // Material Design & ConstraintLayout (UI Components)
    implementation(libs.google.android.material)
    implementation(libs.androidx.constraintlayout)

    // Jetpack Navigation (Fragment & UI)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Retrofit & OkHttp (Networking)
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.converter.gson)
    implementation(libs.squareup.okhttp.logging.interceptor)

    // Kotlin Coroutines (Concurrency)
    implementation(libs.kotlinx.coroutines.core)

    // Glide (Image Loading)
    implementation(libs.github.bumptech.glide)
    kapt(libs.github.bumptech.glide.compiler)

    // Hilt (Dependency Injection)
    implementation(libs.hilt.android) // Hilt Android Library
    kapt(libs.hilt.compiler) // Hilt Compiler

    // JUnit & Espresso (Testing)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

