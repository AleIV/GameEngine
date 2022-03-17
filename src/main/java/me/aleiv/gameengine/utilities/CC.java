package me.aleiv.gameengine.utilities;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

@UtilityClass
public class CC {

  public String translate(String text) {
    return ChatColor.translateAlternateColorCodes('&', text);
  }
}
