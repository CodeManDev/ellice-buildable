package dev.felix.ellice.hud.layout;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import java.time.LocalTime;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;

public final class HudVariableResolver implements LayoutOperationHandler {
  private final Minecraft minecraft2 = Minecraft.getInstance();

  @Override
  public String string(String text) {
    if (text == null) {
      return "";
    }

    return switch (text) {
      case "client.fps", "fps" -> Integer.toString(this.calculateValue());
      case "client.version" -> CoreIsInitializedHandler.VERSION;
      case "player.name" -> this.createText("Player");
      case "player.health" -> createText6(this.calculateValue3("health"), 1);
      case "player.maxHealth" -> createText6(this.calculateValue3("maxHealth"), 0);
      case "player.hunger" -> Integer.toString((int) this.calculateValue3("hunger"));
      case "player.armor" -> Integer.toString((int) this.calculateValue3("armor"));
      case "player.xp" -> createText6(this.calculateValue3("xp"), 2);
      case "player.level" -> Integer.toString((int) this.calculateValue3("level"));
      case "player.x" -> createText6(this.calculateValue3("x"), 1);
      case "player.y" -> createText6(this.calculateValue3("y"), 1);
      case "player.z" -> createText6(this.calculateValue3("z"), 1);
      case "player.coords" -> this.createText3();
      case "player.facing" -> this.createText4();
      case "world.time" -> this.createText5();
      case "world.day" -> Long.toString(this.calculateValue4());
      case "server.brand" -> this.createText2("Singleplayer");
      case "server.ping" -> Integer.toString(this.calculateValue2());
      case "wall.time" -> LocalTime.now().withNano(0).toString();
      default -> "";
    };
  }

  @Override
  public float number(String text) {
    if (text == null) {
      return 0.0F;
    }

    return switch (text) {
      case "client.fps", "fps" -> this.calculateValue();
      case "player.health" -> this.calculateValue3("health");
      case "player.maxHealth" -> this.calculateValue3("maxHealth");
      case "player.hunger" -> this.calculateValue3("hunger");
      case "player.armor" -> this.calculateValue3("armor");
      case "player.xp" -> this.calculateValue3("xp");
      case "player.level" -> this.calculateValue3("level");
      case "player.x" -> this.calculateValue3("x");
      case "player.y" -> this.calculateValue3("y");
      case "player.z" -> this.calculateValue3("z");
      case "server.ping" -> this.calculateValue2();
      default -> 0.0F;
    };
  }

  @Override
  public float max(String text) {
    if (text == null) {
      return 1.0F;
    }

    return switch (text) {
      case "player.health" -> Math.max(1.0F, this.calculateValue3("maxHealth"));
      case "player.hunger" -> 20.0F;
      case "player.armor" -> 20.0F;
      case "player.xp" -> 1.0F;
      default -> 1.0F;
    };
  }

  private int calculateValue() {
    try {
      return this.minecraft2.getFps();
    } catch (Throwable exception) {
      return 0;
    }
  }

  private int calculateValue2() {
    try {
      if (this.minecraft2.player != null && this.minecraft2.getConnection() != null) {
        PlayerInfo playerInfo =
            this.minecraft2.getConnection().getPlayerInfo(this.minecraft2.player.getUUID());
        return playerInfo != null ? Math.max(0, playerInfo.getLatency()) : 0;
      } else {
        return 0;
      }
    } catch (Throwable exception) {
      return 0;
    }
  }

  private String createText(String text) {
    if (this.minecraft2.player != null) {
      return this.minecraft2.player.getName().getString();
    } else {
      return this.minecraft2.getUser() != null ? this.minecraft2.getUser().getName() : text;
    }
  }

  private String createText2(String text) {
    try {
      ClientPacketListener clientPacketListener = this.minecraft2.getConnection();
      if (clientPacketListener == null) {
        return text;
      }

      String currentText = clientPacketListener.serverBrand();
      return currentText != null && !currentText.isEmpty() ? currentText : "Multiplayer";
    } catch (Throwable exception) {
      return text;
    }
  }

  private float calculateValue3(String text) {
    LocalPlayer localPlayer = this.minecraft2.player;
    if (localPlayer == null) {
      return 0.0F;
    }

    return switch (text) {
      case "health" -> localPlayer.getHealth();
      case "maxHealth" -> localPlayer.getMaxHealth();
      case "hunger" -> localPlayer.getFoodData().getFoodLevel();
      case "armor" -> localPlayer.getArmorValue();
      case "xp" -> localPlayer.experienceProgress;
      case "level" -> localPlayer.experienceLevel;
      case "x" -> (float) localPlayer.getX();
      case "y" -> (float) localPlayer.getY();
      case "z" -> (float) localPlayer.getZ();
      default -> 0.0F;
    };
  }

  private String createText3() {
    return this.minecraft2.player == null
        ? "—"
        : String.format(
            Locale.ROOT,
            "%.0f, %.0f, %.0f",
            this.minecraft2.player.getX(),
            this.minecraft2.player.getY(),
            this.minecraft2.player.getZ());
  }

  private String createText4() {
    if (this.minecraft2.player == null) {
      return "—";
    } else {
      float value = (this.minecraft2.player.getYRot() % 360.0F + 360.0F) % 360.0F;
      if (value < 22.5 || value >= 337.5) {
        return "S";
      } else if (value < 67.5) {
        return "SW";
      } else if (value < 112.5) {
        return "W";
      } else if (value < 157.5) {
        return "NW";
      } else if (value < 202.5) {
        return "N";
      } else if (value < 247.5) {
        return "NE";
      } else {
        return value < 292.5 ? "E" : "SE";
      }
    }
  }

  private String createText5() {
    if (this.minecraft2.level == null) {
      return "—";
    }

    long size = this.calculateValue5() % 24000L;
    long currentSize = (size / 1000L + 6L) % 24L;
    long nextSize = size % 1000L * 60L / 1000L;
    return String.format(Locale.ROOT, "%02d:%02d", currentSize, nextSize);
  }

  private long calculateValue4() {
    return this.minecraft2.level == null ? 0L : this.calculateValue5() / 24000L;
  }

  private long calculateValue5() {
    if (this.minecraft2.level == null) {
      return 0L;
    }

    for (String text : new String[] {"getOverworldClockTime", "getDayTime", "getGameTime"}) {
      try {
        if (this.minecraft2.level.getClass().getMethod(text).invoke(this.minecraft2.level)
            instanceof Number number) {
          return number.longValue();
        }
      } catch (ReflectiveOperationException reflectiveOperationException) {
      }
    }

    return 0L;
  }

  private static String createText6(float value, int currentValue) {
    return currentValue <= 0
        ? Integer.toString(Math.round(value))
        : String.format(Locale.ROOT, "%." + currentValue + "f", value);
  }
}
