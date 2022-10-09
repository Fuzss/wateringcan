package fuzs.wateringcan.network;

import fuzs.puzzleslib.network.message.Message;
import fuzs.wateringcan.WateringCan;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class S2CEntitySprinklesMessage implements Message {
    private int target;

    public S2CEntitySprinklesMessage() {
    }

    public S2CEntitySprinklesMessage(Entity target) {
        this.target = target.getId();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(this.target);
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        this.target = buf.readVarInt();
    }

    @Override
    public PacketHandler<S2CEntitySprinklesMessage> makeHandler() {
        return new PacketHandler<>() {

            @Override
            public void handle(S2CEntitySprinklesMessage packet, Player player, Object gameInstance) {
                WateringCan.PROXY.addEntitySprinkles(player, player.level.getEntity(packet.target));
            }
        };
    }
}
