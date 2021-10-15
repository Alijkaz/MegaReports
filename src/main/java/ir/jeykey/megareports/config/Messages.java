package ir.jeykey.megareports.config;

import ir.jeykey.megacore.config.Configurable;
import ir.jeykey.megacore.utils.Common;

public class Messages extends Configurable {
        public static String PREFIX;
        public static String COOLDOWN;
        public static String SUCCESSFUL;
        public static String NOTIFICATION;
        public static String MISSING_TARGET;
        public static String MISSING_ONLINE_TARGET;
        public static String MISSING_REASON;
        public static String TELEPORT;
        public static String TELEPORT_CROSS_SERVER;

        public Messages() {
                super("messages.yml");
        }

        @Override
        public void init() {
                PREFIX = Common.colorize(getConfig().getString("prefix"));
                COOLDOWN = Common.colorize(getConfig().getString("cooldown"));
                SUCCESSFUL = Common.colorize(getConfig().getString("successful"));
                NOTIFICATION = Common.colorize(getConfig().getString("notification"));
                MISSING_TARGET = Common.colorize(getConfig().getString("missing-target"));
                MISSING_ONLINE_TARGET = Common.colorize(getConfig().getString("missing-online-target"));
                MISSING_REASON = Common.colorize(getConfig().getString("missing-reason"));
                TELEPORT = Common.colorize(getConfig().getString("teleport-succesful"));
                TELEPORT_CROSS_SERVER = Common.colorize(getConfig().getString("teleport-another-server"));
        }
}
