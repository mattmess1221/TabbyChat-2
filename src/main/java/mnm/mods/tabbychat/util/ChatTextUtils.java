package mnm.mods.tabbychat.util;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import mnm.mods.tabbychat.ChatMessage;
import mnm.mods.tabbychat.api.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class ChatTextUtils {

    public static List<ITextComponent> split(ITextComponent chat, int width) {
        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        return GuiUtilRenderComponents.splitText(chat, width, fr, false, false);
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
                List<ITextComponent> chatlist = split(line.getMessageWithOptionalTimestamp(), width);
                for (int i = chatlist.size() - 1; i >= 0; i--) {
                    ITextComponent chat = chatlist.get(i);
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
    public static ITextComponent subChat(ITextComponent chat, int beginIndex) {
        ITextComponent rchat = null;
        Iterator<ITextComponent> ichat = chat.iterator();
        int pos = 0;
        while (ichat.hasNext()) {
            ITextComponent part = ichat.next();
            String s = part.getUnformattedComponentText();

            int len = s.length();
            if (len + pos >= beginIndex) {
                if (pos < beginIndex) {
                    ITextComponent schat = new TextComponentString(s.substring(beginIndex - pos));
                    schat.setStyle(part.getStyle().createShallowCopy());
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
