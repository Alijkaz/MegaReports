package ir.jeykey.megareports.database;

public class Queries {
        public static String SELECT_REPORT = "SELECT * FROM megareports_reports WHERE id = ?";
        public static String INSERT_REPORT = "INSERT INTO megareports_reports (reporter, target, reason, location, server) VALUES (?, ?, ?, ?, ?)";
        public static String SELECT_ALL_REPORTS = "SELECT * FROM megareports_reports ORDER BY created_at DESC";
        public static String CLOSE_REPORT = "UPDATE megareports_reports SET closed_at = ?, closed_reason = ?, closed_by = ?  WHERE id = ?";
        public static String OPEN_REPORT = "UPDATE megareports_reports SET closed_at = NULL, closed_reason = NULL, closed_reason = NULL WHERE id = ?";
        public static String DELETE_REPORT = "DELETE FROM megareports_reports WHERE id = ?";
        public static String CREATE_REPORTS_TABLE = "CREATE TABLE IF NOT EXISTS megareports_reports (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "reporter VARCHAR(16) NOT NULL," +
                "target VARCHAR(16) NOT NULL," +
                "reason VARCHAR(255) NULL," +
                "closed_reason VARCHAR(255) NULL," +
                "closed_by VARCHAR(16) NULL," +
                "location VARCHAR(255) NULL," +
                "server VARCHAR(255) NOT NULL," +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "closed_at DATETIME NULL" +
                ");";
}
