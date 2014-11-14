package mnm.mods.tabbychat.settings;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.util.LogHelper;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class TabbySettings {

    private static LogHelper logger = TabbyChat.getLogger();
    private static Map<String, TabbySettings> settingsList = Maps.newHashMap();
    private static File dir = TabbyChat.getInstance().getDataFolder();

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private JsonObject jobj;
    protected final File file;

    private TabbySettings(String name) {
        this.file = new File(dir, name + ".json");
        loadSettings();
    }

    public static TabbySettings getSettings(String name) {
        if (!settingsList.containsKey(name)) {
            settingsList.put(name, new TabbySettings(name));
        }
        return settingsList.get(name);
    }

    public void saveSettings() {
        try {
            file.getParentFile().mkdirs();
            FileUtils.write(file, gson.toJson(jobj));
        } catch (IOException e) {
            logger.error("Unable to save config", e);
        }
    }

    public void loadSettings() {
        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
            jobj = gson.fromJson(new FileReader(file), JsonObject.class);
        } catch (Exception e) {
            logger.warn("Loading of settings failed. Resetting to defaults.", e);
        }
        if (jobj == null)
            jobj = new JsonObject();
    }

    public JsonObject getObject() {
        return this.jobj;
    }

    private JsonPrimitive createPrimitive(Object value) {
        JsonPrimitive prim = null;

        if (value instanceof Boolean)
            prim = new JsonPrimitive((Boolean) value);

        if (value instanceof Number)
            prim = new JsonPrimitive((Number) value);

        if (value instanceof Character)
            prim = new JsonPrimitive((Character) value);

        if (value instanceof String)
            prim = new JsonPrimitive((String) value);

        if (prim == null)
            throw new IllegalArgumentException(value.getClass().getName()
                    + " cannot be represented as a primitive.");
        return prim;
    }

    public JsonElement getSetting(String key) {
        JsonElement element = jobj;
        String[] path = key.split("\\.");
        for (int i = 0; i < path.length; i++) {
            JsonElement element2 = element.getAsJsonObject().get(path[i]);
            if (element2 == null) {
                if (i == path.length - 1) {
                    element2 = createPrimitive(SettingDefaults.get(key).value);
                } else {
                    element2 = new JsonObject();
                }
                element.getAsJsonObject().add(path[i], element2);
            }
            element = element2;
        }

        return element;
    }

    public String getString(String key) {
        return getSetting(key).getAsString();
    }

    public int getInt(String key) {
        return getSetting(key).getAsInt();
    }

    public JsonElement getElement(String key) {
        JsonElement element = jobj;
        String[] path = key.split("\\.");
        for (int i = 0; i < path.length; i++) {
            if (!element.getAsJsonObject().has(path[i]))
                element.getAsJsonObject().add(path[i], new JsonObject());
            element = element.getAsJsonObject().get(path[i]);
        }
        return element;
    }

    public void setSetting(String key, Object value) {
        String[] path = key.split("\\.");
        JsonObject object = getElement(key.substring(0, key.lastIndexOf('.'))).getAsJsonObject();
        object.add(path[path.length - 1], createPrimitive(value));
        this.saveSettings();
    }
}
