package mnm.mods.util.gui.config;

import mnm.mods.util.config.Value;
import mnm.mods.util.gui.GuiComponent;
import mnm.mods.util.gui.GuiWrappedComponent;
import mnm.mods.util.gui.IGuiInput;

import javax.annotation.Nonnull;

/**
 * A base setting gui wrapper.
 *
 * @author Matthew
 * @param <T> The setting type
 */
public abstract class GuiSetting<T> extends GuiComponent implements IGuiInput<T> {

    private final Value<T> setting;

    GuiSetting(Value<T> setting) {
        this.setting = setting;
        this.setValue(setting.get());
    }

    /**
     * Gets the setting object.
     *
     * @return The setting object
     */
    public Value<T> getSetting() {
        return this.setting;
    }

    @Override
    public void tick() {
        getSetting().set(getValue());
    }

    private abstract static class Wrapped<T, W extends GuiComponent> extends GuiWrappedComponent<W> implements IGuiInput<T> {

        private final Value<T> setting;

        Wrapped(Value<T> setting, @Nonnull W wrap) {
            super(wrap);
            this.setting = setting;
        }

        public Value<T> getSetting() {
            return this.setting;
        }

        @Override
        public void tick() {
            super.tick();
            getSetting().set(getValue());
        }

    }

    public static class GuiSettingWrapped<T, Wrapper extends GuiComponent & IGuiInput<T>>
            extends Wrapped<T, Wrapper> {

        GuiSettingWrapped(Value<T> setting, @Nonnull Wrapper input) {
            super(setting, input);
            input.setValue(setting.get());
        }

        @Override
        public T getValue() {
            return getComponent().getValue();
        }

        @Override
        public void setValue(T value) {
            getComponent().setValue(value);
        }

    }
}
