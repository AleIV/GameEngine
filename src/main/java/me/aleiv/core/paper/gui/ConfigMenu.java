package me.aleiv.core.paper.gui;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.globalUtilities.config.BaseConfig;
import org.bukkit.entity.Player;

public class ConfigMenu {

    private Core instance;

    private ChestGui chestGui;

    private BaseConfig config;
    private Player player;

    public ConfigMenu(Player player, BaseConfig config) {
        this.instance = Core.getInstance();

        this.config = config;
        this.player = player;



    }



}
