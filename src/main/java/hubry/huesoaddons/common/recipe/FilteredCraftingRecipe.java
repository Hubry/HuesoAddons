package hubry.huesoaddons.common.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import xbony2.huesodewiki.recipe.recipes.CraftingRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class FilteredCraftingRecipe extends CraftingRecipe {
	private final List<Predicate<IRecipe>> filters = new ArrayList<>();

	@Override
	public List<IRecipe> gatherRecipes(ItemStack itemstack) {
		List<IRecipe> recipes = new ArrayList<>();
		for (IRecipe recipe : CraftingManager.REGISTRY) {
			if (recipe.getRecipeOutput().isItemEqual(itemstack) && test(recipe))
				recipes.add(recipe);
		}
		return recipes;
	}

	/**
	 * Test if a recipe is filtered by this template.
	 *
	 * @param recipe Tested recipe
	 * @return If this template can process this recipe
	 */
	protected boolean test(IRecipe recipe) {
		for (Predicate<IRecipe> filter : filters) {
			if (!filter.test(recipe))
				return false;
		}
		return true;
	}

	public void addFilter(Predicate<IRecipe> filter) {
		filters.add(filter);
	}
}
