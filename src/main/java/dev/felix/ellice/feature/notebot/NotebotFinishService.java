


package dev.felix.ellice.feature.notebot;

import dev.felix.ellice.feature.notebot.NotebotTitleService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public final class NotebotFinishService {
    public static final int SAMPLE_RATE = 22050;
    private static final int fioym2dmhh8t = 4096;
    private static final int f7d26a06ycj5 = 36;
    private static final int fb4n2n57xd4f = 96;
    private final float[] f8a7mywmli4u = new float[4096];
    private final double[] f4yxldyl6rgm = new double[4096];
    private final double[] f9yqfr34bi0f = new double[4096];
    private final double[] fhke891xrdsp = new double[2048];
    private final double[] f81bnsahj396 = new double[4096];
    private final int[] fzlm8cfati9 = new int[128];
    private final int[] fj4no505hrja = new int[128];
    private final int[] f4dfsjki7xng = new int[128];
    private final float[] f1i4phjkf2w4 = new float[128];
    private final List<NotebotTitleService.Note> f182lfdt8sdn = new ArrayList<NotebotTitleService.Note>();
    private final List<Float> f15qr2mnhrz5 = new ArrayList<Float>();
    private int f5edmtj0cskv;
    private long fcj9o4lnkq3j;
    private int fh7296qfkz2a;

    public NotebotFinishService() {
        for (int i = 0; i < 4096; ++i) {
            this.f81bnsahj396[i] = Double.longBitsToDouble(4602678819172646912L) - Double.longBitsToDouble(4602678819172646912L) * Math.cos(Double.longBitsToDouble(4618760256179416344L) * (double)i / Double.longBitsToDouble(4661223415305207808L));
        }
        Arrays.fill(this.fzlm8cfati9, -100);
        Arrays.fill(this.f4dfsjki7xng, -100);
    }

    public void accept(float f) {
        this.f8a7mywmli4u[this.f5edmtj0cskv] = Float.isFinite(f) ? Math.max(Float.intBitsToFloat(-1082130432), Math.min(1.0f, f)) : 0.0f;
        this.f5edmtj0cskv = this.f5edmtj0cskv + 1 & 0xFFF;
        ++this.fcj9o4lnkq3j;
        long l = 2048L + Math.round((double)(this.fh7296qfkz2a * 22050) / Double.longBitsToDouble(0x4034000000000000L));
        if (this.fcj9o4lnkq3j >= l) {
            this.mydhifje898(this.fh7296qfkz2a++);
        }
    }

    public NotebotTitleService finish(String string) {
        int n;
        int n2;
        long l = this.fcj9o4lnkq3j;
        for (n2 = 0; n2 < 2048; ++n2) {
            this.accept(0.0f);
        }
        n2 = Math.max(1, (int)Math.ceil((double)l * Double.longBitsToDouble(0x4034000000000000L) / Double.longBitsToDouble(4671790271803949056L)));
        float[] fArray = new float[512];
        float f = Float.intBitsToFloat(953267991);
        int n3 = Math.min(n2, this.f15qr2mnhrz5.size());
        for (n = 0; n < fArray.length && n3 > 0; ++n) {
            int n4 = n * n3 / fArray.length;
            int n5 = Math.min(n3, Math.max(n4 + 1, (n + 1) * n3 / fArray.length));
            for (int i = n4; i < n5; ++i) {
                fArray[n] = Math.max(fArray[n], this.f15qr2mnhrz5.get(i).floatValue());
            }
            f = Math.max(f, fArray[n]);
        }
        n = 0;
        while (n < fArray.length) {
            int n6 = n++;
            fArray[n6] = fArray[n6] / f;
        }
        return new NotebotTitleService(string, n2, this.f182lfdt8sdn, fArray);
    }

    private void mydhifje898(int n2) {
        double d = 0.0;
        for (int i = 0; i < 4096; ++i) {
            float f = this.f8a7mywmli4u[this.f5edmtj0cskv + i & 0xFFF];
            d += (double)(f * f);
            this.f4yxldyl6rgm[i] = (double)f * this.f81bnsahj396[i];
            this.f9yqfr34bi0f[i] = 0.0;
        }
        float f = (float)Math.sqrt(d / Double.longBitsToDouble(0x40B0000000000000L));
        this.f15qr2mnhrz5.add(Float.valueOf(f));
        if (f < Float.intBitsToFloat(994352038)) {
            return;
        }
        NotebotFinishService.me1k4mdox2za(this.f4yxldyl6rgm, this.f9yqfr34bi0f);
        double d2 = 0.0;
        double d3 = 0.0;
        for (int i = 1; i < this.fhke891xrdsp.length; ++i) {
            this.fhke891xrdsp[i] = Math.hypot(this.f4yxldyl6rgm[i], this.f9yqfr34bi0f[i]);
            d3 += this.fhke891xrdsp[i];
            d2 = Math.max(d2, this.fhke891xrdsp[i]);
        }
        d3 /= (double)this.fhke891xrdsp.length;
        double[] dArray = new double[128];
        for (int i = 2; i < this.fhke891xrdsp.length - 1; ++i) {
            double d4 = this.fhke891xrdsp[i];
            if (d4 < d2 * Double.longBitsToDouble(4591870180066957722L) || d4 < d3 * Double.longBitsToDouble(4619567317775286272L) || d4 <= this.fhke891xrdsp[i - 1] || d4 < this.fhke891xrdsp[i + 1]) continue;
            double d5 = Math.log(Math.max(Double.longBitsToDouble(4457293557087583675L), this.fhke891xrdsp[i - 1]));
            double d6 = Math.log(Math.max(Double.longBitsToDouble(4457293557087583675L), d4));
            double d7 = Math.log(Math.max(Double.longBitsToDouble(4457293557087583675L), this.fhke891xrdsp[i + 1]));
            double d8 = d5 - Double.longBitsToDouble(0x4000000000000000L) * d6 + d7;
            double d9 = Math.abs(d8) < Double.longBitsToDouble(4457293557087583675L) ? 0.0 : Double.longBitsToDouble(4602678819172646912L) * (d5 - d7) / d8;
            double d10 = ((double)i + Math.max(Double.longBitsToDouble(-4620693217682128896L), Math.min(Double.longBitsToDouble(4602678819172646912L), d9))) * Double.longBitsToDouble(4671790271803949056L) / Double.longBitsToDouble(0x40B0000000000000L);
            double d11 = Double.longBitsToDouble(4634555860285128704L) + Double.longBitsToDouble(4622945017495814144L) * Math.log(d10 / Double.longBitsToDouble(4646448178051153920L)) / Math.log(Double.longBitsToDouble(0x4000000000000000L));
            int n3 = (int)Math.round(d11);
            if (n3 < 36 || n3 > 96 || Math.abs(d11 - (double)n3) > Double.longBitsToDouble(4600877379321698714L)) continue;
            dArray[n3] = Math.max(dArray[n3], d4);
        }
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        double[] dArray2 = (double[])dArray.clone();
        for (int i = 36; i <= 96; ++i) {
            if (dArray[i] == 0.0) continue;
            for (int j = 36; j < i - 2; ++j) {
                double d12;
                long l;
                if (dArray[j] < dArray[i] * Double.longBitsToDouble(4597094355634707497L) || (l = Math.round(d12 = Math.pow(Double.longBitsToDouble(0x4000000000000000L), (double)(i - j) / Double.longBitsToDouble(4622945017495814144L)))) < 2L || l > 6L || !(Math.abs(d12 - (double)l) < Double.longBitsToDouble(4585204852618449388L))) continue;
                int n4 = i;
                dArray2[n4] = dArray2[n4] * Double.longBitsToDouble(4585925428558828667L);
                break;
            }
            arrayList.add(i);
        }
        arrayList.sort(Comparator.comparingDouble((Integer n) -> dArray2[n]).reversed());
        double d13 = arrayList.isEmpty() ? 0.0 : dArray2[(Integer)arrayList.getFirst()];
        int n5 = 0;
        Iterator iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            int n6;
            int n7 = (Integer)iterator.next();
            if (n5 == 3 || dArray2[n7] < d13 * Double.longBitsToDouble(4596373779694328218L)) break;
            ++n5;
            int n8 = n2 - this.fzlm8cfati9[n7];
            this.fj4no505hrja[n7] = n8 == 1 ? this.fj4no505hrja[n7] + 1 : 1;
            float f2 = (float)Math.min(1.0, dArray[n7] / Double.longBitsToDouble(0x4090000000000000L));
            int n9 = n6 = f2 > this.f1i4phjkf2w4[n7] * Float.intBitsToFloat(1071644672) && n2 - this.f4dfsjki7xng[n7] >= 3 ? 1 : 0;
            if (this.fj4no505hrja[n7] >= 2 && (n2 - this.f4dfsjki7xng[n7] >= 8 || n2 - this.f4dfsjki7xng[n7] >= 3 && (this.fj4no505hrja[n7] == 2 || n6 != 0))) {
                this.f182lfdt8sdn.add(new NotebotTitleService.Note(Math.max(0, n2 - 1), n7, Math.max(Float.intBitsToFloat(981668463), f2)));
                this.f4dfsjki7xng[n7] = n2;
            }
            this.fzlm8cfati9[n7] = n2;
            this.f1i4phjkf2w4[n7] = f2;
        }
    }

    private static void me1k4mdox2za(double[] dArray, double[] dArray2) {
        double d;
        int n;
        int n2 = dArray.length;
        int n3 = 0;
        for (n = 1; n < n2; ++n) {
            int n4 = n2 >> 1;
            while ((n3 & n4) != 0) {
                n3 ^= n4;
                n4 >>= 1;
            }
            if (n >= (n3 ^= n4)) continue;
            d = dArray[n];
            dArray[n] = dArray[n3];
            dArray[n3] = d;
            d = dArray2[n];
            dArray2[n] = dArray2[n3];
            dArray2[n3] = d;
        }
        for (n = 2; n <= n2; n <<= 1) {
            double d2 = Double.longBitsToDouble(-4604611780675359464L) / (double)n;
            d = Math.cos(d2);
            double d3 = Math.sin(d2);
            for (int i = 0; i < n2; i += n) {
                double d4 = 1.0;
                double d5 = 0.0;
                for (int j = 0; j < n / 2; ++j) {
                    int n5 = i + j;
                    int n6 = n5 + n / 2;
                    double d6 = dArray[n6] * d4 - dArray2[n6] * d5;
                    double d7 = dArray[n6] * d5 + dArray2[n6] * d4;
                    dArray[n6] = dArray[n5] - d6;
                    dArray2[n6] = dArray2[n5] - d7;
                    int n7 = n5;
                    dArray[n7] = dArray[n7] + d6;
                    int n8 = n5;
                    dArray2[n8] = dArray2[n8] + d7;
                    double d8 = d4 * d - d5 * d3;
                    d5 = d4 * d3 + d5 * d;
                    d4 = d8;
                }
            }
        }
    }
}
