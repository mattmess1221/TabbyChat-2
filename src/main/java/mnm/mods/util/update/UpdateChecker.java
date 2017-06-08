package mnm.mods.util.update;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.mumfrey.liteloader.LiteMod;
import com.mumfrey.liteloader.core.LiteLoader;
import mnm.mods.util.IChatProxy;
import mnm.mods.util.text.ITextBuilder;
import mnm.mods.util.text.TextBuilder;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.List;
import java.util.Set;

/**
 * Update checker for several mods.
 *
 * @author Matthew
 */
public class UpdateChecker extends Thread {

    private static final Logger logger = LogManager.getLogger("Updates");

    private IChatProxy chatProxy;
    private VersionData[] data;

    private UpdateChecker(IChatProxy chat, VersionData[] data) {
        super("Update Checker");
        this.chatProxy = chat;
        this.data = data;
    }

    @Override
    public void run() {
        for (VersionData data : this.data) {
            runCheck(data);
        }

    }

    private void runCheck(VersionData data) {
        InputStream in = null;
        Reader reader = null;
        UpdateResponse response;
        try {
            URL url = new URL(data.getUpdateUrl());
            in = url.openStream();
            reader = new InputStreamReader(in);
            response = new Gson().fromJson(reader, UpdateResponse.class);
        } catch (IOException e) {
            // failure
            logger.warn("Update check failed.", e);
            return;
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(in);
        }

        if (data.isOutdated(response.minecraft)) {
            notifyUser(data, response);
        } else {
            logger.info("Update check for " + data.getName() + " finished. None found.");
        }
    }

    private void notifyUser(VersionData data, UpdateResponse response) {
        ITextBuilder builder = new TextBuilder()
                .translation("update.available")
                .text(data.getName())
                .format(TextFormatting.GOLD)
                .end()
                .text(" ");
        if (data.getUrl() != null)
            builder.translation("update.clickhere").end()
                    .format(TextFormatting.LIGHT_PURPLE)
                    .click(new ClickEvent(ClickEvent.Action.OPEN_URL, data.getUrl()))
                    .text(". ");
        ITextComponent msg = builder
                .text(response.minecraft.version)
                .text(" - ")
                .text(response.minecraft.changes)
                .build();
        LogManager.getLogger("Updates").info(msg.getUnformattedText());
        this.chatProxy.addToChat("Updates", msg);
    }

    public static void runUpdateChecks(IChatProxy chat, Set<Class<? extends LiteMod>> disabled) {

        List<VersionData> list = Lists.newArrayList();
        for (LiteMod mod : LiteLoader.getInstance().getLoadedMods()) {
            VersionData data = VersionData.fromLiteMod(mod);
            if (data != null && !disabled.contains(mod.getClass()))
                list.add(data);
        }
        if (!list.isEmpty()) {
            new UpdateChecker(chat, list.toArray(new VersionData[0])).start();
        }
    }
}
