package dev.felix.ellice.render.render3d;

import dev.felix.ellice.render.render3d.geometry.GeometryXService;
import dev.felix.ellice.render.render3d.lighting.LightingData;
import dev.felix.ellice.render.render3d.lighting.DirectionalLight;
import dev.felix.ellice.render.render3d.material.MaterialErFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import org.joml.Vector3f;

public final class Render3dAddService {
   private final AtomicLong atomicLong = new AtomicLong(1L);
   private final ConcurrentHashMap<Long, Render3dIdService> entries = new ConcurrentHashMap<>();
   private final CopyOnWriteArrayList<LightingData> items = new CopyOnWriteArrayList<>();
   private volatile DirectionalLight lightingData2 = DirectionalLight.daylight();
   private volatile Vector3f vector3f = new Vector3f(
      0.42F, 0.48F, 0.62F
   );
   private volatile float value = 0.16F;

   public Render3dIdService add(GpuMesh gpuMesh, MaterialErFactory materialEr, GeometryXService geometryX) {
      Render3dIdService render3dId = new Render3dIdService(this.atomicLong.getAndIncrement(), gpuMesh, materialEr, geometryX);
      this.entries.put(render3dId.id(), render3dId);
      return render3dId;
   }

   public Render3dIdService add(GpuMesh gpuMesh, MaterialErFactory materialEr) {
      return this.add(gpuMesh, materialEr, GeometryXService.IDENTITY);
   }

   public boolean remove(Render3dIdService render3dId) {
      return render3dId != null && this.entries.remove(render3dId.id(), render3dId);
   }

   public boolean remove(long longValue) {
      return this.entries.remove(longValue) != null;
   }

   public void clearObjects() {
      this.entries.clear();
   }

   public int objectCount() {
      return this.entries.size();
   }

   List<Render3dIdService> snapshotObjects() {
      return new ArrayList<>(this.entries.values());
   }

   public DirectionalLight sun() {
      return this.lightingData2;
   }

   public Render3dAddService sun(DirectionalLight directionalLight) {
      if (directionalLight == null) {
         throw new IllegalArgumentException("sun is required");
      }

      this.lightingData2 = directionalLight;
      return this;
   }

   public Vector3f ambientColor() {
      return new Vector3f(this.vector3f);
   }

   public float ambientIntensity() {
      return this.value;
   }

   public Render3dAddService ambient(Vector3f currentVector3f, float currentValue) {
      if (currentVector3f != null && Float.isFinite(currentVector3f.x) && Float.isFinite(currentVector3f.y) && Float.isFinite(currentVector3f.z) && Float.isFinite(currentValue)) {
         this.vector3f = new Vector3f(currentVector3f);
         this.value = Math.max(0.0F, currentValue);
         return this;
      } else {
         throw new IllegalArgumentException("Ambient color and intensity must be finite");
      }
   }

   public Render3dAddService addLight(LightingData lightingData) {
      if (lightingData != null) {
         this.items.addIfAbsent(lightingData);
      }

      return this;
   }

   public boolean removeLight(LightingData lightingData) {
      return this.items.remove(lightingData);
   }

   public void clearLights() {
      this.items.clear();
   }

   List<LightingData> snapshotLights() {
      return List.copyOf(this.items);
   }
}
