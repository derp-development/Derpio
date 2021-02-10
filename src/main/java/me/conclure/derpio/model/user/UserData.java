package me.conclure.derpio.model.user;

import com.google.gson.annotations.Expose;
import java.util.concurrent.TimeUnit;
import me.conclure.derpio.BotInfo;

public final class UserData {

  @Expose private long xp;
  @Expose private long lastXpMessageTimestamp;

  public void addXP(long amount) {
    this.setXP(xp + amount);
  }

  public long getXP() {
    return xp;
  }

  public void setXP(long amount) {
    this.xp = amount;
  }

  public void reset() {
    this.setXP(0L);
    this.setLastXpMessageTimestamp(0L);
  }

  public boolean hasChatClaimableXp() {
    long now = System.currentTimeMillis();
    long last = lastXpMessageTimestamp;
    long difference = now - last;

    TimeUnit cooldownUnit = BotInfo.CHAT_XP_COOLDOWN_UNIT;
    long cooldownDuration = BotInfo.CHAT_XP_COOLDOWN_DURATION;

    long minimumDifference = cooldownUnit.toMillis(cooldownDuration);

    return minimumDifference <= difference;
  }

  public long getLastXpMessageTimestamp() {
    return lastXpMessageTimestamp;
  }

  public void setLastXpMessageTimestamp(long timestamp) {
    this.lastXpMessageTimestamp = timestamp;
  }

  public void updateLastXpMessageTimestamp() {
    this.setLastXpMessageTimestamp(System.currentTimeMillis());
  }
}
