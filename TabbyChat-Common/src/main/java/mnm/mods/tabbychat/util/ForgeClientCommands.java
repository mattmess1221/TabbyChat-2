package mnm.mods.tabbychat.util;

public class ForgeClientCommands {

    private static ForgeClientCommands instance;

    public static ForgeClientCommands getInstance() {
        if (instance == null) {
            instance = new ForgeClientCommands();
        }
        return instance;
    }

    protected static void setInstance(ForgeClientCommands instance) {
        ForgeClientCommands.instance = instance;
    }

    public void autoComplete(String word, String s1) {}

    public String[] getLatestAutoComplete() {
        return new String[0];
    }
}
