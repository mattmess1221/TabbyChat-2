package mnm.mods.tabbychat.net

import mnm.mods.tabbychat.*
import mnm.mods.tabbychat.TabbyChat
import mnm.mods.tabbychat.api.Channel
import mnm.mods.tabbychat.client.ChatManager
import net.minecraft.network.PacketBuffer
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.fml.network.NetworkEvent

import java.util.Optional
import java.util.function.Supplier

class SSendChannelMessage {

    private var channel: String
    private var message: ITextComponent

    constructor(buffer: PacketBuffer) {
        channel = buffer.readString(20)
        message = buffer.readTextComponent()
    }

    constructor(channel: String, message: ITextComponent) {
        this.channel = channel
        this.message = message
    }

    fun encode(buffer: PacketBuffer) {
        buffer.writeString(channel)
        buffer.writeTextComponent(message)
    }

    fun handle(context: Supplier<NetworkEvent.Context>) {
        context.get().enqueueWork {
            val chat = ChatManager
            val chan = chat.parseChannel(channel)
            if (chan == null) {
                TabbyChat.logger.warn(NETWORK, "Server sent bad channel name: {}", channel)
            } else {
                chat.addMessage(chan, message)
            }
        }
        context.get().packetHandled = true
    }
}
