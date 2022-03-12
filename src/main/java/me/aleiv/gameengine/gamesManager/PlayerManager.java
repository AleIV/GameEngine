package me.aleiv.gameengine.gamesManager;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import me.aleiv.gameengine.Core;
import me.aleiv.gameengine.globalUtilities.objects.Participant;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class PlayerManager {

  private final Core instance;

  public static final String joinMessage =
      ChatColor.translateAlternateColorCodes(
          '&', "&7[&a+&7] &a%player% &7se ha unido a la partida.");
  public static final String leaveMessage =
      ChatColor.translateAlternateColorCodes(
          '&', "&7[&c-&7] &a%player% &7se ha salido de la partida.");

  @Getter private final HashMap<UUID, Participant> participants;

  public PlayerManager(Core instance) {
    this.instance = instance;
    this.participants = new HashMap<>();
  }

  public Participant joinPlayer(Player player) {
    Participant participant = new Participant(player, this.getPlayerDefaultRole(player));

    boolean successful =
        processRoleChange(participant.getPlayer(), null, participant.getPlayerRole());
    if (!successful) return null;

    this.participants.put(player.getUniqueId(), participant);
    instance.getGamesManager().updatePlayerCount();

    return participant;
  }

  public Participant joinPlayer(Player player, @Nullable PlayerRole role) {
    Participant participant =
        new Participant(player, role == null ? this.getPlayerDefaultRole(player) : role);

    boolean successful =
        processRoleChange(participant.getPlayer(), null, participant.getPlayerRole());
    if (!successful) return null;

    this.participants.put(player.getUniqueId(), participant);
    instance.getGamesManager().updatePlayerCount();

    return participant;
  }

  public Participant leavePlayer(Player player) {
    Participant participant = this.participants.remove(player.getUniqueId());
    try {
      if (participant.getPlayerRole() == PlayerRole.PLAYER) {
        instance.getGamesManager().getCurrentGame().leavePlayer(player);
      }
    } catch (NullPointerException ignore) {
    }
    instance.getGamesManager().updatePlayerCount();

    return participant;
  }

  public Participant getParticipant(UUID playerUUID) {
    return this.participants.get(playerUUID);
  }

  public Participant getParticipant(Player player) {
    return this.getParticipant(player.getUniqueId());
  }

  public PlayerRole getPlayerDefaultRole(Player player) {
    PlayerRole role = PlayerRole.PLAYER;
    for (PlayerRole r : PlayerRole.values()) {
      if (player.hasPermission(r.getPermission())) {
        role = r;
      }
    }

    return role;
  }

  public void setPlayerRole(UUID playerUUID, PlayerRole role) {
    Participant participant = this.getParticipant(playerUUID);
    if (participant == null) return;
    Player player = participant.getPlayer();
    if (player == null) return;

    this.leavePlayer(player);
    this.joinPlayer(player, role);
  }

  private boolean processRoleChange(
      Player player, @Nullable PlayerRole oldRole, PlayerRole newRole) {
    if (newRole == PlayerRole.PLAYER) {
      try {
        return instance.getGamesManager().getCurrentGame().joinPlayer(player);
      } catch (Exception ignore) {
      }
    } else if (oldRole == PlayerRole.PLAYER) {
      try {
        instance.getGamesManager().getCurrentGame().leavePlayer(player);
      } catch (Exception ignore) {
      }
    }
    return true;
  }

  public PlayerRole getPlayerRole(Player player) {
    Participant participant = this.getParticipant(player.getUniqueId());
    if (participant == null) return null;

    return participant.getPlayerRole();
  }

  public boolean isRole(Player player, PlayerRole role) {
    Participant participant = this.getParticipant(player.getUniqueId());
    if (participant != null) {
      return participant.getPlayerRole() == role;
    }
    return false;
  }

  public boolean isPlayer(Player player) {
    return this.isRole(player, PlayerRole.PLAYER);
  }

  public boolean isStaff(Player player) {
    return this.isRole(player, PlayerRole.STAFF);
  }

  public Participant getFirstAliveParticipant() {
    return this.participants.values().stream()
        .filter(Participant::isAlive)
        .findFirst()
        .orElse(null);
  }

  public boolean isSpectator(Player player) {
    return this.isRole(player, PlayerRole.SPECTATOR);
  }

  public boolean isAdmin(Player player) {
    return this.isRole(player, PlayerRole.ADMIN);
  }

  public List<Participant> filter(PlayerRole role) {
    return this.participants.values().parallelStream()
        .filter(p -> p.getPlayerRole() == role)
        .toList();
  }
}
