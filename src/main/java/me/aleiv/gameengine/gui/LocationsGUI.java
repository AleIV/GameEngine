package me.aleiv.gameengine.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.aleiv.gameengine.globalUtilities.config.ConfigParameter;
import me.aleiv.gameengine.utilities.GUIUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocationsGUI {

    private final Player player;
    private final ChestGui chestGui;
    private final ConfigParameter param;

    private final ChestGui backGui;

    private PaginatedPane paginatedPane;

    private final String titleTemplate = "(%page%/%maxpage%) - Locations";

    public LocationsGUI(Player player, ConfigParameter param, ChestGui backGui) {
        this.player = player;
        this.param = param;
        this.backGui = backGui;

        this.chestGui = new ChestGui(6, titleTemplate);

        GUIUtils.buildBackground(this.chestGui);
        this.generateNavigation();
        this.generateLocations();
        this.updateTitle();

        this.chestGui.show(this.player);
    }

    private void generateNavigation() {
        GuiItem goBackItem = new GuiItem(GUIUtils.getBackItem(), e -> {
            e.setCancelled(true);
            this.backGui.show(this.player);
        });

        GuiItem nextPageItem = new GuiItem(GUIUtils.getNextPageItem(), e -> {
            e.setCancelled(true);
            if (this.paginatedPane.getPage() < this.paginatedPane.getPages() - 1) {
                this.paginatedPane.setPage(this.paginatedPane.getPage() + 1);

                this.updateTitle();
                this.chestGui.update();
            }
        });

        GuiItem previousPageItem = new GuiItem(GUIUtils.getPreviousPageItem(), e -> {
            e.setCancelled(true);
            if (this.paginatedPane.getPage() > 0) {
                this.paginatedPane.setPage(this.paginatedPane.getPage() - 1);

                this.updateTitle();
                this.chestGui.update();
            }
        });

        StaticPane navigation = new StaticPane(0, 5, 9, 1);
        navigation.addItem(goBackItem, 4, 0);
        navigation.addItem(nextPageItem, 8, 0);
        navigation.addItem(previousPageItem, 0, 0);
    }

    private void generateLocations() {
        this.paginatedPane = new PaginatedPane(0, 0, 9, 4);
        paginatedPane.populateWithGuiItems(this.param.getAsLocationList().stream().map((loc) -> {
            ItemStack item = new ItemStack(Material.COMPASS);
            ItemMeta itemMeta = item.getItemMeta();

            itemMeta.setDisplayName(ChatColor.GREEN + "Click to teleport");
            itemMeta.setLore(Stream.of("&7Location info:", "&7X: " + loc.getX(), "&7Y: " + loc.getY(), "&7Z: " + loc.getZ(), "&7World: " + loc.getWorld().getName(), "&6 ", "&aLeft click &7to teleport", "&cRight click &7to delete").map(l -> ChatColor.translateAlternateColorCodes('&', l)).collect(Collectors.toList()));

            item.setItemMeta(itemMeta);
            return new GuiItem(item, e -> {
                e.setCancelled(true);
                if (e.getClick().isRightClick()) {
                    this.param.removeLocation(loc);
                    new LocationsGUI(this.player, this.param, this.backGui);
                } else {
                    this.player.teleport(loc);
                    this.player.closeInventory();
                }
            });
        }).toList());
    }

    private void updateTitle() {
        this.chestGui.setTitle(ChatColor.translateAlternateColorCodes('&',
                this.titleTemplate
                        .replaceAll("%page%", String.valueOf(this.paginatedPane.getPage() + 1))
                        .replaceAll("%maxpage%", String.valueOf(this.paginatedPane.getPages()))
        ));
    }

}
