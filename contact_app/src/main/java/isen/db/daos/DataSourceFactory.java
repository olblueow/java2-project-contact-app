package isen.db.daos;

import javax.sql.DataSource;
import org.sqlite.SQLiteDataSource;
import java.sql.Connection;
import java.sql.Statement;

public class DataSourceFactory {

    private static SQLiteDataSource dataSource;

    private DataSourceFactory() {
        // This is a static class that should not be instantiated.
        // Here's a way to remember it when this class will have 2K lines and you come
        // back to it in 2 years
        throw new IllegalStateException("This is a static class that should not be instantiated");
    }

    /**
     * @return a connection to the SQLite Database
     *
     */
    public static DataSource getDataSource() {
        if (dataSource == null) {
            dataSource = new SQLiteDataSource();
            dataSource.setUrl("jdbc:sqlite:sqlite.db");
        }
        return dataSource;
    }

    /**
     * Initialize the database schema by creating the person table if it doesn't exist.
     */
    public static void initializeDatabase() {
        try (Connection connection = getDataSource().getConnection();
             Statement statement = connection.createStatement()) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS person (" +
                    "    idperson INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "    lastname VARCHAR(45) NOT NULL," +
                    "    firstname VARCHAR(45) NOT NULL," +
                    "    nickname VARCHAR(45) NOT NULL," +
                    "    phone_number VARCHAR(15) NULL," +
                    "    address VARCHAR(200) NULL," +
                    "    email_address VARCHAR(150) NULL," +
                    "    birth_date DATE NULL)";
            statement.execute(createTableQuery);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
}