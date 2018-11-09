package hubry.huesoaddons.module.botania;

import hubry.huesoaddons.common.recipe.BasicRecipe;
import hubry.huesoaddons.common.recipe.ParamList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePetals;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class PetalApothecaryRecipe extends BasicRecipe<RecipePetals> {
	private static Item specialFlower = ForgeRegistries.ITEMS.getValue(new ResourceLocation("botania", "specialflower"));

	PetalApothecaryRecipe() {
		super("Petal Apothecary");
	}

	PetalApothecaryRecipe(String name) {
		super(name);
	}

	@Nonnull
	@Override
	public List<? extends RecipePetals> gatherRecipes(ItemStack stack) {
		List<RecipePetals> list = new ArrayList<>();
		for (RecipePetals recipe : BotaniaAPI.petalRecipes) {
			if (isOutput(recipe.getOutput(), stack)) {
				list.add(recipe);
			}
		}
		return list;
	}

	private static boolean isOutput(ItemStack recipeOutput, ItemStack tested) {
		if (tested.getItem() == specialFlower) {
			//noinspection ConstantConditions
			return recipeOutput.getItem() == specialFlower
					&& recipeOutput.hasTagCompound() && tested.hasTagCompound()
					&& recipeOutput.getTagCompound().getString("type").equals(tested.getTagCompound().getString("type"));
		}
		return recipeOutput.isItemEqual(tested);
	}

	@Override
	protected ParamList getParameters(RecipePetals recipe) {
		ParamList list = new ParamList();
		List<Object> inputs = recipe.getInputs();
		BotaniaModule.addInputs(list, inputs);
		return list.output("O", recipe.getOutput());
	}
}
