package org.auioc.mods.ahutils.utils.java;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import javax.annotation.Nullable;
import org.auioc.mods.ahutils.utils.LogUtil;

public interface NetUtils {

    @Deprecated
    static String ipv4toString(SocketAddress addr) {
        String s = addr.toString();
        s = s.substring(s.indexOf("/") + 1);
        return s.substring(0, s.indexOf(":"));
    }

    @Nullable
    static InetAddress toInetAddress(String addr) {
        try {
            return InetAddress.getByName(addr);
        } catch (UnknownHostException e) {
            LogUtil.getModLogger().error(LogUtil.getMarker("NetUtils"), "", e);
            return null;
        }
    }

    static boolean isLocalAddress(InetAddress addr) {
        return addr.isLoopbackAddress() || addr.isAnyLocalAddress() || addr.isLinkLocalAddress();
    }

    static boolean isLocalAddress(String addr) {
        if (addr.contains("local:E:")) {
            return true;
        }
        return isLocalAddress(toInetAddress(addr));

    }

    static boolean isLanAddress(InetAddress addr) {
        return addr.isSiteLocalAddress();
    }

    static boolean isLanAddress(String addr) {
        if (addr.contains("local:E:")) {
            return true;
        }
        return isLanAddress(toInetAddress(addr));
    }

}