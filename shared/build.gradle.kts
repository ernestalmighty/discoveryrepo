import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("com.squareup.sqldelight")
}

group = "jp.co.rakuten.oneapp"
version = "1.0-SNAPSHOT"

repositories {
    google()
}

kotlin {
    android() {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        publishLibraryVariants("release", "debug")
    }
    js(LEGACY) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }
    val iosTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget = when {
        System.getenv("SDK_NAME")?.startsWith("iphoneos") == true -> ::iosArm64
        else -> ::iosX64
    }

    iosTarget("ios") {
        binaries {
            framework("discoveryrepo") {
                baseName = "discoveryrepo"
                linkerOpts.add("-lsqlite3")
            }
        }
    }

    val coroutinesVersion = "1.5.0-native-mt"
    val serializationVersion = "1.2.2"
    val ktorVersion = "1.6.3"
    val sqlDelightVersion: String by project

    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation("com.google.android.material:material:1.2.1")
                implementation("io.ktor:ktor-client-android:$ktorVersion")
                implementation("com.squareup.sqldelight:android-driver:$sqlDelightVersion")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation("junit:junit:4.13")
            }
        }
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.2.2")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")
                implementation("com.squareup.sqldelight:runtime:$sqlDelightVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.2.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jsMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:sqljs-driver:1.5.0")
                implementation("io.ktor:ktor-client-js:$ktorVersion")
            }
        }
        val jsTest by getting

        val iosMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-ios:$ktorVersion")
                implementation("com.squareup.sqldelight:native-driver:$sqlDelightVersion")
            }
        }
    }
}

android {
    compileSdkVersion(31)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(31)
    }
}

sqldelight {
    database("DiscoveryDatabase") {
        packageName = "jp.co.rakuten.oneapp.shared.local"
    }
}

tasks {
    val libName = "discoveryrepo"
    register("buildDebugXCFramework", Exec::class.java) {
        description = "Create a Debug XCFramework"

        dependsOn("link${libName}DebugFrameworkIosArm64")
        dependsOn("link${libName}DebugFrameworkIosX64")

        val arm64FrameworkPath = "$rootDir/build/bin/iosArm64/${libName}DebugFramework/${libName}.framework"
        val arm64DebugSymbolsPath = "$rootDir/build/bin/iosArm64/${libName}DebugFramework/${libName}.framework.dSYM"

        val x64FrameworkPath = "$rootDir/build/bin/iosX64/${libName}DebugFramework/${libName}.framework"
        val x64DebugSymbolsPath = "$rootDir/build/bin/iosX64/${libName}DebugFramework/${libName}.framework.dSYM"

        val xcFrameworkDest = File("$rootDir/../kmp-xcframework-dest/$libName.xcframework")
        executable = "xcodebuild"
        args(mutableListOf<String>().apply {
            add("-create-xcframework")
            add("-output")
            add(xcFrameworkDest.path)

            // Real Device
            add("-framework")
            add(arm64FrameworkPath)
            add("-debug-symbols")
            add(arm64DebugSymbolsPath)

            // Simulator
            add("-framework")
            add(x64FrameworkPath)
            add("-debug-symbols")
            add(x64DebugSymbolsPath)
        })

        doFirst {
            xcFrameworkDest.deleteRecursively()
        }
    }
}

