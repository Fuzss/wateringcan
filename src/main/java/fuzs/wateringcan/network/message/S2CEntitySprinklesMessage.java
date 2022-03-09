package fuzs.wateringcan.network.message;

import fuzs.puzzleslib.network.message.Message;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class S2CEntitySprinklesMessage implements Message {
    @Override
    public void write(FriendlyByteBuf buf) {

    }

    @Override
    public void read(FriendlyByteBuf buf) {

    }

    @Override
    public <T extends Message> PacketHandler<T> makeHandler() {
        return null;
    }

    private static class BlockSprinklesHandler extends PacketHandler<S2CEntitySprinklesMessage> {

        @Override
        public void handle(S2CEntitySprinklesMessage packet, Player player, Object gameInstance) {

        }
    }
}
