package me.abhi.nitrogen.managers;

import me.abhi.nitrogen.data.PlayerData;
import me.abhi.nitrogen.manager.Manager;
import me.abhi.nitrogen.manager.ManagerHandler;
import me.abhi.nitrogen.util.ScoreHelper;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class ScoreboardManager extends Manager {

    public ScoreboardManager(ManagerHandler managerHandler) {
        super(managerHandler);
    }

    public void update(Player player) {
        try {
            if (ScoreHelper.hasScore(player)) {
                PlayerData playerData = managerHandler.getPlayerDataManager().getPlayerData(player);
                ScoreHelper scoreHelper = ScoreHelper.getByPlayer(player);
                DecimalFormat decimalFormat = new DecimalFormat("0");
                scoreHelper.setTitle("&aKitPvP");
                playerData.getSlots().clear();
                playerData.getSlots().add("");
                playerData.getSlots().add("&aTerritory");
                if (this.managerHandler.getServerManager().getSpawnRegion() != null && !this.managerHandler.getServerManager().getSpawnRegion().contains(player.getLocation())) {
                    playerData.getSlots().add("War Zone &7(&cUnsafe&7)");
                } else {
                    playerData.getSlots().add("Spawn Island &7(&aSafe&7)");
                }
                playerData.getSlots().add("");
                if (System.currentTimeMillis() - playerData.getLastCombat() <= 30000 || System.currentTimeMillis() - playerData.getLastHook() <= 10000) {
                    playerData.getSlots().add("&aTimers");
                    if (System.currentTimeMillis() - playerData.getLastCombat() <= 30000) {
                    playerData.getSlots().add("&cCombat: &f" + (30 - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - playerData.getLastCombat())) + "s");
                }
                if (System.currentTimeMillis() - playerData.getLastHook() <= 10000) {
                    playerData.getSlots().add("&eAbility: &f" + (10 - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - playerData.getLastHook())) + "s");
                }
                    playerData.getSlots().add("");
                }
                playerData.getSlots().add("&aKit");
                if (playerData.getKit() == null) {
                    playerData.getSlots().add("None");
                } else {
                    playerData.getSlots().add(playerData.getKit().getName());
                }
                playerData.getSlots().add("");
                playerData.getSlots().add("&aserver-ip.com");
                scoreHelper.setSlotsFromList(playerData.getSlots());
            }
        } catch (Exception ex) {
        }
    }
}
