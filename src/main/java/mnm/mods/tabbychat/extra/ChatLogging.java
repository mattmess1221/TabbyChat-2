package mnm.mods.tabbychat.extra;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.listener.ChatRecievedListener;
import mnm.mods.tabbychat.api.listener.events.ChatMessageEvent.ChatRecievedEvent;
import mnm.mods.util.IPUtils;
import net.minecraft.client.Minecraft;

import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipUtils;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class ChatLogging implements ChatRecievedListener {

    private static final SimpleDateFormat LOG_NAME_FORMAT = new SimpleDateFormat("yyyy'-'MM'-'dd");
    private static final SimpleDateFormat LOG_FORMAT = new SimpleDateFormat("'['HH':'mm':'ss'] '");
    private final File directory;

    private Calendar date;
    private InetSocketAddress server;
    private File logFile;
    private PrintStream out;

    public ChatLogging(File dir) {
        this.directory = dir;
        compressLogs();
    }

    @Override
    public void onChatRecieved(ChatRecievedEvent message) {
        if (TabbyChat.getInstance().settings.general.logChat.getValue()) {

            checkLog();
            if (out == null) {
                return;
            }

            out.println(LOG_FORMAT.format(Calendar.getInstance().getTime()) + message.chat.getUnformattedText());
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
            server = TabbyChat.getInstance().getCurrentServer();
            try {

                File old = logFile;
                String server = getLogFolder();
                logFile = findFile(new File(directory, server));
                logFile.getParentFile().mkdirs();
                logFile.createNewFile();
                IOUtils.closeQuietly(out);
                this.out = new PrintStream(new FileOutputStream(logFile, true), true, "UTF-8");

                // compress log
                if (prev != null && prev.get(Calendar.DATE) != date.get(Calendar.DATE)) {
                    gzipFile(old);
                }

            } catch (IOException e) {
                TabbyChat.getLogger().warn("Unable to create log file", e);
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
        boolean ip = TabbyChat.getInstance().getCurrentServer() != server;
        return day || ip;
    }

    private void compressLogs() {
        if (!directory.exists()) {
            return;
        }
        Collection<File> logs = FileUtils.listFiles(directory, new String[] { "log" }, true);
        for (File file : logs) {
            try {
                gzipFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void gzipFile(File file) throws IOException {
        if (file == null) {
            return;
        }
        String name = GzipUtils.getCompressedFilename(file.getName());
        File dest = new File(file.getParentFile(), name);
        FileOutputStream os = new FileOutputStream(dest);
        OutputStream gzip = null;
        try {
            // read the contents
            String contents = FileUtils.readFileToString(file, Charsets.UTF_8);
            gzip = new GzipCompressorOutputStream(os);
            // write / compress
            IOUtils.write(contents, gzip, Charsets.UTF_8);

        } finally {
            IOUtils.closeQuietly(gzip);
            os.close();
            file.delete(); // delete the file
        }
    }

    private String getLogFolder() {
        String ip;
        if (Minecraft.getMinecraft().isSingleplayer() || server == null) {
            ip = "singleplayer";
        } else {
            String url = server.getHostName();
            ip = IPUtils.parse(url).getFileSafeAddress();
        }
        return ip;
    }

    private static File findFile(File dir) {
        String date = LOG_NAME_FORMAT.format(Calendar.getInstance().getTime());
        File file = new File(dir, date + ".log");
        int i = 0;
        while (fileExists(file)) {
            i++;
            file = new File(dir, date + "-" + i + ".log");
        }

        return file;
    }

    private static boolean fileExists(File file) {
        if (!file.exists()) {
            String gzip = GzipUtils.getCompressedFilename(file.getName());
            file = new File(file.getParentFile(), gzip);
        }
        return file.exists();
    }
}
