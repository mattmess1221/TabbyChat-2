package mnm.mods.tabbychat;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;

import mnm.mods.tabbychat.api.Message;
import mnm.mods.tabbychat.settings.GeneralSettings;
import mnm.mods.tabbychat.util.TimeStamps;
import mnm.mods.util.ReflectionHelper;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.event.HoverEvent;
import net.minecraft.event.HoverEvent.Action;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class ChatMessage implements Message {

    @Expose
    private IChatComponent message;
    @Expose
    private int id;
    private transient int counter;
    @Expose
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
        if (date != null && settings.timestampChat.get()) {
            chat = new ChatComponentText("");

            TimeStamps stamp = settings.timestampStyle.get();
            EnumChatFormatting format = settings.timestampColor.get();
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

    void fixSerialization() {
        List<HoverEvent> list = Lists.newArrayList();
        for (IChatComponent message : message) {
            ChatStyle style = message.getChatStyle();
            HoverEvent hover = style.getChatHoverEvent();
            if (list.contains(hover))
                continue;
            list.add(hover);
            if (hover != null && hover.getAction() == Action.SHOW_ENTITY) {
                // serialization is bugged
                try {
                    String[] names = { "b", "field_150703_b", "value" };
                    ReflectionHelper.setFieldValue(HoverEvent.class, hover, null, names);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
