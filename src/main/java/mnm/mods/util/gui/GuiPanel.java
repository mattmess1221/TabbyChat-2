package mnm.mods.util.gui;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import mnm.mods.util.ILocation;
import mnm.mods.util.gui.events.GuiMouseEvent;
import mnm.mods.util.gui.events.GuiMouseEvent.MouseEvent;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A component that can contain multiple components.
 *
 * @author Matthew
 */
public class GuiPanel extends GuiComponent implements Iterable<GuiComponent> {

    private List<GuiComponent> components = Lists.newArrayList();
    private GuiComponent overlay;
    private ILayout layout;

    public GuiPanel(ILayout layout) {
        setLayout(layout);
    }

    public GuiPanel() {
    }

    @Subscribe
    public void unfocus(GuiMouseEvent event) {
        // Unfocuses all focusable on click
        if (event.getType() == MouseEvent.CLICK) {
            unfocusAll();
        }
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        if (this.getOverlay().isPresent()) {
            getOverlay().get().drawComponent(mouseX, mouseY);
            return;
        }
        getLayout().ifPresent(layout -> layout.layoutComponents(this));
        this.components.stream()
                .filter(GuiComponent::isVisible)
                .forEach(gc -> {
                    GlStateManager.pushMatrix();
                    ILocation location = gc.getLocation();
                    int xPos = location.getXPos();
                    int yPos = location.getYPos();
                    int x = (int) (mouseX / gc.getScale()) - xPos;
                    int y = (int) (mouseY / gc.getScale()) - yPos;
                    GlStateManager.translate(xPos, yPos, 0F);
                    GlStateManager.scale(gc.getScale(), gc.getScale(), 1F);
                    gc.drawComponent(x, y);

                    GlStateManager.popMatrix();
                });

        super.drawComponent(mouseX, mouseY);
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
                .forEach(gc -> {
                    GlStateManager.pushMatrix();
                    ILocation location = gc.getLocation();
                    int xPos = location.getXPos();
                    int yPos = location.getYPos();
                    int x = (int) (mouseX / gc.getScale()) - xPos;
                    int y = (int) (mouseY / gc.getScale()) - yPos;
                    GlStateManager.translate(xPos, yPos, 0F);
                    GlStateManager.scale(gc.getScale(), gc.getScale(), 1F);

                    gc.drawCaption(x, y);

                    GlStateManager.popMatrix();
                });
    }

    @Override
    public void updateComponent() {
        getOverlayOrChildren().forEach(GuiComponent::updateComponent);

    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        getOverlayOrChildren().forEach(GuiComponent::handleMouseInput);
    }

    @Override
    public void handleKeyboardInput() {
        super.handleKeyboardInput();
        getOverlayOrChildren().forEach(GuiComponent::handleKeyboardInput);
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
        } else {
            this.unfocusAll();
        }
        this.overlay = gui;
    }

    public Optional<GuiComponent> getOverlay() {
        return Optional.ofNullable(overlay);
    }

    private List<GuiComponent> getOverlayOrChildren() {
        return getOverlay().map(Arrays::asList).orElse(ImmutableList.copyOf(this.components));
    }

    /**
     * Unfocuses all components in this panel that are focusable.
     */
    public void unfocusAll() {
        for (GuiComponent comp : components) {
            if (comp.isFocusable()) {
                comp.setFocused(false);
            } else if (comp instanceof GuiPanel) {
                ((GuiPanel) comp).unfocusAll();
            }
        }
    }

    @Override
    public void onClosed() {
        this.components.forEach(GuiComponent::onClosed);
        this.getOverlay().ifPresent(GuiComponent::onClosed);
    }

    @Nonnull
    @Override
    public Dimension getMinimumSize() {
        Dimension size = getLayout().map(ILayout::getLayoutSize).orElseGet(() -> {
            int width = 0;
            int height = 0;
            for (GuiComponent gc : components) {
                width = Math.max(width, gc.getLocation().getXPos() + gc.getLocation().getWidth());
                height = Math.max(height, gc.getLocation().getYPos() + gc.getLocation().getHeight());
            }
            return new Dimension(width, height);
        });

        return size;

    }

    @Deprecated
    @Override
    public Iterator<GuiComponent> iterator() {
        return components.iterator();
    }

}
