package mnm.mods.tabbychat.client.extra;

import io.netty.channel.local.LocalAddress;
import mnm.mods.tabbychat.TCMarkers;
import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.client.TabbyChatClient;
import mnm.mods.tabbychat.api.events.ChatMessageEvent.ChatReceivedEvent;
import mnm.mods.tabbychat.util.IPUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipUtils;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ChatLogging {

    private static final SimpleDateFormat LOG_NAME_FORMAT = new SimpleDateFormat("yyyy'-'MM'-'dd");
    private static final SimpleDateFormat LOG_FORMAT = new SimpleDateFormat("'['HH':'mm':'ss'] '");

    private final Path directory;

    private Calendar date;
    private SocketAddress server;
    private Path logFile;
    private PrintStream out;

    public ChatLogging(Path dir) {
        this.directory = dir;
        try {
            compressLogs();
        } catch (IOException e) {
            TabbyChat.logger.error(TCMarkers.CHATBOX, "Errored while compressing logs", e);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onChatReceived(ChatReceivedEvent message) {
        if (TabbyChatClient.getInstance().getSettings().general.logChat.get()) {

            checkLog();
            if (out == null) {
                return;
            }

            out.println(LOG_FORMAT.format(Calendar.getInstance().getTime()) + message.text.getString());
        }
    }

    /**
     * Checks that the date hasn't changed. If it has, updates date and out and
     * compressed the previous file.
     */
    private void checkLog() {
        Calendar cal = Calendar.getInstance();
        if (shouldChangeLogFile()) {
            Calendar prev = date;
            date = cal;
            server = Minecraft.getInstance().player.connection.getNetworkManager().getRemoteAddress();
            try {
                Path old = logFile;
                String server = getLogFolder();
                logFile = findFile(directory.resolve(server));

                TabbyChat.logger.debug(TCMarkers.CHATBOX, "Using log file {}", logFile);

                Files.createDirectories(logFile.getParent());
                Files.createFile(logFile);
                IOUtils.closeQuietly(out);
                this.out = new PrintStream(Files.newOutputStream(logFile, StandardOpenOption.APPEND), true, "UTF-8");

                // compress log
                if (prev != null && prev.get(Calendar.DATE) != date.get(Calendar.DATE)) {
                    gzipFile(old);
                }

            } catch (IOException e) {
                TabbyChat.logger.warn(TCMarkers.CHATBOX, "Unable to create log file", e);
                this.date = null;
                this.out = null;
            }
        }
    }

    private boolean shouldChangeLogFile() {

        if (date == null) {
            return true;
        }
        boolean day = Calendar.getInstance().get(Calendar.DATE) != date.get(Calendar.DATE);
        boolean ip = TabbyChatClient.getInstance().getServerSettings().getSocket() != server;
        return day || ip;
    }

    private void compressLogs() throws IOException {
        if (Files.notExists(directory)) {
            return;
        }

        Files.find(directory, 1, ChatLogging::isLogFile).forEach(file -> {
            try {
                TabbyChat.logger.debug(TCMarkers.CHATBOX, "Compressing log file {}", file);
                gzipFile(file);
            } catch (IOException e) {
                TabbyChat.logger.warn(TCMarkers.CHATBOX, "Unable to compress log {}.", file.getFileName(), e);
            }
        });
    }

    private static boolean isLogFile(Path file, BasicFileAttributes attrs) {
        return file.endsWith(".log");
    }

    private static void gzipFile(Path file) throws IOException {
        String name = GzipUtils.getCompressedFilename(file.getFileName().toString());
        Path dest = file.resolveSibling(name);
        try (OutputStream os = new GzipCompressorOutputStream(Files.newOutputStream(dest))) {
            // Copy contents to gzip stream
            Files.copy(file, os);
            Files.delete(file); // delete the file
        }
    }

    private String getLogFolder() {
        String ip;
        if (this.server instanceof LocalAddress || server == null) {
            ip = "singleplayer";
        } else {
            ip = IPUtils.getFileSafeAddress((InetSocketAddress) server);
        }
        return ip;
    }

    private static Path findFile(Path dir) {
        String date = LOG_NAME_FORMAT.format(Calendar.getInstance().getTime());
        Path file = dir.resolve(date + ".log");
        int i = 0;
        while (fileExists(file)) {
            i++;
            file = dir.resolve(String.format("%s-%d.log", date, i));
        }

        return file;
    }

    private static boolean fileExists(Path file) {
        if (Files.notExists(file)) {
            String gzip = GzipUtils.getCompressedFilename(file.getFileName().toString());
            file = file.resolveSibling(gzip);
        }
        return Files.exists(file);
    }
}
