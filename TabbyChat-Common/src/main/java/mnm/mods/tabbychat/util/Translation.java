package mnm.mods.tabbychat.util;

import mnm.mods.util.Translatable;
import net.minecraft.client.resources.I18n;

public enum Translation implements Translatable {

    TABBYCHAT("tabbychat"),

    FORMAT_MESSAGE_VANILLA("tabbychat.formats.messages.vanilla"),
    FORMAT_MESSAGE_ESSENTIALS("tabbychat.formats.messages.essentials"),
    FORMAT_MESSAGE_HEROCHAT("tabbychat.formats.messages.herochat"),
    FORMAT_MESSAGE_DISABLED("tabbychat.formats.messages.disabled"),

    DELIMS_ANGLES("tabbychat.delims.angles"),
    DELIMS_BRACES("tabbychat.delims.braces"),
    DELIMS_BRACKETS("tabbychat.delims.brackets"),
    DELIMS_PARENTHESIS("tabbychat.delims.parenthesis"),
    DELIMS_ANGLES_PARENS("tabbychat.delims.anglesparens"),
    DELIMS_ANGLES_BRAKETS("tabbychat.delims.anglesbrackets"),

    SETTINGS_TITLE("tabbychat.settings.title"),
    SETTINGS_SAVE("tabbychat.settings.save"),
    SETTINGS_CLOSE("tabbychat.settings.close"),

    // settings names
    SETTINGS_GENERAL("tabbychat.settings.general"),
    SETTINGS_SERVER("tabbychat.settings.server"),
    SETTINGS_FILTERS("tabbychat.settings.filters"),
    SETTINGS_COLORS("tabbychat.settings.colors"),
    SETTINGS_ADVANCED("tabbychat.settings.advanced"),

    // general settings
    LOG_CHAT("tabbychat.settings.general.logChat"),
    LOG_CHAT_DESC("tabbychat.settings.general.logChat.desc"),
    SPLIT_LOG("tabbychat.settings.general.splitLog"),
    SPLIT_LOG_DESC("tabbychat.settings.general.splitLog.desc"),
    TIMESTAMP("tabbychat.settings.general.timestamp"),
    TIMESTAMP_STYLE("tabbychat.settings.general.timestamp.style"),
    TIMESTAMP_COLOR("tabbychat.settings.general.timestamp.color"),
    ANTI_SPAM("tabbychat.settings.general.antispam"),
    ANTI_SPAM_DESC("tabbychat.settings.general.antispam.desc"),
    SPAM_PREJUDICE("tabbychat.settings.general.antispam.prejudice"),
    SPAM_PREJUDICE_DESC("tabbychat.settings.general.antispam.prejudice.desc"),
    UNREAD_FLASHING("tabbychat.settings.general.unreadFlashing"),
    CHECK_UPDATES("tabbychat.settings.general.checkUpdates"),

    // server settings
    CHANNELS_ENABLED("tabbychat.settings.server.channelsEnabled"),
    CHANNELS_ENABLED_DESC("tabbychat.settings.server.channelsEnabled.desc"),
    PM_ENABLED("tabbychat.settings.server.pmEnabled"),
    PM_ENABLED_DESC("tabbychat.settings.server.pmEnabled.desc"),
    CHANNEL_PATTERN("tabbychat.settings.server.channelPattern"),
    CHANNEL_PATTERN_DESC("tabbychat.settings.server.channelPattern.desc"),
    USE_DEFAULT("tabbychat.settings.server.useDefault"),
    MESSAGE_PATTERN("tabbychat.settings.server.messagePattern"),
    MESSAGE_PATTERN_DESC("tabbychat.settings.server.messagePattern.desc"),
    IGNORED_CHANNELS("tabbychat.settings.server.ignoredChannels"),
    IGNORED_CHANNELS_DESC("tabbychat.settings.server.ignoredChannels.desc"),
    DEFAULT_CHANNELS("tabbychat.settings.server.defaultChannels"),
    DEFAULT_CHANNELS_DESC("tabbychat.settings.server.defaultChannels.desc"),
    FILTERS("tabbychat.settings.server.filters"),
    FILTERS_NEW("tabbychat.settings.server.filters.new"),

    // filter settings
    FILTER_TITLE("tabbychat.filter.title"),
    FILTER_NAME("tabbychat.filter.name"),
    FILTER_DESTINATIONS("tabbychat.filter.destinations"),
    FILTER_HIDE("tabbychat.filter.hideMatches"),
    FILTER_AUDIO_NOTIFY("tabbychat.filter.audioNotify"),
    FILTER_EXPRESSION("tabbychat.filter.expression"),

    // channel settings
    CHANNEL_TITLE("tabbychat.channel.title"),
    CHANNEL_LABEL("tabbychat.channel.label"),
    CHANNEL_ALIAS("tabbychat.channel.alias"),
    CHANNEL_PREFIX("tabbychat.channel.prefix"),
    CHANNEL_HIDE_PREFIX("tabbychat.channel.hidePrefix"),
    CHANNEL_NONE("tabbychat.channel.none"),
    CHANNEL_SELECT("tabbychat.channel.select"),
    CHANNEL_FORGET("tabbychat.channel.forget"),

    // color stuff
    COLOR_CHATBOX("tabbychat.settings.colors.chatbox"),
    COLOR_CHAT_TEXT("tabbychat.settings.colors.chattext");

    private final String translation;

    private Translation(String trans) {
        this.translation = trans;
    }

    @Override
    public String translate(Object... params) {
        return I18n.format(translation, params);
    }

    @Override
    public String getUnlocalized() {
        return translation;
    }

    @Override
    public String toString() {
        return translate();
    }

}
