package mnm.mods.tabbychat.gui;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.TabbyChatClient;
import mnm.mods.tabbychat.settings.TabbySettings;
import mnm.mods.tabbychat.util.ScaledDimension;
import mnm.mods.util.ILocation;
import mnm.mods.util.Location;
import mnm.mods.util.Vec;
import mnm.mods.util.gui.BorderLayout;
import mnm.mods.util.gui.GuiPanel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.io.IOException;

public class ChatBox extends GuiPanel {

    public static final ResourceLocation GUI_LOCATION = new ResourceLocation("tabbychat", "textures/chatbox.png");

    private ChatArea chatArea;
    private ChatTray pnlTray;
    private TextBox txtChatInput;

    private boolean dragMode;
    private Vec drag;
    private Location tempbox;

    public ChatBox(ILocation rect) {
        super(new BorderLayout());
        this.addComponent(pnlTray = new ChatTray(), BorderLayout.Position.NORTH);
        this.addComponent(chatArea = new ChatArea(), BorderLayout.Position.CENTER);
        this.addComponent(txtChatInput = new TextBox(), BorderLayout.Position.SOUTH);
        this.addComponent(new Scrollbar(chatArea), BorderLayout.Position.EAST);
        super.setLocation(rect);
        super.tick();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && (pnlTray.getLocation().contains(mouseX, mouseY)
                || GuiScreen.isAltKeyDown() && getLocation().contains(mouseX, mouseY))) {
            dragMode = !pnlTray.isHandleHovered(mouseX, mouseY);
            drag = new Vec((int) mouseX, (int) mouseY);
            tempbox = getLocation().copy();
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mx, double my, int mb, double mxd, double myd) {
        if (drag != null) {

            if (!dragMode) {
                setLocation(new Location(
                        tempbox.getXPos(),
                        tempbox.getYPos() + (int) my - drag.y,
                        tempbox.getWidth() + (int) mx - drag.x,
                        tempbox.getHeight() - (int) my + drag.y));
                this.chatArea.markDirty();
            } else {
                setLocation(getLocation().copy()
                        .setXPos(tempbox.getXPos() + (int) mx - drag.x)
                        .setYPos(tempbox.getYPos() + (int) my - drag.y));
            }
        }
        return super.mouseDragged(mx, my, mb, mxd, myd);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (drag != null) {
            drag = null;
            tempbox = null;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double p_mouseScrolled_1_) {
        return this.chatArea.mouseScrolled(p_mouseScrolled_1_);
    }

    @Override
    public void tick() {
        ILocation bounds = getLocation();

        double scale = mc.gameSettings.chatScale;

        // original dims
        final int x = (int) (bounds.getXPos() * scale);
        final int y = (int) (bounds.getYPos() * scale);
        final int w = (int) (bounds.getWidth() * scale);
        final int h = (int) (bounds.getHeight() * scale);

        // the new dims
        int w1 = w;
        int h1 = h;
        int x1 = x;
        int y1 = y;

        final int SCREEN_W = mc.mainWindow.getScaledWidth();
        final int SCREEN_H = mc.mainWindow.getScaledHeight();

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
        // this is different because height anchor is at the top
        // so is affected at the bottom.
        if (h1 < MIN_H) {
            y1 -= MIN_H - h1;
            h1 = MIN_H;
        }
        if (h1 > MAX_H) {
            y1 += h1 - MAX_H;
            h1 = MAX_H;
        }

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
        super.tick();
    }

    @Override
    public void setLocation(ILocation location) {
        super.setLocation(location);
        // save bounds
        TabbySettings sett = TabbyChatClient.getInstance().getSettings();
        sett.advanced.chatX.set(location.getXPos());
        sett.advanced.chatY.set(location.getYPos());
        sett.advanced.chatW.set(location.getWidth());
        sett.advanced.chatH.set(location.getHeight());
        try {
            sett.save();
        } catch (IOException e) {
            TabbyChat.logger.warn("Unable to save settings", e);
        }
    }

    @Override
    public void onClosed() {
        super.onClosed();
        tick();
    }

    public ChatArea getChatArea() {
        return this.chatArea;
    }

    public ChatTray getTray() {
        return this.pnlTray;
    }

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
        this.tick();
    }


}
