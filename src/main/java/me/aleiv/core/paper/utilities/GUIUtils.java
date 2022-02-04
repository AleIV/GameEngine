package me.aleiv.core.paper.utilities;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class GUIUtils {

    public static ChestGui buildBackground(ChestGui gui) {
        StaticPane background = new StaticPane(0, 4, 9, 1, Pane.Priority.LOWEST);

        ItemStack placeholder = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = placeholder.getItemMeta();
        meta.setDisplayName("");
        meta.setLore(List.of(""));
        List.of(ItemFlag.values()).forEach(meta::addItemFlags);
        placeholder.setItemMeta(meta);

        background.fillWith(placeholder, (e) -> e.setCancelled(true));

        gui.addPane(background);
        return gui;
    }

}
