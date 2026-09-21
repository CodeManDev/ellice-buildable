package dev.felix.ellice.module.impl;

import dev.felix.ellice.compat.CompatAdapterService;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.render.render3d.GpuMesh;
import dev.felix.ellice.render.render3d.Render3dIdService;
import dev.felix.ellice.render.render3d.Render3dSceneService;
import dev.felix.ellice.render.render3d.geometry.GeometryCubeService;
import dev.felix.ellice.render.render3d.geometry.GeometryXService;
import dev.felix.ellice.render.render3d.material.MaterialErFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class ColorComponent extends ModuleSettingsService {
  private final ModuleSetting.Color moduleColor =
      this.setting(
          new ModuleSetting.Color("Color", -50312)
              .description(
                  "Sets the heart's base, emissive, and glow hue; the selected alpha is ignored."));
  private final ModuleSetting.Number moduleNumber =
      this.setting(
          new ModuleSetting.Number("Size", 0.95F, 0.2F, 2.5F, 0.02F)
              .description(
                  "Sets the heart's base world-space scale before its subtle pulse is applied."));
  private final ModuleSetting.Number moduleNumber2 =
      this.setting(
          new ModuleSetting.Number("Distance", 4.0F, 1.5F, 12.0F, 0.1F)
              .description("Places the heart this many blocks in front of the active camera."));
  private final ModuleSetting.Number moduleNumber3 =
      this.setting(
          new ModuleSetting.Number("Metallic", 0.58F, 0.0F, 1.0F, 0.01F)
              .description(
                  "Controls the PBR metal response; 0 behaves like a dielectric and 1 like polished"
                      + " metal."));
  private final ModuleSetting.Number moduleNumber4 =
      this.setting(
          new ModuleSetting.Number("Roughness", 0.2F, 0.045F, 1.0F, 0.01F)
              .description(
                  "Controls reflection spread; low values look glossy and sharp, while high values"
                      + " look matte."));
  private final ModuleSetting.Number moduleNumber5 =
      this.setting(
          new ModuleSetting.Number("Emission", 1.15F, 0.0F, 4.0F, 0.05F)
              .description(
                  "Sets the heart surface's self-lit intensity independently of the surrounding"
                      + " glow."));
  private final ModuleSetting.Number moduleNumber6 =
      this.setting(
          new ModuleSetting.Number("Glow Strength", 2.4F, 0.0F, 12.0F, 0.1F)
              .description(
                  "Controls the intensity of the depth-aware bloom around the heart; 0 disables"
                      + " visible glow."));
  private final ModuleSetting.Number moduleNumber7 =
      this.setting(
          new ModuleSetting.Number("Blur Radius", 30.0F, 0.0F, 96.0F, 1.0F)
              .description(
                  "Sets how widely the heart's glow is blurred in screen pixels; larger radii cost"
                      + " more GPU time."));
  private final ModuleSetting.Number moduleNumber8 =
      this.setting(
          new ModuleSetting.Number("Pulse Speed", 1.25F, 0.0F, 4.0F, 0.05F)
              .description(
                  "Sets size-pulse cycles per second; 0 holds the heart at its base size."));
  private GpuMesh renderer;
  private MaterialErFactory materialErFactory;
  private Render3dIdService renderer2;
  private long timestamp;
  private double value;

  public ColorComponent() {
    super(
        ModuleBuilderData.builder("3D Blurred Heart")
            .description("Displays a configurable glowing 3D heart in front of the camera.")
            .category(ModuleFeatureType.DEVELOPMENT)
            .build());
  }

  @Override
  protected void onEnable() {
    this.timestamp = 0L;
    this.on(EventAttackInputService.WORLD_RENDER).run(this::updateState);
    this.on(EventAttackInputService.WORLD)
        .run(
            item -> {
              this.renderer2 = null;
              this.timestamp = 0L;
            });
  }

  @Override
  protected void onDisable() {
    Render3dSceneService render3dScene = CoreIsInitializedHandler.get().renderer3D();
    if (render3dScene != null && this.renderer2 != null) {
      render3dScene.scene().remove(this.renderer2);
    }

    this.renderer2 = null;
  }

  private void updateState(EventAttackInputService.WorldRender worldRender) {
    Minecraft minecraft = CoreIsInitializedHandler.mc();
    if (minecraft.player != null && minecraft.level != null) {
      Render3dSceneService render3dScene = CoreIsInitializedHandler.get().renderer3D();
      if (render3dScene != null) {
        this.updateState2();
        if (this.renderer2 == null) {
          this.renderer2 = render3dScene.scene().add(this.renderer, this.materialErFactory);
        }

        Vector3f vector3f;
        Vector3f currentVector3f;
        if (worldRender.camera() != null) {
          vector3f = CompatAdapterService.cameraForward(worldRender.camera());
          currentVector3f = CompatAdapterService.cameraUp(worldRender.camera());
        } else {
          double doubleValue = Math.toRadians(minecraft.player.getYRot());
          double currentDoubleValue = Math.toRadians(minecraft.player.getXRot());
          double nextDoubleValue = Math.cos(currentDoubleValue);
          vector3f =
              new Vector3f(
                  (float) (-Math.sin(doubleValue) * nextDoubleValue),
                  (float) (-Math.sin(currentDoubleValue)),
                  (float) (Math.cos(doubleValue) * nextDoubleValue));
          currentVector3f = new Vector3f(0.0F, 1.0F, 0.0F);
        }

        vector3f.normalize();
        currentVector3f.normalize();
        Vec3 vec3 = worldRender.cameraPos();
        double previousDoubleValue = ((Float) this.moduleNumber2.get()).floatValue();
        long offset = System.nanoTime();
        double sourceDoubleValue = offset / 1.0E9;
        double targetDoubleValue =
            this.timestamp == 0L ? 0.0 : Math.min((offset - this.timestamp) / 1.0E9, 0.25);
        this.timestamp = offset;
        this.value =
            (this.value
                    + targetDoubleValue
                        * ((Float) this.moduleNumber8.get()).floatValue()
                        * 3.141592653589793
                        * 2.0)
                % 6.283185307179586;
        Vector3f nextVector3f = new Vector3f(vector3f).negate();
        Vector3f previousVector3f =
            new Vector3f(currentVector3f)
                .sub(new Vector3f(nextVector3f).mul(currentVector3f.dot(nextVector3f)))
                .normalize();
        Vector3f sourceVector3f = new Vector3f(previousVector3f).cross(nextVector3f).normalize();
        previousVector3f.set(nextVector3f).cross(sourceVector3f).normalize();
        Matrix3f matrix3f =
            new Matrix3f()
                .setColumn(0, sourceVector3f)
                .setColumn(1, previousVector3f)
                .setColumn(2, nextVector3f);
        float currentValue = (float) Math.sin(sourceDoubleValue * 0.82) * 0.045F;
        Quaternionf quaternionf =
            new Quaternionf().setFromNormalized(matrix3f).rotateZ(currentValue);
        float nextValue =
            this.moduleNumber8.get() <= 0.0F ? 1.0F : 1.0F + 0.065F * (float) Math.sin(this.value);
        float previousValue = (Float) this.moduleNumber.get() * nextValue;
        this.renderer2.transform(
            new GeometryXService(
                vec3.x + vector3f.x * previousDoubleValue,
                vec3.y + vector3f.y * previousDoubleValue,
                vec3.z + vector3f.z * previousDoubleValue,
                quaternionf,
                new Vector3f(previousValue)));
        int sourceValue = (Integer) this.moduleColor.get() | 0xFF000000;
        this.materialErFactory
            .color("uBaseColor", sourceValue)
            .uniform("uMetallic", (Float) this.moduleNumber3.get())
            .uniform("uRoughness", (Float) this.moduleNumber4.get())
            .uniform(
                "uEmissive",
                calculateValue(sourceValue),
                calculateValue2(sourceValue),
                calculateValue3(sourceValue))
            .uniform("uEmissiveStrength", (Float) this.moduleNumber5.get())
            .glow(sourceValue, (Float) this.moduleNumber6.get(), (Float) this.moduleNumber7.get());
      }
    }
  }

  private void updateState2() {
    if (this.materialErFactory == null) {
      Render3dSceneService render3dScene = CoreIsInitializedHandler.get().renderer3D();
      this.renderer = render3dScene.createMesh(GeometryCubeService.heart());
      this.materialErFactory = render3dScene.createPbrMaterial("ellice:blurred-heart");
    }
  }

  private static float calculateValue(int value) {
    return (value >>> 16 & 0xFF) / 255.0F;
  }

  private static float calculateValue2(int value) {
    return (value >>> 8 & 0xFF) / 255.0F;
  }

  private static float calculateValue3(int value) {
    return (value & 0xFF) / 255.0F;
  }
}
