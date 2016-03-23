package mnm.mods.tabbychat.settings;

import mnm.mods.util.Color;
import mnm.mods.util.config.Value;
import mnm.mods.util.config.ValueObject;

public class ColorSettings extends ValueObject {

    public Value<Color> chatBoxColor = value(Color.of(0, 0, 0, 127));
    public Value<Color> chatTextColor = value(Color.of(255, 255, 255, 255));
    public Value<Color> chatBorderColor = value(Color.of(0, 0, 0, 191));
}
