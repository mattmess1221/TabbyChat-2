package mnm.mods.tabbychat.api.internal;

/**
 * Forge proxy used to run compatibility code without needing to use the forge
 * plugin on the entire project.
 */
public interface ForgeProxy {

    void autoComplete(String word, String s1);

    String[] getLatestAutoComplete();
}
