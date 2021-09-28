package me.william278.huskconfirmdrops.config;

import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;

public class Settings {

    public static DatabaseType databaseType;

    private final String mySqlHost;
    private final int mySqlPort;
    private final String mySqlDatabaseName;
    private final String mySqlUsername;
    private final String mySqlPassword;
    private final String mySqlTableName;
    private final String mySqlParams;

    private final String toggleConfirmationOnMessage;
    private final String toggleConfirmationOffMessage;
    private final String confirmationMessage;
    private final Sound confirmationSound;

    public Settings(FileConfiguration config) {
        databaseType = DatabaseType.valueOf(config.getString("database.type", "sqlite").toUpperCase());

        mySqlHost = config.getString("database.mysql_credentials.host", "localhost");
        mySqlPort = config.getInt("database.mysql_credentials.port", 3306);
        mySqlDatabaseName = config.getString("database.mysql_credentials.database", "HuskConfirmDrops");
        mySqlUsername = config.getString("database.mysql_credentials.username", "root");
        mySqlPassword = config.getString("database.mysql_credentials.password", "pa55w0rd");
        mySqlTableName = config.getString("database.mysql_credentials.table_name", "huskconfirmdrops_data");
        mySqlParams = config.getString("database.mysql_credentials.params", "?autoReconnect=true&useSSL=false");

        toggleConfirmationOnMessage = config.getString("messages.toggle_confirmation_on_message", "[Toggled drop confirmation](#00fb9a) [on](#00fb9a bold)");
        toggleConfirmationOffMessage = config.getString("messages.toggle_confirmation_off_message", "[Toggled drop confirmation](#00fb9a) [off](#00fb9a bold)");
        confirmationMessage = config.getString("messages.confirmation_message", "[Press](#00fb9a) &#00fb9a&&l%1%&r [again to drop this item.](#00fb9a)");
        confirmationSound = Sound.valueOf(config.getString("messages.confirmation_sound", "BLOCK_NOTE_BLOCK_BANJO").toUpperCase());
    }

    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    public String getMySqlHost() {
        return mySqlHost;
    }

    public int getMySqlPort() {
        return mySqlPort;
    }

    public String getMySqlDatabaseName() {
        return mySqlDatabaseName;
    }

    public String getMySqlUsername() {
        return mySqlUsername;
    }

    public String getMySqlPassword() {
        return mySqlPassword;
    }

    public String getMySqlTableName() {
        return mySqlTableName;
    }

    public String getMySqlParams() {
        return mySqlParams;
    }

    public String getToggleConfirmationOnMessage() {
        return toggleConfirmationOnMessage;
    }

    public String getToggleConfirmationOffMessage() {
        return toggleConfirmationOffMessage;
    }

    public String getConfirmationMessage() {
        return confirmationMessage;
    }

    public Sound getConfirmationSound() {
        return confirmationSound;
    }

    public enum DatabaseType {
        MYSQL,
        SQLITE
    }

}
