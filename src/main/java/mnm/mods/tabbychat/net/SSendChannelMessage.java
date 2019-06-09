package mnm.mods.tabbychat.net;

import mnm.mods.tabbychat.TCMarkers;
import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.client.ChatManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.Optional;
import java.util.function.Supplier;

public class SSendChannelMessage {

    private String channel;
    private ITextComponent message;

    public SSendChannelMessage(PacketBuffer buffer) {
        channel = buffer.readString(20);
        message = buffer.readTextComponent();
    }

    public SSendChannelMessage(String channel, ITextComponent message) {
        this.channel = channel;
        this.message = message;
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeString(channel);
        buffer.writeTextComponent(message);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ChatManager chat = ChatManager.instance();
            Optional<Channel> chan = chat.parseChannel(channel);
            if (!chan.isPresent()) {
                TabbyChat.logger.warn(TCMarkers.NETWORK, "Server sent bad channel name: {}", channel);
                return;
            }

            chat.addMessage(chan.get(), message);
        });
        context.get().setPacketHandled(true);
    }
}
