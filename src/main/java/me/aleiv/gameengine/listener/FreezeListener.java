package me.aleiv.gameengine.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class FreezeListener implements Listener {

  private final List<UUID> frozenPlayers;

  public FreezeListener() {
    this.frozenPlayers = new ArrayList<>();
  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {
    Location from = event.getFrom();

    if (isFrozen(event.getPlayer().getUniqueId())) {
      event.getPlayer().teleport(from);
      event.setCancelled(true);
    }
  }

  public boolean isFrozen(UUID uuid) {
    return frozenPlayers.contains(uuid);
  }

  public void freeze(UUID uuid) {
    frozenPlayers.add(uuid);
  }

  public void unfreeze(UUID uuid) {
    frozenPlayers.remove(uuid);
  }

  public boolean isFrozen(Player player) {
    return isFrozen(player.getUniqueId());
  }

  public void freeze(Player player) {
    freeze(player.getUniqueId());
  }

  public void unfreeze(Player player) {
    unfreeze(player.getUniqueId());
  }
}
