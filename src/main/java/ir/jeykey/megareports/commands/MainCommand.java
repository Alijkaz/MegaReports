package ir.jeykey.megareports.commands;

import ir.jeykey.megacore.MegaPlugin;
import ir.jeykey.megareports.MegaReports;
import ir.jeykey.megareports.config.Config;
import ir.jeykey.megareports.config.Discord;
import ir.jeykey.megareports.config.Messages;
import ir.jeykey.megareports.config.Storage;
import ir.jeykey.megacore.utils.Common;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                if (args.length == 1) {
                        if (!sender.hasPermission("megareports.admin")) {
                                Common.send(sender, Messages.MISSING_ADMIN_PERMISSION);
                                return true;
                        }

                        if ("reload".equalsIgnoreCase(args[0])) {
                                MegaReports.getConfigManager().reloadAll();
                                Common.send(sender, Messages.CONFIG_RELOAD);
                        } else {
                                Common.send(sender, Messages.WRONG_USAGE);
                        }
                } else {
                        Common.send(sender, "&aMegaReports plugin is the one and only report plugin you'll ever need.");
                        return true;
                }

                return true;
        }

}
