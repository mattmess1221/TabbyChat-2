package mnm.mods.tabbychat.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import mnm.mods.tabbychat.util.ChatTextUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;

public class ChatTextUtilsTest {

    @Test
    public void testSubChat() {
        assertEquals(makeChat(false), ChatTextUtils.subChat(makeChat(true), 7));
    }

    private static ITextComponent makeChat(boolean tag) {

        ITextComponent chat = new StringTextComponent(tag ? "[test] " : "");
        chat.getStyle().setBold(true);
        {
            ITextComponent colored = new StringTextComponent("This should be green. ");
            colored.getStyle().setColor(TextFormatting.GREEN);
            chat.appendSibling(colored);
        }
        chat.appendText(" ");
        {
            ITextComponent link = new StringTextComponent("This is a link.");
            link.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://google.com/"));
            chat.appendSibling(link);
        }
        return chat;
    }
}
