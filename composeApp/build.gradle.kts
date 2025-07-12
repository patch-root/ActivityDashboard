plugins {
    id("activity.dashboard.app")
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.appPlatform)
}

appPlatform {
    enableKotlinInject(true)
    addImplModuleDependencies(true)
    enableComposeUi(true)
    enableMoleculePresenters(true)
}

dependencies {
    commonMainImplementation(project(":about:impl"))
    commonMainImplementation(project(":login:impl"))
    commonMainImplementation(project(":navigation:impl"))
    commonMainImplementation(project(":templates:impl"))
    commonMainImplementation(project(":user:impl"))
    commonMainImplementation(project(":fitbit:impl"))
    commonMainImplementation(project(":settings:impl"))

    commonMainImplementation(project(":common:public"))
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.androidx.navigation.compose)
        }
    }
}
