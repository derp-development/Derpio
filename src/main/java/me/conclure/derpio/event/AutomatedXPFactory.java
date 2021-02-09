package me.conclure.derpio.event;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import me.conclure.derpio.Bot;
import me.conclure.derpio.BotInfo;
import me.conclure.derpio.storage.UserData;
import me.conclure.derpio.storage.UserManager;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

public final class AutomatedXPFactory {

  private final Bot bot;

  public AutomatedXPFactory(Bot bot) {
    this.bot = bot;
  }

  @SubscribeEvent
  public void onMessageReceived(MessageReceivedEvent event) {
    User user = event.getAuthor();

    if (user.isBot()) {
      return;
    }

    if (!event.isFromGuild()) {
      return;
    }

    if (event.isWebhookMessage()) {
      return;
    }

    if (event.getGuild().getIdLong() != BotInfo.GUILD_ID) {
      return;
    }

    UserManager userManager = bot.getUserManager();
    long userId = user.getIdLong();
    UserData userData = userManager.getUserInfo(userId);

    if (!userData.hasChatClaimableXp()) {
      return;
    }

    Random random = ThreadLocalRandom.current();
    int amount = random.nextInt(BotInfo.CHAT_XP_MAX) + BotInfo.CHAT_XP_MIN;
    userData.addXP(amount);
    userData.updateLastXpMessageTimestamp();
  }
}
