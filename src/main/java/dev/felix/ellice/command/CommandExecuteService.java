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
  private static final Map<String, Integer> text = CommandExecuteService.createText3();
  private final CommandDispatcher<Object> commandDispatcher = new CommandDispatcher();
  private final Object object = new Object();
  private final ModuleRegisterService moduleRegisterService;
  private final ModuleBindService moduleBindService;
  private final Runnable fekhldrvrwe7;
  private final Consumer<String> ffgrtjz1pdas;

  public CommandExecuteService(
      ModuleRegisterService moduleRegisterService,
      ModuleBindService moduleBindService,
      Runnable runnable,
      Consumer<String> consumer) {
    this.moduleRegisterService = moduleRegisterService;
    this.moduleBindService = moduleBindService;
    this.fekhldrvrwe7 = runnable;
    this.ffgrtjz1pdas = consumer;
    this.commandDispatcher.register(
        (LiteralArgumentBuilder)
            LiteralArgumentBuilder.literal((String) "toggle")
                .then(
                    this.createText()
                        .executes(
                            commandContext -> {
                              Module module =
                                  this.createModuleOperationHandler2(
                                      StringArgumentType.getString(
                                          (CommandContext) commandContext, (String) "module"));
                              if (module == null) {
                                return 0;
                              }
                              module.toggle();
                              consumer.accept(
                                  module.name()
                                      + (module.isEnabled() ? " enabled." : " disabled."));
                              return 1;
                            })));
    this.commandDispatcher.register(
        (LiteralArgumentBuilder)
            LiteralArgumentBuilder.literal((String) "bind")
                .then(
                    this.createText()
                        .then(
                            RequiredArgumentBuilder.argument(
                                    (String) "key", (ArgumentType) StringArgumentType.word())
                                .suggests(
                                    (commandContext, suggestionsBuilder) -> {
                                      String string =
                                          suggestionsBuilder
                                              .getRemaining()
                                              .toUpperCase(Locale.ROOT);
                                      text.keySet().stream()
                                          .filter(string2 -> string2.startsWith(string))
                                          .forEach(
                                              arg_0 ->
                                                  ((SuggestionsBuilder) suggestionsBuilder)
                                                      .suggest(arg_0));
                                      return suggestionsBuilder.buildFuture();
                                    })
                                .executes(
                                    commandContext -> {
                                      Module module =
                                          this.createModuleOperationHandler2(
                                              StringArgumentType.getString(
                                                  (CommandContext) commandContext,
                                                  (String) "module"));
                                      if (module == null) {
                                        return 0;
                                      }
                                      String string =
                                          StringArgumentType.getString(
                                                  (CommandContext) commandContext, (String) "key")
                                              .toUpperCase(Locale.ROOT);
                                      Integer n = text.get(string);
                                      if (n == null) {
                                        consumer.accept(
                                            "Unknown key: "
                                                + string
                                                + ". Use Tab to choose a key, or NONE to unbind.");
                                        return 0;
                                      }
                                      String string2 = moduleBindService.all().get(n);
                                      moduleBindService.bind(n, module.name());
                                      runnable.run();
                                      consumer.accept(
                                          n < 0
                                              ? "Removed binding for " + module.name() + "."
                                              : "Bound "
                                                  + module.name()
                                                  + " to "
                                                  + string
                                                  + "."
                                                  + (String)
                                                      (string2 != null
                                                              && !string2.equals(module.name())
                                                          ? " Replaced binding for " + string2 + "."
                                                          : ""));
                                      return 1;
                                    }))));
  }

  private RequiredArgumentBuilder<Object, String> createText() {
    return RequiredArgumentBuilder.argument(
            (String) "module", (ArgumentType) StringArgumentType.string())
        .suggests(
            (commandContext, suggestionsBuilder) -> {
              String string =
                  suggestionsBuilder.getRemaining().replace("\"", "").toLowerCase(Locale.ROOT);
              this.moduleRegisterService.stream()
                  .map(Module::name)
                  .sorted(String.CASE_INSENSITIVE_ORDER)
                  .filter(
                      string2 ->
                          CommandExecuteService.createText2(string2)
                              .startsWith(CommandExecuteService.createText2(string)))
                  .map(StringArgumentType::escapeIfRequired)
                  .forEach(arg_0 -> ((SuggestionsBuilder) suggestionsBuilder).suggest(arg_0));
              return suggestionsBuilder.buildFuture();
            });
  }

  private Module createModuleOperationHandler2(String string) {
    Module module2 = this.moduleRegisterService.get(string).orElse(null);
    if (module2 != null) {
      return module2;
    }
    List<Module> list =
        this.moduleRegisterService.stream()
            .filter(
                module ->
                    CommandExecuteService.createText2(module.name())
                        .equals(CommandExecuteService.createText2(string)))
            .toList();
    if (list.size() == 1) {
      return list.getFirst();
    }
    this.ffgrtjz1pdas.accept(
        list.isEmpty()
            ? "Unknown module: " + string + ". Use Tab to choose a module."
            : "Ambiguous module: " + string + ". Use its full name in quotes.");
    return null;
  }

  private static String createText2(String string) {
    return string.replace(" ", "").toLowerCase(Locale.ROOT);
  }

  public boolean execute(String string) {
    if (!string.startsWith(".")) {
      return false;
    }
    try {
      this.commandDispatcher.execute(CommandExecuteService.createReader(string), this.object);
    } catch (CommandSyntaxException commandSyntaxException) {
      this.ffgrtjz1pdas.accept(
          "Usage: .toggle <module> or .bind <module> <key|NONE>. Use quotes for names with spaces"
              + " and Tab for suggestions.");
    }
    return true;
  }

  public CompletableFuture<Suggestions> suggest(String string, int n) {
    if (!string.startsWith(".") || n < 1) {
      return Suggestions.empty();
    }
    return this.commandDispatcher.getCompletionSuggestions(
        this.commandDispatcher.parse(CommandExecuteService.createReader(string), this.object),
        Math.min(n, string.length()));
  }

  private static StringReader createReader(String string) {
    StringReader stringReader = new StringReader(string);
    stringReader.setCursor(1);
    return stringReader;
  }

  private static Map<String, Integer> createText3() {
    TreeMap<String, Integer> treeMap = new TreeMap<String, Integer>();
    for (Field field : GLFW.class.getFields()) {
      if (!field.getName().startsWith("GLFW_KEY_")) continue;
      try {
        int n = field.getInt(null);
        if (field.getName().equals("GLFW_KEY_LAST") || !ModuleBindService.isKeyboardKey(n))
          continue;
        treeMap.put(field.getName().substring(9), n);
      } catch (IllegalAccessException illegalAccessException) {
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
