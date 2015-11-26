package mnm.mods.tabbychat.settings;

import com.google.gson.annotations.Expose;

import mnm.mods.util.Color;
import mnm.mods.util.config.Value;
import mnm.mods.util.config.ValueObject;

public class ColorSettings extends ValueObject {

    @Expose
    public Value<Color> chatBoxColor = value(new Color(0, 0, 0, 127));
    @Expose
    public Value<Color> chatTextColor = value(new Color(255, 255, 255, 255));
}
