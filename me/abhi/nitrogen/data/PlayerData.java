package me.abhi.nitrogen.data;

import me.abhi.nitrogen.kit.Kit;
import me.abhi.nitrogen.util.BlockPos;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlayerData {

    private List<String> slots;
    private int balance;
    private int kills;
    private int deaths;
    private boolean session;
    private Kit kit;
    private Location shotLocation;
    private boolean spawnSelection;
    private long lastCombat;
    private BukkitTask teleportRunnable;
    private long lastHook;
    private Player caught;
    private Location from;
    private Location to;
    private Collection<BlockPos> lastShownBlocks;
    private boolean spawn = true;

    public PlayerData() {
        this.slots = new ArrayList<>();
    }

    public List<String> getSlots() {
        return slots;
    }

    public int getBalance() {
        return balance;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public boolean hasSession() {
        return session;
    }

    public Kit getKit() {
        return kit;
    }

    public Location getShotLocation() {
        return shotLocation;
    }

    public boolean isSpawnSelection() {
        return spawnSelection;
    }

    public long getLastCombat() {
        return lastCombat;
    }

    public BukkitTask getTeleportRunnable() {
        return teleportRunnable;
    }

    public long getLastHook() {
        return lastHook;
    }

    public Player getCaught() {
        return caught;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }

    public Collection<BlockPos> getLastShownBlocks() {
        return lastShownBlocks;
    }

    public boolean isSpawn() {
        return spawn;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setSession(boolean session) {
        this.session = session;
    }

    public void setKit(Kit kit) {
        this.kit = kit;
    }

    public void setShotLocation(Location shotLocation) {
        this.shotLocation = shotLocation;
    }

    public void setSpawnSelection(boolean spawnSelection) {
        this.spawnSelection = spawnSelection;
    }

    public void setLastCombat(long lastCombat) {
        this.lastCombat = lastCombat;
    }

    public void setTeleportRunnable(BukkitTask teleportRunnable) {
        this.teleportRunnable = teleportRunnable;
    }

    public void setLastHook(long lastHook) {
        this.lastHook = lastHook;
    }

    public void setCaught(Player caught) {
        this.caught = caught;
    }

    public void setFrom(Location from) {
        this.from = from;
    }

    public void setTo(Location to) {
        this.to = to;
    }

    public void setLastShownBlocks(Collection<BlockPos> lastShownBlocks) {
        this.lastShownBlocks = lastShownBlocks;
    }

    public void setSpawn(boolean spawn) {
        this.spawn = spawn;
    }
}
