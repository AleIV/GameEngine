package me.aleiv.gameengine.games.beast.trap;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.val;
import me.aleiv.gameengine.Core;
import me.aleiv.gameengine.utilities.CC;
import me.aleiv.gameengine.utilities.items.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@Getter
@Setter
@RequiredArgsConstructor
public class Trap {

  private final Location location;
  private final TrapAnimation animation;
  @Setter private transient ArmorStand armorStand;
  private boolean active;

  public void action(Player player) {
    if (this.active) return;

    int time = animation.getType() == TrapType.DAMAGE ? 1 : 4;
    String title;

    if (animation.getType() == TrapType.DAMAGE) {
      int damage = (int) (Math.random() * 4) + 2;

      if (damage <= 3) {
        title = "\u3415";
      } else if (damage == 4) {
        title = "\u3416";
      } else {
        title = "\u3417";
      }

      player.damage(damage);
    } else {
      title = animation.getTitleChar();
      player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * time, 200, false, false, false));
      player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * time, 200, false, false, false));
      player.setSprinting(false);
    }

    if (title != null) {
      player.sendTitle(title, ChatColor.BLACK + " ", 2, 20 * time, 2);
    }

    if (!animation.getSound().isEmpty()) {
      player.playSound(player.getLocation(), animation.getSound(), 1, 1);
    }

    armorStand
        .getEquipment()
        .setHelmet(
            ItemUtils.setCustomData(
                new ItemStack(Material.BRICK), animation.getAnimateCustomModelData()));

    active = true;

    //player.sendMessage(CC.translate("&aHas caido en una trampa!"));

    /*Bukkit.getScheduler()
        .runTaskLater(
            Core.getInstance(),
            () -> {
              player.setWalkSpeed(0.2F);
              player.setFoodLevel(20);
              player.setFlySpeed(0.1F);
            },
            20L * 5);*/
    Bukkit.getScheduler()
        .runTaskLater(
            Core.getInstance(),
            () -> {
              if (active) {
                armorStand
                    .getEquipment()
                    .setHelmet(
                        ItemUtils.setCustomData(
                            new ItemStack(Material.BRICK), animation.getInitCustomModelData()));
              }
            },
            20L * time);
    Bukkit.getScheduler().runTaskLater(Core.getInstance(), () -> active = false, 20L * (time+2));
  }

  public void spawn() {
    this.armorStand =
        (ArmorStand) this.location.getWorld().spawnEntity(this.location, EntityType.ARMOR_STAND);

    this.armorStand.setVisible(false);
    this.armorStand.setGravity(false);

    this.armorStand
        .getEquipment()
        .setHelmet(
            ItemUtils.setCustomData(
                new ItemStack(Material.BRICK), animation.getInitCustomModelData()));
  }

  public void remove() {
    this.armorStand.remove();
  }

  public void reset() {
    this.armorStand
        .getEquipment()
        .setHelmet(
            ItemUtils.setCustomData(
                new ItemStack(Material.BRICK), animation.getInitCustomModelData()));
  }

}
