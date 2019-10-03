package hubry.huesoaddons.module.mekanism;

import hubry.huesoaddons.IModule;
import hubry.huesoaddons.common.util.HuesoHooks;
import mekanism.common.recipe.RecipeHandler;

public class MekanismModule implements IModule {
	@Override
	public void postInit() {
		HuesoHooks.addRecipes(
				new SingleInputOutputRecipe<>("Enrichment Chamber", RecipeHandler.Recipe.ENRICHMENT_CHAMBER),
				new MetallurgicInfusionRecipe(),
				new PurificationChamberRecipe()
		);
	}
}
