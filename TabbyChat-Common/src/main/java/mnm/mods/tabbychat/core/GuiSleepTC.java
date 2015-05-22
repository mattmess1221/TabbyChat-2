package mnm.mods.tabbychat.core;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.play.client.C0BPacketEntityAction;

import org.lwjgl.input.Keyboard;

public class GuiSleepTC extends GuiChatTC {

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height - 30, I18n.format(
                "multiplayer.stopSleeping", new Object[0])));
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
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) {
            this.wakeFromSleep();
        } else {
            super.actionPerformed(button);
        }
    }

    private void wakeFromSleep() {
        NetHandlerPlayClient nethandlerplayclient = this.mc.thePlayer.sendQueue;
        nethandlerplayclient.addToSendQueue(new C0BPacketEntityAction(this.mc.thePlayer,
                C0BPacketEntityAction.Action.STOP_SLEEPING));
    }
}
