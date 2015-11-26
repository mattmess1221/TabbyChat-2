package mnm.mods.tabbychat.settings;

import com.google.gson.annotations.Expose;

import mnm.mods.util.config.Value;
import mnm.mods.util.config.ValueObject;

public class AdvancedSettings extends ValueObject {

    @Expose
    public Value<Integer> chatX = value(5);
    @Expose
    public Value<Integer> chatY = value(17);
    @Expose
    public Value<Integer> chatW = value(300);
    @Expose
    public Value<Integer> chatH = value(160);
    @Expose
    public Value<Float> unfocHeight = value(0.5F);
    @Expose
    public Value<Integer> fadeTime = value(200);
    @Expose
    public Value<Integer> historyLen = value(100);
    @Expose
    public Value<Integer> msgDelay = value(500);
    @Expose
    public Value<Boolean> hideTag = value(false);

}
