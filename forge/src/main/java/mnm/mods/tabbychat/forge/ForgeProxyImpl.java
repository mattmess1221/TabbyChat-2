package mnm.mods.tabbychat.forge;

import mnm.mods.tabbychat.api.internal.ForgeProxy;
import net.minecraftforge.client.ClientCommandHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ForgeProxyImpl implements ForgeProxy {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public void autoComplete(String word, String s1) {
        try {
            ClientCommandHandler.instance.autoComplete(word, s1);
        } catch (Throwable e) {
            // in case not initialized
            logger.error(e);
        }
    }

    @Override
    public String[] getLatestAutoComplete() {
        String[] result = new String[0];
        try {
            return ClientCommandHandler.instance.latestAutoComplete;
        } catch (Throwable e) {
            // in case not initialized
            logger.error(e);
        }
        return result;
    }
}