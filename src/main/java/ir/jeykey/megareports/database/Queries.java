package ir.jeykey.megareports.database;

public class Queries {
        public static String CREATE_REPORTS_TABLE = "CREATE TABLE IF NOT EXISTS megareports_reports (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "server VARCHAR(255) NOT NULL," +
                "reporter VARCHAR(16) NOT NULL," +
                "target VARCHAR(16) NOT NULL," +
                "reason VARCHAR(255) NULL," +
                "location VARCHAR(255) NULL," +
                "reported_at BIGINT NOT NULL," +
                "report_closed_at BIGINT NULL" +
                ");";
}
