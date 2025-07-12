plugins {
    id("activity.dashboard.library")
    alias(libs.plugins.appPlatform)
}

appPlatform {
    enableModuleStructure(true)
    enableKotlinInject(true)
    enableMoleculePresenters(true)
}

dependencies {
    // Likely should change this from "user"
    commonMainApi(project(":fitbit:public"))
}