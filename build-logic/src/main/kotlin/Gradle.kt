import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

val ExtraPropertiesExtension.compileSdkVersion: Int
    get() = get("compile_sdk_version") as Int
val ExtraPropertiesExtension.ktlintVersion: String
    get() = get("ktlint_version") as String

/**
 * Gets the 'libs' version catalog.
 */
val ExtensionAware.libsCatalog: VersionCatalog
    get() = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

/**
 * Allows for accessing all [ExtraPropertiesExtension].
 */
val ExtensionAware.ext: ExtraPropertiesExtension
    get() {
        val extensionContainer = if (this is Project) {
            rootProject.extensions
        } else {
            extensions
        }
        return extensionContainer.getByType(ExtraPropertiesExtension::class.java)
    }

val Project.kmp: KotlinMultiplatformExtension
    get() = extensions.getByType(KotlinMultiplatformExtension::class.java)

fun Project.getFormattedName() : String {
    return path.removePrefix(":").replace(":", ".")
}

fun Project.camelCaseModuleName(): String {
    return path
        .split(":")
        .filter { it.isNotEmpty() }
        .mapIndexed { index, segment ->
            if (index == 0) segment
            else segment.replaceFirstChar { it.uppercaseChar() }
        }
        .joinToString("")
}