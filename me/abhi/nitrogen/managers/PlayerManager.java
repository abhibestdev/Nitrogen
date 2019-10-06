package me.abhi.nitrogen.managers;

import me.abhi.nitrogen.data.PlayerData;
import me.abhi.nitrogen.manager.Manager;
import me.abhi.nitrogen.manager.ManagerHandler;
import me.abhi.nitrogen.util.ItemBuilder;
import me.abhi.nitrogen.util.Items;
import me.abhi.nitrogen.util.Messages;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class PlayerManager extends Manager {

    public PlayerManager(ManagerHandler managerHandler) {
        super(managerHandler);
    }

    public void giveItems(Player player) {
        resetPlayer(player);
        player.getInventory().setItem(Items.KIT_MENU.getSlot(), Items.KIT_MENU.getItemStack());
    }

    public void resetPlayer(Player player) {
        PlayerData playerData = this.managerHandler.getPlayerDataManager().getPlayerData(player);
        playerData.setKit(null);
        playerData.setCaught(null);
        playerData.setLastHook(0);
        player.getInventory().setArmorContents(null);
        player.getInventory().clear();
        player.updateInventory();
        player.setHealth(20.0);
        player.setFoodLevel(20);
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
    }

    public void statsInventory(Player player, Player player2) {
        PlayerData playerData = this.managerHandler.getPlayerDataManager().getPlayerData(player);
        PlayerData playerData2 = this.managerHandler.getPlayerDataManager().getPlayerData(player2);
        ItemStack kills = new ItemBuilder(Material.DIAMOND_SWORD).setName(ChatColor.GREEN + "Kills: " + ChatColor.WHITE + playerData.getKills()).toItemStack();
        ItemStack deaths = new ItemBuilder(Material.SKULL_ITEM).setName(ChatColor.GREEN + "Deaths: " + ChatColor.WHITE + playerData.getDeaths()).toItemStack();
        ItemStack KDR = new ItemBuilder(Material.BOW).setName(ChatColor.GREEN + "KD/R: " + ChatColor.WHITE + (playerData.getDeaths() == 0 ? "Infinite" : (double) playerData.getKills() / playerData.getKills())).toItemStack();
        Inventory inventory = this.managerHandler.getPlugin().getServer().createInventory(null, 9, ChatColor.GOLD + player2.getName() + "'s Statistics");
        inventory.setItem(0, kills);
        inventory.setItem(1, deaths);
        inventory.setItem(2, KDR);
        player.openInventory(inventory);
    }

    public void teleportToSpawn(Player player) {
        PlayerData playerData = this.managerHandler.getPlayerDataManager().getPlayerData(player);
        playerData.setLastCombat(0);
        player.teleport(this.managerHandler.getServerManager().getSpawn());
        player.sendMessage(Messages.TELEPORTING_TO_SPAWN.getMessage());
    }
}
