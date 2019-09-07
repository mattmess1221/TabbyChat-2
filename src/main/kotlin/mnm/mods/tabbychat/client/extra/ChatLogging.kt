package mnm.mods.tabbychat.client.extra

import io.netty.channel.local.LocalAddress
import mnm.mods.tabbychat.CHATBOX
import mnm.mods.tabbychat.TabbyChat
import mnm.mods.tabbychat.api.events.ChatMessageEvent.ChatReceivedEvent
import mnm.mods.tabbychat.client.TabbyChatClient
import mnm.mods.tabbychat.util.urlEncoded
import net.minecraft.client.Minecraft
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream
import org.apache.commons.compress.compressors.gzip.GzipUtils
import org.apache.commons.io.IOUtils
import java.io.IOException
import java.io.PrintStream
import java.net.SocketAddress
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.nio.file.attribute.BasicFileAttributes
import java.text.SimpleDateFormat
import java.util.*
import java.util.function.BiPredicate

class ChatLogging(private val directory: Path) {

    private var date: Calendar? = null
    private var server: SocketAddress? = null
    private var logFile: Path? = null
    private var out: PrintStream? = null

    private val logFolder: String
        get() {
            return if (server is LocalAddress || server == null) {
                "singleplayer"
            } else {
                server.toString().urlEncoded
            }
        }

    init {
        try {
            compressLogs()
        } catch (e: IOException) {
            TabbyChat.logger.error(CHATBOX, "Errored while compressing logs", e)
        }

    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun onChatReceived(message: ChatReceivedEvent) {
        val text = message.text
        if (text != null && TabbyChatClient.settings.general.logChat.value) {

            checkLog()
            if (out == null) {
                return
            }

            out!!.println(LOG_FORMAT.format(Calendar.getInstance().time) + text.string)
        }
    }

    /**
     * Checks that the date hasn't changed. If it has, updates date and out and
     * compressed the previous file.
     */
    private fun checkLog() {
        val cal = Calendar.getInstance()
        if (shouldChangeLogFile()) {
            val prev = date
            date = cal
            server = Minecraft.getInstance().player.connection.networkManager.remoteAddress
            try {
                val old = logFile
                val server = logFolder
                logFile = findFile(directory.resolve(server)).also {

                    TabbyChat.logger.debug(CHATBOX, "Using log file {}", logFile)

                    Files.createDirectories(it.parent)
                    Files.createFile(it)
                    IOUtils.closeQuietly(out)
                    this.out = PrintStream(Files.newOutputStream(logFile!!, StandardOpenOption.APPEND), true, "UTF-8")

                    // compress log
                    if (prev != null && prev.get(Calendar.DATE) != date!!.get(Calendar.DATE)) {
                        gzipFile(old!!)
                    }
                }


            } catch (e: IOException) {
                TabbyChat.logger.warn(CHATBOX, "Unable to create log file", e)
                this.date = null
                this.out = null
            }

        }
    }

    private fun shouldChangeLogFile(): Boolean {

        if (date == null) {
            return true
        }
        val day = Calendar.getInstance().get(Calendar.DATE) != date!!.get(Calendar.DATE)
        val ip = TabbyChatClient.serverSettings!!.socket !== server
        return day || ip
    }

    @Throws(IOException::class)
    private fun compressLogs() {
        if (Files.notExists(directory)) {
            return
        }

        Files.find(directory, 1, BiPredicate(::isLogFile)).forEach { file ->
            try {
                TabbyChat.logger.debug(CHATBOX, "Compressing log file {}", file)
                gzipFile(file)
            } catch (e: IOException) {
                TabbyChat.logger.warn(CHATBOX, "Unable to compress log {}.", file.fileName, e)
            }
        }
    }

    companion object {

        private val LOG_NAME_FORMAT = SimpleDateFormat("yyyy'-'MM'-'dd")
        private val LOG_FORMAT = SimpleDateFormat("'['HH':'mm':'ss'] '")

        private fun isLogFile(file: Path, attrs: BasicFileAttributes): Boolean {
            return file.endsWith(".log")
        }

        @Throws(IOException::class)
        private fun gzipFile(file: Path) {
            val name = GzipUtils.getCompressedFilename(file.fileName.toString())
            val dest = file.resolveSibling(name)
            GzipCompressorOutputStream(Files.newOutputStream(dest)).use {
                // Copy contents to gzip stream
                Files.copy(file, it)
                Files.delete(file) // delete the file
            }
        }

        private fun findFile(dir: Path): Path {
            val date = LOG_NAME_FORMAT.format(Calendar.getInstance().time)
            var file = dir.resolve("$date.log")
            var i = 0
            while (fileExists(file)) {
                i++
                file = dir.resolve(String.format("%s-%d.log", date, i))
            }

            return file
        }

        private fun fileExists(file: Path): Boolean {
            var file = file
            if (Files.notExists(file)) {
                val gzip = GzipUtils.getCompressedFilename(file.fileName.toString())
                file = file.resolveSibling(gzip)
            }
            return Files.exists(file)
        }
    }
}
