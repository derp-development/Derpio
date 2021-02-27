package me.conclure.derpio.model.user;

import java.util.concurrent.TimeUnit;
import me.conclure.derpio.Bot;
import me.conclure.derpio.command.CommandHandler;
import me.conclure.derpio.util.collection.LoadingMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UserManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(CommandHandler.class);

  private final LoadingMap<Long, UserData> map;
  private final UserHousekeeper housekeeper;

  public UserManager(Bot bot) {
    this.map = new LoadingMap<>(UserData::new);
    this.housekeeper = new UserHousekeeper(1, TimeUnit.MINUTES,this);
    bot.scheduler().asyncRepeating(this.housekeeper,30,TimeUnit.SECONDS);
  }

  public LoadingMap<Long, UserData> getMap() {
    return this.map;
  }

  public UserData getOrMake(Long userId) {
    return this.map.get(userId);
  }

  public UserData getIfLoaded(Long userId) {
    return this.map.getIfPresent(userId);
  }

  public boolean isLoaded(Long userId) {
    return this.map.getIfPresent(userId) != null;
  }

  public void unload(Long userId) {
    this.map.remove(userId);
  }
}
