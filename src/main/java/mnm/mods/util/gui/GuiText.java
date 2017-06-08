package mnm.mods.util.gui;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import com.google.common.eventbus.Subscribe;
import com.mumfrey.liteloader.client.overlays.IGuiTextField;

import mnm.mods.util.Color;
import mnm.mods.util.ILocation;
import mnm.mods.util.gui.events.GuiKeyboardEvent;
import mnm.mods.util.gui.events.GuiMouseEvent;
import mnm.mods.util.gui.events.GuiMouseEvent.MouseEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;

/**
 * A gui component that wraps {@link GuiTextField}.
 *
 * @author Matthew
 */
public class GuiText extends GuiComponent implements IGuiInput<String> {

    private final GuiTextField textField;
    private String hint;

    public GuiText() {
        this(new GuiTextField(0, Minecraft.getMinecraft().fontRendererObj, 0, 0, 1, 1));
    }

    public GuiText(@Nonnull GuiTextField textField) {
        this.textField = textField;
        // This text field must not be calibrated for someone of your...
        // generous..ness
        // I'll add a few 0s to the maximum length...
        this.textField.setMaxStringLength(10000);

        // you look great, by the way.

    }

    @Subscribe
    public void textboxClick(GuiMouseEvent event) {
        if (event.getType() == MouseEvent.CLICK) {
            setFocused(true);

            int x = event.getMouseX();
            int y = event.getMouseY();

            // send to text field.
            textField.mouseClicked(x, y, 0);
        }
    }

    @Subscribe
    public void textboxType(GuiKeyboardEvent event) {
        if (Keyboard.isKeyDown(event.getKey())) {
            textField.textboxKeyTyped(event.getCharacter(), event.getKey());
        }
    }

    @Override
    public void setLocation(ILocation bounds) {
        updateTextbox(bounds);
        super.setLocation(bounds);
    }

    private void updateTextbox(ILocation loc) {
        int width = loc.getWidth();
        int height = loc.getHeight();
        // this interface is provided by liteloader. (Thanks, mum)
        IGuiTextField field = (IGuiTextField) this.textField;
        field.setInternalWidth(width);
        field.setHeight(height);
    }

    @Override
    public boolean isFocusable() {
        return true;
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
        textField.setFocused(focused);
    }

    @Override
    public void updateComponent() {
        super.updateComponent();
        textField.updateCursorCounter();
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        textField.drawTextBox();

        super.drawComponent(mouseX, mouseY);
        if (isFocused() && !StringUtils.isEmpty(getHint())) {
            // draw the hint above.
            drawCaption(getHint(), 1, -5);
        }
    }

    @Override
    public void setPrimaryColor(Color foreColor) {
        textField.setTextColor(foreColor.getHex());
        super.setPrimaryColor(foreColor);
    }

    @Override
    public String getValue() {
        return textField.getText();
    }

    @Override
    public void setValue(String value) {
        textField.setText(value);
        textField.setCursorPositionZero();
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public GuiTextField getTextField() {
        return textField;
    }
}
