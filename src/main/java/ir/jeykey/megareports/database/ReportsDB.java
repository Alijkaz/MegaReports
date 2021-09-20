package ir.jeykey.megareports.database;

import java.sql.SQLException;
import java.sql.Statement;

public class ReportsDB {
        public static void createTables(boolean isMysql) {
                try {
                        final Statement statement = DataSource.getConnection().createStatement();
                        String query = Queries.CREATE_REPORTS_TABLE;
                        if (isMysql) query = query.replace("AUTOINCREMENT", "AUTO_INCREMENT");
                        statement.execute(query);
                } catch (SQLException e) {
                        e.printStackTrace();
                }
        }
}
