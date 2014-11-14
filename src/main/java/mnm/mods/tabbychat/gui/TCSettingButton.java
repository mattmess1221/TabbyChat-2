package mnm.mods.tabbychat.gui;

import java.util.Properties;

import net.minecraft.client.gui.Gui;

public abstract class TCSettingButton extends Gui {

    protected int xPosition, yPosition, width, height;

    protected String display, group;
    private Object defaultValue, value;
    protected Object tempValue;

    public TCSettingButton(String display, String group, Object defaultValue, int xPosition,
            int yPosition, int width, int height) {
        this.display = display;
        this.group = group;
        this.defaultValue = defaultValue;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
    }

    public void saveSetting(Properties properties) {
        properties.setProperty(this.group, String.valueOf(this.tempValue));
    }

    public void loadSetting(Properties properties) {
        this.value = properties.getProperty(this.group, this.defaultValue.toString());
    }

    public Object getValue() {
        return this.value;
    }

    public Object getTempValue() {
        return this.tempValue;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void reset() {
        this.tempValue = this.value;
    }

    public void resetDefault() {
        this.tempValue = this.defaultValue;
    }

    public void draw(int mouseX, int mouseY) {
        Gui.drawRect(xPosition, yPosition, xPosition + width, yPosition + height, 0xAA000000); // center
        Gui.drawRect(xPosition - 1, yPosition, xPosition, yPosition + height, 0xDD000000); // left
        Gui.drawRect(xPosition, yPosition - 1, xPosition + width, yPosition, 0xDD000000); // top
        Gui.drawRect(xPosition + width, yPosition, xPosition + width + 1, yPosition + height,
                0xDD000000); // right
        Gui.drawRect(xPosition, yPosition + height, xPosition + width, yPosition + height + 1,
                0xDD000000); // bottom

    }

    public abstract void click();
}
