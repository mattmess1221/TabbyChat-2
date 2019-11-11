package mnm.mods.tabbychat.client

import mnm.mods.tabbychat.api.Message
import net.minecraft.util.text.ITextComponent
import java.time.LocalDateTime

class ChatMessage(
        @Transient val counter: Int,
        override val message: ITextComponent,
        val id: Int,
        override val dateTime: LocalDateTime? = LocalDateTime.now()
) : Message
