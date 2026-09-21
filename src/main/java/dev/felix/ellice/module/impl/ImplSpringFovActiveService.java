



package dev.felix.ellice.module.impl;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.Minecraft;
import dev.felix.ellice.core.CoreSetBehindScreenService;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import dev.felix.ellice.event.EventAttackInputService;
import net.minecraft.util.Mth;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.util.UtilIsPrimedService;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.module.ModuleNameService;
import dev.felix.ellice.module.ModuleSettingsService;

public final class ImplSpringFovActiveService extends ModuleSettingsService
{
    private final ModuleNameService f1v43asr8kz1;
    private final ModuleNameService f4cx525a723g;
    private final ModuleNameService fd2pslralfoz;
    private final ModuleNameService f5vgidatqoc4;
    private final ModuleSetting.Bool fgcc7vohlfgj;
    private final ModuleSetting.Mode ff2tyep15w16;
    private final ModuleSetting.Bool f8wvfny614q3;
    private final ModuleSetting.Number fb7uv1fzob6d;
    private final ModuleSetting.Bool fbd41oil776j;
    private final ModuleSetting.Number ffe7hrd52hp5;
    private final ModuleSetting.Bool fbkvvb9rjyfl;
    private final ModuleSetting.Bool fh6ppn45kuic;
    private final ModuleSetting.Number fg2z42p8xbz8;
    private final ModuleSetting.Bool fcpjs2hrg7kw;
    private final ModuleSetting.Number fdww15gx8n5a;
    private static final UtilIsPrimedService f7zurjog6n59;
    private static final UtilIsPrimedService f9mq4z5090l4;
    private static final UtilIsPrimedService f6ce5vvcsnxe;
    private static volatile boolean f77se698jgrs;
    private static volatile boolean fiifwm3gwth6;
    private static volatile boolean faqgjdcpizc0;
    private static volatile float fujo30fz2gz;
    private static volatile float f4u47qcmhfos;
    private static volatile SceneEaseHandler.Spring f6gq1y5kxixi;
    private static final SceneEaseHandler.Spring f36q0898x6y1;
    private static final SceneEaseHandler.Spring fcss11geckm9;
    private static volatile float f2lv3em3wz1m;
    private static final float f3p3ckscw7fi = 8.0f;
    private static volatile float f7co66lorpu6;
    private static final float fcsqwp5ue1ym = 4.0f;
    private static long f5nyjj17urim;
    private static long fa8sikwe537j;
    private float f9txone2im8l;
    private long f9bu877ta3cz;
    private float f9mmeg5i8etp;
    private long fbuw1p20c99c;
    private static long fbkki0u51ao5;
    
    public ImplSpringFovActiveService() {
        super(ModuleBuilderData.builder("Kinetics").description("Smooths FOV, crouching, camera lean, item swaps, and damage feedback.").category(ModuleFeatureType.VISUALS).build());
        this.f1v43asr8kz1 = this.settingCategory("Field of view");
        this.f4cx525a723g = this.settingCategory("Damage feedback");
        this.fd2pslralfoz = this.settingCategory("Camera");
        this.f5vgidatqoc4 = this.settingCategory("Held item");
        this.fgcc7vohlfgj = this.setting(this.f1v43asr8kz1, (ModuleSetting.Bool)new ModuleSetting.Bool("Spring FOV", true).description("Applies a spring to client FOV changes such as sprint and speed transitions; gameplay is unchanged."));
        this.ff2tyep15w16 = this.setting(this.f1v43asr8kz1, (ModuleSetting.Mode)new ModuleSetting.Mode("FOV Feel", new String[] { "Snappy", "Smooth", "Gentle" }, "Snappy").description("Selects the FOV spring response, from quick Snappy motion to slower Gentle settling."));
        this.f8wvfny614q3 = this.setting(this.f4cx525a723g, (ModuleSetting.Bool)new ModuleSetting.Bool("Hit Punch", true).description("Briefly narrows your FOV when you take damage, then lets the FOV spring recover."));
        this.fb7uv1fzob6d = this.setting(this.f4cx525a723g, (ModuleSetting.Number)new ModuleSetting.Number("Punch Kick", Float.intBitsToFloat(1080033280), 0.0f, Float.intBitsToFloat(1090519040), Float.intBitsToFloat(1048576000)).description("Sets the base damage-punch reduction in FOV degrees; incoming damage scales the final kick."));
        this.fbd41oil776j = this.setting(this.f4cx525a723g, (ModuleSetting.Bool)new ModuleSetting.Bool("Damage Flash", true).description("Flashes a red full-screen overlay when your health decreases."));
        this.ffe7hrd52hp5 = this.setting(this.f4cx525a723g, (ModuleSetting.Number)new ModuleSetting.Number("Flash Alpha", Float.intBitsToFloat(1051931443), 0.0f, 1.0f, Float.intBitsToFloat(1008981770)).description("Sets the peak opacity of the red damage flash; 0 makes it invisible."));
        this.fbkvvb9rjyfl = this.setting(this.fd2pslralfoz, (ModuleSetting.Bool)new ModuleSetting.Bool("Smooth Sneak", true).description("Springs camera eye height between standing and crouching instead of snapping instantly."));
        this.fh6ppn45kuic = this.setting(this.fd2pslralfoz, (ModuleSetting.Bool)new ModuleSetting.Bool("Camera Lean", true).description("Rolls the camera slightly in the direction of fast yaw turns; player rotation is untouched."));
        this.fg2z42p8xbz8 = this.setting(this.fd2pslralfoz, (ModuleSetting.Number)new ModuleSetting.Number("Lean Amount", Float.intBitsToFloat(1068708659), 0.0f, Float.intBitsToFloat(1082130432), Float.intBitsToFloat(1036831949)).description("Sets the maximum camera roll in degrees during a fast turn."));
        this.fcpjs2hrg7kw = this.setting(this.f5vgidatqoc4, (ModuleSetting.Bool)new ModuleSetting.Bool("Item Swap Smoothing", true).description("Slows the first-person held-item equip animation when changing hotbar items."));
        this.fdww15gx8n5a = this.setting(this.f5vgidatqoc4, (ModuleSetting.Number)new ModuleSetting.Number("Swap Step", Float.intBitsToFloat(1043878380), Float.intBitsToFloat(1028443341), Float.intBitsToFloat(1053609165), Float.intBitsToFloat(1008981770)).description("Limits equip progress per game tick; lower values animate more slowly and 0.40 matches vanilla."));
        this.f9txone2im8l = Float.intBitsToFloat(2143289344);
        this.f9mmeg5i8etp = Float.intBitsToFloat(2143289344);
        this.ff2tyep15w16.onChange(s -> ImplSpringFovActiveService.f6gq1y5kxixi = mcjmfr1ykcqu(s));
        this.fdww15gx8n5a.onChange(n -> ImplSpringFovActiveService.f4u47qcmhfos = ((this.isEnabled() && this.fcpjs2hrg7kw.get()) ? n : Float.intBitsToFloat(1053609165)));
        this.fcpjs2hrg7kw.onChange(b -> ImplSpringFovActiveService.f4u47qcmhfos = ((this.isEnabled() && b) ? this.fdww15gx8n5a.get() : Float.intBitsToFloat(1053609165)));
        this.fg2z42p8xbz8.onChange(n2 -> ImplSpringFovActiveService.fujo30fz2gz = n2);
        this.fbkvvb9rjyfl.onChange(b2 -> ImplSpringFovActiveService.fiifwm3gwth6 = (this.isEnabled() && b2));
        this.fh6ppn45kuic.onChange(b3 -> ImplSpringFovActiveService.faqgjdcpizc0 = (this.isEnabled() && b3));
        this.fgcc7vohlfgj.onChange(b4 -> {
            if (!(ImplSpringFovActiveService.f77se698jgrs = (this.isEnabled() && b4))) {
                ImplSpringFovActiveService.f7zurjog6n59.reset();
            }
        });
    }
    
    private static SceneEaseHandler.Spring mcjmfr1ykcqu(final String s) {
        return switch (s) {
            case "Smooth" -> SceneEaseHandler.Spring.DEFAULT;
            case "Gentle" -> SceneEaseHandler.Spring.GENTLE;
            default -> SceneEaseHandler.Spring.SNAPPY;
        };
    }
    
    public static boolean springFovActive() {
        return ImplSpringFovActiveService.f77se698jgrs;
    }
    
    public static boolean smoothSneakActive() {
        return ImplSpringFovActiveService.fiifwm3gwth6;
    }
    
    public static boolean cameraLeanActive() {
        return ImplSpringFovActiveService.faqgjdcpizc0;
    }
    
    public static float itemSwapClamp() {
        return ImplSpringFovActiveService.f4u47qcmhfos;
    }
    
    public static void resetFovSpring() {
        ImplSpringFovActiveService.f7zurjog6n59.reset();
        ImplSpringFovActiveService.f5nyjj17urim = 0L;
        ImplSpringFovActiveService.f2lv3em3wz1m = 0.0f;
        ImplSpringFovActiveService.fa8sikwe537j = 0L;
    }
    
    public static float advanceFovSpring(final float n) {
        if (!ImplSpringFovActiveService.f77se698jgrs) {
            return n;
        }
        final long nanoTime = System.nanoTime();
        final float m2rdnyoqprb9 = m2rdnyoqprb9(nanoTime, ImplSpringFovActiveService.f5nyjj17urim);
        ImplSpringFovActiveService.f5nyjj17urim = nanoTime;
        return ImplSpringFovActiveService.f7zurjog6n59.step(mc3vlttju681(n, nanoTime), m2rdnyoqprb9, ImplSpringFovActiveService.f6gq1y5kxixi);
    }
    
    public static float advanceEyeHeightSpring(final float n, final float n2) {
        if (!ImplSpringFovActiveService.fiifwm3gwth6) {
            return n2;
        }
        final long nanoTime = System.nanoTime();
        if (!ImplSpringFovActiveService.f9mq4z5090l4.isPrimed()) {
            ImplSpringFovActiveService.f9mq4z5090l4.snap(n);
        }
        return ImplSpringFovActiveService.f9mq4z5090l4.step(n2, mdpv7zwlpnez(nanoTime), ImplSpringFovActiveService.f36q0898x6y1);
    }
    
    private static float mdpv7zwlpnez(final long fbkki0u51ao5) {
        final long fbkki0u51ao6 = ImplSpringFovActiveService.fbkki0u51ao5;
        ImplSpringFovActiveService.fbkki0u51ao5 = fbkki0u51ao5;
        if (fbkki0u51ao6 == 0L) {
            return Float.intBitsToFloat(1028443341);
        }
        return Mth.clamp((fbkki0u51ao5 - fbkki0u51ao6) / Float.intBitsToFloat(1315859240), 0.0f, Float.intBitsToFloat(1048576000));
    }
    
    public static float currentLeanRollRadians() {
        if (!ImplSpringFovActiveService.faqgjdcpizc0) {
            return 0.0f;
        }
        return (float)Math.toRadians(ImplSpringFovActiveService.f6ce5vvcsnxe.position());
    }
    
    @Override
    protected void onEnable() {
        ImplSpringFovActiveService.f6gq1y5kxixi = mcjmfr1ykcqu(this.ff2tyep15w16.get());
        ImplSpringFovActiveService.fujo30fz2gz = this.fg2z42p8xbz8.get();
        ImplSpringFovActiveService.f77se698jgrs = this.fgcc7vohlfgj.get();
        ImplSpringFovActiveService.fiifwm3gwth6 = this.fbkvvb9rjyfl.get();
        ImplSpringFovActiveService.faqgjdcpizc0 = this.fh6ppn45kuic.get();
        ImplSpringFovActiveService.f4u47qcmhfos = (this.fcpjs2hrg7kw.get() ? this.fdww15gx8n5a.get() : Float.intBitsToFloat(1053609165));
        ImplSpringFovActiveService.f7zurjog6n59.reset();
        ImplSpringFovActiveService.f9mq4z5090l4.reset();
        ImplSpringFovActiveService.f6ce5vvcsnxe.reset();
        ImplSpringFovActiveService.f7co66lorpu6 = 0.0f;
        ImplSpringFovActiveService.f2lv3em3wz1m = 0.0f;
        ImplSpringFovActiveService.f5nyjj17urim = 0L;
        ImplSpringFovActiveService.fbkki0u51ao5 = 0L;
        ImplSpringFovActiveService.fa8sikwe537j = 0L;
        this.fbuw1p20c99c = 0L;
        this.f9bu877ta3cz = 0L;
        this.f9txone2im8l = Float.intBitsToFloat(2143289344);
        this.f9mmeg5i8etp = Float.intBitsToFloat(2143289344);
        this.on(EventAttackInputService.PACKET).filter(EventAttackInputService.Packet::isIncoming).run(this::megqvh1ocojp);
        this.on(EventAttackInputService.RENDER).run(this::m72aqbnpdflx);
    }
    
    @Override
    protected void onDisable() {
        ImplSpringFovActiveService.f77se698jgrs = false;
        ImplSpringFovActiveService.fiifwm3gwth6 = false;
        ImplSpringFovActiveService.faqgjdcpizc0 = false;
        ImplSpringFovActiveService.f4u47qcmhfos = Float.intBitsToFloat(1053609165);
        ImplSpringFovActiveService.f7zurjog6n59.reset();
        ImplSpringFovActiveService.f9mq4z5090l4.reset();
        ImplSpringFovActiveService.f6ce5vvcsnxe.reset();
        ImplSpringFovActiveService.f2lv3em3wz1m = 0.0f;
        ImplSpringFovActiveService.f7co66lorpu6 = 0.0f;
    }
    
    private void megqvh1ocojp(final EventAttackInputService.Packet packet) {
        final Packet<?> packet2 = packet.packet();
        if (!(packet2 instanceof ClientboundSetHealthPacket)) {
            return;
        }
        final float health = ((ClientboundSetHealthPacket)packet2).getHealth();
        final float f9mmeg5i8etp = this.f9mmeg5i8etp;
        this.f9mmeg5i8etp = health;
        if (Float.isNaN(f9mmeg5i8etp) || health >= f9mmeg5i8etp - Float.intBitsToFloat(1008981770)) {
            return;
        }
        final float clamp = Mth.clamp((f9mmeg5i8etp - health) / Float.intBitsToFloat(1082130432), Float.intBitsToFloat(1056964608), Float.intBitsToFloat(1069547520));
        if (this.f8wvfny614q3.get()) {
            ImplSpringFovActiveService.f2lv3em3wz1m = Math.max(ImplSpringFovActiveService.f2lv3em3wz1m, this.fb7uv1fzob6d.get() * clamp);
        }
        if (this.fbd41oil776j.get()) {
            ImplSpringFovActiveService.f7co66lorpu6 = Math.min(1.0f, Math.max(ImplSpringFovActiveService.f7co66lorpu6, clamp));
        }
    }
    
    private void m72aqbnpdflx(final EventAttackInputService.Render render) {
        final Minecraft mc = CoreIsInitializedHandler.mc();
        final LocalPlayer player = mc.player;
        if (player == null || mc.level == null) {
            this.f9mmeg5i8etp = Float.intBitsToFloat(2143289344);
            this.f9txone2im8l = Float.intBitsToFloat(2143289344);
            ImplSpringFovActiveService.f7co66lorpu6 = 0.0f;
            ImplSpringFovActiveService.f2lv3em3wz1m = 0.0f;
            ImplSpringFovActiveService.f6ce5vvcsnxe.reset();
            return;
        }
        final long nanoTime = System.nanoTime();
        final float m2rdnyoqprb9 = m2rdnyoqprb9(nanoTime, this.fbuw1p20c99c);
        this.fbuw1p20c99c = nanoTime;
        if (ImplSpringFovActiveService.faqgjdcpizc0) {
            final float yRot = player.getYRot();
            final long f9bu877ta3cz = nanoTime;
            final long f9bu877ta3cz2 = this.f9bu877ta3cz;
            this.f9bu877ta3cz = f9bu877ta3cz;
            float n = 0.0f;
            if (!Float.isNaN(this.f9txone2im8l) && f9bu877ta3cz2 != 0L) {
                final float n2 = (f9bu877ta3cz - f9bu877ta3cz2) / Float.intBitsToFloat(1315859240);
                if (n2 > Float.intBitsToFloat(981668463) && n2 < Float.intBitsToFloat(1048576000)) {
                    n = Mth.clamp(Mth.wrapDegrees(yRot - this.f9txone2im8l) / n2 / Float.intBitsToFloat(1135869952), Float.intBitsToFloat(-1082130432), 1.0f) * ImplSpringFovActiveService.fujo30fz2gz;
                }
            }
            this.f9txone2im8l = yRot;
            ImplSpringFovActiveService.f6ce5vvcsnxe.step(n, m2rdnyoqprb9, ImplSpringFovActiveService.fcss11geckm9);
        }
        if (ImplSpringFovActiveService.f7co66lorpu6 > Float.intBitsToFloat(981668463)) {
            ImplSpringFovActiveService.f7co66lorpu6 *= (float)Math.exp(Float.intBitsToFloat(-1065353216) * m2rdnyoqprb9);
            if (ImplSpringFovActiveService.f7co66lorpu6 < Float.intBitsToFloat(1008981770)) {
                ImplSpringFovActiveService.f7co66lorpu6 = 0.0f;
            }
        }
        if (mc.screen instanceof CoreSetBehindScreenService) {
            return;
        }
        final CompositorPushPresentationScaleService compositor = CoreIsInitializedHandler.get().compositor();
        if (this.fbd41oil776j.get() && ImplSpringFovActiveService.f7co66lorpu6 > Float.intBitsToFloat(981668463)) {
            final float n3 = (float)CoreIsInitializedHandler.get().viewport().width();
            final float n4 = (float)CoreIsInitializedHandler.get().viewport().height();
            final int round = Math.round(ImplSpringFovActiveService.f7co66lorpu6 * this.ffe7hrd52hp5.get() * Float.intBitsToFloat(1132396544));
            if (round > 0) {
                compositor.roundedRect(0.0f, 0.0f, n3, n4, 0.0f, round << 24 | 0xC81E1E);
            }
        }
    }
    
    private static float m2rdnyoqprb9(final long n, final long n2) {
        if (n2 == 0L) {
            return Float.intBitsToFloat(1015580809);
        }
        return Mth.clamp((n - n2) / Float.intBitsToFloat(1315859240), 0.0f, Float.intBitsToFloat(1036831949));
    }
    
    private static float mc3vlttju681(final float n, final long n2) {
        if (ImplSpringFovActiveService.f2lv3em3wz1m <= Float.intBitsToFloat(953267991)) {
            ImplSpringFovActiveService.fa8sikwe537j = n2;
            return n;
        }
        final long fa8sikwe537j = ImplSpringFovActiveService.fa8sikwe537j;
        ImplSpringFovActiveService.fa8sikwe537j = n2;
        if (fa8sikwe537j != 0L) {
            final float n3 = (n2 - fa8sikwe537j) / Float.intBitsToFloat(1315859240);
            if (n3 > 0.0f && n3 < Float.intBitsToFloat(1056964608)) {
                ImplSpringFovActiveService.f2lv3em3wz1m *= (float)Math.exp(Float.intBitsToFloat(-1056964608) * n3);
                if (ImplSpringFovActiveService.f2lv3em3wz1m < Float.intBitsToFloat(1028443341)) {
                    ImplSpringFovActiveService.f2lv3em3wz1m = 0.0f;
                }
            }
        }
        return n - ImplSpringFovActiveService.f2lv3em3wz1m;
    }
    
    static {
        f7zurjog6n59 = new UtilIsPrimedService();
        f9mq4z5090l4 = new UtilIsPrimedService();
        f6ce5vvcsnxe = new UtilIsPrimedService();
        ImplSpringFovActiveService.f4u47qcmhfos = Float.intBitsToFloat(1053609165);
        ImplSpringFovActiveService.f6gq1y5kxixi = SceneEaseHandler.Spring.SNAPPY;
        f36q0898x6y1 = new SceneEaseHandler.Spring(Float.intBitsToFloat(1101004800), Float.intBitsToFloat(1130102784));
        fcss11geckm9 = SceneEaseHandler.Spring.GENTLE;
    }
}
