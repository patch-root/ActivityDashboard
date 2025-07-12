plugins {
    id("activity.dashboard.library")
    alias(libs.plugins.appPlatform)
    alias(libs.plugins.kotlinSerialization)
}

appPlatform {
    enableModuleStructure(true)
    enableKotlinInject(true)
    enableMoleculePresenters(true)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
        }
    }
}

