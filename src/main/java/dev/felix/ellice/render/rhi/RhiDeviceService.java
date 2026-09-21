package dev.felix.ellice.render.rhi;

import dev.felix.ellice.render.rhi.gl.GlShaderFactory;

public final class RhiDeviceService {
   private RhiDeviceService() {
   }

   public static RhiOperationHandler device() {
      return RhiDeviceService.Holder.rhiOperationHandler;
   }

   private static final class Holder {
      private static final RhiOperationHandler rhiOperationHandler = new GlShaderFactory();
   }
}
