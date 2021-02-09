package me.conclure.derpio;

import java.util.stream.Stream;
import javax.security.auth.login.LoginException;
import joptsimple.OptionSet;
import me.conclure.derpio.command.CommandManager;
import me.conclure.derpio.event.AutomatedXPFactory;
import me.conclure.derpio.storage.UserManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Bot {
  private static final Logger LOGGER = LoggerFactory.getLogger(Bot.class);

  private final JDA jda;
  private final UserManager userManager;

  private Bot(String token) throws LoginException, InterruptedException {
    this.jda =
        JDABuilder.createDefault(token)
            .setAutoReconnect(true)
            .setEventManager(new AnnotatedEventManager())
            .build()
            .awaitReady();
    this.userManager = new UserManager();
    this.registerEventListeners();
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

  private void registerEventListeners() {
    Stream.of(new AutomatedXPFactory(this), new CommandManager(this))
        .forEach(jda::addEventListener);
  }

  private void stop() {
    this.userManager.saveAll();
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
