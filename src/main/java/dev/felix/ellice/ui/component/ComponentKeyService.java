package dev.felix.ellice.ui.component;

import dev.felix.ellice.ui.scene.ScenePctService;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ComponentKeyService<N extends ScenePctService<?>> {
  private final String text;
  private final String text2;
  private final Supplier<N> supplier;
  private final Consumer<N> consumer;
  private final Consumer<N> consumer2;
  private final Consumer<N> consumer3;
  private final List<ComponentKeyService<?>> items;

  private ComponentKeyService(
      final String s,
      final String text2,
      final Supplier<N> obj,
      final Consumer<N> obj2,
      final Consumer<N> obj3,
      final Consumer<N> obj4,
      final List<ComponentKeyService<?>> coll) {
    this.text = createText(s, "type");
    this.text2 = text2;
    this.supplier = Objects.requireNonNull(obj, "factory");
    this.consumer = Objects.requireNonNull(obj2, "props");
    this.consumer2 = Objects.requireNonNull(obj3, "mountEffect");
    this.consumer3 = Objects.requireNonNull(obj4, "unmountEffect");
    this.items = List.copyOf((Collection<? extends ComponentKeyService<?>>) coll);
  }

  public static <N extends ScenePctService<?>> ComponentKeyService<N> of(
      final String s,
      final Supplier<N> supplier,
      final Consumer<N> consumer,
      final ComponentKeyService<?>... array) {
    return new ComponentKeyService<N>(
        s,
        null,
        supplier,
        (consumer != null) ? consumer : (p0 -> {}),
        p0 -> {},
        p0 -> {},
        collectValues(array));
  }

  public ComponentKeyService<N> key(final String s) {
    return new ComponentKeyService<N>(
        this.text,
        createText(s, "key"),
        this.supplier,
        this.consumer,
        this.consumer2,
        this.consumer3,
        this.items);
  }

  public ComponentKeyService<N> props(final Consumer<N> consumer) {
    Objects.requireNonNull(consumer, "additionalProps");
    return new ComponentKeyService<N>(
        this.text,
        this.text2,
        this.supplier,
        this.consumer.andThen(consumer),
        this.consumer2,
        this.consumer3,
        this.items);
  }

  public ComponentKeyService<N> style(final ComponentStyleService componentStyleService) {
    Objects.requireNonNull(componentStyleService, "style");
    Objects.requireNonNull(componentStyleService);
    return this.props(componentStyleService::apply);
  }

  public ComponentKeyService<N> children(final ComponentKeyService<?>... array) {
    return new ComponentKeyService<N>(
        this.text,
        this.text2,
        this.supplier,
        this.consumer,
        this.consumer2,
        this.consumer3,
        collectValues(array));
  }

  public ComponentKeyService<N> onMount(final Consumer<N> consumer) {
    Objects.requireNonNull(consumer, "effect");
    return new ComponentKeyService<N>(
        this.text,
        this.text2,
        this.supplier,
        this.consumer,
        this.consumer2.andThen(consumer),
        this.consumer3,
        this.items);
  }

  public ComponentKeyService<N> onUnmount(final Consumer<N> consumer) {
    Objects.requireNonNull(consumer, "effect");
    return new ComponentKeyService<N>(
        this.text,
        this.text2,
        this.supplier,
        this.consumer,
        this.consumer2,
        this.consumer3.andThen(consumer),
        this.items);
  }

  String type() {
    return this.text;
  }

  String key() {
    return this.text2;
  }

  List<ComponentKeyService<?>> children() {
    return this.items;
  }

  N create() {
    final ScenePctService<?> scenePctService =
        Objects.requireNonNull(this.supplier.get(), "Element factory returned null");
    this.apply(scenePctService);
    this.consumer2.accept((N) scenePctService);
    return (N) scenePctService;
  }

  void apply(final ScenePctService<?> scenePctService) {
    this.consumer.accept((N) scenePctService);
  }

  void unmount(final ScenePctService<?> scenePctService) {
    this.consumer3.accept((N) scenePctService);
  }

  boolean compatible(final ComponentKeyService<?> componentKeyService) {
    return componentKeyService != null
        && this.text.equals(componentKeyService.text)
        && Objects.equals(this.text2, componentKeyService.text2);
  }

  private static List<ComponentKeyService<?>> collectValues(final ComponentKeyService<?>[] array) {
    if (array == null || array.length == 0) {
      return List.of();
    }
    final List<ComponentKeyService<?>> list = Arrays.asList((ComponentKeyService[]) array.clone());
    final Iterator<ComponentKeyService<?>> iterator = list.iterator();
    while (iterator.hasNext()) {
      Objects.requireNonNull(iterator.next(), "child");
    }
    return (List<ComponentKeyService<?>>) list;
  }

  private static String createText(final String s, final String s2) {
    if (s == null || s.isBlank()) {
      throw new IllegalArgumentException(s2 + " must not be blank");
    }
    return s;
  }
}
