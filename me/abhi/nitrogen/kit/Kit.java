package me.abhi.nitrogen.kit;

import me.abhi.nitrogen.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Kit {

    private String name;
    private List<String> lore;
    private Material material;
    private ItemStack icon;

    protected Kit(String name, Material material, String... description) {
        this.name = name;
        this.material = material;
        this.lore = new ArrayList<>();
        lore.add(ChatColor.GOLD + "" + ChatColor.STRIKETHROUGH + "-----------------------------");
        for (String line : description) {
            lore.add(ChatColor.GRAY + line);
        }
        lore.add(ChatColor.GOLD + "" + ChatColor.STRIKETHROUGH + "-----------------------------");
        this.icon = new ItemBuilder(material).setName(ChatColor.AQUA + name).setLore(lore).toItemStack();
    }

    public ItemStack getIcon() {
        return icon;
    }

    public void setContents(Player player) {
    }

    public String getName() {
        return name;
    }
}
