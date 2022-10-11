package fuzs.wateringcan.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public abstract class AbstractSpongeTask {
    protected final Level level;
    protected final Block replacement;
    private final BlockPos source;

    public AbstractSpongeTask(Level level, Block replacement, BlockPos source) {
        this.level = level;
        this.replacement = replacement;
        this.source = source;
    }

    public boolean mayAdvance(Level level) {
        return this.level == level;
    }

    public void finishQuickly() {
        this.advance(-1);
    }

    public abstract boolean advance(int amount);

    protected void destroySource() {

    }
}
