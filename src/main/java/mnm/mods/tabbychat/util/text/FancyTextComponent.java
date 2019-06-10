package mnm.mods.tabbychat.util.text;

import com.google.common.collect.Streams;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;

import java.util.Iterator;
import java.util.stream.Stream;

public class FancyTextComponent extends TextComponent {

    private final ITextComponent text;
    private FancyTextStyle style;

    public FancyTextComponent(ITextComponent parent) {
        if (parent instanceof FancyTextComponent)
            throw new IllegalArgumentException("Parent text cannot be fancy");
        this.text = parent;
    }

    @Override
    public String getUnformattedComponentText() {
        return text.getUnformattedComponentText();
    }

    @Override
    public ITextComponent shallowCopy() {
        ITextComponent text = this.text.shallowCopy();
        FancyTextComponent fcc = new FancyTextComponent(text);
        fcc.setFancyStyle(getFancyStyle().createCopy());
        return fcc;
    }

    @Override
    public Iterator<ITextComponent> iterator() {
        // don't iterate using the vanilla components
        return Streams.stream(this.text.iterator())
                .map(it -> it instanceof FancyTextComponent ? it
                        : new FancyTextComponent(it).setFancyStyle(this.getFancyStyle())).iterator();
    }

    @Override
    public Stream<ITextComponent> stream() {
        return Streams.concat(Stream.of(this), getSiblings().stream().flatMap(ITextComponent::stream));
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
