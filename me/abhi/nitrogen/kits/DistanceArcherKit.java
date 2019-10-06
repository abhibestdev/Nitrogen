package me.abhi.nitrogen.kits;

import me.abhi.nitrogen.kit.Kit;
import me.abhi.nitrogen.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

public class DistanceArcherKit extends Kit {

    public DistanceArcherKit() {
        super("Distance Archer", Material.BOW, "Fire an arrow when least expected!", "The further you shoot from, the", "more damage you do!");
    }

    @Override
    public void setContents(Player player) {
        player.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setUnbreakable(true).toItemStack());
        player.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setUnbreakable(true).toItemStack());
        player.getInventory().setLeggings(new ItemBuilder(Material.LEATHER_LEGGINGS).setUnbreakable(true).toItemStack());
        player.getInventory().setBoots(new ItemBuilder(Material.LEATHER_BOOTS).setUnbreakable(true).toItemStack());
        player.getInventory().setItem(0, new ItemBuilder(Material.WOOD_SWORD).setUnbreakable(true).toItemStack());
        player.getInventory().setItem(1, new ItemBuilder(Material.BOW).addEnchant(Enchantment.ARROW_INFINITE, 1).setUnbreakable(true).toItemStack());
        player.getInventory().setItem(8, new ItemBuilder(Material.ARROW).toItemStack());
        player.updateInventory();
    }
}
