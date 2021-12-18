package ir.jeykey.megareports.gui;

import ir.jeykey.megacore.gui.MegaGui;
import ir.jeykey.megacore.utils.MegaItem;
import ir.jeykey.megareports.MegaReports;
import ir.jeykey.megareports.config.Config;
import ir.jeykey.megareports.config.Messages;
import ir.jeykey.megareports.database.models.Report;
import ir.jeykey.megareports.database.models.TeleportMode;
import ir.jeykey.megacore.utils.Common;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.sql.SQLException;
import java.util.HashMap;

public class ManageReportGUI extends MegaGui {
        @Getter private final Report report;
        public static HashMap<Player, Report> WAITING_CLOSE_REASON = new HashMap<>();

        public ManageReportGUI(Report report, Player player) {
                super("&c#" + report.getId() + " &4- &cManage Report", 45, player);
                this.report = report;
        }

        @Override
        public void setup() {
                MegaItem reportItem = new MegaItem(
                        Material.SAND,
                        "&4&l#" + report.getId() + " &cReport " + report.getTarget(),
                        "",
                        "&bServer &a»  &4" + report.getServer(),
                        "&bReporter &a» &4" + report.getReporter(),
                        "&bTarget &a» &4" + report.getTarget(),
                        "&bReason &a» &4" + report.getReason(),
                        "&bWorld &a» &4" + report.getWorldName(),
                        "&bXYZ &a» &4" + Math.round(report.getLocation().getX()) + "," + Math.round(report.getLocation().getY()) + "," + Math.round(report.getLocation().getZ()),
                        "&bReported At&a » &4" + report.getCreatedAt()
                );

                MegaItem closeItem = new MegaItem(
                        Material.BARRIER,
                        "&cClose",
                        "",
                        "&4Close Management Menu"
                );

                MegaItem backItem = new MegaItem(
                        Material.COMPASS,
                        "&aBack",
                        "",
                        "&2Back to All Reports GUI"
                );

                // Management Items
                MegaItem teleportReportItem = new MegaItem(
                        Material.BARRIER,
                        "&aExit Teleport Mode",
                        "",
                        "&2Exit from teleport mode"
                );;

                if (!Report.PLAYERS_IN_TELEPORT_MODE.containsKey(getOwner())) {
                        teleportReportItem = new MegaItem(
                                Material.CARROT_STICK,
                                "&aTeleport",
                                "",
                                "&e&lLEFT CLICK &6» &eTeleport to Reporter",
                                "&e&lMIDDLE CLICK &6» &eTeleport to Report Location",
                                "&e&lRIGHT CLICK &6» &eTeleport to Target",
                                "",
                                "&● &2Teleport to report location &a●"
                        );
                }

                MegaItem closeReportItem = new MegaItem(
                        Material.EMERALD_BLOCK,
                        "&e✔ &6Open Report",
                        "",
                        "&6✔ &eReopen report"
                );

                if (report.getClosedAt() == null) {
                        closeReportItem = new MegaItem(
                                Material.REDSTONE_BLOCK,
                                "&e✘ &6Close Report",
                                "",
                                "&6✘ &eAssign report as closed"
                        );
                }

                MegaItem deleteReportItem = new MegaItem(
                        Material.REDSTONE_TORCH_ON,
                        "&4✘ &c&lDelete Report",
                        "",
                        "&c✘ &4&lCompletely delete report"
                );

                place(20, teleportReportItem, (player, itemStack, slot, clickType) -> {
                        if (!Report.PLAYERS_IN_TELEPORT_MODE.containsKey(getOwner())) {
                                if (clickType == ClickType.LEFT)
                                        report.teleport(player, TeleportMode.REPORTER_LOCATION);
                                else if (clickType == ClickType.MIDDLE)
                                        report.teleport(player, TeleportMode.REPORT_LOCATION);
                                else if (clickType == ClickType.RIGHT)
                                        report.teleport(player, TeleportMode.TARGET_LOCATION);
                        } else {
                                for (String cmd: Config.TELEPORT_EXIT_COMMANDS) {
                                        cmd = cmd.replace("%player%", player.getName());

                                        if (cmd.startsWith("[console]"))
                                                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd.replace("[console]", "").trim());
                                        else if (cmd.startsWith("[player]"))
                                                player.performCommand(cmd.replace("[player]", "").trim());
                                        else if (cmd.startsWith("[server]")) {
                                                if (Config.BUNGEECORD) {
                                                        String server = cmd.replace("[server]", "").trim();
                                                        MegaReports.getBungeeApi().connect(player, server);
                                                }
                                        }
                                }
                                Report.PLAYERS_IN_TELEPORT_MODE.remove(player);
                        }
                        player.closeInventory();
                });

                place(22, closeReportItem, (player, itemStack, slot, clickType) -> {
                        if (report.getClosedAt() == null) {
                                WAITING_CLOSE_REASON.put(player, report);
                                Common.send(player, "&aEnter your reason for closing report:");
                                player.closeInventory();
                        } else {
                                try {
                                        report.open();
                                } catch (SQLException ignored) {
                                        Common.send(player, Messages.DATABASE_ISSUE);
                                }

                                Common.send(player, "&aYou have successfully opened report &c#" + report.getId());
                                player.closeInventory();
                        }
                });

                place(24, deleteReportItem, (player, itemStack, slot, clickType) -> {
                        try {
                                report.delete();
                        } catch (SQLException ignored) {
                                Common.send(player, Messages.DATABASE_ISSUE);
                        }
                        Common.send(player, "&aYou have successfully deleted report &c#" + report.getId());
                        player.closeInventory();
                });

                place(8, backItem, (player, itemStack, slot, clickType) -> {
                        player.closeInventory();

                        new ReportsGUI(player).open();
                        Common.send(player, "&aAll Reports GUI has been opened for you.");
                });

                place(40, closeItem, (player, itemStack, slot, clickType) -> {
                        if (report.getClosedAt() == null) {
                                player.closeInventory();
                                Common.send(player, "&c&lManagement GUI &chas been closed for you.");
                        } else {
                                try {
                                        report.open();
                                } catch (SQLException ignored) {
                                        Common.send(player, Messages.DATABASE_ISSUE);
                                }
                                Common.send(player, "&aYou have successfully opened report &c#" + report.getId());
                                player.closeInventory();
                        }
                });

                place(0, reportItem);
        }
}
