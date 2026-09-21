



package dev.felix.ellice.ui.component;

import java.util.Iterator;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import dev.felix.ellice.ui.scene.ScenePctService;

public final class ComponentKeyService<N extends ScenePctService<?>>
{
    private final String fcaz1vnmtk4y;
    private final String f28ozedkylz5;
    private final Supplier<N> fba3wzfsodj6;
    private final Consumer<N> fbet5r1ca0rf;
    private final Consumer<N> fhh9zrmrrj8t;
    private final Consumer<N> f4mj4vuhxkr5;
    private final List<ComponentKeyService<?>> f67bstowi01j;
    
    private ComponentKeyService(final String s, final String f28ozedkylz5, final Supplier<N> obj, final Consumer<N> obj2, final Consumer<N> obj3, final Consumer<N> obj4, final List<ComponentKeyService<?>> coll) {
        this.fcaz1vnmtk4y = m56n7nxjg6z4(s, "type");
        this.f28ozedkylz5 = f28ozedkylz5;
        this.fba3wzfsodj6 = Objects.requireNonNull(obj, "factory");
        this.fbet5r1ca0rf = Objects.requireNonNull(obj2, "props");
        this.fhh9zrmrrj8t = Objects.requireNonNull(obj3, "mountEffect");
        this.f4mj4vuhxkr5 = Objects.requireNonNull(obj4, "unmountEffect");
        this.f67bstowi01j = List.copyOf((Collection<? extends ComponentKeyService<?>>)coll);
    }
    
    public static <N extends ScenePctService<?>> ComponentKeyService<N> of(final String s, final Supplier<N> supplier, final Consumer<N> consumer, final ComponentKeyService<?>... array) {
        return new ComponentKeyService<N>(s, null, supplier, (consumer != null) ? consumer : (p0 -> {}), p0 -> {}, p0 -> {}, m201vhdpgby7(array));
    }
    
    public ComponentKeyService<N> key(final String s) {
        return new ComponentKeyService<N>(this.fcaz1vnmtk4y, m56n7nxjg6z4(s, "key"), this.fba3wzfsodj6, this.fbet5r1ca0rf, this.fhh9zrmrrj8t, this.f4mj4vuhxkr5, this.f67bstowi01j);
    }
    
    public ComponentKeyService<N> props(final Consumer<N> consumer) {
        Objects.requireNonNull(consumer, "additionalProps");
        return new ComponentKeyService<N>(this.fcaz1vnmtk4y, this.f28ozedkylz5, this.fba3wzfsodj6, this.fbet5r1ca0rf.andThen(consumer), this.fhh9zrmrrj8t, this.f4mj4vuhxkr5, this.f67bstowi01j);
    }
    
    public ComponentKeyService<N> style(final ComponentStyleService componentStyleService) {
        Objects.requireNonNull(componentStyleService, "style");
        Objects.requireNonNull(componentStyleService);
        return this.props(componentStyleService::apply);
    }
    
    public ComponentKeyService<N> children(final ComponentKeyService<?>... array) {
        return new ComponentKeyService<N>(this.fcaz1vnmtk4y, this.f28ozedkylz5, this.fba3wzfsodj6, this.fbet5r1ca0rf, this.fhh9zrmrrj8t, this.f4mj4vuhxkr5, m201vhdpgby7(array));
    }
    
    public ComponentKeyService<N> onMount(final Consumer<N> consumer) {
        Objects.requireNonNull(consumer, "effect");
        return new ComponentKeyService<N>(this.fcaz1vnmtk4y, this.f28ozedkylz5, this.fba3wzfsodj6, this.fbet5r1ca0rf, this.fhh9zrmrrj8t.andThen(consumer), this.f4mj4vuhxkr5, this.f67bstowi01j);
    }
    
    public ComponentKeyService<N> onUnmount(final Consumer<N> consumer) {
        Objects.requireNonNull(consumer, "effect");
        return new ComponentKeyService<N>(this.fcaz1vnmtk4y, this.f28ozedkylz5, this.fba3wzfsodj6, this.fbet5r1ca0rf, this.fhh9zrmrrj8t, this.f4mj4vuhxkr5.andThen(consumer), this.f67bstowi01j);
    }
    
    String type() {
        return this.fcaz1vnmtk4y;
    }
    
    String key() {
        return this.f28ozedkylz5;
    }
    
    List<ComponentKeyService<?>> children() {
        return this.f67bstowi01j;
    }
    
    N create() {
        final ScenePctService<?> scenePctService = Objects.requireNonNull(this.fba3wzfsodj6.get(), "Element factory returned null");
        this.apply(scenePctService);
        this.fhh9zrmrrj8t.accept((N)scenePctService);
        return (N)scenePctService;
    }
    
    void apply(final ScenePctService<?> scenePctService) {
        this.fbet5r1ca0rf.accept((N)scenePctService);
    }
    
    void unmount(final ScenePctService<?> scenePctService) {
        this.f4mj4vuhxkr5.accept((N)scenePctService);
    }
    
    boolean compatible(final ComponentKeyService<?> componentKeyService) {
        return componentKeyService != null && this.fcaz1vnmtk4y.equals(componentKeyService.fcaz1vnmtk4y) && Objects.equals(this.f28ozedkylz5, componentKeyService.f28ozedkylz5);
    }
    
    private static List<ComponentKeyService<?>> m201vhdpgby7(final ComponentKeyService<?>[] array) {
        if (array == null || array.length == 0) {
            return List.of();
        }
        final List<ComponentKeyService<?>> list = Arrays.asList((ComponentKeyService[])array.clone());
        final Iterator<ComponentKeyService<?>> iterator = list.iterator();
        while (iterator.hasNext()) {
            Objects.requireNonNull(iterator.next(), "child");
        }
        return (List<ComponentKeyService<?>>)list;
    }
    
    private static String m56n7nxjg6z4(final String s, final String s2) {
        if (s == null || s.isBlank()) {
            throw new IllegalArgumentException(s2 + " must not be blank");
        }
        return s;
    }
}
