package me.conclure.derpio.storage;

import java.nio.file.Path;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;

public interface ConfigLoader {

  ConfigurationLoader<? extends ConfigurationNode> loader(Path path);

}
