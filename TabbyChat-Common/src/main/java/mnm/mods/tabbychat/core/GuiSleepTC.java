package mnm.mods.tabbychat.core;

import mnm.mods.util.gui.GuiButton;
import mnm.mods.util.gui.events.ActionPerformed;
import mnm.mods.util.gui.events.GuiEvent;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.play.client.C0BPacketEntityAction;

import org.lwjgl.input.Keyboard;

public class GuiSleepTC extends GuiChatTC implements ActionPerformed {

    @Override
    public void initGui() {
        super.initGui();
        GuiButton button = new GuiButton(I18n.format("multiplayer.stopSleeping"));
        button.setBounds(width / 2 - 100, height - 30, 200, 20);
        button.addActionListener(this);
        this.componentList.add(button);
    }

    @Override
    protected void keyTyped(char key, int code) {
        switch (code) {
        case Keyboard.KEY_ESCAPE:
            this.wakeFromSleep();
            break;
        case Keyboard.KEY_RETURN:
        case Keyboard.KEY_NUMPADENTER:
            this.sendCurrentChat(true);
            break;
        default:
            super.keyTyped(key, code);
            break;
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (!mc.thePlayer.isPlayerSleeping()) {
            mc.displayGuiScreen(null);
        }
    }

    @Override
    public void action(GuiEvent event) {
        this.wakeFromSleep();
    }

    private void wakeFromSleep() {
        NetHandlerPlayClient nethandlerplayclient = this.mc.thePlayer.sendQueue;
        nethandlerplayclient.addToSendQueue(new C0BPacketEntityAction(this.mc.thePlayer,
                C0BPacketEntityAction.Action.STOP_SLEEPING));
    }
}
