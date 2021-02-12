package me.conclure.derpio.event;

import java.util.concurrent.ThreadLocalRandom;
import me.conclure.derpio.Bot;
import me.conclure.derpio.BotInfo;
import me.conclure.derpio.model.user.UserData;
import me.conclure.derpio.model.user.UserManager;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

public final class AutomatedExpFactory {

  private final Bot bot;

  public AutomatedExpFactory(Bot bot) {
    this.bot = bot;
  }

  @SubscribeEvent
  public void onMessageReceived(GuildMessageReceivedEvent event) {
    User user = event.getAuthor();

    // ensures sender is not bot
    if (user.isBot()) {
      return;
    }

    // ensures message is not a webhook
    if (event.isWebhookMessage()) {
      return;
    }

    // ensures guild id is the target guild
    if (event.getGuild().getIdLong() != BotInfo.GUILD_ID) {
      return;
    }

    String messageContent = event.getMessage().getContentRaw();

    // ensures message is not a command
    if (messageContent.startsWith(BotInfo.PREFIX)) {
      return;
    }

    UserManager userManager = this.bot.getUserManager();
    long userId = user.getIdLong();
    UserData userData = userManager.getUserData(userId);

    userData.claimChatExp(ThreadLocalRandom.current());
  }
}
