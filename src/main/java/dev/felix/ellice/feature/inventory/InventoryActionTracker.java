package dev.felix.ellice.feature.inventory;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.random.RandomGenerator;

public final class InventoryActionTracker {
  private final InventoryPlanService inventoryPlanService = new InventoryPlanService();
  private final RandomGenerator randomGenerator;
  private final ArrayDeque<InventoryPlanService.Action> items = new ArrayDeque<>();
  private final Map<InventoryPlanService.Action, Integer> entries = new HashMap<>();
  private InventoryPlanService.Action action;
  private InventoryPlanService.Policy policy;
  private Object object;
  private long timestamp;
  private long timestamp2;
  private String text = "Open your inventory";

  public InventoryActionTracker(RandomGenerator currentRandomGenerator) {
    this.randomGenerator = currentRandomGenerator;
  }

  public String status() {
    return this.text;
  }

  public void reset() {
    this.items.clear();
    this.entries.clear();
    this.action = null;
    this.policy = null;
    this.object = null;
    this.text = "Open your inventory";
  }

  public void tick(
      long longValue,
      InventoryActionTracker.Environment environment,
      InventoryPlanService.Policy currentPolicy,
      int value,
      int currentValue) {
    int nextValue = Math.max(50, value);
    int previousValue = Math.max(nextValue, currentValue);
    Optional result = environment.capture();
    if (result.isEmpty()) {
      this.reset();
    } else {
      InventoryCellData inventoryCellData = (InventoryCellData) result.get();
      if (inventoryCellData.menu() != this.object) {
        this.reset();
        this.object = inventoryCellData.menu();
        this.timestamp = longValue + 300L;
      }

      if (!inventoryCellData.paused() && inventoryCellData.cursorEmpty()) {
        if (!currentPolicy.equals(this.policy)) {
          this.items.clear();
          this.action = null;
          this.policy = currentPolicy;
        }

        if (this.action != null) {
          if (this.action.applied(inventoryCellData)) {
            this.action = null;
          } else {
            if (this.action.matches(inventoryCellData) && longValue - this.timestamp2 < 900L) {
              this.text = "Waiting for inventory change";
              return;
            }

            this.action = null;
            this.items.clear();
            this.timestamp = longValue + 300L;
          }
        }

        if (longValue >= this.timestamp) {
          if (!this.items.isEmpty() && !this.items.peek().matches(inventoryCellData)) {
            this.items.clear();
          }

          if (this.items.isEmpty()) {
            InventoryPlanService.Plan currentPlan =
                this.inventoryPlanService.plan(inventoryCellData, currentPolicy);
            this.text = currentPlan.reason();
            this.items.addAll(currentPlan.actions());
          }

          if (!this.items.isEmpty()) {
            InventoryPlanService.Action currentAction = this.items.peek();
            if (this.entries.getOrDefault(currentAction, 0) >= 2) {
              this.text = "Inventory changed repeatedly; reopen it to retry";
            } else {
              this.entries.merge(currentAction, 1, Integer::sum);
              if (environment.execute(inventoryCellData, currentAction)) {
                this.action = this.items.remove();
                this.timestamp2 = longValue;
              } else {
                this.items.clear();
              }

              this.timestamp =
                  longValue
                      + nextValue
                      + (int)
                          ((previousValue - nextValue)
                              * (this.randomGenerator.nextDouble()
                                  + this.randomGenerator.nextDouble())
                              / 2.0);
            }
          }
        }
      } else {
        this.items.clear();
        this.action = null;
        this.timestamp = longValue + 250L;
        this.text = "Paused for your inventory input";
      }
    }
  }

  public interface Environment {
    Optional<InventoryCellData> capture();

    boolean execute(InventoryCellData inventoryCellData, InventoryPlanService.Action action);
  }
}
