package dev.felix.ellice.feature.cape;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.MemoryCacheImageInputStream;

public final class CapeRevisionService implements AutoCloseable {
   private final CapeRepository capeRepository;
   private final Consumer<Runnable> consumer;
   private final ExecutorService executor = Executors.newFixedThreadPool(3, item -> {
      Thread thread = new Thread(item, "ellice Cape media");
      thread.setDaemon(true);
      return thread;
   });
   private final ThreadPoolExecutor executor2 = new ThreadPoolExecutor(
      2, 2, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), item -> {
         Thread thread = new Thread(item, "ellice Cape thumbnails");
         thread.setDaemon(true);
         return thread;
      }
   );
   private Future<?> future;
   private Future<?> future2;
   private final Map<String, BufferedImage> text = new LinkedHashMap<>();
   private final Map<String, CapeSizeService> text2 = new HashMap<>();
   private final Set<String> text3 = new HashSet<>();
   private int count;
   private int count2;
   private int count3;
   private boolean enabled;
   private long timestamp;
   private CapeRevisionService.Loaded loaded;
   private CapeRevisionService.Loaded loaded2;
   private CapePreviewRenderer.Style renderer = CapePreviewRenderer.Style.defaults();
   private final CapePositionService capePositionService = new CapePositionService();
   private BufferedImage bufferedImage = CapeRecoveredFactory.mannequin();
   private boolean enabled2;
   private List<CapeSearchSelector.Entry> items = List.of();
   private int count4 = -1;
   private String text4 = "Choose a cape. Make it yours.";
   private String text5;
   private boolean enabled3;
   private boolean enabled4;
   private boolean enabled5;
   private boolean enabled6;

   public CapeRevisionService(CapeRepository currentCapeRepository, Consumer<Runnable> currentConsumer) {
      this.capeRepository = currentCapeRepository;
      this.consumer = currentConsumer;
      this.text5 = currentCapeRepository.key();
      this.loaded = new CapeRevisionService.Loaded(
         new CapeRepository.Selection("preset", "Aurora", "Aurora", "", "Made for ellice", this.renderer), this.preset("Aurora"), null
      );
   }

   public long revision() {
      return this.timestamp;
   }

   public void dispatch(Runnable runnable) {
      this.consumer.accept(runnable);
   }

   public CapeRevisionService.Loaded selected() {
      return this.loaded;
   }

   public CapeRevisionService.Loaded applied() {
      return this.loaded2;
   }

   public CapePreviewRenderer.Style style() {
      return this.renderer;
   }

   public CapePositionService playback() {
      return this.capePositionService;
   }

   public BufferedImage skin() {
      return this.bufferedImage;
   }

   public boolean slim() {
      return this.enabled2;
   }

   public String message() {
      return this.text4;
   }

   public boolean error() {
      return this.enabled5;
   }

   public boolean loading() {
      return this.enabled3;
   }

   public boolean searching() {
      return this.enabled4;
   }

   public boolean saving() {
      return this.enabled6;
   }

   public String key() {
      return this.text5;
   }

   public List<CapeSearchSelector.Entry> results() {
      return this.items;
   }

   public int nextOffset() {
      return this.count4;
   }

   public CapeSizeService preset(String text) {
      return this.text2.computeIfAbsent(text, CapeRecoveredFactory::create);
   }

   public BufferedImage thumbnail(String currentText) {
      return this.text.get(currentText);
   }

   public void style(CapePreviewRenderer.Style currentStyle) {
      this.renderer = currentStyle;
      this.capePositionService.speed(this.renderer.speed(), System.nanoTime());
      this.timestamp++;
   }

   public void togglePlayback() {
      this.capePositionService.pause(!this.capePositionService.paused(), System.nanoTime());
      this.timestamp++;
   }

   public void choosePreset(String text) {
      this.count++;
      updateState4(this.future);
      this.enabled3 = false;
      this.renderer = CapePreviewRenderer.Style.defaults();
      this.updateState(
         new CapeRevisionService.Loaded(
            new CapeRepository.Selection("preset", text, text, "", "Made for ellice", this.renderer), this.preset(text), null
         )
      );
   }

   private void updateState(CapeRevisionService.Loaded currentLoaded) {
      this.loaded = currentLoaded;
      this.renderer = currentLoaded.source().style() == null ? CapePreviewRenderer.Style.defaults() : currentLoaded.source().style();
      this.capePositionService.seek(0.0, System.nanoTime());
      this.capePositionService.speed(this.renderer.speed(), System.nanoTime());
      this.capePositionService.pause(false, System.nanoTime());
      this.status("Ready to wear · " + currentLoaded.animation().size() + " frame" + (currentLoaded.animation().size() == 1 ? "" : "s"), false);
   }

   public void restore() {
      int currentCount = ++this.count;
      updateState4(this.future);
      this.enabled3 = true;
      this.timestamp++;
      this.future = this.createValue(
         () -> {
            CapeRepository.Selection selection = this.capeRepository.load();
            if (selection == null) {
               return new CapeRevisionService.Loaded(
                  new CapeRepository.Selection("preset", "Aurora", "Aurora", "", "Made for ellice", CapePreviewRenderer.Style.defaults()),
                  CapeRecoveredFactory.create("Aurora"),
                  null
               );
            } else if ("vanilla".equals(selection.kind())) {
               return new CapeRevisionService.Loaded(selection, null, null);
            } else if ("preset".equals(selection.kind())) {
               return new CapeRevisionService.Loaded(selection, CapeRecoveredFactory.create(selection.reference()), null);
            } else if ("giphy".equals(selection.kind())) {
               CapeSearchSelector.Entry entry = CapeSearchSelector.giphy(selection.reference(), this.text5);
               byte[] bytes = CapeHttpsService.get(CapeHttpsService.https(entry.media()), 16777216);
               return new CapeRevisionService.Loaded(selection, CapeCodec.decode(bytes), null);
            } else {
               byte[] currentBytes = this.capeRepository.media(selection.reference());
               return new CapeRevisionService.Loaded(selection, CapeCodec.decode(currentBytes), currentBytes);
            }
         },
         item -> {
            if (currentCount == this.count) {
               this.enabled3 = false;
               this.loaded2 = item;
               CapeEnabledService.apply(item.animation(), item.source().style() == null ? CapePreviewRenderer.Style.defaults() : item.source().style());
               if (item.animation() != null) {
                  this.updateState(item);
               } else {
                  this.status("Your original Minecraft cape is selected.", false);
               }
            }
         },
         item -> {
            if (currentCount == this.count) {
               this.enabled3 = false;
               this.status(item, true);
            }
         }
      );
   }

   public void importFile(Path path) {
      this.updateState2(
         () -> {
            byte[] bytes = CapeRepository.readFile(path);
            CapeSizeService capeSize = CapeCodec.decode(bytes);
            int width = !path.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".gif")
                  && capeSize.frame(0).getWidth() == capeSize.frame(0).getHeight() * 2
               ? 1
               : 0;
            CapePreviewRenderer.Style style = new CapePreviewRenderer.Style(CapePreviewRenderer.Fit.FILL, (width != 0), false, -15264476, 1.0);
            return new CapeRevisionService.Loaded(
               new CapeRepository.Selection("file", "", path.getFileName().toString(), "", "Local file", style), capeSize, bytes
            );
         }
      );
   }

   public void importLink(String text) {
      this.updateState2(
         () -> {
            URI uRI = CapeHttpsService.https(text);
            String host = uRI.getHost().toLowerCase(Locale.ROOT);
            if (!host.equals("giphy.com") && !host.equals("www.giphy.com")) {
               int value = !host.equals("giphy.com") && !host.endsWith(".giphy.com") ? 0 : 1;
               if (value != 0) {
                  throw new IOException("For GIPHY, use its search or paste the GIF page link with your API key configured.");
               }

               byte[] bytes = CapeHttpsService.get(uRI, 16777216);
               String currentText = uRI.getPath().substring(uRI.getPath().lastIndexOf(47) + 1);
               if (currentText.isBlank()) {
                  currentText = "Imported image";
               }

               return new CapeRevisionService.Loaded(
                  new CapeRepository.Selection("file", "", currentText, text, "Imported link", CapePreviewRenderer.Style.defaults()),
                  CapeCodec.decode(bytes),
                  bytes
               );
            } else {
               String nextText = uRI.getPath().substring(uRI.getPath().lastIndexOf(47) + 1);
               String previousText = nextText.substring(nextText.lastIndexOf(45) + 1);
               return this.createLoaded(CapeSearchSelector.giphy(previousText, this.text5));
            }
         }
      );
   }

   public void choose(CapeSearchSelector.Entry entry) {
      this.updateState2(() -> this.createLoaded(entry));
   }

   private CapeRevisionService.Loaded createLoaded(CapeSearchSelector.Entry entry) throws IOException {
      byte[] bytes = CapeHttpsService.get(CapeHttpsService.https(entry.media()), 16777216);
      int value = entry.provider() == CapeSearchSelector.Provider.GIPHY ? 1 : 0;
      return new CapeRevisionService.Loaded(
         new CapeRepository.Selection(
            value != 0 ? "giphy" : "file",
            value != 0 ? entry.id() : "",
            entry.title(),
            entry.page(),
            entry.credit(),
            CapePreviewRenderer.Style.defaults()
         ),
         CapeCodec.decode(bytes),
         value != 0 ? null : bytes
      );
   }

   private void updateState2(Callable<CapeRevisionService.Loaded> callable) {
      int currentCount = ++this.count;
      updateState4(this.future);
      this.enabled3 = true;
      this.status("Loading and decoding animation…", false);
      this.future = this.createValue(callable, item -> {
         if (currentCount == this.count) {
            this.enabled3 = false;
            this.updateState(item);
         }
      }, item -> {
         if (currentCount == this.count) {
            this.enabled3 = false;
            this.status(item, true);
         }
      });
   }

   public void apply(Runnable runnable) {
      if (this.loaded != null && !this.enabled3 && !this.enabled6) {
         CapeRevisionService.Loaded currentLoaded = this.loaded;
         CapePreviewRenderer.Style style = this.renderer;
         this.enabled6 = true;
         this.timestamp++;
         CapeRepository.Selection selection = currentLoaded.source();
         CapeRepository.Selection currentSelection = new CapeRepository.Selection(
            selection.kind(), selection.reference(), selection.title(), selection.page(), selection.credit(), style
         );
         this.createValue(() -> {
            this.capeRepository.save(currentSelection, currentLoaded.bytes());
            return new CapeRevisionService.Loaded(currentSelection, currentLoaded.animation(), currentLoaded.bytes());
         }, item -> {
            this.enabled6 = false;
            this.loaded2 = item;
            CapeEnabledService.apply(item.animation(), style);
            runnable.run();
            this.status("Wearing " + item.source().title() + " · saved", false);
         }, item -> {
            this.enabled6 = false;
            this.status(item, true);
         });
      }
   }

   public void original() {
      if (!this.enabled6) {
         this.count++;
         updateState4(this.future);
         this.enabled3 = false;
         this.enabled6 = true;
         this.timestamp++;
         CapeRepository.Selection selection = new CapeRepository.Selection(
            "vanilla", "", "Minecraft original", "", "", CapePreviewRenderer.Style.defaults()
         );
         this.createValue(() -> {
            this.capeRepository.save(selection, null);
            return selection;
         }, item -> {
            this.enabled6 = false;
            this.loaded2 = new CapeRevisionService.Loaded(item, null, null);
            CapeEnabledService.apply(null, item.style());
            this.status("Original Minecraft cape restored.", false);
         }, item -> {
            this.enabled6 = false;
            this.status(item, true);
         });
      }
   }

   public void saveKey(String text) {
      this.createValue(() -> {
         this.capeRepository.key(text);
         return text.strip();
      }, item -> {
         this.text5 = item;
         this.status(item.isBlank() ? "GIPHY disconnected." : "GIPHY key saved on this device.", false);
      }, item -> this.status(item, true));
   }

   public void search(CapeSearchSelector.Provider value, String currentText, int currentValue) {
      int nextValue = ++this.count2;
      updateState4(this.future2);
      this.executor2.getQueue().clear();
      this.enabled4 = true;
      this.items = List.of();
      this.count4 = -1;
      this.text.clear();
      this.text3.clear();
      this.timestamp++;
      this.future2 = this.createValue(
         () -> CapeSearchSelector.search(value, currentText, currentValue, this.text5),
         item -> {
            if (nextValue == this.count2) {
               this.enabled4 = false;
               this.items = item.entries();
               this.count4 = item.nextOffset();
               this.status(
                  this.items.isEmpty() ? "No GIFs found. Try another search." : "Select a GIF to preview it on your cape.", false
               );

               for (CapeSearchSelector.Entry entry : this.items) {
                  this.updateState3(entry, nextValue);
               }
            }
         },
         item -> {
            if (nextValue == this.count2) {
               this.enabled4 = false;
               this.status(item, true);
            }
         }
      );
   }

   private void updateState3(CapeSearchSelector.Entry entry, int value) {
      if (this.text3.add(entry.id())) {
         this.createValue2(this.executor2, () -> {
            byte[] bytes = CapeHttpsService.get(CapeHttpsService.https(entry.thumbnail()), 2097152);

            try (MemoryCacheImageInputStream memoryCacheImageInputStream = new MemoryCacheImageInputStream(new ByteArrayInputStream(bytes))) {
               Iterator iterator = ImageIO.getImageReaders(memoryCacheImageInputStream);
               if (!iterator.hasNext()) {
                  return null;
               }

               ImageReader imageReader = (ImageReader)iterator.next();

               try {
                  imageReader.setInput(memoryCacheImageInputStream);
                  int width = imageReader.getWidth(0);
                  int height = imageReader.getHeight(0);
                  if ((long)width * height > 4194304L) {
                     return null;
                  }

                  BufferedImage bufferedImage = imageReader.read(0);
                  double doubleValue = Math.min(1.0, 160.0 / Math.max(width, height));
                  return CapeCodec.copy(bufferedImage, Math.max(1, (int)(width * doubleValue)), Math.max(1, (int)(height * doubleValue)));
               } finally {
                  imageReader.dispose();
               }
            }
         }, item -> {
            if (value == this.count2 && item != null) {
               this.text.put(entry.id(), item);
               this.timestamp++;
            }
         }, item -> {});
      }
   }

   public void skin(UUID uUID) {
      int currentValue = ++this.count3;
      this.bufferedImage = CapeRecoveredFactory.mannequin();
      this.enabled2 = false;
      this.timestamp++;
      if (uUID != null) {
         this.createValue(
            () -> {
               JsonObject jsonObject = JsonParser.parseString(
                     new String(
                        CapeHttpsService.get(
                           URI.create("https://sessionserver.mojang.com/session/minecraft/profile/" + uUID.toString().replace("-", "")),
                           131072
                        ),
                        StandardCharsets.UTF_8
                     )
                  )
                  .getAsJsonObject();

               for (JsonElement jsonElement : jsonObject.getAsJsonArray("properties")) {
                  if (jsonElement.getAsJsonObject().get("name").getAsString().equals("textures")) {
                     JsonObject currentJsonObject = JsonParser.parseString(
                           new String(Base64.getDecoder().decode(jsonElement.getAsJsonObject().get("value").getAsString()), StandardCharsets.UTF_8)
                        )
                        .getAsJsonObject();
                     JsonObject nextJsonObject = currentJsonObject.getAsJsonObject("textures").getAsJsonObject("SKIN");
                     String text = nextJsonObject.get("url").getAsString().replaceFirst("^http:", "https:");
                     BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(CapeHttpsService.get(CapeHttpsService.https(text), 1048576)));
                     if (bufferedImage != null && bufferedImage.getWidth() == 64 && (bufferedImage.getHeight() == 64 || bufferedImage.getHeight() == 32)) {
                        return new CapeRevisionService.Skin(
                           bufferedImage, nextJsonObject.has("metadata") && "slim".equals(CapeSearchSelector.str(nextJsonObject.getAsJsonObject("metadata"), "model"))
                        );
                     }
                  }
               }

               return null;
            },
            item -> {
               if (currentValue == this.count3 && item != null) {
                  this.bufferedImage = item.image();
                  this.enabled2 = item.slim();
                  this.timestamp++;
               }
            },
            item -> {}
         );
      }
   }

   public void status(String text, boolean enabled) {
      this.text4 = text;
      this.enabled5 = enabled;
      this.timestamp++;
   }

   private <T> Future<?> createValue(Callable<T> callable, Consumer<T> consumer, Consumer<String> currentConsumer) {
      return this.createValue2(this.executor, callable, consumer, currentConsumer);
   }

   private <T> Future<?> createValue2(ExecutorService executor, Callable<T> callable, Consumer<T> currentConsumer, Consumer<String> nextConsumer) {
      return this.enabled ? CompletableFuture.completedFuture(null) : executor.submit(() -> {
         try {
             T value = callable.call();
            this.consumer.accept(() -> {
               if (!this.enabled) {
                  currentConsumer.accept(value);
               }
            });
         } catch (Exception exception) {
            String text = exception.getMessage();
            this.consumer.accept(() -> {
               if (!this.enabled) {
                  nextConsumer.accept(text == null ? "Could not load this cape." : text);
               }
            });
         }
      });
   }

   private static void updateState4(Future<?> future) {
      if (future != null) {
         future.cancel(true);
      }
   }

   @Override
   public void close() {
      this.enabled = true;
      this.count++;
      this.count2++;
      this.count3++;
      this.executor.shutdownNow();
      this.executor2.shutdownNow();
   }

   public record Loaded(CapeRepository.Selection source, CapeSizeService animation, byte[] bytes) {
   }

   private record Skin(BufferedImage image, boolean slim) {
   }
}

