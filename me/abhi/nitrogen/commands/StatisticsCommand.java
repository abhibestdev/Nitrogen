package me.abhi.nitrogen.commands;

import me.abhi.nitrogen.Nitrogen;
import me.abhi.nitrogen.util.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatisticsCommand implements CommandExecutor {

    private Nitrogen plugin;

    public StatisticsCommand(Nitrogen plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        if (args.length != 1) {
            this.plugin.getManagerHandler().getPlayerManager().statsInventory(player, player);
            return true;
        }
        Player target = this.plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(Messages.COULD_NOT_FIND_PLAYER.getMessage());
            return true;
        }
        this.plugin.getManagerHandler().getPlayerManager().statsInventory(player, target);
        return true;
    }
}
