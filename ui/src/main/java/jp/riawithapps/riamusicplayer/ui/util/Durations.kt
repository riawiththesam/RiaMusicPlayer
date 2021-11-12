package jp.riawithapps.riamusicplayer.ui.util

import org.threeten.bp.Duration

fun Duration.getPatternFunction() = when {
    this.toHours() > 1 -> ::durationPatternHours
    this.toMinutes() > 1 -> ::durationPatternMinutes
    else -> ::durationPatternSeconds
}

private fun durationPatternSeconds(duration: Duration): String {
    return duration.toSecondsPart().toString().padStart(2, '0')
}

private fun durationPatternMinutes(duration: Duration): String {
    val minutes = duration.toMinutesPart().toString().padStart(2, '0')
    val seconds = durationPatternSeconds(duration)
    return "${minutes}:${seconds}"
}

private fun durationPatternHours(duration: Duration): String {
    val hours = duration.toHoursPart().toString().padStart(2, '0')
    val minutes = durationPatternMinutes(duration)
    return "${hours}:${minutes}"
}