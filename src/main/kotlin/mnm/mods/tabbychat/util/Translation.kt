package mnm.mods.tabbychat.util

object Translation {
    private val String.tr: Translatable get() = Translatable { this }

    val TABBYCHAT = "tabbychat".tr

    val FORMAT_MESSAGE_WHISPER = "tabbychat.formats.messages.whisper".tr
    val FORMAT_MESSAGE_ARROW = "tabbychat.formats.messages.arrow".tr
    val FORMAT_MESSAGE_TO_FROM = "tabbychat.formats.messages.toFrom".tr
    val FORMAT_MESSAGE_DISABLED = "tabbychat.formats.messages.disabled".tr

    val DELIMS_ANGLES = "tabbychat.delims.angles".tr
    val DELIMS_BRACES = "tabbychat.delims.braces".tr
    val DELIMS_BRACKETS = "tabbychat.delims.brackets".tr
    val DELIMS_PARENTHESIS = "tabbychat.delims.parenthesis".tr
    val DELIMS_ANGLES_PARENS = "tabbychat.delims.anglesparens".tr
    val DELIMS_ANGLES_BRAKETS = "tabbychat.delims.anglesbrackets".tr

    val SETTINGS_TITLE = "tabbychat.settings.title".tr
    val SETTINGS_SAVE = "tabbychat.settings.save".tr
    val SETTINGS_CLOSE = "tabbychat.settings.close".tr

    // settings names
    val SETTINGS_GENERAL = "tabbychat.settings.general".tr
    val SETTINGS_SERVER = "tabbychat.settings.server".tr
    val SETTINGS_FILTERS = "tabbychat.settings.filters".tr
    val SETTINGS_COLORS = "tabbychat.settings.colors".tr
    val SETTINGS_ADVANCED = "tabbychat.settings.advanced".tr
    val SETTINGS_SPELLING = "tabbychat.settings.spelling".tr

    // general settings
    val LOG_CHAT = "tabbychat.settings.general.logChat".tr
    val LOG_CHAT_DESC = "tabbychat.settings.general.logChat.desc".tr
    val SPLIT_LOG = "tabbychat.settings.general.splitLog".tr
    val SPLIT_LOG_DESC = "tabbychat.settings.general.splitLog.desc".tr
    val TIMESTAMP = "tabbychat.settings.general.timestamp".tr
    val TIMESTAMP_STYLE = "tabbychat.settings.general.timestamp.style".tr
    val TIMESTAMP_COLOR = "tabbychat.settings.general.timestamp.color".tr
    val ANTI_SPAM = "tabbychat.settings.general.antispam".tr
    val ANTI_SPAM_DESC = "tabbychat.settings.general.antispam.desc".tr
    val SPAM_PREJUDICE = "tabbychat.settings.general.antispam.prejudice".tr
    val SPAM_PREJUDICE_DESC = "tabbychat.settings.general.antispam.prejudice.desc".tr
    val UNREAD_FLASHING = "tabbychat.settings.general.unreadFlashing".tr
    val CHECK_UPDATES = "tabbychat.settings.general.checkUpdates".tr

    // server settings
    val CHANNELS_ENABLED = "tabbychat.settings.server.channelsEnabled".tr
    val CHANNELS_ENABLED_DESC = "tabbychat.settings.server.channelsEnabled.desc".tr
    val PM_ENABLED = "tabbychat.settings.server.pmEnabled".tr
    val PM_ENABLED_DESC = "tabbychat.settings.server.pmEnabled.desc".tr
    val CHANNEL_PATTERN = "tabbychat.settings.server.channelPattern".tr
    val CHANNEL_PATTERN_DESC = "tabbychat.settings.server.channelPattern.desc".tr
    val USE_DEFAULT = "tabbychat.settings.server.useDefault".tr
    val MESSAGE_PATTERN = "tabbychat.settings.server.messagePattern".tr
    val MESSAGE_PATTERN_DESC = "tabbychat.settings.server.messagePattern.desc".tr
    val IGNORED_CHANNELS = "tabbychat.settings.server.ignoredChannels".tr
    val IGNORED_CHANNELS_DESC = "tabbychat.settings.server.ignoredChannels.desc".tr
    val DEFAULT_CHANNEL_COMMAND = "tabbychat.settings.server.channelCommand".tr
    val DEFAULT_CHANNEL_COMMAND_DESC = "tabbychat.settings.server.channelCommand.desc".tr
    val DEFAULT_CHANNEL = "tabbychat.settings.server.defaultChannel".tr
    val DEFAULT_CHANNEL_DESC = "tabbychat.settings.server.defaultChannel.desc".tr
    val FILTERS = "tabbychat.settings.server.filters".tr
    val FILTERS_NEW = "tabbychat.settings.server.filters.new".tr

    // filter settings
    val FILTER_TITLE = "tabbychat.filter.title".tr
    val FILTER_NAME = "tabbychat.filter.name".tr
    val FILTER_DESTINATIONS = "tabbychat.filter.destinations".tr
    val FILTER_DESTIONATIONS_DESC = "tabbychat.filter.destinations.desc".tr
    val FILTER_HIDE = "tabbychat.filter.hideMatches".tr
    val FILTER_AUDIO_NOTIFY = "tabbychat.filter.audioNotify".tr
    val FILTER_EXPRESSION = "tabbychat.filter.expression".tr
    val FILTER_REGEX = "tabbychat.filter.regex".tr
    val FILTER_IGNORE_CASE = "tabbychat.filter.ignoreCase".tr
    val FILTER_RAW_INPUT = "tabbychat.filter.rawInput".tr

    // channel settings
    val CHANNEL_TITLE = "tabbychat.channel.title".tr
    val CHANNEL_LABEL = "tabbychat.channel.label".tr
    val CHANNEL_ALIAS = "tabbychat.channel.alias".tr
    val CHANNEL_PREFIX = "tabbychat.channel.prefix".tr
    val CHANNEL_HIDE_PREFIX = "tabbychat.channel.hidePrefix".tr
    val CHANNEL_COMMAND = "tabbychat.channel.command".tr
    val CHANNEL_NONE = "tabbychat.channel.none".tr
    val CHANNEL_SELECT = "tabbychat.channel.select".tr
    val CHANNEL_FORGET = "tabbychat.channel.forget".tr

    // color stuff
    val COLOR_CHATBOX = "tabbychat.settings.colors.chatbox".tr
    val COLOR_CHAT_BORDER = "tabbychat.settings.colors.chatborder".tr
    val COLOR_CHAT_TEXT = "tabbychat.settings.colors.chattext".tr

    // advanced
    val ADVANCED_FADE_TIME = "tabbychat.settings.advanced.fadetime".tr
    val ADVANCED_CHAT_VISIBILITY = "tabbychat.settings.advanced.chatvisibility".tr
    val ADVANCED_HIDE_DELIMS = "tabbychat.settings.advanced.hidedelims".tr
    val ADVANCED_SPELLCHECK = "tabbychat.settings.advanced.spellcheck".tr
    val EXPERIMENTAL = "tabbychat.settings.experimental".tr

    // spellcheck
    val SPELLCHECK_NOPE = "tabbychat.spellcheck.nope".tr
    val SPELLCHECK_DOWNLOAD_LISTS = "tabbychat.spellcheck.download_lists".tr

    // messages
    val WARN_COMPLETIONS = "tabbychat.warn.completions".tr
}
