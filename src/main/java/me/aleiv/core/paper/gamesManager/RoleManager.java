package me.aleiv.core.paper.gamesManager;

import me.aleiv.core.paper.Core;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class RoleManager implements Listener {

    private final Core plugin;

    private final HashMap<UUID, PlayerRole> roles = new HashMap<>();

    public RoleManager(Core plugin) {
        this.plugin = plugin;

        plugin.registerListener(this);
    }

    private void playerJoin(Player player) {
        this.roles.put(player.getUniqueId(), this.getPlayerDefaultRole(player));
    }

    private void playerQuit(Player player) {
        this.roles.remove(player.getUniqueId());
    }

    public PlayerRole getPlayerDefaultRole(Player player) {
        PlayerRole role = PlayerRole.PLAYER;
        for (PlayerRole r : PlayerRole.values()) {
            if (player.hasPermission(r.getPermission())) {
                role = r;
            }
        }

        return role;
    }

    public void setPlayerRole(Player player, PlayerRole role) {
        this.roles.put(player.getUniqueId(), role);
    }

    public PlayerRole getPlayerRole(Player player) {
        return this.roles.get(player.getUniqueId());
    }

    public boolean isPlayer(Player player) {
        return this.roles.get(player.getUniqueId()) == PlayerRole.PLAYER;
    }

    public boolean isStaff(Player player) {
        return this.roles.get(player.getUniqueId()) != PlayerRole.STAFF;
    }

    public boolean isSpectator(Player player) {
        return this.roles.get(player.getUniqueId()) == PlayerRole.SPECTATOR;
    }

    public boolean isAdmin(Player player) {
        return this.roles.get(player.getUniqueId()) == PlayerRole.ADMIN;
    }

    public int getPlayersCount() {
        return this.roles.entrySet().stream().filter(entry -> entry.getValue() == PlayerRole.PLAYER).mapToInt(entry -> 1).sum();
    }

    public List<Player> filter(PlayerRole role) {
        return Bukkit.getOnlinePlayers().stream().filter(player -> this.roles.get(player.getUniqueId()) == role).collect(java.util.stream.Collectors.toList());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        this.playerJoin(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e) {
        this.playerQuit(e.getPlayer());
    }

}
