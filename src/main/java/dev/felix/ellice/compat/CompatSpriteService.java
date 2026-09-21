package dev.felix.ellice.compat;

import dev.felix.ellice.feature.inventory.InventoryRoleData;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class CompatSpriteService {
   private CompatSpriteService() {
   }

   public static TextureUvRegion sprite(InventoryRoleData.Role role) {
      try {
         if (Minecraft.getInstance() == null) {
            return null;
         }

         Item item = switch (role) {
            case LOCKED -> Items.BARRIER;
            case SWORD -> Items.DIAMOND_SWORD;
            case PICKAXE, SILK_TOUCH, FORTUNE -> Items.DIAMOND_PICKAXE;
            case AXE -> Items.DIAMOND_AXE;
            case SHOVEL -> Items.DIAMOND_SHOVEL;
            case HOE -> Items.DIAMOND_HOE;
            case RANGED -> Items.BOW;
            case BLOCKS -> Items.STONE;
            case FOOD -> Items.COOKED_BEEF;
            case HEALING -> Items.GOLDEN_APPLE;
            case PEARLS -> Items.ENDER_PEARL;
            case SHIELD -> Items.SHIELD;
            case UTILITY -> Items.WATER_BUCKET;
            case TOTEM -> Items.TOTEM_OF_UNDYING;
         };
         return CompatAdapterService.itemSprite(new ItemStack(item));
      } catch (RuntimeException | LinkageError runtimeExceptionLinkageError) {
         return null;
      }
   }
}
