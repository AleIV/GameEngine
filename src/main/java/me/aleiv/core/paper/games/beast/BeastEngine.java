package me.aleiv.core.paper.games.beast;

import com.google.common.collect.Lists;
import lombok.Getter;
import me.aleiv.cinematicCore.paper.core.NPCManager;
import me.aleiv.cinematicCore.paper.objects.NPCInfo;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.games.beast.commands.BeastCMD;
import me.aleiv.core.paper.games.beast.config.BeastConfig;
import me.aleiv.core.paper.games.beast.config.BeastMapConfig;
import me.aleiv.core.paper.games.beast.listeners.BeastGlobalListener;
import me.aleiv.core.paper.games.beast.listeners.BeastInGameListener;
import me.aleiv.core.paper.games.beast.listeners.BeastLobbyListener;
import me.aleiv.core.paper.gamesManager.PlayerRole;
import me.aleiv.core.paper.globalUtilities.EngineEnums;
import me.aleiv.core.paper.globalUtilities.objects.BaseEngine;
import me.aleiv.core.paper.globalUtilities.objects.Participant;
import me.aleiv.core.paper.listener.FreezeListener;
import me.aleiv.core.paper.utilities.FireworkUtils;
import me.aleiv.core.paper.utilities.ResourcePackManager;
import me.aleiv.core.paper.utilities.SoundUtils;
import me.aleiv.modeltool.core.EntityModel;
import me.aleiv.modeltool.core.EntityModelManager;
import me.aleiv.modeltool.exceptions.AlreadyUsedNameException;
import me.aleiv.modeltool.exceptions.InvalidModelIdException;
import me.aleiv.modeltool.models.EntityMood;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class BeastEngine extends BaseEngine {

    Core instance;

    private NPCManager npcManager;
    private final EntityModelManager entityModelManager;

    BeastCMD beastCMD;
    BeastGlobalListener beastGlobalListener;
    BeastInGameListener beastInGameListener;
    BeastLobbyListener beastLobbyListener;
    private FreezeListener freezeListener;

    private @Getter final BeastConfig beastConfig;
    private @Getter final List<Player> beasts;
    private final List<BukkitTask> gameTasks;

    public static final String[] MAPS = new String[]{"ghost", "it", "jeison", "puppyplaytime", "slenderman"};
    enum Maps {
        ghost,
        it,
        jeison,
        puppyplaytime,
        slenderman,;

        public static Maps getMap(String name) {
            for (Maps map : values()) {
                if (map.name().equalsIgnoreCase(name)) {
                    return map;
                }
            }
            return ghost;
        }
    }

    private @Getter boolean isBeastWaiting;
    private boolean alreadyFinished;

    public BeastEngine(Core instance) {
        super(new BeastConfig(MAPS));
        this.instance = instance;

        this.npcManager = new NPCManager(instance);
        this.entityModelManager = new EntityModelManager(instance);

        this.beastConfig = (BeastConfig) this.getGameConfig();
        this.beasts = new ArrayList<>();
        this.gameTasks = new ArrayList<>();

        this.beastCMD = new BeastCMD(instance);
        this.beastGlobalListener = new BeastGlobalListener(instance);
        this.beastInGameListener = new BeastInGameListener(instance, this);
        this.beastLobbyListener = new BeastLobbyListener(instance);
        this.freezeListener = new FreezeListener();

        this.isBeastWaiting = false;
        this.alreadyFinished = false;
    }

    @Override
    public void enable(){
        this.instance.getGamesManager().getWorldManager().load(MAPS);
        this.instance.getGamesManager().getWorldManager().load("beastlobby");

        instance.getCommandManager().registerCommand(beastCMD);
        instance.registerListener(beastGlobalListener);
        instance.registerListener(beastLobbyListener);
        instance.registerListener(beastInGameListener);

        ResourcePackManager rpm = this.instance.getGamesManager().getResourcePackManager();
        // TODO: Set rp
        rpm.setResoucePackURL("https://download.mc-packs.net/pack/bf6ddb0714a7161f847c16cf9b730a812cd4213a.zip");
        rpm.setResourcePackHash("bf6ddb0714a7161f847c16cf9b730a812cd4213a");
        rpm.setEnabled(true);
    }

    @Override
    public void disable(){
        this.instance.getGamesManager().getWorldManager().unloadWorld(false, MAPS);
        this.instance.getGamesManager().getWorldManager().unloadWorld(false, "beastlobby");

        instance.getCommandManager().unregisterCommand(beastCMD);
        instance.unregisterListener(beastGlobalListener);
        instance.unregisterListener(beastInGameListener);
        instance.unregisterListener(beastLobbyListener);
        instance.unregisterListener(beastInGameListener);
    }

    private void disguiseBeast(Player player, String modelName) {
        String randomString = UUID.randomUUID().toString().substring(0, 8);
        try {
            EntityModel em = this.entityModelManager.spawnEntityModel(randomString, 20, modelName, player.getLocation(), EntityType.ARMOR_STAND, EntityMood.STATIC);
            this.entityModelManager.disguisePlayer(player, em);
            em.setInvisible(true);
            em.setSeeSelf(true);
        } catch (InvalidModelIdException|AlreadyUsedNameException e) {
            e.printStackTrace();
        }
    }

    private void undisguiseBeast(Player player) {
        EntityModel em = this.entityModelManager.getEntityModel(player.getUniqueId());
        if (em == null) return;

        em.setInvisible(false);
        this.entityModelManager.undisguisePlayer(player);
        em.remove();
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
            // TODO: Change message
            p.sendTitle(ChatColor.GRAY + " ", ChatColor.translateAlternateColorCodes('&', "&8[&c&l!&8] &fEscapa de la bestia &8[&c&l!&8]"), 0, 50, 30);
            SoundUtils.playSound(Sound.BLOCK_NOTE_BLOCK_BIT, 0.8f);
        });
        this.beasts.forEach(p -> {
            p.teleport(this.getBeastConfig().getMap().getBeastLoc());

            switch (Maps.getMap(this.getBeastConfig().getMap().getName())) {
                case it -> this.disguiseBeast(p, "Pennywise");
                case ghost -> this.disguiseBeast(p, "GhostFace");
                case jeison -> this.disguiseBeast(p, "Jason");
                case puppyplaytime -> this.disguiseBeast(p, "HuggyWuggy");
                case slenderman -> this.disguiseBeast(p, "Slenderman");
            }

            // TODO: Change message
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 2, false, false, false));
            String message = "&7&l======= &cERES LA BESTIA &7&l=======\n\n" +
                    "&fEn " + this.getBeastConfig().getPlayerGracePeriod() +
                    " segundos, podrás salir. Tu objetivo es eliminar a todos los jugadores.\n";
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        });

        this.gameTasks.add(Bukkit.getScheduler().runTaskLater(this.instance, this::beastsExit, this.getBeastConfig().getPlayerGracePeriod() * 20L));
        this.isBeastWaiting = true;

        instance.registerListener(beastInGameListener);
        instance.unregisterListener(beastLobbyListener);
    }

    private void beastsExit() {
        if (this.getGameStage() != EngineEnums.GameStage.INGAME || !this.isBeastWaiting) return;

        List<Player> normalPlayers = this.instance.getGamesManager().getPlayerManager().filter(PlayerRole.PLAYER).stream().map(Participant::getPlayer).filter(p -> !this.beasts.contains(p)).toList();

        HashMap<UUID, Location> locationCache = new HashMap<>();
        HashMap<UUID, NPCInfo> NPCCache = new HashMap<>();

        Location cinematicLoc = this.getBeastConfig().getMap().getCinematicLoc();

        // Starting cinematic
        normalPlayers.forEach(p -> {
            locationCache.put(p.getUniqueId(), p.getLocation().clone());

            NPCInfo npcInfo = new NPCInfo(p, false, true, false);
            npcManager.spawnNPC(npcInfo);

            NPCCache.put(p.getUniqueId(), npcInfo);
            p.setGameMode(GameMode.SPECTATOR);

            this.freezeListener.freeze(p);
            p.teleport(cinematicLoc);
        });

        this.beasts.forEach(p -> {
            p.sendTitle(ChatColor.GRAY + " ", ChatColor.translateAlternateColorCodes('&', "&8[&c&l!&8] &fMata a todos los jugadores &8[&c&l!&8]"), 0, 50, 30);
            SoundUtils.playSound(Sound.BLOCK_NOTE_BLOCK_BIT, 1.4f);
        });

        this.gameTasks.add(Bukkit.getScheduler().runTaskLater(this.instance, this::playPrisonBreak, 15L));

        this.gameTasks.add(Bukkit.getScheduler().runTaskLater(this.instance, () -> {
            normalPlayers.forEach((p) -> {
                // Stopping cinematic
                this.freezeListener.unfreeze(p);
                npcManager.removeNPC(NPCCache.remove(p.getUniqueId()));
                p.setGameMode(GameMode.ADVENTURE);
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 2, 1, false, false, false));
                p.teleport(locationCache.remove(p.getUniqueId()));

                p.sendTitle(ChatColor.GRAY + " ", ChatColor.translateAlternateColorCodes('&', "&8[&c&l!&8] &fNo dejes que la bestia de cace &8[&c&l!&8]"), 0, 50, 30);
                SoundUtils.playSound(Sound.BLOCK_NOTE_BLOCK_BIT, 1.4f);
            });
            this.beasts.forEach(p -> p.removePotionEffect(PotionEffectType.SLOW));
        }, 3*20L));
        this.isBeastWaiting = false;
    }

    private void playPrisonBreak() {
        BeastMapConfig bmc = this.beastConfig.getMap();
        List<Block> barrotes = bmc.getBarrotes();

        if (bmc.getBarrotes().size() == 0) {
            barrotes = new ArrayList<>();
            Block initialBarrote = bmc.getBarrotesLoc().getBlock();
            getBlocksForCube(initialBarrote, 6).stream().filter(b -> b.getType() == Material.IRON_BARS).forEach(barrotes::add);
            bmc.getBarrotes().clear();
            bmc.getBarrotes().addAll(barrotes);
        }

        List<List<Block>> barrotesList = Lists.partition(barrotes, (int) Math.ceil(barrotes.size() / 6d));

        int[] delay = new int[]{0, 12, 20, 28, 34, 38};
        for (int i = 0; i < barrotesList.size(); i++) {
            List<Block> toBreak = barrotesList.get(i);
            BukkitTask task = Bukkit.getScheduler().runTaskLater(this.instance, () -> {
                toBreak.forEach(Block::breakNaturally);
                // TODO: Break sound?
            }, delay[i]);
            this.gameTasks.add(task);
            if (i == barrotesList.size() - 1) {
                // TODO: Final sound
            }
        }
    }

    private List<Block> getBlocksForCube(Block start, int radius) {
        if (radius < 0) {
            return new ArrayList<>();
        }
        int iterations = (radius * 2) + 1;
        List<Block> blocks = new ArrayList<>(iterations * iterations * iterations);
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    blocks.add(start.getRelative(x, y, z));
                }
            }
        }
        return blocks;
    }

    @Override
    public void stopGame() {
        instance.registerListener(beastLobbyListener);
        instance.unregisterListener(beastInGameListener);

        if (!this.alreadyFinished) {
            this.checkPlayerCount(true);
        }
    }

    @Override
    public void restartGame() {
        this.instance.getGamesManager().getWorldManager().resetWorld(this.getBeastConfig().getActiveMap());
        instance.registerListener(beastLobbyListener);
        instance.unregisterListener(beastInGameListener);
        this.beasts.clear();

        this.gameTasks.forEach(BukkitTask::cancel);
        this.gameTasks.clear();

        this.randomizeMap();

        this.alreadyFinished = false;

        this.instance.getGamesManager().getPlayerManager().filter(PlayerRole.PLAYER).stream().map(Participant::getPlayer).forEach(this::resetPlayer);
    }

    private void randomizeMap() {
        this.beastConfig.set("map", MAPS[new Random().nextInt(MAPS.length)-1]);
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

        this.checkPlayerCount(false);
    }

    private void checkPlayerCount(boolean forced) {
        this.alreadyFinished = true;
        List<Participant> beastsP = this.beasts.parallelStream().map(p -> this.instance.getGamesManager().getPlayerManager().getParticipant(p)).toList();
        if (forced) {
            beastsP.forEach(p -> p.setDead(true));
        }

        boolean beastsDead = beastsP.stream().allMatch(Participant::isDead);
        List<Participant> normalPlayers = this.instance.getGamesManager().getPlayerManager().filter(PlayerRole.PLAYER).parallelStream().filter(part -> !this.beasts.contains(part.getPlayer())).toList();
        boolean playersDead = normalPlayers.parallelStream().allMatch(Participant::isDead);

        if (!beastsDead && !playersDead) return;

        List<Player> winners = new ArrayList<>();
        this.instance.getGamesManager().stopGame(false);

        if (beastsDead) {
            // TODO: PLAYERS WIN SCREEN | BEASTS LOOSE SCREEN
            winners.addAll(normalPlayers.stream().map(Participant::getPlayer).toList());
        } else {
            // TODO: PLAYERS LOOSE SCREEN | BEASTS WIN SCREEN
            winners.addAll(this.beasts);
        }

        for (int i = 0; i < 15; i += 3) {
            Bukkit.getScheduler().runTaskLater(this.instance, () -> winners.forEach(p -> FireworkUtils.spawnWinnerFirework(p.getLocation())), i*20L);
        }

        this.instance.sendTitle(null, "TODO PLACEHOLDER", 20, 8*20, 3*20);
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
        this.undisguiseBeast(player);
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
