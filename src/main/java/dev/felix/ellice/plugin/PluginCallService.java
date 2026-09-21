



package dev.felix.ellice.plugin;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.plugin.api.ConfigLuaApi;
import dev.felix.ellice.plugin.api.MarketplaceLuaApi;
import dev.felix.ellice.plugin.api.ApiRegisterService;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import org.luaj.vm2.LuaError;
import java.util.Objects;
import java.nio.file.Path;
import org.luaj.vm2.lib.OneArgFunction;
import net.fabricmc.loader.api.FabricLoader;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.luaj.vm2.LuaValue;
import java.util.HashMap;
import dev.felix.ellice.plugin.api.ClientLuaApi;
import org.luaj.vm2.Globals;

public class PluginCallService
{
    private final Globals f3a0yi3ejfte;
    private final ClientLuaApi f9q50pa2e8p7;
    private final HashMap<String, LuaValue> fdgtqkw3li0w;
    
    public PluginCallService(final PluginRepository pluginRepository) {
        this.fdgtqkw3li0w = new HashMap<String, LuaValue>();
        this.f3a0yi3ejfte = JsePlatform.standardGlobals();
        final String[] array = { "io", "os", "debug", "luajava", "loadfile", "dofile", "load", "loadstring" };
        for (int length = array.length, i = 0; i < length; ++i) {
            this.f3a0yi3ejfte.set(array[i], LuaValue.NIL);
        }
        final LuaValue value = this.f3a0yi3ejfte.get("package");
        if (!value.isnil()) {
            final LuaValue value2 = value.get("loaded");
            if (!value2.isnil()) {
                final String[] array2 = { "io", "os", "debug", "luajava", "package" };
                for (int length2 = array2.length, j = 0; j < length2; ++j) {
                    value2.set(array2[j], LuaValue.NIL);
                }
            }
            this.f3a0yi3ejfte.set("package", LuaValue.NIL);
        }
        this.f3a0yi3ejfte.set("require", (LuaValue)new OneArgFunction() {
            final  Path val$componentsDir = FabricLoader.getInstance().getGameDir().resolve("ellice-components");
            
            public LuaValue call(final LuaValue luaValue) {
                final String tojstring = luaValue.tojstring();
                if (tojstring == null || tojstring.isEmpty() || tojstring.contains("..") || tojstring.indexOf(47) >= 0 || tojstring.indexOf(92) >= 0 || tojstring.indexOf(0) >= 0) {
                    throw new LuaError("invalid module name: " + tojstring);
                }
                final LuaValue luaValue2 = PluginCallService.this.fdgtqkw3li0w.get(tojstring);
                if (luaValue2 != null) {
                    return luaValue2;
                }
                Path path = this.val$componentsDir.resolve(tojstring.replace('.', '/') + ".lua");
                if (!Files.exists(path, new LinkOption[0])) {
                    path = pluginRepository.directory().resolve(tojstring.replace('.', '/') + ".lua");
                }
                if (!Files.exists(path, new LinkOption[0])) {
                    throw new LuaError("module not found: " + tojstring);
                }
                Path realPath;
                try {
                    realPath = path.toRealPath(new LinkOption[0]);
                    final boolean startsWith = realPath.startsWith(pluginRepository.directory().toRealPath(new LinkOption[0]));
                    if ((!Files.isDirectory(this.val$componentsDir, new LinkOption[0]) || !realPath.startsWith(this.val$componentsDir.toRealPath(new LinkOption[0]))) && !startsWith) {
                        throw new LuaError("access denied: " + tojstring);
                    }
                }
                catch (final LuaError luaError) {
                    throw luaError;
                }
                catch (final Exception ex) {
                    throw new LuaError("module resolve error: " + tojstring);
                }
                try {
                    final String string = Files.readString(realPath);
                    if (!string.isEmpty() && string.charAt(0) == '\u001b') {
                        throw new LuaError("bytecode not allowed: " + tojstring);
                    }
                    Object value = PluginCallService.this.f3a0yi3ejfte.load(string, tojstring + ".lua").call();
                    if (value == null || ((LuaValue)value).isnil()) {
                        value = LuaValue.TRUE;
                    }
                    PluginCallService.this.fdgtqkw3li0w.put(tojstring, (LuaValue)value);
                    return (LuaValue)value;
                }
                catch (final LuaError luaError2) {
                    throw luaError2;
                }
                catch (final Exception ex2) {
                    throw new LuaError("failed to load: " + tojstring + ": " + ex2.getMessage());
                }
            }
        });
        (this.f9q50pa2e8p7 = new ClientLuaApi()).register(this.f3a0yi3ejfte);
        new ApiRegisterService().register(this.f3a0yi3ejfte);
        new MarketplaceLuaApi().register(this.f3a0yi3ejfte);
        new ConfigLuaApi().register(this.f3a0yi3ejfte);
    }
    
    public void fireEvent(final String s) {
        this.f9q50pa2e8p7.fireEvent(s);
    }
    
    public void fireEvent(final String s, final LuaValue luaValue) {
        this.f9q50pa2e8p7.fireEvent(s, luaValue);
    }
    
    public void executeFile(final Path path) {
        try {
            final String string = Files.readString(path);
            if (!string.isEmpty() && string.charAt(0) == '\u001b') {
                CoreIsInitializedHandler.LOGGER.error("Lua bytecode not allowed: {}", (Object)path.getFileName());
                return;
            }
            this.f3a0yi3ejfte.load(string, path.getFileName().toString()).call();
        }
        catch (final Exception ex) {
            CoreIsInitializedHandler.LOGGER.error("Lua error in {}", (Object)path.getFileName(), (Object)ex);
        }
    }
    
    public void callIfExists(final String s) {
        final LuaValue value = this.f3a0yi3ejfte.get(s);
        if (value.isfunction()) {
            try {
                value.call();
            }
            catch (final LuaError luaError) {
                CoreIsInitializedHandler.LOGGER.error("Lua error in {}(): {}", (Object)s, (Object)luaError.getMessage());
            }
        }
    }
}
