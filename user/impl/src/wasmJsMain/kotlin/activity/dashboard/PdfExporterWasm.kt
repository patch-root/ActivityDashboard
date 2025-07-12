package activity.dashboard

import activity.dashboard.fitbit.Activity
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import me.tatarka.inject.annotations.Inject
import org.w3c.fetch.Response
import org.w3c.files.Blob
import org.w3c.files.FileReader
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@JsFun("() => new window.jspdf.jsPDF()")
external fun createPdf(): JsPdf

@JsFun("reader => reader.result")
external fun getReaderResult(reader: FileReader): String

@Inject
@SingleIn(UserScope::class)
@ContributesBinding(UserScope::class)
class JsPdfExporter : PdfExporter {
    companion object {
        private const val BASE_FILE_NAME = "activity_report"
        private const val LOGO_URL = "patchrootTransparent.png"
        private const val FONT_NAME = "helvetica"
        private const val FONT_BOLD = "bold"
        private const val FONT_NORMAL = "normal"
    }

    override suspend fun exportActivityPdf(
        activities: List<Activity>,
        name: String,
        currentMonthOffset: Int,
    ) {
        val logo = fetchImageAsBase64(LOGO_URL)
        val pdf = createPdf()
        pdf.setFontSize(12)

        val pageHeight = pdf.internal.pageSize.getHeight().toInt()
        val lineHeight = 8
        val topMargin = 40
        val bottomMargin = 20
        val maxY = pageHeight - bottomMargin

        // Column positions
        val xType = 10
        val xDate = 50
        val xTime = 90
        val xCal = 110
        val xDur = 140

        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

        fun drawHeader(
            y: Int,
            logo: String,
        ): Int {
            val logoX = 160
            val logoY = 10
            val logoWidth = 30
            val logoHeight = 30

            pdf.addImage(logo, "PNG", logoX, logoY, logoWidth, logoHeight)

            pdf.setFontSize(14)
            pdf.setFont(FONT_NAME, FONT_BOLD)
            pdf.text(name, 10, y + 10)

            val targetMonthDate = today.minus(DatePeriod(months = -currentMonthOffset))
            val title = "${targetMonthDate.month.name.lowercase().replaceFirstChar(Char::uppercase)} ${targetMonthDate.year}"

            pdf.setFontSize(12)
            pdf.setFont(FONT_NAME, FONT_NORMAL)
            pdf.text("Activities for $title", 10, y + 25)

            val headerStartY = y + 40
            pdf.setFont(FONT_NAME, FONT_BOLD)
            pdf.setFontSize(12)
            pdf.text("Type", xType, headerStartY)
            pdf.text("Date", xDate, headerStartY)
            pdf.text("Time", xTime, headerStartY)
            pdf.text("Calories", xCal, headerStartY)
            pdf.text("Duration", xDur, headerStartY)

            pdf.setFont(FONT_NAME, FONT_NORMAL)
            return headerStartY + lineHeight
        }

        var y = drawHeader(topMargin, logo)

        for (activity in activities) {
            if (y >= maxY) {
                pdf.addPage()
                y = drawHeader(topMargin, logo)
            }

            val start =
                kotlinx.datetime.Instant.parse(activity.startTime)
                    .toLocalDateTime(TimeZone.currentSystemDefault())

            val dateStr = start.date.toString()
            val timeStr = "${start.time.hour.toString().padStart(2, '0')}:${start.time.minute.toString().padStart(2, '0')}"

            val durationMillis = activity.duration
            val totalSeconds = durationMillis / 1000
            val hours = totalSeconds / 3600
            val minutes = (totalSeconds % 3600) / 60
            val seconds = totalSeconds % 60
            val durationStr =
                buildString {
                    if (hours > 0) append("${hours}h ")
                    if (minutes > 0 || hours > 0) append("${minutes}m ")
                    append("${seconds}s")
                }

            pdf.setFontSize(12)
            pdf.text(activity.activityName.take(15), xType, y)
            pdf.text(dateStr, xDate, y)
            pdf.text(timeStr, xTime, y)
            pdf.text("${activity.calories} cal", xCal, y)
            pdf.text(durationStr, xDur, y)

            y += lineHeight
        }

        val formattedDate = "${today.year}-${today.monthNumber.toString().padStart(2, '0')}-${today.dayOfMonth.toString().padStart(2, '0')}"
        val finalFileName = "${BASE_FILE_NAME}_$formattedDate.pdf"

        pdf.save(finalFileName)
    }

    suspend fun fetchImageAsBase64(url: String): String {
        val response: Response = window.fetch(url).await()
        val blob: Blob = response.blob().await()

        return suspendCoroutine { cont ->
            val reader = FileReader()
            reader.onload = {
                try {
                    val result = getReaderResult(reader)
                    cont.resume(result)
                } catch (e: Throwable) {
                    cont.resumeWithException(IllegalStateException("Failed to read image as base64", e))
                }
            }
            reader.onerror = {
                cont.resumeWithException(Exception("Failed to read image blob"))
            }
            reader.readAsDataURL(blob)
        }
    }
}
