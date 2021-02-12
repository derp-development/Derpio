package me.conclure.derpio.model.user;

import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import me.conclure.derpio.BotInfo;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UserRemovalListener implements RemovalListener<Long, UserData> {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserRemovalListener.class);

  private final UserManager userManager;

  UserRemovalListener(UserManager userManager) {
    this.userManager = userManager;
  }

  @Override
  public void onRemoval(
      @Nullable Long userId, @Nullable UserData userData, @NonNull RemovalCause cause) {
    // resolves a path for the specified user
    Path userPath = this.userManager.getStoragePath().resolve(userId + ".json");

    // if a file for the user does not exist then we create it
    if (!Files.exists(userPath)) {
      try {
        Files.createFile(userPath);
      } catch (IOException e) {
        LOGGER.error(e.getMessage(), e);
      }
    }

    // parse the data into json and write to the file
    try (var writer = Files.newBufferedWriter(userPath, StandardCharsets.UTF_8)) {
      System.out.println(1);
      BotInfo.GSON.toJson(userData, writer);
      writer.flush();
      System.out.println(2);
    } catch (Throwable e) {
      LOGGER.error(e.getMessage(), e);
    }
  }
}
