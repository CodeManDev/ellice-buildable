



package dev.felix.ellice.diagnostics.fatal;

import java.util.Arrays;
import java.nio.charset.StandardCharsets;

public final class FatalCodec
{
    private static final int f7qs4aefq50 = 16;
    private static final int[] fhrx8g9p33md;
    private static final int[] f711h9zk52ew;
    private static final int[] fcxpa2hms9z;
    private static final int[][] f2sxv3fiz3i7;
    private static final int[] f5lrwo2d3t8y;
    private static final int[] fdb6h9xltzsi;
    
    private FatalCodec() {
    }
    
    public static boolean[][] encode(final String s) {
        byte[] copy = (s == null) ? new byte[0] : s.getBytes(StandardCharsets.UTF_8);
        final int selectVersion = selectVersion(copy.length);
        final int byteCapacity = byteCapacity(selectVersion);
        if (copy.length > byteCapacity) {
            copy = Arrays.copyOf(copy, byteCapacity);
        }
        return m45s3n45l2vn(copy, selectVersion);
    }
    
    public static int sizeFor(final boolean[][] array) {
        return (array == null) ? 0 : array.length;
    }
    
    static int selectVersion(final int n) {
        for (int i = 1; i <= 16; ++i) {
            if (byteCapacity(i) >= n) {
                return i;
            }
        }
        return 16;
    }
    
    static int byteCapacity(final int n) {
        return Math.max(0, ((FatalCodec.fhrx8g9p33md[n] - FatalCodec.f711h9zk52ew[n]) * 8 - 4 - ((n <= 9) ? 8 : 16)) / 8);
    }
    
    private static boolean[][] m45s3n45l2vn(final byte[] array, final int n) {
        final int n2 = 21 + 4 * (n - 1);
        final boolean[][] array2 = new boolean[n2][n2];
        final boolean[][] array3 = new boolean[n2][n2];
        m4vf6f46u66x(array2, array3, n);
        final byte[] m9kmrmbqb4fd = m9kmrmbqb4fd(array, n);
        final BitWriter bitWriter = new BitWriter(m9kmrmbqb4fd.length * 8 + mij3046c8k01(n));
        final byte[] array4 = m9kmrmbqb4fd;
        for (int length = array4.length, i = 0; i < length; ++i) {
            bitWriter.append(array4[i] & 0xFF, 8);
        }
        bitWriter.append(0, mij3046c8k01(n));
        m2ajoq8oo15z(array2, array3, bitWriter);
        final int mf1cthlutq82 = mf1cthlutq82(array2, array3);
        mbv45pre12d5(array2, array3, mf1cthlutq82);
        m6lhdqk791eb(array2, array3, mf1cthlutq82);
        if (n >= 7) {
            magquq5zl99g(array2, array3, n);
        }
        return array2;
    }
    
    private static byte[] m9kmrmbqb4fd(final byte[] array, final int n) {
        final int n2 = FatalCodec.fhrx8g9p33md[n];
        final int n3 = FatalCodec.f711h9zk52ew[n];
        final int n4 = n2 - n3;
        final int n5 = FatalCodec.fcxpa2hms9z[n];
        final int n6 = n3 / n5;
        final int n7 = n4 / n5;
        final int n8 = n4 % n5;
        final int n9 = n5 - n8;
        final BitWriter bitWriter = new BitWriter(n4 * 8);
        bitWriter.append(4, 4);
        bitWriter.append(array.length, (n <= 9) ? 8 : 16);
        for (int length = array.length, i = 0; i < length; ++i) {
            bitWriter.append(array[i] & 0xFF, 8);
        }
        bitWriter.append(0, Math.min(4, n4 * 8 - bitWriter.f4xlkiesqfyt));
        while (bitWriter.f4xlkiesqfyt % 8 != 0) {
            bitWriter.append(0, 1);
        }
        int n10 = 236;
        while (bitWriter.f4xlkiesqfyt / 8 < n4) {
            bitWriter.append(n10, 8);
            n10 ^= 0xFD;
        }
        final byte[] bytes = bitWriter.toBytes(n4);
        final byte[][] array2 = new byte[n5][];
        final byte[][] array3 = new byte[n5][];
        int from = 0;
        final int[] mfkba0tf4mlj = mfkba0tf4mlj(n6);
        for (int j = 0; j < n5; ++j) {
            final int n11 = n7 + ((j >= n9) ? 1 : 0);
            array2[j] = Arrays.copyOfRange(bytes, from, from + n11);
            array3[j] = mj7uqpem1bty(array2[j], mfkba0tf4mlj);
            from += n11;
        }
        final byte[] array4 = new byte[n2];
        int n12 = 0;
        for (int n13 = n7 + ((n8 > 0) ? 1 : 0), k = 0; k < n13; ++k) {
            for (int l = 0; l < n5; ++l) {
                if (k < array2[l].length) {
                    array4[n12++] = array2[l][k];
                }
            }
        }
        for (int n14 = 0; n14 < n6; ++n14) {
            for (int n15 = 0; n15 < n5; ++n15) {
                array4[n12++] = array3[n15][n14];
            }
        }
        return array4;
    }
    
    private static void m4vf6f46u66x(final boolean[][] array, final boolean[][] array2, final int n) {
        final int length = array.length;
        m6zer76e6284(array, array2, 0, 0);
        m6zer76e6284(array, array2, length - 7, 0);
        m6zer76e6284(array, array2, 0, length - 7);
        m7m0upye4n4m(array2, length);
        for (int i = 8; i < length - 8; ++i) {
            final boolean b = (i & 0x1) == 0x0;
            m3tkpyd0u6ki(array, array2, 6, i, b);
            m3tkpyd0u6ki(array, array2, i, 6, b);
        }
        final int[] array4;
        final int[] array3 = array4 = FatalCodec.f2sxv3fiz3i7[n];
        for (int length2 = array4.length, j = 0; j < length2; ++j) {
            final int n2 = array4[j];
            final int[] array5 = array3;
            for (int length3 = array5.length, k = 0; k < length3; ++k) {
                final int n3 = array5[k];
                if (!m53qtyzl08la(n2, n3, length)) {
                    m4aph4u9a1wj(array, array2, n2, n3);
                }
            }
        }
        m3tkpyd0u6ki(array, array2, 4 * n + 9, 8, true);
        maqb6v4a0wts(array2, length);
        if (n >= 7) {
            mjmomgl7yy3m(array2, length);
        }
    }
    
    private static boolean m53qtyzl08la(final int n, final int n2, final int n3) {
        return (n == 6 && n2 == 6) || (n == 6 && n2 == n3 - 7) || (n == n3 - 7 && n2 == 6);
    }
    
    private static void m6zer76e6284(final boolean[][] array, final boolean[][] array2, final int n, final int n2) {
        for (int i = -1; i <= 7; ++i) {
            for (int j = -1; j <= 7; ++j) {
                final int n3 = n + i;
                final int n4 = n2 + j;
                if (n3 >= 0 && n4 >= 0 && n3 < array.length) {
                    if (n4 < array.length) {
                        m3tkpyd0u6ki(array, array2, n3, n4, j >= 0 && j <= 6 && i >= 0 && i <= 6 && (j == 0 || j == 6 || i == 0 || i == 6 || (j >= 2 && j <= 4 && i >= 2 && i <= 4)));
                    }
                }
            }
        }
    }
    
    private static void m7m0upye4n4m(final boolean[][] array, final int n) {
        for (int i = 0; i < 8; ++i) {
            ma2sige1qpv9(array, 7, i);
            ma2sige1qpv9(array, i, 7);
            ma2sige1qpv9(array, 7, n - 1 - i);
            ma2sige1qpv9(array, i, n - 8);
            ma2sige1qpv9(array, n - 8, i);
            ma2sige1qpv9(array, n - 1 - i, 7);
        }
    }
    
    private static void m4aph4u9a1wj(final boolean[][] array, final boolean[][] array2, final int n, final int n2) {
        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                m3tkpyd0u6ki(array, array2, n + i, n2 + j, Math.max(Math.abs(j), Math.abs(i)) != 1);
            }
        }
    }
    
    private static void maqb6v4a0wts(final boolean[][] array, final int n) {
        for (int i = 0; i <= 8; ++i) {
            ma2sige1qpv9(array, 8, i);
            ma2sige1qpv9(array, i, 8);
            ma2sige1qpv9(array, 8, n - 1 - i);
            ma2sige1qpv9(array, n - 1 - i, 8);
        }
    }
    
    private static void mjmomgl7yy3m(final boolean[][] array, final int n) {
        for (int i = 0; i < 6; ++i) {
            for (int j = 0; j < 3; ++j) {
                ma2sige1qpv9(array, i, n - 11 + j);
                ma2sige1qpv9(array, n - 11 + j, i);
            }
        }
    }
    
    private static void m2ajoq8oo15z(final boolean[][] array, final boolean[][] array2, final BitWriter bitWriter) {
        final int length = array.length;
        int n = 0;
        int n2 = -1;
        int n3 = length - 1;
        for (int i = length - 1; i > 0; i -= 2) {
            if (i == 6) {
                --i;
            }
            do {
                for (int j = 0; j < 2; ++j) {
                    final int n4 = i - j;
                    if (!array2[n3][n4]) {
                        array[n3][n4] = (n < bitWriter.f4xlkiesqfyt && bitWriter.get(n));
                        ++n;
                    }
                }
                n3 += n2;
            } while (n3 >= 0 && n3 < length);
            n3 -= n2;
            n2 = -n2;
        }
    }
    
    private static int mf1cthlutq82(final boolean[][] array, final boolean[][] array2) {
        int n = 0;
        int n2 = Integer.MAX_VALUE;
        for (int i = 0; i < 8; ++i) {
            mbv45pre12d5(array, array2, i);
            m6lhdqk791eb(array, array2, i);
            final int m7m517ksp8ck = m7m517ksp8ck(array);
            m6lhdqk791eb(array, array2, 0);
            mbv45pre12d5(array, array2, i);
            if (m7m517ksp8ck < n2) {
                n2 = m7m517ksp8ck;
                n = i;
            }
        }
        return n;
    }
    
    private static void mbv45pre12d5(final boolean[][] array, final boolean[][] array2, final int n) {
        for (int length = array.length, i = 0; i < length; ++i) {
            for (int j = 0; j < length; ++j) {
                if (!array2[i][j]) {
                    if (m68yh8x6dmat(n, i, j)) {
                        array[i][j] = !array[i][j];
                    }
                }
            }
        }
    }
    
    private static boolean m68yh8x6dmat(final int n, final int n2, final int n3) {
        return switch (n) {
            case 0 -> (n2 + n3 & 0x1) == 0x0;
            case 1 -> (n2 & 0x1) == 0x0;
            case 2 -> n3 % 3 == 0;
            case 3 -> (n2 + n3) % 3 == 0;
            case 4 -> (n2 / 2 + n3 / 3 & 0x1) == 0x0;
            case 5 -> n2 * n3 % 2 + n2 * n3 % 3 == 0;
            case 6 -> (n2 * n3 % 2 + n2 * n3 % 3 & 0x1) == 0x0;
            default -> ((n2 + n3 & 0x1) + n2 * n3 % 3 & 0x1) == 0x0;
        };
    }
    
    private static void m6lhdqk791eb(final boolean[][] array, final boolean[][] array2, final int n) {
        final int mgr0hh5wwo9y = mgr0hh5wwo9y(n);
        final int length = array.length;
        final int[][] array3 = { { 8, 0 }, { 8, 1 }, { 8, 2 }, { 8, 3 }, { 8, 4 }, { 8, 5 }, { 8, 7 }, { 8, 8 }, { 7, 8 }, { 5, 8 }, { 4, 8 }, { 3, 8 }, { 2, 8 }, { 1, 8 }, { 0, 8 } };
        for (int i = 0; i < 15; ++i) {
            m3tkpyd0u6ki(array, array2, array3[i][0], array3[i][1], (mgr0hh5wwo9y >> i & 0x1) != 0x0);
        }
        for (int j = 0; j < 7; ++j) {
            m3tkpyd0u6ki(array, array2, length - 1 - j, 8, (mgr0hh5wwo9y >> j & 0x1) != 0x0);
        }
        for (int k = 7; k < 15; ++k) {
            m3tkpyd0u6ki(array, array2, 8, length - 15 + k, (mgr0hh5wwo9y >> k & 0x1) != 0x0);
        }
        m3tkpyd0u6ki(array, array2, length - 8, 8, true);
    }
    
    private static void magquq5zl99g(final boolean[][] array, final boolean[][] array2, final int n) {
        final int maky8hoylvx8 = maky8hoylvx8(n);
        final int length = array.length;
        int n2 = 0;
        for (int i = 0; i < 6; ++i) {
            for (int j = 0; j < 3; ++j) {
                final boolean b = (maky8hoylvx8 >> n2 & 0x1) != 0x0;
                m3tkpyd0u6ki(array, array2, i, length - 11 + j, b);
                m3tkpyd0u6ki(array, array2, length - 11 + j, i, b);
                ++n2;
            }
        }
    }
    
    private static int mgr0hh5wwo9y(final int n) {
        final int n2 = n & 0x7;
        int n3 = n2 << 10;
        for (int i = 14; i >= 10; --i) {
            if ((n3 >> i & 0x1) != 0x0) {
                n3 ^= 1335 << i - 10;
            }
        }
        return (n2 << 10 | (n3 & 0x3FF)) ^ 0x5412;
    }
    
    private static int maky8hoylvx8(final int n) {
        int n2 = n << 12;
        for (int i = 17; i >= 12; --i) {
            if ((n2 >> i & 0x1) != 0x0) {
                n2 ^= 7973 << i - 12;
            }
        }
        return n << 12 | (n2 & 0xFFF);
    }
    
    private static int m7m517ksp8ck(final boolean[][] array) {
        final int length = array.length;
        int n = 0;
        for (int i = 0; i < length; ++i) {
            n = n + mha09mvj1kes(array, i, true) + mha09mvj1kes(array, i, false);
        }
        for (int j = 0; j < length - 1; ++j) {
            for (int k = 0; k < length - 1; ++k) {
                final boolean b = array[j][k];
                if (b == array[j][k + 1] && b == array[j + 1][k] && b == array[j + 1][k + 1]) {
                    n += 3;
                }
            }
        }
        for (int l = 0; l < length; ++l) {
            n = n + mdctf5uqr67r(array, l, true) + mdctf5uqr67r(array, l, false);
        }
        int n2 = 0;
        for (int length2 = array.length, n3 = 0; n3 < length2; ++n3) {
            final boolean[] array2 = array[n3];
            for (int length3 = array2.length, n4 = 0; n4 < length3; ++n4) {
                if (array2[n4]) {
                    ++n2;
                }
            }
        }
        return n + Math.abs(n2 * 100 / (length * length) - 50) / 5 * 10;
    }
    
    private static int mha09mvj1kes(final boolean[][] array, final int n, final boolean b) {
        final int length = array.length;
        int n2 = 0;
        boolean m58wu9gv852p = m58wu9gv852p(array, n, 0, b);
        int n3 = 1;
        for (int i = 1; i < length; ++i) {
            final boolean m58wu9gv852p2 = m58wu9gv852p(array, n, i, b);
            if (m58wu9gv852p2 == m58wu9gv852p) {
                ++n3;
            }
            else {
                if (n3 >= 5) {
                    n2 += 3 + (n3 - 5);
                }
                m58wu9gv852p = m58wu9gv852p2;
                n3 = 1;
            }
        }
        if (n3 >= 5) {
            n2 += 3 + (n3 - 5);
        }
        return n2;
    }
    
    private static int mdctf5uqr67r(final boolean[][] array, final int n, final boolean b) {
        final int length = array.length;
        int n2 = 0;
        int n3 = 0;
        for (int i = 0; i < length; ++i) {
            n3 = ((n3 << 1 & 0x7FF) | (m58wu9gv852p(array, n, i, b) ? 1 : 0));
            if (i >= 10 && (n3 == 93 || n3 == 1488)) {
                n2 += 40;
            }
        }
        return n2;
    }
    
    private static boolean m58wu9gv852p(final boolean[][] array, final int n, final int n2, final boolean b) {
        return b ? array[n][n2] : array[n2][n];
    }
    
    private static void m3tkpyd0u6ki(final boolean[][] array, final boolean[][] array2, final int n, final int n2, final boolean b) {
        array[n][n2] = b;
        array2[n][n2] = true;
    }
    
    private static void ma2sige1qpv9(final boolean[][] array, final int n, final int n2) {
        if (n >= 0 && n2 >= 0 && n < array.length && n2 < array.length) {
            array[n][n2] = true;
        }
    }
    
    private static int mij3046c8k01(final int n) {
        if (n >= 2 && n <= 6) {
            return 7;
        }
        if (n >= 14) {
            return 3;
        }
        return 0;
    }
    
    private static int[] mfkba0tf4mlj(final int n) {
        int[] array = { 1 };
        for (int i = 0; i < n; ++i) {
            final int[] array2 = new int[array.length + 1];
            for (int j = 0; j < array.length; ++j) {
                final int[] array3 = array2;
                final int n2 = j;
                array3[n2] ^= array[j];
                final int[] array4 = array2;
                final int n3 = j + 1;
                array4[n3] ^= mgkx4w4v7tw(array[j], FatalCodec.f5lrwo2d3t8y[i]);
            }
            array = array2;
        }
        return array;
    }
    
    private static byte[] mj7uqpem1bty(final byte[] array, final int[] array2) {
        final int n = array2.length - 1;
        final int[] array3 = new int[n];
        for (int length = array.length, i = 0; i < length; ++i) {
            final int n2 = (array[i] & 0xFF) ^ array3[0];
            System.arraycopy(array3, 1, array3, 0, n - 1);
            array3[n - 1] = 0;
            if (n2 != 0) {
                for (int j = 0; j < n; ++j) {
                    final int[] array4 = array3;
                    final int n3 = j;
                    array4[n3] ^= mgkx4w4v7tw(array2[j + 1], n2);
                }
            }
        }
        final byte[] array5 = new byte[n];
        for (int k = 0; k < n; ++k) {
            array5[k] = (byte)array3[k];
        }
        return array5;
    }
    
    private static int mgkx4w4v7tw(final int n, final int n2) {
        if (n == 0 || n2 == 0) {
            return 0;
        }
        return FatalCodec.f5lrwo2d3t8y[FatalCodec.fdb6h9xltzsi[n] + FatalCodec.fdb6h9xltzsi[n2]];
    }
    
    static {
        fhrx8g9p33md = new int[] { 0, 26, 44, 70, 100, 134, 172, 196, 242, 292, 346, 404, 466, 532, 581, 655, 733 };
        f711h9zk52ew = new int[] { 0, 10, 16, 26, 36, 48, 64, 72, 88, 110, 130, 150, 176, 198, 216, 240, 280 };
        fcxpa2hms9z = new int[] { 0, 1, 1, 1, 2, 2, 4, 4, 4, 5, 5, 5, 8, 9, 9, 10, 10 };
        f2sxv3fiz3i7 = new int[][] { new int[0], new int[0], { 6, 18 }, { 6, 22 }, { 6, 26 }, { 6, 30 }, { 6, 34 }, { 6, 22, 38 }, { 6, 24, 42 }, { 6, 26, 46 }, { 6, 28, 50 }, { 6, 30, 54 }, { 6, 32, 58 }, { 6, 34, 62 }, { 6, 26, 46, 66 }, { 6, 26, 48, 70 }, { 6, 26, 50, 74 } };
        f5lrwo2d3t8y = new int[512];
        fdb6h9xltzsi = new int[256];
        int n = 1;
        for (int i = 0; i < 255; ++i) {
            FatalCodec.f5lrwo2d3t8y[i] = n;
            FatalCodec.fdb6h9xltzsi[n] = i;
            n <<= 1;
            if ((n & 0x100) != 0x0) {
                n ^= 0x11D;
            }
        }
        System.arraycopy(FatalCodec.f5lrwo2d3t8y, 0, FatalCodec.f5lrwo2d3t8y, 255, 256);
        FatalCodec.f5lrwo2d3t8y[510] = FatalCodec.f5lrwo2d3t8y[255];
        FatalCodec.f5lrwo2d3t8y[511] = FatalCodec.f5lrwo2d3t8y[256];
    }
    
    private static final class BitWriter
    {
        private final boolean[] fjornbyrg2by;
        private int f4xlkiesqfyt;
        
        BitWriter(final int a) {
            this.fjornbyrg2by = new boolean[Math.max(a, 8)];
        }
        
        void append(final int n, final int n2) {
            for (int i = n2 - 1; i >= 0; --i) {
                if (this.f4xlkiesqfyt < this.fjornbyrg2by.length) {
                    this.fjornbyrg2by[this.f4xlkiesqfyt] = ((n >> i & 0x1) != 0x0);
                }
                ++this.f4xlkiesqfyt;
            }
        }
        
        boolean get(final int n) {
            return n < this.fjornbyrg2by.length && this.fjornbyrg2by[n];
        }
        
        byte[] toBytes(final int n) {
            final byte[] array = new byte[n];
            for (int n2 = 0; n2 < n * 8 && n2 < this.f4xlkiesqfyt; ++n2) {
                if (this.fjornbyrg2by[n2]) {
                    final byte[] array2 = array;
                    final int n3 = n2 / 8;
                    array2[n3] |= (byte)(128 >> n2 % 8);
                }
            }
            return array;
        }
    }
}
