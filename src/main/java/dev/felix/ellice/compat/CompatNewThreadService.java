package dev.felix.ellice.compat;

import dev.felix.ellice.account.AccountCodeService;
import dev.felix.ellice.account.AccountCodec;
import java.awt.Desktop;
import java.awt.FileDialog;
import java.awt.Frame;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

public final class CompatNewThreadService {
  private static final Executor executor =
      Executors.newCachedThreadPool(
          new ThreadFactory() {
            private final AtomicInteger atomicInteger = new AtomicInteger();

            @Override
            public Thread newThread(Runnable runnable) {
              Thread thread =
                  new Thread(
                      runnable, "ellice-cookie-import-" + this.atomicInteger.incrementAndGet());
              thread.setDaemon(true);
              return thread;
            }
          });

  private CompatNewThreadService() {}

  public static CompletableFuture<Optional<AccountCodec>> choose() {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            return findResult();
          } catch (AccountCodeService accountCode) {
            throw new CompletionException(accountCode);
          } catch (Throwable exception) {
            throw new CompletionException(
                new AccountCodeService(
                    "COOKIE_FILE",
                    "Couldn't open the cookie file. Try again or paste a cookie instead."));
          }
        },
        executor);
  }

  private static Optional<AccountCodec> findResult() throws Exception {
    if (Desktop.isDesktopSupported()) {
      try {
        FileDialog fileDialog = new FileDialog((Frame) null, "Import Microsoft cookies", 0);
        fileDialog.setFilenameFilter(
            (item, currentItem) ->
                currentItem != null
                    && (currentItem.toLowerCase().endsWith(".txt")
                        || currentItem.toLowerCase().endsWith(".json")));
        fileDialog.setVisible(true);
        String name = fileDialog.getDirectory();
        String currentName = fileDialog.getFile();
        fileDialog.dispose();
        if (currentName != null && !currentName.isEmpty()) {
          return Optional.of(AccountCodec.read(Path.of(name, currentName)));
        }

        return Optional.empty();
      } catch (Throwable exception) {
      }
    }

    MemoryStack stack = MemoryStack.stackPush();

    Optional result;
    try {
      PointerBuffer pointerBuffer = stack.pointers(stack.UTF8("*.txt"), stack.UTF8("*.json"));
      String nextName =
          TinyFileDialogs.tinyfd_openFileDialog(
              "Import Microsoft cookies",
              "",
              pointerBuffer,
              "Cookie exports (*.txt, *.json)",
              false);
      result =
          nextName == null ? Optional.empty() : Optional.of(AccountCodec.read(Path.of(nextName)));
    } catch (Throwable currentException) {
      if (stack != null) {
        try {
          stack.close();
        } catch (Throwable nextException) {
          currentException.addSuppressed(nextException);
        }
      }

      throw currentException;
    }

    if (stack != null) {
      stack.close();
    }

    return result;
  }
}
