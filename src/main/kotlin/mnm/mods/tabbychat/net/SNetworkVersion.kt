package mnm.mods.tabbychat.net

import mnm.mods.tabbychat.*
import mnm.mods.tabbychat.client.gui.NotificationToast
import net.minecraft.client.Minecraft
import net.minecraft.network.PacketBuffer
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraftforge.fml.network.NetworkEvent

import java.util.function.Supplier

class SNetworkVersion {

    private var version: String

    constructor(buffer: PacketBuffer) {
        version = buffer.readString(20)
    }

    constructor(version: String) {
        this.version = version
    }

    fun encode(buffer: PacketBuffer) {
        buffer.writeString(version)
    }

    fun handle(context: Supplier<NetworkEvent.Context>) {
        context.get().enqueueWork {

            if (version != PROTOCOL_VERSION) {
                val title = TranslationTextComponent("tabbychat.network.mismatch")
                Minecraft.getInstance().toastGui.add(NotificationToast("TabbyChat", title))
            }
        }
        context.get().packetHandled = true
    }
}
