package me.abhi.nitrogen.managers;

import me.abhi.nitrogen.kit.Kit;
import me.abhi.nitrogen.manager.Manager;
import me.abhi.nitrogen.manager.ManagerHandler;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;

public class InventoryManager extends Manager {

    private Inventory kitsInventory;

    public InventoryManager(ManagerHandler managerHandler) {
        super(managerHandler);
        createInventories();
    }

    private void createInventories() {
        kitsInventory = this.managerHandler.getPlugin().getServer().createInventory(null, 27, ChatColor.GOLD + "Select a Kit");
        loadKitsInventory();
    }

    private void loadKitsInventory() {
        for (Kit kit : this.managerHandler.getKitsManager().getKitList()) {
            kitsInventory.addItem(kit.getIcon());
        }
    }

    public Inventory getKitsInventory() {
        return kitsInventory;
    }
}
