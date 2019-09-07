package mnm.mods.tabbychat.api.events

import mnm.mods.tabbychat.api.Channel
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.eventbus.api.Cancelable
import net.minecraftforge.eventbus.api.Event

abstract class ChatMessageEvent : Event() {

    /**
     * Used to listen to chat and modify it. Can also select which channels it
     * goes to.
     */
    @Cancelable
    class ChatReceivedEvent(var text: ITextComponent?, var id: Int) : ChatMessageEvent() {
        var channels: MutableSet<Channel> = mutableSetOf()
    }
}
