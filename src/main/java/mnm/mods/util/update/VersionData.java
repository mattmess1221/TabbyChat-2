package mnm.mods.util.update;

import javax.annotation.Nullable;

public class VersionData {

    private String name;
    private String updateUrl;
    private String url;
    private double revision;

    public VersionData(String name, String updateUrl, String url, double revision) {
        this.name = name;
        this.updateUrl = updateUrl;
        this.url = url;
        this.revision = revision;
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
        return update != null && compareTo(update.revision) < 0;
    }
}
