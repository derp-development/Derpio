package me.conclure.derpio.model.user.content;

import me.conclure.derpio.BotInfo;
import me.conclure.derpio.util.Session;

public final class ChatExpManager extends RedeemableSessionManager {

  public ChatExpManager(Session session) {
    super(session);
  }

  public static ChatExpManager newDefault() {
    return new ChatExpManager(Session.NULL_SESSION);
  }

  @Override
  protected Session supplyRenewedSession() {
    long duration = BotInfo.CHAT_EXP_COOLDOWN_UNIT.toMillis(BotInfo.CHAT_EXP_COOLDOWN_DURATION);

    return new Session(System.currentTimeMillis(), duration);
  }
}
