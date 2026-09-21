package dev.felix.ellice.ui.text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class TextCodec {
  public static final char PREFIX = '§';
  private static final int[] fcn1vkkl3fc;

  private TextCodec() {}

  public static boolean contains(final String s) {
    return s != null && s.indexOf(167) >= 0;
  }

  public static String normalizeWhite(final String s) {
    if (s == null || s.isEmpty() || !contains(s)) {
      return s;
    }
    final StringBuilder sb = new StringBuilder(s.length());
    int i = 0;
    while (i < s.length()) {
      if (s.charAt(i) != '§' || i + 1 >= s.length()) {
        sb.append(s.charAt(i++));
      } else {
        final char lowerCase = Character.toLowerCase(s.charAt(i + 1));
        if (lowerCase == 'f') {
          sb.append('§').append('r');
          i += 2;
        } else {
          if (lowerCase == 'x') {
            final int me3xkzp14nbq = me3xkzp14nbq(s, i);
            if (me3xkzp14nbq == 16777215) {
              sb.append('§').append('r');
              i += 14;
              continue;
            }
            if (me3xkzp14nbq != -1) {
              sb.append(s, i, i + 14);
              i += 14;
              continue;
            }
          }
          sb.append('§').append(s.charAt(i + 1));
          i += 2;
        }
      }
    }
    return sb.toString();
  }

  public static List<Segment> parse(final String s, final int n, final TextData textData) {
    if (s == null || s.isEmpty()) {
      return List.of();
    }
    final TextData textData2 = (textData == null) ? TextData.NONE : textData;
    int n2 = n;
    boolean b = textData2.variant() == TextMode.BOLD || textData2.variant() == TextMode.BOLD_ITALIC;
    boolean b2 =
        textData2.variant() == TextMode.ITALIC || textData2.variant() == TextMode.BOLD_ITALIC;
    final boolean b3 = b;
    final boolean b4 = b2;
    final StringBuilder sb = new StringBuilder();
    final ArrayList coll = new ArrayList();
    int i = 0;
    while (i < s.length()) {
      final char char1 = s.charAt(i);
      if (char1 != '§' || i + 1 >= s.length()) {
        sb.append(char1);
        ++i;
      } else {
        final char lowerCase = Character.toLowerCase(s.charAt(i + 1));
        int n3 = n2;
        boolean b5 = b;
        boolean b6 = b2;
        int n4 = 2;
        if (lowerCase == 'x') {
          final int me3xkzp14nbq = me3xkzp14nbq(s, i);
          if (me3xkzp14nbq != -1) {
            n3 = ((n & 0xFF000000) | me3xkzp14nbq);
            b5 = b3;
            b6 = b4;
            n4 = 14;
          }
        } else {
          final int m37iy4fwmt8d = m37iy4fwmt8d(lowerCase);
          if (m37iy4fwmt8d >= 0) {
            n3 = ((n & 0xFF000000) | TextCodec.fcn1vkkl3fc[m37iy4fwmt8d]);
            b5 = b3;
            b6 = b4;
          } else if (lowerCase == 'r') {
            n3 = n;
            b5 = b3;
            b6 = b4;
          } else if (lowerCase == 'l') {
            b5 = true;
          } else if (lowerCase == 'o') {
            b6 = true;
          } else if (lowerCase != 'k' && lowerCase != 'm') {
            if (lowerCase != 'n') {
              sb.append(char1);
              ++i;
              continue;
            }
          }
        }
        m5l8sh49s54x(coll, sb, n2, mgc0ayx2kbkz(textData2, b, b2));
        n2 = n3;
        b = b5;
        b2 = b6;
        i += n4;
      }
    }
    m5l8sh49s54x(coll, sb, n2, mgc0ayx2kbkz(textData2, b, b2));
    return (List<Segment>) List.copyOf((Collection<?>) coll);
  }

  private static void m5l8sh49s54x(
      final List<Segment> list, final StringBuilder sb, final int n, final TextData textData) {
    if (sb.length() == 0) {
      return;
    }
    list.add(new Segment(sb.toString(), n, textData));
    sb.setLength(0);
  }

  private static TextData mgc0ayx2kbkz(final TextData textData, final boolean b, final boolean b2) {
    return textData.withVariant(TextMode.of(b, b2));
  }

  private static int m37iy4fwmt8d(final char c) {
    if (c >= '0' && c <= '9') {
      return c - '0';
    }
    if (c >= 'a' && c <= 'f') {
      return c - 'a' + 10;
    }
    return -1;
  }

  private static int me3xkzp14nbq(final String s, final int n) {
    if (n + 13 >= s.length()) {
      return -1;
    }
    int n2 = 0;
    for (int i = 0; i < 6; ++i) {
      if (s.charAt(n + 2 + i * 2) != '§') {
        return -1;
      }
      final int digit = Character.digit(s.charAt(n + 3 + i * 2), 16);
      if (digit < 0) {
        return -1;
      }
      n2 = (n2 << 4 | digit);
    }
    return n2;
  }

  static {
    fcn1vkkl3fc =
        new int[] {
          0, 170, 43520, 43690, 11141120, 11141290, 16755200, 11184810, 5592405, 5592575, 5635925,
          5636095, 16733525, 16733695, 16777045, 16777215
        };
  }

  record Segment(String text, int color, TextData style) {}
}
