package mnm.mods.tabbychat.filters;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import mnm.mods.tabbychat.api.filters.Filter;
import mnm.mods.tabbychat.api.filters.FilterSettings;
import mnm.mods.tabbychat.util.Translation;
import mnm.mods.util.Consumer;
import mnm.mods.util.SettingValue;
import mnm.mods.util.gui.GuiButton;
import mnm.mods.util.gui.GuiGridLayout;
import mnm.mods.util.gui.GuiLabel;
import mnm.mods.util.gui.GuiPanel;
import mnm.mods.util.gui.GuiSettingBoolean;
import mnm.mods.util.gui.GuiSettingString;
import mnm.mods.util.gui.events.ActionPerformed;
import mnm.mods.util.gui.events.GuiEvent;
import mnm.mods.util.gui.events.GuiKeyboardAdapter;
import mnm.mods.util.gui.events.GuiKeyboardEvent;
import net.minecraft.client.resources.I18n;

public class GuiFilterEditor extends GuiPanel implements GuiKeyboardAdapter {

    private Filter filter;
    private Consumer<Filter> consumer;

    private GuiSettingString txtName;
    private GuiSettingBoolean chkRemove;
    private GuiSettingString txtDestinations;
    private GuiSettingBoolean chkSound;
    private GuiSettingString txtSound;
    private GuiSettingString txtPattern;
    private GuiLabel lblError;

    public GuiFilterEditor(Filter filter, Consumer<Filter> consumer) {
        this.setLayout(new GuiGridLayout(20, 15));
        this.filter = filter;
        this.consumer = consumer;

        Pattern pattern = filter.getPattern();
        FilterSettings settings = filter.getSettings();

        this.addComponent(new GuiLabel(Translation.FILTER_TITLE.translate()),
                new int[] { 8, 0, 1, 2 });

        this.addComponent(new GuiLabel(Translation.FILTER_NAME.translate()), new int[] { 1, 2, });
        this.addComponent(
                txtName = new GuiSettingString(new SettingValue<String>(filter.getName())),
                new int[] { 5, 2, 10, 1 });

        this.addComponent(new GuiLabel(Translation.FILTER_DESTINATIONS.translate()),
                new int[] { 1, 5 });
        this.addComponent(txtDestinations = new GuiSettingString(new SettingValue<String>(
                merge(filter.getSettings().getChannels()))), new int[] { 10, 5, 10, 1 });
        txtDestinations.getTextField().setMaxStringLength(1000);

        this.addComponent(new GuiLabel(Translation.FILTER_HIDE.translate()), new int[] { 2, 7 });
        this.addComponent(
                chkRemove = new GuiSettingBoolean(new SettingValue<Boolean>(settings.isRemove())),
                new int[] { 1, 7 });

        this.addComponent(new GuiLabel(Translation.FILTER_AUDIO_NOTIFY.translate()), new int[] { 2,
                11 });
        this.addComponent(
                chkSound = new GuiSettingBoolean(new SettingValue<Boolean>(settings
                        .isSoundNotification())), new int[] { 1, 11 });
        this.addComponent(
                txtSound = new GuiSettingString(new SettingValue<String>(settings.getSoundName())),
                new int[] { 12, 11, 6, 1 }); // TODO presets

        this.addComponent(new GuiLabel(Translation.FILTER_EXPRESSION.translate()),
                new int[] { 1, 13 });
        this.addComponent(txtPattern = new GuiSettingString(new SettingValue<String>(
                pattern == null ? "" : pattern.toString())), new int[] { 8, 13, 12, 1 });
        this.addComponent(lblError = new GuiLabel(""), new int[] { 6, 14 });

        GuiButton accept = new GuiButton(I18n.format("gui.done"));
        accept.addActionListener(new ActionPerformed() {
            @Override
            public void action(GuiEvent event) {
                accept();
            }
        });
        this.addComponent(accept, new int[] { 1, 14, 4, 1 });

        GuiButton cancel = new GuiButton(I18n.format("gui.cancel"));
        cancel.addActionListener(new ActionPerformed() {
            @Override
            public void action(GuiEvent event) {
                cancel();
            }
        });
        this.addComponent(cancel, new int[] { 1, 15, 4, 1 });
    }

    private String merge(Set<String> set) {
        StringBuilder sb = new StringBuilder();
        for (String s : set) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(s);
        }
        return sb.toString();
    }

    private Set<String> split(String s) {
        Set<String> set = new HashSet<String>();
        String[] split = s.split(",");
        for (String sp : split) {
            sp = sp.trim();
            if (!sp.isEmpty()) {
                set.add(sp);
            }
        }
        return set;
    }

    private void accept() {
        filter.setName(txtName.getValue());
        filter.setPattern(txtPattern.getValue());
        FilterSettings sett = filter.getSettings();
        sett.getChannels().addAll(split(txtDestinations.getValue()));
        sett.setRemove(chkRemove.getValue());

        sett.setSoundNotification(chkSound.getValue());
        sett.setSoundName(txtSound.getValue());

        consumer.apply(filter);
        cancel();
    }

    private void cancel() {
        getParent().setOverlay(null);
    }

    @Override
    public void accept(GuiKeyboardEvent event) {
        if (txtPattern.isFocused()) {
            // check valid regex
            try {
                Pattern.compile(txtPattern.getValue());
                txtPattern.setForeColor(-1);
                lblError.setString("");
            } catch (PatternSyntaxException e) {
                txtPattern.setForeColor(0xffff0000);
                String string = e.getLocalizedMessage();
                lblError.setString(string);
            }
        }
    }
}
