package hubry.huesoaddons.module.mekanism;

import hubry.huesoaddons.common.recipe.BasicRecipe;
import hubry.huesoaddons.common.recipe.ParamList;
import mekanism.api.infuse.InfuseObject;
import mekanism.api.infuse.InfuseRegistry;
import mekanism.api.infuse.InfuseType;
import mekanism.common.recipe.RecipeHandler;
import mekanism.common.recipe.machines.MetallurgicInfuserRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MetallurgicInfusionRecipe extends BasicRecipe<MetallurgicInfuserRecipe> {
	MetallurgicInfusionRecipe() {
		super("Metallurgic Infuser");
	}

	@Override
	public List<? extends MetallurgicInfuserRecipe> gatherRecipes(ItemStack stack) {
		List<MetallurgicInfuserRecipe> recipes = new ArrayList<>();
		for (MetallurgicInfuserRecipe recipe : RecipeHandler.Recipe.METALLURGIC_INFUSER.get().values()) {
			if (recipe.getOutput().output.isItemEqual(stack)) {
				recipes.add(recipe);
			}
		}
		return recipes;
	}

	@Override
	protected ParamList getParameters(MetallurgicInfuserRecipe recipe) {
		ParamList list = new ParamList();
		list.output("I1", recipe.getInput().inputStack);
		List<ItemStack> stacks = new ArrayList<>();
		InfuseType type = recipe.getInput().infuse.getType();
		for (Map.Entry<ItemStack, InfuseObject> entry : InfuseRegistry.getObjectMap().entrySet()) {
			if (entry.getValue().type == type) {
				stacks.add(entry.getKey());
			}
		}
		list.stacks("I2", stacks);
		list.output("O1", recipe.getOutput().output);
		return list;
	}
}
