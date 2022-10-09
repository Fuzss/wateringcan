package fuzs.wateringcan.network;

import fuzs.puzzleslib.network.message.Message;
import fuzs.wateringcan.WateringCan;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class S2CBlockSprinklesMessage implements Message {
    private BlockPos pos;
    private int range;

    public S2CBlockSprinklesMessage() {
    }

    public S2CBlockSprinklesMessage(BlockPos pos, int range) {
        this.pos = pos;
        this.range = range;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeVarInt(this.range);
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.range = buf.readVarInt();
    }

    @Override
    public PacketHandler<S2CBlockSprinklesMessage> makeHandler() {
        return new PacketHandler<>() {

            @Override
            public void handle(S2CBlockSprinklesMessage packet, Player player, Object gameInstance) {
                WateringCan.PROXY.addBlockSprinkles(player, packet.pos, packet.range);
            }
        };
    }
}
