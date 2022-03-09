package fuzs.wateringcan.data;

import fuzs.wateringcan.registry.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(DataGenerator gen, String modid) {
        super(gen, modid, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.add(ModRegistry.WATERING_CAN_ITEM.get(), "Watering Can");
        this.add(ModRegistry.RANGE_ENCHANTMENT.get(), "Range");
        this.add(ModRegistry.CAPACITY_ENCHANTMENT.get(), "Capacity");
        this.add(ModRegistry.FERTILITY_ENCHANTMENT.get(), "Fertility");
        this.add("item.wateringcan.watering_can.fill_level", "Fill Level: %s / %s");
        this.add("enchantment.wateringcan.range.desc", "Prevents a bag of holding from being lost on death. The enchantment level is reduced by one each time.");
        this.add("enchantment.wateringcan.capacity.desc", "Prevents a bag of holding from being lost on death. The enchantment level is reduced by one each time.");
        this.add("enchantment.wateringcan.fertility.desc", "Prevents a bag of holding from being lost on death. The enchantment level is reduced by one each time.");
    }
}
