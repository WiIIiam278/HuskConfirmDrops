package me.william278.huskconfirmdrops;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.HashMap;
import java.util.UUID;

public class EventListener implements Listener {

    private static final HashMap<UUID, Material> attemptingToDropItems = new HashMap<>();

    private void sendCancellationNotice(Player player, Material material) {
        attemptingToDropItems.put(player.getUniqueId(), material);
        HuskConfirmDrops.sendConfirmMessage(player, "confirmation_message");
        HuskConfirmDrops.playSound(player, "confirmation_sound");
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

}
