package me.william278.huskconfirmdrops.database;

import me.william278.huskconfirmdrops.HuskConfirmDrops;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class SQLite extends Database {

    final static String[] SQL_SETUP_STATEMENTS = {
            "PRAGMA foreign_keys = ON;",
            "PRAGMA encoding = 'UTF-8';",

            "CREATE TABLE IF NOT EXISTS " + HuskConfirmDrops.getSettings().getMySqlTableName() + " (" +
                    "`uuid` char(36) NOT NULL UNIQUE," +
                    "`confirm_drops` boolean NOT NULL DEFAULT true," +
                    "PRIMARY KEY (`uuid`)" +
                    ");"
    };

    private static final String DATABASE_NAME = "HuskConfirmDropsData";

    private Connection connection;

    public SQLite(HuskConfirmDrops instance) {
        super(instance);
    }

    @Override
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                File databaseFile = new File(plugin.getDataFolder(), DATABASE_NAME + ".db");
                if (!databaseFile.exists()) {
                    try {
                        if (!databaseFile.createNewFile()) {
                            plugin.getLogger().log(Level.SEVERE, "Failed to write new file: " + DATABASE_NAME + ".db (file already exists)");
                        }
                    } catch (IOException e) {
                        plugin.getLogger().log(Level.SEVERE, "An error occurred writing a file: " + DATABASE_NAME + ".db (" + e.getCause() + ")");
                    }
                }
                try {
                    Class.forName("org.sqlite.JDBC");
                    connection = (DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder().getAbsolutePath() + "/" + DATABASE_NAME + ".db"));
                } catch (SQLException ex) {
                    plugin.getLogger().log(Level.SEVERE, "An exception occurred initialising the SQLite database", ex);
                } catch (ClassNotFoundException ex) {
                    plugin.getLogger().log(Level.SEVERE, "The SQLite JBDC library is missing! This should download automatically on startup if your server is based on the latest version(s) of Spigot (v1.16.5 and newer). Please download and place this in the /lib folder.");
                }
            }
        } catch (SQLException exception) {
            plugin.getLogger().log(Level.WARNING, "An error occurred checking the status of the SQL connection: ", exception);
        }
        return connection;
    }

    @Override
    public void load() {
        connection = getConnection();
        try (Statement statement = connection.createStatement()) {
            for (String tableCreationStatement : SQL_SETUP_STATEMENTS) {
                statement.execute(tableCreationStatement);
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "An error occurred creating the HuskConfirmDrops table: ", e);
        }
        initialize();
    }
}
