package dev.felix.ellice.feature.cape;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public final class CapeRecoveredFactory {
  public static final List<String> NAMES =
      List.of("Aurora", "Ember", "Ender", "Ocean", "ellice", "Starlight");

  private CapeRecoveredFactory() {}

  public static CapeSizeService create(String text) {
    int index = Math.max(0, NAMES.indexOf(text));
    ArrayList arrayList = new ArrayList();
    ArrayList currentArrayList = new ArrayList();

    for (int currentIndex = 0; currentIndex < 40; currentIndex++) {
      BufferedImage bufferedImage = new BufferedImage(80, 128, 2);
      double doubleValue = currentIndex / 40.0 * 3.141592653589793 * 2.0;

      for (int nextIndex = 0; nextIndex < 128; nextIndex++) {
        for (int previousIndex = 0; previousIndex < 80; previousIndex++) {
          double currentDoubleValue = previousIndex / 79.0;
          double nextDoubleValue = nextIndex / 127.0;
          double previousDoubleValue =
              0.5 + 0.5 * Math.sin(currentDoubleValue * 5.0 + nextDoubleValue * 8.0 - doubleValue);
          float value =
              (float)
                  (new double[] {0.48, 0.025, 0.76, 0.58, 0.87, 0.64}[index]
                      + previousDoubleValue * 0.12
                      + nextDoubleValue * 0.04);
          int currentValue =
              Color.HSBtoRGB(value, 0.65F, (float) (0.24 + 0.46 * previousDoubleValue));
          int nextValue = previousIndex / 8;
          int previousValue = nextIndex / 8;
          if (nextValue == 0 || nextValue == 9 || previousValue == 0 || previousValue == 15) {
            currentValue = -15263451;
          }

          if ((nextValue == 1 || nextValue == 8) && previousValue > 1 && previousValue < 14) {
            currentValue = Color.HSBtoRGB(value, 0.4F, 0.85F);
          }

          int sourceValue =
              Math.abs(nextValue - 4.5) + Math.abs(previousValue - 5) < 3.0
                      && Math.abs(nextValue - 4.5) + Math.abs(previousValue - 5) > 1.4
                  ? 1
                  : 0;
          if (sourceValue != 0) {
            currentValue = -1514244;
          }

          if (index == 5 && (nextValue * 13 + previousValue * 7) % 17 == 0) {
            currentValue = -330296;
          }

          bufferedImage.setRGB(previousIndex, nextIndex, currentValue);
        }
      }

      arrayList.add(bufferedImage);
      currentArrayList.add(75);
    }

    return new CapeSizeService(arrayList, currentArrayList);
  }

  public static BufferedImage mannequin() {
    BufferedImage bufferedImage = new BufferedImage(64, 64, 2);
    Graphics2D graphics2D = bufferedImage.createGraphics();

    try {
      graphics2D.setColor(new Color(-4413977, true));
      graphics2D.fillRect(0, 0, 64, 64);
      graphics2D.setColor(new Color(-14146250, true));
      graphics2D.fillRect(0, 16, 64, 48);
      graphics2D.setColor(new Color(-1525348, true));
      graphics2D.fillRect(8, 8, 8, 8);
      graphics2D.fillRect(40, 20, 16, 12);
      graphics2D.fillRect(32, 52, 16, 12);
      graphics2D.setColor(new Color(-13621965, true));
      graphics2D.fillRect(8, 8, 8, 2);
      graphics2D.fillRect(9, 12, 2, 1);
      graphics2D.fillRect(13, 12, 2, 1);
      graphics2D.setColor(new Color(-7636797, true));
      graphics2D.fillRect(20, 20, 8, 12);
      graphics2D.setComposite(AlphaComposite.Clear);
      graphics2D.fillRect(32, 0, 32, 16);
      graphics2D.fillRect(0, 32, 56, 16);
      graphics2D.fillRect(0, 48, 16, 16);
      graphics2D.fillRect(48, 48, 16, 16);
    } finally {
      graphics2D.dispose();
    }

    return bufferedImage;
  }
}
