package mnm.mods.tabbychat.client.gui.settings;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import mnm.mods.tabbychat.TCMarkers;
import mnm.mods.tabbychat.client.AbstractChannel;
import mnm.mods.tabbychat.client.ChatManager;
import mnm.mods.tabbychat.client.DefaultChannel;
import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.util.Color;
import mnm.mods.tabbychat.util.ILocation;
import mnm.mods.tabbychat.util.Location;
import mnm.mods.tabbychat.client.gui.component.layout.BorderLayout;
import mnm.mods.tabbychat.client.gui.component.ComponentScreen;
import mnm.mods.tabbychat.client.gui.component.GuiButton;
import mnm.mods.tabbychat.client.gui.component.GuiComponent;
import mnm.mods.tabbychat.client.gui.component.GuiPanel;
import mnm.mods.tabbychat.client.gui.component.layout.VerticalLayout;
import mnm.mods.tabbychat.client.gui.component.config.SettingPanel;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class GuiSettingsScreen extends ComponentScreen {

    private static Map<Class<? extends SettingPanel<?>>, Supplier<? extends SettingPanel<?>>> settings = Maps.newLinkedHashMap();

    static {
        registerSetting(GuiSettingsGeneral.class, GuiSettingsGeneral::new);
        registerSetting(GuiSettingsServer.class, GuiSettingsServer::new);
        registerSetting(GuiSettingsChannel.class, GuiSettingsChannel::new);
        registerSetting(GuiAdvancedSettings.class, GuiAdvancedSettings::new);
    }

    private List<SettingPanel<?>> panels = Lists.newArrayList();

    private GuiPanel panel;

    private GuiPanel settingsList;
    private SettingPanel<?> selectedSetting;

    public GuiSettingsScreen(@Nullable Channel channel) {
        super(new StringTextComponent("Settings"));
        if (channel != DefaultChannel.INSTANCE) {
            selectedSetting = new GuiSettingsChannel((AbstractChannel) channel);
        }

        for (Map.Entry<Class<? extends SettingPanel<?>>, Supplier<? extends SettingPanel<?>>> sett : settings.entrySet()) {
            try {
                if (selectedSetting != null && selectedSetting.getClass() == sett.getKey()) {
                    panels.add(selectedSetting);
                } else {
                    panels.add(sett.getValue().get());
                }
            } catch (Exception e) {
                TabbyChat.logger.error(TCMarkers.CONFIG, "Unable to add {} as a setting.", sett.getKey(), e);
            }
        }
    }

    @Override
    public void init() {

        getPanel().add(panel = new GuiPanel(new BorderLayout()));

        int x = this.width / 2 - 300 / 2;
        int y = this.height / 2 - 200 / 2;
        panel.setLocation(new Location(x, y, 300, 200));

        GuiPanel panel = new GuiPanel(new BorderLayout());
        this.panel.add(panel, BorderLayout.Position.WEST);
        panel.add(settingsList = new GuiPanel(new VerticalLayout()), BorderLayout.Position.WEST);

        GuiButton close = new GuiButton("Close") {
            @Override
            public void onClick(double mouseX, double mouseY) {
                mc.displayGuiScreen(null);
            }
        };
        close.setLocation(new Location(0, 0, 40, 10));
        close.setSecondaryColor(Color.of(0, 255, 0, 127));
        panel.add(close, BorderLayout.Position.SOUTH);

        {
            // Populate the settings
            for (SettingPanel<?> sett : panels) {
                SettingsButton button = new SettingsButton(sett) {
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        selectSetting(getSettings());
                    }
                };
                settingsList.add(button);
                sett.initGUI();
            }
        }
        SettingPanel<?> panelClass;
        if (selectedSetting == null) {
            panelClass = panels.get(0);
        } else {
            panelClass = selectedSetting;
        }
        selectSetting(panelClass);
    }

    @Override
    public void onClose() {
        super.onClose();
        for (SettingPanel<?> settingPanel : panels) {
            settingPanel.getSettings().save();
        }
        ChatManager.instance().markDirty(null);
    }

    @Override
    public void init(Minecraft mc, int width, int height) {
        this.panels.forEach(GuiPanel::clear);
        super.init(mc, width, height);
    }

    private void deactivateAll() {
        for (GuiComponent comp : settingsList.children()) {
            if (comp instanceof SettingsButton) {
                ((SettingsButton) comp).setActive(false);
            }
        }
    }

    private <T extends SettingPanel<?>> void activate(Class<T> settingClass) {
        for (GuiComponent comp : settingsList.children()) {
            if (comp instanceof SettingsButton
                    && ((SettingsButton) comp).getSettings().getClass().equals(settingClass)) {
                ((SettingsButton) comp).setActive(true);
                break;
            }
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float tick) {
        // drawDefaultBackground();
        ILocation rect = panel.getLocation();
        fill(rect.getXPos(), rect.getYPos(), rect.getXWidth(), rect.getYHeight(), Integer.MIN_VALUE);
        super.render(mouseX, mouseY, tick);
    }

    private void selectSetting(SettingPanel<?> setting) {
//        setting.clearComponents();
        deactivateAll();
        panel.remove(selectedSetting);
        selectedSetting = setting;
        activate(setting.getClass());
        this.panel.add(this.selectedSetting, BorderLayout.Position.CENTER);
    }

    private static <T extends SettingPanel<?>> void registerSetting(Class<T> settings, Supplier<T> constructor) {
        if (!GuiSettingsScreen.settings.containsKey(settings)) {
            GuiSettingsScreen.settings.put(settings, constructor);
        }
    }

}
