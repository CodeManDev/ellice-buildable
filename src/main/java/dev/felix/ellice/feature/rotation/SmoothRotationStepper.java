



package dev.felix.ellice.feature.rotation;

public final class SmoothRotationStepper
{
    private final RotationVanillaGcdService f5i0qvk9t872;
    private boolean f4shtzddilqz;
    private double fitnkeg024s2;
    private double fjbxvbwsdo1y;
    private int fgzzszeezidz;
    private int fb8engl2uqu1;
    private RotationData fnwg55wixt5;
    
    public SmoothRotationStepper() {
        this.f5i0qvk9t872 = new RotationVanillaGcdService();
    }
    
    public RotationNextService.Step next(final RotationData rotationData, final RotationData rotationData2, final double n, final double d, final double d2) {
        if (!Double.isFinite(d) || d <= 0.0 || !Double.isFinite(d2) || d2 < 0.0 || d2 > 1.0) {
            throw new IllegalArgumentException("Invalid return profile");
        }
        if (!this.f4shtzddilqz) {
            this.fitnkeg024s2 = RotationData.yawDelta(rotationData2.yaw(), rotationData.yaw());
            this.fjbxvbwsdo1y = rotationData.pitch() - rotationData2.pitch();
            this.fb8engl2uqu1 = Math.max(4, (int)Math.ceil(Math.hypot(this.fitnkeg024s2, this.fjbxvbwsdo1y) * Double.longBitsToDouble(4611123068473966592L) / d)) + (int)Math.round(d2 * Double.longBitsToDouble(4616189618054758400L));
            this.fgzzszeezidz = 0;
            this.f4shtzddilqz = true;
        }
        final double n2 = Math.min(this.fb8engl2uqu1, ++this.fgzzszeezidz) / (double)this.fb8engl2uqu1;
        final double n3 = 1.0 - n2 * n2 * n2 * (n2 * (Double.longBitsToDouble(4618441417868443648L) * n2 - Double.longBitsToDouble(4624633867356078080L)) + Double.longBitsToDouble(4621819117588971520L));
        final RotationData rotation = this.f5i0qvk9t872.quantize(rotationData, new RotationData(rotationData2.yaw() + this.fitnkeg024s2 * n3, rotationData2.pitch() + this.fjbxvbwsdo1y * n3).withPitchClamped(), n, 0.0).rotation();
        final boolean b = this.fgzzszeezidz >= this.fb8engl2uqu1;
        this.fnwg55wixt5 = (b ? rotation : null);
        return new RotationNextService.Step(rotation, b);
    }
    
    public boolean completed(final RotationData rotationData) {
        return this.fnwg55wixt5 != null && RotationData.distance(this.fnwg55wixt5, rotationData) < Double.longBitsToDouble(4547007122018943789L);
    }
    
    public void reset() {
        this.f4shtzddilqz = false;
        final int n = 0;
        this.fb8engl2uqu1 = n;
        this.fgzzszeezidz = n;
        this.fnwg55wixt5 = null;
    }
}
