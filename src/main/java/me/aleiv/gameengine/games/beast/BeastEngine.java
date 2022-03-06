package me.aleiv.gameengine.games.beast;

import com.google.common.collect.Lists;
import lombok.Getter;
import me.aleiv.cinematicCore.paper.core.NPCManager;
import me.aleiv.cinematicCore.paper.objects.NPCInfo;
import me.aleiv.gameengine.Core;
import me.aleiv.gameengine.exceptions.GameStartException;
import me.aleiv.gameengine.games.beast.commands.BeastCMD;
import me.aleiv.gameengine.games.beast.config.BeastConfig;
import me.aleiv.gameengine.games.beast.config.BeastMapConfig;
import me.aleiv.gameengine.games.beast.listeners.BeastGlobalListener;
import me.aleiv.gameengine.games.beast.listeners.BeastInGameListener;
import me.aleiv.gameengine.games.beast.listeners.BeastLobbyListener;
import me.aleiv.gameengine.gamesManager.PlayerRole;
import me.aleiv.gameengine.globalUtilities.EngineEnums;
import me.aleiv.gameengine.globalUtilities.objects.BaseEngine;
import me.aleiv.gameengine.globalUtilities.objects.Participant;
import me.aleiv.gameengine.listener.FreezeListener;
import me.aleiv.gameengine.utilities.FireworkUtils;
import me.aleiv.gameengine.utilities.ResourcePackManager;
import me.aleiv.gameengine.utilities.SoundUtils;
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
        this.instance.registerListener(this.freezeListener);

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
    public void startGame() throws GameStartException {
        int beastsCount = this.getBeastConfig().getBeastsNumber();
        List<Player> players = new ArrayList<>(this.instance.getGamesManager().getPlayerManager().filter(PlayerRole.PLAYER).stream().map(Participant::getPlayer).toList());
        if (players.size() <= beastsCount) {
            throw new GameStartException(GameStartException.GameStartExceptionReason.NOT_ENOUGTH_PLAYERS);
        }
        // Get beastsCount players randomly from players list without repeating
        for (int i = 0; i < beastsCount; i++) {
            int random = (int) (Math.random() * players.size());
            Player beast = players.get(random);
            players.remove(random);
            this.beasts.add(beast);
        }
        SoundUtils.playSound(Sound.BLOCK_NOTE_BLOCK_BIT, 0.8f);

        players.forEach(p -> {
            p.teleport(this.getBeastConfig().getMap().getPlayerLoc());
            // TODO: Change message
            p.sendTitle(ChatColor.GRAY + " ", ChatColor.translateAlternateColorCodes('&', "&8[&c&l!&8] &fEscapa de la bestia &8[&c&l!&8]"), 0, 50, 30);
        });
        this.beasts.forEach(p -> {
            p.teleport(this.getBeastConfig().getMap().getBeastLoc());

            switch (Maps.getMap(this.getBeastConfig().getMap().getName())) {
                case it -> this.disguiseBeast(p, "pennywise");
                case ghost -> this.disguiseBeast(p, "ghostface");
                case jeison -> this.disguiseBeast(p, "jason");
                case puppyplaytime -> this.disguiseBeast(p, "huggywuggy");
                case slenderman -> this.disguiseBeast(p, "slenderman");
            }

            // TODO: Change message
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 2, false, false, false));
            p.sendTitle(ChatColor.GRAY + " ", ChatColor.translateAlternateColorCodes('&', "&8[&c&l!&8] &fEres una bestia &8[&c&l!&8]"), 0, 50, 30);
        });

        this.instance.broadcast((this.beasts.size() == 1 ? "&cLa bestia " : "&cLas bestias ") + "saldrán en " + this.getBeastConfig().getPlayerGracePeriod() + " segundos.");
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

            p.setFlying(true);
            this.freezeListener.freeze(p);
            p.teleport(cinematicLoc);
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*3, 2, false, false, false));
            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20*3, 2, false, false, false));
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
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 4, 10, false, false, false));
                p.teleport(locationCache.remove(p.getUniqueId()));
                p.removePotionEffect(PotionEffectType.SLOW);
                p.removePotionEffect(PotionEffectType.INVISIBILITY);
                p.setFlying(false);

                p.sendTitle(ChatColor.GRAY + " ", ChatColor.translateAlternateColorCodes('&', "&8[&c&l!&8] &fNo dejes que la bestia de cace &8[&c&l!&8]"), 0, 50, 30);
                SoundUtils.playSound(Sound.BLOCK_NOTE_BLOCK_BIT, 1.4f);
            });
            this.beasts.forEach(p -> p.removePotionEffect(PotionEffectType.SLOW));
        }, 3*20L));
        this.isBeastWaiting = false;

        // Starting sound schedulers
        switch (Maps.getMap(this.getBeastConfig().getMap().getName())) {
            case slenderman -> {
                this.gameTasks.add(Bukkit.getScheduler().runTaskTimer(this.instance, () -> {

                    if (new Random().nextInt(100) < 20) {
                        SoundUtils.playBeastSound(this.beasts, "escape.slenderman");
                    }
                }, 0L, 3 * 20L));

                // TODO: Estatica de titulo
                String LOW = "";
                String NORMAL = "";
                String HIGH = "";
                this.gameTasks.add(Bukkit.getScheduler().runTaskTimerAsynchronously(this.instance,
                        () -> this.instance.getGamesManager().getPlayerManager().filter(PlayerRole.PLAYER).stream().filter(p -> !p.isDead()).forEach(p -> {
                    int d = this.beasts.stream().map(b -> (int) b.getLocation().distance(p.getPlayer().getLocation())).min(Comparator.naturalOrder()).orElse(99);

                    String c = "";
                    if (d < 4) {
                        c = LOW;
                    } else if (d < 8) {
                        c = NORMAL;
                    } else if (d < 12) {
                        c = HIGH;
                    }

                    if (!c.isEmpty()) {
                        p.getPlayer().sendTitle(c, ChatColor.BLACK.toString() + " ", 0, 20, 10);
                    }
                }), 0L, 4L));
            }
            case ghost -> this.gameTasks.add(Bukkit.getScheduler().runTaskTimer(this.instance, () -> {
                if (new Random().nextInt(100) < 20) {
                    SoundUtils.playBeastSound(this.beasts, "escape.ghostface");
                }
            }, 40L, 2*20L));
            case jeison -> this.gameTasks.add(Bukkit.getScheduler().runTaskTimer(this.instance, () -> {
                if (new Random().nextInt(100) < 20) {
                    SoundUtils.playBeastSound(this.beasts, "escape.jason");
                }
            }, 40L, 2*20L));
            case it -> {
                String[] pennySounds = new String[]{"escape.clownlaugh1", "escape.clownlaugh2", "escape.clownlaugh3"};
                this.gameTasks.add(Bukkit.getScheduler().runTaskTimer(this.instance, () -> {
                    if (new Random().nextInt(100) < 20) {
                        SoundUtils.playBeastSound(this.beasts, pennySounds[new Random().nextInt(pennySounds.length)]);
                    }
                }, 40L, 3*20L));
            }
        }
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
                toBreak.forEach(b -> b.breakNaturally(new ItemStack(Material.STICK)));
                SoundUtils.playSound("escape.ironbar", 1f);
            }, delay[i]);
            this.gameTasks.add(task);
            if (i == barrotesList.size() - 1) {
                SoundUtils.playSound(Sound.ENTITY_HUSK_CONVERTED_TO_ZOMBIE, 1.0f);
                SoundUtils.playSound("escape.metalhit", 1f);
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

        List<Block> barrotes = this.beastConfig.getMap().getBarrotes();
        if (barrotes.size() != 0) {
            barrotes.forEach(b -> b.setType(Material.IRON_BARS));
        }

        this.alreadyFinished = false;
        this.randomizeMap();

        this.instance.getGamesManager().getPlayerManager().filter(PlayerRole.PLAYER).stream().map(Participant::getPlayer).forEach(this::resetPlayer);
    }

    private void randomizeMap() {
        this.beastConfig.set("map", MAPS[new Random().nextInt(MAPS.length)]);
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
            this.resetPlayer(player);
        }
    }

    public void playKillSound(Location loc) {
        if (this.beasts.size() == 0) return;

        switch (Maps.getMap(this.beastConfig.getMap().getName())) {
            case puppyplaytime -> SoundUtils.playDirectionalSound(loc, "escape.huggywuggy", 1.0f);
            case jeison -> SoundUtils.playDirectionalSound(loc, "escape.jasonattack", 1.0f);
            case ghost -> SoundUtils.playDirectionalSound(loc, "escape.ghostfaceattack", 1.0f);
            case slenderman -> SoundUtils.playDirectionalSound(loc, "escape.slendermanstatic", 1.0f);
            case it -> SoundUtils.playDirectionalSound(loc, "escape.clownattack", 1.0f);
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
        player.setFlying(false);
        this.freezeListener.unfreeze(player);
        instance.getGamesManager().getPlayerManager().getParticipant(player).setDead(false);
    }
}
