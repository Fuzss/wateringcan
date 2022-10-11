package fuzs.wateringcan.world.level.block;

import com.google.common.collect.Lists;
import fuzs.wateringcan.init.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Tuple;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;

import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class PermanentSpongeBlock extends Block {
    public static final int MAX_DEPTH = 6;
    public static final int MAX_COUNT = 64;
    public static final List<BlockPos> BOOKSHELF_OFFSETS = BlockPos.betweenClosedStream(-MAX_DEPTH, -MAX_DEPTH, -MAX_DEPTH, MAX_DEPTH, MAX_DEPTH, MAX_DEPTH).filter((p_207914_) -> {
        int distance = p_207914_.distManhattan(BlockPos.ZERO);
        return distance > 0 && distance <= MAX_DEPTH;
    }).map(BlockPos::immutable).sorted(Comparator.comparingInt(o -> o.distManhattan(BlockPos.ZERO))).toList();

    public PermanentSpongeBlock(Properties p_56796_) {
        super(p_56796_);
    }

    @Override
    public void onPlace(BlockState p_56811_, Level p_56812_, BlockPos p_56813_, BlockState p_56814_, boolean p_56815_) {
        if (!p_56814_.is(p_56811_.getBlock())) {
            this.tryAbsorbWater(p_56812_, p_56813_);
        }
    }

    @Override
    public void randomTick(BlockState p_60551_, ServerLevel level, BlockPos pos, Random random) {
        SpongeScheduler.createAtPos(level, pos, 6);
    }

    //    @Override
//    public void neighborChanged(BlockState p_56801_, Level p_56802_, BlockPos p_56803_, Block p_56804_, BlockPos p_56805_, boolean p_56806_) {
//        this.tryAbsorbWater(p_56802_, p_56803_);
//        super.neighborChanged(p_56801_, p_56802_, p_56803_, p_56804_, p_56805_, p_56806_);
//    }

    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean p_50941_) {
        if (!oldState.is(newState.getBlock())) {
            SpongeScheduler.createAtPos(level, pos, 6);

//            for (BlockPos offset : BOOKSHELF_OFFSETS) {
//                BlockPos offsetPos = pos.offset(offset);
//                if (level.getBlockState(offsetPos).is(ModRegistry.SPONGE_AIR_BLOCK.get())) {
//                    level.setBlock(offsetPos, Blocks.AIR.defaultBlockState(), 3);
//                }
//            }

            super.onRemove(oldState, level, pos, newState, p_50941_);
        }
    }

    protected void tryAbsorbWater(Level p_56798_, BlockPos p_56799_) {
        if (this.removeWaterBreadthFirstSearch(p_56798_, p_56799_)) {
//         p_56798_.setBlock(p_56799_, Blocks.WET_SPONGE.defaultBlockState(), 2);
//            p_56798_.levelEvent(2001, p_56799_, Block.getId(Blocks.WATER.defaultBlockState()));
        }

    }

    private boolean removeWaterBreadthFirstSearch(Level level, BlockPos pos) {
        Queue<Tuple<BlockPos, Integer>> queue = Lists.newLinkedList();
        queue.add(new Tuple<>(pos, 0));
        int i = 0;

        while (!queue.isEmpty()) {
            Tuple<BlockPos, Integer> tuple = queue.poll();
            BlockPos blockpos = tuple.getA();
            int j = tuple.getB();

            for (Direction direction : Direction.values()) {
                BlockPos blockpos1 = blockpos.relative(direction);
                BlockState blockstate = level.getBlockState(blockpos1);
                FluidState fluidstate = level.getFluidState(blockpos1);
                Material material = blockstate.getMaterial();
                if (fluidstate.is(FluidTags.WATER) || blockstate.isAir()) {
                    if (blockstate.getBlock() instanceof BucketPickup && !((BucketPickup) blockstate.getBlock()).pickupBlock(level, blockpos1, blockstate).isEmpty()) {
                        if (level.getBlockState(blockpos1).is(Blocks.AIR)) {
                            level.setBlock(blockpos1, SpongeScheduler.REPLACEMENT_BLOCK.defaultBlockState(), 11);
                        }
                        ++i;
                        if (j < 6) {
                            queue.add(new Tuple<>(blockpos1, j + 1));
                        }
                    } else if (blockstate.getBlock() instanceof LiquidBlock || blockstate.isAir()) {
                        if (!blockstate.is(ModRegistry.SPONGE_AIR_BLOCK.get())) {
                            level.setBlock(blockpos1, SpongeScheduler.REPLACEMENT_BLOCK.defaultBlockState(), 11);
                        }
                        ++i;
                        if (j < 6) {
                            queue.add(new Tuple<>(blockpos1, j + 1));
                        }
                    } else if (material == Material.WATER_PLANT || material == Material.REPLACEABLE_WATER_PLANT) {
                        BlockEntity blockentity = blockstate.hasBlockEntity() ? level.getBlockEntity(blockpos1) : null;
                        dropResources(blockstate, level, blockpos1, blockentity);
                        level.setBlock(blockpos1, SpongeScheduler.REPLACEMENT_BLOCK.defaultBlockState(), 11);
                        ++i;
                        if (j < 6) {
                            queue.add(new Tuple<>(blockpos1, j + 1));
                        }
                    }
                }
            }

//            if (i > 64) {
//                break;
//            }
        }

        return i > 0;
    }
}