package me.aleiv.gameengine.games.beast.listeners;

import me.aleiv.gameengine.Core;
import me.aleiv.gameengine.games.beast.BeastEngine;
import me.aleiv.gameengine.globalUtilities.EngineEnums;
import me.aleiv.gameengine.globalUtilities.events.participantEvents.ParticipantDeathEvent;
import me.aleiv.gameengine.globalUtilities.events.timerEvents.GlobalTimerSecondEvent;
import me.aleiv.gameengine.globalUtilities.events.timerEvents.GlobalTimerStopEvent;
import me.aleiv.gameengine.globalUtilities.objects.Participant;
import me.aleiv.gameengine.utilities.Animation;
import me.aleiv.gameengine.utilities.Frames;
import me.aleiv.gameengine.utilities.SoundUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BeastGlobalListener implements Listener {

  Core instance;
  private final BeastEngine engine;
  private final Animation animation;

  public BeastGlobalListener(Core instance, BeastEngine engine) {
    this.instance = instance;
    this.engine = engine;
    this.animation = new Animation(Frames.getFramesCharsIntegers(0, 184), false);
  }

  @EventHandler
  public void onRegen(EntityRegainHealthEvent e) {
    e.setCancelled(true);
  }

  @EventHandler
  public void onFoodChange(FoodLevelChangeEvent e) {
    e.setCancelled(true);
    e.setFoodLevel(20);
  }

  @EventHandler
  public void onEntityDamageByBlock(EntityDamageByBlockEvent e) {
    e.setCancelled(true);
  }

  @EventHandler
  public void onEntityDamage(EntityDamageEvent e) {
    if (!(e instanceof EntityDamageByEntityEvent)) {
      e.setCancelled(true);
    }
  }

  @EventHandler
  public void onPlaceBlock(BlockPlaceEvent e) {
    Player p = e.getPlayer();

    if (this.check(p)) {
      e.setCancelled(true);
    }
  }

  @EventHandler
  public void onBreakBlock(BlockBreakEvent e) {
    Player p = e.getPlayer();

    if (this.check(p)) {
      e.setCancelled(true);
    }
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent e) {
    Player p = e.getPlayer();

    if (this.check(p)) {
      e.setCancelled(true);
    }
  }

  @EventHandler
  public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent e) {
    Player p = e.getPlayer();

    if (this.check(p)) {
      e.setCancelled(true);
    }
  }

  @EventHandler
  public void onPlayerEntityInteract(PlayerInteractEntityEvent e) {
    Player p = e.getPlayer();

    if (this.check(p)) {
      e.setCancelled(true);
    }
  }

  private boolean check(Player player) {
    return !instance.getGamesManager().getPlayerManager().isAdmin(player);
  }

  @EventHandler
  public void onTimerTick(GlobalTimerSecondEvent e) {
    if (engine.getGameStage() == EngineEnums.GameStage.PREGAME && e.getSeconds() <= 10) {
      if (e.getSeconds() == 10) {
        animation.play();
      }
      if (e.getSeconds() >= 1) {
        SoundUtils.playSound("escape.beep", 1.3f);
      } else if (e.getSeconds() >= 3) {
        SoundUtils.playSound("escape.beep", 1f);
      } else if (e.getSeconds() >= 5) {
        SoundUtils.playSound("escape.beep", 0.8f);
      } else {
        SoundUtils.playSound("escape.beep", 0.75f);
      }
    }
  }

  @EventHandler
  public void onDamageFireWorks(EntityDamageByEntityEvent event) {
    if (event.getDamager() instanceof Firework) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onTimerStop(GlobalTimerStopEvent e) {
    animation.stop();
  }

  @EventHandler
  public void onPartDeath(ParticipantDeathEvent e) {
    Player player = e.getPlayer();
    Participant part = e.getParticipant();
    if (part.isDead()
        || (this.engine.getGameStage() != EngineEnums.GameStage.INGAME
            && this.engine.getGameStage() != EngineEnums.GameStage.POSTGAME)) return;

    part.setDead(true);

    this.engine.checkPlayerCount();
    this.engine.playKillSound(player.getLocation());
    player.getLocation().getWorld().strikeLightningEffect(player.getLocation());
    if (this.instance.getGamesManager().getGameSettings().getKickOnDeath()) {
      player.kickPlayer(
          ChatColor.translateAlternateColorCodes(
              '&', "&cHas sido expulsado de la partida porque has muerto."));
      return;
    }

    this.instance.broadcast(
        ChatColor.RED + "El jugador " + player.getName() + " ha sido eliminado.");
    player.setGameMode(GameMode.SPECTATOR);
  }
}
