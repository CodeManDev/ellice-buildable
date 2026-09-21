package dev.felix.ellice.network;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public final class NetworkInspectService implements NetworkConnectedHandler {
   private static final String text2 = "http://schemas.xmlsoap.org/soap/envelope/";
   private static final int count = 262144;
   private final URI uRI;
   private final URI uRI2;
   private NetworkInspectService.Service service2;
   private String text3 = "FRITZ!Box";
   private Set<String> text4 = Set.of();
   private boolean enabled;

   public NetworkInspectService(String text) throws IOException {
      URI currentURI = address(text);
      this.uRI2 = currentURI;

      InetAddress[] inetAddressValues;
      try {
         inetAddressValues = InetAddress.getAllByName(currentURI.getHost());
      } catch (UnknownHostException unknownHostException) {
         throw new IOException("FRITZ!Box not found. Enter its local IP, for example 192.168.178.1.");
      }

      InetAddress inetAddress = Arrays.stream(inetAddressValues)
         .filter(NetworkInspectService::localAddress)
         .findFirst()
         .orElseThrow(() -> new NetworkInspectService.Rejected(0, "Choose a FRITZ!Box on your local network."));

      try {
         this.uRI = new URI("http", null, inetAddress.getHostAddress(), currentURI.getPort(), "/", null, null);
      } catch (URISyntaxException uRISyntaxException) {
         throw new IOException("Invalid local network address.");
      }
   }

   static URI address(String text) throws NetworkInspectService.Rejected {
      try {
         String currentText = text == null ? "" : text.strip();
         URI uRI = new URI(currentText.contains("://") ? currentText : "http://" + currentText);
         if ("http".equalsIgnoreCase(uRI.getScheme())
            && uRI.getHost() != null
            && uRI.getUserInfo() == null
            && uRI.getQuery() == null
            && uRI.getFragment() == null
            && List.of("", "/").contains(uRI.getPath())
            && uRI.getPort() != 0
            && uRI.getPort() <= 65535) {
            return new URI("http", null, uRI.getHost(), uRI.getPort() < 0 ? 49000 : uRI.getPort(), "/", null, null);
         } else {
            throw new URISyntaxException(currentText, "Expected a host");
         }
      } catch (URISyntaxException | IllegalArgumentException uRISyntaxExceptionIllegalArgumentException) {
         throw new NetworkInspectService.Rejected(0, "Enter fritz.box or a local IP, optionally with :49000. Do not include credentials or paths.");
      }
   }

   static boolean localAddress(InetAddress inetAddress) {
      byte[] bytes = inetAddress.getAddress();
      return !inetAddress.isAnyLocalAddress()
         && !inetAddress.isMulticastAddress()
         && (
            inetAddress.isSiteLocalAddress()
               || inetAddress.isLinkLocalAddress()
               || inetAddress.isLoopbackAddress()
               || bytes.length == 16 && (bytes[0] & 254) == 252
         );
   }

   @Override
   public NetworkConnectedHandler.Status inspect() throws IOException {
      if (this.service2 == null) {
         this.updateState();
      }

      return this.createStatus(this.service2, this.text4.contains("ForceTermination"), this.enabled);
   }

   private void updateState() throws IOException {
      Document document = xml(this.createByte(this.uRI.resolve("igddesc.xml"), null, null));
      String text = createText(document, "modelName");
      String currentText = createText(document, "manufacturer").toLowerCase(Locale.ROOT);
      if (!currentText.contains("avm") && !currentText.contains("fritz") && !text.toLowerCase(Locale.ROOT).contains("fritz!box")) {
         throw new NetworkInspectService.Rejected(0, "This address does not report a FRITZ!Box.");
      }

      this.text3 = text.isBlank() ? "FRITZ!Box" : createText3(text, 80);
      ArrayList arrayList = new ArrayList();
      NodeList nodeList = document.getElementsByTagNameNS("*", "service");

      for (int index = 0; index < nodeList.getLength(); index++) {
         Element element = (Element)nodeList.item(index);
         String nextText = createText2(element, "serviceType");
         if (nextText.matches("urn:schemas-upnp-org:service:WAN(?:IP|PPP)Connection:[12]")) {
            arrayList.add(
               new NetworkInspectService.Service(
                  nextText, this.createURI(createText2(element, "controlURL")), this.createURI(createText2(element, "SCPDURL"))
               )
            );
         }
      }

      if (arrayList.isEmpty()) {
         throw new NetworkInspectService.Rejected(
            0, "This router does not offer a password-free WAN service. Check UPnP status information in your FRITZ!Box."
         );
      }

      IOException iOException = null;

      for (NetworkInspectService.Service value : (Iterable<NetworkInspectService.Service>) (Iterable<?>) (arrayList)) {
         try {
            NetworkConnectedHandler.Status currentStatus = this.createStatus(value, false, false);
            if (this.service2 == null || currentStatus.connected()) {
               this.service2 = value;
            }

            if (currentStatus.connected()) {
               break;
            }
         } catch (IOException currentIOException) {
            iOException = currentIOException;
         }
      }

      if (this.service2 == null) {
         throw iOException == null ? new NetworkInspectService.Rejected(0, "No WAN status is available.") : iOException;
      }

      try {
         Document currentDocument = xml(this.createByte(this.service2.description, null, null));
         HashSet hashSet = new HashSet();
         NodeList currentNodeList = currentDocument.getElementsByTagNameNS("*", "action");

         for (int currentIndex = 0; currentIndex < currentNodeList.getLength(); currentIndex++) {
            hashSet.add(createText2((Element)currentNodeList.item(currentIndex), "name"));
         }

         this.text4 = Set.copyOf(hashSet);
         this.enabled = this.text4.contains("X_AVM_DE_GetExternalIPv6Address");
      } catch (IOException nextIOException) {
         this.service2 = null;
         throw nextIOException;
      }
   }

   private URI createURI(String text) throws NetworkInspectService.Rejected {
      try {
         URI currentURI = URI.create(text);
         if (!text.isBlank()
            && currentURI.getUserInfo() == null
            && currentURI.getQuery() == null
            && currentURI.getFragment() == null
            && currentURI.getPath() != null) {
            if (currentURI.getRawAuthority() != null || currentURI.isAbsolute()) {
               int value = currentURI.getHost() == null
                     || !currentURI.getHost().equalsIgnoreCase(this.uRI2.getHost())
                        && !currentURI.getHost().equalsIgnoreCase(this.uRI.getHost())
                  ? 0
                  : 1;
               if (!"http".equalsIgnoreCase(currentURI.getScheme()) || value == 0 || currentURI.getPort() != this.uRI.getPort()) {
                  throw new IllegalArgumentException();
               }

               currentURI = URI.create(currentURI.getRawPath());
            }

            URI nextURI = this.uRI.resolve(currentURI).normalize();
            if (nextURI.getPath().startsWith("/")
               && nextURI.getRawAuthority() != null
               && nextURI.getRawAuthority().equals(this.uRI.getRawAuthority())) {
               return nextURI;
            } else {
               throw new IllegalArgumentException();
            }
         } else {
            throw new IllegalArgumentException();
         }
      } catch (IllegalArgumentException illegalArgumentException) {
         throw new NetworkInspectService.Rejected(0, "The FRITZ!Box returned an invalid local service address.");
      }
   }

   private NetworkConnectedHandler.Status createStatus(NetworkInspectService.Service value, boolean currentEnabled, boolean nextEnabled) throws IOException {
      Document document = this.createDocument(value, "GetStatusInfo");
      String text = createText(document, "NewConnectionStatus");
      if (text.isBlank()) {
         throw new NetworkInspectService.Rejected(0, "The router did not return a valid connection status.");
      }

      String currentText = "";
      String nextText = "";

      try {
         currentText = ip(createText(this.createDocument(value, "GetExternalIPAddress"), "NewExternalIPAddress"), false);
      } catch (NetworkInspectService.Rejected rejected) {
         if (rejected.count2 != 401 && rejected.count2 != 501 && rejected.count2 != 713) {
            throw rejected;
         }
      }

      if (nextEnabled) {
         try {
            nextText = ip(createText(this.createDocument(value, "X_AVM_DE_GetExternalIPv6Address"), "NewExternalIPv6Address"), true);
         } catch (NetworkInspectService.Rejected currentRejected) {
            if (currentRejected.count2 != 401 && currentRejected.count2 != 501) {
               throw currentRejected;
            }

            this.enabled = false;
         }
      }

      long size;
      try {
         size = Long.parseLong(createText(document, "NewUptime"));
      } catch (NumberFormatException numberFormatException) {
         size = -1L;
      }

      return new NetworkConnectedHandler.Status(this.text3, text, currentText, nextText, size, currentEnabled);
   }

   @Override
   public void terminate() throws IOException {
      if (this.service2 != null && this.text4.contains("ForceTermination")) {
         this.createDocument(this.service2, "ForceTermination");
      } else {
         throw new NetworkInspectService.Rejected(0, "This router does not offer passwordless reconnects.");
      }
   }

   @Override
   public boolean canRequestConnection() {
      return this.text4.contains("RequestConnection");
   }

   @Override
   public void requestConnection() throws IOException {
      if (this.service2 != null && this.canRequestConnection()) {
         this.createDocument(this.service2, "RequestConnection");
      } else {
         throw new NetworkInspectService.Rejected(0, "Connection restoration is unavailable.");
      }
   }

   private Document createDocument(NetworkInspectService.Service value, String text) throws IOException {
      byte[] bytes = ("<?xml version=\"1.0\" encoding=\"utf-8\"?><s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body><u:"
            + text
            + " xmlns:u=\""
            + value.type
            + "\"/></s:Body></s:Envelope>")
         .getBytes(StandardCharsets.UTF_8);
      Document document = xml(this.createByte(value.control, value.type + "#" + text, bytes));
      updateState2(document);
      if (document.getElementsByTagNameNS(value.type, text + "Response").getLength() == 0) {
         throw new IOException("Incomplete FRITZ!Box response.");
      } else {
         return document;
      }
   }

   private byte[] createByte(URI uRI, String currentText, byte[] bytes) throws IOException {
      HttpURLConnection httpURLConnection = (HttpURLConnection)uRI.toURL().openConnection(Proxy.NO_PROXY);
      httpURLConnection.setConnectTimeout(2500);
      httpURLConnection.setReadTimeout(3500);
      httpURLConnection.setInstanceFollowRedirects(false);
      httpURLConnection.setUseCaches(false);
      httpURLConnection.setRequestProperty("Connection", "close");

      try {
         if (bytes != null) {
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setFixedLengthStreamingMode(bytes.length);
            httpURLConnection.setRequestProperty("Content-Type", "text/xml; charset=\"utf-8\"");
            httpURLConnection.setRequestProperty("SOAPAction", "\"" + currentText + "\"");

            try (OutputStream outputStream = httpURLConnection.getOutputStream()) {
               outputStream.write(bytes);
            }
         }

         int value = httpURLConnection.getResponseCode();
         if (value == 401 || value == 403) {
            throw createRejected();
         }

         if (value >= 300 && value < 400) {
            throw new NetworkInspectService.Rejected(value, "The router redirected the request. Use its local IP address.");
         }

         try (InputStream inputStream = value >= 400 ? httpURLConnection.getErrorStream() : httpURLConnection.getInputStream()) {
            byte[] currentBytes = inputStream == null ? new byte[0] : inputStream.readNBytes(262145);
            if (currentBytes.length > 262144) {
               throw new NetworkInspectService.Rejected(0, "The FRITZ!Box response is too large.");
            }

            if (value >= 400) {
               if (currentBytes.length > 0) {
                  Document document = null;

                  try {
                     document = xml(currentBytes);
                  } catch (IOException iOException) {
                  }

                  if (document != null) {
                     updateState2(document);
                  }
               }

               throw new NetworkInspectService.Rejected(
                  value,
                  value == 404
                     ? "UPnP was not found. Check the router address and UPnP status information in your FRITZ!Box."
                     : "The FRITZ!Box returned HTTP " + value + ". Check UPnP permissions for this computer."
               );
            } else {
               return currentBytes;
            }
         }
      } finally {
         httpURLConnection.disconnect();
      }
   }

   private static void updateState2(Document document) throws NetworkInspectService.Rejected {
      if (document.getElementsByTagNameNS("*", "Fault").getLength() != 0) {
         int value;
         try {
            value = Integer.parseInt(createText(document, "errorCode"));
         } catch (NumberFormatException numberFormatException) {
            value = 0;
         }

         if (value == 606) {
            throw createRejected();
         } else {
            throw new NetworkInspectService.Rejected(
               value,
               "The FRITZ!Box rejected the action (UPnP "
                  + value
                  + "). Check this device's permission to disconnect and restore the internet connection."
            );
         }
      }
   }

   private static NetworkInspectService.Rejected createRejected() {
      return new NetworkInspectService.Rejected(
         606,
         "The FRITZ!Box denied access. Under Home Network → Network → your computer, check permission to disconnect and restore the internet connection."
      );
   }

   static Document xml(byte[] bytes) throws IOException {
      try {
         DocumentBuilderFactory documentBuilder = DocumentBuilderFactory.newInstance();
         documentBuilder.setNamespaceAware(true);
         documentBuilder.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
         documentBuilder.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
         documentBuilder.setFeature("http://xml.org/sax/features/external-general-entities", false);
         documentBuilder.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
         documentBuilder.setXIncludeAware(false);
         documentBuilder.setExpandEntityReferences(false);
         documentBuilder.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD", "");
         documentBuilder.setAttribute("http://javax.xml.XMLConstants/property/accessExternalSchema", "");
         DocumentBuilder currentDocumentBuilder = documentBuilder.newDocumentBuilder();
         currentDocumentBuilder.setErrorHandler(new DefaultHandler() {
            @Override
            public void error(SAXParseException sAXParseException) throws SAXException {
               throw sAXParseException;
            }

            @Override
            public void fatalError(SAXParseException sAXParseException) throws SAXException {
               throw sAXParseException;
            }
         });
         return currentDocumentBuilder.parse(new ByteArrayInputStream(bytes));
      } catch (Exception exception) {
         throw new IOException("Invalid XML response from the FRITZ!Box.");
      }
   }

   private static String createText(Document document, String text) {
      NodeList nodeList = document.getElementsByTagNameNS("*", text);
      return nodeList.getLength() == 0 ? "" : nodeList.item(0).getTextContent().strip();
   }

   private static String createText2(Element element, String text) {
      for (Node node = element.getFirstChild(); node != null; node = node.getNextSibling()) {
         if (node instanceof Element && text.equals(node.getLocalName())) {
            return node.getTextContent().strip();
         }
      }

      return "";
   }

   private static String createText3(String text, int value) {
      String currentText = text.replaceAll("[\\p{Cntrl}§]", "");
      return currentText.substring(0, Math.min(value, currentText.length()));
   }

   static String ip(String text, boolean enabled) {
      if (text.isBlank() || text.length() > 64 || !text.matches(enabled ? "[0-9a-fA-F:]+" : "[0-9]+(?:\\.[0-9]+){3}")) {
         return "";
      }

      if (enabled && !text.contains(":")) {
         return "";
      }

      try {
         InetAddress inetAddress = InetAddress.getByName(text);
         if (!inetAddress.isAnyLocalAddress() && !inetAddress.isMulticastAddress() && !inetAddress.isLoopbackAddress()) {
            return inetAddress instanceof Inet6Address == enabled ? inetAddress.getHostAddress() : "";
         } else {
            return "";
         }
      } catch (UnknownHostException unknownHostException) {
         return "";
      }
   }

   public static final class Rejected extends IOException {
      private final int count2;

      public Rejected(int value, String text) {
         super(text);
         this.count2 = value;
      }

      public int code() {
         return this.count2;
      }
   }

   private record Service(String type, URI control, URI description) {
   }
}

