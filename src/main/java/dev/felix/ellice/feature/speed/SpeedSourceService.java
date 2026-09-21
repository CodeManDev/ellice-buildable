package dev.felix.ellice.feature.speed;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public final class SpeedSourceService {
  public static final String REVISION = "941500a060e89750aeae4b1cbe200ca06907c850";
  public static final List<SpeedSourceService.Source> SOURCES = collectValues();

  private SpeedSourceService() {}

  public static SpeedSourceService.Source source(String text) {
    return SOURCES.stream()
        .filter(item -> item.definition.id().equals(text))
        .findFirst()
        .orElse(null);
  }

  private static List<SpeedSourceService.Source> collectValues() {
    ArrayList arrayList = new ArrayList();
    updateState(
        arrayList,
        SpeedSourceService.Client.RISE,
        "NCP",
        "NCP",
        "NCP",
        "Strafe-event friction, potion-aware takeoff and a configurable bunny slope.",
        "NCPSpeed.java",
        createOption("Jump motion", 0.4, 0.4, 0.42, 0.01),
        createOption("Ground multiplier", 1.75, 0.1, 2.5, 0.05),
        createOption("Bunny slope", 0.66, 0.0, 1.0, 0.01),
        createOption("Timer", 1.0, 0.1, 10.0, 0.05));
    updateState(
        arrayList,
        SpeedSourceService.Client.RISE,
        "KoksCraft",
        "KoksCraft",
        "KoksCraft",
        "Alternating two/four gravity predictions after each hop; preserves hurt motion.",
        "KoksCraftSpeed.java");
    updateState(
        arrayList,
        SpeedSourceService.Client.RISE,
        "VulcanBHop",
        "Vulcan BHop",
        "Vulcan",
        "Ground boost, early air steering and a fifth-air-tick gravity advance.",
        "VulcanSpeed.java");
    updateState(
        arrayList,
        SpeedSourceService.Client.RISE,
        "VerusHop",
        "Verus Hop",
        "Verus",
        "Separate ground, air and backward speeds with potion increments.",
        "VerusSpeed.java");
    updateState(
        arrayList,
        SpeedSourceService.Client.RISE,
        "VerusYPort",
        "Verus yPort",
        "Verus",
        "A vertical move pulse with independent horizontal player velocity.",
        "VerusSpeed.java");
    updateState(
        arrayList,
        SpeedSourceService.Client.SLACK,
        "HypixelBasic",
        "Hypixel Basic",
        "Watchdog",
        "Potion-dependent takeoff and slight air acceleration; archived fixed hop stage.",
        "hypixel/HypixelBasicSpeed.java");
    updateState(
        arrayList,
        SpeedSourceService.Client.SLACK,
        "NCPHop",
        "NCP Hop",
        "NCP",
        "Minimum takeoff speed, air steering and a fifth-tick head-hitter descent.",
        "ncp/NCPHopSpeed.java");
    updateState(
        arrayList,
        SpeedSourceService.Client.SLACK,
        "VulcanLow",
        "Vulcan Low",
        "Vulcan",
        "Randomized 0.54–0.56 takeoff followed by a low-hop pull down.",
        "vulcan/VulcanLowSpeed.java");
    updateState(
        arrayList,
        SpeedSourceService.Client.SLACK,
        "VulcanPort",
        "Vulcan Port",
        "Vulcan",
        "Increased air acceleration, minimum air speed and delayed pull down.",
        "vulcan/VulcanPortSpeed.java");
    updateState(
        arrayList,
        SpeedSourceService.Client.SLACK,
        "VerusHop",
        "Verus Hop",
        "Verus",
        "Move-vector hops with one landing-friction drop followed by gradual decay.",
        "verus/VerusHopSpeed.java");
    updateState(
        arrayList,
        SpeedSourceService.Client.ETERNAL,
        "Accel",
        "Acceleration",
        "General",
        "Build speed over 100 moving update phases; reset the ramp when stopped.",
        "AccelMode.java",
        createOption("Speed", 0.725, 0.2, 10.0, 0.05));
    updateState(
        arrayList,
        SpeedSourceService.Client.ETERNAL,
        "AGC",
        "AGC",
        "AGC",
        "Add 0.625 to base ground speed, then decay the move vector each air tick.",
        "AGCMode.java");
    updateState(
        arrayList,
        SpeedSourceService.Client.ETERNAL,
        "OldNCP",
        "Old NCP",
        "NCP",
        "Preparation, launch and last-distance decay with fractional-height correction.",
        "OldNCPMode.java");
    updateState(
        arrayList,
        SpeedSourceService.Client.ETERNAL,
        "YPort",
        "Y-Port",
        "General",
        "Ground launch, first-air-tick descent and grounded movement packets.",
        "YPortMode.java");
    updateState(
        arrayList,
        SpeedSourceService.Client.ATANI,
        "BHop",
        "BHop",
        "General",
        "Configurable move-vector speed and jump height.",
        "",
        createOption("Boost", 1.2, 0.1, 5.0, 0.1),
        createOption("Jump height", 0.41, 0.01, 1.0, 0.01));
    updateState(
        arrayList,
        SpeedSourceService.Client.ATANI,
        "Karhu",
        "Karhu",
        "Karhu",
        "55-percent jump height with a small randomized air timer.",
        "");
    updateState(
        arrayList,
        SpeedSourceService.Client.ATANI,
        "IntaveGroundStrafe",
        "Intave Ground Strafe",
        "Intave",
        "Repeated-ground recovery, small air acceleration and subtle timer changes.",
        "");
    updateState(
        arrayList,
        SpeedSourceService.Client.ATANI,
        "IncognitoNormal",
        "Incognito Normal",
        "Incognito",
        "Randomized ground speed and air steering with the original potion helper.",
        "");
    updateState(
        arrayList,
        SpeedSourceService.Client.ATANI,
        "NCPHop",
        "NCP Hop",
        "NCP",
        "Natural hops and directional steering; timer rises after 0.9 blocks of falling.",
        "");
    updateState(
        arrayList,
        SpeedSourceService.Client.ATANI,
        "MineMenClub",
        "MineMenClub",
        "AGC",
        "Sprint hops with air steering suspended during the first hurt ticks.",
        "");
    updateState(
        arrayList,
        SpeedSourceService.Client.EXHIBITION,
        "Hop",
        "Hop",
        "NCP",
        "1.533 launch multiplier, third-stage slowdown and collision recovery.",
        "");
    updateState(
        arrayList,
        SpeedSourceService.Client.EXHIBITION,
        "OldHop",
        "Old Hop",
        "NCP",
        "Older 0.4-high staged jumps with a 2.149 launch multiplier.",
        "");
    updateState(
        arrayList,
        SpeedSourceService.Client.EXHIBITION,
        "OldSlow",
        "Old Slow",
        "NCP",
        "Older staged jumps with the slower 1.749 launch multiplier.",
        "");
    updateState(
        arrayList,
        SpeedSourceService.Client.EXHIBITION,
        "OnGround",
        "On Ground",
        "NCP",
        "Alternating ground velocity and odd-tick packet height with a 1.085 timer.",
        "");
    updateState(
        arrayList,
        SpeedSourceService.Client.EXHIBITION,
        "YPort",
        "YPort",
        "NCP",
        "Two-stage horizontal boost with a separate packet-height pulse.",
        "");
    updateState(
        arrayList,
        SpeedSourceService.Client.ETB,
        "Bhop",
        "Bhop",
        "NCP",
        "Three-stage bunny hops, 2.1 launch multiplier and a 1.07 timer.",
        "");
    updateState(
        arrayList,
        SpeedSourceService.Client.ETB,
        "HypixelHop",
        "Hypixel Hop",
        "Watchdog",
        "Staged hops with launch, slowdown and recovery timer changes.",
        "");
    updateState(
        arrayList,
        SpeedSourceService.Client.ETB,
        "Mineplex",
        "Mineplex",
        "Mineplex",
        "0.3-high jumps, fixed 0.64 launch speed and distance-based decay.",
        "");
    updateState(
        arrayList,
        SpeedSourceService.Client.ETB,
        "OldGuardian",
        "Old Guardian",
        "Guardian",
        "1.75 ground boost with a tiny position packet before velocity changes.",
        "");
    updateState(
        arrayList,
        SpeedSourceService.Client.ETB,
        "GuardianYport",
        "Guardian Yport",
        "Guardian",
        "0.08-low jumps, 0.7 ground speed and a movement-only 1.6 timer.",
        "");
    return List.copyOf(arrayList);
  }

  private static SpeedDefinitionService.Option createOption(
      String text,
      double doubleValue,
      double currentDoubleValue,
      double nextDoubleValue,
      double previousDoubleValue) {
    return new SpeedDefinitionService.Option(
        text, doubleValue, currentDoubleValue, nextDoubleValue, previousDoubleValue, List.of());
  }

  private static void updateState(
      List<SpeedSourceService.Source> items,
      SpeedSourceService.Client client,
      String text,
      String currentText,
      String nextText,
      String previousText,
      String sourceText,
      SpeedDefinitionService.Option... options) {
    SpeedDefinitionService.Definition definition =
        new SpeedDefinitionService.Definition(
            client.namespace + "/" + text,
            currentText,
            client.edition,
            nextText,
            previousText,
            true,
            List.of(options));
    items.add(new SpeedSourceService.Source(client, text, sourceText, definition));
  }

  public enum Client {
    RISE("rise-6", "Rise 6.0", "com/alan/clients/module/impl/movement/speed/"),
    SLACK("slack-081024", "Slack 081024", "cc/slack/features/modules/impl/movement/speeds/"),
    ETERNAL("eternal-3", "Eternal 3.0.0", "dev/eternal/client/module/impl/movement/speed/"),
    ATANI(
        "atani-0.0.3", "Atani 0.0.3", "tech/atani/client/feature/module/impl/movement/Speed.java"),
    EXHIBITION(
        "exhibition-081618", "Exhibition 081618", "exhibition/module/impl/movement/Speed.java"),
    ETB("etb-0.6", "ETB 0.6", "com/enjoytheban/module/modules/movement/Speed.java");

    public final String namespace;
    public final String edition;
    public final String path;

    Client(String text, String currentText, String nextText) {
      this.namespace = text;
      this.edition = currentText;
      this.path = nextText;
    }

    private static SpeedSourceService.Client[] $values() {
      return new SpeedSourceService.Client[] {RISE, SLACK, ETERNAL, ATANI, EXHIBITION, ETB};
    }
  }

  public record Source(
      SpeedSourceService.Client client,
      String mode,
      String file,
      SpeedDefinitionService.Definition definition) {
    public String url() {
      try {
        return new URI(
                "https",
                "github.com",
                "/iroot3/mc-client-sources/blob/941500a060e89750aeae4b1cbe200ca06907c850/sources/"
                    + this.client.edition
                    + "/"
                    + this.client.path
                    + this.file,
                null)
            .toASCIIString();
      } catch (URISyntaxException uRISyntaxException) {
        throw new IllegalStateException(uRISyntaxException);
      }
    }

    SpeedStrafeHandler create(SpeedOperationHandler speedOperation) {
      return switch (this.client) {
        case RISE -> new SpeedStrafeService(this.mode, speedOperation);
        case SLACK -> new SlackSpeedMode(this.mode, speedOperation);
        case ETERNAL -> new EternalSpeedMode(this.mode, speedOperation);
        case ATANI -> new AtaniSpeedMode(this.mode, speedOperation);
        case EXHIBITION -> new SpeedMotionService(this.mode, speedOperation);
        case ETB -> new EtbSpeedMode(this.mode, speedOperation);
      };
    }
  }
}
