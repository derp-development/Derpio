package me.conclure.derpio.util.collection;

import com.google.common.collect.ForwardingMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.checkerframework.checker.nullness.qual.Nullable;

public class LoadingMap<K, V> extends ForwardingMap<K, V> {

  private final Map<K, V> map;
  private final Function<K, V> function;

  public LoadingMap(Function<K, V> function) {
    this.function = function;
    this.map = new ConcurrentHashMap<>();
  }

  @Override
  protected Map<K, V> delegate() {
    return this.map;
  }

  public V getIfPresent(K key) {
    return this.map.get(key);
  }

  @Override
  public V get(@Nullable Object key) {
    V val = this.map.get(key);

    if (val != null) {
      return val;
    }

    //noinspection unchecked
    return this.map.computeIfAbsent((K) key, this.function);
  }
}
