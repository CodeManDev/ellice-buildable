


package dev.felix.ellice.feature.terrain;

import dev.felix.ellice.feature.terrain.TerrainKindData;
import dev.felix.ellice.feature.terrain.TerrainRenderer;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;
import dev.felix.ellice.ui.text.TextMode;
import dev.felix.ellice.ui.text.TextTextureService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public final class TerrainReservedService
extends ScenePctService<TerrainReservedService> {
    private final HashMap<UUID, PinMotion> fe15coeydrin = new HashMap();
    private final ArrayList<Painted> fe0wsm82vlqe = new ArrayList();
    private List<Anchor> f6ox4bit49uk = List.of();
    private List<Box> f5q39s3upgmy = List.of();
    private View f9ksrcgex5f4 = View.MAP;
    private UUID fh3yo1e18xxy;
    private boolean fez15cuzacwa;

    public TerrainReservedService() {
        ((TerrainReservedService)((TerrainReservedService)this.absolute()).size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).pointerEvents(false);
    }

    public TerrainReservedService reserved(List<Box> list) {
        this.f5q39s3upgmy = List.copyOf(list);
        return this;
    }

    public void present(List<Anchor> list, View view, UUID uUID, boolean bl) {
        this.f6ox4bit49uk = list;
        this.f9ksrcgex5f4 = view;
        this.fh3yo1e18xxy = uUID;
        this.fez15cuzacwa = bl;
        HashSet<UUID> hashSet = new HashSet<UUID>();
        for (Anchor anchor : list) {
            UUID uUID2 = anchor.point.id();
            hashSet.add(uUID2);
            PinMotion pinMotion = this.fe15coeydrin.get(uUID2);
            if (pinMotion == null) {
                pinMotion = new PinMotion();
                this.fe15coeydrin.put(uUID2, pinMotion);
                this.addChild(pinMotion);
            }
            pinMotion.target(anchor.selected || anchor.navigating, uUID2.equals(uUID));
        }
        this.fe15coeydrin.entrySet().removeIf(entry -> {
            if (hashSet.contains(entry.getKey())) {
                return false;
            }
            this.removeChild((ScenePctService)entry.getValue());
            return true;
        });
    }

    public TerrainKindData hit(float f, float f2) {
        for (int i = this.fe0wsm82vlqe.size() - 1; i >= 0; i += -1) {
            Painted painted = this.fe0wsm82vlqe.get(i);
            Box box = new Box(painted.pin.x - Float.intBitsToFloat(0x40A00000), painted.pin.y - Float.intBitsToFloat(0x40800000), painted.pin.width + Float.intBitsToFloat(1092616192), painted.pin.height + Float.intBitsToFloat(1096810496));
            if (!box.contains(f, f2) && (!painted.interactiveTag || painted.tag == null || !painted.tag.contains(f, f2))) continue;
            return painted.point;
        }
        return null;
    }

    @Override
    protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
        compositorPushPresentationScaleService.requestFontFamily("material-roboto", TextTextureService.FontSource.CLASSPATH, "/assets/ellice/fonts/material/Roboto-Regular.ttf");
        compositorPushPresentationScaleService.requestFontFamily("material-roboto-medium", TextTextureService.FontSource.CLASSPATH, "/assets/ellice/fonts/material/Roboto-Medium.ttf");
        TerrainRenderer.ready(compositorPushPresentationScaleService);
        this.fe0wsm82vlqe.clear();
        if (this.cw < Float.intBitsToFloat(0x42200000) || this.ch < Float.intBitsToFloat(0x42200000)) {
            return;
        }
        ArrayList<Box> arrayList = new ArrayList<Box>();
        ArrayList<Layout> arrayList2 = new ArrayList<Layout>();
        List<Anchor> list = this.f6ox4bit49uk.stream().sorted(Comparator.comparingInt(anchor -> anchor.selected ? 0 : (anchor.navigating ? 1 : (anchor.point.id().equals(this.fh3yo1e18xxy) ? 2 : 3)))).toList();
        for (Anchor anchor2 : list) {
            int n;
            Box box;
            String string;
            String string2;
            boolean bl;
            boolean bl2;
            Box box2;
            PinMotion pinMotion;
            block10: {
                block12: {
                    block11: {
                        pinMotion = this.fe15coeydrin.get(anchor2.point.id());
                        float f2 = this.f9ksrcgex5f4 == View.MINI ? Float.intBitsToFloat(1103101952) : Float.intBitsToFloat(1107820544) + Float.intBitsToFloat(0x40C00000) * pinMotion.f756veubifow + 2.0f * pinMotion.ffrdd4e1aavl;
                        float f3 = (float)(this.fez15cuzacwa && anchor2.selected ? 7 : 0) + Float.intBitsToFloat(0x40800000) * (1.0f - pinMotion.f8kvkdf1zttn);
                        float f4 = this.f9ksrcgex5f4 == View.MINI ? Float.intBitsToFloat(0x40A00000) : Float.intBitsToFloat(0x40E00000);
                        box2 = new Box(anchor2.x - f2 / 2.0f, anchor2.y - f2 - f4 - f3, f2, f2);
                        if (box2.x + box2.width < 0.0f || box2.x > this.cw || box2.y + box2.height < 0.0f || box2.y > this.ch) continue;
                        bl2 = this.f9ksrcgex5f4 != View.MINI && (double)pinMotion.fbmerw9jk44d > Double.longBitsToDouble(4572414629676717179L);
                        bl = this.f9ksrcgex5f4 == View.MAP && anchor2.point.id().equals(this.fh3yo1e18xxy) && !this.fez15cuzacwa;
                        string2 = TerrainReservedService.distance(anchor2.distance) + "  \u00b7  " + (anchor2.navigating ? "Navigating" : anchor2.point.kind().label());
                        string = TerrainReservedService.ma6wxitlrefq(compositorPushPresentationScaleService, anchor2.point.name(), bl2 ? Float.intBitsToFloat(1095761920) : Float.intBitsToFloat(1093664768), bl2 ? Float.intBitsToFloat(0x43400000) : Float.intBitsToFloat(1123549184), "material-roboto-medium");
                        box = null;
                        n = 0;
                        if (this.f9ksrcgex5f4 == View.MINI) break block10;
                        float f5 = bl2 ? Math.max(compositorPushPresentationScaleService.textWidth(string, Float.intBitsToFloat(1095761920), TextMode.REGULAR, "material-roboto-medium"), compositorPushPresentationScaleService.textWidth(string2, Float.intBitsToFloat(1092616192), TextMode.REGULAR, "material-roboto")) + Float.intBitsToFloat(1105199104) : compositorPushPresentationScaleService.textWidth(string, Float.intBitsToFloat(1093664768), TextMode.REGULAR, "material-roboto-medium") + Float.intBitsToFloat(1101004800);
                        f5 = Math.min(this.cw - Float.intBitsToFloat(1101004800), Math.max(bl2 ? Float.intBitsToFloat(0x43040000) : Float.intBitsToFloat(0x42400000), f5));
                        float tooltipHeight = bl2 ? (bl ? Float.intBitsToFloat(1116471296) : Float.intBitsToFloat(1112539136)) : Float.intBitsToFloat(1104150528);
                        if (bl2) {
                            box = TerrainReservedService.tooltipBox(anchor2.x, box2.y, box2.y + f2 + f4, f5, tooltipHeight, this.cw, this.ch);
                            box = this.m1858zckjo6l(box, box2, f4);
                            n = box.y + box.height > box2.y || anchor2.x < box.x + Float.intBitsToFloat(1099956224) || anchor2.x > box.x + box.width - Float.intBitsToFloat(1099956224) ? 1 : 0;
                        } else {
                            box = new Box(Math.clamp(anchor2.x - f5 / 2.0f, Float.intBitsToFloat(1092616192), Math.max(Float.intBitsToFloat(1092616192), this.cw - f5 - Float.intBitsToFloat(1092616192))), box2.y - tooltipHeight - Float.intBitsToFloat(0x40E00000), f5, tooltipHeight);
                        }
                        if (box.y < Float.intBitsToFloat(0x41000000)) break block11;
                        if (arrayList.stream().anyMatch(box::overlaps)) break block11;
                        if (!this.f5q39s3upgmy.stream().anyMatch(box::overlaps)) break block12;
                    }
                    if (!bl2) {
                        box = null;
                    }
                }
                if (box != null) {
                    arrayList.add(box);
                }
            }
            arrayList2.add(new Layout(anchor2, pinMotion, box2, box, string, string2, bl2, n != 0, bl));
        }
        for (int i = arrayList2.size() - 1; i >= 0; i += -1) {
            this.ma97h74gkzwa(compositorPushPresentationScaleService, (Layout)arrayList2.get(i));
        }
    }

    private Box m1858zckjo6l(Box box, Box box2, float f) {
        if (this.f5q39s3upgmy.stream().noneMatch(box::overlaps)) {
            return box;
        }
        Box box3 = box;
        float f2 = this.mivt6r94eufv(box3);
        for (Box box4 : List.of(new Box(box.x, box2.y + box2.height + f + Float.intBitsToFloat(0x41400000), box.width, box.height), new Box(box2.x - box.width - Float.intBitsToFloat(0x41400000), box2.y + (box2.height - box.height) / 2.0f, box.width, box.height), new Box(box2.x + box2.width + Float.intBitsToFloat(0x41400000), box2.y + (box2.height - box.height) / 2.0f, box.width, box.height))) {
            Box box5 = new Box(Math.clamp(box4.x, Float.intBitsToFloat(1092616192), Math.max(Float.intBitsToFloat(1092616192), this.cw - box4.width - Float.intBitsToFloat(1092616192))), Math.clamp(box4.y, Float.intBitsToFloat(0x41000000), Math.max(Float.intBitsToFloat(0x41000000), this.ch - box4.height - Float.intBitsToFloat(0x41000000))), box4.width, box4.height);
            float f3 = this.mivt6r94eufv(box5);
            if (f3 < f2) {
                box3 = box5;
                f2 = f3;
            }
            if (f3 != 0.0f) continue;
            break;
        }
        return box3;
    }

    private float mivt6r94eufv(Box box) {
        float f = 0.0f;
        for (Box box2 : this.f5q39s3upgmy) {
            f += Math.max(0.0f, Math.min(box.x + box.width, box2.x + box2.width + Float.intBitsToFloat(0x40A00000)) - Math.max(box.x, box2.x - Float.intBitsToFloat(0x40A00000))) * Math.max(0.0f, Math.min(box.y + box.height, box2.y + box2.height + Float.intBitsToFloat(0x40A00000)) - Math.max(box.y, box2.y - Float.intBitsToFloat(0x40A00000)));
        }
        return f;
    }

    public static Box tooltipBox(float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        f4 = Math.min(f4, Math.max(0.0f, f6 - Float.intBitsToFloat(1101004800)));
        float f8 = Math.clamp(f - f4 / 2.0f, Float.intBitsToFloat(1092616192), Math.max(Float.intBitsToFloat(1092616192), f6 - f4 - Float.intBitsToFloat(1092616192)));
        float f9 = f2 >= f5 + Float.intBitsToFloat(1099956224) ? f2 - f5 - Float.intBitsToFloat(0x41400000) : f3 + Float.intBitsToFloat(0x41400000);
        f9 = Math.clamp(f9, Float.intBitsToFloat(0x41000000), Math.max(Float.intBitsToFloat(0x41000000), f7 - f5 - Float.intBitsToFloat(0x41000000)));
        return new Box(f8, f9, f4, f5);
    }

    private void ma97h74gkzwa(CompositorPushPresentationScaleService compositorPushPresentationScaleService, Layout layout) {
        Anchor anchor = layout.anchor;
        PinMotion pinMotion = layout.motion;
        Box box = layout.pin;
        float f = this.effectiveOpacity * pinMotion.f8kvkdf1zttn;
        int n = MaterialIsLightService.layer(anchor.point.color(), MaterialIsLightService.PRIMARY, Float.intBitsToFloat(1048576000));
        int n2 = MaterialIsLightService.layer(MaterialIsLightService.SURFACE_HIGH, n, Math.clamp(pinMotion.f756veubifow, 0.0f, 1.0f));
        int n3 = MaterialIsLightService.layer(n, MaterialIsLightService.ON_PRIMARY, Math.clamp(pinMotion.f756veubifow, 0.0f, 1.0f));
        float f2 = this.f9ksrcgex5f4 == View.MINI ? Float.intBitsToFloat(0x40A00000) : Float.intBitsToFloat(0x40E00000);
        compositorPushPresentationScaleService.roundedRect(this.cx + anchor.x - Float.intBitsToFloat(0x40A00000), this.cy + anchor.y - 2.0f, Float.intBitsToFloat(1092616192), Float.intBitsToFloat(0x40800000), 2.0f, TerrainReservedService.mulAlpha(0x45000000, f));
        compositorPushPresentationScaleService.addTooltipBubble(this.cx + box.x, this.cy + box.y, box.width, box.height, box.width * Float.intBitsToFloat(1052938076), box.width / 2.0f, f2 * Float.intBitsToFloat(1072064102), f2, Float.intBitsToFloat(0x40400000), n2, 0, this.f9ksrcgex5f4 == View.MINI ? Float.intBitsToFloat(0x40400000) : Float.intBitsToFloat(0x40C00000), 0x55000000, Float.intBitsToFloat(0x3F333333), MaterialIsLightService.layer(-9344134, n, pinMotion.f756veubifow), 0.0f, 0.0f, false, f, Float.intBitsToFloat(1059481190));
        float f3 = box.width * Float.intBitsToFloat(1057803469);
        TerrainRenderer.draw(compositorPushPresentationScaleService, TerrainRenderer.symbol(anchor.direction.isEmpty() ? anchor.point.kind().icon() : anchor.direction), this.cx + box.x + (box.width - f3) / 2.0f, this.cy + box.y + (box.height - f3) / 2.0f, f3, TerrainReservedService.mulAlpha(n3, f));
        if (layout.tag != null) {
            Box box2 = layout.tag;
            float f4 = layout.tooltip ? pinMotion.fbmerw9jk44d : 1.0f - pinMotion.fbmerw9jk44d;
            float f5 = f * Math.clamp(f4, 0.0f, 1.0f);
            float f6 = this.cy + box2.y + (layout.tooltip ? (1.0f - pinMotion.fbmerw9jk44d) * Float.intBitsToFloat(0x40800000) : 0.0f);
            compositorPushPresentationScaleService.nextLayer();
            if (layout.tooltip && !layout.below) {
                compositorPushPresentationScaleService.addTooltipBubble(this.cx + box2.x, f6, box2.width, box2.height, Float.intBitsToFloat(1096810496), Math.clamp(anchor.x - box2.x, Float.intBitsToFloat(1099956224), box2.width - Float.intBitsToFloat(1099956224)), Float.intBitsToFloat(0x41400000), Float.intBitsToFloat(0x40A00000), 2.0f, MaterialIsLightService.SURFACE_CONTAINER, 0, Float.intBitsToFloat(0x41000000), 0x50000000, Float.intBitsToFloat(1059481190), MaterialIsLightService.OUTLINE_VARIANT, 0.0f, 0.0f, false, f5, Float.intBitsToFloat(1059481190));
            } else {
                compositorPushPresentationScaleService.roundedRect(this.cx + box2.x, f6, box2.width, box2.height, layout.tooltip ? Float.intBitsToFloat(1096810496) : Float.intBitsToFloat(0x41100000), TerrainReservedService.mulAlpha(MaterialIsLightService.SURFACE_CONTAINER, f5), 0.0f, layout.tooltip ? Float.intBitsToFloat(0x40E00000) : Float.intBitsToFloat(0x40400000));
            }
            compositorPushPresentationScaleService.pushClip(this.cx + box2.x + Float.intBitsToFloat(1092616192), f6, box2.width - Float.intBitsToFloat(1101004800), box2.height);
            compositorPushPresentationScaleService.text(this.cx + box2.x + (float)(layout.tooltip ? 14 : 10), f6 + (float)(layout.tooltip ? 11 : 7), layout.title, layout.tooltip ? Float.intBitsToFloat(1095761920) : Float.intBitsToFloat(1093664768), TerrainReservedService.mulAlpha(MaterialIsLightService.ON_SURFACE, f5), MaterialIsLightService.LABEL);
            if (layout.tooltip) {
                compositorPushPresentationScaleService.text(this.cx + box2.x + Float.intBitsToFloat(1096810496), f6 + Float.intBitsToFloat(1106771968), layout.detail, Float.intBitsToFloat(1092616192), TerrainReservedService.mulAlpha(anchor.navigating ? MaterialIsLightService.PRIMARY : MaterialIsLightService.ON_SURFACE_VARIANT, f5), MaterialIsLightService.BODY);
                if (layout.hint) {
                    compositorPushPresentationScaleService.text(this.cx + box2.x + Float.intBitsToFloat(1096810496), f6 + Float.intBitsToFloat(0x42440000), "Drag to move \u00b7 Click to edit", Float.intBitsToFloat(0x41100000), TerrainReservedService.mulAlpha(MaterialIsLightService.ON_SURFACE_VARIANT, f5 * Float.intBitsToFloat(1061997773)), MaterialIsLightService.BODY);
                }
            }
            compositorPushPresentationScaleService.popClip();
        }
        this.fe0wsm82vlqe.add(new Painted(anchor.point, box, layout.tag, layout.tooltip));
    }

    public static String distance(double d) {
        return d >= Double.longBitsToDouble(4652007308841189376L) ? String.format(Locale.ROOT, "%.1fk blocks", d / Double.longBitsToDouble(4652007308841189376L)) : String.format(Locale.ROOT, "%.0f blocks", d);
    }

    private static String ma6wxitlrefq(CompositorPushPresentationScaleService compositorPushPresentationScaleService, String string, float f, float f2, String string2) {
        if (compositorPushPresentationScaleService.textWidth(string, f, TextMode.REGULAR, string2) <= f2) {
            return string;
        }
        int n = string.length();
        while (n > 0) {
            String string3 = string.substring(0, n = string.offsetByCodePoints(n, -1)) + "\u2026";
            if (!(compositorPushPresentationScaleService.textWidth(string3, f, TextMode.REGULAR, string2) <= f2)) continue;
            return string3;
        }
        return "\u2026";
    }

    public static enum View {
        MAP,
        MINI,
        WORLD;

    }

    public record Anchor(TerrainKindData point, float x, float y, double distance, boolean selected, boolean navigating, String direction) {
        public Anchor(TerrainKindData terrainKindData, float f, float f2, double d, boolean bl, boolean bl2) {
            this(terrainKindData, f, f2, d, bl, bl2, "");
        }
    }

    private static final class PinMotion
    extends ScenePctService<PinMotion> {
        private static final MotionFiniteService ffl997ww6rit = MotionFiniteService.finite("pinReveal", scenePctService -> scenePctService instanceof PinMotion);
        private static final MotionFiniteService fd1op9qg6rsu = MotionFiniteService.finite("pinSelection", scenePctService -> scenePctService instanceof PinMotion);
        private static final MotionFiniteService fc4pnwll1l37 = MotionFiniteService.finite("pinHover", scenePctService -> scenePctService instanceof PinMotion);
        private static final MotionFiniteService f5k07kl98dqw = MotionFiniteService.finite("pinDetail", scenePctService -> scenePctService instanceof PinMotion);
        private float f8kvkdf1zttn;
        private float f756veubifow;
        private float ffrdd4e1aavl;
        private float fbmerw9jk44d;
        private boolean ffo3yyj8mlgy;
        private boolean fcuop1e6aflc;
        private boolean f5npnpj7r71r;

        @Override
        protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
        }

        PinMotion() {
            ((PinMotion)((PinMotion)this.absolute()).size(0.0f, 0.0f)).pointerEvents(false);
            MotionAnimateService.animate(this, ffl997ww6rit, 1.0f, (SceneEaseHandler)SceneEaseHandler.Tween.ease(Float.intBitsToFloat(1043878380)));
        }

        void target(boolean bl, boolean bl2) {
            int n;
            if (this.ffo3yyj8mlgy != bl) {
                this.ffo3yyj8mlgy = bl;
                MotionAnimateService.animate(this, fd1op9qg6rsu, bl ? 1.0f : 0.0f, (SceneEaseHandler)MaterialIsLightService.EFFECTS);
            }
            if (this.fcuop1e6aflc != bl2) {
                this.fcuop1e6aflc = bl2;
                MotionAnimateService.animate(this, fc4pnwll1l37, bl2 ? 1.0f : 0.0f, (SceneEaseHandler)MaterialIsLightService.EFFECTS);
            }
            boolean active = bl || bl2;
            if (this.f5npnpj7r71r != active) {
                this.f5npnpj7r71r = active;
                MotionAnimateService.animate(this, f5k07kl98dqw, active ? 1.0f : 0.0f, (SceneEaseHandler)SceneEaseHandler.Tween.ease(Float.intBitsToFloat(1042536202)));
            }
        }

        @Override
        public float getAnimProperty(String string) {
            return switch (string) {
                case "pinReveal" -> this.f8kvkdf1zttn;
                case "pinSelection" -> this.f756veubifow;
                case "pinHover" -> this.ffrdd4e1aavl;
                case "pinDetail" -> this.fbmerw9jk44d;
                default -> super.getAnimProperty(string);
            };
        }

        @Override
        public void setAnimProperty(String string, float f) {
            switch (string) {
                case "pinReveal": {
                    this.f8kvkdf1zttn = f;
                    break;
                }
                case "pinSelection": {
                    this.f756veubifow = f;
                    break;
                }
                case "pinHover": {
                    this.ffrdd4e1aavl = f;
                    break;
                }
                case "pinDetail": {
                    this.fbmerw9jk44d = f;
                    break;
                }
                default: {
                    super.setAnimProperty(string, f);
                }
            }
        }
    }

    private record Painted(TerrainKindData point, Box pin, Box tag, boolean interactiveTag) {
    }

    public record Box(float x, float y, float width, float height) {
        public boolean contains(float f, float f2) {
            return (f >= this.x && f <= this.x + this.width && f2 >= this.y && f2 <= this.y + this.height ? 1 : 0) != 0;
        }

        public boolean overlaps(Box box) {
            return (this.x < box.x + box.width + Float.intBitsToFloat(0x40A00000) && this.x + this.width + Float.intBitsToFloat(0x40A00000) > box.x && this.y < box.y + box.height + Float.intBitsToFloat(0x40A00000) && this.y + this.height + Float.intBitsToFloat(0x40A00000) > box.y ? 1 : 0) != 0;
        }
    }

    private record Layout(Anchor anchor, PinMotion motion, Box pin, Box tag, String title, String detail, boolean tooltip, boolean below, boolean hint) {
    }
}

