package ir.jeykey.megareports;

import com.zaxxer.hikari.pool.HikariPool;
import ir.jeykey.megareports.commands.MainCommand;
import ir.jeykey.megareports.commands.ManageCommand;
import ir.jeykey.megareports.commands.ReportCommand;
import ir.jeykey.megareports.config.Config;
import ir.jeykey.megareports.config.Discord;
import ir.jeykey.megareports.config.Messages;
import ir.jeykey.megareports.config.Storage;
import ir.jeykey.megareports.database.DataSource;
import ir.jeykey.megareports.events.ManageReportGUI;
import ir.jeykey.megareports.events.MessageListener;
import ir.jeykey.megareports.events.ReportsGUI;
import ir.jeykey.megareports.events.PlayerQuit;
import ir.jeykey.megareports.utils.Common;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.SQLException;

public final class MegaReports extends JavaPlugin {
        @Getter public static MegaReports instance;

        @Override
        public void onEnable() {
                // For calculating
                long start = System.currentTimeMillis();

                // Assigning instance
                instance = this;

                // Creating/Loading configuration files
                new Config().setup();

                new Storage().setup();

                new Discord().setup();

                new Messages().setup();

                // Setting up datasource
                try {
                        if (Storage.LOCATION.equalsIgnoreCase("sqlite")) {
                                DataSource.SQLite();
                        } else if (Storage.LOCATION.equalsIgnoreCase("mysql")) {
                                DataSource.MySQL();
                        } else {
                                disablePlugin(true, "&cStorage type defined in config (" + Storage.LOCATION + ") is not valid!");
                                return;
                        }
                } catch (SQLException|HikariPool.PoolInitializationException exception) {
                        disablePlugin(true, "&cPlugin could not work with database! [ Check Stack Trace For More Information ]");
                        return;
                }
                catch (IOException exception) {
                        disablePlugin(true,"&cPlugin is unable to create database file, Please check directory permissions [ Check Stack Trace For More Information ]");
                        return;
                } catch (ClassNotFoundException exception) {
                        disablePlugin(true, "&cIt seems that there's a problem with plugin and it could not be loaded, Please contact plugin developers [ Check Stack Trace For More Information ]");
                        return;
                }

                // Registering commands
                getCommand("report").setExecutor(new ReportCommand());
                getCommand("megareports").setExecutor(new MainCommand());
                getCommand("reports").setExecutor(new ManageCommand());

                // Registering events
                Bukkit.getPluginManager().registerEvents(new PlayerQuit(), instance);
                Bukkit.getPluginManager().registerEvents(new ReportsGUI(), instance);
                Bukkit.getPluginManager().registerEvents(new ManageReportGUI(), instance);

                // Finished loading plugin millis
                long end = System.currentTimeMillis();

                // Calculating plugin load time in milliseconds
                long time = end - start;

                // Registering BungeeCord messaging
                if (Config.BUNGEECORD) {
                        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
                        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new MessageListener());
                }

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
                // Registering BungeeCord messaging
                if (Config.BUNGEECORD) {
                        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
                        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
                }

                // Logging MegaReports has been deactivated
                Common.log(
                        Common.repeat("&c&m=", 12, "&4"),
                        "&c&lMegaReports &cDeactivated",
                        Common.repeat("&c&m=", 12, "&4")
                );
        }

        public static void disablePlugin(boolean addPrefix, String... messages) {
                if (addPrefix)
                        Common.logPrefixed(messages);
                else
                        Common.log(messages);
                Bukkit.getPluginManager().disablePlugin(instance);
        }

}
