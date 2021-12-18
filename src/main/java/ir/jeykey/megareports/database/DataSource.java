package ir.jeykey.megareports.database;

import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import ir.jeykey.megareports.MegaReports;
import ir.jeykey.megareports.config.Storage;
import ir.jeykey.megareports.database.models.Report;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class DataSource {
        private static ConnectionSource connectionSource;

        public static void SQLite() throws SQLException, IOException, ClassNotFoundException {
                File file = new File(MegaReports.getInstance().getDataFolder(), "data.db");
                if (!file.exists()) file.createNewFile();

                String databaseUrl = "jdbc:sqlite:" + MegaReports.getInstance().getDataFolder() + "/data.db";

                connectionSource = new JdbcConnectionSource(databaseUrl);

                setupTables();
        }

        public static void MySQL() throws SQLException {
                String databaseUrl = "jdbc:mysql://" + Storage.MYSQL_HOST + ":" + Storage.MYSQL_PORT + "/" + Storage.MYSQL_DB + "?useSSL=false&autoReconnect=true";

                connectionSource = new JdbcConnectionSource(databaseUrl, Storage.MYSQL_USERNAME, Storage.MYSQL_PASSWORD);

                setupTables();
        }

        public static void setupTables() throws SQLException {
                MegaReports.setReportsDao(DaoManager.createDao(connectionSource, Report.class));
                TableUtils.createTableIfNotExists(connectionSource, Report.class);
        }

        public static ConnectionSource getConnection() {
                return connectionSource;
        }
}
