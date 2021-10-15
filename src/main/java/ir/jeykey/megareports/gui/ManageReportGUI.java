package ir.jeykey.megareports.gui;

import ir.jeykey.megacore.utils.MegaItem;
import ir.jeykey.megacore.gui.MegaGUI;
import ir.jeykey.megareports.config.Config;
import ir.jeykey.megareports.database.models.Report;
import ir.jeykey.megareports.events.BungeeListener;
import ir.jeykey.megareports.events.ReportsGUI;
import ir.jeykey.megacore.utils.Common;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class ManageReportGUI extends MegaGUI {
        @Getter private Report report;
        @Getter private Player player;
        public static HashMap<Player, Report> WAITING_CLOSE_REASON = new HashMap<>();

        public ManageReportGUI(Report report, Player player) {
                super("&c#" + report.getId() + " &4- &cManage Report", 45);
                this.report = report;
                this.player = player;
        }

        @Override
        public void setup() {
                MegaItem reportItem = new MegaItem(
                        Material.SAND,
                        "&4&l#" + report.getId() + " &cReport " + report.getTarget(),
                        "",
                        "&cServer: &4" + report.getServer(),
                        "&cReporter: &4" + report.getReporter(),
                        "&cTarget: &4" + report.getTarget(),
                        "&cReason: &4" + report.getReason(),
                        "&cWorld: &4" + report.getWorldName(),
                        "&cXYZ: &4" + Math.round(report.getLocation().getX()) + "," + Math.round(report.getLocation().getY()) + "," + Math.round(report.getLocation().getZ()),
                        "&cReported At: &4" + report.getCreatedAt()
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
                MegaItem teleportReportItem;

                if (!Report.PLAYERS_IN_TELEPORT_MODE.containsKey(this.player)) {
                        teleportReportItem = new MegaItem(
                                Material.CARROT_STICK,
                                "&aTeleport",
                                "",
                                "&2Teleport to report location"
                        );
                } else {
                        teleportReportItem = new MegaItem(
                                Material.BARRIER,
                                "&aExit Teleport Mode",
                                "",
                                "&2Exit from teleport mode"
                        );
                }

                MegaItem closeReportItem = new MegaItem(
                        Material.EMERALD_BLOCK,
                        "&6Open Report",
                        "",
                        "&aReopen report"
                );

                if (report.getClosedAt() == null) {
                        closeReportItem = new MegaItem(
                                Material.REDSTONE_BLOCK,
                                "&6Close Report",
                                "",
                                "&eAssign report as closed"
                        );
                }

                MegaItem deleteReportItem = new MegaItem(
                        Material.REDSTONE_TORCH_ON,
                        "&cDelete Report",
                        "",
                        "&4Completely delete report"
                );

                place(20, teleportReportItem);

                place(22, closeReportItem);

                place(24, deleteReportItem);

                place(8, backItem);

                place(40, closeItem);

                place(0, reportItem);
        }

        @Override
        public void handle(Player p, ItemStack itemStack, int slot, ClickType clickType) {
                final String clickedItemName = itemStack.getItemMeta().getDisplayName();

                ItemStack reportItem = getInventory().getItem(0);

                if (reportItem == null) return;

                int reportId = Integer.parseInt(reportItem.getItemMeta().getDisplayName().split(" ")[0].split("#")[1]);

                // Finding and loading specific report
                Report report = new Report(reportId);
                report.load();

                if (slot == 8) {
                        p.closeInventory();
                        ReportsGUI.openGui(p);
                        Common.send(p, "&aAll Reports GUI has been opened for you.");
                } else if (slot == 40) {
                        p.closeInventory();
                        Common.send(p, "&cManagement GUI has been closed for you.");
                } else if (slot == 22) {
                        WAITING_CLOSE_REASON.put(p, report);
                        Common.send(p, "&aEnter your reason for closing report:");
                        p.closeInventory();
                } else if (clickedItemName.equalsIgnoreCase(Common.colorize("&6Open Report"))) {
                        report.open();
                        Common.send(p, "&aYou have successfully opened report &c#" + report.getId());
                        p.closeInventory();
                } else if (clickedItemName.equalsIgnoreCase(Common.colorize("&aTeleport"))) {
                        report.teleport(p);
                        p.closeInventory();
                } else if (clickedItemName.equalsIgnoreCase(Common.colorize("&aExit Teleport Mode"))) {
                        for (String cmd: Config.TELEPORT_EXIT_COMMANDS) {
                                cmd = cmd.replace("%player%", p.getName());

                                if (cmd.startsWith("[console]"))
                                        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd.replace("[console]", "").trim());
                                else if (cmd.startsWith("[player]"))
                                        p.performCommand(cmd.replace("[player]", "").trim());
                                else if (cmd.startsWith("[server]")) {
                                        if (Config.BUNGEECORD) {
                                                String server = cmd.replace("[server]", "").trim();
                                                BungeeListener.sendPlayerTo(p, server);
                                        }
                                }
                        }

                        Report.PLAYERS_IN_TELEPORT_MODE.remove(p);

                        p.closeInventory();
                } else if (clickedItemName.equalsIgnoreCase(Common.colorize("&cDelete Report"))) {
                        report.delete();
                        Common.send(p, "&aYou have successfully deleted report &c#" + report.getId());
                        p.closeInventory();
                }
        }
}
