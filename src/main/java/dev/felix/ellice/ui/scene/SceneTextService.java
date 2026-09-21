


package dev.felix.ellice.ui.scene;

import dev.felix.ellice.feature.privacy.PrivacyRevisionService;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.text.TextData;
import dev.felix.ellice.ui.text.TextEndsService;
import dev.felix.ellice.ui.text.TextMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SceneTextService
extends ScenePctService<SceneTextService> {
    String text;
    float fontSize;
    int color;
    TextData style = TextData.NONE;
    TextAlign textAlign = TextAlign.LEFT;
    TextMode fontVariant = TextMode.REGULAR;
    String fontFamily;
    Overflow overflow = Overflow.VISIBLE;
    boolean wordWrap;
    int maxLines;
    private boolean ftkq7gizuuj;
    private String f6cxqvxu3mzy;
    private String f11lj10hh0js;
    private float f92o1e7qn3fr;
    private float fgh21z2s53uf;
    private float f8ezt2ibivkf = Float.intBitsToFloat(2143289344);
    private String[] fgdrt7dod0ii;
    private String fdopvqovkq26;
    private float fwp674qjy06;
    private float faswk13wnv3p;
    private float fje8jwn1vl5x = Float.intBitsToFloat(2143289344);
    private TextMode f2t6ik8k07h1;
    private float fh4vlcvnc4tj;
    private int f1j1hbyce4g7 = -1;
    private CompositorPushPresentationScaleService fdsh1lkpehiq;
    private float f56uy6xt5le0 = Float.intBitsToFloat(2143289344);
    private float f5hfq56fmyzs = Float.intBitsToFloat(2143289344);
    private int f7x2azhynk39 = -1;
    private String ffltkukpxpka;
    private float f3g16bnmvrwp = Float.intBitsToFloat(-1082130432);
    private float fc59dcamrno6 = Float.intBitsToFloat(-1082130432);
    private float f15g20ed3xm3 = Float.intBitsToFloat(2143289344);
    private TextMode fg331wtnkk0;
    private float f3wie2zewueo = Float.intBitsToFloat(2143289344);
    private TextMode fg189t66p79w;

    public SceneTextService() {
    }

    public SceneTextService(String string, float f, int n) {
        this.text = string;
        this.fontSize = f;
        this.color = n;
    }

    public String text() {
        return this.text;
    }

    public String displayText() {
        String string = PrivacyRevisionService.replace(this.text);
        if (!this.ftkq7gizuuj) {
            return string;
        }
        if (string == null || string.isBlank()) {
            return "?";
        }
        string = string.strip();
        return string.substring(0, TextEndsService.nextBoundary(string, 0)).toUpperCase(Locale.ROOT);
    }

    public SceneTextService initial(boolean bl) {
        if (this.ftkq7gizuuj != bl) {
            this.ftkq7gizuuj = bl;
            this.manvq2bceu7s();
            this.invalidateLayout();
        }
        return this;
    }

    public TextMode fontVariant() {
        return this.fontVariant;
    }

    public SceneTextService text(String string) {
        if (Objects.equals(this.text, string)) {
            return this;
        }
        this.text = string;
        this.manvq2bceu7s();
        this.invalidateLayout();
        return this;
    }

    public SceneTextService fontSize(float f) {
        if (Float.floatToIntBits(this.fontSize) == Float.floatToIntBits(f)) {
            return this;
        }
        this.fontSize = f;
        this.manvq2bceu7s();
        this.invalidateLayout();
        return this;
    }

    public SceneTextService color(int n) {
        if (this.color == n) {
            return this;
        }
        this.color = n;
        this.invalidate();
        return this;
    }

    public SceneTextService style(TextData textData) {
        TextData textData2;
        TextData textData3 = textData2 = textData != null ? textData : TextData.NONE;
        if (Objects.equals(this.style, textData2)) {
            return this;
        }
        this.style = textData2;
        this.manvq2bceu7s();
        this.invalidateLayout();
        return this;
    }

    public TextData style() {
        return this.style;
    }

    public SceneTextService letterSpacing(float f) {
        return this.style(this.style.withLetterSpacing(f));
    }

    public SceneTextService textAlign(TextAlign textAlign) {
        TextAlign textAlign2;
        TextAlign textAlign3 = textAlign2 = textAlign != null ? textAlign : TextAlign.LEFT;
        if (this.textAlign == textAlign2) {
            return this;
        }
        this.textAlign = textAlign2;
        this.invalidate();
        return this;
    }

    public SceneTextService fontVariant(TextMode textMode) {
        TextMode textMode2;
        TextMode textMode3 = textMode2 = textMode != null ? textMode : TextMode.REGULAR;
        if (this.fontVariant == textMode2) {
            return this;
        }
        this.fontVariant = textMode2;
        this.manvq2bceu7s();
        this.invalidateLayout();
        return this;
    }

    public SceneTextService fontFamily(String string) {
        if (Objects.equals(this.fontFamily, string)) {
            return this;
        }
        this.fontFamily = string;
        this.manvq2bceu7s();
        this.invalidateLayout();
        return this;
    }

    public SceneTextService overflow(Overflow overflow) {
        Overflow overflow2;
        Overflow overflow3 = overflow2 = overflow != null ? overflow : Overflow.VISIBLE;
        if (this.overflow == overflow2) {
            return this;
        }
        this.overflow = overflow2;
        this.manvq2bceu7s();
        this.invalidateLayout();
        return this;
    }

    public SceneTextService wordWrap(boolean bl) {
        if (this.wordWrap == bl) {
            return this;
        }
        this.wordWrap = bl;
        this.manvq2bceu7s();
        this.invalidateLayout();
        return this;
    }

    public SceneTextService maxLines(int n) {
        if (this.maxLines == n) {
            return this;
        }
        this.maxLines = n;
        this.manvq2bceu7s();
        this.invalidateLayout();
        return this;
    }

    @Override
    public int getColorProperty(String string) {
        return "color".equals(string) ? this.color : super.getColorProperty(string);
    }

    @Override
    public void setColorProperty(String string, int n) {
        if ("color".equals(string)) {
            this.color = n;
        } else {
            super.setColorProperty(string, n);
        }
    }

    public SceneTextService lineHeight(float f) {
        if (!Float.isFinite(f) || f < 0.0f) {
            throw new IllegalArgumentException("line height must be finite and non-negative");
        }
        if (this.fh4vlcvnc4tj != f) {
            this.fh4vlcvnc4tj = f;
            this.manvq2bceu7s();
            this.invalidateLayout();
        }
        return this;
    }

    private float mbl64kdiwkie(CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
        return this.fh4vlcvnc4tj > 0.0f ? this.fh4vlcvnc4tj : compositorPushPresentationScaleService.textLineHeight(this.fontSize, this.meqgax6yboqy(), this.fontFamily);
    }

    private void m3xegb2aqxav(CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
        int n = compositorPushPresentationScaleService.fontMetricsRevision();
        if (this.fdsh1lkpehiq != compositorPushPresentationScaleService || this.f1j1hbyce4g7 != n) {
            this.fdsh1lkpehiq = compositorPushPresentationScaleService;
            this.f1j1hbyce4g7 = n;
            this.manvq2bceu7s();
        }
    }

    private TextMode meqgax6yboqy() {
        if (this.fontVariant != TextMode.REGULAR) {
            return this.fontVariant;
        }
        TextMode textMode = this.style != null ? this.style.variant() : null;
        return textMode != null ? textMode : TextMode.REGULAR;
    }

    @Override
    public float intrinsicWidth(CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
        float f;
        String string = this.displayText();
        this.m3xegb2aqxav(compositorPushPresentationScaleService);
        if (string == null || string.isEmpty()) {
            return 0.0f;
        }
        if (!Float.isNaN(this.f56uy6xt5le0)) {
            if (this.wordWrap && this.width > 0.0f && this.f56uy6xt5le0 > this.width) {
                return this.width;
            }
            if (this.f3wie2zewueo == this.presentationScale) {
                return this.f56uy6xt5le0;
            }
        }
        if ((f = compositorPushPresentationScaleService.textWidthLiteral(string, this.fontSize, this.meqgax6yboqy(), this.fontFamily, this.style.letterSpacing())) > 0.0f) {
            this.f56uy6xt5le0 = f;
            this.f3wie2zewueo = this.presentationScale;
        }
        if (this.wordWrap && this.width > 0.0f && f > this.width) {
            return this.width;
        }
        return f;
    }

    @Override
    public float intrinsicHeight(CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
        return this.intrinsicHeight(compositorPushPresentationScaleService, this.width > 0.0f ? this.width : Float.intBitsToFloat(2143289344));
    }

    @Override
    public float intrinsicHeight(CompositorPushPresentationScaleService compositorPushPresentationScaleService, float f) {
        String string = this.displayText();
        this.m3xegb2aqxav(compositorPushPresentationScaleService);
        float f2 = this.mbl64kdiwkie(compositorPushPresentationScaleService);
        if (this.wordWrap && Float.isFinite(f) && f > 0.0f && string != null && !string.isEmpty()) {
            int n;
            TextMode textMode = this.meqgax6yboqy();
            if (this.f7x2azhynk39 >= 0 && string.equals(this.ffltkukpxpka) && f == this.f3g16bnmvrwp && this.fontSize == this.fc59dcamrno6 && textMode == this.fg331wtnkk0 && this.presentationScale == this.f15g20ed3xm3) {
                n = this.f7x2azhynk39;
            } else {
                float f3 = compositorPushPresentationScaleService.textWidthLiteral(string, this.fontSize, textMode, this.fontFamily, this.style.letterSpacing());
                n = f3 <= f ? 1 : this.m3w8ean6128b(compositorPushPresentationScaleService, f);
                this.ffltkukpxpka = string;
                this.f3g16bnmvrwp = f;
                this.fc59dcamrno6 = this.fontSize;
                this.fg331wtnkk0 = textMode;
                this.f15g20ed3xm3 = this.presentationScale;
                this.f7x2azhynk39 = n;
            }
            if (this.maxLines > 0 && n > this.maxLines) {
                n = this.maxLines;
            }
            return (float)n * f2;
        }
        if (!Float.isNaN(this.f5hfq56fmyzs)) {
            return this.f5hfq56fmyzs;
        }
        if (f2 > this.fontSize) {
            this.f5hfq56fmyzs = f2;
        }
        return f2;
    }

    private int m3w8ean6128b(CompositorPushPresentationScaleService compositorPushPresentationScaleService, float f) {
        String string = this.displayText();
        if (string == null || string.isEmpty() || f <= 0.0f) {
            return 1;
        }
        ArrayList<String> arrayList = new ArrayList<String>();
        for (String string2 : string.split("\\R", -1)) {
            this.ma4hucm0ba6o(compositorPushPresentationScaleService, string2, arrayList, f);
        }
        return Math.max(1, arrayList.size());
    }

    @Override
    protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
        int n;
        TextData textData;
        String string = this.displayText();
        this.m3xegb2aqxav(compositorPushPresentationScaleService);
        if (string == null || string.isEmpty()) {
            return;
        }
        TextData textData2 = textData = this.fontVariant != TextMode.REGULAR ? this.style.withVariant(this.fontVariant) : this.style;
        if (this.fontFamily != null) {
            textData = textData.withFontFamily(this.fontFamily);
        }
        TextMode textMode = this.meqgax6yboqy();
        float f = this.cy + (this.fh4vlcvnc4tj > 0.0f ? Math.max(0.0f, (this.fh4vlcvnc4tj - compositorPushPresentationScaleService.textLineHeight(this.fontSize, textMode, this.fontFamily)) / 2.0f) : 0.0f);
        if (this.effectiveEdgeSoftness > Float.intBitsToFloat(981668463) && textData.edgeSoftness() <= Float.intBitsToFloat(981668463)) {
            textData = textData.withEdgeSoftness(this.effectiveEdgeSoftness);
        }
        int n2 = SceneTextService.mulAlpha(this.color, this.effectiveOpacity);
        if (this.wordWrap && this.cw > 0.0f) {
            int n3;
            String[] stringArray = this.mhwzmgqvoyc1(compositorPushPresentationScaleService);
            float f2 = this.mbl64kdiwkie(compositorPushPresentationScaleService);
            int n4 = n3 = this.maxLines > 0 || this.overflow == Overflow.CLIP ? 1 : 0;
            if (n3 != 0) {
                compositorPushPresentationScaleService.pushClip(this.cx, this.cy, this.cw, this.ch);
            }
            for (int i = 0; i < stringArray.length && (this.maxLines <= 0 || i < this.maxLines); ++i) {
                String string2 = stringArray[i];
                if (this.maxLines > 0 && i == this.maxLines - 1 && i < stringArray.length - 1) {
                    string2 = this.mgaymi8sjl4h(compositorPushPresentationScaleService, string2, this.cw);
                }
                float f3 = this.cx;
                if (this.textAlign != TextAlign.LEFT) {
                    float f4 = compositorPushPresentationScaleService.textWidthLiteral(string2, this.fontSize, this.meqgax6yboqy(), this.fontFamily, this.style.letterSpacing());
                    if (this.textAlign == TextAlign.CENTER) {
                        f3 = this.cx + (this.cw - f4) / 2.0f;
                    } else if (this.textAlign == TextAlign.RIGHT) {
                        f3 = this.cx + this.cw - f4;
                    }
                }
                compositorPushPresentationScaleService.textLiteral(f3, f + (float)i * f2, string2, this.fontSize, n2, textData);
            }
            if (n3 != 0) {
                compositorPushPresentationScaleService.popClip();
            }
            return;
        }
        String string3 = string;
        int n5 = n = this.overflow == Overflow.ELLIPSIS ? 1 : 0;
        if (this.overflow == Overflow.ELLIPSIS && this.cw > 0.0f) {
            string3 = this.mi964y79twu1(compositorPushPresentationScaleService);
        } else if (this.overflow == Overflow.CLIP && this.cw > 0.0f) {
            n = 1;
        }
        float f5 = this.cx;
        if (this.textAlign != TextAlign.LEFT && this.cw > 0.0f) {
            float f6 = compositorPushPresentationScaleService.textWidthLiteral(string3, this.fontSize, this.meqgax6yboqy(), this.fontFamily, this.style.letterSpacing());
            if (this.textAlign == TextAlign.CENTER) {
                f5 = this.cx + (this.cw - f6) / 2.0f;
            } else if (this.textAlign == TextAlign.RIGHT) {
                f5 = this.cx + this.cw - f6;
            }
        }
        if (n != 0) {
            compositorPushPresentationScaleService.pushClip(this.cx, this.cy, this.cw, this.ch);
        }
        compositorPushPresentationScaleService.textLiteral(f5, f, string3, this.fontSize, n2, textData);
        if (n != 0) {
            compositorPushPresentationScaleService.popClip();
        }
    }

    private String[] mhwzmgqvoyc1(CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
        String[] stringArray;
        String string = this.displayText();
        TextMode textMode = this.meqgax6yboqy();
        if (this.fgdrt7dod0ii != null && string.equals(this.fdopvqovkq26) && this.cw == this.fwp674qjy06 && this.fontSize == this.faswk13wnv3p && textMode == this.f2t6ik8k07h1 && this.presentationScale == this.fje8jwn1vl5x) {
            return this.fgdrt7dod0ii;
        }
        this.fdopvqovkq26 = string;
        this.fwp674qjy06 = this.cw;
        this.faswk13wnv3p = this.fontSize;
        this.f2t6ik8k07h1 = textMode;
        this.fje8jwn1vl5x = this.presentationScale;
        ArrayList<String> arrayList = new ArrayList<String>();
        for (String string2 : stringArray = string.split("\\R", -1)) {
            this.mck5u9yx0m1g(compositorPushPresentationScaleService, string2, arrayList);
        }
        this.fgdrt7dod0ii = arrayList.toArray(new String[0]);
        return this.fgdrt7dod0ii;
    }

    private void mck5u9yx0m1g(CompositorPushPresentationScaleService compositorPushPresentationScaleService, String string, List<String> list) {
        this.ma4hucm0ba6o(compositorPushPresentationScaleService, string, list, this.cw);
    }

    private void ma4hucm0ba6o(CompositorPushPresentationScaleService compositorPushPresentationScaleService, String string, List<String> list, float f) {
        if (string.isEmpty()) {
            list.add("");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String string2 : string.split(" ", -1)) {
            String string3;
            if (string2.isEmpty()) {
                this.md6wfnsfwvw(compositorPushPresentationScaleService, stringBuilder, f);
                continue;
            }
            String string4 = string3 = stringBuilder.isEmpty() ? string2 : String.valueOf(stringBuilder) + " " + string2;
            if (compositorPushPresentationScaleService.textWidthLiteral(string3, this.fontSize, this.meqgax6yboqy(), this.fontFamily, this.style.letterSpacing()) <= f) {
                if (!stringBuilder.isEmpty()) {
                    stringBuilder.append(' ');
                }
                stringBuilder.append(string2);
                continue;
            }
            if (!stringBuilder.isEmpty()) {
                list.add(stringBuilder.toString());
                stringBuilder.setLength(0);
            }
            this.maaxqbyuxpjh(compositorPushPresentationScaleService, string2, stringBuilder, list, f);
        }
        if (!stringBuilder.isEmpty()) {
            list.add(stringBuilder.toString());
        }
    }

    private void me2hd7c5gh9e(CompositorPushPresentationScaleService compositorPushPresentationScaleService, StringBuilder stringBuilder) {
        this.md6wfnsfwvw(compositorPushPresentationScaleService, stringBuilder, this.cw);
    }

    private void md6wfnsfwvw(CompositorPushPresentationScaleService compositorPushPresentationScaleService, StringBuilder stringBuilder, float f) {
        if (stringBuilder.isEmpty()) {
            return;
        }
        String string = String.valueOf(stringBuilder) + " ";
        if (compositorPushPresentationScaleService.textWidthLiteral(string, this.fontSize, this.meqgax6yboqy(), this.fontFamily, this.style.letterSpacing()) <= f) {
            stringBuilder.append(' ');
        }
    }

    private void mdbywtna7w9y(CompositorPushPresentationScaleService compositorPushPresentationScaleService, String string, StringBuilder stringBuilder, List<String> list) {
        this.maaxqbyuxpjh(compositorPushPresentationScaleService, string, stringBuilder, list, this.cw);
    }

    private void maaxqbyuxpjh(CompositorPushPresentationScaleService compositorPushPresentationScaleService, String string, StringBuilder stringBuilder, List<String> list, float f) {
        String string2 = string;
        while (!string2.isEmpty() && compositorPushPresentationScaleService.textWidthLiteral(string2, this.fontSize, this.meqgax6yboqy(), this.fontFamily, this.style.letterSpacing()) > f) {
            int n = this.m6mls18vd7wz(compositorPushPresentationScaleService, string2, f);
            if (n <= 0) {
                n = SceneTextService.maovi6vz4u6q(string2);
            }
            list.add(string2.substring(0, n));
            string2 = string2.substring(n);
        }
        if (!string2.isEmpty()) {
            stringBuilder.append(string2);
        }
    }

    private int m6mls18vd7wz(CompositorPushPresentationScaleService compositorPushPresentationScaleService, String string, float f) {
        int[] nArray = TextEndsService.ends(string);
        int n = 0;
        int n2 = nArray.length;
        while (n < n2) {
            int n3 = (n + n2 + 1) / 2;
            int n4 = nArray[n3 - 1];
            if (compositorPushPresentationScaleService.textWidthLiteral(string.substring(0, n4), this.fontSize, this.meqgax6yboqy(), this.fontFamily, this.style.letterSpacing()) <= f) {
                n = n3;
                continue;
            }
            n2 = n3 - 1;
        }
        return n > 0 ? nArray[n - 1] : 0;
    }

    private String mgaymi8sjl4h(CompositorPushPresentationScaleService compositorPushPresentationScaleService, String string, float f) {
        float f2 = compositorPushPresentationScaleService.textWidthLiteral("...", this.fontSize, this.meqgax6yboqy(), this.fontFamily, this.style.letterSpacing());
        if (compositorPushPresentationScaleService.textWidthLiteral(string, this.fontSize, this.meqgax6yboqy(), this.fontFamily, this.style.letterSpacing()) <= f) {
            return string;
        }
        float f3 = f - f2;
        int[] nArray = TextEndsService.ends(string);
        int n = 0;
        int n2 = nArray.length;
        while (n < n2) {
            int n3 = (n + n2 + 1) / 2;
            int n4 = nArray[n3 - 1];
            if (compositorPushPresentationScaleService.textWidthLiteral(string.substring(0, n4), this.fontSize, this.meqgax6yboqy(), this.fontFamily, this.style.letterSpacing()) <= f3) {
                n = n3;
                continue;
            }
            n2 = n3 - 1;
        }
        return n > 0 ? string.substring(0, nArray[n - 1]) + "..." : "...";
    }

    private String mi964y79twu1(CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
        String string = this.displayText();
        TextMode textMode = this.meqgax6yboqy();
        if (this.f6cxqvxu3mzy != null && string.equals(this.f11lj10hh0js) && this.fontSize == this.f92o1e7qn3fr && this.cw == this.fgh21z2s53uf && textMode == this.fg189t66p79w && this.presentationScale == this.f8ezt2ibivkf) {
            return this.f6cxqvxu3mzy;
        }
        this.f11lj10hh0js = string;
        this.f92o1e7qn3fr = this.fontSize;
        this.fgh21z2s53uf = this.cw;
        this.fg189t66p79w = textMode;
        this.f8ezt2ibivkf = this.presentationScale;
        float f = compositorPushPresentationScaleService.textWidthLiteral(string, this.fontSize, this.meqgax6yboqy(), this.fontFamily, this.style.letterSpacing());
        if (f <= this.cw + Float.intBitsToFloat(1028443341) * Math.max(1.0f, this.presentationScale)) {
            this.f6cxqvxu3mzy = string;
            return string;
        }
        float f2 = compositorPushPresentationScaleService.textWidthLiteral("...", this.fontSize, this.meqgax6yboqy(), this.fontFamily, this.style.letterSpacing());
        float f3 = this.cw - f2;
        if (f3 <= 0.0f) {
            this.f6cxqvxu3mzy = "...";
            return "...";
        }
        int[] nArray = TextEndsService.ends(string);
        int n = 0;
        int n2 = nArray.length;
        while (n < n2) {
            int n3 = (n + n2 + 1) / 2;
            int n4 = nArray[n3 - 1];
            if (compositorPushPresentationScaleService.textWidthLiteral(string.substring(0, n4), this.fontSize, this.meqgax6yboqy(), this.fontFamily, this.style.letterSpacing()) <= f3) {
                n = n3;
                continue;
            }
            n2 = n3 - 1;
        }
        this.f6cxqvxu3mzy = n > 0 ? string.substring(0, nArray[n - 1]) + "..." : "...";
        return this.f6cxqvxu3mzy;
    }

    private void manvq2bceu7s() {
        this.f6cxqvxu3mzy = null;
        this.f8ezt2ibivkf = Float.intBitsToFloat(2143289344);
        this.fgdrt7dod0ii = null;
        this.fje8jwn1vl5x = Float.intBitsToFloat(2143289344);
        this.f56uy6xt5le0 = Float.intBitsToFloat(2143289344);
        this.f5hfq56fmyzs = Float.intBitsToFloat(2143289344);
        this.f3wie2zewueo = Float.intBitsToFloat(2143289344);
        this.f7x2azhynk39 = -1;
        this.f15g20ed3xm3 = Float.intBitsToFloat(2143289344);
    }

    private static int maovi6vz4u6q(String string) {
        int[] nArray = TextEndsService.ends(string);
        return nArray.length > 0 ? nArray[0] : string.length();
    }

    public static enum TextAlign {
        LEFT,
        CENTER,
        RIGHT;

    }

    public static enum Overflow {
        VISIBLE,
        CLIP,
        ELLIPSIS;

    }
}

