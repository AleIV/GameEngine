package me.aleiv.gameengine.utilities.items;

import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@UtilityClass
public class ItemUtils {

  public ItemStack setCustomData(ItemStack item, int data) {
    ItemMeta meta = item.getItemMeta();
    meta.setCustomModelData(data);
    item.setItemMeta(meta);
    return item;
  }
}
