package me.conclure.derpio.model.user;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import me.conclure.derpio.BotInfo;
import me.conclure.derpio.command.CommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UserManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(CommandHandler.class);

  private final AsyncLoadingCache<Long, UserData> userCache;
  private final ExecutorService executorService;

  public UserManager() {
    this.executorService = Executors.newCachedThreadPool();

    if (!Files.exists(this.getStoragePath())) {
      try {
        Files.createDirectory(this.getStoragePath());
      } catch (IOException e) {
        LOGGER.error(e.getMessage(), e);
      }
    }

    this.userCache =
        Caffeine.newBuilder()
            .removalListener(new UserRemovalListener(this))
            .expireAfterAccess(BotInfo.USER_EXPIRE_ACCESS_DURATION, BotInfo.USER_EXPIRE_ACCESS_UNIT)
            //.executor(this.executorService)
            .buildAsync(new UserCacheLoader(this));
  }

  public UserData getUserData(long userId) {
    return this.userCache.get(userId).join();
  }

  public void saveAll() {
    this.userCache.synchronous().invalidateAll();
  }

  public void terminate() {
    this.saveAll();
    this.executorService.shutdown();
    try {
      if (!this.executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)) {
        this.executorService.shutdownNow();
      }
    } catch (InterruptedException e) {
      LOGGER.error(e.getMessage(), e);
    }
  }

  public Path getStoragePath() {
    return Paths.get("userdata").toAbsolutePath();
  }
}
