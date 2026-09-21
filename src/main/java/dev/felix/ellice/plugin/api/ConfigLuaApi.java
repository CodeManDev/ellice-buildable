package dev.felix.ellice.plugin.api;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import java.util.List;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

public final class ConfigLuaApi implements ApiOperationHandler {
  @Override
  public void register(Globals globals) {
    LuaTable luaTable = new LuaTable();
    luaTable.set(
        "save",
        new ZeroArgFunction() {
          public LuaValue call() {
            CoreIsInitializedHandler.get().config().save();
            return LuaValue.NIL;
          }
        });
    luaTable.set(
        "saveAs",
        new OneArgFunction() {
          public LuaValue call(LuaValue luaValue) {
            CoreIsInitializedHandler.get().config().saveAs(luaValue.tojstring());
            return LuaValue.NIL;
          }
        });
    luaTable.set(
        "load",
        new OneArgFunction() {
          public LuaValue call(LuaValue luaValue) {
            CoreIsInitializedHandler.get().config().loadConfig(luaValue.tojstring());
            return LuaValue.NIL;
          }
        });
    luaTable.set(
        "list",
        new ZeroArgFunction() {
          public LuaValue call() {
            List items = CoreIsInitializedHandler.get().config().listConfigs();
            LuaTable luaTable = new LuaTable();

            for (int index = 0; index < items.size(); index++) {
              luaTable.set(index + 1, LuaValue.valueOf((String) items.get(index)));
            }

            return luaTable;
          }
        });
    luaTable.set(
        "delete",
        new OneArgFunction() {
          public LuaValue call(LuaValue luaValue) {
            return LuaValue.valueOf(
                CoreIsInitializedHandler.get().config().deleteConfig(luaValue.tojstring()));
          }
        });
    luaTable.set(
        "active",
        new ZeroArgFunction() {
          public LuaValue call() {
            return LuaValue.valueOf(CoreIsInitializedHandler.get().config().activeConfig());
          }
        });
    globals.set("config", luaTable);
  }
}
