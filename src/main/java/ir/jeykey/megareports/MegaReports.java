package ir.jeykey.megareports;

import com.j256.ormlite.dao.Dao;
import ir.jeykey.megacore.MegaPlugin;
import ir.jeykey.megareports.commands.MainCommand;
import ir.jeykey.megareports.commands.ManageCommand;
import ir.jeykey.megareports.commands.ReportCommand;
import ir.jeykey.megareports.config.*;
import ir.jeykey.megareports.database.DataSource;
import ir.jeykey.megareports.database.models.Report;
import ir.jeykey.megareports.events.BungeeListener;
import ir.jeykey.megareports.events.PlayerChat;
import ir.jeykey.megareports.events.PlayerQuit;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.sql.SQLException;

public final class MegaReports extends MegaPlugin {
        @Getter @Setter private static Dao<Report,String> reportsDao;

        @Override
        public void onPluginEnable() {
                // Creating/Loading configuration files
                getConfigManager().register(Config.class);

                getConfigManager().register(Storage.class);

                getConfigManager().register(Discord.class);

                getConfigManager().register(Messages.class);

                setPrefix(Messages.PREFIX);

                // Setting up datasource
                try {
                        if (Storage.LOCATION.equalsIgnoreCase("sqlite")) {
                                DataSource.SQLite();
                        } else if (Storage.LOCATION.equalsIgnoreCase("mysql")) {
                                DataSource.MySQL();
                        } else {
                                disablePlugin( "&cStorage type defined in config (" + Storage.LOCATION + ") is not valid!");
                                return;
                        }
                } catch (SQLException exception) {
                        exception.printStackTrace();
                        disablePlugin( "&cPlugin could not work with database! [ Check Stack Trace For More Information ]");
                        return;
                }
                catch (IOException exception) {
                        exception.printStackTrace();
                        disablePlugin("&cPlugin is unable to create database file, Please check directory permissions [ Check Stack Trace For More Information ]");
                        return;
                } catch (ClassNotFoundException exception) {
                        exception.printStackTrace();
                        disablePlugin( "&cIt seems that there's a problem with plugin and it could not be loaded, Please contact plugin developers [ Check Stack Trace For More Information ]");
                        return;
                }

                // Registering commands
                register("report", new ReportCommand());
                register("megareports", new MainCommand());
                register("reports", new ManageCommand());

                // Registering events
                register(new PlayerQuit());
                register(new PlayerChat());

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
}
