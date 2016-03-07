package mnm.mods.tabbychat.util;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

import mnm.mods.tabbychat.liteloader.TabbyPrivateFields;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundRegistry;
import net.minecraft.util.ResourceLocation;

public class SoundHelper implements Comparable<SoundHelper> {

    private static Supplier<List<SoundHelper>> supplier = Suppliers.memoizeWithExpiration(
            () -> getRegistry().getKeys().stream()
                    .map(SoundHelper::new)
                    .collect(Collectors.toList()),
            5, TimeUnit.MINUTES);

    private ResourceLocation resource;
    private String name;

    public SoundHelper(ResourceLocation res) {
        this.resource = res;
        this.name = res.getResourcePath();
    }

    public ResourceLocation getResource() {
        return resource;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(SoundHelper o) {
        return getName().compareTo(o.getName());
    }

    public static List<SoundHelper> getSounds() {
        return supplier.get();
    }

    private static SoundRegistry getRegistry() {
        try {
            SoundHandler handler = Minecraft.getMinecraft().getSoundHandler();
            return TabbyPrivateFields.sndRegistry.get(handler);
        } catch (Exception e) {
            throw new RuntimeException("Error while getting sound registry.", e);
        }
    }
}
