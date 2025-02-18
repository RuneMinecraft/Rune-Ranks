package net.runemc.utils;

import net.runemc.plugin.Main;
import org.bukkit.Bukkit;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public final class Logger {
    public static void log(String ... s) {
        List.of(s).forEach((message) -> Main.get().getLogger().info(Color.WHITE+"[INFO] [Rune-Core] "+message+ Color.RESET));
    }
    public static void logRaw(String ... s) {
        List.of(s).forEach((message) -> Main.get().getLogger().info(Color.WHITE+"[INFO] "+message+ Color.RESET));
    }
    public static void warn(String ... s) {
        List.of(s).forEach((message) -> Main.get().getLogger().info(Color.YELLOW+"[WARN] [Rune-Core] "+message+ Color.RESET));
    }
    public static void warnRaw(String ... s) {
        List.of(s).forEach((message) -> Main.get().getLogger().info(Color.YELLOW+"[WARN] "+message+ Color.RESET));
    }
    public static void error(String ... s) {
        List.of(s).forEach((message) -> Main.get().getLogger().info(Color.RED+"[ERROR] [Rune-Core] "+message+ Color.RESET));
    }
    public static void errorRaw(String ... s) {
        List.of(s).forEach((message) -> Main.get().getLogger().info(Color.RED+"[ERROR] "+message+ Color.RESET));
    }

    public static class Color {
        public static final String RESET = "\u001B[0m";
        public static final String WHITE = "\u001B[37m";
        public static final String YELLOW = "\u001B[33m";
        public static final String RED = "\u001B[31m";
    }

    private static String getTime() {
        var formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }
}
