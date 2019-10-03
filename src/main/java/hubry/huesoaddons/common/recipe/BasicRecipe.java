package hubry.huesoaddons.common.recipe;

import hubry.huesoaddons.HuesoAddons;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.IWikiRecipe;

import java.util.List;

/**
 * A class that takes the lifting of formatting templates, you just give it a list of params.
 * Also is a generic hell if you try to make a subclass using generics.
 *
 * @param <T> Type of the recipe this class is a wrapper for.
 */
public abstract class BasicRecipe<T> implements IWikiRecipe {
	private final String name;

	/**
	 * @param name Name of the template (after the {@code Cg/} prefix)
	 */
	public BasicRecipe(String name) {
		this.name = name;
	}

	/**
	 * Gathers all recipes matching the stack.
	 *
	 * @param stack Item stack to get the recipes for.
	 * @return A list of matching recipes.
	 */
	public abstract List<? extends T> gatherRecipes(ItemStack stack);

	/**
	 * Gathers all recipe parameters.
	 *
	 * @param recipe Recipe to turn into a list of properties.
	 * @return A list of parameters forming the passed recipe.
	 */
	protected abstract ParamList getParameters(T recipe);

	private String outputRecipe(T recipe) {
		return "{{Cg/" + name + '\n' +
				getParameters(recipe) +
				"}}\n";
	}

	@Override
	public final String getRecipes(ItemStack stack) {
		StringBuilder builder = new StringBuilder();
		try {
			for (T recipe : gatherRecipes(stack)) {
				builder.append(outputRecipe(recipe)).append('\n');
			}
		} catch (Exception e) {
			HuesoAddons.LOGGER.error("Exception occured while processing recipes of template {}", name, e);
		}
		return builder.toString();
	}
}
