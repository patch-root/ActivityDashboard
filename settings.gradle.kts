rootProject.name = "ActivityDashboard"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

includeBuild("build-logic")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

// Top level application module
include(":composeApp")
include(":about:impl")
include(":about:public")
include(":common:public")
include(":fitbit:impl")
include(":fitbit:public")
include(":login:impl")
include(":login:public")
include(":navigation:impl")
include(":navigation:public")
include(":settings:impl")
include(":settings:public")
include(":templates:impl")
include(":templates:public")
include(":user:impl")
include(":user:public")