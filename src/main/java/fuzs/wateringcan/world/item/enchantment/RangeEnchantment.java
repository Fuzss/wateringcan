package fuzs.wateringcan.world.item.enchantment;

import fuzs.wateringcan.registry.ModRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class RangeEnchantment extends Enchantment {
    public RangeEnchantment(Enchantment.Rarity weight, EquipmentSlot... slotTypes) {
        super(weight, ModRegistry.WATERING_CAN_ENCHANTMENT_CATEGORY, slotTypes);
    }

    @Override
    public int getMinCost(int level) {
        return level * 25;
    }

    @Override
    public int getMaxCost(int level) {
        return this.getMinCost(level) + 50;
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }
}
