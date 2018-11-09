package hubry.huesoaddons.common.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreIngredient;
import xbony2.huesodewiki.Utils;

import java.util.ArrayList;

/**
 * Builder class for template parameters.
 */
public class ParamList {
	private final ArrayList<Param> params = new ArrayList<>();
	private boolean skipNewlines;

	/**
	 * Adds a param for output items, formatting with Gc for use on the page of the item used.
	 */
	public ParamList output(String key, ItemStack value) {
		params.add(new ParamPair(key, Utils.outputItemOutput(value)));
		return this;
	}

	/**
	 * Adds a param for a vanilla ingredient, formatting using oredict if the ingredient uses it.
	 */
	public ParamList ingredient(String key, Ingredient value) {
		if (value instanceof OreIngredient)
			params.add(new ParamPair(key, Utils.outputOreDictionaryEntry(value.getMatchingStacks())));
		else
			params.add(new ParamPair(key, Utils.outputIngredient(value)));
		return this;
	}

	/**
	 * Adds a param for the stack, formatting it with Gc
	 */
	public ParamList stack(String key, ItemStack value) {
		params.add(new ParamPair(key, Utils.outputItem(value)));
		return this;
	}

	/**
	 * Adds a param for multiple stacks in the same input.
	 */
	public ParamList stacks(String key, ItemStack[] value) {
		StringBuilder builder = new StringBuilder();
		for (ItemStack stack : value) {
			builder.append(Utils.outputItem(stack));
		}
		params.add(new ParamPair(key, builder.toString()));
		return this;
	}

	/**
	 * Adds a param for the oredict entry represented by the passed stack array.
	 */
	public ParamList ore(String key, ItemStack[] value) {
		params.add(new ParamPair(key, Utils.outputOreDictionaryEntry(value)));
		return this;
	}

	/**
	 * Adds an oredict param for the passed oredict name.
	 */
	public ParamList ore(String key, String value) {
		params.add(new ParamPair(key, "{{O|" + value + "}}"));
		return this;
	}

	/**
	 * Adds a param with the passed string used directly as the value.
	 */
	public ParamList add(String key, String value) {
		params.add(new ParamPair(key, value));
		return this;
	}

	/**
	 * Adds an unnamed parameter with the passed value as the name.
	 */
	public ParamList add(String param) {
		params.add(new ParamSingle(param));
		return this;
	}

	public ParamList add(Param param) {
		params.add(param);
		return this;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Param param : params) {
			builder.append(param);
			if (!skipNewlines) {
				builder.append('\n');
			}
		}
		return builder.toString();
	}

	public void skipNewlines(boolean skip) {
		this.skipNewlines = skip;
	}

	/** Marker interface for parameters */
	private interface Param {
	}

	public static class ParamSingle implements Param {
		private final String name;

		public ParamSingle(String name) {
			this.name = name;
		}

		public String getKey() {
			return name;
		}

		@Override
		public String toString() {
			return '|' + name;
		}

	}

	public static class ParamPair implements Param {
		private final String key;
		private final String value;

		public ParamPair(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return "|" + key + '=' + value;
		}
	}
}
