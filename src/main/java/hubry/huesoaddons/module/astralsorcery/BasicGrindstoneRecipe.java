package hubry.huesoaddons.module.astralsorcery;

import hellfirepvp.astralsorcery.common.crafting.grindstone.GrindstoneRecipe;
import hellfirepvp.astralsorcery.common.crafting.grindstone.GrindstoneRecipeRegistry;
import hubry.huesoaddons.common.recipe.BasicRecipe;
import hubry.huesoaddons.common.recipe.ParamList;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public class BasicGrindstoneRecipe extends BasicRecipe<GrindstoneRecipe> {

	BasicGrindstoneRecipe() {
		super("Astral Sorcery/Grindstone");
	}

	@Nonnull
	@Override
	public List<GrindstoneRecipe> gatherRecipes(ItemStack stack) {
		return GrindstoneRecipeRegistry.getValidRecipes().stream()
				.filter(recipe -> recipe.getOutputForMatching().isItemEqual(stack))
				.collect(Collectors.toList());
	}

	@Override
	protected ParamList getParameters(GrindstoneRecipe recipe) {
		ParamList list = new ParamList()
				.add("I", AstralModule.outputItemHandle(recipe.getInputForRender()))
				.output("O", recipe.getOutputForMatching());
		if (recipe.getChanceToDoubleOutput() > 0.0f) {
			list.add("chance", String.valueOf(Math.round(recipe.getChanceToDoubleOutput() * 100f)));
		}
		return list;
	}
}
