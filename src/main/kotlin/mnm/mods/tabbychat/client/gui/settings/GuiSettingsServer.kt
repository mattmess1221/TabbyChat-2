package mnm.mods.tabbychat.client.gui.settings;

import mnm.mods.tabbychat.client.TabbyChatClient;
import mnm.mods.tabbychat.client.gui.component.GuiLabel;
import mnm.mods.tabbychat.client.gui.component.config.GuiSettingBoolean;
import mnm.mods.tabbychat.client.gui.component.config.GuiSettingEnum;
import mnm.mods.tabbychat.client.gui.component.config.GuiSettingString;
import mnm.mods.tabbychat.client.gui.component.config.GuiSettingStringList;
import mnm.mods.tabbychat.client.gui.component.config.SettingPanel;
import mnm.mods.tabbychat.client.gui.component.layout.GuiGridLayout;
import mnm.mods.tabbychat.client.settings.GeneralServerSettings;
import mnm.mods.tabbychat.client.settings.ServerSettings;
import mnm.mods.tabbychat.util.ChannelPatterns;
import mnm.mods.tabbychat.util.Color;
import mnm.mods.tabbychat.util.MessagePatterns;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TranslationTextComponent;

import static mnm.mods.tabbychat.util.Translation.*;

public class GuiSettingsServer extends SettingPanel<ServerSettings> {

    GuiSettingsServer() {
        this.setLayout(new GuiGridLayout(10, 20));
        this.setDisplayString(I18n.format(SETTINGS_SERVER));
        this.setSecondaryColor(Color.of(255, 215, 0, 64));
    }

    @Override
    public void initGUI() {
        GeneralServerSettings sett = getSettings().general;

        int pos = 1;
        this.add(new GuiLabel(new TranslationTextComponent(CHANNELS_ENABLED)), new int[]{2, pos});
        GuiSettingBoolean chkChannels = new GuiSettingBoolean(sett.channelsEnabled);
        chkChannels.setCaption(new TranslationTextComponent(CHANNELS_ENABLED_DESC));
        this.add(chkChannels, new int[]{1, pos});

        pos += 1;
        this.add(new GuiLabel(new TranslationTextComponent(PM_ENABLED)), new int[]{2, pos});
        GuiSettingBoolean chkPM = new GuiSettingBoolean(sett.pmEnabled);
        chkPM.setCaption(new TranslationTextComponent(PM_ENABLED_DESC));
        this.add(chkPM, new int[]{1, pos});

        pos += 1;
        add(new GuiLabel(new TranslationTextComponent(USE_DEFAULT)), new int[]{2, pos});
        add(new GuiSettingBoolean(sett.useDefaultTab), new int[]{1, pos});

        pos += 2;
        this.add(new GuiLabel(new TranslationTextComponent(CHANNEL_PATTERN)), new int[]{1, pos});
        GuiSettingEnum<ChannelPatterns> enmChanPat = new GuiSettingEnum<>(sett.channelPattern,
                ChannelPatterns.values());
        enmChanPat.setCaption(new TranslationTextComponent(CHANNEL_PATTERN_DESC));
        this.add(enmChanPat, new int[]{5, pos, 4, 1});

        pos += 2;
        this.add(new GuiLabel(new TranslationTextComponent(MESSAGE_PATTERN)), new int[]{1, pos});
        if (sett.messegePattern.get() == null) {
            sett.messegePattern.set(MessagePatterns.WHISPERS);
        }
        GuiSettingEnum<MessagePatterns> enmMsg = new GuiSettingEnum<>(sett.messegePattern, MessagePatterns.values());
        enmMsg.setCaption(new TranslationTextComponent(MESSAGE_PATTERN_DESC));
        this.add(enmMsg, new int[]{5, pos, 4, 1});

        pos += 2;
        this.add(new GuiLabel(new TranslationTextComponent(IGNORED_CHANNELS)), new int[]{0, pos});
        GuiSettingStringList strIgnored = new GuiSettingStringList(sett.ignoredChannels);
        strIgnored.setCaption(new TranslationTextComponent(IGNORED_CHANNELS_DESC));
        this.add(strIgnored, new int[]{5, pos, 5, 1});

        pos += 2;
        this.add(new GuiLabel(new TranslationTextComponent(DEFAULT_CHANNEL_COMMAND)), new int[]{0, pos});
        GuiSettingString strChannels = new GuiSettingString(sett.channelCommand);
        strChannels.setCaption(new TranslationTextComponent(DEFAULT_CHANNEL_COMMAND_DESC));
        this.add(strChannels, new int[]{5, pos, 5, 1});

        pos += 2;
        this.add(new GuiLabel(new TranslationTextComponent(DEFAULT_CHANNEL)), new int[]{0, pos});
        GuiSettingString strMessages = new GuiSettingString(sett.defaultChannel);
        strMessages.setCaption(new TranslationTextComponent(DEFAULT_CHANNEL_DESC));
        this.add(strMessages, new int[]{5, pos, 5, 1});
    }

    @Override
    public ServerSettings getSettings() {
        return TabbyChatClient.getInstance().getServerSettings();
    }

}
