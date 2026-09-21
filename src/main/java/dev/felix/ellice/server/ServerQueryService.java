package dev.felix.ellice.server;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public final class ServerQueryService {
  private static final int count = 1048576;
  private static final int count2 = 3500;

  public ServerQueryService.Status query(
      ParsedServerEndpoint parsedServerEndpoint, InetSocketAddress inetSocketAddress, int value)
      throws IOException {
    try (Socket socket = new Socket()) {
      Thread thread =
          Thread.ofVirtual()
              .name("ellice-server-deadline")
              .start(
                  () -> {
                    try {
                      Thread.sleep(10000L);
                      socket.close();
                    } catch (InterruptedException interruptedException) {
                      Thread.currentThread().interrupt();
                    } catch (IOException iOException) {
                    }
                  });

      try {
        socket.connect(inetSocketAddress, 3500);
        socket.setSoTimeout(3500);
        socket.setTcpNoDelay(true);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream currentDataOutputStream = new DataOutputStream(byteArrayOutputStream);
        writeVarInt(currentDataOutputStream, 0);
        writeVarInt(currentDataOutputStream, value);
        byte[] bytes = parsedServerEndpoint.host().getBytes(StandardCharsets.UTF_8);
        writeVarInt(currentDataOutputStream, bytes.length);
        currentDataOutputStream.write(bytes);
        currentDataOutputStream.writeShort(parsedServerEndpoint.port());
        writeVarInt(currentDataOutputStream, 1);
        updateState(dataOutputStream, byteArrayOutputStream.toByteArray());
        updateState(dataOutputStream, new byte[] {0});
        DataInputStream currentDataInputStream = createDataInputStream(dataInputStream);
        if (readVarInt(currentDataInputStream) != 0) {
          throw new IOException("Unexpected status packet");
        }

        int currentValue = readVarInt(currentDataInputStream);
        if (currentValue < 0 || currentValue > currentDataInputStream.available()) {
          throw new IOException("Invalid status length");
        }

        String text =
            new String(currentDataInputStream.readNBytes(currentValue), StandardCharsets.UTF_8);
        long longValue = System.nanoTime();
        long currentLongValue = -1L;

        try {
          ByteArrayOutputStream currentByteArrayOutputStream = new ByteArrayOutputStream();
          DataOutputStream nextDataOutputStream =
              new DataOutputStream(currentByteArrayOutputStream);
          nextDataOutputStream.writeByte(1);
          nextDataOutputStream.writeLong(longValue);
          updateState(dataOutputStream, currentByteArrayOutputStream.toByteArray());
          DataInputStream nextDataInputStream = createDataInputStream(dataInputStream);
          if (readVarInt(nextDataInputStream) == 1 && nextDataInputStream.readLong() == longValue) {
            currentLongValue = Math.max(0L, (System.nanoTime() - longValue) / 1000000L);
          }
        } catch (IOException iOException) {
        }

        return parse(text, currentLongValue);
      } finally {
        thread.interrupt();
      }
    }
  }

  static ServerQueryService.Status parse(String text, long longValue) throws IOException {
    try {
      JsonObject jsonObject = JsonParser.parseString(text).getAsJsonObject();
      JsonObject currentJsonObject = createJsonObject(jsonObject, "players");
      JsonObject nextJsonObject = createJsonObject(jsonObject, "version");
      byte[] bytes = null;
      String currentText = createText2(jsonObject, "favicon");
      String nextText = "data:image/png;base64,";
      if (currentText.startsWith(nextText) && currentText.length() <= 349589) {
        try {
          bytes = Base64.getDecoder().decode(currentText.substring(nextText.length()));
        } catch (IllegalArgumentException illegalArgumentException) {
        }
      }

      ArrayList arrayList = new ArrayList();
      if (currentJsonObject.has("sample") && currentJsonObject.get("sample").isJsonArray()) {
        for (JsonElement jsonElement : currentJsonObject.getAsJsonArray("sample")) {
          if (arrayList.size() >= 32) {
            break;
          }

          if (jsonElement.isJsonObject()) {
            String previousText =
                clean(createText2(jsonElement.getAsJsonObject(), "name"), 64).strip();
            if (!previousText.isEmpty() && !arrayList.contains(previousText)) {
              arrayList.add(previousText);
            }
          }
        }
      }

      return new ServerQueryService.Status(
          calculateValue(currentJsonObject, "online"),
          calculateValue(currentJsonObject, "max"),
          longValue,
          clean(createText2(nextJsonObject, "name"), 100),
          clean(createText(jsonObject.get("description"), 0), 512),
          bytes,
          arrayList,
          calculateValue(nextJsonObject, "protocol"));
    } catch (RuntimeException exception) {
      throw new IOException("Invalid server status", exception);
    }
  }

  private static String createText(JsonElement jsonElement, int value) {
    if (jsonElement == null || jsonElement.isJsonNull() || value > 16) {
      return "";
    }

    if (jsonElement.isJsonPrimitive()) {
      return jsonElement.getAsString();
    }

    StringBuilder stringBuilder = new StringBuilder();
    if (jsonElement.isJsonArray()) {
      for (JsonElement currentJsonElement : jsonElement.getAsJsonArray()) {
        if (stringBuilder.length() >= 512) {
          break;
        }

        stringBuilder.append(createText(currentJsonElement, value + 1));
      }
    } else if (jsonElement.isJsonObject()) {
      JsonObject jsonObject = jsonElement.getAsJsonObject();
      stringBuilder.append(createText2(jsonObject, "text"));
      if (stringBuilder.isEmpty()) {
        stringBuilder.append(createText2(jsonObject, "translate"));
      }

      stringBuilder.append(createText(jsonObject.get("extra"), value + 1));
    }

    return stringBuilder.substring(0, Math.min(512, stringBuilder.length()));
  }

  static String clean(String text, int value) {
    String currentText =
        text.replaceAll("(?i)§[0-9a-fk-orx]", "").replaceAll("[\\p{Cntrl}&&[^\\n]]", "").strip();
    return currentText.substring(0, Math.min(value, currentText.length()));
  }

  private static JsonObject createJsonObject(JsonObject jsonObject, String text) {
    return jsonObject.has(text) && jsonObject.get(text).isJsonObject()
        ? jsonObject.getAsJsonObject(text)
        : new JsonObject();
  }

  private static String createText2(JsonObject jsonObject, String text) {
    return jsonObject.has(text) && jsonObject.get(text).isJsonPrimitive()
        ? jsonObject.get(text).getAsString()
        : "";
  }

  private static int calculateValue(JsonObject jsonObject, String text) {
    try {
      return Math.max(-1, jsonObject.get(text).getAsInt());
    } catch (RuntimeException exception) {
      return -1;
    }
  }

  private static DataInputStream createDataInputStream(DataInputStream dataInputStream)
      throws IOException {
    int index = readVarInt(dataInputStream);
    if (index >= 1 && index <= 1048576) {
      byte[] bytes = new byte[index];
      dataInputStream.readFully(bytes);
      return new DataInputStream(new ByteArrayInputStream(bytes));
    } else {
      throw new IOException("Invalid packet size");
    }
  }

  private static void updateState(DataOutputStream dataOutputStream, byte[] bytes)
      throws IOException {
    writeVarInt(dataOutputStream, bytes.length);
    dataOutputStream.write(bytes);
    dataOutputStream.flush();
  }

  static int readVarInt(DataInputStream dataInputStream) throws IOException {
    int value = 0;

    for (int index = 0; index < 5; index++) {
      int currentValue = dataInputStream.readUnsignedByte();
      if (index == 4 && (currentValue & 240) != 0) {
        throw new IOException("Invalid VarInt");
      }

      value |= (currentValue & 127) << index * 7;
      if ((currentValue & 128) == 0) {
        return value;
      }
    }

    throw new IOException("VarInt too long");
  }

  static void writeVarInt(DataOutputStream dataOutputStream, int value) throws IOException {
    do {
      int currentValue = value & 127;
      value >>>= 7;
      dataOutputStream.writeByte(value == 0 ? currentValue : currentValue | 128);
    } while (value != 0);
  }

  public record Status(
      int players,
      int capacity,
      long latencyMs,
      String version,
      String motd,
      byte[] icon,
      List<String> samplePlayers,
      int protocol) {
    public Status(
        int players,
        int capacity,
        long latencyMs,
        String version,
        String motd,
        byte[] icon,
        List<String> samplePlayers,
        int protocol) {
      samplePlayers = List.copyOf(samplePlayers);
      this.players = players;
      this.capacity = capacity;
      this.latencyMs = latencyMs;
      this.version = version;
      this.motd = motd;
      this.icon = icon;
      this.samplePlayers = samplePlayers;
      this.protocol = protocol;
    }

    public Status(
        int value,
        int currentValue,
        long longValue,
        String text,
        String currentText,
        byte[] bytes) {
      this(value, currentValue, longValue, text, currentText, bytes, List.of(), -1);
    }
  }
}
