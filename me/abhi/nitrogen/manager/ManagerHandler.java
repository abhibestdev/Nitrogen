package me.abhi.nitrogen.manager;

import me.abhi.nitrogen.Nitrogen;
import me.abhi.nitrogen.managers.*;

public class ManagerHandler {

    private Nitrogen plugin;
    private PlayerDataManager playerDataManager;
    private ScoreboardManager scoreboardManager;
    private ServerManager serverManager;
    private KitsManager kitsManager;
    private InventoryManager inventoryManager;
    private PlayerManager playerManager;

    public ManagerHandler(Nitrogen plugin) {
        this.plugin = plugin;
        this.playerDataManager = new PlayerDataManager(this);
        this.scoreboardManager = new ScoreboardManager(this);
        this.serverManager = new ServerManager(this);
        this.kitsManager = new KitsManager(this);
        this.inventoryManager = new InventoryManager(this);
        this.playerManager = new PlayerManager(this);
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public ServerManager getServerManager() {
        return serverManager;
    }

    public KitsManager getKitsManager() {
        return kitsManager;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public Nitrogen getPlugin() {
        return plugin;
    }
}
