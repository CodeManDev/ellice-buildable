package dev.felix.ellice.feature.inventory;

import java.util.List;
import java.util.Objects;

public record InventoryCellData(
    Object menu, List<InventoryCellData.Cell> cells, boolean paused, boolean cursorEmpty) {
  public InventoryCellData(
      Object menu, List<InventoryCellData.Cell> cells, boolean paused, boolean cursorEmpty) {
    cells = List.copyOf(cells);
    this.menu = menu;
    this.cells = cells;
    this.paused = paused;
    this.cursorEmpty = cursorEmpty;
  }

  public InventoryCellData.Cell cell(int value) {
    return this.cells.stream().filter(item -> item.index() == value).findFirst().orElseThrow();
  }

  public List<InventoryKindData> owned() {
    return this.cells.stream().map(InventoryCellData.Cell::item).filter(Objects::nonNull).toList();
  }

  public record Cell(int index, InventoryKindData item, boolean mayTake) {}
}
