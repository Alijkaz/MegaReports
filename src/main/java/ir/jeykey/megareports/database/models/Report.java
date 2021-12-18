package ir.jeykey.megareports.database.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import ir.jeykey.megacore.MegaPlugin;
import ir.jeykey.megacore.utils.Common;
import ir.jeykey.megareports.MegaReports;
import ir.jeykey.megareports.config.Config;
import ir.jeykey.megareports.config.Discord;
import ir.jeykey.megareports.config.Messages;
import ir.jeykey.megareports.events.BungeeListener;
import ir.jeykey.megareports.utils.DiscordWebhook;
import ir.jeykey.megareports.utils.Serialization;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.awt.*;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@DatabaseTable(tableName = "megareports_reports")
public class Report {
    public static HashMap<Player,Integer> PLAYERS_IN_TELEPORT_MODE = new HashMap<>();

    @DatabaseField(columnName = "id", generatedId = true) @Getter @Setter
    private int id;

    @DatabaseField(canBeNull = false) @Getter @Setter
    private String reporter;

    @DatabaseField() @Getter @Setter
    private String target;

    @DatabaseField() @Getter @Setter
    private String reason;

    @DatabaseField() @Getter @Setter
    private String closedReason;

    @DatabaseField() @Getter @Setter
    private String closedBy;

    @DatabaseField() @Getter @Setter
    private String rawLocation;

    @Getter @Setter
    private Location location;

    @DatabaseField() @Getter @Setter
    private String server;

    @DatabaseField(dataType = DataType.SQL_DATE) @Getter @Setter
    private Date createdAt;

    @DatabaseField(dataType = DataType.SQL_DATE) @Getter @Setter
    private Date closedAt;

    public Report() {

    }

    public Report(int id) {
        setId(id);
    }

    public Report(String reporter, String target, String reason, Location loc) {
        setReporter(reporter);
        setTarget(target);
        setReason(reason);
        setLocation(loc);
        setRawLocation(Serialization.serializeLocation(getLocation()));
    }

    public void save() throws SQLException {
        setServer(Config.SERVER);
        setCreatedAt(getDate());

        MegaReports.getReportsDao().create(this);

        // Sending notification to discord if enabled in config
        if (Discord.DISCORD_ENABLED)
            this.notifyDiscord();

    }

    public static Report find(int id) throws SQLException {
        Report report = MegaReports.getReportsDao().queryForId(String.valueOf(id));
        report.setLocation(Serialization.deserializeLocation(report.getRawLocation()));
        return report;
    }

    public void close() throws SQLException {
        setClosedAt(getDate());
        setClosedBy(getClosedBy());
        setClosedReason(getClosedReason());
        save();
    }

    public void open() throws SQLException {
        setClosedAt(null);
        setClosedBy(null);
        setClosedReason(null);
        save();
    }

    public void delete() throws SQLException {
        MegaReports.getReportsDao().delete(this);
    }

    public static long count() {
        try {
            return MegaReports.getReportsDao().countOf();
        } catch(SQLException ignored) {
            return 0;
        }
    }

    public String getWorldName() {
        if (this.location == null) return null;
        String[] rawLocationSplit = this.rawLocation.split(",");
        return rawLocationSplit.length > 0 ? rawLocationSplit[0] : null;
    }

    public void teleport(Player p, TeleportMode teleportMode) {
        if (!getServer().equalsIgnoreCase(Config.SERVER) && Config.BUNGEECORD) {
            MegaReports.getBungeeApi().connect(p, getServer());
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

            PLAYERS_IN_TELEPORT_MODE.put(p, getId());

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
                                    "%id%", Long.toString(getId())
                            )
            );
        }
    }

    public static List<Report> all() {
        try {
            return all(0, (int) MegaReports.getReportsDao().countOf());
        } catch (SQLException ignored) {
            return new ArrayList<>();
        }
    }

    public static List<Report> all(int offset, int limit) {
        try {
            List<Report> reports = MegaReports.getReportsDao().queryBuilder().orderBy("id", false).offset(Integer.toUnsignedLong(offset)).limit(Integer.toUnsignedLong(limit)).query();
            reports.forEach((report) -> {
                report.setLocation(Serialization.deserializeLocation(report.getRawLocation()));
            });
            return reports;
        } catch (SQLException ignored) {
            return new ArrayList<>();
        }
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

    public Date getDate() {
        return Date.valueOf(LocalDate.now());
    }


}
