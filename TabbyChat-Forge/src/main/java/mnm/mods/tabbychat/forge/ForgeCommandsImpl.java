package mnm.mods.tabbychat.forge;

import mnm.mods.tabbychat.util.ForgeClientCommands;
import mnm.mods.util.ForgeUtils;
import mnm.mods.util.LogHelper;
import net.minecraftforge.client.ClientCommandHandler;

public class ForgeCommandsImpl extends ForgeClientCommands {

    private static final LogHelper logger = LogHelper.getLogger();

    @Override
    public void autoComplete(String word, String s1) {
        if (ForgeUtils.FORGE_INSTALLED) {
            try {
                ClientCommandHandler.instance.autoComplete(word, s1);
            } catch (Exception e) {
                // in case not initialized
                logger.error(e);
            }
        }
    }

    @Override
    public String[] getLatestAutoComplete() {
        String[] result = new String[0];
        if (ForgeUtils.FORGE_INSTALLED) {
            try {
                return ClientCommandHandler.instance.latestAutoComplete;
            } catch (Exception e) {
                // in case not initialized
                logger.error(e);
            }
        }
        return result;
    }

    static void setInstance() {
        setInstance(new ForgeCommandsImpl());
    }
}
