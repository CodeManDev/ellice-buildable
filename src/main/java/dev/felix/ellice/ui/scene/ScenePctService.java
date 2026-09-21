package dev.felix.ellice.ui.scene;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.theme.ThemeMixService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class ScenePctService<Self extends ScenePctService<Self>> {
  public static final float AUTO = -1.0F;
  protected ScenePctService<?> parent;
  private final List<ScenePctService<?>> items = new ArrayList<>();
  private final List<ScenePctService<?>> items2 = Collections.unmodifiableList(this.items);
  private String text;
  private String text2;
  private static int count;
  private static int count2;
  private int count3;
  private static final float value2 = -10000.0F;
  public float x;
  public float y;
  public float width = -1.0F;
  public float height = -1.0F;
  public float minWidth = -1.0F;
  public float maxWidth = -1.0F;
  public float minHeight = -1.0F;
  public float maxHeight = -1.0F;
  protected float flex;
  protected float flexShrink = 1.0F;
  private LayoutOperationHandler layoutOperationHandler = LayoutOperationHandler.auto();
  protected ScenePctService.Direction direction = ScenePctService.Direction.NONE;
  protected ScenePctService.Align align = ScenePctService.Align.STRETCH;
  protected ScenePctService.Justify justify = ScenePctService.Justify.START;
  protected float paddingTop;
  protected float paddingRight;
  protected float paddingBottom;
  protected float paddingLeft;
  protected float marginTop;
  protected float marginRight;
  protected float marginBottom;
  protected float marginLeft;
  protected float gap;
  protected ScenePctService.PositionType positionType = ScenePctService.PositionType.FLOW;
  private LayoutOperationHandler layoutOperationHandler2 = LayoutOperationHandler.auto();
  private LayoutOperationHandler layoutOperationHandler3 = LayoutOperationHandler.auto();
  private LayoutOperationHandler layoutOperationHandler4 = LayoutOperationHandler.auto();
  private LayoutOperationHandler layoutOperationHandler5 = LayoutOperationHandler.auto();
  protected float opacity = 1.0F;
  protected float scale = 1.0F;
  protected float translateX;
  protected float translateY;
  protected boolean inheritEdgeSoftness = true;
  public boolean visible = true;
  protected float cx;
  protected float cy;
  protected float cw;
  protected float ch;
  static boolean inLayout;
  protected float effectiveOpacity = 1.0F;
  protected float presentationScale = 1.0F;
  protected float effectiveEdgeSoftness = 0.0F;
  protected boolean clip;
  protected boolean scrollable;
  protected float scrollY;
  protected float scrollTarget;
  protected float scrollMin;
  protected float scrollContentSize;
  protected float scrollViewSize;
  protected int scrollFadeColor;
  protected float scrollInset = 6.0F;
  protected float scrollbarWidth = 3.0F;
  protected int scrollbarColor = 1358954495;
  protected float scrollbarRadius = -1.0F;
  protected int scrollbarTrackColor;
  protected boolean scrollBounce;
  protected boolean scrollbarAutoHide;
  protected float scrollbarOpacity = 1.0F;
  private float value3;
  private float value4;
  private float value5;
  private boolean enabled;
  private static final float value6 = 20.0F;
  private static final float value7 = 0.075F;
  private static final float value8 = 0.22F;
  private static final float value9 = 28.0F;
  private static final float value10 = 7.0F;
  protected boolean interactive;
  protected boolean pointerEvents = true;
  protected boolean draggable;
  protected boolean layerBreak;
  protected String tooltip;
  protected boolean hovered;
  protected boolean pressed;
  protected float hoverProgress;
  protected float pressProgress;
  protected boolean stopPropagation;
  protected Runnable onClick;
  protected Runnable onRightClick;
  protected BiConsumer<Float, Float> onDrag;
  protected Consumer<Boolean> onHoverChange;
  protected Consumer<Float> onScrollEvent;
  protected Runnable onLayout;
  protected ScenePctService.CursorStyle cursorStyle;
  private final List<ScenePctService.RunningAnim> items3 = new ArrayList<>();
  private final List<ScenePctService.RunningColorAnim> items4 = new ArrayList<>();
  private boolean enabled2;
  private boolean enabled3;
  private boolean enabled4;

  public static float pct(float value) {
    return -10000.0F - value;
  }

  static boolean isPct(float value) {
    return value <= -10000.0F;
  }

  static float pctValue(float value) {
    return -(value - -10000.0F);
  }

  static float resolve(float value, float currentValue) {
    if (value == -1.0F) {
      return -1.0F;
    } else {
      return isPct(value) ? currentValue * pctValue(value) / 100.0F : value;
    }
  }

  private static float calculateValue(LayoutOperationHandler layoutOperation) {
    return switch (createLayoutOperationHandler(layoutOperation)) {
      case LayoutOperationHandler.Auto auto -> -1.0F;
      case LayoutOperationHandler.Px px -> px.value();
      case LayoutOperationHandler.Percent percent -> pct(percent.value());
      default -> throw new MatchException(null, null);
    };
  }

  private static LayoutOperationHandler createLayoutOperationHandler(
      LayoutOperationHandler layoutOperation) {
    return Objects.requireNonNull(layoutOperation, "length");
  }

  private static float calculateValue2(String text, float value) {
    if (Float.isFinite(value) && !(value < 0.0F)) {
      return value;
    } else {
      throw new IllegalArgumentException(text + " must be finite and >= 0: " + value);
    }
  }

  static float clampSize(float value, float currentValue, float nextValue, float previousValue) {
    if (nextValue != -1.0F) {
      float sourceValue = resolve(nextValue, previousValue);
      if (value > sourceValue) {
        value = sourceValue;
      }
    }

    if (currentValue != -1.0F) {
      float targetValue = resolve(currentValue, previousValue);
      if (value < targetValue) {
        value = targetValue;
      }
    }

    return value;
  }

  public float layoutX() {
    return this.cx;
  }

  public float layoutY() {
    return this.cy;
  }

  public float layoutWidth() {
    return this.cw;
  }

  public float layoutHeight() {
    return this.ch;
  }

  protected Self self() {
    return (Self) this;
  }

  public static int layoutVersion() {
    return count;
  }

  public int subtreeVersion() {
    return this.count3;
  }

  private static void updateState() {
    count++;
  }

  protected final void invalidateLayout() {
    updateState();
    this.invalidate();
  }

  private static boolean checkCondition(float value, float currentValue) {
    return Float.floatToIntBits(value) == Float.floatToIntBits(currentValue);
  }

  public void invalidate() {
    count2++;

    for (ScenePctService scenePct = this; scenePct != null; scenePct = scenePct.parent) {
      scenePct.count3 = count2;
    }
  }

  public Self addChild(ScenePctService<?> scenePct) {
    if (scenePct == null) {
      throw new IllegalArgumentException("child must not be null");
    }

    this.updateState2(scenePct);
    if (scenePct.parent != null) {
      ScenePctService currentScenePct = scenePct.parent;
      currentScenePct.items.remove(scenePct);
      currentScenePct.invalidateLayout();
    }

    scenePct.parent = this;
    this.items.add(scenePct);
    this.invalidateLayout();
    return this.self();
  }

  public Self removeChild(ScenePctService<?> scenePct) {
    if (this.items.remove(scenePct)) {
      scenePct.parent = null;
      scenePct.updateState6();
      this.invalidateLayout();
    }

    return this.self();
  }

  public Self bringChildToFront(ScenePctService<?> scenePct) {
    if (this.items.size() >= 2 && this.items.get(this.items.size() - 1) != scenePct) {
      if (this.items.remove(scenePct)) {
        this.items.add(scenePct);
        this.invalidateLayout();
      }

      return this.self();
    } else {
      return this.self();
    }
  }

  public Self clearChildren() {
    if (this.items.isEmpty()) {
      return this.self();
    }

    for (ScenePctService scenePct : this.items) {
      scenePct.parent = null;
      scenePct.updateState6();
    }

    this.items.clear();
    this.invalidateLayout();
    return this.self();
  }

  public Self reconcileChildren(List<ScenePctService<?>> currentItems) {
    if (currentItems == null) {
      throw new IllegalArgumentException("ordered must not be null");
    }

    ArrayList arrayList = new ArrayList(currentItems);
    Set values = Collections.newSetFromMap(new IdentityHashMap());
    HashSet hashSet = new HashSet();

    for (ScenePctService scenePct : (Iterable<ScenePctService>) (Iterable<?>) (arrayList)) {
      if (scenePct == null) {
        throw new IllegalArgumentException("children must not contain null");
      }

      if (!values.add(scenePct)) {
        throw new IllegalArgumentException("duplicate child instance: " + scenePct);
      }

      if (scenePct.text2 != null && !hashSet.add(scenePct.text2)) {
        throw new IllegalArgumentException("duplicate child key: " + scenePct.text2);
      }

      this.updateState2(scenePct);
    }

    if (this.items.size() == arrayList.size()) {
      byte byteValue = 1;

      for (int index = 0; index < this.items.size(); index++) {
        if (this.items.get(index) != arrayList.get(index)) {
          byteValue = 0;
          break;
        }
      }

      if (byteValue != 0) {
        return this.self();
      }
    }

    for (ScenePctService currentScenePct : (Iterable<ScenePctService>) (Iterable<?>) (arrayList)) {
      ScenePctService nextScenePct = currentScenePct.parent;
      if (nextScenePct != null && nextScenePct != this) {
        nextScenePct.items.remove(currentScenePct);
        nextScenePct.invalidateLayout();
      }

      currentScenePct.parent = this;
    }

    for (ScenePctService previousScenePct : new ArrayList<>(this.items)) {
      if (!values.contains(previousScenePct)) {
        previousScenePct.parent = null;
        previousScenePct.updateState6();
      }
    }

    this.items.clear();
    this.items.addAll(arrayList);
    this.invalidateLayout();
    return this.self();
  }

  private void updateState2(ScenePctService<?> scenePct) {
    for (ScenePctService currentScenePct = this;
        currentScenePct != null;
        currentScenePct = currentScenePct.parent) {
      if (currentScenePct == scenePct) {
        throw new IllegalArgumentException("adding child would create a scene-graph cycle");
      }
    }
  }

  public List<ScenePctService<?>> children() {
    return this.items2;
  }

  public ScenePctService<?> parent() {
    return this.parent;
  }

  public ScenePctService<?> findById(String name) {
    if (name.equals(this.text)) {
      return this;
    }

    for (ScenePctService scenePct : this.items) {
      ScenePctService currentScenePct = scenePct.findById(name);
      if (currentScenePct != null) {
        return currentScenePct;
      }
    }

    return null;
  }

  public Self id(String currentId) {
    if (Objects.equals(this.text, currentId)) {
      return this.self();
    }

    this.text = currentId;
    this.invalidate();
    return this.self();
  }

  public Self key(String text) {
    if (Objects.equals(this.text2, text)) {
      return this.self();
    }

    this.text2 = text;
    this.invalidate();
    return this.self();
  }

  public String key() {
    return this.text2;
  }

  public Self position(float currentX, float currentY) {
    if (checkCondition(this.x, currentX) && checkCondition(this.y, currentY)) {
      return this.self();
    }

    this.x = currentX;
    this.y = currentY;
    this.invalidateLayout();
    return this.self();
  }

  public Self size(float value, float currentValue) {
    if (checkCondition(this.width, value) && checkCondition(this.height, currentValue)) {
      return this.self();
    }

    this.width = value;
    this.height = currentValue;
    this.invalidateLayout();
    return this.self();
  }

  public Self size(
      LayoutOperationHandler layoutOperation, LayoutOperationHandler currentLayoutOperation) {
    return this.size(calculateValue(layoutOperation), calculateValue(currentLayoutOperation));
  }

  public Self width(LayoutOperationHandler layoutOperation) {
    return this.size(calculateValue(layoutOperation), this.height);
  }

  public Self height(LayoutOperationHandler layoutOperation) {
    return this.size(this.width, calculateValue(layoutOperation));
  }

  public Self minWidth(float value) {
    if (checkCondition(this.minWidth, value)) {
      return this.self();
    }

    this.minWidth = value;
    this.invalidateLayout();
    return this.self();
  }

  public Self maxWidth(float value) {
    if (checkCondition(this.maxWidth, value)) {
      return this.self();
    }

    this.maxWidth = value;
    this.invalidateLayout();
    return this.self();
  }

  public Self minHeight(float value) {
    if (checkCondition(this.minHeight, value)) {
      return this.self();
    }

    this.minHeight = value;
    this.invalidateLayout();
    return this.self();
  }

  public Self maxHeight(float value) {
    if (checkCondition(this.maxHeight, value)) {
      return this.self();
    }

    this.maxHeight = value;
    this.invalidateLayout();
    return this.self();
  }

  public Self minWidth(LayoutOperationHandler layoutOperation) {
    return this.minWidth(calculateValue(layoutOperation));
  }

  public Self maxWidth(LayoutOperationHandler layoutOperation) {
    return this.maxWidth(calculateValue(layoutOperation));
  }

  public Self minHeight(LayoutOperationHandler layoutOperation) {
    return this.minHeight(calculateValue(layoutOperation));
  }

  public Self maxHeight(LayoutOperationHandler layoutOperation) {
    return this.maxHeight(calculateValue(layoutOperation));
  }

  public Self flex(float value) {
    float currentValue = calculateValue2("flex", value);
    LayoutOperationHandler layoutOperation =
        currentValue > 0.0F ? LayoutOperationHandler.px(0.0F) : LayoutOperationHandler.auto();
    if (checkCondition(this.flex, currentValue)
        && checkCondition(this.flexShrink, 1.0F)
        && this.layoutOperationHandler.equals(layoutOperation)) {
      return this.self();
    }

    this.flex = currentValue;
    this.flexShrink = 1.0F;
    this.layoutOperationHandler = layoutOperation;
    this.invalidateLayout();
    return this.self();
  }

  public Self flexGrow(float value) {
    float currentValue = calculateValue2("flexGrow", value);
    if (checkCondition(this.flex, currentValue)) {
      return this.self();
    }

    this.flex = currentValue;
    this.invalidateLayout();
    return this.self();
  }

  public Self flexShrink(float value) {
    float currentValue = calculateValue2("flexShrink", value);
    if (checkCondition(this.flexShrink, currentValue)) {
      return this.self();
    }

    this.flexShrink = currentValue;
    this.invalidateLayout();
    return this.self();
  }

  public Self flexBasis(LayoutOperationHandler layoutOperation) {
    LayoutOperationHandler currentLayoutOperation = createLayoutOperationHandler(layoutOperation);
    if (this.layoutOperationHandler.equals(currentLayoutOperation)) {
      return this.self();
    }

    this.layoutOperationHandler = currentLayoutOperation;
    this.invalidateLayout();
    return this.self();
  }

  public Self opacity(float currentOpacity) {
    if (checkCondition(this.opacity, currentOpacity)) {
      return this.self();
    }

    this.opacity = currentOpacity;
    this.invalidate();
    return this.self();
  }

  public Self scale(float currentScale) {
    if (checkCondition(this.scale, currentScale)) {
      return this.self();
    }

    this.scale = currentScale;
    this.invalidate();
    return this.self();
  }

  public Self translate(float value, float currentValue) {
    if (checkCondition(this.translateX, value) && checkCondition(this.translateY, currentValue)) {
      return this.self();
    }

    this.translateX = value;
    this.translateY = currentValue;
    this.invalidate();
    return this.self();
  }

  public Self translateX(float value) {
    return this.translate(value, this.translateY);
  }

  public Self translateY(float value) {
    return this.translate(this.translateX, value);
  }

  public Self inheritEdgeSoftness(boolean enabled) {
    if (this.inheritEdgeSoftness == enabled) {
      return this.self();
    }

    this.inheritEdgeSoftness = enabled;
    this.invalidate();
    return this.self();
  }

  public boolean inheritsEdgeSoftness() {
    return this.inheritEdgeSoftness;
  }

  public Self visible(boolean currentVisible) {
    if (this.visible == currentVisible) {
      return this.self();
    }

    this.visible = currentVisible;
    this.invalidateLayout();
    return this.self();
  }

  public Self padding(float value) {
    if (checkCondition(this.paddingTop, value)
        && checkCondition(this.paddingRight, value)
        && checkCondition(this.paddingBottom, value)
        && checkCondition(this.paddingLeft, value)) {
      return this.self();
    }

    this.paddingTop = this.paddingRight = this.paddingBottom = this.paddingLeft = value;
    this.invalidateLayout();
    return this.self();
  }

  public Self padding(float value, float currentValue) {
    if (checkCondition(this.paddingTop, value)
        && checkCondition(this.paddingBottom, value)
        && checkCondition(this.paddingLeft, currentValue)
        && checkCondition(this.paddingRight, currentValue)) {
      return this.self();
    }

    this.paddingTop = this.paddingBottom = value;
    this.paddingLeft = this.paddingRight = currentValue;
    this.invalidateLayout();
    return this.self();
  }

  public Self padding(float value, float currentValue, float nextValue, float previousValue) {
    if (checkCondition(this.paddingTop, value)
        && checkCondition(this.paddingRight, currentValue)
        && checkCondition(this.paddingBottom, nextValue)
        && checkCondition(this.paddingLeft, previousValue)) {
      return this.self();
    }

    this.paddingTop = value;
    this.paddingRight = currentValue;
    this.paddingBottom = nextValue;
    this.paddingLeft = previousValue;
    this.invalidateLayout();
    return this.self();
  }

  public Self paddingTop(float value) {
    if (checkCondition(this.paddingTop, value)) {
      return this.self();
    }

    this.paddingTop = value;
    this.invalidateLayout();
    return this.self();
  }

  public Self paddingRight(float value) {
    if (checkCondition(this.paddingRight, value)) {
      return this.self();
    }

    this.paddingRight = value;
    this.invalidateLayout();
    return this.self();
  }

  public Self paddingBottom(float value) {
    if (checkCondition(this.paddingBottom, value)) {
      return this.self();
    }

    this.paddingBottom = value;
    this.invalidateLayout();
    return this.self();
  }

  public Self paddingLeft(float value) {
    if (checkCondition(this.paddingLeft, value)) {
      return this.self();
    }

    this.paddingLeft = value;
    this.invalidateLayout();
    return this.self();
  }

  public Self margin(float value) {
    if (checkCondition(this.marginTop, value)
        && checkCondition(this.marginRight, value)
        && checkCondition(this.marginBottom, value)
        && checkCondition(this.marginLeft, value)) {
      return this.self();
    }

    this.marginTop = this.marginRight = this.marginBottom = this.marginLeft = value;
    this.invalidateLayout();
    return this.self();
  }

  public Self margin(float value, float currentValue) {
    if (checkCondition(this.marginTop, value)
        && checkCondition(this.marginBottom, value)
        && checkCondition(this.marginLeft, currentValue)
        && checkCondition(this.marginRight, currentValue)) {
      return this.self();
    }

    this.marginTop = this.marginBottom = value;
    this.marginLeft = this.marginRight = currentValue;
    this.invalidateLayout();
    return this.self();
  }

  public Self margin(float value, float currentValue, float nextValue, float previousValue) {
    if (checkCondition(this.marginTop, value)
        && checkCondition(this.marginRight, currentValue)
        && checkCondition(this.marginBottom, nextValue)
        && checkCondition(this.marginLeft, previousValue)) {
      return this.self();
    }

    this.marginTop = value;
    this.marginRight = currentValue;
    this.marginBottom = nextValue;
    this.marginLeft = previousValue;
    this.invalidateLayout();
    return this.self();
  }

  public Self marginTop(float value) {
    if (checkCondition(this.marginTop, value)) {
      return this.self();
    }

    this.marginTop = value;
    this.invalidateLayout();
    return this.self();
  }

  public Self marginRight(float value) {
    if (checkCondition(this.marginRight, value)) {
      return this.self();
    }

    this.marginRight = value;
    this.invalidateLayout();
    return this.self();
  }

  public Self marginBottom(float value) {
    if (checkCondition(this.marginBottom, value)) {
      return this.self();
    }

    this.marginBottom = value;
    this.invalidateLayout();
    return this.self();
  }

  public Self marginLeft(float value) {
    if (checkCondition(this.marginLeft, value)) {
      return this.self();
    }

    this.marginLeft = value;
    this.invalidateLayout();
    return this.self();
  }

  public Self gap(float value) {
    if (checkCondition(this.gap, value)) {
      return this.self();
    }

    this.gap = value;
    this.invalidateLayout();
    return this.self();
  }

  public Self direction(ScenePctService.Direction currentDirection) {
    ScenePctService.Direction nextDirection = Objects.requireNonNull(currentDirection, "direction");
    if (this.direction == nextDirection) {
      return this.self();
    }

    this.direction = nextDirection;
    this.invalidateLayout();
    return this.self();
  }

  public Self align(ScenePctService.Align currentAlign) {
    ScenePctService.Align nextAlign = Objects.requireNonNull(currentAlign, "align");
    if (this.align == nextAlign) {
      return this.self();
    }

    this.align = nextAlign;
    this.invalidateLayout();
    return this.self();
  }

  public Self justify(ScenePctService.Justify currentJustify) {
    ScenePctService.Justify nextJustify = Objects.requireNonNull(currentJustify, "justify");
    if (this.justify == nextJustify) {
      return this.self();
    }

    this.justify = nextJustify;
    this.invalidateLayout();
    return this.self();
  }

  public Self positionType(ScenePctService.PositionType currentPositionType) {
    ScenePctService.PositionType nextPositionType =
        Objects.requireNonNull(currentPositionType, "positionType");
    if (this.positionType == nextPositionType) {
      return this.self();
    }

    this.positionType = nextPositionType;
    this.invalidateLayout();
    return this.self();
  }

  public Self flow() {
    return this.positionType(ScenePctService.PositionType.FLOW);
  }

  public Self absolute() {
    return this.positionType(ScenePctService.PositionType.ABSOLUTE);
  }

  public Self inset(LayoutOperationHandler layoutOperation) {
    return this.inset(layoutOperation, layoutOperation, layoutOperation, layoutOperation);
  }

  public Self inset(
      LayoutOperationHandler layoutOperation, LayoutOperationHandler currentLayoutOperation) {
    return this.inset(
        layoutOperation, currentLayoutOperation, layoutOperation, currentLayoutOperation);
  }

  public Self inset(
      LayoutOperationHandler layoutOperation,
      LayoutOperationHandler currentLayoutOperation,
      LayoutOperationHandler nextLayoutOperation,
      LayoutOperationHandler previousLayoutOperation) {
    LayoutOperationHandler sourceLayoutOperation = createLayoutOperationHandler(layoutOperation);
    LayoutOperationHandler targetLayoutOperation =
        createLayoutOperationHandler(currentLayoutOperation);
    LayoutOperationHandler inputLayoutOperation = createLayoutOperationHandler(nextLayoutOperation);
    LayoutOperationHandler outputLayoutOperation =
        createLayoutOperationHandler(previousLayoutOperation);
    if (this.layoutOperationHandler2.equals(sourceLayoutOperation)
        && this.layoutOperationHandler3.equals(targetLayoutOperation)
        && this.layoutOperationHandler4.equals(inputLayoutOperation)
        && this.layoutOperationHandler5.equals(outputLayoutOperation)) {
      return this.self();
    }

    this.layoutOperationHandler2 = sourceLayoutOperation;
    this.layoutOperationHandler3 = targetLayoutOperation;
    this.layoutOperationHandler4 = inputLayoutOperation;
    this.layoutOperationHandler5 = outputLayoutOperation;
    this.invalidateLayout();
    return this.self();
  }

  public Self top(LayoutOperationHandler layoutOperation) {
    return this.inset(
        layoutOperation,
        this.layoutOperationHandler3,
        this.layoutOperationHandler4,
        this.layoutOperationHandler5);
  }

  public Self right(LayoutOperationHandler layoutOperation) {
    return this.inset(
        this.layoutOperationHandler2,
        layoutOperation,
        this.layoutOperationHandler4,
        this.layoutOperationHandler5);
  }

  public Self bottom(LayoutOperationHandler layoutOperation) {
    return this.inset(
        this.layoutOperationHandler2,
        this.layoutOperationHandler3,
        layoutOperation,
        this.layoutOperationHandler5);
  }

  public Self left(LayoutOperationHandler layoutOperation) {
    return this.inset(
        this.layoutOperationHandler2,
        this.layoutOperationHandler3,
        this.layoutOperationHandler4,
        layoutOperation);
  }

  public Self clip(boolean enabled) {
    if (this.clip == enabled) {
      return this.self();
    }

    this.clip = enabled;
    this.invalidate();
    return this.self();
  }

  public Self scrollable(boolean currentEnabled) {
    if (this.scrollable == currentEnabled) {
      return this.self();
    }

    if (currentEnabled && !this.scrollable && !this.clip) {
      this.enabled2 = true;
    }

    if (currentEnabled && !this.scrollable && !this.interactive) {
      this.interactive = true;
      this.enabled3 = true;
    }

    if (!currentEnabled && this.enabled2) {
      this.clip = false;
      this.enabled2 = false;
    }

    if (!currentEnabled && this.enabled3) {
      this.interactive = false;
      this.enabled3 = false;
    }

    this.scrollable = currentEnabled;
    if (currentEnabled) {
      this.clip = true;
    }

    if (!currentEnabled) {
      this.value3 = 0.0F;
      this.enabled = false;
      this.value5 = 0.0F;
    }

    this.invalidateLayout();
    return this.self();
  }

  public Self scrollY(float value) {
    if (checkCondition(this.scrollY, value)) {
      return this.self();
    }

    this.scrollY = value;
    this.invalidateLayout();
    return this.self();
  }

  public Self scrollTarget(float value) {
    if (checkCondition(this.scrollTarget, value)) {
      return this.self();
    }

    this.scrollTarget = value;
    this.invalidate();
    return this.self();
  }

  public Self scrollFadeColor(int color) {
    if (this.scrollFadeColor == color) {
      return this.self();
    }

    this.scrollFadeColor = color;
    this.invalidate();
    return this.self();
  }

  public Self scrollInset(float value) {
    if (checkCondition(this.scrollInset, value)) {
      return this.self();
    }

    this.scrollInset = value;
    this.invalidate();
    return this.self();
  }

  public Self scrollbarWidth(float value) {
    if (checkCondition(this.scrollbarWidth, value)) {
      return this.self();
    }

    this.scrollbarWidth = value;
    this.invalidate();
    return this.self();
  }

  public Self scrollbarColor(int color) {
    if (this.scrollbarColor == color) {
      return this.self();
    }

    this.scrollbarColor = color;
    this.invalidate();
    return this.self();
  }

  public Self scrollbarRadius(float value) {
    if (checkCondition(this.scrollbarRadius, value)) {
      return this.self();
    }

    this.scrollbarRadius = value;
    this.invalidate();
    return this.self();
  }

  public Self scrollbarTrackColor(int color) {
    if (this.scrollbarTrackColor == color) {
      return this.self();
    }

    this.scrollbarTrackColor = color;
    this.invalidate();
    return this.self();
  }

  public Self scrollBounce(boolean currentEnabled) {
    if (this.scrollBounce == currentEnabled) {
      return this.self();
    }

    this.scrollBounce = currentEnabled;
    this.value3 = 0.0F;
    this.enabled = false;
    this.value4 = 0.0F;
    if (!currentEnabled) {
      this.scrollY = this.calculateValue8(this.scrollY);
      this.scrollTarget = this.calculateValue8(this.scrollTarget);
    }

    this.invalidateLayout();
    return this.self();
  }

  public Self scrollbarAutoHide(boolean enabled) {
    if (this.scrollbarAutoHide == enabled) {
      return this.self();
    }

    this.scrollbarAutoHide = enabled;
    this.scrollbarOpacity = enabled ? 0.0F : 1.0F;
    this.value5 = 0.0F;
    this.invalidate();
    return this.self();
  }

  public Self interactive(boolean enabled) {
    this.enabled3 = false;
    if (this.interactive == enabled) {
      return this.self();
    }

    this.interactive = enabled;
    this.invalidate();
    return this.self();
  }

  public Self pointerEvents(boolean enabled) {
    if (this.pointerEvents == enabled) {
      return this.self();
    }

    this.pointerEvents = enabled;
    this.invalidate();
    return this.self();
  }

  public boolean pointerEvents() {
    return this.pointerEvents;
  }

  public Self draggable(boolean enabled) {
    if (this.draggable != enabled || enabled && !this.interactive) {
      this.draggable = enabled;
      if (enabled) {
        this.interactive = true;
      }

      this.invalidate();
      return this.self();
    } else {
      return this.self();
    }
  }

  public Self layerBreak(boolean enabled) {
    if (this.layerBreak == enabled) {
      return this.self();
    }

    this.layerBreak = enabled;
    this.invalidate();
    return this.self();
  }

  public Self onClick(Runnable runnable) {
    this.onClick = runnable;
    this.interactive = true;
    this.invalidate();
    return this.self();
  }

  public Self tooltip(String text) {
    this.tooltip = text;
    this.invalidate();
    return this.self();
  }

  public String tooltip() {
    return this.tooltip;
  }

  public Self onDrag(BiConsumer<Float, Float> biConsumer) {
    this.onDrag = biConsumer;
    this.interactive = true;
    this.invalidate();
    return this.self();
  }

  public Self onHoverChange(Consumer<Boolean> consumer) {
    this.onHoverChange = consumer;
    this.interactive = true;
    this.invalidate();
    return this.self();
  }

  public Self onScrollEvent(Consumer<Float> consumer) {
    this.onScrollEvent = consumer;
    this.interactive = true;
    this.invalidate();
    return this.self();
  }

  public Self onLayout(Runnable runnable) {
    if (this.onLayout == runnable) {
      return this.self();
    }

    this.onLayout = runnable;
    this.invalidateLayout();
    return this.self();
  }

  public Self cursorStyle(ScenePctService.CursorStyle currentCursorStyle) {
    this.cursorStyle = currentCursorStyle;
    this.invalidate();
    return this.self();
  }

  public Self stopPropagation(boolean enabled) {
    this.stopPropagation = enabled;
    this.invalidate();
    return this.self();
  }

  public ScenePctService.CursorStyle cursorStyle() {
    return this.cursorStyle;
  }

  public Self onRightClick(Runnable runnable) {
    this.onRightClick = runnable;
    this.interactive = true;
    this.invalidate();
    return this.self();
  }

  public Self animate(String text, float value, SceneEaseHandler sceneEase) {
    return this.animate(text, value, sceneEase, 0.0F, 0, false, null);
  }

  public Self animate(String text, float value, SceneEaseHandler sceneEase, float currentValue) {
    return this.animate(text, value, sceneEase, currentValue, 0, false, null);
  }

  public Self animate(
      String text,
      float value,
      SceneEaseHandler sceneEase,
      float currentValue,
      int nextValue,
      boolean enabled,
      Runnable runnable) {
    for (ScenePctService.RunningAnim runningAnim : this.items3) {
      if (runningAnim.property.equals(text)) {
        runningAnim.from = this.getAnimProperty(text);
        runningAnim.to = value;
        runningAnim.anim = sceneEase;
        runningAnim.elapsed = 0.0F;
        runningAnim.delay = currentValue;
        runningAnim.repeat = nextValue;
        runningAnim.yoyo = enabled;
        runningAnim.onComplete = runnable;
        this.invalidate();
        return this.self();
      }
    }

    ScenePctService.RunningAnim currentRunningAnim =
        new ScenePctService.RunningAnim(text, this.getAnimProperty(text), value, sceneEase);
    currentRunningAnim.delay = currentValue;
    currentRunningAnim.repeat = nextValue;
    currentRunningAnim.yoyo = enabled;
    currentRunningAnim.onComplete = runnable;
    this.items3.add(currentRunningAnim);
    this.invalidate();
    return this.self();
  }

  public Self cancelAnimation(String text) {
    this.items3.removeIf(item -> item.property.equals(text));
    return this.self();
  }

  public Self loop(
      String text, float value, float currentValue, SceneEaseHandler sceneEase, boolean enabled) {
    this.setAnimProperty(text, value);
    return this.animate(text, currentValue, sceneEase, 0.0F, -1, enabled, null);
  }

  public Self animateColor(String text, int color, SceneEaseHandler sceneEase) {
    return this.animateColor(text, color, sceneEase, 0.0F, null);
  }

  public Self animateColor(
      String text, int color, SceneEaseHandler sceneEase, float value, Runnable runnable) {
    for (ScenePctService.RunningColorAnim runningColorAnim : this.items4) {
      if (runningColorAnim.property.equals(text)) {
        runningColorAnim.from = this.getColorProperty(text);
        runningColorAnim.to = color;
        runningColorAnim.anim = sceneEase;
        runningColorAnim.progress = 0.0F;
        runningColorAnim.elapsed = 0.0F;
        runningColorAnim.delay = value;
        runningColorAnim.onComplete = runnable;
        this.invalidate();
        return this.self();
      }
    }

    int currentColor = this.getColorProperty(text);
    if (currentColor == color && !(value > 0.0F)) {
      this.setColorProperty(text, color);
    } else {
      ScenePctService.RunningColorAnim currentRunningColorAnim =
          new ScenePctService.RunningColorAnim(text, currentColor, color, sceneEase);
      currentRunningColorAnim.delay = value;
      currentRunningColorAnim.onComplete = runnable;
      this.items4.add(currentRunningColorAnim);
    }

    this.invalidate();
    return this.self();
  }

  public int getColorProperty(String text) {
    return 0;
  }

  public void setColorProperty(String text, int color) {}

  static int lerpColorChannels(int color, int currentColor, float value) {
    if (value <= 0.0F) {
      return color;
    }

    if (value >= 1.0F) {
      return currentColor;
    }

    int nextColor = color >> 24 & 0xFF;
    int previousColor = color >> 16 & 0xFF;
    int sourceColor = color >> 8 & 0xFF;
    int targetColor = color & 0xFF;
    int inputColor = currentColor >> 24 & 0xFF;
    int outputColor = currentColor >> 16 & 0xFF;
    int resultColor = currentColor >> 8 & 0xFF;
    int candidateColor = currentColor & 0xFF;
    return nextColor + (int) ((inputColor - nextColor) * value) << 24
        | previousColor + (int) ((outputColor - previousColor) * value) << 16
        | sourceColor + (int) ((resultColor - sourceColor) * value) << 8
        | targetColor + (int) ((candidateColor - targetColor) * value);
  }

  static int lerpColorOklab(int color, int currentColor, float value) {
    return ThemeMixService.mix(color, currentColor, value);
  }

  void tickAnimations() {
    this.tickAnimations(0.016666668F);
  }

  public void tickAnimations(float value) {
    if (Float.isFinite(value) && !(value < 0.0F)) {
      ArrayList arrayList = new ArrayList();
      this.updateState3(value, arrayList);

      for (Runnable runnable : (Iterable<Runnable>) (Iterable<?>) (arrayList)) {
        try {
          runnable.run();
        } catch (Exception exception) {
        }
      }
    }
  }

  private void updateState3(float value, List<Runnable> currentItems) {
    this.updateState4(value);
    int index = 0;

    while (index < this.items3.size()) {
      ScenePctService.RunningAnim runningAnim = this.items3.get(index);
      float currentValue = this.calculateValue3(runningAnim, value);
      if (currentValue <= 0.0F) {
        index++;
      } else {
        boolean animationFinished = false;
        float nextValue = this.getAnimProperty(runningAnim.property);
        if (runningAnim.anim instanceof SceneEaseHandler.Spring spring) {
          SceneEaseHandler.Spring.Sample sample =
              spring.advance(nextValue, runningAnim.velocity, runningAnim.to, currentValue);
          float previousValue = sample.position();
          float sourceValue = sample.velocity();
          runningAnim.velocity = sourceValue;
          this.setAnimProperty(runningAnim.property, previousValue);
          this.invalidate();
          if (Math.abs(previousValue - runningAnim.to) < 0.001F && Math.abs(sourceValue) < 0.01F) {
            animationFinished = this.checkCondition3(runningAnim);
          }
        } else if (runningAnim.anim instanceof SceneEaseHandler.Tween tween) {
          runningAnim.elapsed += currentValue;
          float targetValue = Math.min(runningAnim.elapsed / tween.duration(), 1.0F);
          this.setAnimProperty(
              runningAnim.property,
              runningAnim.from
                  + (runningAnim.to - runningAnim.from) * tween.easing().apply(targetValue));
          this.invalidate();
          if (targetValue >= 1.0F) {
            animationFinished = this.checkCondition3(runningAnim);
          }
        }

        if (animationFinished) {
          this.items3.remove(index);
          if (runningAnim.onComplete != null) {
            currentItems.add(runningAnim.onComplete);
            runningAnim.onComplete = null;
          }
        } else {
          index++;
        }
      }
    }

    index = 0;

    while (index < this.items4.size()) {
      ScenePctService.RunningColorAnim runningColorAnim = this.items4.get(index);
      float inputValue = this.calculateValue4(runningColorAnim, value);
      if (inputValue <= 0.0F) {
        index++;
      } else {
        boolean animationFinished = false;
        if (runningColorAnim.anim instanceof SceneEaseHandler.Spring currentSpring) {
          SceneEaseHandler.Spring.Sample currentSample =
              currentSpring.advance(
                  runningColorAnim.progress, runningColorAnim.velocity, 1.0F, inputValue);
          float outputValue = currentSample.position();
          float resultValue = currentSample.velocity();
          runningColorAnim.velocity = resultValue;
          runningColorAnim.progress = outputValue;
          this.setColorProperty(
              runningColorAnim.property,
              lerpColorOklab(
                  runningColorAnim.from,
                  runningColorAnim.to,
                  Math.max(0.0F, Math.min(1.0F, outputValue))));
          this.invalidate();
          if (Math.abs(outputValue - 1.0F) < 0.002F && Math.abs(resultValue) < 0.01F) {
            this.setColorProperty(runningColorAnim.property, runningColorAnim.to);
            animationFinished = this.checkCondition4(runningColorAnim);
          }
        } else if (runningColorAnim.anim instanceof SceneEaseHandler.Tween currentTween) {
          runningColorAnim.elapsed += inputValue;
          float candidateValue = Math.min(runningColorAnim.elapsed / currentTween.duration(), 1.0F);
          runningColorAnim.progress = currentTween.easing().apply(candidateValue);
          this.setColorProperty(
              runningColorAnim.property,
              lerpColorOklab(
                  runningColorAnim.from, runningColorAnim.to, runningColorAnim.progress));
          this.invalidate();
          if (candidateValue >= 1.0F) {
            this.setColorProperty(runningColorAnim.property, runningColorAnim.to);
            animationFinished = this.checkCondition4(runningColorAnim);
          }
        }

        if (animationFinished) {
          this.items4.remove(index);
          if (runningColorAnim.onComplete != null) {
            currentItems.add(runningColorAnim.onComplete);
            runningColorAnim.onComplete = null;
          }
        } else {
          index++;
        }
      }
    }

    for (ScenePctService scenePct : this.items) {
      scenePct.updateState3(value, currentItems);
    }
  }

  private void updateState4(float value) {
    if (!(value <= 0.0F)
        && Float.isFinite(value)
        && this.scrollable
        && (this.scrollBounce || this.scrollbarAutoHide)) {
      if (this.scrollBounce && this.enabled) {
        float currentValue = value;
        int nextValue =
            !(this.scrollTarget > 0.0F) && !(this.scrollTarget < this.scrollMin) ? 0 : 1;
        if (nextValue != 0 && this.value4 < 0.075F) {
          float previousValue = 0.075F - this.value4;
          float sourceValue = Math.min(currentValue, previousValue);
          this.updateState5(sourceValue);
          this.value4 += sourceValue;
          currentValue -= sourceValue;
        }

        if (nextValue != 0 && this.value4 >= 0.075F) {
          this.scrollTarget = this.calculateValue8(this.scrollTarget);
        }

        if (currentValue > 0.0F) {
          this.updateState5(currentValue);
          this.value4 += currentValue;
        }

        this.updateState16();
        if (Math.abs(this.scrollY - this.scrollTarget) < 0.005F
            && Math.abs(this.value3) < 0.02F
            && this.scrollTarget <= 0.0F
            && this.scrollTarget >= this.scrollMin) {
          this.scrollY = this.scrollTarget;
          this.value3 = 0.0F;
          this.enabled = false;
        }

        this.invalidateLayout();
      } else {
        this.value4 += value;
      }

      if (this.value5 > 0.0F) {
        this.value5 = Math.max(0.0F, this.value5 - value);
      }

      if (!this.scrollbarAutoHide) {
        this.scrollbarOpacity = 1.0F;
      } else {
        int targetValue =
            !this.enabled4
                    && !this.enabled
                    && !this.checkCondition2("scrollY")
                    && !(Math.abs(this.value3) > 0.02F)
                ? 0
                : 1;
        float inputValue = !(this.value5 > 0.0F) && targetValue == 0 ? 0.0F : 1.0F;
        float outputValue = inputValue > this.scrollbarOpacity ? 28.0F : 7.0F;
        float resultValue = 1.0F - (float) Math.exp(-outputValue * value);
        float candidateValue =
            this.scrollbarOpacity + (inputValue - this.scrollbarOpacity) * resultValue;
        if (Math.abs(candidateValue - inputValue) < 0.001F) {
          candidateValue = inputValue;
        }

        if (!checkCondition(this.scrollbarOpacity, candidateValue)) {
          this.scrollbarOpacity = candidateValue;
          this.invalidate();
        }
      }
    }
  }

  private void updateState5(float value) {
    if (!(value <= 0.0F)) {
      float currentValue = this.scrollY - this.scrollTarget;
      float nextValue = this.value3 + 20.0F * currentValue;
      float previousValue = (float) Math.exp(-20.0F * value);
      float sourceValue = (currentValue + nextValue * value) * previousValue;
      float targetValue = (this.value3 - 20.0F * nextValue * value) * previousValue;
      this.scrollY = this.scrollTarget + sourceValue;
      this.value3 = targetValue;
    }
  }

  private boolean checkCondition2(String text) {
    for (ScenePctService.RunningAnim runningAnim : this.items3) {
      if (runningAnim.property.equals(text)) {
        return true;
      }
    }

    return false;
  }

  private float calculateValue3(ScenePctService.RunningAnim runningAnim, float value) {
    if (runningAnim.delay <= 0.0F) {
      return value;
    } else {
      this.setAnimProperty(runningAnim.property, runningAnim.from);
      this.invalidate();
      if (value <= runningAnim.delay) {
        runningAnim.delay -= value;
        return 0.0F;
      } else {
        float currentValue = value - runningAnim.delay;
        runningAnim.delay = 0.0F;
        return currentValue;
      }
    }
  }

  private float calculateValue4(ScenePctService.RunningColorAnim runningColorAnim, float value) {
    if (runningColorAnim.delay <= 0.0F) {
      return value;
    } else {
      this.setColorProperty(runningColorAnim.property, runningColorAnim.from);
      this.invalidate();
      if (value <= runningColorAnim.delay) {
        runningColorAnim.delay -= value;
        return 0.0F;
      } else {
        float currentValue = value - runningColorAnim.delay;
        runningColorAnim.delay = 0.0F;
        return currentValue;
      }
    }
  }

  private boolean checkCondition3(ScenePctService.RunningAnim runningAnim) {
    if (runningAnim.repeat != 0) {
      if (runningAnim.repeat > 0) {
        runningAnim.repeat--;
      }

      if (runningAnim.yoyo) {
        float value = runningAnim.from;
        runningAnim.from = runningAnim.to;
        runningAnim.to = value;
      }

      this.setAnimProperty(runningAnim.property, runningAnim.from);
      runningAnim.elapsed = 0.0F;
      runningAnim.velocity = 0.0F;
      return false;
    } else {
      this.setAnimProperty(runningAnim.property, runningAnim.to);
      return true;
    }
  }

  private boolean checkCondition4(ScenePctService.RunningColorAnim runningColorAnim) {
    if (runningColorAnim.repeat != 0) {
      if (runningColorAnim.repeat > 0) {
        runningColorAnim.repeat--;
      }

      if (runningColorAnim.yoyo) {
        int value = runningColorAnim.from;
        runningColorAnim.from = runningColorAnim.to;
        runningColorAnim.to = value;
      }

      this.setColorProperty(runningColorAnim.property, runningColorAnim.from);
      runningColorAnim.progress = 0.0F;
      runningColorAnim.elapsed = 0.0F;
      runningColorAnim.velocity = 0.0F;
      return false;
    } else {
      return true;
    }
  }

  public float getAnimProperty(String text) {
    return switch (text) {
      case "x" -> this.x;
      case "y" -> this.y;
      case "width" -> this.width;
      case "height" -> this.height;
      case "opacity" -> this.opacity;
      case "scale" -> this.scale;
      case "translateX" -> this.translateX;
      case "translateY" -> this.translateY;
      case "scrollY" -> this.scrollY;
      case "padding" -> this.paddingTop;
      case "paddingTop" -> this.paddingTop;
      case "paddingRight" -> this.paddingRight;
      case "paddingBottom" -> this.paddingBottom;
      case "paddingLeft" -> this.paddingLeft;
      case "margin" -> this.marginTop;
      case "marginTop" -> this.marginTop;
      case "marginRight" -> this.marginRight;
      case "marginBottom" -> this.marginBottom;
      case "marginLeft" -> this.marginLeft;
      case "gap" -> this.gap;
      case "hoverProgress" -> this.hoverProgress;
      case "pressProgress" -> this.pressProgress;
      default -> 0.0F;
    };
  }

  public void setAnimProperty(String text, float value) {
    byte byteValue = 1;
    int currentValue = 1;
    switch (text) {
      case "x":
        currentValue = !checkCondition(this.x, value) ? 1 : 0;
        this.x = value;
        break;
      case "y":
        currentValue = !checkCondition(this.y, value) ? 1 : 0;
        this.y = value;
        break;
      case "width":
        currentValue = !checkCondition(this.width, value) ? 1 : 0;
        this.width = value;
        break;
      case "height":
        currentValue = !checkCondition(this.height, value) ? 1 : 0;
        this.height = value;
        break;
      case "opacity":
        currentValue = !checkCondition(this.opacity, value) ? 1 : 0;
        this.opacity = value;
        byteValue = 0;
        break;
      case "scale":
        currentValue = !checkCondition(this.scale, value) ? 1 : 0;
        this.scale = value;
        byteValue = 0;
        break;
      case "translateX":
        currentValue = !checkCondition(this.translateX, value) ? 1 : 0;
        this.translateX = value;
        byteValue = 0;
        break;
      case "translateY":
        currentValue = !checkCondition(this.translateY, value) ? 1 : 0;
        this.translateY = value;
        byteValue = 0;
        break;
      case "scrollY":
        currentValue = !checkCondition(this.scrollY, value) ? 1 : 0;
        this.scrollY = value;
        break;
      case "scrollTarget":
        currentValue = !checkCondition(this.scrollTarget, value) ? 1 : 0;
        this.scrollTarget = value;
        byteValue = 0;
        break;
      case "padding":
        currentValue =
            checkCondition(this.paddingTop, value)
                    && checkCondition(this.paddingRight, value)
                    && checkCondition(this.paddingBottom, value)
                    && checkCondition(this.paddingLeft, value)
                ? 0
                : 1;
        this.paddingTop = this.paddingRight = this.paddingBottom = this.paddingLeft = value;
        break;
      case "paddingTop":
        currentValue = !checkCondition(this.paddingTop, value) ? 1 : 0;
        this.paddingTop = value;
        break;
      case "paddingRight":
        currentValue = !checkCondition(this.paddingRight, value) ? 1 : 0;
        this.paddingRight = value;
        break;
      case "paddingBottom":
        currentValue = !checkCondition(this.paddingBottom, value) ? 1 : 0;
        this.paddingBottom = value;
        break;
      case "paddingLeft":
        currentValue = !checkCondition(this.paddingLeft, value) ? 1 : 0;
        this.paddingLeft = value;
        break;
      case "margin":
        currentValue =
            checkCondition(this.marginTop, value)
                    && checkCondition(this.marginRight, value)
                    && checkCondition(this.marginBottom, value)
                    && checkCondition(this.marginLeft, value)
                ? 0
                : 1;
        this.marginTop = this.marginRight = this.marginBottom = this.marginLeft = value;
        break;
      case "marginTop":
        currentValue = !checkCondition(this.marginTop, value) ? 1 : 0;
        this.marginTop = value;
        break;
      case "marginRight":
        currentValue = !checkCondition(this.marginRight, value) ? 1 : 0;
        this.marginRight = value;
        break;
      case "marginBottom":
        currentValue = !checkCondition(this.marginBottom, value) ? 1 : 0;
        this.marginBottom = value;
        break;
      case "marginLeft":
        currentValue = !checkCondition(this.marginLeft, value) ? 1 : 0;
        this.marginLeft = value;
        break;
      case "gap":
        currentValue = !checkCondition(this.gap, value) ? 1 : 0;
        this.gap = value;
        break;
      case "hoverProgress":
        currentValue = !checkCondition(this.hoverProgress, value) ? 1 : 0;
        this.hoverProgress = value;
        byteValue = 0;
        break;
      case "pressProgress":
        currentValue = !checkCondition(this.pressProgress, value) ? 1 : 0;
        this.pressProgress = value;
        byteValue = 0;
        break;
      default:
        currentValue = 0;
        byteValue = 0;
    }

    if (currentValue != 0) {
      if (byteValue != 0) {
        this.invalidateLayout();
      } else {
        this.invalidate();
      }
    }
  }

  protected float ownEdgeSoftness() {
    return 0.0F;
  }

  protected void onPress(float value, float currentValue) {}

  protected void onScenePress(float value, float currentValue) {}

  protected void onRelease(boolean enabled) {}

  protected boolean handlesContinuousPointer() {
    return this.onDrag != null;
  }

  protected void handleClick() {
    if (this.onClick != null) {
      this.onClick.run();
    }

    if (!this.stopPropagation && this.parent != null) {
      this.parent.handleClick();
    }
  }

  protected void handleRightClick() {
    if (this.onRightClick != null) {
      this.onRightClick.run();
    }

    if (!this.stopPropagation && this.parent != null) {
      this.parent.handleRightClick();
    }
  }

  protected void updateWhilePressed(float value, float currentValue) {
    if (this.onDrag != null) {
      this.onDrag.accept(value, currentValue);
    }
  }

  protected void updateWhileScenePressed(float value, float currentValue) {}

  void applyScrollDelta(float value, SceneEaseHandler sceneEase) {
    if (this.scrollable && !(this.scrollMin >= -1.0E-4F) && Float.isFinite(value)) {
      this.value5 = 0.22F;
      this.value4 = 0.0F;
      if (!this.scrollBounce) {
        this.scrollTarget = this.calculateValue8(this.scrollTarget + value);
        this.animate("scrollY", this.scrollTarget, sceneEase);
        this.invalidate();
      } else {
        this.cancelAnimation("scrollY");
        this.enabled = true;
        float currentValue = this.scrollTarget;
        float nextValue = this.calculateValue5(currentValue, value);
        int previousValue = value > 0.0F && this.scrollY >= -0.75F && nextValue > 0.0F ? 1 : 0;
        int sourceValue =
            value < 0.0F && this.scrollY <= this.scrollMin + 0.75F && nextValue < this.scrollMin
                ? 1
                : 0;
        if (previousValue != 0 || sourceValue != 0) {
          this.scrollY = nextValue;
          this.value3 = 0.0F;
        }

        this.scrollTarget = nextValue;
        this.invalidateLayout();
      }
    }
  }

  boolean hasScrollableOverflow() {
    return this.scrollable && this.scrollMin < -1.0E-4F;
  }

  boolean isAutoInteractiveScroll() {
    return this.enabled3;
  }

  boolean scrollsHorizontally() {
    return this.direction == ScenePctService.Direction.ROW;
  }

  void beginPointerScroll() {
    this.cancelAnimation("scrollY");
    this.value3 = 0.0F;
    this.enabled = false;
    this.value4 = 0.0F;
    this.value5 = 0.22F;
  }

  void dragScrollTo(float value) {
    if (this.hasScrollableOverflow() && Float.isFinite(value)) {
      float currentValue = this.calculateValue8(value);
      this.value5 = 0.22F;
      if (!checkCondition(this.scrollY, currentValue)
          || !checkCondition(this.scrollTarget, currentValue)) {
        this.scrollY = currentValue;
        this.scrollTarget = currentValue;
        this.invalidateLayout();
      }
    }
  }

  boolean hitScrollbar(float value, float currentValue) {
    ScenePctService.ScrollbarMetrics scrollbarMetrics = this.createScrollbarMetrics();
    if (scrollbarMetrics == null) {
      return false;
    }

    ScenePctService.PresentationTransform currentPresentationTransform =
        this.presentationTransform();
    float nextValue = currentPresentationTransform.inverseX(value) - this.cx;
    float previousValue = currentPresentationTransform.inverseY(currentValue) - this.cy;
    float sourceValue = this.scrollsHorizontally() ? nextValue : previousValue;
    float targetValue = this.scrollsHorizontally() ? previousValue : nextValue;
    return targetValue >= scrollbarMetrics.crossStart - 4.0F
        && targetValue <= scrollbarMetrics.crossStart + scrollbarMetrics.crossSize + 4.0F
        && sourceValue >= scrollbarMetrics.thumbStart
        && sourceValue <= scrollbarMetrics.thumbStart + scrollbarMetrics.thumbSize;
  }

  float scrollbarGrabOffset(float value, float currentValue) {
    ScenePctService.ScrollbarMetrics scrollbarMetrics = this.createScrollbarMetrics();
    if (scrollbarMetrics == null) {
      return 0.0F;
    }

    float nextValue =
        this.scrollsHorizontally()
            ? this.presentationTransform().inverseX(value) - this.cx
            : this.presentationTransform().inverseY(currentValue) - this.cy;
    return nextValue - scrollbarMetrics.thumbStart;
  }

  void beginScrollbarDrag() {
    this.enabled4 = true;
    this.beginPointerScroll();
    this.invalidate();
  }

  void endScrollbarDrag() {
    if (this.enabled4) {
      this.enabled4 = false;
      this.invalidate();
    }
  }

  void dragScrollbarTo(float value, float currentValue, float nextValue) {
    ScenePctService.ScrollbarMetrics scrollbarMetrics = this.createScrollbarMetrics();
    if (scrollbarMetrics != null) {
      float previousValue =
          this.scrollsHorizontally()
              ? this.presentationTransform().inverseX(value) - this.cx
              : this.presentationTransform().inverseY(currentValue) - this.cy;
      float sourceValue = Math.max(0.0F, scrollbarMetrics.trackSize - scrollbarMetrics.thumbSize);
      float targetValue =
          Math.clamp(
              previousValue - nextValue,
              scrollbarMetrics.trackStart,
              scrollbarMetrics.trackStart + sourceValue);
      float inputValue =
          sourceValue > 0.0F ? (targetValue - scrollbarMetrics.trackStart) / sourceValue : 0.0F;
      this.dragScrollTo(-inputValue * (this.scrollContentSize - this.scrollViewSize));
    }
  }

  private ScenePctService.ScrollbarMetrics createScrollbarMetrics() {
    if (this.hasScrollableOverflow()
        && !(this.scrollbarWidth <= 0.0F)
        && (!this.scrollbarAutoHide || !(this.scrollbarOpacity < 0.05F))) {
      float value = this.presentationTransform().scale();
      if (!(value > 1.0E-6F)) {
        return null;
      }

      float currentValue = this.scrollbarWidth * (this.enabled4 ? 1.65F : 1.0F);
      float nextValue = Math.max(0.0F, this.scrollInset);
      float previousValue = (this.scrollsHorizontally() ? this.cw : this.ch) - nextValue * 2.0F;
      if (!(previousValue > 0.0F)) {
        return null;
      }

      float sourceValue = this.scrollViewSize / Math.max(1.0F, this.scrollContentSize);
      float targetValue =
          Math.min(
              previousValue, Math.max(this.scrollbarWidth * 4.0F, previousValue * sourceValue));
      float inputValue = Math.max(0.0F, this.scrollContentSize - this.scrollViewSize);
      float outputValue =
          inputValue > 0.0F ? -this.calculateValue8(this.scrollY) / inputValue : 0.0F;
      float resultValue = nextValue + outputValue * Math.max(0.0F, previousValue - targetValue);
      float candidateValue =
          this.scrollsHorizontally()
              ? this.ch - currentValue - 3.0F
              : this.cw - currentValue - 3.0F;
      float selectedValue = currentValue;
      return new ScenePctService.ScrollbarMetrics(
          nextValue, previousValue, resultValue, targetValue, candidateValue, selectedValue);
    } else {
      return null;
    }
  }

  private float calculateValue5(float value, float currentValue) {
    float nextValue = this.scrollMin;
    float previousValue = this.calculateValue9();
    if (value > 0.0F) {
      return currentValue > 0.0F
          ? Math.min(previousValue, value + calculateValue6(currentValue, value, previousValue))
          : Math.max(nextValue, value + currentValue);
    }

    if (value < nextValue) {
      if (currentValue < 0.0F) {
        float sourceValue = nextValue - value;
        return Math.max(
            nextValue - previousValue,
            value + calculateValue6(currentValue, sourceValue, previousValue));
      } else {
        return Math.min(0.0F, value + currentValue);
      }
    } else {
      float targetValue = value + currentValue;
      if (targetValue > 0.0F) {
        float inputValue = targetValue;
        return Math.min(previousValue, calculateValue6(inputValue, 0.0F, previousValue));
      } else if (targetValue < nextValue) {
        float outputValue = targetValue - nextValue;
        return Math.max(
            nextValue - previousValue,
            nextValue + calculateValue6(outputValue, 0.0F, previousValue));
      } else {
        return targetValue;
      }
    }
  }

  private static float calculateValue6(float value, float currentValue, float nextValue) {
    float previousValue = Math.max(0.0F, 1.0F - Math.abs(currentValue) / Math.max(1.0F, nextValue));
    float sourceValue = 0.42F * previousValue * previousValue;
    return value * Math.max(0.08F, sourceValue);
  }

  protected boolean handleScroll(float value) {
    if (this.onScrollEvent != null) {
      this.onScrollEvent.accept(value);
      return true;
    } else {
      return false;
    }
  }

  public boolean isHovered() {
    return this.hovered;
  }

  public boolean isPressed() {
    return this.pressed;
  }

  public boolean isDraggable() {
    return this.draggable;
  }

  public String getId() {
    return this.text;
  }

  public float computedX() {
    return this.cx;
  }

  public float computedY() {
    return this.cy;
  }

  public float computedW() {
    return this.cw;
  }

  public float computedH() {
    return this.ch;
  }

  public float scrollY() {
    return this.scrollY;
  }

  public float scrollMin() {
    return this.scrollMin;
  }

  public float scrollbarOpacity() {
    return this.scrollbarAutoHide ? this.scrollbarOpacity : 1.0F;
  }

  public float intrinsicWidth(
      CompositorPushPresentationScaleService compositorPushPresentationScale) {
    if (this.items.isEmpty()) {
      return 0.0F;
    }

    float value = this.paddingLeft + this.paddingRight;
    if (this.direction == ScenePctService.Direction.ROW) {
      return this.calculateValue10(compositorPushPresentationScale, true) + value;
    }

    float currentValue = 0.0F;

    for (ScenePctService scenePct : this.items) {
      if (scenePct.visible && scenePct.positionType != ScenePctService.PositionType.ABSOLUTE) {
        float nextValue =
            calculateValue11(
                scenePct.width, scenePct.intrinsicWidth(compositorPushPresentationScale));
        currentValue =
            Math.max(currentValue, nextValue + scenePct.marginLeft + scenePct.marginRight);
      }
    }

    return currentValue + value;
  }

  public float intrinsicHeight(
      CompositorPushPresentationScaleService compositorPushPresentationScale) {
    if (this.items.isEmpty()) {
      return 0.0F;
    }

    float value = this.paddingTop + this.paddingBottom;
    if (this.direction == ScenePctService.Direction.COLUMN) {
      return this.calculateValue10(compositorPushPresentationScale, false) + value;
    }

    float currentValue = 0.0F;

    for (ScenePctService scenePct : this.items) {
      if (scenePct.visible && scenePct.positionType != ScenePctService.PositionType.ABSOLUTE) {
        float nextValue =
            calculateValue11(
                scenePct.height, scenePct.intrinsicHeight(compositorPushPresentationScale));
        currentValue =
            Math.max(currentValue, nextValue + scenePct.marginTop + scenePct.marginBottom);
      }
    }

    return currentValue + value;
  }

  public float intrinsicHeight(
      CompositorPushPresentationScaleService compositorPushPresentationScale, float value) {
    if (this.direction == ScenePctService.Direction.COLUMN
        && !this.items.isEmpty()
        && Float.isFinite(value)
        && !(value <= 0.0F)) {
      float currentValue = Math.max(0.0F, value - this.paddingLeft - this.paddingRight);
      float nextValue = this.paddingTop + this.paddingBottom;
      int index = 0;

      for (ScenePctService scenePct : this.items) {
        if (scenePct.visible && scenePct.positionType != ScenePctService.PositionType.ABSOLUTE) {
          float previousValue =
              this.calculateValue12(compositorPushPresentationScale, scenePct, currentValue);
          float sourceValue =
              calculateValue11(
                  scenePct.height,
                  scenePct.intrinsicHeight(compositorPushPresentationScale, previousValue));
          sourceValue = calculateValue13(scenePct, sourceValue);
          nextValue += sourceValue + scenePct.marginTop + scenePct.marginBottom;
          index++;
        }
      }

      if (index > 1) {
        nextValue += this.gap * (index - 1);
      }

      return calculateValue18(nextValue);
    } else {
      return this.intrinsicHeight(compositorPushPresentationScale);
    }
  }

  protected abstract void draw(
      CompositorPushPresentationScaleService compositorPushPresentationScale);

  ScenePctService.PresentationTransform presentationTransform(
      ScenePctService.PresentationTransform currentPresentationTransform) {
    return currentPresentationTransform.append(this);
  }

  ScenePctService.PresentationTransform presentationTransform() {
    ArrayList arrayList = new ArrayList();

    for (ScenePctService scenePct = this; scenePct != null; scenePct = scenePct.parent) {
      arrayList.add(scenePct);
    }

    ScenePctService.PresentationTransform currentPresentationTransform =
        ScenePctService.PresentationTransform.IDENTITY;

    for (int currentSize = arrayList.size() - 1; currentSize >= 0; currentSize += -1) {
      currentPresentationTransform =
          currentPresentationTransform.append((ScenePctService<?>) arrayList.get(currentSize));
    }

    return currentPresentationTransform;
  }

  protected void onDetached() {}

  private void updateState6() {
    this.onDetached();
    this.items3.clear();
    this.items4.clear();
    this.onClick = null;
    this.onRightClick = null;
    this.onDrag = null;
    this.onHoverChange = null;
    this.onScrollEvent = null;
    this.onLayout = null;

    for (ScenePctService scenePct : this.items) {
      scenePct.updateState6();
    }
  }

  public void drawTree(
      CompositorPushPresentationScaleService compositorPushPresentationScale, float y) {
    this.drawTree(
        compositorPushPresentationScale, y, ScenePctService.PresentationTransform.IDENTITY);
  }

  void drawTree(
      CompositorPushPresentationScaleService compositorPushPresentationScale,
      float y,
      ScenePctService.PresentationTransform currentPresentationTransform) {
    if (this.visible) {
      if (!this.clip || !(this.cw <= 0.0F) && !(this.ch <= 0.0F)) {
        this.effectiveOpacity = Math.max(0.0F, this.opacity) * y;
        if (!(this.effectiveOpacity < 0.005F)) {
          ScenePctService.PresentationTransform nextPresentationTransform =
              this.presentationTransform(currentPresentationTransform);
          float value = this.cx;
          float currentValue = this.cy;
          float nextValue = this.cw;
          float previousValue = this.ch;
          float sourceValue = this.presentationScale;
          this.cx = nextPresentationTransform.mapX(value);
          this.cy = nextPresentationTransform.mapY(currentValue);
          this.cw = nextValue * nextPresentationTransform.scale;
          this.ch = previousValue * nextPresentationTransform.scale;
          this.presentationScale = nextPresentationTransform.scale;
          compositorPushPresentationScale.pushPresentationScale(nextPresentationTransform.scale);
          byte byteValue = 0;

          try {
            float targetValue =
                this.inheritEdgeSoftness && this.parent != null
                    ? this.parent.effectiveEdgeSoftness
                    : 0.0F;
            float inputValue =
                this.presentationScale > 1.0E-6F ? 1.0F / this.presentationScale : 0.0F;
            float outputValue = Math.min(this.cw, this.ch) * inputValue * 0.4F;
            this.effectiveEdgeSoftness =
                Math.min(Math.max(targetValue, this.ownEdgeSoftness()), outputValue);
            if (this.draggable || this.layerBreak) {
              compositorPushPresentationScale.nextLayer();
            }

            this.draw(compositorPushPresentationScale);
            if (this.clip) {
              if (this instanceof SceneCornerRadiusService sceneCornerRadius) {
                float resultValue =
                    sceneCornerRadius.cornerTL >= 0.0F
                        ? sceneCornerRadius.cornerTL
                        : sceneCornerRadius.cornerRadius;
                float candidateValue =
                    sceneCornerRadius.cornerTR >= 0.0F
                        ? sceneCornerRadius.cornerTR
                        : sceneCornerRadius.cornerRadius;
                float selectedValue =
                    sceneCornerRadius.cornerBR >= 0.0F
                        ? sceneCornerRadius.cornerBR
                        : sceneCornerRadius.cornerRadius;
                float defaultValue =
                    sceneCornerRadius.cornerBL >= 0.0F
                        ? sceneCornerRadius.cornerBL
                        : sceneCornerRadius.cornerRadius;
                compositorPushPresentationScale.pushClip(
                    this.cx,
                    this.cy,
                    this.cw,
                    this.ch,
                    resultValue,
                    candidateValue,
                    selectedValue,
                    defaultValue);
              } else {
                compositorPushPresentationScale.pushClip(this.cx, this.cy, this.cw, this.ch);
              }

              byteValue = 1;
            }

            for (ScenePctService scenePct : this.items) {
              scenePct.drawTree(
                  compositorPushPresentationScale,
                  this.effectiveOpacity,
                  nextPresentationTransform);
            }

            if (this.scrollable && this.scrollContentSize > this.scrollViewSize) {
              this.updateState7(compositorPushPresentationScale);
            }
          } finally {
            if (byteValue != 0) {
              compositorPushPresentationScale.popClip();
            }

            compositorPushPresentationScale.popPresentationScale();
            this.cx = value;
            this.cy = currentValue;
            this.cw = nextValue;
            this.ch = previousValue;
            this.presentationScale = sourceValue;
          }
        }
      }
    }
  }

  private void updateState7(
      CompositorPushPresentationScaleService compositorPushPresentationScale) {
    float value = this.scrollbarAutoHide ? this.scrollbarOpacity : 1.0F;
    if (!(value < 0.003F)) {
      if (this.scrollsHorizontally()) {
        this.updateState8(compositorPushPresentationScale, value);
      } else {
        float currentValue =
            this.scrollbarWidth * this.presentationScale * (this.enabled4 ? 1.65F : 1.0F);
        float nextValue = this.scrollInset * this.presentationScale;
        float previousValue = this.ch - nextValue * 2.0F;
        if (!(previousValue <= 0.0F)) {
          float sourceValue = this.scrollViewSize / this.scrollContentSize;
          float targetValue =
              Math.min(
                  previousValue,
                  Math.max(
                      this.scrollbarWidth * this.presentationScale * 4.0F,
                      previousValue * sourceValue));
          float inputValue = this.scrollContentSize - this.scrollViewSize;
          float outputValue = this.calculateValue8(this.scrollY);
          float resultValue = inputValue > 0.0F ? -outputValue / inputValue : 0.0F;
          float candidateValue = this.cy + nextValue + resultValue * (previousValue - targetValue);
          if (this.scrollY > 0.0F) {
            float selectedValue = this.scrollY * this.presentationScale * 0.45F;
            targetValue = Math.max(currentValue * 2.0F, targetValue - selectedValue);
            candidateValue = this.cy + nextValue;
          } else if (this.scrollY < this.scrollMin) {
            float defaultValue = (this.scrollMin - this.scrollY) * this.presentationScale * 0.45F;
            targetValue = Math.max(currentValue * 2.0F, targetValue - defaultValue);
            candidateValue = this.cy + nextValue + previousValue - targetValue;
          }

          float initialValue = this.cx + this.cw - currentValue - 3.0F * this.presentationScale;
          float resolvedValue =
              this.scrollbarRadius >= 0.0F ? this.scrollbarRadius : this.scrollbarWidth / 2.0F;
          float computedValue = this.effectiveOpacity * value;
          if (this.scrollbarTrackColor != 0) {
            compositorPushPresentationScale.roundedRect(
                initialValue,
                this.cy + nextValue,
                currentValue,
                previousValue,
                resolvedValue,
                mulAlpha(this.scrollbarTrackColor, computedValue));
          }

          compositorPushPresentationScale.roundedRect(
              initialValue,
              candidateValue,
              currentValue,
              targetValue,
              resolvedValue,
              mulAlpha(
                  this.enabled4 ? calculateValue7(this.scrollbarColor) : this.scrollbarColor,
                  computedValue));
        }
      }
    }
  }

  private void updateState8(
      CompositorPushPresentationScaleService compositorPushPresentationScale, float value) {
    float currentValue =
        this.scrollbarWidth * this.presentationScale * (this.enabled4 ? 1.65F : 1.0F);
    float nextValue = this.scrollInset * this.presentationScale;
    float previousValue = this.cw - nextValue * 2.0F;
    if (!(previousValue <= 0.0F)) {
      float sourceValue = this.scrollViewSize / this.scrollContentSize;
      float targetValue =
          Math.min(
              previousValue,
              Math.max(
                  this.scrollbarWidth * this.presentationScale * 4.0F,
                  previousValue * sourceValue));
      float inputValue = this.scrollContentSize - this.scrollViewSize;
      float outputValue =
          inputValue > 0.0F ? -this.calculateValue8(this.scrollY) / inputValue : 0.0F;
      float resultValue = this.cx + nextValue + outputValue * (previousValue - targetValue);
      float candidateValue = this.cy + this.ch - currentValue - 3.0F * this.presentationScale;
      float selectedValue =
          this.scrollbarRadius >= 0.0F ? this.scrollbarRadius : this.scrollbarWidth / 2.0F;
      float defaultValue = this.effectiveOpacity * value;
      if (this.scrollbarTrackColor != 0) {
        compositorPushPresentationScale.roundedRect(
            this.cx + nextValue,
            candidateValue,
            previousValue,
            currentValue,
            selectedValue,
            mulAlpha(this.scrollbarTrackColor, defaultValue));
      }

      compositorPushPresentationScale.roundedRect(
          resultValue,
          candidateValue,
          targetValue,
          currentValue,
          selectedValue,
          mulAlpha(
              this.enabled4 ? calculateValue7(this.scrollbarColor) : this.scrollbarColor,
              defaultValue));
    }
  }

  private static int calculateValue7(int value) {
    int currentValue = value & 0xFF000000;
    int nextValue = Math.min(255, (value >>> 16 & 0xFF) + 28);
    int previousValue = Math.min(255, (value >>> 8 & 0xFF) + 28);
    int sourceValue = Math.min(255, (value & 0xFF) + 28);
    return currentValue | nextValue << 16 | previousValue << 8 | sourceValue;
  }

  public void performLayout(
      CompositorPushPresentationScaleService compositorPushPresentationScale,
      float value,
      float currentValue,
      float nextValue,
      float previousValue) {
    this.updateState10(
        compositorPushPresentationScale, value, currentValue, nextValue, previousValue, false);
  }

  private void updateState9(
      CompositorPushPresentationScaleService compositorPushPresentationScale,
      float value,
      float currentValue,
      float nextValue,
      float previousValue) {
    this.updateState10(
        compositorPushPresentationScale, value, currentValue, nextValue, previousValue, true);
  }

  private void updateState10(
      CompositorPushPresentationScaleService compositorPushPresentationScale,
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      boolean enabled) {
    boolean currentEnabled = inLayout;
    inLayout = true;

    try {
      if (enabled) {
        this.cw = calculateValue18(nextValue);
        this.ch = calculateValue18(previousValue);
      } else {
        float sourceValue = resolve(this.width, nextValue);
        float targetValue = resolve(this.height, previousValue);
        this.cw = sourceValue == -1.0F ? nextValue : sourceValue;
        this.ch = targetValue == -1.0F ? previousValue : targetValue;
        this.cw = calculateValue18(clampSize(this.cw, this.minWidth, this.maxWidth, nextValue));
        this.ch =
            calculateValue18(clampSize(this.ch, this.minHeight, this.maxHeight, previousValue));
      }

      this.cx = value + this.x;
      this.cy = currentValue + this.y;
      float inputValue = Math.max(0.0F, this.cw - this.paddingLeft - this.paddingRight);
      float outputValue = Math.max(0.0F, this.ch - this.paddingTop - this.paddingBottom);
      float resultValue = this.cx + this.paddingLeft;
      float candidateValue = this.cy + this.paddingTop;
      if (!this.items.isEmpty()) {
        if (this.direction == ScenePctService.Direction.NONE) {
          this.updateState11(
              compositorPushPresentationScale,
              resultValue,
              candidateValue,
              inputValue,
              outputValue);
        } else {
          this.updateState12(
              compositorPushPresentationScale,
              resultValue,
              candidateValue,
              inputValue,
              outputValue);
        }
      }

      if (this.onLayout != null) {
        try {
          this.onLayout.run();
        } catch (Exception exception) {
        }
      }
    } finally {
      inLayout = currentEnabled;
    }
  }

  private void updateState11(
      CompositorPushPresentationScaleService compositorPushPresentationScale,
      float value,
      float currentValue,
      float nextValue,
      float previousValue) {
    for (ScenePctService scenePct : this.items) {
      if (scenePct.visible) {
        if (scenePct.positionType == ScenePctService.PositionType.ABSOLUTE) {
          this.updateState14(
              compositorPushPresentationScale,
              scenePct,
              value,
              currentValue,
              nextValue,
              previousValue);
        } else {
          float sourceValue =
              calculateValue14(
                  scenePct.width,
                  nextValue,
                  scenePct.intrinsicWidth(compositorPushPresentationScale));
          float targetValue =
              calculateValue14(
                  scenePct.height,
                  previousValue,
                  scenePct.intrinsicHeight(compositorPushPresentationScale, sourceValue));
          sourceValue = calculateValue17(scenePct, sourceValue, nextValue, true);
          targetValue = calculateValue17(scenePct, targetValue, previousValue, false);
          scenePct.updateState9(
              compositorPushPresentationScale,
              value + scenePct.marginLeft,
              currentValue + scenePct.marginTop,
              sourceValue,
              targetValue);
        }
      }
    }
  }

  private void updateState12(
      CompositorPushPresentationScaleService compositorPushPresentationScale,
      float value,
      float currentValue,
      float nextValue,
      float previousValue) {
    int sourceValue = this.direction == ScenePctService.Direction.ROW ? 1 : 0;
    float targetValue = sourceValue != 0 ? nextValue : previousValue;
    float inputValue = sourceValue != 0 ? previousValue : nextValue;
    ArrayList currentSize = new ArrayList(this.items.size());

    for (ScenePctService scenePct : this.items) {
      if (scenePct.visible && scenePct.positionType == ScenePctService.PositionType.FLOW) {
        currentSize.add(scenePct);
      }
    }

    int index = currentSize.size();
    float[] floats = new float[index];
    float[] currentFloats = new float[index];
    float outputValue = 0.0F;

    for (int currentIndex = 0; currentIndex < index; currentIndex++) {
      ScenePctService currentScenePct = (ScenePctService) currentSize.get(currentIndex);
      float resultValue =
          sourceValue != 0
              ? currentScenePct.intrinsicWidth(compositorPushPresentationScale)
              : currentScenePct.intrinsicHeight(
                  compositorPushPresentationScale,
                  this.calculateValue12(
                      compositorPushPresentationScale, currentScenePct, inputValue));
      float candidateValue = sourceValue != 0 ? currentScenePct.width : currentScenePct.height;
      float selectedValue =
          currentScenePct.layoutOperationHandler instanceof LayoutOperationHandler.Auto
              ? calculateValue14(candidateValue, targetValue, resultValue)
              : calculateValue15(currentScenePct.layoutOperationHandler, targetValue, resultValue);
      floats[currentIndex] = calculateValue18(selectedValue);
      currentFloats[currentIndex] =
          calculateValue17(currentScenePct, floats[currentIndex], targetValue, (sourceValue != 0));
      outputValue +=
          sourceValue != 0
              ? currentScenePct.marginLeft + currentScenePct.marginRight
              : currentScenePct.marginTop + currentScenePct.marginBottom;
    }

    float defaultValue = index > 1 ? this.gap * (index - 1) : 0.0F;
    float initialValue = targetValue - outputValue - defaultValue;
    updateState13(
        currentSize, floats, currentFloats, initialValue, targetValue, (sourceValue != 0));
    float resolvedValue = outputValue + defaultValue;

    for (float computedValue : currentFloats) {
      resolvedValue += computedValue;
    }

    float cachedValue = Math.max(0.0F, targetValue - resolvedValue);
    float pendingValue = 0.0F;
    float activeValue = this.gap;
    switch (this.justify) {
      case CENTER:
        pendingValue = cachedValue / 2.0F;
        break;
      case END:
        pendingValue = cachedValue;
        break;
      case SPACE_BETWEEN:
        if (index > 1) {
          activeValue = this.gap + cachedValue / (index - 1);
        }
    }

    float fallbackValue = value;
    float primaryValue = currentValue;
    if (this.scrollable) {
      if (this.scrollBounce && this.enabled) {
        this.updateState16();
      } else {
        this.scrollY = this.calculateValue8(this.scrollY);
      }

      if (sourceValue != 0) {
        fallbackValue += this.scrollY;
      } else {
        primaryValue += this.scrollY;
      }
    }

    for (int nextIndex = 0; nextIndex < index; nextIndex++) {
      ScenePctService nextScenePct = (ScenePctService) currentSize.get(nextIndex);
      float secondaryValue = currentFloats[nextIndex];
      float tertiaryValue = sourceValue != 0 ? nextScenePct.marginLeft : nextScenePct.marginTop;
      float temporaryValue =
          sourceValue != 0 ? nextScenePct.marginRight : nextScenePct.marginBottom;
      float requestedValue = sourceValue != 0 ? nextScenePct.marginTop : nextScenePct.marginLeft;
      float actualValue = sourceValue != 0 ? nextScenePct.marginBottom : nextScenePct.marginRight;
      float expectedValue = Math.max(0.0F, inputValue - requestedValue - actualValue);
      float minimumValue = sourceValue != 0 ? nextScenePct.height : nextScenePct.width;
      float maximumValue =
          sourceValue != 0
              ? nextScenePct.intrinsicHeight(compositorPushPresentationScale, secondaryValue)
              : nextScenePct.intrinsicWidth(compositorPushPresentationScale);
      float startValue =
          this.align == ScenePctService.Align.STRETCH && minimumValue == -1.0F
              ? expectedValue
              : calculateValue14(minimumValue, inputValue, maximumValue);
      startValue = calculateValue17(nextScenePct, startValue, inputValue, sourceValue == 0);

      float endValue =
          switch (this.align) {
            case CENTER -> requestedValue + (expectedValue - startValue) / 2.0F;
            case END -> requestedValue + expectedValue - startValue;
            default -> requestedValue;
          };
      pendingValue += tertiaryValue;
      if (sourceValue != 0) {
        nextScenePct.updateState9(
            compositorPushPresentationScale,
            fallbackValue + pendingValue,
            primaryValue + endValue,
            secondaryValue,
            startValue);
      } else {
        nextScenePct.updateState9(
            compositorPushPresentationScale,
            fallbackValue + endValue,
            primaryValue + pendingValue,
            startValue,
            secondaryValue);
      }

      pendingValue += secondaryValue + temporaryValue;
      if (nextIndex + 1 < index) {
        pendingValue += activeValue;
      }
    }

    if (this.scrollable) {
      this.updateState15(resolvedValue, targetValue);
    }

    for (ScenePctService previousScenePct : this.items) {
      if (previousScenePct.visible
          && previousScenePct.positionType == ScenePctService.PositionType.ABSOLUTE) {
        this.updateState14(
            compositorPushPresentationScale,
            previousScenePct,
            fallbackValue,
            primaryValue,
            nextValue,
            previousValue);
      }
    }
  }

  private static void updateState13(
      List<ScenePctService<?>> items,
      float[] floats,
      float[] currentFloats,
      float value,
      float currentValue,
      boolean enabled) {
    int index = items.size();
    boolean[] booleans = new boolean[index];

    for (int currentIndex = 0; currentIndex <= index; currentIndex++) {
      float nextValue = 0.0F;

      for (float previousValue : currentFloats) {
        nextValue += previousValue;
      }

      float sourceValue = value - nextValue;
      if (Math.abs(sourceValue) < 1.0E-4F) {
        return;
      }

      int targetValue = sourceValue > 0.0F ? 1 : 0;
      float inputValue = 0.0F;

      for (int nextIndex = 0; nextIndex < index; nextIndex++) {
        if (!booleans[nextIndex]) {
          ScenePctService scenePct = (ScenePctService) items.get(nextIndex);
          float outputValue =
              targetValue != 0
                  ? scenePct.flex
                  : scenePct.flexShrink * Math.max(0.0F, floats[nextIndex]);
          if (outputValue > 0.0F) {
            inputValue += outputValue;
          } else {
            booleans[nextIndex] = true;
          }
        }
      }

      if (inputValue <= 0.0F) {
        return;
      }

      byte byteValue = 0;
      float[] nextFloats = (float[]) currentFloats.clone();

      for (int previousIndex = 0; previousIndex < index; previousIndex++) {
        if (!booleans[previousIndex]) {
          ScenePctService currentScenePct = (ScenePctService) items.get(previousIndex);
          float resultValue =
              targetValue != 0
                  ? currentScenePct.flex
                  : currentScenePct.flexShrink * Math.max(0.0F, floats[previousIndex]);
          float candidateValue =
              currentFloats[previousIndex] + sourceValue * resultValue / inputValue;
          float selectedValue =
              calculateValue17(currentScenePct, candidateValue, currentValue, enabled);
          nextFloats[previousIndex] = selectedValue;
          if (Math.abs(selectedValue - candidateValue) > 1.0E-4F) {
            booleans[previousIndex] = true;
            byteValue = 1;
          }
        }
      }

      System.arraycopy(nextFloats, 0, currentFloats, 0, index);
      if (byteValue == 0) {
        return;
      }
    }
  }

  private void updateState14(
      CompositorPushPresentationScaleService compositorPushPresentationScale,
      ScenePctService<?> scenePct,
      float value,
      float currentValue,
      float nextValue,
      float previousValue) {
    float sourceValue = calculateValue16(scenePct.layoutOperationHandler5, nextValue);
    float targetValue = calculateValue16(scenePct.layoutOperationHandler3, nextValue);
    float inputValue = calculateValue16(scenePct.layoutOperationHandler2, previousValue);
    float outputValue = calculateValue16(scenePct.layoutOperationHandler4, previousValue);
    int resultValue = scenePct.width == -1.0F ? 1 : 0;
    int candidateValue = scenePct.height == -1.0F ? 1 : 0;
    float selectedValue =
        resultValue != 0 && checkCondition5(sourceValue) && checkCondition5(targetValue)
            ? nextValue - sourceValue - targetValue - scenePct.marginLeft - scenePct.marginRight
            : calculateValue14(
                scenePct.width,
                nextValue,
                scenePct.intrinsicWidth(compositorPushPresentationScale));
    float defaultValue =
        candidateValue != 0 && checkCondition5(inputValue) && checkCondition5(outputValue)
            ? previousValue - inputValue - outputValue - scenePct.marginTop - scenePct.marginBottom
            : calculateValue14(
                scenePct.height,
                previousValue,
                scenePct.intrinsicHeight(compositorPushPresentationScale, selectedValue));
    selectedValue = calculateValue17(scenePct, selectedValue, nextValue, true);
    defaultValue = calculateValue17(scenePct, defaultValue, previousValue, false);
    float initialValue;
    if (checkCondition5(sourceValue)) {
      initialValue = value + sourceValue + scenePct.marginLeft;
    } else if (checkCondition5(targetValue)) {
      initialValue = value + nextValue - targetValue - selectedValue - scenePct.marginRight;
    } else {
      initialValue = value + scenePct.marginLeft;
    }

    float resolvedValue;
    if (checkCondition5(inputValue)) {
      resolvedValue = currentValue + inputValue + scenePct.marginTop;
    } else if (checkCondition5(outputValue)) {
      resolvedValue =
          currentValue + previousValue - outputValue - defaultValue - scenePct.marginBottom;
    } else {
      resolvedValue = currentValue + scenePct.marginTop;
    }

    scenePct.updateState9(
        compositorPushPresentationScale, initialValue, resolvedValue, selectedValue, defaultValue);
  }

  private void updateState15(float value, float currentValue) {
    this.scrollContentSize = Math.max(0.0F, value);
    this.scrollViewSize = Math.max(0.0F, currentValue);
    this.scrollMin = Math.min(0.0F, -(this.scrollContentSize - this.scrollViewSize));
    if (this.scrollMin >= -1.0E-4F) {
      this.scrollY = 0.0F;
      this.scrollTarget = 0.0F;
      this.value3 = 0.0F;
      this.enabled = false;
    } else if (this.scrollBounce && this.enabled) {
      this.updateState16();
    } else {
      this.scrollY = this.calculateValue8(this.scrollY);
      this.scrollTarget = this.calculateValue8(this.scrollTarget);
    }

    for (ScenePctService.RunningAnim runningAnim : this.items3) {
      if (runningAnim.property.equals("scrollY")) {
        float nextValue = this.calculateValue8(runningAnim.to);
        if (!checkCondition(nextValue, runningAnim.to)) {
          runningAnim.to = nextValue;
          runningAnim.from = this.scrollY;
          runningAnim.velocity = 0.0F;
          runningAnim.elapsed = 0.0F;
        }
      }
    }
  }

  private float calculateValue8(float value) {
    return Math.max(this.scrollMin, Math.min(0.0F, value));
  }

  private float calculateValue9() {
    return Math.min(36.0F, Math.max(16.0F, this.scrollViewSize * 0.12F));
  }

  private void updateState16() {
    float value = this.calculateValue9();
    float currentValue = this.scrollMin - value;
    float nextValue = value;
    if (this.scrollY < currentValue) {
      this.scrollY = currentValue;
      if (this.value3 < 0.0F) {
        this.value3 = 0.0F;
      }
    } else if (this.scrollY > nextValue) {
      this.scrollY = nextValue;
      if (this.value3 > 0.0F) {
        this.value3 = 0.0F;
      }
    }

    this.scrollTarget = Math.max(currentValue, Math.min(nextValue, this.scrollTarget));
  }

  private float calculateValue10(
      CompositorPushPresentationScaleService compositorPushPresentationScale, boolean enabled) {
    float value = 0.0F;
    int index = 0;

    for (ScenePctService scenePct : this.items) {
      if (scenePct.visible && scenePct.positionType != ScenePctService.PositionType.ABSOLUTE) {
        float currentValue = enabled ? scenePct.width : scenePct.height;
        float nextValue =
            enabled
                ? scenePct.intrinsicWidth(compositorPushPresentationScale)
                : scenePct.intrinsicHeight(compositorPushPresentationScale);
        value += calculateValue11(currentValue, nextValue);
        value +=
            enabled
                ? scenePct.marginLeft + scenePct.marginRight
                : scenePct.marginTop + scenePct.marginBottom;
        index++;
      }
    }

    if (index > 1) {
      value += this.gap * (index - 1);
    }

    return Math.max(0.0F, value);
  }

  private static float calculateValue11(float value, float currentValue) {
    return value != -1.0F && !isPct(value)
        ? calculateValue18(value)
        : calculateValue18(currentValue);
  }

  private float calculateValue12(
      CompositorPushPresentationScaleService compositorPushPresentationScale,
      ScenePctService<?> scenePct,
      float value) {
    float currentValue = Math.max(0.0F, value - scenePct.marginLeft - scenePct.marginRight);
    float nextValue =
        this.align == ScenePctService.Align.STRETCH && scenePct.width == -1.0F
            ? currentValue
            : calculateValue14(
                scenePct.width, value, scenePct.intrinsicWidth(compositorPushPresentationScale));
    return calculateValue17(scenePct, nextValue, value, true);
  }

  private static float calculateValue13(ScenePctService<?> scenePct, float value) {
    float currentValue = calculateValue18(value);
    if (scenePct.minHeight != -1.0F && !isPct(scenePct.minHeight)) {
      currentValue = Math.max(currentValue, calculateValue18(scenePct.minHeight));
    }

    if (scenePct.maxHeight != -1.0F && !isPct(scenePct.maxHeight)) {
      currentValue = Math.min(currentValue, calculateValue18(scenePct.maxHeight));
    }

    return currentValue;
  }

  private static float calculateValue14(float value, float currentValue, float nextValue) {
    return value == -1.0F
        ? calculateValue18(nextValue)
        : calculateValue18(resolve(value, currentValue));
  }

  private static float calculateValue15(
      LayoutOperationHandler layoutOperation, float currentValue, float nextValue) {
    return switch (layoutOperation) {
      case LayoutOperationHandler.Auto auto -> calculateValue18(nextValue);
      case LayoutOperationHandler.Px px -> px.value();
      case LayoutOperationHandler.Percent percent ->
          calculateValue18(currentValue) * percent.value() / 100.0F;
      default -> throw new MatchException(null, null);
    };
  }

  private static float calculateValue16(
      LayoutOperationHandler layoutOperation, float currentValue) {
    return switch (layoutOperation) {
      case LayoutOperationHandler.Auto auto -> Float.NaN;
      case LayoutOperationHandler.Px px -> px.value();
      case LayoutOperationHandler.Percent percent ->
          calculateValue18(currentValue) * percent.value() / 100.0F;
      default -> throw new MatchException(null, null);
    };
  }

  private static boolean checkCondition5(float value) {
    return !Float.isNaN(value);
  }

  private static float calculateValue17(
      ScenePctService<?> scenePct, float value, float currentValue, boolean enabled) {
    float nextValue =
        enabled
            ? clampSize(value, scenePct.minWidth, scenePct.maxWidth, currentValue)
            : clampSize(value, scenePct.minHeight, scenePct.maxHeight, currentValue);
    return calculateValue18(nextValue);
  }

  private static float calculateValue18(float value) {
    return !Float.isFinite(value) ? 0.0F : Math.max(0.0F, value);
  }

  public static int mulAlpha(int value, float opacity) {
    if (opacity >= 1.0F) {
      return value;
    }

    int currentValue = (int) ((value >>> 24 & 0xFF) * Math.max(0.0F, opacity));
    return currentValue << 24 | value & 16777215;
  }

  public enum Align {
    START,
    CENTER,
    END,
    STRETCH;

    private static ScenePctService.Align[] $values() {
      return new ScenePctService.Align[] {START, CENTER, END, STRETCH};
    }
  }

  public enum CursorStyle {
    DEFAULT,
    POINTER,
    TEXT,
    CROSSHAIR;

    private static ScenePctService.CursorStyle[] $values() {
      return new ScenePctService.CursorStyle[] {DEFAULT, POINTER, TEXT, CROSSHAIR};
    }
  }

  public enum Direction {
    NONE,
    ROW,
    COLUMN;

    private static ScenePctService.Direction[] $values() {
      return new ScenePctService.Direction[] {NONE, ROW, COLUMN};
    }
  }

  public enum Justify {
    START,
    CENTER,
    END,
    SPACE_BETWEEN;

    private static ScenePctService.Justify[] $values() {
      return new ScenePctService.Justify[] {START, CENTER, END, SPACE_BETWEEN};
    }
  }

  public enum PositionType {
    FLOW,
    ABSOLUTE;

    private static ScenePctService.PositionType[] $values() {
      return new ScenePctService.PositionType[] {FLOW, ABSOLUTE};
    }
  }

  record PresentationTransform(float scale, float tx, float ty) {
    static final ScenePctService.PresentationTransform IDENTITY =
        new ScenePctService.PresentationTransform(1.0F, 0.0F, 0.0F);

    ScenePctService.PresentationTransform append(ScenePctService<?> scenePct) {
      float value = Float.isFinite(scenePct.scale) ? Math.max(0.0F, scenePct.scale) : 1.0F;
      float currentValue = Float.isFinite(scenePct.translateX) ? scenePct.translateX : 0.0F;
      float nextValue = Float.isFinite(scenePct.translateY) ? scenePct.translateY : 0.0F;
      float previousValue = scenePct.cx + scenePct.cw * 0.5F;
      float sourceValue = scenePct.cy + scenePct.ch * 0.5F;
      float targetValue = (1.0F - value) * previousValue + currentValue;
      float inputValue = (1.0F - value) * sourceValue + nextValue;
      return new ScenePctService.PresentationTransform(
          this.scale * value,
          this.scale * targetValue + this.tx,
          this.scale * inputValue + this.ty);
    }

    float mapX(float value) {
      return this.scale * value + this.tx;
    }

    float mapY(float value) {
      return this.scale * value + this.ty;
    }

    float inverseX(float value) {
      return this.scale > 1.0E-6F ? (value - this.tx) / this.scale : value - this.tx;
    }

    float inverseY(float value) {
      return this.scale > 1.0E-6F ? (value - this.ty) / this.scale : value - this.ty;
    }
  }

  static final class RunningAnim {
    final String property;
    float from;
    float to;
    SceneEaseHandler anim;
    float elapsed;
    float velocity;
    float delay;
    int repeat;
    boolean yoyo;
    Runnable onComplete;

    RunningAnim(String text, float value, float currentValue, SceneEaseHandler sceneEase) {
      this.property = text;
      this.from = value;
      this.to = currentValue;
      this.anim = sceneEase;
    }
  }

  static final class RunningColorAnim {
    final String property;
    int from;
    int to;
    SceneEaseHandler anim;
    float progress;
    float velocity;
    float elapsed;
    float delay;
    int repeat;
    boolean yoyo;
    Runnable onComplete;

    RunningColorAnim(String text, int value, int currentValue, SceneEaseHandler sceneEase) {
      this.property = text;
      this.from = value;
      this.to = currentValue;
      this.anim = sceneEase;
    }
  }

  private record ScrollbarMetrics(
      float trackStart,
      float trackSize,
      float thumbStart,
      float thumbSize,
      float crossStart,
      float crossSize) {}
}
