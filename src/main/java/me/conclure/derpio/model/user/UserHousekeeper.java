package me.conclure.derpio.model.user;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import me.conclure.derpio.util.collection.ExpiringSet;

public final class UserHousekeeper implements Runnable {
  private final Set<Long> recentlyUsed;
  private final UserManager userManager;

  UserHousekeeper(long duration, TimeUnit unit,
      UserManager userManager) {
    this.userManager = userManager;
    this.recentlyUsed = new ExpiringSet<>(duration, unit);
  }

  public void registerUsage(Long userId) {
    this.recentlyUsed.add(userId);
  }

  @Override
  public void run() {
    for (Long key : this.userManager.getMap().keySet()) {
      this.tryCleanUp(key);
    }
  }

  public void tryCleanUp(Long userId) {
    if (this.recentlyUsed.contains(userId)) {
      return;
    }

    this.userManager.unload(userId);
  }
}
