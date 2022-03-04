package me.aleiv.core.paper.games.beast.gui;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.aleiv.core.paper.utilities.GameUtils;
import org.bukkit.entity.Player;

public class BeastsGUI {

    private final Player player;
    private ChestGui chestGui;

    public BeastsGUI(Player player) {
        this.player = player;
        this.chestGui = new ChestGui(3, "§f ");

        this.build();
        // TODO: ADD TEXTURED GUI
        this.chestGui.show(player);
    }

    private void build() {
        StaticPane startButton = new StaticPane(0, 0 , 2, 3);
        startButton.setOnClick(e -> {
            player.closeInventory();
            GameUtils.formalGameStart(player);
        });

        StaticPane stopButton = new StaticPane(7, 0 , 2, 3);
        stopButton.setOnClick(e -> {
            player.closeInventory();
            GameUtils.formalGameFinish(player);
        });

        this.chestGui.addPane(startButton);
        this.chestGui.addPane(stopButton);
    }

}
