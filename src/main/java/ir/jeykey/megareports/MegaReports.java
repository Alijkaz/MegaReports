package ir.jeykey.megareports;

import com.zaxxer.hikari.pool.HikariPool;
import ir.jeykey.megacore.MegaPlugin;
import ir.jeykey.megareports.commands.MainCommand;
import ir.jeykey.megareports.commands.ManageCommand;
import ir.jeykey.megareports.commands.ReportCommand;
import ir.jeykey.megareports.config.Config;
import ir.jeykey.megareports.config.Discord;
import ir.jeykey.megareports.config.Messages;
import ir.jeykey.megareports.config.Storage;
import ir.jeykey.megareports.database.DataSource;
import ir.jeykey.megareports.events.BungeeListener;
import ir.jeykey.megareports.events.ReportsGUI;
import ir.jeykey.megareports.events.PlayerQuit;
import ir.jeykey.megareports.gui.ManageReportGUI;
import ir.jeykey.megacore.utils.Common;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.sql.SQLException;

public final class MegaReports extends MegaPlugin {
        @Override
        public void onPluginEnable() {
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
                register("report", new ReportCommand());
                register("megareports", new MainCommand());
                register("reports", new ManageCommand());

                // Registering events
                register(new PlayerQuit());
                register(new ReportsGUI());

                // Registering BungeeCord messaging
                if (Config.BUNGEECORD) {
                        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
                        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeListener());
                }
        }

        @Override
        public void onPluginDisable() {
                // Registering BungeeCord messaging
                if (Config.BUNGEECORD) {
                        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
                        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
                }
        }

        public static void disablePlugin(boolean addPrefix, String... messages) {
                if (addPrefix)
                        Common.logPrefixed(messages);
                else
                        Common.log(messages);
                Bukkit.getPluginManager().disablePlugin(instance);
        }

}
