package fuzs.wateringcan.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public abstract class AbstractSpongeTask {
    protected final ServerLevel level;
    protected final BlockPos source;
    protected final Block replacement;

    public AbstractSpongeTask(ServerLevel level, BlockPos source, Block replacement) {
        this.level = level;
        this.source = source;
        this.replacement = replacement;
    }

    public boolean mayAdvance(Level level) {
        return this.level == level;
    }

    public void finishQuickly() {
        this.advance(-1);
    }

    public final long position() {
        return this.source.asLong();
    }

    public abstract boolean containsBlocksAtChunkPos(int x, int z);

    public abstract boolean advance(int amount);
}
