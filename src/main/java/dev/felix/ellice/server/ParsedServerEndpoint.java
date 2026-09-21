package dev.felix.ellice.server;

import java.net.IDN;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.Locale;

public record ParsedServerEndpoint(String host, int port) {
  public ParsedServerEndpoint(String host, int port) {
    if (host != null && !host.isBlank() && port >= 1 && port <= 65535) {
      host = host.toLowerCase(Locale.ROOT);
      this.host = host;
      this.port = port;
    } else {
      throw new IllegalArgumentException("Enter a server address and a port from 1 to 65535.");
    }
  }

  public static ParsedServerEndpoint parse(String text) {
    String currentText = text == null ? "" : text.trim();
    if (!currentText.isEmpty()
        && currentText.length() <= 255
        && !currentText.chars().anyMatch(Character::isWhitespace)
        && !currentText.matches(".*[/\\\\?#@].*")) {
      String nextText = currentText;
      int value = 25565;

      try {
        if (currentText.startsWith("[")) {
          int currentValue = currentText.indexOf(93);
          if (currentValue < 2) {
            throw new IllegalArgumentException();
          }

          nextText = currentText.substring(1, currentValue);
          String previousText = currentText.substring(currentValue + 1);
          if (!previousText.isEmpty()) {
            if (!previousText.matches(":[0-9]{1,5}")) {
              throw new IllegalArgumentException();
            }

            value = Integer.parseInt(previousText.substring(1));
          }

          if (!nextText.contains(":")) {
            throw new IllegalArgumentException();
          }
        } else if (currentText.indexOf(58) == currentText.lastIndexOf(58)
            && currentText.contains(":")) {
          int nextValue = currentText.indexOf(58);
          nextText = currentText.substring(0, nextValue);
          String sourceText = currentText.substring(nextValue + 1);
          if (!sourceText.matches("[0-9]{1,5}")) {
            throw new IllegalArgumentException();
          }

          value = Integer.parseInt(sourceText);
        }

        if (nextText.contains(":")) {
          if (!nextText.matches("[0-9a-fA-F:.]+")
              || !(InetAddress.getByName(nextText) instanceof Inet6Address)) {
            throw new IllegalArgumentException();
          }
        } else {
          if (nextText.endsWith(".")) {
            nextText = nextText.substring(0, nextText.length() - 1);
          }

          nextText = IDN.toASCII(nextText, 2);
        }

        return new ParsedServerEndpoint(nextText, value);
      } catch (Exception exception) {
        throw new IllegalArgumentException("Enter a valid server address and port.");
      }
    } else {
      throw new IllegalArgumentException(
          "Enter a Minecraft server address, for example play.example.com.");
    }
  }

  public String address() {
    String text = this.host.contains(":") ? "[" + this.host + "]" : this.host;
    return this.port == 25565 ? text : text + ":" + this.port;
  }
}
