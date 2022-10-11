package fuzs.wateringcan.init;

import com.google.common.collect.ImmutableSet;
import fuzs.puzzleslib.registry.RegistryManager;
import fuzs.wateringcan.WateringCan;
import fuzs.wateringcan.world.item.WateringCanItem;
import fuzs.wateringcan.world.item.enchantment.CapacityEnchantment;
import fuzs.wateringcan.world.item.enchantment.FertilityEnchantment;
import fuzs.wateringcan.world.item.enchantment.RangeEnchantment;
import fuzs.wateringcan.world.level.block.PermanentSpongeBlock;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.RegistryObject;

import java.util.Locale;

public class ModRegistry {
    public static final EnchantmentCategory WATERING_CAN_ENCHANTMENT_CATEGORY = EnchantmentCategory.create(WateringCan.MOD_ID.toUpperCase(Locale.ROOT).concat("_WATERING_CAN"), item -> item instanceof WateringCanItem);

    private static final RegistryManager REGISTRY = RegistryManager.of(WateringCan.MOD_ID);
    public static final RegistryObject<Block> SPONGE_AIR_BLOCK = REGISTRY.registerBlock("sponge_air", () -> new AirBlock(BlockBehaviour.Properties.of(Material.STRUCTURAL_AIR).noCollission().noDrops().air()));
    public static final RegistryObject<Block> PERMANENT_SPONGE_BLOCK = REGISTRY.registerBlockWithItem("permanent_sponge", () -> new PermanentSpongeBlock(BlockBehaviour.Properties.of(Material.SPONGE).strength(0.6F).sound(SoundType.GRASS).randomTicks()), CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final RegistryObject<PoiType> PERMANENT_SPONGE_POI_TYPE = REGISTRY.register(PoiType.class, "permanent_sponge", () -> new PoiType("permanent_sponge", ImmutableSet.copyOf(PERMANENT_SPONGE_BLOCK.get().getStateDefinition().getPossibleStates()), 0, 1));
    public static final RegistryObject<Item> WATERING_CAN_ITEM = REGISTRY.registerItem("watering_can", () -> new WateringCanItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).stacksTo(1)));
    public static final RegistryObject<Enchantment> RANGE_ENCHANTMENT = REGISTRY.registerEnchantment("range", () -> new RangeEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> CAPACITY_ENCHANTMENT = REGISTRY.registerEnchantment("capacity", () -> new CapacityEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> FERTILITY_ENCHANTMENT = REGISTRY.registerEnchantment("fertility", () -> new FertilityEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));

    public static void touch() {

    }
}
