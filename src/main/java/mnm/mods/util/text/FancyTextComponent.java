package mnm.mods.util.text;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Iterators;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;

public class FancyTextComponent implements ITextComponent {

    private final ITextComponent text;
    private FancyTextStyle style;

    public FancyTextComponent(ITextComponent parent) {
        if (parent instanceof FancyTextComponent)
            throw new IllegalArgumentException("Parent text cannot be fancy");
        this.text = parent;
    }

    public FancyTextComponent(String string) {
        this(new TextComponentString(string));
    }

    @Override
    public String getUnformattedComponentText() {
        return text.getUnformattedComponentText();
    }

    @Override
    public ITextComponent createCopy() {
        ITextComponent text = this.text.createCopy();
        FancyTextComponent fcc = new FancyTextComponent(text);
        fcc.setFancyStyle(getFancyStyle().createCopy());
        return fcc;
    }

    @Override
    public ITextComponent appendSibling(ITextComponent component) {
        text.appendSibling(component);
        return this;
    }

    @Override
    public ITextComponent appendText(String text) {
        this.text.appendText(text);
        return this;
    }

    @Override
    public Style getStyle() {
        return text.getStyle();
    }

    @Override
    public ITextComponent setStyle(Style style) {
        text.setStyle(style);
        return this;
    }

    @Override
    public String getFormattedText() {
        return text.getFormattedText();
    }

    @Override
    public List<ITextComponent> getSiblings() {
        return text.getSiblings();
    }

    @Override
    public String getUnformattedText() {
        return text.getUnformattedText();
    }

    @Override
    public Iterator<ITextComponent> iterator() {
        // don't iterate using the vanilla components
        return Iterators.transform(this.text.iterator(), it -> it instanceof FancyTextComponent ? it
                : new FancyTextComponent(it).setFancyStyle(this.getFancyStyle()));
    }

    public ITextComponent getText() {
        return text;
    }

    public FancyTextStyle getFancyStyle() {
        if (this.style == null)
            this.style = new FancyTextStyle();
        return this.style;
    }

    public FancyTextComponent setFancyStyle(FancyTextStyle style) {
        this.style = style;
        return this;
    }

    @Override
    public String toString() {
        return String.format("FancyText{text=%s, fancystyle=%s}", text, style);
    }
}
