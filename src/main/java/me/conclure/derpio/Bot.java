package me.conclure.derpio;

import javax.security.auth.login.LoginException;
import joptsimple.OptionSet;
import me.conclure.derpio.command.CommandManager;
import me.conclure.derpio.event.MessageReceivedListener;
import me.conclure.derpio.storage.UserManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Bot {
  private static final Logger LOGGER = LoggerFactory.getLogger(Bot.class);

  private final JDA jda;
  private final CommandManager commandManager;
  private final UserManager userManager;

  private Bot(String token) throws LoginException, InterruptedException {
    this.jda =
        JDABuilder.createDefault(token)
            .setAutoReconnect(true)
            .setEventManager(new AnnotatedEventManager())
            .build()
            .awaitReady();
    this.commandManager = new CommandManager(this);
    this.userManager = new UserManager();
    this.jda.addEventListener(new MessageReceivedListener(bot));
  }

  static void init(OptionSet optionSet) {
    try {
      String token = (String) optionSet.valueOf("token");
      Thread thread =
          new Thread(
              () -> {
                try {
                  new Bot(token);
                } catch (LoginException | InterruptedException e) {
                  throw new RuntimeException(e);
                }
              },
              "Bot thread");
      thread.setUncaughtExceptionHandler((t, e) -> LOGGER.error(e.getMessage(), e));
      thread.start();
    } catch (Exception e) {
      LOGGER.error("Failed bootstrap", e);
    }
  }

  private void stop() {
    this.userManager.save();
    this.jda.shutdown();
  }

  public void shutdown() {
    this.stop();
    System.exit(0);
  }

  public JDA getJDA() {
    return jda;
  }

  public UserManager getUserManager() {
    return userManager;
  }
}
