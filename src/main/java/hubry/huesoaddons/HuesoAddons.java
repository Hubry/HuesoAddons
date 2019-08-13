package hubry.huesoaddons;

import hubry.huesoaddons.common.config.Config;
import hubry.huesoaddons.common.recipe.FilteredCraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xbony2.huesodewiki.api.IWikiRecipe;
import xbony2.huesodewiki.recipe.RecipeCreator;
import xbony2.huesodewiki.recipe.recipes.CraftingRecipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

@Mod(
		modid = HuesoAddons.MODID,
		name = HuesoAddons.NAME,
		version = HuesoAddons.VERSION,
		dependencies = "required-after:huesodewiki",
		clientSideOnly = true,
		guiFactory = "hubry.huesoaddons.common.config.GuiFactory"
)
public class HuesoAddons {
	public static final String MODID = "huesoaddons";
	public static final String NAME = "HuesoAddons";
	public static final String VERSION = "@VERSION@";

	public static final Logger LOGGER = LogManager.getLogger(MODID);

	private static final List<IModule> modules = new ArrayList<>();
	private static FilteredCraftingRecipe filteredRecipe;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Config.initConfig(event.getSuggestedConfigurationFile());

		List<IWikiRecipe> recipes = RecipeCreator.recipes;
		for (int i = 0; i < recipes.size(); i++) {
			IWikiRecipe recipe = recipes.get(i);
			if (recipe instanceof CraftingRecipe) {
				recipes.set(i, new FilteredCraftingRecipe());
				break;
			}
		}
	}

	public static void configureModules() {
		for (IModule module : modules) {
			module.configure();
		}
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		modules.forEach(IModule::postInit);
	}


	public static void addModule(IModule module) {
		modules.add(module);
	}

	/**
	 * Adds a filter for the vanilla recipe handler to filter out mods sticking stuff in the vanilla registry
	 *
	 * @param filter Filter that matches recipes to remove (returns true if it should be removed)
	 */
	public static void addFilter(Predicate<IRecipe> filter) {
		if (filteredRecipe == null) {
			filteredRecipe = new FilteredCraftingRecipe();
			List<IWikiRecipe> recipes = RecipeCreator.recipes;
			for (int i = 0; i < recipes.size(); i++) {
				if (recipes.get(i) instanceof CraftingRecipe) {
					recipes.set(i, filteredRecipe);
					filteredRecipe.addFilter(filter);
					return;
				}
			}
			LOGGER.error("Could not make CraftingRecipe filterable!", new Exception());
		}
		filteredRecipe.addFilter(filter);
	}
}
