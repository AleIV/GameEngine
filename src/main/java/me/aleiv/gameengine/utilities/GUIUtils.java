package me.aleiv.gameengine.utilities;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.ChatColor;
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

    public static ItemStack getBackItem() {
        ItemStack goBack = new ItemStack(Material.FEATHER);
        ItemMeta goBackMeta = goBack.getItemMeta();
        goBackMeta.setDisplayName("Go back");
        goBackMeta.setLore(List.of("Go back to the previous menu"));
        goBack.setItemMeta(goBackMeta);

        return goBack;
    }

    public static ItemStack getPreviousPageItem() {
        ItemStack previousPage = new ItemStack(Material.ARROW);
        ItemMeta previousPageMeta = previousPage.getItemMeta();
        previousPageMeta.setDisplayName("Previous page");
        previousPageMeta.setLore(List.of("Click to go to the previous page"));
        previousPage.setItemMeta(previousPageMeta);

        return previousPage;
    }

    public static ItemStack getNextPageItem() {
        ItemStack nextPage = new ItemStack(Material.ARROW);
        ItemMeta nextPageMeta = nextPage.getItemMeta();
        nextPageMeta.setDisplayName("Next page");
        nextPageMeta.setLore(List.of("Click to go to the next page"));
        nextPage.setItemMeta(nextPageMeta);
        return nextPage;
    }

    public static ItemStack getCloseItem() {
        ItemStack closeItem = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = closeItem.getItemMeta();
        closeMeta.setDisplayName(ChatColor.RED + "Close");
        closeMeta.setLore(List.of(ChatColor.GRAY + "Click to close the config menu"));
        closeItem.setItemMeta(closeMeta);

        return closeItem;
    }

}
