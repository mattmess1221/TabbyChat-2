package mnm.mods.tabbychat.util;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import mnm.mods.tabbychat.liteloader.TabbyPrivateFields;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundRegistry;
import net.minecraft.util.ResourceLocation;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;

public class SoundHelper implements Comparable<SoundHelper> {

    private static Supplier<List<SoundHelper>> supplier = Suppliers.memoizeWithExpiration(
            Suppliers.compose(
                    new Function<SoundRegistry, List<SoundHelper>>() {
                        @Override
                        public List<SoundHelper> apply(SoundRegistry input) {
                            List<SoundHelper> list = Lists.newArrayList();
                            Set<ResourceLocation> sounds = input.getKeys();
                            for (ResourceLocation res : sounds) {
                                list.add(new SoundHelper(res));
                            }
                            Collections.sort(list);
                            return list;
                        }
                    }, Suppliers.ofInstance(getRegistry())),
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
