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
    commonMainApi(project(":common:public"))
    // Likely should change this from "user"
    commonMainApi(project(":fitbit:public"))
    commonMainApi(project(":templates:public"))
    commonMainApi(project(":settings:public"))
    commonMainImplementation(libs.compose.material.icons)
    commonMainImplementation(libs.kotlinx.datetime)
}
