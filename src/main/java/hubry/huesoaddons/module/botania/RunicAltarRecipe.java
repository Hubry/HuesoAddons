package hubry.huesoaddons.module.botania;

import hubry.huesoaddons.common.recipe.ParamList;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.api.recipe.RecipeRuneAltar;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class RunicAltarRecipe extends PetalApothecaryRecipe {
	RunicAltarRecipe() {
		super("Runic Altar");
	}

	@Nonnull
	@Override
	public List<? extends RecipeRuneAltar> gatherRecipes(ItemStack stack) {
		List<RecipeRuneAltar> list = new ArrayList<>();
		for (RecipeRuneAltar recipe : BotaniaAPI.runeAltarRecipes) {
			if (stack.isItemEqual(recipe.getOutput())) {
				list.add(recipe);
			}
		}
		return list;
	}

	@Override
	protected ParamList getParameters(RecipePetals recipe) {
		return super.getParameters(recipe).add("mana", "{{Mana|" + ((RecipeRuneAltar) recipe).getManaUsage() + "}}");
	}
}
