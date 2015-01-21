package mnm.mods.tabbychat.util;

import mnm.mods.util.Translatable;
import net.minecraft.client.resources.I18n;

public class Translation implements Translatable {

    public static final Translatable
            TABBYCHAT = new Translation("tabbychat"),

            FORMAT_MESSAGE_VANILLA = new Translation("tabbychat.formats.messages.vanilla"),
            FORMAT_MESSAGE_ESSENTIALS = new Translation("tabbychat.formats.messages.essentials"),
            FORMAT_MESSAGE_HEROCHAT = new Translation("tabbychat.formats.messages.herochat"),
            FORMAT_MESSAGE_CUSTOM = new Translation("tabbychat.formats.messages.custom"),

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
            SETTINGS_COLORS = new Translation("tabbychat.settings.colors");

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

}
