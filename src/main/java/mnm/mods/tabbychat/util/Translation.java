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

    SETTINGS_GENERAL("tabbychat.settings.general"),
    SETTINGS_SERVER("tabbychat.settings.server"),
    SETTINGS_FILTERS("tabbychat.settings.filters"),
    SETTINGS_COLORS("tabbychat.settings.colors"),

    LOG_CHAT("tabbychat.settings.general.logChat"),
    SPLIT_LOG("tabbychat.settings.general.splitLog"),
    TIMESTAMP("tabbychat.settings.general.timestamp"),
    TIMESTAMP_STYLE("tabbychat.settings.general.timestamp.style"),
    TIMESTAMP_COLOR("tabbychat.settings.general.timestamp.color"),
    ANTI_SPAM("tabbychat.settings.general.antispam"),
    UNREAD_FLASHING("tabbychat.settings.general.unreadFlashing"),
    CHECK_UPDATES("tabbychat.settings.general.checkUpdates"),

    CHANNELS_ENABLED("tabbychat.settings.server.channelsEnabled"),
    PM_ENABLED("tabbychat.settings.server.pmEnabled"),
    CHANNEL_PATTERN("tabbychat.settings.server.channelPattern"),
    MESSAGE_PATTERN("tabbychat.settings.server.messagePattern"),
    IGNORED_CHANNELS("tabbychat.settings.server.ignoredChannels"),
    DEFAULT_CHANNELS("tabbychat.settings.server.defaultChannels"),
    FILTERS("tabbychat.settings.server.filters"),
    FILTERS_NEW("tabbychat.settings.server.filters.new"),

    FILTER_TITLE("tabbychat.filter.title"),
    FILTER_NAME("tabbychat.filter.name"),
    FILTER_DESTINATIONS("tabbychat.filter.destinations"),
    FILTER_HIDE("tabbychat.filter.hideMatches"),
    FILTER_AUDIO_NOTIFY("tabbychat.filter.audioNotify"),
    FILTER_EXPRESSION("tabbychat.filter.expression"),

    CHANNEL_TITLE("tabbychat.channel.title"),
    CHANNEL_LABEL("tabbychat.channel.label"),
    CHANNEL_ALIAS("tabbychat.channel.alias"),
    CHANNEL_PREFIX("tabbychat.channel.prefix"),
    CHANNEL_HIDE_PREFIX("tabbychat.channel.hidePrefix"),
    CHANNEL_NONE("tabbychat.channel.none"),
    CHANNEL_SELECT("tabbychat.channel.select"),

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
