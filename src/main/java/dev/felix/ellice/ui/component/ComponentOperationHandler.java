package dev.felix.ellice.ui.component;

@FunctionalInterface
public interface ComponentOperationHandler {
   ComponentKeyService<?> render(ComponentThemeService componentTheme);
}
