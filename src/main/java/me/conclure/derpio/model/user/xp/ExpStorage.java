package me.conclure.derpio.model.user.xp;

import com.google.gson.annotations.Expose;

public final class ExpStorage {

  @Expose private long exp;

  public ExpStorage(long exp) {
    this.exp = exp;
  }

  public static ExpStorage newDefault() {
    return new ExpStorage(0);
  }

  public long getExp() {
    return this.exp;
  }

  public void setExp(long xp) {
    this.exp = xp;
  }

  public void addExp(long xp) {
    this.setExp(this.getExp() + xp);
  }

  public void reset() {
    this.setExp(0);
  }
}
