package dev.felix.ellice.compat;

import dev.felix.ellice.feature.antibot.AntibotFeatureType;
import dev.felix.ellice.feature.nametags.NametagsData;
import dev.felix.ellice.feature.nametags.NametagsIsImportantService;
import dev.felix.ellice.friends.FriendsMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;

public final class NametagTargetCapture {
  private NametagTargetCapture() {}

  public static List<NametagTargetCapture.Target> capture(
      NametagTargetCapture.Options currentOptions, float value) {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.level != null && minecraft.player != null) {
      Entity currentValue =
          minecraft.getCameraEntity() != null ? minecraft.getCameraEntity() : minecraft.player;
      Vec3 vec3 = currentValue.getPosition(value);
      double doubleValue = currentOptions.range * currentOptions.range;
      ArrayList<NametagTargetCapture.Target> arrayList = new ArrayList<>();

      for (Entity currentEntity : minecraft.level.entitiesForRendering()) {
        if (!FriendSyncController.excluded(FriendsMode.NAMETAGS, currentEntity)
            && !CompatDecisionTracker.excluded(AntibotFeatureType.NAMETAGS, currentEntity)
            && currentEntity.isAlive()
            && !currentEntity.isSpectator()
            && !currentEntity.isInvisibleTo(minecraft.player)
            && (currentEntity != minecraft.player || currentOptions.self)
            && (currentEntity != currentValue
                || !minecraft.options.getCameraType().isFirstPerson())) {
          Vec3 currentVec3 = currentEntity.getPosition(value);
          double currentDoubleValue = currentVec3.distanceToSqr(vec3);
          if (Double.isFinite(currentDoubleValue) && !(currentDoubleValue > doubleValue)) {
            byte byteValue;
            if (currentEntity instanceof Player) {
              if (!currentOptions.players) {
                continue;
              }

              byteValue = 0;
            } else if (currentEntity instanceof ItemEntity itemEntity) {
              ItemStack itemStack = itemEntity.getItem();
              if (!currentOptions.items
                  || itemStack.isEmpty()
                  || currentOptions.importantOnly && !checkCondition(itemStack)) {
                continue;
              }

              byteValue = 1;
            } else if (currentEntity.hasCustomName() && currentOptions.named) {
              byteValue = 2;
            } else {
              if (!(currentEntity instanceof LivingEntity)
                  || !currentOptions.mobs.equals("All")
                      && (!currentOptions.mobs.equals("Hostile")
                          || !(currentEntity instanceof Enemy))) {
                continue;
              }

              byteValue = 3;
            }

            double nextDoubleValue =
                currentEntity instanceof ItemEntity ? 0.5 : currentEntity.getBbHeight() + 0.22;
            arrayList.add(
                new NametagTargetCapture.Target(
                    currentEntity,
                    currentVec3.add(0.0, nextDoubleValue, 0.0),
                    Math.sqrt(currentDoubleValue),
                    byteValue));
          }
        }
      }

      arrayList.sort(
          Comparator.comparingInt(NametagTargetCapture.Target::priority)
              .thenComparingDouble(NametagTargetCapture.Target::distance)
              .thenComparing(item -> item.entity.getUUID()));
      return arrayList;
    } else {
      return List.of();
    }
  }

  public static boolean visible(NametagTargetCapture.Target target, Vec3 vec3) {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.level != null && minecraft.player != null) {
      Vec3 currentVec3 =
          target.anchor.add(0.0, target.entity instanceof ItemEntity ? -0.3 : -0.32, 0.0);
      return minecraft
              .level
              .clip(
                  new ClipContext(vec3, currentVec3, Block.COLLIDER, Fluid.NONE, minecraft.player))
              .getType()
          == Type.MISS;
    } else {
      return false;
    }
  }

  public static NametagsData describe(
      NametagTargetCapture.Target target, boolean enabled, boolean currentEnabled) {
    Entity currentEntity = target.entity;
    String text = currentEnabled ? Math.round(target.distance) + "m" : "";
    if (currentEntity instanceof ItemEntity itemEntity) {
      ItemStack itemStack = itemEntity.getItem();
      String currentText = itemStack.getCount() > 1 ? "×" + itemStack.getCount() : "";
      String nextText =
          currentText + (!currentText.isEmpty() && !text.isEmpty() ? " · " : "") + text;

      int value =
          switch (itemStack.getRarity()) {
            case EPIC -> -3232513;
            case RARE -> -7221249;
            case UNCOMMON -> -10358;
            default -> itemStack.isEnchanted() ? -3232513 : -6559788;
          };
      return new NametagsData(
          CompatSessionNameService.replace(createText(itemStack.getHoverName())),
          nextText,
          value,
          -1.0F,
          0.0F,
          CompatAdapterService.itemSprite(itemStack),
          true);
    } else {
      int currentValue =
          currentEntity instanceof Player
              ? -5650945
              : (currentEntity instanceof Enemy ? -25691 : -6559788);
      float nextValue = -1.0F;
      float previousValue = 0.0F;
      if (enabled
          && currentEntity instanceof LivingEntity livingEntity
          && livingEntity.getMaxHealth() > 0.0F) {
        nextValue = livingEntity.getHealth() / livingEntity.getMaxHealth();
        previousValue = livingEntity.getAbsorptionAmount() / livingEntity.getMaxHealth();
      }

      return new NametagsData(
          CompatSessionNameService.replace(createText(currentEntity.getDisplayName())),
          text,
          currentValue,
          nextValue,
          previousValue,
          null,
          false);
    }
  }

  private static String createText(Component component) {
    if (component == null) {
      return "?";
    }

    StringBuilder stringBuilder = new StringBuilder();
    component.visit(
        (item, currentItem) -> {
          updateState(stringBuilder, item);
          stringBuilder.append(currentItem);
          return Optional.empty();
        },
        Style.EMPTY);
    return stringBuilder.isEmpty() ? component.getString() : stringBuilder.toString();
  }

  private static void updateState(StringBuilder stringBuilder, Style style) {
    stringBuilder.append('§').append('r');
    if (style.getColor() != null) {
      int value = style.getColor().getValue() & 16777215;
      stringBuilder.append('§').append('x');

      for (byte byteValue = 20; byteValue >= 0; byteValue += -4) {
        stringBuilder
            .append('§')
            .append(Character.toLowerCase(Character.forDigit(value >> byteValue & 15, 16)));
      }
    }

    if (style.isBold()) {
      stringBuilder.append('§').append('l');
    }

    if (style.isItalic()) {
      stringBuilder.append('§').append('o');
    }

    if (style.isUnderlined()) {
      stringBuilder.append('§').append('n');
    }

    if (style.isStrikethrough()) {
      stringBuilder.append('§').append('m');
    }

    if (style.isObfuscated()) {
      stringBuilder.append('§').append('k');
    }
  }

  private static boolean checkCondition(ItemStack itemStack) {
    return NametagsIsImportantService.isImportant(
        BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString(),
        itemStack.getRarity() != Rarity.COMMON,
        itemStack.isEnchanted(),
        itemStack.getCustomName() != null);
  }

  public record Options(
      boolean players,
      boolean items,
      boolean importantOnly,
      boolean named,
      String mobs,
      boolean self,
      double range) {}

  public record Target(Entity entity, Vec3 anchor, double distance, int priority) {}
}
