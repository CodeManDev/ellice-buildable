package dev.felix.ellice.feature.studio;

import dev.felix.ellice.config.LocalConfigRepository;
import dev.felix.ellice.hud.layout.LayoutOperationHandler;
import dev.felix.ellice.module.Module;
import dev.felix.ellice.module.ModuleRegisterService;
import dev.felix.ellice.module.impl.ImplOpenPanelService;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class StudioRenderer {
   private final StudioRepository studioRepository;
   private final ModuleRegisterService moduleRegisterService;
   private final Map<String, StudioRepository.Document> text = new LinkedHashMap<>();
   private final Map<String, StudioOpenPanelService> text2 = new LinkedHashMap<>();
   private Consumer<String> text3 = item -> {};
   private final long timestamp = System.nanoTime();
   private final StudioCanvasRenderer renderer = new StudioCanvasRenderer();

   public StudioRenderer(Path path, ModuleRegisterService moduleRegister) {
      this.studioRepository = new StudioRepository(path);
      this.moduleRegisterService = moduleRegister;
   }

   public void opener(Consumer<String> consumer) {
      this.text3 = consumer;
   }

   public void load() {
      for (StudioRepository.Document document : this.studioRepository.load()) {
         this.text.put(document.draft().id, document);
         if (document.published() != null && this.moduleRegisterService.get(document.published().name).isEmpty()) {
            this.updateState(document.published(), false);
         }
      }
   }

   public List<StudioShapeService> projects() {
      return this.text.values().stream().map(item -> item.draft().copy()).toList();
   }

   public List<String> warnings() {
      return this.studioRepository.warnings();
   }

   public StudioShapeService project(String currentText) {
      StudioRepository.Document document = this.text.get(currentText);
      return document == null ? null : document.draft().copy();
   }

   public boolean published(String text) {
      return this.text2.containsKey(text);
   }

   public StudioShapeService firstOrExample() {
      return this.text.isEmpty() ? StudioOrbitService.orbit() : this.text.values().iterator().next().draft().copy();
   }

   public void save(StudioShapeService studioShape) throws IOException {
      StudioRepository.Document document = this.text.get(studioShape.id);
      StudioRepository.Document currentDocument = new StudioRepository.Document(studioShape.copy(), document == null ? null : document.published());
      this.studioRepository.save(currentDocument);
      this.text.put(studioShape.id, currentDocument);
   }

   public void publish(StudioShapeService studioShape) throws IOException {
      List items = StudioValidateValidator.validate(studioShape, true);
      if (!items.isEmpty()) {
         throw new IOException((String)items.getFirst());
      }

      StudioOpenPanelService studioOpenPanel = this.text2.get(studioShape.id);
      Module currentModule = this.moduleRegisterService.get(studioShape.name).orElse(null);
      if (currentModule != null && currentModule != studioOpenPanel) {
         throw new IOException("A module named “" + studioShape.name + "” already exists.");
      }

      for (StudioShapeService.Block block : studioShape.blocks) {
         if (block.kind == StudioMode.MODULE_GATE) {
            Module nextModule = this.moduleRegisterService.get(block.target).orElse(null);
            if (nextModule == null || nextModule instanceof StudioOpenPanelService || nextModule instanceof ImplOpenPanelService) {
               throw new IOException("Choose an existing built-in module for the switch action.");
            }
         }
      }

      StudioRepository.Document document = new StudioRepository.Document(studioShape.copy(), studioShape.copy());
      this.studioRepository.save(document);
      this.text.put(studioShape.id, document);
      this.updateState(studioShape, true);
   }

   private void updateState(StudioShapeService studioShape, boolean enabled) {
      StudioOpenPanelService studioOpenPanel = this.text2.get(studioShape.id);
      boolean currentEnabled = studioOpenPanel == null ? enabled : studioOpenPanel.isEnabled();
      StudioOpenPanelService currentStudioOpenPanel = new StudioOpenPanelService(studioShape, this.moduleRegisterService, () -> this.text3.accept(studioShape.id));
      LocalConfigRepository.runWithoutSaving(() -> {
         if (studioOpenPanel != null) {
            currentStudioOpenPanel.copySettings(studioOpenPanel);
            this.moduleRegisterService.unregister(studioOpenPanel);
         }

         this.moduleRegisterService.register(currentStudioOpenPanel);
         this.text2.put(studioShape.id, currentStudioOpenPanel);
         if (currentEnabled) {
            currentStudioOpenPanel.enable();
         }
      });
   }

   public void unpublish(String currentText) throws IOException {
      StudioRepository.Document document = this.text.get(currentText);
      if (document != null) {
         StudioRepository.Document currentDocument = new StudioRepository.Document(document.draft().copy(), null);
         this.studioRepository.save(currentDocument);
         StudioOpenPanelService studioOpenPanel = this.text2.remove(currentText);
         if (studioOpenPanel != null) {
            LocalConfigRepository.runWithoutSaving(() -> this.moduleRegisterService.unregister(studioOpenPanel));
         }

         this.text.put(currentText, currentDocument);
      }
   }

   public Path exportProject(StudioShapeService studioShape) throws IOException {
      return this.studioRepository.exportProject(studioShape);
   }

   public StudioShapeService importProject(Path path) throws IOException {
      StudioShapeService studioShape = this.studioRepository.importProject(path);
      this.save(studioShape);
      return studioShape;
   }

   public List<String> moduleNames() {
      return this.moduleRegisterService
         .stream()
         .filter(item -> !(item instanceof StudioOpenPanelService) && !(item instanceof ImplOpenPanelService))
         .map(item -> item.name())
         .sorted()
         .toList();
   }

   public float seconds() {
      return (float)(System.nanoTime() - this.timestamp) / 1.0E9F;
   }

   public void releaseActions() {
      for (StudioOpenPanelService studioOpenPanel : this.text2.values()) {
         studioOpenPanel.releaseActions();
      }
   }

   public void tick(LayoutOperationHandler layoutOperation) {
      for (StudioOpenPanelService studioOpenPanel : List.copyOf(this.text2.values())) {
         if (studioOpenPanel.isEnabled()) {
            studioOpenPanel.gates(StudioValidateValidator.evaluate(studioOpenPanel.project, layoutOperation, this.seconds()).moduleGates());
         }
      }
   }

   public void render(CompositorPushPresentationScaleService compositorPushPresentationScale, LayoutOperationHandler layoutOperation, float width, float height) {
      this.render(compositorPushPresentationScale, layoutOperation, width, height, 1.0F);
   }

   public void render(CompositorPushPresentationScaleService compositorPushPresentationScale, LayoutOperationHandler layoutOperation, float currentWidth, float currentHeight, float value) {
      for (StudioOpenPanelService studioOpenPanel : List.copyOf(this.text2.values())) {
         if (studioOpenPanel.isEnabled()) {
            float currentValue = (Float)studioOpenPanel.scale.get();
            float nextValue = (Float)studioOpenPanel.x.get();
            float previousValue = (Float)studioOpenPanel.y.get();
            switch ((String)studioOpenPanel.anchor.get()) {
               case "Top right":
                  nextValue = currentWidth - studioOpenPanel.project.width * currentValue - nextValue;
                  break;
               case "Bottom left":
                  previousValue = currentHeight - studioOpenPanel.project.height * currentValue - previousValue;
                  break;
               case "Bottom right":
                  nextValue = currentWidth - studioOpenPanel.project.width * currentValue - nextValue;
                  previousValue = currentHeight - studioOpenPanel.project.height * currentValue - previousValue;
                  break;
               case "Center":
                  nextValue += (currentWidth - studioOpenPanel.project.width * currentValue) / 2.0F;
                  previousValue += (currentHeight - studioOpenPanel.project.height * currentValue) / 2.0F;
            }

            this.renderer
               .draw(
                  compositorPushPresentationScale,
                  studioOpenPanel.project,
                  StudioValidateValidator.evaluate(studioOpenPanel.project, layoutOperation, this.seconds()),
                  nextValue * value,
                  previousValue * value,
                  currentValue * value,
                  (Float)studioOpenPanel.opacity.get(),
                  this.seconds()
               );
         }
      }
   }
}
