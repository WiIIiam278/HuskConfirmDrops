package me.william278.huskconfirmdrops;

import de.themoep.minedown.MineDown;
import net.md_5.bungee.api.chat.KeybindComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
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
                HuskConfirmDrops.sendMessage(player, "toggle_confirmation_on_message");
            } else {
                disabledDropConfirmationPlayers.add(player.getUniqueId());
                HuskConfirmDrops.sendMessage(player, "toggle_confirmation_off_message");

            }
        } else {
            sender.spigot().sendMessage(new MineDown("[You cannot run this command from console!](#ff3300)").toComponent());
        }
        return true;
    }
}
