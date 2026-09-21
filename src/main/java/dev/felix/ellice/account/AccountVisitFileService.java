



package dev.felix.ellice.account;

import java.util.Objects;
import com.google.gson.JsonParser;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.nio.file.FileVisitor;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.SimpleFileVisitor;
import java.util.concurrent.TimeoutException;
import java.net.http.WebSocket;
import java.util.Collection;
import java.nio.file.attribute.FileAttribute;
import java.util.concurrent.atomic.AtomicBoolean;
import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Iterator;
import java.net.HttpCookie;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import java.nio.file.Path;
import java.util.function.BooleanSupplier;
import java.net.URI;
import java.time.Duration;

final class AccountVisitFileService
{
    private static final Duration fgj2czr6lvvb;
    private static final Duration f16qm3r16i7q;
    
    private AccountVisitFileService() {
    }
    
    static URI authorize(final AccountCodec accountCodec, final URI uri, final BooleanSupplier booleanSupplier) {
        final Path path = findBrowser().orElseThrow(() -> new AccountCodeService("COOKIE_BROWSER_UNAVAILABLE", "This session needs a browser. Install Chrome, Edge or Chromium, then retry the cookie, or use Microsoft sign-in."));
        MicrosoftOAuthBrowserFlow.checkCancelled(booleanSupplier);
        try (final BrowserSession open = BrowserSession.open(path, booleanSupplier, false)) {
            return authorizeInSession(open, accountCodec, uri);
        }
        catch (final AccountCodeService accountCodeService) {
            throw accountCodeService;
        }
        catch (final InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new AccountCodeService("CANCELLED", "Sign-in cancelled.");
        }
        catch (final Exception ex2) {
            throw m7c80pwff0an();
        }
    }
    
    static URI authorizeInSession(final BrowserSession browserSession, final AccountCodec accountCodec, final URI uri) throws Exception {
        browserSession.pageCommand("Network.setCookies", cookieParams(accountCodec));
        final JsonArray jsonArray = new JsonArray();
        jsonArray.add((JsonElement)object("urlPattern", "https://login.live.com/oauth20_desktop.srf*", "resourceType", "Document", "requestStage", "Request"));
        final JsonObject jsonObject = new JsonObject();
        jsonObject.add("patterns", (JsonElement)jsonArray);
        browserSession.pageCommand("Fetch.enable", jsonObject);
        final String asString = browserSession.pageCommand("Page.getFrameTree", new JsonObject()).getAsJsonObject("frameTree").getAsJsonObject("frame").get("id").getAsString();
        final CompletableFuture<JsonObject> pageAsync = browserSession.pageAsync("Page.navigate", object("url", uri.toString()));
        final long n = System.nanoTime() + AccountVisitFileService.fgj2czr6lvvb.toNanos();
        while (true) {
            browserSession.checkAlive();
            final JsonObject jsonObject2 = browserSession.fgvtqb45mlnr.callbacks.poll(100L, TimeUnit.MILLISECONDS);
            if (jsonObject2 != null) {
                final String asString2 = jsonObject2.get("requestId").getAsString();
                final URI create = URI.create(jsonObject2.getAsJsonObject("request").get("url").getAsString());
                final boolean b = asString.equals(jsonObject2.get("frameId").getAsString()) && MicrosoftOAuthBrowserFlow.isCallback(create);
                browserSession.pageCommand("Fetch.failRequest", object("requestId", asString2, "errorReason", "Aborted"));
                if (b) {
                    return create;
                }
                throw m7c80pwff0an();
            }
            else {
                if (pageAsync.isDone() && pageAsync.join().has("errorText")) {
                    throw new AccountCodeService("COOKIE_BROWSER_NETWORK", "The sign-in window couldn't load Microsoft. Check your connection and try again.");
                }
                if (System.nanoTime() >= n) {
                    throw new AccountCodeService("COOKIE_BROWSER_TIMEOUT", "The sign-in window timed out. Try again and complete any Microsoft prompts within five minutes.");
                }
                continue;
            }
        }
    }
    
    static JsonObject cookieParams(final AccountCodec accountCodec) {
        final JsonArray jsonArray = new JsonArray();
        for (final HttpCookie httpCookie : accountCodec.cookies()) {
            final JsonObject object = object("name", httpCookie.getName(), "value", httpCookie.getValue(), "path", httpCookie.getPath());
            final String domain = httpCookie.getDomain();
            if (domain.startsWith(".") && !httpCookie.getName().startsWith("__Host-")) {
                object.addProperty("domain", domain);
            }
            else {
                object.addProperty("url", AccountCodec.origin(httpCookie).toString());
            }
            object.addProperty("secure", Boolean.valueOf(httpCookie.getSecure()));
            object.addProperty("httpOnly", Boolean.valueOf(httpCookie.isHttpOnly()));
            jsonArray.add((JsonElement)object);
        }
        final JsonObject jsonObject = new JsonObject();
        jsonObject.add("cookies", (JsonElement)jsonArray);
        return jsonObject;
    }
    
    static Optional<Path> findBrowser() {
        final ArrayList<Path> list = new ArrayList<>();
        final String lowerCase = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
        if (lowerCase.contains("mac")) {
            for (String first : List.of("/Applications", Path.of(System.getProperty("user.home"), "Applications").toString())) {
                for (String s : List.of("Google Chrome", "Microsoft Edge", "Chromium", "Brave Browser")) {
                    list.add(Path.of(first, s + ".app", "Contents", "MacOS", s));
                }
            }
        }
        else if (lowerCase.contains("win")) {
            final Iterator<String> iterator3 = List.of("ProgramFiles", "ProgramFiles(x86)", "LOCALAPPDATA").iterator();
            while (iterator3.hasNext()) {
                final String getenv = System.getenv(iterator3.next());
                if (getenv != null) {
                    if (getenv.isBlank()) {
                        continue;
                    }
                    final Iterator<String> iterator4 = List.of("Google/Chrome/Application/chrome.exe", "Microsoft/Edge/Application/msedge.exe", "Chromium/Application/chrome.exe", "BraveSoftware/Brave-Browser/Application/brave.exe").iterator();
                    while (iterator4.hasNext()) {
                        list.add(Path.of(getenv, new String[0]).resolve(iterator4.next()));
                    }
                }
            }
        }
        else {
            final Iterator<String> iterator5 = List.of("/usr/bin/google-chrome", "/usr/bin/google-chrome-stable", "/usr/bin/microsoft-edge", "/usr/bin/microsoft-edge-stable", "/usr/bin/chromium", "/usr/bin/chromium-browser", "/usr/bin/brave-browser", "/snap/bin/chromium").iterator();
            while (iterator5.hasNext()) {
                list.add(Path.of(iterator5.next(), new String[0]));
            }
        }
        return list.stream().filter(path -> Files.isRegularFile(path, new LinkOption[0]) && Files.isExecutable(path)).findFirst();
    }
    
    static JsonObject object(final String... array) {
        final JsonObject jsonObject = new JsonObject();
        for (int i = 0; i < array.length; i += 2) {
            jsonObject.addProperty(array[i], array[i + 1]);
        }
        return jsonObject;
    }
    
    private static AccountCodeService m7c80pwff0an() {
        return new AccountCodeService("COOKIE_BROWSER_FAILED", "Couldn't complete the cookie sign-in window. Retry the cookie or use Microsoft sign-in.");
    }
    
    static {
        fgj2czr6lvvb = Duration.ofMinutes(5L);
        f16qm3r16i7q = Duration.ofSeconds(15L);
    }
    
    static final class BrowserSession implements AutoCloseable
    {
        final Path profile;
        private final Process fcknfmvmu0zp;
        private final BooleanSupplier fdmwc6l3fsed;
        private final HttpClient f1j8hs6jkucs;
        private final AtomicBoolean fj5gwnniwig2;
        private final Thread fcjrq1leumie;
        private Connection fgvtqb45mlnr;
        private String f1fpzqfoktz;
        
        private BrowserSession(final Path profile, final Process fcknfmvmu0zp, final BooleanSupplier fdmwc6l3fsed) {
            this.f1j8hs6jkucs = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5L)).build();
            this.fj5gwnniwig2 = new AtomicBoolean();
            this.profile = profile;
            this.fcknfmvmu0zp = fcknfmvmu0zp;
            this.fdmwc6l3fsed = fdmwc6l3fsed;
            this.fcjrq1leumie = new Thread(this::close, "ellice-cookie-browser-cleanup");
            Runtime.getRuntime().addShutdownHook(this.fcjrq1leumie);
        }
        
        static BrowserSession open(final Path path, final BooleanSupplier booleanSupplier, final boolean b) throws Exception {
            MicrosoftOAuthBrowserFlow.checkCancelled(booleanSupplier);
            final Path tempDirectory = Files.createTempDirectory("ellice-cookie-login-", (FileAttribute<?>[])new FileAttribute[0]);
            final ArrayList command = new ArrayList<String>(List.of(path.toString(), "--user-data-dir=" + String.valueOf(tempDirectory), "--remote-debugging-address=127.0.0.1", "--remote-debugging-port=0", "--no-first-run", "--no-default-browser-check", "--disable-sync", "--disable-extensions", "--no-startup-window", "--remote-allow-origins=*"));
            if (b) {
                command.add("--headless=new");
            }
            BrowserSession browserSession = null;
            try {
                browserSession = new BrowserSession(tempDirectory, new ProcessBuilder((List<String>)command).redirectOutput(ProcessBuilder.Redirect.DISCARD).redirectError(ProcessBuilder.Redirect.DISCARD).start(), booleanSupplier);
                browserSession.mav79z75fft9();
                return browserSession;
            }
            catch (final Exception ex) {
                if (browserSession != null) {
                    browserSession.close();
                }
                else {
                    deleteProfile(tempDirectory);
                }
                throw ex;
            }
        }
        
        private void mav79z75fft9() throws Exception {
            final long n = System.nanoTime() + AccountVisitFileService.f16qm3r16i7q.toNanos();
            final Path resolve = this.profile.resolve("DevToolsActivePort");
            Object o = List.of();
            while (((List)o).size() < 2) {
                this.checkAlive();
                if (System.nanoTime() >= n) {
                    throw AccountVisitFileService.m7c80pwff0an();
                }
                if (Files.isRegularFile(resolve, new LinkOption[0]) && Files.size(resolve) < 16384L) {
                    o = Files.readAllLines(resolve);
                }
                if (((List)o).size() >= 2) {
                    continue;
                }
                Thread.sleep(100L);
            }
            final int int1 = Integer.parseInt((String)((List)o).getFirst());
            final String path = (String)((List)o).get(1);
            if (int1 < 1 || int1 > 65535 || !path.matches("/devtools/browser/[A-Za-z0-9-]+")) {
                throw AccountVisitFileService.m7c80pwff0an();
            }
            final URI uri = new URI("ws", null, "127.0.0.1", int1, path, null, null);
            this.fgvtqb45mlnr = new Connection();
            this.fgvtqb45mlnr.socket = this.ma1l5kq6by7d(this.f1j8hs6jkucs.newWebSocketBuilder().connectTimeout(Duration.ofSeconds(5L)).buildAsync(uri, this.fgvtqb45mlnr));
            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("disposeOnDetach", Boolean.valueOf(true));
            final String asString = this.mbvmnpdzde41("Target.createBrowserContext", jsonObject).get("browserContextId").getAsString();
            this.mbvmnpdzde41("Browser.setDownloadBehavior", AccountVisitFileService.object("behavior", "deny", "browserContextId", asString));
            final JsonObject object = AccountVisitFileService.object("url", "about:blank", "browserContextId", asString);
            object.addProperty("newWindow", Boolean.valueOf(true));
            final JsonObject object2 = AccountVisitFileService.object("targetId", this.mbvmnpdzde41("Target.createTarget", object).get("targetId").getAsString());
            object2.addProperty("flatten", Boolean.valueOf(true));
            this.f1fpzqfoktz = this.mbvmnpdzde41("Target.attachToTarget", object2).get("sessionId").getAsString();
            this.fgvtqb45mlnr.session = this.f1fpzqfoktz;
            this.pageCommand("Page.enable", new JsonObject());
        }
        
        private JsonObject mbvmnpdzde41(final String s, final JsonObject jsonObject) throws Exception {
            return this.ma1l5kq6by7d(this.fgvtqb45mlnr.send(s, jsonObject, null));
        }
        
        JsonObject pageCommand(final String s, final JsonObject jsonObject) throws Exception {
            return this.ma1l5kq6by7d(this.pageAsync(s, jsonObject));
        }
        
        CompletableFuture<JsonObject> pageAsync(final String s, final JsonObject jsonObject) {
            return this.fgvtqb45mlnr.send(s, jsonObject, this.f1fpzqfoktz);
        }
        
        private <T> T ma1l5kq6by7d(final CompletableFuture<T> completableFuture) throws Exception {
            final long n = System.nanoTime() + AccountVisitFileService.f16qm3r16i7q.toNanos();
            while (true) {
                this.checkAlive();
                try {
                    return completableFuture.get(100L, TimeUnit.MILLISECONDS);
                }
                catch (final TimeoutException ex) {
                    if (System.nanoTime() >= n) {
                        throw AccountVisitFileService.m7c80pwff0an();
                    }
                    continue;
                }
            }
        }
        
        void checkAlive() {
            MicrosoftOAuthBrowserFlow.checkCancelled(this.fdmwc6l3fsed);
            if (!this.fcknfmvmu0zp.isAlive() || (this.fgvtqb45mlnr != null && this.fgvtqb45mlnr.disconnected.get())) {
                throw new AccountCodeService("COOKIE_BROWSER_CLOSED", "The sign-in window was closed. Retry the cookie to continue.");
            }
        }
        
        @Override
        public void close() {
            if (!this.fj5gwnniwig2.compareAndSet(false, true)) {
                return;
            }
            if (Thread.currentThread() != this.fcjrq1leumie) {
                try {
                    Runtime.getRuntime().removeShutdownHook(this.fcjrq1leumie);
                }
                catch (final IllegalStateException ex) {}
            }
            if (this.fgvtqb45mlnr != null && this.fgvtqb45mlnr.socket != null) {
                this.fgvtqb45mlnr.socket.abort();
            }
            this.f1j8hs6jkucs.shutdownNow();
            final List<ProcessHandle> list = this.fcknfmvmu0zp.descendants().toList();
            this.fcknfmvmu0zp.destroy();
            boolean interrupted = Thread.interrupted();
            try {
                if (!this.fcknfmvmu0zp.waitFor(2L, TimeUnit.SECONDS)) {
                    this.fcknfmvmu0zp.destroyForcibly();
                    this.fcknfmvmu0zp.waitFor(2L, TimeUnit.SECONDS);
                }
                list.stream().filter(ProcessHandle::isAlive).forEach(ProcessHandle::destroyForcibly);
                deleteProfile(this.profile);
            }
            catch (final InterruptedException ex2) {
                interrupted = true;
                this.fcknfmvmu0zp.destroyForcibly();
                list.stream().filter(ProcessHandle::isAlive).forEach(ProcessHandle::destroyForcibly);
                deleteProfile(this.profile);
            }
            finally {
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        
        static void deleteProfile(final Path start) {
            try {
                Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(final Path path, final BasicFileAttributes basicFileAttributes) throws IOException {
                        Files.deleteIfExists(path);
                        return FileVisitResult.CONTINUE;
                    }
                    
                    @Override
                    public FileVisitResult postVisitDirectory(final Path path, final IOException ex) throws IOException {
                        if (ex != null) {
                            throw ex;
                        }
                        Files.deleteIfExists(path);
                        return FileVisitResult.CONTINUE;
                    }
                });
            }
            catch (final IOException ex) {
                System.getLogger(AccountVisitFileService.class.getName()).log(System.Logger.Level.WARNING, "Could not fully remove a temporary sign-in browser directory.");
            }
        }
    }
    
    private static final class Connection implements WebSocket.Listener
    {
        private final AtomicInteger fvrgq0i2fwl;
        private final Map<Integer, CompletableFuture<JsonObject>> fimjzv7y7ugz;
        final BlockingQueue<JsonObject> callbacks;
        final AtomicBoolean disconnected;
        private final StringBuilder fcd6q7tb8zx;
        volatile WebSocket socket;
        volatile String session;
        
        private Connection() {
            this.fvrgq0i2fwl = new AtomicInteger();
            this.fimjzv7y7ugz = new ConcurrentHashMap<Integer, CompletableFuture<JsonObject>>();
            this.callbacks = new LinkedBlockingQueue<JsonObject>(16);
            this.disconnected = new AtomicBoolean();
            this.fcd6q7tb8zx = new StringBuilder();
        }
        
        synchronized CompletableFuture<JsonObject> send(final String s, final JsonObject jsonObject, final String s2) {
            if (this.disconnected.get()) {
                return CompletableFuture.failedFuture(AccountVisitFileService.m7c80pwff0an());
            }
            final int incrementAndGet = this.fvrgq0i2fwl.incrementAndGet();
            final CompletableFuture completableFuture = new CompletableFuture();
            this.fimjzv7y7ugz.put(incrementAndGet, completableFuture);
            final JsonObject object = AccountVisitFileService.object("method", s);
            object.addProperty("id", (Number)incrementAndGet);
            object.add("params", (JsonElement)jsonObject);
            if (s2 != null) {
                object.addProperty("sessionId", s2);
            }
            try {
                this.socket.sendText(object.toString(), true).join();
            }
            catch (final RuntimeException ex) {
                this.fimjzv7y7ugz.remove(incrementAndGet);
                completableFuture.completeExceptionally(AccountVisitFileService.m7c80pwff0an());
            }
            return completableFuture;
        }
        
        @Override
        public void onOpen(final WebSocket webSocket) {
            webSocket.request(1L);
        }
        
        @Override
        public CompletionStage<?> onText(final WebSocket webSocket, final CharSequence s, final boolean b) {
            try {
                if (this.fcd6q7tb8zx.length() + s.length() > 2097152) {
                    throw AccountVisitFileService.m7c80pwff0an();
                }
                this.fcd6q7tb8zx.append(s);
                if (b) {
                    final JsonObject asJsonObject = JsonParser.parseString(this.fcd6q7tb8zx.toString()).getAsJsonObject();
                    this.fcd6q7tb8zx.setLength(0);
                    if (asJsonObject.has("id")) {
                        final CompletableFuture completableFuture = this.fimjzv7y7ugz.remove(asJsonObject.get("id").getAsInt());
                        if (completableFuture != null) {
                            if (asJsonObject.has("error")) {
                                completableFuture.completeExceptionally(AccountVisitFileService.m7c80pwff0an());
                            }
                            else {
                                completableFuture.complete(asJsonObject.getAsJsonObject("result"));
                            }
                        }
                    }
                    else if (asJsonObject.has("sessionId") && Objects.equals(this.session, asJsonObject.get("sessionId").getAsString()) && asJsonObject.has("method") && asJsonObject.get("method").getAsString().equals("Fetch.requestPaused")) {
                        if (!this.callbacks.offer(asJsonObject.getAsJsonObject("params"))) {
                            throw AccountVisitFileService.m7c80pwff0an();
                        }
                    }
                    else if (asJsonObject.has("method") && asJsonObject.get("method").getAsString().equals("Target.detachedFromTarget") && Objects.equals(this.session, asJsonObject.getAsJsonObject("params").get("sessionId").getAsString())) {
                        this.mjpffqgpk4os();
                    }
                }
            }
            catch (final RuntimeException ex) {
                this.mjpffqgpk4os();
                webSocket.abort();
            }
            webSocket.request(1L);
            return null;
        }
        
        private void mjpffqgpk4os() {
            this.disconnected.set(true);
            this.fimjzv7y7ugz.values().forEach(completableFuture -> completableFuture.completeExceptionally(AccountVisitFileService.m7c80pwff0an()));
            this.fimjzv7y7ugz.clear();
        }
        
        @Override
        public CompletionStage<?> onClose(final WebSocket webSocket, final int n, final String s) {
            this.mjpffqgpk4os();
            return null;
        }
        
        @Override
        public void onError(final WebSocket webSocket, final Throwable t) {
            this.mjpffqgpk4os();
        }
    }
}
