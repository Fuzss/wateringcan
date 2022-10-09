package fuzs.wateringcan.world.item.enchantment;

import fuzs.wateringcan.init.ModRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class CapacityEnchantment extends Enchantment {
    public CapacityEnchantment(Rarity weight, EquipmentSlot... slotTypes) {
        super(weight, ModRegistry.WATERING_CAN_ENCHANTMENT_CATEGORY, slotTypes);
    }

    @Override
    public int getMinCost(int level) {
        return 5 + (level - 1) * 8;
    }

    @Override
    public int getMaxCost(int level) {
        return super.getMinCost(level) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}
