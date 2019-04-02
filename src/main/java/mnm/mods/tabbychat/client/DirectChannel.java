package mnm.mods.tabbychat.client;

import com.mojang.authlib.GameProfile;
import mnm.mods.tabbychat.api.UserChannel;

import java.util.Objects;

public class DirectChannel extends AbstractChannel implements UserChannel {

    private GameProfile user;

    DirectChannel(GameProfile user) {
        super(user.getName());
        this.user = user;
        setPrefix("/msg " + user.getName());
    }

    @Override
    public GameProfile getUser() {
        return user;
    }

    @Override
    public String getName() {
        return user.getName();
    }

    @Override
    public String getDisplayName() {
        return "@" + getName();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(user);
    }

    @Override
    public String toString() {
        return "@" + user.getName();
    }
}
