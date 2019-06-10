package mnm.mods.tabbychat.client.settings;

import mnm.mods.tabbychat.util.LocalVisibility;
import mnm.mods.tabbychat.util.ILocation;
import mnm.mods.tabbychat.util.Location;
import mnm.mods.tabbychat.util.config.Value;
import mnm.mods.tabbychat.util.config.ValueObject;

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
    public Value<LocalVisibility> visibility = value(LocalVisibility.NORMAL);

    public ILocation getChatboxLocation() {
        return new Location(
                chatX.get(), chatY.get(),
                chatW.get(), chatH.get()
        );
    }
}
