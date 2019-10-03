package hubry.huesoaddons.module.mekanism;

import hubry.huesoaddons.common.recipe.BasicRecipe;
import hubry.huesoaddons.common.recipe.ParamList;
import mekanism.common.recipe.RecipeHandler;
import mekanism.common.recipe.inputs.ItemStackInput;
import mekanism.common.recipe.machines.BasicMachineRecipe;
import mekanism.common.recipe.outputs.ItemStackOutput;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SingleInputOutputRecipe<T extends BasicMachineRecipe<T>> extends BasicRecipe<BasicMachineRecipe<T>> {
	private final RecipeHandler.Recipe<ItemStackInput, ItemStackOutput, T> recipeType;

	SingleInputOutputRecipe(String name, RecipeHandler.Recipe<ItemStackInput, ItemStackOutput, T> recipeType) {
		super(name);
		this.recipeType = recipeType;
	}

	@Override
	public List<? extends BasicMachineRecipe<T>> gatherRecipes(ItemStack stack) {
		List<T> recipes = new ArrayList<>();
		for (T recipe : recipeType.get().values()) {
			if (recipe.getOutput().output.isItemEqual(stack)) {
				recipes.add(recipe);
			}
		}
		return recipes;
	}

	@Override
	protected ParamList getParameters(BasicMachineRecipe recipe) {
		return new ParamList()
				.output("I1", ((ItemStackInput) recipe.getInput()).ingredient)
				.output("O1", ((ItemStackOutput) recipe.getOutput()).output);
	}
}
