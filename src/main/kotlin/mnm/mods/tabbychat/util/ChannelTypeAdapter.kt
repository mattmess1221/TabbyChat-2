package mnm.mods.tabbychat.util

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import mnm.mods.tabbychat.client.ChatManager
import mnm.mods.tabbychat.api.Channel

import java.io.IOException

class ChannelTypeAdapter(private val chat: ChatManager) : TypeAdapter<Channel>() {

    @Throws(IOException::class)
    override fun write(jsonOut: JsonWriter, value: Channel) {
        jsonOut.value(value.toString())
    }

    @Throws(IOException::class)
    override fun read(jsonIn: JsonReader): Channel {
        return chat.parseChannel(jsonIn.nextString()) ?: throw IOException("Serialized channels must start with @ or #")
    }
}
