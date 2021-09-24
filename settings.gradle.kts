pluginManagement {
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
        mavenCentral()
    }
    
}
rootProject.name = "discoveryrepo"


include(":browser")
include(":shared")
include(":android")

