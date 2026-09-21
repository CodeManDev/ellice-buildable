



package dev.felix.ellice.feature.timer;

import java.util.Random;

public final class TimerModeTracker
{
    private final Random f4ilwrf7tp8p;
    private Profile f43qlikq47o6;
    private float f9y13mtnhovd;
    private int f4tyr43mtfx3;
    private int f6fcwq9vfpmk;
    private int f205lxomrjs5;
    private boolean f1fs0sb8nmwy;
    
    public TimerModeTracker() {
        this(System.nanoTime());
    }
    
    TimerModeTracker(final long seed) {
        this.f9y13mtnhovd = 1.0f;
        this.f1fs0sb8nmwy = true;
        this.f4ilwrf7tp8p = new Random(seed);
    }
    
    public void configure(final Profile f43qlikq47o6) {
        if (f43qlikq47o6 == null) {
            throw new IllegalArgumentException("Timer profile is required");
        }
        this.f43qlikq47o6 = f43qlikq47o6;
        this.f4tyr43mtfx3 = 0;
        this.f6fcwq9vfpmk = 0;
        this.f205lxomrjs5 = 0;
        this.f1fs0sb8nmwy = true;
        this.f9y13mtnhovd = this.mcwdx6tdxxzs();
        if (this.f43qlikq47o6.mode() == Mode.RANDOM) {
            this.f6fcwq9vfpmk = this.f43qlikq47o6.randomTicks();
        }
    }
    
    public void tick() {
        if (this.f43qlikq47o6 == null) {
            this.f9y13mtnhovd = 1.0f;
            return;
        }
        switch (this.f43qlikq47o6.mode().ordinal()) {
            case 0: {
                this.f9y13mtnhovd = this.f43qlikq47o6.constantSpeed();
                break;
            }
            case 1: {
                this.m8fu3t44yzog();
                break;
            }
            case 2: {
                this.m5hfxauq9ypk();
                break;
            }
            case 3: {
                this.f9y13mtnhovd = (this.f1fs0sb8nmwy ? this.f43qlikq47o6.boostSpeed() : this.f43qlikq47o6.brakeSpeed());
                break;
            }
        }
    }
    
    public void movementPacketAccepted() {
        if (this.f43qlikq47o6 == null || this.f43qlikq47o6.mode() != Mode.PACKET_PULSE) {
            return;
        }
        if (++this.f205lxomrjs5 < this.f43qlikq47o6.packetBatch()) {
            return;
        }
        this.f205lxomrjs5 = 0;
        this.f1fs0sb8nmwy = !this.f1fs0sb8nmwy;
        this.f9y13mtnhovd = (this.f1fs0sb8nmwy ? this.f43qlikq47o6.boostSpeed() : this.f43qlikq47o6.brakeSpeed());
    }
    
    public float multiplier() {
        return this.f9y13mtnhovd;
    }
    
    public boolean boostPhase() {
        return this.f1fs0sb8nmwy;
    }
    
    public int packetCount() {
        return this.f205lxomrjs5;
    }
    
    private float mcwdx6tdxxzs() {
        return switch (this.f43qlikq47o6.mode().ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> this.f43qlikq47o6.constantSpeed();
            case 1,  3 -> this.f43qlikq47o6.boostSpeed();
            case 2 -> this.mgio3v7k2zdd();
        };
    }
    
    private void m8fu3t44yzog() {
        if (++this.f4tyr43mtfx3 >= (this.f1fs0sb8nmwy ? this.f43qlikq47o6.boostTicks() : this.f43qlikq47o6.brakeTicks())) {
            this.f4tyr43mtfx3 = 0;
            this.f1fs0sb8nmwy = !this.f1fs0sb8nmwy;
        }
        this.f9y13mtnhovd = (this.f1fs0sb8nmwy ? this.f43qlikq47o6.boostSpeed() : this.f43qlikq47o6.brakeSpeed());
    }
    
    private void m5hfxauq9ypk() {
        if (this.f6fcwq9vfpmk <= 0) {
            this.f9y13mtnhovd = this.mgio3v7k2zdd();
            this.f6fcwq9vfpmk = this.f43qlikq47o6.randomTicks();
        }
        --this.f6fcwq9vfpmk;
    }
    
    private float mgio3v7k2zdd() {
        return this.f43qlikq47o6.randomMinimum() + this.f4ilwrf7tp8p.nextFloat() * (this.f43qlikq47o6.randomMaximum() - this.f43qlikq47o6.randomMinimum());
    }
    
    public record Profile(Mode mode, float constantSpeed, float boostSpeed, float brakeSpeed, int boostTicks, int brakeTicks, float randomMinimum, float randomMaximum, int randomTicks, int packetBatch) {
        public Profile {
            if (mode == null || !m7t9f6eg9npb(constantSpeed) || !m7t9f6eg9npb(boostSpeed) || !m7t9f6eg9npb(brakeSpeed) || !m7t9f6eg9npb(randomMinimum) || !m7t9f6eg9npb(randomMaximum) || randomMinimum > randomMaximum || boostTicks < 1 || brakeTicks < 1 || randomTicks < 1 || packetBatch < 1) {
                throw new IllegalArgumentException("Invalid timer profile");
            }
        }
        
        private static boolean m7t9f6eg9npb(final float f) {
            return Float.isFinite(f) && f > 0.0f;
        }
    }
    
    public enum Mode
    {
        CONSTANT, 
        PULSE, 
        RANDOM, 
        PACKET_PULSE;
    }
}
