package fuzs.wateringcan.handler;

import fuzs.wateringcan.world.item.WateringCanItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CanInteractionHandler {
    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteract evt) {
        LivingEntity entity = evt.getEntityLiving();
        Player player = evt.getPlayer();
        InteractionHand hand = evt.getHand();
        ItemStack stack = player.getStackInHand(hand);
        if (stack.getItem() instanceof WateringCanItem item && !item.isFillLevelEmpty(stack) && !player.getCooldowns().isOnCooldown(item)) {
            if (true) {
                boolean success = false;
                if (entity.isSensitiveToWater() && item.useWateringCan(player, stack, 3)) {
                    entity.hurt(DamageSource.indirectMagic(entity, player), 1.0F);
                    success = true;
                }
                if (entity instanceof Axolotl axolotlEntity && item.useWateringCan(player, stack, 3)) {
                    axolotlEntity.rehydrate();
                    success = true;
                }
                if (entity.isOnFire() && item.useWateringCan(player, stack, 5)) {
                    entity.clearFire();
                    success = true;
                }
                if (success) {
                    player.getCooldowns().addCooldown(item, 16);
                    proxy.addEntitySprinkles(player, entity);
                    // don't use CONSUME on client as server won't be notified then
                    return ActionResult.SUCCESS;
                }
            }
        }
    }
}
