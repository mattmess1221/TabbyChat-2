package mnm.mods.util;

import java.net.InetSocketAddress;

/**
 * Class for dealing with IPs.
 */
public class IPUtils {

    private static final int DEFAULT_PORT = 25565;

    /**
     * Gets the file safe address of an {@link InetSocketAddress}. Replaces all
     * the colons (:) with underscores (_) and surrounds the port in
     * parentheses.
     *
     * @return A string usable as a file name.
     */
    public static String getFileSafeAddress(InetSocketAddress addr) {
        String host = addr.getHostString();
        int port = addr.getPort();
        return host.replace(':', '_') + (port == DEFAULT_PORT ? "" : "(" + port + ")");
    }

}
