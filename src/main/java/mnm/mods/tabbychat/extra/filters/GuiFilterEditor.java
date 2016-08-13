package mnm.mods.tabbychat.extra.filters;

import static mnm.mods.tabbychat.util.Translation.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

import org.lwjgl.input.Keyboard;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.eventbus.Subscribe;

import mnm.mods.tabbychat.api.filters.Filter;
import mnm.mods.tabbychat.api.filters.FilterSettings;
import mnm.mods.util.Color;
import mnm.mods.util.gui.GuiButton;
import mnm.mods.util.gui.GuiCheckbox;
import mnm.mods.util.gui.GuiGridLayout;
import mnm.mods.util.gui.GuiLabel;
import mnm.mods.util.gui.GuiPanel;
import mnm.mods.util.gui.GuiText;
import mnm.mods.util.gui.events.ActionPerformedEvent;
import mnm.mods.util.gui.events.GuiKeyboardEvent;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class GuiFilterEditor extends GuiPanel {

    private Filter filter;
    private Consumer<Filter> consumer;

    private GuiText txtName;
    private GuiCheckbox chkRemove;
    private GuiText txtDestinations;
    private GuiCheckbox chkPm;
    private GuiCheckbox chkSound;
    private GuiText txtSound;
    private GuiText txtPattern;
    private GuiLabel lblError;

    public GuiFilterEditor(Filter filter, Consumer<Filter> consumer) {
        this.setLayout(Optional.of(new GuiGridLayout(20, 15)));
        this.filter = filter;
        this.consumer = consumer;

        String pattern = filter.getUnresolvedPattern();
        FilterSettings settings = filter.getSettings();

        int pos = 0;

        this.addComponent(new GuiLabel(new TextComponentTranslation(FILTER_TITLE)), new int[] { 8, pos, 1, 2 });

        pos += 2;
        this.addComponent(new GuiLabel(new TextComponentTranslation(FILTER_NAME)), new int[] { 1, pos });
        this.addComponent(txtName = new GuiText(), new int[] { 5, pos, 10, 1 });
        txtName.setValue(filter.getName());

        pos += 2;
        this.addComponent(new GuiLabel(new TextComponentTranslation(FILTER_DESTINATIONS)), new int[] { 1, pos });
        this.addComponent(txtDestinations = new GuiText(), new int[] { 8, pos, 10, 1 });
        txtDestinations.setValue(Joiner.on(", ").join(filter.getSettings().getChannels()));
        txtDestinations.setCaption(I18n.format(FILTER_DESTIONATIONS_DESC));

        pos += 1;
        this.addComponent(new GuiLabel(new TextComponentTranslation(FILTER_IS_PM)), new int[] { 2, pos });
        this.addComponent(chkPm = new GuiCheckbox(), new int[] { 1, pos });
        chkPm.setValue(filter.getSettings().isDestinationPm());

        pos += 1;
        this.addComponent(new GuiLabel(new TextComponentTranslation(FILTER_HIDE)), new int[] { 2, pos });
        this.addComponent(chkRemove = new GuiCheckbox(), new int[] { 1, pos });
        chkRemove.setValue(settings.isRemove());

        pos += 1;
        this.addComponent(new GuiLabel(new TextComponentTranslation(FILTER_AUDIO_NOTIFY)), new int[] { 2, pos });
        this.addComponent(chkSound = new GuiCheckbox(), new int[] { 1, pos });
        chkSound.setValue(settings.isSoundNotification());

        pos += 1;
        this.addComponent(txtSound = new GuiText(), new int[] { 3, pos, 14, 1 });
        txtSound.setValue(settings.getSoundName());
        txtSound.getBus().register(new Object() {
            private int pos;

            @Subscribe
            public void suggestSounds(GuiKeyboardEvent event) {
                final int max = 10;
                if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                    pos++;
                } else if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                    pos--;
                }

                // suggest sounds
                final String val = txtSound.getValue().toLowerCase()
                        .substring(0, txtSound.getTextField().getCursorPosition());
                List<String> list = SoundEvent.REGISTRY.getKeys().stream()
                        .map(Object::toString)
                        .filter(s -> s.contains(val))
                        .collect(Collectors.toList());

                pos = Math.min(pos, list.size() - max);
                pos = Math.max(pos, 0);
                if (list.size() > max) {
                    list = list.subList(pos, pos + max);
                }
                txtSound.setHint(Joiner.on('\n').join(list));
                if ((Keyboard.isKeyDown(Keyboard.KEY_RETURN) || Keyboard.isKeyDown(Keyboard.KEY_NUMPADENTER)) && !list.isEmpty()) {
                    txtSound.setValue(list.get(0));
                    txtSound.setFocused(false);
                }
            }
        });

        GuiButton play = new GuiButton("\u25b6") {
            @Override
            public SoundEvent getSound() {
                return SoundEvent.REGISTRY.getObject(new ResourceLocation(txtSound.getValue()));
            }
        };
        this.addComponent(play, new int[] { 18, pos, 2, 1 });

        pos += 2;
        this.addComponent(new GuiLabel(new TextComponentTranslation(FILTER_EXPRESSION)), new int[] { 1, pos });
        this.addComponent(txtPattern = new GuiText(), new int[] { 8, pos, 12, 1 });

        txtPattern.setValue(pattern == null ? "" : pattern);

        pos++;
        this.addComponent(lblError = new GuiLabel(), new int[] { 6, pos });

        GuiButton accept = new GuiButton(I18n.format("gui.done"));
        accept.getBus().register(new Object() {
            @Subscribe
            public void finish(ActionPerformedEvent event) {
                accept();
            }
        });
        this.addComponent(accept, new int[] { 5, 14, 4, 1 });

        GuiButton cancel = new GuiButton(I18n.format("gui.cancel"));
        cancel.getBus().register(new Object() {
            @Subscribe
            public void stopEverything(ActionPerformedEvent event) {
                cancel();
            }
        });
        this.addComponent(cancel, new int[] { 1, 14, 4, 1 });
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
        sett.setDestinationPm(chkPm.getValue());
        sett.setRemove(chkRemove.getValue());

        sett.setSoundNotification(chkSound.getValue());
        sett.setSoundName(txtSound.getValue());

        consumer.accept(filter);
        cancel();
    }

    private void cancel() {
        getParent().setOverlay(Optional.empty());
    }

    @Subscribe
    public void updateError(GuiKeyboardEvent event) {
        if (txtPattern.isFocused()) {
            // check valid regex
            try {
                String resolved = ChatFilter.resolveVariables(txtPattern.getValue());
                Pattern.compile(resolved);
                txtPattern.setPrimaryColor(Color.WHITE);
                lblError.setText(null);
            } catch (PatternSyntaxException e) {
                txtPattern.setPrimaryColor(Color.RED);
                String string = e.getLocalizedMessage();
                lblError.setText(new TextComponentString(string));
            }
        }
    }
}
