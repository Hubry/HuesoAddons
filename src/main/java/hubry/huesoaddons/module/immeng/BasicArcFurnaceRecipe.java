package hubry.huesoaddons.module.immeng;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import hubry.huesoaddons.common.recipe.BasicRecipe;
import hubry.huesoaddons.common.recipe.ParamList;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BasicArcFurnaceRecipe extends BasicRecipe<ArcFurnaceRecipe> {
	BasicArcFurnaceRecipe() {
		super("Arc Furnace");
	}

	@Override
	public List<? extends ArcFurnaceRecipe> gatherRecipes(ItemStack stack) {
		List<ArcFurnaceRecipe> list = new ArrayList<>();
		for (ArcFurnaceRecipe recipe : ArcFurnaceRecipe.recipeList) {
			if (recipe.output != null && recipe.output.isItemEqual(stack)) {
				list.add(recipe);
			}
		}
		return list;
	}

	@Override
	protected ParamList getParameters(ArcFurnaceRecipe recipe) {
		return new ParamList();
	}
}
