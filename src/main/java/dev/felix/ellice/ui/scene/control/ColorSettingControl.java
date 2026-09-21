



package dev.felix.ellice.ui.scene.control;

import java.util.Locale;
import java.awt.Color;
import dev.felix.ellice.ui.text.TextMode;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.scene.ScenePctService;
import java.util.Objects;
import java.util.function.Consumer;
import dev.felix.ellice.ui.scene.color.AlphaSliderControl;
import dev.felix.ellice.ui.scene.color.ColorGetAnimPropertyService;
import dev.felix.ellice.ui.scene.color.SaturationBrightnessPicker;
import dev.felix.ellice.ui.scene.SceneContentService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.ui.scene.LayoutContainerNode;

public final class ColorSettingControl extends LayoutContainerNode
{
    public static final float DEFAULT_WIDTH = 112.0f;
    public static final float TRIGGER_HEIGHT = 19.0f;
    public static final float FIELD_HEIGHT = 68.0f;
    public static final float BAR_HEIGHT = 9.0f;
    private static final int fguv031sgl86 = 572070949;
    private static final int felvcm5u92eq = 942288442;
    private static final int ffecuyll0a1w = 1244804932;
    private static final int f23bytaigz0l = -1458432996;
    private static final int ffqkvub2kffp = -805306369;
    private static final int fegm4fd5dtf3 = 1493172223;
    private final ModuleSetting.Color f9v9lww07hoc;
    private final SceneCornerRadiusService ffbqqd42sjr2;
    private final SceneTextService f1iwl8np6n9h;
    private final SceneCornerRadiusService f9gsn5psokm5;
    private final SceneContentService fhkayuexdwn2;
    private final AutoCloseable f9bxy0us314w;
    private SceneCornerRadiusService fbtk3d3kopb9;
    private SaturationBrightnessPicker fdx4w0jlrwyj;
    private ColorGetAnimPropertyService f8un4e9jh121;
    private AlphaSliderControl fiqx8s8qvcl9;
    private Consumer<Integer> fsrkr2z4mmk;
    private Runnable f12u51k49pdv;
    private int f3qd85ixvvix;
    private float f8wrjbrpke1d;
    private float f8u05j8skbfa;
    private float f8wcu9k7jr75;
    private float f7hjcfl397oy;
    private boolean f83h3r6js8m0;
    private boolean f5fgq10g09e4;
    private boolean fbzxnbfij97b;
    private boolean fdehh37k9utp;
    private boolean fh2h2qhhrl7l;
    private boolean fam58u1v3rzh;
    
    public ColorSettingControl(final ModuleSetting.Color obj) {
        this.f83h3r6js8m0 = true;
        this.f5fgq10g09e4 = true;
        this.f9v9lww07hoc = Objects.requireNonNull(obj, "setting");
        final Integer n = obj.get();
        this.mbj6zpxdxsgt(this.f3qd85ixvvix = ((n != null) ? n : -1));
        this.direction(Direction.COLUMN);
        this.align(Align.STRETCH);
        this.justify(Justify.START);
        this.gap(Float.intBitsToFloat(1077936128));
        this.size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.auto());
        this.flexShrink(0.0f);
        this.ffbqqd42sjr2 = new SceneCornerRadiusService() {
            @Override
            protected void onPress(final float n, final float n2) {
                ColorSettingControl.this.mchtuz34mvok();
            }
        };
        this.ffbqqd42sjr2.size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.px(Float.intBitsToFloat(1100480512))).flexShrink(0.0f).cornerRadius(Float.intBitsToFloat(1084227584)).backgroundColor(572070949).hoverBackground(942288442).pressBackground(1244804932).gradientEnd(0).glass(false).blur(0.0f).direction(Direction.ROW).align(Align.CENTER).justify(Justify.SPACE_BETWEEN).padding(0.0f, Float.intBitsToFloat(1084227584)).gap(Float.intBitsToFloat(1084227584)).cursorStyle(CursorStyle.POINTER).onClick(this::toggle);
        this.f1iwl8np6n9h = new SceneTextService(m44pmmsma4ma(this.f3qd85ixvvix), Float.intBitsToFloat(1088421888), -805306369).fontVariant(TextMode.REGULAR).overflow(SceneTextService.Overflow.ELLIPSIS).inheritEdgeSoftness(false).flex(1.0f);
        this.f9gsn5psokm5 = new SceneCornerRadiusService().size(Float.intBitsToFloat(1103101952), Float.intBitsToFloat(1093664768)).flexShrink(0.0f).cornerRadius(Float.intBitsToFloat(1077936128)).backgroundColor(this.f3qd85ixvvix).gradientEnd(0).glass(false).blur(0.0f).border(Float.intBitsToFloat(1061158912), 1493172223).pointerEvents(false);
        this.ffbqqd42sjr2.addChild(this.f1iwl8np6n9h);
        this.ffbqqd42sjr2.addChild(this.f9gsn5psokm5);
        (this.fhkayuexdwn2 = new SceneContentService()).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
        this.addChild(this.ffbqqd42sjr2);
        this.addChild(this.fhkayuexdwn2);
        this.refreshState();
        this.f9bxy0us314w = obj.onStateChanged(this::refreshState);
    }
    
    public ModuleSetting.Color setting() {
        return this.f9v9lww07hoc;
    }
    
    public int value() {
        return this.f3qd85ixvvix;
    }
    
    public boolean isExpanded() {
        return this.fhkayuexdwn2.isExpanded();
    }
    
    public boolean dependencyVisible() {
        return this.fbzxnbfij97b;
    }
    
    public boolean dependencyActive() {
        return this.fdehh37k9utp;
    }
    
    public boolean available() {
        return this.fbzxnbfij97b && this.fdehh37k9utp && this.f83h3r6js8m0 && this.f5fgq10g09e4;
    }
    
    @Override
    public ColorSettingControl visible(final boolean f83h3r6js8m0) {
        this.f83h3r6js8m0 = f83h3r6js8m0;
        this.refreshState();
        return this;
    }
    
    public ColorSettingControl enabled(final boolean f5fgq10g09e4) {
        this.f5fgq10g09e4 = f5fgq10g09e4;
        this.refreshState();
        return this;
    }
    
    @Override
    public ColorSettingControl id(final String s) {
        super.id(s);
        this.m4glshw5edmw();
        return this;
    }
    
    public ColorSettingControl onChange(final Consumer<Integer> fsrkr2z4mmk) {
        this.fsrkr2z4mmk = fsrkr2z4mmk;
        return this;
    }
    
    public ColorSettingControl onInteraction(final Runnable f12u51k49pdv) {
        this.f12u51k49pdv = f12u51k49pdv;
        return this;
    }
    
    public ColorSettingControl toggle() {
        return this.setExpanded(!this.isExpanded(), true);
    }
    
    public ColorSettingControl setExpanded(final boolean b) {
        return this.setExpanded(b, true);
    }
    
    public ColorSettingControl setExpanded(final boolean b, final boolean b2) {
        if (this.fam58u1v3rzh || (b && !this.available())) {
            return this;
        }
        if (b) {
            this.mdmn8aydbdt1();
        }
        this.fhkayuexdwn2.setExpanded(b, b2);
        return this;
    }
    
    public boolean trySetColor(final int i) {
        this.refreshState();
        if (!this.available() || this.fam58u1v3rzh) {
            return false;
        }
        final Integer n = this.f9v9lww07hoc.get();
        if (n == null) {
            return false;
        }
        final int intValue = n;
        this.f9v9lww07hoc.set(i);
        final Integer n2 = this.f9v9lww07hoc.get();
        if (n2 == null) {
            this.mjmmuxhtycv5(intValue, true);
            return false;
        }
        final int intValue2 = n2;
        this.mjmmuxhtycv5(intValue2, true);
        final boolean b = intValue != intValue2;
        if (b && this.fsrkr2z4mmk != null) {
            this.fsrkr2z4mmk.accept(intValue2);
        }
        return b;
    }
    
    public void refreshState() {
        if (this.fam58u1v3rzh) {
            return;
        }
        this.fbzxnbfij97b = this.f9v9lww07hoc.isVisible();
        this.fdehh37k9utp = this.f9v9lww07hoc.isActive();
        final boolean available = this.available();
        if (this.visible != available) {
            this.visible = available;
            this.invalidateLayout();
        }
        if (this.pointerEvents != available) {
            this.pointerEvents = available;
            this.invalidate();
        }
        if (this.interactive != available) {
            this.interactive = available;
            this.invalidate();
        }
        this.cursorStyle(available ? CursorStyle.POINTER : CursorStyle.DEFAULT);
        if (!available && this.fhkayuexdwn2.isExpanded()) {
            this.fhkayuexdwn2.setExpanded(false, false);
        }
        final Integer n = this.f9v9lww07hoc.get();
        if (n != null) {
            this.mjmmuxhtycv5(n, true);
        }
    }
    
    @Override
    protected boolean handleScroll(final float n) {
        return false;
    }
    
    @Override
    protected void onDetached() {
        this.dispose();
    }
    
    public void dispose() {
        if (this.fam58u1v3rzh) {
            return;
        }
        this.fam58u1v3rzh = true;
        this.fh2h2qhhrl7l = false;
        this.fhkayuexdwn2.setExpanded(false, false);
        this.visible = false;
        this.pointerEvents = false;
        this.interactive = false;
        this.fsrkr2z4mmk = null;
        this.f12u51k49pdv = null;
        try {
            this.f9bxy0us314w.close();
        }
        catch (final Exception ex) {}
    }
    
    private void mdmn8aydbdt1() {
        if (this.fbtk3d3kopb9 != null) {
            return;
        }
        this.fbtk3d3kopb9 = new SceneCornerRadiusService().size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.auto()).flexShrink(0.0f).cornerRadius(Float.intBitsToFloat(1086324736)).backgroundColor(-1458432996).gradientEnd(0).glass(false).blur(0.0f).border(0.0f, 0).padding(Float.intBitsToFloat(1084227584)).gap(Float.intBitsToFloat(1082130432)).direction(Direction.COLUMN).align(Align.STRETCH).justify(Justify.START).clip(true);
        this.fdx4w0jlrwyj = new SaturationBrightnessPicker() {
            @Override
            protected void onPress(final float n, final float n2) {
                ColorSettingControl.this.mchtuz34mvok();
            }
        };
        this.fdx4w0jlrwyj.hue(this.f8wrjbrpke1d).sat(this.f8u05j8skbfa).bri(this.f8wcu9k7jr75).cornerRadius(Float.intBitsToFloat(1084227584)).size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.px(Float.intBitsToFloat(1116209152))).flexShrink(0.0f).cursorStyle(CursorStyle.POINTER).onChange((n, n2) -> {
            this.f8u05j8skbfa = n;
            this.f8wcu9k7jr75 = n2;
            this.m3uoyqecl6qu();
            return;
        }).onCommit((p0, p1) -> this.m3p1jwcpe7tc());
        this.f8un4e9jh121 = new ColorGetAnimPropertyService() {
            @Override
            protected void onPress(final float n, final float n2) {
                ColorSettingControl.this.mchtuz34mvok();
            }
        };
        this.f8un4e9jh121.hue(this.f8wrjbrpke1d).cornerRadius(Float.intBitsToFloat(1082130432)).size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.px(Float.intBitsToFloat(1091567616))).flexShrink(0.0f).cursorStyle(CursorStyle.POINTER).onChange(n3 -> {
            this.f8wrjbrpke1d = n3;
            if (this.fdx4w0jlrwyj != null) {
                this.fdx4w0jlrwyj.hue(this.f8wrjbrpke1d);
            }
            this.m3uoyqecl6qu();
            return;
        }).onCommit(p0 -> this.m3p1jwcpe7tc());
        this.fiqx8s8qvcl9 = new AlphaSliderControl() {
            @Override
            protected void onPress(final float n, final float n2) {
                ColorSettingControl.this.mchtuz34mvok();
            }
        };
        this.fiqx8s8qvcl9.hue(this.f8wrjbrpke1d).sat(this.f8u05j8skbfa).bri(this.f8wcu9k7jr75).alpha(this.f7hjcfl397oy).cornerRadius(Float.intBitsToFloat(1082130432)).size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.px(Float.intBitsToFloat(1091567616))).flexShrink(0.0f).cursorStyle(CursorStyle.POINTER).onChange(n4 -> {
            this.f7hjcfl397oy = n4;
            this.m3uoyqecl6qu();
            return;
        }).onCommit(p0 -> this.m3p1jwcpe7tc());
        this.fbtk3d3kopb9.addChild(this.fdx4w0jlrwyj);
        this.fbtk3d3kopb9.addChild(this.f8un4e9jh121);
        this.fbtk3d3kopb9.addChild(this.fiqx8s8qvcl9);
        this.fhkayuexdwn2.content(this.fbtk3d3kopb9);
        this.m4glshw5edmw();
    }
    
    private void m3uoyqecl6qu() {
        final int f3qd85ixvvix = Math.round(this.f7hjcfl397oy * Float.intBitsToFloat(1132396544)) << 24 | (Color.HSBtoRGB(this.f8wrjbrpke1d, this.f8u05j8skbfa, this.f8wcu9k7jr75) & 0xFFFFFF);
        if (this.f3qd85ixvvix == f3qd85ixvvix) {
            return;
        }
        this.f3qd85ixvvix = f3qd85ixvvix;
        final Integer n = this.f9v9lww07hoc.get();
        this.fh2h2qhhrl7l = (n == null || n != this.f3qd85ixvvix);
        this.f9gsn5psokm5.backgroundColor(this.f3qd85ixvvix);
        this.f9gsn5psokm5.invalidate();
        if (this.fiqx8s8qvcl9 != null) {
            this.fiqx8s8qvcl9.hue(this.f8wrjbrpke1d).sat(this.f8u05j8skbfa).bri(this.f8wcu9k7jr75).alpha(this.f7hjcfl397oy);
        }
    }
    
    private void m3p1jwcpe7tc() {
        if (!this.fh2h2qhhrl7l) {
            return;
        }
        final int f3qd85ixvvix = this.f3qd85ixvvix;
        this.fh2h2qhhrl7l = false;
        if (!this.available() || this.fam58u1v3rzh) {
            final Integer n = this.f9v9lww07hoc.get();
            if (n != null) {
                this.mjmmuxhtycv5(n, true);
            }
            return;
        }
        final Integer n2 = this.f9v9lww07hoc.get();
        if (n2 == null) {
            return;
        }
        final int intValue = n2;
        this.f9v9lww07hoc.set(f3qd85ixvvix);
        final Integer n3 = this.f9v9lww07hoc.get();
        if (n3 == null) {
            this.mjmmuxhtycv5(intValue, true);
            return;
        }
        final int intValue2 = n3;
        this.mjmmuxhtycv5(intValue2, true);
        if (intValue != intValue2 && this.fsrkr2z4mmk != null) {
            this.fsrkr2z4mmk.accept(intValue2);
        }
    }
    
    private void mjmmuxhtycv5(final int f3qd85ixvvix, final boolean b) {
        this.f3qd85ixvvix = f3qd85ixvvix;
        this.fh2h2qhhrl7l = false;
        this.mbj6zpxdxsgt(f3qd85ixvvix);
        this.f9gsn5psokm5.backgroundColor(f3qd85ixvvix);
        this.f9gsn5psokm5.invalidate();
        if (b) {
            this.f1iwl8np6n9h.text(m44pmmsma4ma(f3qd85ixvvix));
        }
        if (this.fdx4w0jlrwyj != null) {
            this.fdx4w0jlrwyj.hue(this.f8wrjbrpke1d).sat(this.f8u05j8skbfa).bri(this.f8wcu9k7jr75);
        }
        if (this.f8un4e9jh121 != null) {
            this.f8un4e9jh121.hue(this.f8wrjbrpke1d);
        }
        if (this.fiqx8s8qvcl9 != null) {
            this.fiqx8s8qvcl9.hue(this.f8wrjbrpke1d).sat(this.f8u05j8skbfa).bri(this.f8wcu9k7jr75).alpha(this.f7hjcfl397oy);
        }
    }
    
    private void mbj6zpxdxsgt(final int n) {
        this.f7hjcfl397oy = (n >>> 24 & 0xFF) / Float.intBitsToFloat(1132396544);
        final float[] rgBtoHSB = Color.RGBtoHSB(n >>> 16 & 0xFF, n >>> 8 & 0xFF, n & 0xFF, null);
        this.f8wrjbrpke1d = rgBtoHSB[0];
        this.f8u05j8skbfa = rgBtoHSB[1];
        this.f8wcu9k7jr75 = rgBtoHSB[2];
    }
    
    private void mchtuz34mvok() {
        if (!this.fam58u1v3rzh && this.available() && this.f12u51k49pdv != null) {
            this.f12u51k49pdv.run();
        }
    }
    
    private void m4glshw5edmw() {
        final String id = this.getId();
        if (id == null || id.isBlank()) {
            return;
        }
        this.ffbqqd42sjr2.id(id + ".trigger");
        this.f1iwl8np6n9h.id(id + ".value");
        this.f9gsn5psokm5.id(id + ".swatch");
        this.fhkayuexdwn2.id(id + ".reveal");
        if (this.fbtk3d3kopb9 != null) {
            this.fbtk3d3kopb9.id(id + ".editor");
        }
        if (this.fdx4w0jlrwyj != null) {
            this.fdx4w0jlrwyj.id(id + ".field");
        }
        if (this.f8un4e9jh121 != null) {
            this.f8un4e9jh121.id(id + ".hue");
        }
        if (this.fiqx8s8qvcl9 != null) {
            this.fiqx8s8qvcl9.id(id + ".alpha");
        }
    }
    
    private static String m44pmmsma4ma(final int i) {
        return String.format(Locale.ROOT, "#%08X", i);
    }
}
