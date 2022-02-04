package me.aleiv.core.paper.gui;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.globalUtilities.config.BaseConfig;
import me.aleiv.core.paper.gui.components.ParameterComponent;
import me.aleiv.core.paper.utilities.GUIUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ConfigMenu {

    private Core instance;

    private ChestGui chestGui;

    private BaseConfig config;
    private Player player;

    public ConfigMenu(Player player, BaseConfig config) {
        this.instance = Core.getInstance();

        this.config = config;
        this.player = player;

        this.chestGui = new ChestGui(6, "Config Menu - " + config.getName());
        this.chestGui.setOnClose(e -> this.config.save());
        this.chestGui.setOnTopClick(e -> e.setCancelled(true));
        this.chestGui.setOnTopDrag(e -> e.setCancelled(true));
        GUIUtils.buildBackground(this.chestGui);
        this.buildOptions();

        this.chestGui.show(player);
    }

    private void buildOptions() {
        AtomicInteger atomicSlot = new AtomicInteger(0);
        this.config.getConfigParameters().forEach((param) -> {
            int slot = atomicSlot.getAndIncrement();
            int x = slot > 3 ? 5 : 1;
            int y = slot % 4;
            ParameterComponent panel = new ParameterComponent(this.player, param, x, y, this.chestGui);
            this.chestGui.addPane(panel);
        });
    }

}
