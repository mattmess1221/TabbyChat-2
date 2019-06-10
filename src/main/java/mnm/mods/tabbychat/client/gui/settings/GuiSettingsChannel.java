package mnm.mods.tabbychat.client.gui.settings;

import static mnm.mods.tabbychat.util.Translation.*;

import mnm.mods.tabbychat.client.AbstractChannel;
import mnm.mods.tabbychat.client.ChatChannel;
import mnm.mods.tabbychat.client.TabbyChatClient;
import mnm.mods.tabbychat.client.core.GuiNewChatTC;
import mnm.mods.tabbychat.client.settings.ServerSettings;
import mnm.mods.tabbychat.util.Color;
import mnm.mods.tabbychat.util.Location;
import mnm.mods.tabbychat.client.gui.component.BorderLayout;
import mnm.mods.tabbychat.client.gui.component.GuiButton;
import mnm.mods.tabbychat.client.gui.component.GuiCheckbox;
import mnm.mods.tabbychat.client.gui.component.GuiComponent;
import mnm.mods.tabbychat.client.gui.component.GuiGridLayout;
import mnm.mods.tabbychat.client.gui.component.GuiLabel;
import mnm.mods.tabbychat.client.gui.component.GuiPanel;
import mnm.mods.tabbychat.client.gui.component.GuiScrollingPanel;
import mnm.mods.tabbychat.client.gui.component.GuiText;
import mnm.mods.tabbychat.client.gui.component.VerticalLayout;
import mnm.mods.tabbychat.client.gui.component.config.SettingPanel;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TranslationTextComponent;

public class GuiSettingsChannel extends SettingPanel<ServerSettings> {

    private AbstractChannel channel;

    private GuiScrollingPanel channels;
    private GuiPanel panel;

    private GuiText alias;
    private GuiText prefix;
    private GuiCheckbox hidePrefix;
    private GuiText command;

    GuiSettingsChannel() {
        this(null);
    }

    public GuiSettingsChannel(AbstractChannel channel) {
        this.channel = channel;
        this.setLayout(new BorderLayout());
        this.setDisplayString(I18n.format(CHANNEL_TITLE));
        this.setSecondaryColor(Color.of(0, 15, 100, 65));

    }

    @Override
    public void initGUI() {
        channels = new GuiScrollingPanel();
        channels.setLocation(new Location(0, 0, 60, 200));
        channels.getContentPanel().setLayout(new VerticalLayout());
        for (ChatChannel channel : getSettings().channels.get().values()) {
            channels.getContentPanel().addComponent(new ChannelButton(channel));
        }
        this.addComponent(channels, BorderLayout.Position.WEST);
        panel = new GuiPanel();
        panel.setLayout(new GuiGridLayout(8, 20));
        this.addComponent(panel, BorderLayout.Position.CENTER);

        this.select(channel);
    }

    private void select(AbstractChannel channel) {

        for (GuiComponent comp : channels.getContentPanel().children()) {
            if (((ChannelButton) comp).channel == channel) {
                comp.setEnabled(false);
            } else {
                comp.setEnabled(true);
            }
        }

        int pos = 1;

        this.channel = channel;
        this.panel.clearComponents();
        if (channel == null) {
            if (channels.getContentPanel().getComponentCount() > 0) {
                this.panel.addComponent(new GuiLabel(new TranslationTextComponent(CHANNEL_SELECT)), new int[] { 1, pos });
            } else {
                this.panel.addComponent(new GuiLabel(new TranslationTextComponent(CHANNEL_NONE)), new int[] { 1, pos });
            }
            return;
        }
        this.panel.addComponent(
                new GuiLabel(new TranslationTextComponent(CHANNEL_LABEL, channel.getName())),
                new int[] { 1, pos });

        pos += 3;
        this.panel.addComponent(new GuiLabel(new TranslationTextComponent(CHANNEL_ALIAS)), new int[] { 1, pos });
        this.panel.addComponent(alias = new GuiText(), new int[] { 3, pos, 4, 1 });
        alias.setValue(channel.getAlias());

        pos += 2;
        this.panel.addComponent(new GuiLabel(new TranslationTextComponent(CHANNEL_PREFIX)), new int[] { 1, pos });
        this.panel.addComponent(prefix = new GuiText(), new int[] { 3, pos, 4, 1 });
        prefix.setValue(channel.getPrefix());

        pos += 2;
        this.panel.addComponent(hidePrefix = new GuiCheckbox(), new int[] { 1, pos });
        hidePrefix.setValue(channel.isPrefixHidden());
        this.panel.addComponent(new GuiLabel(new TranslationTextComponent(CHANNEL_HIDE_PREFIX)), new int[] { 2, pos });

        pos += 2;
        this.panel.addComponent(command = new GuiText(), new int[] { 3, pos, 4, 1 });
        command.setValue(channel.getCommand());
        this.panel.addComponent(new GuiLabel(new TranslationTextComponent(CHANNEL_COMMAND)), new int[] { 1, pos });

        GuiButton accept = new GuiButton(I18n.format("gui.done")){
            @Override
            public void onClick(double mouseX, double mouseY) {
                save();
            }
        };
        this.panel.addComponent(accept, new int[] { 2, 15, 4, 2 });

        GuiButton forget = new GuiButton(I18n.format(CHANNEL_FORGET)){
            @Override
            public void onClick(double mouseX, double mouseY) {

                AbstractChannel channel = GuiSettingsChannel.this.channel;
                // remove from chat
                GuiNewChatTC.getInstance().getChatBox().removeChannel(channel);
                // remove from settings file
                getSettings().channels.get().remove(channel.getName());
                // don't add this channel again.
                getSettings().general.ignoredChannels.add(channel.toString());
                // remove from settings gui
                for (GuiComponent comp : channels.getContentPanel().children()) {
                    if (comp instanceof ChannelButton && ((ChannelButton) comp).channel == channel) {
                        channels.getContentPanel().removeComponent(comp);
                        break;
                    }
                }
                select(null);
            }
        };
        this.panel.addComponent(forget, new int[] { 2, 17, 4, 2 });
    }

    private void save() {
        channel.setAlias(alias.getValue());
        channel.setPrefix(prefix.getValue());
        channel.setPrefixHidden(hidePrefix.getValue());
        channel.setCommand(command.getValue());
    }

    @Override
    public ServerSettings getSettings() {
        return TabbyChatClient.getInstance().getServerSettings();
    }

    public class ChannelButton extends GuiButton {

        private ChatChannel channel;

        ChannelButton(ChatChannel channel) {
            super(channel.getName());
            this.channel = channel;
            setLocation(new Location(0, 0, 60, 15));
        }

        @Override
        public void onClick(double mouseX, double mouseY) {
            select(channel);
        }
    }
}
