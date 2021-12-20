package ir.jeykey.megareports.config;

import ir.jeykey.megacore.MegaPlugin;
import ir.jeykey.megacore.config.Configurable;
import ir.jeykey.megacore.utils.Common;

public class Messages extends Configurable {
        public static String PREFIX;
        public static String DATABASE_ISSUE;
        public static String WRONG_USAGE;

        public static String REPORT_COOLDOWN;
        public static String REPORT_SUCCESSFUL;
        public static String REPORT_MISSING_TARGET;
        public static String REPORT_MISSING_ONLINE_TARGET;
        public static String REPORT_MISSING_REASON;

        public static String MANAGEMENT_MISSING_PERMISSION;
        public static String MANAGEMENT_MENU_CLOSED;
        public static String MANAGEMENT_MENU_OPENED;
        public static String MANAGEMENT_MANAGING_REPORT;
        public static String MANAGEMENT_CLOSING_REASON;
        public static String MANAGEMENT_CLOSING_CANCELLED;
        public static String MANAGEMENT_REPORT_OPENED;
        public static String MANAGEMENT_REPORT_CLOSED;
        public static String MANAGEMENT_REPORT_DELETED;
        public static String MANAGEMENT_TELEPORT;
        public static String MANAGEMENT_TELEPORT_SERVER;
        public static String MANAGEMENT_NOTIFICATION;

        public static String ADMIN_MISSING_PERMISSION;
        public static String ADMIN_CONFIG_RELOADED;



        public Messages(MegaPlugin plugin) {
                super(plugin, "messages.yml");
        }

        @Override
        public void init() {
                PREFIX = Common.colorize(getConfig().getString("prefix"));
                DATABASE_ISSUE = Common.colorize(getConfig().getString("database-issue"));
                WRONG_USAGE = Common.colorize(getConfig().getString("wrong-usage"));

                REPORT_COOLDOWN = Common.colorize(getConfig().getString("report.cooldown"));
                REPORT_SUCCESSFUL = Common.colorize(getConfig().getString("report.successful"));
                REPORT_MISSING_TARGET = Common.colorize(getConfig().getString("report.missing-requirement.target"));
                REPORT_MISSING_ONLINE_TARGET = Common.colorize(getConfig().getString("report.missing-requirement.online-target"));
                REPORT_MISSING_REASON = Common.colorize(getConfig().getString("report.missing-requirement.reason"));

                MANAGEMENT_MISSING_PERMISSION = Common.colorize(getConfig().getString("management.missing-permission"));
                MANAGEMENT_MENU_OPENED = Common.colorize(getConfig().getString("management.menu-opened"));
                MANAGEMENT_MENU_CLOSED = Common.colorize(getConfig().getString("management.menu-closed"));
                MANAGEMENT_MANAGING_REPORT = Common.colorize(getConfig().getString("management.managing-report"));
                MANAGEMENT_CLOSING_REASON = Common.colorize(getConfig().getString("management.enter-closing-reason"));
                MANAGEMENT_CLOSING_CANCELLED = Common.colorize(getConfig().getString("management.closing-cancelled"));
                MANAGEMENT_REPORT_OPENED = Common.colorize(getConfig().getString("management.opened-report"));
                MANAGEMENT_REPORT_CLOSED = Common.colorize(getConfig().getString("management.closed-report"));
                MANAGEMENT_REPORT_DELETED = Common.colorize(getConfig().getString("management.deleted-report"));
                MANAGEMENT_TELEPORT = Common.colorize(getConfig().getString("management.teleport"));
                MANAGEMENT_TELEPORT_SERVER = Common.colorize(getConfig().getString("management.teleport-server"));
                MANAGEMENT_NOTIFICATION = Common.colorize(getConfig().getString("management.notification"));

                ADMIN_MISSING_PERMISSION = Common.colorize(getConfig().getString("admin.missing-permission"));
                ADMIN_CONFIG_RELOADED = Common.colorize(getConfig().getString("admin.config-reloaded"));
        }
}
