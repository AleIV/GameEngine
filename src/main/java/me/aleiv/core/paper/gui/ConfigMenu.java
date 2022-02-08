package me.aleiv.core.paper.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.MasonryPane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.google.common.collect.Lists;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.globalUtilities.config.BaseConfig;
import me.aleiv.core.paper.globalUtilities.config.ConfigParameter;
import me.aleiv.core.paper.gui.components.ParameterComponent;
import me.aleiv.core.paper.utilities.GUIUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ConfigMenu {

    private Core instance;

    private ChestGui chestGui;
    private PaginatedPane paginatedPane;

    private BaseConfig config;
    private Player player;
    private List<BaseConfig> subConfigs;

    private final String titleTemplate = "(%page%/%maxpage%) - Config Menu - " + config.getName();

    public ConfigMenu(Player player, BaseConfig config) {
        this(player, config, new ArrayList<>(0));
    }

    public ConfigMenu(Player player, BaseConfig config, BaseConfig subconfig) {
        this(player, config, List.of(subconfig));
    }

    public ConfigMenu(Player player, BaseConfig config, final List<BaseConfig> subConfigs) {
        this.instance = Core.getInstance();

        this.config = config;
        this.player = player;
        this.subConfigs = subConfigs;

        this.chestGui = new ChestGui(6, " ");
        this.chestGui.setOnClose(e -> this.config.save());
        this.chestGui.setOnTopClick(e -> e.setCancelled(true));
        this.chestGui.setOnTopDrag(e -> e.setCancelled(true));
        GUIUtils.buildBackground(this.chestGui);
        this.buildOptions();
        this.generateNavigation();
        this.buildSubConfigs();
        this.updateTitle();

        this.chestGui.show(player);
    }

    private void buildOptions() {
        this.paginatedPane = new PaginatedPane(0, 0, 9, 4);

        int page = 0;

        for (List<ConfigParameter> parameters : Lists.partition(this.config.getConfigParameters(), 8)) {
            MasonryPane masonryPane = new MasonryPane(0, 0, 9, 4);
            AtomicInteger atomicSlot = new AtomicInteger(0);
            parameters.forEach((param) -> {
                int slot = atomicSlot.getAndIncrement();
                int x = slot > 3 ? 5 : 1;
                int y = slot % 4;
                ParameterComponent panel = new ParameterComponent(this.player, param, x, y, this.chestGui);
                masonryPane.addPane(panel);
            });

            paginatedPane.addPane(page, masonryPane);
            page++;
        }
    }

    private void generateNavigation() {
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
        navigation.addItem(nextPageItem, 8, 0);
        navigation.addItem(previousPageItem, 0, 0);
    }

    private void buildSubConfigs() {
        if (this.subConfigs == null || this.subConfigs.size() == 0) return;

        ChestGui subconfigs = new ChestGui(6, "Subconfigs of " + this.config.getName());
        GUIUtils.buildBackground(subconfigs);
        StaticPane nav = new StaticPane(0, 5, 9, 1);
        nav.addItem(new GuiItem(GUIUtils.getBackItem(), e -> {
            e.setCancelled(true);
            this.chestGui.show(this.player);
        }), 4, 0);
        subconfigs.addPane(nav);

        PaginatedPane pp = new PaginatedPane(0, 0, 9, 4);
        pp.populateWithGuiItems(this.subConfigs.stream().map(sb -> {
            ItemStack sc = new ItemStack(Material.PAPER);
            ItemMeta sm = sc.getItemMeta();
            sm.setDisplayName(ChatColor.BLUE + sb.getName() + " - " + sb.getSubconfigPath());
            sm.setLore(List.of(ChatColor.GRAY + "Click to open"));
            sc.setItemMeta(sm);

            return new GuiItem(sc, e -> {
                e.setCancelled(true);
                new ConfigMenu(this.player, sb);
            });
        }).toList());
        subconfigs.addPane(pp);

        StaticPane subbutton = new StaticPane(6, 5, 1, 1);
        ItemStack subbuttonItem = new ItemStack(Material.PAPER);
        ItemMeta subbuttonItemMeta = subbuttonItem.getItemMeta();
        subbuttonItemMeta.setDisplayName(ChatColor.BLUE + "Open Subconfigs");
        subbuttonItemMeta.setLore(List.of(ChatColor.GRAY + "Click to see all the subconfig"));
        subbuttonItem.setItemMeta(subbuttonItemMeta);

        subbutton.addItem(new GuiItem(subbuttonItem, e -> {
            e.setCancelled(true);
            subconfigs.show(this.player);
        }), 0, 0);
    }

    private void updateTitle() {
        this.chestGui.setTitle(ChatColor.translateAlternateColorCodes('&',
                this.titleTemplate
                        .replaceAll("%page%", String.valueOf(this.paginatedPane.getPage() + 1))
                        .replaceAll("%maxpage%", String.valueOf(this.paginatedPane.getPages()))
        ));
    }

}
