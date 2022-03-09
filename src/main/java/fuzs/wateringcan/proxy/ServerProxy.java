package fuzs.wateringcan.proxy;

import dqu.additionaladditions.AdditionalAdditions;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class ServerProxy implements IProxy {
    @Override
    public void addEntitySprinkles(Player player, Entity target) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeVarInt(target.getId());
        Packet<?> s2CPacket = ServerPlayNetworking.createS2CPacket(AdditionalAdditions.ENTITY_SPRINKLES_PACKET_ID, buf);
        ((ServerPlayer) player).getLevel().getChunkSource().broadcastAndSend(player, s2CPacket);
    }

    @Override
    public void addBlockSprinkles(Player player, BlockPos pos, int range) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(pos);
        buf.writeVarInt(range);
        Packet<?> s2CPacket = ServerPlayNetworking.createS2CPacket(AdditionalAdditions.BLOCK_SPRINKLES_PACKET_ID, buf);
        ((ServerPlayer) player).server.getPlayerList().broadcast(null, pos.getX(), pos.getY(), pos.getZ(), 64.0, player.level.dimension(), s2CPacket);
    }
}
