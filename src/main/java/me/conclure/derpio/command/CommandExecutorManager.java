package me.conclure.derpio.command;

import me.conclure.derpio.Bot;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class CommandExecutorManager extends CommandExecutor {

  @Override
  protected Result execute(Bot bot, MessageReceivedEvent event, String[] args) {

    return null;
  }
}
