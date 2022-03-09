package fuzs.wateringcan.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class ClientProxy implements IProxy {
    @Override
    public void addEntitySprinkles(Player player, Entity target) {
        Minecraft.getInstance().particleEngine.createTrackingEmitter(target, ParticleTypes.RAIN);
    }

    @Override
    public void addBlockSprinkles(Player player, BlockPos pos, int range) {

    }
}
