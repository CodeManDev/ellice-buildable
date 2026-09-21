package dev.felix.ellice.ui.material;

import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.color.ColorFeatureType;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.Consumer;

public final class MaterialTabsService {
  private MaterialTabsService() {}

  public static ComponentKeyService<?> tabs(
      String string, ColorFeatureType colorFeatureType, Consumer<ColorFeatureType> consumer) {
    ComponentKeyService[] componentKeyServiceArray =
        (ComponentKeyService[])
            Arrays.stream(ColorFeatureType.values())
                .map(
                    colorFeatureType2 -> {
                      boolean bl = colorFeatureType2 == colorFeatureType;
                      return MaterialTextService.button(
                              string + "." + colorFeatureType2.name().toLowerCase(Locale.ROOT),
                              colorFeatureType2.label(),
                              () ->
                                  consumer.accept((ColorFeatureType) ((Object) colorFeatureType2)),
                              bl
                                  ? MaterialIsLightService.PRIMARY
                                  : MaterialIsLightService.SECONDARY_CONTAINER,
                              bl
                                  ? MaterialIsLightService.ON_PRIMARY
                                  : MaterialIsLightService.ON_SECONDARY_CONTAINER,
                              Float.intBitsToFloat(1101004800))
                          .props(
                              materialJoinedService ->
                                  ((SceneCornerRadiusService)
                                          ((SceneCornerRadiusService)
                                                  ((SceneCornerRadiusService)
                                                          materialJoinedService
                                                              .joined(
                                                                  colorFeatureType2
                                                                      == ColorFeatureType.GRADIENT,
                                                                  colorFeatureType2
                                                                      == ColorFeatureType.TRIANGLE,
                                                                  bl)
                                                              .flex(1.0f))
                                                      .minWidth(0.0f))
                                              .padding(0.0f, Float.intBitsToFloat(0x41400000)))
                                      .tooltip(
                                          colorFeatureType2 == ColorFeatureType.GRADIENT
                                              ? "Saturation and brightness with a hue slider"
                                              : "Rotate the hue ring; pick saturation and"
                                                    + " brightness inside the triangle"));
                    })
                .toArray(ComponentKeyService[]::new);
    return ComponentBoxService.row(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                ((LayoutContainerNode) layoutContainerNode.id(string))
                                    .width(
                                        LayoutOperationHandler.percent(
                                            Float.intBitsToFloat(1120403456))))
                            .gap(Float.intBitsToFloat(0x40400000)))
                    .flexShrink(0.0f),
            componentKeyServiceArray)
        .key(string);
  }
}
