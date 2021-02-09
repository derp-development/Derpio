package me.conclure.derpio.storage;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import me.conclure.derpio.command.CommandManager;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UserManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(CommandManager.class);

  private final LoadingCache<Long, UserInfo> userCache;
  private final Gson gson;

  public UserManager() {
    if (Files.notExists(getStoragePath())) {
      try {
        Files.createDirectory(getStoragePath());
      } catch (IOException e) {
        LOGGER.error(e.getMessage(), e);
      }
    }

    this.gson =
        new Gson()
            .newBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .serializeNulls()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    this.userCache =
        Caffeine.newBuilder()
            .<Long, UserInfo>removalListener(
                (userId, info, cause) -> {
                  Path userPath = getStoragePath().resolve(userId + ".json");

                  if (Files.notExists(userPath)) {
                    try {
                      Files.createFile(userPath);
                    } catch (IOException e) {
                      LOGGER.error(e.getMessage(), e);
                    }
                  }

                  try (BufferedWriter reader =
                      Files.newBufferedWriter(userPath, StandardCharsets.UTF_8)) {
                    gson.toJson(info, reader);
                    reader.flush();
                  } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                  }
                })
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .build(
                new CacheLoader<Long, UserInfo>() {
                  @Override
                  public @Nullable UserInfo load(@NonNull Long userId) {
                    Path userPath = getStoragePath().resolve(userId + ".json");
                    UserInfo result = null;

                    if (Files.notExists(userPath)) {
                      try {
                        Files.createFile(userPath);
                      } catch (IOException e) {
                        LOGGER.error(e.getMessage(), e);
                      }
                    }

                    try (BufferedReader reader =
                        Files.newBufferedReader(userPath, StandardCharsets.UTF_8)) {
                      result = gson.fromJson(reader, UserInfo.class);
                    } catch (IOException e) {
                      LOGGER.error(e.getMessage(), e);
                    }

                    if (result == null) {
                      result = new UserInfo();
                    }
                    return result;
                  }
                });
  }

  public UserInfo getUserInfo(long userId) {
    return userCache.get(userId);
  }

  public void save() {
    userCache.invalidateAll();
  }

  public Path getStoragePath() {
    return Paths.get("userdata").toAbsolutePath();
  }
}
