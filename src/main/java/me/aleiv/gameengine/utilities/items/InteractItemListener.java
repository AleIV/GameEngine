package me.aleiv.gameengine.utilities.items;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InteractItemListener implements Listener {

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    ItemStack itemStack = event.getItem();

    if (itemStack == null) return;

    if (itemStack.getItemMeta() == null) return;

    if (itemStack.getItemMeta().getDisplayName().isEmpty()) return;

    InteractItem interactItem = InteractItem.get(itemStack);

    if (interactItem == null) return;

    interactItem.action().accept(event);
  }
}
