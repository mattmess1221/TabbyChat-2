package mnm.mods.tabbychat.core;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.google.common.collect.Lists;

import mnm.mods.tabbychat.ChatManager;
import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.events.ChatMessageEvent.ChatSentEvent;
import mnm.mods.tabbychat.api.events.ChatScreenEvents.ChatCloseEvent;
import mnm.mods.tabbychat.api.events.ChatScreenEvents.ChatInitEvent;
import mnm.mods.tabbychat.api.events.ChatScreenEvents.ChatUpdateEvent;
import mnm.mods.tabbychat.util.BackgroundChatThread;
import mnm.mods.util.gui.GuiComponent;
import mnm.mods.util.gui.GuiText;
import mnm.mods.util.gui.events.GuiKeyboardEvent;
import mnm.mods.util.gui.events.GuiMouseEvent;
import mnm.mods.util.gui.events.GuiMouseEvent.MouseEvent;
import mnm.mods.util.gui.events.GuiRenderEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.ITextComponent;

public class GuiChatTC extends GuiChat {

    protected Minecraft mc = Minecraft.getMinecraft();
    protected List<GuiComponent> componentList = Lists.newArrayList();
    protected GuiNewChatTC chatGui;
    protected ChatManager chat;

    private String defaultInputFieldText;
    private int sentHistoryIndex;
    private String sentHistoryBuffer = "";

    protected GuiText textBox;

    private boolean opened = false;

    private TabbyChat tc = TabbyChat.getInstance();

    public GuiChatTC() {
        this("");

    }

    public GuiChatTC(String text) {
        super(text);
        this.defaultInputFieldText = text;
        chatGui = tc.getChatGui();
        sentHistoryIndex = chatGui.getSentMessages().size();
        chat = chatGui.getChatManager();
        textBox = chat.getChatBox().getChatInput().getTextField();
        if (defaultInputFieldText.isEmpty()
                && !chat.getActiveChannel().isPrefixHidden()
                && !chat.getActiveChannel().getPrefix().isEmpty()) {
            defaultInputFieldText = chat.getActiveChannel().getPrefix() + " ";
        }

        this.componentList.add(chat.getChatBox());
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        chatGui.getBus().post(new ChatInitEvent(this));
        if (!opened) {
            textBox.setValue("");
            textBox.getTextField().writeText(defaultInputFieldText);
            this.opened = true;
            updateScreen();
        }
    }

    @Override
    public void updateScreen() {
        for (GuiComponent comp : this.componentList) {
            comp.updateComponent();
        }
        chatGui.getBus().post(new ChatUpdateEvent(this));
    }

    @Override
    public void onGuiClosed() {
        chatGui.getBus().post(new ChatCloseEvent(this));
        this.sentHistoryBuffer = "";
        for (GuiComponent comp : this.componentList) {
            comp.onClosed();
        }
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        for (GuiComponent comp : this.componentList) {
            comp.handleMouseInput();
        }
    }

    @Override
    public void handleKeyboardInput() throws IOException {
        super.handleKeyboardInput();
        for (GuiComponent comp : this.componentList) {
            comp.handleKeyboardInput();
        }
    }

    @Override
    protected void keyTyped(char key, int code) {
        chatGui.getBus().post(new GuiKeyboardEvent(null, code, key, 0L));
        switch (code) {
        case Keyboard.KEY_RETURN:
        case Keyboard.KEY_NUMPADENTER:
            // send chat
            sendCurrentChat(tc.settings.advanced.keepChatOpen.get());
            break;
        case Keyboard.KEY_DOWN:
            // next send
            if (this.sentHistoryIndex < chatGui.getSentMessages().size() - 1) {
                this.sentHistoryIndex++;
                this.textBox.setValue(chatGui.getSentMessages().get(this.sentHistoryIndex));
            } else {
                this.sentHistoryIndex = chatGui.getSentMessages().size();
                this.textBox.setValue(sentHistoryBuffer);
            }
            break;
        case Keyboard.KEY_UP:
            // previous send
            if (this.sentHistoryIndex > 0) {
                this.sentHistoryIndex--;
                this.textBox.setValue(chatGui.getSentMessages().get(this.sentHistoryIndex));
            }
            break;
        case Keyboard.KEY_ESCAPE:
            // close chat
            mc.displayGuiScreen(null);
            break;
        case Keyboard.KEY_PRIOR:
            // Page up
            this.chatGui.getChatManager().getChatBox().getChatArea().scroll(chatGui.getLineCount() + 1);
            break;
        case Keyboard.KEY_NEXT:
            // Page down
            this.chatGui.getChatManager().getChatBox().getChatArea().scroll(-chatGui.getLineCount() - 1);
            break;
        default:
            // type
            this.textBox.getTextField().textboxKeyTyped(key, code);
        }
        if (code != Keyboard.KEY_UP && code != Keyboard.KEY_DOWN) {
            sentHistoryBuffer = textBox.getValue();
            sentHistoryIndex = chatGui.getSentMessages().size();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        chatGui.getBus().post(new GuiMouseEvent(null, MouseEvent.CLICK, mouseX, mouseY, mouseButton, 0));
        if (mouseButton == 0) {
            ITextComponent chat = chatGui.getChatComponent(Mouse.getX(), Mouse.getY());
            this.handleComponentClick(chat);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float tick) {

        // Draw the components
        for (GuiComponent component : componentList) {
            if (component.isVisible()) {
                GlStateManager.pushMatrix();
                GlStateManager.scale(component.getScale(), component.getScale(), 0);
                GlStateManager.translate(component.getBounds().x, component.getBounds().y, 0);
                component.drawComponent(mouseX, mouseY);
                GlStateManager.popMatrix();
            }
        }
        chatGui.getBus().post(new GuiRenderEvent(null, mouseX, mouseY, tick));

        ITextComponent chat = chatGui.getChatComponent(Mouse.getX(), Mouse.getY());
        this.handleComponentHover(chat, mouseX, mouseY);

    }

    protected void sendCurrentChat(boolean keepOpen) {
        String message = this.textBox.getValue().trim();
        // send the outbound message to ChatSent modules.
        chatGui.getBus().post(new ChatSentEvent(message));
        Channel active = chat.getActiveChannel();
        String[] toSend = processSends(message, active.getPrefix(), active.isPrefixHidden());
        // time to wait between each send
        long wait = tc.settings.advanced.msgDelay.get();
        new BackgroundChatThread(this, toSend, wait).start();

        // add to sent chat manually
        chatGui.addToSentMessages(message);
        this.sentHistoryIndex = chatGui.getSentMessages().size();
        this.sentHistoryBuffer = "";

        chatGui.resetScroll();
        textBox.setValue("");

        if (!keepOpen) {
            mc.displayGuiScreen(null);
        } else {
            this.textBox.setValue("");
        }
    }

    public static String[] processSends(String msg, String prefix, boolean hidden) {
        if (StringUtils.isEmpty(msg)) {
            return null;
        }
        // get rid of spaces
        msg = msg.trim().replaceAll("  +", " ");
        int len = 100 - prefix.length();
        if (!hidden && msg.startsWith(prefix)) {
            msg = msg.substring(prefix.length()).trim();
        }
        String[] sends = WordUtils.wrap(msg, len).split("\r?\n");

        // is command && (no prefix || not right prefix)
        if (sends[0].startsWith("/") && (StringUtils.isEmpty(prefix) || !sends[0].startsWith(prefix))) {
            // limit commands to 1 send.
            return new String[] { sends[0] };
        }
        if (StringUtils.isEmpty(prefix)) {
            return sends;
        }

        for (int i = 0; i < sends.length; i++) {
            sends[i] = prefix + " " + sends[i];
        }

        return sends;

    }

    @Override
    public void setCompletions(String... newCompletions) {
        this.chat.getChatBox().setCompletions(newCompletions);
    }

    @Override
    protected void setText(String text, boolean overwrite) {
        if (overwrite) {
            textBox.getTextField().setText(text);
        } else {
            textBox.getTextField().writeText(text);
        }
    }

}
