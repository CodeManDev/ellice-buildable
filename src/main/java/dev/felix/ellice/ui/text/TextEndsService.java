package dev.felix.ellice.ui.text;

import java.util.ArrayList;

public final class TextEndsService {
  private TextEndsService() {}

  public static int[] ends(final String s) {
    if (s == null || s.isEmpty()) {
      return new int[0];
    }
    final ArrayList list = new ArrayList();
    int i = 0;
    int n = 0;
    while (i < s.length()) {
      final int n2 = i;
      final int codePoint = s.codePointAt(i);
      i += Character.charCount(codePoint);
      final boolean checkCondition4 = checkCondition4(codePoint);
      n = (checkCondition4 ? (n + 1) : 0);
      while (i < s.length()) {
        final int codePoint2 = s.codePointAt(i);
        if (checkCondition(codePoint2)
            || checkCondition2(codePoint2)
            || checkCondition3(codePoint2)
            || checkCondition5(codePoint2)
            || codePoint2 == 8419) {
          i += Character.charCount(codePoint2);
        } else if (codePoint2 == 8205) {
          i += Character.charCount(codePoint2);
          if (i >= s.length()) {
            continue;
          }
          i += Character.charCount(s.codePointAt(i));
        } else {
          if (!checkCondition4 || n % 2 != 1 || !checkCondition4(codePoint2)) {
            break;
          }
          i += Character.charCount(codePoint2);
          ++n;
        }
      }
      if (i > n2) {
        list.add(i);
      }
    }
    final int[] array = new int[list.size()];
    for (int j = 0; j < list.size(); ++j) {
      array[j] = (int) list.get(j);
    }
    return array;
  }

  public static int previousBoundary(final String s, int min) {
    if (s == null || s.isEmpty() || min <= 0) {
      return 0;
    }
    min = Math.min(min, s.length());
    int n = 0;
    final int[] ends = ends(s);
    for (int length = ends.length, i = 0; i < length; ++i) {
      final int n2 = ends[i];
      if (n2 >= min) {
        return n;
      }
      n = n2;
    }
    return n;
  }

  public static int nextBoundary(final String s, int max) {
    if (s == null || s.isEmpty()) {
      return 0;
    }
    max = Math.max(0, Math.min(max, s.length()));
    final int[] ends = ends(s);
    for (int length = ends.length, i = 0; i < length; ++i) {
      final int n = ends[i];
      if (n > max) {
        return n;
      }
    }
    return s.length();
  }

  public static int clampToBoundary(final String s, int max) {
    if (s == null || s.isEmpty()) {
      return 0;
    }
    max = Math.max(0, Math.min(max, s.length()));
    int n = 0;
    final int[] ends = ends(s);
    for (int length = ends.length, i = 0; i < length; ++i) {
      final int n2 = ends[i];
      if (n2 == max) {
        return max;
      }
      if (n2 > max) {
        return n;
      }
      n = n2;
    }
    return s.length();
  }

  public static boolean isEmojiLike(final int codePoint) {
    return Character.isEmoji(codePoint);
  }

  public static boolean hasEmojiPresentation(final String s, final int n, final int n2) {
    boolean b = false;
    boolean b2 = false;
    boolean b3 = false;
    boolean b4 = false;
    int i = n;
    while (i < n2) {
      final int codePoint = s.codePointAt(i);
      i += Character.charCount(codePoint);
      if (codePoint == 65038) {
        b4 = true;
      }
      if (codePoint == 65039 || codePoint == 8419) {
        b2 = true;
      }
      b3 |= Character.isEmoji(codePoint);
      b |= Character.isEmojiPresentation(codePoint);
    }
    return !b4 && (b || (b2 && b3));
  }

  private static boolean checkCondition(final int codePoint) {
    final int type = Character.getType(codePoint);
    return type == 6 || type == 8 || type == 7;
  }

  private static boolean checkCondition2(final int n) {
    return (n >= 65024 && n <= 65039) || (n >= 917760 && n <= 917999);
  }

  private static boolean checkCondition3(final int n) {
    return n >= 127995 && n <= 127999;
  }

  private static boolean checkCondition4(final int n) {
    return n >= 127462 && n <= 127487;
  }

  private static boolean checkCondition5(final int n) {
    return n >= 917536 && n <= 917631;
  }
}
