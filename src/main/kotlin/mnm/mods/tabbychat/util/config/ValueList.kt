package mnm.mods.tabbychat.util.config;

import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

public class ValueList<T> extends Value<List<T>> implements Iterable<T> {

    ValueList() {
        set(Lists.newArrayList());
    }

    public void add(T value) {
        this.get().add(value);
    }

    public void add(int index, T value) {
        this.get().add(index, value);
    }

    public void remove(T value) {
        this.get().remove(value);
    }

    public void remove(int index) {
        this.get().remove(index);
    }

    public void clear() {
        this.get().clear();
    }

    public T get(int index) {
        return this.get().get(index);
    }

    @Override
    public void set(List<T> val) {
        super.set(Lists.newArrayList(val));
    }

    @Override
    public Iterator<T> iterator() {
        return get().iterator();
    }
}
