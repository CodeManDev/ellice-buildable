package dev.felix.ellice.module.impl;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.core.CoreSetBehindScreenService;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleNameService;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.util.UtilIsPrimedService;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.util.Mth;

public final class ImplSpringFovActiveService extends ModuleSettingsService {
  private final ModuleNameService f1v43asr8kz1;
  private final ModuleNameService f4cx525a723g;
  private final ModuleNameService fd2pslralfoz;
  private final ModuleNameService f5vgidatqoc4;
  private final ModuleSetting.Bool fgcc7vohlfgj;
  private final ModuleSetting.Mode ff2tyep15w16;
  private final ModuleSetting.Bool f8wvfny614q3;
  private final ModuleSetting.Number fb7uv1fzob6d;
  private final ModuleSetting.Bool moduleBool3;
  private final ModuleSetting.Number moduleNumber2;
  private final ModuleSetting.Bool fbkvvb9rjyfl;
  private final ModuleSetting.Bool fh6ppn45kuic;
  private final ModuleSetting.Number fg2z42p8xbz8;
  private final ModuleSetting.Bool fcpjs2hrg7kw;
  private final ModuleSetting.Number fdww15gx8n5a;
  private static final UtilIsPrimedService f7zurjog6n59;
  private static final UtilIsPrimedService f9mq4z5090l4;
  private static final UtilIsPrimedService f6ce5vvcsnxe;
  private static volatile boolean enabled;
  private static volatile boolean enabled2;
  private static volatile boolean faqgjdcpizc0;
  private static volatile float fujo30fz2gz;
  private static volatile float f4u47qcmhfos;
  private static volatile SceneEaseHandler.Spring f6gq1y5kxixi;
  private static final SceneEaseHandler.Spring f36q0898x6y1;
  private static final SceneEaseHandler.Spring fcss11geckm9;
  private static volatile float value3;
  private static final float value4 = 8.0f;
  private static volatile float value5;
  private static final float value6 = 4.0f;
  private static long timestamp;
  private static long currentLongValue;
  private float f9txone2im8l;
  private long timestamp3;
  private float f9mmeg5i8etp;
  private long timestamp4;
  private static long timestamp5;

  public ImplSpringFovActiveService() {
    super(
        ModuleBuilderData.builder("Kinetics")
            .description("Smooths FOV, crouching, camera lean, item swaps, and damage feedback.")
            .category(ModuleFeatureType.VISUALS)
            .build());
    this.f1v43asr8kz1 = this.settingCategory("Field of view");
    this.f4cx525a723g = this.settingCategory("Damage feedback");
    this.fd2pslralfoz = this.settingCategory("Camera");
    this.f5vgidatqoc4 = this.settingCategory("Held item");
    this.fgcc7vohlfgj =
        this.setting(
            this.f1v43asr8kz1,
            (ModuleSetting.Bool)
                new ModuleSetting.Bool("Spring FOV", true)
                    .description(
                        "Applies a spring to client FOV changes such as sprint and speed"
                            + " transitions; gameplay is unchanged."));
    this.ff2tyep15w16 =
        this.setting(
            this.f1v43asr8kz1,
            (ModuleSetting.Mode)
                new ModuleSetting.Mode(
                        "FOV Feel", new String[] {"Snappy", "Smooth", "Gentle"}, "Snappy")
                    .description(
                        "Selects the FOV spring response, from quick Snappy motion to slower Gentle"
                            + " settling."));
    this.f8wvfny614q3 =
        this.setting(
            this.f4cx525a723g,
            (ModuleSetting.Bool)
                new ModuleSetting.Bool("Hit Punch", true)
                    .description(
                        "Briefly narrows your FOV when you take damage, then lets the FOV spring"
                            + " recover."));
    this.fb7uv1fzob6d =
        this.setting(
            this.f4cx525a723g,
            (ModuleSetting.Number)
                new ModuleSetting.Number(
                        "Punch Kick",
                        Float.intBitsToFloat(1080033280),
                        0.0f,
                        Float.intBitsToFloat(1090519040),
                        Float.intBitsToFloat(1048576000))
                    .description(
                        "Sets the base damage-punch reduction in FOV degrees; incoming damage"
                            + " scales the final kick."));
    this.moduleBool3 =
        this.setting(
            this.f4cx525a723g,
            (ModuleSetting.Bool)
                new ModuleSetting.Bool("Damage Flash", true)
                    .description("Flashes a red full-screen overlay when your health decreases."));
    this.moduleNumber2 =
        this.setting(
            this.f4cx525a723g,
            (ModuleSetting.Number)
                new ModuleSetting.Number(
                        "Flash Alpha",
                        Float.intBitsToFloat(1051931443),
                        0.0f,
                        1.0f,
                        Float.intBitsToFloat(1008981770))
                    .description(
                        "Sets the peak opacity of the red damage flash; 0 makes it invisible."));
    this.fbkvvb9rjyfl =
        this.setting(
            this.fd2pslralfoz,
            (ModuleSetting.Bool)
                new ModuleSetting.Bool("Smooth Sneak", true)
                    .description(
                        "Springs camera eye height between standing and crouching instead of"
                            + " snapping instantly."));
    this.fh6ppn45kuic =
        this.setting(
            this.fd2pslralfoz,
            (ModuleSetting.Bool)
                new ModuleSetting.Bool("Camera Lean", true)
                    .description(
                        "Rolls the camera slightly in the direction of fast yaw turns; player"
                            + " rotation is untouched."));
    this.fg2z42p8xbz8 =
        this.setting(
            this.fd2pslralfoz,
            (ModuleSetting.Number)
                new ModuleSetting.Number(
                        "Lean Amount",
                        Float.intBitsToFloat(1068708659),
                        0.0f,
                        Float.intBitsToFloat(1082130432),
                        Float.intBitsToFloat(1036831949))
                    .description("Sets the maximum camera roll in degrees during a fast turn."));
    this.fcpjs2hrg7kw =
        this.setting(
            this.f5vgidatqoc4,
            (ModuleSetting.Bool)
                new ModuleSetting.Bool("Item Swap Smoothing", true)
                    .description(
                        "Slows the first-person held-item equip animation when changing hotbar"
                            + " items."));
    this.fdww15gx8n5a =
        this.setting(
            this.f5vgidatqoc4,
            (ModuleSetting.Number)
                new ModuleSetting.Number(
                        "Swap Step",
                        Float.intBitsToFloat(1043878380),
                        Float.intBitsToFloat(1028443341),
                        Float.intBitsToFloat(1053609165),
                        Float.intBitsToFloat(1008981770))
                    .description(
                        "Limits equip progress per game tick; lower values animate more slowly and"
                            + " 0.40 matches vanilla."));
    this.f9txone2im8l = Float.intBitsToFloat(2143289344);
    this.f9mmeg5i8etp = Float.intBitsToFloat(2143289344);
    this.ff2tyep15w16.onChange(s -> ImplSpringFovActiveService.f6gq1y5kxixi = mcjmfr1ykcqu(s));
    this.fdww15gx8n5a.onChange(
        n ->
            ImplSpringFovActiveService.f4u47qcmhfos =
                ((this.isEnabled() && this.fcpjs2hrg7kw.get())
                    ? n
                    : Float.intBitsToFloat(1053609165)));
    this.fcpjs2hrg7kw.onChange(
        b ->
            ImplSpringFovActiveService.f4u47qcmhfos =
                ((this.isEnabled() && b)
                    ? this.fdww15gx8n5a.get()
                    : Float.intBitsToFloat(1053609165)));
    this.fg2z42p8xbz8.onChange(n2 -> ImplSpringFovActiveService.fujo30fz2gz = n2);
    this.fbkvvb9rjyfl.onChange(
        b2 -> ImplSpringFovActiveService.enabled2 = (this.isEnabled() && b2));
    this.fh6ppn45kuic.onChange(
        b3 -> ImplSpringFovActiveService.faqgjdcpizc0 = (this.isEnabled() && b3));
    this.fgcc7vohlfgj.onChange(
        b4 -> {
          if (!(ImplSpringFovActiveService.enabled = (this.isEnabled() && b4))) {
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
    return ImplSpringFovActiveService.enabled;
  }

  public static boolean smoothSneakActive() {
    return ImplSpringFovActiveService.enabled2;
  }

  public static boolean cameraLeanActive() {
    return ImplSpringFovActiveService.faqgjdcpizc0;
  }

  public static float itemSwapClamp() {
    return ImplSpringFovActiveService.f4u47qcmhfos;
  }

  public static void resetFovSpring() {
    ImplSpringFovActiveService.f7zurjog6n59.reset();
    ImplSpringFovActiveService.timestamp = 0L;
    ImplSpringFovActiveService.value3 = 0.0f;
    ImplSpringFovActiveService.currentLongValue = 0L;
  }

  public static float advanceFovSpring(final float n) {
    if (!ImplSpringFovActiveService.enabled) {
      return n;
    }
    final long nanoTime = System.nanoTime();
    final float calculateValue2 = calculateValue2(nanoTime, ImplSpringFovActiveService.timestamp);
    ImplSpringFovActiveService.timestamp = nanoTime;
    return ImplSpringFovActiveService.f7zurjog6n59.step(
        calculateValue3(n, nanoTime), calculateValue2, ImplSpringFovActiveService.f6gq1y5kxixi);
  }

  public static float advanceEyeHeightSpring(final float n, final float n2) {
    if (!ImplSpringFovActiveService.enabled2) {
      return n2;
    }
    final long nanoTime = System.nanoTime();
    if (!ImplSpringFovActiveService.f9mq4z5090l4.isPrimed()) {
      ImplSpringFovActiveService.f9mq4z5090l4.snap(n);
    }
    return ImplSpringFovActiveService.f9mq4z5090l4.step(
        n2, mdpv7zwlpnez(nanoTime), ImplSpringFovActiveService.f36q0898x6y1);
  }

  private static float mdpv7zwlpnez(final long timestamp5) {
    final long fbkki0u51ao6 = ImplSpringFovActiveService.timestamp5;
    ImplSpringFovActiveService.timestamp5 = timestamp5;
    if (fbkki0u51ao6 == 0L) {
      return Float.intBitsToFloat(1028443341);
    }
    return Mth.clamp(
        (timestamp5 - fbkki0u51ao6) / Float.intBitsToFloat(1315859240),
        0.0f,
        Float.intBitsToFloat(1048576000));
  }

  public static float currentLeanRollRadians() {
    if (!ImplSpringFovActiveService.faqgjdcpizc0) {
      return 0.0f;
    }
    return (float) Math.toRadians(ImplSpringFovActiveService.f6ce5vvcsnxe.position());
  }

  @Override
  protected void onEnable() {
    ImplSpringFovActiveService.f6gq1y5kxixi = mcjmfr1ykcqu(this.ff2tyep15w16.get());
    ImplSpringFovActiveService.fujo30fz2gz = this.fg2z42p8xbz8.get();
    ImplSpringFovActiveService.enabled = this.fgcc7vohlfgj.get();
    ImplSpringFovActiveService.enabled2 = this.fbkvvb9rjyfl.get();
    ImplSpringFovActiveService.faqgjdcpizc0 = this.fh6ppn45kuic.get();
    ImplSpringFovActiveService.f4u47qcmhfos =
        (this.fcpjs2hrg7kw.get() ? this.fdww15gx8n5a.get() : Float.intBitsToFloat(1053609165));
    ImplSpringFovActiveService.f7zurjog6n59.reset();
    ImplSpringFovActiveService.f9mq4z5090l4.reset();
    ImplSpringFovActiveService.f6ce5vvcsnxe.reset();
    ImplSpringFovActiveService.value5 = 0.0f;
    ImplSpringFovActiveService.value3 = 0.0f;
    ImplSpringFovActiveService.timestamp = 0L;
    ImplSpringFovActiveService.timestamp5 = 0L;
    ImplSpringFovActiveService.currentLongValue = 0L;
    this.timestamp4 = 0L;
    this.timestamp3 = 0L;
    this.f9txone2im8l = Float.intBitsToFloat(2143289344);
    this.f9mmeg5i8etp = Float.intBitsToFloat(2143289344);
    this.on(EventAttackInputService.PACKET)
        .filter(EventAttackInputService.Packet::isIncoming)
        .run(this::megqvh1ocojp);
    this.on(EventAttackInputService.RENDER).run(this::updateState2);
  }

  @Override
  protected void onDisable() {
    ImplSpringFovActiveService.enabled = false;
    ImplSpringFovActiveService.enabled2 = false;
    ImplSpringFovActiveService.faqgjdcpizc0 = false;
    ImplSpringFovActiveService.f4u47qcmhfos = Float.intBitsToFloat(1053609165);
    ImplSpringFovActiveService.f7zurjog6n59.reset();
    ImplSpringFovActiveService.f9mq4z5090l4.reset();
    ImplSpringFovActiveService.f6ce5vvcsnxe.reset();
    ImplSpringFovActiveService.value3 = 0.0f;
    ImplSpringFovActiveService.value5 = 0.0f;
  }

  private void megqvh1ocojp(final EventAttackInputService.Packet packet) {
    final Packet<?> packet2 = packet.packet();
    if (!(packet2 instanceof ClientboundSetHealthPacket)) {
      return;
    }
    final float health = ((ClientboundSetHealthPacket) packet2).getHealth();
    final float f9mmeg5i8etp = this.f9mmeg5i8etp;
    this.f9mmeg5i8etp = health;
    if (Float.isNaN(f9mmeg5i8etp) || health >= f9mmeg5i8etp - Float.intBitsToFloat(1008981770)) {
      return;
    }
    final float clamp =
        Mth.clamp(
            (f9mmeg5i8etp - health) / Float.intBitsToFloat(1082130432),
            Float.intBitsToFloat(1056964608),
            Float.intBitsToFloat(1069547520));
    if (this.f8wvfny614q3.get()) {
      ImplSpringFovActiveService.value3 =
          Math.max(ImplSpringFovActiveService.value3, this.fb7uv1fzob6d.get() * clamp);
    }
    if (this.moduleBool3.get()) {
      ImplSpringFovActiveService.value5 =
          Math.min(1.0f, Math.max(ImplSpringFovActiveService.value5, clamp));
    }
  }

  private void updateState2(final EventAttackInputService.Render render) {
    final Minecraft mc = CoreIsInitializedHandler.mc();
    final LocalPlayer player = mc.player;
    if (player == null || mc.level == null) {
      this.f9mmeg5i8etp = Float.intBitsToFloat(2143289344);
      this.f9txone2im8l = Float.intBitsToFloat(2143289344);
      ImplSpringFovActiveService.value5 = 0.0f;
      ImplSpringFovActiveService.value3 = 0.0f;
      ImplSpringFovActiveService.f6ce5vvcsnxe.reset();
      return;
    }
    final long nanoTime = System.nanoTime();
    final float calculateValue2 = calculateValue2(nanoTime, this.timestamp4);
    this.timestamp4 = nanoTime;
    if (ImplSpringFovActiveService.faqgjdcpizc0) {
      final float yRot = player.getYRot();
      final long timestamp3 = nanoTime;
      final long nextOffset = this.timestamp3;
      this.timestamp3 = timestamp3;
      float n = 0.0f;
      if (!Float.isNaN(this.f9txone2im8l) && nextOffset != 0L) {
        final float n2 = (timestamp3 - nextOffset) / Float.intBitsToFloat(1315859240);
        if (n2 > Float.intBitsToFloat(981668463) && n2 < Float.intBitsToFloat(1048576000)) {
          n =
              Mth.clamp(
                      Mth.wrapDegrees(yRot - this.f9txone2im8l)
                          / n2
                          / Float.intBitsToFloat(1135869952),
                      Float.intBitsToFloat(-1082130432),
                      1.0f)
                  * ImplSpringFovActiveService.fujo30fz2gz;
        }
      }
      this.f9txone2im8l = yRot;
      ImplSpringFovActiveService.f6ce5vvcsnxe.step(
          n, calculateValue2, ImplSpringFovActiveService.fcss11geckm9);
    }
    if (ImplSpringFovActiveService.value5 > Float.intBitsToFloat(981668463)) {
      ImplSpringFovActiveService.value5 *=
          (float) Math.exp(Float.intBitsToFloat(-1065353216) * calculateValue2);
      if (ImplSpringFovActiveService.value5 < Float.intBitsToFloat(1008981770)) {
        ImplSpringFovActiveService.value5 = 0.0f;
      }
    }
    if (mc.screen instanceof CoreSetBehindScreenService) {
      return;
    }
    final CompositorPushPresentationScaleService compositor =
        CoreIsInitializedHandler.get().compositor();
    if (this.moduleBool3.get()
        && ImplSpringFovActiveService.value5 > Float.intBitsToFloat(981668463)) {
      final float n3 = (float) CoreIsInitializedHandler.get().viewport().width();
      final float n4 = (float) CoreIsInitializedHandler.get().viewport().height();
      final int round =
          Math.round(
              ImplSpringFovActiveService.value5
                  * this.moduleNumber2.get()
                  * Float.intBitsToFloat(1132396544));
      if (round > 0) {
        compositor.roundedRect(0.0f, 0.0f, n3, n4, 0.0f, round << 24 | 0xC81E1E);
      }
    }
  }

  private static float calculateValue2(final long n, final long n2) {
    if (n2 == 0L) {
      return Float.intBitsToFloat(1015580809);
    }
    return Mth.clamp(
        (n - n2) / Float.intBitsToFloat(1315859240), 0.0f, Float.intBitsToFloat(1036831949));
  }

  private static float calculateValue3(final float n, final long n2) {
    if (ImplSpringFovActiveService.value3 <= Float.intBitsToFloat(953267991)) {
      ImplSpringFovActiveService.currentLongValue = n2;
      return n;
    }
    final long currentLongValue = ImplSpringFovActiveService.currentLongValue;
    ImplSpringFovActiveService.currentLongValue = n2;
    if (currentLongValue != 0L) {
      final float n3 = (n2 - currentLongValue) / Float.intBitsToFloat(1315859240);
      if (n3 > 0.0f && n3 < Float.intBitsToFloat(1056964608)) {
        ImplSpringFovActiveService.value3 *=
            (float) Math.exp(Float.intBitsToFloat(-1056964608) * n3);
        if (ImplSpringFovActiveService.value3 < Float.intBitsToFloat(1028443341)) {
          ImplSpringFovActiveService.value3 = 0.0f;
        }
      }
    }
    return n - ImplSpringFovActiveService.value3;
  }

  static {
    f7zurjog6n59 = new UtilIsPrimedService();
    f9mq4z5090l4 = new UtilIsPrimedService();
    f6ce5vvcsnxe = new UtilIsPrimedService();
    ImplSpringFovActiveService.f4u47qcmhfos = Float.intBitsToFloat(1053609165);
    ImplSpringFovActiveService.f6gq1y5kxixi = SceneEaseHandler.Spring.SNAPPY;
    f36q0898x6y1 =
        new SceneEaseHandler.Spring(
            Float.intBitsToFloat(1101004800), Float.intBitsToFloat(1130102784));
    fcss11geckm9 = SceneEaseHandler.Spring.GENTLE;
  }
}
