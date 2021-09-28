package me.william278.huskconfirmdrops.database;

import me.william278.huskconfirmdrops.HuskConfirmDrops;
import me.william278.huskconfirmdrops.command.ToggleCommand;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

public class DataManager {

    private static final HuskConfirmDrops plugin = HuskConfirmDrops.getInstance();

    // Return the database connection
    private static Connection getConnection() {
        return HuskConfirmDrops.getConnection();
    }

    // Fetch the player's toggle status (if they exist on the database)
    public static void fetchPlayerToggleStatus(UUID uuid) {
        Connection connection = getConnection();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + HuskConfirmDrops.getSettings().getMySqlTableName() + " WHERE `uuid`=?;")) {
                statement.setString(1, uuid.toString());
                ResultSet resultSet = statement.executeQuery();

                // No need to check playerExists() because we do this
                if (resultSet.next()) {
                    if (!resultSet.getBoolean("confirm_drops")) {
                        ToggleCommand.disabledDropConfirmationPlayers.add(uuid);
                    } else {
                        ToggleCommand.disabledDropConfirmationPlayers.remove(uuid);
                    }
                }
            } catch (SQLException e) {
                plugin.getLogger().log(Level.WARNING, "An SQL exception occurred fetching player drop confirmation toggle data on the database: ", e);
            }
        });
    }

    // Update the player's toggle status
    public static void setPlayerToggle(UUID uuid, boolean toggleValue) {
        Connection connection = getConnection();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            // Add a player to the database if they don't exist
            if (!playerExists(uuid, connection)) {
                createPlayerData(uuid, connection);
            }

            // Update their status
            try (PreparedStatement statement = connection.prepareStatement("UPDATE " + HuskConfirmDrops.getSettings().getMySqlTableName() + " SET `confirm_drops`=? WHERE `uuid`=?;")) {
                statement.setBoolean(1, toggleValue);
                statement.setString(2, uuid.toString());
            } catch (SQLException e) {
                plugin.getLogger().log(Level.WARNING, "An SQL exception occurred updating player drop confirmation toggle data on the database: ", e);
            }
        });
    }

    // Add a player to the database
    private static void createPlayerData(UUID uuid, Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO " + HuskConfirmDrops.getSettings().getMySqlTableName() + " (uuid) VALUES(?);")) {
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "An SQL exception occurred creating player data on the database: ", e);
        }
    }

    // Check if a player exists on the database
    private static boolean playerExists(UUID uuid, Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + HuskConfirmDrops.getSettings().getMySqlTableName() + " WHERE `uuid`=?;")) {
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "An SQL exception occurred getting whether a player exists: ", e);
        }
        return false;
    }

}