package main

import com.espresso.pbmobile.utlis.DateParser
import org.junit.Assert
import org.junit.Test

class DateParserTest {

    @Test
    fun parseLongReversedPatternToLongPattern() {
        val entryValue = "2020-04-07T02:28:38.513"
        val expectedValue = "07.04.2020 02:28"
        val resultValue = DateParser.parse(entryValue, DateParser.longReversedPattern, DateParser.longPattern)
        Assert.assertEquals(resultValue, expectedValue)
    }

    @Test
    fun parseExtraLongReversedPatternToLongPattern() {
        val entryValue = "2020-04-09T17:59:39.669+0000"
        val expectedValue = "09.04.2020 17:59"
        val resultValue = DateParser.parse(entryValue, DateParser.longReversedPattern, DateParser.longPattern)
        Assert.assertEquals(resultValue, expectedValue)
    }

    @Test
    fun parseShortPatternToShortReversedPattern() {
        val entryValue = "12.04.2020"
        val expectedValue = "2020-04-12"
        val resultValue = DateParser.parse(entryValue, DateParser.shortPattern, DateParser.shortReversedPattern)
        Assert.assertEquals(resultValue, expectedValue)
    }
}
