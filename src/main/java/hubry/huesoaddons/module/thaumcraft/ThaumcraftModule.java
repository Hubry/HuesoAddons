package hubry.huesoaddons.module.thaumcraft;

import hubry.huesoaddons.HuesoAddons;
import hubry.huesoaddons.IModule;
import hubry.huesoaddons.common.config.Config;
import hubry.huesoaddons.common.recipe.LinkBuilder;
import hubry.huesoaddons.common.recipe.ParamList;
import hubry.huesoaddons.common.util.HuesoHooks;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IArcaneRecipe;

public class ThaumcraftModule implements IModule {
	static boolean itemAspectsEnabled;

	@Override
	public void configure() {
		itemAspectsEnabled = Config.cfg.getBoolean("Item aspects in infobox", "thaumcraft", true, "Should item aspects be added to the infobox?");
	}

	@Override
	public void postInit() {
		HuesoAddons.addFilter(recipe -> !(recipe instanceof IArcaneRecipe));

		HuesoHooks.addRecipes(
				new ArcaneRecipe(),
				new BasicCrucibleRecipe(),
				new InfusionAltarRecipe()
		);

		HuesoHooks.addInfoboxParam(new ItemAspectParameter());
		//        HuesoHooks.addCategory(new EssentiaCategory()); //TODO
	}

	static String outputAspect(Aspect aspect, int amount) {
		StringBuilder b = new StringBuilder("{{TC6A|").append(aspect.getName());
		if (amount > 1)
			b.append('|').append(amount);
		return b.append("}}").toString();
	}

	static void addAspects(ParamList params, AspectList aspectList) {
		Aspect[] aspects = aspectList.getAspectsSortedByAmount();
		for (int i = 0; i < aspects.length; i++) {
			params.add("A" + (i + 1), outputAspect(aspects[i], aspectList.getAmount(aspects[i])));
		}
	}

	static String outputCrystal(Aspect aspect, int amount) {
		return new LinkBuilder(ThaumcraftApiHelper.makeCrystal(aspect, amount)).setOverrideLink("Vis Crystal").toString();
	}
}
