package fuzs.wateringcan.registry;

import fuzs.puzzleslib.registry.RegistryManager;
import fuzs.wateringcan.WateringCan;
import fuzs.wateringcan.world.item.enchantment.CapacityEnchantment;
import fuzs.wateringcan.world.item.enchantment.FertilityEnchantment;
import fuzs.wateringcan.world.item.enchantment.RangeEnchantment;
import fuzs.wateringcan.world.item.WateringCanItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.RegistryObject;

import java.util.Locale;

public class ModRegistry {
    private static final RegistryManager REGISTRY = RegistryManager.of(WateringCan.MOD_ID);
    public static final RegistryObject<Item> WATERING_CAN_ITEM = REGISTRY.registerItem("watering_can", () -> new WateringCanItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).stacksTo(1)));
    public static final RegistryObject<Enchantment> RANGE_ENCHANTMENT = REGISTRY.registerEnchantment("range", () -> new RangeEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> CAPACITY_ENCHANTMENT = REGISTRY.registerEnchantment("capacity", () -> new CapacityEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> FERTILITY_ENCHANTMENT = REGISTRY.registerEnchantment("fertility", () -> new FertilityEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));

    public static final EnchantmentCategory WATERING_CAN_ENCHANTMENT_CATEGORY = EnchantmentCategory.create(WateringCan.MOD_ID.toUpperCase(Locale.ROOT).concat("_WATERING_CAN"), item -> item instanceof WateringCanItem);

    public static void touch() {

    }
}
