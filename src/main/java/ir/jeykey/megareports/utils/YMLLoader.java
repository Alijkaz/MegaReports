package ir.jeykey.megareports.utils;

import ir.jeykey.megareports.MegaReports;
import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

// ------------------------------------
//       Handling Config Files
// ------------------------------------
public class YMLLoader {

        private static File configFile;
        private static File messagesFile;
        @Getter private static FileConfiguration config;
        @Getter private static FileConfiguration messages;

        /**
         * This method will fully load config files
         *
         * @throws IOException Will be thrown if config file is not readable
         * @throws InvalidConfigurationException Will be thrown if config file is not valid
         */
        public static void load() throws IOException, InvalidConfigurationException {
                // Loading and assigning config.yml
                configFile = createConfig("config.yml");
                config = new YamlConfiguration();
                config.load(configFile);
                Config.init();

                // Loading and assigning messages.yml
                messagesFile = createConfig("messages.yml");
                messages = new YamlConfiguration();
                messages.load(messagesFile);
                Messages.init();
        }

        /**
         * This method will attempt to create config.yml/messages.yml file with default values
         *
         * @param filename Config file name that will be created/loaded
         * @return Config file in plugins data folder
         */
        public static File createConfig(String filename)
        {
                // Config file in plugins data folder
                File file = new File(MegaReports.getInstance().getDataFolder(), filename);

                // Checking if config exists, if not creating one with default values
                if (!file.exists()) {
                        file.getParentFile().mkdirs();
                        MegaReports.getInstance().saveResource(filename, false);
                }

                return file;
        }

        public static class Config {
                public static String STORAGE;

                public static String MYSQL_USERNAME;
                public static String MYSQL_PASSWORD;
                public static String MYSQL_HOST;
                public static String MYSQL_DB;
                public static String MYSQL_PORT;

                public static Integer COOLDOWN;
                public static String SERVER;
                public static boolean REASON_REQUIRED;

                public static boolean DISCORD_ENABLED;
                public static String DISCORD_WEBHOOK;

                public static void init() {
                        STORAGE = YMLLoader.getConfig().getString("storage").toLowerCase();

                        MYSQL_USERNAME = YMLLoader.getConfig().getString("mysql-username");
                        MYSQL_PASSWORD = YMLLoader.getConfig().getString("mysql-password");
                        MYSQL_HOST = YMLLoader.getConfig().getString("mysql-host");
                        MYSQL_DB = YMLLoader.getConfig().getString("mysql-database");
                        MYSQL_PORT = YMLLoader.getConfig().getString("mysql-port");

                        COOLDOWN = YMLLoader.getConfig().getInt("cooldown");
                        SERVER = YMLLoader.getConfig().getString("server");
                        REASON_REQUIRED = YMLLoader.getConfig().getBoolean("reason-required");

                        DISCORD_ENABLED = YMLLoader.getConfig().getBoolean("discord.enable");
                        DISCORD_WEBHOOK = YMLLoader.getConfig().getString("discord.webhook");
                }
        }


        public static class Messages {
                public static String PREFIX;
                public static String COOLDOWN;
                public static String SUCCESSFUL;
                public static String MISSING_TARGET;
                public static String MISSING_REASON;

                public static void init() {
                        PREFIX = Common.colorize(YMLLoader.getMessages().getString("prefix"));
                        COOLDOWN = Common.colorize(YMLLoader.getMessages().getString("cooldown"));
                        SUCCESSFUL = Common.colorize(YMLLoader.getMessages().getString("successful"));
                        MISSING_TARGET = Common.colorize(YMLLoader.getMessages().getString("missing-target"));
                        MISSING_REASON = Common.colorize(YMLLoader.getMessages().getString("missing-reason"));

                }
        }
}