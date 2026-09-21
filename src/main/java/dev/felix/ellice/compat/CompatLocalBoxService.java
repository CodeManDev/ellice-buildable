








package dev.felix.ellice.compat;

import dev.felix.ellice.compat.CompatEnableHandler;
import dev.felix.ellice.feature.combat.CombatCommonBoxService;
import dev.felix.ellice.feature.rotation.RotationBoundingBox;
import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationObserveService;
import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.Optional;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.slf4j.LoggerFactory;

public final class CompatLocalBoxService {
    private CompatLocalBoxService() {
    }

    public static RotationBoundingBox localBox(Entity entity) {
        return CompatLocalBoxService.miduisg8kpm2(entity.getBoundingBox());
    }

    public static RotationBoundingBox receivedBox(Entity entity) {
        Optional<AABB> optional = CompatEnableHandler.receivedBox(entity.getUUID());
        if (optional.isPresent()) {
            return CompatLocalBoxService.miduisg8kpm2(optional.get());
        }
        Vec3 vec3 = entity.getPositionCodec().getBase().subtract(entity.position());
        return CompatLocalBoxService.miduisg8kpm2(entity.getBoundingBox().move(vec3));
    }

    public static RotationBoundingBox aimBox(Entity entity) {
        return CompatLocalBoxService.aimBox(CompatLocalBoxService.localBox(entity), CompatLocalBoxService.receivedBox(entity));
    }

    public static RotationBoundingBox aimBox(RotationBoundingBox rotationBoundingBox, RotationBoundingBox rotationBoundingBox2) {
        return CombatCommonBoxService.commonBox(rotationBoundingBox, rotationBoundingBox2).orElse(rotationBoundingBox);
    }

    public static boolean reachable(Entity entity, RotationVector rotationVector, RotationData rotationData, double d, double d2) {
        Optional<RotationBoundingBox> optional;
        RotationBoundingBox rotationBoundingBox2 = CompatLocalBoxService.localBox(entity);
        RotationBoundingBox rotationBoundingBox3 = CompatLocalBoxService.receivedBox(entity);
        Optional<RotationVector> optional2 = CombatCommonBoxService.hitPoint(rotationVector, rotationData, rotationBoundingBox2, d, d2);
        Optional<RotationVector> optional3 = CombatCommonBoxService.hitPoint(rotationVector, rotationData, rotationBoundingBox3, d, d2);
        if (Boolean.getBoolean("ellice.combat.trace")) {
            LoggerFactory.getLogger((String)"CombatTrace").info("geometry nanos={} target={} eye={} rotation={} range={} local={} received={} localHit={} receivedHit={} wire={}", new Object[]{System.nanoTime(), entity.getId(), rotationVector, rotationData, Math.min(d, d2), rotationBoundingBox2, rotationBoundingBox3, optional2.orElse(null), optional3.orElse(null), RotationObserveService.snapshot().orElse(null)});
        }
        if ((optional = CombatCommonBoxService.commonBox(rotationBoundingBox2, rotationBoundingBox3)).isPresent()) {
            return (optional2.isPresent() && optional3.isPresent() && optional.flatMap(rotationBoundingBox -> CombatCommonBoxService.hitPoint(rotationVector, rotationData, rotationBoundingBox, d, d2)).isPresent() ? 1 : 0) != 0;
        }
        if (CompatEnableHandler.receivedBox(entity.getUUID()).isPresent()) {
            return (optional2.isPresent() && CombatCommonBoxService.hitPoint(rotationVector, rotationData, rotationBoundingBox3, d + 1.0, d2 + 1.0).isPresent() ? 1 : 0) != 0;
        }
        return false;
    }

    private static RotationBoundingBox miduisg8kpm2(AABB aABB) {
        return new RotationBoundingBox(aABB.minX, aABB.minY, aABB.minZ, aABB.maxX, aABB.maxY, aABB.maxZ);
    }
}

