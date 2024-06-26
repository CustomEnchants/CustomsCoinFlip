package CustomsCoinFlip.MySQL;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 * Connects to and uses a SQLite database
 *
 * @author tips48
 */
public class SQLite extends Database {
    private final File file;

    public SQLite(File file) {
        this.file = file;
    }

    @Override
    public Connection openConnection() throws SQLException,
            ClassNotFoundException {
        if (checkConnection()) {
            return connection;
        }
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        if (!(file.exists())) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE,
                        "Unable to create database!");
            }
        }
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager
                .getConnection("jdbc:sqlite:" + file.toPath());
        return connection;
    }

}
