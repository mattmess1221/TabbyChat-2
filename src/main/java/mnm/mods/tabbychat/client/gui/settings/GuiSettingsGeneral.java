package mnm.mods.tabbychat.client.gui.settings;

import static mnm.mods.tabbychat.util.Translation.*;

import mnm.mods.tabbychat.client.TabbyChatClient;
import mnm.mods.tabbychat.client.settings.GeneralSettings;
import mnm.mods.tabbychat.client.settings.TabbySettings;
import mnm.mods.tabbychat.util.TimeStamps;
import mnm.mods.tabbychat.util.Color;
import mnm.mods.tabbychat.client.gui.component.GuiGridLayout;
import mnm.mods.tabbychat.client.gui.component.GuiLabel;
import mnm.mods.tabbychat.client.gui.component.config.GuiSettingBoolean;
import mnm.mods.tabbychat.client.gui.component.config.GuiSettingEnum;
import mnm.mods.tabbychat.client.gui.component.config.GuiSettingNumber.GuiSettingDouble;
import mnm.mods.tabbychat.client.gui.component.config.SettingPanel;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import java.text.NumberFormat;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GuiSettingsGeneral extends SettingPanel<TabbySettings> {

    GuiSettingsGeneral() {
        setLayout(new GuiGridLayout(10, 20));
        setDisplayString(I18n.format(SETTINGS_GENERAL));
        setSecondaryColor(Color.of(255, 0, 255, 64));
    }

    @Override
    public void initGUI() {
        GeneralSettings sett = getSettings().general;

        int pos = 1;
        addComponent(new GuiLabel(new TextComponentTranslation(LOG_CHAT)), new int[] { 2, pos });
        GuiSettingBoolean chkLogChat = new GuiSettingBoolean(sett.logChat);
        chkLogChat.setCaption(new TextComponentTranslation(LOG_CHAT_DESC));
        addComponent(chkLogChat, new int[] { 1, pos });

        addComponent(new GuiLabel(new TextComponentTranslation(SPLIT_LOG)), new int[] { 7, pos });
        GuiSettingBoolean chkSplitLog = new GuiSettingBoolean(sett.splitLog);
        chkSplitLog.setCaption(new TextComponentTranslation(SPLIT_LOG_DESC));
        addComponent(chkSplitLog, new int[] { 6, pos });

        pos += 2;
        addComponent(new GuiLabel(new TextComponentTranslation(TIMESTAMP)), new int[] { 2, pos });
        addComponent(new GuiSettingBoolean(sett.timestampChat), new int[] { 1, pos });

        pos += 2;
        addComponent(new GuiLabel(new TextComponentTranslation(TIMESTAMP_STYLE)), new int[] { 3, pos });
        addComponent(new GuiSettingEnum<>(sett.timestampStyle, TimeStamps.values()), new int[] { 5, pos, 4, 1 });

        pos += 2;
        addComponent(new GuiLabel(new TextComponentTranslation(TIMESTAMP_COLOR)), new int[] { 3, pos });
        addComponent(new GuiSettingEnum<>(sett.timestampColor, getColors(), GuiSettingsGeneral::getColorName), new int[] { 5, pos, 4, 1 });

        pos += 2;
        addComponent(new GuiLabel(new TextComponentTranslation(ANTI_SPAM)), new int[] { 2, pos });
        GuiSettingBoolean chkSpam = new GuiSettingBoolean(sett.antiSpam);
        chkSpam.setCaption(new TextComponentTranslation(ANTI_SPAM_DESC));
        addComponent(chkSpam, new int[] { 1, pos });

        pos += 2;
        addComponent(new GuiLabel(new TextComponentTranslation(SPAM_PREJUDICE)), new int[] { 3, pos });
        GuiSettingDouble nud = new GuiSettingDouble(sett.antiSpamPrejudice);
        nud.getComponent().setMin(0);
        nud.getComponent().setMax(1);
        nud.getComponent().setInterval(0.05);
        nud.getComponent().setFormat(NumberFormat.getPercentInstance());
        nud.setCaption(new TextComponentTranslation(SPAM_PREJUDICE_DESC));
        addComponent(nud, new int[] { 6, pos, 2, 1 });

        pos += 2;
        addComponent(new GuiLabel(new TextComponentTranslation(UNREAD_FLASHING)), new int[] { 2, pos });
        addComponent(new GuiSettingBoolean(sett.unreadFlashing), new int[] { 1, pos });

        pos += 2;
        addComponent(new GuiLabel(new TextComponentTranslation(CHECK_UPDATES)), new int[] { 2, pos });
        addComponent(new GuiSettingBoolean(sett.checkUpdates), new int[] { 1, pos });
    }

    private static List<TextFormatting> getColors() {
        return Stream.of(TextFormatting.values())
                .filter(TextFormatting::isColor)
                .collect(Collectors.toList());
    }

    private static String getColorName(TextFormatting input) {
        return "colors." + input.getFriendlyName();
    }

    @Override
    public TabbySettings getSettings() {
        return TabbyChatClient.getInstance().getSettings();
    }

}
