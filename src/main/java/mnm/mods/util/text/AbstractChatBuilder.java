package mnm.mods.util.text;

import mnm.mods.util.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentScore;
import net.minecraft.util.text.TextComponentSelector;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public abstract class AbstractChatBuilder implements ITextBuilder {

    protected ITextComponent current;

    @Override
    public ITextBuilder format(TextFormatting f) {
        checkCreated();
        if (f.isColor()) {
            current.getStyle().setColor(f);
        } else if (f.isFancyStyling()) {
            if (f == TextFormatting.BOLD) {
                current.getStyle().setBold(true);
            } else if (f == TextFormatting.ITALIC) {
                current.getStyle().setItalic(true);
            } else if (f == TextFormatting.UNDERLINE) {
                current.getStyle().setUnderlined(true);
            } else if (f == TextFormatting.STRIKETHROUGH) {
                current.getStyle().setStrikethrough(true);
            } else if (f == TextFormatting.OBFUSCATED) {
                current.getStyle().setObfuscated(true);
            }
        } else if (f == TextFormatting.RESET) {
            current.setStyle(new Style());
        }
        return this;
    }

    @Override
    public ITextBuilder color(Color color) {
        asFancy().getFancyStyle().setColor(color);
        return this;
    }

    @Override
    public ITextBuilder underline(Color color) {
        asFancy().getFancyStyle().setUnderline(color);
        return this;
    }

    @Override
    public ITextBuilder highlight(Color color) {
        asFancy().getFancyStyle().setHighlight(color);
        return this;
    }

    private FancyTextComponent asFancy() {
        if (!(current instanceof FancyTextComponent)) {
            current = new FancyTextComponent(current);
        }
        return (FancyTextComponent) current;
    }

    @Override
    public ITextBuilder click(ClickEvent event) {
        checkCreated();
        current.getStyle().setClickEvent(event);
        return this;
    }

    @Override
    public ITextBuilder hover(HoverEvent event) {
        checkCreated();
        current.getStyle().setHoverEvent(event);
        return this;
    }

    @Override
    public ITextBuilder insertion(String insertion) {
        checkCreated();
        current.getStyle().setInsertion(insertion);
        return this;
    }

    private void checkCreated() {
        if (current == null) {
            throw new IllegalStateException("A chat component has not been created yet.");
        }
    }

    @Override
    public ITextBuilder score(String player, String objective) {
        return append(new TextComponentScore(player, objective));
    }

    @Override
    public ITextBuilder text(String text) {
        return append(new TextComponentString(text));
    }

    @Override
    public ITextBuilder selector(Selector selector) {
        return append(new TextComponentSelector(selector.toString()));
    }

    @Override
    public ITextBuilder translation(String key) {
        return new TranslationBuilder(this, key);
    }

    @Override
    public ITextBuilder quickTranslate(String key) {
        return translation(key).end();
    }

}
