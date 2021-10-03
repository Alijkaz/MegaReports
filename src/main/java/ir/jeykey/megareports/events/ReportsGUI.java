package ir.jeykey.megareports.events;

import ir.jeykey.megareports.database.models.Report;
import ir.jeykey.megareports.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ReportsGUI implements Listener {
        protected static Inventory getGui() {
                Inventory gui = Bukkit.createInventory(null, 45, Common.colorize("&cReports Management"));


                for (Report report: Report.all()) {
                        ItemStack reportItem = Common.createItem(
                                Material.EMERALD,
                                "&2&l#" + report.getId() + " &aReport " + report.getTarget(),
                                "",
                                "&aServer: &2" + report.getServer(),
                                "&aReported At: &2" + report.getCreatedAt(),
                                "&aReported Closed At: &2" + report.getClosedAt(),
                                "&aClosed Reason: &2" + report.getClosedReason(),
                                "",
                                "&2&l[ &aCLICK TO MANAGE &2&l]"
                        );

                        if (report.getClosedAt() == null)
                                 reportItem = Common.createItem(
                                         Material.REDSTONE_BLOCK,
                                        "&4&l#" + report.getId() + " &cReport " + report.getTarget(),
                                        "",
                                        "&cServer: &4" + report.getServer(),
                                        "&cReporter: &4" + report.getReporter(),
                                        "&cTarget: &4" + report.getTarget(),
                                        "&cWorld: &4" + report.getWorldName(),
                                        "&cXYZ: &4" + Math.round(report.getLocation().getX()) + "," + Math.round(report.getLocation().getY()) + "," + Math.round(report.getLocation().getZ()),
                                        "&cReported At: &4" + report.getCreatedAt(),
                                        "",
                                        "&4&l[ &cCLICK TO MANAGE &4&l]"
                                );

                        gui.addItem(
                                reportItem
                        );
                }

                ItemStack closeItem = Common.createItem(
                        Material.BARRIER,
                        "&cClose",
                        "",
                        "&4Close Management Menu"
                );

                gui.setItem(40, closeItem);

                return gui;
        }

        public static void openGui(final HumanEntity ent) {
                ent.openInventory(getGui());
        }

        @EventHandler
        public void onInventoryClick(final InventoryClickEvent e) {
                if (!e.getInventory().getName().equals(getGui().getName())) return;

                e.setCancelled(true);

                final ItemStack clickedItem = e.getCurrentItem();

                // verify current item is not null
                if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

                final String clickedItemName = clickedItem.getItemMeta().getDisplayName();

                final Player p = (Player) e.getWhoClicked();

                if (clickedItemName.equalsIgnoreCase(Common.colorize("&cClose"))) {
                        p.closeInventory();
                        return;
                }

                // Getting report id from inventory name ( so i don't need nms / itemdata / tagdata and other bs )
                int reportId = Integer.parseInt(clickedItemName.split(" ")[0].split("#")[1]);

                // Finding and loading specific report
                Report report = new Report(reportId);
                report.load();

                // Using slots click is a best option for your inventory click's
                Common.send(p, "&cNow managing report id " + reportId);

                // Opening single report management gui
                ManageReportGUI.openGui(p, report);
        }
}
