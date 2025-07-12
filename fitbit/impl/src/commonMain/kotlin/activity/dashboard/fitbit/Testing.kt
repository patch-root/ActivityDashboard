package activity.dashboard.fitbit

import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlin.random.Random

data class ActivityEntry(
    val date: LocalDate,
    val hour: Int, // used for sorting by time
    val json: String,
)

val testingJson: String = buildJson()

fun buildJson(): String {
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val year = currentDate.year
    val month = currentDate.monthNumber
    val firstOfMonth = LocalDate(year, month, 1)

    val daysInMonth =
        if (month == 12) {
            31
        } else {
            LocalDate(year, month + 1, 1).minus(DatePeriod(days = 1)).dayOfMonth
        }

    val activityList =
        (1..30).map { i ->
            val day = Random.nextInt(1, daysInMonth + 1)
            val date = LocalDate(year, month, day)
            val hour = 6 + (i % 12)
            val time = hour.toString().padStart(2, '0') + ":00:00"

            val names = listOf("Walk", "Run", "Bike", "Swim", "Yoga", "Hike", "Rowing", "Elliptical", "Dance", "Strength Training")
            val name = names[i % names.size]
            val typeId = 90000 + i
            val duration = (10..30).random() * 60000
            val activeDuration = duration - 300000
            val calories = (100..600).random()
            val steps = if (name == "Swim" || name == "Yoga" || name == "Strength Training") 0 else (1000..7000).random()
            val logId = 19018673300 + i
            val levels =
                listOf("sedentary", "lightly", "fairly", "very").joinToString(", ") { levelName ->
                    val minutes = (0..10).random()
                    """{ "minutes": $minutes, "name": "$levelName" }"""
                }

            val dateStr = date.toString()

            val json =
                """
                {
                  "activeDuration": $activeDuration,
                  "activityLevel": [ $levels ],
                  "activityName": "$name",
                  "activityTypeId": $typeId,
                  "calories": $calories,
                  "caloriesLink": "https://api.fitbit.com/1/user/-/activities/calories/date/$dateStr/$dateStr/1min/time/${time.substring(
                    0,
                    5,
                )}/${time.substring(0,5)}.json",
                  "duration": $duration,
                  "elevationGain": ${(0..100).random()},
                  "lastModified": "${dateStr}T$time.000Z",
                  "logId": $logId,
                  "logType": "manual",
                  "manualValuesSpecified": {
                    "calories": ${listOf(true, false).random()},
                    "distance": ${listOf(true, false).random()},
                    "steps": ${listOf(true, false).random()}
                  },
                  "originalDuration": $duration,
                  "originalStartTime": "${dateStr}T$time-08:00",
                  "startTime": "${dateStr}T$time-08:00",
                  "steps": $steps,
                  "tcxLink": "https://api.fitbit.com/1/user/-/activities/$logId.tcx"
                }
                """.trimIndent()

            ActivityEntry(date = date, hour = hour, json = json)
        }

    val sorted =
        activityList.sortedWith(
            compareByDescending<ActivityEntry> { it.date }
                .thenByDescending { it.hour },
        )

    return """
{
  "activities": [
${sorted.joinToString(",\n") { it.json }}
  ],
  "pagination": {
    "afterDate": "$firstOfMonth",
    "limit": 30,
    "next": "https://api.fitbit.com/1/user/-/activities/list.json?offset=30&limit=30&sort=desc&afterDate=$firstOfMonth",
    "offset": 0,
    "previous": "",
    "sort": "desc"
  }
}
        """.trimIndent()
}
