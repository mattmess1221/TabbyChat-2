package mnm.mods.tabbychat.gui.settings;

import java.awt.Rectangle;
import java.util.List;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.util.Color;
import mnm.mods.util.gui.BorderLayout;
import mnm.mods.util.gui.ComponentScreen;
import mnm.mods.util.gui.FlowLayout;
import mnm.mods.util.gui.GuiButton;
import mnm.mods.util.gui.GuiComponent;
import mnm.mods.util.gui.GuiPanel;
import mnm.mods.util.gui.SettingPanel;
import mnm.mods.util.gui.VerticalLayout;
import mnm.mods.util.gui.events.ActionPerformed;
import mnm.mods.util.gui.events.GuiEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

import com.google.common.collect.Lists;

@SuppressWarnings("rawtypes")
public class GuiSettingsScreen extends ComponentScreen {

    private static List<Class<? extends SettingPanel>> settings = Lists.newArrayList();

    static {
        registerSetting(GuiSettingsGeneral.class);
        registerSetting(GuiSettingsServer.class);
        registerSetting(GuiSettingsChannel.class);
        registerSetting(GuiSettingsColors.class);
    }

    private GuiPanel panel;
    private GuiPanel settingsList;
    private GuiPanel closeSaveButtons;
    private SettingPanel selectedSetting;

    @Override
    public void initGui() {
        getPanel().addComponent(panel = new GuiPanel());
        panel.setLayout(new BorderLayout());
        panel.setSize(300, 200);
        // redundant casting for reobfuscation
        panel.setPosition(((GuiScreen) this).width / 2 - panel.getBounds().width / 2, ((GuiScreen) this).height / 2
                - panel.getBounds().height / 2);
        panel.addComponent(settingsList = new GuiPanel(new VerticalLayout()),
                BorderLayout.Position.WEST);
        panel.addComponent(closeSaveButtons = new GuiPanel(new FlowLayout()),
                BorderLayout.Position.SOUTH);
        GuiButton save = new GuiButton("Save");
        save.setSize(40, 10);
        save.setBackColor(Color.getColor(0, 255, 0, 127));
        save.addActionListener(new ActionPerformed() {
            @Override
            public void action(GuiEvent event) {
                selectedSetting.saveSettings();
                selectedSetting.getSettings().saveSettingsFile();
            }
        });
        closeSaveButtons.addComponent(save);
        GuiButton close = new GuiButton("Close");
        close.setSize(40, 10);
        close.setBackColor(Color.getColor(0, 255, 0, 127));
        close.addActionListener(new ActionPerformed() {
            @Override
            public void action(GuiEvent event) {
                Minecraft.getMinecraft().displayGuiScreen(null);
            }
        });
        closeSaveButtons.addComponent(close);

        {
            // Populate the settings
            for (Class<? extends SettingPanel> sett : settings) {
                try {
                    SettingsButton button = new SettingsButton(sett.newInstance());
                    button.addActionListener(new ActionPerformed() {
                        @Override
                        public void action(GuiEvent event) {
                            selectSetting(((SettingsButton) event.component).getSettings()
                                    .getClass(), true);
                        }
                    });
                    settingsList.addComponent(button);
                } catch (Exception e) {
                    TabbyChat.getLogger().error(
                            "Unable to add " + sett.getName() + " as a setting.", e);
                }
            }
        }
        boolean init;
        Class<? extends SettingPanel> panelClass;
        if (selectedSetting == null) {
            init = true;
            panelClass = settings.get(0);
        } else {
            init = false;
            panelClass = selectedSetting.getClass();
        }
        selectSetting(panelClass, init);
    }

    private void deactivateAll() {
        for (GuiComponent comp : settingsList) {
            if (comp instanceof SettingsButton) {
                ((SettingsButton) comp).setActive(false);
            }
        }
    }

    private void activate(Class<? extends SettingPanel> settingClass) {
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

    private void selectSetting(Class<? extends SettingPanel> settingClass, boolean init) {
        if (!settings.contains(settingClass)) {
            throw new IllegalArgumentException(settingClass.getName()
                    + " is not a registered setting category.");
        }
        try {
            selectSetting(settingClass.newInstance());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void selectSetting(SettingPanel setting) {
        deactivateAll();
        panel.removeComponent(selectedSetting);
        selectedSetting = setting;
        selectedSetting.initGUI();
        activate(setting.getClass());
        panel.addComponent(selectedSetting, BorderLayout.Position.CENTER);
    }

    public void saveSettings() {
        for (GuiComponent comp : settingsList) {
            if (comp instanceof SettingPanel) {
                ((SettingPanel) comp).getSettings().saveSettingsFile();
            }
        }
    }

    public static void registerSetting(Class<? extends SettingPanel> settings) {
        if (!GuiSettingsScreen.settings.contains(settings)) {
            GuiSettingsScreen.settings.add(settings);
        }
    }
}
