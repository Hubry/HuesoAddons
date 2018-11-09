package hubry.huesoaddons.common.util;

import hubry.huesoaddons.HuesoAddons;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import xbony2.huesodewiki.api.IPagePrefix;
import xbony2.huesodewiki.api.IWikiRecipe;
import xbony2.huesodewiki.api.category.ICategory;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;
import xbony2.huesodewiki.api.infobox.type.IType;
import xbony2.huesodewiki.category.CategoryCreator;
import xbony2.huesodewiki.infobox.InfoboxCreator;
import xbony2.huesodewiki.infobox.parameters.TypeParameter;
import xbony2.huesodewiki.prefix.PrefixCreator;
import xbony2.huesodewiki.recipe.RecipeCreator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HuesoHooks {
	private static List<IPagePrefix> pagePrefixes;

	public static void addRecipes(IWikiRecipe... recipes) {
		Collections.addAll(RecipeCreator.recipes, recipes);
	}

	public static void addInfoboxParam(IInfoboxParameter parameter) {
		InfoboxCreator.parameters.add(parameter);
	}

	public static void addItemType(IType type) {
		TypeParameter.types.add(type);
	}

	public static void addCategory(ICategory category) {
		CategoryCreator.categories.add(category);
	}

	public static void addPagePrefix(IPagePrefix prefix) {
		if (pagePrefixes == null) {
			try {
				pagePrefixes = ReflectionHelper.getPrivateValue(PrefixCreator.class, null, "prefixes");
			} catch (ReflectionHelper.UnableToAccessFieldException e) {
				HuesoAddons.LOGGER.warn("Could not add prefix! Did Bony change the prefix list or something?", e);
				pagePrefixes = new ArrayList<>();
			}
		}
		pagePrefixes.add(prefix);
	}
}
