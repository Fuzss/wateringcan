package fuzs.wateringcan;

import fuzs.puzzleslib.config.AbstractConfig;
import fuzs.puzzleslib.config.ConfigHolder;
import fuzs.puzzleslib.config.ConfigHolderImpl;
import fuzs.puzzleslib.core.EnvTypeExecutor;
import fuzs.puzzleslib.network.MessageDirection;
import fuzs.puzzleslib.network.NetworkHandler;
import fuzs.wateringcan.config.ServerConfig;
import fuzs.wateringcan.data.ModLanguageProvider;
import fuzs.wateringcan.data.ModRecipeProvider;
import fuzs.wateringcan.handler.CanInteractionHandler;
import fuzs.wateringcan.init.ModRegistry;
import fuzs.wateringcan.network.S2CBlockSprinklesMessage;
import fuzs.wateringcan.network.S2CEntitySprinklesMessage;
import fuzs.wateringcan.proxy.ClientProxy;
import fuzs.wateringcan.proxy.IProxy;
import fuzs.wateringcan.proxy.ServerProxy;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(WateringCan.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class WateringCan {
    public static final String MOD_ID = "wateringcan";
    public static final String MOD_NAME = "Watering Can";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public static final NetworkHandler NETWORK = NetworkHandler.of(MOD_ID);
    @SuppressWarnings("Convert2MethodRef")
    public static final ConfigHolder<AbstractConfig, ServerConfig> CONFIG = ConfigHolder.server(() -> new ServerConfig());
    @SuppressWarnings("Convert2MethodRef")
    public static final IProxy PROXY = EnvTypeExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ((ConfigHolderImpl<?, ?>) CONFIG).addConfigs(MOD_ID);
        ModRegistry.touch();
        registerHandlers();
        registerMessages();
    }

    private static void registerHandlers() {
        MinecraftForge.EVENT_BUS.addListener(CanInteractionHandler::onEntityInteract);
    }

    private static void registerMessages() {
        NETWORK.register(S2CBlockSprinklesMessage.class, S2CBlockSprinklesMessage::new, MessageDirection.TO_CLIENT);
        NETWORK.register(S2CEntitySprinklesMessage.class, S2CEntitySprinklesMessage::new, MessageDirection.TO_CLIENT);
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent evt) {
        DataGenerator generator = evt.getGenerator();
        final ExistingFileHelper existingFileHelper = evt.getExistingFileHelper();
        generator.addProvider(new ModRecipeProvider(generator));
        generator.addProvider(new ModLanguageProvider(generator, MOD_ID));
    }
}
