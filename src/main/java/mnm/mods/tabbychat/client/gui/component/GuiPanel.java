package mnm.mods.tabbychat.client.gui.component;

import com.google.common.collect.Lists;
import mnm.mods.tabbychat.client.gui.component.layout.ILayout;
import mnm.mods.tabbychat.util.Dim;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.INestedGuiEventHandler;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A component that can contain multiple components.
 *
 * @author Matthew
 */
public class GuiPanel extends GuiComponent implements INestedGuiEventHandler {

    private List<GuiComponent> components = Lists.newArrayList();
    @Deprecated
    private GuiComponent overlay;
    private ILayout layout;
    private IGuiEventListener focused;

    private boolean dragging;

    public GuiPanel(ILayout layout) {
        setLayout(layout);
    }

    public GuiPanel() {
    }

    @Override
    public void render(int mouseX, int mouseY, float parTicks) {
        getLayout().ifPresent(layout -> layout.layoutComponents(this));
        this.components.stream()
                .filter(GuiComponent::isVisible)
                .forEach(gc -> gc.render(mouseX, mouseY, parTicks));

        super.render(mouseX, mouseY, parTicks);
    }

    @Override
    public void renderCaption(int mouseX, int mouseY) {
        super.renderCaption(mouseX, mouseY);
        this.children().stream()
                .filter(GuiComponent::isVisible)
                .forEach(gc -> gc.renderCaption(mouseX, mouseY));
    }

    @Override
    public void tick() {
        children().forEach(GuiComponent::tick);
    }

    /**
     * Adds a component to this panel.
     *
     * @param guiComponent The component
     */
    public void add(GuiComponent guiComponent) {
        add(guiComponent, (Object) null);
    }

    /**
     * Adds a component to this panel with constraints.
     *
     * @param guiComponent The component
     * @param constraints  The constraints
     */
    public void add(GuiComponent guiComponent, Object constraints) {
        if (guiComponent != null) {
            guiComponent.setParent(this);
            components.add(guiComponent);
            getLayout().ifPresent(layout -> layout.addComponent(guiComponent, constraints));
        }
    }

    /**
     * Removes all components from this panel.
     */
    public void clear() {
        components.forEach(comp -> {
            comp.setParent(null);
            getLayout().ifPresent(layout -> layout.removeComponent(comp));
        });
        components.clear();
    }

    /**
     * Removes a component from this panel.
     *
     * @param guiComp The component to remove
     */
    public void remove(GuiComponent guiComp) {
        components.remove(guiComp);
        getLayout().ifPresent(layout -> layout.removeComponent(guiComp));
    }

    @Nonnull
    public List<GuiComponent> children() {
        return components;
    }

    @Nullable
    @Override
    public IGuiEventListener getFocused() {
        return focused;
    }


    public void setFocused(IGuiEventListener focused) {
        this.focused = focused;
    }

    /**
     * Sets the layout for this panel.
     *
     * @param lmg The layout manager
     */
    public void setLayout(ILayout lmg) {
        this.layout = lmg;
    }

    /**
     * Gets the layout for this panel.
     *
     * @return The layout
     */
    public Optional<ILayout> getLayout() {
        return Optional.ofNullable(layout);
    }

    /**
     * Sets the overlay for this component. An overlay temporarily replaces the
     * contents of the panel with itself. The contents are placed back when the
     * overlay is set to null.
     *
     * @param gui The component to overlay
     */
    @Deprecated
    public void setOverlay(@Nullable GuiComponent gui) {
        if (gui != null) {
            gui.setParent(this);
            gui.setLocation(gui.getLocation().copy()
                    .setWidth(getLocation().getWidth())
                    .setHeight(getLocation().getHeight()));
        }
        this.overlay = gui;
    }

    @Override
    public boolean isDragging() {
        return this.dragging;
    }

    @Override
    public final void setDragging(boolean p_195072_1_) {
        this.dragging = p_195072_1_;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return INestedGuiEventHandler.super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double x, double y, int b) {
        return INestedGuiEventHandler.super.mouseReleased(x, y, b);
    }

    @Override
    public boolean mouseDragged(double x, double y, int b, double dx, double dy) {
        return INestedGuiEventHandler.super.mouseDragged(x, y, b, dx, dy);
    }

    @Override
    public void onClosed() {
        this.children().forEach(GuiComponent::onClosed);
    }

    @Nonnull
    @Override
    public Dim getMinimumSize() {

        return getLayout().map(ILayout::getLayoutSize).orElseGet(() -> {
            int width = 0;
            int height = 0;
            for (GuiComponent gc : components) {
                width = Math.max(width, gc.getLocation().getWidth());
                height = Math.max(height, gc.getLocation().getHeight());
            }
            return new Dim(width, height);
        });
    }
}
