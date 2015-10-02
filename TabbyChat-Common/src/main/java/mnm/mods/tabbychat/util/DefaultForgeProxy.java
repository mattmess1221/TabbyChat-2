package mnm.mods.tabbychat.util;

import mnm.mods.tabbychat.api.internal.ForgeProxy;

public class DefaultForgeProxy implements ForgeProxy {

    @Override
    public void autoComplete(String word, String s1) {}

    @Override
    public String[] getLatestAutoComplete() {
        return new String[0];
    }

}
