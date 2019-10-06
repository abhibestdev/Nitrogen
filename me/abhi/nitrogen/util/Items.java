package me.abhi.nitrogen.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Items {

    KIT_MENU(new ItemBuilder(Material.WATCH).setName(ChatColor.GOLD + "Select a Kit").toItemStack(), 4);

    private ItemStack itemStack;
    private int slot;

    private Items(ItemStack itemStack, int slot) {
        this.itemStack = itemStack;
        this.slot = slot;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public int getSlot() {
        return slot;
    }
}
