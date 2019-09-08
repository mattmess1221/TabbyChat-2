package mnm.mods.tabbychat.client

import com.google.common.collect.ImmutableSet
import com.google.gson.GsonBuilder
import mnm.mods.tabbychat.CHATBOX
import mnm.mods.tabbychat.TabbyChat
import mnm.mods.tabbychat.api.Channel
import mnm.mods.tabbychat.api.Chat
import mnm.mods.tabbychat.api.events.MessageAddedToChannelEvent
import mnm.mods.tabbychat.client.gui.ChatBox
import mnm.mods.tabbychat.client.settings.ServerSettings
import mnm.mods.tabbychat.client.settings.TabbySettings
import mnm.mods.tabbychat.util.ChannelTypeAdapter
import mnm.mods.tabbychat.util.ChatTextUtils
import mnm.mods.tabbychat.util.DateTimeTypeAdapter
import mnm.mods.tabbychat.util.config.ValueMap
import mnm.mods.tabbychat.util.style
import net.minecraft.client.Minecraft
import net.minecraft.util.EnumTypeAdapterFactory
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import net.minecraft.util.text.Style
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.common.MinecraftForge
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream
import java.io.*
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDateTime
import java.util.*

object ChatManager : Chat {

    private val gson = GsonBuilder()
            .registerTypeHierarchyAdapter(ITextComponent::class.java, ITextComponent.Serializer())
            .registerTypeAdapter(Style::class.java, Style.Serializer())
            .registerTypeAdapterFactory(EnumTypeAdapterFactory())
            .registerTypeHierarchyAdapter(Channel::class.java, ChannelTypeAdapter(this))
            .registerTypeAdapter(LocalDateTime::class.java, DateTimeTypeAdapter())
            .create()

    const val MAX_CHAT_LENGTH = 256

    private val lock = Any()


    private val allChannels: MutableMap<String, Channel> = hashMapOf()
    private val allPms: MutableMap<String, Channel> = hashMapOf()

    /**
     * A map of all the messages per channel
     */
    private val messages = HashMap<Channel, MutableList<ChatMessage>>()

    /**
     * The split messages used for rendering the chatbox.
     */
    private val msgsplit = HashMap<Channel, List<ChatMessage>>()

    override val channels: Set<Channel>
        get() = ImmutableSet.copyOf<Channel>(ChatBox.getChannels())

    private fun server(): ServerSettings {
        return TabbyChatClient.serverSettings!!
    }

    private fun settings(): TabbySettings {
        return TabbyChatClient.settings
    }

    init {
        allChannels["*"] = DefaultChannel
    }

    override fun getChannel(name: String): Channel {
        return allChannels.getOrPut(name, getChannel(name, server().channels) { ChatChannel(name) })
    }

    override fun getUserChannel(user: String): Channel {
        return allPms.getOrPut(user, getChannel(user, server().pms) { UserChannel(user) })
    }

    private fun <T> getChannel(name: String, config: ValueMap<T>, absent: () -> T): () -> T {
        return { config.getOrPut(name, absent) }
    }

    /**
     * Parses a channel name and returns the channel. format must start with either # or @
     *
     * @param format The representation of the channel as a string
     * @return The channel
     */
    fun parseChannel(format: String): Channel? {
        if (format.length <= 1) {
            return null
        }
        val name = format.substring(1)
        return when (format[0]) {
            '@' -> getUserChannel(name)
            '#' -> getChannel(name)
            else -> null
        }

    }

    override fun getMessages(channel: Channel): List<ChatMessage> {
        return Collections.unmodifiableList(getChannelMessages(channel))
    }

    private fun getChannelMessages(channel: Channel): MutableList<ChatMessage> {
        return messages.getOrPut(channel) { ArrayList() }
    }

    fun getVisible(channel: Channel, width: Int): List<ChatMessage> {
        return msgsplit.getOrPut(channel) { ChatTextUtils.split(getMessages(channel), width) }
    }

    fun markDirty(channel: Channel?) {
        if (channel == null) {
            this.msgsplit.clear()
        } else {
            this.msgsplit.remove(channel)
        }
    }

    override fun addMessage(channel: Channel, message: ITextComponent) {
        addMessage(channel, message, 0)
    }

    fun addMessage(channel: Channel, text: ITextComponent, id: Int) {
        val event = MessageAddedToChannelEvent.Pre(text.deepCopy(), id, channel)
        if (MinecraftForge.EVENT_BUS.post(event) || event.text == null) {
            return
        }
        val text = event.text!!
        val id = event.id

        if (id != 0) {
            removeMessages(channel, id)
        }

        val uc = Minecraft.getInstance().ingameGUI.ticks
        val msg = ChatMessage(uc, text, id)
        val messages = getChannelMessages(channel)
        messages.add(0, msg)

        trimMessages(messages, settings().advanced.historyLen.value)

        MinecraftForge.EVENT_BUS.post(MessageAddedToChannelEvent.Post(text, id, channel))

        save()
        markDirty(channel)
    }

    private fun trimMessages(list: MutableList<*>, size: Int) {
        val iter = list.iterator()

        var i = 0
        while (iter.hasNext()) {
            iter.next()
            if (i > size) {
                iter.remove()
            }
            i++
        }
    }

    fun removeMessages(chan: Channel, id: Int) {
        getChannelMessages(chan).removeIf { m -> m.id == id }
    }

    fun removeMessageAt(channel: AbstractChannel, index: Int) {
        getChannelMessages(channel).removeAt(index)
        save()
    }

    fun clearMessages() {
        this.messages.clear()
        this.msgsplit.clear()

        this.allChannels.clear()
        this.allChannels["*"] = DefaultChannel
        this.allPms.clear()

        ChatBox.clearMessages()
    }

    @Throws(IOException::class)
    fun loadFrom(dir: Path) {
        synchronized(lock) {
            loadFromPrivate(dir)
        }
    }

    @Synchronized
    @Throws(IOException::class)
    private fun loadFromPrivate(dir: Path) {
        val file = dir.resolve("data.gz")
        if (Files.notExists(file)) {
            return
        }

        clearMessages()

        newCompressedReader(file).use { gzin ->
            val root = gson.fromJson(gzin, PersistantChat::class.java)

            for ((k, v) in root.messages) {
                this.messages[k] = v.toMutableList()
            }
            ChatBox.addChannels(root.active)

            val chat: ITextComponent = StringTextComponent("Chat log from ${root.time}").style {
                color = TextFormatting.GRAY
            }

            for (c in channels) {
                if (getMessages(c).isNotEmpty()) {
                    addMessage(c, chat, -1)
                }
            }
        }
    }

    fun save() {
        synchronized(lock) {
            try {
                saveTo(server().path.parent)
            } catch (e: IOException) {
                TabbyChat.logger.warn(CHATBOX, "Error while saving chat data", e)
            }

        }
    }

    @Synchronized
    @Throws(IOException::class)
    private fun saveTo(dir: Path) {
        val file = dir.resolve("data.gz")
        Files.createDirectories(dir)

        newCompressedWriter(file).use {
            gson.toJson(PersistantChat(messages, ChatBox.getChannels()), PersistantChat::class.java, it)
        }
    }

    private class PersistantChat(
            messages: Map<Channel, List<ChatMessage>>?,
            active: Collection<AbstractChannel>?,
            datetime: LocalDateTime? = LocalDateTime.now()
    ) {
        val messages = messages ?: emptyMap()
        val active = active ?: emptyList()
        val time = datetime?.toString() ?: "UNKNOWN"
    }

    @Throws(IOException::class)
    private fun newCompressedReader(path: Path): Reader {
        return InputStreamReader(GzipCompressorInputStream(Files.newInputStream(path)), StandardCharsets.UTF_8)
    }

    @Throws(IOException::class)
    private fun newCompressedWriter(path: Path): Writer {
        return OutputStreamWriter(GzipCompressorOutputStream(Files.newOutputStream(path)), StandardCharsets.UTF_8)
    }

}
