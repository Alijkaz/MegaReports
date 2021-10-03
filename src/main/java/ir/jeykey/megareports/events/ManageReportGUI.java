package ir.jeykey.megareports.events;

import ir.jeykey.megareports.database.models.Report;
import ir.jeykey.megareports.utils.Common;
import ir.jeykey.megareports.utils.YMLLoader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class ManageReportGUI implements Listener {
        public static HashMap<Player, Report> WAITING_CLOSE_REASON = new HashMap<>();

        protected static Inventory getGui(Report report) {
                Inventory gui = Bukkit.createInventory(null, 45, Common.colorize( "&c#" + report.getId() + " &4- &cManage Report"));

                ItemStack reportItem = Common.createItem(
                        Material.SAND,
                        "&4&l#" + report.getId() + " &cReport " + report.getTarget(),
                        "",
                        "&cServer: &4" + report.getServer(),
                        "&cReporter: &4" + report.getReporter(),
                        "&cTarget: &4" + report.getTarget(),
//                        "&cWorld: &4" + report.getLocation().getWorld().getName(),
                        "&cXYZ: &4" + Math.round(report.getLocation().getX()) + "," + Math.round(report.getLocation().getY()) + "," + Math.round(report.getLocation().getZ()),
                        "&cReported At: &4" + report.getCreatedAt()
                );

                ItemStack closeItem = Common.createItem(
                        Material.BARRIER,
                        "&cClose",
                        "",
                        "&4Close Management Menu"
                );

                ItemStack backItem = Common.createItem(
                        Material.COMPASS,
                        "&aBack",
                        "",
                        "&2Back to All Reports GUI"
                );

                // Management Items
                ItemStack teleportReportItem = Common.createItem(
                        Material.CARROT_STICK,
                        "&aTeleport",
                        "",
                        "&2Teleport to report location"
                );

                ItemStack closeReportItem = Common.createItem(
                        Material.EMERALD_BLOCK,
                        "&6Open Report",
                        "",
                        "&aReopen report"
                );

                if (report.getClosedAt() == null) {
                        closeReportItem = Common.createItem(
                                Material.REDSTONE_BLOCK,
                                "&6Close Report",
                                "",
                                "&eAssign report as closed"
                        );
                }

                ItemStack deleteReportItem = Common.createItem(
                        Material.REDSTONE_TORCH_ON,
                        "&cDelete Report",
                        "",
                        "&4Completely delete report"
                );

                gui.setItem(20, teleportReportItem);

                gui.setItem(22, closeReportItem);

                gui.setItem(24, deleteReportItem);

                gui.setItem(8, backItem);

                gui.setItem(40, closeItem);

                gui.addItem(
                        reportItem
                );

                return gui;
        }

        public static void openGui(final HumanEntity ent, final Report report) {
                ent.openInventory(getGui(report));
        }

        @EventHandler
        public void onPlayerChat(final AsyncPlayerChatEvent e) {
                // TODO checking for close reason

                if (WAITING_CLOSE_REASON.containsKey(e.getPlayer())) {
                        Report report = WAITING_CLOSE_REASON.get(e.getPlayer());

                        report.setClosedReason(e.getMessage());

                        report.close();

                        Common.send(e.getPlayer(), "&aYou have successfully closed report &c#" + report.getId() + " &awith reason: &2" + e.getMessage());

                        WAITING_CLOSE_REASON.remove(e.getPlayer());

                        e.setCancelled(true);
                }

        }

        @EventHandler
        public void onInventoryClick(final InventoryClickEvent e) {
                if (!e.getInventory().getName().contains("Manage Report")) return;

                e.setCancelled(true);

                final ItemStack clickedItem = e.getCurrentItem();

                // verify current item is not null
                if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

                final String clickedItemName = clickedItem.getItemMeta().getDisplayName();

                final Player p = (Player) e.getWhoClicked();

                ItemStack reportItem = e.getInventory().getItem(0);

                if (reportItem == null) return;

                int reportId = Integer.parseInt(reportItem.getItemMeta().getDisplayName().split(" ")[0].split("#")[1]);

                // Finding and loading specific report
                Report report = new Report(reportId);
                report.load();

                if (clickedItemName.equalsIgnoreCase(Common.colorize("&aBack"))) {
                        p.closeInventory();
                        ReportsGUI.openGui(p);
                        Common.send(p, "&aAll Reports GUI has been opened for you.");
                } else if (clickedItemName.equalsIgnoreCase(Common.colorize("&cClose"))) {
                        p.closeInventory();
                        Common.send(p, "&cManagement GUI has been closed for you.");
                } else if (clickedItemName.equalsIgnoreCase(Common.colorize("&6Close Report"))) {
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
                } else if (clickedItemName.equalsIgnoreCase(Common.colorize("&cDelete Report"))) {
                        report.delete();
                        Common.send(p, "&aYou have successfully deleted report &c#" + report.getId());
                        p.closeInventory();
                }
        }
}
