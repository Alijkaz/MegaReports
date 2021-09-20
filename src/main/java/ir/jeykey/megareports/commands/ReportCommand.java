package ir.jeykey.megareports.commands;

import ir.jeykey.megareports.utils.Common;
import ir.jeykey.megareports.utils.Cooldown;
import ir.jeykey.megareports.utils.YMLLoader;
import lombok.SneakyThrows;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class ReportCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

                if (!(sender instanceof Player)) {
                        Common.send(sender,  "&cThis command is only applicable for players!");
                        return true;
                }

                Player p = (Player) sender;

                if (YMLLoader.Config.COOLDOWN != -1) {
                        if (Cooldown.isCooldown(p.getUniqueId())) {
                                Common.send(
                                        sender,
                                        YMLLoader.Messages.COOLDOWN.replace(
                                                "%seconds%", Cooldown.getCooldownSeconds(p.getUniqueId()).toString()
                                        )
                                );
                                return true;
                        }
                }

                Common.send(sender, "Hale");

                return true;
        }
}
