package mnm.mods.tabbychat.extra.log

import io.netty.channel.local.LocalAddress
import mnm.mods.tabbychat.CHATBOX
import mnm.mods.tabbychat.MODID
import mnm.mods.tabbychat.TabbyChat
import mnm.mods.tabbychat.api.events.ChatMessageEvent.ChatReceivedEvent
import mnm.mods.tabbychat.util.config.ConfigManager
import mnm.mods.tabbychat.util.div
import mnm.mods.tabbychat.util.mc
import mnm.mods.tabbychat.util.urlEncoded
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.loading.FMLPaths
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream
import org.apache.commons.compress.compressors.gzip.GzipUtils
import java.io.Closeable
import java.io.IOException
import java.io.PrintStream
import java.net.SocketAddress
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Mod.EventBusSubscriber(modid = MODID, value = [Dist.CLIENT])
object ChatLogging {

    private val LOG_NAME_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE
    private val LOG_FORMAT = DateTimeFormatter.ISO_LOCAL_TIME

    val config = ChatLoggingConfig(TabbyChat.dataFolder)

    private val directory = FMLPaths.GAMEDIR.get() / "logs/chat"

    class LogFile(val server: SocketAddress, val date: LocalDate = LocalDate.now()) : Closeable {

        private val dateString = LOG_NAME_FORMAT.format(date)
        private val logFolder = (server as? LocalAddress)?.let { "singleplayer" } ?: server.toString().urlEncoded
        private val logFile = findFile(directory / logFolder).also {
            TabbyChat.logger.debug(CHATBOX, "Using log file {}", it)

            Files.createDirectories(it.parent)
            Files.createFile(it)
        }

        private val out = PrintStream(Files.newOutputStream(logFile, StandardOpenOption.APPEND), true, "UTF-8")

        fun println(msg: Any) {
            out.println("[%s] %s".format(LOG_FORMAT.format(LocalDateTime.now()), msg))
        }

        private tailrec fun findFile(dir: Path, file: Path = dir / "$dateString.log", i: Int = 0): Path {
            if (fileExists(file)) {
                return findFile(dir, dir / "$dateString-$i.log", i + 1)
            }
            return file

        }

        private fun fileExists(file: Path): Boolean {
            if (Files.notExists(file)) {
                val gzip = GzipUtils.getCompressedFilename(file.fileName.toString())
                val gzipFile = file.resolveSibling(gzip)
                return Files.exists(gzipFile)
            }
            return true
        }

        override fun close() {
            out.close()
            if (LocalDate.now() != date) {
                gzipFile(logFile)
            }
        }
    }

    private var log: LogFile? = null

    init {
        ConfigManager.addConfigs(config)
        try {
            compressLogs()
        } catch (e: IOException) {
            TabbyChat.logger.error(CHATBOX, "Errored while compressing logs", e)
        }
    }

    @SubscribeEvent
    fun onChatReceived(message: ChatReceivedEvent) {
        val text = message.text
        if (text != null && config.logChat) {
            checkLog()
            log?.println("[%s] %s".format(LOG_FORMAT.format(LocalDateTime.now()), text.string))
        }
    }

    /**
     * Checks that the date hasn't changed. If it has, updates date and out and
     * compressed the previous file.
     */
    private fun checkLog() {
        if (shouldChangeLogFile()) {
            try {
                val server = mc.connection?.networkManager?.remoteAddress
                if (server != null) {
                    log?.closeQuietly()
                    log = LogFile(server)
                }
            } catch (e: IOException) {
                TabbyChat.logger.warn(CHATBOX, "Unable to create log file", e)
                log = null
            }
        }
    }

    private fun Closeable.closeQuietly() {
        try {
            close()
        } catch (e: IOException) {
            // ignore
        }
    }

    private fun shouldChangeLogFile(): Boolean {

        if (log == null) {
            return true
        }
        val day = LocalDate.now() != log?.date
        val ip = mc.connection?.networkManager?.remoteAddress !== log?.server
        return day || ip
    }

    @Throws(IOException::class)
    private fun compressLogs() {
        if (Files.notExists(directory)) {
            return
        }

        Files.list(directory).filter { it.endsWith(".log") }.forEach { file ->
            try {
                TabbyChat.logger.debug(CHATBOX, "Compressing log file {}", file)
                gzipFile(file)
            } catch (e: IOException) {
                TabbyChat.logger.warn(CHATBOX, "Unable to compress log {}.", file.fileName, e)
            }
        }
    }

    @Throws(IOException::class)
    private fun gzipFile(file: Path) {
        val name = GzipUtils.getCompressedFilename(file.fileName.toString())
        val dest = file.parent / name
        GzipCompressorOutputStream(Files.newOutputStream(dest)).use {
            // Copy contents to gzip stream
            Files.copy(file, it)
            Files.delete(file) // delete the original
        }
    }
}
