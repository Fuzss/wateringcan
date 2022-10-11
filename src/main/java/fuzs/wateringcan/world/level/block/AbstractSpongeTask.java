package fuzs.wateringcan.world.level.block;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public abstract class AbstractSpongeTask {
    protected final ServerLevel level;
    protected final Block replacement;

    public AbstractSpongeTask(ServerLevel level, Block replacement) {
        this.level = level;
        this.replacement = replacement;
    }

    public boolean mayAdvance(Level level) {
        return this.level == level;
    }

    public void finishQuickly() {
        this.advance(-1);
    }

    public abstract boolean containsBlocksAtChunkPos(int x, int z);

    public abstract boolean advance(int amount);
}
