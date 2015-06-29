package mnm.mods.tabbychat.util;

import java.util.Iterator;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public final class ChatUtils {

    private ChatUtils() {}

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
