package mnm.mods.tabbychat.gui;

import java.awt.Point;
import java.awt.Rectangle;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.gui.ChatGui;
import mnm.mods.tabbychat.core.GuiNewChatTC;
import mnm.mods.tabbychat.settings.ColorSettings;
import mnm.mods.tabbychat.settings.TabbySettings;
import mnm.mods.util.gui.BorderLayout;
import mnm.mods.util.gui.GuiPanel;
import mnm.mods.util.gui.events.GuiMouseAdapter;
import mnm.mods.util.gui.events.GuiMouseEvent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;

import org.lwjgl.input.Mouse;

public class ChatBox extends GuiPanel implements GuiMouseAdapter, ChatGui<GuiPanel> {

    private static ColorSettings colors = TabbyChat.getInstance().settings.colors;

    private ChatArea chatArea;
    private ChatTray pnlTray;
    private TextBox txtChatInput;

    private boolean dragMode;
    private Point drag;
    private Rectangle tempbox;

    public ChatBox(Rectangle rect) {
        super();
        this.setLayout(new BorderLayout());
        this.addComponent(pnlTray = new ChatTray(), BorderLayout.Position.NORTH);
        this.addComponent(chatArea = new ChatArea(), BorderLayout.Position.CENTER);
        this.addComponent(txtChatInput = new TextBox(), BorderLayout.Position.SOUTH);
        this.addComponent(new Scrollbar(chatArea), BorderLayout.Position.EAST);
        this.setBounds(rect);
    }

    @Override
    public void accept(GuiMouseEvent event) {
        Rectangle bounds = getBounds();

        // divide by scale because smaller scales make the point movement larger
        int x = bounds.x + event.position.x;
        int y = bounds.y + event.position.y;

        if (event.event == GuiMouseEvent.PRESSED) {
            if (Mouse.isButtonDown(0) && (pnlTray.isHovered() || (GuiScreen.isAltKeyDown() && isHovered()))) {
                dragMode = !pnlTray.isHandleHovered();
                drag = new Point(x, y);
                tempbox = new Rectangle(bounds);
            }
        }

        if (drag != null) {
            if (event.event == GuiMouseEvent.RELEASED) {
                // save bounds
                TabbySettings sett = TabbyChat.getInstance().settings;
                sett.advanced.chatX.set(bounds.x);
                sett.advanced.chatY.set(bounds.y);
                sett.advanced.chatW.set(bounds.width);
                sett.advanced.chatH.set(bounds.height);

                drag = null;
                tempbox = null;
            } else if (event.event == GuiMouseEvent.DRAGGED) {
                if (!dragMode) {
                    bounds.setSize(tempbox.width + x - drag.x, tempbox.height - y + drag.y);
                    bounds.setLocation(tempbox.x, tempbox.y + y - drag.y);
                } else {
                    bounds.setLocation(tempbox.x + x - drag.x, tempbox.y + y - drag.y);
                }
            }
        }
    }

    @Override
    public int getForeColor() {
        return colors.chatTextColor.get().getColor();
    }

    @Override
    public int getBackColor() {
        return colors.chatBoxColor.get().getColor();
    }

    @Override
    public float getScale() {
        return GuiNewChatTC.getInstance().getChatScale();
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

    @Override
    public GuiPanel asGui() {
        return this;
    }

}
