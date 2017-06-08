package mnm.mods.util;

import net.minecraft.util.text.ITextComponent;

public interface IChatProxy {

    void addToChat(String channel, ITextComponent message);
}
