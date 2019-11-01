package hubry.huesoaddons.module.botania;

import hubry.huesoaddons.common.recipe.BasicRecipe;
import hubry.huesoaddons.common.recipe.ParamList;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePetals;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class PetalApothecaryRecipe extends BasicRecipe<RecipePetals> {

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
			if (BotaniaModule.isOutput(recipe.getOutput(), stack)) {
				list.add(recipe);
			}
		}
		return list;
	}

	@Override
	protected ParamList getParameters(RecipePetals recipe) {
		ParamList list = new ParamList();
		List<Object> inputs = recipe.getInputs();
		BotaniaModule.addInputs(list, inputs);
		return list.output("O", recipe.getOutput());
	}
}
