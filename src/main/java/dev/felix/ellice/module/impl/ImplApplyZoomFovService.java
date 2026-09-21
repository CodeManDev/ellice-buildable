



package dev.felix.ellice.module.impl;

import org.lwjgl.glfw.GLFW;
import dev.felix.ellice.compat.CompatAdapterService;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.module.ModuleSettingsService;

public final class ImplApplyZoomFovService extends ModuleSettingsService
{
    private final ModuleSetting.Keybind f653gl8ldeuh;
    private final ModuleSetting.Number fduust49exo1;
    private final ModuleSetting.Bool f75ob8fv8nne;
    private final ModuleSetting.Number ffbatvktoi2g;
    private static volatile boolean fe0pif97xwjx;
    private static volatile boolean feepxeav130m;
    private static volatile float ffv0qsjab3jz;
    private long fdsh2ddmn3tl;
    
    public ImplApplyZoomFovService() {
        super(ModuleBuilderData.builder("Zoom").description("Hold a configurable key to narrow the FOV with optional easing.").category(ModuleFeatureType.VISUALS).build());
        this.f653gl8ldeuh = this.setting((ModuleSetting.Keybind)new ModuleSetting.Keybind("Zoom Key", 67).description("Hold this key in-game to zoom; it is ignored while a screen or menu is open."));
        this.fduust49exo1 = this.setting((ModuleSetting.Number)new ModuleSetting.Number("Zoom Factor", Float.intBitsToFloat(1049582633), Float.intBitsToFloat(1036831949), Float.intBitsToFloat(1063675494), Float.intBitsToFloat(1008981770)).description("Multiplies the current field of view while held; lower values zoom in farther."));
        this.f75ob8fv8nne = this.setting((ModuleSetting.Bool)new ModuleSetting.Bool("Smooth", true).description("Eases into and out of zoom instead of switching the field of view instantly."));
        this.ffbatvktoi2g = this.setting((ModuleSetting.Number)new ModuleSetting.Number("Ease Speed", Float.intBitsToFloat(1096810496), 1.0f, Float.intBitsToFloat(1106247680), Float.intBitsToFloat(1056964608)).description("Controls smooth zoom response per second; higher values reach the target faster."));
    }
    
    @Override
    protected void onEnable() {
        ImplApplyZoomFovService.fe0pif97xwjx = true;
        ImplApplyZoomFovService.feepxeav130m = false;
        ImplApplyZoomFovService.ffv0qsjab3jz = 1.0f;
        this.fdsh2ddmn3tl = 0L;
        this.on(EventAttackInputService.RENDER).run(this::mf7565xskxer);
    }
    
    @Override
    protected void onDisable() {
        ImplApplyZoomFovService.fe0pif97xwjx = false;
        ImplApplyZoomFovService.feepxeav130m = false;
        ImplApplyZoomFovService.ffv0qsjab3jz = 1.0f;
        this.fdsh2ddmn3tl = 0L;
    }
    
    private void mf7565xskxer(final EventAttackInputService.Render render) {
        final Minecraft mc = CoreIsInitializedHandler.mc();
        final float ffv0qsjab3jz = (ImplApplyZoomFovService.feepxeav130m = (mc.level != null && mc.player != null && mc.screen == null && this.m5ar3yptinjj(mc))) ? this.fduust49exo1.get() : 1.0f;
        if (!this.f75ob8fv8nne.get()) {
            ImplApplyZoomFovService.ffv0qsjab3jz = ffv0qsjab3jz;
            this.fdsh2ddmn3tl = System.nanoTime();
            return;
        }
        final long nanoTime = System.nanoTime();
        final float n = (this.fdsh2ddmn3tl == 0L) ? Float.intBitsToFloat(1015580809) : ((nanoTime - this.fdsh2ddmn3tl) / Float.intBitsToFloat(1315859240));
        this.fdsh2ddmn3tl = nanoTime;
        ImplApplyZoomFovService.ffv0qsjab3jz = Mth.lerp(Mth.clamp(1.0f - (float)Math.exp(-this.ffbatvktoi2g.get() * Mth.clamp(n, 0.0f, Float.intBitsToFloat(1036831949))), 0.0f, 1.0f), ImplApplyZoomFovService.ffv0qsjab3jz, ffv0qsjab3jz);
    }
    
    private boolean m5ar3yptinjj(final Minecraft minecraft) {
        final int intValue = this.f653gl8ldeuh.get();
        return intValue > 0 && GLFW.glfwGetKey(CompatAdapterService.windowHandle(minecraft), intValue) == 1;
    }
    
    public static float applyZoomFov(final float n) {
        if (!ImplApplyZoomFovService.fe0pif97xwjx) {
            return n;
        }
        return Math.max(1.0f, n * Mth.clamp(ImplApplyZoomFovService.ffv0qsjab3jz, Float.intBitsToFloat(1028443341), 1.0f));
    }
    
    public static boolean controlsFov() {
        return ImplApplyZoomFovService.fe0pif97xwjx && (ImplApplyZoomFovService.feepxeav130m || ImplApplyZoomFovService.ffv0qsjab3jz < Float.intBitsToFloat(1065336439));
    }
    
    static {
        ImplApplyZoomFovService.ffv0qsjab3jz = 1.0f;
    }
}
