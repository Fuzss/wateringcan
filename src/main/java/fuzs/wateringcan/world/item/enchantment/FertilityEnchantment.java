package fuzs.wateringcan.world.item.enchantment;

import fuzs.wateringcan.init.ModRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class FertilityEnchantment extends Enchantment {
    public FertilityEnchantment(Rarity weight, EquipmentSlot... slotTypes) {
        super(weight, ModRegistry.WATERING_CAN_ENCHANTMENT_CATEGORY, slotTypes);
    }

    @Override
    public int getMinCost(int level) {
        return level * 10;
    }

    @Override
    public int getMaxCost(int level) {
        return this.getMinCost(level) + 15;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }
}
