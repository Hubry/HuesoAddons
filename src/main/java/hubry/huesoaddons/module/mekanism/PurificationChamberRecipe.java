package hubry.huesoaddons.module.mekanism;

import hubry.huesoaddons.common.recipe.BasicRecipe;
import hubry.huesoaddons.common.recipe.ParamList;
import mekanism.common.recipe.RecipeHandler;
import mekanism.common.recipe.machines.PurificationRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PurificationChamberRecipe extends BasicRecipe<PurificationRecipe> {
	PurificationChamberRecipe() {
		super("Purification Chamber");
	}

	@Override
	public List<? extends PurificationRecipe> gatherRecipes(ItemStack stack) {
		List<PurificationRecipe> recipes = new ArrayList<>();
		for (PurificationRecipe recipe : RecipeHandler.Recipe.PURIFICATION_CHAMBER.get().values()) {
			if (recipe.getOutput().output.isItemEqual(stack)) {
				recipes.add(recipe);
			}
		}
		return recipes;
	}

	@Override
	protected ParamList getParameters(PurificationRecipe recipe) {
		return new ParamList()
				.output("I1", recipe.getInput().itemStack)
				.output("O1", recipe.getOutput().output);
		//todo gases
	}
}
