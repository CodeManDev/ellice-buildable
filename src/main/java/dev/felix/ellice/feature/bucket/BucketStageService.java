


package dev.felix.ellice.feature.bucket;

public final class BucketStageService {
    private Stage fhmke0q5niv4 = Stage.IDLE;
    private long f2gz21zb5led;
    private long f6isqj8cjhfk;
    private long fbbv0x91c300;
    private String f5ix1736lx17 = "Ready";

    public Stage stage() {
        return this.fhmke0q5niv4;
    }

    public String status() {
        return this.f5ix1736lx17;
    }

    public void prepare(long l, Environment environment, Policy policy) {
        if (this.fhmke0q5niv4 == Stage.IDLE) {
            if (l < this.f6isqj8cjhfk || !environment.plan() || !environment.begin(false)) {
                return;
            }
            this.fhmke0q5niv4 = Stage.AIM;
            this.f2gz21zb5led = l + 1500L;
            this.f5ix1736lx17 = "Aiming";
        }
        if (!environment.valid(this.fhmke0q5niv4 != Stage.RECOVER_WAIT)) {
            this.mdolhbi0q130(l, environment, policy, "Paused");
            return;
        }
        if (l > this.f2gz21zb5led) {
            this.mdolhbi0q130(l, environment, policy, "Server wait expired");
            return;
        }
        if (this.fhmke0q5niv4 == Stage.WAIT_PLACE || this.fhmke0q5niv4 == Stage.WAIT_RECOVER) {
            boolean bl = this.fhmke0q5niv4 == Stage.WAIT_RECOVER;
            Reply reply = environment.reply(bl);
            if (reply == Reply.REJECTED) {
                this.mdolhbi0q130(l, environment, policy, "Placement rejected");
                return;
            }
            if (reply == Reply.ACCEPTED) {
                environment.release();
                if (!bl && policy.recover()) {
                    this.fhmke0q5niv4 = Stage.RECOVER_WAIT;
                    this.fbbv0x91c300 = l + policy.recoveryDelayMs();
                    this.f2gz21zb5led = this.fbbv0x91c300 + policy.serverWaitMs();
                    this.f5ix1736lx17 = "Waiting to recover bucket";
                } else {
                    this.mdolhbi0q130(l, environment, policy, bl ? "Bucket recovered" : "Placed");
                    return;
                }
            }
        }
        if (this.fhmke0q5niv4 == Stage.RECOVER_WAIT) {
            if (l < this.fbbv0x91c300 || !environment.canRecover() || !environment.begin(true)) {
                return;
            }
            this.fhmke0q5niv4 = Stage.RECOVER_AIM;
            this.f2gz21zb5led = l + 1500L;
            this.f5ix1736lx17 = "Recovering bucket";
        }
        if (this.fhmke0q5niv4 == Stage.AIM || this.fhmke0q5niv4 == Stage.RECOVER_AIM) {
            environment.aim(this.fhmke0q5niv4 == Stage.RECOVER_AIM);
        }
    }

    public void afterMovement(long l, Environment environment, Policy policy) {
        boolean bl;
        if (this.fhmke0q5niv4 != Stage.AIM && this.fhmke0q5niv4 != Stage.RECOVER_AIM) {
            return;
        }
        boolean bl2 = bl = this.fhmke0q5niv4 == Stage.RECOVER_AIM;
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
        this.fhmke0q5niv4 = bl ? Stage.WAIT_RECOVER : Stage.WAIT_PLACE;
        this.f2gz21zb5led = l + policy.serverWaitMs();
        this.f5ix1736lx17 = "Waiting for server";
    }

    public void watchdog(long l, Environment environment, Policy policy) {
        if (!(this.fhmke0q5niv4 == Stage.IDLE || environment.valid(this.fhmke0q5niv4 != Stage.RECOVER_WAIT) && l <= this.f2gz21zb5led)) {
            this.mdolhbi0q130(l, environment, policy, "Paused");
        }
    }

    public void stop(Environment environment) {
        environment.release();
        environment.forget();
        this.fhmke0q5niv4 = Stage.IDLE;
        this.f6isqj8cjhfk = 0L;
        this.f2gz21zb5led = 0L;
        this.f5ix1736lx17 = "Ready";
    }

    private void mdolhbi0q130(long l, Environment environment, Policy policy, String string) {
        long l2 = this.fhmke0q5niv4 == Stage.AIM || this.fhmke0q5niv4 == Stage.RECOVER_AIM ? Math.min(100L, policy.cooldownMs()) : policy.cooldownMs();
        environment.release();
        environment.forget();
        this.fhmke0q5niv4 = Stage.IDLE;
        this.f6isqj8cjhfk = l + l2;
        this.f5ix1736lx17 = string;
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

    public record Policy(boolean recover, long cooldownMs, long serverWaitMs, long recoveryDelayMs) {
    }

    public static enum Reply {
        WAITING,
        ACCEPTED,
        REJECTED;

    }
}

