package dev.felix.ellice.render.world.cosmetic;

import java.util.Collection;
import net.minecraft.world.entity.player.Player;

public interface CosmeticOperationHandler<S> extends AutoCloseable {
  void render(Collection<? extends Player> items, CosmeticData cosmeticData, S s);

  void shutdown();

  @Override
  default void close() {
    this.shutdown();
  }
}
