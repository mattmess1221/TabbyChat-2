package mnm.mods.tabbychat.gui;

import mnm.mods.tabbychat.core.GuiNewChatTC;
import mnm.mods.util.gui.GuiPanel;
import mnm.mods.util.gui.ILayout;

public class ChatPanel extends GuiPanel {

    public ChatPanel() {
        super();
    }

    public ChatPanel(ILayout layout) {
        super(layout);
    }

    @Override
    public boolean isVisible() {
        return super.isVisible() && GuiNewChatTC.getInstance().getChatOpen();
    }
}
