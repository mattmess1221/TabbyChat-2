package mnm.mods.tabbychat.util;

import mnm.mods.util.ForgeUtils;
import mnm.mods.util.LogHelper;
import mnm.mods.util.ReflectionHelper;

public class ForgeClientCommands {

    private static final LogHelper logger = LogHelper.getLogger();

    public static void autoComplete(String word, String s1) {
        if (ForgeUtils.FORGE_INSTALLED) {
            try {
                Class<?> cl = getClientCommandHandler();
                Object instance = getClientCommandHalderInstance();
                ReflectionHelper.invokeMethod(cl, instance, "autoComplete",
                        new String[] { word, s1 });
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

    public static String[] getLatestAutoComplete() {
        String[] result = new String[0];
        if (ForgeUtils.FORGE_INSTALLED) {
            try {
                Class<?> cl = getClientCommandHandler();
                Object instance = getClientCommandHalderInstance();
                ReflectionHelper.getFieldValue(cl, instance, "latestAutoComplete");
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return result;
    }

    private static Class<?> getClientCommandHandler() throws ClassNotFoundException {
        Class<?> result = null;
        result = Class.forName("net.minecraftforge.client.ClientCommandHandler");
        return result;
    }

    private static Object getClientCommandHalderInstance() throws ClassNotFoundException,
            IllegalAccessException, NoSuchFieldException {
        Object result = null;
        Class<?> cl = getClientCommandHandler();
        result = ReflectionHelper.getFieldValue(cl, null, "instance");
        return result;
    }

}
