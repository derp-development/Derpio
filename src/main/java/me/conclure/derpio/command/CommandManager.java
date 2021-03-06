package me.conclure.derpio.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import me.conclure.derpio.Bot;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public abstract class CommandManager extends CommandExecutor {
  private final Map<String, CommandExecutor> commandMap;

  protected CommandManager() {
    this.commandMap = new HashMap<>();
    this.registerCommandExecutors()
        .forEach(
            executor -> {
              this.register(executor.getName(), executor);
              String[] aliases = executor.getAliases();

              if (aliases == null) {
                return;
              }

              if (aliases.length == 0) {
                return;
              }

              for (String alias : aliases) {
                this.register(alias, executor);
              }
            });
  }

  private void register(String string, CommandExecutor executor) {
    this.commandMap.put(string, executor);
  }

  protected abstract Stream<CommandExecutor> registerCommandExecutors();

  @Override
  protected Result execute(Bot bot, GuildMessageReceivedEvent event, String[] args) {
    if (args.length == 0) {
      return this.execute(bot, event);
    }

    CommandExecutor executor = this.commandMap.get(args[0].toLowerCase());

    if (executor == null) {
      return ResultType.UNKNOWN_ARGUMENT.toResult();
    }

    return executor.execute(bot, event, Arrays.copyOfRange(args, 1, args.length));
  }

  protected Result execute(Bot bot, GuildMessageReceivedEvent event) {
    return ResultType.MISSING_ARGUMENT.toResult();
  }
}
