



package dev.felix.ellice.ui.text;

import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL11;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import java.text.AttributedCharacterIterator;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.image.WritableRaster;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.nio.ByteBuffer;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.awt.font.TextLayout;
import java.awt.font.TextHitInfo;
import java.util.Collection;
import java.text.Bidi;
import java.util.ArrayList;
import java.util.List;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;
import java.util.Objects;
import java.util.LinkedHashMap;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.HashMap;
import java.awt.font.FontRenderContext;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import java.util.Set;
import java.util.Map;
import java.awt.Font;
import dev.felix.ellice.render.rhi.RhiOperationHandler;

final class GlyphAtlas implements TextOperationHandler
{
    private static final int f4cwlzea6asz = 2048;
    private static final int f1efp3bywdia = 96;
    private static final float f2lyb9mdnsdr = 6.0f;
    private static final int fhygr87oq6rp;
    private final RhiOperationHandler f1jf6qbv99v7;
    private final TextMode f7n1wzl4ohl7;
    private final Font f94wt352bfwh;
    private final Font f5lh7et6tzor;
    private final Map<Integer, TextTextureService.GlyphInfo> ffp9qahymf1z;
    private final Map<GlyphKey, TextTextureService.GlyphInfo> f2lmmai28e3f;
    private final Map<String, TextTextureService.GlyphInfo> fdj8oso9cct5;
    private final Set<TextTextureService.GlyphInfo> fj1y8bhc2lf8;
    private final Map<String, ShapedLine> f55osi4tbyio;
    private RhiBlendStateService.TextureHandle fjkpauwthczv;
    private int ff6g11lyyppx;
    private int fdr42xz9vjae;
    private int f9x4n4rvoz1w;
    private boolean fegb2m90q2zi;
    private final float f5w46jul576z;
    private final float f6hd5flmyjf9;
    private final FontRenderContext f3g8ehmjuwgq;
    
    GlyphAtlas(final RhiOperationHandler rhiOperationHandler, final TextMode textMode) {
        this(rhiOperationHandler, textMode, new TextTextureService(textMode));
    }
    
    GlyphAtlas(final RhiOperationHandler f1jf6qbv99v7, final TextMode f7n1wzl4ohl7, final TextTextureService textTextureService) {
        this.ffp9qahymf1z = new HashMap<Integer, TextTextureService.GlyphInfo>();
        this.f2lmmai28e3f = new HashMap<GlyphKey, TextTextureService.GlyphInfo>();
        this.fdj8oso9cct5 = new HashMap<String, TextTextureService.GlyphInfo>();
        this.fj1y8bhc2lf8 = Collections.newSetFromMap(new IdentityHashMap<TextTextureService.GlyphInfo, Boolean>());
        this.f55osi4tbyio = new LinkedHashMap<String, ShapedLine>(128, Float.intBitsToFloat(1061158912), true) {
            @Override
            protected boolean removeEldestEntry(final Map.Entry<String, ShapedLine> entry) {
                return this.size() > 512;
            }
        };
        this.fjkpauwthczv = RhiBlendStateService.TextureHandle.NONE;
        this.f3g8ehmjuwgq = new FontRenderContext(null, true, true);
        this.f1jf6qbv99v7 = f1jf6qbv99v7;
        this.f7n1wzl4ohl7 = f7n1wzl4ohl7;
        this.f94wt352bfwh = mb27s6uvmm5f(textTextureService.font(Float.intBitsToFloat(1119879168), f7n1wzl4ohl7));
        this.f5lh7et6tzor = mb27s6uvmm5f(new Font("SansSerif", f7n1wzl4ohl7.awtStyle(), 96));
        final LineMetrics lineMetrics = this.f94wt352bfwh.getLineMetrics("Hg", this.f3g8ehmjuwgq);
        this.f6hd5flmyjf9 = lineMetrics.getAscent() / Float.intBitsToFloat(1119879168);
        this.f5w46jul576z = lineMetrics.getHeight() / Float.intBitsToFloat(1119879168);
    }
    
    @Override
    public RhiBlendStateService.TextureHandle texture() {
        this.m5bzx5wuw5eg();
        return this.fjkpauwthczv;
    }
    
    @Override
    public float pixelRange() {
        return Float.intBitsToFloat(1094713344);
    }
    
    @Override
    public float lineHeight() {
        return this.f5w46jul576z;
    }
    
    public float ascent() {
        return this.f6hd5flmyjf9;
    }
    
    @Override
    public TextTextureService.GlyphInfo glyph(final int i) {
        if (!Character.isValidCodePoint(i)) {
            return null;
        }
        if (mij4x3sgny9w(i)) {
            return new TextTextureService.GlyphInfo(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        }
        final TextTextureService.GlyphInfo glyphInfo = this.ffp9qahymf1z.get(i);
        if (glyphInfo != null) {
            return glyphInfo;
        }
        final TextTextureService.GlyphInfo m9u9ofrocz8m = this.m9u9ofrocz8m(i);
        if (m9u9ofrocz8m != null) {
            this.ffp9qahymf1z.put(i, m9u9ofrocz8m);
            return m9u9ofrocz8m;
        }
        if (i != 63) {
            return this.glyph(63);
        }
        return null;
    }
    
    ShapedLine shapeLine(final String paragraph) {
        if (paragraph == null || paragraph.isEmpty()) {
            return new ShapedLine(List.of(), 0.0f);
        }
        final ShapedLine shapedLine = this.f55osi4tbyio.get(paragraph);
        if (shapedLine != null) {
            return shapedLine;
        }
        final ArrayList coll = new ArrayList();
        float n = 0.0f;
        final Bidi bidi = new Bidi(paragraph, -2);
        final int runCount = bidi.getRunCount();
        final Integer[] objects = new Integer[runCount];
        final byte[] levels = new byte[runCount];
        for (int i = 0; i < runCount; ++i) {
            objects[i] = i;
            levels[i] = (byte)bidi.getRunLevel(i);
        }
        Bidi.reorderVisually(levels, 0, objects, 0, runCount);
        for (int j = 0; j < runCount; ++j) {
            final int intValue = objects[j];
            n += this.mdul5rb2mp0n(paragraph.substring(bidi.getRunStart(intValue), bidi.getRunLimit(intValue)), (bidi.getRunLevel(intValue) & 0x1) != 0x0, n, coll);
        }
        final ShapedLine shapedLine2 = new ShapedLine(List.copyOf((Collection<? extends ShapedGlyph>)coll), n);
        this.f55osi4tbyio.put(paragraph, shapedLine2);
        return shapedLine2;
    }
    
    float caretX(final String s, int max) {
        if (s == null || s.isEmpty()) {
            return 0.0f;
        }
        max = Math.max(0, Math.min(max, s.length()));
        final TextLayout mdfirxzd2bwz = this.mdfirxzd2bwz(s);
        TextHitInfo hit;
        if (max <= 0) {
            hit = TextHitInfo.leading(0);
        }
        else if (max >= s.length()) {
            hit = TextHitInfo.trailing(s.length() - 1);
        }
        else {
            hit = TextHitInfo.leading(max);
        }
        return mdfirxzd2bwz.getCaretInfo(hit)[0] / Float.intBitsToFloat(1119879168);
    }
    
    List<SelectionSpan> selectionSpans(final String s, int max, int max2) {
        if (s == null || s.isEmpty() || max == max2) {
            return List.of();
        }
        max = Math.max(0, Math.min(max, s.length()));
        max2 = Math.max(0, Math.min(max2, s.length()));
        if (max > max2) {
            final int n = max;
            max = max2;
            max2 = n;
        }
        final Rectangle2D bounds2D = this.mdfirxzd2bwz(s).getLogicalHighlightShape(max, max2).getBounds2D();
        if (bounds2D.isEmpty()) {
            return List.of();
        }
        return List.of(new SelectionSpan((float)bounds2D.getX() / Float.intBitsToFloat(1119879168), (float)bounds2D.getWidth() / Float.intBitsToFloat(1119879168)));
    }
    
    @Override
    public boolean isReady() {
        return this.fjkpauwthczv.valid() || !this.ffp9qahymf1z.isEmpty();
    }
    
    @Override
    public boolean colorGlyph(final TextTextureService.GlyphInfo glyphInfo) {
        return this.fj1y8bhc2lf8.contains(glyphInfo);
    }
    
    void shutdown() {
        if (this.fjkpauwthczv.valid()) {
            this.f1jf6qbv99v7.destroyTexture(this.fjkpauwthczv);
            this.fjkpauwthczv = RhiBlendStateService.TextureHandle.NONE;
        }
        this.ffp9qahymf1z.clear();
        this.f2lmmai28e3f.clear();
        this.fdj8oso9cct5.clear();
        this.f55osi4tbyio.clear();
        this.fj1y8bhc2lf8.clear();
    }
    
    private TextTextureService.GlyphInfo m9u9ofrocz8m(final int n) {
        final int m847php8r0od = this.m847php8r0od(n);
        final String str = new String(Character.toChars(m847php8r0od));
        final Font m31phuiibmxy = this.m31phuiibmxy(this.m3sw6r21f0ns(m847php8r0od));
        return this.m2vk534962b9(m31phuiibmxy, m31phuiibmxy.createGlyphVector(this.f3g8ehmjuwgq, str), false);
    }
    
    private TextTextureService.GlyphInfo mane4mw6ph29(final FontSlot fontSlot, final int n) {
        final GlyphKey glyphKey = new GlyphKey(fontSlot, n);
        final TextTextureService.GlyphInfo glyphInfo = this.f2lmmai28e3f.get(glyphKey);
        if (glyphInfo != null) {
            return glyphInfo;
        }
        final Font m31phuiibmxy = this.m31phuiibmxy(fontSlot);
        final TextTextureService.GlyphInfo m2vk534962b9 = this.m2vk534962b9(m31phuiibmxy, m31phuiibmxy.createGlyphVector(this.f3g8ehmjuwgq, new int[] { n }), false);
        if (m2vk534962b9 != null) {
            this.f2lmmai28e3f.put(glyphKey, m2vk534962b9);
            return m2vk534962b9;
        }
        return this.glyph(63);
    }
    
    private TextTextureService.GlyphInfo mdyw81jqzok(final String s) {
        final TextTextureService.GlyphInfo glyphInfo = this.fdj8oso9cct5.get(s);
        if (glyphInfo != null) {
            return glyphInfo;
        }
        final char[] charArray = s.toCharArray();
        final TextTextureService.GlyphInfo m2vk534962b9 = this.m2vk534962b9(this.f5lh7et6tzor, this.f5lh7et6tzor.layoutGlyphVector(this.f3g8ehmjuwgq, charArray, 0, charArray.length, 0), true);
        if (m2vk534962b9 != null) {
            this.fdj8oso9cct5.put(s, m2vk534962b9);
            return m2vk534962b9;
        }
        return this.glyph(63);
    }
    
    private TextTextureService.GlyphInfo m2vk534962b9(final Font font, final GlyphVector glyphVector, final boolean b) {
        final Graphics2D graphics = new BufferedImage(1, 1, 2).createGraphics();
        graphics.setFont(font);
        final FontMetrics fontMetrics = graphics.getFontMetrics();
        graphics.dispose();
        final int numGlyphs = glyphVector.getNumGlyphs();
        float n = Math.max((float)glyphVector.getLogicalBounds().getWidth(), Math.abs((float)((numGlyphs > 0) ? glyphVector.getGlyphPosition(numGlyphs) : new Point2D.Float()).getX())) / Float.intBitsToFloat(1119879168);
        if (n <= 0.0f) {
            for (int i = 0; i < numGlyphs; ++i) {
                n += Math.max(0.0f, glyphVector.getGlyphMetrics(i).getAdvanceX()) / Float.intBitsToFloat(1119879168);
            }
        }
        if (n <= 0.0f) {
            n = Math.max(0, fontMetrics.charWidth(' ')) / Float.intBitsToFloat(1119879168);
        }
        final RasterGlyph mhhpha3cbf2s = this.mhhpha3cbf2s(font, glyphVector, b);
        if (mhhpha3cbf2s == null) {
            return new TextTextureService.GlyphInfo(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, n);
        }
        final int width = mhhpha3cbf2s.image.getWidth();
        final int height = mhhpha3cbf2s.image.getHeight();
        if (width > 2048 || height > 2048) {
            return null;
        }
        final int[] mg8f5tddylxj = this.mg8f5tddylxj(width, height);
        if (mg8f5tddylxj == null) {
            return null;
        }
        final ByteBuffer allocateDirect = ByteBuffer.allocateDirect(width * height * 4);
        final boolean color = mhhpha3cbf2s.color;
        if (color) {
            for (int j = 0; j < height; ++j) {
                for (int k = 0; k < width; ++k) {
                    final int rgb = mhhpha3cbf2s.image.getRGB(k, j);
                    allocateDirect.put((byte)(rgb >> 16 & 0xFF));
                    allocateDirect.put((byte)(rgb >> 8 & 0xFF));
                    allocateDirect.put((byte)(rgb & 0xFF));
                    allocateDirect.put((byte)(rgb >>> 24 & 0xFF));
                }
            }
        }
        else {
            final float[] mpmsxinwfjb = mpmsxinwfjb(mhhpha3cbf2s.image, width, height);
            for (int length = mpmsxinwfjb.length, l = 0; l < length; ++l) {
                final int max = Math.max(0, Math.min(255, Math.round(mpmsxinwfjb[l] * Float.intBitsToFloat(1132396544))));
                allocateDirect.put((byte)max);
                allocateDirect.put((byte)max);
                allocateDirect.put((byte)max);
                allocateDirect.put((byte)(-1));
            }
        }
        allocateDirect.flip();
        this.m5bzx5wuw5eg();
        if (!this.fjkpauwthczv.valid()) {
            return null;
        }
        try (final TextureUploadState textureUploadState = new TextureUploadState()) {
            this.f1jf6qbv99v7.updateTexture(this.fjkpauwthczv, mg8f5tddylxj[0], mg8f5tddylxj[1], width, height, allocateDirect);
        }
        final TextTextureService.GlyphInfo glyphInfo = new TextTextureService.GlyphInfo(mg8f5tddylxj[0] / Float.intBitsToFloat(1157627904), mg8f5tddylxj[1] / Float.intBitsToFloat(1157627904), (mg8f5tddylxj[0] + width) / Float.intBitsToFloat(1157627904), (mg8f5tddylxj[1] + height) / Float.intBitsToFloat(1157627904), width / Float.intBitsToFloat(1119879168), height / Float.intBitsToFloat(1119879168), mhhpha3cbf2s.bearingX, mhhpha3cbf2s.bearingY, n);
        if (color) {
            this.fj1y8bhc2lf8.add(glyphInfo);
        }
        return glyphInfo;
    }
    
    private RasterGlyph mhhpha3cbf2s(final Font font, final GlyphVector glyphVector, final boolean b) {
        final int n = 480;
        final float intBitsToFloat = Float.intBitsToFloat(1128267776);
        final float intBitsToFloat2 = Float.intBitsToFloat(1128267776);
        final BufferedImage bufferedImage = new BufferedImage(n, n, 2);
        final Graphics2D graphics = bufferedImage.createGraphics();
        try {
            m6h97yut4sqd(graphics, font);
            graphics.drawGlyphVector(glyphVector, intBitsToFloat, intBitsToFloat2);
        }
        finally {
            graphics.dispose();
        }
        int min = n;
        int min2 = n;
        int max = -1;
        int max2 = -1;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (m1jt2yxxz2bg(bufferedImage.getRGB(j, i)) > 0) {
                    min = Math.min(min, j);
                    min2 = Math.min(min2, i);
                    max = Math.max(max, j);
                    max2 = Math.max(max2, i);
                }
            }
        }
        if (max < min || max2 < min2) {
            return null;
        }
        final int width = max - min + 1 + GlyphAtlas.fhygr87oq6rp * 2;
        final int height = max2 - min2 + 1 + GlyphAtlas.fhygr87oq6rp * 2;
        final boolean b2 = b && hasIntrinsicColorPixels(bufferedImage, min, min2, max, max2);
        final BufferedImage bufferedImage2 = new BufferedImage(width, height, b2 ? 2 : 10);
        final WritableRaster raster = bufferedImage2.getRaster();
        for (int k = min2; k <= max2; ++k) {
            for (int l = min; l <= max; ++l) {
                final int n2 = l - min + GlyphAtlas.fhygr87oq6rp;
                final int n3 = k - min2 + GlyphAtlas.fhygr87oq6rp;
                final int rgb = bufferedImage.getRGB(l, k);
                if (b2) {
                    bufferedImage2.setRGB(n2, n3, rgb);
                }
                else {
                    raster.setSample(n2, n3, 0, m1jt2yxxz2bg(rgb));
                }
            }
        }
        return new RasterGlyph(bufferedImage2, (min - intBitsToFloat - GlyphAtlas.fhygr87oq6rp) / Float.intBitsToFloat(1119879168), (this.f6hd5flmyjf9 * Float.intBitsToFloat(1119879168) + min2 - intBitsToFloat2 - GlyphAtlas.fhygr87oq6rp) / Float.intBitsToFloat(1119879168), b2);
    }
    
    private static void m6h97yut4sqd(final Graphics2D graphics2D, final Font font) {
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        graphics2D.setFont(font);
        graphics2D.setColor(Color.WHITE);
    }
    
    private static int m1jt2yxxz2bg(final int n) {
        return n >>> 24 & 0xFF;
    }
    
    private static int m8bt8b78dfvf(final BufferedImage bufferedImage, final int n, final int n2) {
        if (bufferedImage.getColorModel().hasAlpha()) {
            return m1jt2yxxz2bg(bufferedImage.getRGB(n, n2));
        }
        return bufferedImage.getRaster().getSample(n, n2, 0);
    }
    
    static boolean hasIntrinsicColorPixels(final BufferedImage bufferedImage, final int n, final int n2, final int n3, final int n4) {
        int n5 = 0;
        int n6 = 0;
        for (int i = n2; i <= n4; ++i) {
            for (int j = n; j <= n3; ++j) {
                final int rgb = bufferedImage.getRGB(j, i);
                if ((rgb >>> 24 & 0xFF) >= 8) {
                    ++n6;
                    final int n7 = rgb >> 16 & 0xFF;
                    final int n8 = rgb >> 8 & 0xFF;
                    final int n9 = rgb & 0xFF;
                    final int min = Math.min(n7, Math.min(n8, n9));
                    if (Math.max(n7, Math.max(n8, n9)) - min > 8 || min < 240) {
                        ++n5;
                    }
                }
            }
        }
        return n6 > 0 && n5 >= Math.max(4, n6 / 20);
    }
    
    private float mdul5rb2mp0n(final String s, final boolean b, final float n, final List<ShapedGlyph> list) {
        float n2 = n;
        final int[] ends = TextEndsService.ends(s);
        int i = 0;
        int n3 = 0;
        while (i < ends.length) {
            final int endIndex = ends[i];
            if (TextEndsService.hasEmojiPresentation(s, n3, endIndex)) {
                final TextTextureService.GlyphInfo mdyw81jqzok = this.mdyw81jqzok(s.substring(n3, endIndex));
                if (mdyw81jqzok != null) {
                    list.add(new ShapedGlyph(mdyw81jqzok, n2, 0.0f));
                }
                n2 += ((mdyw81jqzok != null) ? mdyw81jqzok.advance() : 0.0f);
                ++i;
                n3 = endIndex;
            }
            else {
                final FontSlot maxdwk7buqv = this.maxdwk7buqv(s, n3, ends[i]);
                final int m2fb1fi5vrk5 = this.m2fb1fi5vrk5(s, ends, i, maxdwk7buqv);
                n2 += this.mdjruolx889f(s.substring(n3, m2fb1fi5vrk5), maxdwk7buqv, b, n2, list);
                while (i < ends.length && ends[i] < m2fb1fi5vrk5) {
                    ++i;
                }
                if (i < ends.length && ends[i] == m2fb1fi5vrk5) {
                    ++i;
                }
                n3 = m2fb1fi5vrk5;
            }
        }
        return n2 - n;
    }
    
    private int m2fb1fi5vrk5(final String s, final int[] array, final int n, final FontSlot fontSlot) {
        int n3;
        int n2 = n3 = array[n];
        for (int i = n + 1; i < array.length; ++i) {
            if (TextEndsService.hasEmojiPresentation(s, n3, array[i])) {
                break;
            }
            if (this.maxdwk7buqv(s, n3, array[i]) != fontSlot) {
                break;
            }
            n2 = (n3 = array[i]);
        }
        return n2;
    }
    
    private float mdjruolx889f(final String s, final FontSlot fontSlot, final boolean flags, final float n, final List<ShapedGlyph> list) {
        if (s.isEmpty()) {
            return 0.0f;
        }
        final Font m31phuiibmxy = this.m31phuiibmxy(fontSlot);
        final char[] charArray = s.toCharArray();
        final GlyphVector layoutGlyphVector = m31phuiibmxy.layoutGlyphVector(this.f3g8ehmjuwgq, charArray, 0, charArray.length, flags ? 1 : 0);
        final int numGlyphs = layoutGlyphVector.getNumGlyphs();
        final Rectangle2D logicalBounds = layoutGlyphVector.getLogicalBounds();
        final float n2 = (float)Math.max(0.0, -logicalBounds.getX()) / Float.intBitsToFloat(1119879168);
        for (int i = 0; i < numGlyphs; ++i) {
            final TextTextureService.GlyphInfo mane4mw6ph29 = this.mane4mw6ph29(fontSlot, layoutGlyphVector.getGlyphCode(i));
            if (mane4mw6ph29 != null) {
                final Point2D glyphPosition = layoutGlyphVector.getGlyphPosition(i);
                list.add(new ShapedGlyph(mane4mw6ph29, n + (float)glyphPosition.getX() / Float.intBitsToFloat(1119879168) + n2, (float)glyphPosition.getY() / Float.intBitsToFloat(1119879168)));
            }
        }
        return Math.max(0.0f, Math.max((float)logicalBounds.getWidth(), Math.abs((float)layoutGlyphVector.getGlyphPosition(numGlyphs).getX())) / Float.intBitsToFloat(1119879168));
    }
    
    private int m847php8r0od(final int n) {
        if (this.f94wt352bfwh.canDisplay(n) || this.f5lh7et6tzor.canDisplay(n)) {
            return n;
        }
        if (this.f94wt352bfwh.canDisplay('�') || this.f5lh7et6tzor.canDisplay('�')) {
            return 65533;
        }
        return 63;
    }
    
    private TextLayout mdfirxzd2bwz(final String text) {
        final AttributedString attributedString = new AttributedString(text);
        final int[] ends = TextEndsService.ends(text);
        int beginIndex = 0;
        final int[] array = ends;
        for (int length = array.length, i = 0; i < length; ++i) {
            final int endIndex = array[i];
            attributedString.addAttribute(TextAttribute.FONT, this.m31phuiibmxy(this.maxdwk7buqv(text, beginIndex, endIndex)), beginIndex, endIndex);
            beginIndex = endIndex;
        }
        return new TextLayout(attributedString.getIterator(), this.f3g8ehmjuwgq);
    }
    
    private FontSlot maxdwk7buqv(final String s, final int n, final int n2) {
        if (TextEndsService.hasEmojiPresentation(s, n, n2)) {
            return FontSlot.SYSTEM;
        }
        int i = n;
        while (i < n2) {
            final int codePoint = s.codePointAt(i);
            i += Character.charCount(codePoint);
            if (m3bl6desuc8o(codePoint)) {
                continue;
            }
            if (!this.f94wt352bfwh.canDisplay(codePoint)) {
                return FontSlot.SYSTEM;
            }
        }
        return FontSlot.PRIMARY;
    }
    
    private FontSlot m3sw6r21f0ns(final int codePoint) {
        if (Character.isEmojiPresentation(codePoint)) {
            return FontSlot.SYSTEM;
        }
        if (this.f94wt352bfwh.canDisplay(codePoint)) {
            return FontSlot.PRIMARY;
        }
        if (this.f5lh7et6tzor.canDisplay(codePoint)) {
            return FontSlot.SYSTEM;
        }
        return this.f94wt352bfwh.canDisplay('?') ? FontSlot.PRIMARY : FontSlot.SYSTEM;
    }
    
    private Font m31phuiibmxy(final FontSlot fontSlot) {
        return (fontSlot == FontSlot.PRIMARY) ? this.f94wt352bfwh : this.f5lh7et6tzor;
    }
    
    private static boolean m3bl6desuc8o(final int codePoint) {
        final int type = Character.getType(codePoint);
        return type == 6 || type == 8 || type == 7 || codePoint == 8205 || codePoint == 8204 || (codePoint >= 65024 && codePoint <= 65039) || (codePoint >= 917760 && codePoint <= 917999) || (codePoint >= 127995 && codePoint <= 127999);
    }
    
    private int[] mg8f5tddylxj(final int n, final int b) {
        if (this.ff6g11lyyppx + n > 2048) {
            this.ff6g11lyyppx = 0;
            this.fdr42xz9vjae += this.f9x4n4rvoz1w + 1;
            this.f9x4n4rvoz1w = 0;
        }
        if (this.fdr42xz9vjae + b > 2048) {
            if (!this.fegb2m90q2zi) {
                CoreIsInitializedHandler.LOGGER.info("Runtime glyph atlas full for {} — will compact at end of frame", (Object)this.f7n1wzl4ohl7);
                this.fegb2m90q2zi = true;
            }
            return null;
        }
        final int[] array = { this.ff6g11lyyppx, this.fdr42xz9vjae };
        this.ff6g11lyyppx += n + 1;
        this.f9x4n4rvoz1w = Math.max(this.f9x4n4rvoz1w, b);
        return array;
    }
    
    void compactIfNeeded() {
        if (!this.fegb2m90q2zi) {
            return;
        }
        this.fegb2m90q2zi = false;
        this.ffp9qahymf1z.clear();
        this.f2lmmai28e3f.clear();
        this.fdj8oso9cct5.clear();
        this.fj1y8bhc2lf8.clear();
        this.f55osi4tbyio.clear();
        this.ff6g11lyyppx = 0;
        this.fdr42xz9vjae = 0;
        this.f9x4n4rvoz1w = 0;
        if (this.fjkpauwthczv.valid()) {
            final ByteBuffer allocateDirect = ByteBuffer.allocateDirect(16777216);
            try (final TextureUploadState textureUploadState = new TextureUploadState()) {
                this.f1jf6qbv99v7.updateTexture(this.fjkpauwthczv, 0, 0, 2048, 2048, allocateDirect);
            }
        }
    }
    
    private void m5bzx5wuw5eg() {
        if (this.fjkpauwthczv.valid()) {
            return;
        }
        final ByteBuffer allocateDirect = ByteBuffer.allocateDirect(16777216);
        try (final TextureUploadState textureUploadState = new TextureUploadState()) {
            this.fjkpauwthczv = this.f1jf6qbv99v7.createTexture(new RhiBlendStateService.TextureDescriptor(2048, 2048, RhiBlendStateService.TextureFormat.RGBA8, RhiBlendStateService.FilterMode.LINEAR, RhiBlendStateService.FilterMode.LINEAR, RhiBlendStateService.AddressMode.CLAMP), allocateDirect);
        }
    }
    
    private static Font mb27s6uvmm5f(final Font font) {
        return font.deriveFont(Map.of(TextAttribute.LIGATURES, TextAttribute.LIGATURES_ON));
    }
    
    private static boolean mij4x3sgny9w(final int codePoint) {
        return Character.isISOControl(codePoint) && codePoint != 9 && codePoint != 10 && codePoint != 13;
    }
    
    private static float[] mpmsxinwfjb(final BufferedImage bufferedImage, final int n, final int n2) {
        final float intBitsToFloat = Float.intBitsToFloat(1343554297);
        final float[] array = new float[n * n2];
        final float[] array2 = new float[n * n2];
        for (int i = 0; i < n2; ++i) {
            for (int j = 0; j < n; ++j) {
                final float n3 = m8bt8b78dfvf(bufferedImage, j, i) / Float.intBitsToFloat(1132396544);
                final int n4 = i * n + j;
                if (n3 >= 1.0f) {
                    array[n4] = 0.0f;
                    array2[n4] = intBitsToFloat;
                }
                else if (n3 <= 0.0f) {
                    array[n4] = intBitsToFloat;
                    array2[n4] = 0.0f;
                }
                else {
                    final float max = Math.max(0.0f, Float.intBitsToFloat(1056964608) - n3);
                    final float max2 = Math.max(0.0f, n3 - Float.intBitsToFloat(1056964608));
                    array[n4] = max * max;
                    array2[n4] = max2 * max2;
                }
            }
        }
        m5u1t27rohvf(array, n, n2);
        m5u1t27rohvf(array2, n, n2);
        final float[] array3 = new float[n * n2];
        for (int k = 0; k < array3.length; ++k) {
            array3[k] = Math.max(0.0f, Math.min(1.0f, Float.intBitsToFloat(1056964608) - (float)(Math.sqrt(array[k]) - Math.sqrt(array2[k])) / Float.intBitsToFloat(1094713344)));
        }
        return array3;
    }
    
    private static void m5u1t27rohvf(final float[] array, final int a, final int b) {
        final int max = Math.max(a, b);
        final float[] array2 = new float[max];
        final float[] array3 = new float[max];
        final int[] array4 = new int[max];
        final float[] array5 = new float[max + 1];
        for (int i = 0; i < a; ++i) {
            for (int j = 0; j < b; ++j) {
                array2[j] = array[j * a + i];
            }
            m4lyoozxwdip(array2, array3, array4, array5, b);
            for (int k = 0; k < b; ++k) {
                array[k * a + i] = array3[k];
            }
        }
        for (int l = 0; l < b; ++l) {
            System.arraycopy(array, l * a, array2, 0, a);
            m4lyoozxwdip(array2, array3, array4, array5, a);
            System.arraycopy(array3, 0, array, l * a, a);
        }
    }
    
    private static void m4lyoozxwdip(final float[] array, final float[] array2, final int[] array3, final float[] array4, final int n) {
        array4[array3[0] = 0] = Float.intBitsToFloat(-8388608);
        array4[1] = Float.intBitsToFloat(2139095040);
        int n2 = 0;
        for (int i = 1; i < n; ++i) {
            float n4;
            while (true) {
                final int n3 = array3[n2];
                n4 = (array[i] + i * (float)i - (array[n3] + n3 * (float)n3)) / (2.0f * i - 2.0f * n3);
                if (n4 > array4[n2]) {
                    break;
                }
                --n2;
                if (n2 < 0) {
                    n2 = 0;
                    break;
                }
            }
            ++n2;
            array3[n2] = i;
            array4[n2] = n4;
            array4[n2 + 1] = Float.intBitsToFloat(2139095040);
        }
        int n5 = 0;
        for (int j = 0; j < n; ++j) {
            while (array4[n5 + 1] < j) {
                ++n5;
            }
            final float n6 = (float)(j - array3[n5]);
            array2[j] = n6 * n6 + array[array3[n5]];
        }
    }
    
    static {
        fhygr87oq6rp = (int)Math.ceil(Double.longBitsToDouble(4618441417868443648L)) + 2;
    }
    
    record ShapedLine(List<ShapedGlyph> glyphs, float width) {}
    
    record ShapedGlyph(TextTextureService.GlyphInfo glyph, float x, float y) {}
    
    record SelectionSpan(float x, float width) {}
    
    private enum FontSlot
    {
        PRIMARY, 
        SYSTEM;
    }
    
    record GlyphKey(FontSlot slot, int glyphCode) {}
    
    record RasterGlyph(BufferedImage image, float bearingX, float bearingY, boolean color) {}
    
    private static final class TextureUploadState implements AutoCloseable
    {
        private final int f848kstdfqi7;
        private final int fhf3ezwkjfsd;
        private final int fdcdgmmpnj9t;
        private final int ffjug69t64w1;
        private final int fcuytkmhhdcr;
        private final int feuzy924db8o;
        
        private TextureUploadState() {
            this.f848kstdfqi7 = GL11.glGetInteger(34016);
            this.fhf3ezwkjfsd = GL11.glGetInteger(32873);
            this.fdcdgmmpnj9t = GL11.glGetInteger(3314);
            this.ffjug69t64w1 = GL11.glGetInteger(3315);
            this.fcuytkmhhdcr = GL11.glGetInteger(3316);
            this.feuzy924db8o = GL11.glGetInteger(3317);
        }
        
        @Override
        public void close() {
            GL11.glPixelStorei(3314, this.fdcdgmmpnj9t);
            GL11.glPixelStorei(3315, this.ffjug69t64w1);
            GL11.glPixelStorei(3316, this.fcuytkmhhdcr);
            GL11.glPixelStorei(3317, this.feuzy924db8o);
            GL13.glActiveTexture(this.f848kstdfqi7);
            GL11.glBindTexture(3553, this.fhf3ezwkjfsd);
        }
    }
}
