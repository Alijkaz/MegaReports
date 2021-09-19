package ir.jeykey.megareports.utils;

import ir.jeykey.megareports.MegaReports;
import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {
        private static File configFile;
        @Getter private static FileConfiguration config;

        // ------------------------------------
        //       Handling Config File
        // ------------------------------------

        /**
         * This method will attempt to create config.yml file with default values
         * And load configurations as a valid yml config
         *
         * @throws IOException Will be thrown if config file is not readable
         * @throws InvalidConfigurationException Will be thrown if config file is not valid
         */
        public static void createConfig() throws IOException, InvalidConfigurationException {
                // config.yml file in plugins data folder
                configFile = new File(MegaReports.getInstance().getDataFolder(), "config.yml");

                // Checking if config exists, if not creating one with default values
                if (!configFile.exists()) {
                        configFile.getParentFile().mkdirs();
                        MegaReports.getInstance().saveResource("config.yml", false);
                }

                // Loading and assigning yml config
                config = new YamlConfiguration();
                config.load(configFile);
        }

        /**
         * This method is used to reload configurations (config.yml)
         */
        public static void reloadConfig() {
                try {
                        config.load(configFile);
                } catch (IOException | InvalidConfigurationException e) {
                        e.printStackTrace();
                }
        }

        // ------------------------------------
        //       Reading Config Values
        // ------------------------------------

        final static String PREFIX;

        static {
                PREFIX = getConfig().getString("prefix");
        }

}
