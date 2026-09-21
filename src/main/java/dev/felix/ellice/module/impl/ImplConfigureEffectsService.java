


package dev.felix.ellice.module.impl;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleNameService;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.render.render3d.Render3dSceneService;
import dev.felix.ellice.render.render3d.post.PostIdService;
import java.util.Arrays;

public final class ImplConfigureEffectsService
extends ModuleSettingsService {
    private final ModuleNameService f8j188dg7t43 = this.settingCategory("Look");
    private final ModuleNameService fdrefzrgv80i = this.settingCategory("Tone");
    private final ModuleNameService f7nzpbf9gv3l = this.settingCategory("Lens & atmosphere");
    private final ModuleNameService fe6220a6nueg = this.settingCategory("Light & film");
    private final ModuleNameService f8nom6kx8xwt = this.settingCategory("World atmosphere");
    private final ModuleSetting.Mode fexylzydn2xi = this.setting(this.f8j188dg7t43, (ModuleSetting.Mode)new ModuleSetting.Mode("Preset", new String[]{"Cinematic", "Neon", "Dream", "Noir", "Golden Hour", "Sakura", "Nordic", "Emerald", "Moonlight", "Vaporwave", "Amber Film", "Arctic"}, "Cinematic").preview("Cinematic", -14394010, -6508639, -1719648).preview("Neon", -13096616, -11155741, -1807172).preview("Dream", -8292204, -2574145, -532543).preview("Noir", -14211283, -7171431, -1579029).preview("Golden Hour", -10138302, -1595300, -8024).preview("Sakura", -8029282, -2381119, -7712).preview("Nordic", -13350568, -8019034, -2364964).preview("Emerald", -14463415, -8537459, -2041952).preview("Moonlight", -15194817, -9534791, -4007438).preview("Vaporwave", -11317863, -1932351, -8724256).preview("Amber Film", -11057089, -4158117, -994134).preview("Arctic", -13019028, -6500899, -1641228).description("Twelve color grades with matching atmosphere colors. Use the module presets above for complete light, sky and lens setups."));
    private final ModuleSetting.Number f9hlwyvtb36k = this.setting(this.f8j188dg7t43, (ModuleSetting.Number)new ModuleSetting.Number("Intensity", 1.0f, 0.0f, 1.0f, Float.intBitsToFloat(1008981770)).description("Blends between the original world at 0 and the fully processed result at 1."));
    private final ModuleSetting.Number fe0eqrqa3cxx = this.setting(this.fdrefzrgv80i, (ModuleSetting.Number)new ModuleSetting.Number("Exposure", 0.0f, Float.intBitsToFloat(-1073741824), 2.0f, Float.intBitsToFloat(1028443341)).description("Adjusts world brightness in photographic stops; each +1 doubles incoming light."));
    private final ModuleSetting.Number feqa8uhidhvk = this.setting(this.fdrefzrgv80i, (ModuleSetting.Number)new ModuleSetting.Number("Contrast", Float.intBitsToFloat(1065772646), Float.intBitsToFloat(0x3F000000), Float.intBitsToFloat(1072064102), Float.intBitsToFloat(1008981770)).description("Scales tonal separation around mid-gray; 1 is neutral and higher values deepen extremes."));
    private final ModuleSetting.Number f56j8vpinlbu = this.setting(this.fdrefzrgv80i, (ModuleSetting.Number)new ModuleSetting.Number("Saturation", Float.intBitsToFloat(1066024305), 0.0f, 2.0f, Float.intBitsToFloat(1008981770)).description("Scales world color intensity; 0 is grayscale and 1 preserves the graded colors."));
    private final ModuleSetting.Number fba1faw03z4i = this.setting(this.f7nzpbf9gv3l, (ModuleSetting.Number)new ModuleSetting.Number("Vignette", Float.intBitsToFloat(1046562734), 0.0f, 1.0f, Float.intBitsToFloat(1008981770)).description("Darkens the edges of the world image; higher values focus attention toward the center."));
    private final ModuleSetting.Number f16uy7evjhhn = this.setting(this.f7nzpbf9gv3l, (ModuleSetting.Number)new ModuleSetting.Number("Chromatic", Float.intBitsToFloat(985963430), 0.0f, Float.intBitsToFloat(1011129254), Float.intBitsToFloat(973279855)).description("Offsets red and blue channels radially from the center; high values create obvious color fringing."));
    private final ModuleSetting.Number f2jsfj90s4wy = this.setting(this.f7nzpbf9gv3l, (ModuleSetting.Number)new ModuleSetting.Number("Depth Fog", 0.0f, 0.0f, 1.0f, Float.intBitsToFloat(1008981770)).description("Blends distant world geometry into the preset's atmospheric tint using scene depth; 0 disables it."));
    private final ModuleSetting.Number f400z446m4yx = this.muoqyr1q0kt(this.f7nzpbf9gv3l, "Fog Distance", Float.intBitsToFloat(1121976320), Float.intBitsToFloat(1103101952), Float.intBitsToFloat(1132462080), 1.0f, "Distance in blocks at which the added depth fog reaches full strength.");
    private final ModuleSetting.Number fg2cfycl0nb4 = this.muoqyr1q0kt(this.fe6220a6nueg, "Bloom", 0.0f, 0.0f, 1.0f, Float.intBitsToFloat(1008981770), "Soft light scattered from bright world pixels; 0 skips its texture samples.");
    private final ModuleSetting.Number f2pffq240nrv = this.muoqyr1q0kt(this.fe6220a6nueg, "Bloom Radius", Float.intBitsToFloat(0x41000000), 1.0f, Float.intBitsToFloat(1103101952), 1.0f, "Spread of the soft highlight glow, scaled with screen resolution.");
    private final ModuleSetting.Number fduc08pl93h8 = this.muoqyr1q0kt(this.fe6220a6nueg, "Bloom Threshold", Float.intBitsToFloat(1059481190), Float.intBitsToFloat(1045220557), 1.0f, Float.intBitsToFloat(1008981770), "Only pixels brighter than this level contribute to bloom and halation.");
    private final ModuleSetting.Number fhus9w39pm91 = this.muoqyr1q0kt(this.fe6220a6nueg, "Halation", 0.0f, 0.0f, 1.0f, Float.intBitsToFloat(1008981770), "Warm red-orange light bleeding around highlights, inspired by photographic film.");
    private final ModuleSetting.Number fdwul5vidz6g = this.muoqyr1q0kt(this.fe6220a6nueg, "Film Grain", Float.intBitsToFloat(1007908028), 0.0f, Float.intBitsToFloat(1034147594), Float.intBitsToFloat(981668463), "Fine animated film texture. Set to 0 for a clean digital image.");
    private final ModuleSetting.Mode fekjkcrbywe0 = this.setting(this.f8nom6kx8xwt, (ModuleSetting.Mode)new ModuleSetting.Mode("Sky Effect", new String[]{"Off", "Aurora", "Nebula", "Sunset"}, "Off").description("Animated sky anchored to your viewing direction and masked by world depth. It does not change the server's time or weather."));
    private final ModuleSetting.Number fct1m7ywdrem = this.muoqyr1q0kt(this.f8nom6kx8xwt, "Sky Strength", Float.intBitsToFloat(1057803469), 0.0f, 1.0f, Float.intBitsToFloat(1008981770), "Blends the decorative sky with the original sky; terrain stays in front.");
    private final ModuleSetting.Number f85kuiy1tp0k = this.muoqyr1q0kt(this.f8nom6kx8xwt, "Stars", 0.0f, 0.0f, 1.0f, Float.intBitsToFloat(1008981770), "Directional star field visible through open sky, with gentle twinkling.");
    private final ModuleSetting.Number fcz1ni03p31x = this.muoqyr1q0kt(this.f8nom6kx8xwt, "Height Mist", 0.0f, 0.0f, 1.0f, Float.intBitsToFloat(1008981770), "Depth-aware haze concentrated near Mist Height; fades into distant scenery.");
    private final ModuleSetting.Number f7o7u00tlobw = this.muoqyr1q0kt(this.f8nom6kx8xwt, "Mist Height", Float.intBitsToFloat(1115684864), Float.intBitsToFloat(-1031798784), Float.intBitsToFloat(1134559232), 1.0f, "World Y level of the mist layer. Set this near the ground height of your scene.");
    private final ModuleSetting.Number fkd2t7k1rpe = this.muoqyr1q0kt(this.f8nom6kx8xwt, "Light Motes", 0.0f, 0.0f, 1.0f, Float.intBitsToFloat(1008981770), "Small drifting lights in 3D around the camera, occluded by terrain. Adds a bounded particle shader pass.");
    private final ModuleSetting.Color f4xf2w8ilzxt = this.setting(this.f8nom6kx8xwt, (ModuleSetting.Color)new ModuleSetting.Color("Mote Color", -8796).description("Color and opacity of the floating world-space lights."));
    private final ModuleSetting.Number f37fdpxjwsf4 = this.muoqyr1q0kt(this.f8nom6kx8xwt, "Atmosphere Speed", Float.intBitsToFloat(0x3EB33333), 0.0f, 2.0f, Float.intBitsToFloat(1028443341), "Animation rate of the sky, stars, mist and floating lights. Zero freezes their animation.");

    public ImplConfigureEffectsService() {
        super(ModuleBuilderData.builder("World Shaders").description("Twelve color grades, bloom, film light, animated skies, height mist and 3D light motes.").category(ModuleFeatureType.VISUALS).build());
        this.settings().forEach(moduleSetting -> moduleSetting.onValueChanged(this::m39uaembhmmo));
    }

    private ModuleSetting.Number muoqyr1q0kt(ModuleNameService moduleNameService, String string, float f, float f2, float f3, float f4, String string2) {
        return this.setting(moduleNameService, (ModuleSetting.Number)new ModuleSetting.Number(string, f, f2, f3, f4).description(string2));
    }

    @Override
    protected void onEnable() {
        this.m39uaembhmmo();
    }

    @Override
    protected void onDisable() {
        Render3dSceneService render3dSceneService = this.m7x9bmw1g5z0();
        if (render3dSceneService != null) {
            render3dSceneService.worldGradeEffect().enabled(false);
            render3dSceneService.worldAtmosphereEffect().enabled(false);
        }
    }

    private void m39uaembhmmo() {
        Render3dSceneService render3dSceneService = this.m7x9bmw1g5z0();
        if (render3dSceneService == null) {
            return;
        }
        this.configureEffects(render3dSceneService.worldAtmosphereEffect(), render3dSceneService.worldGradeEffect());
    }

    public void configureEffects(PostIdService postIdService, PostIdService postIdService2) {
        boolean bl = this.isEnabled() && ((Float)this.f9hlwyvtb36k.get()).floatValue() > Float.intBitsToFloat(953267991);
        int n = Arrays.asList(this.fekjkcrbywe0.options()).indexOf(this.fekjkcrbywe0.get());
        postIdService.enabled((bl && (n > 0 && ((Float)this.fct1m7ywdrem.get()).floatValue() > Float.intBitsToFloat(953267991) || ((Float)this.f85kuiy1tp0k.get()).floatValue() > Float.intBitsToFloat(953267991) || ((Float)this.fcz1ni03p31x.get()).floatValue() > Float.intBitsToFloat(953267991) || ((Float)this.fkd2t7k1rpe.get()).floatValue() > Float.intBitsToFloat(953267991) || ((Float)this.f2jsfj90s4wy.get()).floatValue() > Float.intBitsToFloat(953267991)) ? 1 : 0) != 0).requiresDepth(true).uniform("uMode", this.md1f1no6gt5g()).uniform("uIntensity", ((Float)this.f9hlwyvtb36k.get()).floatValue()).uniform("uSkyMode", n).uniform("uSkyStrength", ((Float)this.fct1m7ywdrem.get()).floatValue()).uniform("uStars", ((Float)this.f85kuiy1tp0k.get()).floatValue()).uniform("uHeightMist", ((Float)this.fcz1ni03p31x.get()).floatValue()).uniform("uMistHeight", ((Float)this.f7o7u00tlobw.get()).floatValue()).uniform("uDepthFog", ((Float)this.f2jsfj90s4wy.get()).floatValue()).uniform("uFogDistance", ((Float)this.f400z446m4yx.get()).floatValue()).uniform("uMotes", ((Float)this.fkd2t7k1rpe.get()).floatValue()).color("uMoteColor", (Integer)this.f4xf2w8ilzxt.get()).uniform("uAtmosphereSpeed", ((Float)this.f37fdpxjwsf4.get()).floatValue());
        postIdService2.enabled(bl).requiresDepth(false).uniform("uMode", this.md1f1no6gt5g()).uniform("uIntensity", ((Float)this.f9hlwyvtb36k.get()).floatValue()).uniform("uExposure", ((Float)this.fe0eqrqa3cxx.get()).floatValue()).uniform("uContrast", ((Float)this.feqa8uhidhvk.get()).floatValue()).uniform("uSaturation", ((Float)this.f56j8vpinlbu.get()).floatValue()).uniform("uVignette", ((Float)this.fba1faw03z4i.get()).floatValue()).uniform("uChromatic", ((Float)this.f16uy7evjhhn.get()).floatValue()).uniform("uDepthFog", 0.0f).uniform("uFogDistance", ((Float)this.f400z446m4yx.get()).floatValue()).uniform("uBloom", ((Float)this.fg2cfycl0nb4.get()).floatValue()).uniform("uBloomRadius", ((Float)this.f2pffq240nrv.get()).floatValue()).uniform("uBloomThreshold", ((Float)this.fduc08pl93h8.get()).floatValue()).uniform("uHalation", ((Float)this.fhus9w39pm91.get()).floatValue()).uniform("uGrain", ((Float)this.fdwul5vidz6g.get()).floatValue());
    }

    private Render3dSceneService m7x9bmw1g5z0() {
        if (!CoreIsInitializedHandler.isReady()) {
            return null;
        }
        return CoreIsInitializedHandler.get().renderer3D();
    }

    private int md1f1no6gt5g() {
        return Math.max(0, Arrays.asList(this.fexylzydn2xi.options()).indexOf(this.fexylzydn2xi.get()));
    }
}

