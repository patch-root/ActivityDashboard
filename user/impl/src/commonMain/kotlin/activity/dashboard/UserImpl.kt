package activity.dashboard

import software.amazon.app.platform.scope.Scope

/** Production implementation of [User]. This is a data class for equals() and hashcode(). */
internal data class UserImpl(
    override val accessToken: String,
    override val name: String,
    override val profileImage: String,
) : User {
    @Suppress("DataClassShouldBeImmutable")
    override lateinit var scope: Scope
        internal set
}
