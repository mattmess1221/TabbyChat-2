package mnm.mods.tabbychat.client.gui.component;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.util.ILocation;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/**
 * A slider for double values. Click and drag or scroll to change the value.
 *
 * @author Matthew
 */
public class GuiSlider extends GuiComponent implements IGuiInput<Double> {

    private static final ResourceLocation TRANSPARENCY = new ResourceLocation(TabbyChat.MODID, "textures/transparency.png");

    private boolean vertical;
    private double value;

    public GuiSlider(double value, boolean vertical) {
        this.vertical = vertical;
        this.setValue(value);
    }

    @Override
    public void render(int mouseX, int mouseY, float parTicks) {
        GlStateManager.enableBlend();
        ILocation loc = getLocation();
        Gui.drawRect(loc.getXPos(), loc.getYPos(), loc.getXWidth(), loc.getYHeight(), -1);
        mc.getTextureManager().bindTexture(TRANSPARENCY);
        Gui.drawModalRectWithCustomSizedTexture(loc.getXPos() + 1, loc.getYPos() + 1, 0, 0, loc.getWidth() - 2, loc.getHeight() - 2, 6, 6);
        drawMid(loc);
        if (vertical) {
            int nook = loc.getYPos() + Math.abs((int) (loc.getHeight() * getValue()) - loc.getHeight());
            Gui.drawRect(loc.getXPos() - 1, nook - 1, loc.getWidth() + 1, nook + 2, 0xffffffff);
            Gui.drawRect(loc.getXPos(), nook, loc.getXWidth(), nook + 1, 0xff000000);
        } else {
            int nook = loc.getXPos() + (int) (loc.getWidth() * getValue());
            Gui.drawRect(nook, loc.getYPos(), nook + 1, loc.getYHeight(), 0xff000000);
        }
        int midX = loc.getXCenter();
        int midY = loc.getYCenter();
        drawCenteredString(mc.fontRenderer, getFormattedValue(), midX, midY, -1);
        GlStateManager.disableAlphaTest();
        GlStateManager.disableBlend();
        super.render(mouseX, mouseY, parTicks);
    }

    protected void drawMid(ILocation loc) {
        Gui.drawRect(loc.getXPos() + 1, loc.getYPos() + 1, loc.getXWidth() - 1, loc.getYHeight() - 1, getPrimaryColorProperty().getHex());
    }

    public String getFormattedValue() {
        return String.format("%%%.0f", getValue() * 100);
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        return moveSlider(x, y, button);
    }

    @Override
    public boolean mouseDragged(double x, double y, int button, double xDragged, double yDragged) {
        return moveSlider(x, y, button);
    }

    private boolean moveSlider(double x, double y, int button) {
        if (x < 0 || y < 0 || x > getLocation().getWidth() || y > getLocation().getHeight()) {
            return false;
        }
        if (button == 0) {
            double val;
            if (vertical) {
                val = Math.abs(y / getLocation().getHeight() - 1);
            } else {
                val = x / getLocation().getWidth();
            }
            setValue(val);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double p_mouseScrolled_1_) {
        double x = mc.mouseHelper.getMouseX();
        double y = mc.mouseHelper.getMouseY();
        if (!getLocation().contains(x, y)) {
            return false;
        }
        setValue(getValue() + p_mouseScrolled_1_ / 7360D);
        return true;
    }

    @Override
    public void setValue(Double value) {
        if (value < 0) {
            value = 0D;
        }
        if (value > 1) {
            value = 1D;
        }
        this.value = value;
    }

    @Override
    public Double getValue() {
        return value;
    }

    public boolean isVertical() {
        return vertical;
    }
}
