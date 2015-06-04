package mnm.mods.tabbychat.update;

public class UpdateRequest {

    private final String url;
    private final String modId;

    public UpdateRequest(String url, String modid) {
        this.url = url;
        this.modId = modid;
    }

    public String getUrl() {
        return url;
    }

    public String getModId() {
        return modId;
    }

}
