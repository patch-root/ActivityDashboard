package activity.dashboard

import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Observes the browser's URL hash and updates the application state accordingly.
 *
 * This is an experimental approach to handling routing in a Compose for Web (WASM) application
 * using URL anchors (e.g. `#privacy`). It reads the current hash on startup and sets up a listener
 * for future hash changes using the browser's native `onhashchange` event.
 *
 * If the hash is `#privacy`, it updates the [AboutRepository] to show the About view. Otherwise,
 * it clears that state.
 *
 * This approach allows basic view-based routing without a full navigation framework. It should be
 * considered a temporary or lightweight solution until a more robust solution is implemented.
 *
 */
fun handleHashRouting(
    aboutRepository: AboutRepository,
    scope: CoroutineScope,
) {
    if (window.location.hash.removePrefix("#") == "privacy") {
        scope.launch {
            aboutRepository.toggleAbout(override = true)
        }
    }

    window.onhashchange = {
        val hash = window.location.hash.removePrefix("#")
        scope.launch {
            aboutRepository.toggleAbout(override = hash == "privacy")
        }
    }
}
