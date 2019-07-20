package mnm.mods.tabbychat.client.gui.settings;

import mnm.mods.tabbychat.client.TabbyChatClient;
import mnm.mods.tabbychat.client.extra.filters.GuiFilterEditor;
import mnm.mods.tabbychat.client.extra.filters.UserFilter;
import mnm.mods.tabbychat.client.gui.component.GuiButton;
import mnm.mods.tabbychat.client.gui.component.GuiPanel;
import mnm.mods.tabbychat.client.gui.component.config.SettingPanel;
import mnm.mods.tabbychat.client.gui.component.layout.BorderLayout;
import mnm.mods.tabbychat.client.gui.component.layout.FlowLayout;
import mnm.mods.tabbychat.client.settings.ServerSettings;
import mnm.mods.tabbychat.util.Color;
import net.minecraft.client.resources.I18n;

import static mnm.mods.tabbychat.util.Translation.FILTERS;

public class GuiSettingsFilters extends SettingPanel<ServerSettings> {

    private GuiFilterEditor currentFilter;

    private int index = 0;

    private GuiButton prev;
    private GuiButton next;
    private GuiButton delete;

    GuiSettingsFilters() {
        setLayout(new BorderLayout());
        this.setDisplayString(I18n.format(FILTERS));
        this.setSecondaryColor(Color.of(50, 200, 50, 64));
    }

    @Override
    public void initGUI() {
        index = getSettings().filters.get().size() - 1;

        GuiPanel panel = new GuiPanel(new FlowLayout());
        this.add(panel, BorderLayout.Position.NORTH);

        prev = new GuiButton("<") {
            @Override
            public void onClick(double mouseX, double mouseY) {
                select(index - 1);
            }
        };
        panel.add(prev);

        GuiButton _new = new GuiButton("+"){//I18n.format(FILTERS_NEW)) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                add();
            }
        };

        panel.add(_new);
        delete = new GuiButton("-") {//I18n.format("selectServer.delete")){
            @Override
            public void onClick(double mouseX, double mouseY) {
                delete(index);
            }
        };
        panel.add(delete);

        next = new GuiButton(">") {
            @Override
            public void onClick(double mouseX, double mouseY) {
                select(index + 1);
            }
        };
        panel.add(next);
        prev.setEnabled(false);
        if (index == -1) {
            delete.setEnabled(false);
            next.setEnabled(false);
        } else {
            select(index);
        }

        update();
    }

    @Override
    public ServerSettings getSettings() {
        return TabbyChatClient.getInstance().getServerSettings();
    }

    private void select(int i) {
        this.index = i;
        if (currentFilter != null) {
            this.remove(currentFilter);
        }

        UserFilter filter = getSettings().filters.get(i);
        currentFilter = new GuiFilterEditor(filter);
        this.add(currentFilter, BorderLayout.Position.CENTER);
        setFocused(currentFilter);

        update();
    }

    private void delete(int i) {
        // deletes a filter
        getSettings().filters.remove(i);
        this.remove(this.currentFilter);
        update();
    }

    private void add() {
        // creates a new filter, adds it to the list, and selects it.
        getSettings().filters.add(new UserFilter());
        select(getSettings().filters.get().size() - 1);
        update();
    }

    private void update() {
        this.next.setEnabled(true);
        this.prev.setEnabled(true);
        this.delete.setEnabled(true);

        int size = getSettings().filters.get().size();

        if (index >= size - 1) {
            this.next.setEnabled(false);
            index = size - 1;
        }
        if (index < 1) {
            this.prev.setEnabled(false);
            index = 0;
        }
        if (size < 1) {
            this.delete.setEnabled(false);
            this.index = 0;
        }
    }
}
