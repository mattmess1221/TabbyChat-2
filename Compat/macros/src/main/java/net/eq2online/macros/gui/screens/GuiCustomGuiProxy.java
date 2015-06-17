package net.eq2online.macros.gui.screens;

import java.awt.Point;
import java.awt.Rectangle;

import net.eq2online.macros.gui.controls.GuiDropDownMenu;

public class GuiCustomGuiProxy {

    public static GuiDropDownMenu getContextMenu(GuiCustomGui gui) {
        return gui.contextMenu;
    }

    public static void setBoundingBox(GuiCustomGui gui, Rectangle rect) {
        gui.boundingBox = rect;
    }

    public static void setContextMenuLocation(GuiCustomGui gui, Point p) {
        gui.contextMenuLocation = p;
    }

    public static boolean controlClicked(GuiCustomGui gui, int mouseX, int mouseY, int button) {
        return gui.controlClicked(mouseX, mouseY, button);
    }
}
