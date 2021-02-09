package me.conclure.derpio.storage;

import com.google.gson.annotations.Expose;

public final class UserInfo {

  @Expose private long xp;
  @Expose private long lastMessageTimestamp;

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
    this.setLastMessageTimestamp(0L);
  }

  public long getLastMessageTimestamp() {
    return lastMessageTimestamp;
  }

  public void setLastMessageTimestamp(long timestamp) {
    this.lastMessageTimestamp = timestamp;
  }

  public void updateLastMessageTimestamp() {
    this.setLastMessageTimestamp(System.currentTimeMillis());
  }
}
