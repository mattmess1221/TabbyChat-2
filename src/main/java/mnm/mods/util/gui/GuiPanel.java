package mnm.mods.util.gui;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import mnm.mods.util.Dim;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IGuiEventListenerDeferred;
import net.minecraft.client.renderer.GlStateManager;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A component that can contain multiple components.
 *
 * @author Matthew
 */
public class GuiPanel extends GuiComponent implements IGuiEventListenerDeferred {

    private List<GuiComponent> components = Lists.newArrayList();
    private GuiComponent overlay;
    private ILayout layout;
    private GuiComponent focused;

    private boolean dragging;

    public GuiPanel(ILayout layout) {
        setLayout(layout);
    }

    public GuiPanel() {
    }

    @Override
    public void render(int mouseX, int mouseY, float parTicks) {
        if (this.getOverlay().isPresent()) {
            getOverlay().get().render(mouseX, mouseY, parTicks);
            return;
        }
        getLayout().ifPresent(layout -> layout.layoutComponents(this));
        this.components.stream()
                .filter(GuiComponent::isVisible)
                .forEach(gc -> gc.render(mouseX, mouseY, parTicks));

        super.render(mouseX, mouseY, parTicks);
    }

    @Override
    public void drawCaption(int mouseX, int mouseY) {
        super.drawCaption(mouseX, mouseY);
        if (this.getOverlay().isPresent()) {
            getOverlay().get().drawCaption(mouseX, mouseY);
            return;
        }
        this.components.stream()
                .filter(GuiComponent::isVisible)
                .forEach(gc -> gc.drawCaption(mouseX, mouseY));
    }

    @Override
    public void tick() {
        getOverlayOrChildren().forEach(GuiComponent::tick);
    }

    /**
     * Gets the number of components in this panel.
     *
     * @return The count
     */
    public int getComponentCount() {
        return this.components.size();
    }

    /**
     * Adds a component to this panel.
     *
     * @param guiComponent The component
     */
    public void addComponent(GuiComponent guiComponent) {
        addComponent(guiComponent, (Object) null);
    }

    /**
     * Adds a component to this panel with constraints.
     *
     * @param guiComponent The component
     * @param constraints  The constraints
     */
    public void addComponent(GuiComponent guiComponent, Object constraints) {
        if (guiComponent != null) {
            guiComponent.setParent(this);
            components.add(guiComponent);
            getLayout().ifPresent(layout -> layout.addComponent(guiComponent, constraints));
        }
    }

    /**
     * Removes all components from this panel.
     */
    public void clearComponents() {
        components.forEach(comp -> {
            comp.setParent(null);
            getLayout().ifPresent(layout -> layout.removeComponent(comp));
        });
        components.clear();
        setOverlay((GuiComponent) null);
    }

    /**
     * Removes a component from this panel.
     *
     * @param guiComp The component to remove
     */
    public void removeComponent(GuiComponent guiComp) {
        components.remove(guiComp);
        getLayout().ifPresent(layout -> layout.removeComponent(guiComp));
    }

    @Nonnull
    public List<GuiComponent> getChildren() {
        return components;
    }

    @Nullable
    @Override
    public GuiComponent getFocused() {
        return getOverlay().orElse(focused);
    }

    public void setFocused(GuiComponent focused) {
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
    public void setOverlay(@Nullable GuiComponent gui) {
        if (gui != null) {
            gui.setParent(this);
            gui.setLocation(gui.getLocation().copy()
                    .setWidth(getLocation().getWidth())
                    .setHeight(getLocation().getHeight()));
        }
        this.overlay = gui;
    }

    public Optional<GuiComponent> getOverlay() {
        return Optional.ofNullable(overlay);
    }

    private List<GuiComponent> getOverlayOrChildren() {
        return getOverlay().map(Arrays::asList).orElse(ImmutableList.copyOf(this.components));
    }

    private final boolean isDragging() {
        return this.dragging;
    }

    protected final void setDragging(boolean p_195072_1_) {
        this.dragging = p_195072_1_;
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for(GuiComponent comp : this.getChildren()) {
            boolean flag = comp.mouseClicked(mouseX, mouseY, button);
            if (flag) {
                this.focusOn(comp);
                if (button == 0) {
                    this.setDragging(true);
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public boolean mouseDragged(double mx, double my, int mb, double mxd, double myd) {
        return this.getFocused() != null && this.isDragging() && mb == 0 && this.getFocused().mouseDragged(mx, my, mb, mxd, myd);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.setDragging(false);
        return IGuiEventListenerDeferred.super.mouseReleased(mouseX, mouseY, button);
    }


    public void focusOn(@Nullable GuiComponent comp) {
        this.switchFocus(comp, this.getChildren().indexOf(this.getFocused()));
    }

    private void switchFocus(@Nullable GuiComponent comp, int indx) {
        IGuiEventListener iguieventlistener = indx == -1 ? null : this.getChildren().get(indx);
        if (iguieventlistener != comp) {
            if (iguieventlistener != null) {
                iguieventlistener.focusChanged(false);
            }

            if (comp != null) {
                comp.focusChanged(true);
            }

            this.setFocused(comp);
        }
    }

    @Override
    public void onClosed() {
        this.components.forEach(GuiComponent::onClosed);
        this.getOverlay().ifPresent(GuiComponent::onClosed);
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
