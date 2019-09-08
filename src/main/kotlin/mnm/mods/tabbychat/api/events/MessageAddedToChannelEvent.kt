package mnm.mods.tabbychat.api.events

import mnm.mods.tabbychat.api.Channel
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.eventbus.api.Cancelable
import net.minecraftforge.eventbus.api.Event

sealed class MessageAddedToChannelEvent(open val text: ITextComponent?, open val id: Int, val channel: Channel) : Event() {

    @Cancelable
    class Pre(override var text: ITextComponent?,
              override var id: Int, channel: Channel) : MessageAddedToChannelEvent(text, id, channel)

    class Post(text: ITextComponent, id: Int, channel: Channel) : MessageAddedToChannelEvent(text, id, channel)
}
