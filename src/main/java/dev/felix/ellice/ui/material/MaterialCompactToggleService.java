package dev.felix.ellice.ui.material;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;

public final class MaterialCompactToggleService {
  private MaterialCompactToggleService() {}

  private static int calculateValue(int value, float currentValue) {
    return Math.round((value >>> 24) * currentValue) << 24 | value & 16777215;
  }

  private static void updateState(
      CompositorPushPresentationScaleService compositorPushPresentationScale,
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      float targetValue,
      float inputValue,
      float outputValue,
      int resultValue,
      float candidateValue) {
    if (!(nextValue <= 0.0F) && !(previousValue <= 0.0F)) {
      compositorPushPresentationScale.roundedRect(
          value,
          currentValue,
          nextValue,
          previousValue,
          sourceValue,
          targetValue,
          inputValue,
          outputValue,
          calculateValue(resultValue, candidateValue),
          0.0F,
          0.0F,
          0,
          0.0F,
          0,
          1.0F,
          0.0F);
    }
  }

  public static void compactToggle(
      CompositorPushPresentationScaleService compositorPushPresentationScale,
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      float targetValue,
      boolean enabled,
      boolean currentEnabled,
      boolean nextEnabled,
      float inputValue) {
    float outputValue = Math.max(0.0F, Math.min(1.0F, sourceValue));
    float resultValue = value + (nextValue - 36.0F) / 2.0F;
    float candidateValue = currentValue + (previousValue - 22.0F) / 2.0F;
    int selectedValue =
        currentEnabled
            ? MaterialIsLightService.layer(
                MaterialIsLightService.SURFACE_HIGHEST, MaterialIsLightService.PRIMARY, outputValue)
            : MaterialIsLightService.layer(
                MaterialIsLightService.SURFACE_CONTAINER, MaterialIsLightService.ON_SURFACE, 0.12F);
    compositorPushPresentationScale.roundedRect(
        resultValue + 0.75F,
        candidateValue + 0.75F,
        34.5F,
        20.5F,
        10.25F,
        10.25F,
        10.25F,
        10.25F,
        calculateValue(selectedValue, inputValue),
        0.0F,
        0.0F,
        0,
        1.5F * (1.0F - outputValue),
        calculateValue(MaterialIsLightService.OUTLINE, inputValue),
        1.0F,
        0.0F);
    float defaultValue =
        12.0F
            + 4.0F * outputValue
            + (6.0F - 4.0F * outputValue) * Math.max(-0.2F, Math.min(1.0F, targetValue));
    float initialValue = resultValue + 11.0F + 14.0F * outputValue;
    int resolvedValue =
        currentEnabled
            ? MaterialIsLightService.layer(
                MaterialIsLightService.OUTLINE, MaterialIsLightService.ON_PRIMARY, outputValue)
            : MaterialIsLightService.layer(
                MaterialIsLightService.SURFACE_CONTAINER, MaterialIsLightService.ON_SURFACE, 0.38F);
    updateState(
        compositorPushPresentationScale,
        initialValue - defaultValue / 2.0F,
        candidateValue + 11.0F - defaultValue / 2.0F,
        defaultValue,
        defaultValue,
        defaultValue / 2.0F,
        defaultValue / 2.0F,
        defaultValue / 2.0F,
        defaultValue / 2.0F,
        resolvedValue,
        inputValue);
    if (nextEnabled) {
      compositorPushPresentationScale.roundedRect(
          resultValue - 3.0F,
          candidateValue - 3.0F,
          42.0F,
          28.0F,
          14.0F,
          14.0F,
          14.0F,
          14.0F,
          0,
          0.0F,
          0.0F,
          0,
          1.5F,
          calculateValue(MaterialIsLightService.PRIMARY, inputValue),
          1.0F,
          0.0F);
    }
  }

  public static void compactSlider(
      CompositorPushPresentationScaleService compositorPushPresentationScale,
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      float targetValue,
      boolean enabled,
      boolean currentEnabled,
      float inputValue,
      boolean nextEnabled,
      boolean previousEnabled,
      float outputValue) {
    float resultValue = value + 2.0F;
    float candidateValue = value + nextValue - 2.0F;
    float selectedValue =
        resultValue + (candidateValue - resultValue) * Math.max(0.0F, Math.min(1.0F, sourceValue));
    float defaultValue =
        resultValue + (candidateValue - resultValue) * Math.max(0.0F, Math.min(1.0F, targetValue));
    float initialValue = currentValue + (previousValue - 6.0F) / 2.0F;
    int resolvedValue =
        nextEnabled
            ? MaterialIsLightService.PRIMARY
            : MaterialIsLightService.layer(
                MaterialIsLightService.SURFACE_CONTAINER, MaterialIsLightService.ON_SURFACE, 0.38F);
    int computedValue =
        nextEnabled
            ? MaterialIsLightService.SECONDARY_CONTAINER
            : MaterialIsLightService.layer(
                MaterialIsLightService.SURFACE_CONTAINER, MaterialIsLightService.ON_SURFACE, 0.12F);
    float cachedValue = 3.0F - Math.max(-0.2F, Math.min(1.0F, inputValue));
    float pendingValue = enabled && !currentEnabled ? 3.0F : cachedValue;
    float activeValue = currentEnabled ? 3.0F : cachedValue;
    float fallbackValue = 3.0F + pendingValue / 2.0F;
    float primaryValue = 3.0F + activeValue / 2.0F;
    if (enabled) {
      updateState(
          compositorPushPresentationScale,
          resultValue,
          initialValue,
          selectedValue - resultValue - primaryValue,
          6.0F,
          3.0F,
          1.0F,
          1.0F,
          3.0F,
          computedValue,
          outputValue);
      updateState(
          compositorPushPresentationScale,
          selectedValue + primaryValue,
          initialValue,
          defaultValue - selectedValue - primaryValue - fallbackValue,
          6.0F,
          1.0F,
          1.0F,
          1.0F,
          1.0F,
          resolvedValue,
          outputValue);
    } else {
      updateState(
          compositorPushPresentationScale,
          resultValue,
          initialValue,
          defaultValue - resultValue - fallbackValue,
          6.0F,
          3.0F,
          1.0F,
          1.0F,
          3.0F,
          resolvedValue,
          outputValue);
    }

    updateState(
        compositorPushPresentationScale,
        defaultValue + fallbackValue,
        initialValue,
        candidateValue - defaultValue - fallbackValue,
        6.0F,
        1.0F,
        3.0F,
        3.0F,
        1.0F,
        computedValue,
        outputValue);
    updateState(
        compositorPushPresentationScale,
        defaultValue - pendingValue / 2.0F,
        currentValue + (previousValue - 22.0F) / 2.0F,
        pendingValue,
        22.0F,
        1.5F,
        1.5F,
        1.5F,
        1.5F,
        resolvedValue,
        outputValue);
    if (enabled) {
      updateState(
          compositorPushPresentationScale,
          selectedValue - activeValue / 2.0F,
          currentValue + (previousValue - 22.0F) / 2.0F,
          activeValue,
          22.0F,
          1.5F,
          1.5F,
          1.5F,
          1.5F,
          resolvedValue,
          outputValue);
    }

    if (previousEnabled) {
      compositorPushPresentationScale.roundedRect(
          value - 2.0F,
          currentValue + 1.0F,
          nextValue + 4.0F,
          previousValue - 2.0F,
          8.0F,
          8.0F,
          8.0F,
          8.0F,
          0,
          0.0F,
          0.0F,
          0,
          1.5F,
          calculateValue(MaterialIsLightService.PRIMARY, outputValue),
          1.0F,
          0.0F);
    }
  }

  public static void toggle(
      CompositorPushPresentationScaleService compositorPushPresentationScale,
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      float targetValue,
      boolean enabled,
      boolean currentEnabled,
      boolean nextEnabled,
      float inputValue) {
    float outputValue = Math.max(0.0F, Math.min(1.0F, sourceValue));
    float resultValue = value + (nextValue - 52.0F) / 2.0F;
    float candidateValue = currentValue + (previousValue - 32.0F) / 2.0F;
    int selectedValue =
        MaterialIsLightService.layer(
            MaterialIsLightService.SURFACE_HIGHEST, MaterialIsLightService.PRIMARY, outputValue);
    if (!currentEnabled) {
      selectedValue =
          MaterialIsLightService.layer(
              MaterialIsLightService.SURFACE_CONTAINER, MaterialIsLightService.ON_SURFACE, 0.12F);
    }

    float defaultValue = 1.0F - outputValue;
    float initialValue = 16.0F - defaultValue;
    compositorPushPresentationScale.roundedRect(
        resultValue + defaultValue,
        candidateValue + defaultValue,
        52.0F - 2.0F * defaultValue,
        32.0F - 2.0F * defaultValue,
        initialValue,
        initialValue,
        initialValue,
        initialValue,
        calculateValue(selectedValue, inputValue),
        0.0F,
        0.0F,
        0,
        2.0F * (1.0F - outputValue),
        calculateValue(
            currentEnabled
                ? MaterialIsLightService.OUTLINE
                : MaterialIsLightService.layer(
                    MaterialIsLightService.SURFACE_CONTAINER,
                    MaterialIsLightService.ON_SURFACE,
                    0.12F),
            inputValue),
        1.0F,
        0.0F);
    float resolvedValue = resultValue + 16.0F + 20.0F * outputValue;
    if (currentEnabled && (enabled || nextEnabled || targetValue > 0.01F)) {
      updateState(
          compositorPushPresentationScale,
          resolvedValue - 20.0F,
          candidateValue - 4.0F,
          40.0F,
          40.0F,
          20.0F,
          20.0F,
          20.0F,
          20.0F,
          calculateValue(
              outputValue > 0.5
                  ? MaterialIsLightService.PRIMARY
                  : MaterialIsLightService.ON_SURFACE,
              !(targetValue > 0.01F) && !nextEnabled ? 0.08F : 0.1F),
          inputValue);
    }

    float computedValue = Math.max(-0.2F, Math.min(1.0F, targetValue));
    float cachedValue =
        (16.0F + 8.0F * outputValue) * (1.0F - computedValue) + 28.0F * computedValue;
    int pendingValue =
        MaterialIsLightService.layer(
            MaterialIsLightService.OUTLINE, MaterialIsLightService.ON_PRIMARY, outputValue);
    if (currentEnabled && (enabled || targetValue > 0.01F)) {
      pendingValue =
          MaterialIsLightService.layer(
              MaterialIsLightService.ON_SURFACE_VARIANT,
              MaterialIsLightService.PRIMARY_CONTAINER,
              outputValue);
    }

    if (!currentEnabled) {
      pendingValue =
          outputValue > 0.5
              ? MaterialIsLightService.SURFACE
              : calculateValue(MaterialIsLightService.ON_SURFACE, 0.38F);
    }

    updateState(
        compositorPushPresentationScale,
        resolvedValue - cachedValue / 2.0F,
        candidateValue + 16.0F - cachedValue / 2.0F,
        cachedValue,
        cachedValue,
        cachedValue / 2.0F,
        cachedValue / 2.0F,
        cachedValue / 2.0F,
        cachedValue / 2.0F,
        pendingValue,
        inputValue);
    if (nextEnabled) {
      compositorPushPresentationScale.roundedRect(
          resultValue - 4.0F,
          candidateValue - 4.0F,
          60.0F,
          40.0F,
          20.0F,
          20.0F,
          20.0F,
          20.0F,
          0,
          0.0F,
          0.0F,
          0,
          2.0F,
          calculateValue(MaterialIsLightService.PRIMARY, inputValue),
          1.0F,
          0.0F);
    }
  }

  public static void slider(
      CompositorPushPresentationScaleService compositorPushPresentationScale,
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      float targetValue,
      boolean enabled,
      boolean currentEnabled,
      float inputValue,
      boolean nextEnabled,
      boolean previousEnabled,
      float outputValue) {
    float resultValue = value + 2.0F;
    float candidateValue = value + nextValue - 2.0F;
    float selectedValue =
        resultValue + (candidateValue - resultValue) * Math.max(0.0F, Math.min(1.0F, sourceValue));
    float defaultValue =
        resultValue + (candidateValue - resultValue) * Math.max(0.0F, Math.min(1.0F, targetValue));
    float initialValue = currentValue + (previousValue - 16.0F) / 2.0F;
    int resolvedValue =
        nextEnabled
            ? MaterialIsLightService.PRIMARY
            : calculateValue(MaterialIsLightService.ON_SURFACE, 0.38F);
    int computedValue =
        nextEnabled
            ? MaterialIsLightService.SECONDARY_CONTAINER
            : calculateValue(MaterialIsLightService.ON_SURFACE, 0.12F);
    float cachedValue = 4.0F - 2.0F * Math.max(-0.2F, Math.min(1.0F, inputValue));
    float pendingValue = enabled && !currentEnabled ? 4.0F : cachedValue;
    float activeValue = currentEnabled ? 4.0F : cachedValue;
    float fallbackValue = 6.0F + pendingValue / 2.0F;
    float primaryValue = 6.0F + activeValue / 2.0F;
    if (enabled) {
      if (selectedValue - primaryValue > resultValue + 8.0F) {
        updateState(
            compositorPushPresentationScale,
            resultValue,
            initialValue,
            selectedValue - primaryValue - resultValue,
            16.0F,
            8.0F,
            2.0F,
            2.0F,
            8.0F,
            computedValue,
            outputValue);
      }

      updateState(
          compositorPushPresentationScale,
          selectedValue + primaryValue,
          initialValue,
          defaultValue - selectedValue - primaryValue - fallbackValue,
          16.0F,
          2.0F,
          2.0F,
          2.0F,
          2.0F,
          resolvedValue,
          outputValue);
    } else if (defaultValue - fallbackValue > resultValue + 8.0F) {
      updateState(
          compositorPushPresentationScale,
          resultValue,
          initialValue,
          defaultValue - fallbackValue - resultValue,
          16.0F,
          8.0F,
          2.0F,
          2.0F,
          8.0F,
          resolvedValue,
          outputValue);
    }

    if (defaultValue + fallbackValue < candidateValue - 8.0F) {
      updateState(
          compositorPushPresentationScale,
          defaultValue + fallbackValue,
          initialValue,
          candidateValue - defaultValue - fallbackValue,
          16.0F,
          2.0F,
          8.0F,
          8.0F,
          2.0F,
          computedValue,
          outputValue);
      updateState(
          compositorPushPresentationScale,
          candidateValue - 10.0F,
          initialValue + 6.0F,
          4.0F,
          4.0F,
          2.0F,
          2.0F,
          2.0F,
          2.0F,
          resolvedValue,
          outputValue);
    }

    if (enabled && selectedValue - primaryValue > resultValue + 8.0F) {
      updateState(
          compositorPushPresentationScale,
          resultValue + 6.0F,
          initialValue + 6.0F,
          4.0F,
          4.0F,
          2.0F,
          2.0F,
          2.0F,
          2.0F,
          resolvedValue,
          outputValue);
    }

    float secondaryValue = currentValue + (previousValue - 44.0F) / 2.0F;
    updateState(
        compositorPushPresentationScale,
        defaultValue - pendingValue / 2.0F,
        secondaryValue,
        pendingValue,
        44.0F,
        2.0F,
        2.0F,
        2.0F,
        2.0F,
        resolvedValue,
        outputValue);
    if (enabled) {
      updateState(
          compositorPushPresentationScale,
          selectedValue - activeValue / 2.0F,
          secondaryValue,
          activeValue,
          44.0F,
          2.0F,
          2.0F,
          2.0F,
          2.0F,
          resolvedValue,
          outputValue);
    }

    if (previousEnabled) {
      compositorPushPresentationScale.roundedRect(
          value - 4.0F,
          currentValue - 2.0F,
          nextValue + 8.0F,
          previousValue + 4.0F,
          12.0F,
          12.0F,
          12.0F,
          12.0F,
          0,
          0.0F,
          0.0F,
          0,
          2.0F,
          calculateValue(MaterialIsLightService.PRIMARY, outputValue),
          1.0F,
          0.0F);
    }
  }
}
