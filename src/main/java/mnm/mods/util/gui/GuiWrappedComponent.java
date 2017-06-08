package mnm.mods.util.gui;

import com.google.common.eventbus.EventBus;
import mnm.mods.util.Color;
import mnm.mods.util.ILocation;
import net.minecraft.util.text.ITextComponent;

import java.awt.Dimension;
import java.util.Optional;
import javax.annotation.Nonnull;

public class GuiWrappedComponent<T extends GuiComponent> extends GuiComponent {

    private final T wrapper;

    public GuiWrappedComponent(@Nonnull T wrap) {
        this.wrapper = wrap;
        this.getBus().register(this);
    }

    public T getComponent() {
        return wrapper;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        wrapper.drawComponent(mouseX, mouseY);
    }

    @Override
    public void updateComponent() {
        wrapper.updateComponent();
    }

    @Override
    public void handleMouseInput() {
        wrapper.handleMouseInput();
    }

    @Override
    public void handleKeyboardInput() {
        wrapper.handleKeyboardInput();
    }

    @Override
    public ILocation getLocation() {
        return wrapper.getLocation();
    }

    @Override
    public void setLocation(ILocation location) {
        wrapper.setLocation(location);
    }

    @Override
    public EventBus getBus() {
        return wrapper.getBus();
    }

    @Override
    public Optional<GuiPanel> getParent() {
        return wrapper.getParent();
    }

    @Override
    void setParent(GuiPanel guiPanel) {
        wrapper.setParent(guiPanel);
    }

    @Override
    public void setMinimumSize(Dimension size) {
        wrapper.setMinimumSize(size);
    }

    @Override
    public Dimension getMinimumSize() {
        return wrapper.getMinimumSize();
    }

    @Override
    public void setScale(float scale) {
        wrapper.setScale(scale);
    }

    @Override
    public float getScale() {
        return wrapper.getScale();
    }

    @Override
    public Optional<Color> getPrimaryColor() {
        return wrapper.getPrimaryColor();
    }

    @Override
    public void setPrimaryColor(Color color) {
        wrapper.setPrimaryColor(color);
    }

    @Override
    public Optional<Color> getSecondaryColor() {
        return wrapper.getSecondaryColor();
    }

    @Override
    public void setSecondaryColor(Color color) {
        wrapper.setSecondaryColor(color);
    }

    @Override
    public boolean isEnabled() {
        return wrapper.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        wrapper.setEnabled(enabled);
    }

    @Override
    public boolean isVisible() {
        return wrapper.isVisible();
    }

    @Override
    public void setVisible(boolean visible) {
        wrapper.setVisible(visible);
    }

    @Override
    public boolean isHovered() {
        return wrapper.isHovered();
    }

    @Override
    public void setCaption(ITextComponent text) {
        wrapper.setCaption(text);
    }

    @Override
    public Optional<ITextComponent> getCaptionText() {
        return wrapper.getCaptionText();
    }

    @Override
    public boolean isFocusable() {
        return wrapper.isFocusable();
    }

    @Override
    public boolean isFocused() {
        return wrapper.isFocused();
    }

    @Override
    public void setFocused(boolean focused) {
        wrapper.setFocused(focused);
    }

}
