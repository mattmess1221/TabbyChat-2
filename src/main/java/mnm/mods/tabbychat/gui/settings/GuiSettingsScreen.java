package mnm.mods.tabbychat.gui.settings;

import java.awt.Rectangle;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.eventbus.Subscribe;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.util.Color;
import mnm.mods.util.config.SettingsFile;
import mnm.mods.util.gui.BorderLayout;
import mnm.mods.util.gui.ComponentScreen;
import mnm.mods.util.gui.FlowLayout;
import mnm.mods.util.gui.GuiButton;
import mnm.mods.util.gui.GuiComponent;
import mnm.mods.util.gui.GuiPanel;
import mnm.mods.util.gui.VerticalLayout;
import mnm.mods.util.gui.config.SettingPanel;
import mnm.mods.util.gui.events.ActionPerformedEvent;
import mnm.mods.util.gui.events.GuiEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

public class GuiSettingsScreen extends ComponentScreen {

    private static List<Class<? extends SettingPanel<?>>> settings = Lists.newArrayList();

    static {
        registerSetting(GuiSettingsGeneral.class);
        registerSetting(GuiSettingsServer.class);
        registerSetting(GuiSettingsChannel.class);
        registerSetting(GuiSettingsColors.class);
    }

    private List<SettingPanel<?>> panels = Lists.newArrayList();

    private GuiPanel panel;
    private GuiPanel settingsList;
    private GuiPanel closeSaveButtons;
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

        getPanel().addComponent(panel = new GuiPanel());
        panel.setLayout(new BorderLayout());
        panel.setSize(300, 200);
        // redundant casting for reobfuscation
        panel.setPosition(((GuiScreen) this).width / 2 - panel.getBounds().width / 2, ((GuiScreen) this).height / 2 - panel.getBounds().height / 2);
        GuiPanel panel = new GuiPanel(new BorderLayout());
        this.panel.addComponent(panel, BorderLayout.Position.WEST);
        panel.addComponent(settingsList = new GuiPanel(new VerticalLayout()), BorderLayout.Position.WEST);
        panel.addComponent(closeSaveButtons = new GuiPanel(new FlowLayout()), BorderLayout.Position.SOUTH);
        GuiButton save = new GuiButton("Save");
        save.setSize(40, 10);
        save.setBackColor(Color.getColor(0, 255, 0, 127));
        save.getBus().register(new Object() {
            @Subscribe
            public void applyAllTheSettingsAndCloseTheScreen(ActionPerformedEvent event) {
                Set<SettingsFile> files = Sets.newHashSet();
                for (SettingPanel<?> sett : panels) {
                    sett.saveSettings();
                    files.add(sett.getSettings());
                }
                Minecraft.getMinecraft().displayGuiScreen(null);
            }
        });
        closeSaveButtons.addComponent(save);
        GuiButton close = new GuiButton("Close");
        close.setSize(40, 10);
        close.setBackColor(Color.getColor(0, 255, 0, 127));
        close.getBus().register(new Object() {
            @Subscribe
            public void closeTheScreenWithoutSaving(ActionPerformedEvent event) {
                Minecraft.getMinecraft().displayGuiScreen(null);
            }
        });
        closeSaveButtons.addComponent(close);

        {
            // Populate the settings
            for (SettingPanel<?> sett : panels) {
                SettingsButton button = new SettingsButton(sett);
                button.getBus().register(new Object() {
                    @Subscribe
                    public void switchToThisPanel(GuiEvent event) {
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
        Rectangle rect = panel.getBounds();
        Gui.drawRect(rect.x, rect.y, rect.x + rect.width, rect.y + rect.height, Integer.MIN_VALUE);
        super.drawScreen(mouseX, mouseY, tick);
    }

    public void selectSetting(SettingPanel<?> setting) {
        // setting.clearComponents();
        deactivateAll();
        panel.removeComponent(selectedSetting);
        selectedSetting = setting;
        selectedSetting.clearComponents();
        selectedSetting.initGUI();
        activate(setting.getClass());
        panel.addComponent(selectedSetting, BorderLayout.Position.CENTER);
    }

    public static void registerSetting(Class<? extends SettingPanel<?>> settings) {
        if (!GuiSettingsScreen.settings.contains(settings)) {
            GuiSettingsScreen.settings.add(settings);
        }
    }

}
