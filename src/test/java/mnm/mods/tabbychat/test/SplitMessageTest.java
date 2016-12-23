package mnm.mods.tabbychat.test;

import mnm.mods.tabbychat.util.ChatProcessor;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

public class SplitMessageTest {

    @Test
    public void testEmpty() {

        // empty string. Shouldn't be sent anyway.
        assertNull(ChatProcessor.processChatSends("", "", false));
    }

    @Test
    public void testLong() {
        // no prefix, not command
        String msg = "This is a really long message that will be split into 2 or 3 different strings, each with less than two hundred fifty-six characters. Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard" +
                " dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was" +
                " popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";
        String[] target = {
                "This is a really long message that will be split into 2 or 3 different strings, each with less than two hundred fifty-six characters. Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard",
                "dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was",
                "popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
        };
        assertArrayEquals(ChatProcessor.processChatSends(msg, "", false), target);
    }

    @Test
    public void testLongWithPrefixHidden() {
        // prefix hidden
        String msg = "This string is also long, but will have some hidden prefixes added to make the result offset. Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";
        String[] target = new String[]{
                "/say This string is also long, but will have some hidden prefixes added to make the result offset. Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when",
                "/say an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the",
                "/say release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
        };
        assertArrayEquals(ChatProcessor.processChatSends(msg, "/say", true), target);
    }

    @Test
    public void testLongCommandWithNoPrefix() {
        // unknown command prefix
        String msg = "/msg JoyJoy This command has multiple arguments and the prefix is not set. Because TabbyChat does not know where the actual message is, it will only send the first 256 characters. Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";
        String[] target = new String[]{
                "/msg JoyJoy This command has multiple arguments and the prefix is not set. Because TabbyChat does not know where the actual message is, it will only send the first 256 characters. Lorem Ipsum is simply dummy text of the printing and typesetting industry."
        };
        assertArrayEquals(ChatProcessor.processChatSends(msg, "", false), target);
    }

    @Test
    public void testSinglePrefix() {
        // one line, prefix shown
        String msg = "/msg JoyJoy This one is shorter, so it won't be split.";
        String[] target = new String[]{"/msg JoyJoy This one is shorter, so it won't be split."};
        assertArrayEquals(ChatProcessor.processChatSends(msg, "/msg JoyJoy", false), target);
    }

    @Test
    public void testSinglePrefixHidden() {
        String msg = "This message is also short, but the prefix is hidden.";
        String[] target = new String[]{"/g This message is also short, but the prefix is hidden."};
        assertArrayEquals(ChatProcessor.processChatSends(msg, "/g", true), target);
    }

    @Test
    public void testDifferentCommandPrefix() {
        // a different command is sent
        String msg = "/warp spawn";
        String[] target = new String[]{"/warp spawn"};
        assertArrayEquals(ChatProcessor.processChatSends(msg, "/msg Friend", true), target);
        assertArrayEquals(ChatProcessor.processChatSends(msg, "/msg Friend", false), target);

    }

}
