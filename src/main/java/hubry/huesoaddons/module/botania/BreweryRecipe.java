package hubry.huesoaddons.module.botania;

import hubry.huesoaddons.common.recipe.BasicRecipe;
import hubry.huesoaddons.common.recipe.ParamList;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.brew.IBrewContainer;
import vazkii.botania.api.brew.IBrewItem;
import vazkii.botania.api.recipe.RecipeBrew;
import vazkii.botania.common.item.ModItems;
import xbony2.huesodewiki.Utils;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BreweryRecipe extends BasicRecipe<RecipeBrew> { //todo everything
	private static final ItemStack[] brewContainers = {
			new ItemStack(ModItems.vial),
			new ItemStack(ModItems.vial, 1, 1),
			new ItemStack(ModItems.incenseStick),
			new ItemStack(ModItems.bloodPendant)
	};

	BreweryRecipe() {
		super("Botanical Brewery");
	}

	@Nonnull
	@Override
	public List<? extends RecipeBrew> gatherRecipes(ItemStack stack) {
		if (stack.getItem() instanceof IBrewItem) {
			Brew brew = ((IBrewItem) stack.getItem()).getBrew(stack);
			return BotaniaAPI.brewRecipes.stream().filter(recipe -> recipe.getBrew().equals(brew)).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Override
	protected ParamList getParameters(RecipeBrew recipe) {
		Brew brew = recipe.getBrew();
		ParamList list = new ParamList();
		StringBuilder inputs = new StringBuilder(), outputs = new StringBuilder(), mana = new StringBuilder();

		for (ItemStack container : brewContainers) {
			ItemStack stack = ((IBrewContainer) container.getItem()).getItemForBrew(brew, container);
			if (!stack.isEmpty()) {
				inputs.append(Utils.outputItem(container));
				outputs.append(Utils.outputItemOutput(stack));
				mana.append("{{Mana|").append(((IBrewContainer) container.getItem()).getManaCost(brew, container)).append("}}");
			}
		}
		list.add("C", inputs.toString());
		BotaniaModule.addInputs(list, recipe.getInputs());
		return list.add("O", outputs.toString()).add("mana", mana.toString());
	}
}
