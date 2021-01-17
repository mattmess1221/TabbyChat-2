package mnm.mods.tabbychat.test

import com.google.common.collect.Lists
import mnm.mods.tabbychat.util.ChannelPatterns
import org.junit.Assert
import org.junit.Test

class PatternTests {

    @Test
    fun testAngles() {
        test(ChannelPatterns.ANGLES, "<%s> %s")
    }

    @Test
    @Throws(Exception::class)
    fun testBraces() {
        test(ChannelPatterns.BRACES, "{%s} %s")
    }

    @Test
    fun testBrackets() {
        test(ChannelPatterns.BRACKETS, "[%s] %s")
    }

    @Test
    fun testParens() {
        test(ChannelPatterns.PARENS, "(%s) %s")
    }

    @Test
    fun testAnglesParens() {
        test(ChannelPatterns.ANGLESPARENS, "<(%s) Someone> %s")
    }

    @Test
    fun testAnglesBrackets() {
        test(ChannelPatterns.ANGLESBRACKETS, "<[%s] Someone> %s")
    }

    private fun test(pattern: ChannelPatterns, format: String) {
        val list = setupChannelPatterns(pattern, format)
        for (chan in list) {
            Assert.assertEquals(chan.channel, chan.testChannel())
        }
    }

    private fun setupChannelPatterns(pattern: ChannelPatterns, chan: String): List<ChanPattern> {
        val channels = arrayOf("global", "g", "local")
        val messages = arrayOf("<JoyJoy> Hello there", "[Admin] <MrTCP> ur ba&ned")
        val received = Lists.newArrayList<ChanPattern>()
        for (channel in channels) {
            for (message in messages) {
                val msg = String.format(chan, channel, message)
                received.add(ChanPattern(pattern, channel, msg))
            }
        }
        return received
    }

    private inner class ChanPattern(val pattern: ChannelPatterns, val channel: String, val message: String) {

        fun testChannel(): String? {
            val pattern = this.pattern.pattern
            return pattern.find(message)?.groups?.get(1)?.value
        }

    }
}
