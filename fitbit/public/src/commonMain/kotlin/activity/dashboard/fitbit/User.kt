package activity.dashboard.fitbit

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val fullName: String,
    val age: Int? = null,
    val ambassador: Boolean? = null,
    val autoStrideEnabled: Boolean? = null,
    val avatar: String,
    val avatar150: String? = null,
    val avatar640: String? = null,
    val challengesBeta: Boolean? = null,
    val clockTimeDisplayFormat: String? = null,
    val corporate: Boolean? = null,
    val corporateAdmin: Boolean? = null,
    val dateOfBirth: String? = null,
    val displayName: String,
    val displayNameSetting: String? = null,
    val distanceUnit: String? = null,
    val encodedId: String,
    val features: Features? = null,
    val firstName: String? = null,
    val foodsBeta: Boolean? = null,
    val gender: String? = null,
    val glucoseUnit: String? = null,
    val height: Double? = null,
    val heightUnit: String? = null,
    val isChild: Boolean? = null,
    val lastName: String? = null,
    val legalTermsAcceptanceRequired: Boolean? = null,
    val locale: String? = null,
    val memberSince: String? = null,
    val mfaEnabled: Boolean? = null,
    val offsetFromUTCMillis: Int? = null,
    val sdkDeveloper: Boolean? = null,
    val sleepTracking: String? = null,
    val startDayOfWeek: String? = null,
    val strideLengthRunning: Double? = null,
    val strideLengthRunningType: String? = null,
    val strideLengthWalking: Double? = null,
    val strideLengthWalkingType: String? = null,
    val swimUnit: String? = null,
    val timezone: String? = null,
    val topBadges: List<Badge>? = null,
    val userType: String? = null,
    val waterUnit: String? = null,
    val waterUnitName: String? = null,
    val weight: Double? = null,
    val weightUnit: String? = null,
)
