package me.william278.huskconfirmdrops.listener;

import me.william278.huskconfirmdrops.HuskConfirmDrops;
import me.william278.huskconfirmdrops.command.ToggleCommand;
import me.william278.huskconfirmdrops.database.DataManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.UUID;

public class EventListener implements Listener {

    private static final HashMap<UUID, Material> attemptingToDropItems = new HashMap<>();

    private void sendCancellationNotice(Player player, Material material) {
        attemptingToDropItems.put(player.getUniqueId(), material);
        HuskConfirmDrops.sendConfirmMessage(player, HuskConfirmDrops.getSettings().getConfirmationMessage());
        player.playSound(player.getLocation(), HuskConfirmDrops.getSettings().getConfirmationSound(), 1F, 1F);
    }

    // Returns if the inventory has a free slot left (prevents items getting lost)
    private boolean doesInventoryHaveSpace(Player player) {
        return (player.getInventory().firstEmpty() != -1);
    }

    @EventHandler
    public void playerDropItemEvent(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (ToggleCommand.disabledDropConfirmationPlayers.contains(player.getUniqueId())) {
            return;
        }
        if (attemptingToDropItems.containsKey(e.getPlayer().getUniqueId())) {
            if (attemptingToDropItems.get(player.getUniqueId()) != e.getItemDrop().getItemStack().getType()) {
                if (doesInventoryHaveSpace(player)) {
                    e.setCancelled(true);
                    sendCancellationNotice(player, e.getItemDrop().getItemStack().getType());
                }
            }
        } else {
            if (doesInventoryHaveSpace(player)) {
                e.setCancelled(true);
                sendCancellationNotice(player, e.getItemDrop().getItemStack().getType());
            }
        }
    }

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        DataManager.fetchPlayerToggleStatus(player.getUniqueId());
    }

}
