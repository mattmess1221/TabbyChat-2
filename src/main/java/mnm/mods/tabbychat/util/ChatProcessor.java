package mnm.mods.tabbychat.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import javax.annotation.Nullable;

public class ChatProcessor {

    public static final int MAX_CHAT_LENGTH = 256;

    @Nullable
    public static String[] processChatSends(@Nullable String msg, String prefix, boolean hidden) {
        if (StringUtils.isEmpty(msg)) {
            return null;
        }
        // get rid of spaces
        msg = msg.trim().replaceAll("  +", " ");
        int len = MAX_CHAT_LENGTH - prefix.length();
        if (!hidden && msg.startsWith(prefix)) {
            msg = msg.substring(prefix.length()).trim();
        }
        String[] sends = WordUtils.wrap(msg, len).split("\r?\n");

        // is command && (no prefix || not right prefix)
        if (sends[0].startsWith("/") && (StringUtils.isEmpty(prefix) || !sends[0].startsWith(prefix))) {
            // limit commands to 1 send.
            return new String[] { sends[0] };
        }
        if (StringUtils.isEmpty(prefix)) {
            return sends;
        }

        for (int i = 0; i < sends.length; i++) {
            sends[i] = prefix + " " + sends[i];
        }

        return sends;

    }
}
