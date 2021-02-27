package me.conclure.derpio.util.collection;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.ForwardingSet;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ExpiringSet<E> extends ForwardingSet<E> {
  private final Set<E> setView;

  public ExpiringSet(long duration, TimeUnit unit) {
    this.setView =
        Collections.newSetFromMap(
            Caffeine.newBuilder().expireAfterAccess(duration, unit).<E, Boolean>build().asMap());
  }

  @Override
  protected Set<E> delegate() {
    return this.setView;
  }
}
