package dev.felix.ellice.compat;

import dev.felix.ellice.core.CoreSetBehindScreenService;
import dev.felix.ellice.server.ParsedServerEndpoint;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerData.Type;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.multiplayer.resolver.ResolvedServerAddress;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.client.multiplayer.resolver.ServerNameResolver;

public final class CompatSavedService {
  private CompatSavedService() {}

  public static List<CompatSavedService.Saved> saved() {
    ServerList serverList = new ServerList(Minecraft.getInstance());
    ArrayList arrayList = new ArrayList();

    for (int index = 0; index < serverList.size(); index++) {
      ServerData serverData = serverList.get(index);

      try {
        arrayList.add(
            new CompatSavedService.Saved(
                ParsedServerEndpoint.parse(serverData.ip).address(),
                serverData.name,
                serverData.getIconBytes()));
      } catch (IllegalArgumentException illegalArgumentException) {
      }
    }

    return arrayList;
  }

  public static CompatSavedService.Session session() {
    Minecraft minecraft = Minecraft.getInstance();
    ServerData serverData = minecraft.getCurrentServer();
    if (minecraft.level != null
        && minecraft.getConnection() != null
        && serverData != null
        && !serverData.isRealm()) {
      try {
        return new CompatSavedService.Session(
            minecraft.getUser().getProfileId(),
            new CompatSavedService.Saved(
                ParsedServerEndpoint.parse(serverData.ip).address(),
                serverData.name,
                serverData.getIconBytes()));
      } catch (IllegalArgumentException illegalArgumentException) {
        return null;
      }
    } else {
      return null;
    }
  }

  public static InetSocketAddress resolve(ParsedServerEndpoint parsedServerEndpoint)
      throws IOException {
    return ((ResolvedServerAddress)
            ServerNameResolver.DEFAULT
                .resolveAddress(
                    new ServerAddress(parsedServerEndpoint.host(), parsedServerEndpoint.port()))
                .orElseThrow(() -> new IOException("Server address unavailable")))
        .asInetSocketAddress();
  }

  public static int protocol() {
    return SharedConstants.getProtocolVersion();
  }

  public static void copyAddress(String text) {
    Minecraft.getInstance().keyboardHandler.setClipboard(text);
  }

  public static void add(String text) {
    Minecraft minecraft = Minecraft.getInstance();
    String currentText = ParsedServerEndpoint.parse(text).address();
    ServerList serverList = new ServerList(minecraft);

    for (int index = 0; index < serverList.size(); index++) {
      try {
        if (ParsedServerEndpoint.parse(serverList.get(index).ip).address().equals(currentText)) {
          return;
        }
      } catch (IllegalArgumentException illegalArgumentException) {
      }
    }

    serverList.add(new ServerData(currentText, currentText, Type.OTHER), false);
    serverList.save();
  }

  public static void connect(String text, String currentText) {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.level == null) {
      String nextText = ParsedServerEndpoint.parse(text).address();
      ServerList serverList = new ServerList(minecraft);
      ServerData serverData = null;

      for (int index = 0; index < serverList.size(); index++) {
        try {
          if (ParsedServerEndpoint.parse(serverList.get(index).ip).address().equals(nextText)) {
            serverData = serverList.get(index);
            break;
          }
        } catch (IllegalArgumentException illegalArgumentException) {
        }
      }

      if (serverData == null) {
        serverData = new ServerData(currentText, nextText, Type.OTHER);
      }

      ConnectScreen.startConnecting(
          (Screen)
              (minecraft.screen != null && !(minecraft.screen instanceof CoreSetBehindScreenService)
                  ? minecraft.screen
                  : new TitleScreen()),
          minecraft,
          ServerAddress.parseString(nextText),
          serverData,
          false,
          null);
    }
  }

  public record Saved(String address, String name, byte[] icon) {}

  public record Session(UUID account, CompatSavedService.Saved server) {}
}
