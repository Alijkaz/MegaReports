package ir.jeykey.megareports.events;

import ir.jeykey.megareports.database.models.Report;
import ir.jeykey.megareports.gui.ManageReportGUI;
import ir.jeykey.megacore.utils.Common;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChat implements Listener {
        @EventHandler
        public void onPlayerChat(final AsyncPlayerChatEvent e) {
                if (ManageReportGUI.WAITING_CLOSE_REASON.containsKey(e.getPlayer())) {
                        Report report = ManageReportGUI.WAITING_CLOSE_REASON.get(e.getPlayer());

                        report.setClosedReason(e.getMessage());
                        report.setClosedBy(e.getPlayer().getName());

                        report.close();

                        Common.send(e.getPlayer(), "&aYou have successfully closed report &c#" + report.getId() + " &awith reason: &2" + e.getMessage());

                        ManageReportGUI.WAITING_CLOSE_REASON.remove(e.getPlayer());

                        e.setCancelled(true);
                }

        }
}
