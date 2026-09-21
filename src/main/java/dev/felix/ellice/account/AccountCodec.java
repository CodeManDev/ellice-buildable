







package dev.felix.ellice.account;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.felix.ellice.account.AccountCodeService;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpCookie;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public final class AccountCodec {
    public static final int MAX_IMPORT_BYTES = 0x100000;
    public static final int MAX_INLINE_LENGTH = 32768;
    private static final Set<String> f6w6hjvnpmdl = Set.of("__Host-MSAAUTH", "__Host-MSAAUTHP", "MSPAuth");
    private static final Set<String> f7zrut2jk913 = Set.of("live.com", "login.live.com", "account.live.com", "microsoft.com", "login.microsoft.com", "account.microsoft.com", "microsoftonline.com", "login.microsoftonline.com");
    private final List<HttpCookie> fgc470ylju1j;

    private AccountCodec(List<HttpCookie> list) {
        this.fgc470ylju1j = list;
    }

    public static AccountCodec read(Path path) throws IOException {
        try (InputStream inputStream = Files.newInputStream(path, new OpenOption[0]);){
            byte[] byArray = inputStream.readNBytes(0x100001);
            if (byArray.length > 0x100000) {
                throw AccountCodec.m2ujqdjryt1n("Choose a cookie export smaller than 1 MB.");
            }
            AccountCodec accountCodec = AccountCodec.parse(new String(byArray, StandardCharsets.UTF_8));
            return accountCodec;
        }
    }

    public static AccountCodec parse(String string) {
        if (string == null || string.isBlank()) {
            throw AccountCodec.m2ujqdjryt1n("Paste a Microsoft session cookie or choose its export file.");
        }
        if (string.length() > 0x100000) {
            throw AccountCodec.m2ujqdjryt1n("Choose a cookie export smaller than 1 MB.");
        }
        Object object = string.strip();
        if (((String)object).startsWith("\ufeff")) {
            object = ((String)object).substring(1).strip();
        }
        LinkedHashMap<String, HttpCookie> linkedHashMap = new LinkedHashMap<String, HttpCookie>();
        try {
            if (((String)object).startsWith("[") || ((String)object).startsWith("{")) {
                JsonElement jsonElement = JsonParser.parseString((String)object);
                if (jsonElement.isJsonObject() && jsonElement.getAsJsonObject().has("cookies")) {
                    jsonElement = jsonElement.getAsJsonObject().get("cookies");
                }
                if (jsonElement.isJsonArray()) {
                    for (JsonElement jsonElement2 : jsonElement.getAsJsonArray()) {
                        AccountCodec.mdoit8tbzw6t(jsonElement2.getAsJsonObject(), linkedHashMap);
                    }
                } else {
                    AccountCodec.mdoit8tbzw6t(jsonElement.getAsJsonObject(), linkedHashMap);
                }
            } else if (((String)object).indexOf(9) >= 0 || ((String)object).startsWith("#")) {
                for (String string2 : ((String)object).split("\\R")) {
                    boolean bl = string2.startsWith("#HttpOnly_");
                    if (bl) {
                        string2 = string2.substring(10);
                    } else if (string2.isBlank() || string2.startsWith("#")) continue;
                    String[] stringArray = string2.split("\t", 7);
                    if (stringArray.length != 7) {
                        throw AccountCodec.m2ujqdjryt1n("The Netscape cookie export is incomplete.");
                    }
                    String string3 = AccountCodec.mi1hkoepe6iv(stringArray[0]);
                    if (string3 == null) continue;
                    AccountCodec.ma8gmmrf05o1(linkedHashMap, stringArray[5], stringArray[6], AccountCodec.mb74ia0zkstd(stringArray[4]), AccountCodec.m9ibc6958ycd(string3, Boolean.parseBoolean(stringArray[1])), stringArray[2], Boolean.parseBoolean(stringArray[3]), bl);
                }
            } else {
                int n;
                int n2;
                if (((String)object).regionMatches(true, 0, "Cookie:", 0, 7)) {
                    object = ((String)object).substring(7).strip();
                }
                int n3 = n2 = (n = ((String)object).indexOf(61)) > 0 && f6w6hjvnpmdl.contains(((String)object).substring(0, n)) ? 1 : 0;
                if (!(((String)object).contains(";") || n2 != 0 || ((String)object).contains("=") && !((String)object).substring(((String)object).indexOf(61)).matches("=+"))) {
                    object = "__Host-MSAAUTH=" + (String)object;
                }
                for (String string4 : ((String)object).split(";")) {
                    int n4 = string4.indexOf(61);
                    if (n4 <= 0) {
                        throw AccountCodec.m2ujqdjryt1n("Use a cookie value or name=value, or choose an export file.");
                    }
                    AccountCodec.ma8gmmrf05o1(linkedHashMap, string4.substring(0, n4).strip(), string4.substring(n4 + 1).strip(), 0L, "login.live.com", "/", true, true);
                }
            }
        }
        catch (AccountCodeService accountCodeService) {
            throw accountCodeService;
        }
        catch (RuntimeException runtimeException) {
            throw AccountCodec.m2ujqdjryt1n("Couldn't read this cookie export. Use Netscape text or Cookie Quick Manager JSON.");
        }
        if (linkedHashMap.values().stream().noneMatch(httpCookie -> (f6w6hjvnpmdl.contains(httpCookie.getName()) && !"Disabled".equalsIgnoreCase(httpCookie.getValue()) ? 1 : 0) != 0)) {
            throw AccountCodec.m2ujqdjryt1n("No valid Microsoft session cookie found. Use __Host-MSAAUTH, __Host-MSAAUTHP or MSPAuth.");
        }
        return new AccountCodec(List.copyOf(linkedHashMap.values()));
    }

    private static void mdoit8tbzw6t(JsonObject jsonObject, Map<String, HttpCookie> map) {
        String string = AccountCodec.mi1hkoepe6iv(AccountCodec.miga3whbn377(jsonObject, "domain", "host", "Host raw"));
        if (string == null) {
            return;
        }
        boolean bl = AccountCodec.m5ihklzdfjqs(jsonObject, !string.startsWith("."), "hostOnly");
        String string2 = AccountCodec.miga3whbn377(jsonObject, "path", "Path raw");
        AccountCodec.ma8gmmrf05o1(map, AccountCodec.miga3whbn377(jsonObject, "name", "Name raw"), AccountCodec.miga3whbn377(jsonObject, "value", "Content raw"), AccountCodec.mb74ia0zkstd(AccountCodec.miga3whbn377(jsonObject, "expirationDate", "expires", "Expires raw")), AccountCodec.m9ibc6958ycd(string, !bl), string2.isEmpty() ? "/" : string2, AccountCodec.m5ihklzdfjqs(jsonObject, true, "secure", "isSecure", "Send for"), AccountCodec.m5ihklzdfjqs(jsonObject, true, "httpOnly", "isHttpOnly", "HTTP only"));
    }

    private static boolean m5ihklzdfjqs(JsonObject jsonObject, boolean bl, String ... stringArray) {
        for (String string : stringArray) {
            if (!jsonObject.has(string) || jsonObject.get(string).isJsonNull()) continue;
            String string2 = jsonObject.get(string).getAsString();
            if (string2.equalsIgnoreCase("true") || string2.equalsIgnoreCase("https")) {
                return true;
            }
            if (!string2.equalsIgnoreCase("false") && !string2.equalsIgnoreCase("any")) continue;
            return false;
        }
        return bl;
    }

    private static String miga3whbn377(JsonObject jsonObject, String ... stringArray) {
        for (String string : stringArray) {
            if (!jsonObject.has(string) || jsonObject.get(string).isJsonNull()) continue;
            return jsonObject.get(string).getAsString();
        }
        return "";
    }

    private static long mb74ia0zkstd(String string) {
        return string == null || string.isBlank() ? 0L : (long)Double.parseDouble(string);
    }

    private static String mi1hkoepe6iv(String string) {
        if (string == null) {
            return null;
        }
        String string2 = string.strip().toLowerCase(Locale.ROOT);
        if (string2.contains("://")) {
            string2 = URI.create(string2).getHost();
        }
        if (string2 == null) {
            return null;
        }
        if (f7zrut2jk913.contains(AccountCodec.m9ibc6958ycd(string2, false))) {
            return string2;
        }
        return null;
    }

    private static String m9ibc6958ycd(String string, boolean bl) {
        String string2 = string.startsWith(".") ? string.substring(1) : string;
        return bl ? "." + string2 : string2;
    }

    static URI origin(HttpCookie httpCookie) {
        return URI.create("https://" + AccountCodec.m9ibc6958ycd(httpCookie.getDomain(), false) + "/");
    }

    private static void ma8gmmrf05o1(Map<String, HttpCookie> map, String string, String string2, long l, String string3, String string4, boolean bl, boolean bl2) {
        if (string2.isEmpty() || l > 0L && l <= Instant.now().getEpochSecond()) {
            return;
        }
        if (!string.matches("[A-Za-z0-9_.$-]{1,128}") || string2.length() > 32768 || string2.chars().anyMatch(n -> (n < 33 || n > 126 || n == 59 || n == 34 || n == 44 || n == 92 ? 1 : 0) != 0)) {
            throw AccountCodec.m2ujqdjryt1n("The cookie contains invalid characters. Import the original export file instead.");
        }
        if (!string4.startsWith("/") || string4.chars().anyMatch(n -> (n < 32 || n > 126 ? 1 : 0) != 0)) {
            throw AccountCodec.m2ujqdjryt1n("The cookie export contains an invalid path.");
        }
        if (string.startsWith("__Host-") && (string3.startsWith(".") || !string4.equals("/") || !bl) || string.startsWith("__Secure-") && !bl) {
            throw AccountCodec.m2ujqdjryt1n("The cookie export contains an invalid secure cookie scope.");
        }
        String string5 = string + "\n" + AccountCodec.m9ibc6958ycd(string3, false) + "\n" + string4;
        HttpCookie httpCookie = map.get(string5);
        if (httpCookie != null && !httpCookie.getValue().equals(string2)) {
            throw AccountCodec.m2ujqdjryt1n("This export contains multiple sessions. Export cookies for one Microsoft account.");
        }
        if (map.size() >= 64 && httpCookie == null) {
            throw AccountCodec.m2ujqdjryt1n("Too many Microsoft cookies in this export.");
        }
        HttpCookie httpCookie2 = new HttpCookie(string, string2);
        httpCookie2.setVersion(0);
        httpCookie2.setPath(string4);
        httpCookie2.setSecure(bl);
        httpCookie2.setHttpOnly(bl2);
        httpCookie2.setDomain(string3);
        if (l > 0L) {
            httpCookie2.setMaxAge(Math.max(0L, l - Instant.now().getEpochSecond()));
        }
        map.put(string5, httpCookie2);
    }

    List<HttpCookie> cookies() {
        return this.fgc470ylju1j.stream().map(httpCookie -> (HttpCookie)httpCookie.clone()).toList();
    }

    static AccountCodec fromCookieJar(List<HttpCookie> list) {
        return new AccountCodec(list.stream().filter(httpCookie -> (!httpCookie.hasExpired() && AccountCodec.mi1hkoepe6iv(httpCookie.getDomain()) != null ? 1 : 0) != 0).map(httpCookie -> (HttpCookie)httpCookie.clone()).toList());
    }

    public int size() {
        return this.fgc470ylju1j.size();
    }

    public String toString() {
        return "MicrosoftSessionCookies[redacted]";
    }

    private static AccountCodeService m2ujqdjryt1n(String string) {
        return new AccountCodeService("COOKIE_INVALID", string);
    }
}

