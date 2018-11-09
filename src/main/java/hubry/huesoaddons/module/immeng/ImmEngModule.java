package hubry.huesoaddons.module.immeng;

import hubry.huesoaddons.HuesoAddons;
import hubry.huesoaddons.IModule;
import hubry.huesoaddons.common.util.HuesoHooks;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class ImmEngModule implements IModule {
	@Override
	public void postInit() {
		HuesoHooks.addRecipes(
				new BasicBlastFurnaceRecipe()//,
				//                new BasicArcFurnaceRecipe()
		);
	}

	/**
	 * Blu's recipe inputs made me unreasonably annoyed.
	 *
	 * @param ingredient Object to templateify
	 * @return A simple {{Gc}}.
	 */
	static String outputObject(Object ingredient) {
		if (ingredient instanceof ItemStack) {
			return Utils.outputItem((ItemStack) ingredient);
		} else if (ingredient instanceof String) {
			return "{{O|" + ingredient + "}}";
		} else if (ingredient instanceof Collection) {
			ArrayList<ItemStack> list = new ArrayList<>();
			for (Object o : ((Collection) ingredient)) {
				if (o instanceof ItemStack) {
					list.add((ItemStack) o);
				}
			}
			String output = Utils.outputOreDictionaryEntry(list.toArray(new ItemStack[0]));
			if (output != null) {
				return output;
			} else {
				return list.stream().map(Utils::outputItem).collect(Collectors.joining());
			}
		}
		HuesoAddons.LOGGER.warn("Unknown input type for input: {}", ingredient);
		return "?";
	}
}
