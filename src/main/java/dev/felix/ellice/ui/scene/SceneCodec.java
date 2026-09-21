


package dev.felix.ellice.ui.scene;

import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;

public final class SceneCodec {
    private static final MotionFiniteService feip9jjw007v = MotionColorsContainer.Floats.TRANSLATE_X;
    private static final MotionFiniteService f1b33szqhwfj = MotionColorsContainer.Floats.TRANSLATE_Y;
    private static final MotionFiniteService fd7h9ms1y6s0 = MotionColorsContainer.Floats.SCALE;
    private static final MotionFiniteService fekx17u5nzbn = MotionColorsContainer.Floats.OPACITY;
    private static final MotionFiniteService ftxbgif29y4 = MotionColorsContainer.Floats.EDGE_SOFTNESS;
    private static final float fi1qvjrp6i9n = 1.0E-5f;
    private final Map<ScenePctService<?>, State> fgtj05rfwah8 = new IdentityHashMap();

    public void transition(ScenePctService<?> scenePctService, Action action, Preset preset, Options options) {
        Action action2;
        if (scenePctService == null) {
            return;
        }
        Action action3 = action2 = action != null ? action : Action.SHOW;
        if (action2 == Action.TOGGLE) {
            State state = this.fgtj05rfwah8.get(scenePctService);
            if (state != null) {
                action2 = state.phase == Phase.ENTERING ? Action.HIDE : Action.SHOW;
            } else {
                Action action4 = action2 = scenePctService.visible ? Action.HIDE : Action.SHOW;
            }
        }
        if (action2 == Action.SHOW) {
            this.show(scenePctService, preset, options);
        } else {
            this.hide(scenePctService, preset, options);
        }
    }

    public void show(ScenePctService<?> scenePctService, Preset preset, Options options) {
        if (scenePctService == null) {
            return;
        }
        Preset preset2 = preset != null ? preset : Preset.POP;
        NormalizedOptions normalizedOptions = SceneCodec.m8wfaa39atxa(preset2, options);
        State state = this.fgtj05rfwah8.get(scenePctService);
        if (preset2 == Preset.NONE) {
            this.mry0h8m3y4n(scenePctService, state, normalizedOptions);
            return;
        }
        if (state == null && scenePctService.visible) {
            return;
        }
        if (state != null && state.phase == Phase.ENTERING) {
            return;
        }
        Spec spec = SceneCodec.mhychxh4x3ah(preset2, normalizedOptions);
        if (state == null) {
            Baseline baseline = new Baseline(feip9jjw007v.get(scenePctService), f1b33szqhwfj.get(scenePctService), normalizedOptions.scale(), normalizedOptions.opacity(), SceneCodec.m8vgq2z7bq8j(scenePctService));
            state = new State(baseline, Phase.ENTERING, spec);
            this.fgtj05rfwah8.put(scenePctService, state);
            this.mcgxhlos0vxs(scenePctService, state, true);
        } else {
            SceneCodec.m8eni77lz33n(scenePctService, state.spec);
            state.phase = Phase.ENTERING;
            state.spec = spec;
            this.mcgxhlos0vxs(scenePctService, state, false);
        }
        this.mcf0uzp7mqmd(scenePctService, state);
    }

    public void hide(ScenePctService<?> scenePctService, Preset preset, Options options) {
        if (scenePctService == null) {
            return;
        }
        Preset preset2 = preset != null ? preset : Preset.POP;
        NormalizedOptions normalizedOptions = SceneCodec.m8wfaa39atxa(preset2, options);
        State state = this.fgtj05rfwah8.get(scenePctService);
        if (preset2 == Preset.NONE) {
            this.m410mvs28zem(scenePctService, state);
            return;
        }
        if (state == null && !scenePctService.visible) {
            return;
        }
        if (state != null && state.phase == Phase.EXITING) {
            return;
        }
        Spec spec = SceneCodec.mhychxh4x3ah(preset2, normalizedOptions);
        if (state == null) {
            Baseline baseline = new Baseline(feip9jjw007v.get(scenePctService), f1b33szqhwfj.get(scenePctService), fd7h9ms1y6s0.get(scenePctService), fekx17u5nzbn.get(scenePctService), SceneCodec.m8vgq2z7bq8j(scenePctService));
            state = new State(baseline, Phase.EXITING, spec);
            this.fgtj05rfwah8.put(scenePctService, state);
        } else {
            SceneCodec.m8eni77lz33n(scenePctService, state.spec);
            state.phase = Phase.EXITING;
            state.spec = spec;
        }
        this.mcvpayld2c8h(scenePctService, state);
        this.mcf0uzp7mqmd(scenePctService, state);
    }

    public void tick(float f) {
        if (!Float.isFinite(f) || f <= 0.0f || this.fgtj05rfwah8.isEmpty()) {
            return;
        }
        Iterator<Map.Entry<ScenePctService<?>, State>> iterator = this.fgtj05rfwah8.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<ScenePctService<?>, State> entry = iterator.next();
            State state = entry.getValue();
            state.remaining -= f;
            if (state.remaining > 0.0f) continue;
            SceneCodec.mbz32xtqscwb(entry.getKey(), state);
            iterator.remove();
        }
    }

    public void clear() {
        for (Map.Entry<ScenePctService<?>, State> entry : this.fgtj05rfwah8.entrySet()) {
            SceneCodec.mbz32xtqscwb(entry.getKey(), entry.getValue());
        }
        this.fgtj05rfwah8.clear();
    }

    public int activeCount() {
        return this.fgtj05rfwah8.size();
    }

    public boolean isTransitioning(ScenePctService<?> scenePctService) {
        return (scenePctService != null && this.fgtj05rfwah8.containsKey(scenePctService) ? 1 : 0) != 0;
    }

    private void mcgxhlos0vxs(ScenePctService<?> scenePctService, State state, boolean bl) {
        Baseline baseline = state.baseline;
        Spec spec = state.spec;
        scenePctService.visible(true);
        if (bl) {
            fekx17u5nzbn.set(scenePctService, 0.0f);
            fd7h9ms1y6s0.set(scenePctService, baseline.scale() * spec.startScaleFactor());
            SceneCodec.m4qxr21qmusk(scenePctService, baseline, spec, spec.offset());
            if (ftxbgif29y4.supports(scenePctService) && spec.startBlur() > 0.0f) {
                ftxbgif29y4.set(scenePctService, baseline.edgeSoftness() + spec.startBlur());
            }
        }
        float f = 0.0f;
        f = Math.max(f, SceneCodec.mf03b46fduno(scenePctService, fekx17u5nzbn, baseline.opacity(), spec.enterOpacityAnim()));
        f = Math.max(f, SceneCodec.mf03b46fduno(scenePctService, fd7h9ms1y6s0, baseline.scale(), spec.enterTransformAnim()));
        if (ftxbgif29y4.supports(scenePctService)) {
            f = Math.max(f, SceneCodec.mf03b46fduno(scenePctService, ftxbgif29y4, baseline.edgeSoftness(), spec.enterEdgeAnim()));
        }
        state.remaining = f = Math.max(f, SceneCodec.m1h92thw646c(scenePctService, baseline, spec));
    }

    private void mcvpayld2c8h(ScenePctService<?> scenePctService, State state) {
        Baseline baseline = state.baseline;
        Spec spec = state.spec;
        scenePctService.visible(true);
        float f = 0.0f;
        f = Math.max(f, SceneCodec.mf03b46fduno(scenePctService, fekx17u5nzbn, 0.0f, spec.exitOpacityAnim()));
        f = Math.max(f, SceneCodec.mf03b46fduno(scenePctService, fd7h9ms1y6s0, baseline.scale() * spec.exitScaleFactor(), spec.exitTransformAnim()));
        if (ftxbgif29y4.supports(scenePctService) && spec.exitBlur() > 0.0f) {
            f = Math.max(f, SceneCodec.mf03b46fduno(scenePctService, ftxbgif29y4, baseline.edgeSoftness() + spec.exitBlur(), spec.exitEdgeAnim()));
        }
        state.remaining = f = Math.max(f, SceneCodec.m79kho1ym98(scenePctService, baseline, spec));
    }

    private void mry0h8m3y4n(ScenePctService<?> scenePctService, State state, NormalizedOptions normalizedOptions) {
        Baseline baseline;
        if (state != null) {
            SceneCodec.m8eni77lz33n(scenePctService, state.spec);
            baseline = state.baseline;
        } else {
            baseline = new Baseline(feip9jjw007v.get(scenePctService), f1b33szqhwfj.get(scenePctService), normalizedOptions.scale(), normalizedOptions.opacity(), SceneCodec.m8vgq2z7bq8j(scenePctService));
        }
        SceneCodec.mj8kwqlys6de(scenePctService, baseline);
        scenePctService.visible(true);
        this.fgtj05rfwah8.remove(scenePctService);
    }

    private void m410mvs28zem(ScenePctService<?> scenePctService, State state) {
        Baseline baseline;
        if (state == null && !scenePctService.visible) {
            return;
        }
        if (state != null) {
            SceneCodec.m8eni77lz33n(scenePctService, state.spec);
            baseline = state.baseline;
        } else {
            baseline = new Baseline(feip9jjw007v.get(scenePctService), f1b33szqhwfj.get(scenePctService), fd7h9ms1y6s0.get(scenePctService), fekx17u5nzbn.get(scenePctService), SceneCodec.m8vgq2z7bq8j(scenePctService));
        }
        SceneCodec.mj8kwqlys6de(scenePctService, baseline);
        scenePctService.visible(false);
        this.fgtj05rfwah8.remove(scenePctService);
    }

    private void mcf0uzp7mqmd(ScenePctService<?> scenePctService, State state) {
        if (state.remaining > 0.0f) {
            return;
        }
        SceneCodec.mbz32xtqscwb(scenePctService, state);
        this.fgtj05rfwah8.remove(scenePctService);
    }

    private static void mbz32xtqscwb(ScenePctService<?> scenePctService, State state) {
        SceneCodec.m8eni77lz33n(scenePctService, state.spec);
        SceneCodec.mj8kwqlys6de(scenePctService, state.baseline);
        scenePctService.visible(state.phase == Phase.ENTERING);
    }

    private static void mj8kwqlys6de(ScenePctService<?> scenePctService, Baseline baseline) {
        feip9jjw007v.set(scenePctService, baseline.translateX());
        f1b33szqhwfj.set(scenePctService, baseline.translateY());
        fd7h9ms1y6s0.set(scenePctService, baseline.scale());
        fekx17u5nzbn.set(scenePctService, baseline.opacity());
        if (ftxbgif29y4.supports(scenePctService)) {
            ftxbgif29y4.set(scenePctService, baseline.edgeSoftness());
        }
    }

    private static float mf03b46fduno(ScenePctService<?> scenePctService, MotionFiniteService motionFiniteService, float f, SceneEaseHandler sceneEaseHandler) {
        if (Math.abs(motionFiniteService.get(scenePctService) - f) <= Float.intBitsToFloat(925353388)) {
            motionFiniteService.set(scenePctService, f);
            return 0.0f;
        }
        MotionAnimateService.animate(scenePctService, motionFiniteService, f, sceneEaseHandler);
        return SceneCodec.miixfzaem44h(sceneEaseHandler);
    }

    private static float m1h92thw646c(ScenePctService<?> scenePctService, Baseline baseline, Spec spec) {
        return switch (spec.axis().ordinal()) {
            default -> throw new MatchException(null, null);
            case 1 -> SceneCodec.mf03b46fduno(scenePctService, feip9jjw007v, baseline.translateX(), spec.enterTransformAnim());
            case 2 -> SceneCodec.mf03b46fduno(scenePctService, f1b33szqhwfj, baseline.translateY(), spec.enterTransformAnim());
            case 0 -> 0.0f;
        };
    }

    private static float m79kho1ym98(ScenePctService<?> scenePctService, Baseline baseline, Spec spec) {
        return switch (spec.axis().ordinal()) {
            default -> throw new MatchException(null, null);
            case 1 -> SceneCodec.mf03b46fduno(scenePctService, feip9jjw007v, baseline.translateX() + (float)spec.sign() * spec.offset(), spec.exitTransformAnim());
            case 2 -> SceneCodec.mf03b46fduno(scenePctService, f1b33szqhwfj, baseline.translateY() + (float)spec.sign() * spec.offset(), spec.exitTransformAnim());
            case 0 -> 0.0f;
        };
    }

    private static void m4qxr21qmusk(ScenePctService<?> scenePctService, Baseline baseline, Spec spec, float f) {
        switch (spec.axis().ordinal()) {
            case 1: {
                feip9jjw007v.set(scenePctService, baseline.translateX() + (float)spec.sign() * f);
                break;
            }
            case 2: {
                f1b33szqhwfj.set(scenePctService, baseline.translateY() + (float)spec.sign() * f);
                break;
            }
        }
    }

    private static void m8eni77lz33n(ScenePctService<?> scenePctService, Spec spec) {
        MotionAnimateService.cancel(scenePctService, fekx17u5nzbn);
        MotionAnimateService.cancel(scenePctService, fd7h9ms1y6s0);
        if (spec != null) {
            if (spec.axis() == Axis.X) {
                MotionAnimateService.cancel(scenePctService, feip9jjw007v);
            }
            if (spec.axis() == Axis.Y) {
                MotionAnimateService.cancel(scenePctService, f1b33szqhwfj);
            }
        }
        if (ftxbgif29y4.supports(scenePctService)) {
            MotionAnimateService.cancel(scenePctService, ftxbgif29y4);
        }
    }

    private static float m8vgq2z7bq8j(ScenePctService<?> scenePctService) {
        return ftxbgif29y4.supports(scenePctService) ? ftxbgif29y4.get(scenePctService) : 0.0f;
    }

    private static float miixfzaem44h(SceneEaseHandler sceneEaseHandler) {
        double d;
        if (sceneEaseHandler instanceof SceneEaseHandler.Tween) {
            SceneEaseHandler.Tween tween = (SceneEaseHandler.Tween)sceneEaseHandler;
            return tween.duration();
        }
        SceneEaseHandler.Spring spring = (SceneEaseHandler.Spring)sceneEaseHandler;
        double d2 = spring.damping();
        double d3 = spring.stiffness();
        double d4 = d2 * d2 - Double.longBitsToDouble(0x4010000000000000L) * d3;
        double d5 = d = d4 <= 0.0 ? d2 * Double.longBitsToDouble(4602678819172646912L) : (d2 - Math.sqrt(d4)) * Double.longBitsToDouble(4602678819172646912L);
        if (!(d > 0.0) || !Double.isFinite(d)) {
            return 1.0f;
        }
        return (float)Math.max(Double.longBitsToDouble(4591870180066957722L), Math.min(Double.longBitsToDouble(0x4008000000000000L), Math.log(Double.longBitsToDouble(4652007308841189376L)) / d));
    }

    private static NormalizedOptions m8wfaa39atxa(Preset preset, Options options) {
        Options options2 = options != null ? options : new Options();
        float f = Float.isFinite(options2.duration) && options2.duration > 0.0f ? options2.duration : SceneCodec.mj7pmkqqrk0w(preset);
        float f2 = Float.isFinite(options2.offset) && options2.offset >= 0.0f ? options2.offset : SceneCodec.m9blk30g6ezv(preset);
        float f3 = Float.isFinite(options2.opacity) ? Math.max(0.0f, Math.min(1.0f, options2.opacity)) : 1.0f;
        float f4 = Float.isFinite(options2.scale) && options2.scale >= 0.0f ? options2.scale : 1.0f;
        return new NormalizedOptions(f, f2, f3, f4);
    }

    private static Spec mhychxh4x3ah(Preset preset, NormalizedOptions normalizedOptions) {
        float f = normalizedOptions.duration();
        float f2 = normalizedOptions.offset();
        Record record = preset == Preset.FADE ? SceneEaseHandler.Tween.ease(f) : SceneEaseHandler.Spring.SNAPPY;
        SceneEaseHandler.Tween tween = SceneEaseHandler.Tween.ease(Math.min(f, Float.intBitsToFloat(1046562734)));
        SceneEaseHandler.Tween tween2 = SceneEaseHandler.Tween.ease(Math.min(f, Float.intBitsToFloat(1049582633)));
        SceneEaseHandler.Tween tween3 = tween;
        SceneEaseHandler.Tween tween4 = tween2;
        SceneEaseHandler.Tween tween5 = tween;
        return switch (preset.ordinal()) {
            default -> throw new MatchException(null, null);
            case 10 -> throw new IllegalArgumentException("NONE has no animated spec");
            case 0 -> new Spec(1.0f, 1.0f, 0.0f, Axis.NONE, 1, (SceneEaseHandler)((Object)record), tween, tween2, tween3, tween4, tween5, Float.intBitsToFloat(0x40400000), 2.0f);
            case 1 -> new Spec(Float.intBitsToFloat(1063339950), Float.intBitsToFloat(1064011039), 0.0f, Axis.NONE, 1, (SceneEaseHandler)((Object)record), tween, tween2, tween3, tween4, tween5, Float.intBitsToFloat(0x41000000), Float.intBitsToFloat(0x40800000));
            case 2 -> new Spec(Float.intBitsToFloat(1064011039), Float.intBitsToFloat(1064346583), f2, Axis.Y, 1, (SceneEaseHandler)((Object)record), tween, tween2, tween3, tween4, tween5, Float.intBitsToFloat(0x40C00000), Float.intBitsToFloat(0x40400000));
            case 3 -> new Spec(1.0f, 1.0f, f2, Axis.Y, 1, (SceneEaseHandler)((Object)record), tween, tween2, tween3, tween4, tween5, 0.0f, 0.0f);
            case 4 -> new Spec(1.0f, 1.0f, f2, Axis.Y, -1, (SceneEaseHandler)((Object)record), tween, tween2, tween3, tween4, tween5, 0.0f, 0.0f);
            case 5 -> new Spec(1.0f, 1.0f, f2, Axis.X, 1, (SceneEaseHandler)((Object)record), tween, tween2, tween3, tween4, tween5, 0.0f, 0.0f);
            case 6 -> new Spec(1.0f, 1.0f, f2, Axis.X, -1, (SceneEaseHandler)((Object)record), tween, tween2, tween3, tween4, tween5, 0.0f, 0.0f);
            case 7 -> new Spec(Float.intBitsToFloat(1062333317), Float.intBitsToFloat(1063004406), 0.0f, Axis.NONE, 1, (SceneEaseHandler)((Object)record), tween, tween2, tween3, tween4, tween5, Float.intBitsToFloat(1092616192), Float.intBitsToFloat(0x40A00000));
            case 9 -> new Spec(Float.intBitsToFloat(1064715682), Float.intBitsToFloat(1065084781), f2, Axis.Y, 1, new SceneEaseHandler.Spring(Float.intBitsToFloat(1109131264), Float.intBitsToFloat(1140457472)), SceneEaseHandler.Tween.exit(Float.intBitsToFloat(1042536202)), SceneEaseHandler.Tween.enter(Float.intBitsToFloat(1043878380)), SceneEaseHandler.Tween.exit(Float.intBitsToFloat(1041194025)), SceneEaseHandler.Tween.enter(Float.intBitsToFloat(1043878380)), SceneEaseHandler.Tween.exit(Float.intBitsToFloat(1041194025)), 0.0f, 0.0f);
            case 8 -> {
                SceneEaseHandler.Tween var10_10 = SceneEaseHandler.Tween.enter(f);
                SceneEaseHandler.Tween var11_11 = SceneEaseHandler.Tween.exit(f);
                SceneEaseHandler.Tween var12_12 = SceneEaseHandler.Tween.enter(f * Float.intBitsToFloat(1060655596));
                SceneEaseHandler.Tween var13_13 = SceneEaseHandler.Tween.exit(f * Float.intBitsToFloat(1061662228));
                yield new Spec(Float.intBitsToFloat(1064883454), Float.intBitsToFloat(1065051226), 0.0f, Axis.NONE, 1, var10_10, var11_11, var12_12, var13_13, var10_10, var11_11, Float.intBitsToFloat(1102053376), Float.intBitsToFloat(1099956224));
            }
        };
    }

    private static float mj7pmkqqrk0w(Preset preset) {
        return switch (preset.ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> Float.intBitsToFloat(1046562734);
            case 1, 2, 7 -> Float.intBitsToFloat(1049582633);
            case 3, 4, 5, 6 -> Float.intBitsToFloat(1047904911);
            case 8 -> Float.intBitsToFloat(1057635697);
            case 9 -> Float.intBitsToFloat(1050924810);
            case 10 -> 0.0f;
        };
    }

    private static float m9blk30g6ezv(Preset preset) {
        return switch (preset.ordinal()) {
            case 2 -> Float.intBitsToFloat(1092616192);
            case 9 -> Float.intBitsToFloat(0x41000000);
            case 3, 4, 5, 6 -> Float.intBitsToFloat(1099956224);
            default -> 0.0f;
        };
    }

    public static enum Action {
        SHOW,
        HIDE,
        TOGGLE;


        public static Action parse(String string) {
            if (string == null || string.isBlank()) {
                return SHOW;
            }
            return switch (string.toLowerCase()) {
                case "in", "show", "open", "enter" -> SHOW;
                case "out", "hide", "close", "exit" -> HIDE;
                case "toggle" -> TOGGLE;
                default -> SHOW;
            };
        }
    }

    private static final class State {
        final Baseline baseline;
        Phase phase;
        Spec spec;
        float remaining;

        State(Baseline baseline, Phase phase, Spec spec) {
            this.baseline = baseline;
            this.phase = phase;
            this.spec = spec;
        }
    }

    private static enum Phase {
        ENTERING,
        EXITING;

    }

    public static enum Preset {
        FADE,
        POP,
        MODAL,
        SLIDE_UP,
        SLIDE_DOWN,
        SLIDE_LEFT,
        SLIDE_RIGHT,
        ZOOM,
        SPREAD,
        WORKSPACE,
        NONE;


        public static Preset parse(String string) {
            if (string == null || string.isBlank()) {
                return POP;
            }
            return switch (string.toLowerCase().replace('_', '-')) {
                case "fade" -> FADE;
                case "modal", "sheet" -> MODAL;
                case "slide-up", "up" -> SLIDE_UP;
                case "slide-down", "down" -> SLIDE_DOWN;
                case "slide-left", "left" -> SLIDE_LEFT;
                case "slide-right", "right" -> SLIDE_RIGHT;
                case "zoom" -> ZOOM;
                case "spread", "canvas" -> SPREAD;
                case "workspace" -> WORKSPACE;
                case "none", "instant" -> NONE;
                default -> POP;
            };
        }
    }

    public static final class Options {
        public float duration = Float.intBitsToFloat(-1082130432);
        public float offset = Float.intBitsToFloat(-1082130432);
        public float opacity = 1.0f;
        public float scale = 1.0f;
    }

    private record NormalizedOptions(float duration, float offset, float opacity, float scale) {
    }

    private record Spec(float startScaleFactor, float exitScaleFactor, float offset, Axis axis, int sign, SceneEaseHandler enterTransformAnim, SceneEaseHandler exitTransformAnim, SceneEaseHandler enterOpacityAnim, SceneEaseHandler exitOpacityAnim, SceneEaseHandler enterEdgeAnim, SceneEaseHandler exitEdgeAnim, float startBlur, float exitBlur) {
    }

    private record Baseline(float translateX, float translateY, float scale, float opacity, float edgeSoftness) {
    }

    private static enum Axis {
        NONE,
        X,
        Y;

    }
}

