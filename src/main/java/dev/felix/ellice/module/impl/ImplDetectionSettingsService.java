







package dev.felix.ellice.module.impl;

import dev.felix.ellice.compat.CompatDecisionTracker;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.event.EventIsAfterHandler;
import dev.felix.ellice.feature.antibot.AntibotDecisionTracker;
import dev.felix.ellice.feature.antibot.AntibotFeatureType;
import dev.felix.ellice.hud.HudRenderer;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleNameService;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.ui.component.ComponentMountService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public final class ImplDetectionSettingsService
extends ModuleSettingsService {
    private final ModuleNameService f69dx2h1qulc = this.settingCategory("Behavior").description("Combines independent evidence over time. Observe lets you inspect decisions before filtering targets.");
    private final ModuleNameService fi1e360cb3rk = this.settingCategory("Detection").description("Missing skins, zero ping, armor, invisibility and UUID versions are not evidence.");
    private final ModuleNameService fn5glclyump = this.settingCategory("Timing").description("Spawn grace, confirmation and release prevent one-tick decisions and flickering targets.");
    private final ModuleNameService f5khkd59i7ww = this.settingCategory("Exceptions").description("Exact player names or UUIDs, separated by commas. Allow takes priority; UUIDs avoid name collisions.");
    private final ModuleNameService f4ndqbt405it = this.settingCategory("Display");
    private final ModuleSetting.Mode f3m7wqpduy1g = this.setting(this.f69dx2h1qulc, (ModuleSetting.Mode)new ModuleSetting.Mode("Mode", new String[]{"Filter", "Observe"}, "Filter").description("Observe reports what would be filtered and leaves every target available."));
    private final ModuleSetting.MultiSelect f7s76b692tsg = this.setting(this.f69dx2h1qulc, (ModuleSetting.MultiSelect)new ModuleSetting.MultiSelect("Affected Modules", AntibotFeatureType.labels(), AntibotFeatureType.combatDefaults()).description("Filters automated targets. Optional visual filters keep entity collisions and ray occlusion intact."));
    private final ModuleSetting.Number f7z8ia52vybn = this.setting(this.fi1e360cb3rk, (ModuleSetting.Number)new ModuleSetting.Number("Evidence Threshold", Float.intBitsToFloat(1115815936), Float.intBitsToFloat(1106247680), Float.intBitsToFloat(1120403456), Float.intBitsToFloat(0x40A00000)).description("Evidence score, not a probability. Lower values react to weaker combinations."));
    private final ModuleSetting.Number f35akal8fxz0 = this.setting(this.fi1e360cb3rk, (ModuleSetting.Number)new ModuleSetting.Number("Minimum Evidence Families", 2.0f, 1.0f, Float.intBitsToFloat(0x40800000), 1.0f).description("Correlated clues count once per family. A second entity using your own UUID is a structural exception."));
    private final ModuleSetting.Mode fctoa86h1qu1 = this.setting(this.fi1e360cb3rk, (ModuleSetting.Mode)new ModuleSetting.Mode("Tab Presence", new String[]{"Auto", "Required", "Off"}, "Auto").description("Auto uses profile presence only when your own profile exists. An unlisted player is a weak clue; it never suffices on its own."));
    private final ModuleSetting.Bool f8m4mwk1rlga = this.setting(this.fi1e360cb3rk, (ModuleSetting.Bool)new ModuleSetting.Bool("Identity Conflicts", true).description("Profile/name conflicts, duplicate names without matching profiles and copies of your UUID."));
    private final ModuleSetting.Bool f61nrhs4zwjf = this.setting(this.fi1e360cb3rk, new ModuleSetting.Bool("NPC Name Markers", true));
    private final ModuleSetting.Text fc4dhtr6el64 = this.setting(this.fi1e360cb3rk, (ModuleSetting.Text)new ModuleSetting.Text("NPC Markers", "[NPC], [SHOP], [BOT]", 512).description("Case-insensitive literal fragments in display names, separated by commas. A marker alone is insufficient in Balanced."));
    private final ModuleSetting.Bool f5fexmvdvbla = this.setting(this.fi1e360cb3rk, (ModuleSetting.Bool)new ModuleSetting.Bool("Attached Motion", true).description("Looks for an almost fixed world or camera offset through sustained turns. Pauses after corrections and during unreliable movement."));
    private final ModuleSetting.Bool foguis6v39k = this.setting(this.fi1e360cb3rk, (ModuleSetting.Bool)new ModuleSetting.Bool("Spawn Churn", true).description("Several nearby incarnations of one UUID in six seconds without an observed death; requires corroboration."));
    private final ModuleSetting.Bool f3ldypefcwgb = this.setting(this.fi1e360cb3rk, (ModuleSetting.Bool)new ModuleSetting.Bool("Invalid Entity Data", true).description("Non-finite values, invalid bounding boxes and pitch outside the player range. No assumptions about armor or flight."));
    private final ModuleSetting.Number f3w7m5ecsox = this.setting(this.fn5glclyump, new ModuleSetting.Number("Spawn Grace (ms)", Float.intBitsToFloat(1140457472), 0.0f, Float.intBitsToFloat(1161527296), Float.intBitsToFloat(1112014848)));
    private final ModuleSetting.Bool fjtos77zesx = this.setting(this.fn5glclyump, (ModuleSetting.Bool)new ModuleSetting.Bool("Hold Suspicious Spawns", true).description("Temporarily skips new targets with a strong initial clue during spawn grace. Ordinary profile-backed joins stay targetable."));
    private final ModuleSetting.Number f45pbfzzl4ut = this.setting(this.fn5glclyump, new ModuleSetting.Number("Confirmation (ms)", Float.intBitsToFloat(1132068864), 0.0f, Float.intBitsToFloat(1157234688), Float.intBitsToFloat(1112014848)));
    private final ModuleSetting.Number fdnqqxohw69y = this.setting(this.fn5glclyump, new ModuleSetting.Number("Release Delay (ms)", Float.intBitsToFloat(1144750080), 0.0f, Float.intBitsToFloat(1161527296), Float.intBitsToFloat(1112014848)));
    private final ModuleSetting.Number f5tebygmxtn4 = this.setting(this.fn5glclyump, (ModuleSetting.Number)new ModuleSetting.Number("History (seconds)", Float.intBitsToFloat(1106247680), Float.intBitsToFloat(0x40A00000), Float.intBitsToFloat(1123024896), Float.intBitsToFloat(0x40A00000)).description("Retains short-lived identities within a bounded history. World changes and disabling clear all observations."));
    private final ModuleSetting.Text femr9qsqe39i = this.setting(this.f5khkd59i7ww, new ModuleSetting.Text("Allow Players", "", 4096));
    private final ModuleSetting.Text fb28d2re5njb = this.setting(this.f5khkd59i7ww, new ModuleSetting.Text("Block Players", "", 4096));
    private final ModuleSetting.Mode fgqm0051ur4v = this.setting(this.f4ndqbt405it, (ModuleSetting.Mode)new ModuleSetting.Mode("Status Display", new String[]{"Summary", "Inspect", "Off"}, "Summary").description("Inspect explains the player under your crosshair. Summary counts filtered and temporarily held targets."));
    private final ModuleSetting.Number f2a4floscv3b = this.setting(this.f4ndqbt405it, new ModuleSetting.Number("Status Offset", Float.intBitsToFloat(1120403456), Float.intBitsToFloat(-1018691584), Float.intBitsToFloat(1133903872), 2.0f));
    private HudRenderer f8ejqh9ylpor;
    private ComponentMountService fb3j0q9oiuf;

    public ImplDetectionSettingsService() {
        super(ModuleBuilderData.builder("AntiBot").category(ModuleFeatureType.COMBAT).description("Filters suspected fake players using corroborated identity, motion and spawn evidence, with reasons and manual exceptions.").build());
        this.fc4dhtr6el64.visibleWhen(this.f61nrhs4zwjf);
        this.f2a4floscv3b.visibleWhen(this.fgqm0051ur4v, string -> !string.equals("Off"));
        this.fjtos77zesx.visibleWhen(this.f3w7m5ecsox, f -> f.floatValue() > 0.0f);
    }

    public AntibotDecisionTracker.Settings detectionSettings() {
        return new AntibotDecisionTracker.Settings(((Float)this.f7z8ia52vybn.get()).floatValue(), ((Float)this.f35akal8fxz0.get()).intValue(), ((Float)this.f3w7m5ecsox.get()).longValue(), ((Float)this.f45pbfzzl4ut.get()).longValue(), ((Float)this.fdnqqxohw69y.get()).longValue(), ((Float)this.f5tebygmxtn4.get()).longValue() * 1000L, (String)this.fctoa86h1qu1.get(), (Boolean)this.f8m4mwk1rlga.get(), (Boolean)this.f61nrhs4zwjf.get(), (Boolean)this.f5fexmvdvbla.get(), (Boolean)this.foguis6v39k.get(), (Boolean)this.f3ldypefcwgb.get(), (Boolean)this.fjtos77zesx.get(), ImplDetectionSettingsService.entries((String)this.fc4dhtr6el64.get()), ImplDetectionSettingsService.entries((String)this.femr9qsqe39i.get()), ImplDetectionSettingsService.entries((String)this.fb28d2re5njb.get()));
    }

    public boolean observing() {
        return ((String)this.f3m7wqpduy1g.get()).equals("Observe");
    }

    public boolean filters(AntibotFeatureType antibotFeatureType) {
        return (!this.observing() && ((Set)this.f7s76b692tsg.get()).contains(antibotFeatureType.label()) ? 1 : 0) != 0;
    }

    public static Set<String> entries(String string2) {
        return Arrays.stream(string2.split("[,;\\n\\r]")).map(String::strip).filter(string -> !string.isEmpty()).map(string -> string.toLowerCase(Locale.ROOT)).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    protected void onEnable() {
        CompatDecisionTracker.enable(this);
        this.on(EventAttackInputService.TICK).priority(EventIsAfterHandler.Priority.FIRST).run(tick -> CompatDecisionTracker.tick(this));
        this.on(EventAttackInputService.WORLD).run(world -> CompatDecisionTracker.reset());
        this.on(EventAttackInputService.PLAYER_POSITION_CORRECTED).run(playerPositionCorrected -> CompatDecisionTracker.corrected());
        this.f8ejqh9ylpor = new HudRenderer();
        this.fb3j0q9oiuf = new ComponentMountService().mount(this.f8ejqh9ylpor, CoreIsInitializedHandler.get().theme());
        ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)this.fb3j0q9oiuf.id("antibot.status")).absolute()).size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.px(Float.intBitsToFloat(1107820544)))).pointerEvents(false)).visible(false);
        CoreIsInitializedHandler.get().scene().root().addChild(this.fb3j0q9oiuf);
        this.on(EventAttackInputService.RENDER).run(render -> this.m52l7mxz0yhm());
    }

    @Override
    protected void onDisable() {
        CompatDecisionTracker.disable(this);
        if (this.fb3j0q9oiuf != null) {
            CoreIsInitializedHandler.get().scene().root().removeChild(this.fb3j0q9oiuf);
        }
        this.fb3j0q9oiuf = null;
        this.f8ejqh9ylpor = null;
    }

    private void m52l7mxz0yhm() {
        Optional<AntibotDecisionTracker.Decision> optional;
        HitResult hitResult;
        String string;
        CoreIsInitializedHandler coreIsInitializedHandler = CoreIsInitializedHandler.get();
        Minecraft minecraft = CoreIsInitializedHandler.mc();
        int n = !((String)this.fgqm0051ur4v.get()).equals("Off") && minecraft.player != null && minecraft.level != null && minecraft.screen == null && !minecraft.options.hideGui && !coreIsInitializedHandler.screens().isActive() ? 1 : 0;
        this.fb3j0q9oiuf.visible(n != 0);
        if (n == 0) {
            return;
        }
        this.fb3j0q9oiuf.position(0.0f, (float)(coreIsInitializedHandler.viewport().height() / 2) + ((Float)this.f2a4floscv3b.get()).floatValue());
        List<AntibotDecisionTracker.Decision> list = CompatDecisionTracker.decisions();
        long l = list.stream().filter(AntibotDecisionTracker.Decision::filtered).count();
        long l2 = list.stream().filter(AntibotDecisionTracker.Decision::hold).count();
        String string2 = "AntiBot \u00b7 " + l + (this.observing() ? " would filter" : " filtered") + " \u00b7 " + l2 + " observing";
        String string3 = string = list.isEmpty() ? "Waiting for nearby players" : list.size() + " players checked \u00b7 aim at a player in Inspect mode";
        if (((String)this.fgqm0051ur4v.get()).equals("Inspect") && (hitResult = minecraft.hitResult) instanceof EntityHitResult) {
            EntityHitResult entityHitResult = (EntityHitResult)hitResult;
            optional = CompatDecisionTracker.decision(entityHitResult.getEntity());
        } else {
            optional = Optional.empty();
        }
        if (optional.isPresent()) {
            AntibotDecisionTracker.Decision decision = optional.get();
            string2 = "AntiBot \u00b7 " + decision.name() + " \u00b7 " + this.meevo42qawqp(decision) + " \u00b7 evidence " + Math.round(decision.score()) + "/100";
            string = decision.explanation();
        } else if (((String)this.fgqm0051ur4v.get()).equals("Inspect")) {
            string = "Aim at a player to inspect the evidence";
        } else if (l > 0L) {
            string = list.stream().filter(AntibotDecisionTracker.Decision::filtered).max(Comparator.comparingDouble(AntibotDecisionTracker.Decision::score)).map(decision -> decision.name() + " \u00b7 " + decision.explanation()).orElse(string);
        }
        this.f8ejqh9ylpor.update(string2, string, optional.map(AntibotDecisionTracker.Decision::exclude).orElse(l > 0L));
    }

    private String meevo42qawqp(AntibotDecisionTracker.Decision decision) {
        if (this.observing() && decision.exclude()) {
            return "would skip";
        }
        return switch (decision.state()) {
            default -> throw new MatchException(null, null);
            case AntibotDecisionTracker.State.FILTERED -> "filtered";
            case AntibotDecisionTracker.State.BLOCKED -> "blocked by list";
            case AntibotDecisionTracker.State.ALLOWED -> "allowed by list";
            case AntibotDecisionTracker.State.OBSERVING -> {
                if (decision.hold()) {
                    yield "spawn grace";
                }
                yield "observing";
            }
            case AntibotDecisionTracker.State.SUSPECT -> "weak evidence";
            case AntibotDecisionTracker.State.CLEAR -> "clear";
        };
    }
}
