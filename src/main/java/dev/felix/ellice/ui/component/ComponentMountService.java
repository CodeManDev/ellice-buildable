package dev.felix.ellice.ui.component;

import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.theme.ThemeIsSetService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class ComponentMountService extends LayoutContainerNode {
  private ComponentOperationHandler componentOperationHandler;
  private ComponentThemeService componentThemeService;
  private ComponentMountService.Mounted mounted;
  private boolean enabled;
  private boolean enabled2;
  private Runnable runnable = () -> {};

  public ComponentMountService mount(
      ComponentOperationHandler componentOperation, ThemeIsSetService themeIsSet) {
    this.runnable.run();
    this.componentOperationHandler = Objects.requireNonNull(componentOperation, "component");
    this.componentThemeService = new ComponentThemeService(themeIsSet, this::invalidateComponent);
    this.runnable = themeIsSet.onAppearanceChanged(this::invalidateComponent);
    this.invalidateComponent();
    return this;
  }

  public void invalidateComponent() {
    if (this.componentOperationHandler != null && this.componentThemeService != null) {
      if (this.enabled) {
        this.enabled2 = true;
      } else {
        do {
          this.enabled2 = false;
          this.enabled = true;

          try {
            ComponentKeyService<?> componentKey =
                Objects.requireNonNull(
                    this.componentOperationHandler.render(this.componentThemeService),
                    "Component returned null");
            ComponentMountService.Mounted currentMounted =
                this.createMounted(this.mounted, componentKey);
            this.reconcileChildren(List.of(currentMounted.node));
            this.mounted = currentMounted;
          } finally {
            this.enabled = false;
          }
        } while (this.enabled2);
      }
    }
  }

  public void unmountComponent() {
    this.runnable.run();
    this.runnable = () -> {};
    if (this.mounted != null) {
      updateState2(this.mounted);
    }

    this.mounted = null;
    this.componentOperationHandler = null;
    this.componentThemeService = null;
    this.reconcileChildren(List.of());
  }

  @Override
  protected void onDetached() {
    this.unmountComponent();
  }

  private ComponentMountService.Mounted createMounted(
      ComponentMountService.Mounted mounted, ComponentKeyService<?> componentKey) {
    if (mounted != null && mounted.element.compatible(componentKey)) {
      componentKey.apply(mounted.node);
      List items = this.collectValues(mounted.children, componentKey.children());
      mounted.node.reconcileChildren(collectValues2(items));
      return new ComponentMountService.Mounted(componentKey, mounted.node, items);
    }

    if (mounted != null) {
      updateState2(mounted);
    }

    return this.createMounted2(componentKey);
  }

  private ComponentMountService.Mounted createMounted2(ComponentKeyService<?> componentKey) {
    ScenePctService scenePct = componentKey.create();
    ArrayList currentSize = new ArrayList(componentKey.children().size());
    updateState(componentKey.children());

    for (ComponentKeyService<?> currentComponentKey : componentKey.children()) {
      currentSize.add(this.createMounted2(currentComponentKey));
    }

    scenePct.reconcileChildren(collectValues2(currentSize));
    return new ComponentMountService.Mounted(componentKey, scenePct, currentSize);
  }

  private List<ComponentMountService.Mounted> collectValues(
      List<ComponentMountService.Mounted> items, List<ComponentKeyService<?>> currentItems) {
    updateState(currentItems);
    HashMap hashMap = new HashMap();
    ArrayList arrayList = new ArrayList();

    for (ComponentMountService.Mounted mounted : items) {
      String text = mounted.element.key();
      if (text != null) {
        hashMap.put(text, mounted);
      } else {
        arrayList.add(mounted);
      }
    }

    Set values = Collections.newSetFromMap(new IdentityHashMap());
    ArrayList currentSize = new ArrayList(currentItems.size());
    int index = 0;

    for (ComponentKeyService<?> componentKey : currentItems) {
      ComponentMountService.Mounted currentMounted =
          componentKey.key() != null
              ? (ComponentMountService.Mounted) hashMap.get(componentKey.key())
              : null;
      if (currentMounted == null && componentKey.key() == null) {
        while (index < arrayList.size()) {
          ComponentMountService.Mounted nextMounted =
              (ComponentMountService.Mounted) arrayList.get(index++);
          if (!values.contains(nextMounted) && nextMounted.element.compatible(componentKey)) {
            currentMounted = nextMounted;
            break;
          }
        }
      }

      if (currentMounted != null) {
        values.add(currentMounted);
      }

      currentSize.add(this.createMounted(currentMounted, componentKey));
    }

    for (ComponentMountService.Mounted previousMounted : items) {
      if (!values.contains(previousMounted)) {
        updateState2(previousMounted);
      }
    }

    return currentSize;
  }

  private static void updateState(List<ComponentKeyService<?>> items) {
    HashSet hashSet = new HashSet();

    for (ComponentKeyService<?> componentKey : items) {
      String text = componentKey.key();
      if (text != null && !hashSet.add(text)) {
        throw new IllegalArgumentException("Duplicate sibling key: " + text);
      }
    }
  }

  private static List<ScenePctService<?>> collectValues2(
      List<ComponentMountService.Mounted> items) {
    ArrayList currentSize = new ArrayList(items.size());

    for (ComponentMountService.Mounted mounted : items) {
      currentSize.add(mounted.node);
    }

    return currentSize;
  }

  private static void updateState2(ComponentMountService.Mounted mounted) {
    for (ComponentMountService.Mounted currentMounted : mounted.children) {
      updateState2(currentMounted);
    }

    mounted.element.unmount(mounted.node);
  }

  private record Mounted(
      ComponentKeyService<?> element,
      ScenePctService<?> node,
      List<ComponentMountService.Mounted> children) {}
}
