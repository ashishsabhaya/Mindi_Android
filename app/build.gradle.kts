plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.navigation.safe.args)
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.keylogic.mindi"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.keylogic.mindi"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.play.services.ads.api)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.lifecycle.process)

    implementation(libs.sdp.android)
    implementation(libs.ssp.android)

    // Room components
    implementation(libs.androidx.room.runtime)
    //noinspection KaptUsageInsteadOfKsp
    kapt(libs.androidx.room.compiler) // for annotation processing

    // Kotlin Coroutines support for Room
    implementation(libs.androidx.room.ktx)

    implementation(libs.gson)

    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)

//    implementation(libs.bom)
//    implementation(libs.storage.kt)
//    implementation(libs.supabase.kt)
//    implementation(libs.postgrest.kt)
//    implementation(libs.realtime.kt)
//    implementation(libs.auth.kt)
//
//    implementation(libs.ktor.client.core)
//    implementation(libs.ktor.client.cio)
//    implementation(libs.ktor.client.websockets)
//
//    implementation(libs.ktor.client.android)
//    implementation(libs.ktor.utils)
//    implementation(libs.ktor.client.okhttp)

}