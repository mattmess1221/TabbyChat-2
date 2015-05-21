package mnm.mods.tabbychat.util;

import java.util.List;
import java.util.Set;

import net.minecraft.client.audio.SoundRegistry;
import net.minecraft.util.ResourceLocation;

import com.google.common.collect.Lists;

public class SoundHelper {

    private ResourceLocation resource;
    private String name;

    public SoundHelper(ResourceLocation res) {
        this.resource = res;
        String sound = res.getResourcePath();
        sound = sound.substring(sound.lastIndexOf('/') + 1, sound.indexOf('.'));
        this.name = sound;
    }

    public ResourceLocation getResource() {
        return resource;
    }

    public String getName() {
        return name;
    }

    public static List<SoundHelper> getSounds(SoundRegistry reg) {
        List<SoundHelper> list = Lists.newArrayList();
        Set<ResourceLocation> sounds = reg.getKeys();
        for (ResourceLocation res : sounds) {
            list.add(new SoundHelper(res));
        }
        return list;
    }

}
