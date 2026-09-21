package dev.felix.ellice.render.render3d;

import dev.felix.ellice.render.render3d.geometry.GeometryXService;
import dev.felix.ellice.render.render3d.material.MaterialErFactory;
import java.util.Objects;

public final class Render3dIdService {
  private final long timestamp;
  private final GpuMesh renderer;
  private final MaterialErFactory materialErFactory;
  private volatile GeometryXService geometryXService;
  private volatile boolean enabled = true;

  Render3dIdService(
      long longValue, GpuMesh gpuMesh, MaterialErFactory materialEr, GeometryXService geometryX) {
    this.timestamp = longValue;
    this.renderer = Objects.requireNonNull(gpuMesh, "mesh");
    this.materialErFactory = Objects.requireNonNull(materialEr, "material");
    this.geometryXService = Objects.requireNonNull(geometryX, "transform");
  }

  public long id() {
    return this.timestamp;
  }

  public GpuMesh mesh() {
    return this.renderer;
  }

  public MaterialErFactory material() {
    return this.materialErFactory;
  }

  public GeometryXService transform() {
    return this.geometryXService;
  }

  public boolean visible() {
    return this.enabled;
  }

  public Render3dIdService transform(GeometryXService geometryX) {
    this.geometryXService = Objects.requireNonNull(geometryX, "transform");
    return this;
  }

  public Render3dIdService visible(boolean currentVisible) {
    this.enabled = currentVisible;
    return this;
  }
}
