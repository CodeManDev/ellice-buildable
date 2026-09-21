package dev.felix.ellice.feature.task;

public final class TaskDayNumberService {
  private TaskDayNumberService() {}

  public static long dayNumber(long longValue) {
    return Math.floorDiv(longValue, 24000L);
  }

  public static boolean isNight(long longValue) {
    long currentLongValue = Math.floorMod(longValue, 24000L);
    return currentLongValue >= 13000L && currentLongValue <= 23000L;
  }
}
