package ir.jeykey.megareports.database.models;

import ir.jeykey.megareports.MegaReports;
import ir.jeykey.megareports.database.DataSource;
import ir.jeykey.megareports.database.Queries;
import ir.jeykey.megareports.events.MessageListener;
import ir.jeykey.megareports.utils.Common;
import ir.jeykey.megareports.utils.DiscordWebhook;
import ir.jeykey.megareports.utils.Serialization;
import ir.jeykey.megareports.utils.YMLLoader;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.awt.*;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Report {
        @Setter @Getter private Integer id;
        @Setter @Getter private String reporter;
        @Setter @Getter private String target;
        @Setter @Getter private String reason;
        @Setter @Getter private String closedReason;
        @Setter @Getter private Location location;
        @Setter @Getter private String server;
        @Setter @Getter private String createdAt;
        @Setter @Getter private String closedAt;

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
                setServer(YMLLoader.Config.SERVER);

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
                if (YMLLoader.Config.DISCORD_ENABLED)
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
                                String server = rs.getString("server");
                                String reportedAt = rs.getString("created_at");
                                String reportedClosedAt = rs.getString("closed_at");
                                Location location = Serialization.deserializeLocation(rs.getString("location"));

                                setId(id);
                                setReporter(reporter);
                                setTarget(target);
                                setReason(reason);
                                setClosedReason(closedReason);
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
                        pst.setInt(3, getId());
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

        public void teleport(Player p) {
                if (!getServer().equalsIgnoreCase(YMLLoader.Config.SERVER) && YMLLoader.Config.BUNGEECORD) {
                        MessageListener.sendPlayerTo(p, getServer());
                        MessageListener.teleportPlayerTo(p, this);
                        Common.send(
                                p,
                                YMLLoader.Messages.TELEPORT_CROSS_SERVER
                                        .replace("%from%", YMLLoader.Config.SERVER)
                                        .replace("%to%", getServer())
                        );
                } else {
                        for (String cmd: YMLLoader.Config.TELEPORT_COMMANDS)
                                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd.replace("%player%", p.getName()));

                        p.teleport(getLocation());

                        Common.send(
                                p,
                                YMLLoader.Messages.TELEPORT
                                        .replace("%id%", getId().toString())
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
                                String server = rs.getString("server");
                                String reportedAt = rs.getString("created_at");
                                String reportedClosedAt = rs.getString("closed_at");
                                Location location = Serialization.deserializeLocation(rs.getString("location"));

                                report = new Report(reporter, target, reason, location);

                                report.setId(id);
                                report.setServer(server);
                                report.setClosedReason(closedReason);
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
                                DiscordWebhook webhook = new DiscordWebhook(YMLLoader.Config.DISCORD_WEBHOOK);
                                DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject();

                                embedObject.setColor(Color.GREEN);
                                embedObject.setTitle(
                                        YMLLoader.Config.EMBED_TITLE
                                                .replace("%reporter%", getReporter())
                                                .replace("%target%", getTarget())
                                                .replace("%reason%", getReason())
                                );

                                embedObject.setDescription(
                                        YMLLoader.Config.EMBED_DESCRIPTION
                                                .replace("%reporter%", getReporter())
                                                .replace("%target%", getTarget())
                                                .replace("%reason%", getReason())
                                );

                                embedObject.addField("Server:", getServer(), false);
                                embedObject.addField("World:", location.getWorld().getName(), false);
                                embedObject.addField("Reported At:", Common.getBeautifiedDt(), false);

                                embedObject.setFooter(
                                        YMLLoader.Config.EMBED_FOOTER
                                                .replace("%reporter%", getReporter())
                                                .replace("%target%", getTarget())
                                                .replace("%reason%", getReason()),
                                        null
                                );

                                embedObject.setThumbnail(
                                        YMLLoader.Config.EMBED_THUMBNAIL
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
