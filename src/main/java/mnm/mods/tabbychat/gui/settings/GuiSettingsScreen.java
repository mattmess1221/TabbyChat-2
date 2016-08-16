package mnm.mods.tabbychat.gui.settings;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.mumfrey.liteloader.core.LiteLoader;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.util.Color;
import mnm.mods.util.ILocation;
import mnm.mods.util.Location;
import mnm.mods.util.config.SettingsFile;
import mnm.mods.util.gui.BorderLayout;
import mnm.mods.util.gui.ComponentScreen;
import mnm.mods.util.gui.GuiButton;
import mnm.mods.util.gui.GuiComponent;
import mnm.mods.util.gui.GuiPanel;
import mnm.mods.util.gui.VerticalLayout;
import mnm.mods.util.gui.config.SettingPanel;
import mnm.mods.util.gui.events.ActionPerformedEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class GuiSettingsScreen extends ComponentScreen {

    private static List<Class<? extends SettingPanel<?>>> settings = Lists.newArrayList();

    static {
        registerSetting(GuiSettingsGeneral.class);
        registerSetting(GuiSettingsServer.class);
        registerSetting(GuiSettingsChannel.class);
        registerSetting(GuiSettingsColors.class);
        registerSetting(GuiAdvancedSettings.class);
    }

    private List<SettingPanel<?>> panels = Lists.newArrayList();

    private GuiPanel panel;

    private GuiPanel settingsList;
    private SettingPanel<?> selectedSetting;

    public GuiSettingsScreen(SettingPanel<?> setting) {
        this.selectedSetting = setting;

        for (Class<? extends SettingPanel<?>> sett : settings) {
            try {
                if (setting != null && setting.getClass() == sett) {
                    panels.add(setting);
                } else {
                    panels.add(sett.newInstance());
                }
            } catch (Exception e) {
                TabbyChat.getLogger().error("Unable to add " + sett.getName() + " as a setting.", e);
            }
        }
    }

    @Override
    public void initGui() {

        getPanel().addComponent(panel = new GuiPanel(new BorderLayout()));

        int x = this.width / 2 - 300 / 2;
        int y = this.height / 2 - 200 / 2;
        panel.setLocation(new Location(x, y, 300, 200));

        GuiPanel panel = new GuiPanel(new BorderLayout());
        this.panel.addComponent(panel, BorderLayout.Position.WEST);
        panel.addComponent(settingsList = new GuiPanel(new VerticalLayout()), BorderLayout.Position.WEST);

        GuiButton close = new GuiButton("Close");
        close.setLocation(new Location(0, 0, 40, 10));
        close.setSecondaryColor(Color.of(0, 255, 0, 127));
        close.getBus().register(new Object() {
            @Subscribe
            public void closeTheScreen(ActionPerformedEvent event) {
                Minecraft.getMinecraft().displayGuiScreen(null);
            }
        });
        panel.addComponent(close, BorderLayout.Position.SOUTH);

        {
            // Populate the settings
            for (SettingPanel<?> sett : panels) {
                SettingsButton button = new SettingsButton(sett);
                button.getBus().register(new Object() {
                    @Subscribe
                    public void switchToThisPanel(ActionPerformedEvent event) {
                        selectSetting(((SettingsButton) event.getComponent()).getSettings());
                    }
                });
                settingsList.addComponent(button);
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
    public void onGuiClosed() {
        super.onGuiClosed();
        for (SettingPanel<?> settingPanel : panels) {
            SettingsFile config = settingPanel.getSettings();
            LiteLoader.getInstance().writeConfig(config);
        }
    }

    private void deactivateAll() {
        for (GuiComponent comp : settingsList) {
            if (comp instanceof SettingsButton) {
                ((SettingsButton) comp).setActive(false);
            }
        }
    }

    private <T extends SettingPanel<?>> void activate(Class<T> settingClass) {
        for (GuiComponent comp : settingsList) {
            if (comp instanceof SettingsButton
                    && ((SettingsButton) comp).getSettings().getClass().equals(settingClass)) {
                ((SettingsButton) comp).setActive(true);
                break;
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float tick) {
        // drawDefaultBackground();
        ILocation rect = panel.getLocation();
        Gui.drawRect(rect.getXPos(), rect.getYPos(), rect.getXWidth(), rect.getYHeight(), Integer.MIN_VALUE);
        super.drawScreen(mouseX, mouseY, tick);
    }

    public void selectSetting(SettingPanel<?> setting) {
//        setting.clearComponents();
        deactivateAll();
        panel.removeComponent(selectedSetting);
        selectedSetting = setting;
        selectedSetting.clearComponents();
        selectedSetting.initGUI();
        activate(setting.getClass());
        this.panel.addComponent(this.selectedSetting, BorderLayout.Position.CENTER);
    }

    public static void registerSetting(Class<? extends SettingPanel<?>> settings) {
        if (!GuiSettingsScreen.settings.contains(settings)) {
            GuiSettingsScreen.settings.add(settings);
        }
    }

}
