package mnm.mods.tabbychat.test;

import static org.junit.Assert.*;

import org.junit.Test;

import mnm.mods.tabbychat.util.ChatTextUtils;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class ChatTextUtilsTest {

    @Test
    public void testSubChat() {
        assertEquals(makeChat(false), ChatTextUtils.subChat(makeChat(true), 7));
    }

    private static IChatComponent makeChat(boolean tag) {

        IChatComponent chat = new ChatComponentText(tag ? "[test] " : "");
        chat.getChatStyle().setBold(true);
        {
            IChatComponent colored = new ChatComponentText("This should be green. ");
            colored.getChatStyle().setColor(EnumChatFormatting.GREEN);
            chat.appendSibling(colored);
        }
        chat.appendText(" ");
        {
            IChatComponent link = new ChatComponentText("This is a link.");
            link.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://google.com/"));
            chat.appendSibling(link);
        }
        return chat;
    }
}
