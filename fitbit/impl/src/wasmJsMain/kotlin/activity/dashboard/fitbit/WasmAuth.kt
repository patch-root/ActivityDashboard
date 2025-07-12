package activity.dashboard.fitbit

import activity.dashboard.common.logging.Logger
import kotlinx.browser.window
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Clock
import me.tatarka.inject.annotations.Inject
import okio.ByteString.Companion.encodeUtf8
import software.amazon.app.platform.scope.Scope
import software.amazon.app.platform.scope.Scoped
import software.amazon.app.platform.scope.coroutine.launch
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class WasmAuth(
    private val fitbitClient: FitbitClient,
    private val queryParamProvider: QueryParamProvider,
) : Auth, Scoped {
    companion object {
        private const val ACCESS_TOKEN = "fitbit_access_token"
        private const val REFRESH_TOKEN = "fitbit_refresh_token"
        private const val EXPIRES_AT = "fitbit_expires_at"
        private const val CODE_VERIFIER = "fitbit_code_verifier"
    }

    private val _accessToken = MutableStateFlow<String?>(null)
    override val accessToken: StateFlow<String?> = _accessToken.asStateFlow()

    private var redirectUrlCache: String? = null

    override fun onEnterScope(scope: Scope) {
        scope.launch {
            Logger.log("Calling initAccessToken")
            initAccessToken()
        }
    }

    override fun login(url: String) {
        window.location.href = url
    }

    suspend fun initAccessToken(): String? {
        val now = Clock.System.now().epochSeconds

        // 1. In-memory token
        val memToken = _accessToken.value
        val memExpires = window.localStorage.getItem(EXPIRES_AT)?.toLongOrNull()
        if (memToken != null && memExpires != null && now < memExpires) {
            Logger.log("Returning in-memory accessToken")
            return memToken
        }

        // 2. Stored token
        val localToken = window.localStorage.getItem(ACCESS_TOKEN)
        if (localToken != null && memExpires != null && now < memExpires) {
            Logger.log("Retrieved accessToken from localStorage")
            _accessToken.value = localToken
            return localToken
        }

        // 3. Refresh token
        val refreshed = tryRefreshToken(now)
        if (refreshed != null) return refreshed

        // 4. Exchange code
        val code = queryParamProvider.getQueryParam("code")
        if (code != null) {
            Logger.log("OAuth code found in URL: $code")
            val exchanged = tryExchangeCodeForToken(code, now)
            if (exchanged != null) return exchanged
        }

        _accessToken.value = null
        return null
    }

    override suspend fun getValidAccessToken(): String {
        val now = Clock.System.now().epochSeconds
        val expiresAt = window.localStorage.getItem(EXPIRES_AT)?.toLongOrNull()
        val currentToken = _accessToken.value

        if (currentToken != null && expiresAt != null && now < expiresAt - 120) {
            return currentToken
        }

        val refreshed = tryRefreshToken(now)
        if (refreshed != null) return refreshed

        throw IllegalStateException("Access token expired and could not be refreshed!")
    }

    private suspend fun tryRefreshToken(now: Long): String? {
        val refreshToken = window.localStorage.getItem(REFRESH_TOKEN) ?: return null

        return when (val result = fitbitClient.refreshToken(refreshToken)) {
            is FitbitResult.Success -> {
                val response = result.data
                saveTokens(response.accessToken, response.refreshToken, now + response.expiresIn)
                Logger.log("Token refreshed")
                response.accessToken
            }
            is FitbitResult.Error -> {
                Logger.log("Token refresh failed: ${result.message}")
                clearFitbitAuth()
                null
            }
        }
    }

    private suspend fun tryExchangeCodeForToken(
        code: String,
        now: Long,
    ): String? {
        return when (val result = fitbitClient.exchangeCodeForToken(code, retrieveCodeVerifier())) {
            is FitbitResult.Success -> {
                val response = result.data
                saveTokens(response.accessToken, response.refreshToken, now + response.expiresIn)
                redirectUrlCache = null
                window.localStorage.removeItem(CODE_VERIFIER)
                window.history.replaceState(null, "", window.location.pathname)
                Logger.log("Token exchanged from code")
                response.accessToken
            }
            is FitbitResult.Error -> {
                Logger.log("Code exchange failed: ${result.message}")
                null
            }
        }
    }

    private fun saveTokens(
        accessToken: String,
        refreshToken: String,
        expiresAt: Long,
    ) {
        window.localStorage.setItem(ACCESS_TOKEN, accessToken)
        window.localStorage.setItem(REFRESH_TOKEN, refreshToken)
        window.localStorage.setItem(EXPIRES_AT, expiresAt.toString())
        _accessToken.value = accessToken
    }

    override fun buildRedirectUrl(): String {
        redirectUrlCache?.let {
            Logger.log("Reusing cached redirect URL")
            return it
        }

        val existingVerifier = retrieveCodeVerifierOrNull()
        val codeVerifier =
            existingVerifier ?: generateCodeVerifier().also {
                Logger.log("Storing fresh code_verifier")
                storeCodeVerifier(it)
            }

        val codeChallenge = generateCodeChallenge(codeVerifier)
        val url = generateUrl(codeChallenge)

        Logger.log("Generated new redirect URL: $url")
        redirectUrlCache = url
        return url
    }

    private fun storeCodeVerifier(codeVerifier: String) {
        Logger.log("setting fitbit_code_verifier")
        window.localStorage.setItem(CODE_VERIFIER, codeVerifier)
    }

    fun retrieveCodeVerifierOrNull(): String? {
        return window.localStorage.getItem(CODE_VERIFIER)
    }

    override fun retrieveCodeVerifier(): String {
        return window.localStorage.getItem(CODE_VERIFIER)
            ?: error("No $CODE_VERIFIER found in localStorage")
    }

    private fun generateCodeVerifier(length: Int = 64): String {
        return (1..length)
            .map { Auth.Companion.ALLOWED_CHARS.random() }
            .joinToString("")
    }

    private fun generateCodeChallenge(codeVerifier: String): String {
        val sha256 = codeVerifier.encodeUtf8().sha256()
        return sha256.base64Url().replace("=", "")
    }

    private fun generateUrl(codeChallenge: String): String {
        return "https://www.fitbit.com/oauth2/authorize" +
            "?response_type=code" +
            "&client_id=${Auth.Companion.CLIENT_ID}" +
            "&scope=activity+profile" +
            "&code_challenge=$codeChallenge" +
            "&code_challenge_method=S256" +
            "&redirect_uri=${Auth.Companion.REDIRECT}"
    }

    override fun clearFitbitAuth() {
        Logger.log("Clearing all Fitbit auth state")
        window.localStorage.removeItem(ACCESS_TOKEN)
        window.localStorage.removeItem(REFRESH_TOKEN)
        window.localStorage.removeItem(EXPIRES_AT)
        window.localStorage.removeItem(CODE_VERIFIER)
        redirectUrlCache = null
        _accessToken.value = null
    }
}
