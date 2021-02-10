package me.conclure.derpio.model.product.cache;

import com.github.benmanes.caffeine.cache.AsyncCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import me.conclure.derpio.model.product.Product;

public final class ProductCache {

  private final AsyncCache<UUID, Product> cache;
  private final Map<String,UUID> name2id;

  public ProductCache() {
    this.cache = Caffeine.newBuilder()
        .buildAsync();
    this.name2id = new ConcurrentHashMap<>();
  }

  public Product getProduct(UUID id) {
    return null;
  }

  public Product getProduct(String name) {
    return null;
  }
}
