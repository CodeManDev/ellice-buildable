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
   private static final int fhzxihqhun84 = 8192;
   private final CompatLoadedHandler f3o6r4j1q23w;
   private final ThreadPoolExecutor fatzplw596qf;
   private final long fcpynarpw8ap;
   private final Map<TerrainData, TerrainLayerData> f6e6t8d9qq7n = new LinkedHashMap<>();
   private final Map<TerrainData, Long> f5ji0zw9x2r = new HashMap<>();
   private final Set<TerrainData> fepldkz1gmov = new HashSet<>();
   private final Set<TerrainData> fflsrr1eczl1 = new HashSet<>();
   private final Set<Long> f9s2z73rqwdv = new LinkedHashSet<>();
   private final ConcurrentLinkedQueue<TerrainStateController.Result> faowg1y7b6eg = new ConcurrentLinkedQueue<>();
   private final Set<TerrainData> fc6jfhm0t0t4 = ConcurrentHashMap.newKeySet();
   private final Set<TerrainData> fifqgtvzmc7n = new HashSet<>();
   private final Set<TerrainData> fewxafny3vwk = new HashSet<>();
   private long f9edqzu3ud8s;
   private long f1p89hnowgt1;
   private long f3wzc360iuyo;
   private volatile boolean fgb72yaqzba7;
   private int fh9ohoi5sb4 = Integer.MIN_VALUE;
   private int f232r7mp5421 = Integer.MIN_VALUE;
   private double f38a5tvwc4ou;
   private double fhcn7samjr3o;
   private String f2k68rjmcitp = "";

   public TerrainStateController(CompatLoadedHandler var1) {
      this(var1, 201326592L);
   }

   public TerrainStateController(CompatLoadedHandler var1, long var2) {
      this.f3o6r4j1q23w = Objects.requireNonNull(var1);
      if (var2 <= 0L) {
         throw new IllegalArgumentException("A positive cache budget is required");
      }

      this.fcpynarpw8ap = var2;
      this.fatzplw596qf = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(2), var0 -> {
         Thread var1x = new Thread(var0, "ellice terrain builder");
         var1x.setDaemon(true);
         var1x.setPriority(Math.max(1, 4));
         return var1x;
      });
   }

   public void invalidate(TerrainData var1) {
      if (!this.fgb72yaqzba7 && this.fc6jfhm0t0t4.size() < 16384) {
         this.fc6jfhm0t0t4.add(var1);
      }
   }

   public void update(List<TerrainData> var1, double var2, double var4) {
      if (!this.fgb72yaqzba7) {
         int var6 = Math.floorDiv((int)Math.floor(var2), 16);
         int var7 = Math.floorDiv((int)Math.floor(var4), 16);
         if (var6 != this.fh9ohoi5sb4 || var7 != this.f232r7mp5421) {
            this.fifqgtvzmc7n.clear();
            this.fh9ohoi5sb4 = var6;
            this.f232r7mp5421 = var7;
         }

         HashSet var8 = new HashSet(var1);

         for (TerrainData var10 : Set.copyOf(this.fc6jfhm0t0t4)) {
            this.fc6jfhm0t0t4.remove(var10);
            if (this.f6e6t8d9qq7n.containsKey(var10) || this.fepldkz1gmov.contains(var10) || var8.contains(var10)) {
               this.fflsrr1eczl1.add(var10);
               this.f5ji0zw9x2r.merge(var10, 1L, Long::sum);
               this.fewxafny3vwk.remove(var10);
            }
         }

         TerrainStateController.Result var22;
         while ((var22 = this.faowg1y7b6eg.poll()) != null) {
            if (var22.generation == this.f9edqzu3ud8s) {
               this.fepldkz1gmov.remove(var22.key);
               if (var22.version == this.f5ji0zw9x2r.getOrDefault(var22.key, 0L)) {
                  if (var22.error != null) {
                     this.fewxafny3vwk.add(var22.key);
                     this.f2k68rjmcitp = var22.error.getClass().getSimpleName() + ": " + var22.error.getMessage();
                  } else {
                     TerrainLayerData var23 = this.f6e6t8d9qq7n.put(var22.key, var22.mesh);
                     this.f1p89hnowgt1 = this.f1p89hnowgt1 + (var22.mesh.bytes() - (var23 == null ? 0L : var23.bytes()));
                     this.f9s2z73rqwdv.add(var22.key.column());
                     this.fflsrr1eczl1.remove(var22.key);
                     this.f38a5tvwc4ou = var22.millis;
                     this.f3wzc360iuyo++;
                  }
               }
            }
         }

         for (TerrainData var11 : List.copyOf(this.fflsrr1eczl1)) {
            if (!var8.contains(var11)) {
               TerrainLayerData var12 = this.f6e6t8d9qq7n.remove(var11);
               if (var12 != null) {
                  this.f1p89hnowgt1 = this.f1p89hnowgt1 - var12.bytes();
                  this.f9s2z73rqwdv.add(var11.column());
               }

               this.fflsrr1eczl1.remove(var11);
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
               && (!this.f6e6t8d9qq7n.containsKey(var14) || this.fflsrr1eczl1.contains(var14))) {
               long var15 = this.f9edqzu3ud8s;
               long var17 = this.f5ji0zw9x2r.getOrDefault(var14, 0L);

               CompatLoadedHandler.BuildJob var19;
               try {
                  var19 = this.f3o6r4j1q23w.snapshot(var14);
               } catch (RuntimeException var21) {
                  this.fewxafny3vwk.add(var14);
                  this.f2k68rjmcitp = var21.getClass().getSimpleName() + ": " + var21.getMessage();
                  continue;
               }

               if (var19 != null) {
                  this.fepldkz1gmov.add(var14);
                  var26++;
                  this.fatzplw596qf
                     .execute(
                        () -> {
                           long var7x = System.nanoTime();
                           TerrainLayerData var9 = null;
                           Exception var10x = null;

                           try {
                              var9 = Objects.requireNonNull(var19.build());
                           } catch (Exception var12x) {
                              var10x = var12x;
                           }

                           if (!this.fgb72yaqzba7) {
                              this.faowg1y7b6eg
                                 .offer(
                                    new TerrainStateController.Result(
                                       var15, var17, var14, var9, (System.nanoTime() - var7x) / Double.longBitsToDouble(4696837146684686336L), var10x
                                    )
                                 );
                           }
                        }
                     );
               }
            }
         }

         this.fhcn7samjr3o = (System.nanoTime() - var25) / Double.longBitsToDouble(4696837146684686336L);
         this.mjlmdf5x9tqg(var2, var4);
      }
   }

   private void mjlmdf5x9tqg(double var1, double var3) {
      if (this.f1p89hnowgt1 > this.fcpynarpw8ap || this.f6e6t8d9qq7n.size() > 8192) {
         ArrayList var5 = new ArrayList<>(this.f6e6t8d9qq7n.keySet());
         var5.sort(Comparator.<TerrainData>comparingDouble(var4 -> {
            double var5x = var4.blockX() - var1;
            double var7x = var4.blockZ() - var3;
            return var5x * var5x + var7x * var7x;
         }).reversed());

         for (TerrainData var7 : (Iterable<TerrainData>) (Iterable<?>) (var5)) {
            if (this.f1p89hnowgt1 <= this.fcpynarpw8ap && this.f6e6t8d9qq7n.size() <= 8192) {
               break;
            }

            this.f1p89hnowgt1 = this.f1p89hnowgt1 - this.f6e6t8d9qq7n.remove(var7).bytes();
            this.f9s2z73rqwdv.add(var7.column());
            this.fifqgtvzmc7n.add(var7);
            this.f5ji0zw9x2r.remove(var7);
         }
      }
   }

   public Set<Long> takeChangedColumns() {
      LinkedHashSet var1 = new LinkedHashSet<>(this.f9s2z73rqwdv);
      this.f9s2z73rqwdv.clear();
      return var1;
   }

   public List<TerrainLayerData> column(long var1) {
      return this.f6e6t8d9qq7n.values().stream().filter(var2 -> var2.key().column() == var1).sorted(Comparator.comparingInt(var0 -> var0.key().y())).toList();
   }

   public Iterable<TerrainLayerData> meshes() {
      return Collections.unmodifiableCollection(this.f6e6t8d9qq7n.values());
   }

   public long bytes() {
      return this.f1p89hnowgt1;
   }

   public int size() {
      return this.f6e6t8d9qq7n.size();
   }

   public int pending() {
      return this.fepldkz1gmov.size();
   }

   public long builds() {
      return this.f3wzc360iuyo;
   }

   public double buildMillis() {
      return this.f38a5tvwc4ou;
   }

   public double snapshotMillis() {
      return this.fhcn7samjr3o;
   }

   public String lastError() {
      return this.f2k68rjmcitp;
   }

   public void reset() {
      this.f9edqzu3ud8s++;
      this.faowg1y7b6eg.clear();
      this.fatzplw596qf.getQueue().clear();

      for (TerrainData var2 : this.f6e6t8d9qq7n.keySet()) {
         this.f9s2z73rqwdv.add(var2.column());
      }

      this.f6e6t8d9qq7n.clear();
      this.f5ji0zw9x2r.clear();
      this.fepldkz1gmov.clear();
      this.fflsrr1eczl1.clear();
      this.fewxafny3vwk.clear();
      this.fc6jfhm0t0t4.clear();
      this.fifqgtvzmc7n.clear();
      this.f1p89hnowgt1 = 0L;
      this.f2k68rjmcitp = "";
   }

   @Override
   public void close() {
      if (!this.fgb72yaqzba7) {
         this.fgb72yaqzba7 = true;
         this.reset();
         this.fatzplw596qf.execute(this.f3o6r4j1q23w::closeWorker);
         this.fatzplw596qf.shutdown();
      }
   }

   private record Result(long generation, long version, TerrainData key, TerrainLayerData mesh, double millis, Exception error) {
   }
}
