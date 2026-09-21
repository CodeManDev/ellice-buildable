








package dev.felix.ellice.account;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.felix.ellice.account.AccountCodec;
import dev.felix.ellice.account.AccountVisitFileService;
import dev.felix.ellice.account.CookieBrowserLoginFlow;
import dev.felix.ellice.account.MicrosoftOAuthBrowserFlow;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BooleanSupplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class AccountImportCookiesService
implements CookieBrowserLoginFlow.Session {
    private static final Duration f9do9d9qv0ue = Duration.ofSeconds(15L);
    private static final Pattern fdec11q0hpgy = Pattern.compile("WebDriver BiDi listening on ws://127\\.0\\.0\\.1:(\\d+)");
    final Path profile;
    private final Process f9a68dknvt18;
    private final BooleanSupplier fgwvqcepi4km;
    private final HttpClient f2mj2qpm09mv = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5L)).build();
    private final AtomicBoolean ffkawb0zq659 = new AtomicBoolean();
    private final CompletableFuture<URI> f913xqu51f16 = new CompletableFuture();
    private final Connection ffrbf93zjz7b = new Connection();
    private final Thread fdpp5tu9giuj;
    private WebSocket f5mga45ajosu;
    private String f2vnwolviuvs;

    private AccountImportCookiesService(Path path, Process process, BooleanSupplier booleanSupplier) {
        this.profile = path;
        this.f9a68dknvt18 = process;
        this.fgwvqcepi4km = booleanSupplier;
        this.fdpp5tu9giuj = new Thread(this::close, "ellice-firefox-cleanup");
        Runtime.getRuntime().addShutdownHook(this.fdpp5tu9giuj);
        Thread thread = new Thread(() -> {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8));){
                String string;
                while ((string = bufferedReader.readLine()) != null) {
                    int n;
                    Matcher matcher = fdec11q0hpgy.matcher(string);
                    if (!matcher.find() || (n = Integer.parseInt(matcher.group(1))) <= 0 || n > 65535) continue;
                    this.f913xqu51f16.complete(URI.create("ws://127.0.0.1:" + n + "/session"));
                }
            }
            catch (Exception exception) {
                this.f913xqu51f16.completeExceptionally(CookieBrowserLoginFlow.failure());
            }
        }, "ellice-firefox-startup");
        thread.setDaemon(true);
        thread.start();
    }

    static AccountImportCookiesService open(Path path, BooleanSupplier booleanSupplier, boolean bl) throws Exception {
        MicrosoftOAuthBrowserFlow.checkCancelled(booleanSupplier);
        Path path2 = Files.createTempDirectory("ellice-firefox-login-", new FileAttribute[0]);
        AccountImportCookiesService accountImportCookiesService = null;
        try {
            Files.writeString(path2.resolve("user.js"), (CharSequence)"user_pref(\"browser.shell.checkDefaultBrowser\", false);\nuser_pref(\"browser.sessionstore.resume_from_crash\", false);\nuser_pref(\"signon.rememberSignons\", false);\n", new OpenOption[0]);
            ArrayList<String> arrayList = new ArrayList<String>(List.of(path.toString(), "--no-remote", "--profile", path2.toString(), "--remote-debugging-port", "0"));
            if (bl) {
                arrayList.add("--headless");
            }
            arrayList.add("about:blank");
            Process process = new ProcessBuilder(arrayList).redirectOutput(ProcessBuilder.Redirect.DISCARD).start();
            accountImportCookiesService = new AccountImportCookiesService(path2, process, booleanSupplier);
            accountImportCookiesService.mjno98puxkiv();
            return accountImportCookiesService;
        }
        catch (Exception exception) {
            if (accountImportCookiesService != null) {
                accountImportCookiesService.close();
            } else {
                AccountVisitFileService.BrowserSession.deleteProfile(path2);
            }
            throw exception;
        }
    }

    private void mjno98puxkiv() throws Exception {
        this.f5mga45ajosu = this.mguxkfyt8dti(this.f2mj2qpm09mv.newWebSocketBuilder().connectTimeout(Duration.ofSeconds(5L)).buildAsync(this.mguxkfyt8dti(this.f913xqu51f16), this.ffrbf93zjz7b));
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("capabilities", (JsonElement)new JsonObject());
        this.command("session.new", jsonObject);
        this.f2vnwolviuvs = this.command("browsingContext.create", AccountVisitFileService.object("type", "tab")).get("context").getAsString();
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.add("downloadBehavior", (JsonElement)AccountVisitFileService.object("type", "denied"));
        this.command("browser.setDownloadBehavior", jsonObject2);
    }

    @Override
    public void importCookies(AccountCodec accountCodec) throws Exception {
        for (HttpCookie httpCookie : accountCodec.cookies()) {
            if (httpCookie.hasExpired()) continue;
            this.command("storage.setCookie", AccountImportCookiesService.cookieParams(httpCookie));
        }
    }

    static JsonObject cookieParams(HttpCookie httpCookie) {
        JsonObject jsonObject = AccountVisitFileService.object("name", httpCookie.getName(), "domain", httpCookie.getDomain(), "path", httpCookie.getPath());
        jsonObject.add("value", (JsonElement)AccountVisitFileService.object("type", "string", "value", httpCookie.getValue()));
        jsonObject.addProperty("secure", Boolean.valueOf(httpCookie.getSecure()));
        jsonObject.addProperty("httpOnly", Boolean.valueOf(httpCookie.isHttpOnly()));
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.add("cookie", (JsonElement)jsonObject);
        return jsonObject2;
    }

    @Override
    public void navigate(URI uRI) throws Exception {
        this.command("browsingContext.navigate", AccountVisitFileService.object("context", this.f2vnwolviuvs, "url", uRI.toString(), "wait", "none"));
    }

    @Override
    public JsonArray cookies() throws Exception {
        return this.command("storage.getCookies", new JsonObject()).getAsJsonArray("cookies");
    }

    @Override
    public String location() throws Exception {
        JsonArray jsonArray = this.command("browsingContext.getTree", AccountVisitFileService.object("root", this.f2vnwolviuvs)).getAsJsonArray("contexts");
        if (jsonArray.isEmpty()) {
            throw CookieBrowserLoginFlow.closed();
        }
        return jsonArray.get(0).getAsJsonObject().get("url").getAsString();
    }

    


    JsonObject command(String string, JsonObject jsonObject) throws Exception {
        this.checkAlive();
        int n = this.ffrbf93zjz7b.sequence.incrementAndGet();
        CompletableFuture completableFuture = new CompletableFuture();
        this.ffrbf93zjz7b.pending.put(n, completableFuture);
        JsonObject jsonObject2 = AccountVisitFileService.object("method", string);
        jsonObject2.addProperty("id", (Number)n);
        jsonObject2.add("params", (JsonElement)jsonObject);
        try {
            this.mguxkfyt8dti(this.f5mga45ajosu.sendText(jsonObject2.toString(), true));
            JsonObject jsonObject3 = (JsonObject)this.mguxkfyt8dti(completableFuture);
            return jsonObject3;
        }
        finally {
            this.ffrbf93zjz7b.pending.remove(n);
        }
    }

    private <T> T mguxkfyt8dti(CompletableFuture<T> completableFuture) throws Exception {
        long l = System.nanoTime() + f9do9d9qv0ue.toNanos();
        while (true) {
            this.checkAlive();
            try {
                return completableFuture.get(100L, TimeUnit.MILLISECONDS);
            }
            catch (TimeoutException timeoutException) {
                if (System.nanoTime() < l) continue;
                throw CookieBrowserLoginFlow.failure();
            }
        }
    }

    @Override
    public void checkAlive() {
        MicrosoftOAuthBrowserFlow.checkCancelled(this.fgwvqcepi4km);
        if (this.ffkawb0zq659.get() || !this.f9a68dknvt18.isAlive() || this.ffrbf93zjz7b.disconnected.get()) {
            throw CookieBrowserLoginFlow.closed();
        }
    }

    


    @Override
    public void close() {
        if (!this.ffkawb0zq659.compareAndSet(false, true)) {
            return;
        }
        if (Thread.currentThread() != this.fdpp5tu9giuj) {
            try {
                Runtime.getRuntime().removeShutdownHook(this.fdpp5tu9giuj);
            }
            catch (IllegalStateException illegalStateException) {
                
            }
        }
        if (this.f5mga45ajosu != null) {
            this.f5mga45ajosu.abort();
        }
        this.f2mj2qpm09mv.shutdownNow();
        List<ProcessHandle> list = this.f9a68dknvt18.descendants().toList();
        this.f9a68dknvt18.destroy();
        boolean interrupted = Thread.interrupted();
        try {
            if (!this.f9a68dknvt18.waitFor(2L, TimeUnit.SECONDS)) {
                this.f9a68dknvt18.destroyForcibly();
                this.f9a68dknvt18.waitFor(2L, TimeUnit.SECONDS);
            }
            list.stream().filter(ProcessHandle::isAlive).forEach(ProcessHandle::destroyForcibly);
        }
        catch (InterruptedException interruptedException) {
            interrupted = true;
            this.f9a68dknvt18.destroyForcibly();
            list.stream().filter(ProcessHandle::isAlive).forEach(ProcessHandle::destroyForcibly);
        }
        finally {
            AccountVisitFileService.BrowserSession.deleteProfile(this.profile);
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static Optional<Path> findBrowser() {
        ArrayList<Path> arrayList = new ArrayList<Path>();
        String string = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
        if (string.contains("mac")) {
            for (String string2 : List.of("/Applications", Path.of(System.getProperty("user.home"), "Applications").toString())) {
                arrayList.add(Path.of(string2, "Firefox.app", "Contents", "MacOS", "firefox"));
            }
        } else if (string.contains("win")) {
            for (String string3 : List.of("ProgramFiles", "ProgramFiles(x86)", "LOCALAPPDATA")) {
                String string4 = System.getenv(string3);
                if (string4 == null || string4.isBlank()) continue;
                arrayList.add(Path.of(string4, "Mozilla Firefox", "firefox.exe"));
            }
        } else {
            for (String string5 : List.of("/usr/bin/firefox", "/usr/bin/firefox-esr", "/snap/bin/firefox")) {
                arrayList.add(Path.of(string5, new String[0]));
            }
        }
        return arrayList.stream().filter(path -> (Files.isRegularFile(path, new LinkOption[0]) && Files.isExecutable(path) ? 1 : 0) != 0).findFirst();
    }

    private static final class Connection
    implements WebSocket.Listener {
        final AtomicInteger sequence = new AtomicInteger();
        final Map<Integer, CompletableFuture<JsonObject>> pending = new ConcurrentHashMap<Integer, CompletableFuture<JsonObject>>();
        final AtomicBoolean disconnected = new AtomicBoolean();
        private final StringBuilder ft2yvmaluvl = new StringBuilder();

        private Connection() {
        }

        @Override
        public void onOpen(WebSocket webSocket) {
            webSocket.request(1L);
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence charSequence, boolean bl) {
            try {
                if (this.ft2yvmaluvl.length() + charSequence.length() > 0x200000) {
                    throw CookieBrowserLoginFlow.failure();
                }
                this.ft2yvmaluvl.append(charSequence);
                if (bl) {
                    CompletableFuture<JsonObject> completableFuture;
                    JsonObject jsonObject = JsonParser.parseString((String)this.ft2yvmaluvl.toString()).getAsJsonObject();
                    this.ft2yvmaluvl.setLength(0);
                    if (jsonObject.has("id") && (completableFuture = this.pending.remove(jsonObject.get("id").getAsInt())) != null) {
                        if (jsonObject.has("error")) {
                            completableFuture.completeExceptionally(CookieBrowserLoginFlow.failure());
                        } else {
                            completableFuture.complete(jsonObject.getAsJsonObject("result"));
                        }
                    }
                }
            }
            catch (RuntimeException runtimeException) {
                this.m427l2hccahf();
                webSocket.abort();
            }
            webSocket.request(1L);
            return null;
        }

        private void m427l2hccahf() {
            this.disconnected.set(true);
            this.pending.values().forEach(completableFuture -> completableFuture.completeExceptionally(CookieBrowserLoginFlow.closed()));
            this.pending.clear();
        }

        @Override
        public CompletionStage<?> onClose(WebSocket webSocket, int n, String string) {
            this.m427l2hccahf();
            return null;
        }

        @Override
        public void onError(WebSocket webSocket, Throwable throwable) {
            this.m427l2hccahf();
        }
    }
}
