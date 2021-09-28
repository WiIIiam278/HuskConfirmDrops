package me.william278.huskconfirmdrops;

import de.themoep.minedown.MineDown;
import me.william278.huskconfirmdrops.command.ToggleCommand;
import me.william278.huskconfirmdrops.config.Settings;
import me.william278.huskconfirmdrops.database.Database;
import me.william278.huskconfirmdrops.database.MySQL;
import me.william278.huskconfirmdrops.database.SQLite;
import me.william278.huskconfirmdrops.listener.EventListener;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.KeybindComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bstats.bukkit.Metrics;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.util.Objects;

public final class HuskConfirmDrops extends JavaPlugin {

    // Metrics ID for bStats integration
    private static final int METRICS_PLUGIN_ID = 12867;

    private static HuskConfirmDrops instance;
    public static HuskConfirmDrops getInstance() {
        return instance;
    }

    private static Database database;
    public static Connection getConnection() { return database.getConnection(); }

    private static Settings settings;
    public static Settings getSettings() {
        return settings;
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        // Config setup
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        saveConfig();
        reloadConfig();
        settings = new Settings(getConfig());

        // Initialize database
        database = switch (getSettings().getDatabaseType()) {
            case MYSQL -> new MySQL(this);
            case SQLITE -> new SQLite(this);
        };
        database.load();

        // Register event
        getServer().getPluginManager().registerEvents(new EventListener(), this);

        // Register command
        Objects.requireNonNull(getCommand("toggledropconfirmation")).setExecutor(new ToggleCommand());

        // bStats
        try {
            new Metrics(this, METRICS_PLUGIN_ID);
        } catch (Exception e) {
            getLogger().warning("An exception occurred initialising metrics; skipping.");
        }
    }

    public static void sendConfirmMessage(Player player, String rawMessage) {
        ComponentBuilder builder = new ComponentBuilder().append(new MineDown(rawMessage.split("%1%")[0]).toComponent())
                .append(new KeybindComponent("key.drop")).append(new TextComponent()).reset()
                .append(new MineDown(rawMessage.split("%1%")[1]).toComponent());
        player.spigot().sendMessage(builder.create());
    }
}
