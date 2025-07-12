import PluginIds.KOTLIN_MULTIPLATFORM
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Sync
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import java.util.Properties

/**
 * A Gradle plugin for Kotlin Multiplatform projects that generates a simple `Env.kt` file
 * containing environment-specific constants (such as `IS_DEBUG`) based on values defined in `local.properties`.
 *
 * This plugin is designed to:
 * - Load values from a `local.properties` file at the root of the project.
 * - Dynamically generate a Kotlin source file at build time containing constants to be used in code.
 * - Automatically add the generated source directory to the `commonMain` source set.
 * - Ensure the generated code is available for the `build` and `wasmJsBrowserDistribution` tasks.
 *
 * This plugin is intended only for the `common:public` module
 * located at the Gradle path `:common:public`.
 * Applying it elsewhere may result in incorrect behavior.
 */
class CommonBuildConfigPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = with(project) {
        if (path != ":common:public") {
            error("BuildConfigPlugin should only be applied to :common:public module. Current module: $path")
        }

        val localProperties = Properties().apply {
            val file = rootProject.file("local.properties")
            if (file.exists()) {
                file.inputStream().use { load(it) }
            }
        }

        val apiBaseUrl = localProperties.getProperty("API_BASE_URL") ?: "https://api.fitbit.com"
        val isDebug = localProperties.getProperty("isDebug")?.toBoolean() ?: false
        val buildConfigGenerator = tasks.register("buildConfigGenerator", Sync::class) {
            from(
                resources.text.fromString(
                    """
                    |package activity.dashboard.common
                    |
                    |object Env {
                    |    const val IS_DEBUG = $isDebug
                    |    const val API_BASE_URL = "$apiBaseUrl"
                    |}
                    """.trimMargin()
                )
            ) {
                rename { "Env.kt" }
                into("activity/dashboard/common")
            }

            into(layout.buildDirectory.dir("generated-src/kotlin/"))
        }

        plugins.withId(KOTLIN_MULTIPLATFORM) {
            extensions.configure<KotlinMultiplatformExtension>("kotlin") {
                sourceSets.named("commonMain") {
                    kotlin.srcDir(buildConfigGenerator.map { it.destinationDir })
                }
            }
        }

        // Attach the generated task to relevant build tasks
        tasks.matching { it.name == "wasmJsBrowserDistribution" || it.name == "build" }.configureEach {
            dependsOn(buildConfigGenerator)
        }
    }
}
