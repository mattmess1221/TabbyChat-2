package mnm.mods.tabbychat.util

import mnm.mods.tabbychat.STARTUP
import mnm.mods.tabbychat.TabbyChat
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.IngameGui
import net.minecraft.client.gui.NewChatGui
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import net.minecraft.util.text.Style
import net.minecraftforge.fml.common.ObfuscationReflectionHelper
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

val mc get() = Minecraft.getInstance()
