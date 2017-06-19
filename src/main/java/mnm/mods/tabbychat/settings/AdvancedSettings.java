package mnm.mods.tabbychat.settings;

import mnm.mods.tabbychat.util.ChatVisibility;
import mnm.mods.util.config.Value;
import mnm.mods.util.config.ValueObject;

public class AdvancedSettings extends ValueObject {

    public Value<Integer> chatX = value(5);
    public Value<Integer> chatY = value(17);
    public Value<Integer> chatW = value(300);
    public Value<Integer> chatH = value(160);
    public Value<Float> unfocHeight = value(0.5F);
    public Value<Integer> fadeTime = value(200);
    public Value<Integer> historyLen = value(100);
    public Value<Boolean> hideTag = value(false);
    public Value<Boolean> keepChatOpen = value(false);
    public Value<Boolean> spelling = value(true);
    public Value<ChatVisibility> visibility = value(ChatVisibility.NORMAL);
}
