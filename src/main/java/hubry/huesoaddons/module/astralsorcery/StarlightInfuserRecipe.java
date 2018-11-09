package hubry.huesoaddons.module.astralsorcery;

import hellfirepvp.astralsorcery.common.crafting.infusion.AbstractInfusionRecipe;
import hellfirepvp.astralsorcery.common.crafting.infusion.InfusionRecipeRegistry;
import hubry.huesoaddons.common.recipe.BasicRecipe;
import hubry.huesoaddons.common.recipe.ParamList;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public class StarlightInfuserRecipe extends BasicRecipe<AbstractInfusionRecipe> {

	StarlightInfuserRecipe() {
		super("Starlight Infuser");
	}

	@Nonnull
	@Override
	public List<AbstractInfusionRecipe> gatherRecipes(ItemStack stack) {
		return InfusionRecipeRegistry.recipes.stream()
				.filter(recipe -> recipe.getOutputForMatching().isItemEqual(stack))
				.collect(Collectors.toList());
	}

	@Override
	protected ParamList getParameters(AbstractInfusionRecipe recipe) {
		return new ParamList().add("I", AstralModule.outputItemHandle(recipe.getInput()))
				.output("O", recipe.getOutputForMatching())
				.add("starlight", getStarlight(recipe));
	}

	private String getStarlight(AbstractInfusionRecipe recipe) {
		float chance = recipe.getLiquidStarlightConsumptionChance();
		if (recipe.doesConsumeMultiple()) {
			if (chance >= 1.0f) {
				return "12000 mB";
			}
			return "~" + chance * 12000 + " mB";
		}
		if (chance >= 1.0f) {
			return "1000 mB";
		}
		return String.valueOf(Math.round((1 - Math.pow(1 - chance, 12)) * 100)) + "% chance";
	}
}
