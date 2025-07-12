package activity.dashboard

import software.amazon.app.platform.scope.Scope

/** A [User] represents an account to implement login and logout in our sample app. */
interface User {
    val accessToken: String
    val name: String
    val profileImage: String

    /**
     * The scope is tied to the lifecycle of the user. It hosts a user specific `CoroutineScope` and
     * kotlin-inject component. The scope is destroyed on logout.
     */
    val scope: Scope
}
