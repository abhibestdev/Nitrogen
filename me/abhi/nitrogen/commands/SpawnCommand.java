package me.abhi.nitrogen.commands;

import me.abhi.nitrogen.Nitrogen;
import me.abhi.nitrogen.data.PlayerData;
import me.abhi.nitrogen.util.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class SpawnCommand implements CommandExecutor {

    private Nitrogen plugin;

    public SpawnCommand(Nitrogen plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        if (sender.hasPermission("nitrogen.staff")) {
            this.plugin.getManagerHandler().getPlayerManager().teleportToSpawn(player);
            return true;
        }

        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        if (System.currentTimeMillis() - playerData.getLastCombat() <= 30000) {
            sender.sendMessage(Messages.CANT_DO_THIS_IN_COMBAT.getMessage());
            return true;
        }
        if (playerData.getTeleportRunnable() != null) {
            sender.sendMessage(Messages.ALREADY_TELEPORTING.getMessage());
            return true;
        }
        sender.sendMessage(Messages.TELEPORTING.getMessage());
        BukkitTask teleportRunnable = new BukkitRunnable() {
            int i = 6;

            public void run() {
                if (i > 1) {
                    sender.sendMessage(Messages.TELEPORTING_IN.getMessage().replace("%seconds%", String.valueOf(i - 1)));
                }
                i--;
                if (i == 0) {
                    this.cancel();
                    plugin.getManagerHandler().getPlayerManager().teleportToSpawn(player);
                    playerData.setTeleportRunnable(null);
                }
            }
        }.runTaskTimerAsynchronously(this.plugin, 0L, 20L);
        playerData.setTeleportRunnable(teleportRunnable);
        return true;
    }
}
