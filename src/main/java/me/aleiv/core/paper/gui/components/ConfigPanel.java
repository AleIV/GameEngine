package me.aleiv.core.paper.gui.components;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.aleiv.core.paper.globalUtilities.config.ConfigParameter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ConfigPanel extends StaticPane {

    private Player player;

    private ConfigParameter param;
    private StaticPane nextArrow;
    private StaticPane previousArrow;
    private StaticPane mainItem;

    public ConfigPanel(Player player, ConfigParameter param, int x, int y) {
        super(x, y, 3, 1, Priority.NORMAL);

        this.player = player;
    }

    private GuiItem addNumberItem() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("Add amount");
        meta.setLore(List.of("Left click to add +1", "Right click to add +10"));

        item.setItemMeta(meta);

        GuiItem guiItem = new GuiItem(item, (e) -> {
            e.setCancelled(true);
            this.param.addToNumber(e.getClick().isRightClick() ? 10 : 1);
        });

        return guiItem;
    }

    private GuiItem removeNumberItem() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("Remove amount");
        meta.setLore(List.of("Left click to add -1", "Right click to add -10"));

        item.setItemMeta(meta);

        GuiItem guiItem = new GuiItem(item, (e) -> {
            e.setCancelled(true);
            this.param.addToNumber(e.getClick().isRightClick() ? -10 : -1);
        });

        return guiItem;
    }

    private GuiItem setTrueItem() {
        ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("Set true");
        meta.setLore(List.of("Click to set the boolean true"));

        item.setItemMeta(meta);

        GuiItem guiItem = new GuiItem(item, (e) -> {
            e.setCancelled(true);
            this.param.set(true);
        });

        return guiItem;
    }

    private GuiItem setFalseItem() {
        ItemStack item = new ItemStack(Material.RED_STAINED_GLASS);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("Set false");
        meta.setLore(List.of("Click to set the boolean false"));

        item.setItemMeta(meta);

        GuiItem guiItem = new GuiItem(item, (e) -> {
            e.setCancelled(true);
            this.param.set(false);
        });

        return guiItem;
    }

    private GuiItem setLocation() {
        ItemStack item = new ItemStack(Material.BLUE_STAINED_GLASS);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("Set location");
        meta.setLore(List.of("Click to set the location"));

        item.setItemMeta(meta);

        GuiItem guiItem = new GuiItem(item, (e) -> {
            e.setCancelled(true);
            this.param.set(this.player.getLocation().clone());
        });

        return guiItem;
    }

    private GuiItem addLocation() {
        ItemStack item = new ItemStack(Material.BLUE_STAINED_GLASS);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("Add location");
        meta.setLore(List.of("Click to add your location"));

        item.setItemMeta(meta);

        GuiItem guiItem = new GuiItem(item, (e) -> {
            e.setCancelled(true);
            this.param.set();
        });

        return guiItem;
    }

}
