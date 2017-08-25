package mnm.mods.util.gui;

import com.google.common.eventbus.EventBus;
import mnm.mods.util.Color;
import mnm.mods.util.ILocation;
import mnm.mods.util.Location;
import mnm.mods.util.TexturedModal;
import mnm.mods.util.gui.events.ActionPerformedEvent;
import mnm.mods.util.gui.events.GuiKeyboardEvent;
import mnm.mods.util.gui.events.GuiMouseEvent;
import mnm.mods.util.gui.events.GuiMouseEvent.MouseEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.ITextComponent;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.annotation.Nullable;

/**
 * The base class for all gui components.
 *
 * @author Matthew
 */
public abstract class GuiComponent extends Gui {

    private boolean enabled = true;
    private boolean visible = true;
    private boolean hovered;
    private boolean focused;
    private int touchValue;
    private int lastButton;
    private long lastButtonTime;

    protected Minecraft mc = Minecraft.getMinecraft();

    private Color secondaryColor;
    private Color primaryColor;
    private GuiPanel parent;
    private ILocation location = new Location();
    private Dimension minimumSize = new Dimension();
    private float scale = 1;
    private ITextComponent caption;

    private EventBus bus = new EventBus((exception, context) -> LogManager.getLogger().throwing(exception));

    public GuiComponent() {
        this.bus.register(this);
    }

    /**
     * Draws this component on screen.
     *
     * @param mouseX The mouse x
     * @param mouseY The mouse y
     */
    public void drawComponent(int mouseX, int mouseY) {
    }

    public void drawCaption(int x, int y) {
        getCaptionText()
                .map(ITextComponent::getFormattedText)
                .filter(((Predicate<String>) String::isEmpty).negate())
                .filter(t -> this.isHovered())
                .ifPresent(text -> this.drawCaption(text, x, y));
    }

    protected void drawCaption(String caption, int x, int y) {
        caption = StringEscapeUtils.unescapeJava(caption);
        String[] list = caption.split("\n\r?");

        int w = 0;
        // find the largest width
        for (String s : list) {
            w = Math.max(w, (int) (mc.fontRenderer.getStringWidth(s) * getActualScale()));
        }
        y -= mc.fontRenderer.FONT_HEIGHT * list.length;

        Point point = getActualLocation().getPoint();
        ScaledResolution sr = new ScaledResolution(mc);
        int w2 = w;
        int x2 = x;
        while (x2 - 8 + point.x + w2 + 20 > sr.getScaledWidth()) {
            x--;
            w2--;
        }
        // put it on top
        GlStateManager.pushMatrix();
        Gui.drawRect(x - 2, y - 2, x + w + 2, y + mc.fontRenderer.FONT_HEIGHT * list.length + 1,
                0xcc333333);
        drawBorders(x - 2, y - 2, x + w + 2, y + mc.fontRenderer.FONT_HEIGHT * list.length + 1,
                0xccaaaaaa);
        for (String s : list) {
            mc.fontRenderer.drawStringWithShadow(s, x, y, this.getPrimaryColorProperty().getHex());
            y += mc.fontRenderer.FONT_HEIGHT;
        }
        GlStateManager.popMatrix();
    }

    protected void drawBorders(int x1, int y1, int x2, int y2, int color) {
        this.drawVerticalLine(x1 - 1, y1 - 1, y2 + 1, color); // left
        this.drawHorizontalLine(x1 - 1, x2, y1 - 1, color); // top
        this.drawVerticalLine(x2, y1 - 1, y2 + 1, color); // right
        this.drawHorizontalLine(x1, x2 - 1, y2, color); // bottom
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
    protected void drawBorders(int x1, int y1, int x2, int y2) {
        Color color = getSecondaryColorProperty();
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        double amt = .75;
        r += luminance(r, amt);
        g += luminance(g, amt);
        b += luminance(b, amt);
        color = Color.of(r, g, b, 0xaa);
        drawBorders(x1, y1, x2, y2, color.getHex());
    }

    private static int luminance(int o, double amt) {
        return (int) ((255 - o) * amt);
    }

    /**
     * Updates the component. Called when it is called on the {@link GuiScreen}.
     */
    public void updateComponent() {
    }

    /**
     * Handles the mouse input and sends it to the mouse and action listeners.
     */
    public void handleMouseInput() {
        if (!isEnabled()) {
            this.hovered = false;
            return;
        }
        if (mc.currentScreen != null) {
            float scale = getActualScale();
            Point point = scalePoint(new Point(Mouse.getX(), Mouse.getY()), mc.currentScreen);
            ILocation actual = getActualLocation();
            // adjust for position and scale
            int x = (int) ((point.x - actual.getXPos()) / scale);
            int y = (int) ((point.y - actual.getYPos()) / scale);

            int button = Mouse.getEventButton();
            int scroll = Mouse.getEventDWheel();

            this.hovered = point.x >= actual.getXPos() && point.x <= actual.getXWidth()
                    && point.y >= actual.getYPos() && point.y <= actual.getYHeight();

            getBus().post(new GuiMouseEvent(this, MouseEvent.RAW, x, y, button, scroll));

            if (Mouse.getEventButtonState()) {
                if (this.mc.gameSettings.touchscreen && this.touchValue++ > 0) {
                    return;
                }
                lastButton = button;
                lastButtonTime = Minecraft.getSystemTime();
                if (isHovered()) {
                    getBus().post(new GuiMouseEvent(this, MouseEvent.CLICK, x, y, button, 0));
                    getBus().post(new ActionPerformedEvent(this));
                }
            } else if (button != -1) {
                if (this.mc.gameSettings.touchscreen && --this.touchValue > 0) {
                    return;
                }
                lastButton = -1;
                if (isHovered())
                    getBus().post(new GuiMouseEvent(this, MouseEvent.RELEASE, x, y, button, 0));
            } else if (lastButton != -1 && lastButtonTime > 0) {
                long buttonTime = Minecraft.getSystemTime() - this.lastButtonTime;
                getBus().post(new GuiMouseEvent(this, MouseEvent.DRAG, x, y, this.lastButton, buttonTime));
            }

            if (scroll != 0) {
                getBus().post(new GuiMouseEvent(this, MouseEvent.SCROLL, x, y, -1, -1, scroll));
            }
        }
    }

    /**
     * Handles the keyboard input and sends it to the keyboard listeners.
     */
    public void handleKeyboardInput() {
        int key = Keyboard.getEventKey();
        char character = Keyboard.getEventCharacter();
        long time = Keyboard.getEventNanoseconds();
        GuiKeyboardEvent event = new GuiKeyboardEvent(this, key, character, time);
        getBus().post(event);
    }

    /**
     * Called when the screen is closed.
     */
    public void onClosed() {
        this.hovered = false;
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
     * Gets the event bus used for the gui events for this component. This
     * replaces the listener interfaces previously used. They should easily port
     * over by adding a {@link com.google.common.eventbus.Subscribe} annotation
     * to the method.
     *
     * @return The event bus
     */
    public EventBus getBus() {
        return bus;
    }

    /**
     * Gets the parent of this component. Will return {@code null} until it is
     * added to a panel by being used as the parameter to
     * {@link GuiPanel#addComponent(GuiComponent)} or
     * {@link GuiPanel#addComponent(GuiComponent, Object)}.
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

    /**
     * Gets the top-most parent of this component. May return null if this
     * component has no parent and is not a {@link GuiPanel}.
     *
     * @return The root panel or {@code null}.
     */
    private Optional<GuiPanel> getRootPanel() {
        Optional<GuiPanel> panel = getParent();
        while (true) {
            Optional<GuiPanel> parent = panel.flatMap(GuiComponent::getParent);
            if (!parent.isPresent())
                break;
            panel = parent;
        }
        if (panel.isPresent())
            return panel;
        else if (this instanceof GuiPanel)
            return Optional.of((GuiPanel) this);
        else
            return Optional.empty();
    }

    public ILocation getActualLocation() {
        Location location = this.getLocation().copy();
        location.scale(getActualScale());
        getParent().map(GuiComponent::getActualLocation).ifPresent(loc1 ->
                location.move(loc1.getXPos(), loc1.getYPos())
        );
        return location;
    }

    public void setMinimumSize(Dimension size) {
        this.minimumSize = size;
    }

    public Dimension getMinimumSize() {
        return minimumSize;
    }

    /**
     * Sets the scale for this component.
     *
     * @param scale The scale
     */
    public void setScale(float scale) {
        this.scale = scale;
    }

    /**
     * Gets the scale for this component.
     *
     * @return The scale
     */
    public float getScale() {
        return scale;
    }

    /**
     * Gets the total scale of this component. Takes into account all the
     * parents.
     *
     * @return The scale
     */
    protected float getActualScale() {
        return getScale() * getParent().map(GuiComponent::getActualScale).orElse(1F);
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

    /**
     * Returns if the cursor is hovered over this component.
     *
     * @return THe hover state
     */
    public boolean isHovered() {
        return hovered && getParent().map(GuiComponent::isHovered).orElse(true);
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

    /**
     * Gets the current focus of this component.
     *
     * @return The focus
     */
    public boolean isFocused() {
        return focused;
    }

    /**
     * Sets the component's focus
     *
     * @param focused The focus value
     */
    public void setFocused(boolean focused) {
        // check if is focusable
        if (isFocusable()) {
            if (focused) {
                // unfocus everything before focusing this one
                getRootPanel().ifPresent(GuiPanel::unfocusAll);
            }
            this.focused = focused;
        }
        // otherwise ignore
    }

    /**
     * Denotes if this is focusable. Override to change.
     *
     * @return Whether this is focusable
     */
    public boolean isFocusable() {
        return false;
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

    protected static Point scalePoint(Point point, GuiScreen screen) {
        Minecraft mc = Minecraft.getMinecraft();
        int x = point.x * screen.width / mc.displayWidth;
        int y = screen.height - point.y * screen.height / mc.displayHeight - 1;
        return new Point(x, y);
    }

    protected void drawModalCorners(TexturedModal modal) {
        ILocation location = getLocation();
        int x = 0;
        int y = 0;

        int w = location.getWidth() / 2;
        int w2 = location.getWidth() - w;
        int h = location.getHeight() / 2;
        int h2 = location.getHeight() - h + 1;

        int mx = modal.getXPos();
        int my = modal.getYPos();
        int mw = modal.getWidth() - w2;
        int mh = modal.getHeight() - h2;

        // bind the texture
        mc.getTextureManager().bindTexture(modal.getResourceLocation());

        // top left
        drawTexturedModalRect(x, y, mx, my, w, h);
        // top right
        drawTexturedModalRect(x + w, y, mx + mw, my, w2, h);
        // bottom left
        drawTexturedModalRect(x, y + h, mx, my + mh, w, h2);
        // bottom right
        drawTexturedModalRect(x + w, y + h, mx + mw, my + mh, w2, h2);

    }
}
