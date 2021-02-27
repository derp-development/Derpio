package me.conclure.derpio.concurrent;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import me.conclure.derpio.util.ExecutorServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DaemonScheduler {
  private static final Logger LOGGER = LoggerFactory.getLogger(DaemonScheduler.class);
  private final ScheduledThreadPoolExecutor scheduler;
  private final ExecutorService schedulerWorkerPool;

  public DaemonScheduler() {
    this.scheduler =
        new ScheduledThreadPoolExecutor(
            1,
            new ThreadFactoryBuilder().setDaemon(true).setNameFormat("daemon-scheduler").build());
    this.scheduler.setRemoveOnCancelPolicy(true);
    this.schedulerWorkerPool =
        Executors.newCachedThreadPool(
            new ThreadFactoryBuilder()
                .setDaemon(true)
                .setNameFormat("daemon-scheduler-worker-%d")
                .build());
  }

  public ScheduledFuture<?> asyncLater(Runnable task, long delay, TimeUnit unit) {
    return this.scheduler.schedule(() -> this.schedulerWorkerPool.execute(task), delay, unit);
  }

  public ScheduledFuture<?> asyncRepeating(Runnable task, long interval, TimeUnit unit) {
    return this.scheduler.scheduleAtFixedRate(
        () -> this.schedulerWorkerPool.execute(task), interval, interval, unit);
  }

  public void shutdownScheduler() {
    ExecutorServiceUtil.awaitTermination(this.scheduler, LOGGER);
  }

  public void shutdownExecutor() {
    ExecutorServiceUtil.awaitTermination(this.schedulerWorkerPool, LOGGER);
  }
}
