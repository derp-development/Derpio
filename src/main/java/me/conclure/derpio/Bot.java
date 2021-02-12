package me.conclure.derpio;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.security.auth.login.LoginException;
import joptsimple.OptionSet;
import me.conclure.derpio.command.CommandHandler;
import me.conclure.derpio.event.AutomatedExpFactory;
import me.conclure.derpio.model.user.UserManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDA.Status;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Bot {
  private static final Logger LOGGER = LoggerFactory.getLogger(Bot.class);

  private final String token;
  private final JDA jda;
  private final UserManager userManager;
  private final CountDownLatch latch;

  private Bot(String token) throws LoginException, InterruptedException {
    this.latch = new CountDownLatch(1);
    this.token = token;
    this.jda =
        JDABuilder.createDefault(token)
            .setAutoReconnect(true)
            .setEventManager(new AnnotatedEventManager())
            .addEventListeners(
                new Object() {
                  @SubscribeEvent
                  public void onReady(ReadyEvent event) {
                    LOGGER.info("JDA finished bootstrapping.");
                  }
                })
            .build()
            .awaitReady();
    this.jda.getPresence().setActivity(Activity.watching("you! :>)"));
    this.userManager = new UserManager();
    this.registerEventListeners();
    this.jda.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
    this.latch.countDown();
    LOGGER.info("Bot ready!");
  }

  static void init(OptionSet optionSet) {
    try {
      // getting token from arguments
      String token = (String) optionSet.valueOf("token");
      // load bot on new thread
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
    Stream.of(new AutomatedExpFactory(this), new CommandHandler(this))
        .forEach(this.jda::addEventListener);
  }

  private void stop(Runnable callback) {
    this.userManager.terminate();
    this.jda.shutdown();

    while (true) {
      if (this.jda.getStatus() == Status.SHUTDOWN) {
        break;
      }
    }

    this.jda.shutdownNow();

    try {
      //noinspection ResultOfMethodCallIgnored
      ForkJoinPool.commonPool().awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    } catch (InterruptedException e) {
      LOGGER.error(e.getMessage(), e);
    } finally{
      callback.run();
    }
  }

  public void shutdown() {
    this.stop(() -> System.exit(0));
    LOGGER.info("Goodbye! :)");
  }

  public void restart(Consumer<JDA> callback) {
    Runnable startFunction =
        () -> {
          Bot bot = null;

          try {
            bot = new Bot(this.token);
          } catch (LoginException | InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
          }

          if (bot != null) {
            callback.accept(bot.getJDA());
          }
        };

    this.stop(startFunction);
  }

  public JDA getJDA() {
    return this.jda;
  }

  public UserManager getUserManager() {
    return this.userManager;
  }

  public boolean isReady() {
    return this.latch.getCount() == 0;
  }

  public void awaitReady() {
    try {
      this.latch.await();
    } catch (InterruptedException e) {
      LOGGER.error(e.getMessage(), e);
    }
  }
}
