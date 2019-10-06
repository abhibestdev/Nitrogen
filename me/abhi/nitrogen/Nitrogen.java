package me.abhi.nitrogen;

import me.abhi.nitrogen.commands.KitsCommand;
import me.abhi.nitrogen.commands.SetSpawnCommand;
import me.abhi.nitrogen.commands.SpawnCommand;
import me.abhi.nitrogen.commands.StatisticsCommand;
import me.abhi.nitrogen.data.PlayerData;
import me.abhi.nitrogen.listener.CombatListener;
import me.abhi.nitrogen.listener.PlayerListener;
import me.abhi.nitrogen.manager.ManagerHandler;
import me.abhi.nitrogen.runnable.ScoreboardRunnable;
import me.abhi.nitrogen.util.ScoreHelper;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Nitrogen extends JavaPlugin {

    private static Nitrogen instance;
    private ManagerHandler managerHandler;

    @Override
    public void onEnable() {
        instance = this;
        createFiles();
        registerManagers();
        registerListeners();
        registerCommands();
        registerPlayers();
        registerRunnables();
    }

    private void createFiles() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }

    private void registerManagers() {
        managerHandler = new ManagerHandler(this);
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        this.getServer().getPluginManager().registerEvents(new CombatListener(this), this);
    }

    private void registerCommands() {
        getCommand("setspawn").setExecutor(new SetSpawnCommand(this));
        getCommand("spawn").setExecutor(new SpawnCommand(this));
        getCommand("kits").setExecutor(new KitsCommand(this));
        getCommand("statistics").setExecutor(new StatisticsCommand(this));
    }

    private void registerPlayers() {
        for (Player all : this.getServer().getOnlinePlayers()) {
            managerHandler.getPlayerDataManager().addPlayer(all);
            ScoreHelper.createScore(all);
            PlayerData playerData = this.managerHandler.getPlayerDataManager().getPlayerData(all);
            playerData.setSession(true);
            managerHandler.getPlayerManager().teleportToSpawn(all);
            managerHandler.getPlayerManager().giveItems(all);
            for (Player all1 : this.getServer().getOnlinePlayers()) {
                if (all != all1) {
                    ScoreHelper.getByPlayer(all).addEnemy(all1);
                }
            }
        }
    }

    private void registerRunnables() {
        new ScoreboardRunnable(this).runTaskTimerAsynchronously(this, 0L, 0L);
        //  new CombatRunnable(this).runTaskTimerAsynchronously(this, 0L, 0L);
    }

    public static Nitrogen getInstance() {
        return instance;
    }

    public ManagerHandler getManagerHandler() {
        return managerHandler;
    }
}
