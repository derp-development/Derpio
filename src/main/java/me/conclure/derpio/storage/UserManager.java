package me.conclure.derpio.storage;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import me.conclure.derpio.BotInfo;
import me.conclure.derpio.command.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UserManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(CommandManager.class);

  private final LoadingCache<Long, UserData> userCache;

  public UserManager() {
    if (!Files.exists(getStoragePath())) {
      try {
        Files.createDirectory(getStoragePath());
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

  public UserData getUserInfo(long userId) {
    return userCache.get(userId);
  }

  public void saveAll() {
    userCache.invalidateAll();
  }

  public Path getStoragePath() {
    return Paths.get("userdata").toAbsolutePath();
  }
}
