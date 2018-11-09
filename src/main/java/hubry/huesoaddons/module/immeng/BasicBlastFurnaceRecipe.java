package hubry.huesoaddons.module.immeng;

import blusunrize.immersiveengineering.api.crafting.BlastFurnaceRecipe;
import hubry.huesoaddons.common.recipe.BasicRecipe;
import hubry.huesoaddons.common.recipe.ParamList;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class BasicBlastFurnaceRecipe extends BasicRecipe<BlastFurnaceRecipe> {
	BasicBlastFurnaceRecipe() {
		super("Crude Blast Furnace");
	}

	@Nonnull
	@Override
	public List<? extends BlastFurnaceRecipe> gatherRecipes(ItemStack stack) {
		List<BlastFurnaceRecipe> list = new ArrayList<>();
		for (BlastFurnaceRecipe recipe : BlastFurnaceRecipe.recipeList) {
			if (recipe.output.isItemEqual(stack)) {
				list.add(recipe);
			}
		}
		return list;
	}

	@Override
	protected ParamList getParameters(BlastFurnaceRecipe recipe) {
		ParamList list = new ParamList()
				.add("I", ImmEngModule.outputObject(recipe.input))
				.stack("O", recipe.output);
		if (!recipe.slag.isEmpty()) {
			list.stack("S", recipe.slag);
		}
		return list;
	}
}
