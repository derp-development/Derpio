package me.conclure.derpio.storage;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import me.conclure.derpio.Bot;
import me.conclure.derpio.model.user.UserData;
import me.conclure.derpio.util.FilesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class StorageProvider {
  private static final Logger LOGGER = LoggerFactory.getLogger(FilesUtil.class);

  private final Bot bot;
  private final Storage storage;

  public StorageProvider(Bot bot) throws IOException {
    this.bot = bot;
    this.storage = new FileStorage(new JsonLoader(), bot);
  }

  public CompletableFuture<UserData> loadUser(Long userId) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        return this.storage.loadUser(userId);
      } catch (Exception e) {
        throw new CompletionException(e);
      }
    }, this.bot.asyncExecutor());
  }

  public CompletableFuture<Void> saveUser(UserData userData) {
    return CompletableFuture.runAsync(() -> {
      try {
        this.storage.saveUser(userData);
      } catch (Exception e) {
        throw new CompletionException(e);
      }
    }, this.bot.asyncExecutor());
  }
}