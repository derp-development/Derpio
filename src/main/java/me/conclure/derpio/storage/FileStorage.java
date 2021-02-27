package me.conclure.derpio.storage;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import me.conclure.derpio.Bot;
import me.conclure.derpio.model.user.UserData;
import me.conclure.derpio.util.FilesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;

public final class FileStorage implements Storage {
  private static final Logger LOGGER = LoggerFactory.getLogger(FileStorage.class);

  private final ConfigLoader loader;
  private final LoadingCache<Path, ReentrantLock> ioLocks;
  private final Bot bot;
  private final Path dataDirectory;

  public FileStorage(
      ConfigLoader loader,
      Bot bot) throws IOException {
    this.loader = loader;
    this.dataDirectory = Paths.get("userdata");
    FilesUtil.createDirsIfNotExist(this.dataDirectory);

    this.bot = bot;
    this.ioLocks =
        Caffeine.newBuilder()
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .build(key -> new ReentrantLock());
  }

  private ConfigurationNode readFile(Long userId) throws ConfigurateException {
    Path userPath = this.dataDirectory.resolve(userId + ".json");
    ReentrantLock lock = this.ioLocks.get(userPath);
    Objects.requireNonNull(lock);

    lock.lock();
    try {
      if (!Files.exists(userPath)) {
        return null;
      }

      return this.loader.loader(userPath).load();
    } finally {
      lock.unlock();
    }
  }

  @Override
  public UserData loadUser(Long userId) {
    UserData userData = this.bot.userManager().getOrMake(userId);

    try {
      ConfigurationNode data = this.readFile(userId);

      if (data != null) {
        long xp = data.node("xp").getLong();

        userData.setXP(xp);
      }

    } catch (Exception e) {
      LOGGER.error(e.getMessage(),e);
    }

    return userData;
  }

  @Override
  public void saveUser(UserData userData) {

  }
}
