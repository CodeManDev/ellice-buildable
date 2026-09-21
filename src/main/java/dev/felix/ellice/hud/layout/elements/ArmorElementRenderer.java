package dev.felix.ellice.hud.layout.elements;

import dev.felix.ellice.compat.TextureUvRegion;
import dev.felix.ellice.compat.CompatAdapterService;
import dev.felix.ellice.hud.layout.LayoutIsContainerService;
import dev.felix.ellice.hud.layout.LayoutOperationHandler;
import dev.felix.ellice.hud.layout.LayoutStringService;
import dev.felix.ellice.hud.layout.HudElementRenderer;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.ui.theme.ThemeMixService;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public final class ArmorElementRenderer implements HudElementRenderer {
   private static final EquipmentSlot[] equipmentSlot = new EquipmentSlot[]{
      EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
   };

   @Override
   public void draw(
      CompositorPushPresentationScaleService compositorPushPresentationScale, LayoutIsContainerService layoutIsContainer, float width, float height, float value, float currentValue, LayoutOperationHandler layoutOperation, float nextValue, float previousValue
   ) {
      float sourceValue = Math.max(8.0F, LayoutStringService.number(layoutIsContainer.props, "size", 26.0F))
         * previousValue;
      float targetValue = Math.max(0.0F, LayoutStringService.number(layoutIsContainer.props, "gap", 5.0F)) * previousValue;
      boolean enabled = "vertical".equalsIgnoreCase(LayoutStringService.string(layoutIsContainer.props, "orientation", "horizontal"));
      boolean currentEnabled = LayoutStringService.bool(layoutIsContainer.props, "showEmpty", false);
      boolean nextEnabled = LayoutStringService.bool(layoutIsContainer.props, "durabilityBar", true);
      int inputValue = LayoutStringService.withOpacity(LayoutStringService.color(layoutIsContainer.props, "accent", -10262799), nextValue);
      List items = collectValues(currentEnabled);

      for (int index = 0; index < items.size(); index++) {
         ItemStack itemStack = (ItemStack)items.get(index);
         float outputValue = width + (enabled ? 0.0F : index * (sourceValue + targetValue));
         float resultValue = height + (enabled ? index * (sourceValue + targetValue) : 0.0F);
         float candidateValue = Math.max(3.0F, sourceValue * 0.2F);
         int selectedValue = LayoutStringService.withOpacity(itemStack.isEmpty() ? 807411752 : 1881153584, nextValue);
         compositorPushPresentationScale.roundedRect(
            outputValue,
            resultValue,
            sourceValue,
            sourceValue,
            candidateValue,
            candidateValue,
            candidateValue,
            candidateValue,
            selectedValue,
            0.0F,
            itemStack.isEmpty() ? 0.0F : 4.0F,
            LayoutStringService.withOpacity(1342177280, nextValue),
            0.7F * previousValue,
            LayoutStringService.withOpacity(905969663, nextValue),
            nextValue
         );
         if (!itemStack.isEmpty()) {
            TextureUvRegion textureUvRegion = CompatAdapterService.itemSprite(itemStack);
            if (textureUvRegion != null && textureUvRegion.textureId() != 0) {
               float defaultValue = Math.max(2.0F, sourceValue * 0.13F);
               float initialValue = sourceValue - defaultValue * 2.0F;
               compositorPushPresentationScale.drawTextureRegion(
                  new RhiBlendStateService.TextureHandle(textureUvRegion.textureId()),
                  outputValue + defaultValue,
                  resultValue + defaultValue,
                  initialValue,
                  initialValue,
                  nextValue,
                  0.0F,
                  0.0F,
                  0.0F,
                  0.0F,
                  0,
                  0.0F,
                  0.0F,
                  textureUvRegion.u0(),
                  textureUvRegion.v0(),
                  textureUvRegion.u1(),
                  textureUvRegion.v1(),
                  true
               );
            }

            if (nextEnabled && itemStack.isDamageableItem() && itemStack.getMaxDamage() > 0) {
               float resolvedValue = 1.0F - (float)itemStack.getDamageValue() / itemStack.getMaxDamage();
               resolvedValue = Math.max(0.0F, Math.min(1.0F, resolvedValue));
               float computedValue = outputValue + sourceValue * 0.15F;
               float cachedValue = resultValue + sourceValue - Math.max(3.0F, sourceValue * 0.13F);
               float pendingValue = sourceValue * 0.7F;
               float activeValue = Math.max(1.5F, sourceValue * 0.075F);
               int fallbackValue = ThemeMixService.mix(-45730, -12197757, resolvedValue);
               compositorPushPresentationScale.roundedRect(
                  computedValue, cachedValue, pendingValue, activeValue, activeValue * 0.5F, LayoutStringService.withOpacity(2013265920, nextValue)
               );
               compositorPushPresentationScale.roundedRect(
                  computedValue,
                  cachedValue,
                  Math.max(activeValue, pendingValue * resolvedValue),
                  activeValue,
                  activeValue * 0.5F,
                  LayoutStringService.withOpacity(fallbackValue, nextValue)
               );
            }
         } else {
            float primaryValue = sourceValue * 0.3F;
            compositorPushPresentationScale.roundedRect(
               outputValue + primaryValue,
               resultValue + primaryValue,
               sourceValue - primaryValue * 2.0F,
               sourceValue - primaryValue * 2.0F,
               2.0F * previousValue,
               LayoutStringService.withOpacity(inputValue & 1442840575, nextValue)
            );
         }
      }
   }

   @Override
   public HudElementRenderer.Size measure(LayoutIsContainerService layoutIsContainer, LayoutOperationHandler layoutOperation, float value, float currentValue) {
      float nextValue = Math.max(8.0F, LayoutStringService.number(layoutIsContainer.props, "size", 26.0F));
      float previousValue = Math.max(0.0F, LayoutStringService.number(layoutIsContainer.props, "gap", 5.0F));
      int currentSize = Math.max(1, collectValues(LayoutStringService.bool(layoutIsContainer.props, "showEmpty", false)).size());
      boolean enabled = "vertical".equalsIgnoreCase(LayoutStringService.string(layoutIsContainer.props, "orientation", "horizontal"));
      return enabled
         ? new HudElementRenderer.Size(nextValue, currentSize * nextValue + Math.max(0, currentSize - 1) * previousValue)
         : new HudElementRenderer.Size(currentSize * nextValue + Math.max(0, currentSize - 1) * previousValue, nextValue);
   }

   private static List<ItemStack> collectValues(boolean enabled) {
      ArrayList arrayList = new ArrayList(4);

      try {
         LocalPlayer localPlayer = Minecraft.getInstance().player;
         if (localPlayer == null) {
            if (enabled) {
               for (int index = 0; index < 4; index++) {
                  arrayList.add(ItemStack.EMPTY);
               }
            }

            return arrayList;
         }

         for (EquipmentSlot currentEquipmentSlot : equipmentSlot) {
            ItemStack itemStack = localPlayer.getItemBySlot(currentEquipmentSlot);
            if (enabled || itemStack != null && !itemStack.isEmpty()) {
               arrayList.add(itemStack == null ? ItemStack.EMPTY : itemStack);
            }
         }
      } catch (Throwable exception) {
         if (enabled) {
            for (int currentIndex = arrayList.size(); currentIndex < 4; currentIndex++) {
               arrayList.add(ItemStack.EMPTY);
            }
         }
      }

      return arrayList;
   }
}
