package dev.felix.ellice.compat;

import dev.felix.ellice.feature.combat.CombatBeginTickService;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ServerboundClientTickEndPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;

public final class CompatBeginTickService {
   private static Object object;
   private static Object object2;
   private static Object object3;
   private static final CombatBeginTickService combatBeginTickService = new CombatBeginTickService();

   private CompatBeginTickService() {
   }

   private static boolean checkCondition() {
      Minecraft minecraft = Minecraft.getInstance();
      return object != null && object == minecraft.player && object2 == minecraft.level && object3 == minecraft.getConnection();
   }

   private static void updateState() {
      if (!checkCondition()) {
         combatBeginTickService.reset();
         Minecraft minecraft = Minecraft.getInstance();
         object = minecraft.player;
         object2 = minecraft.level;
         object3 = minecraft.getConnection();
      }
   }

   public static void beginTick() {
      updateState();
      combatBeginTickService.beginTick();
   }

   public static void beginInput() {
      updateState();
      combatBeginTickService.beginInput();
   }

   public static void endInput() {
      combatBeginTickService.endInput();
   }

   public static boolean canAct() {
      return checkCondition() && combatBeginTickService.canAct();
   }

   public static void accepted() {
      updateState();
      combatBeginTickService.reserve();
   }

   public static void observeAccepted(Object value) {
      updateState();
      if (value instanceof ServerboundMovePlayerPacket) {
         combatBeginTickService.movement();
      } else if (value instanceof ServerboundClientTickEndPacket) {
         combatBeginTickService.tickEnd();
      } else if (value instanceof ServerboundUseItemPacket
         || value instanceof ServerboundUseItemOnPacket
         || value instanceof ServerboundPlayerActionPacket) {
         combatBeginTickService.action();
      }
   }

   public static boolean blocking() {
      return checkCondition() && combatBeginTickService.reserved();
   }
}
