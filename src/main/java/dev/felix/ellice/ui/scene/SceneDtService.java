package dev.felix.ellice.ui.scene;

import dev.felix.ellice.compat.CompatAdapterService;
import dev.felix.ellice.config.LocalConfigRepository;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.material.MaterialPrepareService;
import dev.felix.ellice.ui.material.MaterialTerrainTooltipsService;
import dev.felix.ellice.ui.material.MeasuredTextNode;
import dev.felix.ellice.ui.scene.color.ColorColorService;
import dev.felix.ellice.ui.scene.control.ControlGetAnimPropertyService;
import dev.felix.ellice.ui.scene.control.ControlLetterSpacingService;
import dev.felix.ellice.ui.theme.ThemeCornerData;
import dev.felix.ellice.ui.theme.ThemeIsSetService;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;


public class SceneDtService {
    private final CompositorPushPresentationScaleService f568o86x5xd0;
    private float fcj7abwjsytb;
    private float f8fi2qiucidm;
    private boolean fehya2u5nzu;
    private boolean f3p97pp7pz0a;
    private boolean fft7ix2f1uyh;
    private boolean fdv0b6zkflek;
    private ScenePctService<?> f5lr2smq58sw;
    private ScenePctService<?> fazx7p2hnbyo;
    private ScenePctService<?> fdzs16frq0fl;
    private ScenePctService<?> ff9vtrjwn24x;
    private float fecd97xmt5yo;
    private float fdqu9lkew5k9;
    private float f3q5w9vf4kao;
    private float fa2lr4aj54kv;
    private boolean fj89x9nop0nu;
    private ScenePctService<?> fr8js66i2ea;
    private float f280741c104h;
    private float f3z1dtei2p08;
    private boolean f89l46l9cnan;
    private float fiv6u29ak6fw;
    private boolean f260br8f048k;
    private ScenePctService<?> fb9nut7rkbqo;
    private float f3qikn44agas;
    private float f85e0dl6ag14;
    private String f75x04q7t437;
    private long f9zzi2zct2w8;
    private long fhvp5dzgb245;
    private long fiyvg5tgfpf3;
    private long fak6h8r3upvo;
    private ControlLetterSpacingService fgsqw8bmz2dc;
    private ControlGetAnimPropertyService ff9ajfimnku5;
    private ColorColorService fiirjmit3ppl;
    private int fegbblroo2ad;
    private boolean fafkbaaj09i3;
    private float f2elqz8kijo1;
    private float f3tv3g1jfyhr;
    private static final SceneEaseHandler.Spring ffoiff032s0s = new SceneEaseHandler.Spring(Float.intBitsToFloat(1101004800), Float.intBitsToFloat(1120403456));
    private static final SceneEaseHandler.Spring f1tfj4yhrpws = new SceneEaseHandler.Spring(Float.intBitsToFloat(1104150528), Float.intBitsToFloat(1142292480));
    private static float ficq520a6i4d = Float.intBitsToFloat(1015580809);
    private static final SceneEaseHandler.Spring fh3pvmeo3fdt = new SceneEaseHandler.Spring(Float.intBitsToFloat(1099956224), Float.intBitsToFloat(1132068864));
    private final LayoutContainerNode fhzbc4bk6ke0 = new LayoutContainerNode();
    private final SceneCodec fe71brer2lrw = new SceneCodec();
    private final MeasuredTextNode f4m8kqg23z7t = new MeasuredTextNode();
    private final MaterialPrepareService f9jt7ex7h0gq = new MaterialPrepareService();
    private final SceneRenderer fe23qtkhx1v2 = new SceneRenderer();
    private ScenePctService.CursorStyle fdt9op6rrdzg = ScenePctService.CursorStyle.DEFAULT;
    private float fanvcof4wqy6 = 1.0f;
    private int fdhzunsjr1kr = -1;
    private int fia8feego55z = ThemeIsSetService.UNSET_COLOR;
    private float fedi4sljrmnd = Float.intBitsToFloat(2143289344);
    private float ff9fbm4emgl2 = Float.intBitsToFloat(2143289344);

    public static float dt() {
        return ficq520a6i4d;
    }

    public SceneDtService(CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
        this.f568o86x5xd0 = compositorPushPresentationScaleService;
        this.fhzbc4bk6ke0.direction(ScenePctService.Direction.NONE);
    }

    public LayoutContainerNode root() {
        return this.fhzbc4bk6ke0;
    }

    public SceneRenderer toasts() {
        return this.fe23qtkhx1v2;
    }

    public SceneCodec transitions() {
        return this.fe71brer2lrw;
    }

    public ScenePctService<?> findById(String str) {
        return this.fhzbc4bk6ke0.findById(str);
    }

    public void shutdown() {
        this.fe71brer2lrw.clear();
        this.fhzbc4bk6ke0.clearChildren();
        if (this.fhvp5dzgb245 != 0) {
            GLFW.glfwDestroyCursor(this.fhvp5dzgb245);
            this.fhvp5dzgb245 = 0L;
        }
        if (this.fiyvg5tgfpf3 != 0) {
            GLFW.glfwDestroyCursor(this.fiyvg5tgfpf3);
            this.fiyvg5tgfpf3 = 0L;
        }
        if (this.fak6h8r3upvo != 0) {
            GLFW.glfwDestroyCursor(this.fak6h8r3upvo);
            this.fak6h8r3upvo = 0L;
        }
        if (this.f9zzi2zct2w8 != 0) {
            GLFW.glfwDestroyCursor(this.f9zzi2zct2w8);
            this.f9zzi2zct2w8 = 0L;
        }
        try {
            GLFW.glfwSetCursor(CompatAdapterService.windowHandle(Minecraft.getInstance()), 0L);
        } catch (Exception e) {
        }
        this.fdt9op6rrdzg = ScenePctService.CursorStyle.DEFAULT;
    }

    public boolean hasInteractiveAt(float f, float f2) {
        return m7nsb8icmy0u(this.fhzbc4bk6ke0, f, f2) != null;
    }

    public void mouseInput(float f, float f2, boolean z, boolean z2) {
        this.fcj7abwjsytb = f;
        this.f8fi2qiucidm = f2;
        this.f3p97pp7pz0a = this.fehya2u5nzu;
        this.fehya2u5nzu = z;
        this.fdv0b6zkflek = this.fft7ix2f1uyh;
        this.fft7ix2f1uyh = z2;
    }

    public void charTyped(char c) {
        if (this.fgsqw8bmz2dc == null || !this.fgsqw8bmz2dc.focused()) {
            return;
        }
        this.fgsqw8bmz2dc.charTyped(c);
    }

    public boolean keyPressed(int i) {
        return keyPressed(i, 0);
    }

    public boolean keyPressed(int i, int i2) {
        if (this.ff9ajfimnku5 != null && this.ff9ajfimnku5.isListening()) {
            this.ff9ajfimnku5.acceptKey(i);
            this.ff9ajfimnku5 = null;
            return true;
        }
        if (this.fgsqw8bmz2dc == null || !this.fgsqw8bmz2dc.focused()) {
            return false;
        }
        this.fgsqw8bmz2dc.keyPressed(i, i2);
        return true;
    }

    public void paste(String str) {
        if (this.fgsqw8bmz2dc == null || !this.fgsqw8bmz2dc.focused()) {
            return;
        }
        this.fgsqw8bmz2dc.paste(str);
    }

    public String copy() {
        if (this.fgsqw8bmz2dc == null || !this.fgsqw8bmz2dc.focused()) {
            return null;
        }
        return this.fgsqw8bmz2dc.copy();
    }

    public void cut() {
        if (this.fgsqw8bmz2dc == null || !this.fgsqw8bmz2dc.focused()) {
            return;
        }
        this.fgsqw8bmz2dc.cut();
    }

    public void selectAll() {
        if (this.fgsqw8bmz2dc == null || !this.fgsqw8bmz2dc.focused()) {
            return;
        }
        this.fgsqw8bmz2dc.selectAll();
    }

    public boolean hasFocusedInput() {
        return (this.fgsqw8bmz2dc != null && this.fgsqw8bmz2dc.focused()) || (this.ff9ajfimnku5 != null && this.ff9ajfimnku5.isListening());
    }

    public void clearFocus() {
        if (this.fgsqw8bmz2dc != null) {
            this.fgsqw8bmz2dc.unfocus();
            this.fgsqw8bmz2dc = null;
        }
        m739ahj8h82r(null);
    }

    public void setFocusedInput(ControlLetterSpacingService controlLetterSpacingService) {
        if (this.fgsqw8bmz2dc == controlLetterSpacingService) {
            if (controlLetterSpacingService != null) {
                controlLetterSpacingService.focus();
            }
        } else {
            if (this.fgsqw8bmz2dc != null) {
                this.fgsqw8bmz2dc.unfocus();
            }
            this.fgsqw8bmz2dc = controlLetterSpacingService;
            if (controlLetterSpacingService != null) {
                controlLetterSpacingService.focus();
            }
        }
    }

    public void mouseScroll(float f) {
        ScenePctService<?> scenePctServiceMd5cvmflypzx;
        ScenePctService<?> scenePctServiceM7nsb8icmy0u = m7nsb8icmy0u(this.fhzbc4bk6ke0, this.fcj7abwjsytb, this.f8fi2qiucidm);
        ScenePctService<?> scenePctService = scenePctServiceM7nsb8icmy0u;
        while (true) {
            ScenePctService<?> scenePctService2 = scenePctService;
            if (scenePctService2 == null) {
                break;
            }
            if (scenePctService2.hitScrollbar(this.fcj7abwjsytb, this.f8fi2qiucidm)) {
                scenePctServiceM7nsb8icmy0u = scenePctService2;
            }
            scenePctService = scenePctService2.parent;
        }
        if ((scenePctServiceM7nsb8icmy0u == null || !scenePctServiceM7nsb8icmy0u.handleScroll(f)) && (scenePctServiceMd5cvmflypzx = md5cvmflypzx(this.fhzbc4bk6ke0, this.fcj7abwjsytb, this.f8fi2qiucidm)) != null) {
            scenePctServiceMd5cvmflypzx.applyScrollDelta(f * Float.intBitsToFloat(1103101952) * ThemeCornerData.current().scrollSpeed(), ffoiff032s0s);
        }
    }

    private ScenePctService<?> md5cvmflypzx(ScenePctService<?> scenePctService, float f, float f2) {
        return mbseyvrjdg20(scenePctService, f, f2, 1.0f, ScenePctService.PresentationTransform.IDENTITY);
    }

    private ScenePctService<?> mbseyvrjdg20(ScenePctService<?> scenePctService, float f, float f2, float f3, ScenePctService.PresentationTransform presentationTransform) {
        if (!scenePctService.visible || !scenePctService.pointerEvents) {
            return null;
        }
        float fMax = Math.max(0.0f, scenePctService.opacity) * f3;
        if (fMax < Float.intBitsToFloat(994352038)) {
            return null;
        }
        ScenePctService.PresentationTransform presentationTransform2 = scenePctService.presentationTransform(presentationTransform);
        float fMapX = presentationTransform2.mapX(scenePctService.cx);
        float fMapY = presentationTransform2.mapY(scenePctService.cy);
        float fScale = scenePctService.cw * presentationTransform2.scale();
        float fScale2 = scenePctService.ch * presentationTransform2.scale();
        if (scenePctService.clip && !mhwro40jvhdr(scenePctService, fMapX, fMapY, fScale, fScale2, presentationTransform2.scale(), f, f2)) {
            return null;
        }
        List<ScenePctService<?>> listChildren = scenePctService.children();
        ScenePctService<?> scenePctService2 = null;
        int size = listChildren.size() - 1;
        while (true) {
            int i = size;
            if (i >= 0) {
                ScenePctService<?> scenePctServiceMbseyvrjdg20 = mbseyvrjdg20(listChildren.get(i), f, f2, fMax, presentationTransform2);
                if (scenePctServiceMbseyvrjdg20 != null) {
                    if (scenePctServiceMbseyvrjdg20.scrollMin() >= Float.intBitsToFloat(-1165815185)) {
                        scenePctService2 = scenePctServiceMbseyvrjdg20;
                        break;
                    }
                    return scenePctServiceMbseyvrjdg20;
                }
                size = i - 1;
            } else {
                break;
            }
        }
        return (scenePctService.scrollable && mjju7w1iir77(fMapX, fMapY, fScale, fScale2, f, f2)) ? scenePctService : scenePctService2;
    }

    public void setFramebufferInfo(float f, int i) {
        this.fanvcof4wqy6 = f;
        this.fegbblroo2ad = i;
    }

    public void frame(float f, float f2) {
        frame(Float.intBitsToFloat(1015580809), f, f2);
    }

    public void frame(float f, float f2, float f3) {
        if (this.fgsqw8bmz2dc != null && !this.fgsqw8bmz2dc.focused()) {
            this.fgsqw8bmz2dc = null;
        }
        if (this.ff9ajfimnku5 != null && !this.ff9ajfimnku5.isListening()) {
            this.ff9ajfimnku5 = null;
        }
        if (!Float.isFinite(f) || f < 0.0f) {
            f = 0.0f;
        }
        if (f > Float.intBitsToFloat(1036831949)) {
            f = Float.intBitsToFloat(1036831949);
        }
        ficq520a6i4d = f;
        this.fhzbc4bk6ke0.tickAnimations(f);
        this.fe71brer2lrw.tick(f);
        mdkphaivk69m(this.fhzbc4bk6ke0, f2, f3);
        this.f2elqz8kijo1 = f2;
        this.f3tv3g1jfyhr = f3;
        mh9qtix3krgu(f2, f3);
        if (mhv8yiuy3oa6() || ScenePctService.layoutVersion() != this.fia8feego55z) {
            m1amcgk4j8de(f2, f3);
        }
        this.f568o86x5xd0.pointer(this.fcj7abwjsytb, this.f8fi2qiucidm, this.fehya2u5nzu);
        this.fhzbc4bk6ke0.drawTree(this.f568o86x5xd0, 1.0f);
        this.fe23qtkhx1v2.tick(f, f2, f3);
        if (this.fe23qtkhx1v2.hasToasts()) {
            this.f568o86x5xd0.afterFlush(() -> {
                this.fe23qtkhx1v2.render(this.f568o86x5xd0);
            });
        }
        m8zwz221plw9(f, f2, f3);
    }

    private void mh9qtix3krgu(float f, float f2) {
        if (ScenePctService.layoutVersion() == this.fia8feego55z && this.f568o86x5xd0.fontMetricsRevision() == this.fdhzunsjr1kr && Float.floatToIntBits(f) == Float.floatToIntBits(this.fedi4sljrmnd) && Float.floatToIntBits(f2) == Float.floatToIntBits(this.ff9fbm4emgl2) && this.f568o86x5xd0.isInitialized() == this.fafkbaaj09i3) {
            return;
        }
        m1amcgk4j8de(f, f2);
    }

    private void m1amcgk4j8de(float f, float f2) {
        boolean z = false;
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= 3) {
                break;
            }
            int iLayoutVersion = ScenePctService.layoutVersion();
            this.fhzbc4bk6ke0.performLayout(this.f568o86x5xd0, 0.0f, 0.0f, f, f2);
            if (ScenePctService.layoutVersion() == iLayoutVersion) {
                z = true;
                break;
            }
            i = i2 + 1;
        }
        this.fia8feego55z = z ? ScenePctService.layoutVersion() : ThemeIsSetService.UNSET_COLOR;
        this.fedi4sljrmnd = f;
        this.ff9fbm4emgl2 = f2;
        this.fafkbaaj09i3 = this.f568o86x5xd0.isInitialized();
        this.fdhzunsjr1kr = this.f568o86x5xd0.fontMetricsRevision();
    }

    private void m8zwz221plw9(float f, float f2, float f3) {
        ThemeCornerData themeCornerDataCurrent = ThemeCornerData.current();
        if (!themeCornerDataCurrent.tooltips()) {
            this.f85e0dl6ag14 = 0.0f;
            this.f3qikn44agas = 0.0f;
            this.f75x04q7t437 = null;
            return;
        }
        String strResolveTooltip = resolveTooltip(this.f5lr2smq58sw);
        if (!Objects.equals(strResolveTooltip, this.f75x04q7t437)) {
            this.f85e0dl6ag14 = 0.0f;
            this.f3qikn44agas = 0.0f;
            this.f75x04q7t437 = strResolveTooltip;
        }
        if (strResolveTooltip == null || strResolveTooltip.isEmpty()) {
            this.f3qikn44agas = 0.0f;
            this.f85e0dl6ag14 = Math.max(0.0f, this.f85e0dl6ag14 - (f * Float.intBitsToFloat(1094713344)));
        } else {
            this.f3qikn44agas += f;
            if (this.f3qikn44agas >= themeCornerDataCurrent.tooltipDelay()) {
                this.f85e0dl6ag14 = Math.min(1.0f, this.f85e0dl6ag14 + (f * Float.intBitsToFloat(1090519040)));
            }
        }
        if (this.f85e0dl6ag14 < Float.intBitsToFloat(1008981770) || strResolveTooltip == null || strResolveTooltip.isEmpty()) {
            return;
        }
        float fIntBitsToFloat = this.f85e0dl6ag14 * Float.intBitsToFloat(1064514355);
        float f4 = this.fcj7abwjsytb;
        float f5 = this.f8fi2qiucidm;
        boolean z = false;
        ScenePctService<?> scenePctServiceParent = this.f5lr2smq58sw;
        while (true) {
            ScenePctService<?> scenePctService = scenePctServiceParent;
            if (scenePctService != null) {
                if ((scenePctService instanceof MaterialTerrainTooltipsService) && ((MaterialTerrainTooltipsService) scenePctService).terrainTooltips()) {
                    z = true;
                    break;
                }
                scenePctServiceParent = scenePctService.parent();
            } else {
                break;
            }
        }
        if (z) {
            this.f568o86x5xd0.afterFlush(() -> {
                this.f9jt7ex7h0gq.prepare(this.f568o86x5xd0, strResolveTooltip, Math.max(Float.intBitsToFloat(1113587712), f2 - Float.intBitsToFloat(1098907648)));
                this.f9jt7ex7h0gq.tickAnimations(f);
                float fBubbleWidth = this.f9jt7ex7h0gq.bubbleWidth();
                float fBubbleHeight = this.f9jt7ex7h0gq.bubbleHeight();
                float fMiqwwqmazdgw = miqwwqmazdgw(f4 - (fBubbleWidth * Float.intBitsToFloat(1056964608)), Float.intBitsToFloat(1090519040), Math.max(Float.intBitsToFloat(1090519040), (f2 - fBubbleWidth) - Float.intBitsToFloat(1090519040)));
                boolean z2 = (f5 - fBubbleHeight) - Float.intBitsToFloat(1098907648) >= Float.intBitsToFloat(1090519040);
                float fIntBitsToFloat2 = z2 ? (f5 - fBubbleHeight) - Float.intBitsToFloat(1098907648) : Math.min((f3 - fBubbleHeight) - Float.intBitsToFloat(1090519040), f5 + Float.intBitsToFloat(1101004800));
                this.f9jt7ex7h0gq.anchor(f4 - fMiqwwqmazdgw, z2);
                this.f9jt7ex7h0gq.performLayout(this.f568o86x5xd0, fMiqwwqmazdgw, Math.max(Float.intBitsToFloat(1090519040), fIntBitsToFloat2), fBubbleWidth, fBubbleHeight);
                this.f9jt7ex7h0gq.drawTree(this.f568o86x5xd0, Math.min(1.0f, fIntBitsToFloat / Float.intBitsToFloat(1064514355)));
            });
            return;
        }
        boolean z2 = false;
        ScenePctService<?> scenePctServiceParent2 = this.f5lr2smq58sw;
        while (true) {
            ScenePctService<?> scenePctService2 = scenePctServiceParent2;
            if (scenePctService2 == null) {
                break;
            }
            if ((scenePctService2 instanceof MaterialTerrainTooltipsService) || "clickgui.panel-shell".equals(scenePctService2.getId()) || "account-manager.panel-shell".equals(scenePctService2.getId())) {
                z2 = true;
                break;
            }
            scenePctServiceParent2 = scenePctService2.parent();
        }
        if (z2) {
            this.f568o86x5xd0.afterFlush(() -> {
                this.f4m8kqg23z7t.text(strResolveTooltip);
                float fMin = Math.min(f2 - Float.intBitsToFloat(1098907648), this.f4m8kqg23z7t.intrinsicWidth(this.f568o86x5xd0));
                float fIntrinsicHeight = this.f4m8kqg23z7t.intrinsicHeight(this.f568o86x5xd0);
                float fMiqwwqmazdgw = miqwwqmazdgw(f4 + Float.intBitsToFloat(1092616192), Float.intBitsToFloat(1090519040), Math.max(Float.intBitsToFloat(1090519040), (f2 - fMin) - Float.intBitsToFloat(1090519040)));
                float fIntBitsToFloat2 = (f5 + Float.intBitsToFloat(1101004800)) + fIntrinsicHeight < f3 - Float.intBitsToFloat(1090519040) ? f5 + Float.intBitsToFloat(1101004800) : Math.max(Float.intBitsToFloat(1090519040), (f5 - fIntrinsicHeight) - Float.intBitsToFloat(1094713344));
                this.f4m8kqg23z7t.opacity(Math.min(1.0f, fIntBitsToFloat / Float.intBitsToFloat(1064514355)));
                this.f4m8kqg23z7t.performLayout(this.f568o86x5xd0, fMiqwwqmazdgw, fIntBitsToFloat2, fMin, fIntrinsicHeight);
                this.f4m8kqg23z7t.drawTree(this.f568o86x5xd0, 1.0f);
            });
        } else {
            this.f568o86x5xd0.afterFlush(() -> {
                float fIntBitsToFloat2 = Float.intBitsToFloat(1088421888);
                float fIntBitsToFloat3 = Float.intBitsToFloat(1088421888);
                float fIntBitsToFloat4 = Float.intBitsToFloat(1084227584);
                List<String> listWrapTooltipLines = wrapTooltipLines(this.f568o86x5xd0, strResolveTooltip, fIntBitsToFloat2, Math.max(Float.intBitsToFloat(1117782016), Math.min(Float.intBitsToFloat(1131413504), (f2 - Float.intBitsToFloat(1098907648)) - (fIntBitsToFloat3 * 2.0f))), 4);
                float fMax = 0.0f;
                Iterator<String> it = listWrapTooltipLines.iterator();
                while (it.hasNext()) {
                    fMax = Math.max(fMax, this.f568o86x5xd0.textWidth(it.next(), fIntBitsToFloat2));
                }
                float fTextLineHeight = this.f568o86x5xd0.textLineHeight(fIntBitsToFloat2);
                float f6 = fMax + (fIntBitsToFloat3 * 2.0f);
                float size = (fTextLineHeight * listWrapTooltipLines.size()) + (1.0f * Math.max(0, listWrapTooltipLines.size() - 1)) + (fIntBitsToFloat4 * 2.0f);
                float fMiqwwqmazdgw = miqwwqmazdgw(f4 + Float.intBitsToFloat(1092616192), Float.intBitsToFloat(1082130432), Math.max(Float.intBitsToFloat(1082130432), (f2 - f6) - Float.intBitsToFloat(1082130432)));
                float fIntBitsToFloat5 = f5 + Float.intBitsToFloat(1096810496);
                float fMiqwwqmazdgw2 = fIntBitsToFloat5 + size <= f3 - Float.intBitsToFloat(1082130432) ? fIntBitsToFloat5 : miqwwqmazdgw((f5 - size) - Float.intBitsToFloat(1092616192), Float.intBitsToFloat(1082130432), Math.max(Float.intBitsToFloat(1082130432), (f3 - size) - Float.intBitsToFloat(1082130432)));
                this.f568o86x5xd0.roundedRect(fMiqwwqmazdgw, fMiqwwqmazdgw2, f6, size, Float.intBitsToFloat(1084227584), (((int) (fIntBitsToFloat * Float.intBitsToFloat(1131413504))) << 24) | 1710628);
                this.f568o86x5xd0.roundedRect(fMiqwwqmazdgw, fMiqwwqmazdgw2, f6, size, Float.intBitsToFloat(1084227584), Float.intBitsToFloat(1084227584), Float.intBitsToFloat(1084227584), Float.intBitsToFloat(1084227584), 0, 0.0f, 0.0f, 0, Float.intBitsToFloat(1056964608), (((int) (fIntBitsToFloat * Float.intBitsToFloat(1111490560))) << 24) | 16777215);
                float f7 = fMiqwwqmazdgw2 + fIntBitsToFloat4;
                Iterator<String> it2 = listWrapTooltipLines.iterator();
                while (it2.hasNext()) {
                    this.f568o86x5xd0.text(fMiqwwqmazdgw + fIntBitsToFloat3, f7, it2.next(), fIntBitsToFloat2, (((int) (fIntBitsToFloat * Float.intBitsToFloat(1130168320))) << 24) | 16777215);
                    f7 += fTextLineHeight + 1.0f;
                }
            });
        }
    }

    static List<String> wrapTooltipLines(CompositorPushPresentationScaleService compositorPushPresentationScaleService, String str, float f, float f2, int i) {
        if (str == null || str.isBlank() || i <= 0) {
            return List.of();
        }
        ArrayList arrayList = new ArrayList();
        String[] strArrSplit = str.strip().split("\\R", -1);
        boolean z = false;
        int i2 = 0;
        loop0: while (true) {
            int i3 = i2;
            if (i3 >= strArrSplit.length) {
                break;
            }
            String str2 = strArrSplit[i3];
            if (!str2.isBlank()) {
                String strReplaceAll = str2.trim().replaceAll("\\s+", " ");
                while (true) {
                    String str3 = strReplaceAll;
                    if (str3.isEmpty()) {
                        if (arrayList.size() >= i && i3 < strArrSplit.length - 1) {
                            z = true;
                            break;
                        }
                        break;
                    }
                    if (arrayList.size() >= i) {
                        z = true;
                        break loop0;
                    }
                    if (compositorPushPresentationScaleService.textWidth(str3, f) <= f2) {
                        arrayList.add(str3);
                        strReplaceAll = "";
                    } else {
                        int iMe2sq53381c7 = me2sq53381c7(compositorPushPresentationScaleService, str3, f, f2);
                        int iLastIndexOf = str3.lastIndexOf(32, Math.max(0, iMe2sq53381c7 - 1));
                        int iMax = iLastIndexOf > 0 ? iLastIndexOf : Math.max(1, iMe2sq53381c7);
                        arrayList.add(str3.substring(0, iMax).stripTrailing());
                        strReplaceAll = str3.substring(iMax).stripLeading();
                    }
                }
                i2 = i3 + 1;
            } else {
                if (arrayList.size() >= i) {
                    z = true;
                    break;
                }
                arrayList.add("");
                i2 = i3 + 1;
            }
        }
        if (arrayList.isEmpty()) {
            arrayList.add("");
        }
        if (z) {
            int size = arrayList.size() - 1;
            arrayList.set(size, m8qnn4ktg4nl(compositorPushPresentationScaleService, (String) arrayList.get(size), f, f2));
        }
        return List.copyOf(arrayList);
    }

    private static int me2sq53381c7(CompositorPushPresentationScaleService compositorPushPresentationScaleService, String str, float f, float f2) {
        int i = 1;
        int length = str.length();
        int i2 = 1;
        while (i <= length) {
            int i3 = (i + length) >>> 1;
            if (compositorPushPresentationScaleService.textWidth(str.substring(0, i3), f) <= f2) {
                i2 = i3;
                i = i3 + 1;
            } else {
                length = i3 - 1;
            }
        }
        return i2;
    }

    private static String m8qnn4ktg4nl(CompositorPushPresentationScaleService compositorPushPresentationScaleService, String str, float f, float f2) {
        int i;
        if (compositorPushPresentationScaleService.textWidth(str + "…", f) <= f2) {
            return str + "…";
        }
        int length = str.length();
        while (true) {
            i = length;
            if (i <= 0 || compositorPushPresentationScaleService.textWidth(str.substring(0, i).stripTrailing() + "…", f) <= f2) {
                break;
            }
            length = i - 1;
        }
        return str.substring(0, i).stripTrailing() + "…";
    }

    private static float miqwwqmazdgw(float f, float f2, float f3) {
        return Math.max(f2, Math.min(f, f3));
    }

    static String resolveTooltip(ScenePctService<?> scenePctService) {
        ScenePctService<?> scenePctServiceParent = scenePctService;
        while (true) {
            ScenePctService<?> scenePctService2 = scenePctServiceParent;
            if (scenePctService2 == null) {
                return null;
            }
            String str = scenePctService2.tooltip();
            if (str != null && !str.isBlank()) {
                return str;
            }
            scenePctServiceParent = scenePctService2.parent();
        }
    }

    private boolean mhv8yiuy3oa6() {
        boolean z = false;
        ScenePctService<?> scenePctServiceM7nsb8icmy0u = m7nsb8icmy0u(this.fhzbc4bk6ke0, this.fcj7abwjsytb, this.f8fi2qiucidm);
        ScenePctService<?> scenePctService = scenePctServiceM7nsb8icmy0u;
        while (true) {
            ScenePctService<?> scenePctService2 = scenePctService;
            if (scenePctService2 == null) {
                break;
            }
            if (scenePctService2.hitScrollbar(this.fcj7abwjsytb, this.f8fi2qiucidm)) {
                scenePctServiceM7nsb8icmy0u = scenePctService2;
            }
            scenePctService = scenePctService2.parent;
        }
        if (this.f5lr2smq58sw != scenePctServiceM7nsb8icmy0u) {
            if (this.f5lr2smq58sw != null) {
                this.f5lr2smq58sw.hovered = false;
                this.f5lr2smq58sw.animate("hoverProgress", 0.0f, f1tfj4yhrpws);
                if (this.f5lr2smq58sw.onHoverChange != null) {
                    this.f5lr2smq58sw.onHoverChange.accept(false);
                }
            }
            this.f5lr2smq58sw = scenePctServiceM7nsb8icmy0u;
            if (this.f5lr2smq58sw != null) {
                this.f5lr2smq58sw.hovered = true;
                this.f5lr2smq58sw.animate("hoverProgress", 1.0f, f1tfj4yhrpws);
                if (this.f5lr2smq58sw.onHoverChange != null) {
                    this.f5lr2smq58sw.onHoverChange.accept(true);
                }
            }
            mb4hvy9zufns();
        }
        if (this.fehya2u5nzu && !this.f3p97pp7pz0a) {
            this.fj89x9nop0nu = false;
            this.f3q5w9vf4kao = this.fcj7abwjsytb;
            this.fa2lr4aj54kv = this.f8fi2qiucidm;
            if (this.fiirjmit3ppl != null) {
                this.fiirjmit3ppl.eyedropperPick(this.f568o86x5xd0.readPixel((int) (this.fcj7abwjsytb * this.fanvcof4wqy6), (int) (this.f8fi2qiucidm * this.fanvcof4wqy6), this.fegbblroo2ad));
                this.fiirjmit3ppl = null;
                return true;
            }
            ColorColorService colorColorServiceMa85juy9wyeb = ma85juy9wyeb(this.fhzbc4bk6ke0);
            if (colorColorServiceMa85juy9wyeb != null) {
                this.fiirjmit3ppl = colorColorServiceMa85juy9wyeb;
            }
            if (this.fgsqw8bmz2dc != null && this.f5lr2smq58sw != this.fgsqw8bmz2dc) {
                this.fgsqw8bmz2dc.unfocus();
                this.fgsqw8bmz2dc = null;
            }
            if (this.ff9ajfimnku5 != null && this.f5lr2smq58sw != this.ff9ajfimnku5) {
                m739ahj8h82r(null);
            }
            this.fazx7p2hnbyo = this.f5lr2smq58sw;
            if (this.fazx7p2hnbyo != null) {
                this.fazx7p2hnbyo.pressed = true;
                this.fazx7p2hnbyo.animate("pressProgress", 1.0f, f1tfj4yhrpws);
                this.fazx7p2hnbyo.onPress(mi6qfizzllu8(this.fazx7p2hnbyo, this.fcj7abwjsytb), mdy0wcaemydv(this.fazx7p2hnbyo, this.f8fi2qiucidm));
                this.fazx7p2hnbyo.onScenePress(this.fcj7abwjsytb, this.f8fi2qiucidm);
                ScenePctService<?> scenePctService3 = this.fazx7p2hnbyo;
                if (scenePctService3 instanceof ControlLetterSpacingService) {
                    this.fgsqw8bmz2dc = (ControlLetterSpacingService) scenePctService3;
                }
                if (this.fazx7p2hnbyo.draggable) {
                    this.ff9vtrjwn24x = this.fazx7p2hnbyo;
                    float fM6dwcz2taw86 = m6dwcz2taw86(this.ff9vtrjwn24x, this.fcj7abwjsytb);
                    float fMb6bqerjpdfe = mb6bqerjpdfe(this.ff9vtrjwn24x, this.f8fi2qiucidm);
                    if (Float.isFinite(fM6dwcz2taw86) && Float.isFinite(this.ff9vtrjwn24x.x)) {
                        this.fecd97xmt5yo = fM6dwcz2taw86 - this.ff9vtrjwn24x.x;
                    }
                    if (Float.isFinite(fMb6bqerjpdfe) && Float.isFinite(this.ff9vtrjwn24x.y)) {
                        this.fdqu9lkew5k9 = fMb6bqerjpdfe - this.ff9vtrjwn24x.y;
                    }
                }
            }
            this.fr8js66i2ea = m2zrhynh8k7x(this.fazx7p2hnbyo);
            this.f89l46l9cnan = false;
            this.f260br8f048k = false;
            if (this.fr8js66i2ea != null) {
                if (this.fr8js66i2ea.hitScrollbar(this.fcj7abwjsytb, this.f8fi2qiucidm)) {
                    this.f260br8f048k = true;
                    this.f89l46l9cnan = true;
                    this.fj89x9nop0nu = true;
                    this.fr8js66i2ea.beginScrollbarDrag();
                    this.fiv6u29ak6fw = this.fr8js66i2ea.scrollbarGrabOffset(this.fcj7abwjsytb, this.f8fi2qiucidm);
                } else if (macpy9qvkkns(this.fazx7p2hnbyo, this.fr8js66i2ea)) {
                    this.fr8js66i2ea = null;
                } else {
                    this.f280741c104h = mc0t8l91q0cn(this.fr8js66i2ea, this.fcj7abwjsytb, this.f8fi2qiucidm);
                    this.f3z1dtei2p08 = this.fr8js66i2ea.scrollY();
                }
            }
            m2dbbc6v3r27(mc2sue2alnn2(scenePctServiceM7nsb8icmy0u));
        }
        if (this.fehya2u5nzu && this.fazx7p2hnbyo != null && !this.fj89x9nop0nu && (Math.abs(this.fcj7abwjsytb - this.f3q5w9vf4kao) > 2.0f || Math.abs(this.f8fi2qiucidm - this.fa2lr4aj54kv) > 2.0f)) {
            this.fj89x9nop0nu = true;
        }
        if (this.fehya2u5nzu && this.fazx7p2hnbyo != null && this.ff9vtrjwn24x == null && this.fr8js66i2ea != null && !this.f89l46l9cnan) {
            float fMc0t8l91q0cn = mc0t8l91q0cn(this.fr8js66i2ea, this.fcj7abwjsytb, this.f8fi2qiucidm) - this.f280741c104h;
            float f = this.fr8js66i2ea.scrollsHorizontally() ? this.f8fi2qiucidm - this.fa2lr4aj54kv : this.fcj7abwjsytb - this.f3q5w9vf4kao;
            if (Math.abs(fMc0t8l91q0cn) > Float.intBitsToFloat(1082130432) && Math.abs(fMc0t8l91q0cn) >= Math.abs(f) * Float.intBitsToFloat(1059481190)) {
                this.f89l46l9cnan = true;
                this.fj89x9nop0nu = true;
                this.fr8js66i2ea.beginPointerScroll();
            }
        }
        if (this.fehya2u5nzu && this.f89l46l9cnan && this.fr8js66i2ea != null && !this.f260br8f048k) {
            this.fr8js66i2ea.dragScrollTo(this.f3z1dtei2p08 + (mc0t8l91q0cn(this.fr8js66i2ea, this.fcj7abwjsytb, this.f8fi2qiucidm) - this.f280741c104h));
        }
        if (this.fehya2u5nzu && this.f260br8f048k && this.fr8js66i2ea != null) {
            this.fr8js66i2ea.dragScrollbarTo(this.fcj7abwjsytb, this.f8fi2qiucidm, this.fiv6u29ak6fw);
        }
        if (this.fehya2u5nzu && this.fazx7p2hnbyo != null && this.ff9vtrjwn24x == null && !this.f89l46l9cnan) {
            this.fazx7p2hnbyo.updateWhilePressed(mi6qfizzllu8(this.fazx7p2hnbyo, this.fcj7abwjsytb), mdy0wcaemydv(this.fazx7p2hnbyo, this.f8fi2qiucidm));
            this.fazx7p2hnbyo.updateWhileScenePressed(this.fcj7abwjsytb, this.f8fi2qiucidm);
        }
        if (this.fehya2u5nzu && this.ff9vtrjwn24x != null) {
            float fM6dwcz2taw87 = m6dwcz2taw86(this.ff9vtrjwn24x, this.fcj7abwjsytb) - this.fecd97xmt5yo;
            float fMb6bqerjpdfe2 = mb6bqerjpdfe(this.ff9vtrjwn24x, this.f8fi2qiucidm) - this.fdqu9lkew5k9;
            if (this.fj89x9nop0nu && Float.isFinite(fM6dwcz2taw87) && Float.isFinite(fMb6bqerjpdfe2)) {
                this.ff9vtrjwn24x.x = fM6dwcz2taw87;
                this.ff9vtrjwn24x.y = fMb6bqerjpdfe2;
                this.ff9vtrjwn24x.invalidate();
                z = true;
            }
        }
        if (!this.fehya2u5nzu && this.f3p97pp7pz0a) {
            if (this.fazx7p2hnbyo != null) {
                this.fazx7p2hnbyo.pressed = false;
                this.fazx7p2hnbyo.animate("pressProgress", 0.0f, f1tfj4yhrpws);
                boolean z2 = !this.fj89x9nop0nu && this.fazx7p2hnbyo == this.f5lr2smq58sw;
                this.fazx7p2hnbyo.onRelease(z2);
                if (z2) {
                    ScenePctService<?> scenePctService4 = this.fazx7p2hnbyo;
                    scenePctService4.handleClick();
                    if (scenePctService4 instanceof ControlGetAnimPropertyService) {
                        ControlGetAnimPropertyService controlGetAnimPropertyService = (ControlGetAnimPropertyService) scenePctService4;
                        if (controlGetAnimPropertyService.isListening()) {
                            m739ahj8h82r(controlGetAnimPropertyService);
                            this.ff9ajfimnku5 = controlGetAnimPropertyService;
                        } else if (this.ff9ajfimnku5 == controlGetAnimPropertyService) {
                            this.ff9ajfimnku5 = null;
                        }
                    }
                }
                this.fazx7p2hnbyo = null;
            }
            this.ff9vtrjwn24x = null;
            if (this.f260br8f048k && this.fr8js66i2ea != null) {
                this.fr8js66i2ea.endScrollbarDrag();
            }
            this.fr8js66i2ea = null;
            this.f89l46l9cnan = false;
            this.f260br8f048k = false;
            float fIntBitsToFloat = Float.intBitsToFloat(2143289344);
            this.fa2lr4aj54kv = fIntBitsToFloat;
            this.f3q5w9vf4kao = fIntBitsToFloat;
        }
        if (this.fft7ix2f1uyh && !this.fdv0b6zkflek) {
            this.fdzs16frq0fl = this.f5lr2smq58sw;
        }
        if (!this.fft7ix2f1uyh && this.fdv0b6zkflek) {
            if (this.fdzs16frq0fl != null && this.fdzs16frq0fl == this.f5lr2smq58sw) {
                this.fdzs16frq0fl.handleRightClick();
            }
            this.fdzs16frq0fl = null;
        }
        mb4hvy9zufns();
        return z;
    }

    private void m739ahj8h82r(ControlGetAnimPropertyService controlGetAnimPropertyService) {
        if (this.ff9ajfimnku5 != null && this.ff9ajfimnku5 != controlGetAnimPropertyService && this.ff9ajfimnku5.isListening()) {
            this.ff9ajfimnku5.acceptKey(256);
        }
        if (this.ff9ajfimnku5 != controlGetAnimPropertyService) {
            this.ff9ajfimnku5 = null;
        }
    }

    private ScenePctService<?> m7nsb8icmy0u(ScenePctService<?> scenePctService, float f, float f2) {
        return maxzee1yg4u3(scenePctService, f, f2, 1.0f, ScenePctService.PresentationTransform.IDENTITY);
    }

    private ScenePctService<?> maxzee1yg4u3(ScenePctService<?> scenePctService, float f, float f2, float f3, ScenePctService.PresentationTransform presentationTransform) {
        if (!scenePctService.visible || !scenePctService.pointerEvents) {
            return null;
        }
        float fMax = Math.max(0.0f, scenePctService.opacity) * f3;
        if (fMax < Float.intBitsToFloat(994352038)) {
            return null;
        }
        ScenePctService.PresentationTransform presentationTransform2 = scenePctService.presentationTransform(presentationTransform);
        float fMapX = presentationTransform2.mapX(scenePctService.cx);
        float fMapY = presentationTransform2.mapY(scenePctService.cy);
        float fScale = scenePctService.cw * presentationTransform2.scale();
        float fScale2 = scenePctService.ch * presentationTransform2.scale();
        if (scenePctService.clip && !mhwro40jvhdr(scenePctService, fMapX, fMapY, fScale, fScale2, presentationTransform2.scale(), f, f2)) {
            return null;
        }
        List<ScenePctService<?>> listChildren = scenePctService.children();
        int size = listChildren.size() - 1;
        while (true) {
            int i = size;
            if (i < 0) {
                if (!scenePctService.interactive || !mjju7w1iir77(fMapX, fMapY, fScale, fScale2, f, f2)) {
                    return null;
                }
                if (!scenePctService.isAutoInteractiveScroll() || scenePctService.hasScrollableOverflow()) {
                    return scenePctService;
                }
                return null;
            }
            ScenePctService<?> scenePctServiceMaxzee1yg4u3 = maxzee1yg4u3(listChildren.get(i), f, f2, fMax, presentationTransform2);
            if (scenePctServiceMaxzee1yg4u3 != null) {
                return scenePctServiceMaxzee1yg4u3;
            }
            size = i - 1;
        }
    }

    private static boolean mjju7w1iir77(float f, float f2, float f3, float f4, float f5, float f6) {
        return f3 > 0.0f && f4 > 0.0f && f5 >= f && f5 < f + f3 && f6 >= f2 && f6 < f2 + f4;
    }

    private static boolean mhwro40jvhdr(ScenePctService<?> scenePctService, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        float f8;
        if (!mjju7w1iir77(f, f2, f3, f4, f6, f7)) {
            return false;
        }
        if (!(scenePctService instanceof SceneCornerRadiusService)) {
            return true;
        }
        SceneCornerRadiusService sceneCornerRadiusService = (SceneCornerRadiusService) scenePctService;
        float fMax = Math.max(0.0f, sceneCornerRadiusService.cornerTL >= 0.0f ? sceneCornerRadiusService.cornerTL : sceneCornerRadiusService.cornerRadius) * f5;
        float fMax2 = Math.max(0.0f, sceneCornerRadiusService.cornerTR >= 0.0f ? sceneCornerRadiusService.cornerTR : sceneCornerRadiusService.cornerRadius) * f5;
        float fMax3 = Math.max(0.0f, sceneCornerRadiusService.cornerBR >= 0.0f ? sceneCornerRadiusService.cornerBR : sceneCornerRadiusService.cornerRadius) * f5;
        float fMax4 = Math.max(0.0f, sceneCornerRadiusService.cornerBL >= 0.0f ? sceneCornerRadiusService.cornerBL : sceneCornerRadiusService.cornerRadius) * f5;
        if (Math.max(Math.max(fMax, fMax2), Math.max(fMax3, fMax4)) <= 0.0f) {
            return true;
        }
        float fIntBitsToFloat = f3 * Float.intBitsToFloat(1056964608);
        float fIntBitsToFloat2 = f4 * Float.intBitsToFloat(1056964608);
        float f9 = f6 - (f + fIntBitsToFloat);
        float f10 = f7 - (f2 + fIntBitsToFloat2);
        if (f9 < 0.0f) {
            f8 = f10 < 0.0f ? fMax : fMax4;
        } else {
            f8 = f10 < 0.0f ? fMax2 : fMax3;
        }
        float fMin = Math.min(f8, Math.min(fIntBitsToFloat, fIntBitsToFloat2));
        float fAbs = (Math.abs(f9) - fIntBitsToFloat) + fMin;
        float fAbs2 = (Math.abs(f10) - fIntBitsToFloat2) + fMin;
        return (((float) Math.sqrt((double) ((Math.max(fAbs, 0.0f) * Math.max(fAbs, 0.0f)) + (Math.max(fAbs2, 0.0f) * Math.max(fAbs2, 0.0f))))) + Math.min(Math.max(fAbs, fAbs2), 0.0f)) - fMin <= 0.0f;
    }

    private static float mi6qfizzllu8(ScenePctService<?> scenePctService, float f) {
        return scenePctService.presentationTransform().inverseX(f);
    }

    private static float mdy0wcaemydv(ScenePctService<?> scenePctService, float f) {
        return scenePctService.presentationTransform().inverseY(f);
    }

    private static float m6dwcz2taw86(ScenePctService<?> scenePctService, float f) {
        return scenePctService.parent == null ? f : scenePctService.parent.presentationTransform().inverseX(f);
    }

    private static float mb6bqerjpdfe(ScenePctService<?> scenePctService, float f) {
        return scenePctService.parent == null ? f : scenePctService.parent.presentationTransform().inverseY(f);
    }

    private static float mc0t8l91q0cn(ScenePctService<?> scenePctService, float f, float f2) {
        return scenePctService.scrollsHorizontally() ? scenePctService.presentationTransform().inverseX(f) : scenePctService.presentationTransform().inverseY(f2);
    }

    private static ScenePctService<?> m2zrhynh8k7x(ScenePctService<?> scenePctService) {
        ScenePctService<?> scenePctService2 = scenePctService;
        while (true) {
            ScenePctService<?> scenePctService3 = scenePctService2;
            if (scenePctService3 == null) {
                return null;
            }
            if (scenePctService3.hasScrollableOverflow()) {
                return scenePctService3;
            }
            scenePctService2 = scenePctService3.parent;
        }
    }

    private static boolean macpy9qvkkns(ScenePctService<?> scenePctService, ScenePctService<?> scenePctService2) {
        ScenePctService<?> scenePctService3 = scenePctService;
        while (true) {
            ScenePctService<?> scenePctService4 = scenePctService3;
            if (scenePctService4 == null || scenePctService4 == scenePctService2) {
                break;
            }
            if (scenePctService4.draggable || scenePctService4.handlesContinuousPointer()) {
                return true;
            }
            scenePctService3 = scenePctService4.parent;
        }
        return scenePctService2.handlesContinuousPointer();
    }

    private ColorColorService ma85juy9wyeb(ScenePctService<?> scenePctService) {
        if (scenePctService instanceof ColorColorService) {
            ColorColorService colorColorService = (ColorColorService) scenePctService;
            if (colorColorService.isEyedropperActive()) {
                return colorColorService;
            }
        }
        Iterator<ScenePctService<?>> it = scenePctService.children().iterator();
        while (it.hasNext()) {
            ColorColorService colorColorServiceMa85juy9wyeb = ma85juy9wyeb(it.next());
            if (colorColorServiceMa85juy9wyeb != null) {
                return colorColorServiceMa85juy9wyeb;
            }
        }
        return null;
    }

    private ScenePctService<?> mc2sue2alnn2(ScenePctService<?> scenePctService) {
        while (scenePctService != null) {
            if (scenePctService.draggable) {
                return scenePctService;
            }
            scenePctService = scenePctService.parent;
        }
        return null;
    }

    private ScenePctService<?> miwo14q1pk7k(ScenePctService<?> scenePctService) {
        ScenePctService<?> scenePctService2 = scenePctService;
        while (scenePctService != null && scenePctService != this.fhzbc4bk6ke0) {
            scenePctService2 = scenePctService;
            scenePctService = scenePctService.parent;
        }
        if (scenePctService == this.fhzbc4bk6ke0) {
            return scenePctService2;
        }
        return null;
    }

    private void mdkphaivk69m(ScenePctService<?> scenePctService, float f, float f2) {
        LocalConfigRepository localConfigRepositoryConfig;
        float[] position;
        boolean z = (f == this.f2elqz8kijo1 && f2 == this.f3tv3g1jfyhr) ? false : true;
        if (scenePctService.isDraggable() && scenePctService.width > 0.0f && scenePctService.height > 0.0f) {
            float fIntBitsToFloat = scenePctService.width > 0.0f ? scenePctService.width : Float.intBitsToFloat(1120403456);
            float fIntBitsToFloat2 = scenePctService.height > 0.0f ? scenePctService.height : Float.intBitsToFloat(1112014848);
            if (z && scenePctService.getId() != null && CoreIsInitializedHandler.isReady() && (localConfigRepositoryConfig = CoreIsInitializedHandler.get().config()) != null && (position = localConfigRepositoryConfig.getPosition(scenePctService.getId(), f, f2, fIntBitsToFloat, fIntBitsToFloat2)) != null) {
                scenePctService.x = position[0];
                scenePctService.y = position[1];
            }
            scenePctService.x = Math.max(0.0f, Math.min(f - fIntBitsToFloat, scenePctService.x));
            scenePctService.y = Math.max(0.0f, Math.min(f2 - fIntBitsToFloat2, scenePctService.y));
        }
        Iterator<ScenePctService<?>> it = scenePctService.children().iterator();
        while (it.hasNext()) {
            mdkphaivk69m(it.next(), f, f2);
        }
    }

    private void mb4hvy9zufns() {
        long j;
        ScenePctService<?> scenePctService;
        ScenePctService.CursorStyle cursorStyle = ScenePctService.CursorStyle.DEFAULT;
        ScenePctService<?> scenePctServiceM2zrhynh8k7x = this.f260br8f048k ? this.fr8js66i2ea : m2zrhynh8k7x(this.f5lr2smq58sw);
        if (scenePctServiceM2zrhynh8k7x != null && (this.f260br8f048k || scenePctServiceM2zrhynh8k7x.hitScrollbar(this.fcj7abwjsytb, this.f8fi2qiucidm))) {
            cursorStyle = ScenePctService.CursorStyle.POINTER;
        }
        if (this.f5lr2smq58sw != null) {
            ScenePctService<?> scenePctServiceParent = this.f5lr2smq58sw;
            while (true) {
                scenePctService = scenePctServiceParent;
                if (scenePctService == null || scenePctService.cursorStyle() != null) {
                    break;
                } else {
                    scenePctServiceParent = scenePctService.parent();
                }
            }
            if (scenePctService != null && scenePctService.cursorStyle() != null) {
                cursorStyle = scenePctService.cursorStyle();
            } else if (cursorStyle == ScenePctService.CursorStyle.DEFAULT && this.f5lr2smq58sw.interactive) {
                cursorStyle = ScenePctService.CursorStyle.POINTER;
            } else if (this.f5lr2smq58sw instanceof ControlLetterSpacingService) {
                cursorStyle = ScenePctService.CursorStyle.TEXT;
            }
        }
        if (cursorStyle != this.fdt9op6rrdzg) {
            this.fdt9op6rrdzg = cursorStyle;
            if (Minecraft.getInstance() == null) {
                return;
            }
            long jWindowHandle = CompatAdapterService.windowHandle(Minecraft.getInstance());
            switch (cursorStyle) {
                case POINTER:
                    if (this.fhvp5dzgb245 == 0) {
                        this.fhvp5dzgb245 = GLFW.glfwCreateStandardCursor(221188);
                    }
                    j = this.fhvp5dzgb245;
                    break;
                case TEXT:
                    if (this.fiyvg5tgfpf3 == 0) {
                        this.fiyvg5tgfpf3 = GLFW.glfwCreateStandardCursor(221186);
                    }
                    j = this.fiyvg5tgfpf3;
                    break;
                case CROSSHAIR:
                    if (this.fak6h8r3upvo == 0) {
                        this.fak6h8r3upvo = GLFW.glfwCreateStandardCursor(221187);
                    }
                    j = this.fak6h8r3upvo;
                    break;
                default:
                    j = 0;
                    break;
            }
            GLFW.glfwSetCursor(jWindowHandle, j);
        }
    }

    private void m2dbbc6v3r27(ScenePctService<?> scenePctService) {
        if (scenePctService == null || scenePctService == this.fb9nut7rkbqo) {
            return;
        }
        ScenePctService<?> scenePctService2 = this.fb9nut7rkbqo;
        if (scenePctService2 instanceof SceneCornerRadiusService) {
            SceneCornerRadiusService sceneCornerRadiusService = (SceneCornerRadiusService) scenePctService2;
            if (sceneCornerRadiusService.visible) {
                sceneCornerRadiusService.animate("shadow", Float.intBitsToFloat(1086324736), fh3pvmeo3fdt);
            }
        }
        ScenePctService<?> scenePctServiceMiwo14q1pk7k = miwo14q1pk7k(scenePctService);
        if (scenePctServiceMiwo14q1pk7k != null) {
            this.fhzbc4bk6ke0.addChild(scenePctServiceMiwo14q1pk7k);
        }
        this.fb9nut7rkbqo = scenePctService;
        if (scenePctService instanceof SceneCornerRadiusService) {
            ((SceneCornerRadiusService) scenePctService).animate("shadow", Float.intBitsToFloat(1102053376), fh3pvmeo3fdt);
        }
    }
}
