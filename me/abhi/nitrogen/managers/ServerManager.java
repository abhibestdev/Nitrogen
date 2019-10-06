package me.abhi.nitrogen.managers;

import me.abhi.nitrogen.manager.Manager;
import me.abhi.nitrogen.manager.ManagerHandler;
import me.abhi.nitrogen.util.Border;
import me.abhi.nitrogen.util.LocationUtil;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class ServerManager extends Manager {

    private Location spawn;
    private Location spawnPoint1;
    private Location spawnPoint2;
    private Border spawnRegion;

    public ServerManager(ManagerHandler managerHandler) {
        super(managerHandler);
        load();
    }

    private void load() {
        if (this.managerHandler.getPlugin().getConfig().get("spawn.point") != null) {
            this.spawn = LocationUtil.getLocationFromString(this.managerHandler.getPlugin().getConfig().getString("spawn.point"));
        }
        if (this.managerHandler.getPlugin().getConfig().get("spawn.region.1") != null && this.managerHandler.getPlugin().getConfig().get("spawn.region.2") != null) {
            this.spawnPoint1 = LocationUtil.getLocationFromString(this.managerHandler.getPlugin().getConfig().getString("spawn.region.1"));
            this.spawnPoint2 = LocationUtil.getLocationFromString(this.managerHandler.getPlugin().getConfig().getString("spawn.region.2"));
            this.spawnRegion = new Border(new Vector(spawnPoint1.getX(), spawnPoint1.getY(), spawnPoint1.getZ()), new Vector(spawnPoint2.getX(), spawnPoint2.getY(), spawnPoint2.getZ()));
        }
    }

    public Location getSpawn() {
        return spawn;
    }

    public Border getSpawnRegion() {
        return spawnRegion;
    }

    public Location getSpawnPoint1() {
        return spawnPoint1;
    }

    public Location getSpawnPoint2() {
        return spawnPoint2;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }
}
