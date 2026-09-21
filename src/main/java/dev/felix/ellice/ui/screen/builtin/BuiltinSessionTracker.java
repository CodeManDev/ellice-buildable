package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.account.AccountNewThreadService;
import dev.felix.ellice.account.AccountTypeData;
import dev.felix.ellice.account.AccountCodec;
import dev.felix.ellice.account.AccountFetchHeadService;
import dev.felix.ellice.account.AccountAllService;
import dev.felix.ellice.account.AccountApplyOfflineService;
import dev.felix.ellice.account.AccountCodeService;
import dev.felix.ellice.compat.CompatNewThreadService;
import dev.felix.ellice.compat.CompatOpenUriService;
import dev.felix.ellice.compat.CompatSavedService;
import dev.felix.ellice.compat.DisplayMetrics;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.feature.privacy.PrivacyRevisionService;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.server.ServerViewTracker;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentMountService;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentThemeService;
import dev.felix.ellice.ui.material.MaterialLabelService;
import dev.felix.ellice.ui.material.MaterialComponent;
import dev.felix.ellice.ui.material.MaterialEnterService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.material.MaterialJoinedService;
import dev.felix.ellice.ui.material.MaterialResponsiveService;
import dev.felix.ellice.ui.material.MaterialTerrainTooltipsService;
import dev.felix.ellice.ui.material.AnimatedSelectionIndicator;
import dev.felix.ellice.ui.material.MaterialTextService;
import dev.felix.ellice.ui.scene.SceneTextureService;
import dev.felix.ellice.ui.scene.SceneCodec;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.SceneRenderer;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.SceneResizeMarginService;
import dev.felix.ellice.ui.scene.control.ControlLetterSpacingService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;
import dev.felix.ellice.ui.screen.ScreenScreenIdService;
import dev.felix.ellice.ui.screen.ScreenOperationHandler;
import dev.felix.ellice.ui.theme.ThemeIsSetService;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletionException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;

public final class BuiltinSessionTracker implements ScreenOperationHandler {
   private final AccountAllService accountAllService;
   private final AccountNewThreadService accountNewThreadService = new AccountNewThreadService();
   private final CompositorPushPresentationScaleService compositorPushPresentationScaleService;
   private final ServerViewTracker serverViewTracker;
   private final Supplier<BuiltinSessionTracker.Session> supplier;
   private final AtomicLong atomicLong = new AtomicLong();
   private final Set<UUID> values2 = new HashSet<>();
   private final List<BuiltinSessionTracker.DelayedAction> items = new ArrayList<>();
   private BuiltinSessionTracker.Mode mode = BuiltinSessionTracker.Mode.LIST;
   private SceneCornerRadiusService sceneCornerRadiusService;
   private ComponentMountService componentMountService;
   private SceneResizeMarginService sceneResizeMarginService;
   private ScreenScreenIdService screenScreenIdService;
   private ServerListRenderer renderer;
   private ScenePctService<?> scenePctService;
   private ControlLetterSpacingService controlLetterSpacingService;
   private ControlLetterSpacingService controlLetterSpacingService2;
   private AccountCodec accountCodec;
   private String text2 = "";
   private String text3 = "Paste a session cookie or choose a .txt / .json export.";
   private boolean enabled;
   private boolean enabled2;
   private BuiltinSessionTracker.Session session2;
   private boolean enabled3;
   private boolean enabled4;
   private boolean enabled5;
   private boolean enabled6;
   private float value2 = 1120.0F;
   private float value3 = 720.0F;
   private float value4 = 1040.0F;
   private float value5 = 660.0F;
   private UUID uUID;
   private String text4 = "";
   private String text5 = "";
   private String text6;
   private String text7 = "--------";
   private String text8 = "Asking Microsoft for a sign-in code…";
   private boolean enabled7;
   private String text9 = "1–16 characters: letters, numbers and underscores.";
   private boolean enabled8;

   public BuiltinSessionTracker(AccountAllService accountAll, CompositorPushPresentationScaleService compositorPushPresentationScale) {
      this(accountAll, compositorPushPresentationScale, null, () -> {
         Minecraft minecraft = Minecraft.getInstance();
         return new BuiltinSessionTracker.Session(minecraft.getUser().getProfileId(), minecraft.getUser().getName(), minecraft.level != null);
      });
   }

   public BuiltinSessionTracker(AccountAllService accountAll, CompositorPushPresentationScaleService compositorPushPresentationScale, ServerViewTracker currentServerViewTracker, Supplier<BuiltinSessionTracker.Session> currentSupplier) {
      this.accountAllService = Objects.requireNonNull(accountAll);
      this.compositorPushPresentationScaleService = compositorPushPresentationScale;
      this.serverViewTracker = currentServerViewTracker;
      this.supplier = Objects.requireNonNull(currentSupplier);
   }

   @Override
   public String id() {
      return "account-manager";
   }

   @Override
   public SceneCodec.Preset transitionPreset() {
      return SceneCodec.Preset.FADE;
   }

   @Override
   public SceneCodec.Options transitionOptions() {
      SceneCodec.Options options = new SceneCodec.Options();
      options.duration = 0.18F;
      return options;
   }

   @Override
   public float backgroundDesaturation() {
      return 0.0F;
   }

   @Override
   public boolean overlaysPreviousScreen() {
      return true;
   }

   @Override
   public boolean opensAsWindow() {
      return true;
   }

   @Override
   public void onSuspend(ScreenScreenIdService screenScreenId) {
      this.updateState33();
   }

   @Override
   public ScenePctService<?> build(ScreenScreenIdService screenScreenId) {
      this.screenScreenIdService = screenScreenId;
      this.session2 = this.supplier.get();
      ServerViewTracker currentServerViewTracker = this.serverViewTracker != null ? this.serverViewTracker : (CoreIsInitializedHandler.isReady() ? CoreIsInitializedHandler.get().serverHub() : null);
      if (currentServerViewTracker != null) {
         if (CoreIsInitializedHandler.isReady() && this.serverViewTracker == null) {
            currentServerViewTracker.reloadSaved();
         }

         this.renderer = new ServerListRenderer(currentServerViewTracker, (item, currentItem) -> {
            if (!this.supplier.get().inWorld() && this.uUID == null) {
               CoreIsInitializedHandler.get().screens().closeForNavigation();
               CompatSavedService.connect(item, currentItem);
            }
         });
      }

      this.sceneCornerRadiusService = new MaterialTerrainTooltipsService();
      this.sceneCornerRadiusService
         .id("account-manager.panel-shell")
         .size(LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.percent(100.0F))
         .direction(ScenePctService.Direction.NONE)
         .backgroundColor(
            ScenePctService.mulAlpha(
               MaterialIsLightService.SURFACE_LOWEST, MaterialIsLightService.isLight() ? 0.24F : 0.18F
            )
         )
         .blur(8.0F)
         .interactive(true);
      this.componentMountService = new ComponentMountService().mount(this::createComponentKeyService, screenScreenId == null ? new ThemeIsSetService() : screenScreenId.theme());
      this.componentMountService.absolute().inset(LayoutOperationHandler.px(0.0F)).size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto());
      this.sceneCornerRadiusService.addChild(this.componentMountService);
      return this.sceneCornerRadiusService;
   }

   public void viewport(float x, float y) {
      if (x != this.value2 || y != this.value3) {
         this.value2 = x;
         this.value3 = y;
         if (this.sceneResizeMarginService == null || this.sceneResizeMarginService.isAnchored()) {
            this.updateState(
               Math.min(1040.0F, x - this.calculateValue() * 2.0F),
               Math.min(660.0F, y - this.calculateValue() * 2.0F)
            );
         }

         this.updateState2();
      }
   }

   private float calculateValue() {
      return !(this.value2 < 600.0F) && !(this.value3 < 360.0F)
         ? 24.0F
         : 8.0F;
   }

   private void updateState(float value, float currentValue) {
      value = Math.max(0.0F, value);
      currentValue = Math.max(0.0F, currentValue);
      if (this.value4 != value || this.value5 != currentValue) {
         this.value4 = value;
         this.value5 = currentValue;
         int nextValue = value < 940.0F ? 1 : 0;
         int previousValue = value < 600.0F ? 1 : 0;
         int sourceValue = currentValue < 360.0F ? 1 : 0;
          if (this.enabled3 != (nextValue != 0) || this.enabled4 != (previousValue != 0) || this.enabled5 != (sourceValue != 0)) {
            this.updateState33();
         }

         this.enabled3 = (nextValue != 0);
         this.enabled4 = (previousValue != 0);
         this.enabled5 = (sourceValue != 0);
         this.updateState2();
      }
   }

   private ComponentKeyService<?> createComponentKeyService(ComponentThemeService componentTheme) {
      this.sceneCornerRadiusService
         .backgroundColor(
            ScenePctService.mulAlpha(
               MaterialIsLightService.SURFACE_LOWEST, MaterialIsLightService.isLight() ? 0.24F : 0.18F
            )
         );
      ArrayList arrayList = new ArrayList();
      arrayList.add(this.createComponentKeyService6().props(item -> item.visible(!this.enabled3 || !this.enabled6 || this.renderer == null)));
      if (this.renderer != null) {
         this.renderer
            .account(this.session2.uuid(), this.session2.name(), !this.session2.inWorld() && this.uUID == null);
         float value = this.value4;
         this.renderer.width(this.enabled3 ? value - (this.enabled4 ? 16 : 32) : value - 384.0F);
         arrayList.add(
            this.renderer
               .render(componentTheme)
               .key("servers")
               .props(
                  item -> item.flex(1.0F)
                     .minWidth(0.0F)
                     .height(LayoutOperationHandler.percent(100.0F))
                     .visible(!this.enabled3 || this.enabled6)
               )
         );
      }

      ArrayList<ComponentKeyService<?>> currentArrayList = new ArrayList<>();
      currentArrayList.add(this.createComponentKeyService2());
      currentArrayList.add(this.createComponentKeyService3());
      currentArrayList.add(
         ComponentBoxService.row(
               item -> item.id("accounts.workspace")
                  .width(LayoutOperationHandler.percent(100.0F))
                  .flex(1.0F)
                  .minHeight(0.0F)
                  .padding(
                     this.enabled5
                        ? 4.0F
                        : (this.enabled4 ? 8.0F : 16.0F)
                  )
                  .gap(16.0F)
                  .align(ScenePctService.Align.STRETCH)
                  .visible(this.mode == BuiltinSessionTracker.Mode.LIST),
               arrayList.toArray(ComponentKeyService[]::new)
            )
            .key("workspace")
      );
      if (this.mode != BuiltinSessionTracker.Mode.LIST) {
         currentArrayList.add(this.createComponentKeyService9().key("form:" + this.mode));
      }

      return ComponentBoxService.column(
            item -> item.size(
                  LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.percent(100.0F)
               )
               .padding(this.calculateValue())
               .align(ScenePctService.Align.CENTER)
               .justify(ScenePctService.Justify.CENTER),
            ComponentBoxService.<SceneResizeMarginService>node(
                  "account-window",
                  SceneResizeMarginService::new,
                  item -> {
                     item.minimumSize(304.0F, 216.0F)
                        .resizeMargin(6.0F)
                        .moveHandleHeight(this.enabled4 ? 64.0F : 72.0F)
                        .onResize(this::updateState);
                     if (item.isAnchored()) {
                        item.size(
                              LayoutOperationHandler.percent(100.0F),
                              LayoutOperationHandler.percent(100.0F)
                           )
                           .maxWidth(1040.0F)
                           .maxHeight(660.0F);
                     }

                     item.id("accounts.sheet")
                        .cornerRadius(26.0F)
                        .backgroundColor(ScenePctService.mulAlpha(MaterialIsLightService.SURFACE, 0.97F))
                        .blur(28.0F)
                        .border(1.0F, ScenePctService.mulAlpha(MaterialIsLightService.OUTLINE_VARIANT, 0.7F))
                        .direction(ScenePctService.Direction.COLUMN)
                        .clip(true);
                  },
                  currentArrayList.toArray(ComponentKeyService[]::new)
               )
               .key("account-window")
               .onMount(
                  item -> {
                     this.sceneResizeMarginService = item;
                     item.scale(0.97F).translateY(16.0F);
                     MotionAnimateService.animate(
                        item,
                        MotionColorsContainer.Floats.SCALE,
                        1.0F,
                        new SceneEaseHandler.Spring(24.0F, 190.0F)
                     );
                     MotionAnimateService.animate(
                        item,
                        MotionColorsContainer.Floats.TRANSLATE_Y,
                        0.0F,
                        new SceneEaseHandler.Spring(24.0F, 190.0F)
                     );
                  }
               )
         )
         .key("screen");
   }

   private ComponentKeyService<?> createComponentKeyService2() {
      int value = !this.session2.inWorld() && this.uUID == null ? 1 : 0;
      int currentValue = value != 0
         ? (this.enabled4 ? MaterialIsLightService.ON_SURFACE_VARIANT : MaterialIsLightService.ON_PRIMARY)
         : ScenePctService.mulAlpha(MaterialIsLightService.ON_SURFACE, 0.38F);
      return this.enabled3 && this.enabled5 && this.mode == BuiltinSessionTracker.Mode.LIST && this.renderer != null
         ? ComponentBoxService.row(
               item -> item.id("accounts.toolbar")
                  .height(LayoutOperationHandler.px(56.0F))
                  .padding(4.0F)
                  .align(ScenePctService.Align.CENTER)
                  .flexShrink(0.0F),
               this.createComponentKeyService4().props(item -> item.width(LayoutOperationHandler.auto()).flex(1.0F).minWidth(0.0F)),
               MaterialTextService.iconButton("accounts.network", "swap_horiz", "FRITZ!Box · Reconnect", this::updateState4),
               MaterialTextService.iconButton("accounts.add", "person_add", "Add account", this::updateState6)
                  .props(item -> item.available(value != 0))
                  .children(
                     MaterialTextService.icon(
                        "person_add",
                        value != 0
                           ? MaterialIsLightService.ON_SURFACE_VARIANT
                           : ScenePctService.mulAlpha(MaterialIsLightService.ON_SURFACE, 0.38F)
                     )
                  ),
               MaterialTextService.iconButton("accounts.close", "close", "Close · Esc", this::updateState3)
            )
            .key("toolbar")
         : ComponentBoxService.row(
               item -> item.id("accounts.toolbar")
                  .height(LayoutOperationHandler.px(this.enabled4 ? 64.0F : 72.0F))
                  .padding(
                     8.0F,
                     this.enabled4 ? 4.0F : 16.0F
                  )
                  .gap(8.0F)
                  .align(ScenePctService.Align.CENTER)
                  .flexShrink(0.0F),
               this.mode == BuiltinSessionTracker.Mode.LIST
                  ? ComponentBoxService.node(
                     "material-emblem",
                     MaterialLabelService::new,
                     item -> item.shape("clover4")
                        .active(true)
                        .tint(MaterialIsLightService.PRIMARY)
                        .size(36.0F, 36.0F)
                        .margin(0.0F, 8.0F)
                        .visible(!this.enabled4)
                  )
                  : MaterialTextService.iconButton("accounts.back", "arrow_back", "Back to accounts", this::updateState16),
               MaterialTextService.text(
                     this.mode == BuiltinSessionTracker.Mode.LIST ? "Accounts" : "Add account",
                     this.enabled4 ? 18.0F : 22.0F,
                     MaterialIsLightService.ON_SURFACE
                  )
                  .props(item -> item.flex(1.0F).minWidth(0.0F)),
               (this.enabled4
                     ? MaterialTextService.iconButton("accounts.add", "person_add", "Add account", this::updateState6)
                        .children(MaterialTextService.icon("person_add", currentValue))
                     : createComponentKeyService12("accounts.add", "Add account", this::updateState6, true, (value != 0))
                        .children(MaterialTextService.icon("person_add", currentValue), MaterialTextService.label("Add account", currentValue)))
                  .props(
                     item -> item.available(value != 0)
                        .visible(this.mode == BuiltinSessionTracker.Mode.LIST)
                        .tooltip(this.session2.inWorld() ? "Return to the main menu to add an account" : "Add account")
                  ),
               MaterialTextService.iconButton("accounts.network", "swap_horiz", "FRITZ!Box · Reconnect", this::updateState4)
                  .props(item -> item.visible(this.mode == BuiltinSessionTracker.Mode.LIST)),
               MaterialTextService.iconButton("accounts.close", "close", "Close · Esc", this::updateState3)
            )
            .key("toolbar");
   }

   private ComponentKeyService<?> createComponentKeyService3() {
      return this.enabled3 && this.enabled5
         ? ComponentBoxService.column(item -> item.visible(false)).key("tabs")
         : ComponentBoxService.column(
               item -> item.visible(this.enabled3 && this.mode == BuiltinSessionTracker.Mode.LIST && this.renderer != null)
                  .flexShrink(0.0F),
               this.createComponentKeyService4(),
               MaterialTextService.divider()
            )
            .key("tabs");
   }

   private ComponentKeyService<AnimatedSelectionIndicator> createComponentKeyService4() {
      return ComponentBoxService.<AnimatedSelectionIndicator>node(
            "material-tabs",
            AnimatedSelectionIndicator::new,
            item -> item.selection(this.enabled6 ? 1 : 0)
               .id("accounts.tabs")
               .height(LayoutOperationHandler.px(48.0F))
               .direction(ScenePctService.Direction.ROW)
               .padding(0.0F, this.enabled4 ? 0.0F : 16.0F)
               .scrollable(true)
               .scrollbarWidth(0.0F)
               .clip(true),
            this.createComponentKeyService5("Accounts", false),
            this.createComponentKeyService5("Servers", true)
         )
         .key("navigation");
   }

   private ComponentKeyService<?> createComponentKeyService5(String text, boolean enabled) {
      int value = this.enabled6 == enabled ? MaterialIsLightService.PRIMARY : MaterialIsLightService.ON_SURFACE_VARIANT;
      return ComponentBoxService.<MaterialJoinedService>node(
            "material-tab",
            MaterialJoinedService::new,
            item -> {
               item.colors(0, value).shape(0.0F, 48.0F);
               item.id("accounts.tab." + text.toLowerCase(Locale.ROOT))
                  .size(112.0F, 48.0F)
                  .flexShrink(0.0F)
                  .direction(ScenePctService.Direction.ROW)
                  .justify(ScenePctService.Justify.CENTER)
                  .align(ScenePctService.Align.CENTER)
                  .onClick(() -> this.updateState17(enabled));
            },
            MaterialTextService.label(text, value)
         )
         .key(text);
   }

   private ComponentKeyService<?> createComponentKeyService6() {
      ArrayList arrayList = new ArrayList();
      List<AccountTypeData> items = this.accountAllService
         .all()
         .stream()
         .sorted(
            Comparator.<AccountTypeData, Boolean>comparing(item -> !item.uuid().equals(this.session2.uuid()))
               .thenComparing(Comparator.comparing(AccountTypeData::lastUsedAt).reversed())
         )
         .toList();
      if (this.session2.uuid() != null
         && items.stream().noneMatch(item -> item.uuid().equals(this.session2.uuid()))
         && this.checkCondition(this.session2.uuid(), this.session2.name(), "current account")) {
         arrayList.add(this.createComponentKeyService7(this.session2.uuid(), this.session2.name(), null));
      }

      for (AccountTypeData accountTypeData : (Iterable<AccountTypeData>) (Iterable<?>) (items)) {
         if (this.checkCondition(
            accountTypeData.uuid(), accountTypeData.username(), accountTypeData.isOffline() ? "offline" : (accountTypeData.isSession() ? "cookie session" : "microsoft")
         )) {
            arrayList.add(this.createComponentKeyService7(accountTypeData.uuid(), accountTypeData.username(), accountTypeData));
         }
      }

      if (arrayList.isEmpty()) {
         arrayList.add(
            ComponentBoxService.column(
               item -> item.id("accounts.empty")
                  .padding(24.0F, 12.0F)
                  .gap(8.0F),
               MaterialTextService.text(
                  this.text4.isBlank() ? "No saved accounts" : "No accounts found",
                  18.0F,
                  MaterialIsLightService.ON_SURFACE
               ),
               createComponentKeyService11(
                  this.text4.isBlank()
                     ? "Add a Microsoft or offline account to get started."
                     : "Try a different name, UUID or account type.",
                  MaterialIsLightService.ON_SURFACE_VARIANT
               )
            )
         );
      }

      ArrayList<ComponentKeyService<?>> currentArrayList = new ArrayList<>();
      currentArrayList.add(
         ComponentBoxService.column(
            item -> item.gap(4.0F).visible(!this.enabled5),
            MaterialTextService.text("Your accounts", 24.0F, MaterialIsLightService.ON_SURFACE),
            createComponentKeyService11(
               this.session2.inWorld() ? "Return to the main menu to switch accounts." : "Choose a profile to play with.",
               MaterialIsLightService.ON_SURFACE_VARIANT
            )
         )
      );
      currentArrayList.add(MaterialTextService.search("accounts.search", "Search accounts", this.text4, item -> {
         this.text4 = item;
         this.updateState2();
      }));
      currentArrayList.add(
         ComponentBoxService.<MaterialComponent>node(
               "material-reflow-list",
               MaterialComponent::new,
               item -> item.id("accounts.list").gap(4.0F),
               arrayList.toArray(ComponentKeyService[]::new)
            )
            .key("list")
      );
      return ComponentBoxService.panel(
            item -> item.id("accounts.pane")
               .backgroundColor(MaterialIsLightService.SURFACE_LOW)
               .cornerRadius(28.0F)
               .direction(ScenePctService.Direction.COLUMN)
               .width(
                  this.enabled3
                     ? LayoutOperationHandler.percent(100.0F)
                     : LayoutOperationHandler.px(336.0F)
               )
               .height(LayoutOperationHandler.percent(100.0F))
               .flexShrink(0.0F)
               .padding(this.enabled5 ? 8.0F : 16.0F)
               .clip(true),
            ComponentBoxService.column(
               item -> item.id("accounts.scroll")
                  .flex(1.0F)
                  .minHeight(0.0F)
                  .gap(this.enabled5 ? 8.0F : 20.0F)
                  .scrollable(true)
                  .clip(true)
                  .scrollbarWidth(3.0F)
                  .scrollbarColor(MaterialIsLightService.OUTLINE_VARIANT)
                  .scrollInset(1.0F)
                  .padding(0.0F, 4.0F, 8.0F, 0.0F),
               currentArrayList.stream().map(componentKey -> componentKey.props(component -> component.flexShrink(0.0F))).toArray(ComponentKeyService[]::new)
            )
         )
         .key("accounts");
   }

   private boolean checkCondition(UUID uUID, String text, String currentText) {
      String nextText = this.text4.strip().toLowerCase(Locale.ROOT);
      return nextText.isEmpty() || (text + " " + PrivacyRevisionService.replace(text) + " " + uUID + " " + currentText).toLowerCase(Locale.ROOT).contains(nextText);
   }

   private ComponentKeyService<?> createComponentKeyService7(UUID currentUUID, String currentText, AccountTypeData accountTypeData) {
      boolean enabled = Objects.equals(currentUUID, this.session2.uuid());
      boolean currentEnabled = Objects.equals(currentUUID, this.uUID);
      String nextText = accountTypeData == null
         ? "Current account"
         : (
            accountTypeData.isOffline()
               ? "Offline account"
               : (accountTypeData.isSession() ? (accountTypeData.hasCurrentSession() ? "Cookie session" : "Cookie session · import again") : "Microsoft account")
         );
      String previousText = currentEnabled ? "Signing in…" : (enabled ? "Active · " + nextText : nextText);
      int value = enabled ? MaterialIsLightService.SECONDARY_CONTAINER : MaterialIsLightService.SURFACE_CONTAINER;
      int currentValue = enabled ? MaterialIsLightService.ON_SECONDARY_CONTAINER : MaterialIsLightService.ON_SURFACE;
      ArrayList arrayList = new ArrayList();
      arrayList.add(
         ComponentBoxService.node(
            "account-select",
            MaterialJoinedService::new,
            item -> {
               item.colors(0, currentValue)
                  .shape(16.0F, 72.0F)
                  .disabledSurface(0)
                  .available(accountTypeData != null && !this.session2.inWorld() && this.uUID == null);
               item.id("accounts.select." + currentUUID)
                  .height(LayoutOperationHandler.px(72.0F))
                  .flex(1.0F)
                  .minWidth(0.0F)
                  .padding(12.0F)
                  .gap(12.0F)
                  .direction(ScenePctService.Direction.ROW)
                  .align(ScenePctService.Align.CENTER)
                  .tooltip(
                     accountTypeData == null
                        ? "Your current Minecraft session"
                        : (currentEnabled ? "Signing in…" : "Use " + currentText + " · last used " + createText(accountTypeData.lastUsedAt()))
                  )
                  .onClick(() -> this.updateState23(accountTypeData));
            },
            this.createComponentKeyService8(currentUUID, currentText, accountTypeData == null || !accountTypeData.isOffline()),
            ComponentBoxService.column(
               item -> item.flex(1.0F).minWidth(0.0F),
               MaterialTextService.text(currentText, 16.0F, currentValue),
               MaterialTextService.text(
                     previousText, 12.0F, enabled ? MaterialIsLightService.ON_SECONDARY_CONTAINER : MaterialIsLightService.ON_SURFACE_VARIANT
                  )
                  .props(item -> item.id("accounts.status." + currentUUID))
            )
         )
      );
      if (accountTypeData != null) {
         arrayList.add(MaterialTextService.iconButton("accounts.remove." + currentUUID, "delete", "Remove " + currentText + " from saved accounts", () -> {
            if (currentUUID.equals(this.uUID)) {
               this.updateState28();
            }

            this.accountAllService.remove(currentUUID);
            this.updateState19();
         }).props(item -> item.stopPropagation(true).marginRight(4.0F)));
      }

      return ComponentBoxService.<MaterialResponsiveService>node(
            "account-surface",
            MaterialResponsiveService::new,
            item -> item.surface(value)
               .id("accounts.row." + currentUUID)
               .height(LayoutOperationHandler.px(72.0F))
               .flexShrink(0.0F)
               .cornerRadius(20.0F)
               .direction(ScenePctService.Direction.ROW)
               .align(ScenePctService.Align.CENTER),
            arrayList.toArray(ComponentKeyService[]::new)
         )
         .key("account:" + currentUUID);
   }

   private ComponentKeyService<?> createComponentKeyService8(UUID uUID, String currentText, boolean enabled) {
      String nextText = "mc.head." + uUID;
      boolean value = this.compositorPushPresentationScaleService != null
            && this.compositorPushPresentationScaleService.getNamedTexture(nextText) != null
            && this.compositorPushPresentationScaleService.getNamedTexture(nextText).valid()
         ;
      if (enabled && !value && CoreIsInitializedHandler.isReady()) {
         this.updateState30(uUID);
      }

      return ComponentBoxService.panel(
         item -> item.size(40.0F, 40.0F)
            .cornerRadius(20.0F)
            .backgroundColor(MaterialIsLightService.PRIMARY_CONTAINER)
            .direction(ScenePctService.Direction.NONE)
            .flexShrink(0.0F)
            .pointerEvents(false),
         ComponentBoxService.row(
            item -> item.size(40.0F, 40.0F)
               .align(ScenePctService.Align.CENTER)
               .justify(ScenePctService.Justify.CENTER)
               .visible(!value),
            MaterialTextService.text(currentText, 20.0F, MaterialIsLightService.ON_PRIMARY_CONTAINER).props(item -> item.initial(true))
         ),
         ComponentBoxService.node(
            "account-avatar",
            SceneTextureService::new,
            item -> item.textureName(nextText)
               .size(40.0F, 40.0F)
               .cornerRadius(20.0F)
               .visible(value)
         )
      );
   }

   private ComponentKeyService<?> createComponentKeyService9() {
      ArrayList arrayList = new ArrayList();
      switch (this.mode) {
         case CHOOSE:
            arrayList.add(
               MaterialTextService.text("Make yourself at home", 28.0F, MaterialIsLightService.ON_SURFACE)
                  .props(item -> item.lineHeight(36.0F).wordWrap(true).visible(!this.enabled5))
            );
            arrayList.add(
               createComponentKeyService11("Add a profile, then choose it whenever you want to play.", MaterialIsLightService.ON_SURFACE_VARIANT)
                  .props(item -> item.visible(!this.enabled5))
            );
            arrayList.add(
               this.createComponentKeyService10(
                  "accounts.choose.microsoft", "login", "Microsoft account", "Sign in securely in your browser", this::updateState26
               )
            );
            arrayList.add(
               this.createComponentKeyService10("accounts.choose.cookie", "cookie", "Cookie login", "Use an existing Microsoft session", this::updateState8)
            );
            arrayList.add(
               this.createComponentKeyService10(
                  "accounts.choose.offline", "person", "Offline account", "For servers that allow offline players", this::updateState7
               )
            );
            arrayList.add(
               MaterialTextService.button(
                  "accounts.cancel", "Cancel", this::updateState16, 0, MaterialIsLightService.PRIMARY, 20.0F
               )
            );
            break;
         case MICROSOFT:
            arrayList.add(
               MaterialTextService.text("Sign in with Microsoft", 28.0F, MaterialIsLightService.ON_SURFACE)
                  .props(item -> item.lineHeight(36.0F).wordWrap(true))
            );
            arrayList.add(createComponentKeyService11("Open microsoft.com/link and enter this code.", MaterialIsLightService.ON_SURFACE_VARIANT));
            arrayList.add(
               ComponentBoxService.node(
                  "device-code",
                  MaterialJoinedService::new,
                  item -> {
                     item.colors(MaterialIsLightService.SECONDARY_CONTAINER, MaterialIsLightService.ON_SECONDARY_CONTAINER)
                        .shape(20.0F, 80.0F)
                        .available(!this.enabled7 && this.text6 != null);
                     item.id("accounts.microsoft.code")
                        .height(LayoutOperationHandler.px(80.0F))
                        .padding(16.0F)
                        .gap(12.0F)
                        .direction(ScenePctService.Direction.ROW)
                        .align(ScenePctService.Align.CENTER)
                        .onClick(this::updateState22)
                        .tooltip("Copy sign-in code");
                  },
                  MaterialTextService.text(this.text7, 28.0F, MaterialIsLightService.ON_SECONDARY_CONTAINER)
                     .props(item -> item.letterSpacing(2.0F).flex(1.0F).minWidth(0.0F)),
                  MaterialTextService.icon("content_copy", MaterialIsLightService.ON_SECONDARY_CONTAINER)
               )
            );
            arrayList.add(
               createComponentKeyService11(this.text8, this.enabled7 ? MaterialIsLightService.ERROR : MaterialIsLightService.ON_SURFACE_VARIANT)
                  .props(item -> item.id("accounts.microsoft.status"))
            );
            arrayList.add(
               ComponentBoxService.row(
                  item -> item.gap(8.0F),
                  createComponentKeyService12(
                     "accounts.microsoft.open",
                     this.enabled7 ? "Try again" : "Open sign-in page",
                     this.enabled7 ? this::updateState26 : this::updateState29,
                     true,
                     this.enabled7 || this.text6 != null
                  ),
                  MaterialTextService.button(
                     "accounts.cancel", "Cancel", this::updateState16, 0, MaterialIsLightService.PRIMARY, 20.0F
                  )
               )
            );
            break;
         case COOKIE:
            arrayList.add(
               MaterialTextService.text("Sign in with a cookie", 28.0F, MaterialIsLightService.ON_SURFACE)
                  .props(item -> item.lineHeight(36.0F).wordWrap(true))
            );
            arrayList.add(createComponentKeyService11("Paste a Microsoft session cookie or import your cookie export.", MaterialIsLightService.ON_SURFACE_VARIANT));
            arrayList.add(
               ComponentBoxService.<ControlLetterSpacingService>node(
                     "cookie-input",
                     ControlLetterSpacingService::new,
                     item -> {
                        MaterialTextService.input(item);
                        item.materialError(this.enabled).password(true);
                        this.controlLetterSpacingService2 = item;
                        item.id("accounts.cookie.value")
                           .height(LayoutOperationHandler.px(56.0F))
                           .placeholder("Session cookie or name=value")
                           .maxLength(32768)
                           .onChanged(this::updateState9)
                            .onSubmit(text -> this.updateState11())
                           .visible(!this.enabled2);
                        if (!item.focused()) {
                           item.text(this.text2);
                        }
                     }
                  )
                  .key("cookie-input")
                  .onMount(item -> this.updateState32(0.0F, () -> {
                     if (this.mode == BuiltinSessionTracker.Mode.COOKIE && !this.enabled2) {
                        updateState34(item);
                     }
                  }))
                  .onUnmount(item -> {
                     item.clearSensitiveText();
                     if (this.controlLetterSpacingService2 == item) {
                        this.controlLetterSpacingService2 = null;
                     }
                  })
            );
            arrayList.add(
               createComponentKeyService11(this.text3, this.enabled ? MaterialIsLightService.ERROR : MaterialIsLightService.ON_SURFACE_VARIANT)
                  .props(item -> item.id("accounts.cookie.status"))
            );
            arrayList.add(
               createComponentKeyService12(
                     "accounts.cookie.file",
                     "Choose cookie file",
                     this::updateState10,
                     false,
                     !this.enabled2 && !this.session2.inWorld()
                  )
                  .children(
                     MaterialTextService.icon("folder", MaterialIsLightService.ON_SECONDARY_CONTAINER),
                     MaterialTextService.label("Choose cookie file", MaterialIsLightService.ON_SECONDARY_CONTAINER)
                  )
            );
            arrayList.add(
               createComponentKeyService11(
                  "A value without a name is treated as __Host-MSAAUTH. Minecraft.net opens in a separate sign-in window. The session lasts until it expires or you restart the client; import your cookies again to renew it.",
                  MaterialIsLightService.ON_SURFACE_VARIANT
               )
            );
            arrayList.add(
               ComponentBoxService.row(
                  item -> item.gap(8.0F),
                  createComponentKeyService12(
                     "accounts.cookie.login",
                     "Sign in",
                     this::updateState11,
                     true,
                     !this.enabled2 && this.accountCodec != null && !this.session2.inWorld()
                  ),
                  MaterialTextService.button(
                     "accounts.cancel", "Cancel", this::updateState16, 0, MaterialIsLightService.PRIMARY, 20.0F
                  )
               )
            );
            arrayList.add(
               MaterialTextService.button(
                  "accounts.cookie.browser",
                  "Use Microsoft sign-in",
                  this::updateState26,
                  0,
                  MaterialIsLightService.PRIMARY,
                  20.0F
               )
            );
            break;
         case OFFLINE:
            arrayList.add(
               MaterialTextService.text("Add offline account", 28.0F, MaterialIsLightService.ON_SURFACE)
                  .props(item -> item.lineHeight(36.0F).wordWrap(true))
            );
            arrayList.add(createComponentKeyService11("Offline accounts only work on servers that allow offline players.", MaterialIsLightService.ON_SURFACE_VARIANT));
            arrayList.add(
               ComponentBoxService.<ControlLetterSpacingService>node(
                     "offline-name",
                     ControlLetterSpacingService::new,
                     item -> {
                        MaterialTextService.input(item);
                        item.materialError(this.enabled8);
                        this.controlLetterSpacingService = item;
                        item.id("accounts.offline.name")
                           .height(LayoutOperationHandler.px(56.0F))
                           .placeholder("Minecraft username")
                           .maxLength(16)
                           .onChanged(this::updateState18)
                            .onSubmit(text -> this.updateState27());
                        if (!item.focused()) {
                           item.text(this.text5);
                        }
                     }
                  )
                  .key("offline-name")
                  .onMount(item -> this.updateState32(0.0F, () -> {
                     if (this.mode == BuiltinSessionTracker.Mode.OFFLINE) {
                        updateState34(item);
                     }
                  }))
                  .onUnmount(item -> {
                     if (this.controlLetterSpacingService == item) {
                        this.controlLetterSpacingService = null;
                     }
                  })
            );
            arrayList.add(
               createComponentKeyService11(this.text9, this.enabled8 ? MaterialIsLightService.ERROR : MaterialIsLightService.ON_SURFACE_VARIANT)
                  .props(item -> item.id("accounts.offline.status"))
            );
            arrayList.add(
               ComponentBoxService.row(
                  item -> item.gap(8.0F),
                  createComponentKeyService12(
                     "accounts.offline.add",
                     "Add offline",
                     this::updateState27,
                     true,
                     AccountTypeData.isValidOfflineUsername(this.text5.trim()) && !this.session2.inWorld()
                  ),
                  MaterialTextService.button(
                     "accounts.cancel", "Cancel", this::updateState16, 0, MaterialIsLightService.PRIMARY, 20.0F
                  )
               )
            );
      }

      return ComponentBoxService.column(
            item -> item.id("accounts.form.scroll")
               .flex(1.0F)
               .minHeight(0.0F)
               .padding(
                  this.enabled5
                     ? 8.0F
                     : (this.enabled4 ? 16.0F : 32.0F)
               )
               .scrollable(true)
               .clip(true)
               .scrollbarWidth(3.0F)
               .scrollbarColor(MaterialIsLightService.OUTLINE_VARIANT)
               .align(ScenePctService.Align.CENTER),
            ComponentBoxService.panel(
               item -> item.id("accounts.form.card")
                  .width(LayoutOperationHandler.percent(100.0F))
                  .maxWidth(480.0F)
                  .cornerRadius(28.0F)
                  .backgroundColor(MaterialIsLightService.SURFACE_LOW)
                  .direction(ScenePctService.Direction.COLUMN)
                  .padding(this.enabled4 ? 20.0F : 24.0F)
                  .gap(20.0F)
                  .flexShrink(0.0F),
               arrayList.toArray(ComponentKeyService[]::new)
            )
         )
         .onMount(
            item -> MaterialEnterService.enter(
               item,
               this.mode == BuiltinSessionTracker.Mode.CHOOSE ? 0.0F : 20.0F,
               this.mode == BuiltinSessionTracker.Mode.CHOOSE ? 18.0F : 0.0F
            )
         );
   }

   private ComponentKeyService<?> createComponentKeyService10(String currentText, String nextText, String previousText, String sourceText, Runnable runnable) {
      return ComponentBoxService.<MaterialJoinedService>node(
            "account-choice",
            MaterialJoinedService::new,
            item -> {
               item.colors(MaterialIsLightService.SECONDARY_CONTAINER, MaterialIsLightService.ON_SECONDARY_CONTAINER)
                  .shape(20.0F, 0.0F)
                  .available(!this.session2.inWorld());
               item.id(currentText)
                  .minHeight(88.0F)
                  .padding(16.0F)
                  .gap(16.0F)
                  .direction(ScenePctService.Direction.ROW)
                  .align(ScenePctService.Align.CENTER)
                  .onClick(runnable);
            },
            MaterialTextService.icon(nextText, MaterialIsLightService.ON_SECONDARY_CONTAINER),
            ComponentBoxService.column(
               item -> item.flex(1.0F).minWidth(0.0F).gap(4.0F),
               MaterialTextService.text(previousText, 16.0F, MaterialIsLightService.ON_SECONDARY_CONTAINER)
                  .props(item -> item.wordWrap(true)),
               createComponentKeyService11(sourceText, MaterialIsLightService.ON_SECONDARY_CONTAINER)
            ),
            MaterialTextService.icon("chevron_right", MaterialIsLightService.ON_SECONDARY_CONTAINER)
         )
         .key(currentText);
   }

   private static ComponentKeyService<SceneTextService> createComponentKeyService11(String currentText, int value) {
      return MaterialTextService.text(currentText, 14.0F, value).props(item -> item.wordWrap(true));
   }

   private static ComponentKeyService<MaterialJoinedService> createComponentKeyService12(String text, String currentText, Runnable runnable, boolean enabled, boolean currentEnabled) {
      int value = currentEnabled
         ? (enabled ? MaterialIsLightService.PRIMARY : MaterialIsLightService.SECONDARY_CONTAINER)
         : ScenePctService.mulAlpha(MaterialIsLightService.ON_SURFACE, 0.12F);
      int currentValue = currentEnabled
         ? (enabled ? MaterialIsLightService.ON_PRIMARY : MaterialIsLightService.ON_SECONDARY_CONTAINER)
         : ScenePctService.mulAlpha(MaterialIsLightService.ON_SURFACE, 0.38F);
      return MaterialTextService.button(text, currentText, runnable, value, currentValue, 20.0F).props(item -> item.available(currentEnabled));
   }

   private void updateState2() {
      if (this.componentMountService != null) {
         this.componentMountService.invalidateComponent();
      }
   }

   private UUID createUUID() {
      return this.supplier.get().uuid();
   }

   private void updateState3() {
      if (this.screenScreenIdService != null) {
         this.screenScreenIdService.close();
      }
   }

   private void updateState4() {
      if (this.screenScreenIdService != null) {
         this.screenScreenIdService.open("fritz-box");
      }
   }

   private void updateState5() {
      this.text4 = "";
   }

   private void updateState6() {
      if (!this.supplier.get().inWorld()) {
         this.updateState28();
         this.updateState15(BuiltinSessionTracker.Mode.CHOOSE);
      }
   }

   private void updateState7() {
      if (!this.supplier.get().inWorld()) {
         this.updateState28();
         this.text5 = "";
         this.text9 = "1–16 characters: letters, numbers and underscores.";
         this.enabled8 = false;
         this.updateState15(BuiltinSessionTracker.Mode.OFFLINE);
      }
   }

   private void updateState8() {
      if (!this.supplier.get().inWorld()) {
         this.updateState28();
         this.text3 = "Paste a session cookie or choose a .txt / .json export.";
         this.enabled = false;
         this.updateState15(BuiltinSessionTracker.Mode.COOKIE);
      }
   }

   private void updateState9(String text) {
      this.text2 = text == null ? "" : text;
      this.accountCodec = null;
      this.enabled = false;
      this.text3 = "Paste a session cookie or choose a .txt / .json export.";
      if (!this.text2.isBlank()) {
         try {
            this.accountCodec = AccountCodec.parse(this.text2);
            this.text3 = "Ready to sign in with your Microsoft session.";
         } catch (AccountCodeService accountCode) {
            this.enabled = true;
            this.text3 = accountCode.getMessage();
         }
      }

      this.updateState2();
   }

   private void updateState10() {
      if (!this.enabled2 && !this.supplier.get().inWorld()) {
         long offset = this.atomicLong.incrementAndGet();
         this.enabled2 = true;
         this.enabled = false;
         this.text3 = "Choose your cookie export…";
         this.updateState33();
         this.updateState2();
         CompatNewThreadService.choose()
            .whenComplete(
               (item, currentItem) -> updateState31(
                  () -> {
                     if (this.atomicLong.get() == offset && this.mode == BuiltinSessionTracker.Mode.COOKIE) {
                        this.enabled2 = false;
                        if (currentItem != null) {
                           this.updateState13(currentItem);
                        } else if (item.isPresent()) {
                           this.updateState14();
                           this.accountCodec = (AccountCodec)item.get();
                           this.text3 = "Cookie file ready · " + this.accountCodec.size() + " Microsoft cookies.";
                        } else {
                           this.text3 = this.accountCodec == null
                              ? "Paste a session cookie or choose a .txt / .json export."
                              : "Ready to sign in with your Microsoft session.";
                        }

                        this.updateState2();
                     }
                  }
               )
            );
      }
   }

   private void updateState11() {
      if (this.mode == BuiltinSessionTracker.Mode.COOKIE
         && !this.enabled2
         && this.accountCodec != null
         && !this.supplier.get().inWorld()) {
         long offset = this.atomicLong.incrementAndGet();
         AccountCodec currentAccountCodec = this.accountCodec;
         this.updateState14();
         this.updateState33();
         this.enabled2 = true;
         this.enabled = false;
         this.text3 = "Signing in with Microsoft…";
         this.updateState2();
         this.accountNewThreadService.loginWithCookies(currentAccountCodec, () -> this.atomicLong.get() != offset, item -> updateState31(() -> {
            if (this.atomicLong.get() == offset && this.mode == BuiltinSessionTracker.Mode.COOKIE && this.enabled2) {
               this.text3 = item;
               this.updateState2();
            }
         })).whenComplete((item, currentItem) -> updateState31(() -> this.updateState12(offset, item, currentItem)));
      }
   }

   private void updateState12(long offset, AccountNewThreadService.LoginResult loginResult, Throwable exception) {
      if (this.atomicLong.get() == offset && this.mode == BuiltinSessionTracker.Mode.COOKIE) {
         this.enabled2 = false;
         if (exception != null) {
            this.updateState13(exception);
            this.updateState2();
         } else if (this.supplier.get().inWorld()) {
            this.enabled = true;
            this.text3 = "Return to the main menu and try signing in again.";
            this.updateState2();
         } else {
            AccountApplyOfflineService.apply(loginResult);
            AccountTypeData accountTypeData = this.accountAllService.upsert(loginResult.account());
            this.accountAllService.setActive(accountTypeData.uuid());
            this.updateState5();
            this.updateState30(accountTypeData.uuid());
            this.updateState25("Signed in", accountTypeData.username(), SceneRenderer.Type.SUCCESS, 3.5F);
            this.updateState16();
         }
      }
   }

   private void updateState13(Throwable exception) {
      this.updateState14();
      Throwable currentException = createThrowable(exception);
      this.enabled = true;
      this.text3 = currentException instanceof AccountCodeService accountCode
         ? accountCode.getMessage()
         : "Couldn't sign in. Try another cookie or use Microsoft sign-in.";
   }

   private void updateState14() {
      this.text2 = "";
      this.accountCodec = null;
      if (this.controlLetterSpacingService2 != null) {
         this.controlLetterSpacingService2.clearSensitiveText();
      }
   }

   private void updateState15(BuiltinSessionTracker.Mode currentMode) {
      this.updateState33();
      this.mode = currentMode;
      this.updateState2();
   }

   private void updateState16() {
      this.updateState28();
      this.updateState33();
      this.mode = BuiltinSessionTracker.Mode.LIST;
      this.updateState19();
      MaterialEnterService.enter(
         this.sceneCornerRadiusService == null ? null : this.sceneCornerRadiusService.findById("accounts.workspace"), -16.0F, 0.0F
      );
   }

   private void updateState17(boolean enabled) {
      if (this.enabled6 != enabled) {
         this.updateState33();
         this.enabled6 = enabled;
         this.updateState2();
         MaterialEnterService.enter(
            this.sceneCornerRadiusService.findById(enabled ? "servers.pane" : "accounts.pane"),
            enabled ? 16.0F : -16.0F,
            0.0F
         );
      }
   }

   @Override
   public void onOpen(ScreenScreenIdService screenScreenId) {
      MaterialEnterService.enter(
         this.sceneCornerRadiusService == null ? null : this.sceneCornerRadiusService.findById("accounts.sheet"), 0.0F, 18.0F
      );
   }

   private void updateState18(String text) {
      this.text5 = text == null ? "" : text;
      String currentText = this.text5.trim();
      boolean validOfflineUsername = AccountTypeData.isValidOfflineUsername(currentText);
      this.text9 = currentText.isEmpty()
         ? "1–16 characters: letters, numbers and underscores."
         : (validOfflineUsername ? "Ready to add " + currentText + "." : "Use 1–16 letters, numbers or underscores only.");
      this.enabled8 = !currentText.isEmpty() && !validOfflineUsername;
      this.updateState2();
   }

   private void updateState19() {
      this.session2 = this.supplier.get();
      this.updateState2();
   }

   private void updateState20() {
      this.updateState32(0.0F, () -> {
         if (this.mode == BuiltinSessionTracker.Mode.OFFLINE && this.controlLetterSpacingService != null) {
            updateState34(this.controlLetterSpacingService);
         }
      });
   }

   private void updateState21(Throwable exception) {
      this.text8 = createThrowable(exception) instanceof AccountCodeService accountCode
         ? accountCode.getMessage()
         : "Couldn't reach Microsoft. Check your connection and try again.";
      this.enabled7 = true;
      this.text6 = null;
      this.text7 = "--------";
      this.updateState2();
   }

   private void updateState22() {
      if (!this.enabled7 && this.text6 != null && !this.text7.isBlank()) {
         Minecraft.getInstance().keyboardHandler.setClipboard(this.text7);
         this.updateState25("Code copied", this.text7, SceneRenderer.Type.SUCCESS, 2.0F);
      }
   }

   private void updateState23(AccountTypeData accountTypeData) {
      if (accountTypeData != null && this.uUID == null) {
         UUID currentUUID = this.createUUID();
         if (currentUUID != null && currentUUID.equals(accountTypeData.uuid())) {
            this.accountAllService.setActive(accountTypeData.uuid());
            this.updateState19();
         } else if (this.supplier.get().inWorld()) {
            this.updateState25(
               "Account is in use",
               "Return to the main menu before switching accounts.",
               SceneRenderer.Type.INFO,
               3.0F
            );
         } else if (accountTypeData.isOffline()) {
            AccountApplyOfflineService.applyOffline(accountTypeData);
            this.accountAllService.setActive(accountTypeData.uuid());
            this.updateState25(
               "Switched account", accountTypeData.username() + " (offline)", SceneRenderer.Type.SUCCESS, 3.0F
            );
            this.updateState19();
         } else {
            this.uUID = accountTypeData.uuid();
            this.updateState19();
            long offset = this.atomicLong.incrementAndGet();
            this.updateState25("Signing in", accountTypeData.username() + "…", SceneRenderer.Type.INFO, 2.5F);
            this.accountNewThreadService.login(accountTypeData).whenComplete((item, currentItem) -> updateState31(() -> {
               if (this.atomicLong.get() == offset && this.uUID != null && this.uUID.equals(accountTypeData.uuid())) {
                  this.uUID = null;
                  if (currentItem != null) {
                     this.updateState24(currentItem, accountTypeData.username());
                     this.updateState19();
                  } else {
                     AccountApplyOfflineService.apply(item);
                      AccountTypeData signedInAccount = this.accountAllService.upsert(item.account());
                      this.accountAllService.setActive(signedInAccount.uuid());
                      this.updateState30(signedInAccount.uuid());
                      this.updateState25("Signed in", signedInAccount.username(), SceneRenderer.Type.SUCCESS, 3.0F);
                     this.updateState19();
                  }
               }
            }));
         }
      }
   }

   private void updateState24(Throwable exception, String text) {
      String currentText = createThrowable(exception) instanceof AccountCodeService accountCode
         ? accountCode.getMessage()
         : "Couldn't sign in. Check your connection and try again.";
      this.updateState25(
         text != null ? "Sign-in failed: " + text : "Sign-in failed", currentText, SceneRenderer.Type.ERROR, 5.0F
      );
   }

   private void updateState25(String text, String currentText, SceneRenderer.Type type, float value) {
      try {
         CoreIsInitializedHandler.get().scene().toasts().show(text, currentText, value, type);
      } catch (Throwable exception) {
      }
   }

   private void updateState26() {
      if (!this.supplier.get().inWorld()) {
         this.updateState28();
         long offset = this.atomicLong.get();
         this.text6 = null;
         this.text7 = "--------";
         this.text8 = "Asking Microsoft for a sign-in code…";
         this.enabled7 = false;
         this.updateState15(BuiltinSessionTracker.Mode.MICROSOFT);
         this.accountNewThreadService
            .requestDeviceCode()
            .whenComplete(
                                  (loginResult, loginError) -> updateState31(
                  () -> {
                     if (this.atomicLong.get() == offset) {
                                           if (loginError != null) {
                                              this.updateState21(loginError);
                        } else {
            this.text6 = loginResult.verificationUri();
            this.text7 = loginResult.userCode();
                           this.text8 = "Waiting for you to finish signing in…";
                           this.updateState2();
                           this.accountNewThreadService
               .completeLogin(loginResult, () -> this.atomicLong.get() != offset)
                              .whenComplete(
                                  (completedLogin, loginFailure) -> updateState31(
                                    () -> {
                                       if (this.atomicLong.get() == offset) {
                                           if (loginFailure != null) {
                                              this.updateState21(loginFailure);
                                          } else if (this.supplier.get().inWorld()) {
                                             this.enabled7 = true;
                                             this.text8 = "Return to the main menu and try signing in again.";
                                             this.updateState2();
                                          } else {
                                              AccountApplyOfflineService.apply(completedLogin);
                                              AccountTypeData accountTypeData = this.accountAllService.upsert(completedLogin.account());
                                             this.accountAllService.setActive(accountTypeData.uuid());
                                             this.updateState5();
                                             this.text7 = "Linked";
                                             this.text8 = "Signed in as " + accountTypeData.username() + ".";
                                             this.text6 = null;
                                             this.updateState2();
                                             this.updateState25(
                                                "Account linked",
                                                accountTypeData.username(),
                                                SceneRenderer.Type.SUCCESS,
                                                3.5F
                                             );
                                             this.updateState30(accountTypeData.uuid());
                                             this.updateState32(0.35F, () -> {
                                                if (this.atomicLong.get() == offset) {
                                                   this.updateState16();
                                                }
                                             });
                                          }
                                       }
                                    }
                                 )
                              );
                        }
                     }
                  }
               )
            );
      }
   }

   private void updateState27() {
      if (this.controlLetterSpacingService != null && !this.supplier.get().inWorld()) {
         String currentText = this.controlLetterSpacingService.text() == null ? "" : this.controlLetterSpacingService.text().trim();
         if (!AccountTypeData.isValidOfflineUsername(currentText)) {
            this.updateState18(currentText);
            this.updateState20();
         } else {
            AccountTypeData accountTypeData = AccountTypeData.offline(currentText);
            AccountTypeData currentAccountTypeData = this.accountAllService
               .all()
               .stream()
               .filter(AccountTypeData::isOffline)
               .filter(item -> item.username().equalsIgnoreCase(currentText))
               .findFirst()
               .orElse(null);
            AccountTypeData nextAccountTypeData = this.accountAllService.get(accountTypeData.uuid()).orElse(null);
            if (currentAccountTypeData == null && nextAccountTypeData != null && !nextAccountTypeData.isOffline()) {
               this.text9 = "That UUID is already used by a Microsoft account.";
               this.enabled8 = true;
               this.updateState2();
               this.updateState20();
            } else {
               AccountTypeData previousAccountTypeData = currentAccountTypeData != null ? currentAccountTypeData : nextAccountTypeData;
               if (previousAccountTypeData != null) {
                  accountTypeData = new AccountTypeData(previousAccountTypeData.uuid(), previousAccountTypeData.username(), AccountTypeData.Type.OFFLINE, "", previousAccountTypeData.addedAt(), Instant.now());
               }

               AccountTypeData sourceAccountTypeData = this.accountAllService.upsert(accountTypeData);
               AccountApplyOfflineService.applyOffline(sourceAccountTypeData);
               this.accountAllService.setActive(sourceAccountTypeData.uuid());
               this.updateState5();
               this.updateState33();
               this.updateState25("Offline account added", sourceAccountTypeData.username(), SceneRenderer.Type.SUCCESS, 3.0F);
               this.updateState16();
            }
         }
      }
   }

   private void updateState28() {
      this.atomicLong.incrementAndGet();
      this.uUID = null;
      this.updateState14();
      this.enabled2 = false;
   }

   private void updateState29() {
      String text = this.text6 != null ? this.text6 : "https://microsoft.com/link";
      CompatOpenUriService.openUri(text);
   }

   private static Throwable createThrowable(Throwable exception) {
      Throwable currentException = exception;

      while (currentException instanceof CompletionException && currentException.getCause() != null) {
         currentException = currentException.getCause();
      }

      return currentException;
   }

   private void updateState30(UUID uUID) {
      if (this.compositorPushPresentationScaleService != null && CoreIsInitializedHandler.isReady() && uUID != null && this.values2.add(uUID)) {
         String text = "mc.head." + uUID;
         AccountFetchHeadService.fetchHead(uUID).whenComplete((item, currentItem) -> updateState31(() -> {
            this.values2.remove(uUID);
            if (item != null) {
               this.compositorPushPresentationScaleService.registerImageTexture(text, item);
               this.updateState2();
            }
         }));
      }
   }

   private static String createText(Instant instant) {
      long size = Math.max(0L, Duration.between(instant, Instant.now()).toDays());
      return size == 0L ? "today" : (size == 1L ? "yesterday" : size + "d ago");
   }

   private static void updateState31(Runnable runnable) {
      Minecraft.getInstance().execute(runnable);
   }

   @Override
   public void tick(ScreenScreenIdService screenScreenId) {
      float value = screenScreenId == null ? 0.016666668F : screenScreenId.deltaTime();
      ArrayList<Runnable> arrayList = new ArrayList<>();
      this.items.removeIf(item -> {
         item.delay -= value;
         if (item.delay > 0.0F) {
            return false;
         }

         arrayList.add(item.run);
         return true;
      });
      arrayList.forEach(Runnable::run);
      if (CoreIsInitializedHandler.isReady()) {
         DisplayMetrics displayMetrics = CoreIsInitializedHandler.get().viewport();
         this.viewport(displayMetrics.width(), displayMetrics.height());
      }

      if (this.sceneResizeMarginService != null) {
         this.sceneResizeMarginService.constrainToParent();
      }

      BuiltinSessionTracker.Session session = this.supplier.get();
      if (!Objects.equals(session, this.session2)) {
         this.session2 = session;
         this.updateState2();
      }

      if (this.renderer != null && CoreIsInitializedHandler.isReady()) {
         this.renderer.tick(this.mode == BuiltinSessionTracker.Mode.LIST && (!this.enabled3 || this.enabled6));
      }
   }

   @Override
   public void onClose(ScreenScreenIdService screenScreenId) {
      this.updateState28();
      this.items.clear();
      this.updateState33();
      this.mode = BuiltinSessionTracker.Mode.LIST;
      this.text6 = null;
   }

   private void updateState32(float value, Runnable runnable) {
      this.items.add(new BuiltinSessionTracker.DelayedAction(value, runnable));
   }

   @Override
   public boolean keyPressed(ScreenScreenIdService screenScreenId, int value, int currentValue) {
      if (value == 256) {
         if (this.mode != BuiltinSessionTracker.Mode.LIST) {
            this.updateState16();
            return true;
         } else {
            return false;
         }
      } else if (value == 70 && (currentValue & 10) != 0 && this.mode == BuiltinSessionTracker.Mode.LIST) {
         String text = this.enabled3 && this.enabled6 && this.renderer != null ? "servers.search" : "accounts.search";
         if (this.sceneCornerRadiusService.findById(text) instanceof ControlLetterSpacingService controlLetterSpacing) {
            this.updateState33();
            updateState34(controlLetterSpacing);
            controlLetterSpacing.selectAll();
            updateState36(controlLetterSpacing);
         }

         return true;
      } else if (value == 258) {
         ArrayList arrayList = new ArrayList();
         updateState35(this.sceneCornerRadiusService, arrayList);
         if (!arrayList.isEmpty()) {
            ScenePctService scenePct = this.scenePctService;

            for (ScenePctService currentScenePct : (Iterable<ScenePctService>) (Iterable<?>) (arrayList)) {
               if (currentScenePct instanceof ControlLetterSpacingService currentControlLetterSpacing && currentControlLetterSpacing.focused()) {
                  scenePct = currentScenePct;
               }
            }

            int index = arrayList.indexOf(scenePct);
            int nextValue = (currentValue & 1) != 0 ? 1 : 0;
            this.updateState33();
            this.scenePctService = (ScenePctService<?>)arrayList.get(
               index < 0 ? (nextValue != 0 ? arrayList.size() - 1 : 0) : Math.floorMod(index + (nextValue != 0 ? -1 : 1), arrayList.size())
            );
            if (this.scenePctService instanceof MaterialJoinedService materialJoined) {
               materialJoined.keyboardFocused(true);
            }

            if (this.scenePctService instanceof ControlLetterSpacingService nextControlLetterSpacing) {
               updateState34(nextControlLetterSpacing);
            }

            updateState36(this.scenePctService);
         }

         return true;
      } else if ((value == 257 || value == 32) && this.scenePctService instanceof MaterialJoinedService currentMaterialJoined && currentMaterialJoined.available() && checkCondition2(currentMaterialJoined)) {
         currentMaterialJoined.activate();
         return true;
      } else {
         return false;
      }
   }

   private void updateState33() {
      if (this.scenePctService instanceof MaterialJoinedService materialJoined) {
         materialJoined.keyboardFocused(false);
      }

      this.scenePctService = null;
      ArrayList arrayList = new ArrayList();
      updateState35(this.sceneCornerRadiusService, arrayList);

      for (ScenePctService scenePct : (Iterable<ScenePctService>) (Iterable<?>) (arrayList)) {
         if (scenePct instanceof ControlLetterSpacingService controlLetterSpacing) {
            controlLetterSpacing.unfocus();
         }
      }

      if (CoreIsInitializedHandler.isReady()) {
         CoreIsInitializedHandler.get().scene().clearFocus();
      }
   }

   private static void updateState34(ControlLetterSpacingService controlLetterSpacing) {
      if (CoreIsInitializedHandler.isReady()) {
         CoreIsInitializedHandler.get().scene().setFocusedInput(controlLetterSpacing);
      } else {
         controlLetterSpacing.focus();
      }
   }

   private static boolean checkCondition2(ScenePctService<?> scenePct) {
      for (ScenePctService currentScenePct = scenePct; currentScenePct != null; currentScenePct = currentScenePct.parent()) {
         if (!currentScenePct.visible) {
            return false;
         }
      }

      return true;
   }

   private static void updateState35(ScenePctService<?> scenePct, List<ScenePctService<?>> items) {
      if (scenePct != null && scenePct.visible && scenePct.pointerEvents()) {
         if (scenePct instanceof MaterialJoinedService materialJoined && materialJoined.available() || scenePct instanceof ControlLetterSpacingService) {
            items.add(scenePct);
         }

         for (ScenePctService currentScenePct : scenePct.children()) {
            updateState35(currentScenePct, items);
         }
      }
   }

   private static void updateState36(ScenePctService<?> scenePct) {
      for (ScenePctService currentScenePct = scenePct.parent(); currentScenePct != null; currentScenePct = currentScenePct.parent()) {
         if (!(currentScenePct.scrollMin() >= 0.0F)) {
            float value = scenePct.computedY() < currentScenePct.computedY() + 6.0F
               ? currentScenePct.computedY() + 6.0F - scenePct.computedY()
               : (
                  scenePct.computedY() + scenePct.computedH() > currentScenePct.computedY() + currentScenePct.computedH() - 6.0F
                     ? currentScenePct.computedY() + currentScenePct.computedH() - 6.0F - scenePct.computedY() - scenePct.computedH()
                     : 0.0F
               );
            if (value != 0.0F) {
               float currentValue = Math.max(currentScenePct.scrollMin(), Math.min(0.0F, currentScenePct.scrollY() + value));
               currentScenePct.scrollTarget(currentValue);
               MotionAnimateService.animate(currentScenePct, MotionColorsContainer.Floats.SCROLL_Y, currentValue, MaterialIsLightService.FAST_SPATIAL);
            }
         }
      }
   }

   private static final class DelayedAction {
      float delay;
      final Runnable run;

      DelayedAction(float value, Runnable runnable) {
         this.delay = value;
         this.run = runnable;
      }
   }

   private enum Mode {
      LIST,
      CHOOSE,
      MICROSOFT,
      COOKIE,
      OFFLINE;


      private static BuiltinSessionTracker.Mode[] $values() {
         return new BuiltinSessionTracker.Mode[]{LIST, CHOOSE, MICROSOFT, COOKIE, OFFLINE};
      }
   }

   public record Session(UUID uuid, String name, boolean inWorld) {
   }
}

