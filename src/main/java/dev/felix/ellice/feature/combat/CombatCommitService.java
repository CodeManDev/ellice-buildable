


package dev.felix.ellice.feature.combat;

import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.scaffold.LocalPhysicsFrameBus;
import dev.felix.ellice.feature.scaffold.PlacementOverrideBus;
import dev.felix.ellice.feature.scaffold.ScaffoldPublishService;
import dev.felix.ellice.feature.scaffold.ScaffoldRemapService;

public final class CombatCommitService {
    private final ScaffoldRemapService fh2r4xzsw3ld = new ScaffoldRemapService();

    public boolean commit(long l, double d, RotationData rotationData, ScaffoldRemapService.Input input) {
        return this.commit(l, d, rotationData, input, false);
    }

    public boolean commit(long l, double d, RotationData rotationData, ScaffoldRemapService.Input input, boolean bl) {
        ScaffoldRemapService.Result result = this.fh2r4xzsw3ld.remap(d, (float)rotationData.yaw(), input);
        return this.commitCorrected(l, rotationData, result.serverInput(), bl);
    }

    public boolean commitCorrected(long l, RotationData rotationData, ScaffoldRemapService.Input input, boolean bl) {
        boolean bl2;
        if (input.forward() && input.backward() || input.left() && input.right()) {
            throw new IllegalArgumentException("Corrected input must use canonical Vanilla directions");
        }
        boolean bl3 = bl2 = bl || !input.forward() || input.sneak();
        if (bl2 && input.sprint()) {
            input = new ScaffoldRemapService.Input(input.forward(), input.backward(), input.left(), input.right(), input.jump(), input.sneak(), false);
        }
        PlacementOverrideBus.publish(new PlacementOverrideBus.Override(true, input, false, bl2));
        ScaffoldPublishService.publish(input, bl2);
        LocalPhysicsFrameBus.publish(new LocalPhysicsFrameBus.Frame(l, (float)rotationData.yaw(), input, bl2));
        return bl2;
    }

    public void reconcile(long l, ScaffoldRemapService.Input input) {
        LocalPhysicsFrameBus.Frame frame = LocalPhysicsFrameBus.current().orElse(null);
        if (frame == null || frame.tick() != l) {
            return;
        }
        if (LocalPhysicsFrameBus.publishAppliedInput(l, input, frame.suppressSprint())) {
            LocalPhysicsFrameBus.Frame frame2 = LocalPhysicsFrameBus.current().orElseThrow();
            ScaffoldPublishService.publish(frame2.input(), frame2.suppressSprint());
        }
    }

    public void clear() {
        PlacementOverrideBus.clear();
        ScaffoldPublishService.clear();
        LocalPhysicsFrameBus.clear();
    }
}

