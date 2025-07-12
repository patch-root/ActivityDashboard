plugins {
    id("activity.dashboard.library")
    alias(libs.plugins.appPlatform)
}

appPlatform {
    enableComposeUi(true)
    enableModuleStructure(true)
    enableKotlinInject(true)
    enableMoleculePresenters(true)
}
