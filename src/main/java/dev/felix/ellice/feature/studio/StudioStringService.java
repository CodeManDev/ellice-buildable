package dev.felix.ellice.feature.studio;

import dev.felix.ellice.hud.layout.LayoutOperationHandler;

public final class StudioStringService implements LayoutOperationHandler {
  public float health = 16.0F;

  @Override
  public String string(String text) {
    return switch (text) {
      case "player.name" -> "ellice";
      case "client.fps" -> "144";
      case "server.ping" -> "24";
      case "player.health" -> Integer.toString(Math.round(this.health));
      case "player.coords" -> "120, 64, -48";
      default -> "";
    };
  }

  @Override
  public float number(String text) {
    return switch (text) {
      case "player.health" -> this.health;
      case "player.hunger" -> 18.0F;
      case "client.fps" -> 144.0F;
      case "server.ping" -> 24.0F;
      default -> 0.0F;
    };
  }

  @Override
  public float max(String text) {
    return !text.equals("player.health") && !text.equals("player.hunger") ? 1.0F : 20.0F;
  }
}
