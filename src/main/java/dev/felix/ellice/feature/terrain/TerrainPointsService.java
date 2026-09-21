






package dev.felix.ellice.feature.terrain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.felix.ellice.feature.terrain.TerrainCanonicalService;
import dev.felix.ellice.feature.terrain.TerrainKindData;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;

public final class TerrainPointsService {
    private static final Gson fdmbsz7jwzg6 = new GsonBuilder().setPrettyPrinting().create();
    private final Path f85cuoodiyvn;
    private final List<TerrainKindData> fdgug1chiz44 = new ArrayList<TerrainKindData>();

    public TerrainPointsService(Path path, String string) throws IOException {
        Object object;
        try {
            object = HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(string.getBytes(StandardCharsets.UTF_8)));
            this.f85cuoodiyvn = path.resolve("ellice-maps").resolve((String)object).resolve("waypoints.json");
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new IllegalStateException(noSuchAlgorithmException);
        }
        if (Files.exists(this.f85cuoodiyvn, new LinkOption[0])) {
            if (Files.size(this.f85cuoodiyvn) > 0x200000L) {
                throw new IOException("Waypoint file is too large");
            }
            try {
                object = (Saved)fdmbsz7jwzg6.fromJson(Files.readString(this.f85cuoodiyvn), Saved.class);
                if (object == null || ((Saved)object).version < 1 || ((Saved)object).version > 2 || ((Saved)object).waypoints == null) {
                    throw new IOException("Unsupported waypoint file");
                }
                for (TerrainKindData terrainKindData : ((Saved)object).waypoints) {
                    if (terrainKindData == null || this.fdgug1chiz44.size() >= 1000 || !this.fdgug1chiz44.stream().noneMatch(terrainKindData2 -> terrainKindData2.id().equals(terrainKindData.id()))) continue;
                    this.fdgug1chiz44.add(terrainKindData);
                }
            }
            catch (RuntimeException runtimeException) {
                throw new IOException("Could not read waypoints", runtimeException);
            }
        }
    }

    public List<TerrainKindData> points(String string) {
        return this.fdgug1chiz44.stream().filter(terrainKindData -> terrainKindData.dimension().equals(TerrainCanonicalService.canonical(string))).toList();
    }

    public List<TerrainKindData> all() {
        return List.copyOf(this.fdgug1chiz44);
    }

    public void put(TerrainKindData terrainKindData) throws IOException {
        ArrayList<TerrainKindData> arrayList = new ArrayList<TerrainKindData>(this.fdgug1chiz44);
        arrayList.removeIf(terrainKindData2 -> terrainKindData2.id().equals(terrainKindData.id()));
        if (arrayList.size() >= 1000) {
            throw new IOException("Waypoint limit reached");
        }
        arrayList.add(terrainKindData);
        this.mduqocgbjbfs(arrayList);
    }

    public void remove(UUID uUID) throws IOException {
        ArrayList<TerrainKindData> arrayList = new ArrayList<TerrainKindData>(this.fdgug1chiz44);
        arrayList.removeIf(terrainKindData -> terrainKindData.id().equals(uUID));
        this.mduqocgbjbfs(arrayList);
    }

    


    private void mduqocgbjbfs(List<TerrainKindData> list) throws IOException {
        Files.createDirectories(this.f85cuoodiyvn.getParent(), new FileAttribute[0]);
        Path path = Files.createTempFile(this.f85cuoodiyvn.getParent(), "waypoints-", ".tmp", new FileAttribute[0]);
        try {
            Files.writeString(path, (CharSequence)(fdmbsz7jwzg6.toJson((Object)new Saved(2, list)) + "\n"), new OpenOption[0]);
            try {
                Files.move(path, this.f85cuoodiyvn, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            }
            catch (AtomicMoveNotSupportedException atomicMoveNotSupportedException) {
                Files.move(path, this.f85cuoodiyvn, StandardCopyOption.REPLACE_EXISTING);
            }
            this.fdgug1chiz44.clear();
            this.fdgug1chiz44.addAll(list);
        }
        finally {
            Files.deleteIfExists(path);
        }
    }

    private record Saved(int version, List<TerrainKindData> waypoints) {
    }
}

