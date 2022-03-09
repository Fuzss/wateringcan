package fuzs.wateringcan.proxy;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public interface IProxy {
    void addEntitySprinkles(Player player, Entity target);

    void addBlockSprinkles(Player player, BlockPos pos, int range);
}
