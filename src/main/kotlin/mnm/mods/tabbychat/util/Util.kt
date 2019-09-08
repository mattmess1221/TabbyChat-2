package mnm.mods.tabbychat.util

import net.minecraft.client.Minecraft
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import net.minecraft.util.text.Style
import net.minecraftforge.eventbus.api.Event
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.IEventBus
import java.net.URLEncoder
import java.nio.file.Path
import java.nio.file.Paths


operator fun Path.div(other: Path): Path = this.resolve(other)
operator fun Path.div(other: String): Path = this.resolve(other)
operator fun String.div(other: String): Path = this.toPath() / other


fun String.toPath(): Path = Paths.get(this)
val String.urlEncoded: String get() = URLEncoder.encode(this, "utf-8")

data class Vec2i(val x: Int, val y: Int)
data class Dim(val width: Int, val height: Int)

fun String.toComponent() = StringTextComponent(this)

fun <T : ITextComponent> T.style(block: Style.() -> Unit): T = apply { style.block() }

val mc: Minecraft get() = Minecraft.getInstance()

inline fun <reified T : Event> IEventBus.listen(
        priority: EventPriority = EventPriority.NORMAL,
        cancelled: Boolean = false,
        crossinline listener: (T) -> Unit) {
    this.addListener(priority, cancelled, T::class.java) { listener(it) }
}