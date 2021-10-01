package org.auioc.mods.ahutils.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class LogUtil {

    private static StackTraceElement getCaller() {
        StackTraceElement[] callStack = Thread.currentThread().getStackTrace();

        StackTraceElement caller = null;

        String logClassName = LogUtil.class.getName();

        int i = 0;
        for (int len = callStack.length; i < len; i++) {
            if (logClassName.equals(callStack[i].getClassName())) {
                break;
            }
        }

        caller = callStack[i + 3];
        return caller;
    }

    private static Logger getLoggerByCaller() {
        return LogManager.getLogger(getCaller().getClassName());
    }

    private static Logger getModLogger() {
        return LogManager.getLogger("ahutils");
    }



    public static void trace(String msg) {
        getLoggerByCaller().trace(msg);
    }

    public static void debug(String msg) {
        getLoggerByCaller().debug(msg);
    }

    public static void info(String msg) {
        getLoggerByCaller().info(msg);
    }

    public static void warn(String msg) {
        getLoggerByCaller().warn(msg);
    }

    public static void error(String msg) {
        getLoggerByCaller().error(msg);
    }

    public static void error(String msg, Throwable t) {
        getLoggerByCaller().error(msg, t);
    }

    public static void log(Level level, String message, Object... fields) {
        getLoggerByCaller().log(level, message, fields);
    }



    public static Marker getMarker(String marker) {
        return MarkerManager.getMarker(marker);
    }


    public static void trace(Marker marker, String msg) {
        getModLogger().trace(marker, msg);
    }

    public static void debug(Marker marker, String msg) {
        getModLogger().debug(marker, msg);
    }

    public static void info(Marker marker, String msg) {
        getModLogger().info(marker, msg);
    }

    public static void warn(Marker marker, String msg) {
        getModLogger().warn(marker, msg);
    }

    public static void error(Marker marker, String msg) {
        getModLogger().error(marker, msg);
    }

    public static void error(Marker marker, String msg, Throwable t) {
        getModLogger().error(marker, msg, t);
    }

    public static void log(Marker marker, Level level, String message, Object... fields) {
        getModLogger().log(level, marker, message, fields);
    }

}
