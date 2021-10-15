package ir.jeykey.megareports.commands;

import ir.jeykey.megareports.config.Config;
import ir.jeykey.megareports.config.Messages;
import ir.jeykey.megareports.database.models.Report;
import ir.jeykey.megacore.utils.Common;
import ir.jeykey.megareports.utils.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedList;

public class ReportCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

                if (!(sender instanceof Player)) {
                        Common.send(sender,  "&cThis command is only applicable for players!");
                        return true;
                }

                if (!sender.hasPermission("megareports.use.report")) {
                        Common.send(sender, "&cYou don't have &4megareports.use.report &cpermission needed for admin commands!");
                        return true;
                }

                Player p = (Player) sender;

                if (args.length < 1) {
                        Common.send(sender, Messages.MISSING_TARGET.replace("%reporter%", p.getName()));
                        return true;
                } else if (args.length == 1) {
                        if (Config.REASON_REQUIRED) {
                                Common.send(sender, Messages.MISSING_REASON.replace("%reporter%", p.getName()));
                                return true;
                        }
                }


                // Getting target
                String target = args[0];

                // Getting rest of args as reason
                LinkedList<String> argsCopy = new LinkedList<String>(Arrays.asList(args));
                argsCopy.removeFirst();
                String reason = String.join(" ", argsCopy);

                if (Config.ONLINE_TARGET_REQUIRED) {
                        if (Bukkit.getServer().getPlayerExact(target) == null) {
                                Common.send(
                                        sender,
                                        Messages.MISSING_ONLINE_TARGET
                                                .replace("%reporter%", sender.getName())
                                                .replace("%target%", target)
                                );
                                return true;
                        }
                }

                if (Config.COOLDOWN != -1) {
                        if (Cooldown.isCooldown(p.getUniqueId())) {
                                Common.send(
                                        sender,
                                        Messages.COOLDOWN.replace(
                                                "%seconds%", Cooldown.getCooldownSeconds(p.getUniqueId()).toString()
                                        )
                                );
                                return true;
                        }
                }

                // Creating report model
                Report report = new Report(p.getName(), target, reason, p.getLocation());

                // Saving report to database
                report.save();

                // Sending successful
                Common.send(
                        sender,
                        Messages.SUCCESSFUL
                                .replace("%reporter%", p.getName())
                                .replace("%target%", target)
                                .replace("%reason%", reason)
                );

                // Send notification for staff online
                // TODO add cross proxy notification system
                for (Player onlinePlayer: Bukkit.getOnlinePlayers()) {
                        if (onlinePlayer.hasPermission("megareports.notify")) {
                                Common.send(
                                        onlinePlayer,
                                        Messages.NOTIFICATION
                                                .replace("%reporter%", p.getName())
                                                .replace("%target%", target)
                                                .replace("%reason%", reason)
                                                .replace("%server%", report.getServer())
                                );
                        }
                }

                return true;
        }
}
