package dev.felix.ellice.plugin.api;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.multiplayer.ServerData;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

public final class ClientLuaApi implements ApiOperationHandler {
   private final Map<String, List<LuaFunction>> f756661rwbzo = new HashMap<>();

   @Override
   public void register(Globals var1) {
      LuaTable var2 = new LuaTable();
      var2.set("on", new TwoArgFunction() {
         public LuaValue call(LuaValue var1, LuaValue var2x) {
            if (var2x.isfunction()) {
               ClientLuaApi.this.f756661rwbzo.computeIfAbsent(var1.tojstring(), var0 -> new ArrayList<>()).add(var2x.checkfunction());
            }

            return LuaValue.NIL;
         }
      });
      var2.set("fire", new OneArgFunction() {
         public LuaValue call(LuaValue var1) {
            CoreIsInitializedHandler.get().pluginLoader().fireEvent(var1.tojstring());
            return LuaValue.NIL;
         }
      });
      var2.set("fps", new ZeroArgFunction() {
         public LuaValue call() {
            return LuaValue.valueOf(CoreIsInitializedHandler.mc().getFps());
         }
      });
      var2.set("ping", new ZeroArgFunction() {
         public LuaValue call() {
            Minecraft var1 = CoreIsInitializedHandler.mc();
            if (var1.player != null && var1.getConnection() != null) {
               PlayerInfo var2 = var1.getConnection().getPlayerInfo(var1.player.getUUID());
               return LuaValue.valueOf(var2 != null ? var2.getLatency() : -1);
            } else {
               return LuaValue.valueOf(-1);
            }
         }
      });
      var2.set("server", new ZeroArgFunction() {
         public LuaValue call() {
            Minecraft var1 = CoreIsInitializedHandler.mc();
            if (var1.isLocalServer()) {
               return LuaValue.valueOf("Singleplayer");
            }

            ServerData var2 = var1.getCurrentServer();
            if (var2 != null) {
               if (var2.ip != null && !var2.ip.isBlank()) {
                  return LuaValue.valueOf(var2.ip);
               }

               if (var2.name != null) {
                  return LuaValue.valueOf(var2.name);
               }
            }

            return LuaValue.valueOf("Unknown");
         }
      });
      var1.set("client", var2);
   }

   public void fireEvent(String var1) {
      this.fireEvent(var1, LuaValue.NONE);
   }

   public void fireEvent(String var1, LuaValue var2) {
      List var3 = this.f756661rwbzo.get(var1);
      if (var3 != null) {
         for (LuaFunction var5 : (Iterable<LuaFunction>) (Iterable<?>) (var3)) {
            try {
               if (!var2.isnil() && var2 != LuaValue.NONE) {
                  var5.call(var2);
               } else {
                  var5.call();
               }
            } catch (LuaError var7) {
               CoreIsInitializedHandler.LOGGER.error("Lua event error [{}]: {}", var1, var7.getMessage());
            }
         }
      }
   }
}
