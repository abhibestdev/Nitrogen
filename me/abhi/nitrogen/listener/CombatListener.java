package me.abhi.nitrogen.listener;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import me.abhi.nitrogen.Nitrogen;
import me.abhi.nitrogen.data.PlayerData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.server.PluginDisableEvent;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CombatListener implements Listener {

    private Nitrogen plugin;
    private static List<BlockFace> ALL_DIRECTIONS = ImmutableList.of(BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST);
    private Map<UUID, Set<Location>> previousUpdates;
    private ExecutorService executorService;

    public CombatListener(Nitrogen plugin) {
        this.previousUpdates = new HashMap<>();
        this.executorService = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("Nitrogen ForceField Thread").build());
        this.plugin = plugin;
    }

    @EventHandler
    public void shutdown(PluginDisableEvent event) {
        if (event.getPlugin() != this.plugin) {
            return;
        }
        this.executorService.shutdown();
        try {
            this.executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException ex) {
        }
        for (UUID uuid : this.previousUpdates.keySet()) {
            for (Player all : this.plugin.getServer().getOnlinePlayers()) {
                for (Location location : this.previousUpdates.get(uuid)) {
                    Block block = location.getBlock();
                    all.sendBlockChange(location, block.getType(), block.getData());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void updateViewedBlocks(PlayerMoveEvent event) {
        if (this.plugin.getManagerHandler().getServerManager().getSpawnRegion() == null) {
            return;
        }
        Location t = event.getTo();
        Location f = event.getFrom();
        if (t.getBlockX() == f.getBlockX() && t.getBlockY() == f.getBlockY() && t.getBlockZ() == f.getBlockZ()) {
            return;
        }
        Player player = event.getPlayer();
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        this.executorService.submit(new Runnable() {
            @Override
            public void run() {
                UUID uuid = player.getUniqueId();
                if (!player.isOnline()) {
                    previousUpdates.remove(uuid);
                    return;
                }
                Set<Location> changedBlocks = getChangedBlocks(player);
                Material forceFieldMaterial = Material.STAINED_GLASS;
                Set<Location> removeBlocks;
                if (previousUpdates.containsKey(uuid)) {
                    removeBlocks = previousUpdates.get(uuid);
                } else {
                    removeBlocks = new HashSet<>();
                }
                for (Location location : changedBlocks) {
                    if (location.getBlock().getRelative(BlockFace.DOWN).getType() == Material.BEDROCK) {
                        for (int i = 0; i <= 5; i++) {
                            player.sendBlockChange(location.getBlock().getLocation().add(0, i, 0), forceFieldMaterial, (byte) 14);
                        }
                    }
                    removeBlocks.remove(location);
                }

                for (Location location : removeBlocks) {
                    Block block = location.getBlock();
                    if (location.getBlock().getRelative(BlockFace.DOWN).getType() == Material.BEDROCK) {
                        for (int i = 0; i <= 5; i++) {
                            player.sendBlockChange(location.getBlock().getLocation().add(0, i, 0), block.getType(), block.getData());
                        }
                    }
                }
                previousUpdates.put(uuid, changedBlocks);
            }
        });
    }

    private Set<Location> getChangedBlocks(Player player) {
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        Set<Location> locations = new HashSet<>();
        if (System.currentTimeMillis() - playerData.getLastCombat() >= 30000) {
            return locations;
        }
        int r = 10;
        Location l = player.getLocation();
        Location loc1 = l.clone().add((double) r, 0.0, (double) r);
        Location loc2 = l.clone().subtract((double) r, 0.0, (double) r);
        int topBlockX = (loc1.getBlockX() < loc2.getBlockX()) ? loc2.getBlockX() : loc1.getBlockX();
        int bottomBlockX = (loc1.getBlockX() > loc2.getBlockX()) ? loc2.getBlockX() : loc1.getBlockX();
        int topBlockZ = (loc1.getBlockZ() < loc2.getBlockZ()) ? loc2.getBlockZ() : loc1.getBlockZ();
        int bottomBlockZ = (loc1.getBlockZ() > loc2.getBlockZ()) ? loc2.getBlockZ() : loc1.getBlockZ();
        for (int x = bottomBlockX; x <= topBlockX; ++x) {
            for (int z = bottomBlockZ; z <= topBlockZ; ++z) {
                Location location = new Location(l.getWorld(), (double) x, l.getY(), (double) z);
                if (this.plugin.getManagerHandler().getServerManager().getSpawnRegion().contains(location)) {
                    if (this.isPvpSurrounding(location)) {
                        for (int i = -r; i < r; ++i) {
                            Location loc3 = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
                            loc3.setY(loc3.getY() + i);
                            if (loc3.getBlock().getType().equals(Material.AIR)) {
                                locations.add(new Location(loc3.getWorld(), (double) loc3.getBlockX(), (double) loc3.getBlockY(), (double) loc3.getBlockZ()));
                            }
                        }
                    }
                }
            }
        }
        return locations;
    }

    private boolean isPvpSurrounding(Location loc) {
        for (BlockFace direction : ALL_DIRECTIONS) {
            if (!this.plugin.getManagerHandler().getServerManager().getSpawnRegion().contains(loc.getBlock().getRelative(direction).getLocation())) {
                return true;
            }
        }
        return false;
    }
}
