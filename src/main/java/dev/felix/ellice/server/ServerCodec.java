package dev.felix.ellice.server;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public final class ServerCodec {
  public static final int MAX_BYTES = 262144;

  private ServerCodec() {}

  public static BufferedImage decode(byte[] bytes) {
    if (bytes != null && bytes.length >= 8 && bytes.length <= 262144) {
      try (ImageInputStream imageInputStream =
          ImageIO.createImageInputStream(new ByteArrayInputStream(bytes))) {
        Iterator iterator = ImageIO.getImageReaders(imageInputStream);
        if (!iterator.hasNext()) {
          return null;
        }

        ImageReader imageReader = (ImageReader) iterator.next();

        try {
          imageReader.setInput(imageInputStream, true, true);
          return "png".equalsIgnoreCase(imageReader.getFormatName())
                  && imageReader.getWidth(0) >= 1
                  && imageReader.getHeight(0) >= 1
                  && imageReader.getWidth(0) <= 256
                  && imageReader.getHeight(0) <= 256
              ? imageReader.read(0)
              : null;
        } finally {
          imageReader.dispose();
        }
      } catch (Exception exception) {
        return null;
      }
    } else {
      return null;
    }
  }
}
