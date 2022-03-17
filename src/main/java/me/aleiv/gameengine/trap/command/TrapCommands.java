package me.aleiv.gameengine.trap.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import me.aleiv.gameengine.Core;
import me.aleiv.gameengine.trap.Trap;
import me.aleiv.gameengine.trap.TrapAnimation;
import me.aleiv.gameengine.utilities.CC;
import me.aleiv.gameengine.utilities.items.InteractItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.metadata.FixedMetadataValue;

@CommandAlias("trap")
@CommandPermission("trap.command")
public class TrapCommands extends BaseCommand {

  public TrapCommands() {
    for (TrapAnimation trapAnimation : TrapAnimation.values()) {
      InteractItem.register(
          trapAnimation.getItemStack(),
          event -> {
            if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

            Location location = event.getClickedBlock().getLocation().clone().add(0, 1, 0);

            Trap trap = new Trap(location, trapAnimation);

            trap.spawn();

            Player player = event.getPlayer();

            Trap.getTraps().add(trap);

            player.sendMessage(CC.translate("&aTrap &f" + trapAnimation.name() + "&a created!"));
          });
    }
  }

  @Subcommand("gettraps")
  public void create(Player player) {
    for (TrapAnimation trapAnimation : TrapAnimation.values()) {
      player.getInventory().addItem(trapAnimation.getItemStack());
    }

    player.sendMessage(CC.translate("&aTraps added to your inventory!"));
  }

  @Subcommand("selectmap")
  public void selectMap(Player player, String mapName) {
    player.setMetadata("map", new FixedMetadataValue(Core.getInstance(), mapName));

    player.sendMessage(CC.translate("&aMap &f" + mapName + "&a selected!"));
  }

  @Subcommand("removeAll")
  public void removeAll(Player player) {
    Bukkit.getWorlds()
        .forEach(
            world ->
                world
                    .getEntities()
                    .forEach(
                        entity -> {
                          if((entity instanceof ArmorStand armorStand)) {

                            Trap trap = Trap.getByLocation(armorStand.getLocation());

                            if (trap != null) {
                              if (trap.getArmorStand() != armorStand) {
                                armorStand.remove();
                              }
                            }else if(armorStand.getEquipment().getHelmet() != null) {
                              armorStand.remove();
                            }
                          }}));
  }
}
