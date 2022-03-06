package me.aleiv.gameengine.gui;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.aleiv.gameengine.utilities.GameUtils;
import org.bukkit.entity.Player;

public class GameManageGUI {

    private final Player player;
    private ChestGui chestGui;

    public GameManageGUI(Player player) {
        this.player = player;
        this.chestGui = new ChestGui(3, "§f ");

        // TODO: ADD TEXTURED GUI
        this.chestGui.setTitle("\uF808\uE200");
        this.build();
        this.chestGui.show(player);
    }

    private void build() {
        StaticPane startButton = new StaticPane(0, 0 , 3, 3);
        startButton.setOnClick(e -> {
            player.closeInventory();
            GameUtils.formalGameStart(player);
        });

        StaticPane stopButton = new StaticPane(6, 0 , 3, 3);
        stopButton.setOnClick(e -> {
            player.closeInventory();
            GameUtils.formalGameFinish(player);
        });

        this.chestGui.addPane(startButton);
        this.chestGui.addPane(stopButton);
    }

}
