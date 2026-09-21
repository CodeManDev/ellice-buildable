



package dev.felix.ellice.feature.speed;

final class LegacySpeedMode implements SpeedStrafeHandler
{
    private final String f374571n3sax;
    private final SpeedOperationHandler fj6axuthgoxr;
    private double fa262vosp10j;
    private double f7rvpoge4p3d;
    private int f60jwikmk1o1;
    private int fcxllp6zlc7q;
    private long f7b7bxd7ictx;
    
    LegacySpeedMode(final String f374571n3sax, final SpeedOperationHandler fj6axuthgoxr) {
        this.f7rvpoge4p3d = Double.longBitsToDouble(4581421828931458171L);
        this.f374571n3sax = f374571n3sax;
        this.fj6axuthgoxr = fj6axuthgoxr;
    }
    
    private boolean mgoldm1muqml(final String s) {
        return this.fj6axuthgoxr.bool(s);
    }
    
    private double maatm3yoz1kr(final String s) {
        return this.fj6axuthgoxr.number(s);
    }
    
    @Override
    public void enable(final SpeedMovingService speedMovingService) {
        if (this.f374571n3sax.equals("AACHop3.5.0") && speedMovingService.ground) {
            speedMovingService.stop();
        }
        if (this.f374571n3sax.equals("Custom")) {
            if (this.mgoldm1muqml("Reset XZ")) {
                speedMovingService.stop();
            }
            if (this.mgoldm1muqml("Reset Y")) {
                speedMovingService.my = 0.0;
            }
        }
        this.f7b7bxd7ictx = speedMovingService.actions.millis() - 300L;
    }
    
    @Override
    public void update(final SpeedMovingService speedMovingService) {
        final String f374571n3sax = this.f374571n3sax;
        switch (f374571n3sax) {
            case "AACHop3.3.13": {
                if (!speedMovingService.moving() || speedMovingService.obstructed() || speedMovingService.hurtTime > 0) {
                    return;
                }
                if (speedMovingService.ground && speedMovingService.verticalCollision) {
                    speedMovingService.forwardBoost(Double.longBitsToDouble(4596445837541769216L));
                    speedMovingService.my = Double.longBitsToDouble(4600967451314246124L);
                    speedMovingService.strafe();
                    break;
                }
                if (speedMovingService.fallDistance >= Double.longBitsToDouble(4599256083455845335L)) {
                    speedMovingService.airSpeed(Double.longBitsToDouble(4581421828931458171L));
                    break;
                }
                if (speedMovingService.carpet) {
                    return;
                }
                speedMovingService.airSpeed((speedMovingService.sideways == 0.0f) ? Double.longBitsToDouble(4583439441766383616L) : Double.longBitsToDouble(4581710059226005504L));
                speedMovingService.multiply(Double.longBitsToDouble(4607186922399644778L));
                if (!speedMovingService.horizontalCollision) {
                    speedMovingService.my -= Double.longBitsToDouble(4579800529114234880L);
                    break;
                }
                break;
            }
            case "AACHop4": {
                speedMovingService.timer(1.0);
                if (!speedMovingService.moving() || speedMovingService.obstructed()) {
                    return;
                }
                if (speedMovingService.ground) {
                    speedMovingService.jump();
                    break;
                }
                speedMovingService.timer((speedMovingService.fallDistance <= Double.longBitsToDouble(4591870180066957722L)) ? Double.longBitsToDouble(4609434218613702656L) : ((speedMovingService.fallDistance < Double.longBitsToDouble(4608533498688228557L)) ? Double.longBitsToDouble(4604480259023595110L) : 1.0));
                break;
            }
            case "AACHop5": {
                if (!speedMovingService.moving() || speedMovingService.obstructed()) {
                    return;
                }
                if (speedMovingService.ground) {
                    speedMovingService.jump();
                    speedMovingService.timer(Double.longBitsToDouble(4606628475929886720L));
                    speedMovingService.airSpeed(Double.longBitsToDouble(4581450651791261696L));
                }
                if (speedMovingService.fallDistance < Double.longBitsToDouble(4612811918334230528L)) {
                    if (speedMovingService.fallDistance > Double.longBitsToDouble(4604480259023595110L)) {
                        if (speedMovingService.tick % 3 == 0) {
                            speedMovingService.timer(Double.longBitsToDouble(4611348248240586752L));
                        }
                        else if (speedMovingService.fallDistance < Double.longBitsToDouble(4608308318706860032L)) {
                            speedMovingService.timer(Double.longBitsToDouble(4610774039567269888L));
                        }
                    }
                    speedMovingService.airSpeed(Double.longBitsToDouble(4581421828931458171L));
                    break;
                }
                break;
            }
            case "HypixelLowHop": {
                if (!speedMovingService.moving() || speedMovingService.fallDistance > Double.longBitsToDouble(4608083138725491507L)) {
                    return;
                }
                if (speedMovingService.ground) {
                    speedMovingService.jump();
                    speedMovingService.strafe();
                    return;
                }
                switch (speedMovingService.airTicks) {
                    case 1: {
                        speedMovingService.strafe();
                        break;
                    }
                    case 4: {
                        speedMovingService.my -= Double.longBitsToDouble(4584304132692975288L);
                        break;
                    }
                    case 5: {
                        speedMovingService.my -= Double.longBitsToDouble(4596032189879261766L);
                        break;
                    }
                    case 6: {
                        speedMovingService.my *= Double.longBitsToDouble(4607227454796291113L);
                        break;
                    }
                    case 7: {
                        if (this.mgoldm1muqml("Glide")) {
                            speedMovingService.my /= Double.longBitsToDouble(4609434218613702656L);
                            break;
                        }
                        break;
                    }
                }
                if (speedMovingService.airTicks >= 7 && this.mgoldm1muqml("Glide")) {
                    speedMovingService.strafe(Math.max(speedMovingService.speed(), Double.longBitsToDouble(4598733665688616960L)), Double.longBitsToDouble(4604480259023595110L));
                }
                if (speedMovingService.hurtTime == 9) {
                    speedMovingService.strafe();
                }
                if (speedMovingService.speedLevel() == 2 && (speedMovingService.airTicks == 1 || speedMovingService.airTicks == 2 || speedMovingService.airTicks == 5 || speedMovingService.airTicks == 6 || speedMovingService.airTicks == 8)) {
                    speedMovingService.multiply(Double.longBitsToDouble(4608083138725491507L));
                    break;
                }
                break;
            }
            case "IntaveHop14": {
                if (!speedMovingService.moving() || speedMovingService.obstructed()) {
                    return;
                }
                if (speedMovingService.ground) {
                    speedMovingService.my = Double.longBitsToDouble(4601237667291888353L) - (this.mgoldm1muqml("Low hop") ? Double.longBitsToDouble(4400900751364574071L) : 0.0);
                    if (speedMovingService.sprinting) {
                        speedMovingService.strafe(speedMovingService.speed(), this.maatm3yoz1kr("Strafe strength"));
                    }
                    speedMovingService.timer(this.maatm3yoz1kr("Ground timer"));
                }
                else {
                    speedMovingService.timer(this.maatm3yoz1kr("Air timer"));
                }
                if (this.mgoldm1muqml("Boost") && speedMovingService.my > Double.longBitsToDouble(4569063951553953530L) && speedMovingService.sprinting) {
                    speedMovingService.multiply(1.0 + Double.longBitsToDouble(4569063951553953530L) * this.maatm3yoz1kr("Initial boost multiplier"));
                    break;
                }
                break;
            }
            case "MatrixHop":
            case "MatrixSlowHop": {
                if (speedMovingService.obstructed()) {
                    return;
                }
                final boolean equals = this.f374571n3sax.equals("MatrixSlowHop");
                if (!equals && this.mgoldm1muqml("Low hop")) {
                    speedMovingService.airSpeed(Double.longBitsToDouble(4583151211342987264L));
                }
                if (speedMovingService.moving()) {
                    if (equals && !speedMovingService.ground && speedMovingService.fallDistance > Double.longBitsToDouble(4611686018427387904L)) {
                        speedMovingService.timer(1.0);
                        return;
                    }
                    if (speedMovingService.ground) {
                        speedMovingService.strafe(speedMovingService.speed() + ((equals || !speedMovingService.scaffold) ? this.maatm3yoz1kr("Extra ground boost") : 0.0));
                        speedMovingService.my = Double.longBitsToDouble(4601237667291888353L) - (this.mgoldm1muqml("Low hop") ? Double.longBitsToDouble(4570170756198376103L) : 0.0);
                        if (equals) {
                            speedMovingService.timer(Double.longBitsToDouble(4602854459712733184L));
                        }
                    }
                    else if (equals) {
                        speedMovingService.timer(Double.longBitsToDouble(4607620619280842752L));
                    }
                    else if (!speedMovingService.scaffold && speedMovingService.speed() < Double.longBitsToDouble(4596013491724138578L)) {
                        speedMovingService.strafe();
                    }
                    speedMovingService.airSpeed((speedMovingService.fallDistance <= Double.longBitsToDouble(4600877379321698714L) && speedMovingService.sideways == 0.0f) ? Double.longBitsToDouble(4581522709531328512L) : Double.longBitsToDouble(4581421828802609152L));
                }
                else if (equals) {
                    speedMovingService.timer(1.0);
                }
                break;
            }
            case "OldMatrixHop": {
                if (speedMovingService.obstructed()) {
                    return;
                }
                if (!speedMovingService.moving()) {
                    speedMovingService.timer(1.0);
                    break;
                }
                if (speedMovingService.ground) {
                    speedMovingService.jump();
                    speedMovingService.airSpeed(Double.longBitsToDouble(4581704294843023360L));
                    speedMovingService.timer(Double.longBitsToDouble(4607430116543299584L));
                    break;
                }
                speedMovingService.strafe();
                break;
            }
            case "BlocksMCHop": {
                if (!speedMovingService.moving() || speedMovingService.obstructed()) {
                    return;
                }
                if (speedMovingService.ground) {
                    speedMovingService.jump();
                    return;
                }
                if (this.mgoldm1muqml("Full strafe")) {
                    speedMovingService.strafe(speedMovingService.speed() - Double.longBitsToDouble(4571261708391153664L));
                }
                else if (speedMovingService.airTicks >= 6) {
                    speedMovingService.strafe();
                }
                if (speedMovingService.speedLevel() > 0 && speedMovingService.airTicks == 3) {
                    speedMovingService.multiply(Double.longBitsToDouble(4607722850755301868L));
                }
                if (this.mgoldm1muqml("Low hop") && speedMovingService.airTicks == 4 && (!this.mgoldm1muqml("Safe Y") || speedMovingService.y % 1.0 == Double.longBitsToDouble(4595152737135848448L))) {
                    speedMovingService.my = Double.longBitsToDouble(-4631645971838454989L);
                }
                if (speedMovingService.hurtTime == 9 && this.mgoldm1muqml("Damage boost")) {
                    speedMovingService.strafe(Math.max(speedMovingService.speed(), Double.longBitsToDouble(4604480258916220928L)));
                }
                if (this.mgoldm1muqml("Damage low hop") && speedMovingService.hurtTime >= 1 && speedMovingService.my > 0.0) {
                    speedMovingService.my -= Double.longBitsToDouble(4594572339843380019L);
                    break;
                }
                break;
            }
            case "Legit": {
                speedMovingService.sprinting = (speedMovingService.forward > Double.longBitsToDouble(4605380978949069210L));
                break;
            }
            case "VerusHop":
            case "VerusLowHop":
            case "VerusLowHopNew": {
                if (!speedMovingService.moving() || speedMovingService.obstructed()) {
                    return;
                }
                final boolean b = !this.f374571n3sax.equals("VerusHop");
                final boolean equals2 = this.f374571n3sax.equals("VerusLowHopNew");
                if (speedMovingService.ground) {
                    this.fa262vosp10j = ((speedMovingService.speedLevel() >= 1) ? (b ? Float.intBitsToFloat(1056964608) : Float.intBitsToFloat(1055622431)) : ((double)(b ? Float.intBitsToFloat(1052266988) : Float.intBitsToFloat(1051595899))));
                    if (equals2) {
                        this.fa262vosp10j = ((speedMovingService.slownessAmplifier == 1) ? Double.longBitsToDouble(4599075939685498880L) : Double.longBitsToDouble(4599616371662258176L));
                    }
                    speedMovingService.jump();
                }
                else {
                    if (b && speedMovingService.airTicks <= 1) {
                        speedMovingService.my = Double.longBitsToDouble(-4631645971838454989L);
                    }
                    this.fa262vosp10j *= (equals2 ? Double.longBitsToDouble(4607092346893369344L) : Double.longBitsToDouble(4607002274986721280L));
                }
                speedMovingService.strafe(this.fa262vosp10j);
                break;
            }
            case "VulcanGround2.8.8": {
                if (!speedMovingService.obstructed() && speedMovingService.moving() && speedMovingService.collision(0.0, Double.longBitsToDouble(-4650957407178058629L), 0.0)) {
                    speedMovingService.strafe((speedMovingService.speedLevel() > 0) ? Double.longBitsToDouble(4603489466869350400L) : ((speedMovingService.sideways != 0.0f) ? Double.longBitsToDouble(4601057523242369024L) : Double.longBitsToDouble(4601237667055665152L)));
                    speedMovingService.my = Double.longBitsToDouble(4572414629676717179L);
                    break;
                }
                break;
            }
            case "VulcanHop": {
                if (speedMovingService.obstructed()) {
                    return;
                }
                if (!speedMovingService.moving()) {
                    speedMovingService.timer(1.0);
                    break;
                }
                if (!speedMovingService.ground && speedMovingService.fallDistance > Double.longBitsToDouble(4611686018427387904L)) {
                    speedMovingService.timer(1.0);
                    return;
                }
                if (speedMovingService.ground) {
                    speedMovingService.jump();
                    if (speedMovingService.my > 0.0) {
                        speedMovingService.timer(Double.longBitsToDouble(4607836791964172288L));
                    }
                    speedMovingService.strafe(Double.longBitsToDouble(4602345552795926528L));
                    break;
                }
                if (speedMovingService.my < 0.0) {
                    speedMovingService.timer(Double.longBitsToDouble(4606448332116590592L));
                    break;
                }
                break;
            }
            case "VulcanLowHop": {
                if (speedMovingService.obstructed()) {
                    return;
                }
                if (!speedMovingService.moving()) {
                    speedMovingService.timer(1.0);
                    break;
                }
                if (!speedMovingService.ground && speedMovingService.fallDistance > Double.longBitsToDouble(4607632778762754458L)) {
                    speedMovingService.timer(1.0);
                    speedMovingService.my = Double.longBitsToDouble(-4625196817309499392L);
                    return;
                }
                if (speedMovingService.ground) {
                    speedMovingService.jump();
                    speedMovingService.strafe(Double.longBitsToDouble(4602345552795926528L));
                    speedMovingService.timer(Double.longBitsToDouble(4608366865553555456L));
                    break;
                }
                if (speedMovingService.tick % 4 == 0) {
                    if (speedMovingService.tick % 3 == 0) {
                        if (Math.abs(speedMovingService.my) > Double.longBitsToDouble(4472406533629990549L)) {
                            speedMovingService.my = Double.longBitsToDouble(-4646453807550688133L) / speedMovingService.my;
                        }
                    }
                    else if (Math.abs(speedMovingService.y) > Double.longBitsToDouble(4472406533629990549L)) {
                        speedMovingService.my = -speedMovingService.my / speedMovingService.y;
                    }
                    speedMovingService.timer(Double.longBitsToDouble(4606268188303294464L));
                    break;
                }
                break;
            }
            case "AACHop3.5.0":
            case "SpartanYPort":
            case "SpectreLowHop":
            case "SpectreBHop":
            case "SpectreOnGround":
            case "VerusFHop":
            case "TeleportCubeCraft":
            case "HypixelHop":
            case "SlowHop":
            case "Custom": {
                break;
            }
            default: {
                throw new IllegalStateException("Unimplemented Legacy mode: " + this.f374571n3sax);
            }
        }
    }
    
    @Override
    public void motion(final SpeedMovingService speedMovingService) {
        final String f374571n3sax = this.f374571n3sax;
        switch (f374571n3sax) {
            case "Custom": {
                if ((this.mgoldm1muqml("Not on void") && !speedMovingService.actions.hasLanding()) || (this.mgoldm1muqml("Not on falling") && speedMovingService.fallDistance > Double.longBitsToDouble(4612811918334230528L)) || (this.mgoldm1muqml("Not on consuming") && speedMovingService.consuming)) {
                    if (speedMovingService.ground) {
                        speedMovingService.jump();
                    }
                    speedMovingService.timer(1.0);
                    return;
                }
                if (!speedMovingService.moving()) {
                    return;
                }
                if (speedMovingService.ground) {
                    if (this.maatm3yoz1kr("Ground strafe") > 0.0) {
                        speedMovingService.strafe(this.maatm3yoz1kr("Ground strafe"));
                    }
                    speedMovingService.timer(this.maatm3yoz1kr("Ground timer"));
                    speedMovingService.my = this.maatm3yoz1kr("Jump height");
                    break;
                }
                if (this.maatm3yoz1kr("Air strafe") > 0.0) {
                    speedMovingService.strafe(this.maatm3yoz1kr("Air strafe"));
                }
                speedMovingService.timer((speedMovingService.tick % this.fj6axuthgoxr.integer("Air timer tick") == 0) ? this.maatm3yoz1kr("Air timer") : 1.0);
                break;
            }
            case "SlowHop": {
                if (speedMovingService.obstructed()) {
                    return;
                }
                if (!speedMovingService.moving()) {
                    speedMovingService.stop();
                    break;
                }
                if (speedMovingService.ground) {
                    speedMovingService.jump();
                    break;
                }
                speedMovingService.strafe(speedMovingService.speed() * Double.longBitsToDouble(4607231958563422208L));
                break;
            }
            case "SpartanYPort": {
                if (speedMovingService.forward <= 0.0f) {
                    return;
                }
                if (speedMovingService.ground) {
                    speedMovingService.jump();
                    this.f60jwikmk1o1 = 0;
                    break;
                }
                speedMovingService.timer(Double.longBitsToDouble(4607542706963480576L));
                if (this.f60jwikmk1o1 >= 3) {
                    speedMovingService.airSpeed(Double.longBitsToDouble(4583583556709646336L));
                }
                if (this.f60jwikmk1o1 >= 4 && this.f60jwikmk1o1 % 2 == 0) {
                    speedMovingService.my = Double.longBitsToDouble(-4623935809413835653L) - speedMovingService.actions.random() * Double.longBitsToDouble(4576341768551784251L);
                    speedMovingService.airSpeed(Double.longBitsToDouble(4582517104518889472L));
                }
                ++this.f60jwikmk1o1;
                break;
            }
            case "SpectreBHop":
            case "SpectreLowHop": {
                if (!speedMovingService.moving() || speedMovingService.jumpKey) {
                    return;
                }
                if (speedMovingService.ground) {
                    speedMovingService.strafe(Double.longBitsToDouble(4607632778870128640L));
                    speedMovingService.my = (this.f374571n3sax.equals("SpectreBHop") ? Double.longBitsToDouble(4601597955262077993L) : Double.longBitsToDouble(4594572339843380019L));
                    break;
                }
                speedMovingService.strafe();
                break;
            }
            case "VerusFHop": {
                final boolean b = speedMovingService.forward != 0.0f && speedMovingService.sideways != 0.0f;
                if (!speedMovingService.ground) {
                    speedMovingService.strafe(b ? Double.longBitsToDouble(4599688428865454080L) : Double.longBitsToDouble(4599697436485615616L));
                    break;
                }
                speedMovingService.strafe(b ? Double.longBitsToDouble(4602363566962507776L) : Double.longBitsToDouble(4602994071382786048L));
                if (speedMovingService.moving()) {
                    speedMovingService.jump();
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    public void afterMotion(final SpeedMovingService speedMovingService) {
        if (!this.f374571n3sax.equals("AACHop3.5.0") || !speedMovingService.moving() || speedMovingService.liquid) {
            return;
        }
        speedMovingService.airSpeed(this.f7rvpoge4p3d += Double.longBitsToDouble(4566942575998533632L));
        if (speedMovingService.fallDistance <= 1.0) {
            if (speedMovingService.ground) {
                speedMovingService.jump();
                speedMovingService.multiply(Double.longBitsToDouble(4607235561504112640L));
            }
            else {
                speedMovingService.my -= Double.longBitsToDouble(4579627594940416000L);
                speedMovingService.multiply(Double.longBitsToDouble(4607188633617694720L));
            }
        }
    }
    
    @Override
    public void strafe(final SpeedMovingService speedMovingService) {
        if (((this.f374571n3sax.equals("HypixelHop") && !speedMovingService.liquid) || this.f374571n3sax.equals("Legit")) && speedMovingService.ground && speedMovingService.moving()) {
            speedMovingService.jump();
            if (this.f374571n3sax.equals("HypixelHop") && !speedMovingService.usingItem) {
                speedMovingService.strafe(Double.longBitsToDouble(4600877379429072896L));
            }
        }
    }
    
    @Override
    public void beforeJump(final SpeedMovingService speedMovingService) {
        if (this.f374571n3sax.equals("HypixelLowHop") && speedMovingService.moving()) {
            speedMovingService.strafe(Math.max(speedMovingService.speed(), Float.intBitsToFloat(1049616187) + Float.intBitsToFloat(1040522936) * speedMovingService.speedLevel()));
        }
    }
    
    @Override
    public boolean cancelJump(final SpeedMovingService speedMovingService) {
        return this.f374571n3sax.equals("VulcanGround2.8.8");
    }
    
    @Override
    public void packet(final SpeedMovingService speedMovingService, final Packet packet) {
        if (this.f374571n3sax.equals("VulcanGround2.8.8") && packet.hasPosition && speedMovingService.collision(0.0, Double.longBitsToDouble(-4650957407178058629L), 0.0)) {
            packet.y += Double.longBitsToDouble(4572414629676717179L);
        }
    }
    
    @Override
    public void move(final SpeedMovingService speedMovingService, final Move move) {
        if (this.f374571n3sax.equals("TeleportCubeCraft") && speedMovingService.moving() && speedMovingService.ground && speedMovingService.actions.millis() - this.f7b7bxd7ictx >= 300L) {
            move.strafe(speedMovingService, this.maatm3yoz1kr("Port length"));
            this.f7b7bxd7ictx = speedMovingService.actions.millis();
        }
        if (!this.f374571n3sax.equals("SpectreOnGround") || !speedMovingService.moving() || speedMovingService.jumpKey) {
            return;
        }
        if (this.fcxllp6zlc7q >= 10) {
            if (speedMovingService.ground) {
                speedMovingService.stop();
                this.fcxllp6zlc7q = 0;
            }
            return;
        }
        if (speedMovingService.ground && speedMovingService.forward > 0.0f) {
            speedMovingService.forwardBoost(Double.longBitsToDouble(4594392195707961344L));
            move.x = speedMovingService.mx;
            move.y = Double.longBitsToDouble(4572414629676717179L);
            move.z = speedMovingService.mz;
            ++this.fcxllp6zlc7q;
        }
    }
}
