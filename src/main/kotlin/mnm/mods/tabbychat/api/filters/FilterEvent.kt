package mnm.mods.tabbychat.api.filters

import java.util.regex.Matcher

import mnm.mods.tabbychat.api.Channel
import net.minecraft.util.text.ITextComponent

class FilterEvent(val matcher: Matcher, var channels: MutableSet<Channel>, var text: ITextComponent)
