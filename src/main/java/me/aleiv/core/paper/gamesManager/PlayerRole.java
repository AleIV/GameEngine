package me.aleiv.core.paper.gamesManager;

import org.bukkit.ChatColor;

public enum PlayerRole {

    PLAYER("Player", ChatColor.DARK_AQUA + "[Player] " + ChatColor.RESET, "role.player"),
    SPECTATOR("Spectator", ChatColor.GRAY + "[Spectator] " + ChatColor.RESET, "role.spectator"),
    STAFF("Staff", ChatColor.BLUE + "[Spectator] " + ChatColor.RESET, "role.staff"),
    ADMIN("Admin", ChatColor.DARK_RED + "[Admin] " + ChatColor.RESET, "role.admin"),;

    private String name;
    private String prefix;
    private String permission;

    PlayerRole(String name, String prefix, String permission) {
        this.name = name;
        this.prefix = prefix;
        this.permission = permission;
    }

    public String getName() {
        return this.name;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getPermission() {
        return this.permission;
    }

    public static PlayerRole getFromName(String name) {
        for (PlayerRole role : PlayerRole.values()) {
            if (role.getName().equalsIgnoreCase(name)) {
                return role;
            }
        }
        return null;
    }

}
