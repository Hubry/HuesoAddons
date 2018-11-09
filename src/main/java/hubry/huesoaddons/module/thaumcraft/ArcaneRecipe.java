package hubry.huesoaddons.module.thaumcraft;

import hubry.huesoaddons.common.recipe.FilteredCraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IArcaneRecipe;

public class ArcaneRecipe extends FilteredCraftingRecipe {
	/** All primal aspects in the order of recipe slots. */
	private static final Aspect[] primals = {Aspect.AIR, Aspect.WATER, Aspect.ORDER, Aspect.ENTROPY, Aspect.EARTH, Aspect.FIRE};

	@Override
	protected boolean test(IRecipe recipe) {
		return recipe instanceof IArcaneRecipe;
	}

	// Somewhat dumb way of doing this but beats duplicating all the code.
	@Override
	public String outputRecipe(IRecipe recipe) {
		String s = super.outputRecipe(recipe).replace("Cg/Crafting Table", "Cg/Arcane Workbench/Thaumcraft 6");
		StringBuilder ret = new StringBuilder(s.substring(0, s.length() - 3));

		IArcaneRecipe arcaneRecipe = (IArcaneRecipe) recipe;
		ret.append("|Vis=").append(arcaneRecipe.getVis()).append('\n');
		AspectList list = arcaneRecipe.getCrystals();
		if (list != null) {
			for (int i = 0; i < primals.length; i++) {
				Aspect primal = primals[i];
				if (list.getAmount(primal) > 0)
					ret.append("|V").append(i + 1).append('=').append(list.getAmount(primal)).append('\n');
			}
		}
		return ret.append("}}").toString();
	}
}
