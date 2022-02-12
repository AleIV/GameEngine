package me.aleiv.core.paper.games.beast;

import lombok.Getter;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.games.beast.commands.BeastCMD;
import me.aleiv.core.paper.games.beast.config.BeastConfig;
import me.aleiv.core.paper.games.beast.listeners.BeastGlobalListener;
import me.aleiv.core.paper.games.beast.listeners.BeastInGameListener;
import me.aleiv.core.paper.games.beast.listeners.BeastLobbyListener;
import me.aleiv.core.paper.gamesManager.PlayerRole;
import me.aleiv.core.paper.globalUtilities.EngineEnums;
import me.aleiv.core.paper.globalUtilities.objects.BaseEngine;
import me.aleiv.core.paper.globalUtilities.objects.Participant;
import me.aleiv.core.paper.utilities.FireworkUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BeastEngine extends BaseEngine {

    Core instance;

    BeastCMD beastCMD;
    BeastGlobalListener beastGlobalListener;
    BeastInGameListener beastInGameListener;
    BeastLobbyListener beastLobbyListener;

    private @Getter final BeastConfig beastConfig;
    private @Getter final List<Player> beasts;

    public static final String[] MAPS = new String[]{"ghost", "it", "jeison", "puppyplaytime", "slenderman"};

    private @Getter boolean isBeastWaiting;

    public BeastEngine(Core instance) {
        super(new BeastConfig(MAPS));
        this.instance = instance;

        this.beastConfig = (BeastConfig) this.getGameConfig();
        this.beasts = new ArrayList<>();

        this.beastCMD = new BeastCMD(instance);
        this.beastGlobalListener = new BeastGlobalListener(instance);
        this.beastInGameListener = new BeastInGameListener(instance, this);
        this.beastLobbyListener = new BeastLobbyListener(instance);

        this.isBeastWaiting = false;
    }

    @Override
    public void enable(){
        this.instance.getGamesManager().getWorldManager().load(MAPS);
        this.instance.getGamesManager().getWorldManager().load("beastlobby");

        instance.getCommandManager().registerCommand(beastCMD);
        instance.registerListener(beastGlobalListener);
        instance.registerListener(beastLobbyListener);
    }

    @Override
    public void disable(){
        this.instance.getGamesManager().getWorldManager().unloadWorld(false, MAPS);
        this.instance.getGamesManager().getWorldManager().unloadWorld(false, "beastlobby");

        instance.getCommandManager().unregisterCommand(beastCMD);
        instance.unregisterListener(beastGlobalListener);
        instance.unregisterListener(beastInGameListener);
        instance.unregisterListener(beastLobbyListener);

    }

    @Override
    public void startGame() {
        int beastsCount = this.getBeastConfig().getBeastsNumber();
        List<Player> players = new ArrayList<>(this.instance.getGamesManager().getPlayerManager().filter(PlayerRole.PLAYER).stream().map(Participant::getPlayer).toList());
        // Get beastsCount players randomly from players list without repeating
        for (int i = 0; i < beastsCount; i++) {
            int random = (int) (Math.random() * players.size());
            Player beast = players.get(random);
            players.remove(random);
            this.beasts.add(beast);
        }
        players.forEach(p -> {
            p.teleport(this.getBeastConfig().getMap().getPlayerLoc());
            String message = "&7&l======= &aNO ERES LA BESTIA &7&l=======\n\n" +
                    "&fEn " + this.getBeastConfig().getPlayerGracePeriod() +
                    (this.beasts.size() != 1 ? " las bestias saldrán. " : " la bestia saldrá. ") + "Tu objetivo es llegar al final del circuito para equiparte y así poder eliminar a " + (this.beasts.size() != 1 ? "las bestias." : "la bestia.") + "\n";
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        });
        this.beasts.forEach(p -> {
            p.teleport(this.getBeastConfig().getBeastLoc());
            String message = "&7&l======= &cERES LA BESTIA &7&l=======\n\n" +
                    "&fEn " + this.getBeastConfig().getPlayerGracePeriod() +
                    " segundos, podrás salir. Tu objetivo es eliminar a todos los jugadores.\n";
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        });

        Bukkit.getScheduler().runTaskLater(this.instance, () -> {
            if (this.getGameStage() == EngineEnums.GameStage.INGAME && this.isBeastWaiting) {
                this.instance.getGamesManager().getPlayerManager().filter(PlayerRole.PLAYER).stream().map(Participant::getPlayer).filter(p -> !this.beasts.contains(p)).forEach(p -> p.sendTitle(ChatColor.GRAY + " ", ChatColor.translateAlternateColorCodes('&', this.beasts.size() != 1 ? "&8[&c&l!&8] &fLas bestias han salido &8[&c&l!&8]" : "&8[&c&l!&8] &fLa bestia ha salido &8[&c&l!&8]"), 0, 50, 30));
                this.beasts.forEach(p -> {
                    p.teleport(this.getBeastConfig().getMap().getPlayerLoc());
                    p.sendTitle(ChatColor.GRAY + " ", ChatColor.translateAlternateColorCodes('&', "&8[&c&l!&8] &fMata a todos los jugadores &8[&c&l!&8]"), 0, 50, 30);
                });
                this.isBeastWaiting = false;
            }
        }, this.getBeastConfig().getPlayerGracePeriod() * 20L);
        this.isBeastWaiting = true;

        instance.registerListener(beastInGameListener);
        instance.unregisterListener(beastLobbyListener);
    }

    @Override
    public void stopGame() {
        instance.registerListener(beastLobbyListener);
        instance.unregisterListener(beastInGameListener);
    }

    @Override
    public void restartGame() {
        this.instance.getGamesManager().getWorldManager().resetWorld(this.getBeastConfig().getActiveMap());
        instance.registerListener(beastLobbyListener);
        instance.unregisterListener(beastInGameListener);
        this.beasts.clear();

        this.instance.getGamesManager().getPlayerManager().filter(PlayerRole.PLAYER).stream().map(Participant::getPlayer).forEach(this::resetPlayer);
    }

    @Override
    public boolean joinPlayer(Player player) {
        if (this.getGameStage() == EngineEnums.GameStage.INGAME || this.getGameStage() == EngineEnums.GameStage.POSTGAME) {
            player.kickPlayer("Game is already running!");
            return false;
        }

        this.resetPlayer(player);
        return true;
    }

    @Override
    public void leavePlayer(Player player) {
        if (this.getGameStage() == EngineEnums.GameStage.INGAME) {
            this.instance.broadcast(ChatColor.RED + "El jugador " + player.getName() + " ha sido eliminado.");
            if (this.beasts.contains(player)) {
                this.beasts.remove(player);
            }
            this.checkPlayerCount();
        }
    }

    public void checkPlayerCount() {
        if (this.getGameStage() != EngineEnums.GameStage.INGAME) return;

        boolean beastsDead = this.beasts.parallelStream().map(p -> this.instance.getGamesManager().getPlayerManager().getParticipant(p)).allMatch(Participant::isDead);
        List<Participant> normalPlayers = this.instance.getGamesManager().getPlayerManager().filter(PlayerRole.PLAYER).parallelStream().filter(part -> !this.beasts.contains(part.getPlayer())).toList();
        boolean playersDead = normalPlayers.parallelStream().allMatch(Participant::isDead);

        if (!beastsDead && !playersDead) return;

        String message;
        List<Player> winners = new ArrayList<>();
        this.instance.getGamesManager().stopGame(false);

        if (beastsDead) {
            message = ChatColor.GREEN + "Los jugadores han ganado!";
            winners.addAll(normalPlayers.stream().map(Participant::getPlayer).toList());
        } else {
            message = ChatColor.RED + "Las bestias han ganado!";
            winners.addAll(this.beasts);
        }

        for (int i = 0; i < 15; i += 3) {
            Bukkit.getScheduler().runTaskLater(this.instance, () -> winners.forEach(p -> FireworkUtils.spawnWinnerFirework(p.getLocation())), i*20L);
        }

        this.instance.broadcast(message);
        this.instance.sendTitle(null, message, 20, 8*20, 3*20);
    }

    public void giveBeastItems(Player player) {
        if (this.beasts.contains(player)) return;

        ItemStack helmet = this.enchant(new ItemStack(Material.DIAMOND_HELMET));
        ItemStack chestplate = this.enchant(new ItemStack(Material.DIAMOND_CHESTPLATE));
        ItemStack leggings = this.enchant(new ItemStack(Material.DIAMOND_LEGGINGS));
        ItemStack boots = this.enchant(new ItemStack(Material.DIAMOND_BOOTS));
        ItemStack sword = this.enchant(new ItemStack(Material.DIAMOND_SWORD));

        Inventory pinv = player.getInventory();
        EntityEquipment eq = player.getEquipment();
        pinv.clear();
        pinv.setItem(0, sword);
        eq.setHelmet(helmet);
        eq.setChestplate(chestplate);
        eq.setLeggings(leggings);
        eq.setBoots(boots);
        player.updateInventory();
    }

    private ItemStack enchant(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.addEnchant(Enchantment.DURABILITY, 3, true);
        if (item.getType().toString().contains("SWORD")) {
            itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 4, true);
        } else if (item.getType().toString().contains("HELMET") ||
                item.getType().toString().contains("CHESTPLATE") ||
                item.getType().toString().contains("LEGGINGS") ||
                item.getType().toString().contains("BOOTS")) {
            itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
            if (item.getType().toString().contains("BOOTS")) {
                itemMeta.addEnchant(Enchantment.PROTECTION_FALL, 2, true);
            }
        }

        item.setItemMeta(itemMeta);
        return item;
    }

    private void resetPlayer(Player player) {
        player.setNoDamageTicks(20);
        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(this.getBeastConfig().getLobbyLoc());
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.getInventory().clear();
        player.getActivePotionEffects().forEach(pe -> player.removePotionEffect(pe.getType()));
        instance.getGamesManager().getPlayerManager().getParticipant(player).setDead(false);
    }
}
