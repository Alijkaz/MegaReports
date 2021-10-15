package ir.jeykey.megareports.config;

import ir.jeykey.megacore.config.Configurable;

import java.util.List;

public class Config extends Configurable {
        public static Integer COOLDOWN;
        public static String SERVER;
        public static boolean BUNGEECORD;
        public static List<String> TELEPORT_COMMANDS;
        public static List<String> TELEPORT_EXIT_COMMANDS;

        public static boolean REASON_REQUIRED;
        public static boolean ONLINE_TARGET_REQUIRED;

        public Config() {
                super("config.yml");
        }

        @Override
        public void init() {
                COOLDOWN = getConfig().getInt("cooldown");
                SERVER = getConfig().getString("server");
                BUNGEECORD = getConfig().getBoolean("bungeecord");
                TELEPORT_COMMANDS = getConfig().getStringList("teleport-enter-commands");
                TELEPORT_EXIT_COMMANDS = getConfig().getStringList("teleport-exit-commands");

                REASON_REQUIRED = getConfig().getBoolean("report-requirements.reason");
                ONLINE_TARGET_REQUIRED = getConfig().getBoolean("report-requirements.online-target");
        }
}
