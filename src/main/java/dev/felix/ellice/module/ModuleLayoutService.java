package dev.felix.ellice.module;

import dev.felix.ellice.feature.inventory.InventoryRoleData;

public final class ModuleLayoutService extends ModuleSetting.Text {
  public ModuleLayoutService(String text, InventoryRoleData inventoryRoleData) {
    super(text, inventoryRoleData.encode(), 256);
  }

  public InventoryRoleData layout() {
    return InventoryRoleData.parse(this.get());
  }

  public void layout(InventoryRoleData inventoryRoleData) {
    this.set(inventoryRoleData.encode());
  }

  @Override
  public void set(String text) {
    super.set(InventoryRoleData.parse(text).encode());
  }
}
