package fuzs.wateringcan.world.level.block;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;

import java.util.Queue;

public class SetSpongeTask extends AbstractSpongeTask {
    private final Queue<Tuple<BlockPos, Integer>> queue;
    private final int distance;
    private boolean hasDestroyedSource;

    private SetSpongeTask(ServerLevel level, Block replacement, BlockPos source, int distance) {
        super(level, source, replacement);
        this.distance = distance;
        this.queue = Lists.newLinkedList();
        this.queue.add(new Tuple<>(this.source, 0));
    }

    public static SetSpongeTask createSetTask(ServerLevel level, Block replacement, BlockPos source, int distance) {
        return new SetSpongeTask(level, replacement, source, distance);
    }

    @Override
    public boolean containsBlocksAtChunkPos(int x, int z) {
        return Math.abs(SectionPos.blockToSection(this.source.getX()) - x) <= 1 && Math.abs(SectionPos.blockToSection(this.source.getZ()) - z) <= 1;
    }

    @Override
    public boolean advance(int amount) {
        for (int i = 0; (i < amount || amount == -1) && !this.queue.isEmpty(); i++) {
            Tuple<BlockPos, Integer> tuple = this.queue.poll();
            this.removeWaterBreadthFirstSearch(tuple);
        }
        return this.queue.isEmpty();
    }

    private void removeWaterBreadthFirstSearch(Tuple<BlockPos, Integer> tuple) {
        BlockPos blockpos = tuple.getA();
        int j = tuple.getB();

        for (Direction direction : Direction.values()) {
            BlockPos blockpos1 = blockpos.relative(direction);
            BlockState blockstate = this.level.getBlockState(blockpos1);
            FluidState fluidstate = this.level.getFluidState(blockpos1);
            Material material = blockstate.getMaterial();
            if (!fluidstate.isEmpty() || blockstate.isAir()) {
                if (this.isFluidHot(fluidstate)) {
                    this.destroySource();
                }
                if (blockstate.getBlock() instanceof BucketPickup && !((BucketPickup) blockstate.getBlock()).pickupBlock(this.level, blockpos1, blockstate).isEmpty()) {
                    if (this.level.getBlockState(blockpos1).is(Blocks.AIR)) {
                        this.level.setBlock(blockpos1, this.replacement.defaultBlockState(), 3);
                    }
                    if (j < this.distance) {
                        this.queue.add(new Tuple<>(blockpos1, j + 1));
                    }
                } else if (blockstate.getBlock() instanceof LiquidBlock || blockstate.isAir()) {
                    this.level.setBlock(blockpos1, this.replacement.defaultBlockState(), 3);
                    if (j < this.distance) {
                        this.queue.add(new Tuple<>(blockpos1, j + 1));
                    }
                } else if (material == Material.WATER_PLANT || material == Material.REPLACEABLE_WATER_PLANT) {
                    BlockEntity blockentity = blockstate.hasBlockEntity() ? this.level.getBlockEntity(blockpos1) : null;
                    Block.dropResources(blockstate, this.level, blockpos1, blockentity);
                    this.level.setBlock(blockpos1, this.replacement.defaultBlockState(), 3);
                    if (j < this.distance) {
                        this.queue.add(new Tuple<>(blockpos1, j + 1));
                    }
                }
            }
        }
    }

    private void destroySource() {
        if (!this.hasDestroyedSource) {
            this.level.setBlock(this.source, this.replacement.defaultBlockState(), 3);
            this.hasDestroyedSource = true;
        }
    }

    private boolean isFluidHot(FluidState fluidstate) {
        return fluidstate.getType().getAttributes().getTemperature() >= 1000;
    }
}
