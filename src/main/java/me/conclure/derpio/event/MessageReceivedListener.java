package me.conclure.derpio.event;

import me.conclure.derpio.Bot;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

public final class MessageReceivedListener {

  private final Bot bot;

  public MessageReceivedListener(Bot bot) {
    this.bot = bot;
  }

  @SubscribeEvent
  public void onMessageReceived(MessageReceivedEvent event) {}
}
