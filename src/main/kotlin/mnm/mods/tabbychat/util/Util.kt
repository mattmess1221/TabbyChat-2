package mnm.mods.tabbychat.util

import net.minecraft.client.Minecraft
import net.minecraft.util.text.StringTextComponent
import java.net.URLEncoder
import java.nio.file.Path
import java.nio.file.Paths

operator fun Path.div(other: Path): Path = this.resolve(other)
operator fun Path.div(other: String): Path = this.resolve(other)
operator fun String.div(other: String): Path = this.toPath() / other

fun String.toPath(): Path = Paths.get(this)
val String.urlEncoded: String get() = URLEncoder.encode(this, "utf-8")

fun String.toComponent() = StringTextComponent(this)

val mc: Minecraft get() = Minecraft.getInstance()
