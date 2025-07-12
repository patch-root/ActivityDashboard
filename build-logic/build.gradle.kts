plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

gradlePlugin {
    plugins {
        register("appPlugin") {
            id = "activity.dashboard.app"
            implementationClass = "AppPlugin"
        }
        register("libraryPlugin") {
            id = "activity.dashboard.library"
            implementationClass = "LibraryPlugin"
        }
        register("featurePlugin") {
            id = "activity.dashboard.feature"
            implementationClass = "FeaturePlugin"
        }
        register("buildConfigPlugin") {
            id = "activity.dashboard.build.config"
            implementationClass = "CommonBuildConfigPlugin"
        }
    }
}

apply(from = rootProject.file("../dependencies.gradle"))

dependencies {
    implementation(libs.kotlin.gradle.plugin)
}