package mnm.mods.tabbychat.gui.settings;

import static mnm.mods.tabbychat.util.Translation.*;

import java.text.NumberFormat;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.settings.GeneralSettings;
import mnm.mods.tabbychat.settings.TabbySettings;
import mnm.mods.tabbychat.util.TimeStamps;
import mnm.mods.util.Color;
import mnm.mods.util.gui.GuiGridLayout;
import mnm.mods.util.gui.GuiLabel;
import mnm.mods.util.gui.config.GuiSettingBoolean;
import mnm.mods.util.gui.config.GuiSettingEnum;
import mnm.mods.util.gui.config.GuiSettingNumber.GuiSettingDouble;
import mnm.mods.util.gui.config.SettingPanel;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;

public class GuiSettingsGeneral extends SettingPanel<TabbySettings> {

    public GuiSettingsGeneral() {
        setLayout(new GuiGridLayout(10, 20));
        setDisplayString(I18n.format(SETTINGS_GENERAL));
        setBackColor(Color.getColor(255, 0, 255, 64));
    }

    @Override
    public void initGUI() {
        GeneralSettings sett = getSettings().general;

        int pos = 1;
        addComponent(new GuiLabel(new ChatComponentTranslation(LOG_CHAT)), new int[] { 2, pos });
        GuiSettingBoolean chkLogChat = new GuiSettingBoolean(sett.logChat);
        chkLogChat.setCaption(I18n.format(LOG_CHAT_DESC));
        addComponent(chkLogChat, new int[] { 1, pos });

        addComponent(new GuiLabel(new ChatComponentTranslation(SPLIT_LOG)), new int[] { 7, pos });
        GuiSettingBoolean chkSplitLog = new GuiSettingBoolean(sett.splitLog);
        chkSplitLog.setCaption(I18n.format(SPLIT_LOG_DESC));
        addComponent(chkSplitLog, new int[] { 6, pos });

        pos += 2;
        addComponent(new GuiLabel(new ChatComponentTranslation(TIMESTAMP)), new int[] { 2, pos });
        addComponent(new GuiSettingBoolean(sett.timestampChat), new int[] { 1, pos });

        pos += 2;
        addComponent(new GuiLabel(new ChatComponentTranslation(TIMESTAMP_STYLE)), new int[] { 3, pos });
        addComponent(new GuiSettingEnum<TimeStamps>(sett.timestampStyle, TimeStamps.values()), new int[] { 5, pos, 4, 1 });

        pos += 2;
        addComponent(new GuiLabel(new ChatComponentTranslation(TIMESTAMP_COLOR)), new int[] { 3, pos });
        addComponent(new GuiSettingEnum<EnumChatFormatting>(sett.timestampColor, getColors(), getColorNames()), new int[] { 5, pos, 4, 1 });

        pos += 2;
        addComponent(new GuiLabel(new ChatComponentTranslation(ANTI_SPAM)), new int[] { 2, pos });
        GuiSettingBoolean chkSpam = new GuiSettingBoolean(sett.antiSpam);
        chkSpam.setCaption(I18n.format(ANTI_SPAM_DESC));
        addComponent(chkSpam, new int[] { 1, pos });

        pos += 2;
        addComponent(new GuiLabel(new ChatComponentTranslation(SPAM_PREJUDICE)), new int[] { 3, pos });
        GuiSettingDouble nud = new GuiSettingDouble(sett.antiSpamPrejudice);
        nud.getInput().setMin(0);
        nud.getInput().setMax(1);
        nud.getInput().setInterval(0.05);
        nud.getInput().setFormat(NumberFormat.getPercentInstance());
        nud.setCaption(I18n.format(SPAM_PREJUDICE_DESC));
        addComponent(nud, new int[] { 6, pos, 2, 1 });

        pos += 2;
        addComponent(new GuiLabel(new ChatComponentTranslation(UNREAD_FLASHING)), new int[] { 2, pos });
        addComponent(new GuiSettingBoolean(sett.unreadFlashing), new int[] { 1, pos });

        pos += 2;
        addComponent(new GuiLabel(new ChatComponentTranslation(CHECK_UPDATES)), new int[] { 2, pos });
        addComponent(new GuiSettingBoolean(sett.checkUpdates), new int[] { 1, pos });
    }

    private static EnumChatFormatting[] getColors() {
        return Sets.filter(Sets.newHashSet(EnumChatFormatting.values()), new Predicate<EnumChatFormatting>() {
            @Override
            public boolean apply(EnumChatFormatting input) {
                return input.isColor();
            }
        }).toArray(new EnumChatFormatting[0]);
    }

    private static String[] getColorNames() {
        return Lists.transform(Lists.newArrayList(getColors()), new Function<EnumChatFormatting, String>() {
            @Override
            public String apply(EnumChatFormatting input) {
                return "colors." + input.getFriendlyName();
            }
        }).toArray(new String[0]);
    }

    @Override
    public TabbySettings getSettings() {
        return TabbyChat.getInstance().settings;
    }

}
