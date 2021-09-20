package ir.jeykey.megareports;

import ir.jeykey.megareports.commands.ReportCommand;
import ir.jeykey.megareports.database.DataSource;
import ir.jeykey.megareports.events.PlayerQuit;
import ir.jeykey.megareports.utils.Common;
import ir.jeykey.megareports.utils.YMLLoader;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public final class MegaReports extends JavaPlugin {
        @Getter public static MegaReports instance;

        @Override
        public void onEnable() {
                // For calculating
                long start = System.currentTimeMillis();

                // Assigning instance
                instance = this;

                // Creating/Loading configuration files
                try {
                        YMLLoader.load();
                } catch(IOException|InvalidConfigurationException exception) {
                        disablePlugin(
                                "&cFailed to load config file. Please check config directory/file permissions",
                                "*vIf you believe this problem is from our plugin please contact development team",
                                "&cReason: &4" + exception.getMessage()
                        );
                }

                // Setting up datasource
                try {
                        if (YMLLoader.Config.STORAGE.equalsIgnoreCase("sqlite")) {
                                DataSource.SQLite();
                        } else if (YMLLoader.Config.STORAGE.equalsIgnoreCase("mysql")) {
                                DataSource.MySQL();
                        } else {
                                disablePlugin(
                                        "&cStorage type defined in config (" + YMLLoader.Config.STORAGE + ") is not valid!"
                                );
                        }
                } catch (SQLException exception) {
                        disablePlugin(
                                "&cPlugin could not connect to storage, Please check your database information!",
                                "&cStack Trace:",
                                exception.getStackTrace().toString()
                        );

                } catch (IOException exception) {
                        disablePlugin(
                                "&cPlugin is unable to create database file, Please check directory permissions",
                                "&cStack Trace:",
                                exception.getStackTrace().toString()
                        );
                } catch (ClassNotFoundException exception) {
                        disablePlugin(
                                "&cIt seems that there's a problem with plugin and it could not be loaded, Please contact plugin developers",
                                "&cStack Trace:",
                                exception.getStackTrace().toString()
                        );
                }

                // Registering commands
                getCommand("report").setExecutor(new ReportCommand());

                // Registering events
                Bukkit.getPluginManager().registerEvents(new PlayerQuit(), instance);

                // Finished loading plugin millis
                long end = System.currentTimeMillis();

                // Calculating plugin load time in milliseconds
                long time = end - start;

                // Logging MegaReports has been activated
                Common.log(
                        Common.repeat("&a&m=", 12, "&2"),
                        "&a&lMegaReports &aActivated",
                        "&a&lVersion: &2" + getDescription().getVersion(),
                        "&a&lTook: &2" + (end - start) + " ms",
                        Common.repeat("&a&m=", 12, "&2")
                );
        }

        @Override
        public void onDisable() {
                // Logging MegaReports has been deactivated
                Common.log(
                        Common.repeat("&c&m=", 12, "&4"),
                        "&c&lMegaReports &cDeactivated",
                        Common.repeat("&c&m=", 12, "&4")
                );
        }

        private void disablePlugin(String... messages) {
                Common.log(messages);
                Bukkit.getPluginManager().disablePlugin(instance);
        }

}
