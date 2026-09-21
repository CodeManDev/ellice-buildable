



package dev.felix.ellice.feature.velocity;

import dev.felix.ellice.module.Module;
import dev.felix.ellice.feature.scaffold.LocalPhysicsFrameBus;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.feature.combat.CombatOwnsService;
import dev.felix.ellice.feature.rotation.RotationVector;
import net.minecraft.world.phys.Vec3;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.player.LocalPlayer;
import dev.felix.ellice.module.impl.ImplSelectedModeSelector;

public final class VelocityActivateService
{
    private static final VelocityTransformConverter fh08yztkwnsf;
    private static ImplSelectedModeSelector fabzr49momga;
    private static LocalPlayer f9htd26qouxk;
    private static boolean f3n21tq0izu;
    private static int f82o9ou846g1;
    
    private VelocityActivateService() {
    }
    
    public static void activate(final ImplSelectedModeSelector fabzr49momga) {
        VelocityActivateService.fabzr49momga = fabzr49momga;
        reset();
    }
    
    public static void deactivate() {
        VelocityActivateService.fabzr49momga = null;
        reset();
    }
    
    public static void reset() {
        VelocityActivateService.fh08yztkwnsf.reset();
        VelocityActivateService.f9htd26qouxk = null;
        VelocityActivateService.f3n21tq0izu = false;
    }
    
    private static boolean mbbtv2hwugcv(final Entity entity) {
        if (VelocityActivateService.fabzr49momga == null) {
            return false;
        }
        final Minecraft instance = Minecraft.getInstance();
        return entity != null && entity == instance.player && instance.level != null && instance.player.isAlive() && !instance.player.isSpectator() && !instance.player.isPassenger();
    }
    
    public static Vec3 impulse(final Entity entity, final Vec3 vec3, final boolean b) {
        if (!mbbtv2hwugcv(entity) || (b && !VelocityActivateService.fabzr49momga.explosions.get())) {
            return vec3;
        }
        final VelocityTransformConverter.Mode selectedMode = VelocityActivateService.fabzr49momga.selectedMode();
        if (selectedMode == VelocityTransformConverter.Mode.PUSH || (VelocityActivateService.fabzr49momga.onlyGround.get() && selectedMode != VelocityTransformConverter.Mode.JUMP && !entity.onGround()) || entity.getRandom().nextFloat() * Float.intBitsToFloat(1120403456) >= VelocityActivateService.fabzr49momga.chance.get()) {
            return vec3;
        }
        if (selectedMode == VelocityTransformConverter.Mode.CANCEL) {
            return null;
        }
        if (selectedMode == VelocityTransformConverter.Mode.JUMP) {
            if (vec3.lengthSqr() > Double.longBitsToDouble(4457293557087583675L)) {
                if (VelocityActivateService.f9htd26qouxk != entity) {
                    VelocityActivateService.fh08yztkwnsf.reset();
                    VelocityActivateService.f9htd26qouxk = (LocalPlayer)entity;
                }
                VelocityActivateService.fh08yztkwnsf.impulse(VelocityActivateService.f9htd26qouxk.tickCount, Math.round(VelocityActivateService.fabzr49momga.jumpDelay.get()));
            }
            return vec3;
        }
        final RotationVector transform = VelocityTransformConverter.transform(selectedMode, mel8vsez0l7r(entity.getDeltaMovement()), mel8vsez0l7r(vec3), b, (selectedMode == VelocityTransformConverter.Mode.REVERSE) ? VelocityActivateService.fabzr49momga.reverseStrength.get() : VelocityActivateService.fabzr49momga.horizontal.get(), VelocityActivateService.fabzr49momga.vertical.get());
        return new Vec3(transform.x(), transform.y(), transform.z());
    }
    
    public static double pushScale(final Entity entity) {
        return (mbbtv2hwugcv(entity) && VelocityActivateService.fabzr49momga.selectedMode() == VelocityTransformConverter.Mode.PUSH) ? (VelocityActivateService.fabzr49momga.entityPush.get() / Float.intBitsToFloat(1120403456)) : 1.0;
    }
    
    public static boolean cancelBlockPush(final Entity entity) {
        return mbbtv2hwugcv(entity) && VelocityActivateService.fabzr49momga.selectedMode() == VelocityTransformConverter.Mode.PUSH && !VelocityActivateService.fabzr49momga.blockPush.get();
    }
    
    public static void beforeMovement() {
        VelocityActivateService.f3n21tq0izu = false;
        final Minecraft instance = Minecraft.getInstance();
        final LocalPlayer player = instance.player;
        final CombatOwnsService.Owner owner = CombatOwnsService.currentOwner().orElse(null);
        final boolean b = owner != null && owner != CombatOwnsService.Owner.KILL_AURA;
        final boolean b2 = CoreIsInitializedHandler.isReady() && CoreIsInitializedHandler.get().modules().get("Speed").map(module -> module.isEnabled()).orElse(false);
        VelocityActivateService.f3n21tq0izu = VelocityActivateService.fh08yztkwnsf.jump((player == null) ? 0L : ((long)player.tickCount), mbbtv2hwugcv((Entity)player) && VelocityActivateService.f9htd26qouxk == player && VelocityActivateService.fabzr49momga.selectedMode() == VelocityTransformConverter.Mode.JUMP && instance.screen == null && instance.getOverlay() == null && !instance.isPaused() && instance.isWindowActive() && !player.isInWater() && !player.isInLava() && !player.onClimbable() && !player.isFallFlying() && !player.getAbilities().flying && !b && !b2, player != null && player.onGround());
        if (VelocityActivateService.f3n21tq0izu) {
            VelocityActivateService.f82o9ou846g1 = player.tickCount;
        }
    }
    
    public static boolean jumpRequested() {
        final LocalPlayer player = Minecraft.getInstance().player;
        return VelocityActivateService.f3n21tq0izu && mbbtv2hwugcv((Entity)player) && player == VelocityActivateService.f9htd26qouxk && player.tickCount == VelocityActivateService.f82o9ou846g1 && !LocalPhysicsFrameBus.wasAborted(VelocityActivateService.f82o9ou846g1);
    }
    
    private static RotationVector mel8vsez0l7r(final Vec3 vec3) {
        return new RotationVector(vec3.x, vec3.y, vec3.z);
    }
    
    static {
        fh08yztkwnsf = new VelocityTransformConverter();
    }
}
