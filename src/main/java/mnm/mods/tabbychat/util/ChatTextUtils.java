package mnm.mods.tabbychat.util;

import java.util.Iterator;
import java.util.List;

import mnm.mods.tabbychat.api.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.util.IChatComponent;

import com.google.common.collect.Lists;

public class ChatTextUtils {

    @SuppressWarnings("unchecked")
    public static List<IChatComponent> split(IChatComponent chat, int width) {
        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        return GuiUtilRenderComponents.func_178908_a(chat, width, fr, false, false);
    }

    public static List<Message> split(List<Message> list, int width) {
        List<Message> result = Lists.newArrayList();
        Iterator<Message> iter = list.iterator();
        while (iter.hasNext() && result.size() <= 100) {
            Message line = iter.next();
            List<IChatComponent> chatlist = split(line.getMessage(), width);
            for (int i = chatlist.size() - 1; i >= 0; i--) {
                IChatComponent chat = chatlist.get(i);
                result.add(new ChatMessage(line.getCounter(), chat, line.getID()));
            }
        }
        return result;
    }
}
