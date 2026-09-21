


package dev.felix.ellice.module;

import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.ui.text.TextData;
import dev.felix.ellice.ui.text.TextMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class HudStyleSettings {
    private final List<ModuleSetting<?>> fjo0w9iagbly = new ArrayList();
    public final ModuleSetting.Mode design = this.m4rvwditawht((ModuleSetting.Mode)new ModuleSetting.Mode("Design", new String[]{"Glass", "Solid", "Outline", "Pill", "Minimal", "Custom"}, "Glass").description("Chooses the HUD panel treatment; presets override incompatible fill, blur, border, or corner values."));
    public final ModuleSetting.Number scale = this.m4rvwditawht((ModuleSetting.Number)new ModuleSetting.Number("Scale", 1.0f, Float.intBitsToFloat(1058642330), 2.0f, Float.intBitsToFloat(1028443341)).description("Multiplies HUD text, spacing, panel geometry, and element bounds."));
    public final ModuleSetting.Number fontSize = this.m4rvwditawht((ModuleSetting.Number)new ModuleSetting.Number("Font Size", Float.intBitsToFloat(0x41100000), Float.intBitsToFloat(0x40C00000), Float.intBitsToFloat(1101004800), Float.intBitsToFloat(0x3F000000)).description("Sets the base HUD text size in pixels before the global scale is applied."));
    public final ModuleSetting.Mode font = this.m4rvwditawht((ModuleSetting.Mode)new ModuleSetting.Mode("Font", new String[]{"Regular", "Bold", "Italic", "Bold Italic"}, "Regular").description("Selects the typeface weight and slant used by every text-based HUD element."));
    public final ModuleSetting.Number paddingX = this.m4rvwditawht((ModuleSetting.Number)new ModuleSetting.Number("Padding X", Float.intBitsToFloat(0x40A00000), 0.0f, Float.intBitsToFloat(1099956224), Float.intBitsToFloat(0x3F000000)).description("Adds horizontal space around HUD text, in pixels before scaling."));
    public final ModuleSetting.Number paddingY = this.m4rvwditawht((ModuleSetting.Number)new ModuleSetting.Number("Padding Y", Float.intBitsToFloat(0x40400000), 0.0f, Float.intBitsToFloat(0x41400000), Float.intBitsToFloat(0x3F000000)).description("Adds vertical space around HUD text, in pixels before scaling."));
    public final ModuleSetting.Color textColor = this.m4rvwditawht((ModuleSetting.Color)new ModuleSetting.Color("Text Color", -434628574).description("Sets the text tint and alpha used by text-based HUD elements."));
    public final ModuleSetting.Number textOutline = this.m4rvwditawht((ModuleSetting.Number)new ModuleSetting.Number("Text Outline", 0.0f, 0.0f, 2.0f, Float.intBitsToFloat(1048576000)).description("Sets the glyph outline thickness in pixels; 0 disables the outline."));
    public final ModuleSetting.Color textOutlineColor = this.m4rvwditawht((ModuleSetting.Color)new ModuleSetting.Color("Text Outline Color", -1442840576).description("Sets the tint and alpha of the glyph outline when its thickness is above 0."));
    public final ModuleSetting.Bool textShadow = this.m4rvwditawht((ModuleSetting.Bool)new ModuleSetting.Bool("Text Shadow", false).description("Draws an offset shadow behind HUD text to improve contrast on busy scenes."));
    public final ModuleSetting.Number textShadowDistance = this.m4rvwditawht((ModuleSetting.Number)new ModuleSetting.Number("Text Shadow Distance", 1.0f, Float.intBitsToFloat(0x3F000000), Float.intBitsToFloat(0x40800000), Float.intBitsToFloat(0x3F000000)).description("Sets the text-shadow offset in pixels before global scaling."));
    public final ModuleSetting.Color textShadowColor = this.m4rvwditawht((ModuleSetting.Color)new ModuleSetting.Color("Text Shadow Color", -1879048192).description("Sets the tint and alpha of the optional text shadow."));
    public final ModuleSetting.Bool background = this.m4rvwditawht((ModuleSetting.Bool)new ModuleSetting.Bool("Background", true).description("Draws a panel behind HUD content; Minimal and Outline designs suppress the fill."));
    public final ModuleSetting.Color backgroundColor = this.m4rvwditawht((ModuleSetting.Color)new ModuleSetting.Color("Background Color", 1557387490).description("Sets the panel's primary fill tint and alpha."));
    public final ModuleSetting.Color gradientEnd = this.m4rvwditawht((ModuleSetting.Color)new ModuleSetting.Color("Gradient End", 1353231802).description("Sets the second color of the vertical panel gradient; transparent alpha removes the gradient."));
    public final ModuleSetting.Number panelOpacity = this.m4rvwditawht((ModuleSetting.Number)new ModuleSetting.Number("Panel Opacity", 1.0f, 0.0f, 1.0f, Float.intBitsToFloat(1028443341)).description("Multiplies panel fill opacity from fully transparent at 0 to unchanged at 1."));
    public final ModuleSetting.Number cornerRadius = this.m4rvwditawht((ModuleSetting.Number)new ModuleSetting.Number("Corner Radius", Float.intBitsToFloat(0x41000000), 0.0f, Float.intBitsToFloat(1099956224), Float.intBitsToFloat(0x3F000000)).description("Sets panel corner roundness in pixels before scaling; Pill derives its radius from panel height."));
    public final ModuleSetting.Number blur = this.m4rvwditawht((ModuleSetting.Number)new ModuleSetting.Number("Blur", Float.intBitsToFloat(1106247680), 0.0f, Float.intBitsToFloat(0x42200000), 1.0f).description("Controls the backdrop blur radius behind filled panels; higher values cost more GPU time."));
    public final ModuleSetting.Number shadow = this.m4rvwditawht((ModuleSetting.Number)new ModuleSetting.Number("Shadow", Float.intBitsToFloat(1101004800), 0.0f, Float.intBitsToFloat(1103101952), 1.0f).description("Sets the panel shadow softness in pixels before scaling; 0 disables it."));
    public final ModuleSetting.Color shadowColor = this.m4rvwditawht((ModuleSetting.Color)new ModuleSetting.Color("Shadow Color", Integer.MIN_VALUE).description("Sets the tint and alpha of panel shadows."));
    public final ModuleSetting.Number borderWidth = this.m4rvwditawht((ModuleSetting.Number)new ModuleSetting.Number("Border Width", Float.intBitsToFloat(0x3F000000), 0.0f, Float.intBitsToFloat(0x40800000), Float.intBitsToFloat(1048576000)).description("Sets panel border thickness in pixels before scaling; 0 removes the border."));
    public final ModuleSetting.Color borderColor = this.m4rvwditawht((ModuleSetting.Color)new ModuleSetting.Color("Border Color", 0x60FFFFFF).description("Sets the primary border tint and alpha, used alone or as the top of its gradient."));
    public final ModuleSetting.Color secondaryBorderColor = this.m4rvwditawht((ModuleSetting.Color)new ModuleSetting.Color("Border Color 2", 0x28000000).description("Sets the lower border-gradient color; transparent alpha falls back to a single border color."));
    public final ModuleSetting.Number edgeSoftness = this.m4rvwditawht((ModuleSetting.Number)new ModuleSetting.Number("Edge Softness", 0.0f, 0.0f, Float.intBitsToFloat(0x41400000), Float.intBitsToFloat(0x3F000000)).description("Feathers panel edges by this many pixels before scaling; high values make silhouettes less crisp."));
    public final ModuleSetting.Bool insetShadow = this.m4rvwditawht((ModuleSetting.Bool)new ModuleSetting.Bool("Inset Shadow", false).description("Moves the configured panel shadow inside the fill for a recessed appearance."));
    public final ModuleSetting.Bool glass = this.m4rvwditawht((ModuleSetting.Bool)new ModuleSetting.Bool("Glass Material", true).description("Uses the refractive glass shader for filled panels instead of the standard panel material."));
    public final ModuleSetting.Number glassSaturation = this.m4rvwditawht((ModuleSetting.Number)new ModuleSetting.Number("Glass Saturation", Float.intBitsToFloat(1068037571), 0.0f, 2.0f, Float.intBitsToFloat(1008981770)).description("Multiplies backdrop color saturation through glass; 0 is grayscale and 1 is unchanged."));
    public final ModuleSetting.Number glassBrightness = this.m4rvwditawht((ModuleSetting.Number)new ModuleSetting.Number("Glass Brightness", Float.intBitsToFloat(1024416809), Float.intBitsToFloat(-1102263091), Float.intBitsToFloat(1045220557), Float.intBitsToFloat(1000593162)).description("Adds brightness to the scene sampled through glass; 0 leaves luminance unchanged."));
    public final ModuleSetting.Number glassContrast = this.m4rvwditawht((ModuleSetting.Number)new ModuleSetting.Number("Glass Contrast", Float.intBitsToFloat(1065940419), Float.intBitsToFloat(0x3F000000), 2.0f, Float.intBitsToFloat(1008981770)).description("Multiplies backdrop contrast through glass; 1 is neutral."));
    public final ModuleSetting.Number glassRefraction = this.m4rvwditawht((ModuleSetting.Number)new ModuleSetting.Number("Glass Refraction", Float.intBitsToFloat(1092616192), 0.0f, Float.intBitsToFloat(1099956224), Float.intBitsToFloat(1048576000)).description("Controls edge lensing and backdrop displacement in pixels; 0 removes refraction."));
    public final ModuleSetting.Number glassNoise = this.m4rvwditawht((ModuleSetting.Number)new ModuleSetting.Number("Glass Noise", Float.intBitsToFloat(992204554), 0.0f, Float.intBitsToFloat(1017370378), Float.intBitsToFloat(973279855)).description("Adds fine grain to glass panels to reduce banding; excessive values look visibly noisy."));
    public final ModuleSetting.Number glassChromatic = this.m4rvwditawht((ModuleSetting.Number)new ModuleSetting.Number("Glass Chromatic", Float.intBitsToFloat(0x3F666666), 0.0f, 2.0f, Float.intBitsToFloat(1028443341)).description("Controls subtle RGB separation along refracted glass edges; 0 keeps channels aligned."));
    public final ModuleSetting.Color glassHighlightColor = this.m4rvwditawht((ModuleSetting.Color)new ModuleSetting.Color("Glass Highlight Color", 0x38FFFFFF).description("Tints the glass rim, top sheen, and caustic highlights; alpha controls their strength."));

    private <S extends ModuleSetting<?>> S m4rvwditawht(S s) {
        this.fjo0w9iagbly.add(s);
        return s;
    }

    public List<ModuleSetting<?>> settings() {
        return Collections.unmodifiableList(this.fjo0w9iagbly);
    }

    public ModuleSetting<?> setting(String string) {
        for (ModuleSetting<?> moduleSetting : this.fjo0w9iagbly) {
            if (!moduleSetting.name().equals(string)) continue;
            return moduleSetting;
        }
        return null;
    }

    public float scaledFontSize() {
        return ((Float)this.fontSize.get()).floatValue() * ((Float)this.scale.get()).floatValue();
    }

    public TextMode fontVariant() {
        return switch ((String)this.font.get()) {
            case "Bold" -> TextMode.BOLD;
            case "Italic" -> TextMode.ITALIC;
            case "Bold Italic" -> TextMode.BOLD_ITALIC;
            default -> TextMode.REGULAR;
        };
    }

    public TextData textStyle() {
        TextData textData = TextData.NONE.withVariant(this.fontVariant());
        if (((Boolean)this.textShadow.get()).booleanValue()) {
            float f = ((Float)this.textShadowDistance.get()).floatValue() * ((Float)this.scale.get()).floatValue();
            textData = textData.withShadow(f, f, (Integer)this.textShadowColor.get());
        }
        if (((Float)this.textOutline.get()).floatValue() > 0.0f) {
            textData = textData.withOutline(((Float)this.textOutline.get()).floatValue(), (Integer)this.textOutlineColor.get());
        }
        return textData;
    }

    public RenderStyle renderStyle(float f) {
        String string = (String)this.design.get();
        float f2 = ((Float)this.scale.get()).floatValue();
        float f3 = ((Float)this.cornerRadius.get()).floatValue() * f2;
        float f4 = ((Float)this.blur.get()).floatValue();
        float f5 = ((Float)this.shadow.get()).floatValue() * f2;
        float f6 = ((Float)this.borderWidth.get()).floatValue() * f2;
        int n = (Integer)this.borderColor.get();
        boolean bl = (Boolean)this.background.get();
        switch (string) {
            case "Minimal": {
                bl = false;
                f4 = 0.0f;
                f5 = 0.0f;
                f6 = 0.0f;
                break;
            }
            case "Solid": {
                f4 = 0.0f;
                break;
            }
            case "Outline": {
                bl = false;
                f4 = 0.0f;
                f5 = 0.0f;
                f6 = Math.max(1.0f, f6);
                if (n >>> 24 != 0) break;
                n = 0x55FFFFFF;
                break;
            }
            case "Pill": {
                f3 = f * Float.intBitsToFloat(0x3F000000);
                break;
            }
        }
        return new RenderStyle(bl, f3, f4, f5, f6, n);
    }

    public void applyPreset(String string) {
        switch (string) {
            case "Solid": {
                this.design.set("Solid");
                this.glass.set(false);
                this.background.set(true);
                this.backgroundColor.set(-804122088);
                this.gradientEnd.set(0);
                this.blur.set(Float.valueOf(0.0f));
                this.shadow.set(Float.valueOf(Float.intBitsToFloat(0x40C00000)));
                this.borderWidth.set(Float.valueOf(0.0f));
                this.cornerRadius.set(Float.valueOf(Float.intBitsToFloat(0x40A00000)));
                this.textColor.set(-419430401);
                this.textShadow.set(false);
                break;
            }
            case "Outline": {
                this.design.set("Outline");
                this.glass.set(false);
                this.background.set(false);
                this.blur.set(Float.valueOf(0.0f));
                this.shadow.set(Float.valueOf(0.0f));
                this.borderWidth.set(Float.valueOf(1.0f));
                this.borderColor.set(-2130706433);
                this.secondaryBorderColor.set(0);
                this.cornerRadius.set(Float.valueOf(Float.intBitsToFloat(0x40A00000)));
                this.textColor.set(-419430401);
                this.textShadow.set(true);
                break;
            }
            case "Pill": {
                this.design.set("Pill");
                this.glass.set(true);
                this.background.set(true);
                this.backgroundColor.set(-1946157056);
                this.gradientEnd.set(540686933);
                this.blur.set(Float.valueOf(Float.intBitsToFloat(0x41400000)));
                this.shadow.set(Float.valueOf(Float.intBitsToFloat(0x40400000)));
                this.borderWidth.set(Float.valueOf(Float.intBitsToFloat(0x3F000000)));
                this.borderColor.set(0x28FFFFFF);
                this.secondaryBorderColor.set(0x14000000);
                this.cornerRadius.set(Float.valueOf(Float.intBitsToFloat(0x41400000)));
                this.paddingX.set(Float.valueOf(Float.intBitsToFloat(0x41000000)));
                this.paddingY.set(Float.valueOf(Float.intBitsToFloat(0x40400000)));
                this.textColor.set(-419430401);
                break;
            }
            case "Minimal": {
                this.design.set("Minimal");
                this.glass.set(false);
                this.background.set(false);
                this.blur.set(Float.valueOf(0.0f));
                this.shadow.set(Float.valueOf(0.0f));
                this.borderWidth.set(Float.valueOf(0.0f));
                this.textColor.set(-419430401);
                this.textShadow.set(true);
                this.textOutline.set(Float.valueOf(0.0f));
                break;
            }
            default: {
                this.design.set("Glass");
                this.glass.set(true);
                this.background.set(true);
                this.backgroundColor.set(1557387490);
                this.gradientEnd.set(1353231802);
                this.panelOpacity.set(Float.valueOf(1.0f));
                this.blur.set(Float.valueOf(Float.intBitsToFloat(1106247680)));
                this.shadow.set(Float.valueOf(Float.intBitsToFloat(1101004800)));
                this.shadowColor.set(Integer.MIN_VALUE);
                this.borderWidth.set(Float.valueOf(Float.intBitsToFloat(0x3F000000)));
                this.borderColor.set(0x60FFFFFF);
                this.secondaryBorderColor.set(0x28000000);
                this.cornerRadius.set(Float.valueOf(Float.intBitsToFloat(0x41000000)));
                this.edgeSoftness.set(Float.valueOf(0.0f));
                this.insetShadow.set(false);
                this.glassSaturation.set(Float.valueOf(Float.intBitsToFloat(1068037571)));
                this.glassBrightness.set(Float.valueOf(Float.intBitsToFloat(1024416809)));
                this.glassContrast.set(Float.valueOf(Float.intBitsToFloat(1065940419)));
                this.glassRefraction.set(Float.valueOf(Float.intBitsToFloat(1092616192)));
                this.glassNoise.set(Float.valueOf(Float.intBitsToFloat(992204554)));
                this.glassChromatic.set(Float.valueOf(Float.intBitsToFloat(0x3F666666)));
                this.glassHighlightColor.set(0x38FFFFFF);
                this.textColor.set(-434628574);
                this.textShadow.set(false);
                this.textOutline.set(Float.valueOf(0.0f));
            }
        }
    }

    public static int applyOpacity(int n, float f) {
        int n2 = n >>> 24 & 0xFF;
        n2 = Math.max(0, Math.min(255, Math.round((float)n2 * f)));
        return n2 << 24 | n & 0xFFFFFF;
    }

    public record RenderStyle(boolean drawFill, float radius, float blur, float shadow, float borderWidth, int borderColor) {
    }
}

