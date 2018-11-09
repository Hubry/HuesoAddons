package hubry.huesoaddons.module.thaumcraft;

import hubry.huesoaddons.common.recipe.BasicRecipe;
import hubry.huesoaddons.common.recipe.ParamList;
import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.CrucibleRecipe;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BasicCrucibleRecipe extends BasicRecipe<CrucibleRecipe> {
	private final List<CrucibleRecipe> recipes;

	BasicCrucibleRecipe() {
		super("Crucible");
		recipes = ThaumcraftApi.getCraftingRecipes().values().stream()
				.filter(r -> r instanceof CrucibleRecipe)
				.map(r -> (CrucibleRecipe) r)
				.collect(Collectors.toList());
	}

	@Nonnull
	@Override
	public List<CrucibleRecipe> gatherRecipes(ItemStack stack) {
		List<CrucibleRecipe> list = new ArrayList<>();
		for (CrucibleRecipe recipe : recipes) {
			if (recipe.getRecipeOutput().isItemEqual(stack)) {
				list.add(recipe);
			}
		}
		return list;
	}

	@Override
	protected ParamList getParameters(CrucibleRecipe recipe) {
		ParamList params = new ParamList()
				.ingredient("I", recipe.getCatalyst())
				.output("O", recipe.getRecipeOutput());
		ThaumcraftModule.addAspects(params, recipe.getAspects());
		return params;
	}
}
