package mnm.mods.tabbychat.api.listener;

import mnm.mods.tabbychat.api.listener.events.ChatInitEvent;
import net.minecraft.client.gui.GuiButton;

/**
 * Interface for modules that add buttons to GuiChat.
 *
 * @author Matthew
 */
public interface ChatScreenListener extends TabbyListener {

    void onInitScreen(ChatInitEvent chatInitEvent);

    void onUpdateScreen();

    void onCloseScreen();

    void actionPreformed(GuiButton button);
}
