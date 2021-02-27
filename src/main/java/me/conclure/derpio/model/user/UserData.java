package me.conclure.derpio.model.user;

public final class UserData {
  private final long userId;
  private long xp;

  public UserData(long userId) {
    this.userId = userId;
  }

  public long getXP() {
    return this.xp;
  }

  public void setXP(long xp) {
    this.xp = xp;
  }

  /*
  @Expose private final ExpStorage expStorage;
  @Expose private final DailyManager dailyManager;
  @Expose private final ChatExpManager chatExpManager;

  public UserData(ExpStorage expStorage, ChatExpManager chatExpManager, DailyManager dailyManager) {
    this.expStorage = expStorage;
    this.chatExpManager = chatExpManager;
    this.dailyManager = dailyManager;
  }

  public static UserData newDefault() {
    return new UserData(
        ExpStorage.newDefault(), ChatExpManager.newDefault(), DailyManager.newDefault());
  }

  public ExpStorage getExpStorage() {
    return this.expStorage;
  }

  public long getExp() {
    return this.expStorage.getExp();
  }

  public void increaseExp(long amount) {
    this.expStorage.addExp(amount);
  }

  public long claimDailyExp() {
    boolean claimed = this.dailyManager.redeem();

    if (claimed) {
      this.increaseExp(BotInfo.DAILY_EXP_AMOUNT);
    }

    return claimed ? 0L : this.dailyManager.getCurrentSession().getTimeUntilExpire();
  }

  public void claimChatExp(Random random) {
    if (this.chatExpManager.redeem()) {
      long amount = random.nextInt(BotInfo.CHAT_EXP_MAX) + BotInfo.CHAT_EXP_MIN;
      this.increaseExp(amount);
    }
  }

  public void reset() {
    this.expStorage.reset();
    this.dailyManager.reset();
    this.chatExpManager.reset();
  }

   */
}
