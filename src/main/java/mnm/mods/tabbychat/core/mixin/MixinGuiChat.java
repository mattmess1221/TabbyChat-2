package mnm.mods.tabbychat.core.mixin;

import com.google.common.collect.Lists;
import mnm.mods.tabbychat.ChatManager;
import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.events.ChatScreenEvents.ChatInitEvent;
import mnm.mods.tabbychat.core.GuiNewChatTC;
import mnm.mods.util.gui.GuiComponent;
import mnm.mods.util.gui.GuiText;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ITabCompleter;
import net.minecraft.util.TabCompleter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.List;

@Mixin(GuiChat.class)
public abstract class MixinGuiChat extends GuiScreen implements ITabCompleter {

    private final GuiChat that = (GuiChat) (Object) this;

    protected List<GuiComponent> componentList = Lists.newArrayList();
    private GuiNewChatTC chatGui;
    private ChatManager chat;
    private GuiText textBox;

    @Shadow
    private String historyBuffer;
    @Shadow
    private int sentHistoryCursor;
    @Shadow
    private TabCompleter tabCompleter;
    @Shadow
    protected GuiTextField inputField;
    @Shadow
    private String defaultInputFieldText;

    private boolean opened;

    private TabbyChat tc = TabbyChat.getInstance();

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void onInitialization(CallbackInfo ci) {

        this.chatGui = tc.getChatGui();
        this.sentHistoryCursor = chatGui.getSentMessages().size();
        this.chat = chatGui.getChatManager();
        this.textBox = chat.getChatBox().getChatInput().getTextField();

        Channel chan = chat.getActiveChannel();
        if (this.defaultInputFieldText.isEmpty()
                && !chan.isPrefixHidden()
                && !chan.getPrefix().isEmpty()) {
            defaultInputFieldText = chan.getPrefix() + " ";
        }

        this.componentList.add(chat.getChatBox());
    }

    @Inject(method = "initGui()V", at = @At("RETURN"))
    private void onInitGui(CallbackInfo ci) {
        this.inputField = this.textBox.getTextField();
        ((IChatTabCompleter) this.tabCompleter).setTextField(this.inputField);
        chatGui.getBus().post(new ChatInitEvent(that));
        if (!opened) {
            textBox.setValue("");
            textBox.getTextField().writeText(defaultInputFieldText);
            this.opened = true;
            updateScreen();
        }
    }

    @Inject(method = "updateScreen()V", at = @At("RETURN"))
    private void onUpdateScreen(CallbackInfo ci) {
        this.componentList.forEach(GuiComponent::updateComponent);
    }

    @Inject(method = "onGuiClosed()V", at = @At("RETURN"))
    private void onChatClosed(CallbackInfo ci) {
        this.historyBuffer = "";
        this.componentList.forEach(GuiComponent::onClosed);
    }

    @Override
    public void handleKeyboardInput() throws IOException {
        super.handleKeyboardInput();
        this.componentList.forEach(GuiComponent::handleKeyboardInput);
    }

    @Inject(method = "handleMouseInput()V", at = @At("RETURN"))
    private void onHandleMouseInput(CallbackInfo ci) {
        this.componentList.forEach(GuiComponent::handleMouseInput);
    }

    @Inject(
            method = "keyTyped(CI)V",
            cancellable = true,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V",
                    ordinal = 1))
    private void keepChatOpen(char key, int code, CallbackInfo ci) {
        this.chatGui.resetScroll();
        setText(this.defaultInputFieldText, true);
        if (tc.settings.advanced.keepChatOpen.get()) {
            ci.cancel();
        }
    }

    @Redirect(
            method = "drawScreen(IIF)V",
            require = 1,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiChat;drawRect(IIIII)V"))
    private void onDrawScreen(int x1, int y1, int x2, int y2, int color) {
        // noop
    }

}
