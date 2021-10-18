package ir.jeykey.megacore.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public interface HandleEvent {
        void handle(Player player, ItemStack itemStack, int slot, ClickType clickType);
}
