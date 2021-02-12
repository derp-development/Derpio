package me.conclure.derpio.util;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnegative;

public final class Session {
  public static final Session NULL_SESSION = new Session(0L, 0L);

  @Expose private final long initialTimestamp;
  @Expose private final long duration;

  public Session(long initialTimestamp, long duration) {
    this.initialTimestamp = initialTimestamp;
    this.duration = duration;
  }

  public static Session create(long initialTimestamp, long duration, TimeUnit unit) {
    return new Session(unit.toMillis(initialTimestamp), unit.toMillis(duration));
  }

  public long getDuration() {
    return this.duration;
  }

  public long getEndTimestamp() {
    return this.initialTimestamp + this.duration;
  }

  public long getInitialTimestamp() {
    return this.initialTimestamp;
  }

  @Nonnegative
  public long getTimeUntilExpire() {
    long difference = this.getEndTimestamp() - System.currentTimeMillis();

    if (difference <= 0) {
      return 0;
    }

    return difference;
  }

  public boolean isExpired() {
    return this.getEndTimestamp() < System.currentTimeMillis();
  }
}
