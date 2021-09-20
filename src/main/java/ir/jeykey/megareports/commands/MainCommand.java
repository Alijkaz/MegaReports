package ir.jeykey.megareports.commands;

import ir.jeykey.megareports.utils.Common;
import ir.jeykey.megareports.utils.YMLLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;

import java.io.IOException;

public class MainCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                if (args.length == 1) {
                        if (!sender.hasPermission("megareports.admin")) {
                                Common.send(sender, "&cYou don't have &4megareports.admin &cpermission needed for admin commands!");
                                return true;
                        }

                        switch(args[0]) {
                                case "reload":
                                        try {
                                                YMLLoader.load();
                                                Common.send(sender, "&aYou've fully re-loaded configuration files.");
                                        } catch (IOException|InvalidConfigurationException exception) {
                                                Common.send(sender, "&cYour config file is not valid and could not be loaded");
                                                exception.printStackTrace();
                                        }
                                        break;
                                default:
                                        Common.send(sender, "&cEntered arg is invalid! Use help command.");
                        }
                } else {
                        Common.send(sender, "&aMegaReports plugin is the one and only report plugin you'll ever need.");
                        return true;
                }

                return true;
        }

}
