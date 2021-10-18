package ir.jeykey.megareports.database.models;

import ir.jeykey.megareports.MegaReports;
import ir.jeykey.megareports.config.Config;
import ir.jeykey.megareports.config.Discord;
import ir.jeykey.megareports.config.Messages;
import ir.jeykey.megareports.database.DataSource;
import ir.jeykey.megareports.database.Queries;
import ir.jeykey.megareports.events.BungeeListener;
import ir.jeykey.megacore.utils.Common;
import ir.jeykey.megareports.utils.DiscordWebhook;
import ir.jeykey.megareports.utils.Serialization;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.awt.*;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Report {
        @Setter @Getter private Integer id;
        @Setter @Getter private String reporter;
        @Setter @Getter private String target;
        @Setter @Getter private String reason;
        @Setter @Getter private String closedReason;
        @Setter @Getter private String closedBy;
        @Setter @Getter private String rawLocation;
        @Setter @Getter private Location location;
        @Setter @Getter private String server;
        @Setter @Getter private String createdAt;
        @Setter @Getter private String closedAt;

        public static HashMap<Player,Integer> PLAYERS_IN_TELEPORT_MODE = new HashMap<>();

        public Report(int id) {
                setId(id);
        }

        public Report(String reporter, String target, String reason, Location loc) {
                setReporter(reporter);
                setTarget(target);
                setReason(reason);
                setLocation(loc);
        }

        public void save() {
                setServer(Config.SERVER);

                try {
                        PreparedStatement pst = DataSource.getConnection().prepareStatement(Queries.INSERT_REPORT);
                        pst.setString(1, getReporter());
                        pst.setString(2, getTarget());
                        pst.setString(3, getReason());
                        pst.setString(4, Serialization.serializeLocation(getLocation()));
                        pst.setString(5, getServer());
                        DataSource.executeQueryAsync(pst);
                } catch (SQLException exception) {
                        exception.printStackTrace();
                }

                // Sending notification to discord if enabled in config
                if (Discord.DISCORD_ENABLED)
                        this.notifyDiscord();
        }

        /**
         * This method is used to find and load specific report using Report#id
         */
        public void load() {
                try {
                        Connection con = DataSource.getConnection();
                        PreparedStatement pst = con.prepareStatement(Queries.SELECT_REPORT);
                        pst.setInt(1, getId());
                        ResultSet rs = pst.executeQuery();
                        while (rs.next()) {
                                Integer id = rs.getInt("id");
                                String reporter = rs.getString("reporter");
                                String target = rs.getString("target");
                                String reason = rs.getString("reason");
                                String closedReason = rs.getString("closed_reason");
                                String closedBy = rs.getString("closed_by");
                                String server = rs.getString("server");
                                String reportedAt = rs.getString("created_at");
                                String reportedClosedAt = rs.getString("closed_at");
                                String rawLocation = rs.getString("location");
                                Location location = Serialization.deserializeLocation(rawLocation);

                                setId(id);
                                setReporter(reporter);
                                setTarget(target);
                                setReason(reason);
                                setClosedReason(closedReason);
                                setClosedBy(closedBy);
                                setRawLocation(rawLocation);
                                setLocation(location);
                                setServer(server);
                                setCreatedAt(reportedAt);
                                setClosedAt(reportedClosedAt);
                        }
                } catch (SQLException exception) {
                        exception.printStackTrace();
                }
        }


        public void close() {
                try {
                        PreparedStatement pst = DataSource.getConnection().prepareStatement(Queries.CLOSE_REPORT);
                        pst.setDate(1, Date.valueOf(LocalDate.now()));
                        pst.setString(2, getClosedReason());
                        pst.setString(3, getClosedBy());
                        pst.setInt(4, getId());
                        DataSource.executeQueryAsync(pst);
                } catch (SQLException exception) {
                        exception.printStackTrace();
                }
        }

        public void open() {
                try {
                        PreparedStatement pst = DataSource.getConnection().prepareStatement(Queries.OPEN_REPORT);
                        pst.setInt(1, getId());
                        DataSource.executeQueryAsync(pst);
                } catch (SQLException exception) {
                        exception.printStackTrace();
                }
        }

        public void delete() {
                try {
                        PreparedStatement pst = DataSource.getConnection().prepareStatement(Queries.DELETE_REPORT);
                        pst.setInt(1, getId());
                        DataSource.executeQueryAsync(pst);
                } catch (SQLException exception) {
                        exception.printStackTrace();
                }
        }

        public String getWorldName() {
                if (this.location == null) return null;
                String[] rawLocationSplit = this.rawLocation.split(",");
                return rawLocationSplit.length > 0 ? rawLocationSplit[0] : null;
        }

        public void teleport(Player p, TeleportMode teleportMode) {
                if (!getServer().equalsIgnoreCase(Config.SERVER) && Config.BUNGEECORD) {
                        BungeeListener.sendPlayerTo(p, getServer());
                        BungeeListener.teleportPlayerTo(p, this, teleportMode);
                        Common.send(
                                p,
                                Messages.TELEPORT_CROSS_SERVER
                                        .replace("%from%", Config.SERVER)
                                        .replace("%to%", getServer())
                        );
                } else {
                        for (String cmd: Config.TELEPORT_COMMANDS) {
                                cmd = cmd.replace("%player%", p.getName());

                                if (cmd.startsWith("[console]"))
                                        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd.replace("[console]", "").trim());
                                else if (cmd.startsWith("[player]"))
                                        p.performCommand(cmd.replace("[player]", "").trim());
                        }

                        PLAYERS_IN_TELEPORT_MODE.put(p, this.id);

                        if (teleportMode == TeleportMode.REPORT_LOCATION)
                                p.teleport(getLocation());
                        else if (teleportMode == TeleportMode.REPORTER_LOCATION)
                                p.teleport(Bukkit.getOfflinePlayer(getReporter()).getPlayer().getLocation());
                        else if (teleportMode == TeleportMode.TARGET_LOCATION)
                                p.teleport(Bukkit.getOfflinePlayer(getTarget()).getPlayer().getLocation());

                        Common.send(
                                p,
                                Messages.TELEPORT
                                        .replace(
                                                "%id%", getId().toString()
                                        )
                        );
                }
        }


        /**
         * This method is used to retrieve all the reports from database
         * @return All the reports that are saved in database
         */
        public static List<Report> all() {

                List<Report> reports = new ArrayList<>();

                try {
                        Connection con = DataSource.getConnection();
                        PreparedStatement pst = con.prepareStatement(Queries.SELECT_ALL_REPORTS);
                        ResultSet rs = pst.executeQuery();
                        reports = new ArrayList<>();
                        Report report;
                        while (rs.next()) {
                                Integer id = rs.getInt("id");
                                String reporter = rs.getString("reporter");
                                String target = rs.getString("target");
                                String reason = rs.getString("reason");
                                String closedReason = rs.getString("closed_reason");
                                String closedBy = rs.getString("closed_by");
                                String server = rs.getString("server");
                                String reportedAt = rs.getString("created_at");
                                String reportedClosedAt = rs.getString("closed_at");
                                String rawLocation = rs.getString("location");
                                Location location = Serialization.deserializeLocation(rawLocation);

                                report = new Report(reporter, target, reason, location);

                                report.setId(id);
                                report.setServer(server);
                                report.setClosedReason(closedReason);
                                report.setClosedBy(closedBy);
                                report.setRawLocation(rawLocation);
                                report.setCreatedAt(reportedAt);
                                report.setClosedAt(reportedClosedAt);

                                reports.add(report);
                        }
                } catch (SQLException exception) {
                        exception.printStackTrace();
                }

                return reports;
        }


        private void notifyDiscord() {
                Bukkit.getScheduler().runTaskAsynchronously(MegaReports.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                                DiscordWebhook webhook = new DiscordWebhook(Discord.DISCORD_WEBHOOK);
                                DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject();

                                embedObject.setColor(Color.GREEN);
                                embedObject.setTitle(
                                        Discord.EMBED_TITLE
                                                .replace("%reporter%", getReporter())
                                                .replace("%target%", getTarget())
                                                .replace("%reason%", getReason())
                                );

                                embedObject.setDescription(
                                        Discord.EMBED_DESCRIPTION
                                                .replace("%reporter%", getReporter())
                                                .replace("%target%", getTarget())
                                                .replace("%reason%", getReason())
                                );

                                embedObject.addField("Server:", getServer(), false);
                                embedObject.addField("World:", location.getWorld().getName(), false);
                                embedObject.addField("Reported At:", Common.getBeautifiedDt(), false);

                                embedObject.setFooter(
                                        Discord.EMBED_FOOTER
                                                .replace("%reporter%", getReporter())
                                                .replace("%target%", getTarget())
                                                .replace("%reason%", getReason()),
                                        null
                                );

                                embedObject.setThumbnail(
                                        Discord.EMBED_THUMBNAIL
                                                .replace("%reporter%", getReporter())
                                                .replace("%target%", getTarget())
                                );

                                webhook.addEmbed(embedObject);

                                try {
                                        webhook.execute();
                                } catch (IOException exception) {
                                        Common.logPrefixed("&cFailed to send webhook notification, please check your network connectivity!", "&cStack Trace:");
                                        exception.printStackTrace();
                                }
                        }
                });
        }

}
