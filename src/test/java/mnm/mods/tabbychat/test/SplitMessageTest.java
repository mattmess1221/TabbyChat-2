package mnm.mods.tabbychat.test;

import static org.junit.Assert.*;

import org.junit.Test;

import mnm.mods.tabbychat.util.ChatProcessor;

public class SplitMessageTest {

    @Test
    public void test() {

        // empty string. Shouldn't be sent anyway.
        assertNull(ChatProcessor.processChatSends("", "", false));

        // no prefix, not command
        String msg = "This is a really long message that will be split into 2 or 3 different strings, each with less than one hundred characters.";
        String[] target = {
                "This is a really long message that will be split into 2 or 3 different strings, each with less than",
                "one hundred characters."
        };
        assertArrayEquals(ChatProcessor.processChatSends(msg, "", false), target);

        // prefix hidden
        msg = "This string is also long, but will have some hidden prefixes added to make the result offset. Blah blah blah blah blah blah blah blah";
        target = new String[] {
                "/say This string is also long, but will have some hidden prefixes added to make the result offset.",
                "/say Blah blah blah blah blah blah blah blah"
        };
        assertArrayEquals(ChatProcessor.processChatSends(msg, "/say", true), target);

        // nothing
        msg = "";
        target = null;
        assertArrayEquals(ChatProcessor.processChatSends(msg, "", false), target);

        // unknown command prefix
        msg = "/msg JoyJoy This command has multiple arguments and the prefix is not set. Because TabbyChat does not know where the actual message is, it will only send the first 100 characters.";
        target = new String[] {
                "/msg JoyJoy This command has multiple arguments and the prefix is not set. Because TabbyChat does"
        };
        assertArrayEquals(ChatProcessor.processChatSends(msg, "", false), target);

        // one line, prefix hidden
        msg = "/msg JoyJoy This one is shorter, so it won't be split.";
        target = new String[] { "/msg JoyJoy This one is shorter, so it won't be split." };
        assertArrayEquals(ChatProcessor.processChatSends(msg, "/msg JoyJoy", false), target);

        msg = "This message is also short, but the prefix is hidden.";
        target = new String[] { "/g This message is also short, but the prefix is hidden." };
        assertArrayEquals(ChatProcessor.processChatSends(msg, "/g", true), target);

        // a different command is sent
        msg = "/warp spawn";
        target = new String[] { "/warp spawn" };
        assertArrayEquals(ChatProcessor.processChatSends(msg, "/msg Friend", true), target);
        assertArrayEquals(ChatProcessor.processChatSends(msg, "/msg Friend", false), target);

    }

}
