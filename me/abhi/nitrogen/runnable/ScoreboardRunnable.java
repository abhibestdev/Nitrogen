package me.abhi.nitrogen.runnable;

import me.abhi.nitrogen.Nitrogen;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ScoreboardRunnable extends BukkitRunnable {

    private Nitrogen plugin;

    public ScoreboardRunnable(Nitrogen plugin) {
        this.plugin = plugin;
    }

    public void run() {
        for (Player all : this.plugin.getServer().getOnlinePlayers()) {
            this.plugin.getManagerHandler().getScoreboardManager().update(all);
        }
    }
}
