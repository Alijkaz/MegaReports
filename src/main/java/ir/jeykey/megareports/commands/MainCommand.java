package ir.jeykey.megareports.commands;

import ir.jeykey.megareports.config.Config;
import ir.jeykey.megareports.config.Discord;
import ir.jeykey.megareports.config.Messages;
import ir.jeykey.megareports.config.Storage;
import ir.jeykey.megareports.utils.Common;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;

public class MainCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                if (args.length == 1) {
                        if (!sender.hasPermission("megareports.admin")) {
                                Common.send(sender, "&cYou don't have &4megareports.admin &cpermission needed for admin commands!");
                                return true;
                        }

                        if ("reload".equalsIgnoreCase(args[0])) {
                                new Config().setup();

                                new Storage().setup();

                                new Discord().setup();

                                new Messages().setup();

                                Common.send(sender, "&aYou've fully re-loaded configuration files.");
                        } else {
                                Common.send(sender, "&cEntered arg is invalid! Use help command.");
                        }
                } else {
                        Common.send(sender, "&aMegaReports plugin is the one and only report plugin you'll ever need.");
                        return true;
                }

                return true;
        }

}
