package me.abhi.nitrogen.util;

import org.bukkit.ChatColor;

public enum Messages {

    NO_PERMISSION("&cNo permission."),
    LEAVING_SPAWN("&eNow leaving: &aSpawn &7(&aSafe&c&7)"),
    ENTERING_SPAWN("&eNow entering: &aSpawn &7(&aSafe&c&7)"),
    LEAVING_WARZONE("&eNow leaving: &cWar Zone &7(&cUnsafe&c&7)"),
    ENTERING_WARZONE("&eNow entering: &cWar Zone &7(&cUnsafe&c&7)"),
    HOOKED_BY_PLAYER("&eYou have been hooked by &d%player%&e!"),
    HOOKED_PLAYER("&eYou hooked player &d%player%&e!"),
    YOU_DIED("&cYou died."),
    YOU_WERE_KILLED_BY("&eYou were killed by &d%player%&e."),
    YOU_KILLED("&eYou killed player &d%player%&e."),
    MUST_BE_IN_SPAWN("&cYou must in be at spawn to do this!"),
    YOU_GRAPPLED("&eYou have grappled!"),
    HAVE_TO_WAIT_TO_HOOK("&eYou must wait &d%seconds%s &eto do that again!"),
    YOU_MAY_HOOK_AGAIN("&eYour may use your hook ability!"),
    TELEPORTATION_CANCELLED("&cYou moved! Teleportation cancelled."),
    TELEPORTING("&eYou will be teleported soon. Don't move!"),
    TELEPORTING_TO_SPAWN("&eTeleporting you to spawn."),
    TELEPORTING_IN("&eTeleporting you in &d%seconds%..."),
    ALREADY_TELEPORTING("&cYou are already teleporting somewhere!"),
    CANT_DO_THIS_IN_COMBAT("&cYou can't do this while combat tagged."),
    LEFT_SPAWN_SELECTION("&cExited Spawn Region selection mode."),
    ENTERED_SPAWN_SELECTION("&aEntered Spawn Region selection mode."),
    SPAWN_POINT_SET("&aSpawn point has been set."),
    SPAWN_POINT_1_SET("&aSpawn point 1 has been set."),
    SPAWN_POINT_2_SET("&aSpawn point 2 has been set."),
    SPAWN_REGION_SET("&aSpawn region has been set."),
    YOU_SHOT_PLAYER_FROM("&eYou shot player &d%player% &efrom &d%blocks% blocks &eaway (&c-%health% ❤&e)!"),
    GIVEN_KIT("&eYou have been given kit &d%kit%&e."),
    COULD_NOT_FIND_PLAYER("&cCould not find player."),
    CANT_DO_THIS_IN_SPAWN("&cYou cannot do this in spawn!");

    private String message;

    Messages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
