package me.abhi.nitrogen.kits;

import me.abhi.nitrogen.kit.Kit;
import me.abhi.nitrogen.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

            public class IronKit extends Kit {

    public IronKit() {
        super("Iron", Material.IRON_CHESTPLATE, "Dominate your opponents with this classic!");
    }

    @Override
    public void setContents(Player player) {
        player.getInventory().setHelmet(new ItemBuilder(Material.IRON_HELMET).setUnbreakable(true).toItemStack());
        player.getInventory().setChestplate(new ItemBuilder(Material.IRON_CHESTPLATE).setUnbreakable(true).toItemStack());
        player.getInventory().setLeggings(new ItemBuilder(Material.IRON_LEGGINGS).setUnbreakable(true).toItemStack());
        player.getInventory().setBoots(new ItemBuilder(Material.IRON_BOOTS).setUnbreakable(true).toItemStack());
        player.getInventory().setItem(0, new ItemBuilder(Material.DIAMOND_SWORD).setUnbreakable(true).toItemStack());
        player.updateInventory();
    }
}
