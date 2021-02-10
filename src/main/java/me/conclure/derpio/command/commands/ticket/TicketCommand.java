package me.conclure.derpio.command.commands.ticket;

import java.util.stream.Stream;
import me.conclure.derpio.command.CommandExecutor;
import me.conclure.derpio.command.CommandManager;

public final class TicketCommand extends CommandManager {

  @Override
  protected String getName() {
    return "ticket";
  }

  @Override
  protected Stream<CommandExecutor> registerCommandExecutors() {
    return Stream.of(new NewTicketCommand());
  }
}
