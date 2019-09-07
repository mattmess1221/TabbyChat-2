package mnm.mods.tabbychat.util.config;

import com.google.common.collect.Maps;

import java.util.Map;

public class ValueMap<T> extends Value<Map<String, T>> {

    ValueMap() {
        this.set(Maps.newHashMap());
    }

    public void set(String key, T value) {
        this.get().put(key, value);
    }

    public T get(String key) {
        return this.get().get(key);
    }

    @Override
    public void set(Map<String, T> val) {
        super.set(Maps.newHashMap(val));
    }
}
