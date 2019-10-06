package me.abhi.nitrogen.commands;

import me.abhi.nitrogen.Nitrogen;
import me.abhi.nitrogen.commands.subcommands.SetSpawnRegionSubCommand;
import me.abhi.nitrogen.util.LocationUtil;
import me.abhi.nitrogen.util.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {

    private Nitrogen plugin;
    private SetSpawnRegionSubCommand setSpawnRegionCommand;

    public SetSpawnCommand(Nitrogen plugin) {
        this.plugin = plugin;
        this.setSpawnRegionCommand = new SetSpawnRegionSubCommand(this.plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        if (!sender.hasPermission("nitrogen.command.setspawn")) {
            sender.sendMessage(Messages.NO_PERMISSION.getMessage());
            return true;
        }
        if (args.length < 1) {
            Player player = (Player) sender;
            this.plugin.getConfig().set("spawn.point", LocationUtil.getStringFromLocation(player.getLocation()));
            this.plugin.saveConfig();
            this.plugin.getManagerHandler().getServerManager().setSpawn(player.getLocation());
            sender.sendMessage(Messages.SPAWN_POINT_SET.getMessage());
            return true;
        }
        switch (args[0]) {
            case "region": {
                this.setSpawnRegionCommand.onCommand(sender, cmd, commandLabel, args);
            }
        }
        return true;
    }
}
