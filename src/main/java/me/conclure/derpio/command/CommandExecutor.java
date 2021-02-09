package me.conclure.derpio.command;

import me.conclure.derpio.Bot;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface CommandExecutor {

  String getName();

  Result execute(Bot bot, MessageReceivedEvent event, String[] args);

  enum Result {
    SUCCESS,
    UNEXPECTED_ERROR,
    NO_PERMISSION
  }

  class Exception extends RuntimeException {

    Exception(Throwable cause) {
      super(cause.getMessage(), cause);
    }
  }
}
