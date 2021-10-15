package ir.jeykey.megacore.gui;

import ir.jeykey.megacore.MegaItem;
import ir.jeykey.megareports.MegaReports;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class MegaGUI implements Listener {
        @Getter private final String name;
        @Getter private final Inventory inventory;

        public MegaGUI(String name, int size) {
                this.name = name;
                this.inventory = Bukkit.createInventory(null, size, getName());

                setup();
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
                p.openInventory(getInventory());
        }

        public void register() {
                Bukkit.getServer().getPluginManager().registerEvents(this, MegaReports.getInstance());
        }

        /**
         * Handling click events
         * @param event InventoryClickEvent
         */
        @EventHandler
        protected void onGUIClickEvent(InventoryClickEvent event) {
                if (!event.getInventory().getName().equalsIgnoreCase(getName()))
                        return;
                if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
                        return;

                handle((Player) event.getWhoClicked(), event.getCurrentItem(), event.getSlot(), event.getClick());
        }
        
}
