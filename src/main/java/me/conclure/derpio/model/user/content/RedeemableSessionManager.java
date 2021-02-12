package me.conclure.derpio.model.user.content;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import me.conclure.derpio.util.Session;

public abstract class RedeemableSessionManager {
  @Expose protected Session session;

  protected RedeemableSessionManager(Session session) {
    this.session = session;
  }

  protected abstract Session supplyRenewedSession();

  public Session getCurrentSession() {
    return this.session;
  }

  public boolean isRedeemable() {
    return this.session.isExpired();
  }

  public boolean redeem() {
    boolean renew = this.isRedeemable();

    if (renew) {
      this.session = this.supplyRenewedSession();
    }

    return renew;
  }

  public void reset() {
    this.session = Session.NULL_SESSION;
  }
}
