package activity.dashboard

import activity.dashboard.fitbit.Activity

interface PdfExporter {
    suspend fun exportActivityPdf(
        activities: List<Activity>,
        name: String,
        currentMonthOffset: Int,
    )
}
