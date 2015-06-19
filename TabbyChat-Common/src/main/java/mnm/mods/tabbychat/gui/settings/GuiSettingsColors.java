package mnm.mods.tabbychat.gui.settings;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.settings.TabbySettings;
import mnm.mods.tabbychat.util.Translation;
import mnm.mods.util.Color;
import mnm.mods.util.gui.GuiGridLayout;
import mnm.mods.util.gui.GuiLabel;
import mnm.mods.util.gui.config.GuiSettingColor;
import mnm.mods.util.gui.config.SettingPanel;

public class GuiSettingsColors extends SettingPanel<TabbySettings> {

    public GuiSettingsColors() {
        setDisplayString(Translation.SETTINGS_COLORS.translate());
        setBackColor(Color.getColor(0, 255, 0, 64));
        this.setLayout(new GuiGridLayout(50, 50));
    }

    @Override
    public void initGUI() {
        this.addComponent(new GuiSettingColor(getSettings().colors.chatBoxColor), new int[] { 2, 1, 4, 4 });
        this.addComponent(new GuiSettingColor(getSettings().colors.chatTextColor), new int[] { 2, 6, 4, 4 });

        this.addComponent(new GuiLabel(Translation.COLOR_CHATBOX.translate()), new int[] { 8, 3 });
        this.addComponent(new GuiLabel(Translation.COLOR_CHAT_TEXT.translate()), new int[] { 8, 8 });
    }

    @Override
    public TabbySettings getSettings() {
        return TabbyChat.getInstance().settings;
    }

}
