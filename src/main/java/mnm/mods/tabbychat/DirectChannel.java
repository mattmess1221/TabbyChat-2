package mnm.mods.tabbychat;

import com.mojang.authlib.GameProfile;
import mnm.mods.tabbychat.api.UserChannel;

import java.util.Objects;

public class DirectChannel extends AbstractChannel implements UserChannel {

    private GameProfile user;

    public DirectChannel(GameProfile user) {
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
    public int hashCode() {
        return Objects.hashCode(user);
    }

    @Override
    public String toString() {
        return "@" + user.getName();
    }
}
