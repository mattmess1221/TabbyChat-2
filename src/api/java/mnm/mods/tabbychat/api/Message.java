package mnm.mods.tabbychat.api;

import net.minecraft.util.IChatComponent;

public interface Message {

    IChatComponent getMessage();

    int getCounter();

    int getID();

    Channel[] getChannels();

    void addChannel(Channel... channels);

    boolean isActive();

}
