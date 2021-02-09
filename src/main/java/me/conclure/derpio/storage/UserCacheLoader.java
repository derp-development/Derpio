package me.conclure.derpio.storage;

import com.github.benmanes.caffeine.cache.CacheLoader;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import me.conclure.derpio.BotInfo;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UserCacheLoader implements CacheLoader<Long, UserData> {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserCacheLoader.class);

  private final UserManager userManager;

  UserCacheLoader(UserManager userManager) {
    this.userManager = userManager;
  }

  @Override
  public @Nullable UserData load(@NonNull Long userId) {
    Path userPath = userManager.getStoragePath().resolve(userId + ".json");
    UserData result = null;

    if (!Files.exists(userPath)) {
      try {
        Files.createFile(userPath);
      } catch (IOException e) {
        LOGGER.error(e.getMessage(), e);
      }
    }

    try (BufferedReader reader = Files.newBufferedReader(userPath, StandardCharsets.UTF_8)) {
      result = BotInfo.GSON.fromJson(reader, UserData.class);
    } catch (IOException e) {
      LOGGER.error(e.getMessage(), e);
    }

    if (result == null) {
      result = new UserData();
    }
    return result;
  }
}