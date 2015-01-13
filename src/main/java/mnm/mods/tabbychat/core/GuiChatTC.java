package mnm.mods.tabbychat.core;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.gui.ChatBox;
import mnm.mods.tabbychat.gui.TextBox;
import mnm.mods.tabbychat.util.ForgeClientCommands;
import mnm.mods.util.gui.GuiComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MovingObjectPosition;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Lists;
import com.google.common.collect.ObjectArrays;

public class GuiChatTC extends GuiChat {

    protected Minecraft mc = Minecraft.getMinecraft();
    protected List<GuiComponent> componentList = Lists.newArrayList();
    protected GuiNewChatTC chatGui = GuiNewChatTC.getInstance();
    protected ChatBox chatbox;
    private int sentHistoryIndex;
    private String sentHistoryBuffer = "";

    protected TextBox textBox;

    private boolean waitingOnAutocomplete = false;
    private boolean playerNamesFound;
    private List<String> foundPlayerNames = Lists.newArrayList();
    private int autocompleteIndex;

    private boolean opened = false;

    private TabbyChat tc = TabbyChat.getInstance();

    public GuiChatTC() {
        this("");

    }

    public GuiChatTC(String text) {
        super(text);
        sentHistoryIndex = chatGui.getSentMessages().size();
        chatbox = chatGui.getChatbox();
        textBox = chatbox.getChatInput();
    }

    @Override
    public void initGui() {
        super.initGui();
        tc.getEventManager().onInitScreen(componentList);
        if (!opened) {
            textBox.clear();
            textBox.writeText(defaultInputFieldText);
            this.opened = true;
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        chatbox.updateComponent();
        for (GuiComponent comp : this.componentList) {
            comp.updateComponent();
        }
        tc.getEventManager().onUpdateScreen();
    }

    public boolean hasOpened() {
        return this.opened;
    }

    @Override
    public void onGuiClosed() {
        tc.getEventManager().onCloseScreen();
        this.sentHistoryBuffer = "";
        textBox.clear();
        chatbox.getChatArea().resetScroll();
        super.onGuiClosed();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        tc.getEventManager().onActionPerformed(button);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.chatbox.handleMouseInput();
        for (GuiComponent comp : this.componentList) {
            comp.handleMouseInput();
        }
    }

    @Override
    public void handleKeyboardInput() throws IOException {
        super.handleKeyboardInput();
        this.chatbox.handleKeyboardInput();
        for (GuiComponent comp : this.componentList) {
            comp.handleKeyboardInput();
        }
    }

    @Override
    protected void keyTyped(char key, int code) {
        this.waitingOnAutocomplete = false;
        if (code != Keyboard.KEY_TAB) {
            this.playerNamesFound = false;
        }
        switch (code) {
        case Keyboard.KEY_RETURN:
        case Keyboard.KEY_NUMPADENTER:
            sendCurrentChat(false);
            break;
        case Keyboard.KEY_TAB:
            autocompletePlayerNames();
            break;
        case Keyboard.KEY_DOWN:
            if (this.sentHistoryIndex < chatGui.getSentMessages().size() - 1) {
                this.sentHistoryIndex++;
                this.textBox.setText(chatGui.getSentMessages().get(this.sentHistoryIndex));
            } else {
                this.sentHistoryIndex = chatGui.getSentMessages().size();
                this.textBox.setText(sentHistoryBuffer);
            }
            break;
        case Keyboard.KEY_UP:
            if (this.sentHistoryIndex > 0) {
                this.sentHistoryIndex--;
                this.textBox.setText(chatGui.getSentMessages().get(this.sentHistoryIndex));
            }
            break;
        case Keyboard.KEY_ESCAPE:
            mc.displayGuiScreen(null);
            break;
        default:
            this.textBox.keyTyped(key, code);
        }
        if (code != Keyboard.KEY_UP && code != Keyboard.KEY_DOWN) {
            sentHistoryBuffer = textBox.getText();
            sentHistoryIndex = chatGui.getSentMessages().size();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {
            IChatComponent chat = chatGui.getChatComponent(mouseX, mouseY);
            this.handleComponentClick(chat);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float tick) {

        IChatComponent chat = chatGui.getChatComponent(mouseX, mouseY);
        this.handleComponentHover(chat, mouseX, mouseY);

        // Draw the components
        for (GuiComponent component : componentList) {
            if (component.visible) {
                component.drawComponent(mouseX, mouseY);
            }
        }

        // Draw buttons
        for (Object button : this.buttonList) {
            ((GuiButton) button).drawButton(mc, mouseX, mouseY);
        }
        // Draw labels
        for (Object label : this.labelList) {
            ((GuiLabel) label).drawLabel(mc, mouseX, mouseY);
        }
    }

    protected void sendCurrentChat(boolean keepOpen) {
        String message = this.textBox.getText().trim();
        // send the outbound message to ChatSent modules.
        message = tc.getEventManager().onChatSent(message);

        if (message != null && !message.isEmpty()) {
            this.sendChatMessage(message);
            this.sentHistoryIndex = chatGui.getSentMessages().size();
            this.sentHistoryBuffer = "";
        }
        chatGui.resetScroll();
        textBox.clear();

        if (!keepOpen) {
            mc.displayGuiScreen(null);
        } else {
            this.textBox.setText("");
        }
    }

    private String getCleanText(String dirty) {
        return EnumChatFormatting.getTextWithoutFormattingCodes(dirty);
    }

    private void printListToChat(List<String> list, int id) {
        // TODO Pagination

        // Limit number of items to 50.
        if (list.size() > 50) {
            list = list.subList(0, 49);
        }
        StringBuilder sb = new StringBuilder();

        Iterator<String> iter = list.iterator();
        while (iter.hasNext()) {
            String next = iter.next();
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(next);
        }
        chatGui.printChatMessageWithOptionalDeletion(new ChatComponentText(sb.toString()), id);
    }

    @Override
    public void autocompletePlayerNames() {
        String s1;
        if (this.playerNamesFound) {
            this.textBox.deleteFromCursor(this.textBox.func_146197_a(-1,
                    this.textBox.getCursorPosition(), false)
                    - this.textBox.getCursorPosition());

            if (this.autocompleteIndex >= this.foundPlayerNames.size()) {
                this.autocompleteIndex = 0;
            }
        } else {
            int i = this.textBox.func_146197_a(-1, this.textBox.getCursorPosition(), false);
            this.foundPlayerNames.clear();
            this.autocompleteIndex = 0;
            String s = this.textBox.getText().substring(i).toLowerCase();
            s1 = this.textBox.getText().substring(0, this.textBox.getCursorPosition());
            this.sendAutocompleteRequest(s1, s);

            if (foundPlayerNames.isEmpty()) {
                return;
            }

            this.playerNamesFound = true;
            this.textBox.deleteFromCursor(i - this.textBox.getCursorPosition());

        }

        if (this.foundPlayerNames.size() > 1) {
            this.printListToChat(foundPlayerNames, 1);
        }

        textBox.writeText(getCleanText(this.foundPlayerNames.get(this.autocompleteIndex++)));
    }

    private void sendAutocompleteRequest(String word, String s1) {
        if (word.length() >= 1) {
            // Forge auto complete
            ForgeClientCommands.autoComplete(word, s1);

            BlockPos blockpos = null;
            if (this.mc.objectMouseOver != null
                    && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                blockpos = this.mc.objectMouseOver.getBlockPos();
            }

            this.mc.thePlayer.sendQueue.addToSendQueue(new C14PacketTabComplete(word, blockpos));
            this.waitingOnAutocomplete = true;
        }
    }

    @Override
    public void onAutocompleteResponse(String[] array) {
        if (this.waitingOnAutocomplete) {
            this.playerNamesFound = false;
            this.foundPlayerNames.clear();

            // Forge auto complete
            String[] complete = ForgeClientCommands.getLatestAutoComplete();
            if (complete != null) {
                array = ObjectArrays.concat(complete, array, String.class);
            }

            // Put completions in list
            for (String string : array) {
                foundPlayerNames.add(string);
            }

            String s1 = this.textBox.getText().substring(
                    this.textBox.func_146197_a(-1, this.textBox.getCursorPosition(), false));
            String s2 = StringUtils.getCommonPrefix(array);

            if (s2.length() > 0 && !s1.equalsIgnoreCase(s2)) {
                this.textBox.deleteFromCursor(this.textBox.func_146197_a(-1,
                        this.textBox.getCursorPosition(), false)
                        - this.textBox.getCursorPosition());

                this.textBox.writeText(s2);
            } else if (this.foundPlayerNames.size() > 0) {
                this.playerNamesFound = true;
                this.autocompletePlayerNames();
            }
        }
    }
}
