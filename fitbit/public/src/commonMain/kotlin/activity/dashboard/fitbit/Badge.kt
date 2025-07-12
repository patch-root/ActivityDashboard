package activity.dashboard.fitbit

import kotlinx.serialization.Serializable

@Serializable
data class Badge(
    val altText: String? = null,
    val badgeGradientEndColor: String? = null,
    val badgeGradientStartColor: String? = null,
    val badgeType: String? = null,
    val category: String? = null,
    val clickThroughUrl: String? = null,
    val dateTime: String? = null,
    val description: String? = null,
    val earnedMessage: String? = null,
    val image100px: String? = null,
    val image125px: String? = null,
    val image300px: String? = null,
    val image50px: String? = null,
    val image75px: String? = null,
    val marketingDescription: String? = null,
    val mobileDescription: String? = null,
    val name: String,
    val shortDescription: String? = null,
    val shareImage640px: String? = null,
    val shareText: String? = null,
    val slug: String? = null,
    val timesEarned: Int? = null,
    val unit: String? = null,
    val value: Int? = null,
)
