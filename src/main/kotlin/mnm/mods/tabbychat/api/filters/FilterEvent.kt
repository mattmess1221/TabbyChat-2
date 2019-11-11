package mnm.mods.tabbychat.api.filters

import mnm.mods.tabbychat.api.Channel
import net.minecraft.util.text.ITextComponent
import java.util.regex.Matcher

class FilterEvent(val matcher: Matcher, var channels: MutableSet<Channel>, var text: ITextComponent)
