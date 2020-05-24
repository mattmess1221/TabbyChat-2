package mnm.mods.tabbychat.util.config

import net.minecraftforge.eventbus.api.Event
import kotlin.reflect.KProperty

class ConfigEvent(val prop: KProperty<*>) : Event()

