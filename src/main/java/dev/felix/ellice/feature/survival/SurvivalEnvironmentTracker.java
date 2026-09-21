package dev.felix.ellice.feature.survival;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class SurvivalEnvironmentTracker {
  private SurvivalEnvironmentTracker.Phase phase = SurvivalEnvironmentTracker.Phase.IDLE;
  private Object object;
  private long timestamp;
  private long timestamp2;
  private long timestamp3;
  private int count2 = -1;
  private int count3;
  private int count4 = -1;
  private String text = "";
  private String text2 = "";
  private String text3 = "Ready";

  public String status() {
    return this.text3;
  }

  public boolean eating() {
    return this.phase == SurvivalEnvironmentTracker.Phase.USE;
  }

  public void reset() {
    this.phase = SurvivalEnvironmentTracker.Phase.IDLE;
    this.object = null;
    this.timestamp = this.timestamp2 = this.timestamp3 = 0L;
    this.count2 = this.count4 = -1;
    this.text = this.text2 = "";
  }

  public void stop(SurvivalEnvironmentTracker.Environment environment, boolean enabled) {
    environment.finishUse(enabled);
    this.reset();
    this.text3 = "Disabled";
  }

  public void tick(
      long longValue,
      SurvivalEnvironmentTracker.Environment environment,
      SurvivalEnvironmentTracker.Policy currentPolicy) {
    Objects.requireNonNull(environment, "env");
    Objects.requireNonNull(currentPolicy, "policy");
    SurvivalItemData survivalItemData = environment.capture();
    if (survivalItemData == null) {
      environment.finishUse(currentPolicy.restoreSlot());
      this.reset();
      this.text3 = "Waiting for gameplay";
    } else {
      if (this.object != null && this.object != survivalItemData.session()) {
        environment.finishUse(currentPolicy.restoreSlot());
        this.reset();
      }

      this.object = survivalItemData.session();
      if (survivalItemData.manualInput()) {
        environment.finishUse(currentPolicy.restoreSlot());
        this.updateState2();
        this.timestamp3 = longValue + 400L;
        this.text3 = "Paused for your input";
      } else if (survivalItemData.paused()) {
        environment.finishUse(currentPolicy.restoreSlot());
        if (this.phase == SurvivalEnvironmentTracker.Phase.USE) {
          this.updateState2();
        }

        this.text3 = "Paused";
      } else if (longValue >= this.timestamp3 && longValue >= this.timestamp) {
        if (this.phase == SurvivalEnvironmentTracker.Phase.USE) {
          if (survivalItemData.food() > this.count3) {
            environment.finishUse(currentPolicy.restoreSlot());
            this.updateState2();
            this.timestamp = longValue + Math.max(200, currentPolicy.actionMillis());
            this.text3 = "Ate " + createText2(this.text);
          } else if (longValue >= this.timestamp2) {
            this.updateState(
                longValue,
                environment,
                survivalItemData,
                "No eating response from server",
                currentPolicy);
          }
        } else {
          if (this.phase == SurvivalEnvironmentTracker.Phase.REFILL) {
            SurvivalItemData.Item currentItem = survivalItemData.hotbar().get(this.count4);
            if (!this.text2.equals(currentItem.id()) || currentItem.count() <= 0) {
              this.updateState(
                  longValue,
                  environment,
                  survivalItemData,
                  "Inventory changed; refill stopped",
                  currentPolicy);
              return;
            }

            this.updateState2();
            this.text3 = "Hotbar refilled";
          }

          if (survivalItemData.usingItem()) {
            this.text3 = "Waiting for current item use";
          } else if (survivalItemData.food() >= 20) {
            this.text3 = this.createText(survivalItemData);
          } else {
            boolean enabled = currentPolicy.needsFood(survivalItemData);
            Optional result = SurvivalBestService.best(survivalItemData.hotbar());
            Optional currentResult = SurvivalBestService.best(List.of(survivalItemData.offhand()));
            if (currentResult.isPresent()
                && (result.isEmpty()
                    || calculateValue(
                            (SurvivalBestService.Pick) currentResult.get(),
                            (SurvivalBestService.Pick) result.get())
                        < 0)) {
              SurvivalBestService.Pick pick = (SurvivalBestService.Pick) currentResult.get();
              result =
                  Optional.of(
                      new SurvivalBestService.Pick(
                          9, pick.id(), pick.count(), pick.food(), pick.saturation()));
            }

            if (enabled && result.isPresent()) {
              SurvivalBestService.Pick currentPick = (SurvivalBestService.Pick) result.get();
              int value =
                  currentPick.index() == 9
                      ? SurvivalItemData.offhandMenuSlot()
                      : SurvivalItemData.hotbarMenuSlot(currentPick.index());
              this.count2 = value;
              this.text = currentPick.id();
              this.count3 = survivalItemData.food();
              if (environment.use(survivalItemData, value)) {
                this.phase = SurvivalEnvironmentTracker.Phase.USE;
                this.timestamp2 = longValue + Math.max(currentPolicy.responseMillis(), 3000);
                this.timestamp = longValue + currentPolicy.actionMillis();
                this.text3 = "Eating " + createText2(this.text);
              } else {
                this.text3 = "Eat unavailable";
                this.timestamp = longValue + 250L;
              }
            } else if (enabled && currentPolicy.refill()) {
              Optional nextResult = SurvivalBestService.best(survivalItemData.storage());
              if (nextResult.isPresent()) {
                int currentValue = calculateValue2(survivalItemData);
                if (currentValue >= 0
                    && environment.swap(
                        survivalItemData,
                        SurvivalItemData.storageMenuSlot(
                            ((SurvivalBestService.Pick) nextResult.get()).index()),
                        currentValue)) {
                  this.phase = SurvivalEnvironmentTracker.Phase.REFILL;
                  this.count4 = currentValue;
                  this.text2 = ((SurvivalBestService.Pick) nextResult.get()).id();
                  this.timestamp = longValue + currentPolicy.actionMillis();
                  this.text3 = "Moving " + createText2(this.text2) + " to hotbar";
                }
              } else {
                this.text3 = "No food anywhere";
              }
            } else {
              this.text3 = this.createText(survivalItemData);
            }
          }
        }
      }
    }
  }

  private static int calculateValue(
      SurvivalBestService.Pick pick, SurvivalBestService.Pick currentPick) {
    int value = Double.compare(currentPick.saturation(), pick.saturation());
    if (value != 0) {
      return value;
    }

    int currentValue = Integer.compare(currentPick.food(), pick.food());
    return currentValue != 0 ? currentValue : Integer.compare(currentPick.count(), pick.count());
  }

  private static int calculateValue2(SurvivalItemData survivalItemData) {
    for (int index = 0; index < 9; index++) {
      if (survivalItemData.hotbar().get(index).empty()) {
        return index;
      }
    }

    return survivalItemData.selected();
  }

  private String createText(SurvivalItemData survivalItemData) {
    int value = survivalItemData.readyCount();
    int currentValue = survivalItemData.storedCount();
    if (value > 0) {
      return "Ready · " + value + " food ready";
    } else {
      return currentValue > 0 ? "Food stored — enable refill or move it up" : "No food";
    }
  }

  private void updateState(
      long offset,
      SurvivalEnvironmentTracker.Environment environment,
      SurvivalItemData survivalItemData,
      String text,
      SurvivalEnvironmentTracker.Policy policy) {
    environment.finishUse(policy.restoreSlot());
    this.updateState2();
    this.timestamp3 = offset + Math.max(1000, policy.responseMillis());
    this.text3 = text;
  }

  private void updateState2() {
    this.phase = SurvivalEnvironmentTracker.Phase.IDLE;
    this.count2 = this.count4 = -1;
    this.text = this.text2 = "";
  }

  private static String createText2(String text) {
    return text.startsWith("minecraft:") ? text.substring("minecraft:".length()) : text;
  }

  public interface Environment {
    SurvivalItemData capture();

    boolean use(SurvivalItemData survivalItemData, int value);

    void finishUse(boolean enabled);

    boolean swap(SurvivalItemData survivalItemData, int value, int currentValue);
  }

  private enum Phase {
    IDLE,
    REFILL,
    USE;

    private static SurvivalEnvironmentTracker.Phase[] $values() {
      return new SurvivalEnvironmentTracker.Phase[] {IDLE, REFILL, USE};
    }
  }

  public record Policy(
      int foodThreshold,
      float criticalHearts,
      boolean refill,
      int actionMillis,
      int responseMillis,
      boolean restoreSlot) {
    public Policy(
        int foodThreshold,
        float criticalHearts,
        boolean refill,
        int actionMillis,
        int responseMillis,
        boolean restoreSlot) {
      if (foodThreshold >= 0
          && foodThreshold <= 19
          && Float.isFinite(criticalHearts)
          && !(criticalHearts < 0.0F)
          && !(criticalHearts > 20.0F)
          && actionMillis >= 50
          && responseMillis >= 500) {
        this.foodThreshold = foodThreshold;
        this.criticalHearts = criticalHearts;
        this.refill = refill;
        this.actionMillis = actionMillis;
        this.responseMillis = responseMillis;
        this.restoreSlot = restoreSlot;
      } else {
        throw new IllegalArgumentException("Invalid food policy");
      }
    }

    public boolean needsFood(SurvivalItemData survivalItemData) {
      return survivalItemData.food() <= this.foodThreshold
          ? true
          : this.criticalHearts > 0.0F
              && survivalItemData.food() < 20
              && survivalItemData.health() + survivalItemData.absorption()
                  <= this.criticalHearts * 2.0F;
    }
  }
}
