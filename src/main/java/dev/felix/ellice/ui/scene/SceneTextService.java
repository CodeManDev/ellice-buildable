package dev.felix.ellice.ui.scene;

import dev.felix.ellice.feature.privacy.PrivacyRevisionService;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.text.TextData;
import dev.felix.ellice.ui.text.TextEndsService;
import dev.felix.ellice.ui.text.TextMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SceneTextService extends ScenePctService<SceneTextService> {
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
  private String text2;
  private String text3;
  private float value;
  private float value2;
  private float value3 = Float.intBitsToFloat(2143289344);
  private String[] text4;
  private String text5;
  private float fwp674qjy06;
  private float value5;
  private float value6 = Float.intBitsToFloat(2143289344);
  private TextMode textMode;
  private float value7;
  private int count = -1;
  private CompositorPushPresentationScaleService fdsh1lkpehiq;
  private float value8 = Float.intBitsToFloat(2143289344);
  private float value9 = Float.intBitsToFloat(2143289344);
  private int count2 = -1;
  private String ffltkukpxpka;
  private float f3g16bnmvrwp = Float.intBitsToFloat(-1082130432);
  private float fc59dcamrno6 = Float.intBitsToFloat(-1082130432);
  private float value12 = Float.intBitsToFloat(2143289344);
  private TextMode fg331wtnkk0;
  private float value13 = Float.intBitsToFloat(2143289344);
  private TextMode textMode3;

  public SceneTextService() {}

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
      this.updateState8();
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
    this.updateState8();
    this.invalidateLayout();
    return this;
  }

  public SceneTextService fontSize(float f) {
    if (Float.floatToIntBits(this.fontSize) == Float.floatToIntBits(f)) {
      return this;
    }
    this.fontSize = f;
    this.updateState8();
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
    this.updateState8();
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
    this.updateState8();
    this.invalidateLayout();
    return this;
  }

  public SceneTextService fontFamily(String string) {
    if (Objects.equals(this.fontFamily, string)) {
      return this;
    }
    this.fontFamily = string;
    this.updateState8();
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
    this.updateState8();
    this.invalidateLayout();
    return this;
  }

  public SceneTextService wordWrap(boolean bl) {
    if (this.wordWrap == bl) {
      return this;
    }
    this.wordWrap = bl;
    this.updateState8();
    this.invalidateLayout();
    return this;
  }

  public SceneTextService maxLines(int n) {
    if (this.maxLines == n) {
      return this;
    }
    this.maxLines = n;
    this.updateState8();
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
    if (this.value7 != f) {
      this.value7 = f;
      this.updateState8();
      this.invalidateLayout();
    }
    return this;
  }

  private float calculateValue(
      CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
    return this.value7 > 0.0f
        ? this.value7
        : compositorPushPresentationScaleService.textLineHeight(
            this.fontSize, this.meqgax6yboqy(), this.fontFamily);
  }

  private void updateState(
      CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
    int n = compositorPushPresentationScaleService.fontMetricsRevision();
    if (this.fdsh1lkpehiq != compositorPushPresentationScaleService || this.count != n) {
      this.fdsh1lkpehiq = compositorPushPresentationScaleService;
      this.count = n;
      this.updateState8();
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
  public float intrinsicWidth(
      CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
    float f;
    String string = this.displayText();
    this.updateState(compositorPushPresentationScaleService);
    if (string == null || string.isEmpty()) {
      return 0.0f;
    }
    if (!Float.isNaN(this.value8)) {
      if (this.wordWrap && this.width > 0.0f && this.value8 > this.width) {
        return this.width;
      }
      if (this.value13 == this.presentationScale) {
        return this.value8;
      }
    }
    if ((f =
            compositorPushPresentationScaleService.textWidthLiteral(
                string,
                this.fontSize,
                this.meqgax6yboqy(),
                this.fontFamily,
                this.style.letterSpacing()))
        > 0.0f) {
      this.value8 = f;
      this.value13 = this.presentationScale;
    }
    if (this.wordWrap && this.width > 0.0f && f > this.width) {
      return this.width;
    }
    return f;
  }

  @Override
  public float intrinsicHeight(
      CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
    return this.intrinsicHeight(
        compositorPushPresentationScaleService,
        this.width > 0.0f ? this.width : Float.intBitsToFloat(2143289344));
  }

  @Override
  public float intrinsicHeight(
      CompositorPushPresentationScaleService compositorPushPresentationScaleService, float f) {
    String string = this.displayText();
    this.updateState(compositorPushPresentationScaleService);
    float f2 = this.calculateValue(compositorPushPresentationScaleService);
    if (this.wordWrap && Float.isFinite(f) && f > 0.0f && string != null && !string.isEmpty()) {
      int n;
      TextMode textMode = this.meqgax6yboqy();
      if (this.count2 >= 0
          && string.equals(this.ffltkukpxpka)
          && f == this.f3g16bnmvrwp
          && this.fontSize == this.fc59dcamrno6
          && textMode == this.fg331wtnkk0
          && this.presentationScale == this.value12) {
        n = this.count2;
      } else {
        float f3 =
            compositorPushPresentationScaleService.textWidthLiteral(
                string, this.fontSize, textMode, this.fontFamily, this.style.letterSpacing());
        n = f3 <= f ? 1 : this.calculateValue2(compositorPushPresentationScaleService, f);
        this.ffltkukpxpka = string;
        this.f3g16bnmvrwp = f;
        this.fc59dcamrno6 = this.fontSize;
        this.fg331wtnkk0 = textMode;
        this.value12 = this.presentationScale;
        this.count2 = n;
      }
      if (this.maxLines > 0 && n > this.maxLines) {
        n = this.maxLines;
      }
      return (float) n * f2;
    }
    if (!Float.isNaN(this.value9)) {
      return this.value9;
    }
    if (f2 > this.fontSize) {
      this.value9 = f2;
    }
    return f2;
  }

  private int calculateValue2(
      CompositorPushPresentationScaleService compositorPushPresentationScaleService, float f) {
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
  protected void draw(
      CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
    int n;
    TextData textData;
    String string = this.displayText();
    this.updateState(compositorPushPresentationScaleService);
    if (string == null || string.isEmpty()) {
      return;
    }
    TextData textData2 =
        textData =
            this.fontVariant != TextMode.REGULAR
                ? this.style.withVariant(this.fontVariant)
                : this.style;
    if (this.fontFamily != null) {
      textData = textData.withFontFamily(this.fontFamily);
    }
    TextMode textMode = this.meqgax6yboqy();
    float f =
        this.cy
            + (this.value7 > 0.0f
                ? Math.max(
                    0.0f,
                    (this.value7
                            - compositorPushPresentationScaleService.textLineHeight(
                                this.fontSize, textMode, this.fontFamily))
                        / 2.0f)
                : 0.0f);
    if (this.effectiveEdgeSoftness > Float.intBitsToFloat(981668463)
        && textData.edgeSoftness() <= Float.intBitsToFloat(981668463)) {
      textData = textData.withEdgeSoftness(this.effectiveEdgeSoftness);
    }
    int n2 = SceneTextService.mulAlpha(this.color, this.effectiveOpacity);
    if (this.wordWrap && this.cw > 0.0f) {
      int n3;
      String[] stringArray = this.mhwzmgqvoyc1(compositorPushPresentationScaleService);
      float f2 = this.calculateValue(compositorPushPresentationScaleService);
      int n4 = n3 = this.maxLines > 0 || this.overflow == Overflow.CLIP ? 1 : 0;
      if (n3 != 0) {
        compositorPushPresentationScaleService.pushClip(this.cx, this.cy, this.cw, this.ch);
      }
      for (int i = 0; i < stringArray.length && (this.maxLines <= 0 || i < this.maxLines); ++i) {
        String string2 = stringArray[i];
        if (this.maxLines > 0 && i == this.maxLines - 1 && i < stringArray.length - 1) {
          string2 = this.createText2(compositorPushPresentationScaleService, string2, this.cw);
        }
        float f3 = this.cx;
        if (this.textAlign != TextAlign.LEFT) {
          float f4 =
              compositorPushPresentationScaleService.textWidthLiteral(
                  string2,
                  this.fontSize,
                  this.meqgax6yboqy(),
                  this.fontFamily,
                  this.style.letterSpacing());
          if (this.textAlign == TextAlign.CENTER) {
            f3 = this.cx + (this.cw - f4) / 2.0f;
          } else if (this.textAlign == TextAlign.RIGHT) {
            f3 = this.cx + this.cw - f4;
          }
        }
        compositorPushPresentationScaleService.textLiteral(
            f3, f + (float) i * f2, string2, this.fontSize, n2, textData);
      }
      if (n3 != 0) {
        compositorPushPresentationScaleService.popClip();
      }
      return;
    }
    String string3 = string;
    int n5 = n = this.overflow == Overflow.ELLIPSIS ? 1 : 0;
    if (this.overflow == Overflow.ELLIPSIS && this.cw > 0.0f) {
      string3 = this.createText3(compositorPushPresentationScaleService);
    } else if (this.overflow == Overflow.CLIP && this.cw > 0.0f) {
      n = 1;
    }
    float f5 = this.cx;
    if (this.textAlign != TextAlign.LEFT && this.cw > 0.0f) {
      float f6 =
          compositorPushPresentationScaleService.textWidthLiteral(
              string3,
              this.fontSize,
              this.meqgax6yboqy(),
              this.fontFamily,
              this.style.letterSpacing());
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

  private String[] mhwzmgqvoyc1(
      CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
    String[] stringArray;
    String string = this.displayText();
    TextMode textMode = this.meqgax6yboqy();
    if (this.text4 != null
        && string.equals(this.text5)
        && this.cw == this.fwp674qjy06
        && this.fontSize == this.value5
        && textMode == this.textMode
        && this.presentationScale == this.value6) {
      return this.text4;
    }
    this.text5 = string;
    this.fwp674qjy06 = this.cw;
    this.value5 = this.fontSize;
    this.textMode = textMode;
    this.value6 = this.presentationScale;
    ArrayList<String> arrayList = new ArrayList<String>();
    for (String string2 : stringArray = string.split("\\R", -1)) {
      this.mck5u9yx0m1g(compositorPushPresentationScaleService, string2, arrayList);
    }
    this.text4 = arrayList.toArray(new String[0]);
    return this.text4;
  }

  private void mck5u9yx0m1g(
      CompositorPushPresentationScaleService compositorPushPresentationScaleService,
      String string,
      List<String> list) {
    this.ma4hucm0ba6o(compositorPushPresentationScaleService, string, list, this.cw);
  }

  private void ma4hucm0ba6o(
      CompositorPushPresentationScaleService compositorPushPresentationScaleService,
      String string,
      List<String> list,
      float f) {
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
      String string4 =
          string3 =
              stringBuilder.isEmpty() ? string2 : String.valueOf(stringBuilder) + " " + string2;
      if (compositorPushPresentationScaleService.textWidthLiteral(
              string3,
              this.fontSize,
              this.meqgax6yboqy(),
              this.fontFamily,
              this.style.letterSpacing())
          <= f) {
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

  private void updateState4(
      CompositorPushPresentationScaleService compositorPushPresentationScaleService,
      StringBuilder stringBuilder) {
    this.md6wfnsfwvw(compositorPushPresentationScaleService, stringBuilder, this.cw);
  }

  private void md6wfnsfwvw(
      CompositorPushPresentationScaleService compositorPushPresentationScaleService,
      StringBuilder stringBuilder,
      float f) {
    if (stringBuilder.isEmpty()) {
      return;
    }
    String string = String.valueOf(stringBuilder) + " ";
    if (compositorPushPresentationScaleService.textWidthLiteral(
            string, this.fontSize, this.meqgax6yboqy(), this.fontFamily, this.style.letterSpacing())
        <= f) {
      stringBuilder.append(' ');
    }
  }

  private void updateState6(
      CompositorPushPresentationScaleService compositorPushPresentationScaleService,
      String string,
      StringBuilder stringBuilder,
      List<String> list) {
    this.maaxqbyuxpjh(compositorPushPresentationScaleService, string, stringBuilder, list, this.cw);
  }

  private void maaxqbyuxpjh(
      CompositorPushPresentationScaleService compositorPushPresentationScaleService,
      String string,
      StringBuilder stringBuilder,
      List<String> list,
      float f) {
    String string2 = string;
    while (!string2.isEmpty()
        && compositorPushPresentationScaleService.textWidthLiteral(
                string2,
                this.fontSize,
                this.meqgax6yboqy(),
                this.fontFamily,
                this.style.letterSpacing())
            > f) {
      int n = this.calculateValue3(compositorPushPresentationScaleService, string2, f);
      if (n <= 0) {
        n = SceneTextService.calculateValue4(string2);
      }
      list.add(string2.substring(0, n));
      string2 = string2.substring(n);
    }
    if (!string2.isEmpty()) {
      stringBuilder.append(string2);
    }
  }

  private int calculateValue3(
      CompositorPushPresentationScaleService compositorPushPresentationScaleService,
      String string,
      float f) {
    int[] nArray = TextEndsService.ends(string);
    int n = 0;
    int n2 = nArray.length;
    while (n < n2) {
      int n3 = (n + n2 + 1) / 2;
      int n4 = nArray[n3 - 1];
      if (compositorPushPresentationScaleService.textWidthLiteral(
              string.substring(0, n4),
              this.fontSize,
              this.meqgax6yboqy(),
              this.fontFamily,
              this.style.letterSpacing())
          <= f) {
        n = n3;
        continue;
      }
      n2 = n3 - 1;
    }
    return n > 0 ? nArray[n - 1] : 0;
  }

  private String createText2(
      CompositorPushPresentationScaleService compositorPushPresentationScaleService,
      String string,
      float f) {
    float f2 =
        compositorPushPresentationScaleService.textWidthLiteral(
            "...", this.fontSize, this.meqgax6yboqy(), this.fontFamily, this.style.letterSpacing());
    if (compositorPushPresentationScaleService.textWidthLiteral(
            string, this.fontSize, this.meqgax6yboqy(), this.fontFamily, this.style.letterSpacing())
        <= f) {
      return string;
    }
    float f3 = f - f2;
    int[] nArray = TextEndsService.ends(string);
    int n = 0;
    int n2 = nArray.length;
    while (n < n2) {
      int n3 = (n + n2 + 1) / 2;
      int n4 = nArray[n3 - 1];
      if (compositorPushPresentationScaleService.textWidthLiteral(
              string.substring(0, n4),
              this.fontSize,
              this.meqgax6yboqy(),
              this.fontFamily,
              this.style.letterSpacing())
          <= f3) {
        n = n3;
        continue;
      }
      n2 = n3 - 1;
    }
    return n > 0 ? string.substring(0, nArray[n - 1]) + "..." : "...";
  }

  private String createText3(
      CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
    String string = this.displayText();
    TextMode textMode = this.meqgax6yboqy();
    if (this.text2 != null
        && string.equals(this.text3)
        && this.fontSize == this.value
        && this.cw == this.value2
        && textMode == this.textMode3
        && this.presentationScale == this.value3) {
      return this.text2;
    }
    this.text3 = string;
    this.value = this.fontSize;
    this.value2 = this.cw;
    this.textMode3 = textMode;
    this.value3 = this.presentationScale;
    float f =
        compositorPushPresentationScaleService.textWidthLiteral(
            string,
            this.fontSize,
            this.meqgax6yboqy(),
            this.fontFamily,
            this.style.letterSpacing());
    if (f <= this.cw + Float.intBitsToFloat(1028443341) * Math.max(1.0f, this.presentationScale)) {
      this.text2 = string;
      return string;
    }
    float f2 =
        compositorPushPresentationScaleService.textWidthLiteral(
            "...", this.fontSize, this.meqgax6yboqy(), this.fontFamily, this.style.letterSpacing());
    float f3 = this.cw - f2;
    if (f3 <= 0.0f) {
      this.text2 = "...";
      return "...";
    }
    int[] nArray = TextEndsService.ends(string);
    int n = 0;
    int n2 = nArray.length;
    while (n < n2) {
      int n3 = (n + n2 + 1) / 2;
      int n4 = nArray[n3 - 1];
      if (compositorPushPresentationScaleService.textWidthLiteral(
              string.substring(0, n4),
              this.fontSize,
              this.meqgax6yboqy(),
              this.fontFamily,
              this.style.letterSpacing())
          <= f3) {
        n = n3;
        continue;
      }
      n2 = n3 - 1;
    }
    this.text2 = n > 0 ? string.substring(0, nArray[n - 1]) + "..." : "...";
    return this.text2;
  }

  private void updateState8() {
    this.text2 = null;
    this.value3 = Float.intBitsToFloat(2143289344);
    this.text4 = null;
    this.value6 = Float.intBitsToFloat(2143289344);
    this.value8 = Float.intBitsToFloat(2143289344);
    this.value9 = Float.intBitsToFloat(2143289344);
    this.value13 = Float.intBitsToFloat(2143289344);
    this.count2 = -1;
    this.value12 = Float.intBitsToFloat(2143289344);
  }

  private static int calculateValue4(String string) {
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
