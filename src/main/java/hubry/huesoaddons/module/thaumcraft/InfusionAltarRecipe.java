package hubry.huesoaddons.module.thaumcraft;

import hubry.huesoaddons.common.recipe.BasicRecipe;
import hubry.huesoaddons.common.recipe.ParamList;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.InfusionRecipe;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InfusionAltarRecipe extends BasicRecipe<InfusionRecipe> {
	private final List<InfusionRecipe> recipes;

	InfusionAltarRecipe() {
		super("Infusion Altar");
		recipes = ThaumcraftApi.getCraftingRecipes().values().stream()
				.filter(r -> r instanceof InfusionRecipe)
				.map(r -> (InfusionRecipe) r)
				.collect(Collectors.toList());
	}

	@Nonnull
	@Override
	public List<InfusionRecipe> gatherRecipes(ItemStack stack) {
		List<InfusionRecipe> list = new ArrayList<>();
		for (InfusionRecipe recipe : recipes) {
			Object output = recipe.getRecipeOutput();
			if (output instanceof ItemStack && ((ItemStack) output).isItemEqual(stack)) {
				list.add(recipe);
			}
		}
		return list;
	}

	@Override
	protected ParamList getParameters(InfusionRecipe recipe) {
		ParamList params = new ParamList();
		NonNullList<Ingredient> components = recipe.getComponents();
		for (int i = 0; i < components.size(); i++) {
			params.ingredient("I" + (i + 1), components.get(i));
		}
		params.ingredient("C", recipe.getRecipeInput())
				.output("O", (ItemStack) recipe.getRecipeOutput());
		ThaumcraftModule.addAspects(params, recipe.getAspects());
		params.add("In", String.valueOf(recipe.instability));
		return params;
	}
}
