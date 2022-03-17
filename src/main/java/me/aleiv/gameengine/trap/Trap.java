package me.aleiv.gameengine.trap;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.val;
import me.aleiv.gameengine.Core;
import me.aleiv.gameengine.utilities.CC;
import me.aleiv.gameengine.utilities.items.ItemUtils;
import org.bukkit.Bukkit;
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

  @Getter private static List<Trap> traps = Lists.newArrayList();

  private final Location location;
  private final TrapAnimation animation;
  @Setter private transient ArmorStand armorStand;
  private boolean active;

  public static void loadTraps() {
    val gson = Core.getGSON();

    val file = new File(Core.getInstance().getDataFolder(), "traps.json");

    if (!file.exists()) {
      return;
    }

    try {
      val fr = new FileReader(file);
      traps = gson.fromJson(fr, new TypeToken<List<Trap>>() {}.getType());
      fr.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    for (val trap : traps) {
      trap.setActive(false);
    }

    for (val trap : traps) {
      if (trap.getArmorStand() == null || !trap.getArmorStand().isValid()) {
        trap.spawn();
      }
    }
  }

  public static void saveTraps() {

    if(traps.isEmpty()) return;

    val gson = Core.getGSON();

    if (!Core.getInstance().getDataFolder().exists()) {
      Core.getInstance().getDataFolder().mkdir();
    }

    val file = new File(Core.getInstance().getDataFolder(), "traps.json");

    try {
      Writer writer = new FileWriter(file);
      writer.write(gson.toJson(traps));
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    for (val trap : traps) {
      trap.remove();
    }
  }

  public void action(Player player) {

    if (animation.getType() == TrapType.SLOWNESS) {
      player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 5, 3));
    } else {
      player.damage(6);
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

    player.sendMessage(CC.translate("&aHas caido en una trampa!"));

    Bukkit.getScheduler()
        .runTaskLater(
            Core.getInstance(),
            () -> {
              if (active) {
                active = false;
                armorStand
                    .getEquipment()
                    .setHelmet(
                        ItemUtils.setCustomData(
                            new ItemStack(Material.BRICK), animation.getInitCustomModelData()));
              }
            },
            20L * 5);
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

  public static Trap getByLocation(Location location) {
    return traps.stream().filter(trap -> trap.contains(location, 0.5)).findFirst().orElse(null);
  }

  public boolean contains(Location location, double radius) {
    return location.getX() >= this.location.getX() - radius
        && location.getX() <= this.location.getX() + radius
        && location.getY() >= this.location.getY() - radius
        && location.getY() <= this.location.getY() + radius
        && location.getZ() >= this.location.getZ() - radius
        && location.getZ() <= this.location.getZ() + radius;
  }
}
