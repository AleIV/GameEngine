package me.aleiv.gameengine.games.beast.trap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.aleiv.gameengine.utilities.CC;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import us.jcedeno.libs.rapidinv.ItemBuilder;

@AllArgsConstructor
@Getter
public enum TrapAnimation {
  SLENDERMAN_TENTACLES(
      32,
      33,
      TrapType.DAMAGE,
      new ItemBuilder(Material.ARMOR_STAND).name(CC.translate("&aSlenderman Note")).build(),
      "escape.slenderman_tentacles",
          "\u3419"
  ),
  SLENDERMAN_NOTE(
      16,
      17,
      TrapType.SLOWNESS,
      new ItemBuilder(Material.ARMOR_STAND).name(CC.translate("&aSlenderman Tentacles")).build(),
      "",
          null
          ),

  JASON_ROPE(
      18,
      19,
      TrapType.SLOWNESS,
      new ItemBuilder(Material.ARMOR_STAND).name(CC.translate("&aJason Rope")).build(),
      "escape.jason_rope",
          "\u3418"
  ),
  JASON_BEAR_TRAP(
      35,
      34,
      TrapType.DAMAGE,
      new ItemBuilder(Material.ARMOR_STAND).name(CC.translate("&aJason Bear Trap")).build(),
      "escape.jason_beartrap",
          null
          ),

  HUGGYWUGGY_NAILS(
      21,
      20,
      TrapType.DAMAGE,
      new ItemBuilder(Material.ARMOR_STAND).name(CC.translate("&aHuggy Wuggy Nails")).build(),
      "escape.huggy_nails",
          null
  ),
  HUGGYWUGGY_RATS_TRAP(
      22,
      23,
      TrapType.SLOWNESS,
      new ItemBuilder(Material.ARMOR_STAND).name(CC.translate("&aHuggy Rats Trap")).build(),
      "escape.huggy_mousetrap",
          null
  ),

  PENNYWISE_NAILS(
      24,
      25,
      TrapType.DAMAGE,
      new ItemBuilder(Material.ARMOR_STAND).name(CC.translate("&aPennyWise Nails")).build(),
      "escape.huggy_nails",
          null
  ),
  PENNYWISE_GIFT_BOX(
      26,
      27,
      TrapType.SLOWNESS,
      new ItemBuilder(Material.ARMOR_STAND).name(CC.translate("&aPennyWise Gift Box")).build(),
      "escape.gift_box",
          "\u3420"
  ),

  GHOSTFACE_KNIFES(
      28,
      29,
      TrapType.DAMAGE,
      new ItemBuilder(Material.ARMOR_STAND).name(CC.translate("&aGhostFace Knifes")).build(),
      "escape.knifetrap",
          null
  ),
  GHOSTFACE_BLOCKS(
      30,
      31,
      TrapType.SLOWNESS,
      new ItemBuilder(Material.ARMOR_STAND).name(CC.translate("&aGhostFace Block")).build(),
      "escape.block_crack",
          null
  );

  private int initCustomModelData;
  private int animateCustomModelData;
  private TrapType type;
  private ItemStack itemStack;
  private String sound;
  private String titleChar;

  public static TrapAnimation getByCustomModelData(int customModelData) {
    for (TrapAnimation animation : values()) {
      if (animation.getInitCustomModelData() == customModelData) {
        return animation;
      }
    }
    return null;
  }
}
