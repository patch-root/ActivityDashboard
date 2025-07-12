import org.gradle.kotlin.dsl.commonMainApi

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

dependencies {
    commonMainApi(project(":about:public"))
    commonMainApi(project(":common:public"))
    commonMainApi(project(":user:public"))
    commonMainApi(project(":login:public"))
    commonMainApi(project(":templates:public"))
    commonMainApi(project(":settings:public"))

    commonMainImplementation(libs.coil3.compose)
    commonMainImplementation(libs.coil3.ktor)
    commonMainImplementation(libs.compose.material.icons)
}