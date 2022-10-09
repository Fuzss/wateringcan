package fuzs.wateringcan.proxy;

import fuzs.wateringcan.WateringCan;
import fuzs.wateringcan.network.S2CBlockSprinklesMessage;
import fuzs.wateringcan.network.S2CEntitySprinklesMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class ServerProxy implements IProxy {
    @Override
    public void addEntitySprinkles(Player player, Entity target) {
        // TODO: must be sendToAllTracking (not implemented in 1.18 Puzzles Lib)
        WateringCan.NETWORK.sendTo(new S2CEntitySprinklesMessage(target), (ServerPlayer) player);
    }

    @Override
    public void addBlockSprinkles(Player player, BlockPos pos, int range) {
        WateringCan.NETWORK.sendToAllNear(new S2CBlockSprinklesMessage(pos, range), pos, player.level);
    }
}
