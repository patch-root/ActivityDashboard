package activity.dashboard.fitbit

import activity.dashboard.common.Env
import activity.dashboard.common.logging.Logger
import activity.dashboard.fitbit.Auth.Companion.CLIENT_ID
import activity.dashboard.fitbit.Auth.Companion.REDIRECT
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.js.Js
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.formUrlEncode
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class FitbitClientImpl(
    private val urlEncoder: UrlEncoder,
    private val proxySettings: ProxySettings,
) : FitbitClient {
    companion object {
        private const val BASE_FITBIT_URL = "https://api.fitbit.com"
    }

    private val useProxy get() = proxySettings.load()
    private val baseUrl: String
        get() = if (useProxy) Env.API_BASE_URL else BASE_FITBIT_URL

    private val client =
        HttpClient(Js) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    },
                )
            }
        }

    override suspend fun exchangeCodeForToken(
        code: String,
        codeVerifier: String,
    ): FitbitResult<FitbitResponse> {
        val url = "https://api.fitbit.com/oauth2/token"

        return try {
            val response =
                client.post(url) {
                    contentType(ContentType.Application.FormUrlEncoded)
                    setBody(
                        Parameters.build {
                            append("client_id", CLIENT_ID)
                            append("grant_type", "authorization_code")
                            append("redirect_uri", REDIRECT)
                            append("code", code)
                            append("code_verifier", codeVerifier)
                        }.formUrlEncode(),
                    )
                }

            if (!response.status.isSuccess()) {
                val errorBody = response.bodyAsText()
                Logger.log("Fitbit token exchange failed: ${response.status}")
                Logger.log("Body: $errorBody")
                return FitbitResult.Error("Token exchange failed: ${response.status}", null)
            }

            val token = response.body<FitbitResponse>()
            Logger.log("Token exchange success: $token")
            FitbitResult.Success(token)
        } catch (e: Exception) {
            Logger.error("Exception during token exchange: ${e.message}", e)
            FitbitResult.Error("Exception during token exchange", e)
        }
    }

    override suspend fun refreshToken(refreshToken: String): FitbitResult<FitbitResponse> {
        val url = "https://api.fitbit.com/oauth2/token"

        return try {
            val response =
                client.post(url) {
                    contentType(ContentType.Application.FormUrlEncoded)
                    setBody(
                        Parameters.build {
                            append("client_id", CLIENT_ID)
                            append("grant_type", "refresh_token")
                            append("refresh_token", refreshToken)
                        }.formUrlEncode(),
                    )
                }

            if (!response.status.isSuccess()) {
                val errorBody = response.bodyAsText()
                Logger.log("Fitbit token refresh failed: ${response.status}")
                Logger.log("Body: $errorBody")
                return FitbitResult.Error("Token refresh failed: ${response.status}", null)
            }

            val token = response.body<FitbitResponse>()
            Logger.log("🔁 Token refresh success: $token")
            FitbitResult.Success(token)
        } catch (e: Exception) {
            Logger.error("Exception during token refresh: ${e.message}", e)
            FitbitResult.Error("Exception during token refresh", e)
        }
    }

    override suspend fun getActivities(
        accessToken: String,
        monthOffset: Int,
    ): FitbitResult<FitbitActivitiesResponse> {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val targetMonth = today.minus(DatePeriod(months = -monthOffset))
        val startOfMonth = LocalDate(targetMonth.year, targetMonth.monthNumber, 1)
        val formatter = { date: LocalDate -> date.toString() }

        val url =
            "$baseUrl/user/-/activities/list.json" +
                "?afterDate=${formatter(startOfMonth)}" +
                "&sort=asc" +
                "&offset=0" +
                "&limit=100"

        Logger.log("Requesting from: $url")

        return corsSafeCall(useProxy = useProxy, endpoint = url) {
            val response: FitbitActivitiesResponse =
                client.get(url) {
                    headers {
                        append("Authorization", "Bearer $accessToken")
                    }
                }.body()

            Logger.log("response from get: $response")
//            testing
//            val json = Json { ignoreUnknownKeys = true }
//            json.decodeFromString<FitbitActivitiesResponse>(testingJson)
            response
        }
    }

    override suspend fun getProfile(accessToken: String): FitbitResult<FitbitProfileResponse> {
        val url = "$baseUrl/user/-/profile.json"
        Logger.log("Fetching profile from: $url")

        return corsSafeCall(useProxy, url) {
            val response =
                client.get(url) {
                    headers {
                        append("Authorization", "Bearer $accessToken")
                    }
                }

            if (!response.status.isSuccess()) {
                val errorBody = response.bodyAsText()
                Logger.log("Failed to fetch profile: ${response.status}")
                Logger.log("Body: $errorBody")
                throw IllegalStateException("HTTP error ${response.status}")
            }

            val original = response.body<FitbitProfileResponse>()
            val proxiedAvatar = urlEncoder.proxiedFitbitImageUrl(original.user.avatar)
            original.copy(user = original.user.copy(avatar = proxiedAvatar))
        }
    }
}
