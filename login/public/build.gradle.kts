plugins {
    id("activity.dashboard.library")
    alias(libs.plugins.appPlatform)
}

appPlatform {
    enableModuleStructure(true)
    enableKotlinInject(true)
    enableMoleculePresenters(true)
}


