package fuzs.wateringcan.data;

import fuzs.wateringcan.registry.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator p_125973_) {
        super(p_125973_);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> p_176532_) {
        ShapedRecipeBuilder.shaped(ModRegistry.WATERING_CAN_ITEM.get())
                .group("watering_can")
                .define('C', Items.COPPER_INGOT)
                .define('B', Items.BUCKET)
                .define('M', Items.BONE_MEAL)
                .pattern("CM ")
                .pattern("CBC")
                .pattern(" C ")
                .unlockedBy("has_bucket", has(Items.BUCKET))
                .save(p_176532_);
        ShapedRecipeBuilder.shaped(ModRegistry.WATERING_CAN_ITEM.get())
                .group("watering_can")
                .define('C', Items.COPPER_INGOT)
                .define('B', Items.BUCKET)
                .define('M', Items.BONE_MEAL)
                .pattern(" MC")
                .pattern("CBC")
                .pattern(" C ")
                .unlockedBy("has_bucket", has(Items.BUCKET))
                .save(p_176532_);
    }
}
