package me.abhi.nitrogen.runnable;

import me.abhi.nitrogen.Nitrogen;
import me.abhi.nitrogen.data.PlayerData;
import me.abhi.nitrogen.util.Util;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class CombatRunnable extends BukkitRunnable {

    private Nitrogen plugin;
    private HashMap<UUID, ArrayList<Location>> wallLocations;
    private HashMap<UUID, Map<Location, Boolean>> sentChanges = new HashMap<>();

    public CombatRunnable(Nitrogen plugin) {
        this.plugin = plugin;
        this.wallLocations = new HashMap<>();
    }

    public void run() {
        for (Player player : this.plugin.getServer().getOnlinePlayers()) {
            if (!sentChanges.containsKey(player.getUniqueId())) {
                sentChanges.put(player.getUniqueId(), new HashMap<>());
                return;
            }
            Map<Location, Boolean> sentChanges = this.sentChanges.get(player.getUniqueId());
            PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
            Location from = playerData.getFrom();
            Location to = playerData.getTo();
            if (to != null && from != null) {
                player.sendMessage(MinecraftServer.getServer().tps1.getAverage() + "");
                if (System.currentTimeMillis() - playerData.getLastCombat() <= 30000 && this.plugin.getManagerHandler().getServerManager().getSpawnRegion().contains(to)) {
                    Location min = new Location(this.plugin.getManagerHandler().getServerManager().getSpawnPoint1().getWorld(), this.plugin.getManagerHandler().getServerManager().getSpawnPoint1().getBlockX(), player.getLocation().getBlockY(), this.plugin.getManagerHandler().getServerManager().getSpawnPoint1().getBlockZ());
                    Location max = new Location(this.plugin.getManagerHandler().getServerManager().getSpawnPoint2().getWorld(), this.plugin.getManagerHandler().getServerManager().getSpawnPoint2().getBlockX(), player.getLocation().getBlockY(), this.plugin.getManagerHandler().getServerManager().getSpawnPoint2().getBlockZ());
                    List<Block> blocks = Util.blocksFromTwoPoints(min, max);
                    List<Location> locs = new ArrayList<>();
                    for (Block block : blocks) {
                        if (to.distance(block.getLocation()) <= 5.0) {
                            if (block.getRelative(BlockFace.DOWN).getType() == Material.BEDROCK) { //Only spawn glass on the bedrock blocks around spawn claim
                                for (int i = 0; i < 5; i++) {
                                    locs.add(block.getLocation().add(0, i, 0));
                                }
                            }
                        } else if (to.distance(block.getLocation()) >= 5 && to.distance(block.getLocation()) >= 7) {
                            for (int i = 0; i < 5; i++) {
                                if (sentChanges.getOrDefault(block.getLocation().add(0, i, 0), false)) {
                                    player.sendBlockChange(block.getLocation().add(0, i, 0), Material.AIR, (byte) 0);
                                    sentChanges.put(block.getLocation().add(0, i, 0), false);
                                }
                            }
                        }
                    }
                    if (!locs.isEmpty()) {
                        for (Location loc : locs) {
                            if (!sentChanges.getOrDefault(loc, false)) {
                                player.sendBlockChange(loc, Material.STAINED_GLASS, (byte) 14);
                                ArrayList<Location> list = new ArrayList<>();
                                if (wallLocations.containsKey(player.getUniqueId())) {
                                    list.addAll(wallLocations.get(player.getUniqueId()));
                                }
                                list.add(loc);
                                wallLocations.put(player.getUniqueId(), list);
                                sentChanges.put(loc, true);
                            }
                        }
                    }
                } else {
                    Location min = new Location(this.plugin.getManagerHandler().getServerManager().getSpawnPoint1().getWorld(), this.plugin.getManagerHandler().getServerManager().getSpawnPoint1().getBlockX(), player.getLocation().getBlockY(), this.plugin.getManagerHandler().getServerManager().getSpawnPoint1().getBlockZ());
                    Location max = new Location(this.plugin.getManagerHandler().getServerManager().getSpawnPoint2().getWorld(), this.plugin.getManagerHandler().getServerManager().getSpawnPoint2().getBlockX(), player.getLocation().getBlockY(), this.plugin.getManagerHandler().getServerManager().getSpawnPoint2().getBlockZ());
                    List<Block> blocks = Util.blocksFromTwoPoints(min, max);
                    List<Location> locs = new ArrayList<>();
                    for (Block block : blocks) {
                        if (to.distance(block.getLocation()) >= 5 && to.distance(block.getLocation()) >= 7) {
                            for (int i = 0; i < 5; i++) {
                                if (sentChanges.getOrDefault(block.getLocation().add(0, i, 0), false)) {
                                    player.sendBlockChange(block.getLocation().add(0, i, 0), Material.AIR, (byte) 0);
                                    sentChanges.put(block.getLocation().add(0, i, 0), false);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
