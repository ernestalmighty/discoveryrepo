plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android-extensions")
}

group = "jp.co.rakuten.oneapp"
version = "1.0-SNAPSHOT"

repositories {
    google()
    jcenter()
}

dependencies {
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.2")
    implementation(project(":shared"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0")
    implementation("com.squareup.sqldelight:android-driver:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
    implementation("io.ktor:ktor-client-android:1.6.3")
    implementation("io.ktor:ktor-client-serialization:1.6.3")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.0")
}

android {
    compileSdkVersion(29)
    defaultConfig {
        applicationId = "jp.co.rakuten.oneapp.android"
        minSdkVersion(24)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}