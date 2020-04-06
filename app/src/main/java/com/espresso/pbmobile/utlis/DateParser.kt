package com.espresso.pbmobile.utlis

import java.text.SimpleDateFormat
import java.util.*

object DateParser {
    const val shortPattern = "dd.MM.yyyy"
    const val shortReversedPattern = "yyyy-MM-dd"
    const val longReversedPattern = "yyyy-MM-dd'T'HH:mm:ss"
    const val longPattern = "dd.MM.yyyy HH:mm"
    const val extraLongReversedPattern = "yyyy-MM-dd'T'HH:mm:ss.SSSz"

    @JvmStatic
    fun parse(date: String, inputPattern: String, outputPattern: String): String {
        val parser = SimpleDateFormat(inputPattern, Locale.getDefault())
        val formatter = SimpleDateFormat(outputPattern, Locale.getDefault())
        return formatter.format(parser.parse(date))
    }
}
