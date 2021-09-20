package ir.jeykey.megareports.events;

import ir.jeykey.megareports.database.models.Report;
import ir.jeykey.megareports.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

import java.util.Arrays;
import java.util.Date;

public class ManageGUI implements Listener {
        protected static Inventory getGui() {
                Inventory gui = Bukkit.createInventory(null, 45, Common.colorize("&cReports Management"));

                for (Report report: Report.all()) {

                        ItemStack reportItem = Common.createItem(
                                Material.SAND,
                                "&4&l#" + report.getId() + " &cReport " + report.getTarget(),
                                "",
                                "&cServer: &4" + report.getServer(),
                                "&cReporter: &4" + report.getReporter(),
                                "&cTarget: &4" + report.getTarget(),
                                "&cReported At: &4" + report.getReportedAt()
                        );

                        gui.addItem(
                                reportItem
                        );
                }

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

                int reportId = Integer.parseInt(clickedItemName.split(" ")[0].split("#")[1]);

                // Using slots click is a best option for your inventory click's
                Common.send(p, "&cYou opened report id " + reportId);
                
                // TODO open specific report management gui after clicking on any report so that user can moderate/close report
        }
}
