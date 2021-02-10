package me.conclure.derpio.model.user;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import me.conclure.derpio.BotInfo;
import me.conclure.derpio.command.CommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UserManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(CommandHandler.class);

  private final LoadingCache<Long, UserData> userCache;
  private final Executor executor;

  public UserManager() {
    this.executor =
        Executors.newCachedThreadPool(
            new ThreadFactoryBuilder()
                .setUncaughtExceptionHandler((t, e) -> LOGGER.error(e.getMessage(), e))
                .build());

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
            .build(new UserCacheLoader(this));
  }

  Executor getExecutor() {
    return this.executor;
  }

  public UserData getUserInfo(long userId) {
    return this.userCache.get(userId);
  }

  public void saveAll() {
    this.userCache.invalidateAll();
  }

  public Path getStoragePath() {
    return Paths.get("userdata").toAbsolutePath();
  }
}
