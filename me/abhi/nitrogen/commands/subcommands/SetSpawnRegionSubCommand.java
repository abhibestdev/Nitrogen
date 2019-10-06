package me.abhi.nitrogen.commands.subcommands;

import me.abhi.nitrogen.Nitrogen;
import me.abhi.nitrogen.data.PlayerData;
import me.abhi.nitrogen.util.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnRegionSubCommand implements CommandExecutor {

    private Nitrogen plugin;

    public SetSpawnRegionSubCommand(Nitrogen plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player player = (Player) sender;
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        if (playerData.isSpawnSelection()) {
            playerData.setSpawnSelection(false);
            sender.sendMessage(Messages.LEFT_SPAWN_SELECTION.getMessage());
            return true;
        }
        playerData.setSpawnSelection(true);
        sender.sendMessage(Messages.ENTERED_SPAWN_SELECTION.getMessage());
        return true;
    }
}
