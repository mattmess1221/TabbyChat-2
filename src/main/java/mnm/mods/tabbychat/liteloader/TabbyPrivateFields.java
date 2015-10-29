package mnm.mods.tabbychat.liteloader;

import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundRegistry;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiNewChat;

import com.mumfrey.liteloader.core.runtime.Obf;
import com.mumfrey.liteloader.util.PrivateFields;

public class TabbyPrivateFields<P, T> extends PrivateFields<P, T> {

    public static final PrivateFields<GuiIngame, GuiNewChat> persistantChatGUI = new TabbyPrivateFields<GuiIngame, GuiNewChat>(GuiIngame.class, TabbyObf.persistantChatGUI);
    public static final PrivateFields<GuiChat, String> defaultInputFieldText = new TabbyPrivateFields<GuiChat, String>(GuiChat.class, TabbyObf.defaultInputFieldText);
    public static final PrivateFields<SoundHandler, SoundRegistry> sndRegistry = new TabbyPrivateFields<SoundHandler, SoundRegistry>(SoundHandler.class, TabbyObf.sndRegistry);
    
    protected TabbyPrivateFields(Class<P> owner, Obf obf) {
        super(owner, obf);
        // TODO Auto-generated constructor stub
    }

}
