package ir.jeykey.megareports.events;

import ir.jeykey.megareports.gui.ManageReportGUI;
import ir.jeykey.megareports.utils.Cooldown;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {
        @EventHandler
        protected void onPlayerQuit(PlayerQuitEvent e) {
                // Removing player from cooldown hashmap on quit
                Cooldown.cooldown.remove(e.getPlayer().getUniqueId());

                // Removing player from report reasoning hashmap on quit
                ManageReportGUI.WAITING_CLOSE_REASON.remove(e.getPlayer());

        }
}
