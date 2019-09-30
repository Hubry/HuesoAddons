package hubry.huesoaddons.common.util;

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

import java.util.Collections;

public class HuesoHooks {
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
		PrefixCreator.prefixes.add(prefix);
	}
}
