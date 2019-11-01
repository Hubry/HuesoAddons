package hubry.huesoaddons.module.botania;

import hubry.huesoaddons.IModule;
import hubry.huesoaddons.common.recipe.ParamList;
import hubry.huesoaddons.common.util.HuesoHooks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.List;

public class BotaniaModule implements IModule {
	private static Item specialFlower = ForgeRegistries.ITEMS.getValue(new ResourceLocation("botania", "specialflower"));

	@Override
	public void postInit() {
		HuesoHooks.addRecipes(
				new BreweryRecipe(),
				new ManaInfusionRecipe(),
				new PetalApothecaryRecipe(),
				new RunicAltarRecipe()
		);
	}

	static void addInputs(ParamList list, List<Object> inputs) {
		for (int i = 0; i < inputs.size(); i++) {
			Object input = inputs.get(i);

			if (input instanceof ItemStack) {
				list.stack("I" + (i + 1), (ItemStack) input);
			} else if (input instanceof String) {
				list.ore("I" + (i + 1), (String) input);
			}
		}
	}

	static boolean isOutput(ItemStack recipeOutput, ItemStack tested) {
		if (tested.getItem() == specialFlower) {
			//noinspection ConstantConditions
			return recipeOutput.getItem() == specialFlower
					&& recipeOutput.hasTagCompound() && tested.hasTagCompound()
					&& recipeOutput.getTagCompound().getString("type").equals(tested.getTagCompound().getString("type"));
		}
		return recipeOutput.isItemEqual(tested);
	}
}
