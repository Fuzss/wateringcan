package fuzs.wateringcan.world.level.block;

import com.google.common.collect.Lists;
import fuzs.wateringcan.init.ModRegistry;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpongeScheduler {

    public static final Block REPLACEMENT_BLOCK = Blocks.GLASS;
    private static final int DEFAULT_SPONGE_RADIUS = 6;
    private static final Int2ObjectMap<List<BlockPos>> SPONGE_RADIUS = new Int2ObjectOpenHashMap<>();

    private final Level level;
    private final Queue<BlockPos> blocks;

    public SpongeScheduler(Level level, Queue<BlockPos> blocks) {
        this.level = level;
        this.blocks = blocks;
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void createAtPos(Level level, BlockPos pos, int depth) {
        PoiManager poiManager = ((ServerLevelAccessor) level).getLevel().getPoiManager();
        Stream<BlockPos> all = poiManager.findAll(poiType -> poiType == ModRegistry.PERMANENT_SPONGE_POI_TYPE.get(), pos1 -> !pos1.equals(pos), pos, (depth + 1) * 2, PoiManager.Occupancy.ANY);
        Set<BlockPos> collect = all.flatMap(pos1 -> getCachedSpongeRadius(depth + 1).stream().map(pos1::offset)).collect(Collectors.toSet());
        LinkedList<BlockPos> positions = getCachedSpongeRadius(depth + 1).stream().map(pos::offset).filter(Predicate.not(collect::contains)).collect(Collectors.toCollection(LinkedList::new));
        new SpongeScheduler(level, positions);
    }

    private static List<BlockPos> getCachedSpongeRadius(int depth) {
        return SPONGE_RADIUS.computeIfAbsent(depth, SpongeScheduler::getSpongeRadius);
    }

    private static List<BlockPos> getSpongeRadius(int depth) {
        return BlockPos.betweenClosedStream(-depth, -depth, -depth, depth, depth, depth).filter((pos) -> {
            int distance = pos.distManhattan(BlockPos.ZERO);
            return distance > 0 && distance <= depth;
        }).map(BlockPos::immutable).sorted(Comparator.<BlockPos>comparingInt(o -> o.distManhattan(BlockPos.ZERO)).reversed()).toList();
    }

    @SubscribeEvent
    public void onServerTick(final TickEvent.WorldTickEvent evt) {
        if (evt.phase == TickEvent.Phase.END && evt.world == this.level) {
            if (this.advance(evt.world, 3)) {
                MinecraftForge.EVENT_BUS.unregister(this);
            }
        }
    }

    @SubscribeEvent
    public void onChunkUnload(final ChunkEvent.Unload evt) {
        if (evt.getChunk() instanceof LevelChunk levelChunk) {
            if (levelChunk.getLevel() == this.level) {
                if (this.theChunk(levelChunk)) {
                    this.finishUp(this.level);
                    MinecraftForge.EVENT_BUS.unregister(this);
                }
            }
        }
    }

    private boolean theChunk(LevelChunk levelChunk) {
        ChunkPos chunkPos = levelChunk.getPos();
        for (BlockPos pos : this.blocks) {
            if (SectionPos.blockToSection(pos.getX()) == chunkPos.x && SectionPos.blockToSection(pos.getZ()) == chunkPos.z) {
                return true;
            }
        }
        return false;
    }

    public void finishUp(Level level) {
        this.advance(level, -1);
    }

    public boolean advance(Level level, int amount) {
        for (int i = 0; (i < amount || amount == -1) && !this.blocks.isEmpty(); i++) {
            BlockPos pos = this.blocks.poll();
            if (level.getBlockState(pos).is(REPLACEMENT_BLOCK)) {
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            }
        }
        return this.blocks.isEmpty();
    }

    private void removeWaterBreadthFirstSearch(Level level, BlockPos pos) {
        Queue<Tuple<BlockPos, Integer>> queue = Lists.newLinkedList();
        queue.add(new Tuple<>(pos, 0));

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
                            level.setBlock(blockpos1, ModRegistry.SPONGE_AIR_BLOCK.get().defaultBlockState(), 11);
                        }
                        if (j < DEFAULT_SPONGE_RADIUS) {
                            queue.add(new Tuple<>(blockpos1, j + 1));
                        }
                    } else if (blockstate.getBlock() instanceof LiquidBlock || blockstate.isAir()) {
                        if (!blockstate.is(ModRegistry.SPONGE_AIR_BLOCK.get())) {
                            level.setBlock(blockpos1, ModRegistry.SPONGE_AIR_BLOCK.get().defaultBlockState(), 11);
                        }
                        if (j < DEFAULT_SPONGE_RADIUS) {
                            queue.add(new Tuple<>(blockpos1, j + 1));
                        }
                    } else if (material == Material.WATER_PLANT || material == Material.REPLACEABLE_WATER_PLANT) {
                        BlockEntity blockentity = blockstate.hasBlockEntity() ? level.getBlockEntity(blockpos1) : null;
                        Block.dropResources(blockstate, level, blockpos1, blockentity);
                        level.setBlock(blockpos1, ModRegistry.SPONGE_AIR_BLOCK.get().defaultBlockState(), 11);
                        if (j < DEFAULT_SPONGE_RADIUS) {
                            queue.add(new Tuple<>(blockpos1, j + 1));
                        }
                    }
                }
            }
        }

    }
}
