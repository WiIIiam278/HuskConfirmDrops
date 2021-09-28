package me.william278.huskconfirmdrops.database;

import me.william278.huskconfirmdrops.HuskConfirmDrops;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class MySQL extends Database {

    final static String[] SQL_SETUP_STATEMENTS = {
            "CREATE TABLE IF NOT EXISTS " + HuskConfirmDrops.getSettings().getMySqlTableName() + " (" +
                    "`uuid` char(36) NOT NULL UNIQUE," +
                    "`confirm_drops` boolean NOT NULL DEFAULT true," +
                    "PRIMARY KEY (`uuid`)" +
                    ");"
    };

    final String host = HuskConfirmDrops.getSettings().getMySqlHost();
    final int port = HuskConfirmDrops.getSettings().getMySqlPort();
    final String database = HuskConfirmDrops.getSettings().getMySqlDatabaseName();
    final String username = HuskConfirmDrops.getSettings().getMySqlUsername();
    final String password = HuskConfirmDrops.getSettings().getMySqlPassword();
    final String params = HuskConfirmDrops.getSettings().getMySqlParams();

    private Connection connection;

    public MySQL(HuskConfirmDrops instance) {
        super(instance);
    }

    @Override
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                try {
                    synchronized (HuskConfirmDrops.getInstance()) {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        connection = (DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + params, username, password));
                    }
                } catch (SQLException ex) {
                    plugin.getLogger().log(Level.SEVERE, "An exception occurred initialising the mySQL database: ", ex);
                } catch (ClassNotFoundException ex) {
                    plugin.getLogger().log(Level.SEVERE, "The mySQL JBDC library is missing! Please download and place this in the /lib folder.");
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
        try(Statement statement = connection.createStatement()) {
            for (String tableCreationStatement : SQL_SETUP_STATEMENTS) {
                statement.execute(tableCreationStatement);
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "An error occurred creating the HuskConfirmDrops table: ", e);
        }
        initialize();
    }
}
