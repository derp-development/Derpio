package me.conclure.derpio.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import me.conclure.derpio.Bot;
import me.conclure.derpio.BotInfo;
import me.conclure.derpio.command.CommandExecutor.Result;
import me.conclure.derpio.command.commands.HelpCommand;
import me.conclure.derpio.command.commands.StopCommand;
import me.conclure.derpio.command.commands.UserInfoCommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CommandManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(CommandManager.class);

  private final Map<String, CommandExecutor> commandMap;

  public CommandManager(Bot bot) {
    this.commandMap = new HashMap<>();

    // register commands
    Stream.of(new HelpCommand(), new StopCommand(), new UserInfoCommand())
        .forEach(executor -> commandMap.put(executor.getName().toLowerCase(), executor));

    // register event listener
    bot.getJDA()
        .addEventListener(
            new Object() {
              @SubscribeEvent
              public void onMessageReceived(MessageReceivedEvent event) {
                if (event.getAuthor().isBot()) {
                  return;
                }

                if (!event.isFromGuild()) {
                  return;
                }

                if (event.getGuild().getIdLong() != BotInfo.GUILD_ID) {
                  return;
                }

                String content = event.getMessage().getContentRaw();

                if (!content.startsWith(BotInfo.PREFIX)) {
                  return;
                }

                String[] line = StringUtils.split(content, " ");
                String node = line[0].substring(1);
                CommandExecutor executor = commandMap.get(node.toLowerCase());

                if (executor == null) {
                  return;
                }

                CommandExecutor.Result result = Result.UNEXPECTED_ERROR;

                try {
                  result = executor.execute(bot, event, Arrays.copyOfRange(line, 1, line.length));
                } catch (Exception e) {
                  e = new CommandExecutor.Exception(e);
                  LOGGER.error(e.getMessage(), e);
                } finally {
                  LOGGER.debug("Command: " + node + " - " + result);
                }

                MessageChannel channel = event.getChannel();

                switch (result) {
                  case UNEXPECTED_ERROR:
                    {
                      channel
                          .sendMessage("An error occurred whilst performing this command.")
                          .delay(10, TimeUnit.SECONDS)
                          .flatMap(Message::delete)
                          .queue();
                      break;
                    }
                  case NO_PERMISSION:
                    {
                      channel
                          .sendMessage("You are not permitted to perform this command.")
                          .delay(10, TimeUnit.SECONDS)
                          .flatMap(Message::delete)
                          .queue();
                      break;
                    }
                  case SUCCESS:
                    {
                      // TODO
                    }
                }
              }
            });
  }
}
