package me.aleiv.gameengine.trap;

import me.aleiv.gameengine.utilities.CC;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.EntitiesLoadEvent;

public class TrapListeners implements Listener {

  @EventHandler
  public void onMove(PlayerMoveEvent event) {
    Location to = event.getTo();
    Location from = event.getFrom();

    //if(engine.getGameStage() != GameStage.INGAME) return;

    if (to.getBlockZ() == from.getBlockZ() && to.getBlockX() == from.getBlockX()) return;

    Trap trap = Trap.getByLocation(to);

    if (trap != null && !trap.isActive()) {
      trap.action(event.getPlayer());
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onInteractEntity(PlayerInteractAtEntityEvent event) {
    Player player = event.getPlayer();

    if(event.getRightClicked() instanceof ArmorStand){
      event.setCancelled(true);
    }

    if(player.getGameMode() != org.bukkit.GameMode.CREATIVE){
      event.setCancelled(true);
      return;
    }

    if(player.isSneaking()){
      if(event.getRightClicked() instanceof ArmorStand armorStand){
        armorStand.remove();

        Location location = armorStand.getLocation();

        if(Trap.getByLocation(location) != null){
          Trap trap = Trap.getByLocation(location);
          Trap.getTraps().remove(trap);
          player.sendMessage(ChatColor.GREEN + "Trap removed.");
          return;
        }
        return;
      }
    }

    if(event.getRightClicked() instanceof ArmorStand armorStand){
      Location location = armorStand.getLocation();

      if(Trap.getByLocation(location) != null){
        player.sendMessage(ChatColor.RED + "Trap already exists.");
        return;
      }

      event.setCancelled(true);

      if(armorStand.getEquipment().getHelmet() == null) return;

      TrapAnimation animation = TrapAnimation.getByCustomModelData(armorStand.getEquipment().getHelmet().getItemMeta().getCustomModelData());

      if(animation == null) return;

      armorStand.remove();

      Trap trap = new Trap(location, animation);

      trap.spawn();

      Trap.getTraps().add(trap);
      player.sendMessage(CC.translate("&aTrap &f" + animation.name() + "&a created!"));
    }
  }

  @EventHandler
  public void onEntityLoad(EntitiesLoadEvent event){

    if(Trap.getTraps().isEmpty()) return;

    for (Entity entity : event.getEntities()) {
      if(entity instanceof ArmorStand armorStand){

        Trap trap = Trap.getByLocation(armorStand.getLocation());
        if(trap != null){

          if(trap.getArmorStand().isValid() && armorStand == trap.getArmorStand()){
            continue;
          }

          if(trap.getArmorStand() == null || !trap.getArmorStand().isValid()) {
            trap.setArmorStand(armorStand);
          }else{
            armorStand.remove();
          }
        }else if(armorStand.getEquipment().getHelmet() != null) {
          armorStand.remove();
        }
      }
    }
  }
}
