package ir.jeykey.megareports.database.models;

import ir.jeykey.megareports.database.DataSource;
import ir.jeykey.megareports.database.Queries;
import ir.jeykey.megareports.utils.Serialization;
import ir.jeykey.megareports.utils.YMLLoader;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

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

                return this;
        }

}
