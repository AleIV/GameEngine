package me.aleiv.gameengine.gui.components;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.aleiv.gameengine.globalUtilities.config.ConfigParameter;
import me.aleiv.gameengine.gui.LocationsGUI;
import me.aleiv.gameengine.utilities.ChatUtils;
import me.aleiv.gameengine.utilities.ObjectUtils;
import me.aleiv.gameengine.utilities.ParseUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ParameterComponent extends StaticPane {

    private Player player;

    private ConfigParameter param;
    private GuiItem nextArrow;
    private GuiItem previousArrow;
    private GuiItem mainItem;

    private ChestGui gui;

    public ParameterComponent(Player player, ConfigParameter param, int x, int y, ChestGui gui) {
        super(x, y, 3, 1, Priority.NORMAL);

        this.player = player;
        this.gui = gui;

        this.param = param;

        this.setMainItem();
        this.setArrows();
    }

    private void setMainItem() {
        this.mainItem = this.getMainItem();
        this.addItem(this.mainItem, 1, 0);
    }

    private void setArrows() {
        if (param.getType() == ConfigParameter.ConfigParameterType.BOOLEAN) {
            this.nextArrow = this.setTrueItem();
            this.previousArrow = this.setFalseItem();
        } else if (param.getType() == ConfigParameter.ConfigParameterType.DOUBLE || param.getType() == ConfigParameter.ConfigParameterType.INTEGER) {
            this.nextArrow = this.addNumberItem();
            this.previousArrow = this.removeNumberItem();
        } else if (param.getType() == ConfigParameter.ConfigParameterType.LOCATION) {
            this.nextArrow = this.setLocation();
        } else if (param.getType() == ConfigParameter.ConfigParameterType.LOCATIONLIST) {
            this.nextArrow = this.addLocation();
        }

        if (this.nextArrow != null) {
            this.addItem(this.nextArrow, 2, 0);
        }
        if (this.previousArrow != null) {
            this.addItem(this.previousArrow, 0, 0);
        }
    }
    
    private GuiItem getMainItem() {
        switch (param.getType()) {
            case BOOLEAN -> {
                boolean value = param.getAsBoolean();
                return this.getParamStatus(value ? ChatColor.GREEN + "true" : ChatColor.RED + "false", false);
            }
            case DOUBLE, INTEGER -> {
                String value = "";
                if (param.getType() == ConfigParameter.ConfigParameterType.DOUBLE) {
                    value = String.valueOf(param.getAsDouble());
                } else {
                    value = String.valueOf(param.getAsInt());
                }

                return this.getParamStatus(value, true);
            }
            case STRING -> {
                String value = param.getAsString();
                return this.getParamStatus(value, true);
            }
            case LOCATION -> {
                String value = ParseUtils.getLocationLore(param.getAsLocation());
                return this.getParamStatus(value, false);
            }
            case LOCATIONLIST -> {
                return this.openLocation();
            }
            default -> {
                return this.getParamStatus("null", false);
            }
        }
    }

    public void updateMainItem() {
        this.getItems().clear();
        this.setMainItem();
        this.setArrows();
        this.gui.update();
    }

    private GuiItem addNumberItem() {
        ItemStack item = this.generateItemStack(Material.ARROW, "Add amount", "Left click to remove +1", "Right click to remove +10");

        GuiItem guiItem = new GuiItem(item, (e) -> {
            e.setCancelled(true);
            this.param.addToNumber(e.getClick().isRightClick() ? 10 : 1);
            this.update();
        });

        return guiItem;
    }

    private GuiItem removeNumberItem() {
        ItemStack item = this.generateItemStack(Material.ARROW, "Remove amount", "Left click to remove -1", "Right click to remove -10");

        GuiItem guiItem = new GuiItem(item, (e) -> {
            e.setCancelled(true);
            this.param.addToNumber(e.getClick().isRightClick() ? -10 : -1);
            this.update();
        });

        return guiItem;
    }
    
    private GuiItem getParamStatus(String value, boolean canChatChange) {
        List<String> lore = new ArrayList<>();
        lore.add("The value of this parameter");
        if (value.contains("\n")) {
            List<String> loreLines = new ArrayList<>(List.of(value.split("\n")));
            lore.add("is: " + loreLines.remove(0));
            lore.addAll(loreLines);
        } else {
            lore.add("is: " + value);
        }
        if (canChatChange) {
            lore.add(ChatColor.GRAY + " ");
            lore.add("Left click to change");
        }

        ItemStack item = this.generateItemStack(Material.OAK_SIGN, "Value of " + this.param.getKey(), lore.toArray(new String[lore.size()-1]));

        GuiItem guiItem = new GuiItem(item, (e) -> {
            e.setCancelled(true);
            if (!canChatChange) return;

            Consumer<String> callback = (input) -> {
                switch (param.getType()) {
                    case BOOLEAN -> {
                        if (ObjectUtils.canBeBoolean(input)) {
                            this.param.set(Boolean.parseBoolean(input));
                        } else {
                            player.sendMessage(ChatColor.RED + "The value must be true or false!");
                        }
                    }
                    case DOUBLE -> {
                        if (ObjectUtils.canBeDouble(input)) {
                            this.param.set(Double.parseDouble(input));
                        } else {
                            player.sendMessage(ChatColor.RED + "The value must be a double!");
                        }
                    }
                    case INTEGER -> {
                        if (ObjectUtils.canBeInt(input)) {
                            this.param.set(Integer.parseInt(input));
                        } else {
                            player.sendMessage(ChatColor.RED + "The value must be an integer!");
                        }
                    }
                    case STRING -> this.param.set(input);
                }

                this.update();
                this.gui.show(player);
            };

            this.player.closeInventory();
            ChatUtils.askInput(player, "&eType in chat the value you want to change to...", callback);
        });


        return guiItem;
    }

    private GuiItem setTrueItem() {
        ItemStack item = this.generateItemStack(Material.GREEN_STAINED_GLASS, "Set true", "Click to set the boolean true");

        GuiItem guiItem = new GuiItem(item, (e) -> {
            e.setCancelled(true);
            this.param.set(true);
            this.update();
        });

        return guiItem;
    }

    private GuiItem setFalseItem() {
        ItemStack item = this.generateItemStack(Material.RED_STAINED_GLASS, "Set false", "Click to set the boolean false");

        GuiItem guiItem = new GuiItem(item, (e) -> {
            e.setCancelled(true);
            this.param.set(false);
            this.update();
        });

        return guiItem;
    }

    private GuiItem setLocation() {
        ItemStack item = this.generateItemStack(Material.BLUE_STAINED_GLASS, "Set location", "Click to set the location");

        GuiItem guiItem = new GuiItem(item, (e) -> {
            e.setCancelled(true);
            this.param.set(this.player.getLocation().clone());
            this.update();
        });

        return guiItem;
    }

    private GuiItem addLocation() {
        ItemStack item = this.generateItemStack(Material.BLUE_STAINED_GLASS, "Add location", "Click to add the location");

        GuiItem guiItem = new GuiItem(item, (e) -> {
            e.setCancelled(true);
            this.param.addLocation(this.player.getLocation().clone());
            this.update();
        });

        return guiItem;
    }

    private GuiItem openLocation() {
        ItemStack item = this.generateItemStack(Material.NETHER_PORTAL, "Open location", "Click to open the location");

        GuiItem guiItem = new GuiItem(item, (e) -> {
            e.setCancelled(true);
            new LocationsGUI(this.player, this.param, this.gui);
        });

        return guiItem;
    }

    private ItemStack generateItemStack(Material mat, String name, String... lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        meta.setLore(List.of(lore).stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList()));

        item.setItemMeta(meta);

        return item;
    }

    public void update() {
        this.updateMainItem();
        this.gui.update();
    }

}
