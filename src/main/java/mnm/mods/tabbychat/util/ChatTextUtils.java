package mnm.mods.tabbychat.util;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import mnm.mods.tabbychat.ChatMessage;
import mnm.mods.tabbychat.api.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class ChatTextUtils {

    public static List<IChatComponent> split(IChatComponent chat, int width) {
        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        return GuiUtilRenderComponents.func_178908_a(chat, width, fr, false, false);
    }

    public static List<Message> split(List<Message> list, int width) {
        if (width <= 8) // ignore, characters are larger than width
            return Lists.newArrayList(list);
        // prevent concurrent modification caused by chat thread
        synchronized (list) {
            List<Message> result = Lists.newArrayList();
            Iterator<Message> iter = list.iterator();
            while (iter.hasNext() && result.size() <= 100) {
                Message line = iter.next();
                List<IChatComponent> chatlist = split(line.getMessageWithOptionalTimestamp(), width);
                for (int i = chatlist.size() - 1; i >= 0; i--) {
                    IChatComponent chat = chatlist.get(i);
                    result.add(new ChatMessage(line.getCounter(), chat, line.getID(), false));
                }
            }
            return result;
        }
    }

    /**
     * Returns a ChatComponent that is a sub-component of another one. It begins
     * at the specified index and extends to the end of the componenent.
     *
     * @param chat The chat to subchat
     * @param beginIndex The beginning index, inclusive
     * @return The end of the chat
     * @see String#substring(int)
     */
    public static IChatComponent subChat(IChatComponent chat, int beginIndex) {
        IChatComponent rchat = null;
        Iterator<IChatComponent> ichat = chat.iterator();
        int pos = 0;
        while (ichat.hasNext()) {
            IChatComponent part = ichat.next();
            String s = part.getUnformattedTextForChat();

            int len = s.length();
            if (len + pos >= beginIndex) {
                if (pos < beginIndex) {
                    IChatComponent schat = new ChatComponentText(s.substring(beginIndex - pos));
                    schat.setChatStyle(part.getChatStyle().createShallowCopy());
                    part = schat;
                }
                if (rchat == null) {
                    rchat = part;
                } else {
                    rchat.appendSibling(part);
                }
            }
            pos += len;
        }
        return rchat;
    }
}
