package mnm.mods.tabbychat.gui.settings;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.filters.Filter;
import mnm.mods.tabbychat.extra.filters.ChatFilter;
import mnm.mods.tabbychat.extra.filters.GuiFilterEditor;
import mnm.mods.tabbychat.settings.GeneralServerSettings;
import mnm.mods.tabbychat.settings.ServerSettings;
import mnm.mods.tabbychat.util.ChannelPatterns;
import mnm.mods.tabbychat.util.MessagePatterns;
import mnm.mods.tabbychat.util.Translation;
import mnm.mods.util.Color;
import mnm.mods.util.Consumer;
import mnm.mods.util.gui.GuiButton;
import mnm.mods.util.gui.GuiGridLayout;
import mnm.mods.util.gui.GuiLabel;
import mnm.mods.util.gui.config.GuiSettingBoolean;
import mnm.mods.util.gui.config.GuiSettingEnum;
import mnm.mods.util.gui.config.GuiSettingString;
import mnm.mods.util.gui.config.SettingPanel;
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
        this.setLayout(new GuiGridLayout(10, 20));
        this.setDisplayString(Translation.SETTINGS_SERVER.toString());
        this.setBackColor(new Color(255, 215, 0, 64).getColor());
    }

    @Override
    public void initGUI() {
        GeneralServerSettings sett = getSettings().general;
        index = getSettings().filters.getValue().size() - 1;

        int pos = 1;
        this.addComponent(new GuiLabel(Translation.CHANNELS_ENABLED.toString()), new int[] { 2, pos });
        GuiSettingBoolean chkChannels = new GuiSettingBoolean(sett.channelsEnabled);
        chkChannels.setCaption(Translation.CHANNELS_ENABLED_DESC.toString());
        this.addComponent(chkChannels, new int[] { 1, pos });

        pos += 1;
        this.addComponent(new GuiLabel(Translation.PM_ENABLED.toString()), new int[] { 2, pos });
        GuiSettingBoolean chkPM = new GuiSettingBoolean(sett.pmEnabled);
        chkPM.setCaption(Translation.PM_ENABLED_DESC.toString());
        this.addComponent(chkPM, new int[] { 1, pos });

        pos += 1;
        addComponent(new GuiLabel(Translation.USE_DEFAULT.toString()), new int[] { 2, pos });
        addComponent(new GuiSettingBoolean(sett.useDefaultTab), new int[] { 1, pos });

        pos += 2;
        this.addComponent(new GuiLabel(Translation.CHANNEL_PATTERN.toString()), new int[] { 1, pos });
        GuiSettingEnum<ChannelPatterns> enmChanPat = new GuiSettingEnum<ChannelPatterns>(sett.channelPattern,
                ChannelPatterns.values());
        enmChanPat.setCaption(Translation.CHANNEL_PATTERN_DESC.toString());
        this.addComponent(enmChanPat, new int[] { 5, pos, 4, 1 });

        pos += 2;
        this.addComponent(new GuiLabel(Translation.MESSAGE_PATTERN.toString()), new int[] { 1, pos });
        if (sett.messegePattern.getValue() == null) {
            sett.messegePattern.setValue(MessagePatterns.WHISPERS);
        }
        GuiSettingEnum<MessagePatterns> enmMsg = new GuiSettingEnum<MessagePatterns>(sett.messegePattern,
                MessagePatterns.values());
        enmMsg.setCaption(Translation.MESSAGE_PATTERN_DESC.toString());
        this.addComponent(enmMsg, new int[] { 5, pos, 4, 1 });

        pos += 2;
        this.addComponent(new GuiLabel(Translation.IGNORED_CHANNELS.toString()), new int[] { 0, pos });
        GuiSettingString strIgnored = new GuiSettingString(sett.ignoredChannels);
        strIgnored.setCaption(Translation.IGNORED_CHANNELS_DESC.toString());
        this.addComponent(strIgnored, new int[] { 5, pos, 5, 1 });

        pos += 2;
        this.addComponent(new GuiLabel(Translation.DEFAULT_CHANNELS.toString()), new int[] { 0, pos });
        GuiSettingString strDefaults = new GuiSettingString(sett.defaultChannels);
        strDefaults.setCaption(Translation.DEFAULT_CHANNELS_DESC.toString());
        this.addComponent(strDefaults, new int[] { 5, pos, 5, 1 });

        // Filters
        pos += 2;
        this.addComponent(new GuiLabel(Translation.FILTERS.toString()), new int[] { 4, pos, 1, 2 });

        pos += 2;
        prev = new GuiButton("<");
        prev.addActionListener(new ActionPerformed() {
            @Override
            public void action(GuiEvent event) {
                select(index - 1);
            }
        });
        this.addComponent(prev, new int[] { 0, pos, 1, 2 });

        edit = new GuiButton(I18n.format("selectServer.edit"));
        edit.addActionListener(new ActionPerformed() {
            @Override
            public void action(GuiEvent event) {
                edit(index);
            }
        });
        this.addComponent(edit, new int[] { 1, pos, 2, 2 });

        next = new GuiButton(">");
        next.addActionListener(new ActionPerformed() {
            @Override
            public void action(GuiEvent event) {
                select(index + 1);
            }
        });
        this.addComponent(next, new int[] { 3, pos, 1, 2 });

        this.addComponent(lblFilter = new GuiLabel(""), new int[] { 5, pos, 1, 3 });
        GuiButton _new = new GuiButton(Translation.FILTERS_NEW.toString());
        _new.addActionListener(new ActionPerformed() {
            @Override
            public void action(GuiEvent event) {
                add();
            }
        });

        pos += 2;
        this.addComponent(_new, new int[] { 0, pos, 2, 2 });
        delete = new GuiButton(I18n.format("selectServer.delete"));
        delete.addActionListener(new ActionPerformed() {
            @Override
            public void action(GuiEvent event) {
                delete(index);
            }
        });
        this.addComponent(delete, new int[] { 2, pos, 2, 2 });
        prev.setEnabled(false);
        if (index == -1) {
            delete.setEnabled(false);
            edit.setEnabled(false);
            next.setEnabled(false);
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
        getSettings().filters.remove(i);
        update();
    }

    private void add() {
        // creates a new filter, adds it to the list, and selects it.
        getSettings().filters.add(new ChatFilter());
        select(getSettings().filters.getValue().size() - 1);
        update();
    }

    private void update() {
        this.next.setEnabled(true);
        this.prev.setEnabled(true);
        this.edit.setEnabled(true);
        this.delete.setEnabled(true);

        int size = getSettings().filters.getValue().size();

        if (index >= size - 1) {
            this.next.setEnabled(false);
            index = size - 1;
        }
        if (index < 1) {
            this.prev.setEnabled(false);
            index = 0;
        }
        if (size < 1) {
            this.edit.setEnabled(false);
            this.delete.setEnabled(false);
            this.index = 0;
        } else {
            Filter filter = getSettings().filters.get(index);
            this.lblFilter.setString(filter.getName());
        }
    }

    private void edit(int i) {
        Filter filter = getSettings().filters.get(i);
        setOverlay(new GuiFilterEditor(filter, this));
    }
}
