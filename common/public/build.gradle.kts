plugins {
    id("activity.dashboard.library")
    id("activity.dashboard.build.config")
    alias(libs.plugins.appPlatform)
}

appPlatform {
    enableModuleStructure(true)
    enableKotlinInject(true)
    enableMoleculePresenters(true)
}