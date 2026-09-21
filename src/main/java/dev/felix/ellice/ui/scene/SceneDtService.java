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
  private final CompositorPushPresentationScaleService compositorPushPresentationScaleService;
  private float fcj7abwjsytb;
  private float value2;
  private boolean fehya2u5nzu;
  private boolean enabled2;
  private boolean enabled3;
  private boolean enabled4;
  private ScenePctService<?> scenePctService;
  private ScenePctService<?> scenePctService2;
  private ScenePctService<?> scenePctService3;
  private ScenePctService<?> scenePctService4;
  private float value3;
  private float value4;
  private float value5;
  private float value6;
  private boolean enabled5;
  private ScenePctService<?> fr8js66i2ea;
  private float value7;
  private float value8;
  private boolean enabled6;
  private float value9;
  private boolean enabled7;
  private ScenePctService<?> scenePctService6;
  private float value10;
  private float f85e0dl6ag14;
  private String text2;
  private long timestamp;
  private long timestamp2;
  private long timestamp3;
  private long timestamp4;
  private ControlLetterSpacingService controlLetterSpacingService;
  private ControlGetAnimPropertyService controlGetAnimPropertyService;
  private ColorColorService fiirjmit3ppl;
  private int fegbblroo2ad;
  private boolean enabled8;
  private float f2elqz8kijo1;
  private float f3tv3g1jfyhr;
  private static final SceneEaseHandler.Spring spring =
      new SceneEaseHandler.Spring(
          Float.intBitsToFloat(1101004800), Float.intBitsToFloat(1120403456));
  private static final SceneEaseHandler.Spring spring2 =
      new SceneEaseHandler.Spring(
          Float.intBitsToFloat(1104150528), Float.intBitsToFloat(1142292480));
  private static float value13 = Float.intBitsToFloat(1015580809);
  private static final SceneEaseHandler.Spring spring3 =
      new SceneEaseHandler.Spring(
          Float.intBitsToFloat(1099956224), Float.intBitsToFloat(1132068864));
  private final LayoutContainerNode sceneComponent4 = new LayoutContainerNode();
  private final SceneCodec sceneCodec = new SceneCodec();
  private final MeasuredTextNode materialTextService2 = new MeasuredTextNode();
  private final MaterialPrepareService materialPrepareService = new MaterialPrepareService();
  private final SceneRenderer renderer = new SceneRenderer();
  private ScenePctService.CursorStyle cursorStyle2 = ScenePctService.CursorStyle.DEFAULT;
  private float value12 = 1.0f;
  private int fdhzunsjr1kr = -1;
  private int count3 = ThemeIsSetService.UNSET_COLOR;
  private float fedi4sljrmnd = Float.intBitsToFloat(2143289344);
  private float value15 = Float.intBitsToFloat(2143289344);

  public static float dt() {
    return value13;
  }

  public SceneDtService(
      CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
    this.compositorPushPresentationScaleService = compositorPushPresentationScaleService;
    this.sceneComponent4.direction(ScenePctService.Direction.NONE);
  }

  public LayoutContainerNode root() {
    return this.sceneComponent4;
  }

  public SceneRenderer toasts() {
    return this.renderer;
  }

  public SceneCodec transitions() {
    return this.sceneCodec;
  }

  public ScenePctService<?> findById(String str) {
    return this.sceneComponent4.findById(str);
  }

  public void shutdown() {
    this.sceneCodec.clear();
    this.sceneComponent4.clearChildren();
    if (this.timestamp2 != 0) {
      GLFW.glfwDestroyCursor(this.timestamp2);
      this.timestamp2 = 0L;
    }
    if (this.timestamp3 != 0) {
      GLFW.glfwDestroyCursor(this.timestamp3);
      this.timestamp3 = 0L;
    }
    if (this.timestamp4 != 0) {
      GLFW.glfwDestroyCursor(this.timestamp4);
      this.timestamp4 = 0L;
    }
    if (this.timestamp != 0) {
      GLFW.glfwDestroyCursor(this.timestamp);
      this.timestamp = 0L;
    }
    try {
      GLFW.glfwSetCursor(CompatAdapterService.windowHandle(Minecraft.getInstance()), 0L);
    } catch (Exception e) {
    }
    this.cursorStyle2 = ScenePctService.CursorStyle.DEFAULT;
  }

  public boolean hasInteractiveAt(float f, float f2) {
    return createScenePctService3(this.sceneComponent4, f, f2) != null;
  }

  public void mouseInput(float f, float f2, boolean z, boolean z2) {
    this.fcj7abwjsytb = f;
    this.value2 = f2;
    this.enabled2 = this.fehya2u5nzu;
    this.fehya2u5nzu = z;
    this.enabled4 = this.enabled3;
    this.enabled3 = z2;
  }

  public void charTyped(char c) {
    if (this.controlLetterSpacingService == null || !this.controlLetterSpacingService.focused()) {
      return;
    }
    this.controlLetterSpacingService.charTyped(c);
  }

  public boolean keyPressed(int i) {
    return keyPressed(i, 0);
  }

  public boolean keyPressed(int i, int i2) {
    if (this.controlGetAnimPropertyService != null
        && this.controlGetAnimPropertyService.isListening()) {
      this.controlGetAnimPropertyService.acceptKey(i);
      this.controlGetAnimPropertyService = null;
      return true;
    }
    if (this.controlLetterSpacingService == null || !this.controlLetterSpacingService.focused()) {
      return false;
    }
    this.controlLetterSpacingService.keyPressed(i, i2);
    return true;
  }

  public void paste(String str) {
    if (this.controlLetterSpacingService == null || !this.controlLetterSpacingService.focused()) {
      return;
    }
    this.controlLetterSpacingService.paste(str);
  }

  public String copy() {
    if (this.controlLetterSpacingService == null || !this.controlLetterSpacingService.focused()) {
      return null;
    }
    return this.controlLetterSpacingService.copy();
  }

  public void cut() {
    if (this.controlLetterSpacingService == null || !this.controlLetterSpacingService.focused()) {
      return;
    }
    this.controlLetterSpacingService.cut();
  }

  public void selectAll() {
    if (this.controlLetterSpacingService == null || !this.controlLetterSpacingService.focused()) {
      return;
    }
    this.controlLetterSpacingService.selectAll();
  }

  public boolean hasFocusedInput() {
    return (this.controlLetterSpacingService != null && this.controlLetterSpacingService.focused())
        || (this.controlGetAnimPropertyService != null
            && this.controlGetAnimPropertyService.isListening());
  }

  public void clearFocus() {
    if (this.controlLetterSpacingService != null) {
      this.controlLetterSpacingService.unfocus();
      this.controlLetterSpacingService = null;
    }
    updateState4(null);
  }

  public void setFocusedInput(ControlLetterSpacingService controlLetterSpacingService) {
    if (this.controlLetterSpacingService == controlLetterSpacingService) {
      if (controlLetterSpacingService != null) {
        controlLetterSpacingService.focus();
      }
    } else {
      if (this.controlLetterSpacingService != null) {
        this.controlLetterSpacingService.unfocus();
      }
      this.controlLetterSpacingService = controlLetterSpacingService;
      if (controlLetterSpacingService != null) {
        controlLetterSpacingService.focus();
      }
    }
  }

  public void mouseScroll(float f) {
    ScenePctService<?> scenePctServiceMd5cvmflypzx;
    ScenePctService<?> scenePctServiceM7nsb8icmy0u =
        createScenePctService3(this.sceneComponent4, this.fcj7abwjsytb, this.value2);
    ScenePctService<?> scenePctService = scenePctServiceM7nsb8icmy0u;
    while (true) {
      ScenePctService<?> scenePctService2 = scenePctService;
      if (scenePctService2 == null) {
        break;
      }
      if (scenePctService2.hitScrollbar(this.fcj7abwjsytb, this.value2)) {
        scenePctServiceM7nsb8icmy0u = scenePctService2;
      }
      scenePctService = scenePctService2.parent;
    }
    if ((scenePctServiceM7nsb8icmy0u == null || !scenePctServiceM7nsb8icmy0u.handleScroll(f))
        && (scenePctServiceMd5cvmflypzx =
                md5cvmflypzx(this.sceneComponent4, this.fcj7abwjsytb, this.value2))
            != null) {
      scenePctServiceMd5cvmflypzx.applyScrollDelta(
          f * Float.intBitsToFloat(1103101952) * ThemeCornerData.current().scrollSpeed(), spring);
    }
  }

  private ScenePctService<?> md5cvmflypzx(ScenePctService<?> scenePctService, float f, float f2) {
    return createScenePctService2(
        scenePctService, f, f2, 1.0f, ScenePctService.PresentationTransform.IDENTITY);
  }

  private ScenePctService<?> createScenePctService2(
      ScenePctService<?> scenePctService,
      float f,
      float f2,
      float f3,
      ScenePctService.PresentationTransform presentationTransform) {
    if (!scenePctService.visible || !scenePctService.pointerEvents) {
      return null;
    }
    float fMax = Math.max(0.0f, scenePctService.opacity) * f3;
    if (fMax < Float.intBitsToFloat(994352038)) {
      return null;
    }
    ScenePctService.PresentationTransform presentationTransform2 =
        scenePctService.presentationTransform(presentationTransform);
    float fMapX = presentationTransform2.mapX(scenePctService.cx);
    float fMapY = presentationTransform2.mapY(scenePctService.cy);
    float fScale = scenePctService.cw * presentationTransform2.scale();
    float fScale2 = scenePctService.ch * presentationTransform2.scale();
    if (scenePctService.clip
        && !checkCondition3(
            scenePctService,
            fMapX,
            fMapY,
            fScale,
            fScale2,
            presentationTransform2.scale(),
            f,
            f2)) {
      return null;
    }
    List<ScenePctService<?>> listChildren = scenePctService.children();
    ScenePctService<?> scenePctService2 = null;
    int size = listChildren.size() - 1;
    while (true) {
      int i = size;
      if (i >= 0) {
        ScenePctService<?> scenePctServiceMbseyvrjdg20 =
            createScenePctService2(listChildren.get(i), f, f2, fMax, presentationTransform2);
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
    return (scenePctService.scrollable && checkCondition2(fMapX, fMapY, fScale, fScale2, f, f2))
        ? scenePctService
        : scenePctService2;
  }

  public void setFramebufferInfo(float f, int i) {
    this.value12 = f;
    this.fegbblroo2ad = i;
  }

  public void frame(float f, float f2) {
    frame(Float.intBitsToFloat(1015580809), f, f2);
  }

  public void frame(float f, float f2, float f3) {
    if (this.controlLetterSpacingService != null && !this.controlLetterSpacingService.focused()) {
      this.controlLetterSpacingService = null;
    }
    if (this.controlGetAnimPropertyService != null
        && !this.controlGetAnimPropertyService.isListening()) {
      this.controlGetAnimPropertyService = null;
    }
    if (!Float.isFinite(f) || f < 0.0f) {
      f = 0.0f;
    }
    if (f > Float.intBitsToFloat(1036831949)) {
      f = Float.intBitsToFloat(1036831949);
    }
    value13 = f;
    this.sceneComponent4.tickAnimations(f);
    this.sceneCodec.tick(f);
    updateState5(this.sceneComponent4, f2, f3);
    this.f2elqz8kijo1 = f2;
    this.f3tv3g1jfyhr = f3;
    updateState(f2, f3);
    if (currentEnabled() || ScenePctService.layoutVersion() != this.count3) {
      updateState2(f2, f3);
    }
    this.compositorPushPresentationScaleService.pointer(
        this.fcj7abwjsytb, this.value2, this.fehya2u5nzu);
    this.sceneComponent4.drawTree(this.compositorPushPresentationScaleService, 1.0f);
    this.renderer.tick(f, f2, f3);
    if (this.renderer.hasToasts()) {
      this.compositorPushPresentationScaleService.afterFlush(
          () -> {
            this.renderer.render(this.compositorPushPresentationScaleService);
          });
    }
    updateState3(f, f2, f3);
  }

  private void updateState(float f, float f2) {
    if (ScenePctService.layoutVersion() == this.count3
        && this.compositorPushPresentationScaleService.fontMetricsRevision() == this.fdhzunsjr1kr
        && Float.floatToIntBits(f) == Float.floatToIntBits(this.fedi4sljrmnd)
        && Float.floatToIntBits(f2) == Float.floatToIntBits(this.value15)
        && this.compositorPushPresentationScaleService.isInitialized() == this.enabled8) {
      return;
    }
    updateState2(f, f2);
  }

  private void updateState2(float f, float f2) {
    boolean z = false;
    int i = 0;
    while (true) {
      int i2 = i;
      if (i2 >= 3) {
        break;
      }
      int iLayoutVersion = ScenePctService.layoutVersion();
      this.sceneComponent4.performLayout(
          this.compositorPushPresentationScaleService, 0.0f, 0.0f, f, f2);
      if (ScenePctService.layoutVersion() == iLayoutVersion) {
        z = true;
        break;
      }
      i = i2 + 1;
    }
    this.count3 = z ? ScenePctService.layoutVersion() : ThemeIsSetService.UNSET_COLOR;
    this.fedi4sljrmnd = f;
    this.value15 = f2;
    this.enabled8 = this.compositorPushPresentationScaleService.isInitialized();
    this.fdhzunsjr1kr = this.compositorPushPresentationScaleService.fontMetricsRevision();
  }

  private void updateState3(float f, float f2, float f3) {
    ThemeCornerData themeCornerDataCurrent = ThemeCornerData.current();
    if (!themeCornerDataCurrent.tooltips()) {
      this.f85e0dl6ag14 = 0.0f;
      this.value10 = 0.0f;
      this.text2 = null;
      return;
    }
    String strResolveTooltip = resolveTooltip(this.scenePctService);
    if (!Objects.equals(strResolveTooltip, this.text2)) {
      this.f85e0dl6ag14 = 0.0f;
      this.value10 = 0.0f;
      this.text2 = strResolveTooltip;
    }
    if (strResolveTooltip == null || strResolveTooltip.isEmpty()) {
      this.value10 = 0.0f;
      this.f85e0dl6ag14 =
          Math.max(0.0f, this.f85e0dl6ag14 - (f * Float.intBitsToFloat(1094713344)));
    } else {
      this.value10 += f;
      if (this.value10 >= themeCornerDataCurrent.tooltipDelay()) {
        this.f85e0dl6ag14 =
            Math.min(1.0f, this.f85e0dl6ag14 + (f * Float.intBitsToFloat(1090519040)));
      }
    }
    if (this.f85e0dl6ag14 < Float.intBitsToFloat(1008981770)
        || strResolveTooltip == null
        || strResolveTooltip.isEmpty()) {
      return;
    }
    float fIntBitsToFloat = this.f85e0dl6ag14 * Float.intBitsToFloat(1064514355);
    float f4 = this.fcj7abwjsytb;
    float f5 = this.value2;
    boolean z = false;
    ScenePctService<?> scenePctServiceParent = this.scenePctService;
    while (true) {
      ScenePctService<?> scenePctService = scenePctServiceParent;
      if (scenePctService != null) {
        if ((scenePctService instanceof MaterialTerrainTooltipsService)
            && ((MaterialTerrainTooltipsService) scenePctService).terrainTooltips()) {
          z = true;
          break;
        }
        scenePctServiceParent = scenePctService.parent();
      } else {
        break;
      }
    }
    if (z) {
      this.compositorPushPresentationScaleService.afterFlush(
          () -> {
            this.materialPrepareService.prepare(
                this.compositorPushPresentationScaleService,
                strResolveTooltip,
                Math.max(Float.intBitsToFloat(1113587712), f2 - Float.intBitsToFloat(1098907648)));
            this.materialPrepareService.tickAnimations(f);
            float fBubbleWidth = this.materialPrepareService.bubbleWidth();
            float fBubbleHeight = this.materialPrepareService.bubbleHeight();
            float fMiqwwqmazdgw =
                miqwwqmazdgw(
                    f4 - (fBubbleWidth * Float.intBitsToFloat(1056964608)),
                    Float.intBitsToFloat(1090519040),
                    Math.max(
                        Float.intBitsToFloat(1090519040),
                        (f2 - fBubbleWidth) - Float.intBitsToFloat(1090519040)));
            boolean z2 =
                (f5 - fBubbleHeight) - Float.intBitsToFloat(1098907648)
                    >= Float.intBitsToFloat(1090519040);
            float fIntBitsToFloat2 =
                z2
                    ? (f5 - fBubbleHeight) - Float.intBitsToFloat(1098907648)
                    : Math.min(
                        (f3 - fBubbleHeight) - Float.intBitsToFloat(1090519040),
                        f5 + Float.intBitsToFloat(1101004800));
            this.materialPrepareService.anchor(f4 - fMiqwwqmazdgw, z2);
            this.materialPrepareService.performLayout(
                this.compositorPushPresentationScaleService,
                fMiqwwqmazdgw,
                Math.max(Float.intBitsToFloat(1090519040), fIntBitsToFloat2),
                fBubbleWidth,
                fBubbleHeight);
            this.materialPrepareService.drawTree(
                this.compositorPushPresentationScaleService,
                Math.min(1.0f, fIntBitsToFloat / Float.intBitsToFloat(1064514355)));
          });
      return;
    }
    boolean z2 = false;
    ScenePctService<?> scenePctServiceParent2 = this.scenePctService;
    while (true) {
      ScenePctService<?> scenePctService2 = scenePctServiceParent2;
      if (scenePctService2 == null) {
        break;
      }
      if ((scenePctService2 instanceof MaterialTerrainTooltipsService)
          || "clickgui.panel-shell".equals(scenePctService2.getId())
          || "account-manager.panel-shell".equals(scenePctService2.getId())) {
        z2 = true;
        break;
      }
      scenePctServiceParent2 = scenePctService2.parent();
    }
    if (z2) {
      this.compositorPushPresentationScaleService.afterFlush(
          () -> {
            this.materialTextService2.text(strResolveTooltip);
            float fMin =
                Math.min(
                    f2 - Float.intBitsToFloat(1098907648),
                    this.materialTextService2.intrinsicWidth(
                        this.compositorPushPresentationScaleService));
            float fIntrinsicHeight =
                this.materialTextService2.intrinsicHeight(
                    this.compositorPushPresentationScaleService);
            float fMiqwwqmazdgw =
                miqwwqmazdgw(
                    f4 + Float.intBitsToFloat(1092616192),
                    Float.intBitsToFloat(1090519040),
                    Math.max(
                        Float.intBitsToFloat(1090519040),
                        (f2 - fMin) - Float.intBitsToFloat(1090519040)));
            float fIntBitsToFloat2 =
                (f5 + Float.intBitsToFloat(1101004800)) + fIntrinsicHeight
                        < f3 - Float.intBitsToFloat(1090519040)
                    ? f5 + Float.intBitsToFloat(1101004800)
                    : Math.max(
                        Float.intBitsToFloat(1090519040),
                        (f5 - fIntrinsicHeight) - Float.intBitsToFloat(1094713344));
            this.materialTextService2.opacity(
                Math.min(1.0f, fIntBitsToFloat / Float.intBitsToFloat(1064514355)));
            this.materialTextService2.performLayout(
                this.compositorPushPresentationScaleService,
                fMiqwwqmazdgw,
                fIntBitsToFloat2,
                fMin,
                fIntrinsicHeight);
            this.materialTextService2.drawTree(this.compositorPushPresentationScaleService, 1.0f);
          });
    } else {
      this.compositorPushPresentationScaleService.afterFlush(
          () -> {
            float fIntBitsToFloat2 = Float.intBitsToFloat(1088421888);
            float fIntBitsToFloat3 = Float.intBitsToFloat(1088421888);
            float fIntBitsToFloat4 = Float.intBitsToFloat(1084227584);
            List<String> listWrapTooltipLines =
                wrapTooltipLines(
                    this.compositorPushPresentationScaleService,
                    strResolveTooltip,
                    fIntBitsToFloat2,
                    Math.max(
                        Float.intBitsToFloat(1117782016),
                        Math.min(
                            Float.intBitsToFloat(1131413504),
                            (f2 - Float.intBitsToFloat(1098907648)) - (fIntBitsToFloat3 * 2.0f))),
                    4);
            float fMax = 0.0f;
            Iterator<String> it = listWrapTooltipLines.iterator();
            while (it.hasNext()) {
              fMax =
                  Math.max(
                      fMax,
                      this.compositorPushPresentationScaleService.textWidth(
                          it.next(), fIntBitsToFloat2));
            }
            float fTextLineHeight =
                this.compositorPushPresentationScaleService.textLineHeight(fIntBitsToFloat2);
            float f6 = fMax + (fIntBitsToFloat3 * 2.0f);
            float size =
                (fTextLineHeight * listWrapTooltipLines.size())
                    + (1.0f * Math.max(0, listWrapTooltipLines.size() - 1))
                    + (fIntBitsToFloat4 * 2.0f);
            float fMiqwwqmazdgw =
                miqwwqmazdgw(
                    f4 + Float.intBitsToFloat(1092616192),
                    Float.intBitsToFloat(1082130432),
                    Math.max(
                        Float.intBitsToFloat(1082130432),
                        (f2 - f6) - Float.intBitsToFloat(1082130432)));
            float fIntBitsToFloat5 = f5 + Float.intBitsToFloat(1096810496);
            float fMiqwwqmazdgw2 =
                fIntBitsToFloat5 + size <= f3 - Float.intBitsToFloat(1082130432)
                    ? fIntBitsToFloat5
                    : miqwwqmazdgw(
                        (f5 - size) - Float.intBitsToFloat(1092616192),
                        Float.intBitsToFloat(1082130432),
                        Math.max(
                            Float.intBitsToFloat(1082130432),
                            (f3 - size) - Float.intBitsToFloat(1082130432)));
            this.compositorPushPresentationScaleService.roundedRect(
                fMiqwwqmazdgw,
                fMiqwwqmazdgw2,
                f6,
                size,
                Float.intBitsToFloat(1084227584),
                (((int) (fIntBitsToFloat * Float.intBitsToFloat(1131413504))) << 24) | 1710628);
            this.compositorPushPresentationScaleService.roundedRect(
                fMiqwwqmazdgw,
                fMiqwwqmazdgw2,
                f6,
                size,
                Float.intBitsToFloat(1084227584),
                Float.intBitsToFloat(1084227584),
                Float.intBitsToFloat(1084227584),
                Float.intBitsToFloat(1084227584),
                0,
                0.0f,
                0.0f,
                0,
                Float.intBitsToFloat(1056964608),
                (((int) (fIntBitsToFloat * Float.intBitsToFloat(1111490560))) << 24) | 16777215);
            float f7 = fMiqwwqmazdgw2 + fIntBitsToFloat4;
            Iterator<String> it2 = listWrapTooltipLines.iterator();
            while (it2.hasNext()) {
              this.compositorPushPresentationScaleService.text(
                  fMiqwwqmazdgw + fIntBitsToFloat3,
                  f7,
                  it2.next(),
                  fIntBitsToFloat2,
                  (((int) (fIntBitsToFloat * Float.intBitsToFloat(1130168320))) << 24) | 16777215);
              f7 += fTextLineHeight + 1.0f;
            }
          });
    }
  }

  static List<String> wrapTooltipLines(
      CompositorPushPresentationScaleService compositorPushPresentationScaleService,
      String str,
      float f,
      float f2,
      int i) {
    if (str == null || str.isBlank() || i <= 0) {
      return List.of();
    }
    ArrayList arrayList = new ArrayList();
    String[] strArrSplit = str.strip().split("\\R", -1);
    boolean z = false;
    int i2 = 0;
    loop0:
    while (true) {
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
            int iMe2sq53381c7 = calculateValue(compositorPushPresentationScaleService, str3, f, f2);
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
      arrayList.set(
          size,
          createText(compositorPushPresentationScaleService, (String) arrayList.get(size), f, f2));
    }
    return List.copyOf(arrayList);
  }

  private static int calculateValue(
      CompositorPushPresentationScaleService compositorPushPresentationScaleService,
      String str,
      float f,
      float f2) {
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

  private static String createText(
      CompositorPushPresentationScaleService compositorPushPresentationScaleService,
      String str,
      float f,
      float f2) {
    int i;
    if (compositorPushPresentationScaleService.textWidth(str + "…", f) <= f2) {
      return str + "…";
    }
    int length = str.length();
    while (true) {
      i = length;
      if (i <= 0
          || compositorPushPresentationScaleService.textWidth(
                  str.substring(0, i).stripTrailing() + "…", f)
              <= f2) {
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

  private boolean currentEnabled() {
    boolean z = false;
    ScenePctService<?> scenePctServiceM7nsb8icmy0u =
        createScenePctService3(this.sceneComponent4, this.fcj7abwjsytb, this.value2);
    ScenePctService<?> scenePctService = scenePctServiceM7nsb8icmy0u;
    while (true) {
      ScenePctService<?> scenePctService2 = scenePctService;
      if (scenePctService2 == null) {
        break;
      }
      if (scenePctService2.hitScrollbar(this.fcj7abwjsytb, this.value2)) {
        scenePctServiceM7nsb8icmy0u = scenePctService2;
      }
      scenePctService = scenePctService2.parent;
    }
    if (this.scenePctService != scenePctServiceM7nsb8icmy0u) {
      if (this.scenePctService != null) {
        this.scenePctService.hovered = false;
        this.scenePctService.animate("hoverProgress", 0.0f, spring2);
        if (this.scenePctService.onHoverChange != null) {
          this.scenePctService.onHoverChange.accept(false);
        }
      }
      this.scenePctService = scenePctServiceM7nsb8icmy0u;
      if (this.scenePctService != null) {
        this.scenePctService.hovered = true;
        this.scenePctService.animate("hoverProgress", 1.0f, spring2);
        if (this.scenePctService.onHoverChange != null) {
          this.scenePctService.onHoverChange.accept(true);
        }
      }
      mb4hvy9zufns();
    }
    if (this.fehya2u5nzu && !this.enabled2) {
      this.enabled5 = false;
      this.value5 = this.fcj7abwjsytb;
      this.value6 = this.value2;
      if (this.fiirjmit3ppl != null) {
        this.fiirjmit3ppl.eyedropperPick(
            this.compositorPushPresentationScaleService.readPixel(
                (int) (this.fcj7abwjsytb * this.value12),
                (int) (this.value2 * this.value12),
                this.fegbblroo2ad));
        this.fiirjmit3ppl = null;
        return true;
      }
      ColorColorService colorColorServiceMa85juy9wyeb =
          createColorColorService(this.sceneComponent4);
      if (colorColorServiceMa85juy9wyeb != null) {
        this.fiirjmit3ppl = colorColorServiceMa85juy9wyeb;
      }
      if (this.controlLetterSpacingService != null
          && this.scenePctService != this.controlLetterSpacingService) {
        this.controlLetterSpacingService.unfocus();
        this.controlLetterSpacingService = null;
      }
      if (this.controlGetAnimPropertyService != null
          && this.scenePctService != this.controlGetAnimPropertyService) {
        updateState4(null);
      }
      this.scenePctService2 = this.scenePctService;
      if (this.scenePctService2 != null) {
        this.scenePctService2.pressed = true;
        this.scenePctService2.animate("pressProgress", 1.0f, spring2);
        this.scenePctService2.onPress(
            calculateValue3(this.scenePctService2, this.fcj7abwjsytb),
            mdy0wcaemydv(this.scenePctService2, this.value2));
        this.scenePctService2.onScenePress(this.fcj7abwjsytb, this.value2);
        ScenePctService<?> scenePctService3 = this.scenePctService2;
        if (scenePctService3 instanceof ControlLetterSpacingService) {
          this.controlLetterSpacingService = (ControlLetterSpacingService) scenePctService3;
        }
        if (this.scenePctService2.draggable) {
          this.scenePctService4 = this.scenePctService2;
          float fM6dwcz2taw86 = calculateValue5(this.scenePctService4, this.fcj7abwjsytb);
          float fMb6bqerjpdfe = mb6bqerjpdfe(this.scenePctService4, this.value2);
          if (Float.isFinite(fM6dwcz2taw86) && Float.isFinite(this.scenePctService4.x)) {
            this.value3 = fM6dwcz2taw86 - this.scenePctService4.x;
          }
          if (Float.isFinite(fMb6bqerjpdfe) && Float.isFinite(this.scenePctService4.y)) {
            this.value4 = fMb6bqerjpdfe - this.scenePctService4.y;
          }
        }
      }
      this.fr8js66i2ea = createScenePctService5(this.scenePctService2);
      this.enabled6 = false;
      this.enabled7 = false;
      if (this.fr8js66i2ea != null) {
        if (this.fr8js66i2ea.hitScrollbar(this.fcj7abwjsytb, this.value2)) {
          this.enabled7 = true;
          this.enabled6 = true;
          this.enabled5 = true;
          this.fr8js66i2ea.beginScrollbarDrag();
          this.value9 = this.fr8js66i2ea.scrollbarGrabOffset(this.fcj7abwjsytb, this.value2);
        } else if (macpy9qvkkns(this.scenePctService2, this.fr8js66i2ea)) {
          this.fr8js66i2ea = null;
        } else {
          this.value7 = calculateValue7(this.fr8js66i2ea, this.fcj7abwjsytb, this.value2);
          this.value8 = this.fr8js66i2ea.scrollY();
        }
      }
      updateState7(createScenePctService6(scenePctServiceM7nsb8icmy0u));
    }
    if (this.fehya2u5nzu
        && this.scenePctService2 != null
        && !this.enabled5
        && (Math.abs(this.fcj7abwjsytb - this.value5) > 2.0f
            || Math.abs(this.value2 - this.value6) > 2.0f)) {
      this.enabled5 = true;
    }
    if (this.fehya2u5nzu
        && this.scenePctService2 != null
        && this.scenePctService4 == null
        && this.fr8js66i2ea != null
        && !this.enabled6) {
      float fMc0t8l91q0cn =
          calculateValue7(this.fr8js66i2ea, this.fcj7abwjsytb, this.value2) - this.value7;
      float f =
          this.fr8js66i2ea.scrollsHorizontally()
              ? this.value2 - this.value6
              : this.fcj7abwjsytb - this.value5;
      if (Math.abs(fMc0t8l91q0cn) > Float.intBitsToFloat(1082130432)
          && Math.abs(fMc0t8l91q0cn) >= Math.abs(f) * Float.intBitsToFloat(1059481190)) {
        this.enabled6 = true;
        this.enabled5 = true;
        this.fr8js66i2ea.beginPointerScroll();
      }
    }
    if (this.fehya2u5nzu && this.enabled6 && this.fr8js66i2ea != null && !this.enabled7) {
      this.fr8js66i2ea.dragScrollTo(
          this.value8
              + (calculateValue7(this.fr8js66i2ea, this.fcj7abwjsytb, this.value2) - this.value7));
    }
    if (this.fehya2u5nzu && this.enabled7 && this.fr8js66i2ea != null) {
      this.fr8js66i2ea.dragScrollbarTo(this.fcj7abwjsytb, this.value2, this.value9);
    }
    if (this.fehya2u5nzu
        && this.scenePctService2 != null
        && this.scenePctService4 == null
        && !this.enabled6) {
      this.scenePctService2.updateWhilePressed(
          calculateValue3(this.scenePctService2, this.fcj7abwjsytb),
          mdy0wcaemydv(this.scenePctService2, this.value2));
      this.scenePctService2.updateWhileScenePressed(this.fcj7abwjsytb, this.value2);
    }
    if (this.fehya2u5nzu && this.scenePctService4 != null) {
      float fM6dwcz2taw87 = calculateValue5(this.scenePctService4, this.fcj7abwjsytb) - this.value3;
      float fMb6bqerjpdfe2 = mb6bqerjpdfe(this.scenePctService4, this.value2) - this.value4;
      if (this.enabled5 && Float.isFinite(fM6dwcz2taw87) && Float.isFinite(fMb6bqerjpdfe2)) {
        this.scenePctService4.x = fM6dwcz2taw87;
        this.scenePctService4.y = fMb6bqerjpdfe2;
        this.scenePctService4.invalidate();
        z = true;
      }
    }
    if (!this.fehya2u5nzu && this.enabled2) {
      if (this.scenePctService2 != null) {
        this.scenePctService2.pressed = false;
        this.scenePctService2.animate("pressProgress", 0.0f, spring2);
        boolean z2 = !this.enabled5 && this.scenePctService2 == this.scenePctService;
        this.scenePctService2.onRelease(z2);
        if (z2) {
          ScenePctService<?> scenePctService4 = this.scenePctService2;
          scenePctService4.handleClick();
          if (scenePctService4 instanceof ControlGetAnimPropertyService) {
            ControlGetAnimPropertyService controlGetAnimPropertyService =
                (ControlGetAnimPropertyService) scenePctService4;
            if (controlGetAnimPropertyService.isListening()) {
              updateState4(controlGetAnimPropertyService);
              this.controlGetAnimPropertyService = controlGetAnimPropertyService;
            } else if (this.controlGetAnimPropertyService == controlGetAnimPropertyService) {
              this.controlGetAnimPropertyService = null;
            }
          }
        }
        this.scenePctService2 = null;
      }
      this.scenePctService4 = null;
      if (this.enabled7 && this.fr8js66i2ea != null) {
        this.fr8js66i2ea.endScrollbarDrag();
      }
      this.fr8js66i2ea = null;
      this.enabled6 = false;
      this.enabled7 = false;
      float fIntBitsToFloat = Float.intBitsToFloat(2143289344);
      this.value6 = fIntBitsToFloat;
      this.value5 = fIntBitsToFloat;
    }
    if (this.enabled3 && !this.enabled4) {
      this.scenePctService3 = this.scenePctService;
    }
    if (!this.enabled3 && this.enabled4) {
      if (this.scenePctService3 != null && this.scenePctService3 == this.scenePctService) {
        this.scenePctService3.handleRightClick();
      }
      this.scenePctService3 = null;
    }
    mb4hvy9zufns();
    return z;
  }

  private void updateState4(ControlGetAnimPropertyService controlGetAnimPropertyService) {
    if (this.controlGetAnimPropertyService != null
        && this.controlGetAnimPropertyService != controlGetAnimPropertyService
        && this.controlGetAnimPropertyService.isListening()) {
      this.controlGetAnimPropertyService.acceptKey(256);
    }
    if (this.controlGetAnimPropertyService != controlGetAnimPropertyService) {
      this.controlGetAnimPropertyService = null;
    }
  }

  private ScenePctService<?> createScenePctService3(
      ScenePctService<?> scenePctService, float f, float f2) {
    return createScenePctService4(
        scenePctService, f, f2, 1.0f, ScenePctService.PresentationTransform.IDENTITY);
  }

  private ScenePctService<?> createScenePctService4(
      ScenePctService<?> scenePctService,
      float f,
      float f2,
      float f3,
      ScenePctService.PresentationTransform presentationTransform) {
    if (!scenePctService.visible || !scenePctService.pointerEvents) {
      return null;
    }
    float fMax = Math.max(0.0f, scenePctService.opacity) * f3;
    if (fMax < Float.intBitsToFloat(994352038)) {
      return null;
    }
    ScenePctService.PresentationTransform presentationTransform2 =
        scenePctService.presentationTransform(presentationTransform);
    float fMapX = presentationTransform2.mapX(scenePctService.cx);
    float fMapY = presentationTransform2.mapY(scenePctService.cy);
    float fScale = scenePctService.cw * presentationTransform2.scale();
    float fScale2 = scenePctService.ch * presentationTransform2.scale();
    if (scenePctService.clip
        && !checkCondition3(
            scenePctService,
            fMapX,
            fMapY,
            fScale,
            fScale2,
            presentationTransform2.scale(),
            f,
            f2)) {
      return null;
    }
    List<ScenePctService<?>> listChildren = scenePctService.children();
    int size = listChildren.size() - 1;
    while (true) {
      int i = size;
      if (i < 0) {
        if (!scenePctService.interactive
            || !checkCondition2(fMapX, fMapY, fScale, fScale2, f, f2)) {
          return null;
        }
        if (!scenePctService.isAutoInteractiveScroll() || scenePctService.hasScrollableOverflow()) {
          return scenePctService;
        }
        return null;
      }
      ScenePctService<?> scenePctServiceMaxzee1yg4u3 =
          createScenePctService4(listChildren.get(i), f, f2, fMax, presentationTransform2);
      if (scenePctServiceMaxzee1yg4u3 != null) {
        return scenePctServiceMaxzee1yg4u3;
      }
      size = i - 1;
    }
  }

  private static boolean checkCondition2(
      float f, float f2, float f3, float f4, float f5, float f6) {
    return f3 > 0.0f && f4 > 0.0f && f5 >= f && f5 < f + f3 && f6 >= f2 && f6 < f2 + f4;
  }

  private static boolean checkCondition3(
      ScenePctService<?> scenePctService,
      float f,
      float f2,
      float f3,
      float f4,
      float f5,
      float f6,
      float f7) {
    float f8;
    if (!checkCondition2(f, f2, f3, f4, f6, f7)) {
      return false;
    }
    if (!(scenePctService instanceof SceneCornerRadiusService)) {
      return true;
    }
    SceneCornerRadiusService sceneCornerRadiusService = (SceneCornerRadiusService) scenePctService;
    float fMax =
        Math.max(
                0.0f,
                sceneCornerRadiusService.cornerTL >= 0.0f
                    ? sceneCornerRadiusService.cornerTL
                    : sceneCornerRadiusService.cornerRadius)
            * f5;
    float fMax2 =
        Math.max(
                0.0f,
                sceneCornerRadiusService.cornerTR >= 0.0f
                    ? sceneCornerRadiusService.cornerTR
                    : sceneCornerRadiusService.cornerRadius)
            * f5;
    float fMax3 =
        Math.max(
                0.0f,
                sceneCornerRadiusService.cornerBR >= 0.0f
                    ? sceneCornerRadiusService.cornerBR
                    : sceneCornerRadiusService.cornerRadius)
            * f5;
    float fMax4 =
        Math.max(
                0.0f,
                sceneCornerRadiusService.cornerBL >= 0.0f
                    ? sceneCornerRadiusService.cornerBL
                    : sceneCornerRadiusService.cornerRadius)
            * f5;
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
    return (((float)
                    Math.sqrt(
                        (double)
                            ((Math.max(fAbs, 0.0f) * Math.max(fAbs, 0.0f))
                                + (Math.max(fAbs2, 0.0f) * Math.max(fAbs2, 0.0f)))))
                + Math.min(Math.max(fAbs, fAbs2), 0.0f))
            - fMin
        <= 0.0f;
  }

  private static float calculateValue3(ScenePctService<?> scenePctService, float f) {
    return scenePctService.presentationTransform().inverseX(f);
  }

  private static float mdy0wcaemydv(ScenePctService<?> scenePctService, float f) {
    return scenePctService.presentationTransform().inverseY(f);
  }

  private static float calculateValue5(ScenePctService<?> scenePctService, float f) {
    return scenePctService.parent == null
        ? f
        : scenePctService.parent.presentationTransform().inverseX(f);
  }

  private static float mb6bqerjpdfe(ScenePctService<?> scenePctService, float f) {
    return scenePctService.parent == null
        ? f
        : scenePctService.parent.presentationTransform().inverseY(f);
  }

  private static float calculateValue7(ScenePctService<?> scenePctService, float f, float f2) {
    return scenePctService.scrollsHorizontally()
        ? scenePctService.presentationTransform().inverseX(f)
        : scenePctService.presentationTransform().inverseY(f2);
  }

  private static ScenePctService<?> createScenePctService5(ScenePctService<?> scenePctService) {
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

  private static boolean macpy9qvkkns(
      ScenePctService<?> scenePctService, ScenePctService<?> scenePctService2) {
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

  private ColorColorService createColorColorService(ScenePctService<?> scenePctService) {
    if (scenePctService instanceof ColorColorService) {
      ColorColorService colorColorService = (ColorColorService) scenePctService;
      if (colorColorService.isEyedropperActive()) {
        return colorColorService;
      }
    }
    Iterator<ScenePctService<?>> it = scenePctService.children().iterator();
    while (it.hasNext()) {
      ColorColorService colorColorServiceMa85juy9wyeb = createColorColorService(it.next());
      if (colorColorServiceMa85juy9wyeb != null) {
        return colorColorServiceMa85juy9wyeb;
      }
    }
    return null;
  }

  private ScenePctService<?> createScenePctService6(ScenePctService<?> scenePctService) {
    while (scenePctService != null) {
      if (scenePctService.draggable) {
        return scenePctService;
      }
      scenePctService = scenePctService.parent;
    }
    return null;
  }

  private ScenePctService<?> createScenePctService7(ScenePctService<?> scenePctService) {
    ScenePctService<?> scenePctService2 = scenePctService;
    while (scenePctService != null && scenePctService != this.sceneComponent4) {
      scenePctService2 = scenePctService;
      scenePctService = scenePctService.parent;
    }
    if (scenePctService == this.sceneComponent4) {
      return scenePctService2;
    }
    return null;
  }

  private void updateState5(ScenePctService<?> scenePctService, float f, float f2) {
    LocalConfigRepository localConfigRepositoryConfig;
    float[] position;
    boolean z = (f == this.f2elqz8kijo1 && f2 == this.f3tv3g1jfyhr) ? false : true;
    if (scenePctService.isDraggable()
        && scenePctService.width > 0.0f
        && scenePctService.height > 0.0f) {
      float fIntBitsToFloat =
          scenePctService.width > 0.0f ? scenePctService.width : Float.intBitsToFloat(1120403456);
      float fIntBitsToFloat2 =
          scenePctService.height > 0.0f ? scenePctService.height : Float.intBitsToFloat(1112014848);
      if (z
          && scenePctService.getId() != null
          && CoreIsInitializedHandler.isReady()
          && (localConfigRepositoryConfig = CoreIsInitializedHandler.get().config()) != null
          && (position =
                  localConfigRepositoryConfig.getPosition(
                      scenePctService.getId(), f, f2, fIntBitsToFloat, fIntBitsToFloat2))
              != null) {
        scenePctService.x = position[0];
        scenePctService.y = position[1];
      }
      scenePctService.x = Math.max(0.0f, Math.min(f - fIntBitsToFloat, scenePctService.x));
      scenePctService.y = Math.max(0.0f, Math.min(f2 - fIntBitsToFloat2, scenePctService.y));
    }
    Iterator<ScenePctService<?>> it = scenePctService.children().iterator();
    while (it.hasNext()) {
      updateState5(it.next(), f, f2);
    }
  }

  private void mb4hvy9zufns() {
    long j;
    ScenePctService<?> scenePctService;
    ScenePctService.CursorStyle cursorStyle = ScenePctService.CursorStyle.DEFAULT;
    ScenePctService<?> scenePctServiceM2zrhynh8k7x =
        this.enabled7 ? this.fr8js66i2ea : createScenePctService5(this.scenePctService);
    if (scenePctServiceM2zrhynh8k7x != null
        && (this.enabled7
            || scenePctServiceM2zrhynh8k7x.hitScrollbar(this.fcj7abwjsytb, this.value2))) {
      cursorStyle = ScenePctService.CursorStyle.POINTER;
    }
    if (this.scenePctService != null) {
      ScenePctService<?> scenePctServiceParent = this.scenePctService;
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
      } else if (cursorStyle == ScenePctService.CursorStyle.DEFAULT
          && this.scenePctService.interactive) {
        cursorStyle = ScenePctService.CursorStyle.POINTER;
      } else if (this.scenePctService instanceof ControlLetterSpacingService) {
        cursorStyle = ScenePctService.CursorStyle.TEXT;
      }
    }
    if (cursorStyle != this.cursorStyle2) {
      this.cursorStyle2 = cursorStyle;
      if (Minecraft.getInstance() == null) {
        return;
      }
      long jWindowHandle = CompatAdapterService.windowHandle(Minecraft.getInstance());
      switch (cursorStyle) {
        case POINTER:
          if (this.timestamp2 == 0) {
            this.timestamp2 = GLFW.glfwCreateStandardCursor(221188);
          }
          j = this.timestamp2;
          break;
        case TEXT:
          if (this.timestamp3 == 0) {
            this.timestamp3 = GLFW.glfwCreateStandardCursor(221186);
          }
          j = this.timestamp3;
          break;
        case CROSSHAIR:
          if (this.timestamp4 == 0) {
            this.timestamp4 = GLFW.glfwCreateStandardCursor(221187);
          }
          j = this.timestamp4;
          break;
        default:
          j = 0;
          break;
      }
      GLFW.glfwSetCursor(jWindowHandle, j);
    }
  }

  private void updateState7(ScenePctService<?> scenePctService) {
    if (scenePctService == null || scenePctService == this.scenePctService6) {
      return;
    }
    ScenePctService<?> scenePctService2 = this.scenePctService6;
    if (scenePctService2 instanceof SceneCornerRadiusService) {
      SceneCornerRadiusService sceneCornerRadiusService =
          (SceneCornerRadiusService) scenePctService2;
      if (sceneCornerRadiusService.visible) {
        sceneCornerRadiusService.animate("shadow", Float.intBitsToFloat(1086324736), spring3);
      }
    }
    ScenePctService<?> scenePctServiceMiwo14q1pk7k = createScenePctService7(scenePctService);
    if (scenePctServiceMiwo14q1pk7k != null) {
      this.sceneComponent4.addChild(scenePctServiceMiwo14q1pk7k);
    }
    this.scenePctService6 = scenePctService;
    if (scenePctService instanceof SceneCornerRadiusService) {
      ((SceneCornerRadiusService) scenePctService)
          .animate("shadow", Float.intBitsToFloat(1102053376), spring3);
    }
  }
}
