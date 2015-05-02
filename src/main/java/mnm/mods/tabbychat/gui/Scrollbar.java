package mnm.mods.tabbychat.gui;

import mnm.mods.tabbychat.core.GuiNewChatTC;
import mnm.mods.util.gui.GuiComponent;
import net.minecraft.client.gui.Gui;

public class Scrollbar extends GuiComponent {

    private ChatArea chat;

    public Scrollbar(ChatArea chat) {
        this.chat = chat;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        if (GuiNewChatTC.getInstance().getChatOpen()) {
            int scroll = chat.getScrollPos();
            int max = chat.getBounds().height;
            int lines = max / mc.fontRendererObj.FONT_HEIGHT;
            int total = chat.getChat(false).size();
            if (total <= lines) {
                return;
            }
            total -= lines;
            int size = Math.max(max / 2 - total, 10);
            float perc = Math.abs((float) scroll / (float) total - 1) * Math.abs((float) size / (float) max - 1);
            int pos = (int) (perc * max);

            Gui.drawRect(0, pos, 1, pos + size, -1);
        }
    }

}
