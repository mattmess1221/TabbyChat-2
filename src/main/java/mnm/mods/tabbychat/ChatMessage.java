package mnm.mods.tabbychat;

import java.time.LocalDateTime;

import mnm.mods.tabbychat.api.Message;
import net.minecraft.util.text.ITextComponent;

public class ChatMessage implements Message {

    private ITextComponent message;
    private int id;
    private transient int counter;
    private LocalDateTime instant;

    ChatMessage() {}

    public ChatMessage(int updatedCounter, ITextComponent chat, int id, boolean isNew) {
        // super(updatedCounter, chat, id);
        this.message = chat;
        this.id = id;
        this.counter = updatedCounter;
        if (isNew) {
            this.instant = LocalDateTime.now();
        }
    }

    @Override
    public ITextComponent getMessage() {
        return this.message;
    }

    public int getCounter() {
        return this.counter;
    }

    public int getID() {
        return this.id;
    }

    @Override
    public LocalDateTime getDateTime() {
        return this.instant;
    }

}
