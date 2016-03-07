package mnm.mods.tabbychat.gui.settings;

import static mnm.mods.tabbychat.util.Translation.*;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.settings.TabbySettings;
import mnm.mods.util.Color;
import mnm.mods.util.gui.GuiGridLayout;
import mnm.mods.util.gui.GuiLabel;
import mnm.mods.util.gui.config.GuiSettingColor;
import mnm.mods.util.gui.config.SettingPanel;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentTranslation;

public class GuiSettingsColors extends SettingPanel<TabbySettings> {

    public GuiSettingsColors() {
        setDisplayString(I18n.format(SETTINGS_COLORS));
        setBackColor(Color.of(0, 255, 0, 64));
        this.setLayout(new GuiGridLayout(50, 50));
    }

    @Override
    public void initGUI() {
        this.addComponent(new GuiSettingColor(getSettings().colors.chatBoxColor), new int[] { 2, 1, 4, 4 });
        this.addComponent(new GuiSettingColor(getSettings().colors.chatBorderColor), new int[] { 2, 6, 4, 4 });
        this.addComponent(new GuiSettingColor(getSettings().colors.chatTextColor), new int[] { 2, 11, 4, 4 });

        this.addComponent(new GuiLabel(new ChatComponentTranslation(COLOR_CHATBOX)), new int[] { 8, 3 });
        this.addComponent(new GuiLabel(new ChatComponentTranslation(COLOR_CHAT_BORDER)), new int[]{ 8, 8 });
        this.addComponent(new GuiLabel(new ChatComponentTranslation(COLOR_CHAT_TEXT)), new int[] { 8, 13 });

    }

    @Override
    public TabbySettings getSettings() {
        return TabbyChat.getInstance().settings;
    }

}
