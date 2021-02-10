package me.conclure.derpio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import java.util.concurrent.TimeUnit;

//use interface to declare constants with reduced boilerplate
public interface BotInfo {
  String PREFIX = "/";

  long OWNER_ID = 299969655915806741L;
  long GUILD_ID = 801898583393697802L;

  TimeUnit USER_EXPIRE_ACCESS_UNIT = TimeUnit.MINUTES;
  long USER_EXPIRE_ACCESS_DURATION = 5;

  TimeUnit CHAT_XP_COOLDOWN_UNIT = TimeUnit.MINUTES;
  long CHAT_XP_COOLDOWN_DURATION = 2;
  int CHAT_XP_MIN = 1;
  int CHAT_XP_MAX = 20;

  Gson GSON =
      new GsonBuilder()
          .serializeNulls()
          .setPrettyPrinting()
          .disableHtmlEscaping()
          .excludeFieldsWithoutExposeAnnotation()
          .disableInnerClassSerialization()
          .enableComplexMapKeySerialization()
          .setLenient()
          .setLongSerializationPolicy(LongSerializationPolicy.DEFAULT)
          .create();
}
