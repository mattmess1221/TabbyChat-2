package mnm.mods.tabbychat.client.gui.component;

import com.mojang.blaze3d.platform.GlStateManager;
import mnm.mods.tabbychat.util.Color;
import mnm.mods.tabbychat.util.Dim;
import mnm.mods.tabbychat.util.ILocation;
import mnm.mods.tabbychat.util.Location;
import mnm.mods.tabbychat.util.TexturedModal;
import mnm.mods.tabbychat.util.Vec2i;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The base class for all gui components.
 *
 * @author Matthew
 */
public abstract class GuiComponent extends Widget {

    private boolean enabled = true;
    private boolean visible = true;

    protected Minecraft mc = Minecraft.getInstance();

    private Color secondaryColor;
    private Color primaryColor;
    private GuiPanel parent;
    private ILocation location = new Location();
    private Dim minimumSize = new Dim(0, 0);
    private ITextComponent caption;

    public GuiComponent() {
        super(0, 0, "");
    }

    /**
     * Draws this component on screen.
     *
     * @param mouseX   The mouse x
     * @param mouseY   The mouse y
     * @param parTicks
     */
    public void render(int mouseX, int mouseY, float parTicks) {
    }

    public void renderCaption(int x, int y) {
        getCaptionText()
                .map(ITextComponent::getFormattedText)
                .filter(((Predicate<String>) String::isEmpty).negate())
                .filter(t -> this.getLocation().contains(x, y))
                .ifPresent(text -> this.renderCaption(text, x, y));
    }

    protected void renderCaption(String caption, int x, int y) {
        caption = StringEscapeUtils.unescapeJava(caption);
        String[] list = caption.split("\n\r?");

        int w = 0;
        // find the largest width
        for (String s : list) {
            w = Math.max(w, (int) (mc.fontRenderer.getStringWidth(s)));
        }
        y -= mc.fontRenderer.FONT_HEIGHT * list.length;

        Vec2i point = getLocation().getPoint();
        int sw = mc.mainWindow.getScaledWidth();
        int w2 = w;
        int x2 = x;
        while (x2 - 8 + point.x + w2 + 20 > sw) {
            x--;
            w2--;
        }
        x += getLocation().getXPos();
        y += getLocation().getYPos();
        // put it on top
        GlStateManager.pushMatrix();
        fill(x - 2, y - 2, x + w + 2, y + mc.fontRenderer.FONT_HEIGHT * list.length + 1, 0xcc333333);
        renderBorders(x - 2, y - 2, x + w + 2, y + mc.fontRenderer.FONT_HEIGHT * list.length + 1,
                0xccaaaaaa);
        for (String s : list) {
            mc.fontRenderer.drawStringWithShadow(s, x, y, this.getPrimaryColorProperty().getHex());
            y += mc.fontRenderer.FONT_HEIGHT;
        }
        GlStateManager.popMatrix();
    }

    protected void renderBorders(int x1, int y1, int x2, int y2, int color) {
        this.vLine(x1 - 1, y1 - 1, y2 + 1, color); // left
        this.hLine(x1 - 1, x2, y1 - 1, color); // top
        this.vLine(x2, y1 - 1, y2 + 1, color); // right
        this.hLine(x1, x2 - 1, y2, color); // bottom
    }

    /**
     * Draws borders around the provided points. Uses a brightened background
     * color with 0xaa transparency.
     *
     * @param x1 left point
     * @param y1 upper point
     * @param x2 right point
     * @param y2 lower point
     */
    protected void renderBorders(int x1, int y1, int x2, int y2) {
        Color color = getSecondaryColorProperty();
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        double amt = .75;
        r += lum(r, amt);
        g += lum(g, amt);
        b += lum(b, amt);
        color = Color.of(r, g, b, 0xaa);
        renderBorders(x1, y1, x2, y2, color.getHex());
    }

    private static int lum(int o, double amt) {
        return (int) ((255 - o) * amt);
    }

    /**
     * Updates the component. Called when it is called on the {@link Screen}.
     */
    public void tick() {
    }

    /**
     * Called when the screen is closed.
     */
    public void onClosed() {

    }

    /**
     * Gets the current location of this component.
     *
     * @return The current immutable location.
     */
    public ILocation getLocation() {
        return this.location;
    }

    /**
     * Sets the location of this component. In order to maintain encapsulation,
     * it is wrapped as an immutable.
     *
     * @param location The new location
     */
    public void setLocation(ILocation location) {
        this.location = location.asImmutable();
    }

    /**
     * Gets the parent of this component. Will return {@code null} until it is
     * added to a panel by being used as the parameter to
     * {@link GuiPanel#add(GuiComponent)} or
     * {@link GuiPanel#add(GuiComponent, Object)}.
     *
     * @return The parent or null if there is none
     */
    public Optional<GuiPanel> getParent() {
        return Optional.ofNullable(this.parent);
    }


    /**
     * Sets the parent of this component. Should only be used by
     * {@link GuiPanel}.
     *
     * @param guiPanel The parent
     */
    void setParent(@Nullable GuiPanel guiPanel) {
        this.parent = guiPanel;
    }

    public void setMinimumSize(Dim size) {
        this.minimumSize = size;
    }

    public Dim getMinimumSize() {
        return minimumSize;
    }

    public Optional<Color> getPrimaryColor() {
        return Optional.ofNullable(this.primaryColor);
    }

    public void setPrimaryColor(@Nullable Color color) {
        this.primaryColor = color;
    }

    public Optional<Color> getSecondaryColor() {
        return Optional.ofNullable(this.secondaryColor);
    }

    public void setSecondaryColor(@Nullable Color color) {
        this.secondaryColor = color;
    }

    public Color getPrimaryColorProperty() {
        return getProperty(GuiComponent::getPrimaryColor, Color.WHITE);
    }

    public Color getSecondaryColorProperty() {
        return getProperty(GuiComponent::getSecondaryColor, Color.of(0));
    }

    /**
     * Gets if this is enabled. Disabled components will not handle mouse or
     * keyboard events.
     *
     * @return True if enabled, false if disabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets if this is enabled or not. Disabled components will not handle mouse
     * or keyboard events.
     *
     * @param enabled True for enabled, false for disabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Gets if this component is visible. Non-visible components are not
     * rendered.
     *
     * @return The visibility state
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Sets this component's visibility. Non-visible components are not
     * rendered.
     *
     * @param visible The visibility state
     */
    public void setVisible(boolean visible) {
        if (!visible) {
            this.onClosed();
        }
        this.visible = visible;
    }

    public void setCaption(@Nullable ITextComponent text) {
        this.caption = text;
    }

    /**
     * Gets the caption which is shown when the mouse is hovering over this
     * component.
     *
     * @return The caption
     * @deprecated Use {@link #getCaptionText()} instead.
     */
    @Nullable
    @Deprecated
    public String getCaption() {
        return getCaptionText().map(ITextComponent::getFormattedText).orElse(null);
    }

    public Optional<ITextComponent> getCaptionText() {
        return Optional.ofNullable(caption);
    }

    private <T> T getProperty(final Function<GuiComponent, Optional<T>> prop, T def) {
        return getProperty(prop).orElse(def);
    }

    private <T> Optional<T> getProperty(final Function<GuiComponent, Optional<T>> prop) {
        Optional<T> result = prop.apply(this);
        Optional<GuiPanel> parent = getParent();
        if (!result.isPresent()) {
            result = parent.flatMap((GuiComponent p) -> p.getProperty(prop));
        }
        return result;
    }

    protected void drawModalCorners(TexturedModal modal) {
        ILocation location = getLocation();
        int x = location.getXPos();
        int y = location.getYPos();
        int u = modal.getXPos();
        int v = modal.getYPos();
        int w = location.getWidth() + 1;
        int h = location.getHeight() + 1;
        int uw = modal.getWidth();
        int uh = modal.getHeight();

        GuiUtils.drawContinuousTexturedBox(modal.getResourceLocation(), x, y, u, v, w, h, uw, uh, 2, blitOffset);

    }
}
