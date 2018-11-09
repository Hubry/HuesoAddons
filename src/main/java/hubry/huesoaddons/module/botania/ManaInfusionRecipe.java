package hubry.huesoaddons.module.botania;

import hubry.huesoaddons.common.recipe.BasicRecipe;
import hubry.huesoaddons.common.recipe.ParamList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeManaInfusion;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ManaInfusionRecipe extends BasicRecipe<RecipeManaInfusion> {
	ManaInfusionRecipe() {
		super("Mana Infusion");
	}

	@Nonnull
	@Override
	public List<? extends RecipeManaInfusion> gatherRecipes(ItemStack stack) {
		List<RecipeManaInfusion> list = new ArrayList<>();
		for (RecipeManaInfusion recipe : BotaniaAPI.manaInfusionRecipes) {
			if (recipe.getOutput().isItemEqual(stack)) {
				list.add(recipe);
			}
		}
		return list;
	}

	@Override
	protected ParamList getParameters(RecipeManaInfusion recipe) {
		ParamList list = new ParamList();
		Object input = recipe.getInput();
		if (input instanceof ItemStack) {
			list.stack("I", (ItemStack) input);
		} else if (input instanceof String) {
			list.ore("I", (String) input);
		}
		list.output("O", recipe.getOutput())
				.add("mana", "{{Mana|" + recipe.getManaToConsume() + "}}");
		if (recipe.getCatalyst() != null) {
			IBlockState state = recipe.getCatalyst();
			list.stack("C", new ItemStack(state.getBlock(), 1, state.getBlock().damageDropped(state)));
		}
		return list;
	}
}
