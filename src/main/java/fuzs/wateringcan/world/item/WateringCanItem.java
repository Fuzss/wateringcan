package fuzs.wateringcan.world.item;

import fuzs.wateringcan.init.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class WateringCanItem extends Item implements Vanishable {

    public WateringCanItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlockHitResult hitResult = getPlayerPOVHitResult(world, player, ClipContext.Fluid.SOURCE_ONLY);
        BlockPos pos = hitResult.getBlockPos();
        BlockState state = world.getBlockState(pos);
        BlockPos posBelow = pos.relative(Direction.DOWN);
        BlockState stateBelow = world.getBlockState(posBelow);

//        if (!this.isFillLevelEmpty(stack) || player.isCreative()) {
//            if (state.getBlock() instanceof Fertilizable fertilizable && !(state.getBlock() instanceof GrassBlock)) {
//                player.playSound(SoundEvents.ITEM_BONE_MEAL_USE, 1.0F, 1.5F);
//                if (world.isClient()) return TypedActionResult.success(stack);
//                if (fertilizable.canGrow(world, world.random, pos, state)) {
//                    if (world.random.nextFloat() < 0.25)
//                        fertilizable.grow((ServerWorld) world, world.random, pos, state);
//                }
//
//                if (stateBelow.getBlock() instanceof FarmlandBlock) {
//                    world.setBlockState(posBelow, stateBelow.with(Properties.MOISTURE, FarmlandBlock.MAX_MOISTURE));
//                }
//
//                this.useWateringCan(player, stack, 1);
//                return TypedActionResult.success(stack);
//            }
//
//            if (state.getBlock() instanceof FarmlandBlock) {
//                if (world.isClient()) return TypedActionResult.success(stack);
//                world.setBlockState(pos, state.with(Properties.MOISTURE, FarmlandBlock.MAX_MOISTURE));
//                this.useWateringCan(player, stack, 1);
//                return TypedActionResult.success(stack);
//            }
//        }

        if (hitResult.getType() == HitResult.Type.BLOCK) {

            BlockPos blockPos = hitResult.getBlockPos();
            this.renderParticles(world, blockPos, stack);
            if (!world.mayInteract(player, blockPos)) {

                return InteractionResultHolder.pass(stack);
            }

            if (world.getFluidState(blockPos).is(FluidTags.WATER) && !this.isFillLevelFull(stack)) {
                world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
                world.gameEvent(player, GameEvent.FLUID_PICKUP, blockPos);
                if (!world.isClientSide()) {
                    this.setFillLevel(stack, this.getFillLevel(stack) + 10);
                }
                return InteractionResultHolder.consume(stack);
            }
        }

        // can fill watering can while raining
        if (hitResult.getType() == HitResult.Type.MISS && world.isRaining()) {
            BlockPos blockPos = player.blockPosition();
            Holder<Biome> biome = world.getBiome(blockPos);
            if (!this.isFillLevelFull(stack) && biome.value().getPrecipitation() == Biome.Precipitation.RAIN && !biome.value().coldEnoughToSnow(blockPos)) {
                world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
                world.gameEvent(player, GameEvent.FLUID_PICKUP, blockPos);
                if (!world.isClientSide()) {
                    this.setFillLevel(stack, this.getFillLevel(stack) + 5);
                }
                return InteractionResultHolder.consume(stack);
            }
        }

        return InteractionResultHolder.pass(stack);
    }

    private void renderParticles(Level world, BlockPos blockPos3, ItemStack stack) {

        if (world.isClientSide) {
            return;
        }

        ServerLevel serverWorld = (ServerLevel) world;

        // 100 for fanyc when handled on client
        int i = 50;
        Random random = world.random;
        int effectiveRange = this.getEffectiveRange(stack);
        int area = effectiveRange * 2 + 1;

        for(int j = 0; j < i; ++j) {
            double d = random.nextDouble() * area - effectiveRange;
            double e = random.nextDouble() * area - effectiveRange;
            BlockPos blockPos = new BlockPos((double)blockPos3.getX() + d, (double)blockPos3.getY(), (double)blockPos3.getZ() + e);
            BlockPos blockPos1 = blockPos.below();
            BlockState blockState1 = world.getBlockState(blockPos1);
            FluidState fluidState1 = world.getFluidState(blockPos1);
            VoxelShape voxelShape1 = blockState1.getCollisionShape(world, blockPos1);
            double g1 = voxelShape1.max(Direction.Axis.Y, Mth.frac(d), Mth.frac(e));
            double h1 = fluidState1.getHeight(world, blockPos1);
            double m1 = Math.max(g1, h1);
            if (m1 < 1.0) {
                continue;
            }
            BlockState blockState = world.getBlockState(blockPos);
            FluidState fluidState = world.getFluidState(blockPos);
            VoxelShape voxelShape = blockState.getCollisionShape(world, blockPos);
            double g = voxelShape.max(Direction.Axis.Y, Mth.frac(d), Mth.frac(e));
            double h = fluidState.getHeight(world, blockPos);
            double m = Math.max(g, h);
            ParticleOptions particleEffect = !fluidState.is(FluidTags.LAVA) && !blockState.is(Blocks.MAGMA_BLOCK) && !CampfireBlock.isLitCampfire(blockState) ? ParticleTypes.RAIN : ParticleTypes.SMOKE;
            serverWorld.sendParticles(particleEffect, (double)blockPos3.getX() + d, (double)blockPos3.getY() + m, (double)blockPos3.getZ() + e, 1, 0.0, 0.0D, 0.0D, 0.0D);
        }
    }

    public int getEffectiveRange(ItemStack stack) {
        return EnchantmentHelper.getItemEnchantmentLevel(ModRegistry.RANGE_ENCHANTMENT.get(), stack) * 2 + 1;
    }

    public boolean useWateringCan(Player player, ItemStack stack, int useAmount) {
        if (player.getAbilities().instabuild) {
            return true;
        }
        int fillLevel = this.getFillLevel(stack) - useAmount;
        if (fillLevel < 0) {
            return false;
        }
        this.setFillLevel(stack, fillLevel);
        return true;
    }

    public int getFillLevel(ItemStack stack) {
        return stack.getTag() == null ? 0 : stack.getTag().getInt("FillLevel");
    }

    public void setFillLevel(ItemStack stack, int fillLevel) {
        stack.getOrCreateTag().putInt("FillLevel", Mth.clamp(fillLevel, 0, this.getMaxFillLevel()));
    }

    public boolean isFillLevelFull(ItemStack stack) {
        return stack.getTag() != null && stack.getTag().getInt("FillLevel") == this.getMaxFillLevel();
    }

    public boolean isFillLevelEmpty(ItemStack stack) {
        return stack.getTag() == null || stack.getTag().getInt("FillLevel") == 0;
    }

    public int getMaxFillLevel() {
        return 100;
    }

    public static float getAmountFilled(ItemStack itemStack) {
        return itemStack.getItem() instanceof WateringCanItem item ? item.getFillLevel(itemStack) / (float) item.getMaxFillLevel() : 0.0F;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return Mth.color(65, 135, 235);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(13.0F * this.getFillLevel(stack) / this.getMaxFillLevel());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        if (context.isAdvanced()) {
            tooltip.add(new TranslatableComponent("watering_can.fill_level", this.getFillLevel(stack), this.getMaxFillLevel()));
        }
    }
}
