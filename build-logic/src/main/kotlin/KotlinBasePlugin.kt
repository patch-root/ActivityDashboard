import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.JavaExec
import PluginIds.COMPOSE_COMPILER
import PluginIds.COMPOSE_MULTIPLATFORM
import PluginIds.KOTLIN_MULTIPLATFORM
import PluginIds.KSP
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

open class KotlinBasePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.pluginManager.apply(KOTLIN_MULTIPLATFORM)
        project.pluginManager.apply(KSP)
        project.pluginManager.apply(COMPOSE_MULTIPLATFORM)
        project.pluginManager.apply(COMPOSE_COMPILER)

        project.configureCompose()
        project.configureKotlinMultiPlatform()

        project.afterEvaluate {
            project.configureKtLint()
        }
    }

    private fun Project.configureKotlinMultiPlatform() {
        val name = project.camelCaseModuleName()
        project.plugins.withId(KOTLIN_MULTIPLATFORM) {
            project.extensions.getByType(KotlinMultiplatformExtension::class.java).apply {
                // WASM targets
                @OptIn(ExperimentalWasmDsl::class)
                wasmJs {
                    moduleName = name
                    browser {
                        val rootDirPath = project.rootDir.path
                        val projectDirPath = project.projectDir.path
                        commonWebpackConfig {
                            outputFileName = "${name}.js"
                            devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                                static = (static ?: mutableListOf()).apply {
                                    // Serve sources to debug inside the browser
                                    add(rootDirPath)
                                    add(projectDirPath)
                                }
                            }
                        }
                    }
                    binaries.executable()
                }

                sourceSets.getByName("wasmJsTest").dependencies {
                    implementation(project.libsCatalog.findLibrary("kotlin-test").get())
                }
            }
        }
    }

    private fun Project.configureCompose() {
        plugins.withId(KOTLIN_MULTIPLATFORM) {
            project.kmp.sourceSets.getByName("commonMain").dependencies {
                implementation(project.libsCatalog.findLibrary("compose-runtime").get())
                implementation(project.libsCatalog.findLibrary("compose-foundation").get())
                implementation(project.libsCatalog.findLibrary("compose-material").get())
                implementation(project.libsCatalog.findLibrary("compose-ui").get())
                implementation(project.libsCatalog.findLibrary("compose-components-resources").get())
                implementation(project.libsCatalog.findLibrary("compose-components-ui-tooling").get())
            }

            project.kmp.sourceSets.findByName("wasmJsMain")?.dependencies {
                implementation(project.libsCatalog.findLibrary("compose-html-core").get())
            }
        }
    }

    private fun Project.configureKtLint() {
        val ktlintConfig = configurations.create("ktlint")
        dependencies.add(ktlintConfig.name, "com.pinterest.ktlint:ktlint-cli:${ext.ktlintVersion}")

        tasks.register("ktlint", JavaExec::class.java) {
            description = "Checks the Kotlin code and formatting."
            classpath = ktlintConfig
            mainClass.set("com.pinterest.ktlint.Main")
            args("src/**/*.kt", "!build/**")
        }

        tasks.register("ktlintFormat", JavaExec::class.java) {
            description = "Fix the Kotlin code formatting errors found from `ktlint`."
            jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
            classpath = ktlintConfig
            mainClass.set("com.pinterest.ktlint.Main")
            args("-F", "src/**/*.kt", "!build/**")
        }
    }
}
