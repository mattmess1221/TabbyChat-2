package mnm.mods.tabbychat.core;

import org.lwjgl.input.Keyboard;

import com.google.common.eventbus.Subscribe;

import mnm.mods.util.gui.GuiButton;
import mnm.mods.util.gui.events.ActionPerformedEvent;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.play.client.CPacketEntityAction;

public class GuiSleepTC extends GuiChatTC {

    @Override
    public void initGui() {
        super.initGui();
        GuiButton button = new GuiButton(I18n.format("multiplayer.stopSleeping"));
        button.setBounds(width / 2 - 100, height - 30, 200, 20);
        button.getBus().register(this);
        this.componentList.add(button);
    }

    @Subscribe
    public void wakeUpSleepyHead(ActionPerformedEvent event) {
        wakeFromSleep();
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

    private void wakeFromSleep() {
        NetHandlerPlayClient nethandlerplayclient = this.mc.thePlayer.connection;
        nethandlerplayclient.sendPacket(new CPacketEntityAction(this.mc.thePlayer, CPacketEntityAction.Action.STOP_SLEEPING));
    }
}
