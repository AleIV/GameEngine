package me.aleiv.core.paper.gui.components;

import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

public class StaticPanelPane extends StaticPane {

    public StaticPanelPane(int x, int y, int length, int height, @NotNull Priority priority) {
        super(x, y, length, height, priority);
    }

    public StaticPanelPane(int x, int y, int length, int height) {
        super(x, y, length, height);
    }

    public StaticPanelPane(int length, int height) {
        super(length, height);
    }

    public void addPane(@NotNull StaticPane pane) {
        int paneX = pane.getX();
        int paneY = pane.getY();
        AtomicInteger x = new AtomicInteger(0);
        AtomicInteger y = new AtomicInteger(0);

        // TODO: Do it better, not finished.
        pane.getItems().forEach(item -> {
            int finalX = x.getAndIncrement();
            int finalY = y.get();

            this.addItem(item, paneX + finalX, paneY + finalY);
        });
    }

}
