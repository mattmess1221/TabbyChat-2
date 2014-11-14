package mnm.mods.tabbychat.gui;

public class TCSettingBool extends TCSettingButton {

    public TCSettingBool(String display, String group, boolean defaultValue, int xPosition,
            int yPosition) {
        super(display, group, defaultValue, xPosition, yPosition, 5, 5);

    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);
    }

    @Override
    public void click() {
        this.toggle();
    }

    public void toggle() {
        tempValue = (!(Boolean) tempValue);
    }

    public boolean getBooleanValue() {
        return (Boolean) getValue();
    }
}
