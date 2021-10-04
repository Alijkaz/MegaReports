package ir.jeykey.megareports.config;

public class Storage extends Configurable {
        public static String LOCATION;

        public static String MYSQL_USERNAME;
        public static String MYSQL_PASSWORD;
        public static String MYSQL_HOST;
        public static String MYSQL_DB;
        public static String MYSQL_DRIVER;
        public static String MYSQL_PORT;

        public Storage() {
                super("storage.yml");
        }

        @Override
        public void init() {
                LOCATION = getConfig().getString("location").toLowerCase();
                MYSQL_USERNAME = getConfig().getString("mysql.username");
                MYSQL_PASSWORD = getConfig().getString("mysql.password");
                MYSQL_HOST = getConfig().getString("mysql.host");
                MYSQL_DB = getConfig().getString("mysql.database");
                MYSQL_DRIVER = getConfig().getString("mysql.driver");
                MYSQL_PORT = getConfig().getString("mysql.port");
        }
}
