package mnm.mods.util.update;

import com.google.common.primitives.Doubles;
import com.mumfrey.liteloader.LiteMod;
import com.mumfrey.liteloader.core.LiteLoader;

import javax.annotation.Nullable;

public class VersionData {

    private String name;
    private String updateUrl;
    private String url;
    private double revision;

    private VersionData(String name, String updateUrl, String url, @Nullable Double revision) {
        this.name = name;
        this.updateUrl = updateUrl;
        this.url = url;
        this.revision = revision != null ? revision : Double.MAX_VALUE;
    }

    public String getName() {
        return name;
    }

    String getUpdateUrl() {
        return updateUrl;
    }

    String getUrl() {
        return this.url;
    }

    private int compareTo(double arg0) {
        return Double.compare(revision, arg0);
    }

    boolean isOutdated(@Nullable UpdateResponse.Version update) {
        return update != null && compareTo(update.revision) > 0;
    }

    static VersionData fromLiteMod(LiteMod litemod) {
        String updateurl = LiteLoader.getInstance().getModMetaData(litemod, "updateUrl", null);
        String url = LiteLoader.getInstance().getModMetaData(litemod, "url", null);
        String rev = LiteLoader.getInstance().getModMetaData(litemod, "revision", null);
        if (updateurl == null || rev == null)
            return null;
        return new VersionData(litemod.getName(), updateurl, url, Doubles.tryParse(rev));
    }
}
