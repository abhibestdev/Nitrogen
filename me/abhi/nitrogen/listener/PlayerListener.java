package me.abhi.nitrogen.listener;

import me.abhi.nitrogen.Nitrogen;
import me.abhi.nitrogen.data.PlayerData;
import me.abhi.nitrogen.kit.Kit;
import me.abhi.nitrogen.kits.DistanceArcherKit;
import me.abhi.nitrogen.kits.FishermanKit;
import me.abhi.nitrogen.util.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class PlayerListener implements Listener {

    private Nitrogen plugin;

    public PlayerListener(Nitrogen plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (!this.plugin.getManagerHandler().getPlayerDataManager().hasPlayerData(player)) {
            this.plugin.getManagerHandler().getPlayerDataManager().addPlayer(player);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ScoreHelper.createScore(player);
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        if (!playerData.hasSession()) {
            playerData.setSession(true);
            this.plugin.getManagerHandler().getPlayerManager().teleportToSpawn(player);
            this.plugin.getManagerHandler().getPlayerManager().giveItems(player);
        }
        for (Player all : this.plugin.getServer().getOnlinePlayers()) {
            ScoreHelper.getByPlayer(player).addEnemy(all);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        ScoreHelper.removeScore(player);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (player.getItemInHand().equals(Items.KIT_MENU.getItemStack())) {
                event.setCancelled(true);
                player.openInventory(this.plugin.getManagerHandler().getInventoryManager().getKitsInventory());
            }
        }
        if (playerData.isSpawnSelection() && player.hasPermission("nitrogen.admin")) {
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                event.setCancelled(true);
                this.plugin.getConfig().set("spawn.region.1", LocationUtil.getStringFromLocation(event.getClickedBlock().getLocation()));
                this.plugin.saveConfig();
                player.sendMessage(Messages.SPAWN_POINT_1_SET.getMessage());
                return;
            }
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                event.setCancelled(true);
                this.plugin.getConfig().set("spawn.region.2", LocationUtil.getStringFromLocation(event.getClickedBlock().getLocation()));
                this.plugin.saveConfig();
                player.sendMessage(Messages.SPAWN_POINT_2_SET.getMessage());
                return;
            }
        }

    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        if (event.getInventory().getName().endsWith("Statistics")) {
            event.setCancelled(true);
        }
        if (event.getInventory().equals(this.plugin.getManagerHandler().getInventoryManager().getKitsInventory())) {
            for (Kit kit : this.plugin.getManagerHandler().getKitsManager().getKitList()) {
                if (event.getCurrentItem().equals(kit.getIcon())) {
                    event.setCancelled(true);
                    player.closeInventory();
                    this.plugin.getManagerHandler().getPlayerManager().resetPlayer(player);
                    kit.setContents(player);
                    playerData.setKit(kit);
                    player.sendMessage(Messages.GIVEN_KIT.getMessage().replace("%kit%", kit.getName()));
                }
            }
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getEntity();
            if (arrow.getShooter() instanceof Player) {
                Player player = (Player) arrow.getShooter();
                PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
                if (playerData.getKit() instanceof DistanceArcherKit) {
                    playerData.setShotLocation(player.getLocation());
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
            if (this.plugin.getManagerHandler().getServerManager().getSpawnRegion() != null && (this.plugin.getManagerHandler().getServerManager().getSpawnRegion().contains(player.getLocation()) || this.plugin.getManagerHandler().getServerManager().getSpawnRegion().contains(event.getDamager().getLocation()))) {
                event.setCancelled(true);
            }
            if (event.getDamager() instanceof Player && !event.isCancelled()) {
                Player damager = (Player) event.getDamager();
                PlayerData damagerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(damager);
                damagerData.setLastCombat(System.currentTimeMillis());
                playerData.setLastCombat(System.currentTimeMillis());
            }
            if (event.getDamager() instanceof Arrow && !event.isCancelled()) {
                Arrow arrow = (Arrow) event.getDamager();
                if (arrow.getShooter() instanceof Player) {
                    Player damager = (Player) arrow.getShooter();
                    if (damager == player) {
                        event.setCancelled(true);
                        return;
                    }
                    PlayerData damagerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(damager);
                    damagerData.setLastCombat(System.currentTimeMillis());
                    playerData.setLastCombat(System.currentTimeMillis());
                    if (damagerData.getKit() != null && (damagerData.getKit() instanceof DistanceArcherKit)) {
                        int distance = (int) damagerData.getShotLocation().distance(player.getLocation());
                        event.setDamage(event.getDamage() + (0.5 * distance));
                        damager.sendMessage(Messages.YOU_SHOT_PLAYER_FROM.getMessage().replace("%player%", player.getName()).replace("%blocks%", String.valueOf(distance)).replace("%health%", String.valueOf((int) event.getDamage())));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        event.setFoodLevel(20);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        Player player = event.getEntity();
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        playerData.setDeaths(playerData.getDeaths() + 1);
        playerData.setLastCombat(0);
        event.getDrops().clear();
        if (player.getKiller() != null) {
            Player killer = player.getKiller();
            PlayerData killerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(killer);
            killerData.setKills(killerData.getKills() + 1);
            killer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 120, 1));
            killer.sendMessage(Messages.YOU_KILLED.getMessage().replace("%player%", player.getName()));
            player.sendMessage(Messages.YOU_WERE_KILLED_BY.getMessage().replace("%player%", killer.getName()));
        } else {
            player.sendMessage(Messages.YOU_DIED.getMessage());
        }
        new BukkitRunnable() {
            public void run() {
                player.spigot().respawn();
            }
        }.runTaskLater(this.plugin, 5L);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        this.plugin.getManagerHandler().getPlayerManager().teleportToSpawn(player);
        this.plugin.getManagerHandler().getPlayerManager().giveItems(player);
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("nitrogen.staff")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("nitrogen.staff")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        Location from = event.getFrom();
        Location to = event.getTo();
        if ((int) from.getX() != (int) to.getX() || (int) from.getZ() != (int) to.getZ()) {
            if (playerData.getTeleportRunnable() != null) {
                playerData.getTeleportRunnable().cancel();
                player.sendMessage(Messages.TELEPORTATION_CANCELLED.getMessage());
                playerData.setTeleportRunnable(null);
            }
        }
        if (this.plugin.getManagerHandler().getServerManager().getSpawnRegion() != null && this.plugin.getManagerHandler().getServerManager().getSpawnRegion().contains(to) && (System.currentTimeMillis() - playerData.getLastCombat() <= 3000)) {
            event.setTo(from);
            player.sendMessage(Messages.CANT_DO_THIS_IN_COMBAT.getMessage());
        }
        if (playerData.isSpawn() && this.plugin.getManagerHandler().getServerManager().getSpawnRegion() != null && !this.plugin.getManagerHandler().getServerManager().getSpawnRegion().contains(player.getLocation())) {
            playerData.setSpawn(false);
            player.sendMessage(Messages.LEAVING_SPAWN.getMessage());
            player.sendMessage(Messages.ENTERING_WARZONE.getMessage());
            return;
        }
        if (!playerData.isSpawn() && this.plugin.getManagerHandler().getServerManager().getSpawnRegion() != null && this.plugin.getManagerHandler().getServerManager().getSpawnRegion().contains(player.getLocation())) {
            playerData.setSpawn(true);
            player.sendMessage(Messages.ENTERING_SPAWN.getMessage());
            player.sendMessage(Messages.LEAVING_WARZONE                 .getMessage());
        }
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        if (playerData.getKit() != null && playerData.getKit() instanceof FishermanKit) {
            if (this.plugin.getManagerHandler().getServerManager().getSpawnRegion() != null && this.plugin.getManagerHandler().getServerManager().getSpawnRegion().contains(player.getLocation())) {
                event.setCancelled(true);
                player.sendMessage(Messages.CANT_DO_THIS_IN_SPAWN.getMessage());
            }
            if (event.getCaught() instanceof Player) {
                if (System.currentTimeMillis() - playerData.getLastHook() <= 10000) {
                    event.setCancelled(true);
                    player.sendMessage(Messages.HAVE_TO_WAIT_TO_HOOK.getMessage().replace("%seconds%", String.valueOf(10 - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - playerData.getLastHook()))));
                } else {
                    Player caught = (Player) event.getCaught();
                    if (caught == player) {
                        return;
                    }
                    if (player.getItemInHand().getType() == Material.FISHING_ROD) {
                        if (playerData.getKit() instanceof FishermanKit) {
                            caught.teleport(player);
                            player.sendMessage(Messages.HOOKED_PLAYER.getMessage().replace("%player%", caught.getName()));
                            caught.sendMessage(Messages.HOOKED_BY_PLAYER.getMessage().replace("%player%", player.getName()));
                            playerData.setCaught(null);
                            playerData.setLastHook(System.currentTimeMillis());
                            new BukkitRunnable() {
                                public void run() {
                                    if (playerData.getKit() instanceof FishermanKit && playerData.getLastHook() != 0) {
                                        player.sendMessage(Messages.YOU_MAY_HOOK_AGAIN.getMessage());
                                    }
                                }
                            }.runTaskLaterAsynchronously(this.plugin, 200L);
                        }
                    }
                }
            }
            if (event.getState() == PlayerFishEvent.State.FAILED_ATTEMPT || event.getState() == PlayerFishEvent.State.IN_GROUND) {
                if (event.getHook().getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
                    if (System.currentTimeMillis() - playerData.getLastHook() <= 10000) {
                        player.sendMessage(Messages.HAVE_TO_WAIT_TO_HOOK.getMessage().replace("%seconds%", String.valueOf(10 - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - playerData.getLastHook()))));
                        return;
                    }
                    if (this.plugin.getManagerHandler().getServerManager().getSpawnRegion() != null && this.plugin.getManagerHandler().getServerManager().getSpawnRegion().contains(event.getHook().getLocation())) {
                        player.sendMessage(Messages.CANT_DO_THIS_IN_SPAWN.getMessage());
                        return;
                    }
                    Util.pullEntityToLocation(player, event.getHook().getLocation());
                    playerData.setLastHook(System.currentTimeMillis());
                    player.sendMessage(Messages.YOU_GRAPPLED.getMessage());
                    new BukkitRunnable() {
                        public void run() {
                            if (playerData.getKit() instanceof FishermanKit && playerData.getLastHook() != 0) {
                                player.sendMessage(Messages.YOU_MAY_HOOK_AGAIN.getMessage());
                            }
                        }
                    }.runTaskLaterAsynchronously(this.plugin, 200L);
                }
            }
        }
    }

    @EventHandler
    public void onWeather(WeatherChangeEvent event) {
        event.setCancelled(true);
    }
}
