package me.abhi.nitrogen.kits;

import me.abhi.nitrogen.kit.Kit;
import me.abhi.nitrogen.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class FishermanKit extends Kit {

    public FishermanKit() {
        super("Fisherman", Material.FISHING_ROD, "Reel in your opponents and use your", "fishing rod as a grappling hook!");
    }

    @Override
    public void setContents(Player player) {
        player.getInventory().setHelmet(new ItemBuilder(Material.GOLD_HELMET).setUnbreakable(true).toItemStack());
        player.getInventory().setChestplate(new ItemBuilder(Material.GOLD_CHESTPLATE).setUnbreakable(true).toItemStack());
        player.getInventory().setLeggings(new ItemBuilder(Material.GOLD_LEGGINGS).setUnbreakable(true).toItemStack());
        player.getInventory().setBoots(new ItemBuilder(Material.GOLD_BOOTS).setUnbreakable(true).toItemStack());
        player.getInventory().setItem(0, new ItemBuilder(Material.IRON_SWORD).setUnbreakable(true).toItemStack());
        player.getInventory().setItem(1, new ItemBuilder(Material.FISHING_ROD).setUnbreakable(true).toItemStack());
        player.updateInventory();
    }
}
