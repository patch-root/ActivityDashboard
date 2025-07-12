import PluginIds.KOTLIN_MULTIPLATFORM
import org.gradle.api.Plugin
import org.gradle.api.Project

class FeaturePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply(LibraryPlugin::class.java)

        project.plugins.withId(KOTLIN_MULTIPLATFORM) {
            project.kmp.sourceSets.getByName("commonMain").dependencies {
                implementation(project(":core:public"))
                implementation(project(":navigation:public"))
            }
        }
    }
}