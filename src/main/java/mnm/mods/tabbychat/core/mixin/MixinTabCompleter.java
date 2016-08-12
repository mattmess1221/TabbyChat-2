package mnm.mods.tabbychat.core.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import mnm.mods.tabbychat.core.overlays.IChatTabCompleter;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.TabCompleter;

@Mixin(TabCompleter.class)
public class MixinTabCompleter implements IChatTabCompleter {

    @Final
    @Shadow
    @Mutable
    protected GuiTextField textField;

    @Override
    public void setTextField(GuiTextField textfield) {
        this.textField = textfield;
    }
}
