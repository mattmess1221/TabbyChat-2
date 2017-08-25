package mnm.mods.tabbychat.gui;

import com.google.common.eventbus.Subscribe;
import com.mumfrey.liteloader.core.LiteLoader;
import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.gui.ChatGui;
import mnm.mods.tabbychat.settings.TabbySettings;
import mnm.mods.tabbychat.util.ScaledDimension;
import mnm.mods.util.ILocation;
import mnm.mods.util.Location;
import mnm.mods.util.gui.BorderLayout;
import mnm.mods.util.gui.GuiPanel;
import mnm.mods.util.gui.events.GuiMouseEvent;
import mnm.mods.util.gui.events.GuiMouseEvent.MouseEvent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;

import java.awt.Point;
import java.awt.Rectangle;

public class ChatBox extends GuiPanel implements ChatGui {

    public static final ResourceLocation GUI_LOCATION = new ResourceLocation("tabbychat", "textures/chatbox.png");

    private ChatArea chatArea;
    private ChatTray pnlTray;
    private TextBox txtChatInput;

    private boolean dragMode;
    private Point drag;
    private Location tempbox;

    public ChatBox(ILocation rect) {
        super(new BorderLayout());
        this.addComponent(pnlTray = new ChatTray(), BorderLayout.Position.NORTH);
        this.addComponent(chatArea = new ChatArea(), BorderLayout.Position.CENTER);
        this.addComponent(txtChatInput = new TextBox(), BorderLayout.Position.SOUTH);
        this.addComponent(new Scrollbar(chatArea), BorderLayout.Position.EAST);
        super.setLocation(rect);
        super.updateComponent();
    }

    @Subscribe
    public void killjoysMovingCompanyForAllYourFurnitureMovingNeeds(GuiMouseEvent event) {
        ILocation bounds = getLocation();

        // divide by scale because smaller scales make the point movement larger
        int x = bounds.getXPos() + event.getMouseX();
        int y = bounds.getYPos() + event.getMouseY();

        if (event.getType() == MouseEvent.CLICK) {
            if (Mouse.isButtonDown(0) && (pnlTray.isHovered() || (GuiScreen.isAltKeyDown() && isHovered()))) {
                dragMode = !pnlTray.isHandleHovered();
                drag = new Point(x, y);
                tempbox = bounds.copy();
            }
        }

        if (drag != null) {
            if (event.getType() == MouseEvent.RELEASE) {
                drag = null;
                tempbox = null;
            } else if (event.getType() == MouseEvent.DRAG) {
                if (!dragMode) {
                    setLocation(new Location(
                            tempbox.getXPos(),
                            tempbox.getYPos() + y - drag.y,
                            tempbox.getWidth() + x - drag.x,
                            tempbox.getHeight() - y + drag.y));
                    this.chatArea.markDirty();
                } else {
                    setLocation(getLocation().copy()
                            .setXPos(tempbox.getXPos() + x - drag.x)
                            .setYPos(tempbox.getYPos() + y - drag.y));
                }
            }
        }
    }

    @Override
    public float getScale() {
        return TabbyChat.getInstance().getChatGui().getChatScale();
    }

    @Override
    public void updateComponent() {
        ILocation bounds = getLocation();
        ILocation point = getActualLocation();

        float scale = getActualScale();
        ScaledResolution sr = new ScaledResolution(mc);

        // original dims
        final int x = point.getXPos();
        final int y = point.getYPos();
        final int w = (int) (bounds.getWidth() * scale);
        final int h = (int) (bounds.getHeight() * scale);

        // the new dims
        int w1 = w;
        int h1 = h;
        int x1 = x;
        int y1 = y;

        final int SCREEN_W = sr.getScaledWidth();
        final int SCREEN_H = sr.getScaledHeight();

        // limits for sizes
        // FIXME 500 and 400 max is because of texture limit
        final int MIN_W = 50;
        final int MIN_H = 50;
        final int MAX_W = Math.min(500, SCREEN_W);
        final int MAX_H = Math.min(400, SCREEN_H);

        final int HOTBAR = 25;

        // calculate width and height first
        // used to calculate max x and y
        w1 = Math.max(MIN_W, w1);
        w1 = Math.min(MAX_W, w1);
        h1 = Math.max(MIN_H, h1);
        h1 = Math.min(MAX_H, h1);

        // limits for position
        final int MIN_X = 0;
        final int MIN_Y = 0;
        final int MAX_X = SCREEN_W - w1;
        final int MAX_Y = SCREEN_H - h1 - HOTBAR;

        // calculate x and y coordinates
        x1 = Math.max(MIN_X, x1);
        x1 = Math.min(MAX_X, x1);
        y1 = Math.max(MIN_Y, y1);
        y1 = Math.min(MAX_Y, y1);

        // reset the location if it changed.
        if (x1 != x || y1 != y || w1 != w || h1 != h) {
            setLocation(new Location(
                    MathHelper.ceil(x1 / scale),
                    MathHelper.ceil(y1 / scale),
                    MathHelper.ceil(w1 / scale),
                    MathHelper.ceil(h1 / scale)));
        }
        super.updateComponent();
    }

    @Override
    public void setLocation(ILocation location) {
        super.setLocation(location);
        // save bounds
        TabbySettings sett = TabbyChat.getInstance().settings;
        sett.advanced.chatX.set(location.getXPos());
        sett.advanced.chatY.set(location.getYPos());
        sett.advanced.chatW.set(location.getWidth());
        sett.advanced.chatH.set(location.getHeight());
        LiteLoader.getInstance().writeConfig(sett);
    }

    @Override
    public void onClosed() {
        super.onClosed();
        updateComponent();
    }

    public int getWidth() {
        return getBounds().width;
    }

    @Override
    public ChatArea getChatArea() {
        return this.chatArea;
    }

    @Override
    public ChatTray getTray() {
        return this.pnlTray;
    }

    @Override
    public TextBox getChatInput() {
        return this.txtChatInput;
    }

    public void onScreenHeightResize(int oldWidth, int oldHeight, int newWidth, int newHeight) {

        if (oldWidth == 0 || oldHeight == 0)
            return; // first time!

        // measure the distance from the bottom, then subtract from new height

        ScaledDimension oldDim = new ScaledDimension(oldWidth, oldHeight);
        ScaledDimension newDim = new ScaledDimension(newWidth, newHeight);

        int bottom = oldDim.getScaledHeight() - getLocation().getYPos();
        int y = newDim.getScaledHeight() - bottom;
        this.setLocation(getLocation().copy().setYPos(y));
        this.updateComponent();
    }

    @Override
    public Rectangle getBounds() {
        return getLocation().asRectangle();
    }

}
