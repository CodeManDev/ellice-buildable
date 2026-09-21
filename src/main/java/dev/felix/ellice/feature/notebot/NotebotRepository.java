package dev.felix.ellice.feature.notebot;

import java.util.Optional;
import java.util.function.LongSupplier;

public final class NotebotRepository {
   private static final long timestamp = 50000000L;
   private static final long timestamp2 = 3000000000L;
   private final NotebotAvailableHandler notebotAvailableHandler;
   private final LongSupplier longSupplier;
   private final NotebotPositionData[] notebotPositionData = new NotebotPositionData[3];
   private long timestamp3;
   private long timestamp4;
   private long timestamp5;
   private boolean enabled;
   private boolean enabled2;
   private int count;
   private NotebotTitleService notebotTitleService;
   private NotebotChannelData notebotChannelData;
   private NotebotRepository.Phase phase2 = NotebotRepository.Phase.EMPTY;
   private NotebotRepository.Phase phase3 = NotebotRepository.Phase.PLAYING;
   private String text = "Choose a local audio file to begin.";
   private Object object;
   private double value;
   private double value2 = 1.0;
   private int count2;
   private int count3;
   private int count4;
   private NotebotRepository.PendingTune pendingTune;
   private boolean enabled3;

   public NotebotRepository(NotebotAvailableHandler notebotAvailable) {
      this(notebotAvailable, System::nanoTime);
   }

   public NotebotRepository(NotebotAvailableHandler notebotAvailable, LongSupplier currentLongSupplier) {
      this.notebotAvailableHandler = notebotAvailable;
      this.longSupplier = currentLongSupplier;
      this.timestamp3 = currentLongSupplier.getAsLong();
   }

   public NotebotTitleService song() {
      return this.notebotTitleService;
   }

   public NotebotChannelData arrangement() {
      return this.notebotChannelData;
   }

   public NotebotRepository.Phase phase() {
      return this.phase2;
   }

   public String message() {
      return this.text;
   }

   public double position() {
      return this.value;
   }

   public int skippedNotes() {
      return this.count;
   }

   public boolean loop() {
      return this.enabled2;
   }

   public void loop(boolean enabled) {
      this.enabled2 = enabled;
   }

   public double displayPosition() {
      if (this.notebotTitleService != null && this.phase2 == NotebotRepository.Phase.PLAYING && !this.enabled) {
         double x = Math.max(
            0.0, Math.min(1.0, (this.longSupplier.getAsLong() - this.timestamp3) / 5.0E7)
         );
         return Math.min(this.notebotTitleService.durationTicks(), this.value + x * this.value2);
      } else {
         return this.value;
      }
   }

   public void seek(double doubleValue) {
      if (this.notebotTitleService != null && Double.isFinite(doubleValue)) {
         this.value = Math.max(0L, Math.min(this.notebotTitleService.durationTicks(), Math.round(doubleValue)));
         this.updateState();
         this.timestamp3 = this.longSupplier.getAsLong();
         if (this.value >= this.notebotTitleService.durationTicks()) {
            this.phase2 = NotebotRepository.Phase.FINISHED;
            this.text = "At the end of the track. Press play to start again.";
         } else if (this.phase2 == NotebotRepository.Phase.FINISHED || this.phase2 == NotebotRepository.Phase.ERROR) {
            this.phase2 = NotebotRepository.Phase.READY;
            this.text = "Start from the selected position.";
         }
      }
   }

   private void updateState() {
      if (this.notebotChannelData == null) {
         this.count2 = 0;
      } else {
         int currentValue = 0;
         int currentSize = this.notebotChannelData.hits().size();

         while (currentValue < currentSize) {
            int nextValue = currentValue + currentSize >>> 1;
            if (this.notebotChannelData.hits().get(nextValue).tick() < this.value) {
               currentValue = nextValue + 1;
            } else {
               currentSize = nextValue;
            }
         }

         this.count2 = currentValue;
      }
   }

   public int tunedClicks() {
      return this.count4;
   }

   public void speed(double doubleValue) {
      if (Double.isFinite(doubleValue)) {
         this.value2 = Math.max(
            0.5, Math.min(1.5, doubleValue)
         );
      }
   }

   public void load(NotebotTitleService notebotTitle) {
      this.stop();
      this.notebotTitleService = notebotTitle;
      this.notebotChannelData = null;
      this.phase2 = NotebotRepository.Phase.READY;
      this.text = "Track ready. Scan nearby blocks, then play.";
   }

   public void start(NotebotAvailableHandler.Scan scan, boolean enabled, int currentValue) {
      if (this.notebotTitleService != null) {
         double doubleValue = this.phase2 == NotebotRepository.Phase.FINISHED ? 0.0 : this.value;
         this.stop();
         this.value = doubleValue;
         if (!scan.context().available()) {
            this.updateState4(scan.context().problem());
         } else {
            try {
               this.notebotChannelData = NotebotChannelData.create(this.notebotTitleService, scan.blocks(), enabled, currentValue);
            } catch (IllegalArgumentException illegalArgumentException) {
               this.updateState4(illegalArgumentException.getMessage());
               return;
            }

            this.updateState();
            this.object = scan.context().world();
            this.enabled3 = enabled;
            this.updateState2();
         }
      }
   }

   private void updateState2() {
      this.phase2 = NotebotRepository.Phase.TUNING;
      this.count3 = 0;
      this.count4 = 0;
      if (this.pendingTune != null && this.pendingTune.world() != this.object) {
         this.pendingTune = null;
      }

      this.timestamp4 = 0L;
      this.timestamp3 = this.longSupplier.getAsLong();
      this.text = this.enabled3 ? "Tuning the arrangement..." : "Checking the existing tuning...";
   }

   public void pause() {
      if (this.phase2 == NotebotRepository.Phase.PLAYING || this.phase2 == NotebotRepository.Phase.TUNING) {
         this.phase3 = this.phase2;
         this.phase2 = NotebotRepository.Phase.PAUSED;
         this.text = "Paused. Resume when you are ready.";
      }
   }

   public void resume() {
      if (this.phase2 == NotebotRepository.Phase.PAUSED && this.notebotChannelData != null) {
         if (this.checkCondition()) {
            if (this.phase3 == NotebotRepository.Phase.TUNING) {
               this.phase2 = NotebotRepository.Phase.TUNING;
               this.text = "Tuning the arrangement...";
            } else {
               this.updateState2();
            }
         }
      }
   }

   public void stop() {
      this.phase2 = this.notebotTitleService == null ? NotebotRepository.Phase.EMPTY : NotebotRepository.Phase.READY;
      this.value = 0.0;
      this.count2 = 0;
      this.count = 0;
      this.timestamp3 = this.longSupplier.getAsLong();
      this.text = this.notebotTitleService == null ? "Choose a local audio file to begin." : "Stopped. Your track is ready.";
   }

   public void disconnect() {
      this.stop();
      this.object = null;
      this.pendingTune = null;
      this.notebotChannelData = null;
      this.text = "World changed. Scan the new area before playing.";
   }

   public void tick() {
      if (this.phase2 == NotebotRepository.Phase.TUNING || this.phase2 == NotebotRepository.Phase.PLAYING) {
         long longValue = this.longSupplier.getAsLong();
         if (!this.checkCondition()) {
            this.timestamp3 = longValue;
         } else if (this.notebotAvailableHandler.context().paused()) {
            if (!this.enabled) {
               this.timestamp5 = longValue;
            }

            this.enabled = true;
            this.timestamp3 = longValue;
         } else {
            if (this.enabled) {
               long currentLongValue = longValue - this.timestamp5;
               if (this.pendingTune != null) {
                  this.pendingTune = new NotebotRepository.PendingTune(
                     this.pendingTune.world(), this.pendingTune.block(), this.pendingTune.sentAt() + currentLongValue
                  );
               }

               if (this.timestamp4 != 0L) {
                  this.timestamp4 += currentLongValue;
               }

               this.enabled = false;
               this.timestamp3 = Math.max(this.timestamp3, longValue - 50000000L);
            }

            if (this.phase2 == NotebotRepository.Phase.TUNING) {
               this.timestamp3 = longValue;
               this.updateState3(longValue);
            } else {
               double doubleValue = Math.max(0.0, (longValue - this.timestamp3) / 5.0E7);
               this.timestamp3 = longValue;
               if (doubleValue > 8.0) {
                  this.pause();
                  this.text = "Paused after a long frame stall. Resume to continue without a burst of old notes.";
               } else if (doubleValue != 0.0) {
                  double currentDoubleValue = Math.min(this.notebotTitleService.durationTicks(), this.value + doubleValue * this.value2);
                  long nextLongValue = 0L;
                  int index = 0;
                  int currentIndex = this.count2;

                  while (currentIndex < this.notebotChannelData.hits().size() && this.notebotChannelData.hits().get(currentIndex).tick() < currentDoubleValue) {
                     NotebotChannelData.Hit hit = this.notebotChannelData.hits().get(currentIndex++);
                     long previousLongValue = 1L << hit.channel();
                     if (index != this.notebotPositionData.length && (nextLongValue & previousLongValue) == 0L) {
                        NotebotChannelData.Channel currentChannel = this.notebotChannelData.channels().get(hit.channel());
                        Optional result = this.notebotAvailableHandler.inspect(currentChannel.block().position());
                        if (result.isEmpty()
                           || !((NotebotPositionData)result.get()).instrument().equals(currentChannel.block().instrument())
                           || ((NotebotPositionData)result.get()).pitch() != currentChannel.targetPitch()) {
                           this.pause();
                           this.text = "A block changed or is out of reach. Move closer, then resume.";
                           return;
                        }

                        this.notebotPositionData[index++] = (NotebotPositionData)result.get();
                        nextLongValue |= previousLongValue;
                     }
                  }

                  this.count = this.count + (currentIndex - this.count2 - index);
                  this.count2 = currentIndex;
                  this.value = currentDoubleValue;

                  for (int nextIndex = 0; nextIndex < index; nextIndex++) {
                     if (!this.notebotAvailableHandler.play(this.notebotPositionData[nextIndex])) {
                        this.count += index - nextIndex;
                        this.pause();
                        this.text = "Could not strike a block. Check your reach and empty hand.";
                        return;
                     }
                  }

                  if (this.value >= this.notebotTitleService.durationTicks()) {
                     if (this.enabled2) {
                        this.value = 0.0;
                        this.count2 = 0;
                        this.text = "Looping through nearby note blocks.";
                     } else {
                        this.phase2 = NotebotRepository.Phase.FINISHED;
                        this.text = "Performance finished.";
                     }
                  }

                  return;
               }
            }
         }
      }
   }

   private boolean checkCondition() {
      NotebotAvailableHandler.Context currentContext = this.notebotAvailableHandler.context();
      if (currentContext.world() != this.object) {
         this.disconnect();
         return false;
      }

      if (!currentContext.available()) {
         if (this.phase2 != NotebotRepository.Phase.PAUSED) {
            this.phase3 = this.phase2;
         }

         this.phase2 = NotebotRepository.Phase.PAUSED;
         this.text = currentContext.problem();
         return false;
      } else {
         return true;
      }
   }

   private void updateState3(long offset) {
      if (this.checkCondition2(offset)) {
         while (this.count3 < this.notebotChannelData.channels().size()) {
            NotebotChannelData.Channel channel = this.notebotChannelData.channels().get(this.count3);
            Optional result = this.notebotAvailableHandler.inspect(channel.block().position());
            if (result.isEmpty() || !((NotebotPositionData)result.get()).instrument().equals(channel.block().instrument())) {
               this.updateState4("A note block moved, changed instrument or became unreachable. Scan again.");
               return;
            }

            NotebotPositionData notebotPositionData = (NotebotPositionData)result.get();
            if (notebotPositionData.pitch() != channel.targetPitch()) {
               if (!this.enabled3) {
                  this.updateState4("Existing tuning changed. Scan again or enable auto-tune.");
                  return;
               }

               if (!this.notebotAvailableHandler.tune(notebotPositionData)) {
                  this.updateState4("Could not tune this block. Check your empty hand and reach.");
                  return;
               }

               this.pendingTune = new NotebotRepository.PendingTune(this.object, notebotPositionData, offset);
               return;
            }

            this.count3++;
         }

         if (offset >= this.timestamp4) {
            this.phase2 = NotebotRepository.Phase.PLAYING;
            this.timestamp3 = offset;
            this.text = "Playing through nearby note blocks.";
         }
      }
   }

   private boolean checkCondition2(long longValue) {
      if (this.pendingTune == null) {
         return true;
      }

      Optional result = this.notebotAvailableHandler.inspect(this.pendingTune.block().position());
      if (result.isEmpty()) {
         this.updateState4("The block being tuned is out of reach. Move closer and try again.");
         return false;
      }

      NotebotPositionData notebotPositionData = (NotebotPositionData)result.get();
      if (!notebotPositionData.instrument().equals(this.pendingTune.block().instrument())) {
         this.pendingTune = null;
         this.updateState4("A note block changed instrument. Scan again.");
      } else {
         if (notebotPositionData.pitch() == this.pendingTune.expectedPitch()) {
            this.pendingTune = null;
            this.count4++;
            this.timestamp4 = longValue + 200000000L;
            return true;
         }

         if (notebotPositionData.pitch() != this.pendingTune.block().pitch()) {
            this.pendingTune = null;
            this.updateState4("A block was retuned by something else. Scan again.");
         } else if (longValue - this.pendingTune.sentAt() >= 3000000000L) {
            NotebotPositionData.Position currentPosition = this.pendingTune.block().position();
            this.pendingTune = null;
            this.updateState4(
               "The block at %d, %d, %d did not change pitch. Try a manual right-click, then scan again."
                  .formatted(currentPosition.x(), currentPosition.y(), currentPosition.z())
            );
         }
      }

      return false;
   }

   private void updateState4(String currentText) {
      this.phase2 = NotebotRepository.Phase.ERROR;
      this.text = currentText != null && !currentText.isBlank() ? currentText : "Playback is unavailable.";
   }

   private record PendingTune(Object world, NotebotPositionData block, long sentAt) {
      int expectedPitch() {
         return (this.block.pitch() + 1) % 25;
      }
   }

   public enum Phase {
      EMPTY,
      READY,
      TUNING,
      PLAYING,
      PAUSED,
      FINISHED,
      ERROR;


      private static NotebotRepository.Phase[] $values() {
         return new NotebotRepository.Phase[]{EMPTY, READY, TUNING, PLAYING, PAUSED, FINISHED, ERROR};
      }
   }
}

