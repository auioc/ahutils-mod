package org.auioc.mods.ahutils.utils.java;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public interface NetUtils {

    @Deprecated
    static String ipv4toString(SocketAddress addr) {
        String s = addr.toString();
        s = s.substring(s.indexOf("/") + 1);
        return s.substring(0, s.indexOf(":"));
    }

    static boolean isLocalAddress(InetAddress addr) {
        return addr.isLoopbackAddress() || addr.isAnyLocalAddress() || addr.isLinkLocalAddress();
    }

    static boolean isLocalAddress(String addr) {
        try {
            return isLocalAddress(InetAddress.getByName(addr));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }
    }

}
