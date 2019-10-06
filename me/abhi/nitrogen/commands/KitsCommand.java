package me.abhi.nitrogen.commands;

import me.abhi.nitrogen.Nitrogen;
import me.abhi.nitrogen.util.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitsCommand implements CommandExecutor {

    private Nitrogen plugin;

    public KitsCommand(Nitrogen plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        if (!this.plugin.getManagerHandler().getServerManager().getSpawnRegion().contains(player.getLocation())) {
            sender.sendMessage(Messages.MUST_BE_IN_SPAWN.getMessage());
            return true;
        }
        player.openInventory(this.plugin.getManagerHandler().getInventoryManager().getKitsInventory());
        return true;
    }
}
