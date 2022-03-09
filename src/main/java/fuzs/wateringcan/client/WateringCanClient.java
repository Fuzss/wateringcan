package fuzs.wateringcan.client;

import fuzs.wateringcan.WateringCan;
import fuzs.wateringcan.registry.ModRegistry;
import fuzs.wateringcan.world.item.WateringCanItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod.EventBusSubscriber(modid = WateringCan.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class WateringCanClient {
    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        registerHandlers();
    }

    private static void registerHandlers() {

    }

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent evt) {
        ItemProperties.register(ModRegistry.WATERING_CAN_ITEM.get(), new ResourceLocation("fill_level"), (stack, level, entity, seed) -> {
            return WateringCanItem.getAmountFilled(stack);
        });
    }
}
