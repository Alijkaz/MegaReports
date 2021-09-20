package ir.jeykey.megareports.database;

import java.sql.SQLException;
import java.sql.Statement;

public class ReportsDB {
        public static void createTables() {
                try {
                        final Statement statement = DataSource.getConnection().createStatement();
                        statement.execute(Queries.CREATE_REPORTS_TABLE);
                } catch (SQLException e) {
                        e.printStackTrace();
                }
        }
}
