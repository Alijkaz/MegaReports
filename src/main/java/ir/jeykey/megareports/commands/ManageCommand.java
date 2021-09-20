package ir.jeykey.megareports.commands;

import ir.jeykey.megareports.events.ManageGUI;
import ir.jeykey.megareports.utils.Common;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class ManageCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                if (!(sender instanceof Player)) {
                        Common.send(sender,  "&cThis command is only applicable for players!");
                        return true;
                }

                if (!sender.hasPermission("megareports.manage")) {
                        Common.send(sender, "&cYou don't have &4megareports.manage &cpermission needed for admin commands!");
                        return true;
                }

                ManageGUI.openGui((Player) sender);
                Common.send(sender, "Reports Management GUI has been opened for you.");

                return true;
        }
}
