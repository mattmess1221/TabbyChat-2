package mnm.mods.tabbychat.util;

import java.util.Calendar;
import java.util.Date;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.Message;
import mnm.mods.tabbychat.settings.GeneralSettings;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class ChatMessage extends ChatLine implements Message {

    private Date date;

    public ChatMessage(int updatedCounter, IChatComponent chat, int id, boolean isNew) {
        super(updatedCounter, chat, id);
        if (isNew) {
            this.date = Calendar.getInstance().getTime();
        }
    }

    public ChatMessage(ChatLine chatline) {
        this(chatline.getUpdatedCounter(), chatline.getChatComponent(), chatline.getChatLineID(),
                true);
    }

    @Override
    public IChatComponent getMessage() {
        IChatComponent chat;
        GeneralSettings settings = TabbyChat.getInstance().generalSettings;
        if (date != null && settings.timestampChat.getValue()) {
            chat = new ChatComponentText("");

            TimeStamps stamp = settings.timestampStyle.getValue();
            EnumChatFormatting format = settings.timestampColor.getValue();
            chat = new ChatComponentTranslation("%s %s", format + stamp.format(date),
                    getChatComponent());
        } else {
            chat = getChatComponent();
        }
        return chat;
    }

    @Override
    public int getCounter() {
        return this.getUpdatedCounter();
    }

    @Override
    public int getID() {
        return this.getChatLineID();
    }

    @Override
    public Date getDate() {
        return this.date;
    }
}
