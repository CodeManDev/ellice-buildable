



package dev.felix.ellice.feature.combat;

public final class CombatCorrectedService
{
    private boolean f68l2qaao22e;
    private long fgvbxjo9q599;
    private int fgbw8oqehihm;
    
    public void corrected(final long fgvbxjo9q599, final int fgbw8oqehihm) {
        if (fgbw8oqehihm < 0 || fgbw8oqehihm > 20) {
            throw new IllegalArgumentException("Recovery must be 0–20 ticks");
        }
        this.fgvbxjo9q599 = fgvbxjo9q599;
        this.fgbw8oqehihm = fgbw8oqehihm;
        this.f68l2qaao22e = (fgbw8oqehihm != 0);
    }
    
    public boolean blocks(final long n) {
        return this.f68l2qaao22e && (n < this.fgvbxjo9q599 || n - this.fgvbxjo9q599 <= this.fgbw8oqehihm || (this.f68l2qaao22e = false));
    }
    
    public void clear() {
        this.f68l2qaao22e = false;
    }
}
