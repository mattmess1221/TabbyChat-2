package mnm.mods.tabbychat.client.gui;

import mnm.mods.tabbychat.client.gui.component.GuiPanel;
import mnm.mods.tabbychat.client.gui.component.ILayout;

public class ChatPanel extends GuiPanel {

    public ChatPanel() {
        super();
    }

    public ChatPanel(ILayout layout) {
        super(layout);
    }

    @Override
    public boolean isVisible() {
        return super.isVisible() && mc.ingameGUI.getChatGUI().getChatOpen();
    }
}
