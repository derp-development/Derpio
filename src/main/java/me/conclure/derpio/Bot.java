package me.conclure.derpio;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.security.auth.login.LoginException;
import joptsimple.OptionSet;
import me.conclure.derpio.command.CommandHandler;
import me.conclure.derpio.concurrent.DaemonScheduler;
import me.conclure.derpio.concurrent.SynchronousExecutor;
import me.conclure.derpio.event.AutomatedExpFactory;
import me.conclure.derpio.model.user.UserManager;
import me.conclure.derpio.storage.StorageProvider;
import me.conclure.derpio.util.ExecutorServiceUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDA.Status;
import net.dv8tion.jda.api.JDABuilder;
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
  private final StorageProvider storageProvider;
  private final CountDownLatch latch;
  private final DaemonScheduler scheduler;
  private final SynchronousExecutor synchronousExecutor;
  private final ForkJoinPool asynchronousExecutor;

  private Bot(String token) throws Exception {
    this.latch = new CountDownLatch(1);
    this.synchronousExecutor = new SynchronousExecutor();
    this.asynchronousExecutor =
        new ForkJoinPool(
            32,
            ForkJoinPool.defaultForkJoinWorkerThreadFactory,
            (t, e) -> LOGGER.error(e.getMessage(),e),
            false);
    this.scheduler = new DaemonScheduler();
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
            .build();

    this.synchronousExecutor.execute(this.jda::awaitReady);
    this.jda.getPresence().setActivity(Activity.watching("you! :>)"));

    this.userManager = new UserManager();
    this.storageProvider = new StorageProvider(this);

    this.registerEventListeners();

    this.latch.countDown();
    LOGGER.info("Bot ready!");
  }

  static void init(OptionSet optionSet) {
    try {
      // getting token from arguments
      String token = (String) optionSet.valueOf("token");
      // load bot on new thread
      new Bot(token);
    } catch (Exception e) {
      LOGGER.error("Failed bootstrap", e);
    }
  }

  private void registerEventListeners() {
    Stream.of(
        new AutomatedExpFactory(this),
        new CommandHandler(this)
    ).forEach(this.jda::addEventListener);
  }

  private void stop() {
    this.synchronousExecutor.execute(
        () -> {
          // start by shutting down jda as it may be mandatory to suppress other simulations
          this.jda.shutdown();

          while (true) {
            if (this.jda.getStatus() == Status.SHUTDOWN) {
              break;
            }
          }

          // make sure to forcefully shutdown jda
          this.jda.shutdownNow();

          // shutdown scheduler so no more tasks can be scheduled
          this.scheduler.shutdownScheduler();

          // shutdown asynchronous worker
          ExecutorServiceUtil.awaitTermination(this.asynchronousExecutor, LOGGER);

          try {
            //noinspection ResultOfMethodCallIgnored
            ForkJoinPool.commonPool().awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
          } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
          }

          // shutdown the scheduler executor so no more scheduled tasks can run
          this.scheduler.shutdownExecutor();

          // shut down sync executor lastly
          this.synchronousExecutor.shutdown();
        });
  }

  public void shutdown() {
    this.stop();

    try {
      this.synchronousExecutor.awaitTermination();
      LOGGER.info("Goodbye! :)");
    } catch (InterruptedException e) {
      LOGGER.error(e.getMessage(), e);
    }

    System.exit(0);
  }

  public void restart(Consumer<? super Bot> callback) {
    this.stop();
    Bot bot = null;

    try {
      this.synchronousExecutor.awaitTermination();
      bot = new Bot(this.token);
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
    }

    if (bot != null) {
      callback.accept(bot);
      return;
    }

    throw new Error();
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

  public ForkJoinPool asyncExecutor() {
    return this.asynchronousExecutor;
  }

  public SynchronousExecutor syncExecutor() {
    return this.synchronousExecutor;
  }

  public DaemonScheduler scheduler() {
    return this.scheduler;
  }

  public JDA jda() {
    return this.jda;
  }

  public UserManager userManager() {
    return this.userManager;
  }

  public StorageProvider storage() {
    return this.storageProvider;
  }
}
