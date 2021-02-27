package me.conclure.derpio.storage;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;

public final class JsonLoader implements ConfigLoader {

  @Override
  public ConfigurationLoader<? extends ConfigurationNode> loader(Path path) {
    return GsonConfigurationLoader.builder()
        .indent(2)
        .source(() -> Files.newBufferedReader(path, StandardCharsets.UTF_8))
        .sink(() -> Files.newBufferedWriter(path, StandardCharsets.UTF_8))
        .build();
  }
}
