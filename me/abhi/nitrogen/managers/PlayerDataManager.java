package me.abhi.nitrogen.managers;

import me.abhi.nitrogen.data.PlayerData;
import me.abhi.nitrogen.manager.Manager;
import me.abhi.nitrogen.manager.ManagerHandler;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager extends Manager {

    private Map<UUID, PlayerData> playerDataMap;

    public PlayerDataManager(ManagerHandler managerHandler) {
        super(managerHandler);
        this.playerDataMap = new HashMap<>();
    }

    public void addPlayer(Player player) {
        playerDataMap.put(player.getUniqueId(), new PlayerData());
    }

    public void removePlayer(Player player) {
        playerDataMap.remove(player.getUniqueId());
    }

    public PlayerData getPlayerData(Player player) {
        return playerDataMap.get(player.getUniqueId());
    }

    public boolean hasPlayerData(Player player) {
        return getPlayerData(player) != null;
    }
}
