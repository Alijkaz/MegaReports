package ir.jeykey.megareports.database;

public class Queries {
        public static String INSERT_REPORT = "INSERT INTO megareports_reports (reporter, target, reason, location, server) VALUES (?, ?, ?, ?, ?)";
        public static String SELECT_ALL_REPORTS = "SELECT * FROM megareports_reports ORDER BY reported_at DESC";
        public static String CREATE_REPORTS_TABLE = "CREATE TABLE IF NOT EXISTS megareports_reports (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "reporter VARCHAR(16) NOT NULL," +
                "target VARCHAR(16) NOT NULL," +
                "reason VARCHAR(255) NULL," +
                "location VARCHAR(255) NULL," +
                "server VARCHAR(255) NOT NULL," +
                "reported_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "report_closed_at DATETIME NULL" +
                ");";
}
