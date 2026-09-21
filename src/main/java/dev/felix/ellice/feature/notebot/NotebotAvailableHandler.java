package dev.felix.ellice.feature.notebot;

import java.util.List;
import java.util.Optional;

public interface NotebotAvailableHandler {
   NotebotAvailableHandler.Context context();

   NotebotAvailableHandler.Scan scan();

   Optional<NotebotPositionData> inspect(NotebotPositionData.Position position);

   boolean tune(NotebotPositionData notebotPositionData);

   boolean play(NotebotPositionData notebotPositionData);

   record Context(Object world, String problem, boolean paused) {
      public boolean available() {
         return this.world != null && this.problem.isEmpty();
      }
   }

   record Scan(NotebotAvailableHandler.Context context, List<NotebotPositionData> blocks, int obstructed, int percussion) {
      public Scan(NotebotAvailableHandler.Context context, List<NotebotPositionData> blocks, int obstructed, int percussion) {
         blocks = List.copyOf(blocks);
         this.context = context;
         this.blocks = blocks;
         this.obstructed = obstructed;
         this.percussion = percussion;
      }
   }
}
