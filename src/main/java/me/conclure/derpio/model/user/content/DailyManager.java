package me.conclure.derpio.model.user.content;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import me.conclure.derpio.BotInfo;
import me.conclure.derpio.util.Session;

public final class DailyManager extends RedeemableSessionManager {
  @Expose private long streak;

  public DailyManager(long streak, Session session) {
    super(session);
    this.streak = streak;
  }

  public static DailyManager newDefault() {
    return new DailyManager(0L, Session.NULL_SESSION);
  }

  public long getStreak() {
    return this.streak;
  }

  public void setStreak(long streak) {
    this.streak = streak;
  }

  public void addStreak(long amount) {
    this.setStreak(this.getStreak() + amount);
  }

  @Override
  protected Session supplyRenewedSession() {
    long duration = BotInfo.DAILY_EXP_SESSION_UNIT.toMillis(BotInfo.DAILY_EXP_SESSION_DURATION);

    return new Session(System.currentTimeMillis(), duration);
  }

  @Override
  public boolean redeem() {
    boolean renew = super.redeem();

    if (renew) {
      this.streak++;
    }

    return renew;
  }

  @Override
  public void reset() {
    super.reset();
    this.setStreak(0);
  }
}
