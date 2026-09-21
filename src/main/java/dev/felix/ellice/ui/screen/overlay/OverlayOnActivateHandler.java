package dev.felix.ellice.ui.screen.overlay;

import dev.felix.ellice.account.AccountFetchHeadService;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.SceneColorService;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.SceneDtService;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.SceneTextureService;
import dev.felix.ellice.ui.text.TextMode;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;

public final class OverlayOnActivateHandler {
  private static final int count = 1825822946;
  private static final int count2 = 1755884986;
  private static final int fcm316jjro = 2030043135;
  private static final int count3 = 805306368;
  private static final int count4 = -2030043137;
  private static final int count5 = -434628574;
  private static final int count6 = -1474815966;
  private static final int count7 = -10262799;
  private static final int count8 = -7643914;
  private static final int count9 = -1292135;
  private static final int count10 = -14494738;
  private static final int count11 = -278748;
  private static final float value = 14.0F;
  private static final float value2 = 14.0F;
  private static final float value3 = 40.0F;
  private static final float value4 = 30.0F;
  private final SceneDtService sceneDtService;
  private final CompositorPushPresentationScaleService compositorPushPresentationScaleService;
  private final LayoutContainerNode sceneComponent4;
  private final SceneCornerRadiusService sceneCornerRadiusService;
  private final SceneCornerRadiusService sceneCornerRadiusService2;
  private final SceneColorService sceneColorService;
  private final SceneTextureService sceneTextureService;
  private final SceneTextService sceneTextService;
  private final SceneTextService sceneTextService2;
  private final ConcurrentHashMap<String, Boolean> text2 = new ConcurrentHashMap<>();
  private String text3;
  private String text4;
  private Consumer<OverlayOnActivateHandler> consumer;

  public OverlayOnActivateHandler(
      SceneDtService sceneDt,
      CompositorPushPresentationScaleService compositorPushPresentationScale) {
    this.sceneDtService = sceneDt;
    this.compositorPushPresentationScaleService = compositorPushPresentationScale;
    this.sceneComponent4 =
        new LayoutContainerNode()
            .position(0.0F, 14.0F)
            .size(ScenePctService.pct(100.0F), 40.0F)
            .direction(ScenePctService.Direction.ROW)
            .align(ScenePctService.Align.START)
            .justify(ScenePctService.Justify.END)
            .padding(0.0F, 14.0F, 0.0F, 14.0F)
            .layerBreak(true)
            .visible(false);
    this.sceneComponent4.id("client.accountPill");
    this.sceneCornerRadiusService =
        new SceneCornerRadiusService()
            .size(-1.0F, 40.0F)
            .cornerRadius(20.0F)
            .backgroundColor(1825822946)
            .gradientEnd(1755884986)
            .blur(34.0F)
            .glass(true)
            .glassSaturation(1.32F)
            .glassBrightness(0.035F)
            .glassContrast(1.07F)
            .glassRefraction(8.6F)
            .glassNoise(0.0025F)
            .glassChromatic(1.05F)
            .glassHighlightColor(-2030043137)
            .border(0.85F, 2030043135)
            .secondaryBorderColor(805306368)
            .shadow(16.0F)
            .shadowColor(1879048192)
            .direction(ScenePctService.Direction.ROW)
            .align(ScenePctService.Align.CENTER)
            .justify(ScenePctService.Justify.START)
            .padding(0.0F, 14.0F, 0.0F, 5.0F)
            .gap(10.0F)
            .cursorStyle(ScenePctService.CursorStyle.POINTER)
            .onClick(this::updateState);
    this.sceneCornerRadiusService.onHoverChange(
        item ->
            this.sceneCornerRadiusService.animate(
                "scale", item ? 1.02F : 1.0F, SceneEaseHandler.Spring.SNAPPY));
    this.sceneCornerRadiusService2 =
        new SceneCornerRadiusService()
            .size(30.0F, 30.0F)
            .cornerRadius(15.0F)
            .backgroundColor(0)
            .border(0.5F, 1358954495)
            .secondaryBorderColor(335544320)
            .direction(ScenePctService.Direction.NONE);
    this.sceneColorService =
        new SceneColorService()
            .position(0.0F, 0.0F)
            .size(30.0F, 30.0F)
            .cornerRadius(15.0F)
            .colors(-10262799, -7643914, -1292135, -14494738, -278748)
            .opacity(0.92F);
    this.sceneCornerRadiusService2.addChild(this.sceneColorService);
    this.sceneTextureService =
        new SceneTextureService().position(0.0F, 0.0F).size(30.0F, 30.0F).cornerRadius(15.0F);
    this.sceneTextureService.visible(false);
    this.sceneCornerRadiusService2.addChild(this.sceneTextureService);
    this.sceneCornerRadiusService.addChild(this.sceneCornerRadiusService2);
    LayoutContainerNode currentSize =
        new LayoutContainerNode()
            .direction(ScenePctService.Direction.COLUMN)
            .align(ScenePctService.Align.START)
            .justify(ScenePctService.Justify.CENTER)
            .gap(1.0F)
            .size(-1.0F, -1.0F);
    this.sceneTextService =
        new SceneTextService()
            .text("Player")
            .fontSize(10.0F)
            .color(-434628574)
            .fontVariant(TextMode.BOLD)
            .size(-1.0F, 12.0F);
    this.sceneTextService2 =
        new SceneTextService()
            .text("Minecraft account")
            .fontSize(7.0F)
            .color(-1474815966)
            .fontVariant(TextMode.REGULAR)
            .size(-1.0F, 9.0F);
    currentSize.addChild(this.sceneTextService);
    currentSize.addChild(this.sceneTextService2);
    this.sceneCornerRadiusService.addChild(currentSize);
    this.sceneComponent4.addChild(this.sceneCornerRadiusService);
    sceneDt.root().addChild(this.sceneComponent4);
  }

  public void onActivate(Consumer<OverlayOnActivateHandler> currentConsumer) {
    this.consumer = currentConsumer;
  }

  private void updateState() {
    if (this.consumer != null) {
      this.consumer.accept(this);
    }
  }

  public void update() {
    Minecraft minecraft = Minecraft.getInstance();
    boolean enabled = checkCondition(minecraft.screen);
    this.sceneComponent4.visible(enabled);
    if (enabled) {
      String currentText = minecraft.getUser() != null ? minecraft.getUser().getName() : null;
      if (currentText == null || currentText.isBlank()) {
        currentText = "Player";
      }

      if (!currentText.equals(this.text3)) {
        this.text3 = currentText;
        this.sceneTextService.text(currentText);
        this.updateState2(currentText);
      }
    }
  }

  private void updateState2(String text) {
    Minecraft minecraft = Minecraft.getInstance();
    UUID uUID = minecraft.getUser() != null ? minecraft.getUser().getProfileId() : null;
    if (uUID != null) {
      String currentText = textureName(uUID);
      this.text4 = currentText;
      if (this.compositorPushPresentationScaleService.getNamedTexture(currentText) != null
          && this.compositorPushPresentationScaleService.getNamedTexture(currentText).valid()) {
        this.updateState3(currentText);
      } else if (this.text2.putIfAbsent(currentText, Boolean.TRUE) == null) {
        AccountFetchHeadService.fetchHead(uUID)
            .whenComplete(
                (item, currentItem) ->
                    minecraft.execute(
                        () -> {
                          if (currentItem == null && item != null) {
                            this.compositorPushPresentationScaleService.registerImageTexture(
                                currentText, item);
                            if (currentText.equals(this.text4)) {
                              this.updateState3(currentText);
                            }
                          } else {
                            this.text2.remove(currentText);
                          }
                        }));
      }
    }
  }

  private void updateState3(String text) {
    this.sceneTextureService.textureName(text);
    this.sceneColorService.visible(false);
    this.sceneTextureService.visible(true);
  }

  public static String textureName(UUID uUID) {
    return "mc.head." + uUID.toString();
  }

  public boolean isVisible() {
    return this.sceneComponent4.visible;
  }

  public float pillX() {
    return this.sceneCornerRadiusService.layoutX();
  }

  public float pillY() {
    return this.sceneCornerRadiusService.layoutY();
  }

  public float pillW() {
    return this.sceneCornerRadiusService.layoutWidth();
  }

  public float pillH() {
    return this.sceneCornerRadiusService.layoutHeight();
  }

  public float avatarX() {
    return this.sceneCornerRadiusService2.layoutX();
  }

  public float avatarY() {
    return this.sceneCornerRadiusService2.layoutY();
  }

  public float avatarW() {
    return this.sceneCornerRadiusService2.layoutWidth();
  }

  public float avatarH() {
    return this.sceneCornerRadiusService2.layoutHeight();
  }

  public String username() {
    return this.text3 != null ? this.text3 : "Player";
  }

  public String skinTexture() {
    return this.text4;
  }

  private static boolean checkCondition(Screen screen) {
    return screen instanceof TitleScreen
        || screen instanceof JoinMultiplayerScreen
        || screen instanceof SelectWorldScreen;
  }
}
