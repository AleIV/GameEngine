package me.aleiv.gameengine.games.beast.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import me.aleiv.gameengine.Core;
import me.aleiv.gameengine.games.beast.BeastEngine;
import me.aleiv.gameengine.gamesManager.PlayerRole;
import me.aleiv.gameengine.globalUtilities.events.participantEvents.ParticipantDeathEvent;
import me.aleiv.gameengine.globalUtilities.objects.Participant;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class BeastInGameListener implements Listener {

  Core instance;
  BeastEngine beastEngine;

  private final List<UUID> equipedPlayers;

  public BeastInGameListener(Core instance, BeastEngine beastEngine) {
    this.instance = instance;
    this.beastEngine = beastEngine;

    this.equipedPlayers = new ArrayList<>();
  }

  @EventHandler
  public void onEntityDamage(EntityDamageEvent e) {
    if (!(e.getEntity() instanceof Player player)) return;

    if (e.getCause() == EntityDamageEvent.DamageCause.VOID) {
      Participant p = instance.getGamesManager().getPlayerManager().getParticipant(player);

      if(p == null) return;

      if (p.getPlayerRole() == PlayerRole.PLAYER && !p.isDead()) {
        Bukkit.getPluginManager().callEvent(new ParticipantDeathEvent(p, p.getPlayer().getKiller()));
        Location location = player.getLocation();

        player.setVelocity(new Vector());

        if (location.getY() <= 0) {
          location.setY(20);
        }

        player.teleport(location.add(0, 3, 0));
      }
    }
  }

  @EventHandler
  public void onDamage(EntityDamageByEntityEvent e) {
    if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) {
      e.setCancelled(true);
      return;
    }

    Player player = (Player) e.getEntity();
    Player damager = (Player) e.getDamager();

    boolean damaged = false;

    e.setDamage(0);

    if (this.beastEngine.isBeastWaiting()
        || (beastEngine.getBeasts().contains(player) && beastEngine.getBeasts().contains(damager))
        || (!beastEngine.getBeasts().contains(player)
            && !beastEngine.getBeasts().contains(damager))) {
      e.setCancelled(true);
    } else if (damager.getInventory().getItemInMainHand().getType().toString().contains("SWORD")
        || damager.getInventory().getItemInOffHand().getType().toString().contains("SWORD")) {
      double newHealth = player.getHealth() - 8;
      player.setHealth(newHealth >= 0 ? newHealth : 0);
      damaged = true;
    } else if (beastEngine.getBeasts().contains(damager)) {
      double newHealth = player.getHealth() - (hasArmor(player) ? 4 : 10);
      player.setHealth(newHealth >= 0 ? newHealth : 0);
      //e.setDamage(8);
      damaged = true;
    } else {
      e.setCancelled(true);
    }

    if (damaged) {
      player.setNoDamageTicks(15);
    }
  }

  @EventHandler
  public void onTeleport(PlayerTeleportEvent event){
    Player player = event.getPlayer();

    if(event.getCause() == TeleportCause.SPECTATE){
      event.setCancelled(true);
    }

  }

  @EventHandler
  public void onEntityVelocity(PlayerMoveEvent event){
    Player player = event.getPlayer();

    Location to = event.getTo();
    Location from = event.getFrom();

    if (to.getBlockZ() == from.getBlockZ()
        && to.getBlockX() == from.getBlockX()
        && to.getBlockY() == from.getBlockY()) return;

    if(player.hasPotionEffect(PotionEffectType.JUMP)){
      event.setCancelled(true);
      player.teleport(from);
      event.setTo(from);
    }
  }

  @EventHandler
  public void onItemDamage(PlayerItemDamageEvent event){
    event.setCancelled(true);
  }

  private boolean hasArmor(Player player) {
    return player.getInventory().getHelmet() != null
        && player.getInventory().getChestplate() != null
        && player.getInventory().getLeggings() != null
        && player.getInventory().getBoots() != null;
  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {

    Location to = event.getTo();
    Location from = event.getFrom();

    if (to.getBlockX() == from.getBlockX() && to.getBlockZ() == from.getBlockZ()) {
      return;
    }

    if (event.getPlayer().getGameMode() == GameMode.SPECTATOR) {
      return;
    }

    if (to.getBlockY() < 0 || to.getBlock().getType() == Material.LAVA) {
      event.getPlayer().setHealth(0);
      return;
    }

    boolean inside = this.beastEngine.getBeastConfig().getMap().getEquipmentRegion().contains(to);

    boolean alreadyInside = this.equipedPlayers.contains(event.getPlayer().getUniqueId());

    if (!alreadyInside && inside) {
      this.beastEngine.giveBeastItems(event.getPlayer());
      this.equipedPlayers.add(event.getPlayer().getUniqueId());
    } else if (alreadyInside && !inside) {
      this.equipedPlayers.remove(event.getPlayer().getUniqueId());
    }
  }

  @EventHandler
  public void onBarrotesDrop(ItemSpawnEvent e) {
    e.setCancelled(true);
  }

  @EventHandler
  public void onPlayerDrop(PlayerDropItemEvent e) {
    if (instance.getGamesManager().getPlayerManager().isPlayer(e.getPlayer())) {
      e.setCancelled(true);
    }
  }

  @EventHandler
  public void onInventoryClick(InventoryClickEvent e) {
    if (instance.getGamesManager().getPlayerManager().isPlayer((Player) e.getWhoClicked())) {
      e.setCancelled(true);
    }
  }

  @EventHandler
  public void onInventoryDrag(InventoryDragEvent e) {
    if (instance.getGamesManager().getPlayerManager().isPlayer((Player) e.getWhoClicked())) {
      e.setCancelled(true);
    }
  }
}
