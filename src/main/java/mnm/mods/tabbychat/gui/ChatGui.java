package mnm.mods.tabbychat.gui;

import mnm.mods.tabbychat.core.GuiNewChatTC;
import mnm.mods.util.gui.GuiComponent;

public class ChatGui extends GuiComponent {

    @Override
    public boolean isVisible() {
        return super.isVisible() && GuiNewChatTC.getInstance().getChatOpen();
    }
}
