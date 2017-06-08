package mnm.mods.util.config;

public abstract class ValueObject {

    public static <T> Value<T> value(T t) {
        return new Value<>(t);
    }

    public static <T> ValueList<T> list() {
        return new ValueList<>();
    }

    public static <T> ValueMap<T> map() {
        return new ValueMap<>();
    }

}
