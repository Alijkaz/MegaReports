package ir.jeykey.megareports.database.models;

import ir.jeykey.megareports.MegaReports;
import ir.jeykey.megareports.database.DataSource;
import ir.jeykey.megareports.database.Queries;
import ir.jeykey.megareports.utils.Common;
import ir.jeykey.megareports.utils.DiscordWebhook;
import ir.jeykey.megareports.utils.Serialization;
import ir.jeykey.megareports.utils.YMLLoader;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.awt.*;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;

public class Report {
        @Setter @Getter private String reporter;
        @Setter @Getter private String target;
        @Setter @Getter private String reason;
        @Setter @Getter private Location location;
        @Setter @Getter private String server;
        @Setter @Getter private Long reportedAt;

        public Report(String reporter, String target, String reason, Location loc) {
                setReporter(reporter);
                setTarget(target);
                setReason(reason);
                setLocation(loc);
        }

        public Report save() {
                setReportedAt(Instant.now().getEpochSecond());
                setServer(YMLLoader.Config.SERVER);

                try {
                        PreparedStatement pst = DataSource.getConnection().prepareStatement(Queries.INSERT_REPORT);
                        pst.setString(1, getReporter());
                        pst.setString(2, getTarget());
                        pst.setString(3, getReason());
                        pst.setString(4, Serialization.serializeLocation(getLocation()));
                        pst.setString(5, getServer());
                        pst.setLong(6, getReportedAt());
                        DataSource.executeQueryAsync(pst);
                } catch (SQLException exception) {
                        exception.printStackTrace();
                }

                // Sending notification to discord if enabled in config
                if (YMLLoader.Config.DISCORD_ENABLED)
                        this.notifyDiscord();

                return this;
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

                                embedObject.setThumbnail(YMLLoader.Config.EMBED_THUMBNAIL);

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
