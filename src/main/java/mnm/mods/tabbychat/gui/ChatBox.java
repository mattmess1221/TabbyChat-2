package mnm.mods.tabbychat.gui;

import java.awt.Point;
import java.awt.Rectangle;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.google.common.eventbus.Subscribe;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.gui.ChatGui;
import mnm.mods.tabbychat.settings.ColorSettings;
import mnm.mods.tabbychat.settings.TabbySettings;
import mnm.mods.tabbychat.util.ScaledDimension;
import mnm.mods.util.Color;
import mnm.mods.util.gui.BorderLayout;
import mnm.mods.util.gui.GuiPanel;
import mnm.mods.util.gui.events.GuiKeyboardEvent;
import mnm.mods.util.gui.events.GuiMouseEvent;
import mnm.mods.util.gui.events.GuiMouseEvent.MouseEvent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ITabCompleter;
import net.minecraft.util.TabCompleter;
import net.minecraft.util.math.MathHelper;

public class ChatBox extends GuiPanel implements ChatGui, ITabCompleter {

    private static ColorSettings colors = TabbyChat.getInstance().settings.colors;

    private ChatArea chatArea;
    private ChatTray pnlTray;
    private TextBox txtChatInput;

    private boolean dragMode;
    private Point drag;
    private Rectangle tempbox;

    private TabCompleter tabCompleter;

    public ChatBox(Rectangle rect) {
        super();
        this.setLayout(new BorderLayout());
        this.addComponent(pnlTray = new ChatTray(), BorderLayout.Position.NORTH);
        this.addComponent(chatArea = new ChatArea(), BorderLayout.Position.CENTER);
        this.addComponent(txtChatInput = new TextBox(), BorderLayout.Position.SOUTH);
        this.addComponent(new Scrollbar(chatArea), BorderLayout.Position.EAST);
        this.setBounds(rect);
    }

    @Subscribe
    public void killjoysMovingCompanyForAllYourFurnitureMovingNeeds(GuiMouseEvent event) {
        Rectangle bounds = getBounds();

        // divide by scale because smaller scales make the point movement larger
        int x = bounds.x + event.getMouseX();
        int y = bounds.y + event.getMouseY();

        if (event.getType() == MouseEvent.CLICK) {
            if (Mouse.isButtonDown(0) && (pnlTray.isHovered() || (GuiScreen.isAltKeyDown() && isHovered()))) {
                dragMode = !pnlTray.isHandleHovered();
                drag = new Point(x, y);
                tempbox = new Rectangle(bounds);
            }
        }

        if (drag != null) {
            if (event.getType() == MouseEvent.RELEASE) {
                // save bounds
                TabbySettings sett = TabbyChat.getInstance().settings;
                sett.advanced.chatX.set(bounds.x);
                sett.advanced.chatY.set(bounds.y);
                sett.advanced.chatW.set(bounds.width);
                sett.advanced.chatH.set(bounds.height);

                drag = null;
                tempbox = null;
            } else if (event.getType() == MouseEvent.DRAG) {
                if (!dragMode) {
                    bounds.setSize(tempbox.width + x - drag.x, tempbox.height - y + drag.y);
                    bounds.setLocation(tempbox.x, tempbox.y + y - drag.y);
                } else {
                    bounds.setLocation(tempbox.x + x - drag.x, tempbox.y + y - drag.y);
                }
            }
        }
    }

    @Subscribe
    public void autoCompleteResponses(GuiKeyboardEvent key) {
        this.tabCompleter.resetRequested();
        if (key.getKey() == Keyboard.KEY_TAB)
            this.tabCompleter.complete();
        else {
            this.tabCompleter.resetDidComplete();
        }
    }

    @Override
    public void setCompletions(String... newCompletions) {
        this.tabCompleter.setCompletions(newCompletions);
    }

    @Override
    public Color getForeColor() {
        return colors.chatBorderColor.get();
    }

    @Override
    public Color getBackColor() {
        return colors.chatBoxColor.get();
    }

    @Override
    public float getScale() {
        return TabbyChat.getInstance().getChatGui().getChatScale();
    }

    @Override
    public void updateComponent() {
        Rectangle bounds = getBounds();
        Point point = getActualPosition();
        float scale = getActualScale();
        ScaledResolution sr = new ScaledResolution(mc);

        int x = point.x;
        int y = point.y;
        int w = (int) (bounds.width * scale);
        int h = (int) (bounds.height * scale);

        int w1 = w;
        int h1 = h;
        int x1 = x;
        int y1 = y;

        w1 = Math.min(sr.getScaledWidth(), w);
        h1 = Math.min(sr.getScaledHeight(), h);
        w1 = Math.max(50, w1);
        h1 = Math.max(50, h1);

        x1 = Math.max(0, x1);
        x1 = Math.min(x1, sr.getScaledWidth() - w1);
        y1 = Math.max(0, y1);
        y1 = Math.min(y1, sr.getScaledHeight() - h1);

        if (x1 != x || y1 != y || w1 != w || h1 != h) {
            bounds.x = MathHelper.ceiling_double_int(x1 / scale);
            bounds.y = MathHelper.ceiling_double_int(y1 / scale);
            bounds.width = MathHelper.ceiling_double_int(w1 / scale);
            bounds.height = MathHelper.ceiling_double_int(h1 / scale);
        }
        super.updateComponent();
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
        // measure the distance from the bottom, then subtract from new height

        ScaledDimension oldDim = new ScaledDimension(oldWidth, oldHeight);
        ScaledDimension newDim = new ScaledDimension(newWidth, newHeight);

        int bottom = oldDim.getScaledHeight() - getBounds().y;
        getBounds().y = newDim.getScaledHeight() - bottom;
        this.updateComponent();
    }

}
