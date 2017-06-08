package mnm.mods.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class DefaultChatProxy implements IChatProxy {

    @Override
    public void addToChat(String channel, ITextComponent msg) {
        ITextComponent text = new TextComponentTranslation("[%s] %s", channel, msg);
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(text);
    }
}
