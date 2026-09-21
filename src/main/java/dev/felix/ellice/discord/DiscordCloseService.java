







package dev.felix.ellice.discord;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.felix.ellice.discord.DiscordStateController;
import java.io.EOFException;
import java.io.IOException;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ByteChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

final class DiscordCloseService
implements AutoCloseable {
    private static final int fflsccciq7ci = 0;
    private static final int fb5y1f7okvk7 = 1;
    private static final int fcigs23he0hm = 2;
    private static final int fcw1cqjbu6k7 = 3;
    private static final int f4dardpp5lpt = 4;
    private static final int ff7m3j64jotq = 0x100000;
    private final ByteChannel ff0txogp600c;
    private final Object fg913lj2wpjm = new Object();
    private final AtomicBoolean fhsz4bcc5fjq = new AtomicBoolean(true);

    private DiscordCloseService(ByteChannel byteChannel, String string) throws IOException {
        this.ff0txogp600c = byteChannel;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("v", (Number)1);
        jsonObject.addProperty("client_id", string);
        this.mfho0f0rspqa(0, jsonObject.toString());
        Frame frame = this.m8elpfznzs5g();
        if (frame.opcode() != 1 || !"READY".equals(DiscordCloseService.mfmwhpac2vl5(frame.payload()))) {
            this.close();
            throw new IOException("Discord rejected the RPC handshake");
        }
        Thread.ofVirtual().name("ellice-discord-rpc-reader").start(this::magws8rrrtj8);
    }

    static DiscordCloseService connect(String string) throws IOException {
        IOException iOException = null;
        for (int i = 0; i < 10; ++i) {
            for (Path path : DiscordCloseService.endpoints(i)) {
                try {
                    ByteChannel byteChannel = DiscordCloseService.m8zq5zipriru(path);
                    try {
                        return new DiscordCloseService(byteChannel, string);
                    }
                    catch (IOException iOException2) {
                        try {
                            byteChannel.close();
                        }
                        catch (IOException iOException3) {
                            
                        }
                        throw iOException2;
                    }
                }
                catch (IOException iOException4) {
                    iOException = iOException4;
                }
            }
        }
        throw new IOException("Discord desktop IPC socket is unavailable", iOException);
    }

    boolean isOpen() {
        return (this.fhsz4bcc5fjq.get() && this.ff0txogp600c.isOpen() ? 1 : 0) != 0;
    }

    void setActivity(DiscordStateController.Activity activity) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("pid", (Number)ProcessHandle.current().pid());
        if (activity == null) {
            jsonObject.add("activity", null);
        } else {
            jsonObject.add("activity", (JsonElement)activity.json());
        }
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("cmd", "SET_ACTIVITY");
        jsonObject2.add("args", (JsonElement)jsonObject);
        jsonObject2.addProperty("nonce", UUID.randomUUID().toString());
        this.mfho0f0rspqa(1, jsonObject2.toString());
    }

    private void magws8rrrtj8() {
        try {
            while (this.isOpen()) {
                Frame frame = this.m8elpfznzs5g();
                if (frame.opcode() == 3) {
                    this.mfho0f0rspqa(4, frame.payload());
                    continue;
                }
                if (frame.opcode() != 2) continue;
                break;
            }
        }
        catch (IOException iOException) {
        }
        finally {
            this.close();
        }
    }

    


    private void mfho0f0rspqa(int n, String string) throws IOException {
        byte[] byArray = string.getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteBuffer = ByteBuffer.allocate(8 + byArray.length).order(ByteOrder.LITTLE_ENDIAN).putInt(n).putInt(byArray.length).put(byArray).flip();
        Object object = this.fg913lj2wpjm;
        synchronized (object) {
            while (byteBuffer.hasRemaining()) {
                this.ff0txogp600c.write(byteBuffer);
            }
        }
    }

    private Frame m8elpfznzs5g() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
        this.miel28xg6nll(byteBuffer);
        byteBuffer.flip();
        int n = byteBuffer.getInt();
        int n2 = byteBuffer.getInt();
        if (n2 < 0 || n2 > 0x100000) {
            throw new IOException("Invalid Discord RPC payload length: " + n2);
        }
        ByteBuffer byteBuffer2 = ByteBuffer.allocate(n2);
        this.miel28xg6nll(byteBuffer2);
        byteBuffer2.flip();
        return new Frame(n, StandardCharsets.UTF_8.decode(byteBuffer2).toString());
    }

    private void miel28xg6nll(ByteBuffer byteBuffer) throws IOException {
        while (byteBuffer.hasRemaining()) {
            if (this.ff0txogp600c.read(byteBuffer) >= 0) continue;
            throw new EOFException("Discord closed RPC");
        }
    }

    private static String mfmwhpac2vl5(String string) {
        try {
            return JsonParser.parseString((String)string).getAsJsonObject().get("evt").getAsString();
        }
        catch (RuntimeException runtimeException) {
            return "";
        }
    }

    private static ByteChannel m8zq5zipriru(Path path) throws IOException {
        if (System.getProperty("os.name", "").toLowerCase(Locale.ROOT).contains("win")) {
            return FileChannel.open(path, StandardOpenOption.READ, StandardOpenOption.WRITE);
        }
        if (!Files.exists(path, new LinkOption[0])) {
            throw new IOException("Missing IPC socket: " + String.valueOf(path));
        }
        SocketChannel socketChannel = SocketChannel.open(StandardProtocolFamily.UNIX);
        try {
            socketChannel.connect(UnixDomainSocketAddress.of(path));
            return socketChannel;
        }
        catch (IOException iOException) {
            socketChannel.close();
            throw iOException;
        }
    }

    static List<Path> endpoints(int n) {
        if (System.getProperty("os.name", "").toLowerCase(Locale.ROOT).contains("win")) {
            return List.of(Path.of("\\\\.\\pipe\\discord-ipc-" + n, new String[0]));
        }
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<String>();
        DiscordCloseService.mh2jhz5uz1x7(linkedHashSet, System.getenv("XDG_RUNTIME_DIR"));
        DiscordCloseService.mh2jhz5uz1x7(linkedHashSet, System.getenv("TMPDIR"));
        DiscordCloseService.mh2jhz5uz1x7(linkedHashSet, System.getenv("TMP"));
        DiscordCloseService.mh2jhz5uz1x7(linkedHashSet, System.getenv("TEMP"));
        DiscordCloseService.mh2jhz5uz1x7(linkedHashSet, "/tmp");
        ArrayList<Path> arrayList = new ArrayList<Path>();
        for (String string : linkedHashSet) {
            arrayList.add(Path.of(string, new String[0]).resolve("discord-ipc-" + n));
        }
        return arrayList;
    }

    private static void mh2jhz5uz1x7(Set<String> set, String string) {
        if (string != null && !string.isBlank()) {
            set.add(string);
        }
    }

    @Override
    public void close() {
        if (!this.fhsz4bcc5fjq.getAndSet(false)) {
            return;
        }
        try {
            this.ff0txogp600c.close();
        }
        catch (IOException iOException) {
            
        }
    }

    private record Frame(int opcode, String payload) {
    }
}

