package ir.jeykey.megareports.events;

import ir.jeykey.megareports.config.Messages;
import ir.jeykey.megareports.database.models.Report;
import ir.jeykey.megareports.gui.ManageReportGUI;
import ir.jeykey.megacore.utils.Common;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.sql.SQLException;

public class PlayerChat implements Listener {
        @EventHandler
        public void onPlayerChat(final AsyncPlayerChatEvent e) {
                if (ManageReportGUI.WAITING_CLOSE_REASON.containsKey(e.getPlayer())) {
                        if (e.getMessage().equalsIgnoreCase("cancel")) {
                                ManageReportGUI.WAITING_CLOSE_REASON.remove(e.getPlayer());

                                e.setCancelled(true);

                                Common.send(e.getPlayer(), Messages.MANAGEMENT_CLOSING_CANCELLED);

                                return;
                        }

                        Report report = ManageReportGUI.WAITING_CLOSE_REASON.get(e.getPlayer());

                        report.setClosedReason(e.getMessage());
                        report.setClosedBy(e.getPlayer().getName());

                        try {
                                report.close();
                                Common.send(e.getPlayer(), Messages.MANAGEMENT_REPORT_CLOSED.replace("%id%", Integer.toString(report.getId())).replace("%reason%", e.getMessage()));
                        } catch (SQLException ignored) {
                                Common.send(e.getPlayer(), Messages.DATABASE_ISSUE);
                        }

                        ManageReportGUI.WAITING_CLOSE_REASON.remove(e.getPlayer());

                        e.setCancelled(true);
                }

        }
}
