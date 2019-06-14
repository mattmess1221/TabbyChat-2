package mnm.mods.tabbychat.net;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.client.gui.NotificationToast;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
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

            if (!version.equals(TabbyChat.PROTOCOL_VERSION)) {
                ITextComponent title = new TranslationTextComponent("tabbychat.network.mismatch");
                Minecraft.getInstance().getToastGui().add(new NotificationToast("TabbyChat", title));
            }
        });
        context.get().setPacketHandled(true);
    }
}
