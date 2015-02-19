package mnm.mods.tabbychat.util;

import mnm.mods.util.Translatable;
import net.minecraft.client.resources.I18n;

public class Translation implements Translatable {

    public static final Translatable
            TABBYCHAT = new Translation("tabbychat"),

            FORMAT_MESSAGE_VANILLA = new Translation("tabbychat.formats.messages.vanilla"),
            FORMAT_MESSAGE_ESSENTIALS = new Translation("tabbychat.formats.messages.essentials"),
            FORMAT_MESSAGE_HEROCHAT = new Translation("tabbychat.formats.messages.herochat"),
            FORMAT_MESSAGE_DISABLED = new Translation("tabbychat.formats.messages.disabled"),

            DELIMS_ANGLES = new Translation("tabbychat.delims.angles"),
            DELIMS_BRACES = new Translation("tabbychat.delims.braces"),
            DELIMS_BRACKETS = new Translation("tabbychat.delims.brackets"),
            DELIMS_PARENTHESIS = new Translation("tabbychat.delims.parenthesis"),
            DELIMS_ANGLES_PARENS = new Translation("tabbychat.delims.anglesparens"),
            DELIMS_ANGLES_BRAKETS = new Translation("tabbychat.delims.anglesbrackets"),

            SETTINGS_TITLE = new Translation("tabbychat.settings.title"),
            SETTINGS_SAVE = new Translation("tabbychat.settings.save"),
            SETTINGS_CLOSE = new Translation("tabbychat.settings.close"),

            SETTINGS_GENERAL = new Translation("tabbychat.settings.general"),
            SETTINGS_SERVER = new Translation("tabbychat.settings.server"),
            SETTINGS_FILTERS = new Translation("tabbychat.settings.filters"),
            SETTINGS_COLORS = new Translation("tabbychat.settings.colors"),

            SERVER_CHANNELS_ENABLED = new Translation("tabbychat.settings.server.channelsEnabled"),
            SERVER_PM_ENABLED = new Translation("tabbychat.settings.server.pmEnabled"),
            SERVER_CHANNEL_PATTERN = new Translation("tabbychat.settings.server.channelPattern"),
            SERVER_MESSAGE_PATTERN = new Translation("tabbychat.settings.server.messagePattern"),
            SERVER_IGNORED_CHANNELS = new Translation("tabbychat.settings.server.ignoredChannels"),
            SERVER_DEFAULT_CHANNELS = new Translation("tabbychat.settings.server.defaultChannels"),
            SERVER_FILTERS = new Translation("tabbychat.settings.server.filters"),
            SERVER_FILTERS_NEW = new Translation("tabbychat.settings.server.filters.new"),

            FILTER_TITLE = new Translation("tabbychat.filter.title"),
            FILTER_NAME = new Translation("tabbychat.filter.name"),
            FILTER_DESTINATIONS = new Translation("tabbychat.filter.destinations"),
            FILTER_HIDE = new Translation("tabbychat.filter.hideMatches"),
            FILTER_AUDIO_NOTIFY = new Translation("tabbychat.filter.audioNotify"),
            FILTER_EXPRESSION = new Translation("tabbychat.filter.expression"),

            CHANNEL_TITLE = new Translation("tabbychat.channel.title"),
            CHANNEL_LABEL = new Translation("tabbychat.channel.label"),
            CHANNEL_ALIAS = new Translation("tabbychat.channel.alias"),
            CHANNEL_PREFIX = new Translation("tabbychat.channel.prefix"),
            CHANNEL_HIDE_PREFIX = new Translation("tabbychat.channel.hidePrefix"),
            CHANNEL_NONE = new Translation("tabbychat.channel.none"),
            CHANNEL_SELECT = new Translation("tabbychat.channel.select"),

            COLOR_CHATBOX = new Translation("tabbychat.settings.colors.chatbox"),
            COLOR_CHAT_TEXT = new Translation("tabbychat.settings.colors.chattext");

    private final String translation;

    public Translation(String trans) {
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
