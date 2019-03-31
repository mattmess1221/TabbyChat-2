package mnm.mods.tabbychat.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import mnm.mods.tabbychat.ChatManager;
import mnm.mods.tabbychat.api.Channel;

import java.io.IOException;

public class ChannelTypeAdapter extends TypeAdapter<Channel> {

    private ChatManager chat;

    public ChannelTypeAdapter(ChatManager chat) {
        this.chat = chat;
    }

    @Override
    public void write(JsonWriter out, Channel value) throws IOException {
        out.value(value.toString());
    }

    @Override
    public Channel read(JsonReader in) throws IOException {
        return chat.parseChannel(in.nextString())
                .orElseThrow(() -> new IOException("Serialized channels must start with @ or #"));
    }
}
