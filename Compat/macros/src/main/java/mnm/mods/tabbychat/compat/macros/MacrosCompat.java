package mnm.mods.tabbychat.compat.macros;

import java.awt.Rectangle;

import mnm.mods.tabbychat.api.listener.ChatInputListener;
import mnm.mods.tabbychat.api.listener.ChatScreenListener;
import mnm.mods.tabbychat.api.listener.ChatScreenRenderer;
import mnm.mods.tabbychat.api.listener.events.ChatInitEvent;
import net.eq2online.macros.compatibility.LocalisationProvider;
import net.eq2online.macros.core.MacroModCore;
import net.eq2online.macros.gui.controls.GuiDropDownMenu;
import net.eq2online.macros.gui.controls.GuiMiniToolbarButton;
import net.eq2online.macros.gui.designable.DesignableGuiLayout;
import net.eq2online.macros.gui.designable.LayoutManager;
import net.eq2online.macros.gui.screens.GuiCustomGui;
import net.eq2online.macros.gui.screens.GuiCustomGuiProxy;
import net.eq2online.macros.gui.screens.GuiDesigner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;

import com.mumfrey.liteloader.transformers.event.EventInfo;

public class MacrosCompat implements ChatScreenListener, ChatScreenRenderer, ChatInputListener {

    private Minecraft mc;
    private DesignableGuiLayout inchat;
    private GuiChat screen;
    private GuiMiniToolbarButton button;
    private boolean isHovered;
    private GuiCustomGui gui;
    private GuiDropDownMenu contextMenu;

    @Override
    public void onRender(int mouseX, int mouseY, float parTick) {
        inchat.onTick();
        inchat.draw(new Rectangle(0, 0, screen.width, screen.height), mouseX, mouseY);
        isHovered = button.drawControlAt(mc, mouseX, mouseY,
                screen.width - 20, screen.height - 14,
                0xff00ee00, 0x80000000);
    }

    @Override
    public void onInitScreen(ChatInitEvent chatInitEvent) {
        mc = Minecraft.getMinecraft();
        screen = chatInitEvent.chatScreen;
        button = new GuiMiniToolbarButton(mc, 4, 104, 64);
        inchat = LayoutManager.getBoundLayout("inchat", false);
        gui = new GuiCustomGui(inchat, screen);
        contextMenu = GuiCustomGuiProxy.getContextMenu(gui);

        String guiedit = LocalisationProvider.getLocalisedString("tooltip.guiedit");
        contextMenu.addItem("design", "\247e" + guiedit, 26, 16);
    }

    @Override
    public boolean onMouseClicked(int mouseX, int mouseY, int button) {

        boolean clicked = GuiCustomGuiProxy.controlClicked(gui, mouseX, mouseY, button);

        if (clicked && button == 1) {
            contextMenu.showDropDown();
            /*
            Dimension size = contextMenu.getSize();
            int x = Math.min(mouseX, screen.width - size.width);
            int y = Math.min(mouseY - 8, screen.height - size.height);
            GuiCustomGuiProxy.setContextMenuLocation(gui, new Point(x, y));
             */
        }
        if (clicked) {
            return true;
        }
        if (isHovered) {
            GuiScreen designer = new GuiDesigner("inchat", screen, true);
            Minecraft.getMinecraft().displayGuiScreen(designer);
            return true;
        }
        return clicked;
    }

    @Override
    public boolean onKeyTyped(char ch, int code) {
        return false;
    }

    @Override
    public void onUpdateScreen() {
        MacroModCore.onChatGuiEvent(new EventInfo<GuiChat>("", screen, false));
    }

    @Override
    public void onCloseScreen() {}
}
