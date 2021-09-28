package me.william278.huskconfirmdrops.command;

import de.themoep.minedown.MineDown;
import me.william278.huskconfirmdrops.HuskConfirmDrops;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AdminCommand implements CommandExecutor {

    private static final HuskConfirmDrops plugin = HuskConfirmDrops.getInstance();
    private static final StringBuilder PLUGIN_INFORMATION = new StringBuilder()
            .append("[HuskConfirmDrops](#00fb9a bold) [| Version ").append(plugin.getDescription().getVersion()).append("](#00fb9a)\n")
            .append("[").append(plugin.getDescription().getDescription()).append("](gray)\n")
            .append("[• Author:](white) [William278](gray show_text=&7Click to donate open_url=https://www.buymeacoffee.com/william278)\n")
            .append("[• Report Issues:](white) [[Link]](#00fb9a show_text=&7Click to open link open_url=https://github.com/WiIIiam278/HuskConfirmDrops/issues)\n")
            .append("[• Support Discord:](white) [[Link]](#00fb9a show_text=&7Click to join open_url=https://discord.gg/tVYhJfyDWG)");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 1) {
            switch (args[0]) {
                case "about" -> sender.spigot().sendMessage(new MineDown(PLUGIN_INFORMATION.toString()).toComponent());
                case "reload" -> {
                    plugin.reloadSettingsFromConfig();
                    sender.spigot().sendMessage(new MineDown("[HuskConfirmDrops](#00fb9a bold) &#00fb9a&| Reloaded config & message files.").toComponent());
                }
                default -> sender.spigot().sendMessage(new MineDown("[Error:](#ff3300) [Incorrect syntax. Usage: /huskconfirmdrops <about/reload>](#ff7e5e)").toComponent());
            }
        } else {
            sender.spigot().sendMessage(new MineDown(PLUGIN_INFORMATION.toString()).toComponent());
        }
        return true;
    }

    public static class AdminTabCompleter implements TabCompleter {

        private static final String[] commandTabArgs = {"about", "reload"};

        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
            if (command.getPermission() != null) {
                if (!sender.hasPermission(command.getPermission())) {
                    return Collections.emptyList();
                }
            }
            if (args.length == 0 || args.length == 1) {
                final List<String> tabCompletions = new ArrayList<>();
                StringUtil.copyPartialMatches(args[0], Arrays.asList(commandTabArgs), tabCompletions);
                Collections.sort(tabCompletions);
                return tabCompletions;
            }
            return Collections.emptyList();
        }
    }
}
