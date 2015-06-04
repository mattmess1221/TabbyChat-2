package mnm.mods.tabbychat.update;

import com.google.gson.annotations.SerializedName;

public class UpdateResponse {

    @SerializedName("@MCVERSION@")
    public Update update;
    private transient boolean success;

    public UpdateResponse() {
        this(true);
    }

    public UpdateResponse(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success && update != null;
    }

    public class Update {
        public double revision;
        public String version;
        public String changes;
    }
}
