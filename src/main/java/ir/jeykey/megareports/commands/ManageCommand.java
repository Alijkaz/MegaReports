package ir.jeykey.megareports.commands;

import ir.jeykey.megacore.utils.Common;
import ir.jeykey.megareports.gui.ReportsGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ManageCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                if (!(sender instanceof Player)) {
                        Common.send(sender,  "&cThis command is only applicable for players!");
                        return true;
                }

                if (!sender.hasPermission("megareports.manage")) {
                        Common.send(sender, "&cYou don't have &4&lmegareports.manage &cpermission needed for admin commands!");
                        return true;
                }

<<<<<<< HEAD
                new ReportsGUI((Player) sender).open();

                Common.send(sender, "Reports Management GUI has been opened for you.");
=======
                ReportsGUI.openGui((Player) sender);
                Common.send(sender, "&a&lReports Management GUI &ahas been opened for you.");
>>>>>>> 2b8b0eaac527152af20b00b3ee5f4f863584dea2

                return true;
        }
}
