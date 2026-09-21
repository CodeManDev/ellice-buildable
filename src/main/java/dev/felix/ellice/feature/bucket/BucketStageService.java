package dev.felix.ellice.feature.bucket;

public final class BucketStageService {
  private Stage stage2 = Stage.IDLE;
  private long timestamp;
  private long timestamp2;
  private long timestamp3;
  private String text = "Ready";

  public Stage stage() {
    return this.stage2;
  }

  public String status() {
    return this.text;
  }

  public void prepare(long l, Environment environment, Policy policy) {
    if (this.stage2 == Stage.IDLE) {
      if (l < this.timestamp2 || !environment.plan() || !environment.begin(false)) {
        return;
      }
      this.stage2 = Stage.AIM;
      this.timestamp = l + 1500L;
      this.text = "Aiming";
    }
    if (!environment.valid(this.stage2 != Stage.RECOVER_WAIT)) {
      this.mdolhbi0q130(l, environment, policy, "Paused");
      return;
    }
    if (l > this.timestamp) {
      this.mdolhbi0q130(l, environment, policy, "Server wait expired");
      return;
    }
    if (this.stage2 == Stage.WAIT_PLACE || this.stage2 == Stage.WAIT_RECOVER) {
      boolean bl = this.stage2 == Stage.WAIT_RECOVER;
      Reply reply = environment.reply(bl);
      if (reply == Reply.REJECTED) {
        this.mdolhbi0q130(l, environment, policy, "Placement rejected");
        return;
      }
      if (reply == Reply.ACCEPTED) {
        environment.release();
        if (!bl && policy.recover()) {
          this.stage2 = Stage.RECOVER_WAIT;
          this.timestamp3 = l + policy.recoveryDelayMs();
          this.timestamp = this.timestamp3 + policy.serverWaitMs();
          this.text = "Waiting to recover bucket";
        } else {
          this.mdolhbi0q130(l, environment, policy, bl ? "Bucket recovered" : "Placed");
          return;
        }
      }
    }
    if (this.stage2 == Stage.RECOVER_WAIT) {
      if (l < this.timestamp3 || !environment.canRecover() || !environment.begin(true)) {
        return;
      }
      this.stage2 = Stage.RECOVER_AIM;
      this.timestamp = l + 1500L;
      this.text = "Recovering bucket";
    }
    if (this.stage2 == Stage.AIM || this.stage2 == Stage.RECOVER_AIM) {
      environment.aim(this.stage2 == Stage.RECOVER_AIM);
    }
  }

  public void afterMovement(long l, Environment environment, Policy policy) {
    boolean bl;
    if (this.stage2 != Stage.AIM && this.stage2 != Stage.RECOVER_AIM) {
      return;
    }
    boolean bl2 = bl = this.stage2 == Stage.RECOVER_AIM;
    if (!environment.valid(true)) {
      this.mdolhbi0q130(l, environment, policy, "Paused");
      return;
    }
    if (!environment.ready(bl)) {
      return;
    }
    if (!environment.use(bl)) {
      this.mdolhbi0q130(l, environment, policy, "No usable placement");
      return;
    }
    this.stage2 = bl ? Stage.WAIT_RECOVER : Stage.WAIT_PLACE;
    this.timestamp = l + policy.serverWaitMs();
    this.text = "Waiting for server";
  }

  public void watchdog(long l, Environment environment, Policy policy) {
    if (!(this.stage2 == Stage.IDLE
        || environment.valid(this.stage2 != Stage.RECOVER_WAIT) && l <= this.timestamp)) {
      this.mdolhbi0q130(l, environment, policy, "Paused");
    }
  }

  public void stop(Environment environment) {
    environment.release();
    environment.forget();
    this.stage2 = Stage.IDLE;
    this.timestamp2 = 0L;
    this.timestamp = 0L;
    this.text = "Ready";
  }

  private void mdolhbi0q130(long l, Environment environment, Policy policy, String string) {
    long l2 =
        this.stage2 == Stage.AIM || this.stage2 == Stage.RECOVER_AIM
            ? Math.min(100L, policy.cooldownMs())
            : policy.cooldownMs();
    environment.release();
    environment.forget();
    this.stage2 = Stage.IDLE;
    this.timestamp2 = l + l2;
    this.text = string;
  }

  public static enum Stage {
    IDLE,
    AIM,
    WAIT_PLACE,
    RECOVER_WAIT,
    RECOVER_AIM,
    WAIT_RECOVER;
  }

  public static interface Environment {
    public boolean plan();

    public boolean begin(boolean var1);

    public boolean valid(boolean var1);

    public void aim(boolean var1);

    public boolean ready(boolean var1);

    public boolean use(boolean var1);

    public Reply reply(boolean var1);

    public boolean canRecover();

    public void release();

    public void forget();
  }

  public record Policy(boolean recover, long cooldownMs, long serverWaitMs, long recoveryDelayMs) {}

  public static enum Reply {
    WAITING,
    ACCEPTED,
    REJECTED;
  }
}
