package ir.jeykey.megareports.config;

import ir.jeykey.megacore.MegaPlugin;
import ir.jeykey.megacore.config.Configurable;
import ir.jeykey.megacore.utils.Common;

public class Messages extends Configurable {
        public static String PREFIX;
        public static String COOLDOWN;
        public static String SUCCESSFUL;
        public static String DATABASE_ISSUE;
        public static String NOTIFICATION;
        public static String MISSING_TARGET;
        public static String MISSING_ONLINE_TARGET;
        public static String MISSING_REASON;
        public static String TELEPORT;
        public static String TELEPORT_CROSS_SERVER;
        public static String MANAGEMENT_GUI_OPENED;
        public static String MANAGEMENT_GUI_CLOSED;
        public static String MISSING_MANAGE_PERMISSION;
        public static String MISSING_ADMIN_PERMISSION;
        public static String WRONG_USAGE;
        public static String CONFIG_RELOAD;

        public Messages(MegaPlugin plugin) {
                super(plugin, "messages.yml");
        }

        @Override
        public void init() {
                PREFIX = Common.colorize(getConfig().getString("prefix"));
                COOLDOWN = Common.colorize(getConfig().getString("cooldown"));
                SUCCESSFUL = Common.colorize(getConfig().getString("successful"));
                DATABASE_ISSUE = Common.colorize(getConfig().getString("database-issue"));
                NOTIFICATION = Common.colorize(getConfig().getString("notification"));
                MISSING_TARGET = Common.colorize(getConfig().getString("missing-target"));
                MISSING_ONLINE_TARGET = Common.colorize(getConfig().getString("missing-online-target"));
                MISSING_REASON = Common.colorize(getConfig().getString("missing-reason"));
                TELEPORT = Common.colorize(getConfig().getString("teleport-succesful"));
                TELEPORT_CROSS_SERVER = Common.colorize(getConfig().getString("teleport-another-server"));
                MANAGEMENT_GUI_OPENED = Common.colorize(getConfig().getString("management-gui-opened"));
                MANAGEMENT_GUI_CLOSED = Common.colorize(getConfig().getString("management-gui-closed"));
                MISSING_MANAGE_PERMISSION = Common.colorize(getConfig().getString("missing-manage-permission"));
                MISSING_ADMIN_PERMISSION = Common.colorize(getConfig().getString("missing-admin-permission"));
                WRONG_USAGE = Common.colorize(getConfig().getString("wrong-usage"));
                CONFIG_RELOAD = Common.colorize(getConfig().getString("config-reload"));
        }
}
