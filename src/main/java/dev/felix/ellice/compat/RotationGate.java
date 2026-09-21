package dev.felix.ellice.compat;

@FunctionalInterface
public interface RotationGate {
   boolean allow(float value, float currentValue);
}
