package mnm.mods.tabbychat.client.gui.component;

import mnm.mods.tabbychat.util.Color;
import mnm.mods.tabbychat.util.Dim;
import mnm.mods.tabbychat.util.ILocation;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.util.text.ITextComponent;

import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GuiWrappedComponent<T extends GuiComponent> extends GuiComponent implements IGuiEventListenerDelegate {

    private final T wrapper;

    public GuiWrappedComponent(@Nonnull T wrap) {
        this.wrapper = wrap;
    }

    public T getComponent() {
        return wrapper;
    }

    @Nullable
    @Override
    public IGuiEventListener delegate() {
        return wrapper;
    }

    @Override
    public void render(int mouseX, int mouseY, float parTicks) {
        wrapper.render(mouseX, mouseY, parTicks);
    }

    @Override
    public void tick() {
        wrapper.tick();
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
    public Optional<GuiPanel> getParent() {
        return wrapper.getParent();
    }

    @Override
    void setParent(GuiPanel guiPanel) {
        wrapper.setParent(guiPanel);
    }

    @Override
    public void setMinimumSize(Dim size) {
        wrapper.setMinimumSize(size);
    }

    @Override
    public Dim getMinimumSize() {
        return wrapper.getMinimumSize();
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
    public void setCaption(ITextComponent text) {
        wrapper.setCaption(text);
    }

    @Override
    public Optional<ITextComponent> getCaptionText() {
        return wrapper.getCaptionText();
    }

}
