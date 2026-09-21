package dev.felix.ellice.compat;

import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public record CompatData(Matrix4f view, Matrix4f projection, Vec3 cameraPos) {}
