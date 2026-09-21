















package dev.felix.ellice.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.felix.ellice.module.Module;
import dev.felix.ellice.module.ModuleBindService;
import dev.felix.ellice.module.ModuleRegisterService;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import org.lwjgl.glfw.GLFW;

public final class CommandExecuteService {
    private static final Map<String, Integer> f8mm4nhvlqzc = CommandExecuteService.mb9cta2zq7z4();
    private final CommandDispatcher<Object> f91edigern01 = new CommandDispatcher();
    private final Object f41esx8m3o1m = new Object();
    private final ModuleRegisterService ffuw6l2cw69y;
    private final ModuleBindService fcpp0xuow1lj;
    private final Runnable fekhldrvrwe7;
    private final Consumer<String> ffgrtjz1pdas;

    public CommandExecuteService(ModuleRegisterService moduleRegisterService, ModuleBindService moduleBindService, Runnable runnable, Consumer<String> consumer) {
        this.ffuw6l2cw69y = moduleRegisterService;
        this.fcpp0xuow1lj = moduleBindService;
        this.fekhldrvrwe7 = runnable;
        this.ffgrtjz1pdas = consumer;
        this.f91edigern01.register((LiteralArgumentBuilder)LiteralArgumentBuilder.literal((String)"toggle").then(this.mf4kwfl23kea().executes(commandContext -> {
            Module module = this.mas8g5j03tat(StringArgumentType.getString((CommandContext)commandContext, (String)"module"));
            if (module == null) {
                return 0;
            }
            module.toggle();
            consumer.accept(module.name() + (module.isEnabled() ? " enabled." : " disabled."));
            return 1;
        })));
        this.f91edigern01.register((LiteralArgumentBuilder)LiteralArgumentBuilder.literal((String)"bind").then(this.mf4kwfl23kea().then(RequiredArgumentBuilder.argument((String)"key", (ArgumentType)StringArgumentType.word()).suggests((commandContext, suggestionsBuilder) -> {
            String string = suggestionsBuilder.getRemaining().toUpperCase(Locale.ROOT);
            f8mm4nhvlqzc.keySet().stream().filter(string2 -> string2.startsWith(string)).forEach(arg_0 -> ((SuggestionsBuilder)suggestionsBuilder).suggest(arg_0));
            return suggestionsBuilder.buildFuture();
        }).executes(commandContext -> {
            Module module = this.mas8g5j03tat(StringArgumentType.getString((CommandContext)commandContext, (String)"module"));
            if (module == null) {
                return 0;
            }
            String string = StringArgumentType.getString((CommandContext)commandContext, (String)"key").toUpperCase(Locale.ROOT);
            Integer n = f8mm4nhvlqzc.get(string);
            if (n == null) {
                consumer.accept("Unknown key: " + string + ". Use Tab to choose a key, or NONE to unbind.");
                return 0;
            }
            String string2 = moduleBindService.all().get(n);
            moduleBindService.bind(n, module.name());
            runnable.run();
            consumer.accept(n < 0 ? "Removed binding for " + module.name() + "." : "Bound " + module.name() + " to " + string + "." + (String)(string2 != null && !string2.equals(module.name()) ? " Replaced binding for " + string2 + "." : ""));
            return 1;
        }))));
    }

    private RequiredArgumentBuilder<Object, String> mf4kwfl23kea() {
        return RequiredArgumentBuilder.argument((String)"module", (ArgumentType)StringArgumentType.string()).suggests((commandContext, suggestionsBuilder) -> {
            String string = suggestionsBuilder.getRemaining().replace("\"", "").toLowerCase(Locale.ROOT);
            this.ffuw6l2cw69y.stream().map(Module::name).sorted(String.CASE_INSENSITIVE_ORDER).filter(string2 -> CommandExecuteService.m6o6qz9cyzh7(string2).startsWith(CommandExecuteService.m6o6qz9cyzh7(string))).map(StringArgumentType::escapeIfRequired).forEach(arg_0 -> ((SuggestionsBuilder)suggestionsBuilder).suggest(arg_0));
            return suggestionsBuilder.buildFuture();
        });
    }

    private Module mas8g5j03tat(String string) {
        Module module2 = this.ffuw6l2cw69y.get(string).orElse(null);
        if (module2 != null) {
            return module2;
        }
        List<Module> list = this.ffuw6l2cw69y.stream().filter(module -> CommandExecuteService.m6o6qz9cyzh7(module.name()).equals(CommandExecuteService.m6o6qz9cyzh7(string))).toList();
        if (list.size() == 1) {
            return list.getFirst();
        }
        this.ffgrtjz1pdas.accept(list.isEmpty() ? "Unknown module: " + string + ". Use Tab to choose a module." : "Ambiguous module: " + string + ". Use its full name in quotes.");
        return null;
    }

    private static String m6o6qz9cyzh7(String string) {
        return string.replace(" ", "").toLowerCase(Locale.ROOT);
    }

    public boolean execute(String string) {
        if (!string.startsWith(".")) {
            return false;
        }
        try {
            this.f91edigern01.execute(CommandExecuteService.md390uvs4h2j(string), this.f41esx8m3o1m);
        }
        catch (CommandSyntaxException commandSyntaxException) {
            this.ffgrtjz1pdas.accept("Usage: .toggle <module> or .bind <module> <key|NONE>. Use quotes for names with spaces and Tab for suggestions.");
        }
        return true;
    }

    public CompletableFuture<Suggestions> suggest(String string, int n) {
        if (!string.startsWith(".") || n < 1) {
            return Suggestions.empty();
        }
        return this.f91edigern01.getCompletionSuggestions(this.f91edigern01.parse(CommandExecuteService.md390uvs4h2j(string), this.f41esx8m3o1m), Math.min(n, string.length()));
    }

    private static StringReader md390uvs4h2j(String string) {
        StringReader stringReader = new StringReader(string);
        stringReader.setCursor(1);
        return stringReader;
    }

    private static Map<String, Integer> mb9cta2zq7z4() {
        TreeMap<String, Integer> treeMap = new TreeMap<String, Integer>();
        for (Field field : GLFW.class.getFields()) {
            if (!field.getName().startsWith("GLFW_KEY_")) continue;
            try {
                int n = field.getInt(null);
                if (field.getName().equals("GLFW_KEY_LAST") || !ModuleBindService.isKeyboardKey(n)) continue;
                treeMap.put(field.getName().substring(9), n);
            }
            catch (IllegalAccessException illegalAccessException) {
                throw new IllegalStateException(illegalAccessException);
            }
        }
        treeMap.put("NONE", -1);
        treeMap.put("ESC", 256);
        treeMap.put("LSHIFT", 340);
        treeMap.put("RSHIFT", 344);
        treeMap.put("LCTRL", 341);
        treeMap.put("RCTRL", 345);
        treeMap.put("LALT", 342);
        treeMap.put("RALT", 346);
        return Map.copyOf(treeMap);
    }
}

