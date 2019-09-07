package mnm.mods.tabbychat.util.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonParseException;
import mnm.mods.tabbychat.TCMarkers;
import mnm.mods.tabbychat.TabbyChat;
import net.minecraft.util.EnumTypeAdapterFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

/**
 * Used for creating settings and saving/loading them in the JSON format. Start
 * by creating fields. Mark anything you don't wish to be serialized with {@code transient}.
 *
 * @author Matthew Messinger
 */
public abstract class SettingsFile extends ValueObject {

    private transient final Gson gson = new GsonBuilder()
            .registerTypeAdapter(getClass(), (InstanceCreator) type -> this)
            .setPrettyPrinting()
            .registerTypeAdapterFactory(new EnumTypeAdapterFactory())
            .create();

    private transient final Path path;

    public SettingsFile(Path path) {
        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    public final void save() {
        try {
            Files.createDirectories(path.getParent());
            try (Writer w = Files.newBufferedWriter(path)) {
                gson.toJson(this, w);
            }
        } catch (IOException e) {
            TabbyChat.logger.error(TCMarkers.CONFIG, "Failed to save config to {}", path, e);
        }
    }

    public final void load() {

        try (Reader r = Files.newBufferedReader(path)) {
            gson.fromJson(r, getClass());
        } catch (NoSuchFileException e) {
            TabbyChat.logger.info(TCMarkers.CONFIG, "Saving default config to {}", path);
            save();
        } catch (IOException e) {
            TabbyChat.logger.error(TCMarkers.CONFIG, "Failed to load config from {}", path, e);
        } catch (JsonParseException e) {
            TabbyChat.logger.error(TCMarkers.CONFIG, "Syntax or schema error in {}", path, e);
        }
    }
}
