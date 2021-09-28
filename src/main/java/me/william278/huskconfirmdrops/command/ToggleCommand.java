package me.william278.huskconfirmdrops.command;

import de.themoep.minedown.MineDown;
import me.william278.huskconfirmdrops.HuskConfirmDrops;
import me.william278.huskconfirmdrops.database.DataManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.UUID;

public class ToggleCommand implements CommandExecutor {

    public static HashSet<UUID> disabledDropConfirmationPlayers = new HashSet<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (disabledDropConfirmationPlayers.contains(player.getUniqueId())) {
                disabledDropConfirmationPlayers.remove(player.getUniqueId());
                sender.spigot().sendMessage(new MineDown(HuskConfirmDrops.getSettings().getToggleConfirmationOnMessage()).toComponent());
                DataManager.setPlayerToggle(player.getUniqueId(), true);
            } else {
                disabledDropConfirmationPlayers.add(player.getUniqueId());
                sender.spigot().sendMessage(new MineDown(HuskConfirmDrops.getSettings().getToggleConfirmationOffMessage()).toComponent());
                DataManager.setPlayerToggle(player.getUniqueId(), false);

            }
        } else {
            sender.spigot().sendMessage(new MineDown("[You cannot run this command from console!](#ff3300)").toComponent());
        }
        return true;
    }
}
