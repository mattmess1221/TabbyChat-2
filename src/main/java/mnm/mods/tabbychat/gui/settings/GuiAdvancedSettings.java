package mnm.mods.tabbychat.gui.settings;

import static mnm.mods.tabbychat.util.Translation.*;

import mnm.mods.tabbychat.TabbyChatClient;
import mnm.mods.tabbychat.settings.TabbySettings;
import mnm.mods.tabbychat.util.ChatVisibility;
import mnm.mods.util.Color;
import mnm.mods.util.gui.GuiGridLayout;
import mnm.mods.util.gui.GuiLabel;
import mnm.mods.util.gui.config.GuiSettingBoolean;
import mnm.mods.util.gui.config.GuiSettingEnum;
import mnm.mods.util.gui.config.GuiSettingNumber.GuiSettingInt;
import mnm.mods.util.gui.config.SettingPanel;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

public class GuiAdvancedSettings extends SettingPanel<TabbySettings> {

    public GuiAdvancedSettings() {
        setLayout(new GuiGridLayout(10, 15));
        setDisplayString(I18n.format(SETTINGS_ADVANCED));
        setSecondaryColor(Color.of(255, 0, 0, 64));
    }

    @Override
    public void initGUI() {
        addComponent(new GuiLabel(new TextComponentTranslation(ADVANCED_FADE_TIME)), new int[]{1, 1});
        GuiSettingInt gsi = new GuiSettingInt(getSettings().advanced.fadeTime);
        gsi.getComponent().setInterval(50);
        addComponent(gsi, new int[]{5, 1, 2, 1});

//        addComponent(new GuiLabel(new TextComponentTranslation(ADVANCED_CHAT_DELAY)), new int[]{1, 3});
//        gsi = new GuiSettingInt(getSettings().advanced.msgDelay);
//        gsi.getComponent().setInterval(50);
//        addComponent(gsi, new int[]{5, 3, 2, 1});

        addComponent(new GuiLabel(new TextComponentTranslation(ADVANCED_CHAT_VISIBILITY)), new int[]{1, 3});
        addComponent(new GuiSettingEnum<>(getSettings().advanced.visibility, ChatVisibility.values()), new int[]{5, 3, 3, 1});

        addComponent(new GuiLabel(new TextComponentTranslation(ADVANCED_HIDE_DELIMS)), new int[]{2, 5});
        addComponent(new GuiSettingBoolean(getSettings().advanced.hideTag), new int[]{1, 5});

        addComponent(new GuiLabel(new TextComponentTranslation(ADVANCED_SPELLCHECK)), new int[]{2, 6});
        addComponent(new GuiSettingBoolean(getSettings().advanced.spelling), new int[]{1, 6});

        addComponent(new GuiLabel(new TextComponentTranslation(EXPERIMENTAL)), new int[]{0, 13});
    }

    @Override
    public TabbySettings getSettings() {
        return TabbyChatClient.getInstance().getSettings();
    }
}
