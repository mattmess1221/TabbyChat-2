package mnm.mods.tabbychat.extra.filters;

import static mnm.mods.tabbychat.util.Translation.*;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import mnm.mods.tabbychat.api.filters.FilterSettings;
import mnm.mods.util.Color;
import mnm.mods.util.gui.GuiButton;
import mnm.mods.util.gui.GuiCheckbox;
import mnm.mods.util.gui.GuiComponent;
import mnm.mods.util.gui.GuiGridLayout;
import mnm.mods.util.gui.GuiLabel;
import mnm.mods.util.gui.GuiPanel;
import mnm.mods.util.gui.GuiText;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GuiFilterEditor extends GuiPanel {

    private class ToggleButton extends GuiButton {
        private boolean active;

        private ToggleButton(String text) {
            super(text);
        }

        @Override
        public String getText() {
            String text = super.getText();
            TextFormatting color = active ? TextFormatting.GREEN : TextFormatting.RED;
            return color + text;
        }

        @Override
        public void onClick(double mouseX, double mouseY) {
            active ^= true;
        }
    }

    private UserFilter filter;
    private Consumer<UserFilter> consumer;

    private GuiText txtName;
    private GuiCheckbox chkRemove;
    private GuiText txtDestinations;
    private GuiCheckbox chkSound;
    private GuiText txtSound;
    private GuiText txtPattern;
    private GuiLabel lblError;

    private ToggleButton btnRegexp;
    private ToggleButton btnIgnoreCase;
    private ToggleButton btnRaw;

    public GuiFilterEditor(UserFilter filter, Consumer<UserFilter> consumer) {
        this.setLayout(new GuiGridLayout(20, 15));
        this.filter = filter;
        this.consumer = consumer;

        String pattern = filter.getRawPattern();
        FilterSettings settings = filter.getSettings();

        int pos = 0;

        this.addComponent(new GuiLabel(new TextComponentTranslation(FILTER_TITLE)), new int[]{8, pos, 1, 2});

        pos += 2;
        this.addComponent(new GuiLabel(new TextComponentTranslation(FILTER_NAME)), new int[]{1, pos});
        this.addComponent(txtName = new GuiText(), new int[]{5, pos, 10, 1});
        txtName.setValue(filter.getName());

        pos += 2;
        this.addComponent(new GuiLabel(new TextComponentTranslation(FILTER_DESTINATIONS)), new int[]{1, pos});
        this.addComponent(txtDestinations = new GuiText(), new int[]{8, pos, 10, 1});
        txtDestinations.setValue(Joiner.on(", ").join(settings.getChannels()));
        txtDestinations.setCaption(new TextComponentTranslation(FILTER_DESTIONATIONS_DESC));

        pos += 1;
        this.addComponent(btnRegexp = new ToggleButton(".*"), new int[]{1, pos, 2, 1});
        btnRegexp.active = filter.getSettings().isRegex();
        btnRegexp.setCaption(new TextComponentTranslation(FILTER_REGEX));
        this.addComponent(btnIgnoreCase = new ToggleButton("Aa"), new int[]{3, pos, 2, 1});
        btnIgnoreCase.active = settings.isCaseInsensitive();
        btnIgnoreCase.setCaption(new TextComponentTranslation(FILTER_IGNORE_CASE));
        this.addComponent(btnRaw = new ToggleButton("&0"), new int[]{5, pos, 2, 1});
        btnRaw.active = settings.isRaw();
        btnRaw.setCaption(new TextComponentTranslation(FILTER_RAW_INPUT));

        pos += 2;
        this.addComponent(new GuiLabel(new TextComponentTranslation(FILTER_HIDE)), new int[]{2, pos});
        this.addComponent(chkRemove = new GuiCheckbox(), new int[]{1, pos});
        chkRemove.setValue(settings.isRemove());

        pos += 1;
        this.addComponent(new GuiLabel(new TextComponentTranslation(FILTER_AUDIO_NOTIFY)), new int[]{2, pos});
        this.addComponent(chkSound = new GuiCheckbox(), new int[]{1, pos});
        chkSound.setValue(settings.isSoundNotification());

        pos += 1;
        this.addComponent(txtSound = new GuiText() {
            private int pos;

            @Override
            public boolean charTyped(char c, int key) {
                final int max = 10;
                if (key == GLFW.GLFW_KEY_DOWN) {
                    pos++;
                } else if (key == GLFW.GLFW_KEY_UP) {
                    pos--;
                }
                // suggest sounds
                final String val = getValue().toLowerCase()
                        .substring(0, getTextField().getCursorPosition());
                List<String> list = GameRegistry.findRegistry(SoundEvent.class).getKeys().stream()
                        .map(Object::toString)
                        .filter(s -> s.contains(val))
                        .collect(Collectors.toList());

                pos = Math.min(pos, list.size() - max);
                pos = Math.max(pos, 0);
                if (list.size() > max) {
                    list = list.subList(pos, pos + max);
                }
                setHint(Joiner.on('\n').join(list));
                if ((key == GLFW.GLFW_KEY_ENTER || key == GLFW.GLFW_KEY_KP_ENTER) && !list.isEmpty()) {
                    setValue(list.get(0));
                    GuiFilterEditor.this.setFocused(null);
                }
                return super.charTyped(c, key);
            }
        }, new int[]{3, pos, 14, 1});
        txtSound.setValue(settings.getSoundName().orElse(""));

        GuiButton play = new GuiButton("\u25b6") {
            @Override
            public SoundEvent getSound() {
                return GameRegistry.findRegistry(SoundEvent.class).getValue(new ResourceLocation(txtSound.getValue()));
            }
        };
        this.addComponent(play, new int[]{18, pos, 2, 1});

        pos += 2;
        this.addComponent(new GuiLabel(new TextComponentTranslation(FILTER_EXPRESSION)), new int[]{1, pos});
        this.addComponent(txtPattern = new GuiText() {
            @Override
            public boolean charTyped(char c, int key) {
                boolean r = super.charTyped(c, key);
                setPrimaryColor(Color.WHITE);
                lblError.setText(null);
                if (btnRegexp.active) {
                    // check valid regex
                    try {
                        filter.testPattern(getValue());
                    } catch (UserFilter.UserPatternException e) {
                        setPrimaryColor(Color.RED);
                        String string = e.getCause().getLocalizedMessage();
                        lblError.setText(new TextComponentString(string));
                    }
                }
                return r;
            }
        }, new int[]{8, pos, 12, 1});

        txtPattern.setValue(pattern == null ? "" : pattern);

        pos++;
        this.addComponent(lblError = new GuiLabel(), new int[]{4, pos});

        GuiButton accept = new GuiButton(I18n.format("gui.done")) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                accept();
            }
        };
        this.addComponent(accept, new int[]{5, 14, 4, 1});

        GuiButton cancel = new GuiButton(I18n.format("gui.cancel")) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                cancel();
            }
        };
        this.addComponent(cancel, new int[]{1, 14, 4, 1});
    }

    private void accept() {
        filter.setName(txtName.getValue());
        filter.setPattern(txtPattern.getValue());
        FilterSettings sett = filter.getSettings();
        sett.getChannels().clear();
        sett.getChannels().addAll(Splitter.on(",")
                .omitEmptyStrings()
                .trimResults()
                .splitToList(txtDestinations.getValue()));
        sett.setRemove(chkRemove.getValue());
        sett.setCaseInsensitive(btnIgnoreCase.active);
        sett.setRegex(btnRegexp.active);
        sett.setRaw(btnRaw.active);

        sett.setSoundNotification(chkSound.getValue());
        sett.setSoundName(txtSound.getValue());

        consumer.accept(filter);
        cancel();
    }

    private void cancel() {
        getParent().ifPresent(p -> p.setOverlay(null));
    }

    @Nullable
    @Override
    public GuiComponent getFocused() {
        return txtPattern;
    }

}
