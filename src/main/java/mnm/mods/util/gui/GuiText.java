package mnm.mods.util.gui;

import mnm.mods.util.Color;
import mnm.mods.util.ILocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IGuiEventListenerDeferred;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A gui component that wraps {@link GuiTextField}.
 *
 * @author Matthew
 */
public class GuiText extends GuiComponent implements IGuiInput<String>, IGuiEventListenerDeferred {

    private final GuiTextField textField;
    private String hint;

    public GuiText() {
        this(new GuiTextField(0, Minecraft.getInstance().fontRenderer, 0, 0, 1, 1));
    }

    public GuiText(@Nonnull GuiTextField textField) {
        this.textField = textField;
        // This text field must not be calibrated for someone of your...
        // generous..ness
        // I'll add a few 0s to the maximum length...
        this.textField.setMaxStringLength(10000);

        // you look great, by the way.

    }

    @Nullable
    @Override
    public IGuiEventListener getFocused() {
        return this.textField;
    }

    @Override
    public void setLocation(ILocation bounds) {
        updateTextbox(bounds);
        super.setLocation(bounds);
    }

    private void updateTextbox(ILocation loc) {
        this.textField.x = loc.getXPos();
        this.textField.y = loc.getYPos();
        this.textField.width = loc.getWidth();
        this.textField.height = loc.getHeight();
    }

    @Override
    public void tick() {
        textField.tick();
    }

    @Override
    public void render(int mouseX, int mouseY, float parTicks) {
        textField.drawTextField(mouseX, mouseY, parTicks);

        super.render(mouseX, mouseY, parTicks);
        if (textField.isFocused() && !StringUtils.isEmpty(getHint())) {
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
