



package dev.felix.ellice.feature.esp;

import org.joml.Matrix4fc;
import org.joml.Vector4f;
import org.joml.Matrix4f;

public final class EspProjectService
{
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int TOP = 4;
    public static final int BOTTOM = 8;
    private static final float f5a8mo7sme4f = 1.0E-4f;
    private static final int[] ff4yl0ey2mhu;
    private final Matrix4f fdi8s20jiubp;
    private final Vector4f[] fimzyq916ngk;
    private final double f89zbh50blfs;
    private final double fjhrtg7gc2p7;
    private final double fd3ndh1v7vyo;
    private final float f9xj008t5p3m;
    private final float f308jky2qkmb;
    private final boolean fdz46fd0va37;
    private float f1ttxg8n6cde;
    private float fh4gmqf3jihg;
    private float f4pgzg663qro;
    private float f3y4gqt35ubt;
    
    public EspProjectService(final Matrix4fc matrix4fc, final Matrix4fc matrix4fc2, final double n, final double n2, final double n3, final float n4, final float n5) {
        this.fimzyq916ngk = new Vector4f[8];
        this.fdi8s20jiubp = new Matrix4f(matrix4fc2).mul(matrix4fc);
        this.f89zbh50blfs = n;
        this.fjhrtg7gc2p7 = n2;
        this.fd3ndh1v7vyo = n3;
        this.f9xj008t5p3m = n4;
        this.f308jky2qkmb = n5;
        this.fdz46fd0va37 = (this.fdi8s20jiubp.isFinite() && Double.isFinite(n) && Double.isFinite(n2) && Double.isFinite(n3) && Float.isFinite(n4) && Float.isFinite(n5) && n4 > 0.0f && n5 > 0.0f);
        for (int i = 0; i < this.fimzyq916ngk.length; ++i) {
            this.fimzyq916ngk[i] = new Vector4f();
        }
    }
    
    public Bounds project(final Box box) {
        if (!this.fdz46fd0va37 || box == null || !box.valid() || box.contains(this.f89zbh50blfs, this.fjhrtg7gc2p7, this.fd3ndh1v7vyo)) {
            return null;
        }
        for (int i = 0; i < 8; ++i) {
            final Vector4f set = this.fimzyq916ngk[i].set((float)((((i & 0x1) == 0x0) ? box.minX : box.maxX) - this.f89zbh50blfs), (float)((((i & 0x2) == 0x0) ? box.minY : box.maxY) - this.fjhrtg7gc2p7), (float)((((i & 0x4) == 0x0) ? box.minZ : box.maxZ) - this.fd3ndh1v7vyo), 1.0f);
            this.fdi8s20jiubp.transform(set);
            if (!set.isFinite()) {
                return null;
            }
        }
        for (int j = 0; j < 6; ++j) {
            boolean b = true;
            final Vector4f[] fimzyq916ngk = this.fimzyq916ngk;
            for (int length = fimzyq916ngk.length, k = 0; k < length; ++k) {
                if (magl42hu35vg(fimzyq916ngk[k], j) >= 0.0f) {
                    b = false;
                    break;
                }
            }
            if (b) {
                return null;
            }
        }
        final float intBitsToFloat = Float.intBitsToFloat(2139095040);
        this.fh4gmqf3jihg = intBitsToFloat;
        this.f1ttxg8n6cde = intBitsToFloat;
        final float intBitsToFloat2 = Float.intBitsToFloat(-8388608);
        this.f3y4gqt35ubt = intBitsToFloat2;
        this.f4pgzg663qro = intBitsToFloat2;
        for (int l = 0; l < EspProjectService.ff4yl0ey2mhu.length; l += 2) {
            this.mg4m5162c9ie(this.fimzyq916ngk[EspProjectService.ff4yl0ey2mhu[l]], this.fimzyq916ngk[EspProjectService.ff4yl0ey2mhu[l + 1]]);
        }
        if (!Float.isFinite(this.f1ttxg8n6cde) || this.f4pgzg663qro <= this.f1ttxg8n6cde || this.f3y4gqt35ubt <= this.fh4gmqf3jihg) {
            return null;
        }
        final float n = (this.f1ttxg8n6cde * Float.intBitsToFloat(1056964608) + Float.intBitsToFloat(1056964608)) * this.f9xj008t5p3m;
        final float n2 = (this.f4pgzg663qro * Float.intBitsToFloat(1056964608) + Float.intBitsToFloat(1056964608)) * this.f9xj008t5p3m;
        final float n3 = (Float.intBitsToFloat(1056964608) - this.f3y4gqt35ubt * Float.intBitsToFloat(1056964608)) * this.f308jky2qkmb;
        final float n4 = (Float.intBitsToFloat(1056964608) - this.fh4gmqf3jihg * Float.intBitsToFloat(1056964608)) * this.f308jky2qkmb;
        if (!Float.isFinite(n) || !Float.isFinite(n2) || !Float.isFinite(n3) || !Float.isFinite(n4) || n2 <= 0.0f || n4 <= 0.0f || n >= this.f9xj008t5p3m || n3 >= this.f308jky2qkmb) {
            return null;
        }
        final int n5 = ((n < 0.0f) ? 1 : 0) | ((n2 > this.f9xj008t5p3m) ? 2 : 0) | ((n3 < 0.0f) ? 4 : 0) | ((n4 > this.f308jky2qkmb) ? 8 : 0);
        if (n5 == 15) {
            return null;
        }
        final float max = Math.max(0.0f, n);
        final float min = Math.min(this.f9xj008t5p3m, n2);
        final float max2 = Math.max(0.0f, n3);
        return new Bounds(max, max2, min - max, Math.min(this.f308jky2qkmb, n4) - max2, n5);
    }
    
    private void mg4m5162c9ie(final Vector4f vector4f, final Vector4f vector4f2) {
        float max = 0.0f;
        float min = 1.0f;
        for (int i = 4; i < 7; ++i) {
            final float magl42hu35vg = magl42hu35vg(vector4f, i);
            final float magl42hu35vg2 = magl42hu35vg(vector4f2, i);
            if (magl42hu35vg < 0.0f && magl42hu35vg2 < 0.0f) {
                return;
            }
            if (magl42hu35vg < 0.0f) {
                max = Math.max(max, magl42hu35vg / (magl42hu35vg - magl42hu35vg2));
            }
            else if (magl42hu35vg2 < 0.0f) {
                min = Math.min(min, magl42hu35vg / (magl42hu35vg - magl42hu35vg2));
            }
            if (max > min) {
                return;
            }
        }
        this.md9d7tj2if69(vector4f, vector4f2, max);
        this.md9d7tj2if69(vector4f, vector4f2, min);
    }
    
    private void md9d7tj2if69(final Vector4f vector4f, final Vector4f vector4f2, final float n) {
        final float n2 = vector4f.w + (vector4f2.w - vector4f.w) * n;
        if (n2 < Float.intBitsToFloat(944879383)) {
            return;
        }
        final float n3 = (vector4f.x + (vector4f2.x - vector4f.x) * n) / n2;
        final float n4 = (vector4f.y + (vector4f2.y - vector4f.y) * n) / n2;
        this.f1ttxg8n6cde = Math.min(this.f1ttxg8n6cde, n3);
        this.f4pgzg663qro = Math.max(this.f4pgzg663qro, n3);
        this.fh4gmqf3jihg = Math.min(this.fh4gmqf3jihg, n4);
        this.f3y4gqt35ubt = Math.max(this.f3y4gqt35ubt, n4);
    }
    
    private static float magl42hu35vg(final Vector4f vector4f, final int n) {
        return switch (n) {
            case 0 -> vector4f.w + vector4f.x;
            case 1 -> vector4f.w - vector4f.x;
            case 2 -> vector4f.w + vector4f.y;
            case 3 -> vector4f.w - vector4f.y;
            case 4 -> vector4f.w + vector4f.z;
            case 5 -> vector4f.w - vector4f.z;
            default -> vector4f.w - Float.intBitsToFloat(953267991);
        };
    }
    
    static {
        ff4yl0ey2mhu = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 0, 2, 1, 3, 4, 6, 5, 7, 0, 4, 1, 5, 2, 6, 3, 7 };
    }
    
    public record Box(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        public Box move(final double n, final double n2, final double n3) {
            return new Box(this.minX + n, this.minY + n2, this.minZ + n3, this.maxX + n, this.maxY + n2, this.maxZ + n3);
        }
        
        boolean valid() {
            return Double.isFinite(this.minX) && Double.isFinite(this.minY) && Double.isFinite(this.minZ) && Double.isFinite(this.maxX) && Double.isFinite(this.maxY) && Double.isFinite(this.maxZ) && this.minX < this.maxX && this.minY < this.maxY && this.minZ < this.maxZ;
        }
        
        boolean contains(final double n, final double n2, final double n3) {
            return n >= this.minX && n <= this.maxX && n2 >= this.minY && n2 <= this.maxY && n3 >= this.minZ && n3 <= this.maxZ;
        }
    }
    
    public record Bounds(float x, float y, float width, float height, int clipped) {
        public float right() {
            return this.x + this.width;
        }
        
        public float bottom() {
            return this.y + this.height;
        }
        
        public boolean hasEdge(final int n) {
            return (this.clipped & n) == 0x0;
        }
    }
}
