package ir.jeykey.megacore.gui;

import ir.jeykey.megacore.MegaPlugin;
import ir.jeykey.megacore.utils.Common;
import ir.jeykey.megacore.utils.MegaItem;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class MegaGUI {
        @Getter private final String name;
        @Getter private final Inventory inventory;
        @Getter private final HashMap<ItemStack, HandleEvent> itemHandlers = new HashMap<>();

        public MegaGUI(String name, int size) {
                this.name = Common.colorize(name);
                this.inventory = Bukkit.createInventory(null, size, getName());
        }

        public abstract void setup();

        public void fill(MegaItem item) {
                for (int i = 0; i < getInventory().getSize(); i++) {
                        if (getInventory().getItem(i) == null) place(i, item);
                }
        }

        public void place(int i, MegaItem megaItem, HandleEvent handleEvent) {
                place(i, megaItem.getItemStack(), handleEvent);
        }

        public void place(int i, ItemStack itemStack, HandleEvent handleEvent) {
                place(i, itemStack);
                itemHandlers.put(itemStack, handleEvent);
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
