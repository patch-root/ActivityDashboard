plugins {
    id("activity.dashboard.library")
    alias(libs.plugins.appPlatform)
}

appPlatform {
    enableModuleStructure(true)
    enableComposeUi(true)
    enableKotlinInject(true)
    enableMoleculePresenters(true)
}

dependencies {
    commonMainApi(project(":about:public"))
    commonMainApi(project(":common:public"))
    commonMainApi(project(":fitbit:public"))
    commonMainApi(project(":user:public"))
    commonMainImplementation(libs.coil3.compose)
}

