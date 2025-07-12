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
    commonMainApi(project(":user:public"))
    commonMainApi(project(":common:public"))
    commonMainImplementation(libs.compose.material.icons)
    commonMainImplementation(libs.kotlinx.datetime)
}
