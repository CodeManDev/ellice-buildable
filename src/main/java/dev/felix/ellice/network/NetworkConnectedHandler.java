package dev.felix.ellice.network;

import java.io.IOException;

public interface NetworkConnectedHandler {
  NetworkConnectedHandler.Status inspect() throws IOException;

  void terminate() throws IOException;

  boolean canRequestConnection();

  void requestConnection() throws IOException;

  record Status(
      String device,
      String connection,
      String ipv4,
      String ipv6,
      long uptime,
      boolean canReconnect) {
    public boolean connected() {
      return "Connected".equals(this.connection);
    }

    public boolean hasAddress() {
      return !this.ipv4.isEmpty() || !this.ipv6.isEmpty();
    }

    public boolean changedFrom(NetworkConnectedHandler.Status status) {
      return !status.ipv4.isEmpty() && !this.ipv4.isEmpty() && !status.ipv4.equals(this.ipv4)
          || !status.ipv6.isEmpty() && !this.ipv6.isEmpty() && !status.ipv6.equals(this.ipv6);
    }

    public boolean sameAddresses(NetworkConnectedHandler.Status status) {
      return status != null && this.ipv4.equals(status.ipv4) && this.ipv6.equals(status.ipv6);
    }
  }
}
