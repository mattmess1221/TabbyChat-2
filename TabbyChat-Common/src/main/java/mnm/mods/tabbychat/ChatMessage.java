package mnm.mods.tabbychat;

import java.util.Calendar;
import java.util.Date;

import mnm.mods.tabbychat.api.Message;
import mnm.mods.tabbychat.settings.GeneralSettings;
import mnm.mods.tabbychat.util.TimeStamps;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class ChatMessage implements Message {

    private IChatComponent message;
    private int id;
    private int counter;
    private Date date;

    public ChatMessage(int updatedCounter, IChatComponent chat, int id, boolean isNew) {
        // super(updatedCounter, chat, id);
        this.message = chat;
        this.id = id;
        this.counter = updatedCounter;
        if (isNew) {
            this.date = Calendar.getInstance().getTime();
        }
    }

    public ChatMessage(ChatLine chatline) {
        this(chatline.getUpdatedCounter(), chatline.getChatComponent(), chatline.getChatLineID(), true);
    }

    @Override
    public IChatComponent getMessage() {
        return this.message;
    }

    @Override
    public IChatComponent getMessageWithOptionalTimestamp() {
        IChatComponent chat;
        GeneralSettings settings = TabbyChat.getInstance().settings.general;
        if (date != null && settings.timestampChat.getValue()) {
            chat = new ChatComponentText("");

            TimeStamps stamp = settings.timestampStyle.getValue();
            EnumChatFormatting format = settings.timestampColor.getValue();
            chat = new ChatComponentTranslation("%s %s", format + stamp.format(date),
                    getMessage());
        } else {
            chat = getMessage();
        }
        return chat;
    }

    @Override
    public int getCounter() {
        return this.counter;
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public Date getDate() {
        return this.date;
    }
}
