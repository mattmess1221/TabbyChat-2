package mnm.mods.util;

import io.netty.util.NetUtil;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Class for dealing with IPs.
 */
public class IPUtils {

    private static final int DEFAULT_PORT = 25565;

    private String host;
    private int port;
    private boolean ipv6;

    /**
     * Creates a new IP address with the given ip and port.
     *
     * @param address The ip
     * @param port The port
     * @param ipv6 If the address is ipv6
     */
    public IPUtils(String address, int port, boolean ipv6) {
        this.host = address;
        this.port = port;
        this.ipv6 = ipv6;
    }

    /**
     * Parses a string address. Default port is 25565.
     *
     * @param ipString The full string address with or without port
     * @return The parsed IP
     */
    public static IPUtils parse(String ipString) {
        IPUtils result;
        switch (getType(ipString)) {
        case NAME:
        case IPV4:
            if (ipString.contains(":")) {
                String host = ipString.substring(0, ipString.lastIndexOf(':'));
                int port = Integer.parseInt(ipString.substring(ipString.lastIndexOf(':') + 1));
                result = new IPUtils(host, port, false);
            } else {
                result = new IPUtils(ipString, DEFAULT_PORT, false);
            }
            break;
        case IPV6:
            if (ipString.startsWith("[") && ipString.contains("]:")) {
                String host = ipString.substring(0, ipString.lastIndexOf(':'));
                int port = Integer.parseInt(ipString.substring(ipString.lastIndexOf(':') + 1));
                result = new IPUtils(host, port, true);
            } else {
                result = new IPUtils(ipString, DEFAULT_PORT, true);
            }
            break;
        default:
            throw new RuntimeException("This shouldn't have happened");
        }
        if (result.getHost().isEmpty()) {
            result.host = "localhost";
            result.ipv6 = false;
        }
        return result;
    }

    private static ConnectionType getType(String ipAddress) {
        ConnectionType result;
        if (NetUtil.isValidIpV4Address(ipAddress)) {
            result = ConnectionType.IPV4;
        } else if (NetUtil.isValidIpV6Address(ipAddress)) {
            result = ConnectionType.IPV6;
        } else {
            result = ConnectionType.NAME;
        }
        return result;
    }

    /**
     * Gets the host part of this address.
     *
     * @return The host
     */
    public String getHost() {
        return this.host;
    }

    /**
     * Gets the port of this address.
     *
     * @return The port
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Gets if this ip has a port.
     *
     * @return True if ip has a port, false if it doesn't.
     */
    public boolean hasPort() {
        return this.port != DEFAULT_PORT;
    }

    /**
     * Gets if this ip is IPv6.
     *
     * @return True if IP is IPv6, false if it isn't.
     */
    public boolean isIPv6() {
        return this.ipv6;
    }

    /**
     * Gets the full address of this ip. If it has a port, it is included.
     *
     * @return The full address
     */
    public String getAddress() {
        return this.host + (hasPort() ? "" : ":" + port);
    }

    /**
     * Gets the {@link SocketAddress} for this IP.
     *
     * @return The SocketAddress
     */
    public SocketAddress getSocketAddress() {
        return InetSocketAddress.createUnresolved(host, port);
    }

    /**
     * Gets the file safe address of this IP. Replaces all the colons (:) with
     * underscores (_) and surrounds the port in parentheses.
     *
     * @return A string usable as a file name.
     */
    public String getFileSafeAddress() {
        return this.host.replace(':', '_') + (hasPort() ? "" : "(" + port + ")");
    }

    private enum ConnectionType {
        IPV4,
        IPV6,
        NAME
    }

}
