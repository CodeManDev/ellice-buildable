package dev.felix.ellice.compat;

public interface CompatibilityProvider extends CompatOperationHandler {
   CompatibilityDescriptor metadata();

   @Override
   default CompatibilityCapabilities capabilities() {
      return this.metadata().capabilities();
   }
}
