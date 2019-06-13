package mnm.mods.tabbychat.net;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.client.ChatManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SNetworkVersion {

    private String version;

    public SNetworkVersion(PacketBuffer buffer) {
        version = buffer.readString(20);
    }

    public SNetworkVersion(String version) {
        this.version = version;
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeString(version);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ChatManager chat = ChatManager.instance();
            Channel chan = chat.getChannel("TabbyChat");

            if (!version.equals(TabbyChat.PROTOCOL_VERSION)) {
                chat.addMessage(chan, new TranslationTextComponent("tabbychat.network.mismatch", TabbyChat.PROTOCOL_VERSION, version));
            }
        });
        context.get().setPacketHandled(true);
    }
}
