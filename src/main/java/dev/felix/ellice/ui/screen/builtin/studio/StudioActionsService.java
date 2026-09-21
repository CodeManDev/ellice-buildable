



package dev.felix.ellice.ui.screen.builtin.studio;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.material.MaterialEnterService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;
import dev.felix.ellice.ui.scene.ScenePctService;
import java.util.function.BiConsumer;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;

public final class StudioActionsService extends SceneCornerRadiusService
{
    private BiConsumer<Float, Float> f4pvk7nwrq6d;
    private Runnable fgxcb91ydwo8;
    private BiConsumer<Float, Float> f3xe07noos58;
    private Runnable fdmhuj4r2czq;
    private float f5vixfbja8ac;
    private float fjha5oui30ez;
    private float fbqj47frtl6y;
    private float fhmrg4vybtq5;
    private boolean fcb61zb0cgib;
    private boolean fhsvnrtsy0u5;
    
    public StudioActionsService() {
        this.f4pvk7nwrq6d = ((p0, p1) -> {});
        this.fgxcb91ydwo8 = (() -> {});
        this.f3xe07noos58 = ((p0, p1) -> {});
        this.fdmhuj4r2czq = (() -> {});
        this.interactive(true).stopPropagation(true).cursorStyle(CursorStyle.POINTER);
    }
    
    public StudioActionsService actions(final Runnable fgxcb91ydwo8, final BiConsumer<Float, Float> f4pvk7nwrq6d) {
        this.fgxcb91ydwo8 = fgxcb91ydwo8;
        this.f4pvk7nwrq6d = f4pvk7nwrq6d;
        return this;
    }
    
    public StudioActionsService preview(final BiConsumer<Float, Float> f3xe07noos58, final Runnable fdmhuj4r2czq) {
        this.f3xe07noos58 = f3xe07noos58;
        this.fdmhuj4r2czq = fdmhuj4r2czq;
        return this;
    }
    
    public void focus(final boolean fhsvnrtsy0u5) {
        this.fhsvnrtsy0u5 = fhsvnrtsy0u5;
        this.invalidate();
    }
    
    public void activate() {
        this.fgxcb91ydwo8.run();
    }
    
    @Override
    protected void onScenePress(final float n, final float n2) {
        this.fbqj47frtl6y = n;
        this.f5vixfbja8ac = n;
        this.fhmrg4vybtq5 = n2;
        this.fjha5oui30ez = n2;
        this.fcb61zb0cgib = false;
        MotionAnimateService.animate(this, MotionColorsContainer.Floats.CORNER_RADIUS, Float.intBitsToFloat(1090519040), MaterialEnterService.PRESS);
    }
    
    @Override
    protected void updateWhileScenePressed(final float n, final float n2) {
        this.fbqj47frtl6y = n;
        this.fhmrg4vybtq5 = n2;
        this.fcb61zb0cgib |= (Math.hypot(n - this.f5vixfbja8ac, n2 - this.fjha5oui30ez) > Double.longBitsToDouble(4617315517961601024L));
        if (this.fcb61zb0cgib) {
            this.f3xe07noos58.accept(n, n2);
        }
    }
    
    @Override
    protected void onRelease(final boolean b) {
        MotionAnimateService.animate(this, MotionColorsContainer.Floats.CORNER_RADIUS, Float.intBitsToFloat(1094713344), MaterialEnterService.RELEASE);
        this.fdmhuj4r2czq.run();
        if (this.fcb61zb0cgib) {
            this.f4pvk7nwrq6d.accept(this.fbqj47frtl6y, this.fhmrg4vybtq5);
        }
        else if (b) {
            this.fgxcb91ydwo8.run();
        }
    }
    
    @Override
    protected void handleClick() {
    }
    
    @Override
    protected void draw(final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
        super.draw(compositorPushPresentationScaleService);
        if (this.fhsvnrtsy0u5) {
            compositorPushPresentationScaleService.roundedRect(this.cx + 1.0f, this.cy + 1.0f, this.cw - 2.0f, this.ch - 2.0f, Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1094713344), 0, 0.0f, 0.0f, 0, Float.intBitsToFloat(1069547520), -2701569, this.effectiveOpacity, 0.0f);
        }
    }
}
