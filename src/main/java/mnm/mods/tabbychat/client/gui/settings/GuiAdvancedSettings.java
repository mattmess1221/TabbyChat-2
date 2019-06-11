package mnm.mods.tabbychat.client.gui.settings;

import static mnm.mods.tabbychat.util.Translation.*;

import mnm.mods.tabbychat.client.TabbyChatClient;
import mnm.mods.tabbychat.client.settings.TabbySettings;
import mnm.mods.tabbychat.util.LocalVisibility;
import mnm.mods.tabbychat.util.Color;
import mnm.mods.tabbychat.client.gui.component.layout.GuiGridLayout;
import mnm.mods.tabbychat.client.gui.component.GuiLabel;
import mnm.mods.tabbychat.client.gui.component.config.GuiSettingBoolean;
import mnm.mods.tabbychat.client.gui.component.config.GuiSettingEnum;
import mnm.mods.tabbychat.client.gui.component.config.GuiSettingNumber.GuiSettingInt;
import mnm.mods.tabbychat.client.gui.component.config.SettingPanel;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TranslationTextComponent;

public class GuiAdvancedSettings extends SettingPanel<TabbySettings> {

    public GuiAdvancedSettings() {
        setLayout(new GuiGridLayout(10, 15));
        setDisplayString(I18n.format(SETTINGS_ADVANCED));
        setSecondaryColor(Color.of(255, 0, 0, 64));
    }

    @Override
    public void initGUI() {
        addComponent(new GuiLabel(new TranslationTextComponent(ADVANCED_FADE_TIME)), new int[]{1, 1});
        GuiSettingInt gsi = new GuiSettingInt(getSettings().advanced.fadeTime);
        gsi.getComponent().setInterval(50);
        addComponent(gsi, new int[]{5, 1, 2, 1});

//        addComponent(new GuiLabel(new TextComponentTranslation(ADVANCED_CHAT_DELAY)), new int[]{1, 3});
//        gsi = new GuiSettingInt(getSettings().advanced.msgDelay);
//        gsi.getComponent().setInterval(50);
//        addComponent(gsi, new int[]{5, 3, 2, 1});

        addComponent(new GuiLabel(new TranslationTextComponent(ADVANCED_CHAT_VISIBILITY)), new int[]{1, 3});
        addComponent(new GuiSettingEnum<>(getSettings().advanced.visibility, LocalVisibility.values()), new int[]{5, 3, 3, 1});

        addComponent(new GuiLabel(new TranslationTextComponent(ADVANCED_HIDE_DELIMS)), new int[]{2, 5});
        addComponent(new GuiSettingBoolean(getSettings().advanced.hideTag), new int[]{1, 5});

        addComponent(new GuiLabel(new TranslationTextComponent(ADVANCED_SPELLCHECK)), new int[]{2, 6});
        addComponent(new GuiSettingBoolean(getSettings().advanced.spelling), new int[]{1, 6});

        addComponent(new GuiLabel(new TranslationTextComponent(EXPERIMENTAL)), new int[]{0, 13});
    }

    @Override
    public TabbySettings getSettings() {
        return TabbyChatClient.getInstance().getSettings();
    }
}
