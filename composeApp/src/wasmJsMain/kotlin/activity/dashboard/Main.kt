package activity.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import software.amazon.app.platform.renderer.ComposeRendererFactory
import software.amazon.app.platform.scope.di.diComponent

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(checkNotNull(document.body)) { AppPlatform() }
}

@Composable
private fun AppPlatform() {
    val application =
        remember {
            Application().apply { create(WasmJsAppComponent::class.create(this)) }
        }

    val coroutineScope = rememberCoroutineScope()
    // Set up experimental hash-based routing to toggle the About view based on the URL anchor (e.g. #privacy)
    // This allows directly linking to the about page to display the privacy information.
    DisposableEffect(Unit) {
        handleHashRouting(
            aboutRepository =
                application.rootScope
                    .diComponent<WasmJsAppComponent>()
                    .aboutRepository,
            scope = coroutineScope,
        )
        onDispose {}
    }

    // Create a single instance.
    val templateProvider =
        remember {
            application.rootScope
                .diComponent<WasmJsAppComponent>()
                .templateProviderFactory
                .createTemplateProvider()
        }

    DisposableEffect(Unit) {
        onDispose {
            templateProvider.cancel()
        }
    }

    val factory = remember { ComposeRendererFactory(application) }
    val template by templateProvider.templates.collectAsState()
    val renderer = factory.getRenderer(template::class)
    renderer.renderCompose(template)
}
