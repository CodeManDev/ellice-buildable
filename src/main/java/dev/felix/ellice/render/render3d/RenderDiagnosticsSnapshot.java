package dev.felix.ellice.render.render3d;

public record RenderDiagnosticsSnapshot(
   String status,
   String effects,
   int selected,
   int captured,
   int vertices,
   int regions,
   int width,
   int height,
   boolean terrainDepth,
   boolean handProtection
) {
   public static final RenderDiagnosticsSnapshot IDLE = new RenderDiagnosticsSnapshot("Idle", "None", 0, 0, 0, 0, 0, 0, false, false);
}
