package fuzs.wateringcan.world.level.block;

import fuzs.wateringcan.init.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class PermanentSpongeBlock extends Block {

    public PermanentSpongeBlock(Properties p_56796_) {
        super(p_56796_);
    }

    @Override
    public void onPlace(BlockState p_56811_, Level level, BlockPos pos, BlockState oldState, boolean p_56815_) {
        if (!oldState.is(p_56811_.getBlock())) {
            AbstractSpongeTask task = SetSpongeTask.createSetTask((ServerLevel) level, ModRegistry.SPONGE_AIR_BLOCK.get(), pos, 6);
            SpongeScheduler.INSTANCE.scheduleTask(task);
        }
    }

    @Override
    public void randomTick(BlockState p_60551_, ServerLevel level, BlockPos pos, Random random) {
        AbstractSpongeTask task = SetSpongeTask.createSetTask(level, ModRegistry.SPONGE_AIR_BLOCK.get(), pos, 6);
        SpongeScheduler.INSTANCE.scheduleTask(task);
    }

    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean p_50941_) {
        if (!oldState.is(newState.getBlock())) {
            AbstractSpongeTask task = RemoveSpongeTask.createRemoveTask((ServerLevel) level, pos, 6);
            SpongeScheduler.INSTANCE.scheduleTask(task);
            super.onRemove(oldState, level, pos, newState, p_50941_);
        }
    }
}