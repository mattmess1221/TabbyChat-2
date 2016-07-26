package mnm.mods.tabbychat.core.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import mnm.mods.tabbychat.core.overlays.IGuiIngame;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiNewChat;

@Mixin(GuiIngame.class)
public class MixinGuiIngame extends Gui implements IGuiIngame {

    @Shadow
    @Final
    @Mutable
    private GuiNewChat persistantChatGUI;

    @Override
    public void setChatGUI(GuiNewChat chat) {
        this.persistantChatGUI = chat;
    }
}
