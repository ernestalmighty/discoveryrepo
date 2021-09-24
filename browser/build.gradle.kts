plugins {
    kotlin("js")
}

group = "jp.co.rakuten.oneapp"
version = "1.0-SNAPSHOT"

dependencies {
    testImplementation(kotlin("test"))
    implementation(npm("copy-webpack-plugin", "5.1.1"))
}

kotlin {
    js(LEGACY) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }
}