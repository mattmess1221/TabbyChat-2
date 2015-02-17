package mnm.mods.tabbychat.gui.settings;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.filters.Filter;
import mnm.mods.tabbychat.filters.ChatFilter;
import mnm.mods.tabbychat.filters.GuiFilterEditor;
import mnm.mods.tabbychat.settings.ServerSettings;
import mnm.mods.tabbychat.util.ChannelPatterns;
import mnm.mods.tabbychat.util.MessagePatterns;
import mnm.mods.tabbychat.util.Translation;
import mnm.mods.util.Color;
import mnm.mods.util.Consumer;
import mnm.mods.util.gui.GuiButton;
import mnm.mods.util.gui.GuiGridLayout;
import mnm.mods.util.gui.GuiLabel;
import mnm.mods.util.gui.GuiSettingBoolean;
import mnm.mods.util.gui.GuiSettingEnum;
import mnm.mods.util.gui.GuiSettingString;
import mnm.mods.util.gui.SettingPanel;
import mnm.mods.util.gui.events.ActionPerformed;
import mnm.mods.util.gui.events.GuiEvent;
import net.minecraft.client.resources.I18n;

public class GuiSettingsServer extends SettingPanel<ServerSettings> implements Consumer<Filter> {

    private int index = 0;

    private GuiButton prev;
    private GuiButton edit;
    private GuiButton next;
    private GuiButton delete;
    private GuiLabel lblFilter;

    public GuiSettingsServer() {
        this.setLayout(new GuiGridLayout(10, 16));
        this.setDisplayString(Translation.SETTINGS_SERVER.translate());
        this.setBackColor(new Color(255, 215, 0, 64).getColor());
    }

    @Override
    public void initGUI() {
        ServerSettings sett = getSettings();
        index = sett.filters.getValue().size() - 1;
        this.addComponent(new GuiSettingBoolean(sett.channelsEnabled,
                Translation.SERVER_CHANNELS_ENABLED.translate()), new int[] { 1, 1 });
        this.addComponent(
                new GuiSettingBoolean(sett.pmEnabled, Translation.SERVER_PM_ENABLED.translate()),
                new int[] { 1, 2 });
        this.addComponent(new GuiLabel(Translation.SERVER_CHANNEL_PATTERN + ""), new int[] { 1, 4 });
        this.addComponent(
                new GuiSettingEnum<ChannelPatterns>(sett.channelPattern, ChannelPatterns.values()),
                new int[] { 5, 4, 4, 1 });
        this.addComponent(new GuiLabel(Translation.SERVER_MESSAGE_PATTERN + ""), new int[] { 1, 6 });
        this.addComponent(
                new GuiSettingEnum<MessagePatterns>(sett.messegePattern, MessagePatterns.values()),
                new int[] { 5, 6, 4, 1 });
        this.addComponent(new GuiLabel(Translation.SERVER_IGNORED_CHANNELS.translate()),
                new int[] { 0, 9 });
        this.addComponent(new GuiSettingString(sett.ignoredChannels), new int[] { 5, 9, 5, 1 });
        this.addComponent(new GuiLabel(Translation.SERVER_DEFAULT_CHANNELS.translate()),
                new int[] { 0, 11 });
        this.addComponent(new GuiSettingString(sett.defaultChannels), new int[] { 5, 11, 5, 1 });
        // Filters
        this.addComponent(new GuiLabel(Translation.SERVER_FILTERS.translate()), new int[] { 0, 12,
                1, 2 });
        prev = new GuiButton("<");
        prev.addActionListener(new ActionPerformed() {
            @Override
            public void action(GuiEvent event) {
                select(index - 1);
            }
        });
        this.addComponent(prev, new int[] { 0, 14, 1, 2 });
        edit = new GuiButton(I18n.format("selectServer.edit"));
        edit.addActionListener(new ActionPerformed() {
            @Override
            public void action(GuiEvent event) {
                edit(index);
            }
        });
        this.addComponent(edit, new int[] { 1, 14, 2, 2 });
        next = new GuiButton(">");
        next.addActionListener(new ActionPerformed() {
            @Override
            public void action(GuiEvent event) {
                select(index + 1);
            }
        });
        this.addComponent(next, new int[] { 3, 14, 1, 2 });
        this.addComponent(lblFilter = new GuiLabel(""), new int[] { 4, 14 });
        GuiButton _new = new GuiButton(Translation.SERVER_FILTERS_NEW.translate());
        _new.addActionListener(new ActionPerformed() {
            @Override
            public void action(GuiEvent event) {
                add();
            }
        });
        this.addComponent(_new, new int[] { 8, 14, 2, 1 });
        delete = new GuiButton(I18n.format("selectServer.delete"));
        delete.addActionListener(new ActionPerformed() {
            @Override
            public void action(GuiEvent event) {
                delete(index);
            }
        });
        this.addComponent(delete, new int[] { 8, 15, 2, 1 });
        prev.enabled = false;
        if (index == -1) {
            delete.enabled = false;
            edit.enabled = false;
            next.enabled = false;
        }

        update();
    }

    @Override
    public ServerSettings getSettings() {
        return TabbyChat.getInstance().serverSettings;
    }

    // Filters

    @Override
    public void apply(Filter f) {
        this.lblFilter.setString(f.getName());
    }

    private void select(int i) {
        this.index = i;
        update();
    }

    private void delete(int i) {
        // deletes a filter
        getSettings().filters.getValue().remove(i);
        update();
    }

    private void add() {
        // creates a new filter, adds it to the list, and selects it.
        getSettings().filters.getValue().add(new ChatFilter());
        select(getSettings().filters.getValue().size() - 1);
        update();
    }

    private void update() {
        this.next.enabled = true;
        this.prev.enabled = true;
        this.edit.enabled = true;
        this.delete.enabled = true;

        int size = getSettings().filters.getValue().size();

        if (index >= size - 1) {
            this.next.enabled = false;
            index = size - 1;
        }
        if (index < 1) {
            this.prev.enabled = false;
            index = 0;
        }
        if (size < 1) {
            this.edit.enabled = false;
            this.delete.enabled = false;
            this.index = 0;
        } else {
            Filter filter = getSettings().filters.getValue().get(index);
            this.lblFilter.setString(filter.getName());
        }
    }

    private void edit(int i) {
        Filter filter = getSettings().filters.getValue().get(i);
        setOverlay(new GuiFilterEditor(filter, this));
    }
}
