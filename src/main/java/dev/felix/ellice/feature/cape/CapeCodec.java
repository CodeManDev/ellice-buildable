package dev.felix.ellice.feature.cape;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.MemoryCacheImageInputStream;
import org.w3c.dom.Node;

public final class CapeCodec {
   public static final int MAX_BYTES = 16777216;
   public static final int MAX_FRAMES = 240;
   public static final int MAX_SIDE = 256;

   private CapeCodec() {
   }

   public static CapeSizeService decode(byte[] bytes) throws IOException {
      if (bytes.length != 0 && bytes.length <= 16777216) {
         try (MemoryCacheImageInputStream memoryCacheImageInputStream = new MemoryCacheImageInputStream(new ByteArrayInputStream(bytes))) {
            Iterator iterator = ImageIO.getImageReaders(memoryCacheImageInputStream);
            if (!iterator.hasNext()) {
               throw new IOException("This is not a supported GIF, PNG or JPEG image.");
            }

            ImageReader imageReader = (ImageReader)iterator.next();

            try {
               imageReader.setInput(memoryCacheImageInputStream, false, false);
               boolean enabled = imageReader.getFormatName().equalsIgnoreCase("gif");
               int width = imageReader.getWidth(0);
               int height = imageReader.getHeight(0);
               int value = 0;
               Node node = enabled && imageReader.getStreamMetadata() != null
                  ? imageReader.getStreamMetadata().getAsTree("javax_imageio_gif_stream_1.0")
                  : null;
               Node currentNode = createNode(node, "LogicalScreenDescriptor");
               if (currentNode != null) {
                  width = calculateValue(currentNode, "logicalScreenWidth", width);
                  height = calculateValue(currentNode, "logicalScreenHeight", height);
               }

               if (width >= 1 && height >= 1 && width <= 4096 && height <= 4096 && (long)width * height <= 4194304L) {
                  Node nextNode = createNode(node, "GlobalColorTable");
                  int currentValue = calculateValue(nextNode, "backgroundColorIndex", -1);
                  if (nextNode != null) {
                     for (Node previousNode = nextNode.getFirstChild(); previousNode != null; previousNode = previousNode.getNextSibling()) {
                        if (calculateValue(previousNode, "index", -2) == currentValue) {
                           value = 0xFF000000
                              | calculateValue(previousNode, "red", 0) << 16
                              | calculateValue(previousNode, "green", 0) << 8
                              | calculateValue(previousNode, "blue", 0);
                        }
                     }
                  }

                  BufferedImage bufferedImage = new BufferedImage(width, height, 2);
                  ArrayList arrayList = new ArrayList();
                  ArrayList currentArrayList = new ArrayList();
                  long longValue = 0L;
                  int currentIndex = 0;

                  while (!Thread.currentThread().isInterrupted()) {
                     label414: {
                        Node sourceNode;
                        int nextValue;
                        int previousValue;
                        try {
                           nextValue = imageReader.getWidth(currentIndex);
                           previousValue = imageReader.getHeight(currentIndex);
                           sourceNode = enabled ? imageReader.getImageMetadata(currentIndex).getAsTree("javax_imageio_gif_image_1.0") : null;
                        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
                           break label414;
                        }

                        if (currentIndex >= 240) {
                           throw new IOException("Choose an animation with at most 240 frames.");
                        }

                        if ((long)nextValue * previousValue > 4194304L) {
                           throw new IOException("GIF frame is too large.");
                        }

                        Node targetNode = createNode(sourceNode, "ImageDescriptor");
                        Node inputNode = createNode(sourceNode, "GraphicControlExtension");
                        int sourceValue = calculateValue(targetNode, "imageLeftPosition", 0);
                        int targetValue = calculateValue(targetNode, "imageTopPosition", 0);
                        if (sourceValue < 0 || targetValue < 0 || sourceValue + nextValue > width || targetValue + previousValue > height) {
                           throw new IOException("GIF frame lies outside its canvas.");
                        }

                        boolean currentEnabled = "TRUE".equalsIgnoreCase(createText(inputNode, "transparentColorFlag", "FALSE"));
                        int inputValue = currentEnabled && calculateValue(inputNode, "transparentColorIndex", -2) == currentValue ? 0 : value;
                        if (currentIndex == 0 && !currentEnabled) {
                           updateState(bufferedImage, 0, 0, width, height, value);
                        }

                        String text = createText(inputNode, "disposalMethod", "none");
                        BufferedImage currentBufferedImage = "restoreToPrevious".equals(text) ? copy(bufferedImage, width, height) : null;
                        BufferedImage nextBufferedImage = imageReader.read(currentIndex);
                        Graphics2D graphics2D = bufferedImage.createGraphics();

                        try {
                           graphics2D.setComposite(AlphaComposite.SrcOver);
                           graphics2D.drawImage(nextBufferedImage, sourceValue, targetValue, null);
                        } finally {
                           graphics2D.dispose();
                        }

                        double doubleValue = Math.min(1.0, 256.0 / Math.max(width, height));
                        arrayList.add(copy(bufferedImage, Math.max(1, (int)Math.round(width * doubleValue)), Math.max(1, (int)Math.round(height * doubleValue))));
                        int outputValue = enabled ? calculateValue(inputNode, "delayTime", 10) * 10 : 1000;
                        outputValue = outputValue < 20 ? 100 : Math.min(10000, outputValue);
                        currentArrayList.add(outputValue);
                        longValue += outputValue;
                        if (longValue > 120000L) {
                           throw new IOException("Choose an animation shorter than two minutes.");
                        }

                        if (text.equals("restoreToBackgroundColor")) {
                           updateState(bufferedImage, sourceValue, targetValue, nextValue, previousValue, inputValue);
                        } else if (currentBufferedImage != null) {
                           bufferedImage = currentBufferedImage;
                        }

                        if (enabled) {
                           currentIndex++;
                           continue;
                        }
                     }

                     if (arrayList.isEmpty()) {
                        throw new IOException("The image has no frames.");
                     }

                     return new CapeSizeService(arrayList, currentArrayList);
                  }

                  throw new InterruptedIOException("Image decoding cancelled.");
               } else {
                  throw new IOException("Image dimensions are too large (maximum 4 megapixels).");
               }
            } finally {
               imageReader.dispose();
            }
         }
      } else {
         throw new IOException("Choose an image smaller than 16 MB.");
      }
   }

   public static BufferedImage copy(BufferedImage bufferedImage, int value, int currentValue) {
      BufferedImage currentBufferedImage = new BufferedImage(value, currentValue, 2);
      Graphics2D graphics2D = currentBufferedImage.createGraphics();

      try {
         graphics2D.setComposite(AlphaComposite.Src);
         graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
         graphics2D.drawImage(bufferedImage, 0, 0, value, currentValue, null);
      } finally {
         graphics2D.dispose();
      }

      return currentBufferedImage;
   }

   private static void updateState(BufferedImage bufferedImage, int value, int currentValue, int nextValue, int previousValue, int sourceValue) {
      Graphics2D graphics2D = bufferedImage.createGraphics();

      try {
         graphics2D.setComposite(AlphaComposite.Src);
         graphics2D.setColor(new Color(sourceValue, true));
         graphics2D.fillRect(value, currentValue, nextValue, previousValue);
      } finally {
         graphics2D.dispose();
      }
   }

   private static Node createNode(Node node, String text) {
      if (node != null) {
         for (Node currentNode = node.getFirstChild(); currentNode != null; currentNode = currentNode.getNextSibling()) {
            if (currentNode.getNodeName().equals(text)) {
               return currentNode;
            }
         }
      }

      return null;
   }

   private static String createText(Node node, String text, String currentText) {
      Node currentNode = node != null && node.getAttributes() != null ? node.getAttributes().getNamedItem(text) : null;
      return currentNode == null ? currentText : currentNode.getNodeValue();
   }

   private static int calculateValue(Node node, String text, int value) {
      try {
         return Integer.parseInt(createText(node, text, ""));
      } catch (NumberFormatException numberFormatException) {
         return value;
      }
   }
}
