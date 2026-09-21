package dev.felix.ellice.feature.tool;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

public final class ToolValueService {
  private ToolValueService() {}

  public static double value(
      AttributeInstance attributeInstance, Iterable<ItemStack> items, ItemStack itemStack) {
    if (attributeInstance == null) {
      return 0.0;
    }

    ArrayList arrayList = new ArrayList();
    ArrayList currentArrayList = new ArrayList();

    for (ItemStack currentItemStack : items) {
      currentItemStack.forEachModifier(
          EquipmentSlot.MAINHAND,
          (item, currentItem) -> {
            if (item.equals(attributeInstance.getAttribute())) {
              arrayList.add(currentItem);
            }
          });
    }

    itemStack.forEachModifier(
        EquipmentSlot.MAINHAND,
        (item, currentItem) -> {
          if (item.equals(attributeInstance.getAttribute())) {
            currentArrayList.add(currentItem);
          }
        });
    return project(attributeInstance, arrayList, currentArrayList);
  }

  static double project(
      AttributeInstance attributeInstance,
      List<AttributeModifier> items,
      List<AttributeModifier> currentItems) {
    AttributeInstance entry = new AttributeInstance(attributeInstance.getAttribute(), item -> {});
    entry.replaceFrom(attributeInstance);
    items.forEach(item -> entry.removeModifier(item.id()));
    currentItems.forEach(entry::addOrUpdateTransientModifier);
    return entry.getValue();
  }
}
