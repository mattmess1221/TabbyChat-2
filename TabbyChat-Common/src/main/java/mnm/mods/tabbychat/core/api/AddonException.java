package mnm.mods.tabbychat.core.api;

public class AddonException extends Exception {

    private static final long serialVersionUID = 2253686906805675035L;

    public AddonException(String string, Throwable t) {
        super(string, t);
    }

    public AddonException(Throwable t) {
        super(t);
    }

    public AddonException(String string) {
        super(string);
    }
}
