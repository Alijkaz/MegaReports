package ir.jeykey.megareports.commands;

import ir.jeykey.megacore.utils.Common;
import ir.jeykey.megareports.config.Messages;
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
                        Common.send(sender, Messages.MISSING_MANAGE_PERMISSION);
                        return true;
                }

                new ReportsGUI((Player) sender).open();

                Common.send(sender, Messages.MANAGEMENT_GUI_OPENED);

                return true;
        }
}
