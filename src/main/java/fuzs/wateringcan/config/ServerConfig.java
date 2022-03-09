package fuzs.wateringcan.config;

import fuzs.puzzleslib.config.AbstractConfig;
import fuzs.puzzleslib.config.annotation.Config;

public class ServerConfig extends AbstractConfig {
    @Config(description = "Amount of inventory rows a leather bag of holding has.")
    @Config.IntRange(min = 1, max = 9)
    public int leatherBagRows = 1;

    public ServerConfig() {
        super("");
    }
}
