plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.plugin.serialization") version "2.4.0"
}

android {
    namespace = "com.augistlk.irklavimas"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.augistlk.irklavimas"
        minSdk = 31
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

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
    useLibrary("wear-sdk")
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Jetpack Compose Integration
    implementation("androidx.navigation:navigation-compose:2.9.8")

    // Typed DataStore for custom data objects (for example, using Proto or JSON).
    implementation("androidx.datastore:datastore:1.2.1")
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.material3)

    // JSON Serializer
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
    // Location stuff
    implementation("com.google.android.gms:play-services-location:21.4.0")

    implementation(libs.play.services.wearable)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.compose.ui.tooling)
    implementation(libs.androidx.wear.tooling.preview)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}