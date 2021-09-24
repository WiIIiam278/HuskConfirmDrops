package me.william278.huskconfirmdrops;

import de.themoep.minedown.MineDown;
import org.bstats.bukkit.Metrics;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class HuskConfirmDrops extends JavaPlugin {

    // Metrics ID for bStats integration
    private static final int METRICS_PLUGIN_ID = 12867;

    private static HuskConfirmDrops instance;
    public static HuskConfirmDrops getInstance() {
        return instance;
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

        // Register event
        getServer().getPluginManager().registerEvents(new EventListener(), this);

        // Register command
        Objects.requireNonNull(getCommand("toggleconfirmdrops")).setExecutor(new ToggleCommand());

        // bStats
        try {
            Metrics metrics = new Metrics(this, METRICS_PLUGIN_ID);
        } catch (Exception e) {
            getLogger().warning("An exception occurred initialising metrics; skipping.");
        }
    }

    public static void sendMessage(Player player, String messageId) {
        final String message = getInstance().getConfig().getString(messageId, "[Invalid message](#ff3300)");
        player.spigot().sendMessage(new MineDown(message).toComponent());
    }

    public static void playSound(Player player, String soundId) {
        final String sound = getInstance().getConfig().getString(soundId, "BLOCK_NOTE_BLOCK_BANJO");
        player.playSound(player.getLocation(), Sound.valueOf(sound), 1F, 1F);
    }
}
