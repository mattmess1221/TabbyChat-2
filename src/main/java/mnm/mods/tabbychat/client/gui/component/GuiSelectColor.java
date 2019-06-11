package mnm.mods.tabbychat.client.gui.component;

import mnm.mods.tabbychat.client.gui.component.layout.GuiGridLayout;
import mnm.mods.tabbychat.util.Color;
import mnm.mods.tabbychat.util.config.Value;
import mnm.mods.tabbychat.client.gui.component.config.GuiSettingString;
import mnm.mods.tabbychat.util.text.TextBuilder;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

/**
 * A gui used to select a color.
 *
 * @author Matthew
 */
public class GuiSelectColor extends GuiPanel {

    private Color color;

    private GuiSliderColor sliderRed;
    private GuiSliderColor sliderGreen;
    private GuiSliderColor sliderBlue;
    private GuiSliderColor sliderAlpha;

    private GuiSettingString string;

    private GuiRectangle current = new GuiRectangle() {
        @Override
        public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
            setColor(getPrimaryColorProperty());
            return true;
        }
    };
    private GuiRectangle selected = new GuiRectangle();

    /**
     * Creates a new instance with a color and callback.
     *
     * @param callback_ Called when apply is clicked
     * @param color The starting color
     */
    public GuiSelectColor(final Consumer<Color> callback_, Color color) {
        this.current.setPrimaryColor(color);
        this.selected.setPrimaryColor(color);
        this.setLayout(new GuiGridLayout(20, 20));

        this.addComponent(sliderRed = new GuiSliderColor(color.getRed() / 255D, true, GuiSliderColor.Model.RED, color), new int[] { 1, 1, 2, 10 });
        this.addComponent(sliderGreen = new GuiSliderColor(color.getGreen() / 255D, true, GuiSliderColor.Model.GREEN, color), new int[] { 4, 1, 2, 10 });
        this.addComponent(sliderBlue = new GuiSliderColor(color.getBlue() / 255D, true, GuiSliderColor.Model.BLUE, color), new int[] { 7, 1, 2, 10 });
        this.addComponent(sliderAlpha = new GuiSliderColor(color.getAlpha() / 255D, true, GuiSliderColor.Model.ALPHA, color), new int[] { 10, 1, 2, 10 });

        GuiLabel label;

        label = new GuiLabel();
        label.setText(new TextBuilder().quickTranslate("colors.red").format(TextFormatting.RED).build());
        label.setAngle(300);
        this.addComponent(label, new int[] { 1, 12 });

        label = new GuiLabel();
        label.setText(new TextBuilder().quickTranslate("colors.green").format(TextFormatting.GREEN).build());
        label.setAngle(300);
        this.addComponent(label, new int[] { 4, 12 });

        label = new GuiLabel();
        label.setText(new TextBuilder().quickTranslate("colors.blue").format(TextFormatting.BLUE).build());
        label.setAngle(300);
        this.addComponent(label, new int[] { 7, 12 });

        label = new GuiLabel();
        label.setText(new TextBuilder().quickTranslate("colors.alpha").format(TextFormatting.WHITE).build());
        label.setAngle(300);
        this.addComponent(label, new int[] { 10, 12 });

        this.addComponent(current, new int[] { 14, 1, 6, 3 });
        this.addComponent(selected, new int[] { 14, 4, 6, 3 });

        string = new GuiSettingString(new Value<>("")){
            @Override
            public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
                if (p_keyPressed_1_ == GLFW.GLFW_KEY_ENTER) {
                    String hex = getValue();
                    if (hex.matches("^[0-9a-fA-F]{1,8}$")) { // valid hex
                        int c = Integer.parseInt(hex, 16);
                        setColor(Color.of(c));
                        return true;
                    }
                }
                return false;
            }
        };
        string.getComponent().getTextField().setMaxStringLength(8);
        this.addComponent(string, new int[] { 14, 8, 6, 2 });

        GuiButton random = new GuiButton(I18n.format("createWorld.customize.custom.randomize")){
            @Override
            public void onClick(double mouseX, double mouseY) {
                setColor(Color.random());
            }
        };
        this.addComponent(random, new int[] { 13, 11, 8, 2 });

        GuiButton cancel = new GuiButton(I18n.format("gui.cancel")){
            @Override
            public void onClick(double mouseX, double mouseY) {
                getParent().ifPresent(parent -> parent.setOverlay(null));
            }
        };
        this.addComponent(cancel, new int[] { 13, 13, 4, 2 });
        GuiButton apply = new GuiButton(I18n.format("gui.done")){
            @Override
            public void onClick(double mouseX, double mouseY) {
                callback_.accept(GuiSelectColor.this.color);
            }
        };
        this.addComponent(apply, new int[] { 17, 13, 4, 2 });

        setColor(color);
    }

    public void setColor(Color color) {
        sliderRed.setValue(color.getRed() / 255D);
        sliderGreen.setValue(color.getGreen() / 255D);
        sliderBlue.setValue(color.getBlue() / 255D);
        sliderAlpha.setValue(color.getAlpha() / 255D);
    }

    @Override
    public void tick() {
        super.tick();

        int r = (int) (sliderRed.getValue() * 255);
        int g = (int) (sliderGreen.getValue() * 255);
        int b = (int) (sliderBlue.getValue() * 255);
        int a = (int) (sliderAlpha.getValue() * 255);

        Color color = Color.of(r, g, b, a);
        if (!color.equals(this.color)) {
            this.color = color;
            selected.setPrimaryColor(color);

            sliderRed.setBase(color);
            sliderGreen.setBase(color);
            sliderBlue.setBase(color);
            sliderAlpha.setBase(color);

            string.setValue(Integer.toHexString(color.getHex()));
        }
    }
}
