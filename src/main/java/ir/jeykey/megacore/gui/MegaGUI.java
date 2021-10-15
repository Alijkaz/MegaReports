package ir.jeykey.megacore.gui;

import ir.jeykey.megacore.MegaPlugin;
import ir.jeykey.megacore.utils.MegaItem;
import ir.jeykey.megareports.MegaReports;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class MegaGUI {
        @Getter private final String name;
        @Getter private final Inventory inventory;

        public MegaGUI(String name, int size) {
                this.name = name;
                this.inventory = Bukkit.createInventory(null, size, getName());
        }

        public abstract void setup();

        public abstract void handle(Player p, ItemStack itemStack, int slot, ClickType clickType);

        public void fill(MegaItem item) {
                for (int i = 0; i < getInventory().getSize(); i++) {
                        if (getInventory().getItem(i) == null) place(i, item);
                }
        }

        public void place(int i, ItemStack itemStack) {
                getInventory().setItem(i, itemStack);
        }

        public void place(int i, MegaItem item) {
                place(i, item.getItemStack());
        }

        public void open(Player p) {
                setup();
                register();
                p.openInventory(getInventory());
        }

        public void register() {
                if (!MegaPlugin.registeredGuis.contains(this)) MegaPlugin.registeredGuis.add(this);
        }
}
