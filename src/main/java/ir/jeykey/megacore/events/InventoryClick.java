package ir.jeykey.megacore.events;

import ir.jeykey.megacore.MegaPlugin;
import ir.jeykey.megacore.gui.MegaGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InventoryClick implements Listener {
        /**
         * Handling inventory click events
         * @param event InventoryClickEvent
         */
        @EventHandler
        protected void onGUIClickEvent(InventoryClickEvent event) {
                for (MegaGUI gui: MegaPlugin.registeredGuis) {
                        if (!event.getInventory().getName().equalsIgnoreCase(gui.getName()))
                                continue;
                        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
                                continue;
                        gui.handle((Player) event.getWhoClicked(), event.getCurrentItem(), event.getSlot(), event.getClick());
                        break;
                }
        }
}
