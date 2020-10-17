package mnm.mods.tabbychat.client.gui

import net.minecraft.client.gui.IRenderable
import net.minecraft.client.gui.widget.Widget

interface IScreenHelper {
    val width: Int

    val height: Int

    fun <T : Widget> addWidget(widget: T, init: T.() -> Unit = {}): T

    fun <T : IRenderable> addRender(render: T, init: T.() -> Unit = {}): T
}