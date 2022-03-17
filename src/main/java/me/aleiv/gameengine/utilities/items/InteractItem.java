package me.aleiv.gameengine.utilities.items;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.function.Consumer;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public record InteractItem(ItemStack item,
                           Consumer<PlayerInteractEvent> action) {

  public ItemStack getItem() {
    return item;
  }

  public Consumer<PlayerInteractEvent> getAction() {
    return action;
  }

  private static final Map<ItemStack, InteractItem> items = Maps.newHashMap();

  public static InteractItem get(ItemStack item) {
    return items.get(item);
  }

  public static void register(ItemStack item, Consumer<PlayerInteractEvent> action) {
    items.put(item, new InteractItem(item, action));
  }

  public static void unregister(ItemStack item) {
    items.remove(item);
  }


}
