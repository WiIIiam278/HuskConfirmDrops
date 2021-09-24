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
        HuskConfirmDrops.sendMessage(player, "confirmation_message");
        HuskConfirmDrops.playSound(player, "confirmation_sound");
    }

    @EventHandler
    public void playerDropItemEvent(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (ToggleCommand.disabledDropConfirmationPlayers.contains(player.getUniqueId())) {
            return;
        }
        if (attemptingToDropItems.containsKey(e.getPlayer().getUniqueId())) {
            if (attemptingToDropItems.get(player.getUniqueId()) != e.getItemDrop().getItemStack().getType()) {
                e.setCancelled(true);
                sendCancellationNotice(player, e.getItemDrop().getItemStack().getType());
            }
        } else {
            e.setCancelled(true);
            sendCancellationNotice(player, e.getItemDrop().getItemStack().getType());
        }
    }

}
