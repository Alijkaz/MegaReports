package ir.jeykey.megareports.events;

import ir.jeykey.megareports.MegaReports;
import ir.jeykey.megareports.database.models.Report;
import ir.jeykey.megacore.utils.Common;
import ir.jeykey.megareports.database.models.TeleportMode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.*;

public class BungeeListener implements PluginMessageListener {
        @Override
        public void onPluginMessageReceived(String channel, Player player, byte[] message) {
                if (!channel.equals("BungeeCord")) {
                        return;
                }

                try{
                        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
                        String subchannel = in.readUTF();

                        if (subchannel.equals("MegaReports")) {
                                short len = in.readShort();
                                byte[] data = new byte[len];
                                in.readFully(data);

                                Bukkit.getScheduler().runTaskLater(MegaReports.getInstance(), new Runnable() {
                                        @Override
                                        public void run() {
                                                String[] parts = new String(data).split(",");
                                                String playerName = parts[0];
                                                int reportId =  Integer.parseInt(parts[1]);
                                                TeleportMode teleportMode = TeleportMode.valueOf(parts[2]);

                                                Report report = new Report(reportId);
                                                report.load();

                                                report.teleport(Bukkit.getServer().getPlayerExact(playerName), teleportMode);
                                        }
                                }, 15);

                        }
                }catch(Exception ex){
                        ex.printStackTrace();
                }
        }

        public static void sendPlayerTo(Player p, String server) {
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(b);
                try {
                        out.writeUTF("Connect");
                        out.writeUTF(server);
                } catch (IOException ignored) {
                        Common.logPrefixed("Something wrong happened during sending message to BungeeCord");
                }
                p.sendPluginMessage(MegaReports.getInstance(), "BungeeCord", b.toByteArray());
        }

        public static void teleportPlayerTo(Player p, Report report, TeleportMode teleportMode) {
                try{
                        ByteArrayOutputStream b = new ByteArrayOutputStream();
                        DataOutputStream out = new DataOutputStream(b);

                        out.writeUTF("Forward");
                        out.writeUTF(report.getServer());
                        out.writeUTF("MegaReports");

                        byte[] data = (p.getName() + "," + report.getId().toString() + "," + teleportMode.name()).getBytes();
                        out.writeShort(data.length);
                        out.write(data);

                        p.sendPluginMessage(MegaReports.getInstance(), "BungeeCord", b.toByteArray());
                }catch(Exception ex){
                        ex.printStackTrace();
                }
        }

        public static void reportNotification(Report report) {
                // TODO cross server report notification system
        }
}
