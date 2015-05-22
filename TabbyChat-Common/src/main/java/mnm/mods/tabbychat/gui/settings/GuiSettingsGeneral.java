package mnm.mods.tabbychat.gui.settings;

import java.text.NumberFormat;
import java.util.Collection;
import java.util.List;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.settings.GeneralSettings;
import mnm.mods.tabbychat.util.TimeStamps;
import mnm.mods.tabbychat.util.Translation;
import mnm.mods.util.Color;
import mnm.mods.util.gui.GuiGridLayout;
import mnm.mods.util.gui.GuiLabel;
import mnm.mods.util.gui.GuiSettingBoolean;
import mnm.mods.util.gui.GuiSettingEnum;
import mnm.mods.util.gui.GuiSettingNumber.GuiSettingDouble;
import mnm.mods.util.gui.SettingPanel;
import net.minecraft.util.EnumChatFormatting;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class GuiSettingsGeneral extends SettingPanel<GeneralSettings> {

    public GuiSettingsGeneral() {
        setLayout(new GuiGridLayout(10, 15));
        setDisplayString(Translation.SETTINGS_GENERAL.translate());
        setBackColor(Color.getColor(255, 0, 255, 64));
    }

    @Override
    public void initGUI() {
        GeneralSettings sett = getSettings();
        addComponent(new GuiLabel(Translation.LOG_CHAT.toString()), new int[] { 2, 1 });
        GuiSettingBoolean chkLogChat = new GuiSettingBoolean(sett.logChat);
        chkLogChat.setCaption(Translation.LOG_CHAT_DESC.toString());
        addComponent(chkLogChat, new int[] { 1, 1 });

        addComponent(new GuiLabel(Translation.SPLIT_LOG.toString()), new int[] { 7, 1 });
        GuiSettingBoolean chkSplitLog = new GuiSettingBoolean(sett.splitLog);
        chkSplitLog.setCaption(Translation.SPLIT_LOG_DESC.toString());
        addComponent(chkSplitLog, new int[] { 6, 1 });

        addComponent(new GuiLabel(Translation.TIMESTAMP.toString()), new int[] { 2, 3 });
        addComponent(new GuiSettingBoolean(sett.timestampChat), new int[] { 1, 3 });

        addComponent(new GuiLabel(Translation.TIMESTAMP_STYLE.toString()), new int[] { 3, 5 });
        addComponent(new GuiSettingEnum<TimeStamps>(sett.timestampStyle, TimeStamps.values()),
                new int[] { 5, 5, 4, 1 });

        addComponent(new GuiLabel(Translation.TIMESTAMP_COLOR.toString()), new int[] { 3, 7 });
        addComponent(new GuiSettingEnum<EnumChatFormatting>(sett.timestampColor, getColors(),
                getColorNames()),
                new int[] { 5, 7, 4, 1 });

        addComponent(new GuiLabel(Translation.ANTI_SPAM.toString()), new int[] { 2, 9 });
        GuiSettingBoolean chkSpam = new GuiSettingBoolean(sett.antiSpam);
        chkSpam.setCaption(Translation.ANTI_SPAM_DESC.toString());
        addComponent(chkSpam, new int[] { 1, 9 });

        addComponent(new GuiLabel(Translation.SPAM_PREJUDICE.toString()), new int[] { 3, 11 });
        GuiSettingDouble nud = new GuiSettingDouble(sett.antiSpamPrejudice);
        nud.getNumUpDown().setMin(0);
        nud.getNumUpDown().setMax(1);
        nud.getNumUpDown().setInterval(0.05);
        nud.getNumUpDown().setFormat(NumberFormat.getPercentInstance());
        nud.setCaption(Translation.SPAM_PREJUDICE_DESC.toString());
        addComponent(nud, new int[] { 6, 11, 2, 1 });

        addComponent(new GuiLabel(Translation.UNREAD_FLASHING.toString()), new int[] { 2, 13 });
        addComponent(new GuiSettingBoolean(sett.unreadFlashing), new int[] { 1, 13 });

        addComponent(new GuiLabel(Translation.CHECK_UPDATES.toString()), new int[] { 2, 15 });
        addComponent(new GuiSettingBoolean(sett.checkUpdates), new int[] { 1, 15 });
    }

    private EnumChatFormatting[] getColors() {
        Collection<String> colors = EnumChatFormatting.getValidValues(true, false);
        List<EnumChatFormatting> list = Lists.newArrayList();
        for (String color : colors) {
            list.add(EnumChatFormatting.getValueByName(color));
        }
        return Iterables.toArray(list, EnumChatFormatting.class);
    }

    private String[] getColorNames() {
        Collection<String> colors = EnumChatFormatting.getValidValues(true, false);
        List<String> list = Lists.newArrayList();
        for (String color : colors) {
            list.add("colors." + color);
        }

        return Iterables.toArray(list, String.class);
    }

    @Override
    public GeneralSettings getSettings() {
        return TabbyChat.getInstance().generalSettings;
    }

}
