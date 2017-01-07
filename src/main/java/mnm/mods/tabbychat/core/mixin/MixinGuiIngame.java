package mnm.mods.tabbychat.core.mixin;

import mnm.mods.tabbychat.core.overlays.IGuiIngame;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiIngame.class)
public abstract class MixinGuiIngame extends Gui implements IGuiIngame {

    @Override
    @Accessor
    public abstract void setPersistantChatGUI(GuiNewChat chat);
}
