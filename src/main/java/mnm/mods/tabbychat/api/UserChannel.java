package mnm.mods.tabbychat.api;

import com.mojang.authlib.GameProfile;

public interface UserChannel extends Channel {

    GameProfile getUser();
}
