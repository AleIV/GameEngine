package me.aleiv.gameengine.games.beast.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.gui.type.HopperGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.aleiv.gameengine.games.beast.BeastEngine;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class BeastMapsGUI {

    private final Player player;
    private final BeastEngine engine;

    private final ChestGui chestGui;

    private enum BeastMaps {
        SLENDERMAN("Slenderman", "slenderman", 3),
        JASON("Jason", "jeison", 4),
        IT("IT", "it", 5),
        POPPYPLAYTIME("Poppy Playtime", "puppyplaytime", 6),
        GHOSTFACE("Ghostface", "ghost", 7);

        private String name;
        private String mapname;
        private int id;

        BeastMaps(String name, String mapname, int id) {
            this.name = name;
            this.mapname = mapname;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public String getMapName() {
            return mapname;
        }

        public int getId() {
            return id;
        }
    }

    public BeastMapsGUI(Player player, BeastEngine engine) {
        this.player = player;
        this.engine = engine;

        this.chestGui = new ChestGui(3, ChatColor.WHITE + "\uF808\uE204");

        this.build();

        this.chestGui.show(player);
    }

    private void build() {
        StaticPane pane = new StaticPane(2, 2, 5, 1);

        for (int i = 0; i < BeastMaps.values().length; i++) {
            pane.addItem(this.getBeastItem(BeastMaps.values()[i]), i, 0);
        }

        this.chestGui.addPane(pane);
    }

    private GuiItem getBeastItem(BeastMaps beast) {
        ItemStack item = new ItemStack(Material.BRICK);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(beast.getId());
        meta.setDisplayName(ChatColor.RED.toString() + ChatColor.BOLD + beast.getName());
        meta.setLore(List.of(ChatColor.DARK_GRAY + "Haz click para seleccionar este mapa."));
        item.setItemMeta(meta);

        GuiItem guiItem = new GuiItem(item, (e) -> {
            this.engine.getBeastConfig().set("map", beast.getMapName());
            e.getWhoClicked().closeInventory();
            e.getWhoClicked().sendMessage(ChatColor.GREEN + "Mapa seleccionado: " + ChatColor.GRAY + beast.getName());
            ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        });

        return guiItem;
    }

}
