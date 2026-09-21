package dev.felix.ellice.feature.terrain;

import dev.felix.ellice.compat.CompatLoadedHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class TerrainStateController implements AutoCloseable {
  public static final long DEFAULT_BUDGET = 201326592L;
  private static final int count = 8192;
  private final CompatLoadedHandler compatLoadedHandler;
  private final ThreadPoolExecutor executor;
  private final long fcpynarpw8ap;
  private final Map<TerrainData, TerrainLayerData> entries = new LinkedHashMap<>();
  private final Map<TerrainData, Long> f5ji0zw9x2r = new HashMap<>();
  private final Set<TerrainData> fepldkz1gmov = new HashSet<>();
  private final Set<TerrainData> values3 = new HashSet<>();
  private final Set<Long> values4 = new LinkedHashSet<>();
  private final ConcurrentLinkedQueue<TerrainStateController.Result> concurrentLinkedQueue =
      new ConcurrentLinkedQueue<>();
  private final Set<TerrainData> values5 = ConcurrentHashMap.newKeySet();
  private final Set<TerrainData> fifqgtvzmc7n = new HashSet<>();
  private final Set<TerrainData> fewxafny3vwk = new HashSet<>();
  private long timestamp2;
  private long timestamp3;
  private long timestamp4;
  private volatile boolean enabled;
  private int fh9ohoi5sb4 = Integer.MIN_VALUE;
  private int count3 = Integer.MIN_VALUE;
  private double value;
  private double value2;
  private String text = "";

  public TerrainStateController(CompatLoadedHandler var1) {
    this(var1, 201326592L);
  }

  public TerrainStateController(CompatLoadedHandler var1, long var2) {
    this.compatLoadedHandler = Objects.requireNonNull(var1);
    if (var2 <= 0L) {
      throw new IllegalArgumentException("A positive cache budget is required");
    }

    this.fcpynarpw8ap = var2;
    this.executor =
        new ThreadPoolExecutor(
            1,
            1,
            0L,
            TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(2),
            var0 -> {
              Thread var1x = new Thread(var0, "ellice terrain builder");
              var1x.setDaemon(true);
              var1x.setPriority(Math.max(1, 4));
              return var1x;
            });
  }

  public void invalidate(TerrainData var1) {
    if (!this.enabled && this.values5.size() < 16384) {
      this.values5.add(var1);
    }
  }

  public void update(List<TerrainData> var1, double var2, double var4) {
    if (!this.enabled) {
      int var6 = Math.floorDiv((int) Math.floor(var2), 16);
      int var7 = Math.floorDiv((int) Math.floor(var4), 16);
      if (var6 != this.fh9ohoi5sb4 || var7 != this.count3) {
        this.fifqgtvzmc7n.clear();
        this.fh9ohoi5sb4 = var6;
        this.count3 = var7;
      }

      HashSet var8 = new HashSet(var1);

      for (TerrainData var10 : Set.copyOf(this.values5)) {
        this.values5.remove(var10);
        if (this.entries.containsKey(var10)
            || this.fepldkz1gmov.contains(var10)
            || var8.contains(var10)) {
          this.values3.add(var10);
          this.f5ji0zw9x2r.merge(var10, 1L, Long::sum);
          this.fewxafny3vwk.remove(var10);
        }
      }

      TerrainStateController.Result var22;
      while ((var22 = this.concurrentLinkedQueue.poll()) != null) {
        if (var22.generation == this.timestamp2) {
          this.fepldkz1gmov.remove(var22.key);
          if (var22.version == this.f5ji0zw9x2r.getOrDefault(var22.key, 0L)) {
            if (var22.error != null) {
              this.fewxafny3vwk.add(var22.key);
              this.text = var22.error.getClass().getSimpleName() + ": " + var22.error.getMessage();
            } else {
              TerrainLayerData var23 = this.entries.put(var22.key, var22.mesh);
              this.timestamp3 =
                  this.timestamp3 + (var22.mesh.bytes() - (var23 == null ? 0L : var23.bytes()));
              this.values4.add(var22.key.column());
              this.values3.remove(var22.key);
              this.value = var22.millis;
              this.timestamp4++;
            }
          }
        }
      }

      for (TerrainData var11 : List.copyOf(this.values3)) {
        if (!var8.contains(var11)) {
          TerrainLayerData var12 = this.entries.remove(var11);
          if (var12 != null) {
            this.timestamp3 = this.timestamp3 - var12.bytes();
            this.values4.add(var11.column());
          }

          this.values3.remove(var11);
        }
      }

      long var25 = System.nanoTime();
      int var26 = 0;

      for (TerrainData var14 : var1) {
        if (this.fepldkz1gmov.size() >= 2 || var26 >= 2 || System.nanoTime() - var25 > 1500000L) {
          break;
        }

        if (!this.fepldkz1gmov.contains(var14)
            && !this.fewxafny3vwk.contains(var14)
            && !this.fifqgtvzmc7n.contains(var14)
            && (!this.entries.containsKey(var14) || this.values3.contains(var14))) {
          long var15 = this.timestamp2;
          long var17 = this.f5ji0zw9x2r.getOrDefault(var14, 0L);

          CompatLoadedHandler.BuildJob var19;
          try {
            var19 = this.compatLoadedHandler.snapshot(var14);
          } catch (RuntimeException var21) {
            this.fewxafny3vwk.add(var14);
            this.text = var21.getClass().getSimpleName() + ": " + var21.getMessage();
            continue;
          }

          if (var19 != null) {
            this.fepldkz1gmov.add(var14);
            var26++;
            this.executor.execute(
                () -> {
                  long var7x = System.nanoTime();
                  TerrainLayerData var9 = null;
                  Exception var10x = null;

                  try {
                    var9 = Objects.requireNonNull(var19.build());
                  } catch (Exception var12x) {
                    var10x = var12x;
                  }

                  if (!this.enabled) {
                    this.concurrentLinkedQueue.offer(
                        new TerrainStateController.Result(
                            var15,
                            var17,
                            var14,
                            var9,
                            (System.nanoTime() - var7x)
                                / Double.longBitsToDouble(4696837146684686336L),
                            var10x));
                  }
                });
          }
        }
      }

      this.value2 = (System.nanoTime() - var25) / Double.longBitsToDouble(4696837146684686336L);
      this.updateState(var2, var4);
    }
  }

  private void updateState(double var1, double var3) {
    if (this.timestamp3 > this.fcpynarpw8ap || this.entries.size() > 8192) {
      ArrayList var5 = new ArrayList<>(this.entries.keySet());
      var5.sort(
          Comparator.<TerrainData>comparingDouble(
                  var4 -> {
                    double var5x = var4.blockX() - var1;
                    double var7x = var4.blockZ() - var3;
                    return var5x * var5x + var7x * var7x;
                  })
              .reversed());

      for (TerrainData var7 : (Iterable<TerrainData>) (Iterable<?>) (var5)) {
        if (this.timestamp3 <= this.fcpynarpw8ap && this.entries.size() <= 8192) {
          break;
        }

        this.timestamp3 = this.timestamp3 - this.entries.remove(var7).bytes();
        this.values4.add(var7.column());
        this.fifqgtvzmc7n.add(var7);
        this.f5ji0zw9x2r.remove(var7);
      }
    }
  }

  public Set<Long> takeChangedColumns() {
    LinkedHashSet var1 = new LinkedHashSet<>(this.values4);
    this.values4.clear();
    return var1;
  }

  public List<TerrainLayerData> column(long var1) {
    return this.entries.values().stream()
        .filter(var2 -> var2.key().column() == var1)
        .sorted(Comparator.comparingInt(var0 -> var0.key().y()))
        .toList();
  }

  public Iterable<TerrainLayerData> meshes() {
    return Collections.unmodifiableCollection(this.entries.values());
  }

  public long bytes() {
    return this.timestamp3;
  }

  public int size() {
    return this.entries.size();
  }

  public int pending() {
    return this.fepldkz1gmov.size();
  }

  public long builds() {
    return this.timestamp4;
  }

  public double buildMillis() {
    return this.value;
  }

  public double snapshotMillis() {
    return this.value2;
  }

  public String lastError() {
    return this.text;
  }

  public void reset() {
    this.timestamp2++;
    this.concurrentLinkedQueue.clear();
    this.executor.getQueue().clear();

    for (TerrainData var2 : this.entries.keySet()) {
      this.values4.add(var2.column());
    }

    this.entries.clear();
    this.f5ji0zw9x2r.clear();
    this.fepldkz1gmov.clear();
    this.values3.clear();
    this.fewxafny3vwk.clear();
    this.values5.clear();
    this.fifqgtvzmc7n.clear();
    this.timestamp3 = 0L;
    this.text = "";
  }

  @Override
  public void close() {
    if (!this.enabled) {
      this.enabled = true;
      this.reset();
      this.executor.execute(this.compatLoadedHandler::closeWorker);
      this.executor.shutdown();
    }
  }

  private record Result(
      long generation,
      long version,
      TerrainData key,
      TerrainLayerData mesh,
      double millis,
      Exception error) {}
}
