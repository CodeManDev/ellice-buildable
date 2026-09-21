package dev.felix.ellice.plugin.api;

import org.luaj.vm2.Globals;

public interface ApiOperationHandler {
  void register(Globals globals);
}
