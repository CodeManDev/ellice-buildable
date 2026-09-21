package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.feature.notebot.NotebotDecoderTracker;
import dev.felix.ellice.feature.notebot.NotebotRepository;
import dev.felix.ellice.feature.notebot.NotebotTitleService;
import dev.felix.ellice.render.compositor.CompositorAmplitudeService;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.text.TextMode;

final class TimelineScrubber extends ScenePctService<TimelineScrubber> {
  private final NotebotDecoderTracker notebotDecoderTracker;
  private NotebotTitleService notebotTitleService;
  private CompositorAmplitudeService compositorAmplitudeService = CompositorAmplitudeService.EMPTY;
  private NotebotTitleService notebotTitleService2;
  private boolean enabled2;
  private boolean enabled3;
  private float value = -1.0F;

  TimelineScrubber(NotebotDecoderTracker currentNotebotDecoderTracker) {
    this.notebotDecoderTracker = currentNotebotDecoderTracker;
    this.interactive = true;
    this.stopPropagation(true);
    this.tooltip("Click or drag to seek in the track");
  }

  TimelineScrubber enabled(boolean currentEnabled) {
    this.pointerEvents(currentEnabled);
    this.cursorStyle(
        currentEnabled ? ScenePctService.CursorStyle.POINTER : ScenePctService.CursorStyle.DEFAULT);
    return this;
  }

  @Override
  protected void onPress(float currentValue, float nextValue) {
    if (!this.notebotDecoderTracker.importing()
        && this.notebotDecoderTracker.playback().song() != null) {
      this.notebotTitleService2 = this.notebotDecoderTracker.playback().song();
      this.enabled2 = true;
      this.value = this.calculateValue(currentValue);
      this.enabled3 = this.notebotDecoderTracker.performing();
      if (this.enabled3) {
        this.notebotDecoderTracker.playPause();
      }
    }
  }

  @Override
  protected void updateWhilePressed(float currentValue, float nextValue) {
    if (this.enabled2) {
      this.value = this.calculateValue(currentValue);
    }
  }

  @Override
  protected void onRelease(boolean enabled) {
    this.updateState(true);
  }

  @Override
  protected void onDetached() {
    this.updateState(false);
  }

  private void updateState(boolean enabled) {
    if (this.enabled2) {
      this.enabled2 = false;
      if (this.notebotTitleService2 == this.notebotDecoderTracker.playback().song()
          && !this.notebotDecoderTracker.importing()) {
        if (enabled) {
          this.notebotDecoderTracker.seek(this.value);
        }

        if (this.enabled3
            && this.notebotDecoderTracker.playback().phase() == NotebotRepository.Phase.PAUSED) {
          this.notebotDecoderTracker.playPause();
        }
      }

      this.notebotTitleService2 = null;
      this.value = -1.0F;
    }
  }

  private float calculateValue(float value) {
    return Math.max(0.0F, Math.min(1.0F, (value - this.cx) / Math.max(this.cw, 1.0F)));
  }

  @Override
  protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
    NotebotTitleService notebotTitle = this.notebotDecoderTracker.playback().song();
    if (notebotTitle != this.notebotTitleService) {
      this.notebotTitleService = notebotTitle;
      float[] floats = new float[notebotTitle == null ? 0 : notebotTitle.waveformSize()];

      for (int index = 0; index < floats.length; index++) {
        floats[index] = notebotTitle.amplitude(index);
      }

      this.compositorAmplitudeService = new CompositorAmplitudeService(floats);
    }

    float currentValue =
        notebotTitle == null
            ? 0.0F
            : (float)
                (this.notebotDecoderTracker.playback().displayPosition()
                    / notebotTitle.durationTicks());
    compositorPushPresentationScale.waveform(
        this.cx,
        this.cy,
        this.cw,
        this.ch,
        this.compositorAmplitudeService,
        currentValue,
        this.value,
        this.notebotDecoderTracker.playback().phase() == NotebotRepository.Phase.PLAYING,
        notebotTitle != null,
        this.effectiveOpacity * (this.notebotDecoderTracker.importing() ? 0.4F : 1.0F),
        MaterialIsLightService.PRIMARY,
        MaterialIsLightService.OUTLINE,
        MaterialIsLightService.ON_SURFACE);
    if (this.enabled2 && notebotTitle != null) {
      String currentText = NotebotTitleService.time(this.value * notebotTitle.seconds());
      float nextValue = 12.0F * this.presentationScale;
      float previousValue =
          compositorPushPresentationScale.textWidth(
              currentText, nextValue, TextMode.REGULAR, "material-roboto");
      float sourceValue =
          Math.max(
              this.cx,
              Math.min(
                  this.cx + this.cw - previousValue,
                  this.cx + this.value * this.cw - previousValue / 2.0F));
      compositorPushPresentationScale.text(
          sourceValue,
          this.cy + this.ch - nextValue,
          currentText,
          nextValue,
          mulAlpha(MaterialIsLightService.ON_SURFACE, this.effectiveOpacity),
          MaterialIsLightService.BODY);
    }
  }
}
