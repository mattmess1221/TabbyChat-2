package mnm.mods.tabbychat.test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

import mnm.mods.tabbychat.util.ChannelPatterns;

public class PatternTests {


    @Test
    public void testAngles() throws Exception {
        test(ChannelPatterns.ANGLES, "<%s> %s");
    }
    @Test
    public void testBraces() throws Exception {
        test(ChannelPatterns.BRACES, "{%s} %s");
    }

    @Test
    public void testBrackets() throws Exception {
        test(ChannelPatterns.BRACKETS, "[%s] %s");
    }
    @Test
    public void testParens() throws Exception {
        test(ChannelPatterns.PARENS, "(%s) %s");
    }

    @Test
    public void testAnglesParens() throws Exception {
        test(ChannelPatterns.ANGLESPARENS, "<(%s) Someone> %s");
    }
    @Test
    public void testAnglesBrackets() throws Exception {
        test(ChannelPatterns.ANGLESBRACKETS, "<[%s] Someone> %s");
    }

    private void test(ChannelPatterns pattern, String format) {
        List<ChanPattern> list = setupChannelPatterns(pattern, format);
        for (ChanPattern chan : list) {
            Assert.assertEquals(chan.channel, chan.testChannel());
        }
    }

    private List<ChanPattern> setupChannelPatterns(ChannelPatterns pattern, String chan) {
        String[] channels = {
                "global",
                "g",
                "local"
        };
        String[] messages = {
                "<JoyJoy> Hello there",
                "[Admin] <MrTCP> ur ba&ned"
        };
        List<ChanPattern> received = Lists.newArrayList();
        for (int i = 0; i < channels.length; i++) {
            for (int j = 0; j < messages.length; j++) {
                String msg = String.format(chan, channels[i], messages[j]);
                received.add(new ChanPattern(pattern, channels[i], msg));
            }
        }
        return received;
    }

    private class ChanPattern {
        final ChannelPatterns pattern;
        final String channel;
        final String message;

        public ChanPattern(ChannelPatterns pattern, String channel, String message) {
            this.pattern = pattern;
            this.channel = channel;
            this.message = message;
        }

        public String testChannel() {
            Pattern pattern = this.pattern.getPattern();
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                return matcher.group(1);
            }
            return null;
        }

    }
}
